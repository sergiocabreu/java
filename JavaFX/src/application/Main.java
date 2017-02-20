package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import application.controller.MainLayoutController;
import application.utils.Constants;
import application.utils.Utils;


public class Main extends Application {
	private MainLayoutController mainLayoutWindow;
	

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {    
    	
    	// Configura o titulo da janela
    	stage.setTitle("TCE");
    	
        // Set the application icon.
//        stage.getIcons().add(new Image("file:resources/images/logo.png"));
		
		// Configura o evento ao clicar no botao fechar da janela
        Platform.setImplicitExit(false);
    	stage.setOnCloseRequest(new EventHandler<WindowEvent>() {			
			@Override
			public void handle(WindowEvent event) {
				if(event.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST){
					closeApp(event);
				}
			}
		});
		
        showMainLayout(stage);

        // Exibe a tela
        stage.show();
    }

    /**
     * Instancia o layout a partir do arquivo FXML
     * 
     * @param stage
     * @throws IOException
     */
	private void showMainLayout(Stage stage) throws IOException {

		FXMLLoader loader = new FXMLLoader(Utils.getResourceFX(Constants.FILE_FXML));
		AnchorPane pane = (AnchorPane) loader.load();
		Scene scene = new Scene(pane);
        
		try {
			scene.getStylesheets().add(Utils.getResourceFX(Constants.FILE_CSS).toExternalForm());
		} catch (Exception e) {
			System.err.println("Não foi possível encontrar o arquivo " + Constants.FILE_CSS);
		}
        
        stage.setScene(scene);
        
        
        // Dar ao Controller acesso a MainApp
        mainLayoutWindow = loader.getController();
	        
	}
	
	public void closeApp(WindowEvent event) {
		mainLayoutWindow.close(event);
		Platform.exit();
	}
}
