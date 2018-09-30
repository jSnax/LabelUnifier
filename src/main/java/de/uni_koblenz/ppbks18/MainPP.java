package de.uni_koblenz.ppbks18;
import de.uni_koblenz.cluster.*;
import de.uni_koblenz.label.*;
import de.uni_koblenz.phrase.*;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.realiser.english.Realiser;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MainPP {
	public static void main (String args[]) throws Exception {
		
	System.out.println("Started.");
	Lexicon lexicon = Lexicon.getDefaultLexicon();
	NLGFactory nlgFactory = new NLGFactory(lexicon);
    Realiser realiser = new Realiser(lexicon);
	
	
	/*	in this block the conf file is read -> this will be needed in productive mode
    FileReader fr = new FileReader("conf.txt");
    BufferedReader br = new BufferedReader(fr);
    String line1 = br.readLine();
	*/
    
    
	String[] input=new String[] {
			"Employee checked tiny invoice",
			"Employee verifies small invoice",
			"Employees check bills",
			"Verify bill. Feed dog.",
			"Company pays bill",
			"Bill is payed by company",
			"Verify bill",
			"bill is taken from box",
			"take bill from box",
			"Employer pays salary from savings",
			"Rich Employer pays big salary from huge savings quickly",
			"Cat feeds cute dog well",
			"Cat feeds sweet dog well",
			"Dog feeds charming cat nicely",
			"take bill",
			"Big cat feeds small dog"
			
	};
	LabelList testList = new LabelList(input);
	DomainThesaurus thesaurus = new DomainThesaurus();
	ForbiddenWords bannedList = new ForbiddenWords();
	testList.numberLabels();
	testList.findSynsets(bannedList);
	List<WordCluster> AllClusters = testList.matchSynonyms();
	for (int i = 0; i < AllClusters.size(); i++){
		AllClusters.get(i).calculateDominance(thesaurus);
		AllClusters.get(i).generalizeWords();
	}
	PhraseStructureList completeList = new PhraseStructureList();
	completeList.sortStructures();
	for (int i = 0; i < testList.getInputLabels().size(); i++){
		for (int j = 0; j < testList.getInputLabels().get(i).getSentenceArray().size(); j++){
			testList.getInputLabels().get(i).getSentenceArray().get(j).toPhrase(completeList, realiser, nlgFactory);
		}
	}
    List<String> lines = new ArrayList<String>(); 
	ArrayList<String> originalContent = new ArrayList<String>();
	for (int i = 0; i < testList.getInputLabels().size(); i++){
		for (int j = 0; j < testList.getInputLabels().get(i).getSentenceArray().size(); j++){
			originalContent.add(testList.getInputLabels().get(i).getSentenceArray().get(j).getContentAsString());
		}
	}
	ArrayList<PhraseCluster> finalList = testList.createClusters();
	int i = 0;
	lines.add("Recommended generalized labels:");
	while (i < finalList.size() && !finalList.get(i).isAlternativeCluster()){
		lines.add("");
		lines.add("Phrase "+i);
		lines.add(finalList.get(i).getBuiltPhrase());
		lines.add("With the corresponding original labels:");
		for (int j = 0; j < finalList.get(i).getLabelAndSentencePositions().size(); j++){
			int labelPosition = finalList.get(i).getLabelAndSentencePositions().get(j).get(0);
			int sentencePosition = finalList.get(i).getLabelAndSentencePositions().get(j).get(1);
			lines.add("Label "+labelPosition+", Sentence "+sentencePosition+" ("+testList.getInputLabels().get(labelPosition).getSentenceArray().get(sentencePosition).getContentAsString()+")");
		}
		i++;
	}
	lines.add("");
	lines.add("----------------------------------------------");
	lines.add("");
	lines.add("Full list of possible phrases:");
	for (int j = i; j < finalList.size(); j++){
		lines.add("");
		lines.add("Label "+finalList.get(j).getWasLabel()+", Sentence "+finalList.get(j).getWasSentence()+" was:");
		lines.add(originalContent.get(j-i));
		if (finalList.get(j).getAllPhrases().get(0).getNoStructureFound()){
			lines.add("No applicable PhraseStructure was found.");
		}
		else {
			lines.add("Possible Phrases:");
			for (int counter = 0; counter < finalList.get(j).getAllPhrases().size(); counter++){
				lines.add("Phrase "+counter+": "+finalList.get(j).getAllPhrases().get(counter).getFullContent());
			}
		}
	}
	java.nio.file.Path file = Paths.get("result.txt");
    Files.write(file, lines, Charset.forName("UTF-8"));
	}
}
