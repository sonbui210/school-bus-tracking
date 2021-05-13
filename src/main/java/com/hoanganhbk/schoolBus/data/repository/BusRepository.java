package com.hoanganhbk.schoolBus.data.repository;

import com.hoanganhbk.schoolBus.data.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusRepository extends JpaRepository<Bus, Long> {
    Optional<Bus> findBusByCode(String code);

    Optional<Bus> findByLicensePlate(String licensePlate);

    Iterable<Bus> findByCodeLike(String code);
}
