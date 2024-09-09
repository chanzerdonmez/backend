package com.wineko.api.controller;

import com.wineko.api.dto.*;
import com.wineko.api.model.Users;
import com.wineko.api.service.EmailService;
import com.wineko.api.service.UsersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:4200", "https://wineko.srv589783.hstgr.cloud"})
public class UserController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder; // Ajoutez cette ligne

    @Autowired
    private EmailService emailService; // Ajoutez cette ligne également

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
    public ResponseEntity<UserInfoDto> getUserInfo(@AuthenticationPrincipal UserDetails principal) {
        Users user = (Users) principal;

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        UserInfoDto userInfoDto = new UserInfoDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),  // Récupération du prénom
                user.getName(),       // Récupération du nom
                user.getPhone(),      // Récupération du téléphone
                user.getBirthdate(),
                user.getRole()// Notez que birthdate sera de type Date
        );

        return ResponseEntity.ok(userInfoDto);
    }



    @PostMapping("/requestPasswordReset")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        Users user = usersService.findByEmail(forgotPasswordDto.getEmail());

        if (user == null) {
            return ResponseEntity.badRequest().body("Utilisateur non trouvé");
        }

        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        usersService.save(user);  // Notez que c'est bien usersService.save() ici, pas saveUser.

        sendResetPasswordEmail(user);

        return ResponseEntity.ok("Un email de réinitialisation a été envoyé.");
    }


    private void sendResetPasswordEmail(Users user) {
        String resetUrl = "http://localhost:4200/reset-password?token=" + user.getResetToken();
        emailService.sendEmail(
                user.getEmail(),
                "Réinitialisation de votre mot de passe",
                "Bonjour " + user.getFirstName() + ",\n\n" +
                        "Pour réinitialiser votre mot de passe, cliquez sur le lien suivant :\n" +
                        resetUrl + "\n\n" +
                        "Cordialement,\nL'équipe de notre site"
        );
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        Users user = usersService.findByResetToken(resetPasswordDto.getToken());
        if (user == null) {
            return ResponseEntity.badRequest().body("Token invalide ou expiré.");
        }

        user.setPassword(bCryptPasswordEncoder.encode(resetPasswordDto.getNewPassword()));
        user.setResetToken(null);  // On annule le token après usage
        usersService.save(user);  // Remplacez saveUser par save

        return ResponseEntity.ok(Map.of("message", "Mot de passe réinitialisé avec succès"));
    }



    @PutMapping("/updateProfile")
    public ResponseEntity<Users> updateProfile(@RequestBody UpdateProfileDto updateProfileDto, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        Users user = usersService.findByEmail(email);

        if (user != null) {
            user.setName(updateProfileDto.getName());
            user.setFirstName(updateProfileDto.getFirstName());
            user.setPhone(updateProfileDto.getPhone());
            user.setBirthdate(updateProfileDto.getBirthdate());
            usersService.save(user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/updateEmail")
    public ResponseEntity<Users> updateEmail(@RequestBody UpdateEmailDto updateEmailDto, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        Users user = usersService.findByEmail(email);

        if (user != null) {
            String oldEmail = user.getEmail();
            user.setEmail(updateEmailDto.getNewEmail());
            usersService.save(user);
            if (!oldEmail.equals(updateEmailDto.getNewEmail())) {
                usersService.sendConfirmationUpdateEmail(oldEmail, updateEmailDto.getNewEmail(), user.getFirstName());
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(user);
            }
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PutMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordDto updatePasswordDto, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        Users user = usersService.findByEmail(email);

        if (user != null && bCryptPasswordEncoder.matches(updatePasswordDto.getOldPassword(), user.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(updatePasswordDto.getNewPassword()));
            usersService.save(user);
            usersService.sendConfirmationUpdatePassword(user);
            return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect current password");
        }
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        Users user = usersService.findByEmail(email);

        if (user != null) {
            usersService.anonymizeUser(user.getId());
            SecurityContextHolder.clearContext(); // Déconnexion de l'utilisateur
            return ResponseEntity.ok(Map.of("message", "Votre compte a été supprimé avec succès."));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé.");
        }
    }


    @GetMapping("/me")
    public ResponseEntity<UserInfoDto> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Users user = usersService.findByEmail(userDetails.getUsername());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        UserInfoDto userInfoDto = new UserInfoDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getName(),
                user.getPhone(),
                user.getBirthdate(),
                user.getRole() // Ajoutez le rôle ici
        );

        return ResponseEntity.ok(userInfoDto);
    }



    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUserByAdmin(@PathVariable Integer id) {
        Users userToDelete = usersService.getById(id);

        if (userToDelete == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé.");
        }

        usersService.anonymizeUser(id); // Suppression ou anonymisation de l'utilisateur
        return ResponseEntity.ok(Map.of("message", "Utilisateur supprimé avec succès."));
    }

}
