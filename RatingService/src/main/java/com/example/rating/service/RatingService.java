package com.example.rating.service;

import com.example.rating.entities.Rating;

import java.util.List;

public interface RatingService {


    Rating create(Rating rating);

    List<Rating> getAllRatings();

    List<Rating> getRatingsByUserId(String userId);

    List<Rating> getRatingByHotelId(String hotelId);

}
