package com.matsior.api;

import com.matsior.api.model.Address;
import com.matsior.api.model.Gender;
import com.matsior.api.model.Student;
import com.matsior.api.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class StudentApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentApiApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(
            StudentRepository repository, MongoTemplate mongoTemplate) {
        return args -> {
            Address address = new Address(
                    "Poland",
                    "Gdynia",
                    "81-198"
            );
            Student student = new Student(
                    "John",
                    "Doe",
                    "john@doe.com",
                    Gender.MALE,
                    address,
                    List.of("Computer Science", "Maths"),
                    BigDecimal.TEN,
                    LocalDateTime.now()
            );

//            usingMongoTemplateAndQuery(repository, mongoTemplate, student);

            repository.findStudentByEmail(student.getEmail())
                    .ifPresentOrElse(stud -> {
                        System.out.println(stud + " already exists");
                    }, () -> {
                        System.out.println("Inserting student " + student);
                        repository.save(student);
                    } );
        };
    }

    private void usingMongoTemplateAndQuery(StudentRepository repository, MongoTemplate mongoTemplate, Student student) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is("john@doe.com"));

        List<Student> students = mongoTemplate.find(query, Student.class);

        if (students.size() > 1) {
            throw new IllegalStateException("found many students with email " + "john@doe.com");
        }

        if (students.isEmpty()) {
            System.out.println("Inserting student " + student);
            repository.save(student);
        } else {
            System.out.println(student + " already exists");
        }
    }
}
