package com.example.user.service.controller;

import com.example.user.service.entities.User;
import com.example.user.service.user.service.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

//    private Logger logger = LoggerFactory.getLogger(UserController.class);

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    //create
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
         User user1 = userService.saveUser(user);
         return ResponseEntity.status(HttpStatus.CREATED).body(user1);

    }

    int retryCount = 1;

    @GetMapping("/{userId}")
//    @CircuitBreaker(name = "ratingHotelBreaker",fallbackMethod = "ratingHotelFallback")
//    @Retry(name = "ratingHotelService",fallbackMethod = "ratingHotelFallback")
    @RateLimiter(name = "userRateLimiter",fallbackMethod = "ratingHotelFallback")
    public ResponseEntity<User> getSingleUser(@PathVariable String userId){

        logger.info("Get Single User Handler : UserController");

        logger.info("Retry count : {}", retryCount);
        retryCount++;

        User user = userService.getUser(userId);
        return ResponseEntity.ok(user);

    }

    //return type will be same

    //creating fall back method for circuitbreaker



    public ResponseEntity<User> ratingHotelFallback(String userId, Exception ex){

        ex.printStackTrace();

        logger.info("FallBack is executed because service is downn",ex.getMessage());


        User user = User.builder().email("dummy@gmail.com")
                .about("this user is created dummy because some service is down")
                .userId(userId)
                .build();

        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUser(){

        List<User> allUser = userService.getAllUser();
        return ResponseEntity.ok(allUser);
    }

}
