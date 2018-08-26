package de.uni_koblenz.phrase;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.uni_koblenz.label.*;
import de.uni_koblenz.enums.*;


public class Phrase {

	private List<String> separatedContent;
	private String fullContent;
	private Word[] wordsArray;
	private Vector<String> personalVector;
	private double[] vectorNumeration;
	private Vector<String> personalVectorSpace;
	private int usedStructure;
	private int usedStructureSize;

	public Phrase() {
		
	}
	
	
	public List<String> getseparatedContent() {
		return separatedContent;
	}


	public void setseparatedContent(List<String> separatedContent) {
		this.separatedContent = separatedContent;
	}


	public Word[] getWordsArray() {
		return wordsArray;
	}
	
	public void setWordsArray(Word[] wordsArray) {
		this.wordsArray = wordsArray;
	}

	public Vector<String> getPersonalVector() {
		return personalVector;
	}

	public void setPersonalVector(Vector<String> personalVector) {
		this.personalVector = personalVector;
	}

	public double[] getVectorNumeration() {
		return vectorNumeration;
	}

	public void setVectorNumeration(double[] vectorNumeration) {
		this.vectorNumeration = vectorNumeration;
	}

	public Vector<String> getPersonalVectorSpace() {
		return personalVectorSpace;
	}

	public void setPersonalVectorSpace(Vector<String> personalVectorSpace) {
		this.personalVectorSpace = personalVectorSpace;
	}

	public int getUsedStructure() {
		return usedStructure;
	}


	public void setUsedStructure(int usedStructure) {
		this.usedStructure = usedStructure;
	}
	
	// returns a list of Phrase Structure
		// a Phrase Structure e. g. <verb, imperative> <noun, singular> would be a list of String itself, where "verb, imperative" is one list element
	public List<List<String>> extractPhraseStructure (String Namingconventions){
//		String Namingconventions = "";
//		Namingconventions = new String(Files.readAllBytes(Paths.get("/Users/Monika/Project/SemanticProcessModeling/src/main/java/de/uni_koblenz/ppbks18/examplePhraseStructure.txt")));
		
		//the pattern for the first groups (activity, state) is left out, focus on it maybe later
		Pattern type = Pattern.compile("(<.*>)[,|\n]");
		List<String> tempPS = new ArrayList<String>();
		Matcher PS = type.matcher(Namingconventions);
		while(PS.find()){
			int i = 0;
			tempPS.add(PS.group(i));
			i++;
		}
//		for(int i = 0; i < tempPS.size(); i++){
//			System.out.println(tempPS.get(i));
//			}
		
		//Parts in this case divides the whole document into single Phrase Structures, so one element would be <verb, imperative> <noun, singular> (or longer)
		Pattern parts = Pattern.compile("(<.*?>)");
		List<List<String>> activityPSs = new ArrayList<List<String>>();
		
		// put these elements into a list of Strings
		for(int i = 0; i < tempPS.size(); i++){
			Matcher pp = parts.matcher(tempPS.get(i));
			List<String> oneAPS = new ArrayList<String>();
			while(pp.find()){
				int j = 0;
				oneAPS.add(pp.group(j));
				j++;
				}
			activityPSs.add(oneAPS);
			}
		// Testing the output of this list
//		for(int i = 0; i < activityPSs.size(); i++){
//			for(int j = 0; j < (activityPSs.get(i)).size(); j++){
//				System.out.println((activityPSs.get(i)).get(j));
//				}
//			System.out.println("");
//			}
//		}
		
		//Here each Phrase Structure is divided into a list of Strings
		// so  <verb, imperative> <noun, singular> is divided into the first list item: verb, imperative and second list item:  <noun, singular>
		List<List<String>> finalPSs = new ArrayList<List<String>>();
		for(int i = 0; i < 4; i++){
			finalPSs.add(activityPSs.get(i));
			}
		
		// Test the output of the List of Lists
//		for(int i = 0; i < finalPSs.size(); i++){
//			for(int j = 0; j < (finalPSs.get(i)).size(); j++){
//				System.out.println((finalPSs.get(i)).get(j));
//				}
//			System.out.println("");
//			}
		return finalPSs;
		/* the structure is as follows
		 * List
		 * 		List
		 * 			<verb, imperative>
		 * 			<noun, singular>
		 * 		List
		 * 			<verb, imperative>
		 * 			<noun, singular>
		 * 			<preposition> 
		 * 			<noun, singular, object case>
		 * 		...
		 */		
		
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

		public String getFullContent() {
			return fullContent;
		}


		public void setFullContent(String fullContent) {
			this.fullContent = fullContent;
		}


		public int getUsedStructureSize() {
			return usedStructureSize;
		}


		public void setUsedStructureSize(int usedStructureSize) {
			this.usedStructureSize = usedStructureSize;
		}
}

