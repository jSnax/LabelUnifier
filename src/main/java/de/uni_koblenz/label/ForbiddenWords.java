package de.uni_koblenz.label;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.extjwnl.data.POS;

public class ForbiddenWords {
	List<String> AllWords;   

	public List<String> getAllWords() {
			return AllWords;
		}

		public void setAllWords(List<String> allWords) {
			AllWords = allWords;
		}

	public ForbiddenWords() {
		
		String dtFile = "src/test/resources/forbiddenWords.txt"; 
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
	
	public boolean isForbiddenWord(Word comparingWord){
		int i = 0;
		boolean forbidden = false;
		if (comparingWord.getPartOfSpeech().getJwnlType() != POS.VERB){
			return (forbidden);
		}
		while (i < this.getAllWords().size() && !forbidden){
			if (this.getAllWords().get(i).equals(comparingWord.getBaseform())){
				forbidden = true;
			}
			i++;
		}
		return forbidden;
	}
	
	public boolean isForbiddenString(String comparingWord){
		int i = 0;
		boolean forbidden = false;
		while (i < this.getAllWords().size() && !forbidden){
			if (this.getAllWords().get(i).equals(comparingWord)){
				forbidden = true;
			}
			i++;
		}
		return forbidden;
	}
}
