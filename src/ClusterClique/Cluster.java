package ClusterClique;


public class Cluster {
	private int clusterNo ;
	private int numberCells;
	private int density;
	
	public Cluster(int i){
		clusterNo = i;
		density = 0;
		numberCells = 0;
		
	}
	
	public void addCell(int numberPoints){
		density = (numberCells*density + numberPoints)/(numberCells + 1);
		numberCells +=1;
	}

	public void setclusterNo(int i){
		clusterNo = i;
	}
	public void setNumberCells(int i){
		numberCells = i;
	}
	public void setDensity(int i){
		density = i;
	}
	public int getClusterNo(){
		return clusterNo;
	}
	public int getNumberCells(){
		return numberCells;
	}
	public int getDensity(){
		return density;
	}
}
