package com.gerenciadordetarefas.gerenciadordetarefas.repository;

import com.gerenciadordetarefas.gerenciadordetarefas.model.Task;
import com.gerenciadordetarefas.gerenciadordetarefas.model.enums.Prioridade;
import com.gerenciadordetarefas.gerenciadordetarefas.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

    public interface TaskRepository extends JpaRepository<Task, UUID> {

        List<Task> findByPrioridade(Prioridade prioridade);
        List<Task> findByUsuarioId(UUID usuarioId);
        List<Task> findByStatus(Status status);
         List<Task> findByUsuarioIdAndStatus(UUID usuarioId, Status status);

         //Novos metodos

        List<Task> findByCriadorId(UUID criadorId);

        //Encontra tarefas onde usuario e responsavel ou criador
        @Query("SELECT t FROM Task t WHERE t.usuario.id = :usuarioId OR t.criador.id = :usuarioId")
        List<Task> findByUsuarioIdAndStatus(UUID usuarioId, UUID status);

        //Encontrar tarefas criadas por um usuario para um responsavel especifico
        @Query("SELECT t FROM Task t WHERE t.criador.id = :criadorId AND t.usuario.id = :responsavelId")
        List<Task> findByCriadorIdAndUsuarioId(@Param("criadorId") UUID criadorId, @Param("responsavelId") UUID responsavelId);

        //Encontra tarefas atribuídas a um usuário por outros usuários (não próprias)
        @Query("SELECT t FROM Task t WHERE t.usuario.id = :usuarioId AND (t.criador IS NOT NULL AND t.criador.id != :usuarioId)")
        List<Task> findTarefasAtribuidasPorOutros(@Param("usuarioId") UUID usuarioId);

        //Encontra tarefas próprias (criadas pelo próprio usuário para si mesmo)
        @Query("SELECT t FROM Task t WHERE t.usuario.id = :usuarioId AND (t.criador IS NULL OR t.criador.id = :usuarioId)")
        List<Task> findTarefasProprias(@Param("usuarioId") UUID usuarioId);

        //Encontra tarefas por status e criador
        List<Task> findByCriadorIdAndStatus(UUID criadorId, Status status);

        //Encontra tarefas pendentes atribuídas a um usuário
        @Query("SELECT t FROM Task t WHERE t.usuario.id = :usuarioId AND t.status = :status")
        List<Task> findPendingTasksForUser(@Param("usuarioId") UUID usuarioId, @Param("status") Status status);

        //Conta quantas tarefas pendentes um usuário tem
        @Query("SELECT COUNT(t) FROM Task t WHERE t.usuario.id = :usuarioId AND t.status = 'PENDENTE'")
        long countPendingTasksByUser(@Param("usuarioId") UUID usuarioId);

        //Conta quantas tarefas um usuário criou para outros
        @Query("SELECT COUNT(t) FROM Task t WHERE t.criador.id = :criadorId AND t.criador.id != t.usuario.id")
        long countTasksCreatedForOthers(@Param("criadorId") UUID criadorId);



        @Query("SELECT t FROM Task t WHERE t.usuario.id = :usuarioId OR t.criador.id = :criadorId")
        List<Task> findByUsuarioIdOrCriadorId(@Param("usuarioId") UUID usuarioId, @Param("criadorId") UUID criadorId);



    }


