package ClusterClique;

public class MergeCellTask implements Runnable{

	@Override
	public void run() {
		FacetSet fs = null;

		while ((fs = ClusterCoordinator.GetInstance().FacetSetQueue.poll()) != null) {
			Facet rstFacet = fs.GetCommonFacet();
			ClusterCoordinator.GetInstance().FacetQueue.offer(rstFacet);
		}
		
	}

}
