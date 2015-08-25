package KaggleTube;

public class RowData {
	private String[] dataCols;
	private String[] dataVals;
	
	public RowData(int size){
		dataCols = new String[size];
		dataVals = new String[size];
	}
	
	public RowData(int size,String[] dataCol){
		dataVals = new String[size];
		dataCols = dataCol;
	}
	
	public String[] GetDataCols(){
		return dataCols;
	}
	
	public String[] GetDataVals(){
		return dataVals;
	}
	
	public int GetSize(){
		return dataCols.length;
	}
	
	public String GetColName(int index){
		return dataCols[index];
	}
	
	public String GetColVal(int index){
		return dataVals[index];
	}
	
	public void SetColName(int index, String val){
		dataCols[index]=val;
	}
	
	public void SetColVal(int index, String val){
		dataVals[index]=val;
	}
	
	
	
}
