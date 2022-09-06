package org.bahmni.module.feedintegration;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.bahmni.module.feedintegration.atomfeed.jobs.FeedJob;
import org.bahmni.module.feedintegration.model.openmrsPatientFeedForCraterJob;
import org.bahmni.module.feedintegration.repository.CronJobRepository;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.PeriodicTrigger;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@ComponentScan(basePackages = "org.bahmni.module.*")
@EnableTransactionManagement
@Configuration
public class Application extends SpringBootServletInitializer implements SchedulingConfigurer{

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CronJobRepository cronJobRepository;

    private Map<String, FeedJob> jobs = new HashMap<String, FeedJob>();

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        logger.info("************ Starting Crater Atomfeed app **********");
        SpringApplication.run(Application.class, args);
        logger.info("************ Started Crater Atomfeed app **********");
    }

    @Bean
    public SessionFactory sessionFactory(HibernateEntityManagerFactory hemf) {
        return hemf.getSessionFactory();
    }

    @Bean(destroyMethod = "shutdown")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(1);
    }

    private Trigger getTrigger(openmrsPatientFeedForCraterJob quartzCronScheduler) throws ParseException {
        PeriodicTrigger periodicTrigger;
        Date now = new Date();
        long nextExecutionTimeByStatement = new CronExpression(quartzCronScheduler.getCronStatement()).getNextValidTimeAfter(now).getTime();
        periodicTrigger = new PeriodicTrigger((int) (nextExecutionTimeByStatement - now.getTime()), TimeUnit.MILLISECONDS);
        periodicTrigger.setInitialDelay(quartzCronScheduler.getStartDelay());
        return periodicTrigger;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        final List<openmrsPatientFeedForCraterJob> cronJobs = cronJobRepository.findAll();

        for (final openmrsPatientFeedForCraterJob quartzCronScheduler : cronJobs) {
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

    private Runnable getTask(final openmrsPatientFeedForCraterJob quartzCronScheduler) {
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