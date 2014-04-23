package testBed;
import com.analog.lyric.dimple.factorfunctions.core.FactorFunction;



public class ObservationFactorFunction extends FactorFunction {
	@Override
	public double eval(Object ... args) {
		String state = (String)args[0]; String observation = (String)args[1];
		if (state.equals("sunny"))
			if (observation.equals("walk"))
				return 0.7;
			else if (observation.equals("book"))
				return 0.1; 
			else // cook
				return 0.2; 
		else
			if (observation.equals("walk")) 
				return 0.2;
			else if (observation.equals("book")) 
				return 0.4;
			else // cook 
				return 0.4;
	} }