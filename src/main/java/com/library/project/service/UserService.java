package com.library.project.service;

import com.library.project.model.Group;
import com.library.project.model.UserEntity;
import com.library.project.repository.UserGroupRepository;
import com.library.project.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;
    private UserGroupRepository groupRepository;
    public UserService(UserRepository userRepository, UserGroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    public UserEntity save(UserEntity userEntity){
        userEntity.setPassword(passwordEncoder(userEntity.getPassword()));
        updateCustomerGroup(userEntity);
        return userRepository.save(userEntity);
    }

    public String passwordEncoder(String rawPassword){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(rawPassword);
        return encodedPassword;
    }



    private void updateCustomerGroup(UserEntity userEntity){
        Group group= groupRepository.findByCode("customer");
        userEntity.addUserGroups(group);
    }
}
