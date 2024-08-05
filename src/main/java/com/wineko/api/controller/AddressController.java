package com.wineko.api.controller;

import com.wineko.api.manager.JwtTokenManager;
import com.wineko.api.model.Address;
import com.wineko.api.model.Article;
import com.wineko.api.model.Users;
import com.wineko.api.service.AddressService;
import com.wineko.api.service.ArticleService;
import com.wineko.api.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private UsersService usersService;

    @PostMapping("/add")
    public String addAddress(@RequestBody Address address,@AuthenticationPrincipal UserDetails principal) {

        Users user = (Users) principal;

        addressService.saveAddressForUser(user, address);
        return "Address added successfully";
    }


//    @PostMapping("/add")
//    public ResponseEntity<Map<String, String>> addAddress(@RequestBody Address address, @AuthenticationPrincipal UserDetails principal) {
//        Users user = (Users) principal;
//        if (user == null) {
//            throw new IllegalArgumentException("User not authenticated");
//        }
//        addressService.saveAddressForUser(user, address);
//        Map<String, String> response = new HashMap<>();
//        response.put("message", "Address added successfully");
//        return ResponseEntity.ok(response); // Renvoie une réponse JSON valide
//    }





    @PatchMapping("/update/{id}")
    public Address updateAddress(
            @PathVariable Integer id,
            @RequestBody Address address
    ){
        this.addressService.update(id, address);
        return address;
    }


    @GetMapping("/user/addresses")
    public List<Address> getUserAddresses(@AuthenticationPrincipal UserDetails principal) {
        Users user = usersService.findByUsername(principal.getUsername());
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return addressService.getAddressesByUser(user);
    }


}
