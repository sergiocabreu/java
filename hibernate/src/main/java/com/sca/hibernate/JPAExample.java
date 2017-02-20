package com.sca.hibernate;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;



public class JPAExample {

  private EntityManager entityManager = EntityManagerUtil.getEntityManager();

  public static void main(String[] args) 
  {
    JPAExample example = new JPAExample();
 
    Cliente cliente1 = example.save("Cliente A");
    Cliente cliente2 = example.save("Cliente 1");
    example.listar();
    
    
    /*example.updateStudent(cliente1.getId(), "Sumith Honai");
    example.updateStudent(cliente2.getId(), "Anoop Pavanai");
    example.listar();
    System.out.println("After Sucessfully deletion ");
    example.deleteStudent(cliente2.getId());
    example.listar();*/
   

  }

  public Cliente save(String nome) {
    Cliente cliente = new Cliente();
    try {
      entityManager.getTransaction().begin();
      cliente.setNome(nome);
      cliente = entityManager.merge(cliente);
      entityManager.getTransaction().commit();
    } catch (Exception e) {
      entityManager.getTransaction().rollback();
    }
    return cliente;
  }

  public void listar() {
    try {
      entityManager.getTransaction().begin();
      
      @SuppressWarnings("unchecked")
      List<Cliente> clientes = entityManager.createQuery("from Cliente").getResultList();
      
      for (Iterator<Cliente> iterator = clientes.iterator(); iterator.hasNext();) {
        Cliente cliente = (Cliente) iterator.next();
        System.out.println(cliente.getNome());
      }
      
      entityManager.getTransaction().commit();
      
    } catch (Exception e) {
      entityManager.getTransaction().rollback();
    }
  }

  public void updateStudent(Long studentId, String studentName) {
    try {
      entityManager.getTransaction().begin();
      Cliente cliente = (Cliente) entityManager.find(Cliente.class, studentId);
      cliente.setNome(studentName);
      entityManager.getTransaction().commit();
    } catch (Exception e) {
      entityManager.getTransaction().rollback();
    }
  }

  public void deleteStudent(Long studentId) {
    try {
      entityManager.getTransaction().begin();
      Cliente cliente = (Cliente) entityManager.find(Cliente.class, studentId);
      entityManager.remove(cliente);
      entityManager.getTransaction().commit();
    } catch (Exception e) {
      entityManager.getTransaction().rollback();
    }
  }}
