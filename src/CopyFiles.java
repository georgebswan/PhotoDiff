import java.io.File;

import java.io.IOException;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import aberscan.Photo;


public class CopyFiles extends Thread {
	ComparePane cmpPane;
	String str;
	
	public CopyFiles(String str, ComparePane cmpPane, String typeOfCopy) {
		super(str);
		this.cmpPane = cmpPane;
		this.str = str;
	}
	
	public void run() {
   	
    	//copy out the before photos with the flag set, then do the same for the after photos
		//cmpPane.frame.lPane.print("\nSearching the before folder '" + cmpPane.beforePane.photoDir.getImageDirectory());
    	copyPhotos(cmpPane.beforePane);
    	//cmpPane.frame.lPane.println(" done");
    	
    	//cmpPane.frame.lPane.print("\nSearching the after folder '" + cmpPane.afterPane.photoDir.getImageDirectory());
    	copyPhotos(cmpPane.afterPane);
     	//cmpPane.frame.lPane.println(" done");
    	
    	JOptionPane.showMessageDialog(null, str + " Complete");
    }
	
	
	private void copyPhotos(ImagePane iPane) {
		//first of all, make sure the Export Directory exists
		try {
			FileUtils.forceMkdir(iPane.photoDir.getExportDirectory());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//now make sure the Edit Directory exists
		try {
			FileUtils.forceMkdir(iPane.photoDir.getEditDirectory());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	
	
		// go through each photo.
		for (Photo photo : iPane.photos.getPhotos()){
			//Has this photo been selected for copy to the Export folder?
			if(photo.getCopyFlag() == true) {
				try {
					photo.copyPhotoToExportDir(iPane.photoDir);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}			
		}
		
		//was there an aberscanMappings.txt file? If yes, copy it to
		File txtFile = new File(iPane.photoDir.getImageDirectory() + "\\aberscanPhotoMappings.txt");
        if (txtFile.exists()) {
        	try {
      		  FileUtils.copyFileToDirectory(txtFile, iPane.photoDir.getExportDirectory());
      	    } catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
}
	 
	
	 
