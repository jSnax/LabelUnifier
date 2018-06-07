package de.uni_koblenz.phrase;

import java.util.ArrayList;
import java.util.List;
//<<<<<<< Updated upstream
//=======
import java.util.regex.Pattern;
//>>>>>>> Stashed changes
import java.util.regex.Matcher;

import de.uni_koblenz.enums.PartOfSpeechTypes;

import java.io.IOException;

public class PhraseStructure {

	String phraseElements;
	Phrase[] generatedPhrase;
	// TEMPORARY ADDITION OF STRUCTLIST BELOW, NO LONGER NEEDED ONCE THIS CLASS IS COMPLETED
	List<PartOfSpeechTypes> StructList;

	
	public PhraseStructure() {
		
	}
	
	public List<List<String>> extrPatternTypes(String input) throws IOException {
//		String input = "";
//		input = new String(Files.readAllBytes(Paths.get("/Users/Monika/Project/SemanticProcessModeling/src/main/java/de/uni_koblenz/ppbks18/examplePhraseStructure.txt")));
		
		//the pattern for the first groups (activity, state) is left out, focus on it maybe later
		Pattern type = Pattern.compile("(<.*>)[,|\n]");
		List<String> tempPS = new ArrayList<String>();
		Matcher PS = type.matcher(input);
		while(PS.find()){
			int i = 0;
			tempPS.add(PS.group(i));
			i++;
		}
//		for(int i = 0; i < tempPS.size(); i++){
//			System.out.println(tempPS.get(i));
//			}
		
		Pattern parts = Pattern.compile("(<.*?>)");
		List<List<String>> activityPSs = new ArrayList<List<String>>();
		
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
//		for(int i = 0; i < activityPSs.size(); i++){
//			for(int j = 0; j < (activityPSs.get(i)).size(); j++){
//				System.out.println((activityPSs.get(i)).get(j));
//				}
//			System.out.println("");
//			}
//		}
		
		List<List<String>> finalPSs = new ArrayList<List<String>>();
		for(int i = 0; i < 4; i++){
			finalPSs.add(activityPSs.get(i));
			}
		
		for(int i = 0; i < finalPSs.size(); i++){
			for(int j = 0; j < (finalPSs.get(i)).size(); j++){
				System.out.println((finalPSs.get(i)).get(j));
				}
			System.out.println("");
			}
		return finalPSs;
		}
	
	
	
	public void setPhraseElements(String phraseElements) {
		this.phraseElements = phraseElements;
	}
	
	public Phrase[] getGeneratedPhrase() {
		return generatedPhrase;
	}
	
	public void setGeneratedPhrase(Phrase[] generatedPhrase) {
		this.generatedPhrase = generatedPhrase;
	}
	// TEMPORARY METHODS FOR STRUCTLIST BELOW
	public List<PartOfSpeechTypes> getStructList() {
		return StructList;
	}
		
	public void setStructList(List<PartOfSpeechTypes> StructList) {
		this.StructList = StructList;
	}
}
