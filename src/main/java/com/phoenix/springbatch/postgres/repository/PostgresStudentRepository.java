package com.phoenix.springbatch.postgres.repository;

import com.phoenix.springbatch.postgres.domain.PostgresStudent;
import org.springframework.data.repository.CrudRepository;

public interface PostgresStudentRepository extends CrudRepository<PostgresStudent, Long> {

}
