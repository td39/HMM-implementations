package testBed;

public class Sample {
	//GSON-Dan
	//// So this is just a first example where each sample is a class for transformation to a single JSON object
	//// A good question is whether replicating field information (in the JSON objects) all the time is really worth it
	//// versus a CSV file. Given that we might want to intersperse other information/samples/messages in with the samples
	//// JSON might still be a good idea 
	
	//// Ideally there will be a base Sample class and other samples will extend/overload it
	public int [] data;
	public double [] likelihoods;
	public double cost;
	
	
	public Sample(int size){
		data = new int[size];
		likelihoods = new double[size];
		cost = 0;
	}
	
	//GSON-Dan
	//// Might want to have the setters return 'this' in order to chain them easily
	//getters and setters though I'm lazy and just set public rather than having getters yet
	
	public int [] setData(int [] dataInput){
		for (int i=0;i<dataInput.length;i++){
			//GSON-Dan
			//// Infinity checking is necessary; this is clumsy but it should be done somewhere
			if (dataInput[i]!=Double.POSITIVE_INFINITY){
				data[i]=dataInput[i];
			}
			else
			{
				data[i]=0;
			}
		}
		return data;
	}
	
	public double [] setLikelihoods(double [] likelihoodsInput){
		for (int i=0;i<likelihoodsInput.length;i++){
			if (likelihoodsInput[i]!=Double.POSITIVE_INFINITY){
				likelihoods[i]=likelihoodsInput[i];
			}
			else
			{
				likelihoods[i]=0;
			}
		}
		return likelihoods;
	}
	
	public double setCost(double costInput){
		if (costInput!=Double.POSITIVE_INFINITY){
			cost=costInput;
		}
		else
		{
			cost=0;
		}
	
	return cost;
}

}
