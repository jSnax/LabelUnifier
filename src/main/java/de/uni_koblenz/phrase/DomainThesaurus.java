package de.uni_koblenz.phrase;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DomainThesaurus {
		List<String> AllWords;   

	public List<String> getAllWords() {
			return AllWords;
		}

		public void setAllWords(List<String> allWords) {
			AllWords = allWords;
		}

	public DomainThesaurus() {
		
		String dtFile = "src/test/resources/Structures_And_Thesaurus/DomainThesaurus.txt"; 
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        List<String> domainWords = new ArrayList<String>();
		
		try {
        	
        	br = new BufferedReader(new FileReader(dtFile));
        	
        	while ((line = br.readLine()) != null) {			
			
			String[] phraseElement = line.split(csvSplitBy);    		  		
        		
				for(int i = 0; i < phraseElement.length; i++){
					if (i != 0){
						domainWords.add(i, (phraseElement[i].substring(1)));
					}
					else {
						domainWords.add(i, phraseElement[i]);
					}
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
		this.AllWords = domainWords;
	}
}
