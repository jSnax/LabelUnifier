package de.uni_koblenz.phrase;

import java.util.ArrayList;
import java.util.List;
//<<<<<<< Updated upstream
//=======
import java.util.regex.Pattern;
//>>>>>>> Stashed changes
import java.util.regex.Matcher;

import de.uni_koblenz.enums.PartOfSpeechTypes;
import de.uni_koblenz.enums.PhraseStructureTypes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class PhraseStructureList {
	List<PhraseStructure> AllStructures;
	
    public PhraseStructureList() throws Exception {
    	
    	String tsvFile = "src/test/resources/Structures_And_Thesaurus/PhraseStructureList.txt";
    	// ToDo: Ultimately, the file needs to be read from the JAR's location. Since we don't have a JAR currently,
    	// the file source used above has to be altered to suit the actual location
    	// Once we have the JAR, refer to the link below for possible fixes
    	// https://stackoverflow.com/questions/3627426/loading-a-file-relative-to-the-executing-jar-file
        //...
    	// iterate new BufferedReader in order to read tsvFile
    	BufferedReader br = null;
        String line = "";
        // split phraseStructureList tsvFile by comma
        String tsvSplitBy = ", ";
        // create new ArrayList of PhraseStructureTypes called tempList, which will be used/filled later
		//List<PhraseStructureTypes> tempList = new ArrayList<PhraseStructureTypes>();
		// create new ArrayList of PhraseStructure called structureList, which will be used/filled later
		List<PhraseStructure> structureList = new ArrayList<PhraseStructure>();
        try {
        	
        	br = new BufferedReader(new FileReader(tsvFile));
        	while ((line = br.readLine()) != null) {
        	//new StringArray that will contain the PhraseStructure elements after splitting them
        	String[] phraseElement = line.split(tsvSplitBy);
        	//only use PhraseStructures if user has confirmed their intended use by choosing Yes ('y')
        	if(phraseElement[0].toCharArray()[0] == 'y') {
        		PhraseStructure structure = new PhraseStructure();
        		List<PhraseStructureTypes> tempList = new ArrayList<PhraseStructureTypes>();
        		// iterate over each phraseElement in order to set the matching phraseStructureType into a list later
        		for(int i = 1; i < phraseElement.length; i++) {
        			String PhraseString = phraseElement[i];
        			// initially set pstypes, will be given a value in row 55
        			PhraseStructureTypes pstypes;
        			try{
        			// set the value of phraseElement[i] which is the String PhaseString as a PhraseStructureType in order to make switching possible
        			pstypes = PhraseStructureTypes.valueOf(PhraseString);
        			}catch (IllegalArgumentException e) {
        				System.out.println(PhraseString);
        	            throw new Exception("The element above is not a proper PhraseStructureType.");
        			}
        			//switch over all possible phraseStructureTypes and add the matching type to tempList
        			switch(pstypes) {
        			case VERB_IMPERATIVE: 
        				tempList.add(PhraseStructureTypes.VERB_IMPERATIVE);
        				break;
        			case VERB_BASE: 
        				tempList.add(PhraseStructureTypes.VERB_BASE);
        				break;	
        			case VERB_PASSIVE: 
        				tempList.add(PhraseStructureTypes.VERB_PASSIVE);
        				break;
        			case VERB_PASSIVE_PAST: 
        				tempList.add(PhraseStructureTypes.VERB_PASSIVE_PAST);
        				break;
        			case VERB_PRESENT_PARTICIPLE: 
        				tempList.add(PhraseStructureTypes.VERB_PRESENT_PARTICIPLE);
        				break;
        			case VERB_SIMPLEPAST: 
        				tempList.add(PhraseStructureTypes.VERB_SIMPLEPAST);
        				break;
        			case NOUN_SINGULAR_OBJECT: 
        				tempList.add(PhraseStructureTypes.NOUN_SINGULAR_OBJECT);
        				break;
        			case NOUN_SINGULAR_SUBJECT: 
        				tempList.add(PhraseStructureTypes.NOUN_SINGULAR_SUBJECT);
        				break;
        			case NOUN_PLURAL_OBJECT: 
        				tempList.add(PhraseStructureTypes.NOUN_PLURAL_OBJECT);
        				break;
        			case NOUN_PLURAL_SUBJECT: 
        				tempList.add(PhraseStructureTypes.NOUN_PLURAL_SUBJECT);
        				break;
        			case DETERMINER_DEFINITEARTICLE: 
        				tempList.add(PhraseStructureTypes.DETERMINER_DEFINITEARTICLE);
        				break;
        			case DETERMINER_INDEFINITEARTICLE: 
        				tempList.add(PhraseStructureTypes.DETERMINER_INDEFINITEARTICLE);
        				break;
        			case PUNCTUATION_PERIOD: 
        				tempList.add(PhraseStructureTypes.PUNCTUATION_PERIOD);
        				break;
        			case PUNCTUATION_QUESTIONMARK: 
        				tempList.add(PhraseStructureTypes.PUNCTUATION_QUESTIONMARK);
        				break;
        			case ADJECTIVE_FOR_OBJECT: 
        				tempList.add(PhraseStructureTypes.ADJECTIVE_FOR_OBJECT);
        				break;
        			case ADJECTIVE_FOR_SUBJECT: 
        				tempList.add(PhraseStructureTypes.ADJECTIVE_FOR_SUBJECT);
        				break;
        			case ADVERB: 
        				tempList.add(PhraseStructureTypes.ADVERB);
        				break;
					case VERB_SIMPLEFUTURE:
        				tempList.add(PhraseStructureTypes.VERB_SIMPLEFUTURE);
						break;
					case PREPOSITION:
        				tempList.add(PhraseStructureTypes.PREPOSITION);
						break;
					case PREPOSITIONAL_OBJECT_SINGULAR:
        				tempList.add(PhraseStructureTypes.PREPOSITIONAL_OBJECT_SINGULAR);
						break;
					case PREPOSITIONAL_OBJECT_PLURAL:
        				tempList.add(PhraseStructureTypes.PREPOSITIONAL_OBJECT_PLURAL);
						break;
					case ADJECTIVE_FOR_PREPOSITIONAL_OBJECT:
        				tempList.add(PhraseStructureTypes.ADJECTIVE_FOR_PREPOSITIONAL_OBJECT);
						break;
					default:
						break;
        		}
        			
        	}
        	// finally set        	
        	structure.setElements(tempList);
        	structureList.add(structure);
        	}
        	}
        	
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        this.AllStructures = structureList;
    }
	
	public List<PhraseStructure> getAllStructures() {
		return AllStructures;
	}

	public void setAllStructures(List<PhraseStructure> allStructures) {
		this.AllStructures = allStructures;
	}
	public void sortStructures (){
		for (int i = 0; i < this.AllStructures.size(); i++) {
			for (int j =i+1; j < this.AllStructures.size() ; j++) {
				if ( this.AllStructures.get(j).getElements().size() > this.AllStructures.get(i).getElements().size()) {
					List<PhraseStructureTypes> k = this.AllStructures.get(i).getElements();
					this.AllStructures.get(i).elements = this.AllStructures.get(j).getElements();
					this.AllStructures.get(j).elements = k;
				}
			}
		}
	

	}
}
