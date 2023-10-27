package fr.dgfip.springbatch.writer;

import fr.dgfip.springbatch.secondary.entity.Manager;
import fr.dgfip.springbatch.secondary.repository.ManagerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class MyCustomWriter implements ItemWriter<Manager> {

    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public void write(List<? extends Manager> managerList) throws Exception {
        log.info("MyCustomWriter : Writing data size : " + managerList.size());
        managerRepository.saveAll(managerList);
    }
}
