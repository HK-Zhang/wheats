package KaggleTube;

import java.util.HashMap;
import java.util.Map;

public class TubeAssemble {
	private Map<String, RowData> assembleList = new HashMap<String, RowData>();
	
	public TubeAssemble(RowData rBillMaterial){
		AddBillMaterial(rBillMaterial);
	}
	
	public TubeAssemble(RowData rTube,RowData rBillMaterial){
		AddTube(rTube);
	}
	
	public void AddBillMaterial(RowData r){
		assembleList.put(TubeConfig.BILLMATERIAL_STRING, r);
	}
	
	public void AddTube(RowData r){
		assembleList.put(TubeConfig.TUBE_STRING, r);
	}
	
	public RowData GetBillMaterial() {
		return assembleList.get(TubeConfig.BILLMATERIAL_STRING);
	}
	
	public RowData GetTube() {
		return assembleList.get(TubeConfig.TUBE_STRING);
	}
}
