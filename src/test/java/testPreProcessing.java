
import java.io.IOException;

import de.uni_koblenz.label.LabelList;
import net.sf.extjwnl.JWNLException;


public class testPreProcessing {
	
	public static void main (String args[]) throws JWNLException, ClassNotFoundException, IOException {
	
		String[] test=new String[] {
				
				// label as sentence vs label as label as is
				"Check invoice.",
				"check invoice",
				
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
