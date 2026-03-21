package com.william.cadastro_usuario.dtos.request;

import com.meva.user.dtos.FamilyDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {

    @CPF(message = "CPF inválido")
    @NotBlank(message = "CPF é obrigatório")
    private String cpf;

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    private String genre;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "Data de nascimento deve ser no passado")
    private LocalDate barth; //birth

    private String state;
    private String city;

    @NotNull(message = "Informações da família são obrigatórias")
    @Valid
    private FamilyDTO family;
}
