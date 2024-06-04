package com.example.api.controller;

import com.example.api.model.Pokemon;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

// Typ vom Controller --> Rest API
@RestController
//
@RequestMapping("/api/")
public class ReviewController {
    // ../api/pokemon
    @GetMapping("pokemon")
    public ResponseEntity<List<Pokemon>> getPokemon() {
        List<Pokemon> pokemons = new ArrayList<>();
        pokemons.add(new Pokemon(1, "Squirtle", "Water"));
        pokemons.add(new Pokemon(2, "Pikachu", "Electric"));
        pokemons.add(new Pokemon(3, "Charmander", "Fire"));

        return ResponseEntity.ok(pokemons);
    }

    // Neues Mapping mit Variablen
    @GetMapping("pokemon{id}")
    // Bezieht sich auf Methode pokemonDetail also immer wenn die URL wie oben steht mit einer ID
    // wird pokemonDetail ausgeführt und kriegt @PathVariable id mit übergeben
    // Return einfach nur ID mit Squirtle und Water als Demo
    public Pokemon pokemonDetail(@PathVariable int id) {
        return new Pokemon(id, "Squirtle", "Water");
    }

    @PostMapping("pokemon/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Pokemon> createPokemon(@RequestBody Pokemon pokemon) {
        System.out.println(pokemon.getName());
        System.out.println(pokemon.getType());

        return new ResponseEntity<>(pokemon, HttpStatus.CREATED);
    }
}
