//package no.dnb.reskill.ec_webserver.configurations.sql;
//
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.persistence.EntityManagerFactory;
//import javax.sql.DataSource;
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//@EnableJpaRepositories(
//        entityManagerFactoryRef = "db1EntityMgrFactory",
//        transactionManagerRef = "db1TransactionMgr",
//        basePackages = {
//                "no.dnb.reskill.ec_webserver.repositories.sql"
//        })
//@EnableTransactionManagement
//public class SqlConfig {
//
//    @Bean(name = "datasource1")
//    @ConfigurationProperties(prefix = "spring.db1.datasource")
//    @Primary
//    // setting up the data source for the beer database.
//    public DataSource dataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean(name = "db1EntityMgrFactory")
//    @Primary
//    public LocalContainerEntityManagerFactoryBean db1EntityMgrFactory(
//            final EntityManagerFactoryBuilder builder,
//            @Qualifier("datasource1") final DataSource dataSource) {
//        // dynamically setting up the hibernate properties for each of the datasource.
//        final Map<String, String> properties = new HashMap<>();
//        properties.put("hibernate.hbm2ddl.auto", "create-drop");
//        // in springboot2 the dialect can be automatically detected.
//        // we are setting up here just to avoid any incident.
//        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
//        return builder
//                .dataSource(dataSource)
//                .properties(properties)
//                .packages("no.dnb.reskill.ec_webserver.models.sql")
//                .persistenceUnit("beer")
//                .build();
//    }
//
//    @Bean(name = "db1TransactionMgr")
//    @Primary
//    public PlatformTransactionManager db1TransactionMgr(
//            @Qualifier("db1EntityMgrFactory") final EntityManagerFactory entityManagerFactory) {
//        return new JpaTransactionManager(entityManagerFactory);
//    }
//}