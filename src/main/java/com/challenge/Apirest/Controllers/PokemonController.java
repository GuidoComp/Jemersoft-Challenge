package com.challenge.Apirest.Controllers;

import com.challenge.Apirest.Models.Pokemon;
import com.challenge.Apirest.Repositories.PokemonRepository;
import com.challenge.Apirest.Services.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping("pokemons")
public class PokemonController {
    private final PokemonService pokemonService;
    private PokemonRepository pokemonRepository;
    @Autowired
    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }
    @GetMapping
    private ArrayList<Pokemon> generatePokemons() throws IOException {
        ArrayList<Pokemon> list = pokemonService.generatePokemons();
        pokemonRepository = new PokemonRepository(list);
        return list;
    }

    @GetMapping("/{id}")
    private Pokemon getMoreDetails(@PathVariable int id){
        return this.pokemonRepository.getPokemonsList().get(id-1);
    }
}
