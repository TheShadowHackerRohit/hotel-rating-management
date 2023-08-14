package com.example.hotel.services.Impl;

import com.example.hotel.entities.Hotel;
import com.example.hotel.exception.ResourceNotFoundException;
import com.example.hotel.repositories.HotelRepository;
import com.example.hotel.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HotelServiceImpl implements HotelService {
   @Autowired
   private HotelRepository hotelRepository;

    @Override
    public Hotel create(Hotel hotel) {
        String hotelId = UUID.randomUUID().toString();
        hotel.setId(hotelId);
        return hotelRepository.save(hotel);
    }

    @Override
    public List<Hotel> getAll() {
        return hotelRepository.findAll();
    }

    @Override
    public Hotel get(String id) {
        return hotelRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("hotel with given id does not found")


        );
    }
}
