package com.hotel.app.controller;


import com.hotel.app.model.dto.HotelCreateDto;
import com.hotel.app.model.dto.HotelDto;
import com.hotel.app.model.entity.Hotel;
import com.hotel.app.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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


    @Operation(summary = "Get hotel by ID", description = "Retrieve details of a specific hotel by its ID.")
    @ApiResponse(responseCode = "200", description = "Hotel found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Hotel.class)))
    @ApiResponse(responseCode = "404", description = "Hotel not found")

    @GetMapping("/hotels/{id}")
    public ResponseEntity<Hotel> getHotel(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }


    @Operation(summary = "Get all hotels", description = "Retrieve a list of all hotels.")
    @ApiResponse(responseCode = "200", description = "List of hotels",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = HotelDto.class)))

    @GetMapping("/hotels")
    public ResponseEntity<List<HotelDto>> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }



    @Operation(summary = "Search hotels by filters", description = "Search hotels based on optional filters (name, brand, city, country, amenities).")
    @ApiResponse(responseCode = "200", description = "Filtered hotels found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = HotelDto.class)))

    @GetMapping("/search")
    public ResponseEntity<List<HotelDto>> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) List<String> amenities) {

        return ResponseEntity.ok(hotelService.getFilteredHotels(name, brand, city, country, amenities));
    }


    @Operation(summary = "Get hotel histogram by parameter", description = "Retrieve a histogram of hotels based on a specific parameter (e.g. city, brand, etc.).")
    @ApiResponse(responseCode = "200", description = "Histogram data retrieved",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    @ApiResponse(responseCode = "400", description = "Invalid parameter")

    @GetMapping("/histogram/{param}")
    public ResponseEntity<Map<String,Long>> histogramHotelsByParam(@PathVariable String param) {
        return ResponseEntity.ok(hotelService.histogramHotels(param));
    }


    @Operation(summary = "Create a new hotel", description = "Create a new hotel.")
    @ApiResponse(responseCode = "200", description = "Hotel created",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = HotelDto.class)))
    @ApiResponse(responseCode = "409", description = "Hotel already exist")

    @PostMapping("/hotels")
    public ResponseEntity<HotelDto> createHotel( @Valid @RequestBody HotelCreateDto hotelCreateDto){
        return ResponseEntity.ok(hotelService.addHotel(hotelCreateDto));

    }


    @Operation(summary = "Add amenities to hotel", description = "Add a list of amenities to an existing hotel.")
    @ApiResponse(responseCode = "200", description = "Amenities added successfully")
    @ApiResponse(responseCode = "404", description = "Hotel doesn't exist")

    @PostMapping("/hotels/{id}/amenities")
    public void addHotelAmenities( @PathVariable Long id, @RequestBody Set<String> amenities ){
        hotelService.addAmenities(id,amenities);
    }




}
