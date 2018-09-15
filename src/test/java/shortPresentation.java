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
	/*PhraseStructure Structure = new PhraseStructure();
	PhraseStructure Structure2 = new PhraseStructure();
	PhraseStructure Structure3 = new PhraseStructure();
	PhraseStructure Structure4 = new PhraseStructure();
	PhraseStructure Structure5 = new PhraseStructure();
	List<PhraseStructure> allStructures = new ArrayList<PhraseStructure>(); 
	List<PhraseStructureTypes> tempList = new ArrayList<PhraseStructureTypes>();
	List<PhraseStructureTypes> tempList2 = new ArrayList<PhraseStructureTypes>();
	List<PhraseStructureTypes> tempList3 = new ArrayList<PhraseStructureTypes>();
	List<PhraseStructureTypes> tempList4 = new ArrayList<PhraseStructureTypes>();
	List<PhraseStructureTypes> tempList5 = new ArrayList<PhraseStructureTypes>();
	tempList.add(PhraseStructureTypes.NOUN_SINGULAR_SUBJECT);
	tempList.add(PhraseStructureTypes.VERB_SIMPLEFUTURE);
	tempList.add(PhraseStructureTypes.NOUN_SINGULAR_OBJECT);
	tempList.add(PhraseStructureTypes.ADJECTIVE_FOR_OBJECT);
	Structure.setElements(tempList);
	tempList2.add(PhraseStructureTypes.NOUN_PLURAL_SUBJECT);
	tempList2.add(PhraseStructureTypes.VERB_SIMPLEPAST);
	tempList2.add(PhraseStructureTypes.NOUN_PLURAL_OBJECT);
	Structure2.setElements(tempList2);
	tempList3.add(PhraseStructureTypes.NOUN_SINGULAR_SUBJECT);
	//tempList3.add(PhraseStructureTypes.ADJECTIVE_FOR_SUBJECT);
	tempList3.add(PhraseStructureTypes.VERB_SIMPLEPAST);
	tempList3.add(PhraseStructureTypes.NOUN_SINGULAR_OBJECT);
	tempList3.add(PhraseStructureTypes.ADJECTIVE_FOR_OBJECT);
	tempList3.add(PhraseStructureTypes.PREPOSITION);
	tempList3.add(PhraseStructureTypes.PREPOSITIONAL_OBJECT_PLURAL);
	tempList3.add(PhraseStructureTypes.ADJECTIVE_FOR_PREPOSITIONAL_OBJECT);
	tempList3.add(PhraseStructureTypes.ADVERB);
	Structure3.setElements(tempList3);
	tempList4.add(PhraseStructureTypes.NOUN_SINGULAR_SUBJECT);
	tempList4.add(PhraseStructureTypes.VERB_SIMPLEPAST);
	tempList4.add(PhraseStructureTypes.NOUN_SINGULAR_OBJECT);
	tempList4.add(PhraseStructureTypes.PREPOSITION);
	tempList4.add(PhraseStructureTypes.PREPOSITIONAL_OBJECT_PLURAL);
	Structure4.setElements(tempList4);
	tempList5.add(PhraseStructureTypes.VERB_PASSIVE_PAST);
	tempList5.add(PhraseStructureTypes.NOUN_SINGULAR_SUBJECT);
	tempList5.add(PhraseStructureTypes.PUNCTUATION_QUESTIONMARK);
	Structure5.setElements(tempList5);
	allStructures.add(Structure);
	allStructures.add(Structure2);
	allStructures.add(Structure3);
	allStructures.add(Structure4);
	allStructures.add(Structure5);
	completeList.setAllStructures(allStructures);*/
	ArrayList<ArrayList<Phrase>> PhraseListList = new ArrayList<ArrayList<Phrase>>();

	completeList.sortStructures();
	
	//testList.getInputLabels().get(4).getSentenceArray().get(0).getWordsarray().get(4).setRole(RoleLeopold.SUBJECT);
	//testList.getInputLabels().get(4).getSentenceArray().get(0).getWordsarray().get(0).setRole(RoleLeopold.BUSINESS_OBJECT);
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
    List<String> lines = new ArrayList<String>(); 
    
	for (int i = 0; i < testList.getInputLabels().size(); i++){
		for (int j = 0; j < testList.getInputLabels().get(i).getSentenceArray().size(); j++){
			System.out.println("Label "+i+" was: "+testList.getInputLabels().get(i).getSentenceArray().get(j).getContentAsString());
			lines.add("Label "+i+" was: "+testList.getInputLabels().get(i).getSentenceArray().get(j).getContentAsString());
			System.out.println("Possible Phrases:");
			lines.add("Possible Phrases:");
			for (int k = 0; k < PhraseListList.get(i).size(); k++){
				System.out.println("Phrase "+k+": "+PhraseListList.get(i).get(k).getFullContent());
				lines.add("Phrase "+k+": "+PhraseListList.get(i).get(k).getFullContent());
			}
			System.out.println("");
			lines.add("");
		}
	}
	java.nio.file.Path file = Paths.get("result.txt");
    Files.write(file, lines, Charset.forName("UTF-8"));
	System.out.println("---------------------");
	PhraseList demoPhrasenCompare = new PhraseList();
	demoPhrasenCompare.setWholeInput(PhraseListList);
	demoPhrasenCompare.phraseCompareAndDecisionFinal(testList);
	demoPhrasenCompare.writeToFile();
	}
}
