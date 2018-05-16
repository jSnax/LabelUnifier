package de.uni_koblenz.ppbks18;

import java.io.IOException;

import de.uni_koblenz.label.Label;
import de.uni_koblenz.label.LabelList;
import net.sf.extjwnl.JWNLException;


public class MainPP {
	
	public static void main (String args[]) throws JWNLException, ClassNotFoundException, IOException {
	
		String[] test=new String[] {"checking invoice","invoice checked","This is a really short sentence"};
		System.out.println("Algorithm started");

		LabelList input=new LabelList(test);
		
		System.out.println("Algorithm completed");
		System.out.println(input.getInputLabels()[0].getWordsarray()[0].getPartOfSpeech());
		System.out.println(input.getInputLabels()[0].getWordsarray()[0].getOriginalForm());
		System.out.println(input.getInputLabels()[0].getWordsarray()[0].getBaseform());
	}
}
