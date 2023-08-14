package com.example.user.service.user.service.impl;

import com.example.user.service.entities.Hotel;
import com.example.user.service.entities.Rating;
import com.example.user.service.entities.User;
import com.example.user.service.exceptions.ResourceNotFoundException;
import com.example.user.service.external.services.HotelService;
import com.example.user.service.repositories.UserRepository;
import com.example.user.service.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;
    // make this bean in the myconfig


    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private HotelService hotelService;// from feign client


    @Override
    public User saveUser(User user) {
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(String userId) {

        User user=  userRepository.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException("User with given id is not found on server!! : " +userId)
        );
        // fetch rating of the above user from RATING SERVICE
        //localhost:8083/ratings/users/8d1077c8-685b-4197-814f-481ee77b3b52


        //add the http:// in the url otherwise you will get  this exception { java.net.URISyntaxException: Expected scheme-specific part at index 10: localhost:}
        Rating[] ratingsOfUser = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/"+userId, Rating[].class);

        logger.info("{} ",ratingsOfUser);

        List<Rating> ratings = Arrays.stream(ratingsOfUser).toList();

        List<Rating> ratingList = ratings.stream().map(rating -> {
            //api call to hotel service to get the hotel
            //http://localhost:8082/hotels/bbe62e91-028b-4093-b91c-d9f2e6753d0e

            // for calling service by name we have to mention @LoadBalanced where we create the bean of RestTemplate
//            ResponseEntity<Hotel> hotelResponseEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/"+rating.getHotelId(), Hotel.class);
//            Hotel hotel = hotelResponseEntity.getBody();
//            logger.info("response status code: {} ",hotelResponseEntity.getStatusCode());



            // bye feign client
            Hotel hotel = hotelService.getHotel(rating.getHotelId());


            //set the hotel to rating
            rating.setHotel(hotel);
                  //return the rating
            return rating;
                }).collect(Collectors.toList());



        user.setRatingList(ratings);


        return user;
    }
}
