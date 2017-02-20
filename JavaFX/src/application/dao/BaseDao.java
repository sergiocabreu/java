package application.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class BaseDao 
{
	private static String BANCO = "lipsticksbatons";
	
	private static EntityManager entityManager;
 	
	private static EntityManagerFactory emf;
	
	static
	{
		  emf =  Persistence.createEntityManagerFactory(BANCO);
		 
	}
	
	/**
	 * Abre uma conex�o.
	 * @param transacao true com transa��; false sem transa��o.
	 */
	public void abrir(Boolean transacao)
	{
		entityManager = emf.createEntityManager();
		
		if (transacao)
		{			
			entityManager.getTransaction().begin();			
		}

	}	
	
	public void fechar(Boolean transacao)
	{
		if (transacao)
		{
			entityManager.getTransaction().commit();
		}
			
		try
		{
			
			entityManager.close();
		}
		catch(Exception e)
		{
			entityManager.getTransaction().rollback();	
		}
	}

	public EntityManager entityManager() {
		return entityManager;
	}

	
}
