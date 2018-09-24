package de.uni_koblenz.label;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.extjwnl.data.POS;

//right now, all forbiddenWords are common verbs like "be", "will" or "can"
public class ForbiddenWords {
	List<String> AllWords;   

	public List<String> getAllWords() {
			return AllWords;
		}

		public void setAllWords(List<String> allWords) {
			AllWords = allWords;
		}

	public ForbiddenWords(/*String url*/) {					//	-> this line will be needed in productive mode
		
		// read forbiddenWords from file dtFile at user-specified source
		String dtFile = "src/test/resources/forbiddenWords.txt";
    	//String dtFile = url + "\\PhraseStructureList.txt";	-> this line will be needed in productive mode
        // create BufferedReader to read dtFile
		BufferedReader br = null;
        String line = "";
        // split dtFile by comma
        String csvSplitBy = ",";
        // create a new ArrayList<String> in order to insert the split words from dtFile
        List<String> domainWords = new ArrayList<String>();
		
		try {
        	
        	br = new BufferedReader(new FileReader(dtFile));
        	
        	while ((line = br.readLine()) != null) {			
			// create StringArray phraseElement and fill with forbidden words from source file split by comma
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
		//finally, set the array of words split from the user's ForbiddenWords and add to domainWords as the List AllWords
		this.AllWords = domainWords;
	}
	// compare a Word to the list of Forbidden Words
	public boolean isForbiddenWord(Word comparingWord){
		int i = 0;
		boolean forbidden = false;
		//return false for isForbiddenWord if the Word is not a Verb
		if (comparingWord.getPartOfSpeech().getJwnlType() != POS.VERB){
			return (forbidden);
		}
		//else iterate over allWords and if the baseform of the Word comparingWord matches a forbidden Word, set forbidden as true
		while (i < this.getAllWords().size() && !forbidden){
			if (this.getAllWords().get(i).equals(comparingWord.getBaseform())){
				forbidden = true;
			}
			i++;
		}
		return forbidden;
	}
	// compare a String to the list of Forbidden Words
	public boolean isForbiddenString(String comparingWord){
		int i = 0;
		boolean forbidden = false;
		// iterate over allWords and if the String comparingWord matches a forbidden Word, set forbidden as true
		while (i < this.getAllWords().size() && !forbidden){
			if (this.getAllWords().get(i).equals(comparingWord)){
				forbidden = true;
			}
			i++;
		}
		return forbidden;
	}
}
