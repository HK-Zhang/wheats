package ClusterClique;
import java.util.concurrent.LinkedBlockingQueue;

public class ClusterCoordinator {

	private static ClusterCoordinator instance = new ClusterCoordinator();
	public LinkedBlockingQueue<FacetSet> FacetSetQueue=new LinkedBlockingQueue<FacetSet>();
	public LinkedBlockingQueue<Facet> FacetQueue=new LinkedBlockingQueue<Facet>();
	
	public static ClusterCoordinator GetInstance()
	{
		return instance;
	}
	
	public boolean BuildFacetSet() {
		if(FacetQueue.size()==1)
			return true;
		
		Facet source=FacetQueue.poll();
		
		Facet toFacet=null;
		
		while((toFacet=FacetQueue.poll())!=null){
			FacetSet fs=new FacetSet(source, toFacet);
			FacetSetQueue.offer(fs);
		}
		
		FacetQueue.clear();
		
		return false;
		
	}
	
	
}
