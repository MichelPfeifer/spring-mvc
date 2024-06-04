package com.example.api.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Review {
    @Id
    private int id;
    private String title;
    private String content;
    private int start;
}
