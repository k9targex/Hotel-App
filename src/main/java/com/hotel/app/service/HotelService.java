package com.hotel.app.service;

import com.hotel.app.dao.HotelRepository;
import com.hotel.app.exception.exceptions.HotelNotFoundException;
import com.hotel.app.model.Address;
import com.hotel.app.model.dto.HotelDto;
import com.hotel.app.model.entity.Hotel;
import com.hotel.app.specification.HotelSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
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

    public List<HotelDto> getFilteredHotels(String name, String brand, String city, String country, List<String> amenities) {
        Specification<Hotel> spec = HotelSpecification.filterHotels(name, brand, city, country, amenities);
        return hotelRepository.findAll(spec).stream()
                .map(this::convertToDto)
                .toList();
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
