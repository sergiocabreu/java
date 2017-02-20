package application.dao;

import java.util.List;

import application.model.Cliente;

public interface ClienteDao {

	public void salvar(Cliente cliente);
	
	public void excluir(Cliente cliente);
	
	public List<Cliente> buscaTodos();
	
}
