package ClusterClique;

public class PrepareCellTask implements Runnable  {

	@Override
	public void run() {
		FieldSet fs = null;
		while ((fs = CellCoordinator.GetInstance().FieldSetQueue.poll()) != null) {
			Facet rstFacet = fs.SetupFacet();
			ClusterCoordinator.GetInstance().FacetQueue.offer(rstFacet);
		}
		
	}

}
