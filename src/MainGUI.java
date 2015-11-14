import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class MainGUI extends JFrame{
	static final long serialVersionUID = 1;
	ComparePane iPane;
	ControlPane cPane;
	
	MainGUI() {
        //Set up the main frame 
        super("Photo Compare Tool");
        setSize(1920, 1040);
        
       //Set up the panel
        JPanel pane = new JPanel();
        pane.setLayout(new BorderLayout());
	
	    //Add the 'compare' image panel
        iPane = new ComparePane(this);
        pane.add("Center", iPane);
        
	    //Add the control panel
        cPane = new ControlPane(this, iPane);
        pane.add("South", cPane);
        
        //Add the log pane
        //lPane = new LogPane(this);
        //pane.add("Center", lPane);
        
        setContentPane(pane);
	}

	public static void main(String[] args) {
    	// Show the mapping GUI
    	final MainGUI frame = new MainGUI();
    	
		//ExitWindow exit = new ExitWindow();
        frame.addWindowListener(new WindowAdapter() {

        	public void windowClosing(WindowEvent e) {
				// check to see if you want to create the destination folder before existing
        		if(frame.cPane.exportFolderFlag == false) {
        			if(JOptionPane.showConfirmDialog(null, "Question : Before exiting, do you want to export selected photos?" , "GBS" , JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) { 
        				frame.cPane.exportPhotos();
    				}
        		}
        		
        		//if(vFrame.getIPane().getCopyFlag() == true) {
        			//if(JOptionPane.showConfirmDialog(null, "Question : Before exiting, do you want to copy selected photos?" , "GBS" , JOptionPane.YES_NO_OPTION) == 0) { 
        				//frame.getPhotoList().copyPhotos(frame ); 
        				//}
        			//}
        		//}
        		System.exit(0);
        	}
    	});
        frame.setVisible(true);
	}
}
