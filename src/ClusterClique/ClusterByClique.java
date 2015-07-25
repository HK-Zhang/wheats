package ClusterClique;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import Test.ThreadDemo.MyThreadDemo;

import java.io.*;

public class ClusterByClique {
	private int cellRow;
	private int cellColumn;
	private int numberClusters;
	private List<Cluster> clusters;
	private List<FieldSet> fieldSets;
	private Facet fc;
	private int[][] CellIndx;
	private HashMap<String, Integer> assistMatrix = new HashMap<String, Integer>();
	private List<Cell> fcCells;
	private int d1;
	private int d2;

	public void Run() {
		try {

			initialize();
			prepareData(false);
			prepareCell();
			boolean flag = ClusterCoordinator.GetInstance().BuildFacetSet();

			while (flag == false) {
				MergeCell();

				flag = ClusterCoordinator.GetInstance().BuildFacetSet();
			}

			if (flag) {
				fc = ClusterCoordinator.GetInstance().FacetQueue.poll();

				if (fc.Size() == 0) {
					System.out.println("No Cluster found");
				} else {
					cluster();
					printResult();
				}

				System.out.println("Cluster by Clique is over.:)");
			}

		} catch (Exception e) {
			System.err.println("Err in main() ::" + e);
		}
	}

	private void initialize() {
		try {
			cellRow = ClusterConfig.cellRow;
			cellColumn = ClusterConfig.cellColumn;

			clusters = new ArrayList<Cluster>();
			numberClusters = 0;
			clusters.add(new Cluster(numberClusters));

		} catch (Exception e) {
			System.err.println("Err in initialize ::" + e);
		}

	}// initialize() end;

	private void prepareData(boolean hasTitle) {
		try {
			BufferedReader inByLine = new BufferedReader(new FileReader(ClusterConfig.filePath));
			System.out.println("open data file: " + ClusterConfig.filePath + " opened");
			System.out.println("Read begin");
			String piontStr = null;
			fieldSets = new ArrayList<FieldSet>();
			List<String> rows = new ArrayList<String>();
			int index = 0;

			while ((piontStr = inByLine.readLine()) != null) {
				rows.add(piontStr);
			}

			inByLine.close();
			System.out.println("读取结束:Read() finished ");
			if (rows.isEmpty())
				return;

			String[] pointsStr = Pattern.compile("\\s").split(rows.get(0));

			if (hasTitle == true) {
				for (int j = 1; j < pointsStr.length; j++) {
					FieldSet fs = new FieldSet(pointsStr[0], pointsStr[j], rows.size());
					fieldSets.add(fs);
				}
			} else {
				for (int j = 1; j < pointsStr.length; j++) {
					FieldSet fs = new FieldSet("F1", "F" + j, rows.size());
					fs.SetRawData(Double.valueOf(pointsStr[0]), Double.valueOf(pointsStr[j]), 0);
					fieldSets.add(fs);
					index = 1;
				}
			}

			for (int i = 1; i < rows.size(); i++) {
				pointsStr = Pattern.compile("\\s").split(rows.get(i));
				for (int j = 1; j < pointsStr.length; j++) {
					fieldSets.get(j - 1).SetRawData(Double.valueOf(pointsStr[0]), Double.valueOf(pointsStr[j]), index);
				}
				index++;
			}

			for (int i = 0; i < fieldSets.size(); i++) {
				CellCoordinator.GetInstance().FieldSetQueue.offer(fieldSets.get(i));
			}

		} catch (Exception e) {
			System.err.println("Err in read() ::" + e);
		}
	}// read() end

