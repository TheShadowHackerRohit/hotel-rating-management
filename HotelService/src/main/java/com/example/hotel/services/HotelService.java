package com.example.hotel.services;

import com.example.hotel.entities.Hotel;

import java.util.List;

public interface HotelService {

    //create

    Hotel create(Hotel hotel);

    //get All
    List<Hotel> getAll();

    //get Single
    Hotel get(String id);


}
