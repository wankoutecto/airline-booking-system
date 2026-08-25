package com.example.airline_booking_system.airport;

import com.example.airline_booking_system.airport.dto.AirportRequest;
import com.example.airline_booking_system.airport.dto.AirportResponse;
import com.example.airline_booking_system.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AirportService {

    private final AirportRepository airportRepository;
    private final AirportMapper airportMapper;
    private final CacheManager cacheManager;


    public AirportResponse createAirport(AirportRequest request) {

        validateNewAirportCode(request.getCode());

        Airport airport = Airport.builder()
                .code(request.getCode())
                .city(request.getCity())
                .name(request.getName())
                .country(request.getCountry())
                .active(true)
                .build();

        return airportMapper.toAirportResponse(airportRepository.save(airport));
    }


    public Airport getAirport(Long id) {
        return airportRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Airport not found"));
    }
    public AirportResponse getAirportResponse(Long id) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Airport not found"));
        return airportMapper.toAirportResponse(airport);
    }


    public Airport getAirportByCode(String code){
        return airportRepository.findByCode(code)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Airport not found"));
    }

    //@Cacheable is Spring's caching abstraction. Redis is the actual cache storage.
    @Cacheable(value = "airports", key = "#code")
    public AirportResponse findAirport(String code){
        System.out.println("REQUEST AIRPORT HIT");
        return airportMapper.toAirportResponse(getAirportByCode(code));
    }



    public List<AirportResponse> getAllAirports() {

        return airportMapper.toAirportResponseList(airportRepository.findAll());
    }

    @CachePut(value = "airports", key = "#result.code")
    public AirportResponse updateAirport(Long id, Airport request) {
        Airport airport = getAirport(id);

        airport.setCode(request.getCode());
        airport.setName(request.getName());
        airport.setCity(request.getCity());
        airport.setCountry(request.getCountry());
        airport.setActive(request.isActive());

        return airportMapper.toAirportResponse(airportRepository.save(airport));
    }

    //@CacheEvict(value = "airport", key = "code")
    public void deactivateAirport(Long id) {

        Airport airport = getAirport(id);

        airport.setActive(false);

        airportRepository.save(airport);
        //explicit caching evict
        cacheManager.getCache("airports").evict(airport.getCode());
    }


    private void validateNewAirportCode(String code) {
        if(airportRepository.existsByCode(code)) {
            throw new IllegalArgumentException(
                    "Airport code already exists");
        }
    }

    public void checkAirportExist(String code){
        if(!airportRepository.existsByCode(code)) {
            throw new ResourceNotFoundException("Airport code does not exists: " + code);
        }
    }
}
