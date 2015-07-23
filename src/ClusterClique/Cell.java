package ClusterClique;
import java.util.ArrayList;
import java.util.List;

public class Cell {
	private int qualified;//has cell density reached to threshold
	private int checked;//If cell is checked in final clustering step
	private int clusterNo;//cluster #
	private List<Integer> samples;//list of sample's index

	public Cell(){
		qualified = 0;
		checked = 0;
		clusterNo = 0;
		samples=new ArrayList<Integer> ();
	}

	public void setQualified(int i){
		qualified = i;
	}
	public void setChecked(int i){
		checked = i;
	}
	public void setClusterNo(int i){
		clusterNo = i;
	}
	public void setSamples(List<Integer> smp){
		samples=smp;
	}
	public void addSamples(int i){
		samples.add(i);
	}
	

	public int getNumberPoints(){
		return samples.size();
	}
	public int getChecked(){
		return checked;
	}
	public int getQualified(){
		return qualified;
	}
	public int getClusterNo(){
		return clusterNo;
	}
	public List<Integer> getSamples()
	{
		return samples;
	}
	
	
	public List<Integer> GetCommonSamples(List<Integer> tgt){
		List<Integer> result=new ArrayList<Integer>();
		
		result.addAll(samples);
		result.retainAll(tgt);

		return result;
	}
	
		
	
	
}
