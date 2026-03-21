package com.william.cadastro_usuario.dtos.response;

import com.meva.user.dtos.FamilyDTO;
import com.meva.user.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long id;
    private String cpf;
    private String name;
    private String genre;
    private LocalDate barth;
    private String state;
    private String city;
    private FamilyDTO family;

    public UserResponse(User user) {
        this.id = user.getId();
        this.cpf = user.getCpf();
        this.name = user.getName();
        this.genre = user.getGenre();
        this.barth = user.getBarth();
        this.state = user.getState();
        this.city = user.getCity();
        this.family = new FamilyDTO(user.getFamily().getId(), user.getFamily().getDescription());
    }
}
