package br.com.thiago.gerenciador.dao;

import br.com.thiago.gerenciador.model.Tarefa;
import br.com.thiago.gerenciador.model.enums.Situacao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TarefaDao {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("gerenciador-tarefas");


    public void salvar(Tarefa tarefa){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(tarefa);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace(); throw e;
        } finally {
            if (em != null) em.close();
        }
    }

    public void atualizar(Tarefa tarefa){
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(tarefa);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace(); throw e;
        } finally {
            if (em != null) em.close();
        }
    }

    public void remover(Tarefa tarefa){
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            // Encontra a entidade gerenciada para garantir que estamos removendo a versão correta
            Tarefa tarefaParaRemover = em.find(Tarefa.class, tarefa.getId());
            if(tarefaParaRemover != null){
                em.remove(tarefaParaRemover);
            } else {
                System.out.println("DAO: Tentativa de remover tarefa com ID " + tarefa.getId() + " que não foi encontrada.");
            }
            em.getTransaction().commit();
            System.out.println("DAO: Commit da exclusão para ID " + tarefa.getId() + " realizado.");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.err.println("DAO: Erro ao remover tarefa ID " + tarefa.getId());
            e.printStackTrace(); throw e;
        } finally {
            if (em != null) em.close();
        }
    }

    public Tarefa buscarPorId(Long id){
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Tarefa.class, id);
        } finally {
            if (em != null) em.close();
        }
    }

    public List<Tarefa> listarTodas(){
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Tarefa> query = em.createQuery("SELECT t FROM Tarefa t ORDER BY t.id DESC", Tarefa.class);
            return query.getResultList();
        } finally {
            if (em != null) em.close();
        }
    }


    public List<Tarefa> listarComFiltros(String numeroIdStr, String tituloDescricao, String responsavel, Situacao situacao){
        EntityManager em = emf.createEntityManager(); // Usa um EM local
        try {
            StringBuilder jpqlBuilder = new StringBuilder("SELECT t FROM Tarefa t WHERE 1=1");
            Map<String, Object> parametros = new HashMap<>();

            if (numeroIdStr != null && !numeroIdStr.trim().isEmpty()) {
                try {
                    Long id = Long.parseLong(numeroIdStr.trim());
                    jpqlBuilder.append(" AND t.id = :id");
                    parametros.put("id", id);
                } catch (NumberFormatException e) {
                    System.err.println("DAO: Filtro de ID inválido, não é um número: " + numeroIdStr);
                }
            }
            if(tituloDescricao != null && !tituloDescricao.trim().isEmpty()){
                jpqlBuilder.append(" AND (LOWER(t.titulo) LIKE LOWER(:tituloDescricao) OR LOWER(t.descricao) LIKE LOWER(:tituloDescricao))");
                parametros.put("tituloDescricao", "%" + tituloDescricao.trim() + "%");
            }
            if(responsavel != null && !responsavel.trim().isEmpty()){
                jpqlBuilder.append(" AND LOWER(t.responsavel) = LOWER(:responsavel)"); // Corrigido para LOWER
                parametros.put("responsavel", responsavel.trim());
            }
            if(situacao != null){
                jpqlBuilder.append(" AND t.situacao = :situacao");
                parametros.put("situacao", situacao);
            }
            jpqlBuilder.append(" ORDER BY t.id DESC");

            System.out.println("DAO JPQL Gerada: " + jpqlBuilder.toString());
            TypedQuery<Tarefa> query = em.createQuery(jpqlBuilder.toString(), Tarefa.class);
            for(Map.Entry<String, Object> entry : parametros.entrySet()){
                query.setParameter(entry.getKey(), entry.getValue());
            }
            return query.getResultList();
        } finally {
            if (em != null) em.close();
        }
    }
}