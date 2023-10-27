package fr.dgfip.springbatch.config;

import fr.dgfip.springbatch.SpringBatchApplication;
import fr.dgfip.springbatch.SpringBatchConfig;
import fr.dgfip.springbatch.primary.repository.EmployeeRepository;
import fr.dgfip.springbatch.secondary.repository.ManagerRepository;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Testcontainers
@TestPropertySource("classpath:application-test.yaml")
@SpringJUnitConfig({SpringBatchConfig.class, SpringBatchApplication.class})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class AbstractIntegrationTest {

    @Autowired
    protected EmployeeRepository employeeRepository;

    @Autowired
    protected ManagerRepository managerRepository;

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

    protected JobParameters defaultJobParameters = new JobParametersBuilder()
            .addString("time", FORMAT.format(Calendar.getInstance().getTime())).toJobParameters();

    @Container
    private static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER_1 = new PostgreSQLContainer<>(DockerImageName.parse("postgres:14.2-alpine"))
            .withCopyFileToContainer(
                    MountableFile.forClasspathResource(
                            "init-db1.sql"), "/docker-entrypoint-initdb.d/"
            ).withReuse(true);

    @Container
    private static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER_2 = new PostgreSQLContainer<>(DockerImageName.parse("postgres:14.2-alpine"))
            .withCopyFileToContainer(
                    MountableFile.forClasspathResource(
                            "init-db2.sql"), "/docker-entrypoint-initdb.d/"
            ).withReuse(true);

    @DynamicPropertySource
    static void overrideTestProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_SQL_CONTAINER_1::getJdbcUrl);
        registry.add("spring.datasource.jdbc-url", POSTGRES_SQL_CONTAINER_1::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_SQL_CONTAINER_1::getUsername);
        registry.add("spring.datasource.password", POSTGRES_SQL_CONTAINER_1::getPassword);

        registry.add("spring.seconddatasource.url", POSTGRES_SQL_CONTAINER_2::getJdbcUrl);
        registry.add("spring.seconddatasource.jdbc-url", POSTGRES_SQL_CONTAINER_2::getJdbcUrl);
        registry.add("spring.seconddatasource.username", POSTGRES_SQL_CONTAINER_2::getUsername);
        registry.add("spring.seconddatasource.password", POSTGRES_SQL_CONTAINER_2::getPassword);
    }

}
