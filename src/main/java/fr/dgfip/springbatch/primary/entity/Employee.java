package fr.dgfip.springbatch.primary.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Builder
@Data @NoArgsConstructor @AllArgsConstructor @ToString
public class Employee {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private int id;
    private String name;
    private int salary;
}
