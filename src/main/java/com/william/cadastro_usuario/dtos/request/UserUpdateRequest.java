package com.william.cadastro_usuario.dtos.request;


import com.william.cadastro_usuario.dtos.FamilyDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {

    // CPF não é informado na atualização
    private String name;
    private String genre;
    @Past(message = "Data de nascimento deve ser no passado")
    private LocalDate barth; //birth
    private String state;
    private String city;

    @Valid
    private FamilyDTO family; // Pode conter id ou description
}
