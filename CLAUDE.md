# PetClinic Vets Service

## Overview
Manages veterinarian data and their specialties. Part of the PetClinic multi-repo microservices demo.

## Tech Stack
- Java 17, Spring Boot 4.0.1, Spring Cloud 2025.1.0
- Spring Data JPA + HSQLDB (in-memory, default) or MySQL (`mysql` profile)
- Caffeine cache for vet list
- Eureka client for service discovery
- Spring Cloud Config client

## Package Structure
```
org.springframework.samples.petclinic.vets
├── VetsServiceApplication.java     # Main class
├── model/
│   ├── Vet.java                    # JPA entity
│   ├── Specialty.java              # JPA entity
│   └── VetRepository.java          # Spring Data repo
├── system/
│   ├── CacheConfig.java            # Caffeine cache config
│   └── VetsProperties.java         # Config properties
└── web/
    └── VetResource.java            # REST controller — /vets
```

## API Endpoints
- `GET /vets` — list all vets with specialties (cached)

## Build & Run
```bash
mvn clean package -DskipTests
# Runs on port 8083 (set by Config Server, random locally)
```

## Database
- Schema: `src/main/resources/db/hsqldb/schema.sql`
- Seed data: `src/main/resources/db/hsqldb/data.sql`
- Tables: `vets`, `specialties`, `vet_specialties`

## Service Registration
Registers as `vets-service` with Eureka. The API Gateway routes `/api/vet/**` to this service.
