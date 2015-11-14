import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;

import aberscan.FilePrompterGUI;
import aberscan.Photo;

public class ControlPane extends JPanel {
	static final long serialVersionUID = 2;
	
    JButton afterSelectButton, beforeSelectButton, exportSelectButton, leftSelectButton, rightSelectButton, prevButton;
    JButton editLeftButton, editRightButton, rotateLeftButton, rotateRightButton, rotateVertButton, rotateHorizButton;
    FilePrompterGUI beforeChooser, afterChooser, destChooser;
    MainGUI frame;
    ComparePane cmpPane;
    boolean exportFolderFlag = false;
    String [] fileExtensions = {"jpg", "JPG", "tif", "TIF"};
 
    public ControlPane(MainGUI frame, ComparePane cmpP) {
        super(new BorderLayout());
 
        cmpPane = cmpP;
 
        this.frame = frame;
        //FileNameExtensionFilter filter = new FileNameExtensionFilter("Photos", "jpg", "JPG", "tif", "TIF");	
        
        //Create  the 'export' file chooser
        //destChooser = new JFileChooser();
        //destChooser.addChoosableFileFilter(filter);
        //destChooser.setFileFilter(filter);
        //destChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //destChooser.setMultiSelectionEnabled(false);
        //destChooser.setCurrentDirectory(cmpPane.afterPane.photoDir.getRootDirectory());
        //destChooser.setDialogTitle("Select where to save the exported photos");
        
        //Create the before button. Set it to disabled to start
    	//ImageIcon beforeIcon = new ImageIcon("images/selectbeforeFile.jpg");
        beforeSelectButton = new JButton("Select starting 'before' photo");
        beforeSelectButton.setEnabled(true);
        beforeSelectButton.addActionListener(
        	new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
		        	beforeChooser = new FilePrompterGUI(false, cmpPane.beforePane.photoDir.getRootDirectory(), "Select the 'Before' Photo",  fileExtensions);
		    		if(beforeChooser.isFileSelected() == true) { 
		                //System.out.println("ControlPane (ActionListener): Before dir = " + beforeChooser.getCurrentDirectory() + ", Before photo = " + beforeChooser.getSelectedFile() );
        	    		//set the final copy directory
		    			
		    			cmpPane.beforePane.photoDir.setImageDirectory(beforeChooser.getImageDir());       	    		
        	    		Photo startPhoto = new Photo(beforeChooser.getStartFile());
		                
		                //load photos and display the selected photo
		                cmpPane.loadBeforePhotos(startPhoto);
		                //cmpPane.beforePane.photos.printPhotos();
		        		//Photo photo = cmpPane.beforePane.getFirstPhoto();
		            	//cmpPane.print();
		            	//startPhoto.print();
		            	//System.out.println("Start photo");
		            	//photo.print();
		                
		        		cmpPane.beforePane.setScreenImage(cmpPane.beforePane.getFirstPhoto());
		                afterSelectButton.setEnabled(true);
		            } 
		            else {
		            	JOptionPane.showMessageDialog(null, "Warning: No 'before' photo was selected - please try again");
		            }
        		}
        	}
        );
        
        //Create the after button. Set it to disabled to start
    	//ImageIcon afterIcon = new ImageIcon("images/selectafterFile.jpg");
        afterSelectButton = new JButton("Select starting 'after' photo");
        afterSelectButton.setEnabled(false);
        afterSelectButton.addActionListener(
        	new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
		        	afterChooser = new FilePrompterGUI(false, cmpPane.afterPane.photoDir.getRootDirectory(), "Select the 'After' Photo",  fileExtensions);
		    		if(afterChooser.isFileSelected() == true) { 
		            	//System.out.println("ControlPane (ActionListener): After dir = " + afterChooser.getCurrentDirectory() + ", After photo = " + afterChooser.getSelectedFile() );
        	    		cmpPane.afterPane.photoDir.setImageDirectory(afterChooser.getImageDir());

		                Photo startPhoto = new Photo(afterChooser.getStartFile());
		                
		                //load photos and display the selected photo
		                cmpPane.loadAfterPhotos(startPhoto);
		                cmpPane.afterPane.setScreenImage(cmpPane.afterPane.getFirstPhoto());
		                
		                leftSelectButton.setEnabled(true);
		                rightSelectButton.setEnabled(true);
		                prevButton.setEnabled(true);
		                exportSelectButton.setEnabled(true);
		                editLeftButton.setEnabled(true);
		                editRightButton.setEnabled(true);
		                rotateLeftButton.setEnabled(true);
		                rotateRightButton.setEnabled(true);
		                rotateVertButton.setEnabled(true);
		                rotateHorizButton.setEnabled(true);
		                beforeSelectButton.setEnabled(false);
		                afterSelectButton.setEnabled(false);
		            } 
		            else {
		            	JOptionPane.showMessageDialog(null, "Warning: No 'after' photo was selected - please try again");
		            }
        		}
        	}
        );
        
        //Create the export button. Set it to disabled to start
    	//ImageIcon destIcon = new ImageIcon("images/selectDestFile.jpg");
        exportSelectButton = new JButton("Export selected photos");
        exportSelectButton.setEnabled(false);
        exportSelectButton.addActionListener(
        	new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
        			exportPhotos();
        		}
        	}
        );
 
        //Create the left button. Set it to disabled to start
    	//ImageIcon leftIcon = new ImageIcon("images/left.jpg");
        leftSelectButton = new JButton("     Left    ");
        leftSelectButton.setEnabled(false);
        leftSelectButton.addActionListener(
        	new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
         			// move to next, noting that the copy flag on left is set, and there is nothing to be edited
		        	moveToNextPhoto(true, false, false);
		        	
		        	//note that there is at least one selected photo not yet exported
		        	exportFolderFlag = false;
		        	
	                //saveButton.setEnabled(true);
        		}
        	}
        );
        
        //Create the right button. Set it to disabled to start
    	//ImageIcon rightIcon = new ImageIcon("images/right.jpg");
        rightSelectButton = new JButton("    Right    ");
        rightSelectButton.setEnabled(false);
        rightSelectButton.addActionListener(
        	new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
         			// move to next, noting that the copy flag on right is set, and there is nothing to be edited
		        	moveToNextPhoto(false, false, false);
		        	
		        	//note that there is at least one selected photo not yet exported
		        	exportFolderFlag = false;
		        	
	                //saveButton.setEnabled(true);
        		}
        	}
        );
        
        //Create the prev button. Set it to disabled to start
    	//ImageIcon rightIcon = new ImageIcon("images/previous.jpg");
        prevButton = new JButton("Previous");
        prevButton.setEnabled(false);
        prevButton.addActionListener(
        	new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
		        	moveToPrevPhoto();
		        	
		        	//note that there is at least one selected photo not yet exported
		        	exportFolderFlag = false;
        		}
        	}
        );
        
        //Create the edit left button. Set it to disabled to start
    	//ImageIcon editIcon = new ImageIcon("images/copy.jpg");
        editLeftButton = new JButton("Edit Left");
        editLeftButton.setEnabled(false);
        editLeftButton.addActionListener(
        	new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
         			// move to next, noting that the copy flag on right is set, and there is nothing to be edited
		        	moveToNextPhoto(true, true, false);
		        	
		        	//note that there is at least one selected photo not yet exported
		        	exportFolderFlag = false;
        		}
        	}
        );
        
        //Create the edit right button. Set it to disabled to start
     	//ImageIcon editIcon = new ImageIcon("images/copy.jpg");
         editRightButton = new JButton("Edit Right");
         editRightButton.setEnabled(false);
         editRightButton.addActionListener(
         	new ActionListener() {
         		public void actionPerformed(ActionEvent e) {
         			// move to next, noting that the copy flag on right is set, and there is nothing to be edited
		        	moveToNextPhoto(false, false, true);
		        	
		        	//note that there is at least one selected photo not yet exported
		        	exportFolderFlag = false;
         		}
         	}
         );
        
        //Create the rotateLeft button. Set it to disabled to start
     	//ImageIcon rotateLeftIcon = new ImageIcon("images/rotateLeft.jpg");
        rotateLeftButton = new JButton("Rotate Left");
        rotateLeftButton.setEnabled(false);
        rotateLeftButton.addActionListener(
         	new ActionListener() {
         		public void actionPerformed(ActionEvent e) {
         			rotatePhoto(true);
		        	
		        	//note that there is at least one selected photo not yet exported
		        	exportFolderFlag = false;
         		}
         	}
         );
         
         //Create the rotateRight button. Set it to disabled to start
      	 //ImageIcon rotateRightIcon = new ImageIcon("images/rotateRight.jpg");
         rotateRightButton = new JButton("Rotate Right");
         rotateRightButton.setEnabled(false);
         rotateRightButton.addActionListener(
          	new ActionListener() {
          		public void actionPerformed(ActionEvent e) {
          			rotatePhoto(false);
		        	
		        	//note that there is at least one selected photo not yet exported
		        	exportFolderFlag = false;
          		}
          	}
          );
          
         //Create the rotateVert button. Set it to disabled to start
         //ImageIcon rotateVertIcon = new ImageIcon("images/rotateVert.jpg");
         rotateVertButton = new JButton("Rotate Vert");
         rotateVertButton.setEnabled(false);
         rotateVertButton.addActionListener(
            new ActionListener() {
            	public void actionPerformed(ActionEvent e) {
            		flipPhoto(true);
		        	
		        	//note that there is at least one selected photo not yet exported
		        	exportFolderFlag = false;
            	}
            }
         );
 
         //Create the rotateHoriz button. Set it to disabled to start
         //ImageIcon rotateHorizIcon = new ImageIcon("images/rotateHoriz.jpg");
         rotateHorizButton = new JButton("Rotate Horiz");
         rotateHorizButton.setEnabled(false);
         rotateHorizButton.addActionListener(
            new ActionListener() {
            	public void actionPerformed(ActionEvent e) {
            		flipPhoto(false);
		        	
		        	//note that there is at least one selected photo not yet exported
		        	exportFolderFlag = false;
            	}
            }
         );
 
        
        //For layout purposes, put the buttons in a separate panel
        Box box = Box.createHorizontalBox();
        int fillerWidth = 20;
		
        JToolBar beforeBar = new JToolBar();
		beforeBar.setRollover(true);		
        beforeBar.add(box.createRigidArea(new Dimension(fillerWidth,0)));
        beforeBar.add(beforeSelectButton);
		add(beforeBar, BorderLayout.LINE_START);
		
        JToolBar selectBar = new JToolBar();
		selectBar.setRollover(true);
        selectBar.add(box.createRigidArea(new Dimension(fillerWidth,0)));
        selectBar.add(prevButton);
        //beforeBar.add(Box.createRigidArea(new Dimension((int) frameSize.getWidth()/2 - 260,0)));
        selectBar.add(leftSelectButton);
        selectBar.add(rightSelectButton);
        selectBar.add(editLeftButton);
        selectBar.add(editRightButton);
		add(selectBar, BorderLayout.LINE_START);
        
        JToolBar rotateBar = new JToolBar();
        rotateBar.add(rotateLeftButton);
        rotateBar.add(rotateRightButton);
        rotateBar.add(rotateVertButton);
        rotateBar.add(rotateHorizButton);
		add(rotateBar, BorderLayout.LINE_START);

        JToolBar afterBar = new JToolBar();
		afterBar.setRollover(true);
        afterBar.add(Box.createRigidArea(new Dimension(fillerWidth,0)));
        afterBar.add(afterSelectButton);
		add(afterBar, BorderLayout.LINE_START);
		
		JToolBar destBar = new JToolBar();
		destBar.setRollover(true);
        //destBar.add(saveButton);
		destBar.add(Box.createRigidArea(new Dimension(fillerWidth,0)));
        destBar.add(exportSelectButton);
	    add(afterBar, BorderLayout.LINE_START);

		box.add(beforeBar);
		box.add(selectBar);
		box.add(rotateBar);
		box.add(afterBar);
		box.add(destBar);
		
        //Add the buttons  to this panel.
        add(box, BorderLayout.PAGE_END);
    }
    
    private void copyExportedFiles() {
        Thread copyThread;
        
		copyThread = new CopyFiles("Copy to Export Folder", cmpPane, "final");
		copyThread.start();
    }
    
    private void setUnflaggedPhotos(int ans) {
       //if the ans is 0, then thats 'before'. If 1, then that's 'after'. If 2 then don't copy any
    	boolean atEnd = false;

        //are we already at the end of the photos?
    	if(cmpPane.atEndOfPhotos() == true) {
    		atEnd = true;
    	}
    	
        //start from where we currently are in the list, and go to the end	
		while(atEnd == false) {		
			if(ans == 0) {
				cmpPane.beforePane.getCurPhoto().setCopyFlag(true);
				cmpPane.afterPane.getCurPhoto().setCopyFlag(false);
			}
			else if(ans == 1){
				cmpPane.beforePane.getCurPhoto().setCopyFlag(false);
				cmpPane.afterPane.getCurPhoto().setCopyFlag(true);
			}
			else {
				cmpPane.beforePane.getCurPhoto().setCopyFlag(false);
				cmpPane.afterPane.getCurPhoto().setCopyFlag(false);
			}
			
			//only try and move forward if not already at the end
			if(cmpPane.atEndOfPhotos() != true) {
				cmpPane.beforePane.getNextPhoto();
				cmpPane.afterPane.getNextPhoto();
			}
			else
				atEnd = true;
		}
    }
    
    protected void moveToNextPhoto (boolean beforeCopyFlag, boolean beforeEditFlag, boolean afterEditFlag) {
		
		//set the copy flag on the photo
		cmpPane.beforePane.getCurPhoto().setCopyFlag(beforeCopyFlag);
		cmpPane.afterPane.getCurPhoto().setCopyFlag(!beforeCopyFlag);
		
		//set the edit flags
		cmpPane.beforePane.getCurPhoto().setEditFlag(beforeEditFlag);
		cmpPane.afterPane.getCurPhoto().setEditFlag(afterEditFlag);
		
		//move to the next photo
		if(cmpPane.atEndOfPhotos() == true) {
			JOptionPane.showMessageDialog(frame, "At the end");
		}
		else {	
			//System.out.println("ControlPane(moveToNextPhoto): here for next button");
			Photo photo = cmpPane.beforePane.getNextPhoto();
			cmpPane.beforePane.setScreenImage(photo);
 
            // System.out.println("ControlPane(moveToNextPhoto)::List of before photos");
            //cmpPane.beforePane.photos.printPhotos();
            //System.out.println("ControlPane(moveToNextPhoto)::Current before photo");
            //photo.print();
            
			photo = cmpPane.afterPane.getNextPhoto();

            //System.out.println("ControlPane(moveToNextPhoto)::Current after photo");
            cmpPane.afterPane.setScreenImage(photo);
                  
			frame.setTitle(photo.getName());
		}
	}
    
    protected void moveToPrevPhoto () {
		
		//reset the copy flag on the photo, and move to the prev photo
		cmpPane.beforePane.getCurPhoto().setCopyFlag(false);
		cmpPane.afterPane.getCurPhoto().setCopyFlag(false);
		
		//also reset the edit flag on the photo
		cmpPane.beforePane.getCurPhoto().setEditFlag(false);
		cmpPane.afterPane.getCurPhoto().setEditFlag(false);
		
		if(cmpPane.atStartOfPhotos() == true) {
			JOptionPane.showMessageDialog(frame, "At the beginning");
			//cmpPane.beforePane.photos.printPhotos();
			//cmpPane.afterPane.photos.printPhotos();
		}
		else {
			//System.out.println("ControlPane(moveToPrevPhoto): here for previous button");
			Photo photo = cmpPane.beforePane.getPrevPhoto();
			cmpPane.beforePane.setScreenImage(photo);
			
            //System.out.println("ControlPane(moveToPrevPhoto)::Current before photo");
           // photo.print();
            
			photo = cmpPane.afterPane.getPrevPhoto();
			cmpPane.afterPane.setScreenImage(photo);
			
			frame.setTitle(photo.getName());
		}
	}
    
   
    
	private void rotatePhoto(boolean rotateLeft) {
		try {
			cmpPane.beforePane.getCurPhoto().rotatePhoto(rotateLeft);
			cmpPane.beforePane.photos.resetCurPhoto();
			Photo photo = cmpPane.beforePane.getCurPhoto();
			cmpPane.beforePane.setScreenImage(photo);
			
			cmpPane.afterPane.getCurPhoto().rotatePhoto(rotateLeft);
			cmpPane.afterPane.photos.resetCurPhoto();
			photo = cmpPane.afterPane.getCurPhoto();
			cmpPane.afterPane.setScreenImage(photo);
			
			//this is a hack, because I can't get the rotated image to redraw, but if I go to next, then previous, I do see the proper image
			//I'm just picking the 'after' photo by default, with the assumption that I'm going to go back to 'previous' anyway
			//moveToNextPhoto( false, false, false);
			//moveToPrevPhoto();
		} catch (IOException e1) {
			e1.printStackTrace();
		}	
	}
	
	private void flipPhoto(boolean flipVert) {
		try {
			cmpPane.beforePane.getCurPhoto().flipPhoto(flipVert);
			cmpPane.beforePane.photos.resetCurPhoto();
			Photo photo = cmpPane.beforePane.getCurPhoto();
			cmpPane.beforePane.setScreenImage(photo);
			
			cmpPane.afterPane.getCurPhoto().flipPhoto(flipVert);
			cmpPane.afterPane.photos.resetCurPhoto();
			photo = cmpPane.afterPane.getCurPhoto();
			cmpPane.afterPane.setScreenImage(photo);
			//frame.photoList.resetCurPhoto();
			//((ImagePane) frame.iPane).setScreenImage(frame.photoList.getPhoto());
			
			//this is a hack, because I can't get the flipped image to redraw, but if I go to next, then previous, I do see the proper image
			//moveToNextPhoto(false, false, false);
			//moveToPrevPhoto();
		} catch (IOException e1) {
			e1.printStackTrace();
		}	
	}
	
	public void exportPhotos() {
		int returnVal;
		int ans;
		int bIndex;
		int aIndex;
    	Object[] options = {"Copy from the 'before' folder",
                "Copy from the 'after' folder",
                "Don't copy any unselected photos"};
    	
    	//first, check to see if this is the first time button selected. If yes, throw up the fileChooser. If not
    	//then just proceed with the copy
    	if(exportFolderFlag == false) {
	    	exportFolderFlag = true;
	    	destChooser = new FilePrompterGUI(true, cmpPane.afterPane.photoDir.getRootDirectory(), "Export to Selected Folder",  fileExtensions);
    		if(destChooser.isFileSelected() == true) { 
	    	//returnVal = destChooser.showDialog(ControlPane.this, "Export to selected folder");
	    	//if (returnVal == JFileChooser.APPROVE_OPTION) {
	    		//System.out.println("destDir = " + destChooser.getCurrentDirectory());
        	
	    		//check what to do with unflagged photos (i.e. only some photos were diffed, so there is no selection on the remaining photos
	    		ans = JOptionPane.showOptionDialog(null,
        			"What would you like to do with the photos not yet selected?",
        			"Option Dialog Box", JOptionPane.YES_NO_CANCEL_OPTION,
        			JOptionPane.QUESTION_MESSAGE,
        			null,
        			options,
        			options[1]);
	    		//armed with the answer, go set the flags in the remaining photos so that copy can proceed
	    		//mark where we are, cus the setUnflaggedPhotos destroys that
	    		bIndex  = cmpPane.beforePane.getCurIndex();
	    		aIndex  = cmpPane.afterPane.getCurIndex();
	    		
	    		setUnflaggedPhotos(ans);
	    		
	    		cmpPane.beforePane.setCurIndex(bIndex);
	    		cmpPane.afterPane.setCurIndex(aIndex);
	    		
        	
	    		//set the final copy directory
	    		cmpPane.beforePane.photoDir.setExportDirectory(destChooser.getStartFile());
	    		cmpPane.afterPane.photoDir.setExportDirectory(destChooser.getStartFile());
	    		
	    		//set the final edit directory to be a subdirectory of the copy dir
	    		cmpPane.beforePane.photoDir.setEditDirectory(new File(destChooser.getStartFile().getAbsolutePath() + "\\ToBeEdited"));
	    		cmpPane.afterPane.photoDir.setEditDirectory(new File(destChooser.getStartFile().getAbsolutePath() + "\\ToBeEdited"));
            
	    		//copy the selected files into the export Directory
	    		copyExportedFiles();
	    	}
	    	else {
	        	JOptionPane.showMessageDialog(null, "Warning: No export folder was selected - please try again");
	        }
    	}
	    else {
	    	//here if we have been here before
	    	//check what to do with unflagged photos (i.e. only some photos were diffed, so there is no selection on the remaining photos
	    	ans = JOptionPane.showOptionDialog(null,
        			"What would you like to do with the photos not yet selected?",
        			"Option Dialog Box", JOptionPane.YES_NO_CANCEL_OPTION,
        			JOptionPane.QUESTION_MESSAGE,
        			null,
        			options,
        			options[1]);
	    	//armed with the answer, go set the flags in the remaining photos so that copy can proceed
	    	//mark where we are, cus the setUnflaggedPhotos destroys that
	    	bIndex  = cmpPane.beforePane.getCurIndex();
	    	aIndex  = cmpPane.afterPane.getCurIndex();
	    		
	    	setUnflaggedPhotos(ans);
	    		
	    	cmpPane.beforePane.setCurIndex(bIndex);
	    	cmpPane.afterPane.setCurIndex(aIndex);
           
	    	//copy the selected files into the export Directory
	    	copyExportedFiles();
        }         
	}
}
