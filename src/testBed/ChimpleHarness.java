package testBed;



import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import com.analog.lyric.chmpl.ChimpleProgram.ChmplResults;
import com.analog.lyric.chmpl.*;
//import com.lyricsemi.chimple.test.ExternalCost;
//import com.lyricsemi.chimple.test.ExternalCostProgram;
//import com.lyricsemi.chimple.test.MyBiasProgram;
//import com.lyricsemi.chimple.test.ExternalCost;
//import com.lyricsemi.chimple.test.ExternalCostProgram;
//import com.lyricsemi.chimple.test.MyBiasProgram;

public class ChimpleHarness {

	static Sample sample = new Sample(7);
	public static void main(String [] args) throws IOException
	{

		try(Writer writer = new OutputStreamWriter(new FileOutputStream("../streamgraph/dimple.json"))){
			
			//Start a GSON object
			Gson gson = new GsonBuilder().create();
			
			//Instantiate the probabilistic program
			ExtendedChimpleProgram p = new ExtendedChimpleProgram(gson,writer);





			//Set MH parameters
			int burnin = 100;
			int samples = 1000 ;
			int spacing = 20;

			//Activate the MH algorithm
			//@SuppressWarnings("unused")
			ChmplResults results = p.chimplify(burnin, samples, spacing); 

			System.out.println("HARNESS DONE");

			Object [] days = results.getResults();

			writer.append('\n');

			writer.close();

			//save.close();
			//saveFile.close();
			//System.out.println();

			//for (int k=0;k<7;k++)
			//System.out.println(days[k][0].toString());
			//System.out.println((int) days[k]);
			//System.out.println(costTotal);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}