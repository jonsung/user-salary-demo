package com.example.demo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE ?1 <= u.salary AND u.salary <= ?2") 
    List<User> findAllInRange(Float lower, Float upper);
}