package br.com.thiago.gerenciador.service;

import br.com.thiago.gerenciador.model.Tarefa;
import br.com.thiago.gerenciador.model.enums.Prioridade;
import br.com.thiago.gerenciador.model.enums.Situacao;

import java.time.LocalDate;
import java.util.List;

public class TesteTarefaService {

    public static void main(String[] args) {

        System.out.println("----Testando o tarefa service-----");
        TarefaService tarefaService = new TarefaService();

        System.out.println("");

        System.out.println("-----Testando listar todas as tarefas------");
        List<Tarefa> tarefas = tarefaService.listarTodas();
        for(Tarefa t : tarefas){
            System.out.println(t.getTitulo() + " - " + t.getSituacao());
        }

        System.out.println("");

        System.out.println("----Testando salvar nova tarefa-----");
        Tarefa novaTarefa = new Tarefa();

        novaTarefa.setTitulo("Revisão sobre a descoberta do brasil");
        novaTarefa.setDescricao("Descricao completa sobre a tarefa");
        novaTarefa.setResponsavel("Thiago");
        novaTarefa.setPrioridade(Prioridade.MEDIA);
        novaTarefa.setSituacao(Situacao.EM_ANDAMENTO);
        novaTarefa.setDeadline(LocalDate.of(2025, 5, 28));
        tarefaService.salvar(novaTarefa);

        System.out.println("");
        System.out.println("Tarefa salva com id: " + novaTarefa.getId());

        System.out.println("");

        System.out.println("-----Testando listar todas as tarefas novamente------");
        tarefas = tarefaService.listarTodas();
        for(Tarefa t : tarefas){
            System.out.println(t.getTitulo() + " - " + t.getSituacao());
        }

        System.out.println("");

        if(novaTarefa.getId() != null){
            System.out.println("---- Testando atualizar tarefa por id: " + novaTarefa.getId());
            novaTarefa.setDescricao("Descricao atualizada da tarefa.");
            novaTarefa.setSituacao(Situacao.CONCLUIDA);
            tarefaService.salvar(novaTarefa);

            Tarefa tarefaAtualizada = tarefaService.listarPorId(novaTarefa.getId());

            if(tarefaAtualizada != null){
                System.out.println("Nova descricao: "+ tarefaAtualizada.getDescricao());
                System.out.println("Nova situacao: "+ tarefaAtualizada.getSituacao());

            }else{
                System.out.println("Tarefa nao atualizada.");
            }
        }else{
            System.out.println("id nao encontrado");
        }

        System.out.println("");

        // add
        // - listarPorSituacao
        // - listarPorResponsavel
        // - excluir

        System.out.println("\nTestes do TarefaService concluídos (verifique o banco e os outputs).");

    }
}
