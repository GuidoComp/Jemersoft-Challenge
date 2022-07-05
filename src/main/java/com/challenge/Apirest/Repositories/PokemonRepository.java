package com.challenge.Apirest.Repositories;

import com.challenge.Apirest.Models.Pokemon;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.LinkedList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PokemonRepository {
    private LinkedList<Pokemon> pokemonsList;
}
