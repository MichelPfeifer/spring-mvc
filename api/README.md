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



