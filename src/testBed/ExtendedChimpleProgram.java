package testBed;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Writer;

import com.analog.lyric.chmpl.ChimpleProgram;
import com.google.gson.Gson;

public class ExtendedChimpleProgram extends ChimpleProgram {
	Writer writer = null;
	Gson gson = null;

	public ExtendedChimpleProgram(Gson jsonStream, Writer inputStream) {

		//Take in a FileOutputStream
		// in the constructor
		//		
		writer=inputStream;
		gson=jsonStream;
		Sample sample;

	}


	public Object run(Object ... args)
	{
		{		

			//-------------
			// Here we set up the chimple random variables. In this case, we use a for loop to create a large number and give them unique names.
			// After, we draw the points that are connected to the chimprands and obtain the l2 cost function, returning it to the cost function of the MH algorithm.
			//-------------
			//			

			Sample sample = new Sample(7);
			
			int [] days = new int[7];
			int [] activities = new int[7];
			double [] weight = new double[3];
			double rainWeight;
			Object [] samples= new Object[3];
			Object [] actChoice= new Object[7];
			double [][] weightRef = new double[2][3];
			double [] likelihoods = new double[7];
			
			
			samples[0]=1;
			samples[1]=2;
			samples[2]=3;


			// If it's sunny (no rain) these are the probabilities of going for a walk, cooking, and reading.
			weightRef[0][0]=.7;
			weightRef[0][1]=.2;
			weightRef[0][2]=.1;
			// If it's rainy, these are the same probabilities in the domain
			weightRef[1][0]=.2;
			weightRef[1][1]=.4;
			weightRef[1][2]=.4;


			// Flip a coin to see if it rains
			days[0] =chimpFlip("day0",.3);

			// Set each weight to the appropriate reference weight
			for (int i=0; i<3; i++) weight[i]=weightRef[days[0]][i];

			activities[0]=0;
			actChoice[0]=chimpDiscrete("act0",weight,samples);
			activities[0]=(int) actChoice[0];


			for (int i=1; i<7; i++) {
				if (days[i-1]==1){
					rainWeight=.5;
				}
				else{
					rainWeight=.2;
				}

				days[i] =chimpFlip("day"+i,rainWeight);
				for (int j=0; j<3; j++) weight[j]=weightRef[days[i]][j];

				activities[i]=0;
				actChoice[i]=chimpDiscrete("act"+i,weight,samples);
				activities[i]=(int) actChoice[i];

			}

			//double [][] softener = new double [2][7];
			//for (int i=0;i<2;i++) for (int j=0;j<7;j++) {softener[i][j]=chimpRand("var"+i+j); }//addCost(-Math.log(1/softener[i][j]));} 


			
			
			//day 1 walked
			likelihoods[0]=-Math.log(weightRef[days[0]][0]/(weightRef[0][0]+weightRef[1][0]));
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
			
			for (int i=0;i<7;i++) addCost(likelihoods[i]);

			
			//Define rain likelihoods
			double firstL = .7*(1-days[0])+days[0]*.3;
			addCost(-Math.log(firstL));


			double [][] rainL = new double [2][2];
			double [][] rainLNorm = new double [2][2];
			
			rainL[1][0]=.5;
			rainL[0][1]=.2;
			rainL[1][1]=.8;
			rainL[0][0]=.5;
			
			rainLNorm[1][0]=.5/(.5+.8);
			rainLNorm[0][1]=.2/(.2+.5);
			rainLNorm[1][1]=.8/(.5+.8);
			rainLNorm[0][0]=.5/(.2+.5);
			

			for (int i=1;i<7;i++) addCost(-Math.log(rainLNorm[days[i-1]][days[i]]));

			sample.setData(days);
			sample.setLikelihoods(likelihoods);
			System.out.println(sample.setCost(this.getLikelihood()));

			
			
			
			

			//try {
			gson.toJson(sample,writer);
			
			try {
				writer.append('\n');
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//} catch (IOException e) {
		//		// TODO Auto-generated catch block
		//		e.printStackTrace();
		//	}



			for (int k=0;k<7;k++){
				//System.out.println(days[k].toString());
				//System.out.println("Day "+k+": "+days[k]);
				System.out.println(days[k]);
				//System.out.println(costTotal);
			}




			//for (int k=0;k<7;k++)
				//System.out.println(days[k].toString());
				//System.out.println("Day "+k+": "+days[k]);
				//System.out.println(softener[0][k]);
				//System.out.println(sample.cost);


				return days;	
		}
	}
}