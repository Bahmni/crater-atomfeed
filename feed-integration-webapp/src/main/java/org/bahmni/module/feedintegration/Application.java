package org.bahmni.module.feedintegration;

import org.bahmni.module.feedintegration.atomfeed.jobs.FeedJob;
import org.bahmni.module.feedintegration.model.OpenMRSPatientFeedForCraterJob;
import org.bahmni.module.feedintegration.repository.CronJobRepository;
import org.ict4h.atomfeed.jdbc.AtomFeedJdbcTransactionManager;
import org.ict4h.atomfeed.jdbc.JdbcConnectionProvider;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@SpringBootApplication
@ComponentScan(basePackages = "org.bahmni.module.feedintegration.*")
@EnableTransactionManagement
@Configuration
@EnableScheduling
public class Application extends SpringBootServletInitializer implements SchedulingConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CronJobRepository cronJobRepository;

    private Map<String, FeedJob> jobs = new HashMap<String, FeedJob>();

    public static void main(String[] args) throws Exception {
        logger.info("************ Starting Crater Atomfeed app **********");
        SpringApplication.run(Application.class, args);
        logger.info("************ Started Crater Atomfeed app **********");
    }
    
    @Bean
    public AtomFeedJdbcTransactionManager atomFeedJdbcTransactionManager(JdbcConnectionProvider jdbcConnectionProvider){
        return new AtomFeedJdbcTransactionManager(jdbcConnectionProvider);
    }

    @Bean(destroyMethod = "shutdown")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(1);
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        final List<OpenMRSPatientFeedForCraterJob> cronJobs = cronJobRepository.findAll();

        for (final OpenMRSPatientFeedForCraterJob quartzCronScheduler : cronJobs) {
            jobs.put(quartzCronScheduler.getName(), ((FeedJob) applicationContext.getBean(quartzCronScheduler.getName())));

            try {
                taskRegistrar.setScheduler(taskExecutor());
                taskRegistrar.addTriggerTask(getTask(quartzCronScheduler), getTrigger(quartzCronScheduler));
            } catch (ParseException e) {
                logger.error("Could not parse the cron statement: " + quartzCronScheduler.getCronStatement() + " for: " + quartzCronScheduler.getName());
                e.printStackTrace();
            }
        }
    }

    private Trigger getTrigger(OpenMRSPatientFeedForCraterJob quartzCronScheduler) throws ParseException {
        PeriodicTrigger periodicTrigger;
        Date now = new Date();
        long nextExecutionTimeByStatement = new CronExpression(quartzCronScheduler.getCronStatement()).getNextValidTimeAfter(now).getTime();
        periodicTrigger = new PeriodicTrigger((int) (nextExecutionTimeByStatement - now.getTime()), TimeUnit.MILLISECONDS);
        periodicTrigger.setInitialDelay(quartzCronScheduler.getStartDelay());
        return periodicTrigger;
    }

    private Runnable getTask(final OpenMRSPatientFeedForCraterJob quartzCronScheduler) {
        return new Runnable() {
            @Override
            public void run() {
                FeedJob feedJob = jobs.get(quartzCronScheduler.getName());
                try {
                    feedJob.process();
                } catch (InterruptedException e) {
                    logger.warn("Thread interrupted for the job: " + quartzCronScheduler.getName());
                }
            }
        };
    }
}
