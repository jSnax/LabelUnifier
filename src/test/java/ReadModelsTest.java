import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ReadModelsTest {
	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("Algorithmus gestarted");
		ReadModels Reader = new ReadModels() ;
		ArrayList <String> IDs = new ArrayList <String> ();
		ArrayList <String> Labelliste = new ArrayList <String> ();
		File Model = new File("birthCertificate_p31");
		// not done yet
		IDs= Reader.GetIDs(Model);
		Labelliste = Reader.ReadLabels(Model);
		System.out.println(IDs);
		System.out.println(Labelliste);
		System.out.println("Algorithmus beendet");
	}
}	


