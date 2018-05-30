package de.uni_koblenz.phrase;
import java.util.ArrayList;
import java.util.List;
import de.uni_koblenz.label.*;
import de.uni_koblenz.enums.*;


public class Phrase {

	private String content;
	private Word[] wordsArray;

	public Phrase() {
		
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public Word[] getWordsArray() {
		return wordsArray;
	}
	
	public void setWordsArray(Word[] wordsArray) {
		this.wordsArray = wordsArray;
	}
	public String extractPhraseStructure (String Namingconventions){
		String PhraseStructure = "";
		
		return PhraseStructure;
	}
	
	public List<String> findApplicablePS(List<String> lbls, List<List<String>> phraseStructures){
		List<String> resPS = new ArrayList<String>();
		//if Action/State
		// finding out which type of PhraseStructures to use
		for(int i = 0; i < phraseStructures.size(); i++){
			if (lbls.size() == phraseStructures.size()){
				resPS = phraseStructures.get(i);
			}
		}
		return resPS;
	}
	
	
	public boolean fulfillsPhraseStructure(String wordCluster, List<String> lbls, List<String> phraseStructure){
		boolean correct = true;
		int labelIndex = 1;
		int phraseIndex = 1;
		while(labelIndex < lbls.size()){
			String phrasePOS = phraseStructure.get(phraseIndex);
			String labelPOS = ""; 
//			labelPOS = getPartOfSpeech(lbls.get(labelIndex));
			if(phrasePOS != labelPOS){
				correct = false;
				break;
			}
			else {
				labelIndex++;
				phraseIndex++;
			}
		}
		return correct;
	}	
}

