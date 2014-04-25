package testBed;

import java.io.IOException;
import java.io.Writer;

import com.analog.lyric.chmpl.ChimpleProgram;
import com.google.gson.Gson;

public class ExtendedChimpleProgram extends ChimpleProgram {
	Writer writer = null;
	Gson gson = null;
	int counter = 0;
	int spacing = 0;
	// Contains a current sample
	Sample sample = new Sample(7);

	public ExtendedChimpleProgram(Gson jsonStream, Writer inputStream, int spacingInput) {

		// Constructor takes a GSON conversion object and a file write stream
		writer=inputStream;
		gson=jsonStream;
		spacing = spacingInput;
	}


	public Object run(Object ... args)
	{
		{		

			//-------------
			// Here we set up the chimple random variables. In this case, we use a for loop to create a large number and give them unique names.
			// After, we draw the points that are connected to the chimprands and obtain the l2 cost function, returning it to the cost function of the MH algorithm.
			//-------------

			int [] days = new int[7];
			int [] bestSample = new int[7];
			double [] weight = new double[3];
			double rainWeight;
			Object [] samples= new Object[3];
			Object [] actChoice= new Object[7];
			double [][] weightRef = new double[2][3];
			double [] likelihoods = new double[7];


			samples[0]=1;
			samples[1]=2;
			samples[2]=3;


			// Factor - between two days
			// If it's sunny (no rain) these are the probabilities of 
			// going for a walk (0), cooking (1), and reading (2).
			weightRef[0][0]=.7;
			weightRef[0][1]=.2;
			weightRef[0][2]=.1;

			// If it's rainy, these are the probabilities in said domain
			weightRef[1][0]=.2;
			weightRef[1][1]=.4;
			weightRef[1][2]=.4;


			// Flip a coin to see if it rains
			days[0] =chimpFlip("day0",.3);

			for (int i=1; i<7; i++) {
				// This is the transition factor
				if (days[i-1]==1){rainWeight=.5;}
				else {rainWeight=.2;}
				days[i] = chimpFlip("day"+i,rainWeight);
			}

			//Observation likelihoods
			//day 1 walked
			likelihoods[0]=-Math.log(weightRef[days[0]][0]);
			
			
			
			
			//day 2 walked
			likelihoods[1]=-Math.log(weightRef[days[1]][0]/(weightRef[0][0]+weightRef[1][0]));
			//day 3 cooked
			likelihoods[2]=-Math.log(weightRef[days[2]][1]/(weightRef[0][1]+weightRef[1][1]));
			//day 4 walked
			likelihoods[3]=-Math.log(weightRef[days[3]][0]/(weightRef[0][0]+weightRef[1][0]));
			//day 5 cooked
			likelihoods[4]=-Math.log(weightRef[days[4]][1]/(weightRef[0][1]+weightRef[1][1]));
			//day 6 read
			likelihoods[5]=-Math.log(weightRef[days[5]][2]/(weightRef[0][2]+weightRef[1][2]));
			//day 7 read
			likelihoods[6]=-Math.log(weightRef[days[6]][2]/(weightRef[0][2]+weightRef[1][2]));

			


			//Define rain likelihoods for the first day
			double firstL = .7*(1-days[0])+days[0]*.3;
		
			// Condition
			for (int i=0;i<40;i++) addCost(likelihoods[i]);
			addCost(-Math.log(firstL));

			//record a sample and send it to output
			counter++;
			if (counter==spacing){
				counter=0;
				
				// Save best samples				
				bestSample=(int [])this.getResult();
				for (int i=0;i<7;i++) likelihoods[i]=(double)this.getLikelihood();
				sample.setData(bestSample);
				
				// Save all samples
				//sample.setData(days);
				sample.setLikelihoods(likelihoods);
				
				System.out.println(sample.setCost(this.getLikelihood()));
				gson.toJson(sample,writer);

				try {
					writer.append('\n');
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				for (int k=0;k<7;k++){
					System.out.println(days[k]);
				}

			}
			return days;	
		}
	}
}