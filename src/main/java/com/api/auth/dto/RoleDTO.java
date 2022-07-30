package com.api.auth.dto;

import com.api.auth.model.RoleModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleDTO {
    private Long id;
    private String name;

    public RoleDTO(RoleModel roleModel) {
        parseDTO(roleModel);
    }

    private void parseDTO(RoleModel roleModel) {
        setId(roleModel.getId());
        setName(roleModel.getName());

    }

    public RoleModel parseModel() {
        return new RoleModel(id, name);
    }

    
}
