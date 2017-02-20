package application.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import application.controller.utils.Constants;
import application.controller.utils.ImagesApplication;
import application.controller.utils.Utils;

public class RootLayoutController {

	private Stage stage;
	
	@FXML
	private MenuItem mnURLs;
	
	@FXML
	private MenuItem mnEmails;
	
	@FXML
	private MenuItem mnConfiguracoes;
	
	@FXML
	private MenuItem mnSair;
	
	
	@FXML
    private void handleConfiguracoes() {
        try {
            // Load person overview.
    		FXMLLoader loader = new FXMLLoader(Utils.getResourceFX(Constants.FILE_CONF_DIALOG_FXML));
    		AnchorPane pane = (AnchorPane) loader.load();
    		
    		// Cria o palco dialogStage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Configurações");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);
            dialogStage.setResizable(false);
            
            Scene scene = new Scene(pane);
            dialogStage.setScene(scene);
    		
            // Define a pessoa no controller.
//            ConfDialogController controller = loader.getController();
//            controller.setDialogStage(dialogStage);
    		
    		// Mostra a janela e espera até o usuário fechar.
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	@FXML
    private void handleSair() {
//        System.out.println("handleSair");
        stage.close();
//		Platform.exit();
		System.exit(0); // FIXME Procurar um metodo menos "agressivel"
    }

	@FXML
	private void handleAbout(){
//        System.out.println("handleAbout");		
        
        Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Sobre");
		alert.setHeaderText("Assinador Digital de Documentos - TCE-CE");
		alert.setContentText("Versão 1.0\n\nEste produto foi desenvolvido pelo Tribunal de Contas do Estado do Ceará, sob a responsabilidade da Secretaria de Tecnologia da Informação do TCE-CE\n\nwww.tce.ce.gov.br");
		alert.setGraphic(new ImageView(ImagesApplication.getImageLogo()));

		// Icone da Janela
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(ImagesApplication.getImageLogo());

		alert.showAndWait();
	}

	public void setPrimaryStage(Stage stage) {
		this.stage = stage;		
	}

	public void setMenuDisable(boolean status) {
		mnConfiguracoes.setDisable(status);
		mnEmails.setDisable(status);
		mnURLs.setDisable(status);
//		mnSair.setDisable(status);
	}
	
}
