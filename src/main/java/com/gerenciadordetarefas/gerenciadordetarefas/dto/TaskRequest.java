package com.gerenciadordetarefas.gerenciadordetarefas.dto;

import java.time.LocalDateTime;

public class TaskRequest {
    private String titulo;
    private String descricao;
    private LocalDateTime dataPrazo;

    // getters e setters
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public LocalDateTime getDataPrazo() { return dataPrazo; }
    public void setDataPrazo(LocalDateTime dataPrazo) { this.dataPrazo = dataPrazo; }
}

