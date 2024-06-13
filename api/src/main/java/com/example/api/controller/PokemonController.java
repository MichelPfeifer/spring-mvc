package com.example.api.controller;

import com.example.api.dto.PokemonDto;
import com.example.api.model.Pokemon;
import com.example.api.service.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

// Typ vom Controller --> Rest API
@RestController
//
@RequestMapping("/api/")
public class PokemonController {

    private PokemonService pokemonService;

    @Autowired
    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    // ../api/pokemon
    @GetMapping("pokemon")
    public ResponseEntity<List<PokemonDto>> getPokemon() {
        return new ResponseEntity<>(pokemonService.getAllPokemon(), HttpStatus.OK);
    }

    // Neues Mapping mit Variablen --> Auch wieder GET HTTP Anfrage, hier nur mit bestimmter ID
    @GetMapping("pokemon{id}")
    // GET Request
    // Bezieht sich auf Methode pokemonDetail also immer wenn die URL wie oben steht mit einer ID
    // wird pokemonDetail ausgeführt und kriegt @PathVariable id mit übergeben
    // Return einfach nur ID mit Squirtle und Water als Demo
    public ResponseEntity<PokemonDto> pokemonDetail(@PathVariable int id) {
        return ResponseEntity.ok(pokemonService.getPokemonById(id));
    }

    @PostMapping("pokemon/create")
    @ResponseStatus(HttpStatus.CREATED)
    // POST Request
    // PostMapping bezieht sich auf Create von CRUD und gibt einen HttpStatus CREATED wieder zurück
    // Man kann mit dieser Anfrage bzw. Struktur Daten ins System einbringen wie bspw neue Pokemon.
    // und diese nicht nur abfragen
    public ResponseEntity<PokemonDto> createPokemon(@RequestBody PokemonDto pokemonDto) {
        return new ResponseEntity<>(pokemonService.createPokemon(pokemonDto), HttpStatus.CREATED);
    }

    @PutMapping("pokemon/{id}/update")
    // PUT Request für Update CRUD
    public ResponseEntity<PokemonDto> updatePokemon(@RequestBody PokemonDto pokemonDto , @PathVariable("id") int pokemonId) {
        PokemonDto response = pokemonService.updatePokemon(pokemonDto, pokemonId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("pokemon/{id}/delete")
    // DELETE Request für Delete CRUD
    public ResponseEntity<String> deletePokemon(@PathVariable("id") int pokemonId) {
        pokemonService.deletePokemon(pokemonId);
        return new ResponseEntity<>("Pokemon deleted", HttpStatus.OK);
    }

}
