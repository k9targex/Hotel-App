package com.hotel.app.service;
import com.hotel.app.dao.HotelRepository;
import com.hotel.app.exception.exceptions.HotelAlreadyExistsException;
import com.hotel.app.exception.exceptions.HotelNotFoundException;
import com.hotel.app.exception.exceptions.IncorrectParameter;
import com.hotel.app.model.Address;
import com.hotel.app.model.ArrivalTime;
import com.hotel.app.model.Contacts;
import com.hotel.app.model.dto.HotelCreateDto;
import com.hotel.app.model.dto.HotelDto;
import com.hotel.app.model.entity.Hotel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelService hotelService;

    @Test
    void testGetHotelById_Success() {
        Long hotelId = 1L;
        Hotel hotel = new Hotel();
        hotel.setId(hotelId);
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));

        Hotel result = hotelService.getHotelById(hotelId);

        assertNotNull(result);
        assertEquals(hotelId, result.getId());
        verify(hotelRepository, times(1)).findById(hotelId);
    }
    @Test
    void testGetAllHotels_NonEmptyList() {
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Hotel");
        hotel.setDescription("Description");
        hotel.setAddress(new Address(1, "Street", "City", "Country", "12345"));
        hotel.setContacts(new Contacts("1234567890", "hotel@example.com"));

        when(hotelRepository.findAll()).thenReturn(Collections.singletonList(hotel));

        List<HotelDto> result = hotelService.getAllHotels();

        assertFalse(result.isEmpty());

        verify(hotelRepository, times(1)).findAll();

        HotelDto dto = result.get(0);
        assertEquals(hotel.getId(), dto.getId());
        assertEquals(hotel.getName(), dto.getName());
        assertEquals(hotel.getDescription(), dto.getDescription());
        assertEquals("1 Street, City, 12345, Country", dto.getAddress());
        assertEquals(hotel.getContacts().getPhone(), dto.getPhone());
    }

    @Test
    void testGetHotelById_NotFound() {
        Long hotelId = 1L;
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.empty());

        assertThrows(HotelNotFoundException.class, () -> hotelService.getHotelById(hotelId));
        verify(hotelRepository, times(1)).findById(hotelId);
    }

    @Test
    void testGetFilteredHotels_EmptyParams() {
        List<HotelDto> result = hotelService.getFilteredHotels(null, null, null, null, null);

        assertTrue(result.isEmpty());
        verify(hotelRepository, never()).findAll(any(Specification.class));
    }

    @Test
    void testGetFilteredHotels_NameNotEmpty() {
        hotelService.getFilteredHotels("Hotel", null, null, null, null);
        verify(hotelRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void testGetFilteredHotels_BrandNotEmpty() {
       hotelService.getFilteredHotels(null, "Brand", null, null, null);
        verify(hotelRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void testGetFilteredHotels_CityNotEmpty() {
        hotelService.getFilteredHotels(null, null, "City", null, null);
        verify(hotelRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void testGetFilteredHotels_CountryNotEmpty() {
        hotelService.getFilteredHotels(null, null, null, "Country", null);
        verify(hotelRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void testGetFilteredHotels_AmenitiesNotEmpty() {
        hotelService.getFilteredHotels(null, null, null, null, Collections.singletonList("WiFi"));
        verify(hotelRepository, times(1)).findAll(any(Specification.class));
    }
    @Test
    void testGetFilteredHotels_WithParams() {
        String name = "Hotel";
        String brand = "Brand";
        String city = "City";
        String country = "Country";
        List<String> amenities = Collections.singletonList("WiFi");

        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Hotel");
        hotel.setDescription("Description");
        hotel.setBrand("Brand");
        hotel.setAddress(new Address(1, "Street", "City", "Country", "12345"));
        hotel.setContacts(new Contacts("1234567890", "hotel@example.com"));

        when(hotelRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(hotel));

        List<HotelDto> result = hotelService.getFilteredHotels(name, brand, city, country, amenities);

        assertFalse(result.isEmpty());

        assertEquals(1, result.size());
        HotelDto dto = result.get(0);
        assertEquals(hotel.getId(), dto.getId());
        assertEquals(hotel.getName(), dto.getName());
        assertEquals(hotel.getDescription(), dto.getDescription());
        assertEquals("1 Street, City, 12345, Country", dto.getAddress()); // Проверка форматированного адреса
        assertEquals(hotel.getContacts().getPhone(), dto.getPhone());

        verify(hotelRepository, times(1)).findAll(any(Specification.class));
    }


    @Test
    void testAddHotel_Success() {
        HotelCreateDto hotelCreateDto = new HotelCreateDto();
        hotelCreateDto.setName("Hotel");
        hotelCreateDto.setBrand("Brand");
        hotelCreateDto.setAddress(new Address(1, "Street", "City", "Country", "12345"));
        hotelCreateDto.setContacts(new Contacts("1234567890", "hotel@example.com"));
        hotelCreateDto.setArrivalTime(new ArrivalTime("14:00", "12:00"));

        when(hotelRepository.existsByAddressAndName(any(Address.class), anyString())).thenReturn(false);
        when(hotelRepository.save(any(Hotel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        HotelDto result = hotelService.addHotel(hotelCreateDto);

        assertNotNull(result);
        assertEquals("Hotel", result.getName());
        verify(hotelRepository, times(1)).existsByAddressAndName(any(Address.class), anyString());
        verify(hotelRepository, times(1)).save(any(Hotel.class));
    }

    @Test
    void testAddHotel_AlreadyExists() {
        HotelCreateDto hotelCreateDto = new HotelCreateDto();
        hotelCreateDto.setName("Hotel");
        hotelCreateDto.setAddress(new Address(1, "Street", "City", "Country", "12345"));

        when(hotelRepository.existsByAddressAndName(any(Address.class), anyString())).thenReturn(true);

        assertThrows(HotelAlreadyExistsException.class, () -> hotelService.addHotel(hotelCreateDto));
        verify(hotelRepository, times(1)).existsByAddressAndName(any(Address.class), anyString());
        verify(hotelRepository, never()).save(any(Hotel.class));
    }

    @Test
    void testAddAmenities_Success() {
        Long hotelId = 1L;
        Set<String> amenities = new HashSet<>(Arrays.asList("WiFi", "Pool"));
        Hotel hotel = new Hotel();
        hotel.setId(hotelId);
        hotel.setAmenities(new HashSet<>());

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));
        when(hotelRepository.save(any(Hotel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        hotelService.addAmenities(hotelId, amenities);

        assertEquals(2, hotel.getAmenities().size());
        assertTrue(hotel.getAmenities().containsAll(amenities));
        verify(hotelRepository, times(1)).findById(hotelId);
        verify(hotelRepository, times(1)).save(any(Hotel.class));
    }

    @Test
    void testAddAmenities_HotelNotFound() {
        Long hotelId = 1L;
        Set<String> amenities = new HashSet<>(Arrays.asList("WiFi", "Pool"));

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.empty());

        assertThrows(HotelNotFoundException.class, () -> hotelService.addAmenities(hotelId, amenities));
        verify(hotelRepository, times(1)).findById(hotelId);
        verify(hotelRepository, never()).save(any(Hotel.class));
    }

    @Test
    void testHistogramHotels_ByBrand() {
        List<Hotel> hotels = Arrays.asList(
                new Hotel(1L, "Hotel1", "Desc1", "Brand1", null, null, null, null),
                new Hotel(2L, "Hotel2", "Desc2", "Brand1", null, null, null, null)
        );

        when(hotelRepository.findAll()).thenReturn(hotels);

        Map<String, Long> result = hotelService.histogramHotels("brand");

        assertEquals(1, result.size());
        assertEquals(2, result.get("Brand1"));
        verify(hotelRepository, times(1)).findAll();
    }

    @Test
    void testHistogramHotels_ByCity() {
        List<Hotel> hotels = Arrays.asList(
                new Hotel(1L, "Hotel1", "Desc1", "Brand1", new Address(1, "Street1", "City1", "Country1", "12345"), null, null, null),
                new Hotel(2L, "Hotel2", "Desc2", "Brand2", new Address(2, "Street2", "City1", "Country2", "67890"), null, null, null)
        );

        when(hotelRepository.findAll()).thenReturn(hotels);

        Map<String, Long> result = hotelService.histogramHotels("city");

        assertEquals(1, result.size());
        assertEquals(2, result.get("City1"));
        verify(hotelRepository, times(1)).findAll();
    }

    @Test
    void testHistogramHotels_ByCountry() {
        List<Hotel> hotels = Arrays.asList(
                new Hotel(1L, "Hotel1", "Desc1", "Brand1", new Address(1, "Street1", "City1", "Country1", "12345"), null, null, null),
                new Hotel(2L, "Hotel2", "Desc2", "Brand2", new Address(2, "Street2", "City2", "Country1", "67890"), null, null, null)
        );

        when(hotelRepository.findAll()).thenReturn(hotels);

        Map<String, Long> result = hotelService.histogramHotels("country");

        assertEquals(1, result.size());
        assertEquals(2, result.get("Country1"));
        verify(hotelRepository, times(1)).findAll();
    }

    @Test
    void testHistogramHotels_ByAmenities() {
        Set<String> amenities1 = new HashSet<>(Arrays.asList("WiFi", "Pool"));
        Set<String> amenities2 = new HashSet<>(Arrays.asList("WiFi", "Gym"));
        List<Hotel> hotels = Arrays.asList(
                new Hotel(1L, "Hotel1", "Desc1", "Brand1", null, null, null, amenities1),
                new Hotel(2L, "Hotel2", "Desc2", "Brand2", null, null, null, amenities2)
        );

        when(hotelRepository.findAll()).thenReturn(hotels);

        Map<String, Long> result = hotelService.histogramHotels("amenities");

        assertEquals(3, result.size());
        assertEquals(2, result.get("WiFi"));
        assertEquals(1, result.get("Pool"));
        assertEquals(1, result.get("Gym"));
        verify(hotelRepository, times(1)).findAll();
    }
    @Test
    void testHistogramHotels_Error() {
        List<Hotel> hotels = Arrays.asList(
                new Hotel(1L, "Hotel1", "Desc1", "Brand1", new Address(1, "Street1", "City1", "Country1", "12345"), null, null, null),
                new Hotel(2L, "Hotel2", "Desc2", "Brand2", new Address(2, "Street2", "City2", "Country1", "67890"), null, null, null)
        );

        when(hotelRepository.findAll()).thenReturn(hotels);

        assertThrows(IncorrectParameter.class, () -> hotelService.histogramHotels("INVALID"));
    }

}