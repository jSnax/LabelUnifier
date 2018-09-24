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
	
	System.out.println("Full Phrases to matching Labels:");
    List<String> lines = new ArrayList<String>(); 
    
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
	}
	
	ArrayList<PhraseCluster> finalList = testList.createClusters();
	java.nio.file.Path file = Paths.get("result.txt");
    Files.write(file, lines, Charset.forName("UTF-8"));
	System.out.println("---------------------");
	//finalList.phraseCompareAndDecisionFinal(testList);
	//finalList.writeToFile();
	}
}
