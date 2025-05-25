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
import java.util.Objects;

public class TarefaDao {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("gerenciador-tarefas");

    private EntityManager entityManager;

    public TarefaDao(){
        this.entityManager = emf.createEntityManager();
    }

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

    public List<Tarefa> listarComFiltros(String numeroId, String tituloDescricao, String responsavel, Situacao situacao){
        StringBuilder jpqlBuilder = new StringBuilder("SELECT t FROM Tarefa t WHERE 1=1");
        Map<String, Object> parametros = new HashMap<>();

        if (numeroId != null && !numeroId.trim().isEmpty()) {
            try {
                Long id = Long.parseLong(numeroId.trim());
                jpqlBuilder.append(" AND t.id = :id");
                parametros.put("id", id);

            } catch (NumberFormatException e) {
                System.err.println("Filtro de ID inválido, não é um número: " + numeroId);
            }
        }

        if(tituloDescricao != null && !tituloDescricao.trim().isEmpty()){
            jpqlBuilder.append(" AND (LOWER(t.titulo) LIKE LOWER(:tituloDescricao) " +
                    "OR LOWER(t.descricao) LIKE LOWER(:tituloDescricao))");
            parametros.put("tituloDescricao", "%" + tituloDescricao.trim() + "%");
        }

        if(responsavel != null && !responsavel.trim().isEmpty()){
            jpqlBuilder.append(" AND (LOWER(t.responsavel) = LOWER(:responsavel))");
            parametros.put("responsavel", responsavel.trim());
        }

        if(situacao != null){
            jpqlBuilder.append(" AND t.situacao = :situacao");
            parametros.put("situacao", situacao);
        }

        jpqlBuilder.append(" ORDER BY t.id DESC");

        TypedQuery<Tarefa> query = entityManager.createQuery(jpqlBuilder.toString(), Tarefa.class);

        for(Map.Entry<String, Object> entry : parametros.entrySet()){
            query.setParameter(entry.getKey(), entry.getValue());
        }

        return query.getResultList();
    }
}

