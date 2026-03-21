package com.william.cadastro_usuario.services;


import com.william.cadastro_usuario.dtos.FamilyDTO;
import com.william.cadastro_usuario.dtos.request.UserCreateRequest;
import com.william.cadastro_usuario.dtos.request.UserUpdateRequest;
import com.william.cadastro_usuario.dtos.response.UserResponse;
import com.william.cadastro_usuario.entities.Family;
import com.william.cadastro_usuario.entities.User;
import com.william.cadastro_usuario.exceptions.CpfAlreadyRegisteredException;
import com.william.cadastro_usuario.exceptions.FamilyDescriptionRequiredException;
import com.william.cadastro_usuario.exceptions.FamilyNotFoundException;
import com.william.cadastro_usuario.exceptions.UserNotFoundException;
import com.william.cadastro_usuario.repositories.FamilyRepository;
import com.william.cadastro_usuario.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;

    @Transactional
    public UserResponse createUser(UserCreateRequest request) {

        //valida se CPF existe
        if (userRepository.existsByCpf(request.getCpf())) {
            throw new CpfAlreadyRegisteredException("CPF " + request.getCpf() + " já cadastrado.");
        }

        //determina se já está vinculado a uma família ou se vai criar uma nova
        Family family = validFamily(request.getFamily());

        User user = new User();
        BeanUtils.copyProperties(request, user); //copia propriedades do DTO para entidade
        user.setFamily(family);

        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(
                "Usuário com ID " + id + " não encontrado."
        ));
        return new UserResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(UserResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(
                "Usuário com ID " + id + " não encontrado."
        ));

        //CPF não é atualizado
        BeanUtils.copyProperties(request, existingUser, "cpf", "id"); //Ignora CPF e ID ao copiar

        if (request.getFamily() != null) {
            Family family = validFamily(request.getFamily());
            existingUser.setFamily(family);
        }

        User updatedUser = userRepository.save(existingUser);
        return new UserResponse(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(
                    "Usuário com ID " + id + " não encontrado."
            );
        }
        userRepository.deleteById(id);
    }

    /*
    Se familyDTO().getId() for fornecido, tenta buscar a família existente, se não for fornecido,
    mas familyDTO.getDescription() for, tenta buscar pela descrição. Se não encontrar, cria nova família.
    Se nenhum dos dois for fornecido, ou se familyDTO for nulo, lança exceção.
    A descrição da família só é obrigatória se for para criar uma nova família.
     */
    private Family validFamily(FamilyDTO familyDTO) {
        if (familyDTO == null) {
            throw new FamilyDescriptionRequiredException("Informações da família são obrigatórias.");
        }

        //Se o ID for 0, trata como intenção de criar nova família
        if (familyDTO.getId() != null && familyDTO.getId() == 0) {
            if (familyDTO.getDescription() == null || familyDTO.getDescription().trim().isEmpty()) {
                throw new FamilyDescriptionRequiredException(
                        "Para criar uma nova família (ID=0), a descrição é obrigatória.");
            }
            // Tenta encontrar por descrição para evitar duplicidade antes de criar
            Optional<Family> existingFamily = familyRepository.findByDescription(familyDTO.getDescription());
            return existingFamily.orElseGet(() -> {
                Family newFamily = new Family();
                newFamily.setDescription(familyDTO.getDescription());
                return familyRepository.save(newFamily);
            });
        }
        // Se o ID for fornecido e diferente de 0, tenta buscar a família existente
        else if (familyDTO.getId() != null) {
            return familyRepository.findById(familyDTO.getId())
                    .orElseThrow(() -> new FamilyNotFoundException(
                            "Família com ID " + familyDTO.getId() + " não encontrada."));
        }
        // Se não tem ID (nulo) mas a descrição é fornecida, tenta buscar ou criar
        else if (familyDTO.getDescription() != null && !familyDTO.getDescription().trim().isEmpty()) {
            Optional<Family> existingFamily = familyRepository.findByDescription(familyDTO.getDescription());
            return existingFamily.orElseGet(() -> {
                Family newFamily = new Family();
                newFamily.setDescription(familyDTO.getDescription());
                return familyRepository.save(newFamily);
            });
        } else {
            // Se não forneceu ID (nem 0, nem outro válido) nem descrição válida para criação
            throw new FamilyDescriptionRequiredException(
                    "Para criar uma nova família ou vincular, é necessário fornecer o ID da família existente ou a descrição da nova família.");
        }
    }
}
