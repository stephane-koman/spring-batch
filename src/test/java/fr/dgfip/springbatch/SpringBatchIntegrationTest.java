package fr.dgfip.springbatch;

import fr.dgfip.springbatch.config.AbstractIntegrationTest;
import fr.dgfip.springbatch.primary.entity.Employee;
import fr.dgfip.springbatch.secondary.entity.Manager;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SpringBatchTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SpringBatchIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Test
    @DisplayName("Should read table EMPLOYEE and return all data in this table and verify the size that should be 3")
    @Order(1)
    void should_return_3_employees_at_startup(){
        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees).hasSize(3);
    }

    @Test
    @DisplayName("Should read table MANAGER and return all data in this table and verify the size that should be 0")
    @Order(2)
    void should_return_empty_data_at_startup(){
        List<Manager> managers = managerRepository.findAll();
        assertThat(managers).hasSize(0);
    }

    @Test
    @DisplayName("Should save 3 employees, convert these employees in 3 managers, save them and check the size that should be 3")
    @Order(3)
    @SneakyThrows
    void should_return_3_managers_after_job_process(){
        //GIVEN
        saveEmployees();

        //WHEN

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(defaultJobParameters);
        JobInstance jobInstance = jobExecution.getJobInstance();
        ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

        //THEN
        Assertions.assertEquals("MyJob", jobInstance.getJobName());
        Assertions.assertEquals(ExitStatus.COMPLETED, actualJobExitStatus);

        List<Manager> managers = managerRepository.findAll();
        assertThat(managers).hasSize(3);
    }

    private void saveEmployees() {
        employeeRepository.saveAll(
                Arrays.asList(
                        Employee.builder().id(1).name("name01").salary(1000).build(),
                        Employee.builder().id(2).name("name02").salary(2000).build(),
                        Employee.builder().id(3).name("name03").salary(3000).build()
                )
        );
    }

    @Test
    @Order(4)
    @SneakyThrows
    void givenReferenceOutput_whenStepMyStepExecuted_theSuccess(){
        //GIVEN
        saveEmployees();
        int expectedStepSize = 1;
        int expectedResultSize = 3;
        String stepName = "MyStep";

        //WHEN
        JobExecution jobExecution = jobLauncherTestUtils.launchStep(stepName, defaultJobParameters);
        Collection<StepExecution> actualStepExecutions = jobExecution.getStepExecutions();
        ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

        //THEN
        Assertions.assertEquals(expectedStepSize, actualStepExecutions.size());
        Assertions.assertEquals(ExitStatus.COMPLETED, actualJobExitStatus);
        actualStepExecutions.forEach(stepExecution -> {
            Assertions.assertEquals(stepName, stepExecution.getStepName());
            Assertions.assertEquals(expectedResultSize, stepExecution.getWriteCount());
        });
    }

    @AfterEach
    void tearDown(){
        employeeRepository.deleteAll();
        managerRepository.deleteAll();
        jobRepositoryTestUtils.removeJobExecutions();
    }

}
