package org.internship.task.rideservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.internship.task.rideservice.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ride")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ride {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "passengerId")
    private Long passengerId;

    @Column(name = "driverId")
    private Long driverId;

    @Column(name = "startAddress")
    private String startAddress;

    @Column(name = "finishAddress")
    private String finishAddress;

    @Column(name = "status")
    private Status status;

    @Column(name = "order_date_time")
    private LocalDateTime orderDateTime;

    @Column(name = "price")
    private BigDecimal price;
}
