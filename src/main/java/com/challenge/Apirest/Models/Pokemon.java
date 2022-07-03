package com.challenge.Apirest.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Pokemon {
    private int id;
    private String photo;
    private ArrayList<String> types;
    private int weight;
    private ArrayList<Ability> abilities;
    private String description;
    private ArrayList<String> moves;
}
