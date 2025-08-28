package com.gerenciadordetarefas.gerenciadordetarefas.service;

import com.gerenciadordetarefas.gerenciadordetarefas.model.Task;
import com.gerenciadordetarefas.gerenciadordetarefas.model.User;
import com.gerenciadordetarefas.gerenciadordetarefas.model.enums.Prioridade;
import com.gerenciadordetarefas.gerenciadordetarefas.model.enums.Status;
import com.gerenciadordetarefas.gerenciadordetarefas.repository.TaskRepository;
import com.gerenciadordetarefas.gerenciadordetarefas.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;


    @Transactional
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    //novas funcionalidaas para que um outra pessoa crie uma tarefa para outra pessoa
    @Transactional
    public Task criarTarefaParaOutroUsuario(UUID criadorId, UUID responsavelId, Task task) {
        // Verifica se ambos os usuários existem
        User criador = userRepository.findById(criadorId)
                .orElseThrow(() -> new RuntimeException("Usuário criador não encontrado"));

        User responsavel = userRepository.findById(responsavelId)
                .orElseThrow(() -> new RuntimeException("Usuário responsável não encontrado"));

        // Define quem criou e quem é responsável pela tarefa
        task.setCriador(criador);
        task.setUsuario(responsavel); // O responsável pela execução
        task.setDataCriacao(LocalDateTime.now());

        // Define status inicial como PENDENTE se não foi especificado
        if (task.getStatus() == null) {
            task.setStatus(Status.PENDENTE);
        }

        return taskRepository.save(task);
    }

    //nova funcionalidade Lista tarefas criadas por um usuário para outros
    @Transactional
    public List<Task> listarTarefasCriadasPorMim(UUID criadorId) {
        return taskRepository.findByCriadorId(criadorId);
    }

    //nova funcionalidade Lista tarefas que outros usuários criaram para mim
    @Transactional
    public List<Task> listarTarefasAtribuidasParaMim(UUID responsavelId) {
        return taskRepository.findByUsuarioId(responsavelId);
    }


    @Transactional
    public Task marcarComoConcluida(UUID usuarioId, UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        // Verifica se o usuário tem permissão (é o responsável OU o criador)
        boolean podeMarcarConcluida = task.getUsuario().getId().equals(usuarioId) ||
                (task.getCriador() != null && task.getCriador().getId().equals(usuarioId));

        if (!podeMarcarConcluida) {
            throw new RuntimeException("Você não tem permissão para marcar esta tarefa como concluída");
        }

        task.setStatus(Status.CONCLUIDA);
        task.setDataConclusao(LocalDateTime.now());

        return taskRepository.save(task);
    }

    @Transactional
    public Task updateTask(UUID usuarioId, UUID taskId, Task updatedTask) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        // Verifica se o usuário tem permissão (é o responsável OU o criador)
        boolean podeEditar = task.getUsuario().getId().equals(usuarioId) ||
                (task.getCriador() != null && task.getCriador().getId().equals(usuarioId));

        if (!podeEditar) {
            throw new RuntimeException("Você não pode editar esta tarefa");
        }

        // Atualiza apenas campos não nulos
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

    @Transactional
    public void deleteTask(UUID usuarioId, UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        // Verifica se o usuário tem permissão (é o responsável OU o criador)
        boolean podeDeletar = task.getUsuario().getId().equals(usuarioId) ||
                (task.getCriador() != null && task.getCriador().getId().equals(usuarioId));

        if (!podeDeletar) {
            throw new RuntimeException("Você não pode deletar esta tarefa");
        }

        taskRepository.delete(task);
    }

    // Métodos originais mantidos
    @Transactional
    public List<Task> listsTaksUser(UUID usuarioId) {
        return taskRepository.findByUsuarioId(usuarioId);
    }

    @Transactional
    public List<Task> listPrioridade(Prioridade prioridade) {
        return taskRepository.findByPrioridade(prioridade);
    }

    @Transactional
    public List<Task> listStatus(Status status) {
        return taskRepository.findByStatus(status);
    }

    @Transactional
    public List<Task> listStatusByUser(UUID usuarioId, Status status) {
        return taskRepository.findByUsuarioIdAndStatus(usuarioId, status);
    }

    //nova funcionalidade Buscar usuário por email para facilitar atribuição de tarefas
    @Transactional
    public User buscarUsuarioPorEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com este email"));
    }

    //nova funcionalidade Lista todas as tarefas relacionadas a um usuário
    @Transactional
    public List<Task> listarTodasTarefasRelacionadas(UUID usuarioId) {
        return taskRepository.findByUsuarioIdOrCriadorId(usuarioId, usuarioId);
    }
}