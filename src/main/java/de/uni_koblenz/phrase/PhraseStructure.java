package de.uni_koblenz.phrase;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.nio.file.*;;

public class PhraseStructure {

	String phraseElements;
	Phrase[] generatedPhrase;
	
	public PhraseStructure() {
		
	}
	
	public List<String> extrPatternTypes() throws IOException {
		String input = "";
		input = new String(Files.readAllBytes(Paths.get("/examplePhraseStructure.txt")));
		
		//the pattern for the first groups (activity, state)
		Pattern type = Pattern.compile("\\{(.*?)\\}");
		Matcher m1 = type.matcher(input);
		
		String t1 = "";
		String t2 = "";

		if(m1.find()){
			t1=m1.group(1);
			t2=m1.group(2);
		}
		List<String> types = new LinkedList<String>();
		types.add(t1);
		types.add(t2);
		return types;
	}
	
	public List<String> getPhraseElements(String g) {

		//the pattern within the typegroupp (commas)
		Pattern phraseTypes = Pattern.compile(	",		# Match a comma\n" +
												"(?!	# only if it's not followed by...\n" +
												" [^<]*	#any number of characters except opening parens\n" +
												" \\>	#followed by a closing parens\n" +
												")		# End of lookahead" );
		
		Matcher commas = phraseTypes.matcher(g);
		
		List<String> phraseElementsGroup = new LinkedList<String>();
		int count = 0;
		while(commas.find()){
			count++;
			phraseElementsGroup.add(commas.group(count));
		}
		
		return phraseElementsGroup;
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
	

	
}
