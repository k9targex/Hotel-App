package com.hotel.hotelApp.service;

import com.hotel.hotelApp.dao.HotelRepository;
import com.hotel.hotelApp.exception.exceptions.HotelNotFoundException;
import com.hotel.hotelApp.model.Address;
import com.hotel.hotelApp.model.dto.HotelDto;
import com.hotel.hotelApp.model.entity.Hotel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class HotelService {

    private HotelRepository hotelRepository;

    public Hotel getHotelById(Long hotelId) {
        return hotelRepository.findById(hotelId).
                orElseThrow(() -> new HotelNotFoundException("Hotel doesn't exist."));
    }

    public List<HotelDto> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    private HotelDto convertToDto(Hotel hotel) {
        return HotelDto.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .description(hotel.getDescription())
                .address(formatAddress(hotel.getAddress()))
                .phone(hotel.getContacts().getPhone())
                .build();
    }

    private String formatAddress(Address address) {
        return address.getHouseNumber() + " " + address.getStreet() + ", " +
                address.getCity() + ", " + address.getCountry() + " " +
                address.getPostCode();
    }
}
