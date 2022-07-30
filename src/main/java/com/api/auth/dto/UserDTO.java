package com.api.auth.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
    private List<RoleDTO> roles  = new ArrayList<>();

    public UserDTO(UserModel model) {
        parseDTO(model);
    }

    private void parseDTO(UserModel model) {
        setId(model.getId());
        setName(model.getName());
        setUserName(model.getUserName());
        setPassword(model.getPassword());
        setRoles(
            parseRoles(model.getRoles(),
            el -> new RoleDTO(el)
            ));
    }

    public UserModel parseModel() {
        return new UserModel(
            id, name, userName, password,
            parseRoles(roles,el -> el.parseModel())
            );
    }

    private <T,O> List<O>  parseRoles(List<T> roles, Function<T, O> mapper) {
        return roles.stream().map(mapper).collect(Collectors.toList());
    }
}
