package de.uni_koblenz.label;

import de.uni_koblenz.cluster.GrammaticalRelation;
import de.uni_koblenz.enums.PartOfSpeechTypes;
import de.uni_koblenz.enums.RoleLeopold;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.dictionary.Dictionary;

public class Word {

	private PartOfSpeechTypes partOfSpeech;
	private GrammaticalRelation[] grammaticalRelations;
	private String baseform;
	private String originalForm;
	private RoleLeopold role;
	private Integer dominance;
	
	public Word() {
		
	}
	public Word(String originalForm) {
		this.originalForm = originalForm;
	}
	public Word(PartOfSpeechTypes partOfSpeech) {
		this.partOfSpeech = partOfSpeech;
	}
	public Word(String originalForm, PartOfSpeechTypes partOfSpeech) {
		this.originalForm = originalForm;
		this.partOfSpeech = partOfSpeech;
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
	}*/ 


	public static void stemWord(Word input) throws JWNLException {
		
		Dictionary dict = Dictionary.getDefaultResourceInstance();
		
		POS pos = null;
		
		if (input.getPartOfSpeech().equals(PartOfSpeechTypes.NOUN_SINGULAR_MASS) ||
				input.getPartOfSpeech().equals(PartOfSpeechTypes.NOUN_PLURAL) || 
				input.getPartOfSpeech().equals(PartOfSpeechTypes.PROPER_NOUN_SINGULAR) ||
				input.getPartOfSpeech().equals(PartOfSpeechTypes.PROPER_NOUN_PLURAL) ||
				input.getPartOfSpeech().equals(PartOfSpeechTypes.NOUN_PHRASE)){ 
	        pos = POS.NOUN; 
	      } else if (input.getPartOfSpeech().equals(PartOfSpeechTypes.VERB_BASE) ||
	    		  input.getPartOfSpeech().equals(PartOfSpeechTypes.VERB_PAST) ||
	    		  input.getPartOfSpeech().equals(PartOfSpeechTypes.VERB_GERUND_OR_PRESENT_PARTICIPLE) ||
	    		  input.getPartOfSpeech().equals(PartOfSpeechTypes.VERB_PAST_PARTICIPLE) ||
	    		  input.getPartOfSpeech().equals(PartOfSpeechTypes.VERB_NON_3RD_PERSON_SINGULAR_PRESENT) ||
	    		  input.getPartOfSpeech().equals(PartOfSpeechTypes.VERB_3RD_PERSON_SINGULAR_PRESENT) ||
	    		  input.getPartOfSpeech().equals(PartOfSpeechTypes.VERB_PHRASE)) { 
	        pos = POS.VERB; 
	      } else if (input.getPartOfSpeech().equals(PartOfSpeechTypes.ADVERB) || 
	    		  input.getPartOfSpeech().equals(PartOfSpeechTypes.ADJECTIVE_COMPARATIVE) ||
	    		  input.getPartOfSpeech().equals(PartOfSpeechTypes.ADVERB_SUPERLATIVE)) { 
	        pos = POS.ADVERB; 
	      } else if (input.getPartOfSpeech().equals(PartOfSpeechTypes.ADJECTIVE) ||
	    		  input.getPartOfSpeech().equals(PartOfSpeechTypes.ADJECTIVE_COMPARATIVE) ||
	    		  input.getPartOfSpeech().equals(PartOfSpeechTypes.ADJECTIVE_SUPERLATIVE)) { 
	        pos = POS.ADJECTIVE; 
	      }else {
	    	pos = null;  
	      }
	    
		if(pos == null) {
			input.setBaseform(input.getOriginalForm());
		} else {
            IndexWord word = dict.getIndexWord(pos, input.getOriginalForm());
            String lemma = null;
            if (word != null) {
                lemma = word.getLemma();
            } else {
                IndexWord toForm = dict.getMorphologicalProcessor().lookupBaseForm(pos, input.getOriginalForm());
                if (toForm != null) {
                    lemma = toForm.getLemma();
                } else {
                    lemma = input.getOriginalForm();
                }
            }
            input.setBaseform(lemma);	
		}
	}
}
