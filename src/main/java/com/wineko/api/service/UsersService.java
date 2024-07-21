package com.wineko.api.service;

import com.wineko.api.model.Article;
import com.wineko.api.model.Users;
import com.wineko.api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class UsersService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    public Users save(Users users){
        return userRepository.save(users);
    }

    public Users findByEmail(String email){
        if (email == null) {
            return null;
        }
        return userRepository.findByEmail(email.toLowerCase().trim());
    }

    public Users findByToken(String token){
        if (token == null) {
            return null;
        }
        return userRepository.findByToken(token);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByEmailLikeIgnoreCase(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

//    public List<Users> findAll() {
//        return userRepository.findAll();
//    }

//    public Users update(Integer id, Users updated){
//        updated.setId(id);
//        return this.userRepository.save(updated);
//    }

    public Users update(Integer id, Users updated){
        Users existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Mettre à jour les champs nécessaires
        existingUser.setName(updated.getName());
        existingUser.setFirstName(updated.getFirstName());
        existingUser.setEmail(updated.getEmail());
        existingUser.setPassword(updated.getPassword());
        existingUser.setRole(updated.getRole()); // Mise à jour du rôle

        return userRepository.save(existingUser);
    }
    public Users getCurrentUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email);
    }
    public List<Users> getAll(){

        List<Users> users;
        try {
            users =  this.userRepository.findAll();
        } catch(Exception exception) {
            throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return users;
    }

    public Users getById(Integer id){
        return this.userRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Product not found"));
    }

    public Users getByIdAddress(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }



//    public Users findByUsername(String username) {
//        return userRepository.findByUsername(username);
//    }

    public Users findByUsername(String username) {
        return userRepository.findByEmail(username);
    }

}
