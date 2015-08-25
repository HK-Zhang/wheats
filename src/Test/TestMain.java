package Test;
import ClusterClique.*;
import KaggleTube.TrainingProcessor;

public class TestMain {
	public static void main(String[] args){
		//Test Case 1
/*		ClusterByClique cbc=new ClusterByClique();
		cbc.Run();*/
		
		TrainingProcessor dc= new TrainingProcessor();
		dc.Run();
		
		//Test Case 2
		//ThreadDemo.testMyThreadDemo();
	}
}
