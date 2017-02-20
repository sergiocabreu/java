package application.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import application.model.Cliente;

public class ClienteDaoImpl extends BaseDao implements ClienteDao {

	@Override
	public void salvar(Cliente cliente) 
	{
		try 
		{
			abrir(true);
						
			if (cliente.getId() == null || cliente.getId() <=0 )
			{
				entityManager().persist(cliente);
			}
			else
			{
				entityManager().merge(cliente);
			}
						
			fechar(true);
		}
		catch (Exception e) 
		{
			
		}
	}

	@Override
	public void excluir(Cliente cliente) 
	{
		abrir(true);
		
		entityManager().remove( entityManager().find(Cliente.class, cliente.getId()) );
		
		fechar(true);
	}

	@Override
	public List<Cliente> buscaTodos() 
	{
		abrir(false);
		
		TypedQuery<Cliente> query = entityManager().createQuery("SELECT cli FROM Cliente cli ORDER BY cli.id DESC", Cliente.class);
				 
		List<Cliente> lista = query.getResultList();		
		
		fechar(false);
		
		return lista;
	}
	
	

}
