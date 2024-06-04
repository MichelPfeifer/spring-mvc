package com.example.api.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
public class Pokemon {
    @Id
    private int id;
    private String name;
    private String type;


}
