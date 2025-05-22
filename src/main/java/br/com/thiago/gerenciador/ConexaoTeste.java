package br.com.thiago.gerenciador;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ConexaoTeste {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gerenciador-tarefas");
        EntityManager em = emf.createEntityManager();

        System.out.println("✅ Conexão com PostgreSQL estabelecida!");

        em.close();
        emf.close();
    }
}