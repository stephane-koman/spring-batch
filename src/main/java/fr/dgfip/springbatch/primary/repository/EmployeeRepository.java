package fr.dgfip.springbatch.primary.repository;

import fr.dgfip.springbatch.primary.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
