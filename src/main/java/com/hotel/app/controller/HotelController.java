package com.hotel.app.controller;


import com.hotel.app.model.dto.HotelCreateDto;
import com.hotel.app.model.dto.HotelDto;
import com.hotel.app.model.entity.Hotel;
import com.hotel.app.service.HotelService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;


@RestController
@AllArgsConstructor
@RequestMapping("/property-view")
public class HotelController {


    private HotelService hotelService;

    @GetMapping("/hotels/{id}")
    public ResponseEntity<Hotel> getHotel(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }

    @GetMapping("/hotels")
    public ResponseEntity<List<HotelDto>> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }


    @GetMapping("/search")
    public ResponseEntity<List<HotelDto>> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) List<String> amenities) {

        return ResponseEntity.ok(hotelService.getFilteredHotels(name, brand, city, country, amenities));
    }


    @GetMapping("/histogram/{param}")
    public ResponseEntity<Map<String,Long>> histogramHotelsByParam(@PathVariable String param) {
        return ResponseEntity.ok(hotelService.histogramHotels(param));
    }
    @PostMapping("/hotels")
    public ResponseEntity<HotelDto> createHotel( @Valid @RequestBody HotelCreateDto hotelCreateDto){
        return ResponseEntity.ok(hotelService.addHotel(hotelCreateDto));

    }

    @PostMapping("/hotels/{id}/amenities")
    public void addHotelAmenities( @PathVariable Long id, @RequestBody Set<String> amenities ){
        hotelService.addAmenities(id,amenities);
    }




}
