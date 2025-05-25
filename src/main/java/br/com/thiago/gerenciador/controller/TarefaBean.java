package br.com.thiago.gerenciador.controller;

import br.com.thiago.gerenciador.model.Tarefa;
import br.com.thiago.gerenciador.model.enums.Prioridade;
import br.com.thiago.gerenciador.service.TarefaService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@ManagedBean
@ViewScoped
public class TarefaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Tarefa tarefa;
    private List<String> responsaveis;
    private Date deadlineFormulario;
    private TarefaService tarefaService;

    public TarefaBean(){
        System.out.println("---TarefaBean instanciado---");
    }

    @PostConstruct
    public void init(){
        this.tarefa = new Tarefa();
        this.responsaveis = Arrays.asList("Thiago","Rubens","Pedro","Samuel");
        this.tarefaService = new TarefaService();
    }

    public String salvar(){
        System.out.println("TarefaBean; salar() chamado.");
        try{
            if(this.deadlineFormulario != null){
                this.tarefa.setDeadline(
                        Instant.ofEpochMilli(this.deadlineFormulario.getTime())
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                );
            }else{
                this.tarefa.setDeadline(null);
            }
            this.tarefaService.salvar(this.tarefa);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Tarefa cadastrada com sucesso!"));

            this.tarefa = new Tarefa();
            this.deadlineFormulario = null;

            //return "listagemTarefas?faces-redirect=true";
            return null;

        } catch (Exception e) {
            System.err.println("erro ao salvar tarefa: "+ e.getMessage());
            e.printStackTrace();

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar!",
                    "Ocorreu um problema ao tentar salvar a tarefa. Detalhes: " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null,msg);

            return null;
        }
    }

    public Tarefa getTarefa(){
        return tarefa;
    }

    public void setTarefa(Tarefa tarefa){
        this.tarefa = tarefa;
    }

    public List<String> getResponsaveis(){
        return responsaveis;
    }

    public Prioridade[] getPrioridades(){
        return Prioridade.values();
    }

    public Date getDeadlineFormulario(){
        if (deadlineFormulario == null && tarefa != null && tarefa.getDeadline() != null){
            return Date.from(tarefa.getDeadline().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        return deadlineFormulario;
    }

    public void setDeadlineFormulario(Date deadlineFormulario) {
        this.deadlineFormulario = deadlineFormulario;
    }
}
