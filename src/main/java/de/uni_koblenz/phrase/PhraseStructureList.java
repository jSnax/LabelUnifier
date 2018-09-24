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
	
    public PhraseStructureList(/*String url*/) throws Exception {							//	-> this line will be needed in productive mode
    	
    	String tsvFile = "src/test/resources/Structures_And_Thesaurus/PhraseStructureList.txt";
    	//String tsvFile = url + "\\PhraseStructureList.txt";									-> this line will be needed in productive mode
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
        			case VI: 
        				tempList.add(PhraseStructureTypes.VI);
        				break;
        			case VB: 
        				tempList.add(PhraseStructureTypes.VB);
        				break;	
        			case VP: 
        				tempList.add(PhraseStructureTypes.VP);
        				break;
        			case VPP: 
        				tempList.add(PhraseStructureTypes.VPP);
        				break;
        			case VPrP: 
        				tempList.add(PhraseStructureTypes.VPrP);
        				break;
        			case VSP: 
        				tempList.add(PhraseStructureTypes.VSP);
        				break;
        			case NSO: 
        				tempList.add(PhraseStructureTypes.NSO);
        				break;
        			case NSS: 
        				tempList.add(PhraseStructureTypes.NSS);
        				break;
        			case NPO: 
        				tempList.add(PhraseStructureTypes.NPO);
        				break;
        			case NPS: 
        				tempList.add(PhraseStructureTypes.NPS);
        				break;
        			case DetDef: 
        				tempList.add(PhraseStructureTypes.DetDef);
        				break;
        			case DetIndef: 
        				tempList.add(PhraseStructureTypes.DetIndef);
        				break;
        			case PP: 
        				tempList.add(PhraseStructureTypes.PP);
        				break;
        			case PQ: 
        				tempList.add(PhraseStructureTypes.PQ);
        				break;
        			case AO: 
        				tempList.add(PhraseStructureTypes.AO);
        				break;
        			case AS: 
        				tempList.add(PhraseStructureTypes.AS);
        				break;
        			case ADV: 
        				tempList.add(PhraseStructureTypes.ADV);
        				break;
					case VSF:
        				tempList.add(PhraseStructureTypes.VSF);
						break;
					case Prep:
        				tempList.add(PhraseStructureTypes.Prep);
						break;
					case POS:
        				tempList.add(PhraseStructureTypes.POS);
						break;
					case POP:
        				tempList.add(PhraseStructureTypes.POP);
						break;
					case APO:
        				tempList.add(PhraseStructureTypes.APO);
						break;
					default:
						break;
        		}
        			
        	}
        	// finally set all elements from templist into the PhraseStructure structure
        	// then set the full PhraseStructure structure into the list of PhraseStructures structureList
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
				if ( this.AllStructures.get(j).getTrueSize() > this.AllStructures.get(i).getTrueSize()) {
					List<PhraseStructureTypes> k = this.AllStructures.get(i).getElements();
					this.AllStructures.get(i).elements = this.AllStructures.get(j).getElements();
					this.AllStructures.get(j).elements = k;
				}
			}
		}
	

	}
}
