package com.phoenix.springbatch.postgres;

import com.github.javafaker.Faker;
import com.phoenix.springbatch.postgres.domain.PostgresStudent;
import com.phoenix.springbatch.postgres.repository.PostgresStudentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PostgresStudentService {

  private final PostgresStudentRepository repository;


  public PostgresStudentService(PostgresStudentRepository repository) {
    this.repository = repository;
  }


  @Transactional("postgresTransactionManager")
  public void createStudentData() {
    final List<PostgresStudent> postgresStudents = new ArrayList<>();
    final Faker faker = new Faker();
    for (int i = 0; i < 2500; i++) {
      final PostgresStudent postgresStudent = new PostgresStudent();
      postgresStudent.setFirstName(faker.name().firstName());
      postgresStudent.setLastName(faker.name().lastName());
      postgresStudent.setEmail(faker.internet().emailAddress());
      postgresStudent.setUniversity(faker.university().name());
      postgresStudent.setActive(ThreadLocalRandom.current().nextBoolean());

      postgresStudents.add(postgresStudent);
    }
    repository.saveAll(postgresStudents);
  }


}
