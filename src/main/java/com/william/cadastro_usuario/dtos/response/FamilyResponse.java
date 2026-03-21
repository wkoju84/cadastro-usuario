package com.william.cadastro_usuario.dtos.response;

import com.meva.user.entities.Family;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FamilyResponse {

    private Long id;
    private String description;

    public FamilyResponse(Family family) {
        this.id = family.getId();
        this.description = family.getDescription();
    }

}
