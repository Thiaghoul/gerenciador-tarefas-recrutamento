package br.com.thiago.gerenciador.controller;

import br.com.thiago.gerenciador.model.Tarefa;
import br.com.thiago.gerenciador.model.enums.Situacao;
import br.com.thiago.gerenciador.service.TarefaService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ManagedBean
@ViewScoped
public class TarefaListagemBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private TarefaService tarefaService;
    private List<Tarefa> tarefasFiltradas;


    private String filtroNumero;
    private String filtroTituloDescricao;
    private String filtroResponsavel;
    private Situacao filtroSituacao;

    private List<String> responsaveis;

    public TarefaListagemBean(){

    }

    @PostConstruct
    public void init(){
        this.tarefaService =  new TarefaService();
        this.tarefasFiltradas = new ArrayList<>();
        this.responsaveis = Arrays.asList("Thiago","Rubens","Pedro","Samuel");
    }

    public void buscar(){
        System.out.println("Tarefa listagem bean: buscar()");

        try{
            this.tarefasFiltradas = tarefaService.listarTodas();
            if(this.tarefasFiltradas.isEmpty()){
                System.out.println("Nenhuma tarefa encontrada");

            }else{
                System.out.println(this.tarefasFiltradas.size() + " tarefa(s) encontrada(s).");

            }
        }catch (Exception e){
            System.out.println("Erro ao buscar tarefas: " + e.getMessage());
            e.printStackTrace();
            this.tarefasFiltradas = new ArrayList<>();

        }
    }


    public void excluir(Long id){
        System.out.println("TarefaListagemBean: excluir() para id: "+ id);
        FacesContext context = FacesContext.getCurrentInstance();
        try{
            if(id == null){
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Atenção!",
                        "ID da tarefa inválido para exclusão"));
                return;
            }

            Tarefa tarefaExcluir = tarefaService.listarPorId(id);

            if(tarefaExcluir != null){
                tarefaService.excluir(tarefaExcluir);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Sucesso!",
                        "Tarefa Excluida com sucesso!"));
                buscar();

            } else{
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Erro!",
                        "Tarefa não encontrada nesde id: " + id));

            }
        }catch (Exception e){
            System.err.println("Erro ao excluir tarefa: " + e.getMessage());
            e.printStackTrace();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro ao excluir!",
                    "Ocorreu um problema ao tentar excluir a tarefa."));
        }
    }

    public void concluir(Long id){
        System.out.println("TarefaListagemBean: concluir() para id: "+ id);
        FacesContext context = FacesContext.getCurrentInstance();
        try{
            if(id == null){
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Atenção!",
                        "ID da tarefa inválido para conclusão."));

            }

            Tarefa tarefaConcluir = tarefaService.listarPorId(id);

            if(tarefaConcluir != null){
                if(tarefaConcluir.getSituacao() == Situacao.CONCLUIDA){
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Informação",
                            "Tarefa já está concluida."));

                }else{
                    tarefaConcluir.setSituacao(Situacao.CONCLUIDA);
                    tarefaService.salvar(tarefaConcluir);
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Sucesso!",
                            "Tarefa concluida com sucesso."));
                }
                buscar();

            }else{
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Erro!",
                        "Tarefa não encontrada para conclusão. id: "+ id));
            }
        }catch (Exception e){
            System.err.println("Erro ao concluir tarefa: " + e.getMessage());
            e.printStackTrace();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro ao concluir!",
                    "Ocorreu um problema ao tentar concluir a tarefa."));
        }
    }

    public List<Tarefa> getTarefasFiltradas() {
        return tarefasFiltradas;
    }

    public void setTarefasFiltradas(List<Tarefa> tarefasFiltradas) {
        this.tarefasFiltradas = tarefasFiltradas;
    }

    public String getFiltroNumero() {
        return filtroNumero;
    }

    public void setFiltroNumero(String filtroNumero) {
        this.filtroNumero = filtroNumero;
    }

    public String getFiltroTituloDescricao() {
        return filtroTituloDescricao;
    }

    public void setFiltroTituloDescricao(String filtroTituloDescricao) {
        this.filtroTituloDescricao = filtroTituloDescricao;
    }

    public String getFiltroResponsavel() {
        return filtroResponsavel;
    }

    public void setFiltroResponsavel(String filtroResponsavel) {
        this.filtroResponsavel = filtroResponsavel;
    }

    public Situacao getFiltroSituacao() {
        return filtroSituacao;
    }

    public void setFiltroSituacao(Situacao filtroSituacao) {
        this.filtroSituacao = filtroSituacao;
    }

    public List<String> getResponsaveis() {
        return responsaveis;
    }

    public Situacao[] getSituacoesDisponiveis() {
        return Situacao.values();
    }
}
