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
    Realiser realiser = new Realiser(lexicon);

	// Fill Labels with word arrays
	
	inputList input = new inputList();
	
	LabelList testList = new LabelList(input.getInput());
	
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
	
    List<String> lines = new ArrayList<String>(); 
	ArrayList<String> originalContent = new ArrayList<String>();
	for (int i = 0; i < testList.getInputLabels().size(); i++){
		for (int j = 0; j < testList.getInputLabels().get(i).getSentenceArray().size(); j++){
			originalContent.add(testList.getInputLabels().get(i).getSentenceArray().get(j).getContentAsString());
		}
	}
	ArrayList<PhraseCluster> finalList = testList.createClusters();
	
	// Below, the output is realized
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
		// Simply prints out the Phrases and matching original labels
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
	// Prints out the original list of labels with their corresponding phrases
	java.nio.file.Path file = Paths.get("result.txt");
    Files.write(file, lines, Charset.forName("UTF-8"));
	}
}
