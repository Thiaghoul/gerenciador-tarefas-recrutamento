package br.com.thiago.gerenciador.model;

import br.com.thiago.gerenciador.model.enums.Prioridade;
import br.com.thiago.gerenciador.model.enums.Situacao;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tb_tarefa")
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descricao")
    private String descricao;

    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    @Column(name = "responsavel")
    private String responsavel;

    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    private Situacao situacao;

    public Tarefa(){

    }

    public Tarefa(String titulo, String descricao, Prioridade prioridade,
                  String responsavel, LocalDate deadline){
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.responsavel = responsavel;
        this.situacao = Situacao.EM_ANDAMENTO;
        this.deadline = deadline;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }
}
