package KaggleTube;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DeviationCalculator {
	private Map<String, TubeAssemble> assistMatrix;
	private Map<String, List<PriceDeviation>> compMatrix = new HashMap<String, List<PriceDeviation>>();
	private static final int BASE_INT = 10000;

	public void Run(Map<String, TubeAssemble> dataMatrix) {
		assistMatrix = dataMatrix;
		CalculateDeviation();
		
		Iterator iter = compMatrix.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			System.out.println(entry.getKey().toString());
		}
	}

	public void CalculateDeviation() {

		List<String> keys = new ArrayList<String>();
		keys.addAll(assistMatrix.keySet());

		int cmpRst = 0;
		for (int i = 0; i < keys.size(); i++) {
			TubeAssemble fAssemble = assistMatrix.get(keys.get(i));

			for (int j = i + 1; j < keys.size(); j++) {
				TubeAssemble tAssemble = assistMatrix.get(keys.get(j));
				cmpRst = CompareRowData(fAssemble.GetTube(),
						tAssemble.GetTube());
				
/*				if(keys.get(j).toString().equals("TA-00004") && keys.get(i).toString().equals("TA-00002")){
					String abvString="";
					System.out.println(abvString);
					
				}
				
				if(keys.get(j).toString().equals("TA-00002") && keys.get(i).toString().equals("TA-00004")){
					String abvString="";
					System.out.println(abvString);
					
				}*/

				if (cmpRst > 1 && cmpRst < BASE_INT)
					continue;
				else if (cmpRst >= BASE_INT) {
					cmpRst = cmpRst - BASE_INT;
				}

				int std = 0;
				if (cmpRst == 0)
					std = 1;
				else {
					std = 0;
				}

				int cmpBillRst = CompareRowData(fAssemble.GetBillMaterial(),
						tAssemble.GetBillMaterial(), std);
	
				if(cmpBillRst==0 && cmpRst>0){
					//System.out.println("Indentified Comp:"+keys.get(i).toString()+"/"+keys.get(j).toString()+fAssemble.GetTube().GetColName(cmpRst));
					PriceDeviation pDiv =new PriceDeviation(keys.get(i).toString(),keys.get(j).toString(),0.0);
					if(compMatrix.containsKey(fAssemble.GetTube().GetColName(cmpRst))){
						compMatrix.get(fAssemble.GetTube().GetColName(cmpRst)).add(pDiv);
					}
					else{
						List<PriceDeviation> lst = new ArrayList<PriceDeviation>();
						lst.add(pDiv);
						compMatrix.put(fAssemble.GetTube().GetColName(cmpRst),lst);
					}
				}
				
				if(cmpRst==0 && cmpBillRst>BASE_INT){
					cmpBillRst=cmpBillRst-BASE_INT;
					//System.out.println("Indentified Comp:"+keys.get(i).toString()+"/"+keys.get(j).toString()+fAssemble.GetBillMaterial().GetColName(cmpBillRst));
					PriceDeviation pDiv =new PriceDeviation(keys.get(i).toString(),keys.get(j).toString(),0.0);
					if(compMatrix.containsKey(fAssemble.GetBillMaterial().GetColName(cmpBillRst))){
						compMatrix.get(fAssemble.GetBillMaterial().GetColName(cmpBillRst)).add(pDiv);
					}
					else{
						List<PriceDeviation> lst = new ArrayList<PriceDeviation>();
						lst.add(pDiv);
						compMatrix.put(fAssemble.GetBillMaterial().GetColName(cmpBillRst),lst);
					}
				}
				

			}
		}

	}

	public int CompareRowData(RowData fRow, RowData tRow, int std) {
		if (Math.abs(fRow.GetSize() - tRow.GetSize()) > std)
			return 2;

		if (fRow.GetSize() == 0 && tRow.GetSize() == 0)
			return std;

		RowData extRow = fRow;
		RowData intRow = tRow;

		if (fRow.GetSize() < tRow.GetSize()) {
			extRow = tRow;
			intRow = fRow;
		}
		
		String[] fStrA = extRow.GetDataCols();
		String[] tStrA = intRow.GetDataCols();
		
		int cnt = 0;
		int tindx = 0;
		int tjndx = 0;
		for(int i =0; i<extRow.GetSize();i++){
			for(int j =0; i<intRow.GetSize();i++){
				if(fStrA[i].equals(tStrA[j])){
					if(!extRow.GetColVal(i).equals(intRow.GetColVal(j))){
						cnt++;
						tindx = i;
						tjndx = j;
						if (cnt > std) {
							return cnt;
						}
					}
				}
			}
		}
		
		if (fRow.GetSize() < tRow.GetSize()) {
			return BASE_INT +tjndx;
		}
		else{
			return BASE_INT +tindx;
		}

	}

	public int CompareRowData(RowData fRow, RowData tRow) {
		if (fRow == null) {
			if (tRow == null)
				return 0;
			else {
				return tRow.GetSize();
			}
		} else {
			if (tRow == null) {
				return fRow.GetSize();
			} else {
				int j = 0;
				int tindx = 0;
				String[] fStrA = fRow.GetDataVals();
				String[] tStrA = tRow.GetDataVals();
				for (int i = 0; i < fStrA.length; i++) {
					if (!fStrA[i].equals(tStrA[i])) {
						j++;
						tindx = i;
						if (j > 1) {
							return j;
						}
					}
				}

				if (j == 1) {
					return BASE_INT + tindx;
				}

				return j;
			}
		}

	}

}
