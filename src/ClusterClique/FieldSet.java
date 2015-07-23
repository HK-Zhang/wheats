package ClusterClique;
public class FieldSet {
	private String sourceFieldName ="";
	private String toFieldName ="";
	private double[][] rawData;
	private int sampleSize;

	
	public FieldSet(String srcName,String tgtName,int sSize){
		sourceFieldName=srcName;
		toFieldName=tgtName;
		rawData=new double[2][sSize];
		sampleSize=sSize;
	}
	
	public String GetSourceFieldName(){
		return sourceFieldName;
	}
	
	public String GetToFieldName(){
		return toFieldName;
	}
	
	public int GetSampleSize(){
		return sampleSize;
	}
	
	public void SetRawData(double src,double tgt,int rowIndex){
		rawData[0][rowIndex]=src;
		rawData[1][rowIndex]=tgt;
	}
	
	public double[][] GetRawData() {
		return rawData;
		
	}
	
	
	
	public Facet SetupFacet()
	{
		Facet rstFacet=new Facet();
		
		Cell[][] cells;
		double xmin = 0.0;
		double xmax = 30.0;
		double ymin = 0.0;
		double ymax = 30.0;
		
		int cellRow = ClusterConfig.cellRow;
		int cellColumn = ClusterConfig.cellColumn;
		int cellx;
		int celly;
		
		cells = new Cell[cellRow + 1][cellColumn + 1];
		try {
			
			int i, j;
			for (i = 1; i <= ClusterConfig.cellRow; i++) {
				for (j = 1; j <= ClusterConfig.cellColumn; j++) {
					cells[i][j] = new Cell();
				}
			}

			xmin = xmax = rawData[0][0];
			ymin = ymax = rawData[1][0];

			for (i = 1; i < sampleSize; i++) {

				if (xmin > rawData[0][i])
					xmin = rawData[0][i];

				if (xmax < rawData[0][i])
					xmax = rawData[0][i];
				
				if (ymin > rawData[1][i])
					ymin = rawData[1][i];

				if (ymax < rawData[1][i])
					ymax = rawData[1][i];
			}
			
			
			double intervalx = (xmax - xmin) / ClusterConfig.cellColumn;
			double intervaly = (ymax - ymin) / ClusterConfig.cellRow;
			
			//Adjust min, max, interval to overcome locating point at cells[0][*] or cells[*][0]
			xmin = xmin-0.1*intervalx;
			ymin = ymin-0.1*intervaly;
			
			xmax = xmax+0.1*intervalx;
			ymax = ymax+0.1*intervaly;
			
			intervalx = (xmax - xmin) / ClusterConfig.cellColumn;
			intervaly = (ymax - ymin) / ClusterConfig.cellRow;
			

			
			for(i=0; i<sampleSize;i++){
				celly = (int) Math.ceil((rawData[0][i] - xmin) / intervalx);
				cellx = (int) Math.ceil((rawData[1][i] - ymin) / intervaly);
			
				cells[celly][cellx].addSamples(i+1);
			}
			
			for (i = 1; i <= ClusterConfig.cellRow; i++) {
				for (j = 1; j <= ClusterConfig.cellColumn; j++) {
					if (cells[i][j].getNumberPoints() >= ClusterConfig.densityThreshold){
						cells[i][j].setQualified(1);
						rstFacet.AddCell(i+"", j+"", cells[i][j]);
						System.out.println(rstFacet.hashCode()+":"+i+"-"+j+":"+cells[i][j].getNumberPoints());
					}
				}
			}
			

			
		} catch (Exception e) {
			System.err.println("Err in SetupFacet ::" + e);
			e.printStackTrace();
		}
		
		
		return rstFacet;
		
		
	}
	

}
