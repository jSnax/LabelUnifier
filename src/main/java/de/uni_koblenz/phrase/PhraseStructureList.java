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
        
        try {
        	
        	br = new BufferedReader(new FileReader(tsvFile));
        	while ((line = br.readLine()) != null) {
        	String[] phraseElement = line.split(tsvSplitBy);
        	
        	
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
