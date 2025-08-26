package com.gerenciadordetarefas.gerenciadordetarefas.repository;

import com.gerenciadordetarefas.gerenciadordetarefas.model.Task;
import com.gerenciadordetarefas.gerenciadordetarefas.model.enums.Prioridade;
import com.gerenciadordetarefas.gerenciadordetarefas.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

    public interface TaskRepository extends JpaRepository<Task, UUID> {

        List<Task> findByPrioridade(Prioridade prioridade);
        List<Task> findByUsuarioId(UUID usuarioId);
        List<Task> findByStatus(Status status);
         List<Task> findByUsuarioIdAndStatus(UUID usuarioId, Status status);


    }


