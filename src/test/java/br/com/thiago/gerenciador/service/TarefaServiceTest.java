package br.com.thiago.gerenciador.service;

import br.com.thiago.gerenciador.model.Tarefa;
import br.com.thiago.gerenciador.model.enums.Prioridade;
import br.com.thiago.gerenciador.model.enums.Situacao;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TarefaServiceTest {

    private TarefaService tarefaService;
    private Tarefa tarefa;
    private static EntityManagerFactory emfTest;
    private EntityManager emTest;

    @BeforeAll
    static void initEmf(){
        emfTest = Persistence.createEntityManagerFactory("gerenciador-tarefas");
    }

    @AfterAll
    static void closeEmf(){
        if( emfTest != null && emfTest.isOpen()){
            emfTest.close();
        }
    }

    @BeforeEach
    void setUp(){

        tarefaService = new TarefaService();

        emTest = emfTest.createEntityManager();
        emTest.getTransaction().begin();

        emTest.createQuery("DELETE FROM Tarefa").executeUpdate();
        emTest.getTransaction().commit();


    }

    @AfterEach
    void tearDown(){
        if(emTest != null && emTest.isOpen()){
            emTest.close();
        }
    }

    @Test
    @DisplayName("Deve salvar com sucesso(Sem exception)")
    void deveSalvarTarefaSucesso(){
        tarefa = new Tarefa();
        tarefa.setTitulo("Tarefa de teste");
        tarefa.setDescricao("Descricação da tarefa de teste");
        tarefa.setResponsavel("JUnit");
        tarefa.setPrioridade(Prioridade.MEDIA);
        tarefa.setDeadline(LocalDate.now().plusDays(5));

        //ação
        tarefaService.salvar(tarefa);

        //verificação
        assertNotNull(tarefa.getId(), "ID da tarefa não deveria ser nulo após salvar");

        //busca no banco para confirmar
        emTest.getTransaction().begin();
        Tarefa tarefaBuscada = emTest.find(Tarefa.class, tarefa.getId());
        emTest.getTransaction().commit();

        assertNotNull(tarefaBuscada, "Tarefa não foi encontrada no banco após salvar.");
        assertEquals("Tarefa de teste", tarefaBuscada.getTitulo());

    }

    @Test
    @DisplayName("Deve listar todas as tarefas")
    void deveListarTodasTarefas(){
        // Preparação usando o serviço (que usa o emTest indiretamente via DAO)
        Tarefa t1 = new Tarefa();
        t1.setTitulo("Tarefa Lista 1");
        t1.setDescricao("Desc 1");
        t1.setResponsavel("Resp 1");
        t1.setPrioridade(Prioridade.BAIXA);
        t1.setSituacao(Situacao.EM_ANDAMENTO);
        t1.setDeadline(LocalDate.now().plusDays(1));
        tarefaService.salvar(t1);

        Tarefa t2 = new Tarefa();
        t2.setTitulo("Tarefa Lista 2");
        t2.setDescricao("Desc 2");
        t2.setResponsavel("Resp 2");
        t2.setPrioridade(Prioridade.MEDIA);
        t2.setSituacao(Situacao.EM_ANDAMENTO);
        t2.setDeadline(LocalDate.now().plusDays(2));
        tarefaService.salvar(t2);

        //Ação
        List<Tarefa> tarefas = tarefaService.listarTodas();

        //Verificação
        assertNotNull(tarefas, "A lista de tarefas não deve ser nula.");
        assertEquals(2, tarefas.size(), "Deveria haver 2 tarefas na lista.");
    }

    @Test
    @DisplayName("Deve listar por id")
    void deveBuscarTarefaPorIdExistente(){
        Tarefa t1 = new Tarefa();
        t1.setTitulo("Tarefa para buscar por id");
        t1.setDescricao("Desc busca id");
        t1.setResponsavel("thiago");
        t1.setPrioridade(Prioridade.BAIXA);
        t1.setSituacao(Situacao.EM_ANDAMENTO);
        t1.setDeadline(LocalDate.now().plusDays(1));
        tarefaService.salvar(t1);
        Long id = t1.getId();
        assertNotNull(id, "Id da tarefa de setup não pode ser nulo.");

        //ação
        Tarefa tarefaEncontrada = tarefaService.listarPorId(id);

        //verificação
        assertNotNull(tarefaEncontrada, "Tarefa deveria ser encontrada pelo id.");
        assertEquals(id, tarefaEncontrada.getId());
        assertEquals("Tarefa para buscar por id", tarefaEncontrada.getTitulo());

    }

    @Test
    @DisplayName("Deve retornar nulo ao buscar tarefa por ID inexistente")
    void deveRetornarNuloParaIdInexistente() {
        Long idInexistente = 9999L;

        // Ação
        Tarefa tarefaEncontrada = tarefaService.listarPorId(idInexistente);

        // Verificação
        assertNull(tarefaEncontrada, "Nenhuma tarefa deveria ser encontrada para um ID inexistente.");
    }

    @Test
    @DisplayName("Deve excluir uma tarefa com sucesso")
    void deveExcluirTarefaComSucesso(){
        Tarefa tarefaOriginal = new Tarefa();
        tarefaOriginal.setTitulo("Tarefa para Excluir");
        tarefaOriginal.setDescricao("Descrição para teste de exclusão");
        tarefaOriginal.setResponsavel("JUnit Exclusão");
        tarefaOriginal.setPrioridade(Prioridade.ALTA);
        tarefaOriginal.setSituacao(Situacao.EM_ANDAMENTO);
        tarefaOriginal.setDeadline(LocalDate.now().plusDays(10));

        tarefaService.salvar(tarefaOriginal);
        Long idParaExcluir = tarefaOriginal.getId();
        assertNotNull(idParaExcluir, "ID da tarefa de preparação não pode ser nulo.");

        Tarefa tarefaAntesDeExcluir = tarefaService.listarPorId(idParaExcluir);
        assertNotNull(tarefaAntesDeExcluir, "Tarefa deveria ser encontrada ANTES da exclusão.");

        // Ação:
        tarefaService.excluir(tarefaOriginal);

        // Verificação: Tentar buscar a tarefa pelo ID usando o serviço.
        Tarefa tarefaAposExclusao = tarefaService.listarPorId(idParaExcluir);
        assertNull(tarefaAposExclusao, "Tarefa com ID " + idParaExcluir + " não deveria ser encontrada no banco após excluir.");
    }

    @Test
    @DisplayName("Deve listar tarefas filtrando por Situação")
    void deveListarTarefasFiltrandoPorSituacao() {
        Tarefa tAberta = new Tarefa();
        tAberta.setTitulo("Tarefa Aberta Filtro");
        tAberta.setSituacao(Situacao.CONCLUIDA);
        tAberta.setResponsavel("R1"); tAberta.setPrioridade(Prioridade.MEDIA); tAberta.setDeadline(LocalDate.now());
        tarefaService.salvar(tAberta);

        Tarefa tEmAndamento1 = new Tarefa();
        tEmAndamento1.setTitulo("Tarefa Em Andamento 1 Filtro");
        tEmAndamento1.setSituacao(Situacao.EM_ANDAMENTO);
        tEmAndamento1.setResponsavel("R2"); tEmAndamento1.setPrioridade(Prioridade.ALTA); tEmAndamento1.setDeadline(LocalDate.now());
        tarefaService.salvar(tEmAndamento1);

        Tarefa tEmAndamento2 = new Tarefa();
        tEmAndamento2.setTitulo("Tarefa Em Andamento 2 Filtro");
        tEmAndamento2.setSituacao(Situacao.EM_ANDAMENTO);
        tEmAndamento2.setResponsavel("R1"); tEmAndamento2.setPrioridade(Prioridade.BAIXA); tEmAndamento2.setDeadline(LocalDate.now());
        tarefaService.salvar(tEmAndamento2);

        // Ação: Buscar tarefas com situação EM_ANDAMENTO
        List<Tarefa> tarefasFiltradas = tarefaService.listarComFiltros(null, null, null, Situacao.EM_ANDAMENTO);

        // Verificação
        assertNotNull(tarefasFiltradas, "A lista filtrada não deveria ser nula.");
        assertEquals(2, tarefasFiltradas.size(), "Deveria haver 2 tarefas com situação EM_ANDAMENTO.");
        assertTrue(tarefasFiltradas.stream().allMatch(t -> t.getSituacao() == Situacao.EM_ANDAMENTO), "Todas as tarefas na lista deveriam estar EM_ANDAMENTO.");
        assertTrue(tarefasFiltradas.stream().anyMatch(t -> t.getTitulo().equals("Tarefa Em Andamento 1 Filtro")));
        assertTrue(tarefasFiltradas.stream().anyMatch(t -> t.getTitulo().equals("Tarefa Em Andamento 2 Filtro")));
    }

    @Test
    @DisplayName("Deve listar todas as tarefas quando filtros são nulos ou vazios")
    void deveListarTodasComFiltrosNulos() {
        Tarefa t1 = new Tarefa(); t1.setTitulo("TF Nulo 1"); /* ... preencha ... */ tarefaService.salvar(t1);
        Tarefa t2 = new Tarefa(); t2.setTitulo("TF Nulo 2"); /* ... preencha ... */ tarefaService.salvar(t2);

        // Ação:
        List<Tarefa> tarefas = tarefaService.listarComFiltros(null, "", "", null);

        // Verificação
        assertNotNull(tarefas);
        assertEquals(2, tarefas.size(), "Deveria listar todas as 2 tarefas quando os filtros são nulos/vazios.");
    }

}
