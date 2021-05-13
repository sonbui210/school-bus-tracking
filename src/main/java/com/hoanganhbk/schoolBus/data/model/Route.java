package com.hoanganhbk.schoolBus.data.model;

import lombok.Data;

import javax.persistence.Id;

@Data
public class Route {
    @Id
    private Long id;

    private Place original;

    private Place destination;
}
