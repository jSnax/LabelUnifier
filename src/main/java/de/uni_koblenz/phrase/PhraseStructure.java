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
	
	// returns a list of Phrase Structure
	// a Phrase Structure e. g. <verb, imperative> <noun, singular> would be a list of String itself, where "verb, imperative" is one list element
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
		
		for(int i = 0; i < finalPSs.size(); i++){
			for(int j = 0; j < (finalPSs.get(i)).size(); j++){
				System.out.println((finalPSs.get(i)).get(j));
				}
			System.out.println("");
			}
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
