package com.hotel.app.specification;

import com.hotel.app.model.entity.Hotel;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HotelSpecification {
    public static Specification<Hotel> filterHotels(String name, String brand, String city, String country, List<String> amenities) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Optional.ofNullable(name)
                    .filter(n -> !n.isEmpty())
                    .map(n -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + n.toLowerCase() + "%"))
                    .ifPresent(predicates::add);

            Optional.ofNullable(brand)
                    .filter(b -> !b.isEmpty())
                    .map(b -> criteriaBuilder.like(criteriaBuilder.lower(root.get("brand")), "%" + b.toLowerCase() + "%"))
                    .ifPresent(predicates::add);

            Optional.ofNullable(city)
                    .filter(c -> !c.isEmpty())
                    .map(c -> criteriaBuilder.like(criteriaBuilder.lower(root.get("address").get("city")), "%" + c.toLowerCase() + "%"))
                    .ifPresent(predicates::add);

            Optional.ofNullable(country)
                    .filter(c -> !c.isEmpty())
                    .map(c -> criteriaBuilder.like(criteriaBuilder.lower(root.get("address").get("country")), "%" + c.toLowerCase() + "%"))
                    .ifPresent(predicates::add);

            Optional.ofNullable(amenities)
                    .filter(a -> !a.isEmpty())
                    .map(a -> {
                        List<Predicate> amenityPredicates = new ArrayList<>();
                        for (String amenity : a) {
                            amenityPredicates.add(
                                    criteriaBuilder.like(criteriaBuilder.lower(root.join("amenities").as(String.class)), "%" + amenity.toLowerCase() + "%")
                            );
                        }
                        return criteriaBuilder.or(amenityPredicates.toArray(new Predicate[0]));
                    })
                    .ifPresent(predicates::add);


            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
