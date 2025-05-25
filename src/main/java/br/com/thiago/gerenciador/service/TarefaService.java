package br.com.thiago.gerenciador.service;

import br.com.thiago.gerenciador.dao.TarefaDao;
import br.com.thiago.gerenciador.model.Tarefa;
import br.com.thiago.gerenciador.model.enums.Situacao;

import java.util.List;

public class TarefaService {

    private TarefaDao tarefaDao = new TarefaDao();

    public void salvar(Tarefa tarefa){
        if(tarefa.getId() == null){
            tarefaDao.salvar(tarefa);

        }else {
            tarefaDao.atualizar(tarefa);

        }
    }

    public void excluir(Tarefa tarefa){
        tarefaDao.remover(tarefa);
    }

    public Tarefa listarPorId(Long id){
        return tarefaDao.buscarPorId(id);
    }

    public List<Tarefa> listarTodas(){
        return tarefaDao.listarTodas();
    }

    public List<Tarefa> listarComFiltros(String numeroId, String tituloDescricao, String responsavel, Situacao situacao){
        return tarefaDao.listarComFiltros(numeroId, tituloDescricao, responsavel, situacao);
    }

    public List<Tarefa> listarPorSituacao(Situacao situacao){
        return tarefaDao.listarPorSituacao(situacao);
    }

    public List<Tarefa> listarPorResponsavel(String responsavel){
        return tarefaDao.listarPorResponsavel(responsavel);
    }
}
