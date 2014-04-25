package testBed;



import com.analog.lyric.dimple.model.core.FactorGraph;
import com.analog.lyric.dimple.model.domains.DiscreteDomain;
import com.analog.lyric.dimple.model.variables.Discrete;


public class DimpleHarnessBeliefPropagation {
	
	public static void main(String [] args)
	{
		FactorGraph HMM = new FactorGraph();

		DiscreteDomain domain = DiscreteDomain.create("sunny", "rainy");

		Discrete MondayWeather = new Discrete(domain);
		Discrete TuesdayWeather = new Discrete(domain);
		Discrete WednesdayWeather = new Discrete(domain);
		Discrete ThursdayWeather = new Discrete(domain);
		Discrete FridayWeather = new Discrete(domain);
		Discrete SaturdayWeather = new Discrete(domain);
		Discrete SundayWeather = new Discrete(domain);

		TransitionFactorFunction trans = new TransitionFactorFunction();
		HMM.addFactor(trans, MondayWeather,TuesdayWeather);
		HMM.addFactor(trans, TuesdayWeather,WednesdayWeather);
		HMM.addFactor(trans, WednesdayWeather,ThursdayWeather);
		HMM.addFactor(trans, ThursdayWeather,FridayWeather);
		HMM.addFactor(trans, FridayWeather,SaturdayWeather);
		HMM.addFactor(trans, FridayWeather,SundayWeather);

		ObservationFactorFunction obs = new ObservationFactorFunction();
		HMM.addFactor(obs,MondayWeather ,"walk");
		HMM.addFactor(obs,TuesdayWeather ,"walk");
		HMM.addFactor(obs,WednesdayWeather ,"cook");
		HMM.addFactor(obs,ThursdayWeather ,"walk");
		HMM.addFactor(obs,FridayWeather ,"cook");
		HMM.addFactor(obs,SaturdayWeather ,"book");
		HMM.addFactor(obs,SundayWeather ,"book");

		MondayWeather.setInput(0.7,0.3);
		HMM.solve();

		Object [] belief = new Object [7];
		// This section grabs beliefs after belief propagation		
		belief[0]= MondayWeather.getBelief();
		belief[1]= TuesdayWeather.getBelief();
		belief[2]= WednesdayWeather.getBelief();
		belief[3]= ThursdayWeather.getBelief();
		belief[4]= FridayWeather.getBelief();
		belief[5]= SaturdayWeather.getBelief();
		belief[6]= SundayWeather.getBelief();

		for (int i = 0; i<belief.length;i++)
		{
			System.out.println(("Chance of rain on day "+(i+1)+" is "+((double []) belief[i])[1]));
		}
	}
}