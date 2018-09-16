package de.uni_koblenz.phrase;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.uni_koblenz.label.*;
import de.uni_koblenz.enums.*;


public class Phrase {

	private List<String> separatedContent;
	private String fullContent;
	private Word[] wordsArray;
	private int usedStructure;
	private int usedStructureSize;
	private boolean noStructureFound;

	public Phrase() {
		
	}
	
	
	public List<String> getseparatedContent() {
		return separatedContent;
	}


	public void setseparatedContent(List<String> separatedContent) {
		this.separatedContent = separatedContent;
	}


	public Word[] getWordsArray() {
		return wordsArray;
	}
	
	public void setWordsArray(Word[] wordsArray) {
		this.wordsArray = wordsArray;
	}


	public int getUsedStructure() {
		return usedStructure;
	}


	public void setUsedStructure(int usedStructure) {
		this.usedStructure = usedStructure;
	}
	

	public String getFullContent() {
		return fullContent;
	}


	public void setFullContent(String fullContent) {
		this.fullContent = fullContent;
	}


	public int getUsedStructureSize() {
		return usedStructureSize;
	}


	public void setUsedStructureSize(int usedStructureSize) {
		this.usedStructureSize = usedStructureSize;
	}


	public boolean getNoStructureFound() {
		return noStructureFound;
	}


	public void setNoStructureFound(boolean noStructureFound) {
		this.noStructureFound = noStructureFound;
	}
}

