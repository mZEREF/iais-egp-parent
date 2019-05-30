package com.ecquaria.cloud.moh.iais.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@PropertySource({"classpath:iais-datasource.properties"})
@EnableJpaRepositories(
        basePackages = "com.ecquaria.cloud.moh.iais",
        entityManagerFactoryRef = "iaisEntityManager",
        transactionManagerRef = "iaisTransactionManager"
)
public class IaisDataSource {
    @Autowired
    private Environment environment;

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean iaisEntityManager() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(iaisDataSource());
        em.setPackagesToScan(
                new String[] { "com.ecquaria.cloud.moh.iais.entity" });

        HibernateJpaVendorAdapter vendorAdapter
                = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect",
                environment.getProperty("spring.iais.jpa.properties.hibernate.dialect"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Primary
    @Bean
    public DataSource iaisDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty("spring.iais.datasource.driver-class-name"));
        dataSource.setUrl(environment.getProperty("spring.iais.datasource.url"));
        dataSource.setUsername(environment.getProperty("spring.iais.datasource.username"));
        dataSource.setPassword(environment.getProperty("spring.iais.datasource.password"));

        return dataSource;
    }

    @Primary
    @Bean
    public PlatformTransactionManager iaisTransactionManager() {

        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                iaisEntityManager().getObject());
        return transactionManager;
    }
}
