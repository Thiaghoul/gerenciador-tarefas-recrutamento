package br.com.thiago.gerenciador.dao;

import br.com.thiago.gerenciador.model.Tarefa;
import br.com.thiago.gerenciador.model.enums.Situacao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class TarefaDao {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("gerenciador-tarefas");

    public void salvar(Tarefa tarefa){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(tarefa);
            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }

    public void atualizar(Tarefa tarefa){
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(tarefa);
            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }

    public void remover(Tarefa tarefa){
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Tarefa tarefaRe = em.find(Tarefa.class, tarefa.getId());
            if(tarefaRe != null){
                em.remove(tarefaRe);
            }
            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }

    public Tarefa buscarPorId(Long id){
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Tarefa.class, id);

        } finally {
            em.close();
        }
    }

    public List<Tarefa> listarTodas(){
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Tarefa> query = em.createQuery("SELECT t FROM Tarefa t", Tarefa.class);
            return query.getResultList();

        } finally {
            em.close();
        }
    }

    public List<Tarefa> listarPorSituacao(Situacao situacao){
        EntityManager em = emf.createEntityManager();
        try{
            TypedQuery<Tarefa> query = em.createQuery(
                    "SELECT t FROM Tarefa t WHERE t.situacao = :situacao", Tarefa.class);
            query.setParameter("situacao", situacao);
            return query.getResultList();

        } finally {
            em.close();
        }
    }

    public List<Tarefa> listarPorResponsavel(String responsavel){
        EntityManager em = emf.createEntityManager();
        try{
            TypedQuery<Tarefa> query = em.createQuery(
                    "SELECT t FROM Tarefa t WHERE t.responsavel = :responsavel", Tarefa.class);
            query.setParameter("responsavel", responsavel);
            return query.getResultList();

        } finally {
            em.close();
        }
    }
}

