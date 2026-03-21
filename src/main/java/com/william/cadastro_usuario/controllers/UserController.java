package com.william.cadastro_usuario.controllers;


import com.william.cadastro_usuario.dtos.request.UserCreateRequest;
import com.william.cadastro_usuario.dtos.request.UserUpdateRequest;
import com.william.cadastro_usuario.dtos.response.UserResponse;
import com.william.cadastro_usuario.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/selectIdFamily/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok().body(users);//deve aparecer 200 ok
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();//retorna 204
    }

    @PostMapping("/register")
    public ResponseEntity <UserResponse> createUser(@Valid @RequestBody UserCreateRequest request){

        UserResponse newUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
                                                   @Valid @RequestBody UserUpdateRequest request){

        UserResponse updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok().body(updatedUser);
    }
}
