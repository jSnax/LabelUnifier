package de.uni_koblenz.label;

import de.uni_koblenz.enums.*;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;

import de.uni_koblenz.cluster.*;

import de.uni_koblenz.cluster.GrammaticalRelationBetweenWords;
import de.uni_koblenz.enums.PartOfSpeechTypes;
import de.uni_koblenz.enums.RelationName;
import de.uni_koblenz.enums.RoleLeopold;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.patterns.surface.Token;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.dictionary.Dictionary;

public class Word implements java.io.Serializable{
	
	private transient CoreLabel token;
	
	private PartOfSpeechTypes partOfSpeech;
	private ArrayList<GrammaticalRelationBetweenWords> grammaticalRelations=new ArrayList<GrammaticalRelationBetweenWords>();
	private String baseform;
	private String originalForm;
	private RoleLeopold role;
	private Integer dominance;
	private List<String> Synonyms;
	private Integer ClusterPosition;
	private boolean alreadyUsedForStructure;

	
	public Word() {
		
	}
	
	public Word(String originalForm) {
		this.originalForm = originalForm;
	}
	// main method
	public Word(CoreLabel token) throws JWNLException {
		this.token=token;
		// gets overwritten by method in Label/Sentence Object
		role=RoleLeopold.OPTIONAL_INFORMATION_FRAGMENT;
		setOriginalForm(token.originalText());
		tagLabel();
		stemWord();
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
	
	public List<GrammaticalRelationBetweenWords> getGrammaticalRelations() {
		return grammaticalRelations;
	}
	
	public void setGrammaticalRelations(ArrayList<GrammaticalRelationBetweenWords> grammaticalRelations) {
		this.grammaticalRelations = grammaticalRelations;
	}
	/*
	 *  Get all relations of this word with a specific RelatioNname
	 */
	public List<GrammaticalRelationBetweenWords> getGrammaticalRelationsByName(RelationName relationName) {
		List<GrammaticalRelationBetweenWords> grammaticalRelationsSimplified=new ArrayList<GrammaticalRelationBetweenWords>();
		for(GrammaticalRelationBetweenWords grammaticalRelation:grammaticalRelations) {
			if(grammaticalRelation.getGrammaticalRelationName()==relationName) {
				grammaticalRelationsSimplified.add(grammaticalRelation);
			}
		}
		return grammaticalRelationsSimplified;
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
	
	public List<String> getSynonyms() {
		return Synonyms;
	}
	
	public Integer getClusterPosition() {
		return ClusterPosition;
	}

	public void setClusterPosition(Integer clusterPosition) {
		ClusterPosition = clusterPosition;
	}
	
	/*
	 * method to tag a label into PartOfSpeechTypes
	 */
	public void tagLabel() {
		String pos=token.tag();

        
        // NEUE PART OF SPEECH ABFRAGE
        for (PartOfSpeechTypes type : PartOfSpeechTypes.values()) {
        	if (pos.equals(type.getShortType())) {
        		this.setPartOfSpeech(type);
        		break;
        	}
        	
        }
        // handle if no matching pos was found
        if(partOfSpeech==null) {
        	// set Symbol POS
        	if(pos.length()==1 && !pos.matches("[a-zA-Z0-9]")) {
        		this.setPartOfSpeech(PartOfSpeechTypes.SYMBOL);
        	}else {
        		System.out.println("No POS enum found for: " + baseform + ":" + pos);
        		this.setPartOfSpeech(PartOfSpeechTypes.NONE);
        	}
        }
    }
	
	public void setSynonyms(List<String> Synonyms) {
		this.Synonyms = Synonyms;
	}
	
	public void addSynonym(String newSyn){
		Synonyms.add(newSyn);
	}
	
	public Word cloneWord(){
		Word clone = new Word();
		clone.setBaseform(this.getBaseform());
		clone.setClusterPosition(this.getClusterPosition());
		clone.setDominance(this.dominance);
		// clone.setGrammaticalRelations(this.getGrammaticalRelations());
		// Type mismatch for some reason...
		clone.setOriginalForm(this.getOriginalForm());
		clone.setPartOfSpeech(this.getPartOfSpeech());
		clone.setRole(this.getRole());
		clone.setSynonyms(this.getSynonyms());
		return(clone);
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

	/*
	 *  method to get the lemma of single Word using Wordnet via extJWNL
	 */
	public void stemWord() throws JWNLException {
		
		Dictionary dict = Dictionary.getDefaultResourceInstance();
		POS pos = null;
		if(partOfSpeech!=null) {
			pos = partOfSpeech.getJwnlType();
		}
	    
		if(pos == null) {
			setBaseform(getOriginalForm());
		} else {
            IndexWord word = dict.getIndexWord(pos,getOriginalForm());
            String lemma = null;
            if (word != null) {
                lemma = word.getLemma();
            } else {
                IndexWord toForm = dict.getMorphologicalProcessor().lookupBaseForm(pos,getOriginalForm());
                if (toForm != null) {
                    lemma = toForm.getLemma();
                } else {
                    lemma = getOriginalForm();
                }
            }
            setBaseform(lemma);	
		}
	}
	
	/* calculates the dominance of a word based on the sum of it appeareance
	 * Probably obsolete due to method calculateDominance in WordCluster
	 */
	
/*	public void calculateDominance(List<Word> allWords) {
		int dominanceCalculator = 0;
		
		// shorter version of the loop below ########################
		for (Word w : allWords) {
			if (w.baseform.equals(this.baseform)) {
				dominanceCalculator++;
			} else {
				continue;
			}
		
		}
		// OLD METHOD BELOW
		for (int zaehler = 0; zaehler < allWords.size(); zaehler++) {
			if (allWords.get(zaehler).baseform.equals(this.baseform)) {
				dominanceCalculator++;
			} else {
				continue;
			}
			
		}
		// OLD METHOD ABOVE
		this.dominance = dominanceCalculator;
		
	}	
*/
	
	@Override
	public String toString() {
		String grammaticalRelationAsString=" Grammatical relations: ";
		for(GrammaticalRelationBetweenWords grammaticalRelation:grammaticalRelations) {
			grammaticalRelationAsString+=grammaticalRelation.getGrammaticalRelationName();
			if(grammaticalRelation.getTargetWord()==this) {
				grammaticalRelationAsString+=" <- "+grammaticalRelation.getSourceWord().getOriginalForm();
			}else {
				grammaticalRelationAsString+=" -> "+grammaticalRelation.getTargetWord().getOriginalForm();
			}
			grammaticalRelationAsString+="; ";
		}
		
		return String.format("%-8s%-20s%-8s%-20s%-7s%-38s%-8s%-30s%-1s","Word: ", originalForm ," Base: " , baseform , " POS: " , partOfSpeech , " Role: " , role , grammaticalRelationAsString);

	}

	public boolean getAlreadyUsedForStructure() {
		return alreadyUsedForStructure;
	}

	public void setAlreadyUsedForStructure(boolean alreadyUsedForStructure) {
		this.alreadyUsedForStructure = alreadyUsedForStructure;
	}

	
	public boolean isSynonym(String comparingWord){
		int i = 0;
		boolean hasThatSynonym = false;
		while (i < this.getSynonyms().size() && !hasThatSynonym){
			if (this.getSynonyms().get(i).equals(comparingWord)){
				hasThatSynonym = true;
			}
			i++;
		}
		return hasThatSynonym;
	}

}
	
	/* stem nimmt String originalForm als Input und gibt String(?) baseForm als Output
	 * 
	 * public void stem (String originalForm) {
		
		*/
		
	/* Calculate dominance nimmt Label[] ?? und gibt eine geordnete Reihenfolge von Integern dominance aus - d.h. berechnet das dominante Wort/Label
		 * 
		 * public void calculateDominance (Label[] ??){
		 * 
		 * }

	 */
	
