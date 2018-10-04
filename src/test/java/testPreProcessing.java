
import java.io.IOException;

import de.uni_koblenz.label.LabelList;
import net.sf.extjwnl.JWNLException;


public class testPreProcessing {
	
	public static void main (String args[]) throws JWNLException, ClassNotFoundException, IOException {
	
		String[] test=new String[] {
				
				// passive
				"By bad accident he got injured.",
				"An ambulance is called by Mr Grant.",
				"Bill is payed by company",
				"The letter is written by Tom.",	
				"At dinner, Six shrimp were eaten by Harry.",
				"The savannah is roamed by beautiful giraffes.",
				"The Grand Canyon is viewed by thousands of tourists every year.",
				"The metal beams were eventually corroded by the saltwater",
				"Sugar cane is raised by some people in Hawaii.",
				"The obstacle course was run by me in record time.",
				"The entire stretch of highway was paved by the crew.",
				"A safety video will be watched by staff every year.",
				
				// compound
				"School university teachers.",
				"Signed up.",
				
				"This will survive.",
				// label as sentence vs label as label as is
				"He checks the invoice.",
				"verifying bill",
				
				// example for multiple ACTION SUBJECT and OBJECT.
				"The fire fighter drives the fire truck.",
				
				// main clause and subordinate clause
				"He went to the store, because he wanted to go.",
				
				// problems with to be
				"This is a short sentence",
				"This is a sentence.",
				
				// testing numbers and symbols
				"Number 46 # on 4th May 2018 4:30."
				
				};
		System.out.println("Algorithm started");

		LabelList input=new LabelList(test);
		
		System.out.println("Algorithm completed");
		//print all words and variables
		System.out.println(input);
	}
}
