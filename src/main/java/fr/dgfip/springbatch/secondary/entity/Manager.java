package fr.dgfip.springbatch.secondary.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Data @NoArgsConstructor @AllArgsConstructor @ToString
public class Manager {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(insertable = false, updatable = false)
    private int id;
    private String name;
    private int salary;
}
