package de.uni_koblenz.label;

import de.uni_koblenz.cluster.GrammaticalRelation;
import de.uni_koblenz.enums.PartOfSpeechTypes;
import de.uni_koblenz.enums.RoleLeopold;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.util.CoreMap;
import net.sf.extjwnl.JWNLException;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.*;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class Label {
	
	private Word[] wordsarray;
	// changed variable #############################
	private CoreSentence[] labelAsCoreSentenceList;
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
		List<CoreSentence> labelAsCoreSentenceListList = new ArrayList<CoreSentence>();
		for (CoreSentence sentence:document.sentences()) {
			labelAsCoreSentenceListList.add(sentence);
		}
		this.setLabelAsCoreSentenceList(labelAsCoreSentenceListList.toArray(new CoreSentence[labelAsCoreSentenceListList.size()]));
		
		createWordsArray();
	}
	
	
	public Word[] getWordsarray() {
		return wordsarray;
	}
	
	public void setWordsarray(Word[] wordsarray) {
		this.wordsarray = wordsarray;
	}
	// changed######################### 
	public CoreSentence[] getLabelAsCoreSentenceList() {
		return labelAsCoreSentenceList;
	}
	// changed##########################
	public void setLabelAsCoreSentenceList(CoreSentence[] labelAsCoreSentenceList) {
		this.labelAsCoreSentenceList = labelAsCoreSentenceList;
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
	
	/*
	 * method to convert a String into a Word-array
	 */ 
	public void createWordsArray() throws JWNLException{
		// create words
		List<Word> wordsarrayList = new ArrayList<Word>();
		List<Word> wordsarrayListTemp = new ArrayList<Word>();
		for (CoreSentence sentence:labelAsCoreSentenceList) {
			List<CoreLabel> tokens = sentence.tokens();
			for (CoreLabel token:tokens) {
				//create words and put them in a temporary list (easier to work with index)
				wordsarrayListTemp.add(new Word(token));
				
			}
			//create grammatical relations and add them
			SemanticGraph graph=sentence.dependencyParse();
			//go through all grammatical relations of the current sentence
		    for(SemanticGraphEdge edge:graph.edgeIterable()) {
		    	//get the Word equivalent of the tokens
		    	Word sourceWord=wordsarrayListTemp.get(edge.getSource().index()-1);
		    	Word targetWord=wordsarrayListTemp.get(edge.getTarget().index()-1);
		    	//create GrammaticalRelation
		    	GrammaticalRelation grammaticalRelationTemp= new GrammaticalRelation(sourceWord,targetWord);
		    	//store grammatical Relation in both words
		    	sourceWord.addGrammaticalRelation(grammaticalRelationTemp);
		    	targetWord.addGrammaticalRelation(grammaticalRelationTemp);
		    }
				
				
				
			
			//add the wordlist of this single sentence to the complete list of words
			wordsarrayList.addAll(wordsarrayListTemp);
			wordsarrayListTemp.clear();
		}
		
		
		this.setWordsarray(wordsarrayList.toArray(new Word[wordsarrayList.size()]));
	}
	

	
	
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
	      } */
	    }
	    
	}
	@Override
	public String toString() {
		String result="Label:\n";
		for (Word word:wordsarray) {
			result+=word.toString()+"\n";
			
		}
		return result;
	}
}
