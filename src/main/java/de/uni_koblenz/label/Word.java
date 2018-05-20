package de.uni_koblenz.label;

import de.uni_koblenz.enums.*;


import java.util.List;

import de.uni_koblenz.cluster.*;

public class Word {

	private PartOfSpeechTypes partOfSpeech;
	private GrammaticalRelationBetweenWords[] grammaticalRelations;
	private String baseform;
	private String originalForm;
	private RoleLeopold role;
	private Integer dominance;
	private List<String> Synonyms;
	
	public Word() {
		
	}
	
	public Word(String originalForm) {
		this.originalForm = originalForm;
	}
	
	public PartOfSpeechTypes getPartOfSpeech() {
		return partOfSpeech;
	}
	
	public void setPartOfSpeech(PartOfSpeechTypes partOfSpeech) {
		this.partOfSpeech = partOfSpeech;
	}
	
	public GrammaticalRelationBetweenWords[] getGrammaticalRelations() {
		return grammaticalRelations;
	}
	
	public void setGrammaticalRelations(GrammaticalRelationBetweenWords[] grammaticalRelations) {
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
	
	public List<String> getSynonyms() {
		return Synonyms;
	}
	
	public void setSynonyms(List<String> Synonyms) {
		this.Synonyms = Synonyms;
	}
	
	public void addSynonym(String newSyn){
		Synonyms.add(newSyn);



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
	
}
