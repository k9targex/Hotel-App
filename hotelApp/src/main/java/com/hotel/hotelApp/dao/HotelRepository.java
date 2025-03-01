package com.hotel.hotelApp.dao;

import com.hotel.hotelApp.model.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel,Long> {

}
