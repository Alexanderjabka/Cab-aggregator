package org.internship.task.driverservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    private final static String CAR_NUMBER = "^\\d{4}[АВЕКМНОРСТУХ]{2}[1-7]$";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
    private Long id;

    @NotBlank(message = "color cannot be empty")
    @Column(name = "color")
    private Color color;

    @NotBlank(message = "car brand cannot be empty")
    @Column(name = "car_brand")
    private CarBrand carBrand;

    @NotBlank(message = "car number cannot be empty")
    @Pattern(regexp = CAR_NUMBER,message = "invalid format of car number")
    @Column(name = "car_number")
    private String carNumber;

    @NotBlank(message = "car year cannot be empty")
    @Size(min = 1990 , max = 2025)
    @Column(name = "car_year")
    private short carYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

}
