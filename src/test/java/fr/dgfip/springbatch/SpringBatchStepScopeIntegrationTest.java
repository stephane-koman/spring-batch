package fr.dgfip.springbatch;

import fr.dgfip.springbatch.config.AbstractIntegrationTest;
import fr.dgfip.springbatch.primary.entity.Employee;
import fr.dgfip.springbatch.secondary.entity.Manager;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@SpringBatchTest
public class SpringBatchStepScopeIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private JdbcCursorItemReader<Employee> itemReader;

    @Autowired
    private ItemProcessor<Employee, Manager> itemProcessor;

    @Autowired
    private ItemWriter<Manager> itemWriter;

    @BeforeEach
    void setUp(){
        employeeRepository.deleteAll();
    }

    @AfterEach
    void tearDown(){
        jobRepositoryTestUtils.removeJobExecutions();
        employeeRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    void givenMockedStep_whenReaderCalled_thenSuccess() {

        //GIVEN
        employeeRepository.save(Employee.builder().id(1).name("name01").salary(1000).build());
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(defaultJobParameters);

        //WHEN
        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            Employee employee;
            itemReader.open(stepExecution.getExecutionContext());
            while ((employee = itemReader.read()) != null) {

                //THEN
                assertEquals(1, employee.getId());
                assertEquals("name01", employee.getName());
                assertEquals(1000, employee.getSalary());
            }
            itemReader.close();
            return null;
        });
    }

    @Test
    @SneakyThrows
    void givenMockedStep_whenProcessCalled_thenSuccess() {

        //GIVEN
        Employee employee = Employee.builder().id(1).name("stephane").salary(10000).build();
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(defaultJobParameters);

        //WHEN
        Manager manager = StepScopeTestUtils.doInStepScope(stepExecution, () -> itemProcessor.process(employee));

        //THEN
        assertEquals(employee.getName(), manager.getName());
        assertEquals(employee.getSalary(), manager.getSalary());
    }

    @Test
    @SneakyThrows
    void givenMockedStep_whenWriterCalled_thenSuccess() {

        //GIVEN
        Manager manager = Manager.builder().name("stephane").salary(10000).build();
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(defaultJobParameters);

        //WHEN
        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            itemWriter.write(Collections.singletonList(manager));
            return null;
        });
        List<Manager> managerList = managerRepository.findAll();
        Manager actualResult = managerList.get(0);

        //THEN
        assertEquals(1, managerList.size());
        assertEquals(manager.getName(), actualResult.getName());
        assertEquals(manager.getSalary(), actualResult.getSalary());
    }
}