	private void prepareCell() {
		/*FieldSet fs = null;
		while ((fs = CellCoordinator.GetInstance().FieldSetQueue.poll()) != null) {
			Facet rstFacet = fs.SetupFacet();
			ClusterCoordinator.GetInstance().FacetQueue.offer(rstFacet);
		}*/
		
		ThreadPoolExecutor executor = new ThreadPoolExecutor(5,5, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		 for(int i=0; i<5; i++) { 
	            executor.execute(new PrepareCellTask()); 
	        } 
		 
		 executor.shutdown(); 
		 
	        try { 
	            boolean loop = true; 
	            do {    //等待所有任务完成 
	                loop = !executor.awaitTermination(2, TimeUnit.SECONDS); 
	            } while(loop); 
	        } catch (InterruptedException e) { 
	            e.printStackTrace(); 
	        } 
	}

	private void MergeCell() {

		/*FacetSet fs = null;

		while ((fs = ClusterCoordinator.GetInstance().FacetSetQueue.poll()) != null) {
			Facet rstFacet = fs.GetCommonFacet();
			ClusterCoordinator.GetInstance().FacetQueue.offer(rstFacet);
		}*/
		
		ThreadPoolExecutor executor = new ThreadPoolExecutor(5,5, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		 for(int i=0; i<5; i++) { 
	            executor.execute(new MergeCellTask()); 
	        } 
		 
		 executor.shutdown(); 
		 
	        try { 
	            boolean loop = true; 
	            do {    //等待所有任务完成 
	                loop = !executor.awaitTermination(2, TimeUnit.SECONDS); 
	            } while(loop); 
	        } catch (InterruptedException e) { 
	            e.printStackTrace(); 
	        } 

	}

	private void cluster() {

		try {

			d1 = fc.getMasterIndex(0).split("_").length;
			d2 = fc.Size();
			CellIndx = new int[d1 + 1][d2];

			String[] tmp = new String[d1];
			fcCells = fc.getCells();

			for (int i = 0; i < d2; i++) {

				tmp = fc.getMasterIndex(i).split("_");

				for (int j = 0; j < d1; j++) {
					CellIndx[j][i] = Integer.valueOf(tmp[j]);

				}

				CellIndx[d1][i] = Integer.valueOf(fc.getSlaveIndex(i));

				assistMatrix.put(fc.getMasterIndex(i) + "_" + fc.getSlaveIndex(i), i);

			} // End

			// Start to cluster for each cell.
			for (int j = 0; j < d2; j++) {
				if (fcCells.get(j).getClusterNo() == 0 && fcCells.get(j).getChecked() != 1) {
					numberClusters += 1;
					clusters.add(new Cluster(numberClusters));
					retrieve(numberClusters, j);
					//System.out.println("Cluster(): d= " + fc.getMasterIndex(j) + "_" + fc.getSlaveIndex(j) + " ClusterNo = " + ((Cluster) clusters.get(numberClusters)).getClusterNo());
				} // End
			}
		} catch (Exception e) {
			System.err.println("Err in clusterFacet() ::" + e);
		}

	}

	private void retrieve(int k, int j) {
		try {

			int l;
			String s;
			if (fcCells.get(j).getChecked() != 1 && fcCells.get(j).getClusterNo() == 0) {

				// if (fcCells.get(j).getNumberPoints() >= (int) ((Cluster)
				// clusters.get(k)).getDensity() *
				// ClusterConfig.densityRatioThreshold)
				if (true) {
					fcCells.get(j).setChecked(1);
					fcCells.get(j).setClusterNo(k);
					((Cluster) clusters.get(k)).addCell(fcCells.get(j).getNumberPoints());
					System.out.println("Cluster(): d= " + fc.getMasterIndex(j) + "_" + fc.getSlaveIndex(j) + " ClusterNo = " + ((Cluster) clusters.get(numberClusters)).getClusterNo());

					for (int ix = 0; ix <= d1; ix++) {
						s = "";

						if (CellIndx[ix][j] != 1) {
							l = CellIndx[ix][j] - 1;

							for (int jx = 0; jx <= d1; jx++) {

								if (jx != ix) {
									s = s + CellIndx[jx][j] + "_";
								} else {
									s = s + l + "_";
								}

							}

							s = s.substring(0, s.length() - 1);

							Integer m = assistMatrix.get(s);
							if (m != null) {
								retrieve(k, m);
							}

						}

						s = "";
						if (CellIndx[ix][j] != cellColumn) {
							l = CellIndx[ix][j] + 1;

							for (int jx = 0; jx <= d1; jx++) {

								if (jx != ix) {
									s = s + CellIndx[jx][j] + "_";
								} else {
									s = s + l + "_";
								}

							}

							s = s.substring(0, s.length() - 1);

							Integer m = assistMatrix.get(s);
							if (m != null) {
								retrieve(k, m);
							}

						}

					}

				}
			}

		} catch (Exception e) {
			System.err.println("Err in retrieveFacet() ::" + e);
		}
	}// retrieve() end;

	private void printResult() {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(ClusterConfig.outPath)));

			for (int j = 0; j < d2; j++) {
					out.println("Cluster(): d= " + fc.getMasterIndex(j) + "_" + fc.getSlaveIndex(j) + " ClusterNo = " + fc.getCell(j).getClusterNo());
				}
			
			out.close();

		} catch (Exception e) {
			System.err.println("Err in printResult() ::" + e);
		}
	}// printResult() end;

}
