package pl.bajtas.squaremoose.api.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;

import java.util.Properties;

/**
 * Created by Bajtas on 15.09.2016.
 */
@Configuration
public class HibernateConfig {
    @Bean
    public SessionFactory sessionFactory() {
        LocalSessionFactoryBuilder builder = new LocalSessionFactoryBuilder(dataSource());
        builder.scanPackages("pl.bajtas.squaremoose.api.domain").addProperties(getHibernateProperties());

        return builder.buildSessionFactory();
    }

    @Bean(name = "dataSource")
    public DataSource dataSource() {

        DataSource ds = new DataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://localhost:5432/TriangleMoose");
        ds.setUsername("postgres");
        ds.setPassword("czosnek");
        return ds;
    }

    private Properties getHibernateProperties() {
        Properties prop = new Properties();
        prop.put("hibernate.format_sql", "true");
        prop.put("hibernate.show_sql", "true");
        return prop;
    }
}
