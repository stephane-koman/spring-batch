package fr.dgfip.springbatch.reader;

import fr.dgfip.springbatch.primary.entity.Employee;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class MyCustomReader extends JdbcCursorItemReader<Employee> implements ItemReader<Employee> {

    public MyCustomReader(@Autowired DataSource primaryDataSource) {
        setDataSource(primaryDataSource);
        setSql("SELECT id, name, salary FROM employee");
        setFetchSize(100);
        setRowMapper(new EmployeeRowMapper());
    }

}
