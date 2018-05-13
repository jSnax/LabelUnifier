package de.uni_koblenz.label;

import de.uni_koblenz.enums.*;

import de.uni_koblenz.cluster.*;

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
