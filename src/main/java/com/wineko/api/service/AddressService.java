package com.wineko.api.service;

import com.wineko.api.model.Address;
import com.wineko.api.model.Article;
import com.wineko.api.model.Users;
import com.wineko.api.repository.AddressRepository;
import com.wineko.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    public AddressService(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }


    public void saveAddressForUser(Users user, Address address) {
        address.setUser(user);
        addressRepository.save(address);
    }

    public Address update(Integer id, Address updated){
        updated.setId(id);
        return this.addressRepository.save(updated);
    }


}
