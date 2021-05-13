package com.hoanganhbk.schoolBus.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bus")
@Data
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String licensePlate;

    private String route;

    private String code;

    private String driver;

    private String phone;

    private String status;

    @Column(columnDefinition = "Decimal default '0'")
    private double currentLng;

    @Column(columnDefinition = "Decimal default '0'")
    private double currentLat;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "bus")
    private Set<Student> studentsOnBus = new HashSet<>();

    @ElementCollection
    private Set<String> waypoint;
}
