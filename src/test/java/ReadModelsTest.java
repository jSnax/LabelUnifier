
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReadModelsTest {
	
		@Test
		public void GoldStandardTest() throws FileNotFoundException{
			
			ReadModels Reader = new ReadModels() ;
			ArrayList <String> ComparedIDs = new ArrayList <String> ();
			ArrayList <String> labels = new ArrayList <String> ();
			
			File gold = new File("src/test/resources/dataset2/goldstandard");
			File[] fileListgold = gold.listFiles();
			
			for(File f : fileListgold) {
				File goldmodel = new File(f.toURI());
				ComparedIDs = Reader.GetComparedIDs(goldmodel);
				labels = Reader.GetLabelName(ComparedIDs);
				
				// NOT DONE YET
				// The list has to be processed by the algorithm

				for(int i=0; i< ComparedIDs.size(); i+=2) {
					assertEquals(labels.get(i).trim(), labels.get(i+1).trim());
				}	
			} 
		}
		
		public static void main(String[] args) throws FileNotFoundException {
			System.out.println("Algorithmus gestarted");
			
			ReadModels Reader = new ReadModels() ;
			
			ArrayList <String> ComparedIDs = new ArrayList <String> ();
			ArrayList <String> Labellist = new ArrayList <String> ();
			
			File gold = new File("src/test/resources/dataset2/goldstandard");
			File[] fileListgold = gold.listFiles();
			
			for(File f : fileListgold) {
				File goldmodel = new File(f.toURI());
				ComparedIDs = Reader.GetComparedIDs(goldmodel);
				Labellist = Reader.GetLabelName(ComparedIDs);
			}
			System.out.println(Labellist);

			System.out.println("Algorithmus beendet");
		}
}

