package application.controller;

import helper.ItemAssinatura;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Pair;
import netscape.javascript.JSObject;

import org.controlsfx.dialog.ProgressDialog;

import service.AssinaturaSemEnvio;
import service.AssinaturaTCE;
import service.ListenerAssinatura;
import service.STATUS;
import service.StatusItem;
import util.AppletInfo;
import application.Main;
import application.controller.helper.Item;
import application.controller.utils.CalendarUtils;
import application.controller.utils.Constants;
import application.controller.utils.FxUtilsAnimating;
import application.controller.utils.ImagesApplication;
import application.controller.utils.Utils;
import application.controller.utils.UtilsBD;
import application.controller.utils.XCell;
import application.controller.utils.XCellField;
import application.log.Log;
import br.com.bry.x509.certificado.X509CertificadoICPBrasil;

/**
 * 
 * @author Gabriel Tavares
 *
 */
public class MainLayoutController implements ListenerAssinatura {
	
	@FXML
	private ImageView ivLogo;
	
	@FXML
	private Button btnAssinar;
	
	@FXML
	private Button btnRemover;
	
	@FXML
	private StackPane paneMain;
	
	@FXML
	private Button btnAdicionar;
	
	@FXML
	private TableView<Item> tbArquivos;
	
    @FXML
    private TableColumn<Item, String> tbcArquivo;
    
    @FXML
    private TableColumn<Item, String> tbcCaminho;
	
	
	private Stage stage;
	private Main main;

	
	private ImageView dragImageView;
    
    
	private AssinaturaTCE assinaturaTCE;

	private boolean isRunning = false;

