package com.example.demo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "USERS")
class User {

	private @Id @GeneratedValue Long id;
	private String name;
	private float salary;

	User(String name, float salary) {
		this.name = name;
		this.salary = salary;
	}

	// default constructor
	User() {
	}

}