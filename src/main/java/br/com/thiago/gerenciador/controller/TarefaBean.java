package br.com.thiago.gerenciador.controller;

import br.com.thiago.gerenciador.model.Tarefa;
import br.com.thiago.gerenciador.model.enums.Prioridade;
import br.com.thiago.gerenciador.model.enums.Situacao;
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
import java.util.Map;

@ManagedBean
@ViewScoped
public class TarefaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Tarefa tarefa;
    private List<String> responsaveis;
    private Date deadlineFormulario;
    private TarefaService tarefaService;

    private boolean modoEdicao = false;

    public TarefaBean(){
        System.out.println("---TarefaBean instanciado---");
    }

    @PostConstruct
    public void init(){
        this.tarefa = new Tarefa();
        this.responsaveis = Arrays.asList("Thiago","Rubens","Pedro","Samuel");
        this.tarefaService = new TarefaService();

        FacesContext faces = FacesContext.getCurrentInstance();
        Map<String, String> params = faces.getExternalContext().getRequestParameterMap();
        String idParam = params.get("id");

        if(idParam != null && !idParam.isEmpty()){
            try{
                Long id = Long.parseLong(idParam);
                this.tarefa = tarefaService.listarPorId(id);
                if(this.tarefa != null){
                    this.modoEdicao = true;
                    if(this.tarefa.getDeadline() != null){
                        this.deadlineFormulario = Date.from(this.tarefa.getDeadline()
                                .atStartOfDay(ZoneId.systemDefault()).toInstant());
                    }
                    System.out.println("Modo edição: editar tareda com id: "+ id);
                }else {
                    System.out.println("Tarefa id: "+ id + " não encontrada. Iniciando novo cadastro");
                    this.tarefa = new Tarefa();
                    this.modoEdicao = false;
                }
            }catch (NumberFormatException e){
                System.err.println("Id de tarefa inválido na URL: " + idParam );
                this.tarefa = new Tarefa();
                this.modoEdicao = false;
                System.out.println("Modo novo vadastro");
            }
        }
    }

    public String salvar(){
        System.out.println("TarefaBean salvar() chamado. Modo edição: "+ modoEdicao);
        FacesContext context = FacesContext.getCurrentInstance();
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
            this.tarefa.setSituacao(Situacao.EM_ANDAMENTO);
            this.tarefaService.salvar(this.tarefa);

            String mensagemSucesso = modoEdicao ? "Tarefa atualizada com sucesso.": "Tarefa cadastrada com sucesso.";

            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Sucesso!",
                            mensagemSucesso));

            if(!modoEdicao){
                this.tarefa = new Tarefa();
                this.deadlineFormulario = null;

            }else{
                return "listagemTarefas?faces-redirect=true";

            }
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

    public boolean isModoEdicao() {
        return modoEdicao;
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
