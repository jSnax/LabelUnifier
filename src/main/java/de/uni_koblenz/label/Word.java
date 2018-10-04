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
	// constructor
	public Word(CoreLabel token) throws JWNLException {
		this.token=token;
		// gets overwritten by method in Sentence Object (Subject, Business Object)
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
	 * method to set PartOfSpeechTypes by matching POS as String from corenlp with PartOfSpeechTypes enum
	 */
	public void tagLabel() {
		String pos=token.tag();

        
        // find a enum that matches the string
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
        	// set POS to NONE 
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
	


	/*
	 *  method to get the lemma of single Word using Wordnet via extJWNL
	 */
	public void stemWord() throws JWNLException {
		
		Dictionary dict = Dictionary.getDefaultResourceInstance();
		
		if(partOfSpeech.getJwnlType() == null) {
			setBaseform(getOriginalForm());
		} else {
            IndexWord word = dict.getIndexWord(partOfSpeech.getJwnlType(),getOriginalForm());
            String lemma = null;
            if (word != null) {
                lemma = word.getLemma();
            } else {
                IndexWord toForm = dict.getMorphologicalProcessor().lookupBaseForm(partOfSpeech.getJwnlType(),getOriginalForm());
                if (toForm != null) {
                    lemma = toForm.getLemma();
                } else {
                    lemma = getOriginalForm();
                }
            }
            setBaseform(lemma);	
		}
	}
	
	
	
	@Override
	// pretty print Preprocessing
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
	
	
