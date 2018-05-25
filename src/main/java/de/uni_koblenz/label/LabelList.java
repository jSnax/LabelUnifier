package de.uni_koblenz.label;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import net.sf.extjwnl.JWNLException;

public class LabelList {
	
	private List<Label> inputLabels = new ArrayList<Label>();
	// new variable #####################################
	
	public static StanfordCoreNLP pipeline;
	
	public LabelList() {
		
	}
	// NEW METHOD ########################################
	
	public LabelList(String[] input) throws JWNLException {
		// Setup CORENLP Pipeline
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, depparse, natlog, openie");//, ner");
		pipeline = new StanfordCoreNLP(props);
		
		// add Label objects for each string of the input array.
		for (String stringLabel:input) {
			inputLabels.add(new Label(stringLabel));
		}
		
		// transform arraylist into array and store it in LabeList.

		
	}

	public List<Label> getInputLabels() {
		return inputLabels;
	}

	public void setInputLabels(List<Label> inputLabels) {
		this.inputLabels = inputLabels;
	}
	
	@Override
	public String toString() {
		String result="Result:\n";
		for (Label label:inputLabels) {
			result+=label.toString()+"\n";
			
		}
		return result;
	}
	
}
