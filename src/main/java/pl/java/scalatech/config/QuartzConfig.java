package pl.java.scalatech.config;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.extern.slf4j.Slf4j;
import pl.java.scalatech.quartz.AutowiringSpringBeanJobFactory;
import pl.java.scalatech.service.TaskService;

@Configuration
@Slf4j
@ConditionalOnProperty(name = "quartz.enabled")
public class QuartzConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        log.debug("+++ QuartzConfig initialized.");
    }

    /*
     * @Bean
     * public SpringLiquibase liquibase() {
     * SpringLiquibase liquibase = new SpringLiquibase();
     * liquibase.setChangeLog("classpath:/db/changelog.xml");
     * liquibase.setDataSource(dataSource);
     * return liquibase;
     * }
     */

    @Bean
    public SchedulerFactoryBean quartzScheduler() {
        SchedulerFactoryBean quartzScheduler = new SchedulerFactoryBean();

        quartzScheduler.setDataSource(dataSource);
        quartzScheduler.setTransactionManager(transactionManager);
        quartzScheduler.setOverwriteExistingJobs(true);
        quartzScheduler.setSchedulerName("test-quartz-scheduler");

        // custom job factory of spring with DI support for @Autowired!
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        quartzScheduler.setJobFactory(jobFactory);

        quartzScheduler.setQuartzProperties(quartzProperties());
        Trigger[] triggers = { procesoMQTrigger().getObject() };
        quartzScheduler.setTriggers(triggers);
        return quartzScheduler;
    }

    @Bean
    public JobDetailFactoryBean procesoMQJob() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(TaskService.class);
        jobDetailFactory.setGroup("test-quartz");
        return jobDetailFactory;
    }

    @Bean
    public CronTriggerFactoryBean procesoMQTrigger() {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(procesoMQJob().getObject());
        cronTriggerFactoryBean.setCronExpression("0/2 * * * * ?");
        cronTriggerFactoryBean.setGroup("test-quartz");
        return cronTriggerFactoryBean;
    }

    @Bean
    public Properties quartzProperties() {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        Properties properties = null;
        try {
            propertiesFactoryBean.afterPropertiesSet();
            properties = propertiesFactoryBean.getObject();

        } catch (IOException e) {
            log.warn("Cannot load quartz.properties.");
        }

        return properties;
    }
}