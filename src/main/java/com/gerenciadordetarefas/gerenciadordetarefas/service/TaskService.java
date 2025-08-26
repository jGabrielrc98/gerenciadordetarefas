package com.gerenciadordetarefas.gerenciadordetarefas.service;


import com.gerenciadordetarefas.gerenciadordetarefas.model.Task;
import com.gerenciadordetarefas.gerenciadordetarefas.model.enums.Prioridade;
import com.gerenciadordetarefas.gerenciadordetarefas.model.enums.Status;
import com.gerenciadordetarefas.gerenciadordetarefas.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Transactional
    public Task save(Task task) {
        Task savedTask = taskRepository.save(task);

        return savedTask;
    }

    @Transactional
    public List<Task> listsTaksUser(UUID usuarioId) {
        return taskRepository.findByUsuarioId(usuarioId);
    }

    @Transactional
    public List<Task> listPrioridade(Prioridade prioridade) {
        return taskRepository.findByPrioridade(prioridade);
    }

    @Transactional
    public List<Task> listStatus( Status status) {
        return taskRepository.findByStatus(status);
    }

    @Transactional
    public List<Task> listStatusByUser(UUID usuarioId, Status status) {
        return taskRepository.findByUsuarioIdAndStatus(usuarioId, status);
    }

    @Transactional
    public void deleteTask(UUID usuarioId, UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
        // Verifica se a tarefa pertence ao usuário
        if (!task.getUsuario().equals(usuarioId)) {
            throw new RuntimeException("Você não pode deletar tarefas de outro usuário");
        }

        taskRepository.delete(task);
    }

    @Transactional
    public Task updateTask(UUID usuarioId, UUID taskId, Task updatedTask) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        // Verifica se a tarefa pertence ao usuário
        if (!task.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("Você não pode editar tarefas de outro usuário");
        }

        // Atualiza apenas se o valor novo não for nulo
        if (updatedTask.getTitulo() != null) {
            task.setTitulo(updatedTask.getTitulo());
        }

        if (updatedTask.getDescricao() != null) {
            task.setDescricao(updatedTask.getDescricao());
        }

        if (updatedTask.getPrioridade() != null) {
            task.setPrioridade(updatedTask.getPrioridade());
        }

        if (updatedTask.getStatus() != null) {
            task.setStatus(updatedTask.getStatus());
        }

        if (updatedTask.getDataPrazo() != null) {
            task.setDataPrazo(updatedTask.getDataPrazo());
        }

        return taskRepository.save(task);
    }








}
