import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Stroke;
import javax.swing.JPanel;
import aberscan.Photo;
	
public class ComparePane extends JPanel {
	static final long serialVersionUID = 4;
    ImagePane beforePane, afterPane;
    MainGUI frame;
    
    public ComparePane(MainGUI frame) {
        super(new BorderLayout());
        
        this.frame = frame;
        Dimension frameSize = frame.getSize();
        int buffer = 80;
        
        JPanel pane = new JPanel();
        pane.setLayout(new BorderLayout());

        //add the before Image Pane
        beforePane = new ImagePane(this);
        beforePane.setImageAreaSize(new Rectangle(0, 0, (int) frameSize.getWidth()/2, (int) frameSize.getHeight() - buffer));
        pane.add("West", beforePane);
        
        //add the after Image Pane
        afterPane = new ImagePane(this);
        afterPane.setImageAreaSize(new Rectangle((int) frameSize.getWidth()/2, 0, (int) frameSize.getWidth()/2, (int) frameSize.getHeight() - buffer));
        pane.add("East", afterPane);
    }
    
    public void loadBeforePhotos(Photo startPhoto) { 
    	beforePane.loadPhotos();
    	beforePane.preloadImages("gbs-before");			//run as a background thread
    	beforePane.setCopyFlags(false);		//assume we are taking none of the before photos
    	beforePane.setStartPhoto(startPhoto);
    }
    
    public void loadAfterPhotos(Photo startPhoto) { 
    	afterPane.loadPhotos();
    	afterPane.preloadImages("gbs-after");			//run as a background thread
    	afterPane.setCopyFlags(false);		//assume that we are none of the after photos
    	afterPane.setStartPhoto(startPhoto);
    }
    
    public boolean atEndOfPhotos() { 
    	//have we reached the end of either photoList
    	if(beforePane.endOfPhotos() == true || afterPane.endOfPhotos() == true)
    		return (true);
    	else
    		return(false);
    }
    
    public boolean atStartOfPhotos() { 
    	//have we reached the end of either photoList
    	if(beforePane.startOfPhotos() == true || afterPane.startOfPhotos() == true)
    		return (true);
    	else
    		return(false);
    }
    
    public void paintComponent(Graphics g){
	    super.paintComponent(g);
	    Rectangle imageRect;
	    Image image;
    
	    //draw the before bounding box
	    drawRectangle(g, beforePane.getImageAreaSize());
	    
	    //draw the before image if one exists yet
	    if(beforePane.photos.getNumPhotos() != 0) {
		    image = beforePane.photos.getPhoto().getImage();
		    if (image != null) { 
		    	imageRect = beforePane.photos.getPhoto().getAdjustedImageRect();
		        g.drawImage(image, (int) imageRect.getX(), 
		        				   (int) imageRect.getY(), 
		        				   (int) imageRect.getWidth(), 
		        				   (int) imageRect.getHeight(), 
		        				   this); 
		    }
	    }
	    
	    //draw the after border
	    drawRectangle(g, afterPane.getImageAreaSize());

	    //draw the after image if one exists yet
	    if(afterPane.photos.getNumPhotos() != 0) {
		    image = afterPane.photos.getPhoto().getImage();
		    if (image != null) { 
		    	imageRect = afterPane.photos.getPhoto().getAdjustedImageRect();
		    	g.drawImage(image, (int) (beforePane.getImageAreaSize().getWidth() + imageRect.getX()), 
		    					   (int) imageRect.getY(), 
		    					   (int) imageRect.getWidth(), 
		    					   (int) imageRect.getHeight(), 
		    					   this); 
		    }
	    }
	   
	}
    
    private void drawRectangle(Graphics g, Rectangle rect) {
		 Graphics2D g2 = (Graphics2D) g;
	     g2.setColor(Color.DARK_GRAY);
	     Stroke oldStroke = g2.getStroke();
	     g2.setStroke(new BasicStroke(2));

	     //System.out.println("Draw Rect = " + cropAdjustedRect.toString());
	     g2.draw(rect);
	     
	     g2.setStroke(new BasicStroke(1));
	     g2.setStroke(oldStroke);
	}
    
    public void print() {
    	System.out.println("ComparePane:");
    	System.out.println("\tBeforePane:");
    	beforePane.print();
    	System.out.println("\tAfterPane:");
    	afterPane.print();
    }
}
