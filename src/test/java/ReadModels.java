import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadModels {
	public ArrayList <String> GetIDs (File y) throws FileNotFoundException {
		Scanner sc;
		ArrayList <String> ID = new ArrayList <String>();
		if(y.exists()) {
			 sc = new Scanner(y);
			while (sc.hasNextLine()) {
				if (sc.hasNext("<place") || sc.hasNext("<transition") ) {
					sc.next();
					ID.add(sc.next());}	
				else 
					sc.nextLine();
			}
		}
		return ID ;
	}
	
	public ArrayList <String> ReadLabels (File y) throws FileNotFoundException{
		Scanner sc;
		ArrayList <String> LabelList = new ArrayList <String>();
		if(y.exists()) {
			 sc = new Scanner(y);
			while (sc.hasNextLine()) {
				if (sc.hasNext("<place") || sc.hasNext("<transition")) {
					sc.nextLine();
					sc.nextLine();
						LabelList.add(sc.nextLine().replace("<text>", "").replace("</text>", ""));}
					else 
						sc.nextLine();
			}}
		
		return LabelList;
	}

}