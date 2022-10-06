package me.aetios.sacola.repository;

import me.aetios.sacola.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestauranteReposity extends JpaRepository<Restaurante, Long> {
}
