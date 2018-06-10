package de.uni_koblenz.label;

import de.uni_koblenz.cluster.GrammaticalRelationBetweenWords;
import de.uni_koblenz.enums.PartOfSpeechTypes;
import de.uni_koblenz.enums.RelationName;
import de.uni_koblenz.enums.RoleLeopold;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.util.CoreMap;
import net.sf.extjwnl.JWNLException;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.*;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import net.sf.extjwnl.data.POS;
public class Label {
	

	private List<Sentence> sentenceArray = new ArrayList<Sentence>();
	private String labelAsString;
	private SemanticGraph parsedLabel;
	private boolean isFinal;
	
	public Label() {
		
	}
	
	public Label(String labelAsString) throws JWNLException {
		
		// ##################### TO-DO ######################
		// TEST IF THIS PART HAS NEGATIVE INFLUENCE ON POS TAGING
		// ##################################################
		
		// e.g. check invoice -> noun noun BUT: Check invoice. -> verb noun
		
		// manipulating POS-taging from Corenlp by transforming label into sentence
		if (labelAsString.substring(labelAsString.length()-1).matches("[a-zA-Z0-9]")){
			// set punctuation
			labelAsString+=".";
			// capitalize first letter
			labelAsString=labelAsString.substring(0, 1).toUpperCase()+labelAsString.substring(1);
		}
		//store label as string
		this.labelAsString=labelAsString;	
		// annotate label
		CoreDocument document = new CoreDocument(labelAsString);
		LabelList.pipeline.annotate(document);
		
		// create Sentence objects
		for (CoreSentence sentence:document.sentences()) {
			sentenceArray.add(new Sentence(sentence));
		}
	}
	
	
	
	public List<Sentence> getSentenceArray() {
		return sentenceArray;
	}

	public void setSentenceArray(List<Sentence> sentenceArray) {
		this.sentenceArray = sentenceArray;
	}

	public String getLabelAsString() {
		return labelAsString;
	}
	
	public void setLabelAsString(String labelAsString) {
		this.labelAsString = labelAsString;
	}
	
	public SemanticGraph getParsedLabel() {
		return parsedLabel;
	}
	
	public void setParsedLabel(SemanticGraph parsedLabel) {
		this.parsedLabel = parsedLabel;
	}
	
	public boolean isFinal() {
		return isFinal;
	}
	
	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}
	

	@Override
	public String toString() {
		String result="Label:\n";
		for (Sentence sentence:sentenceArray) {
			result+=sentence.toString()+"\n";
			
		}
		return result;
	}
}
