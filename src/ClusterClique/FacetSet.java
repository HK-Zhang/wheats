package ClusterClique;

public class FacetSet {
	private Facet sourceData;
	private Facet targetData;
	
	public FacetSet(Facet src, Facet tgt){
		sourceData=src;
		targetData=tgt;
	}
	
	public Facet GetCommonFacet(){
		return sourceData.MergeToNewFacet(targetData);
	}
}
