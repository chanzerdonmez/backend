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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/address")
@CrossOrigin(origins = "http://localhost:4200")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private UsersService usersService;

    @PostMapping("/add")
    public String addAddress(@RequestBody Address address) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        Users user = usersService.findByEmail(currentUserName);

        if (user == null) {
            return "User not authenticated";
        }

        address.setUser(user);
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
