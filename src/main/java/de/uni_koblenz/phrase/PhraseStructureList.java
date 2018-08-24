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
	
    public PhraseStructureList() {
    	
    	String tsvFile = "[file-source-here]";
        BufferedReader br = null;
        String line = "";
        String tsvSplitBy = "\t";
        PhraseStructureTypes pstypes;
        
        try {
        	
        	br = new BufferedReader(new FileReader(tsvFile));
        	while ((line = br.readLine()) != null) {
        	
        	String[] phraseElement = line.split(tsvSplitBy);
        	        	
        	if(phraseElement[0] == "n") {
        		br.skip(line.length());
        	}
        	
        	else if(phraseElement[0] == "y") {
        		PhraseStructure Structure = new PhraseStructure();
        		List<PhraseStructureTypes> tempList = new ArrayList<PhraseStructureTypes>();
        		//Ein Tab == 5 Chars (bzw 5 Leerzeichen)
        		br.skip(5);
        		for(int i = 1; i < line.length(); i++) {
        			String PhraseString = phraseElement[i];
        			// Man kann nicht über einen String switchen...
        			pstypes = Structure.getElements().get(i);
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
        			default: System.out.println("No valid PhraseStructureType");
        		}
        			
        		br.skip(5);
        	}
        	
        	//tempList Ergebnis WO speichern?
        	List<PhraseStructure> tsvlist = new ArrayList<PhraseStructure>();
        	
        		
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
