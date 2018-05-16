package de.uni_koblenz.ppbks18;

import java.io.IOException;

import de.uni_koblenz.label.Label;
import net.sf.extjwnl.JWNLException;


public class MainPP {
	
	public static void main (String args[]) throws JWNLException, ClassNotFoundException, IOException {
	
		Label l1 = new Label();
		Label l2 = new Label();
		Label l3 = new Label();
		
		l1.tagLabel("checking invoice");
		l2.tagLabel("invoice checked");
		l3.tagLabel("This is a really short sentence");

	}
}
