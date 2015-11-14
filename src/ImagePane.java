import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

import javax.swing.JPanel;

import aberscan.Directories;
import aberscan.Photo;
import aberscan.PhotoList;
import aberscan.PreloadImages;

public class ImagePane extends JPanel {
	static final long serialVersionUID = 6;
	ComparePane parent;
	PhotoList photos;
	Directories photoDir;
	//Image curImage;
    //Rectangle imageSize;
    //Rectangle adjustedImageSize;
    Rectangle imageAreaSize;
	
    public int getCurIndex() {return photos.getCurIndex();}
    protected void setCurIndex(int i) {photos.setCurIndex(i); }
    //public Image getCurImage() { return(curImage); }
	public Photo getFirstPhoto() { return(photos.getUserSelectedFirstPhoto()); }
	public Photo getCurPhoto() { return(photos.getPhoto()); }
	public Photo getNextPhoto() { return(photos.getNextPhoto()); }
	public Photo getPrevPhoto() { return(photos.getPrevPhoto()); }
	public Rectangle getImageAreaSize() { return(imageAreaSize); }
	public boolean endOfPhotos() { return(photos.atEnd()); }
	public boolean startOfPhotos() { return(photos.atBeginning()); }
	
	//public void setPhotoDir(File dir) { photoDir.setupDirectories(dir); }
	
	public void setImageAreaSize(Rectangle rect) { imageAreaSize = rect; }
	
	public ImagePane(ComparePane parent) {
		this.parent = parent;
		photos = new PhotoList();
		photoDir = new Directories();
		//curImage = null;
		//imageSize = new Rectangle(0,0,0,0);
		//adjustedImageSize = new Rectangle(0,0,0,0);
	}
	
	public void loadPhotos() {
		photos.loadPhotos(photoDir.getImageDirectory());
	}
	
    public void preloadImages(String str) {
    	Thread preloadThread;
    	
    	preloadThread = new PreloadImages("Preload of Images", str, photos);
    	preloadThread.start();
    	
    }
	
	public void setCopyFlags(boolean flag) {
		photos.setCopyFlags(flag);
	}
	
	public void setStartPhoto(Photo photo) {
		photos.setStartPhoto(photo);
	}
	
	protected void setScreenImage(Photo photo) {
		//curImage = photo.getImage("gbs-before");
		int i = 0;
		
		//wait here until the image is ready - it might that that we have caught up with the image pre-load thread
		//In any case, don't wait longer than 100 waits
		//System.out.println("Image(setScreenImage)::image to be displayed");
		//photo.print();
		Image curImage = photo.getImage();
		
		while(curImage == null && i < 100) {
			try{ 
				Thread.sleep(1000); // Sleep for 1 sec 
				} 
			catch(InterruptedException e){
			}
			//System.out.println("ImagePane::(setScreenImage):curImage = " + curImage + ", i = " + i);
			i++;
			curImage = photo.getImage();
		}
		
		//only calculate the adjusted size if we have to
		if(photo.getAdjustedImageRect().getWidth() == 0)
			rightSize();
		
		parent.repaint();

	    //System.out.println("Screen = (" + screen.getWidth() + "," + screen.getHeight() + ")");
	    //System.out.println("OrigImage = (" + origImage.getWidth() + "," + image.getHeight() + ")");
	}
	
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
		if ((infoflags & ImageObserver.ERROR) != 0) {
		    System.out.println("ImagePane ERROR : Error loading image!");
		    return true;
		    //System.exit(-1);
		}
		if ((infoflags & ImageObserver.WIDTH) != 0 && (infoflags & ImageObserver.HEIGHT) != 0)
		    rightSize();
		if ((infoflags & ImageObserver.SOMEBITS) != 0)
		    parent.repaint();
		if ((infoflags & ImageObserver.ALLBITS) != 0) {
		    rightSize();
		    parent.repaint();
		    return false;
		}
		return true;
	}
	
	protected void rightSize() { 
    	int width = 0;		// original width of the image
    	int height = 0;	// original height
    	Photo curPhoto = photos.getPhoto();
	    width = curPhoto.getImage().getWidth(this);
	    height = curPhoto.getImage().getHeight(this);
	    if (width == -1 || height == -1)
	      return;
	    //addNotify();
	    
	    //write the image info into the photo
	    curPhoto.setImageRect(width, height);
	    //System.out.println("Actual Image = " + imageRect.toString());
	    
	    //calculate width and height only if we have too. May already be calculated
	    //System.out.println("ImagePane:: (rightSize)width= " + width + ", height = " + height);
    
		//adjust the height. Note that adjWidth/adjHeight = imageWidth/imageHeight. Therefore if adjHeight = screenHeight, then adjWidth = screenHeight*imageWidth/imageHeight
		height = (int) imageAreaSize.getHeight();
		width = (int)(scaleWidth(height, curPhoto.getImageRect()));
		curPhoto.setAdjustedImageRect(positionEx(width), positionWy(height), width, height);
		//System.out.println("Image after height adjustment = " + adjustedImage.toString());
		
		//if still too wide, then scale down. Use the same ratio as above
		if(width > imageAreaSize.getWidth()) {
			width = (int)imageAreaSize.getWidth();
			height = (int)(scaleHeight(width, curPhoto.getImageRect()));
			curPhoto.setAdjustedImageRect(positionEx(width), positionWy(height), width, height);
		}
	}
	
	private double scaleHeight(double width, Rectangle rect) {
		return (width * rect.getHeight()/rect.getWidth());
	}
	
	private double scaleWidth(double height, Rectangle rect) {
		return( height * rect.getWidth()/rect.getHeight());
	}
	
	private double positionEx(double width) {
		return (imageAreaSize.getWidth()/2 - width/2);
	}
	
	private double positionWy(double height) {
		return (imageAreaSize.getHeight()/2 - height/2);
	}
	
	public void print() {
		System.out.println("ImagePane:");
		photos.printPhotos();
		photoDir.print();
		//if(curImage != null) System.out.println("curImage = " + curImage.toString());
		//System.out.println("imageSize = " + imageSize.toString());
		//System.out.println("adjustedImageSize = " + adjustedImageSize.toString());
	}
}
