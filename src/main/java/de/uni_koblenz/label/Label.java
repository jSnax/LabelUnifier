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
	

	// changed variable #############################
	private List<Sentence> sentenceArray = new ArrayList<Sentence>();
	private String labelAsString;
	private SemanticGraph parsedLabel;
	private boolean isFinal;
	
	public Label() {
		
	}
	
	public Label(String labelAsString) throws JWNLException {
		//store label as string
		this.labelAsString=labelAsString;	
		// annotate label
		CoreDocument document = new CoreDocument(this.labelAsString);
		LabelList.pipeline.annotate(document);
		
		// store sentences in Label object

		for (CoreSentence sentence:document.sentences()) {
			sentenceArray.add(new Sentence(sentence));
		}
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
	


	
	/** not used
	public static void defineRole(String input) {
		
		RoleLeopold action = RoleLeopold.ACTION;
		RoleLeopold businessObject = RoleLeopold.BUSINESS_OBJECT;
		RoleLeopold optInfFra = RoleLeopold.OPTIONAL_INFORMATION_FRAGMENT;
		
		// Create the Stanford CoreNLP pipeline
	    Properties props = new Properties();
	    props.setProperty("annotators", "tokenize,ssplit,pos,lemma,depparse,natlog,openie");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

	    // Annotate an example document.
	    Annotation doc = new Annotation(input);
	    pipeline.annotate(doc);

	    // Loop over sentences in the document
	    for (CoreMap sentence : doc.get(CoreAnnotations.SentencesAnnotation.class)) {
	    	
	      // Get the OpenIE triples for the sentence
	      Collection<RelationTriple> triples =
		          sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);
	      
	     for (RelationTriple triple : triples) {
	    	 Word[] wordsarray = new Word[5];
	    	 Label l = new Label();
	    	 l.setWordsarray(wordsarray);
	    	 for(int i = 0; i< wordsarray.length; i++) {
		    	 wordsarray[i] = new Word();
	    	 }

	    	 System.out.println(triple.relationGloss());
	    	 System.out.println(triple.objectGloss());
	     }
	      
	      /* Print the triples
	      for (RelationTriple triple : triples) {
	        System.out.println(triple.confidence + "\t" +
	            triple.subjectLemmaGloss() + "\t" +
	            triple.relationLemmaGloss() + "\t" +
	            triple.objectLemmaGloss());
	      } 
	    }
	}*/
	@Override
	public String toString() {
		String result="Label:\n";
		for (Sentence sentence:sentenceArray) {
			result+=sentence.toString()+"\n";
			
		}
		return result;
	}
}
