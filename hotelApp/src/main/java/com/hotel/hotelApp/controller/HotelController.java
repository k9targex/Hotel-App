package com.hotel.hotelApp.controller;


import com.hotel.hotelApp.model.dto.HotelDto;
import com.hotel.hotelApp.model.entity.Hotel;
import com.hotel.hotelApp.service.HotelService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/property-view")
public class HotelController {


    private HotelService hotelService;

    @GetMapping("hotels/{id}")
    public ResponseEntity<Hotel> getHotel(@PathVariable Long id){
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }

    @GetMapping("hotels")
    public ResponseEntity<List<HotelDto>> getAllHotels(){
        return ResponseEntity.ok(hotelService.getAllHotels());
    }
}
