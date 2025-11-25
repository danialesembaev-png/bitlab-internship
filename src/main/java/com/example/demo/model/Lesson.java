package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Lesson extends BaseEntity {

    private String title;

    private String description;

    private Integer lessonOrder;

    @ManyToOne()
    private Chapter chapter;
}
