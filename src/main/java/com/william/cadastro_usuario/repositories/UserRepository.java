package com.william.cadastro_usuario.repositories;

import com.meva.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByCpf(String cpf);
    Optional<User> findByCpf(String cpf); // Adicionado para buscar por CPF
}
