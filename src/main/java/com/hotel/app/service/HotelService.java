package com.hotel.app.service;

import com.hotel.app.dao.HotelRepository;
import com.hotel.app.exception.exceptions.HotelNotFoundException;
import com.hotel.app.exception.exceptions.IncorrectParameter;
import com.hotel.app.model.Address;
import com.hotel.app.model.dto.HotelCreateDto;
import com.hotel.app.model.dto.HotelDto;
import com.hotel.app.model.entity.Hotel;
import com.hotel.app.specification.HotelSpecification;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HotelService {

    private HotelRepository hotelRepository;

    public Hotel getHotelById(Long hotelId) {
        return hotelRepository.findById(hotelId).
                orElseThrow(() -> new HotelNotFoundException("Hotel doesn't exist."));
    }

    public List<HotelDto> getFilteredHotels(String name, String brand, String city, String country, List<String> amenities) {
        if (checkParamsEmpty(name,brand,city,country,amenities)){
            return new ArrayList<>();
        }
        else
            return hotelRepository.findAll(HotelSpecification.filterHotels(name, brand, city, country, amenities)).stream()
                .map(this::convertToDto)
                .toList();
    }


    public List<HotelDto> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }
    private boolean checkParamsEmpty(String name, String brand, String city, String country, List<String> amenities) {
        return (name == null || name.isEmpty()) &&
                (brand == null || brand.isEmpty()) &&
                (city == null || city.isEmpty()) &&
                (country == null || country.isEmpty()) &&
                (amenities == null || amenities.isEmpty());
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


    public HotelDto addHotel(HotelCreateDto hotelCreateDto) {
        return convertToDto(hotelRepository.save(Hotel.builder()
                .name(hotelCreateDto.getName())
                .description(hotelCreateDto.getDescription())
                .brand(hotelCreateDto.getBrand())
                .address(hotelCreateDto.getAddress())
                .contacts(hotelCreateDto.getContacts())
                .arrivalTime(hotelCreateDto.getArrivalTime())
                .amenities(new HashSet<>())
                .build()
        ));
    }

    public void addAmenities(Long id, Set<String> amenities) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new HotelNotFoundException("Hotel doesn't exist."));
        hotel.getAmenities().addAll(amenities);
        hotelRepository.save(hotel);
    }

    public Map<String, Long> histogramHotels(String param) {
        List<Hotel> hotels = hotelRepository.findAll();
        switch (param.toLowerCase()) {
            case "brand":
                return hotels.stream()
                        .filter(hotel -> hotel.getBrand() != null && !hotel.getBrand().isEmpty())
                        .collect(Collectors.groupingBy(Hotel::getBrand, Collectors.counting()));

            case "city":
                return hotels.stream()
                        .filter(hotel -> hotel.getAddress() != null && hotel.getAddress().getCity() != null)
                        .collect(Collectors.groupingBy(hotel -> hotel.getAddress().getCity(), Collectors.counting()));

            case "country":
                return hotels.stream()
                        .filter(hotel -> hotel.getAddress() != null && hotel.getAddress().getCountry() != null)
                        .collect(Collectors.groupingBy(hotel -> hotel.getAddress().getCountry(), Collectors.counting()));

            case "amenities":
                return hotels.stream()
                        .filter(hotel -> hotel.getAmenities() != null && !hotel.getAmenities().isEmpty())
                        .flatMap(hotel -> hotel.getAmenities().stream())
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            default:
                throw new IncorrectParameter("Invalid parameter. Available parameters: brand, city, country, amenities.");
        }
    }
}