	private AppletInfo info;
	private Application app;
		

	
	/**
	 * Inicializa a classe controller. 
	 * Eh chamado automaticamente depois do fxml ter sido carregado 
	 */
	@FXML
	private void initialize() {
		ivLogo.setImage(new Image(Utils.getResourceFX(Constants.IMG_LOGO).toExternalForm()));
		btnAdicionar.setText("");
		btnAdicionar.setGraphic(new ImageView(new Image(Utils.getResourceFX(Constants.ICO_PLUS).toExternalForm())));
		btnRemover.setText("");
		btnRemover.setGraphic(new ImageView(new Image(Utils.getResourceFX(Constants.ICO_MINUS).toExternalForm())));
		
		Log.getLogger().log(Level.INFO, "Aplicação Iniciada");
		
		try {
			btnAdicionar.setTooltip(new Tooltip("Adicionar Documento"));
			btnRemover.setTooltip(new Tooltip("Remover Documento"));
			
			// Inicializa a Tabela
	        tbcArquivo.setCellValueFactory(cellData -> cellData.getValue().getArquivo());
	        tbcCaminho.setCellValueFactory(cellData -> cellData.getValue().getCaminho());
			tbArquivos.setItems(FXCollections.observableArrayList());
			tbArquivos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			tbArquivos.setPlaceholder(new Label("Arraste e solte aqui os documentos para realização da assinatura"));
			
			tbcArquivo.prefWidthProperty().bind(tbArquivos.widthProperty().multiply(0.30)); // w * 0.3
			tbcCaminho.prefWidthProperty().bind(tbArquivos.widthProperty().multiply(0.70)); // w * 0.7
			
			// Configura o Drag And Drop
			setupDragAndDrop();
			
			assinaturaTCE = new AssinaturaSemEnvio(this);	
			assinaturaTCE.setInfo(info);
			
			
			checaPeriodoValidade();
			
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
	}

	private void checaPeriodoValidade() throws ParseException {

		Calendar dataHoje = Calendar.getInstance();
		dataHoje.set(Calendar.HOUR_OF_DAY, 0);
		dataHoje.set(Calendar.MINUTE, 0);
		dataHoje.set(Calendar.SECOND, 0);
		dataHoje.set(Calendar.MILLISECOND, 0);
		String dataHojeStr = CalendarUtils.dateToString(dataHoje.getTime());
		
		String dataCompilacaoStr = Constants.DATA_COMPILACAO;
		Calendar dataCompilacao = CalendarUtils.stringToDataBR(dataCompilacaoStr);
		
		Calendar dataExpiracao = Calendar.getInstance();
		dataExpiracao.setTimeInMillis(dataCompilacao.getTimeInMillis());
		dataExpiracao.add(Calendar.MONTH, 2);
		
        
    	// Carrega Banco de Dados
        UtilsBD utilsBD = new UtilsBD();
		utilsBD.init();
		String dataBDStr = utilsBD.findFirst();
		
		if(dataBDStr == null){
			utilsBD.insertOrUpdate(dataHojeStr);
		} else{
			Calendar dataBD = CalendarUtils.stringToDataBR(dataBDStr);
			
			if(dataBD.getTimeInMillis() < dataHoje.getTimeInMillis()){
				utilsBD.insertOrUpdate(dataHojeStr);				
			}
		}

		dataBDStr = utilsBD.findFirst();
		Calendar dataBD = CalendarUtils.stringToDataBR(dataBDStr);
		
		utilsBD.close();
		
		
//		Log.getLogger().log(Level.INFO, "Compilacao: " + CalendarUtils.dateToString(dataCompilacao.getTime()));
//		Log.getLogger().log(Level.INFO, "Expiracao: " + CalendarUtils.dateToString(dataExpiracao.getTime()));
//		Log.getLogger().log(Level.INFO, "DB-Hoje: " + CalendarUtils.dateToString(dataBD.getTime()));
//		Log.getLogger().log(Level.INFO, "Hoje: " + CalendarUtils.datetimeToString(dataHoje.getTime()));
		
		if(dataBD.getTimeInMillis() == dataExpiracao.getTimeInMillis()){
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Atenção");
			alert.setHeaderText("Hoje é o último dia para a utilização do aplicativo!");
			alert.setContentText("O prazo para utilização do aplicativo irá expirar amanhã.\nEntre em contato com o Tribunal de Contas do Estado do Ceará.");

			alert.setOnCloseRequest(event -> {
				alert.close();
				System.exit(0);
			});
			
			// Icone da Janela
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(ImagesApplication.getImageLogo());

			alert.showAndWait();
			
		} else if(dataBD.getTimeInMillis() > dataExpiracao.getTimeInMillis()){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Expirado");
			alert.setHeaderText("Prazo de utilização do Aplicativo Expirado!");
			alert.setContentText("O prazo para utilização do aplicativo foi expirado.\nEntre em contato com o Tribunal de Contas do Estado do Ceará.");

			alert.setOnCloseRequest(event -> {
				alert.close();
				System.exit(0);
			});
			
			// Icone da Janela
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(ImagesApplication.getImageLogo());

			alert.showAndWait();
		}
	}

	@FXML
	public void handleAssinar() {
//		System.out.println("handleAssinar");
		
		List<Item> itemTableList = tbArquivos.getItems();
				
		if(itemTableList.size() > 0){
			carregaListaDeCertificados();
			
		} else{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Operação não Permitida");
			alert.setHeaderText("Adicione pelo menos 1 documento para realizar a assinatura!");
			alert.setContentText("Erro ao realizar a operação!");

			// Icone da Janela
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(ImagesApplication.getImageLogo());
			
			alert.showAndWait();
		}
	}
	
	private void carregaListaDeCertificados() {
		List<X509CertificadoICPBrasil> certificados = assinaturaTCE.getCertificados();
		ObservableList<XCellField> list = FXCollections.observableArrayList();
		
		for (X509CertificadoICPBrasil icpBr: certificados) {
			list.add(new XCellField(icpBr.getAssuntoCNFormatado(), Utils.formatCPF(icpBr.getCPF()), icpBr.getEmissorCN()));
			
//			System.out.println(icpBr.getAssuntoCNFormatado());
//			System.out.println(icpBr.getCPF());
//			System.out.println(icpBr.getEmissorCN());
//			System.out.println("--------------------------------------");

//			icpBr.getAssuntoCNFormatado(); // nome
//			icpBr.getCPF(); // cpf
//			icpBr.getEmissorCN(); // AC final
		}
		
		
		if(list.size() > 0){
			ListView<XCellField> listView = new ListView<>(list);
			
			listView.setCellFactory(new Callback<ListView<XCellField>, ListCell<XCellField>>() {
				@Override
				public ListCell<XCellField> call(ListView<XCellField> p) {
					return new XCell();
				}
			});
						
			// Create the custom dialog.
			Dialog<Pair<String, XCellField>> dialog = new Dialog<>();
			dialog.setTitle("Certificados Encontrados");
			dialog.setHeaderText("Selecione o certificado que deseja realizar a assinatura:");
			dialog.setGraphic(new ImageView(ImagesApplication.getImageLogo()));
	
			// Icone da Janela
			Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
			stage.getIcons().add(ImagesApplication.getImageLogo());
			
			// Set the button types.
			ButtonType loginButtonType = new ButtonType("Ok", ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
			
			dialog.getDialogPane().setContent(listView);
			
			// Seleciona a Primeira Posicao da Lista
			if(list.size() > 0){
				listView.getSelectionModel().selectFirst();
			}
			
			
			// Convert the result to a username-password-pair when the login button is clicked.
			dialog.setResultConverter(dialogButton -> {
			    if (dialogButton == loginButtonType) {
			    	XCellField selecionado = listView.getSelectionModel().getSelectedItem();
			    	
			    	if(selecionado != null){
			    		return new Pair<>(selecionado.getField2(), selecionado);
			    	}
			    }
			    return null;
			});
						
			Optional<Pair<String, XCellField>> result = dialog.showAndWait();
			
			result.ifPresent(
				xCellVal -> {
//					System.out.println("CPF=" + xCellVal.getKey() + ", Obj=" + xCellVal.getValue());
					
					String cpf = xCellVal.getKey();
					String cpfNoMask = Utils.removeMask(cpf);
					
					info.setCpf(cpfNoMask);
					
//					assinar();
//					Dialogs alert = Dialogs.create();
//					        alert.owner(this.stage)
//					        .title("Aguarde")
//					        .masthead("Assinando Documento(s)")
//					        .showWorkerProgress(service);
//					service.start();
							        
			        Service<Void> service = createService();
			        
					ProgressDialog progDiag = new ProgressDialog(service);
			        progDiag.setTitle("Aguarde");
			        progDiag.initOwner(this.stage);
			        progDiag.setHeaderText("Assinando Documento(s)");
			        progDiag.initModality(Modality.WINDOW_MODAL);
			        service.start();
					        
				}
			);
			
//			if(!result.isPresent()){
//				System.out.println("NAO selecionado!");
//				
//				Alert alert = new Alert(AlertType.ERROR);
//				alert.setTitle("Selecione um certificado da lista");
//				alert.setHeaderText("Nenhum certificado selecionado!");
//				alert.setContentText("Erro ao realizar a operação!");
//
//				// Icone da Janela
//				Stage stage1 = (Stage) alert.getDialogPane().getScene().getWindow();
//				stage1.getIcons().add(ImagesApplication.getImageLogo());
//
//				alert.showAndWait();
//				
//				carregaListaDeCertificados();
//			}
			
		} else{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Operação não Permitida");
			alert.setHeaderText("Insira o Token!");
			alert.setContentText("Erro ao realizar a operação!");

			// Icone da Janela
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(ImagesApplication.getImageLogo());

			alert.showAndWait();
			
		}
	}

	private Service<Void> createService() {
		return new Service<Void>() {
		    @Override
		    protected Task<Void> createTask() {
		        return new Task<Void>() {
		            @Override
		            protected Void call()
		                    throws InterruptedException {
		            	
		                updateMessage("Assinando Documento(s) . . .");
//		                updateProgress(0, 10);
//		                
//		                for (int i = 0; i < 10; i++) {
//		                    Thread.sleep(300);
//		                    updateProgress(i + 1, 10);
//		                    updateMessage("Found " + (i + 1) + " friends!");
//		                }
		                
		                try {
							int qtd = realizaAssinatura();
			                updateMessage("Assinatura realizada com sucesso");
							
							if(qtd > 0){
								Platform.runLater(
									() -> {	
										Alert alert = new Alert(AlertType.INFORMATION);
										alert.setTitle("Assinatura realizada com sucesso");
										alert.setHeaderText(qtd + " arquivos assinados!");
										alert.setContentText("Os arquivos de assinatura se encontram no mesmo diretório dos documentos.");
					
										// Icone da Janela
										Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
										stage.getIcons().add(ImagesApplication.getImageLogo());
					
										alert.showAndWait();
								
										tbArquivos.setItems(FXCollections.observableArrayList());
										
							        }
								);
							}
							
						} catch (IOException e) {
							e.printStackTrace();
							addSubLog(e.getMessage());
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
							addSubLog(e.getMessage());
						} catch (Exception e) {
							e.printStackTrace();
						} finally{
//							visibleProcess(false);
							disable(false);
							isRunning = false;
						}
		                
		                return null;
		            }
		        };
		    }
		};
	}
	
	
	private int realizaAssinatura() throws IllegalArgumentException, IOException {
		List<Item> itemTableList = tbArquivos.getItems();
		
		if(itemTableList.size() <= 0){
			Utils.showAlert(
					"Operação não Permitida",
					"Adicione até "+info.getMaxDocumentos()+" para realizar a Assinatura!",
					"Erro ao realizar a operação!", 
					AlertType.ERROR,
					ImagesApplication.getImageLogo());
			return -1;
		}
		
		if(itemTableList.size() > info.getMaxDocumentos()){
			Utils.showAlert(
					"Operação não Permitida",
					"Selecione apenas "+info.getMaxDocumentos()+"!",
					"Erro ao realizar a operação!", 
					AlertType.ERROR,
					ImagesApplication.getImageLogo());
			
			return -1;
		}

		isRunning = true;
		disable(true);
//		disableBtn(true);
//		visibleProcess(true);
			
		List<ItemAssinatura> itemAssinaturaList = itemTableList.stream().map(
				item -> new ItemAssinatura( item.getArquivo().get(), new File(item.getCaminho().get()) )
			).collect(Collectors.toList());
		
		int status = -1;
		
		try {
			status = assinaturaTCE.eventoAssinar(itemAssinaturaList);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			Log.getLogger().log(Level.INFO, e.getMessage());
			Log.getLogger().log(Level.INFO, "Falha na assinatura: Verifique sua conexão com a internet e o estado de revogação do certificado");
			
			Platform.runLater(
					() -> {	
						
					Utils.showAlert(
							"Operação não Permitida",
							"Falha na assinatura",
							"Verifique sua conexão com a internet e o estado de revogação do certificado", 
							AlertType.ERROR,
							ImagesApplication.getImageLogo());
			        }
				);
			return -1;
		}
		
		STATUS statusObj = STATUS.getStatus(status);
		addSubLog(STATUS.getDescricao(statusObj), statusObj);

		
		StatusItem statusMsg = STATUS.getErro(status);
		System.out.println("msg: " + statusMsg.msg + " | "+ statusMsg.codigo + " | " + statusMsg.cor);
		
		return itemTableList.size();
	}
	
	private void assinar() {
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					int qtd = realizaAssinatura();
					
					if(qtd > 0){
						Platform.runLater(
							() -> {	
								Alert alert = new Alert(AlertType.INFORMATION);
								alert.setTitle("Assinatura realizada com sucesso");
								alert.setHeaderText(qtd + " arquivos assinados!");
								alert.setContentText("Os arquivos de assinatura se encontram no mesmo diretório dos documentos.");
			
								// Icone da Janela
								Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
								stage.getIcons().add(ImagesApplication.getImageLogo());
			
								alert.showAndWait();
						
								tbArquivos.setItems(FXCollections.observableArrayList());
								
					        }
						);
					}
					
				} catch (IOException e) {
					e.printStackTrace();
					addSubLog(e.getMessage());
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					addSubLog(e.getMessage());
				} catch (Exception e) {
					e.printStackTrace();
				} finally{
//					visibleProcess(false);
					disable(false);
					isRunning = false;
				}
			}
		}).start();
		
