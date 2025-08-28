package com.gerenciadordetarefas.gerenciadordetarefas.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gerenciadordetarefas.gerenciadordetarefas.model.enums.Prioridade;
import com.gerenciadordetarefas.gerenciadordetarefas.model.enums.Status;
import com.gerenciadordetarefas.gerenciadordetarefas.model.interfaces.Identifiable;
import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_task")
@Component
public class Task implements Identifiable, Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String titulo;

    private String descricao;

    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime dataCriacao;
    private LocalDateTime dataPrazo;

    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tb_IdUser")
    @JsonBackReference
    private User usuario;

    //Nova FIeld
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criador-id")
    private User criador;


    public Task() {}

    public Task(String titulo, String descricao, Prioridade prioridade, Status status, LocalDateTime dataCriacao, LocalDateTime dataPrazo, User usuario) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.dataPrazo = dataPrazo;
        this.usuario = usuario;
    }

    //metodo que roda antes de salvar no banco de dados
    @PrePersist
    public void prePersist() {
        this.dataCriacao = LocalDateTime.now();
        if (this.status == null) {
            this.status = Status.PENDENTE;
        }
        if (this.prioridade == null) {
            this.prioridade = Prioridade.MEDIA;
        }
    }

    // Implementação da interface Identifiable
    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    // getters e setters
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataPrazo() {
        return dataPrazo;
    }

    public void setDataPrazo(LocalDateTime dataPrazo) {
        this.dataPrazo = dataPrazo;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public User getCriador() {
        return criador;
    }

    public void setCriador(User criador) {
        this.criador = criador;
    }

    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDateTime dataConclusao) {
        this.dataConclusao = dataConclusao;
    }
}
