package de.uni_koblenz.label;

import java.util.List;
import java.util.Properties;

import de.uni_koblenz.enums.*;
import de.uni_koblenz.cluster.*;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class Word {

	private PartOfSpeechTypes partOfSpeech;
	private GrammaticalRelation[] grammaticalRelations;
	private String baseform;
	private String originalForm;
	private RoleLeopold role;
	private Integer dominance;
	
	public Word() {
		
	}
	
	public PartOfSpeechTypes getPartOfSpeech() {
		return partOfSpeech;
	}
	
	public void setPartOfSpeech(PartOfSpeechTypes partOfSpeech) {
		this.partOfSpeech = partOfSpeech;
	}
	
	public GrammaticalRelation[] getGrammaticalRelations() {
		return grammaticalRelations;
	}
	
	public void setGrammaticalRelations(GrammaticalRelation[] grammaticalRelations) {
		this.grammaticalRelations = grammaticalRelations;
	}
	
	public String getBaseform() {
		return baseform;
	}
	
	public void setBaseform(String baseform) {
		
		this.baseform = baseform;
	}
	
	public String getOriginalForm() {
		return originalForm;
	}
	
	public void setOriginalForm(String originalForm) {
		this.originalForm = originalForm;
	}
	
	public RoleLeopold getRole() {
		return role;
	}
	
	public void setRole(RoleLeopold role) {
		this.role = role;
	}
	
	public Integer getDominance() {
		return dominance;
	}
	
	public void setDominance(Integer dominance) {
		this.dominance = dominance;
	}
	
	/*  stem(String toStem)
	 *  method to get the lemma of a single Word using the CoreNLP Lemmatizer.
	 */ 
	public void stem(String toStem){		 
		StanfordCoreNLP pipeline = new StanfordCoreNLP(new Properties(){
			
			private static final long serialVersionUID = 1L;
			{
			  setProperty("annotators", "tokenize,ssplit,pos,lemma"); 
			  	// initialize annotator dependencies 
			}});

			Annotation token = new Annotation(toStem);
			pipeline.annotate(token); 
			List<CoreMap> list = token.get(SentencesAnnotation.class);
			String stemmed = list
			                        .get(0).get(TokensAnnotation.class)
			                        .get(0).get(LemmaAnnotation.class);
			toStem = stemmed;
	}
	
	
	
}
