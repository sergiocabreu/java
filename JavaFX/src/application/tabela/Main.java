package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import util.AppletInfo;
import application.controller.MainLayoutController;
import application.controller.RootLayoutController;
import application.controller.utils.Constants;
import application.controller.utils.ImagesApplication;
import application.controller.utils.Utils;


/**
 * Aplicativo Cliente para Assinatura de Documentos FX
 * 
 * @author Gabriel Tavares
 * 
 * 
 * 
 * ************************************************************
 * ** Informações:
 * **  Ao fazer o deploy para EXE e sua respectiva instala��o, o program ser� salvo em:
 * **  C:\\Users\\username\\AppData\\Local
 * 
 */
public class Main extends Application {
	
	private BorderPane paneRoot;
	private AnchorPane paneMain;

	private RootLayoutController rootLayoutController;
	private MainLayoutController mainLayoutController;
	
	private Stage stage;
	

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {  
    	
    	// Carrega os dados iniciais
    	ImagesApplication.loadAllImages();
        Utils.criarDiretorios();
        
    	this.stage = stage;
    	stage.setResizable(true);
    	
    	// Configura o titulo da janela
    	stage.setTitle("Assinador Digital de Documentos - TCE-CE");
    	
        // Set the application icon.
        stage.getIcons().add(ImagesApplication.getImageLogo());
		
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
    	stage.iconifiedProperty().addListener(new ChangeListener<Boolean>() {
    	    @Override
    	    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldSceneVal, Boolean newSceneVal) {
    	    	windowMinimized(newSceneVal.booleanValue());
    	    }
    	});
    	stage.maximizedProperty().addListener(new ChangeListener<Boolean>() {
    	    @Override
    	    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldSceneVal, Boolean newSceneVal) {
    	        windowMaximized(newSceneVal.booleanValue());
    	    }
    	});
    	stage.widthProperty().addListener(new ChangeListener<Number>() {
    		@Override 
    		public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneVal, Number newSceneVal) {
    			windowWidth(newSceneVal);
    	    }
    	});
    	stage.heightProperty().addListener(new ChangeListener<Number>() {
    		@Override 
    		public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneVal, Number newSceneVal) {
    	        windowHeight(newSceneVal);
    	    }
    	});
		
        initRootLayout(stage);

        // Exibe a tela
        stage.show();
                
        showMainLayout();
    }

	private void showMainLayout() {
    	try {
    		AppletInfo info = new AppletInfo();
    		info.setServidorProtocoladora("svtcepdde50075.tce.ce.gov.br");
    		info.setTamanhoMaxArquivo(18);
    		info.setUsarCarimboDoTempo(false);
    		
            // Load person overview.
    		FXMLLoader loader = new FXMLLoader(Utils.getResourceFX(Constants.FILE_MAIN_FXML));
    		paneMain = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
    		paneRoot.setCenter(paneMain);

            // Give the controller access to the main app.
            mainLayoutController = loader.getController();
            mainLayoutController.setStage(this.stage, this);
            
            mainLayoutController.setInfo(info);

        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	/**
     * Instancia o layout a partir do arquivo FXML
     * 
     * @param stage
     * @throws IOException
     */
	private void initRootLayout(Stage stage) throws IOException {
		
		FXMLLoader loader = new FXMLLoader(Utils.getResourceFX(Constants.FILE_ROOT_FXML));
		paneRoot = (BorderPane) loader.load();
		
		Scene scene = new Scene(paneRoot);
        
		try {
			scene.getStylesheets().add(Utils.getResourceFX(Constants.FILE_CSS).toExternalForm());
//			scene.getStylesheets().add(Utils.getResourceFX(Constants.FILE_FIELDSET_CSS).toExternalForm());
			
		} catch (Exception e) {
			System.err.println("Não foi possível encontrar o arquivo " + Constants.FILE_CSS);
		}
        
        stage.setScene(scene);
        
        // Dar ao Controller acesso a MainApp
        rootLayoutController = loader.getController();
        rootLayoutController.setPrimaryStage(stage);
	}
	
	public void setMenuDisable(boolean status) {
		rootLayoutController.setMenuDisable(status);
	}
	
	public void closeApp(WindowEvent event) {
		if(mainLayoutController != null)
			mainLayoutController.close(event);
	}

	protected void windowMinimized(boolean isMinimized) {
		if (mainLayoutController != null)
			mainLayoutController.minimized(isMinimized);
	}

	protected void windowMaximized(boolean isMaximized) {
		if (mainLayoutController != null)
			mainLayoutController.maximized(isMaximized);
	}

	protected void windowHeight(Number newHeight) {
		if (mainLayoutController != null)
			mainLayoutController.windowHeight(newHeight);
	}

	protected void windowWidth(Number newSceneWidth) {
		if (mainLayoutController != null)
			mainLayoutController.windowWidth(newSceneWidth);
	}
	
}
