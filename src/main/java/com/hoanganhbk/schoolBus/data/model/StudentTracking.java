package com.hoanganhbk.schoolBus.data.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "student_tracking")
@Data
public class StudentTracking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private long studentId;

    private long busId;

    @Column(columnDefinition="TEXT")
    private String message;

    private String createAt;

    private int status;

    private String image;
}
