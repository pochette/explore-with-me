package ru.burdak.mainservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "admin_locations")
@NoArgsConstructor
@Getter
@Setter
public class AdminLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 255, unique = true)
    private String name;

    @Column(nullable = false)
    private Float lat;

    @Column(nullable = false)
    private Float lon;

    @Column(nullable = false)
    private Double radius;

}
