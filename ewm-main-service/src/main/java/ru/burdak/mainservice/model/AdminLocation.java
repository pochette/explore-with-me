package ru.burdak.mainservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "admin_locations")
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class AdminLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 255, unique = true)
    private String name;

    @Column(nullable = false, precision = 9, scale = 6)
    private Float lat;

    @Column(nullable = false, precision = 9, scale = 6)
    private Float lon;

    @Column(nullable = false, precision = 12, scale = 3)
    @Positive
    private Double radius;

}
