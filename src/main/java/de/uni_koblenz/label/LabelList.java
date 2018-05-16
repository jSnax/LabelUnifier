package de.uni_koblenz.label;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import net.sf.extjwnl.JWNLException;

public class LabelList {
	
	private Label[] inputLabels;
	// new variable #####################################
	
	public static StanfordCoreNLP pipeline;
	
	public LabelList() {
		
	}
	// NEW METHOD ########################################
	
	public LabelList(String[] input) throws JWNLException {
		// Setup CORENLP Pipeline
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, depparse, ner, natlog, openie");
		pipeline = new StanfordCoreNLP(props);
		
		// Create list and add Label objects for each string of the input array.
		List<Label> LabelListList = new ArrayList<Label>();
		for (String stringLabel:input) {
			LabelListList.add(new Label(stringLabel));
		}
		
		// transform arraylist into array and store it in LabeList.
		this.setInputLabels(LabelListList.toArray(new Label[LabelListList.size()]));
		
	}

	public Label[] getInputLabels() {
		return inputLabels;
	}

	public void setInputLabels(Label[] inputLabels) {
		this.inputLabels = inputLabels;
	}
	
	
}
