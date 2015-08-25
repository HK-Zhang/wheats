package KaggleTube;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

public class FlatTableConvertor {
	private Set<String> fieldSets = new HashSet();
	private String[] tubeFields;
	private Map<String, TubeAssemble> assistMatrix = new HashMap<String, TubeAssemble>();

	public void Run() {
		prepareData(true, true, "NA");
		prepareTubeData("");
		outputToFile();

	}
	
	public Map<String, TubeAssemble> GetAssistMatrix() {
		return assistMatrix;
	}

	private void prepareData(boolean individualValCol, boolean hasTitle,
			String blankStr) {
		try {
			BufferedReader inByLine = new BufferedReader(new FileReader(
					TubeConfig.BILLMATERIALFILE_STRING));
			System.out.println("open data file: "
					+ TubeConfig.BILLMATERIALFILE_STRING + " opened");
			System.out.println("Read begin");
			String piontStr = null;

			if (hasTitle)
				inByLine.readLine();

			int colCnt = 0;

			String[] pointsStr;
			while ((piontStr = inByLine.readLine()) != null) {
				pointsStr = piontStr.split(TubeConfig.CSVDELIMITER_STRING);
				colCnt = 0;
				for (int i = 1; i < pointsStr.length; i++) {
					pointsStr[i] = pointsStr[i].trim();
					if (!pointsStr[i].equals(blankStr)
							&& !pointsStr[i].isEmpty() && i % 2 == 1) {
						fieldSets.add(pointsStr[i]);
						colCnt += 1;
					}
				}

				if (colCnt > 0) {
					RowData r = new RowData(colCnt);

					int j = 0;
					for (int i = 1; i < pointsStr.length; i++) {
						if (!pointsStr[i].equals(blankStr)
								&& !pointsStr[i].isEmpty()) {

							if (i % 2 == 1)
								r.SetColName(j, pointsStr[i]);
							else {
								r.SetColVal(j, pointsStr[i]);
								j++;
								if (j == colCnt)
									break;
							}
						}
					}
					assistMatrix.put(pointsStr[0], new TubeAssemble(r));
				} else {
					assistMatrix.put(pointsStr[0], new TubeAssemble(
							new RowData(0)));
				}
			}

			inByLine.close();
			System.out.println("read completed: prepareData() finished ");

		} catch (Exception e) {
			System.err.println("Err in prepareData() ::" + e);
			e.printStackTrace();
		}

	}

	private void prepareTubeData(String blankStr) {
		try {
			BufferedReader inByLine = new BufferedReader(new FileReader(
					TubeConfig.TUBEFILE_STRING));
			System.out.println("open data file: " + TubeConfig.TUBEFILE_STRING
					+ " opened");
			System.out.println("Read begin");
			String piontStr = null;

			piontStr = inByLine.readLine();
			String[] CStr = piontStr.split(TubeConfig.CSVDELIMITER_STRING);
			int length = CStr.length;
			String[] colStr = new String[length - 1];
			for (int i = 0; i < length - 1; i++) {
				colStr[i] = CStr[i + 1];
			}
			tubeFields = colStr;

			String[] pointsStr;

			while ((piontStr = inByLine.readLine()) != null) {
				pointsStr = piontStr.split(TubeConfig.CSVDELIMITER_STRING);
				RowData r = new RowData(length - 1, colStr);

				for (int i = 1; i < pointsStr.length; i++) {
					pointsStr[i] = pointsStr[i].trim();
					r.SetColVal(i - 1, pointsStr[i]);
				}

				TubeAssemble t = assistMatrix.get(pointsStr[0]);
				if (t == null) {
					assistMatrix.put(pointsStr[0], new TubeAssemble(r, null));
				} else {
					t.AddTube(r);
				}
			}

			inByLine.close();
			System.out.println("read completed: prepareTubeData() finished ");

		} catch (Exception e) {
			System.err.println("Err in prepareTubeData() ::" + e);
			e.printStackTrace();
		}
	}

	private void outputToFile() {
		String[] vals = new String[fieldSets.size() + 1];
		String[] tubeVals = new String[tubeFields.length];
		List<String> tmpList = new ArrayList<String>();
		tmpList.addAll(fieldSets);

		System.out.println("output started: outputToFile() start ");

		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(TubeConfig.OUTPUTFILE_STRING)));

			vals[0] = "Item";
			for (int i = 0; i < tmpList.size(); i++) {
				vals[i + 1] = tmpList.get(i);
			}

			out.println(StringUtils.join(vals, ",") + ","
					+ StringUtils.join(tubeFields, ","));

			Iterator iter = assistMatrix.entrySet().iterator();
			RowData val = null;
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();

				for (int i = 0; i <= fieldSets.size(); i++) {
					vals[i] = "";
				}
				vals[0] = entry.getKey().toString();
				
				TubeAssemble tlis = (TubeAssemble) entry.getValue();
				val = tlis.GetBillMaterial();

				if (val != null) {
					for (int j = 0; j < val.GetSize(); j++) {
						vals[tmpList.indexOf(val.GetColName(j)) + 1] = val
								.GetColVal(j);
					}
				}

				for (int i = 0; i < tubeFields.length; i++) {
					tubeVals[i] = "";
				}

				val = tlis.GetTube();

				if (val != null) {
					for (int j = 0; j < val.GetSize(); j++) {
						tubeVals[j] = val.GetColVal(j);
					}
				}

				out.println(StringUtils.join(vals, ",") + ","
						+ StringUtils.join(tubeVals, ","));

			}
			out.close();
			System.out.println("output completed: outputToFile() finished ");

		} catch (Exception e) {
			System.err.println("Err in outputToFile() ::" + e);
		}

	}

}
