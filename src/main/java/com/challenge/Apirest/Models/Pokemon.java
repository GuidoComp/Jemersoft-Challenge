package com.challenge.Apirest.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.LinkedList;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Pokemon {
    private String photo;
    private LinkedList<String> types;
    private int weight;
    private LinkedList<Ability> abilities;
    private String description;
    private LinkedList<String> moves;
}
