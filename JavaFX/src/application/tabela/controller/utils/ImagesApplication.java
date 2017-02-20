package application.controller.utils;

import javafx.scene.image.Image;

public class ImagesApplication {

	private static Image imageFileDragAndDrop;
	private static Image imageLogo;
	
	public static void loadAllImages() {
		getImageLogo();
		getImageFileDragAndDrop();
	}
	
	public static Image getImageLogo() {
		if(imageLogo == null){
			imageLogo = new Image(Utils.getResourceFX(Constants.LOGO).toExternalForm());
		}
		return imageLogo;
	}
	
	public static Image getImageFileDragAndDrop() {
		if(imageFileDragAndDrop == null){
			imageFileDragAndDrop = new Image(Utils.getResourceFX(Constants.IMG_DRAGDROP).toExternalForm());
		}
		return imageFileDragAndDrop;
	}
	
}
