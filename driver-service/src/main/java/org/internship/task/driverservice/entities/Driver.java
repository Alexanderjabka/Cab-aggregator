package org.internship.task.driverservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.internship.task.driverservice.enums.Gender;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "driver")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "gender")
    private Gender gender;

    @OneToMany(mappedBy = "driver",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Car> cars = new ArrayList<>();

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}
