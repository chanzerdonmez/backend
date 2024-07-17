package com.wineko.api.controller;

import com.wineko.api.dto.UserDto;
import com.wineko.api.model.Users;
import com.wineko.api.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UsersService usersService;

//    @GetMapping("/users")
//    public String getAllUsers() {
//        return "hgfhgfhgf";
//    }

//    @GetMapping("/users")
//    public ResponseEntity<List<Users>> getAllUsers() {
//        System.out.println(usersService.findAll());
//        List<Users> users = usersService.findAll();
//        return ResponseEntity.ok(users);
//    }

    @GetMapping("/users/get/all")
    public List<Users> allUsers(){
        return this.usersService.getAll();
    }

    @GetMapping("/get/{id}")
    public Users usersById(@PathVariable Integer id){
        return this.usersService.getById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable Integer id, @RequestBody Users user) {
        Users updatedUser = usersService.update(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String currentUserName = authentication.getName();
        Users currentUser = usersService.findByEmail(currentUserName);

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        UserDto userDto = new UserDto();
        userDto.setId(currentUser.getId());
        userDto.setName(currentUser.getName());
        userDto.setFirstName(currentUser.getFirstName());
        userDto.setEmail(currentUser.getEmail());
        userDto.setPassword(currentUser.getPassword());

        return ResponseEntity.ok(userDto);
    }


}
