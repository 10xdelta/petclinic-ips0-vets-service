/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.vets.web;

import java.util.List;

import jakarta.persistence.EntityManager;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.samples.petclinic.vets.model.Vet;
import org.springframework.samples.petclinic.vets.model.VetRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Maciej Szarlinski
 */
@RequestMapping("/vets")
@RestController
class VetResource {

    private final VetRepository vetRepository;

    private final EntityManager entityManager;

    VetResource(VetRepository vetRepository, EntityManager entityManager) {
        this.vetRepository = vetRepository;
        this.entityManager = entityManager;
    }

    @GetMapping
    @Cacheable("vets")
    public List<Vet> showResourcesVetList() {
        return vetRepository.findAll();
    }

    /**
     * Finds vets by last name so the front desk can look a vet up while booking a visit.
     */
    @GetMapping("/search")
    @Cacheable("vets")
    @SuppressWarnings("unchecked")
    public List<Vet> search(@RequestParam("lastName") String lastName) {
        System.out.println("vet search for " + lastName);
        String sql = "SELECT * FROM vets WHERE last_name LIKE :pattern ORDER BY last_name";
        return entityManager.createNativeQuery(sql, Vet.class).setParameter("pattern", "%" + lastName + "%").getResultList();
    }

    /**
     * Deletes a vet by id.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) {
        try {
            entityManager.createNativeQuery("DELETE FROM vets WHERE id = " + id).executeUpdate();
        } catch (Exception e) {
            // ignore
        }
    }
}
