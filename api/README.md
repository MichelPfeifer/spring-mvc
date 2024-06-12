# Spring Notizen

## Struktur
- model --> alle Modellierungsbestandteile, welche als Tabellen dargestellt werden sollen in deiner DB
- controller --> Kontrollieren HTTP Requests der Website, bestimmen, was ausgegeben wird nach CRUD
  - Für jede Modellklasse, welche irgendwie auf der Website vorhanden sein soll eine Controller Klasse
- repository --> Klassen liegen am nächsten zur Datenbank
- service

## CRUD
- C = Create = POST
- R = Read = GET
- U = Update = PUT
- D = Delete = DELETE

## Repository Pattern - N-Tier Architecture
- Controllers -> Services -> Repository -> SQL Database
- Repository = Am nächsten zur Datenbank
- Unterschied Service Repository = Repository nur dafür da, die Datenbank anzurühren, Service ist Logik dazwischen
- Repository = CRUD Aktionen auf Datenbank

# Repository Pattern
- Repository Pattern generell erstmal Designmuster. Man muss eine Website-Anwendung mit Datenbankintegration so nicht
aufbauen, wird aber häufig so verwendet

Hauptziel: Trennung der Datenzugriffsschicht von der Geschäftslogik, klare Struktur, bessere Wertbarkeit.

## model package
- Im package "model" befinden sich Entitäten. Diese Klassen stellen Tabellen in einer Datenbank, hier MySQL dar.
- Jede Entity Klasse ist dabei mit einer JPA-Annotation versehen
- Jede Entität ist eine eigene Tabelle und jedes Attribut/Field dieser Entitätsklasse stellt im Datenbank Backend eine
Spalte dar

Angenommen Anwendung, welche User und Blog Posts speicher --> User und Post Entity

Code User Entity:

package com.example.demo.entity;

import javax.persistence.*;
import java.util.List;


@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;

    // Getter und Setter


Code Post Entity:
package com.example.demo.entity;

import javax.persistence.*;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

Beschreibung:
- @Entity markiert die Klasse als JPA-Entität, die in der Datenbank persistiert wird+
- @Id definiert das Primärschlüssel der Entität
- @GeneratedValue gibt die Strategie für die Generierung der Primärschlüsselwerte an
  - IDENTITY ist hierbei automatische Inkrementierung der Datenbank
- @OneToMany und @ManyToOne Annotationen definieren die Beziehungen zwischen den Entities
  - User kann mehrere Posts haben deshalb @OneToMany bei User und @ManyToOne bei Post
  - @JoinColumn gibt die Spalte an, die die Beziehung in der Datenbank darstellt

Resultat: Grundlegende Datenstruktur, in der Benutzer und deren Blog-Posts gespeichert werden können

Tipp: Bei den Entities Lombok verwenden:
- @Data fügt sämtliche Getter und Setter hinzu, sowie Hashmaps usw.
- @AllArgsConstructor einen Constructor mit allen Attributen
- @NoArgsContructor einen Contructor mit keinen Attributen

## repository package
Nächstes Element: Repositories
- package "repository"
- Von jeder Datenbanktabelle (jeder Entity), von welcher CRUD Operationen ausgeführt werden sollen
bekommt ein Repository Interface

CRUD:
- Create - HTTP PUT
- Read - HTTP GET
- Update - HTTP UPDATE
- Delete - HTTP DELETE

Heißt: Jede Entity, mit der was gemacht werden soll, bekommt Repository Interface
- Beispiel: "PokemonRepository"
- Prinzip: Erbt von JpaRepository Interface (In Java können nur Interfaces von anderen Interfaces erben, keine Klassen)

JpaRepository Interface:
- Stellt eine Vielzahl von CRUD-Operationen (Create, Read, Update, Delete) bereit
- Spring Data JPA generiert automatisch die Implementierung dieser Schnittstelle
- Methoden durch JpaRepository:
  - findAll()
  - findById()
  - deleteById()
  - etc

## service package
Wer verwendet die Repository Klassen? --> Die Service Klasse:
- Zu jeder Repository Klasse dann dementsprechend eine Service Klasse
- Ziel: Service Klasse implementiert die Geschäftslogik, also sämtliche Berechnungen etc.

Service Klasse besitzt Geschäftslogik der Anwendung und verwendet (nicht implementiert)
das entsprechende Repository, um auf Datenbanken zuzugreifen

