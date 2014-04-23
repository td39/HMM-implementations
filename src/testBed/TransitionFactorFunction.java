package testBed;
import com.analog.lyric.dimple.factorfunctions.core.FactorFunction;


public class TransitionFactorFunction extends FactorFunction {
	@Override
	public double eval(Object ... args) {
		String state1 = (String)args[0]; String state2 = (String)args[1];
		if (state1.equals("sunny")) {
			if (state2.equals("sunny")) {
				return 0.8; }
			else
			{	
				return 0.2;
			}
		}
		else
		{
			return 0.5;
		}
	} 
}
