package org.internship.task.driverservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.internship.task.driverservice.enums.CarBrand;
import org.internship.task.driverservice.enums.Color;

@Entity
@Table(name = "car")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "color")
    private Color color;

    @Column(name = "carBrand")
    private CarBrand carBrand;

    @Column(name = "carNumber")
    private String carNumber;

    @Column(name = "carYear")
    private short carYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driverId")
    private Driver driver;

    @Column(name = "isDeleted")
    private Boolean isDeleted = false;
}