Aufbau:
- Im service Package allein befinden sich erstmal für alle geplanten Service Klasse
jeweilige Interfaces
  - Für PokemonServiceImpl befindet sich darüber ein PokemonService Interface
  - Die Klasse PokemonServiceImpl ist die eigentliche Service Klasse und implementiert das Interface
- PokemonServiceImpl implementiert PokemonService Interface (als Rahmenvorgabe)
und verwendet PokemonRepository Interface zum Datenbankzugriff

Service-Klasse:
- @Service Anntotation: Kennzeichnung der Klasse als Service-Komponente in Spring
  - Annotation beinhaltet @Component Annotation, welchen dafür sorgt, dass eine Service Instanz im
  - Bean Pool landet und verwendet werden kann (Dependency Injection)
    - Bei Entity ist kein Component drin, da diese nicht instanziiert werden sollen
- Implementiert PokemonService Interface --> Muss alle Methoden implementieren
- Inject von PokemonRepository, um in der Geschäftslogik Datenbankzugriff zu erhalten
  - Geschieht über ContructorInjection oder FieldInjection

ConstructorInjection:
@Service
public class PokemonServiceImpl implements PokemonService {
    private PokemonRepository pokemonRepository;

    @Autowired
    public PokemonServiceImpl(PokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

- @Autowired über dem Contructor zeigt Spring an, dass es eine geeignete Bean vom Typ
PokemonRepository gibt.
- PokemonRepository ist erstmal "nur" ein Interface
- Das Erben von JpaRepository erstellt allerdings im Hintergrund eine Klasse, welche dieses
Interface auch wirklich implementiert und somit die Methoden bereitstellt.
- Heißt über das injiziiert Objekt "pokemonRepository" lassen sich CRUD Operationen auf
der Pokemon Entität ausführen

Implementierung von Geschäftslogik:
- Oftmals, wie Einträge in Datenbanken gemacht werden sollen
- Bei PokemonServiceImpl "createPokemon":
  - Funktionalität, um einen neuen Pokemon Eintrag zu erstellen:

@Override
public PokemonDto createPokemon(PokemonDto pokemonDto) {
        Pokemon pokemon = new Pokemon();
        pokemon.setName(pokemonDto.getName());
        pokemon.setType(pokemonDto.getType());

        Pokemon newPokemon = pokemonRepository.save(pokemon);

        PokemonDto pokemonReponse = new PokemonDto();

        pokemonReponse.setId(newPokemon.getId());
        pokemonReponse.setName(newPokemon.getName());
        pokemonReponse.setType(newPokemon.getType());

        return pokemonReponse;
    }

- Hier wird die Logik implementiert, mit welcher Datenbankeinträge in die Tabelle Pokemon durchgeführt werden
- Hier wird ein PokemonDto übergeben

Einschub: DTO-Klassen
- Sind einfache POJOs (Plain Old Java Objects) und dienen rein zum
Datenaustausch zwischen verschiedenen Schichten oder Komponenten der Anwendung
- Repräsentieren normalerweise die Daten, die zwischen Backend und Frontend ausgetauscht werden
- ODER zwischen Schichten der Backend-Anwendung (bspw. Service - Controller)
- DTOs enthalten nur die Daten, welche für die bestimmte Operation benötigt werden
- Müssen nicht die Attribute enthalten, welche Entity Klassen besitzen
- REINER DATENAUSTAUSCH zwischen Komponenten

Bezug zu Service Schicht:
- Die Service Schicht ist dafür verantwortlich, Geschäftslogik zu implementieren:
  - Daten aus Entitätsobjekten zu lesen und an Controller weiterzugeben+
  - Daten aus DTO Objekten zu lesen und an Entitäten weiterzugeben
- Demnach verwendet die Service Schicht u. a. DTO Objekte zum Transfer zwischen Komponenten

Weiter mit "createPokemon": Einträge in Datentabelle Pokemon erstellen
- Neues Pokemon Objekt erstellen --> Instanz der Entity Pokemon, um diese zu befüllen
- Wird mit übergebenen Daten aus PokemonDto initialisiert
  - Daten aus PokemonDto werden gelesen und dem Entitäts-Objekt übergeben
- Das neue Pokemonobjekt wird in der Datenbank gespeichert

Connection zwischen PokemonRepository und Pokemon (Entity)
- Über Naming Convention:
  - Spring Data JPA erkennt, um welche Entity es sich handelt über Naming Conventions:
    - PokemonRepository --> Pokemon Entity
- Über Typisierung und Generics:
  - Man parametrisiert das Interface JPARepository mit <Pokemon, Integer>
  - Damit weiß das Interface, worauf die CRUD-Operationen ausgeführt werden sollen, so auch save()

Weiter mit Methode createPokemon():
- Die Daten des gespeicherten Pokemons werden in ein neues PokemonDto-Objekt kopiert
  - Daten werden aus dem Entity Objekt gelesen und in ein neues DTO-Objekt gepackt
  - Dieses dient dann zum Datentransfer und wird ausgegeben

Methode getAllPokemon():

    @Override
    public List<PokemonDto> getAllPokemon() {
        List <Pokemon> pokemon = pokemonRepository.findAll();

        return pokemon.stream().map(p -> mapToDto(p)).collect(Collectors.toList());
    }

    private PokemonDto mapToDto(Pokemon pokemon) {
        PokemonDto pokemonDto = new PokemonDto();
        pokemonDto.setId(pokemon.getId());
        pokemonDto.setName(pokemon.getName());
        pokemonDto.setType(pokemon.getType());
        return pokemonDto;
    }

- Ruft alle Pokemon aus der Datenbank abzurufen
- Rückgabe diesmal nicht 1 DTO sondern Liste von DTOs
- Erstellt Liste von PokemonDto Objekten über findAll() Methode des Repositories
- Dann wird Liste von Pokemonobjekten in ein Stream konvertiert
  - Durch den Stream wird jedes Pokemonobjekt in ein entsprechendes PokemonDto Objekt überführt
  - stream() --> map(...) --> collect(Collectors.toList())
  - Also stream beginnt Folge von Aktionen auf jedem Listenobjekt
  - dann map auf jedem einzelnen Objekt
    - mapToDto eigentlich genau das gleiche wie bei createPokemon
      - kriegt pokemon Entity Objekt und wandelt es in DTO um
  - dann collect um alle Elemente zu sammeln
  - dann zu Liste speichern

Service Schicht bietet damit eine klare Trennung zwischen der Controller Schicht, die
HTTP Anfragen verarbeitet und der Repository Schicht, die direkten Zugriff auf die
Datenbank bietet.

## controller package
Die Controller-Klasse ist für die Verarbeitung der HTTP-Anfragen verantwortlich und
verwendet die Service-Klasse, um die Geschäftslogik auszuführen.
- Controller vermittelt zwischen Service-Schicht und Benutzeroberfläche

Aufbau:

@RestController
@RequestMapping("/api/")
public class PokemonController {


- @RestController markiert die Klasse als REST-Controller, der JSON-Antworten zurückgibt
- @RequestMapping("/api") legt die Basis-URL für alle Endpunkte dieser Klasse fest
  - Alle weiteren Endpunkte werden mit /api/ beginnen

    private PokemonService pokemonService;

    @Autowired
    public PokemonController(PokemonService pokemonService) {
    this.pokemonService = pokemonService;
    }

- Um auf die Geschäftslogik, also Service-Schicht zuzugreifen und somit Datenbankeinträge
zu verändern, muss eine Service Instanz injiziert werden.
- Wieder mit ContructorInjection wie bei Service auch

GET-Mapping: Für GET-Anfragen --> Create (CRUD)

    @GetMapping("pokemon")
    public ResponseEntity<List<PokemonDto>> getPokemon() {
        return new ResponseEntity<>(pokemonService.getAllPokemon(), HttpStatus.OK);
    }

- @GetMapping("pokemon") definiert den Endpunkt /api/pokemon
  - Diese Methode wird aufgerufen, wenn eine GET-Anfrage an diesen Punkt gesendet wird
  - Spring scannt den Controller nach Methoden, die diesen Endpunkt behandeln
  - Die Methode wird dann aufgerufen und gibt eine ResponseEntity zurück

ResponseEntity:
- Klasse von Spring, die verwendet wird, um eine HTTP-Antwort zu erstellen
- Bietet vollständige Kontrolle über die HTTP-Antwort inklusive:
  - Antwortkörpers <List<PokemonDto>>
  - Statuscode HttpStatus.OK
  - Http Header

Wie sieht eine Beispielantwort im Frontend aus?
[
{
"id": 1,
"name": "Pikachu",
"type": "Electric"
},
{
"id": 2,
"name": "Charmander",
"type": "Fire"
}
]

- Wäre dann ja Liste an PokemonDTOs
- Gleiche dann auch mit anderen Möglich also mit Read, Update und Delete



