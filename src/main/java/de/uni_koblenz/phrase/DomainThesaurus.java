package de.uni_koblenz.phrase;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DomainThesaurus {

	public DomainThesaurus() {
		
		String csvFile = "[file-source-here]";
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        
        try {
        	
        	br = new BufferedReader(new FileReader(csvFile));
        	//List is placeholder for final type of List to save domainThesaurus preferred words
        	ArrayList<String> preferredWords = new ArrayList<String>();
        	//skip first char, which is "["
        	br.skip(1);
        	while ((line = br.readLine()) != "]") {
        		
        		String[] domainElement = line.split(csvSplitBy);
        		
        		for(int i = 0; i < line.length(); i++) {
        			preferredWords.add(i, domainElement[i]);
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
}
