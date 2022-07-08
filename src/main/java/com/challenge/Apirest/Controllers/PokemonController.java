package com.challenge.Apirest.Controllers;

import com.challenge.Apirest.Exceptions.PokemonOutOfRange;
import com.challenge.Apirest.Models.Pokemon;
import com.challenge.Apirest.Services.PokemonService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.LinkedList;

@RestController
@RequestMapping("pokemons")
@Api(value = "", tags={"PokeDex Service"})
@Tag(name = "PokeDex Service", description = "Jemersoft challenge")
public class PokemonController {
    private final PokemonService pokemonService;

    @Autowired
    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping
    private LinkedList<Pokemon> pokemonsList() throws IOException {
        return pokemonService.getBasicInfoAllPokemons();
    }

    @GetMapping("/{id}")
    private Pokemon getMoreDetails(@PathVariable String id) throws IOException, PokemonOutOfRange {
        return this.pokemonService.getAllDetails(id);
    }
}
