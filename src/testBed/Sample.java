package testBed;

public class Sample {
	
	public int [] data;
	public double [] likelihoods;
	public double cost;
	
	
	public Sample(int size){
		data = new int[size];
		likelihoods = new double[size];
		cost = 0;
	}
	
	
	//getters and setters
	
	public int [] setData(int [] dataInput){
		for (int i=0;i<dataInput.length;i++){
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
