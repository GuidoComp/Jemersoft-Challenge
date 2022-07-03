package com.challenge.Apirest.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ability {
    private String name;
    private String url;
    private boolean is_hidden;
    private int slot;
}
