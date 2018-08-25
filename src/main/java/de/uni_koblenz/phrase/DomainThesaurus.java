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

	public DomainThesaurus() {
		
		String dtFile = "C:\\Users\\jSnax\\Desktop\\testfile.txt"; 
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        List<String> domainWords = new ArrayList<String>();
		
		try {
        	
        	br = new BufferedReader(new FileReader(dtFile));
			List<String> preferredWords = new ArrayList<String>();
        	while ((line = br.readLine()) != null) {			
			
			String[] phraseElement = line.split(csvSplitBy);
        	//skip first char, which is "["
        	br.skip(1);       		  		
        		
				for(int i = 0; i < phraseElement.length; i++){      			
					
					if(phraseElement[i].contains("]")){
					phraseElement[i].replace("]","");
					preferredWords.add(i, phraseElement[i]);
					}
					else{
					preferredWords.add(i, phraseElement[i]);
					}					
        		}
        	// Übergabe so nicht möglich
			//domainWords.add(preferredWords);   
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
