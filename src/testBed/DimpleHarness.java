package testBed;


import java.util.*;

import com.analog.lyric.dimple.model.core.FactorGraph;
import com.analog.lyric.dimple.model.domains.DiscreteDomain;
import com.analog.lyric.dimple.model.variables.Discrete;


public class DimpleHarness {
	List<String> who = Arrays.asList("Mr. Green", "Ms. Scarlett", "Mr. White");
	List<String> what = Arrays.asList("candlestick", "revolver", "knife");
	List<String> where = Arrays.asList("conservatory", "library", "kitchen");
	
	public static void main(String [] args)
	{
		// Create a new factor graph
		FactorGraph HMM = new FactorGraph();
		
		// Create a categorical domain and instantiate a Discrete variable on it
		DiscreteDomain domain = DiscreteDomain.create("sunny", "rainy"); 
		Discrete MondayWeather = new Discrete(domain);
		
		
		
	}
}