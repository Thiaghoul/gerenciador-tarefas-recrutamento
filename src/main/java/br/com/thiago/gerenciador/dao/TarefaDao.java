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

    private static final String PU_NAME = "gerenciador-tarefas";
    private static EntityManagerFactory emf;

    static {
        Map<String, String> properties = new HashMap<>();
        String dbHost = System.getenv("DB_HOST");
        String dbPort = System.getenv("DB_PORT");
        String dbName = System.getenv("DB_NAME");
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");
        String hbm2ddl = System.getenv("HIBERNATE_HBM2DDL_AUTO");
        String driver = System.getenv("JDBC_DRIVER");
        String dialect = System.getenv("HIBERNATE_DIALECT");

        // Verifica se as variáveis de ambiente essenciais para o Render foram setadas
        if (dbHost != null && dbName != null && dbUser != null && dbPassword != null) {
            String jdbcUrl = "jdbc:postgresql://" + dbHost + ":" +
                    (dbPort != null ? dbPort : "5432") + "/" + dbName;

            properties.put("javax.persistence.jdbc.url", jdbcUrl);
            properties.put("javax.persistence.jdbc.user", dbUser);
            properties.put("javax.persistence.jdbc.password", dbPassword);

            if (driver != null) {
                properties.put("javax.persistence.jdbc.driver", driver);
            } else {
                properties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
            }

            if (dialect != null) {
                properties.put("hibernate.dialect", dialect);
            } else {
                properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            }

            if (hbm2ddl != null) {
                properties.put("hibernate.hbm2ddl.auto", hbm2ddl);
            } else {
                properties.put("hibernate.hbm2ddl.auto", "update");
            }

            properties.put("hibernate.show_sql", "false");

            emf = Persistence.createEntityManagerFactory(PU_NAME, properties);
            System.out.println("INFO: TarefaDao - EMF criado com configurações via variáveis de ambiente (Render).");
        } else {
            // Fallback para o persistence.xml local se as variáveis de ambiente não estiverem setadas
            // Útil para rodar localmente sem precisar configurar todas essas variáveis de ambiente.
            emf = Persistence.createEntityManagerFactory(PU_NAME);
            System.out.println("INFO: TarefaDao - EMF criado com persistence.xml padrão (ambiente local?).");
        }
    }


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