package com.developkim.rabbitmq.repository;

import com.developkim.rabbitmq.model.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<StockEntity, Long> {

}
