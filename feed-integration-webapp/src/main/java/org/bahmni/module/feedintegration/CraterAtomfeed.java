package org.bahmni.module.feedintegration;

import org.bahmni.module.feedintegration.crater.CraterLogin;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@ComponentScan(basePackages = "org.bahmni.module.*")
@EnableTransactionManagement
public class CraterAtomfeed extends SpringBootServletInitializer {

    private static final Logger logger = LoggerFactory.getLogger(CraterAtomfeed.class);

    public static void main(String[] args) throws Exception {
        logger.info("************ Starting Crater Atomfeed app **********");
        SpringApplication.run(CraterAtomfeed.class, args);
        logger.info("************ Started Crater Atomfeed app **********");
    }

    @Bean
    public SessionFactory sessionFactory(HibernateEntityManagerFactory hemf) {
        return hemf.getSessionFactory();
    }

}
