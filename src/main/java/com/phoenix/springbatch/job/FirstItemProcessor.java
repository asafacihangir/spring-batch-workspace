package com.phoenix.springbatch.job;

import com.phoenix.springbatch.mysql.domain.MysqlStudent;
import com.phoenix.springbatch.postgres.domain.PostgresStudent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class FirstItemProcessor implements ItemProcessor<PostgresStudent, MysqlStudent> {

  private final Logger logger = LoggerFactory.getLogger(FirstItemProcessor.class);

  @Override
  public MysqlStudent process(PostgresStudent item) throws Exception {

    logger.info("Processing student with information: {}", item.toString());

    final MysqlStudent mysqlStudent = new MysqlStudent();
    mysqlStudent.setId(item.getId());
    mysqlStudent.setFirstName(item.getFirstName());
    mysqlStudent.setLastName(item.getLastName());
    mysqlStudent.setEmail(item.getEmail());
    mysqlStudent.setUniversity(item.getUniversity());
    mysqlStudent.setActive(item.getActive());

    return mysqlStudent;
  }
}
