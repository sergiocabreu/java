package application.controller;

import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.WindowEvent;
import application.dao.ClienteDaoImpl;
import application.model.Cliente;

public class MainLayoutController{

	private ClienteDaoImpl clienteDao = new ClienteDaoImpl();
	
	@FXML
	private javafx.scene.control.TextField nome;
	
	@FXML
	private TableView<Cliente> tabelaCliente = new TableView<Cliente>();
	
	@FXML
	TableColumn<Cliente, Long> colunaId = new TableColumn<Cliente, Long>();
	
	@FXML
    TableColumn<Cliente, String> colunaNome = new TableColumn<Cliente, String>();	
    
	
	
	/**
	 * Inicializa a classe controller. 
	 * Eh chamado automaticamente depois do fxml ter sido carregado 
	 */
	@FXML
	private void initialize() 
	{
		/*HABILITA A EDIÇÃO DA TABELA*/
		tabelaCliente.setEditable(true);
			
		colunaNome.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nome"));
		colunaNome.setCellFactory(TextFieldTableCell.forTableColumn());
		
	    colunaNome.setOnEditCommit
	        (
	            new EventHandler<CellEditEvent<Cliente, String>>() 
	            {
	            	
	                @Override
	                public void handle(CellEditEvent<Cliente, String> t) 
	                {
	                	Cliente cliente = ((Cliente) t.getTableView().getItems().get(t.getTablePosition().getRow()));
	                	
	                    cliente.setNome(t.getNewValue());
	                    
	                    clienteDao.salvar(cliente);
	                }
	            }
	        );
		
		//tabelaCliente.getColumns().addAll(colunaId, colunaNome);
		
		colunaId.setCellValueFactory( new PropertyValueFactory<Cliente, Long>("id") );
		
		//colunaNome.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nome"));		
		
		List<Cliente > clienteLista = clienteDao.buscaTodos();			
		
		tabelaCliente.setItems( FXCollections.observableArrayList( clienteLista ));
		
	}
			      
    
	public void handleOk() {
		
		Cliente cliente = new Cliente();
		cliente.setNome(nome.getText());
		clienteDao.salvar(cliente);
		
		nome.clear();
		
		FXCollections.observableArrayList( clienteDao.buscaTodos() );
		
		tabelaCliente.setItems( FXCollections.observableArrayList( clienteDao.buscaTodos() ));
	}
	
	public void handleExcluir() {
		
		Cliente cliente = tabelaCliente.getSelectionModel().getSelectedItem();
		
		if (cliente != null)
		{
			clienteDao.excluir(cliente);
			
			FXCollections.observableArrayList( clienteDao.buscaTodos() );
			
			tabelaCliente.setItems( FXCollections.observableArrayList( clienteDao.buscaTodos() ));			
		}
		else
		{
			System.out.println("Cliente null");
		}
		
		
	}
	
	public void close(WindowEvent event) 
	{
		Platform.exit();
	}
	
}
	