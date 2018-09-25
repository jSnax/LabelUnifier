import java.io.IOException;


import java.util.*;

import de.uni_koblenz.cluster.*;
import de.uni_koblenz.enums.*;
import de.uni_koblenz.label.*;
import de.uni_koblenz.phrase.*;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.PointerUtils;
import net.sf.extjwnl.data.Synset;
import net.sf.extjwnl.data.list.PointerTargetNode;
import net.sf.extjwnl.data.list.PointerTargetNodeList;
import net.sf.extjwnl.dictionary.Dictionary;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.WordElement;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.english.Realiser;

import java.io.IOException;

import net.sf.extjwnl.JWNLException;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;


import de.uni_koblenz.cluster.*;
import de.uni_koblenz.enums.*;
import de.uni_koblenz.label.*;
import de.uni_koblenz.phrase.*;

public class shortPresentation {
	public static void main (String args[]) throws Exception {
		
	System.out.println("Started.");
	Lexicon lexicon = Lexicon.getDefaultLexicon();
	NLGFactory nlgFactory = new NLGFactory(lexicon);
	SPhraseSpec p = nlgFactory.createClause();
    Realiser realiser = new Realiser(lexicon);
	Dictionary dictionary = Dictionary.getDefaultResourceInstance();
	
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
			"Rich Employer pays big, beautiful salary from huge savings quickly",
			"Cat feeds cute dog well",
			"Cat feeds sweet dog well",
			"Dog feeds charming cat nicely",
			"take bill",
			"Big cat feeds small dog"
			
	};
	

	// Fill Labels with word arrays
	LabelList testList = new LabelList(input);
	
	System.out.println("Printing Preprocessing results:");
	System.out.println(testList);
	
	// Create DomainThesaurus
	DomainThesaurus thesaurus = new DomainThesaurus();
	// Set Input Labels to previously created Label list
	
	/*System.out.println("Printing Labels:");
	for (int i = 0; i < testList.getInputLabels().size(); i++){
		System.out.println("START OF LABEL " + i);		
		for (int k = 0; k < testList.getInputLabels().get(i).getSentenceArray().size(); k++) {
			for (int j = 0; j < testList.getInputLabels().get(i).getSentenceArray().get(k).getWordsarray().size(); j++){
				System.out.println(testList.getInputLabels().get(i).getSentenceArray().get(k).getWordsarray().get(j).getBaseform());
			}
		}
		System.out.println("END OF LABEL " + i);
	}
	// Simply print the labels
	for (int i = 0; i < 3; i++){
		System.out.println("");
	}*/
	
	ForbiddenWords bannedList = new ForbiddenWords();
	testList.numberLabels();
	testList.findSynsets(bannedList);
	/*System.out.println("Printing Synsets:");
	for (int i = 0; i < testList.getInputLabels().size(); i++){
		System.out.println("START OF LABEL " + i);		
		for (int k = 0; k < testList.getInputLabels().get(i).getSentenceArray().size(); k++) {
			for (int j = 0; j < testList.getInputLabels().get(i).getSentenceArray().get(k).getWordsarray().size(); j++){
				System.out.println(testList.getInputLabels().get(i).getSentenceArray().get(k).getWordsarray().get(j).getBaseform()+" has the Synset "+ testList.getInputLabels().get(i).getSentenceArray().get(k).getWordsarray().get(j).getSynonyms());
			}
		}
		System.out.println("END OF LABEL " + i);
	}
	for (int i = 0; i < 3; i++){
		System.out.println("");
	}*/
	
	System.out.println("Printing Wordclusters");
	List<WordCluster> AllClusters = testList.matchSynonyms();
	for (int i = 0; i < AllClusters.size(); i++){
		System.out.println("Start of Cluster " +i);
		for (int j = 0; j < AllClusters.get(i).matchingWords.size(); j++){
			System.out.println(AllClusters.get(i).matchingWords.get(j).getBaseform());
		}
		System.out.println("End of Cluster " +i);
	}
	
	for (int i = 0; i < 3; i++){
		System.out.println("");
	}
	System.out.println("Printing Generalized Wordclusters");

	for (int i = 0; i < AllClusters.size(); i++){
		AllClusters.get(i).calculateDominance(thesaurus);
		AllClusters.get(i).generalizeWords();
	}
	
