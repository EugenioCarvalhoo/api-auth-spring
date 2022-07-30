package com.api.auth.dto;

import java.util.ArrayList;
import java.util.List;

import com.api.auth.model.RoleModel;
import com.api.auth.model.UserModel;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String userName;
    private String password;
    @Setter(value = AccessLevel.PRIVATE)
    private List<RoleModel> roles  = new ArrayList<>();

    public UserDTO(UserModel model) {
        parseDTO(model);
    }

    private UserDTO parseDTO(UserModel model) {
        setId(model.getId());
        setName(model.getName());
        setUserName(model.getUserName());
        setPassword(model.getPassword());
        setRoles(model.getRoles());
        return this;
    }

    public UserModel parseModel() {
        return new UserModel(
            id, name, userName, password, roles);
    }
}
