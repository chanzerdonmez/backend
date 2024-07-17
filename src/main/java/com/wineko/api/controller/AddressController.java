package com.wineko.api.controller;

import com.wineko.api.manager.JwtTokenManager;
import com.wineko.api.model.Address;
import com.wineko.api.model.Article;
import com.wineko.api.model.Users;
import com.wineko.api.service.AddressService;
import com.wineko.api.service.ArticleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/address")
@CrossOrigin(origins = "http://localhost:4200")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/add")
    public String addAddress(@AuthenticationPrincipal Users user, @RequestBody Address address) {
        if (user == null) {
            return "User not authenticated";
        }
        addressService.saveAddressForUser(user, address);
        return "Address added successfully";
    }

    @PatchMapping("/update/{id}")
    public Address updateAddress(
            @PathVariable Integer id,
            @RequestBody Address address
    ){
        this.addressService.update(id, address);
        return address;
    }
}
