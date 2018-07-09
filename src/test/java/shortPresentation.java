import java.io.IOException;


import java.util.*;

import de.uni_koblenz.cluster.*;
import de.uni_koblenz.enums.*;
import de.uni_koblenz.label.*;
import de.uni_koblenz.phrase.*;
import de.uni_koblenz.phrase.PhraseStructure;
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

import de.uni_koblenz.label.Label;
import de.uni_koblenz.label.LabelList;
import net.sf.extjwnl.JWNLException;
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
	public static void main (String args[]) throws JWNLException, ClassNotFoundException, IOException {
		
	System.out.println("Started.");
	Lexicon lexicon = Lexicon.getDefaultLexicon();
	NLGFactory nlgFactory = new NLGFactory(lexicon);
	SPhraseSpec p = nlgFactory.createClause();
    Realiser realiser = new Realiser(lexicon);
	Dictionary dictionary = Dictionary.getDefaultResourceInstance();
	
	String[] input=new String[] {
			"Employee checked beautiful invoice",
			"Employee verifies small invoice",
			"Employees check bills",
			"Company pays bill",
			"Bill is payed by company",
			"Verify bill"
			
	};
	

	// Fill Labels with word arrays
	LabelList testList = new LabelList(input);
	
	System.out.println("Printing Preprocessing results:");
	System.out.println(testList);
	
	// Set Input Labels to previously created Label list
	System.out.println("Printing Labels:");
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
	}
	testList.findSynsets();
	System.out.println("Printing Synsets:");
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
	}
	
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
		AllClusters.get(i).calculateDominance();
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
	PhraseStructure Structure = new PhraseStructure();
	PhraseStructure Structure2 = new PhraseStructure();
	PhraseStructure Structure3 = new PhraseStructure();
	List<PhraseStructure> allStructures = new ArrayList<PhraseStructure>(); 
	List<PhraseStructureTypes> tempList = new ArrayList<PhraseStructureTypes>();
	List<PhraseStructureTypes> tempList2 = new ArrayList<PhraseStructureTypes>();
	List<PhraseStructureTypes> tempList3 = new ArrayList<PhraseStructureTypes>();
	tempList.add(PhraseStructureTypes.NOUN_SINGULAR_SUBJECT);
	tempList.add(PhraseStructureTypes.VERB_SIMPLEFUTURE);
	tempList.add(PhraseStructureTypes.NOUN_SINGULAR_OBJECT);
	Structure.setElements(tempList);
	tempList2.add(PhraseStructureTypes.NOUN_PLURAL_SUBJECT);
	tempList2.add(PhraseStructureTypes.VERB_SIMPLEPAST);
	tempList2.add(PhraseStructureTypes.NOUN_PLURAL_OBJECT);
	Structure2.setElements(tempList2);
	tempList3.add(PhraseStructureTypes.VERB_IMPERATIVE);
	tempList3.add(PhraseStructureTypes.NOUN_SINGULAR_OBJECT);
	Structure3.setElements(tempList3);
	allStructures.add(Structure);
	allStructures.add(Structure2);
	allStructures.add(Structure3);
	completeList.setAllStructures(allStructures);
	ArrayList<ArrayList<Phrase>> PhraseListList = new ArrayList<ArrayList<Phrase>>();

	testList.getInputLabels().get(4).getSentenceArray().get(0).getWordsarray().get(4).setRole(RoleLeopold.SUBJECT);
	testList.getInputLabels().get(4).getSentenceArray().get(0).getWordsarray().get(0).setRole(RoleLeopold.BUSINESS_OBJECT);
	for (int i = 0; i < testList.getInputLabels().size(); i++){
		for (int j = 0; j < testList.getInputLabels().get(i).getSentenceArray().size(); j++){
			ArrayList<Phrase> tempPhrase = testList.getInputLabels().get(i).getSentenceArray().get(j).toPhrase(completeList, realiser, nlgFactory);
			PhraseListList.add(tempPhrase);
		}
	}
	
	for (int i = 0; i < 3; i++){
		System.out.println("");
	}
	
	System.out.println("Full Phrases to matching Labels:");
	for (int i = 0; i < testList.getInputLabels().size(); i++){
		for (int j = 0; j < testList.getInputLabels().get(i).getSentenceArray().size(); j++){
			System.out.println("Label "+i+" was: "+testList.getInputLabels().get(i).getSentenceArray().get(j).getContentAsString());
			System.out.println("Possible Phrases:");
			for (int k = 0; k < PhraseListList.get(i).size(); k++){
				System.out.println("Phrase "+k+": "+PhraseListList.get(i).get(k).getFullContent());
			}
			System.out.println("");
		}
	}
	}
}
