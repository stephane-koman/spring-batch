package fr.dgfip.springbatch.processor;

import fr.dgfip.springbatch.primary.entity.Employee;
import fr.dgfip.springbatch.secondary.entity.Manager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyCustomProcessor implements ItemProcessor<Employee, Manager> {
    @Override
    public Manager process(Employee employee) {
        log.info("MyBatchProcessor : Processing data : " + employee);
        return new Manager(
                employee.getId(),
                employee.getName(),
                employee.getSalary()
        );
    }

}
