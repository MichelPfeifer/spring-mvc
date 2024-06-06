package com.example.api.repository;

import com.example.api.model.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;

// JPARepository gibt eine Reihe an Methoden um CRUD Operations auf Datenbanken durchzuführen
// Repositories aber nur Interface, welche von Service implementiert werden
public interface PokemonRepository extends JpaRepository<Pokemon, Integer> {
}
