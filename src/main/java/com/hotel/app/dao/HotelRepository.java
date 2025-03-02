package com.hotel.app.dao;

import com.hotel.app.model.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HotelRepository extends JpaRepository<Hotel,Long>, JpaSpecificationExecutor<Hotel> {

}
