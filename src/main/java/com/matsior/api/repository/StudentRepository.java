package com.matsior.api.repository;

import com.matsior.api.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentRepository extends MongoRepository<Student, String> {
}
