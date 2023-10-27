package fr.dgfip.springbatch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Configuration
@EnableScheduling
@Slf4j
public class SchedulerConfig {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

    @Scheduled(fixedDelay = 5000, initialDelay = 5000)
    public void scheduleByFixedRate() throws Exception{
        log.info("Batch job starting");
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("time", FORMAT.format(Calendar.getInstance().getTime())).toJobParameters();
        jobLauncher.run(job, jobParameters);
        log.info("Batch job executed successfully");
    }
}
