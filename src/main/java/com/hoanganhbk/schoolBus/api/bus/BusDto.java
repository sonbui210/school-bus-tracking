package com.hoanganhbk.schoolBus.api.bus;

import com.hoanganhbk.schoolBus.data.model.Route;
import lombok.Data;

import java.util.Set;

@Data
public class BusDto {
    private Long id;

    private String licensePlate;

    private Route route;

    private String code;

    private String driver;

    private String phone;

    private Set<String> waypoint;
}
