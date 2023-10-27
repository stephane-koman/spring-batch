package fr.dgfip.springbatch.secondary.repository;

import fr.dgfip.springbatch.secondary.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
}
