package com.example.user.service.user.service;

import com.example.user.service.entities.User;

import java.util.List;

public interface UserService {

    //user operation



    //create
    User saveUser(User user);

    //get al user
    List<User> getAllUser();


    //get single user of given userId
    User getUser(String userId);


    // update the user

    //delete the user





}
