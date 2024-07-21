package com.wineko.api.controller;

import com.wineko.api.dto.IdentificationDto;
import com.wineko.api.dto.UserDto;
import com.wineko.api.dto.UserInfoDto;
import com.wineko.api.model.Users;
import com.wineko.api.service.UsersService;
import jakarta.servlet.http.HttpSession;
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



    @GetMapping("/info")
    public ResponseEntity<UserInfoDto> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.out.println("Authenticated username: " + username);

        Users user = usersService.findByUsername(username);

        if (user == null) {
            System.out.println("User not found for username: " + username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        UserInfoDto userInfoDto = new UserInfoDto(user.getId(), user.getEmail());
        return ResponseEntity.ok(userInfoDto);
    }
}
