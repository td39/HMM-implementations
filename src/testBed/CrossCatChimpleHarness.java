package testBed;

import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import com.analog.lyric.chmpl.ChimpleProgram.ChmplResults;

public class CrossCatChimpleHarness {

	static Sample sample = new Sample(7);
	public static void main(String [] args) throws IOException
	{

		try(Writer writer = new OutputStreamWriter(new FileOutputStream("../streamgraph/dimple.json"))){
			
			//Start a GSON object
			Gson gson = new GsonBuilder().create();

			//Set MH parameters
			int burnin = 0;
			int samples = 500 ;
			int spacing = 50;
						
			//Instantiate the probabilistic program
			ExtendedChimpleProgram p = new ExtendedChimpleProgram(gson,writer,spacing);

			//Activate the MH algorithm
			@SuppressWarnings("unused")
			ChmplResults results = p.chimplify(burnin, samples, spacing); 

			
			
			
			System.out.println("HARNESS DONE");

			writer.append('\n');
			writer.close();
			


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}