	for (int i = 0; i < AllClusters.size(); i++){
		System.out.println("Start of Generalized Cluster " +i);
		for (int j = 0; j < AllClusters.get(i).matchingWords.size(); j++){
			System.out.println(AllClusters.get(i).matchingWords.get(j).getBaseform());
		}
		System.out.println("End of Generalized Cluster " +i);
	}
	PhraseStructureList completeList = new PhraseStructureList();
	

	completeList.sortStructures();
	
	for (int i = 0; i < testList.getInputLabels().size(); i++){
		for (int j = 0; j < testList.getInputLabels().get(i).getSentenceArray().size(); j++){
			testList.getInputLabels().get(i).getSentenceArray().get(j).toPhrase(completeList, realiser, nlgFactory);
		}
	}
	
	for (int i = 0; i < 3; i++){
		System.out.println("");
	}
	
	/*System.out.println("Full Phrases to matching Labels:");
    
	for (int i = 0; i < testList.getInputLabels().size(); i++){
		for (int j = 0; j < testList.getInputLabels().get(i).getSentenceArray().size(); j++){
			System.out.println("Label "+i+", Sentence "+j+ " was: "+testList.getInputLabels().get(i).getSentenceArray().get(j).getContentAsString());
			lines.add("Label "+i+", Sentence "+j+ " was: "+testList.getInputLabels().get(i).getSentenceArray().get(j).getContentAsString());
			System.out.println("Possible Phrases:");
			lines.add("Possible Phrases:");
			for (int k = 0; k < testList.getInputLabels().get(i).getSentenceArray().get(j).possiblePhrases.size(); k++){
				System.out.println("Phrase "+k+": "+testList.getInputLabels().get(i).getSentenceArray().get(j).possiblePhrases.get(k).getFullContent());
				lines.add("Phrase "+k+": "+testList.getInputLabels().get(i).getSentenceArray().get(j).possiblePhrases.get(k).getFullContent());
			}
			System.out.println("");
			lines.add("");
		}
	}*/
    List<String> lines = new ArrayList<String>(); 
	ArrayList<String> originalContent = new ArrayList<String>();
	for (int i = 0; i < testList.getInputLabels().size(); i++){
		for (int j = 0; j < testList.getInputLabels().get(i).getSentenceArray().size(); j++){
			originalContent.add(testList.getInputLabels().get(i).getSentenceArray().get(j).getContentAsString());
		}
	}
	ArrayList<PhraseCluster> finalList = testList.createClusters();
	
	// Below, the output is supposed to be realized
	// The print commands are placeholders at this point
	int i = 0;
	System.out.println("Recommended generalized labels:");
	while (i < finalList.size() && !finalList.get(i).isAlternativeCluster()){
		System.out.println("");
		System.out.println("Phrase "+i);
		System.out.println(finalList.get(i).getBuiltPhrase());
		System.out.println("With the corresponding original labels:");
		for (int j = 0; j < finalList.get(i).getLabelAndSentencePositions().size(); j++){
			System.out.println("Label "+finalList.get(i).getLabelAndSentencePositions().get(j).get(0)+", Sentence "+finalList.get(i).getLabelAndSentencePositions().get(j).get(1));
		}
		// Simply prints out the Phrases and matching original labels
		i++;
	}
	System.out.println("");
	System.out.println("----------------------------------------------");
	System.out.println("");
	System.out.println("Full list of possible labels:");
	for (int j = i; j < finalList.size(); j++){
		System.out.println("");
		System.out.println("Label "+finalList.get(j).getWasLabel()+", Sentence "+finalList.get(j).getWasSentence()+" was:");
		System.out.println(originalContent.get(j-i));
		if (finalList.get(j).getAllPhrases().get(0).getNoStructureFound()){
			System.out.println("No applicable PhraseStructure was found.");
		}
		else {
			System.out.println("Possible Phrases:");
			for (int counter = 0; counter < finalList.get(j).getAllPhrases().size(); counter++){
				System.out.println("Phrase "+counter+": "+finalList.get(j).getAllPhrases().get(counter).getFullContent());
			}
		}
	}
	// Prints out the original list of labels with their corresponding phrases
	java.nio.file.Path file = Paths.get("result.txt");
    Files.write(file, lines, Charset.forName("UTF-8"));
	//finalList.phraseCompareAndDecisionFinal(testList);
	//finalList.writeToFile();
	}
}
