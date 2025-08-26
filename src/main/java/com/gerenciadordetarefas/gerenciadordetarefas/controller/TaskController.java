package com.gerenciadordetarefas.gerenciadordetarefas.controller;

import com.gerenciadordetarefas.gerenciadordetarefas.dto.TaskRequest;
import com.gerenciadordetarefas.gerenciadordetarefas.model.Task;
import com.gerenciadordetarefas.gerenciadordetarefas.model.User;
import com.gerenciadordetarefas.gerenciadordetarefas.model.enums.Status;
import com.gerenciadordetarefas.gerenciadordetarefas.service.TaskService;
import com.gerenciadordetarefas.gerenciadordetarefas.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping({"/tasks"})
public class TaskController {
    private TaskService taskService;
    private UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;

    }

    @PostMapping("/{usuarioId}")
    public ResponseEntity<Task> createTask(
            @PathVariable UUID usuarioId,
            @RequestBody TaskRequest  taskRequest) {
        //BUSCAR O USUARIO
        User usuario = userService.getUserById(usuarioId);

        Task task = new Task();
        task.setTitulo(taskRequest.getTitulo());
        task.setDescricao(taskRequest.getDescricao());
        task.setDataPrazo(taskRequest.getDataPrazo());
        task.setUsuario(usuario); // associa a tarefa ao usuário

        Task savedTask = taskService.save(task);

        return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
    }

    @GetMapping("/user/{usuarioId}/status/{status}")
    public ResponseEntity<List<Task>> listTasksByUserAndStatus(
            @PathVariable UUID usuarioId,
            @PathVariable Status status
    ) {
        List<Task> tasks = taskService.listStatusByUser(usuarioId, status);
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{usuarioId}/{taskId}")
    public ResponseEntity<Task> updateTask(
            @PathVariable UUID usuarioId,
            @PathVariable UUID taskId,
            @RequestBody TaskRequest taskRequest
    ) {
        Task updatedTask = new Task();
        updatedTask.setTitulo(taskRequest.getTitulo());
        updatedTask.setDescricao(taskRequest.getDescricao());
        updatedTask.setDataPrazo(taskRequest.getDataPrazo());

        Task task = taskService.updateTask(usuarioId, taskId, updatedTask);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{usuarioId}/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable UUID usuarioId,
            @PathVariable UUID taskId
    ) {
        taskService.deleteTask(usuarioId, taskId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{usuarioId}")
    public String showTasks(
            @PathVariable UUID usuarioId,
            Model model) {

        // 1. Pega a lista de tarefas do usuário
        List<Task> tasks = taskService.listsTaksUser(usuarioId);

        // 2. Adiciona a lista de tarefas ao modelo para o Thymeleaf
        model.addAttribute("tasks", tasks);

        // 3. Adiciona um objeto TaskRequest vazio para o formulário
        model.addAttribute("taskRequest", new TaskRequest());

        // 4. Adiciona o ID do usuário para o formulário
        model.addAttribute("usuarioId", usuarioId);

        // 5. Retorna o nome do template Thymeleaf (tasks.html)
        return "tasks";
    }




}
