package KaggleTube;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TrainingProcessor {
	private Set<String> tubeSets = new HashSet<String>();
	private String[][] trainingSet = new String[30213][8];
	private Map<String, TubeAssemble> assistMatrix;
	
	public void Run() {
		PrepareData(true);
		FlatTableConvertor fc = new FlatTableConvertor();
		fc.Run();
		assistMatrix = fc.GetAssistMatrix();
		assistMatrix.keySet().retainAll(tubeSets);
		
		DeviationCalculator dcCalculator =new DeviationCalculator();
		dcCalculator.Run(assistMatrix);

	}
	
	public void PrepareData(boolean hasTitle) {
		try {
			BufferedReader inByLine = new BufferedReader(new FileReader(
					TubeConfig.TRAININGEFILE_STRING));
			System.out.println("open data file: "
					+ TubeConfig.TRAININGEFILE_STRING + " opened");
			System.out.println("Read begin");
			
			if (hasTitle)
				inByLine.readLine();
			
			String piontStr = null;
			int cnt = 0;
			String[] pointsStr;
			while ((piontStr = inByLine.readLine()) != null) {
				pointsStr = piontStr.split(TubeConfig.CSVDELIMITER_STRING);
				trainingSet[cnt] = pointsStr;
				tubeSets.add(pointsStr[0]);
				cnt=cnt+1;
				}
			
			inByLine.close();
			System.out.println("read completed: prepareData() finished ");
			
		} catch (Exception e) {
			System.err.println("Err in prepareData() ::" + e);
			e.printStackTrace();
		}
		
	}

}
