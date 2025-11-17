package com.example.demo.Repositories;

import com.example.demo.Models.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealRepository extends JpaRepository<Deal,String> {
}
