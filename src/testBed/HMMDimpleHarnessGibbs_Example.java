package testBed;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.analog.lyric.dimple.model.core.FactorGraph;
import com.analog.lyric.dimple.model.domains.DiscreteDomain;
import com.analog.lyric.dimple.model.variables.Discrete;
import com.analog.lyric.dimple.solvers.gibbs.SDiscreteVariable;
import com.analog.lyric.dimple.solvers.gibbs.SFactorGraph;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class HMMDimpleHarnessGibbs_Example {

	public static void main(String [] args) throws IOException
	{
		//GSON-Dan
		//// Just added a straightforward Writer object in a try-with-resources
		try(Writer writer = new OutputStreamWriter(new FileOutputStream("../streamgraph/dimple.json"))){
			
			//GSON-Dan
			//// Instantiate a Gson object
			Gson gson = new GsonBuilder().create();
			//// Instantiate the sample object to store the sample
			//// Currently this is overwritten on each Gibbs iteration and then written to JSON
			Sample sample = new Sample(7);
			int [] data = new int [7];
			double [] likelihoods = new double [7];
			double cost = 0;

			//GSON-Dan
			//// Not a GSON thing, but eventually a descriptor string or flag like this should be part of the sample class (or whatever replaces it)
			writer.write("Dimple running Gibbs solver on HMM with temperature schedule.\n");

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

			System.out.println(HMM.toString());

			MondayWeather.setInput(0.7,0.3);

			SFactorGraph sHMM = HMM.setSolverFactory(new com.analog.lyric.dimple.solvers.gibbs.Solver());
			sHMM.saveAllSamples();
			sHMM.setNumSamples(500);
			sHMM.setInitialTemperature(1);
			sHMM.enableTempering();
			HMM.initialize();
			
			

			for (int i=0;i<500;i++)
			{	
			
				HMM.getSolver().iterate(1);
				
				data[0]=(int) ((SDiscreteVariable) MondayWeather.getSolver()).getCurrentSampleIndex();
				data[1]=(int) ((SDiscreteVariable) TuesdayWeather.getSolver()).getCurrentSampleIndex();
				data[2]=(int) ((SDiscreteVariable) WednesdayWeather.getSolver()).getCurrentSampleIndex();
				data[3]=(int) ((SDiscreteVariable) ThursdayWeather.getSolver()).getCurrentSampleIndex();
				data[4]=(int) ((SDiscreteVariable) FridayWeather.getSolver()).getCurrentSampleIndex();
				data[5]=(int) ((SDiscreteVariable) SaturdayWeather.getSolver()).getCurrentSampleIndex();
				data[6]=(int) ((SDiscreteVariable) SundayWeather.getSolver()).getCurrentSampleIndex();

				if (i==50) sHMM.setTemperature(1);
				if (i==100) sHMM.setTemperature(2);
				if (i==150) sHMM.setTemperature(5);
				if (i==200) sHMM.setTemperature(10);
				if (i==250) sHMM.setTemperature(5);
				if (i==300) sHMM.setTemperature(3);
				if (i==350) sHMM.setTemperature(1);
				if (i==400) sHMM.setTemperature(.1);
				
				likelihoods[0]=(double) ((SDiscreteVariable) MondayWeather.getSolver()).getCurrentSampleScore();
				likelihoods[1]=(double) ((SDiscreteVariable) TuesdayWeather.getSolver()).getCurrentSampleScore();
				likelihoods[2]=(double) ((SDiscreteVariable) WednesdayWeather.getSolver()).getCurrentSampleScore();
				likelihoods[3]=(double) ((SDiscreteVariable) ThursdayWeather.getSolver()).getCurrentSampleScore();
				likelihoods[4]=(double) ((SDiscreteVariable) FridayWeather.getSolver()).getCurrentSampleScore();
				likelihoods[5]=(double) ((SDiscreteVariable) SaturdayWeather.getSolver()).getCurrentSampleScore();
				likelihoods[6]=(double) ((SDiscreteVariable) SundayWeather.getSolver()).getCurrentSampleScore();

				
				// GSON-Dan
				//// The getScore isn't quite the right quantity but something should be added.
				cost=HMM.getScore();
				//// Using setter methods of the sample class
				//// I'd prefer to have the setters return the object so they can be chained, but I didn't here for code readability
				sample.setCost(cost);
				sample.setData(data);
				sample.setLikelihoods(likelihoods);	
				
				//GSON-Dan
				//// Pass the sample and the writer to the GSON object for writing to the file, then append a newline character to easily separate the samples for the 
				gson.toJson(sample,writer);
				writer.append('\n');


			}
			
			sHMM.disableTempering();
			sHMM.setTemperature(1);
			//**********************
			//This next section does everything over because the getBelief() method doesn't play well with my temperature modification earlier
			
			HMM.initialize();
			
			sHMM.setNumSamples(500);
			sHMM.saveAllSamples();
			((SDiscreteVariable) MondayWeather.getSolver()).saveAllSamples();
			((SDiscreteVariable) TuesdayWeather.getSolver()).saveAllSamples();
			((SDiscreteVariable) WednesdayWeather.getSolver()).saveAllSamples();
			((SDiscreteVariable) ThursdayWeather.getSolver()).saveAllSamples();
			((SDiscreteVariable) FridayWeather.getSolver()).saveAllSamples();
			((SDiscreteVariable) SaturdayWeather.getSolver()).saveAllSamples();
			((SDiscreteVariable) SundayWeather.getSolver()).saveAllSamples();
			
			HMM.solve();
			System.out.println(sHMM.getNumSamples());

			Object [] belief = new Object [7];
			// This section grabs beliefs during belief propagation		
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

			//GSON-Dan
			//// Write a newline and close the file
			writer.append('\n');
			writer.close();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}