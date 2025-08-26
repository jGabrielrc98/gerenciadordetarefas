package com.gerenciadordetarefas.gerenciadordetarefas.service;


import com.gerenciadordetarefas.gerenciadordetarefas.model.User;
import com.gerenciadordetarefas.gerenciadordetarefas.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User save(User user) {
        User newUser = userRepository.save(user);

    return  newUser;
    }

    @Transactional
    public User login(String email, String senha) {
        // Busca o usuário pelo e-mail
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Verifica se a senha bate
        if (!user.getSenha().equals(senha)) {
            throw new RuntimeException("Senha incorreta");
        }

        return user;
    }
    @Transactional
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }



}
