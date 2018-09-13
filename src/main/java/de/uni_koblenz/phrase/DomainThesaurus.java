package de.uni_koblenz.phrase;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DomainThesaurus {
	// create empty list AllWords which will be filled with the DomainThesaurus words later
	List<String> AllWords;   

	public List<String> getAllWords() {
			return AllWords;
		}

		public void setAllWords(List<String> allWords) {
			AllWords = allWords;
		}

	public DomainThesaurus() {
		
		// read from custom file DomainThesaurus those words which override their respective synonyms (like "check" vs verify or "invoice" vs bill)
		// user needs to change the file address to his own local address of course
		String dtFile = "src/test/resources/Structures_And_Thesaurus/DomainThesaurus.txt"; 
        // iterate a BufferedReader to read dtFile
		BufferedReader br = null;
        String line = "";
        // split the words by using commas between words
        String csvSplitBy = ",";
        // create a new ArrayList<String> in order to insert the split words from dtFile
        List<String> domainWords = new ArrayList<String>();
		
		try {
        	
        	br = new BufferedReader(new FileReader(dtFile));
        	
        	while ((line = br.readLine()) != null) {			
        	//new StringArray that will contain the DomainThesaurus elements after splitting them
			String[] phraseElement = line.split(csvSplitBy);    		  		
        		
				for(int i = 0; i < phraseElement.length; i++){
					if (i != 0){
						//removes the comma from a word after the split so it's not inserted into phraseElement as ",word"
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
		//finally, set the array of words split from the user's DomainThesaurus and add to domainWords as the List AllWords
		this.AllWords = domainWords;
	}
}
