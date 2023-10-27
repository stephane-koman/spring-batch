package fr.dgfip.springbatch;

import fr.dgfip.springbatch.primary.entity.Employee;
import fr.dgfip.springbatch.processor.MyCustomProcessor;
import fr.dgfip.springbatch.reader.MyCustomReader;
import fr.dgfip.springbatch.secondary.entity.Manager;
import fr.dgfip.springbatch.writer.MyCustomWriter;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class SpringBatchConfig {

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private MyCustomReader myCustomReader;
    private MyCustomWriter myCustomWriter;
    private MyCustomProcessor myCustomProcessor;

    @Bean
    public Job createJob(){
        return jobBuilderFactory.get("MyJob")
                .incrementer(new RunIdIncrementer())
                .flow(createStep())
                .end().build();
    }

    @Bean
    public Step createStep() {
        return stepBuilderFactory.get("MyStep")
                .<Employee, Manager>chunk(10)
                .reader(myCustomReader)
                .processor(myCustomProcessor)
                .writer(myCustomWriter)
                .build();
    }
}
