package com.hotel.hotelApp.service;

import com.hotel.hotelApp.dao.HotelRepository;
import com.hotel.hotelApp.exception.exceptions.HotelNotFoundException;
import com.hotel.hotelApp.model.entity.Hotel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HotelService {


    private HotelRepository hotelRepository;

    public Hotel getHotelById(Long hotelId) {
        return hotelRepository.findById(hotelId).
                orElseThrow(() -> new HotelNotFoundException("Hotel doesn't exist."));
    }

}