//		new Thread(
//			() -> {
//				try {
//					isRunning = true;
//					disable(true);
//
//					List<Item> itemTableList = tbArquivos.getItems();
//					List<ItemAssinatura> itemAssinaturaList = itemTableList.stream().map(
//							item -> new ItemAssinatura( item.getArquivo().get(), new File(item.getCaminho().get()) )
//						).collect(Collectors.toList());
//					
//					int status = assinaturaTCE.eventoAssinar(itemAssinaturaList, cpf);
//					Log.getLogger().log(Level.INFO, "Processo concluído. [Código = " + status + "]");
//					
//					
//					Platform.runLater(
//						() -> {	
//							Alert alert = new Alert(AlertType.INFORMATION);
//							alert.setTitle("Assinatura realizada com sucesso");
//							alert.setHeaderText(itemTableList.size() + " arquivos assinados!");
//							alert.setContentText("Os arquivos de assinatura se encontram no mesmo diretório dos documentos.");
//		
//							// Icone da Janela
//							Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
//							stage.getIcons().add(ImagesApplication.getImageLogo());
//		
//							alert.showAndWait();
//					
//							tbArquivos.setItems(FXCollections.observableArrayList());
//							
//				        }
//					);
//					
//				} catch (IOException e) {
//					e.printStackTrace();
//					Log.getLogger().log(Level.SEVERE, e.getMessage(), e);
//				} catch (ExcecaoCMS e) {
//					e.printStackTrace();
//					Log.getLogger().log(Level.SEVERE, e.getMessage(), e);
//				} catch (Exception e) {
//					e.printStackTrace();
//				} finally{
//					disable(false);
//					isRunning = false;
//				}
//			}
//		).start();
	}

	@FXML
	public void handleAdicionar() {
//		System.out.println("handleAdicionar");
		
		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(new File(System.getProperty("user.home")));
		chooser.setTitle("Selecione o arquivo a ser assinado");
				
//		List<ExtensionFilter> extensionList = Arrays.asList(new FileChooser.ExtensionFilter[]{
//				new FileChooser.ExtensionFilter("Arquivos PDF (*.pdf)", "*.pdf"),
//				new FileChooser.ExtensionFilter("Arquivos XML (*.xml)", "*.xml")
//		});
//		
//		chooser.getExtensionFilters().addAll(extensionList);
		
		List<File> file = chooser.showOpenMultipleDialog(stage);
		
		if(file != null && file.size() > 0){
			for (File current : file) {
				if(Utils.getFileSizeInMegaBytes(current) <= info.getTamanhoMaxArquivo()){
					tbArquivos.getItems().add(new Item(current.getName(), current.getAbsolutePath()));
				} else{
					Utils.showAlert(
							"Operação não Permitida",
							"Não foi possível inserir o arquivo pois excede o limite de "+info.getTamanhoMaxArquivo()+"MB!",
							"Erro ao importar o arquivo: " + current.getAbsolutePath(), 
							AlertType.ERROR,
							ImagesApplication.getImageLogo());
				}
			}
		}
	}
	
	@FXML
	public void handleRemover() {
//		System.out.println("handleRemover");
		
		List<Item> selecionadoList = tbArquivos.getSelectionModel().getSelectedItems();
		
		if(selecionadoList.size() > 0){
			tbArquivos.getItems().removeAll(selecionadoList);
			
		} else{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Operação não Permitida");
			alert.setHeaderText("Selecione pelo menos 1 item da lista à ser removido!");
			alert.setContentText("Erro ao realizar a operação!");

			// Icone da Janela
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(ImagesApplication.getImageLogo());

			alert.showAndWait();
		}
	}
	
	public void setupDragAndDrop(){

		dragImageView = new ImageView(ImagesApplication.getImageFileDragAndDrop());
        
        paneMain.setOnDragOver(
        		event -> {        			
        			dragImageView.setFitHeight(260);
        			dragImageView.setFitWidth(260);
        			
        			/* data is dragged over the target */
//        			System.out.println("onDragOver - Click pressionado");
        			
        			
        			FxUtilsAnimating.fadeInAnimating(dragImageView, paneMain);

        			dragImageView.setOpacity(0.8);
                    dragImageView.toFront();
                    dragImageView.setMouseTransparent(true);
                    dragImageView.setVisible(true);
                    dragImageView.relocate(
                    		(int) (event.getSceneX() - dragImageView.getBoundsInLocal().getWidth()),
                    		(int) (event.getSceneY() - dragImageView.getBoundsInLocal().getHeight()));
        			
                    
        			Dragboard db = event.getDragboard();
        			if(db.hasFiles()){
        				event.acceptTransferModes(TransferMode.COPY);
        			}
        			
        			event.consume();
        		}
        );
        
        paneMain.setOnDragExited(
        		event -> {
//        			System.out.println("OnDragExited");
                    FxUtilsAnimating.fadeOutAnimating(dragImageView, paneMain);
//        			dragImageView.setVisible(false);
        			event.consume();
        		}
        );
        
		paneMain.setOnDragDropped(
				event -> {
                /* data dropped */
//                System.out.println("onDragDropped - Click finalizado!");
 
                Dragboard db = event.getDragboard();
                
                if(db.hasFiles()){
                    
                    for(File file: db.getFiles()){
                    	
//                    	if(file.getName().endsWith(".pdf")){
                    		
                    		boolean isArquivoExiste = false;
                    		
                    		for (Item i : tbArquivos.getItems()) {
                    			if(i.getCaminho().getValue().equals(file.getAbsolutePath())){
                    				isArquivoExiste = true;
                    				break;
                    			}
							}
                    		
	                    	if(!isArquivoExiste){
	                    		Item item = new Item(file.getName(), file.getAbsolutePath());
	                    		
//		                        System.out.println(file.getAbsolutePath());
		                        
		                        if(Utils.getFileSizeInMegaBytes(file) <= info.getTamanhoMaxArquivo()){
		                        	tbArquivos.getItems().add(item);				
		        				} else{
		        					Utils.showAlert(
		        							"Operação não Permitida",
		        							"Não foi possível inserir o arquivo pois excede o limite de "+info.getTamanhoMaxArquivo()+"MB!",
		        							"Erro ao importar o arquivo: " + file.getAbsolutePath(), 
		        							AlertType.ERROR,
		        							ImagesApplication.getImageLogo());
		        				}
		                        
	                    	} else{
	                    		Alert alert = new Alert(AlertType.ERROR);
	                			alert.setTitle("Operação não Permitida");
	                			alert.setHeaderText("O documento já se encontra na lista!");
	                			alert.setContentText("Erro ao realizar a operação!");

	                			// Icone da Janela
	                			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
	                			stage.getIcons().add(ImagesApplication.getImageLogo());

	                			alert.showAndWait();
	                    	}
	                    	
//                    	} else{
//                    		Alert alert = new Alert(AlertType.ERROR);
//                			alert.setTitle("Operação não Permitida");
//                			alert.setHeaderText("Selecione somente arquivos de extensão PDF (.pdf)");
//                			alert.setContentText("O arquivo " + file.getAbsolutePath() + " não pode ser inserido!");
//
//                			// Icone da Janela
//                			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
//                			stage.getIcons().add(ImagesApplication.getImageLogo());
//
//                			alert.showAndWait();
//                    	}
                    }

                    event.setDropCompleted(true);
                    
				} else {
					event.setDropCompleted(false);
				}

                event.consume();
            }
        );
        
    }
	
	private void disable(final boolean value) {
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {	
	        	btnAdicionar.setDisable(value);
	        	btnAssinar.setDisable(value);
	        	btnRemover.setDisable(value);
	        }
		});	
	}
	
	private void visibleBtn(final boolean value) {
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {	
	        	btnAdicionar.setVisible(value);
	        	btnAssinar.setVisible(value);
	        	btnRemover.setVisible(value);
	        }
		});	
	}
	
	public void setStage(Stage stage, Main main) {
		this.stage = stage;
		this.main = main;
	}
	
	@Override
	public void eval(String callbackfunction, String paramfunction1, String paramfunction2) {
		JSObject jsWin = app.getHostServices().getWebContext();  
		
		if (jsWin != null) { // Null for nonembedded applications
			System.out.println(callbackfunction + "("+ paramfunction1 +", "+ paramfunction2 +")");
			jsWin.eval(callbackfunction + "('"+ paramfunction1 +"', '"+ paramfunction2 +"')");
		}
		
	}


	@Override
	public void addLog(String msg) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {	
//				lbProgress.setTextFill(Color.web(Constants.COR_AZUL));
//				lbProgress.setText(msg);
			}
		});	
	}
	
	@Override
	public void addSubLog(String msg) {
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {	
	        	StatusItem item = STATUS.getErro(STATUS.STATUS_ERRO_GENERICO);

//    			lbResult.setTextFill(item.cor);
//    			lbResult.setText(msg);
	        }
		});	
	}


	@Override
	public void addSubLog(final String msg, final STATUS status) {
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {	
	        	StatusItem item = STATUS.getErro(status);

//    			lbResult.setTextFill(item.cor);
//    			lbResult.setText(STATUS.getDescricao(status));
	        }
		});	
	}
	
	public void setInfo(AppletInfo info) {
		this.info = info;
		assinaturaTCE.setInfo(info);
	}

	public void close(WindowEvent event) {
//		Alert alert = new Alert(AlertType.CONFIRMATION);
//		alert.setTitle("Encerrar");
//		alert.setHeaderText("Deseja encerrar a aplicação");
//		alert.setContentText("Confirma a realização deste procedimento?");
//
//		Optional<ButtonType> result = alert.showAndWait();
//		if (result.get() == ButtonType.OK){
			Log.getLogger().log(Level.INFO, "Aplicação Encerrada");
			
			stage.close();
	//		Platform.exit();
			System.exit(0); // FIXME Procurar um metodo menos "agressivel"
			
//		} else{
//			stage.show();
//		}
	}

	public void minimized(boolean isMinimized) {
//		System.out.println("minimized: " + isMinimized);
	}

	public void maximized(boolean isMaximized) {
//    	System.out.println("maximized: " + isMaximized);
    	getWindowParams();
	}

	public void windowHeight(Number newHeight) {
		getWindowParams();
	}

	public void windowWidth(Number newSceneWidth) {
		getWindowParams();	
	}

	/**
	 * Observação!
	 * Este delay é importante pois há um digergência de tempo em relação 
	 * ao processo de aumentar o tamanho da tela e o tamanho do componente DragAndDrop
	 */
	private void getWindowParams() {
//		new Thread(
//    		() -> {
//				try {
//					Thread.sleep(7);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				
//				this.paneMainHeight = paneMain.getHeight();
//				this.paneMainWidth = paneMain.getWidth();
//				
//				if(this.paneMainHeight > 481 || this.paneMainWidth > 702){
//					this.paneMainHeight = 481;
//					this.paneMainWidth = 702;
//				}
//				
//				System.out.println("h1: " + paneMainHeight);
//				System.out.println("w1: " + paneMainWidth);		
//			}
//		).start();
	}
	
}