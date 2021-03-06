package ivashproject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:db.properties")
@ComponentScan("ivashproject")
@EnableTransactionManagement
@EnableWebMvc
public class AppInit implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;

    @Autowired
    public AppInit(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Autowired
    Environment env;


    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/view/");
        templateResolver.setSuffix(".html");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        registry.viewResolver(resolver);
    }

    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource dm = new DriverManagerDataSource();
        dm.setDriverClassName(env.getProperty("db.driver"));
        dm.setUsername(env.getProperty("db.login"));
        dm.setPassword(env.getProperty("db.password"));
        dm.setUrl(env.getProperty("db.url"));
        return dm;
    }

    @Bean
    public HibernateJpaVendorAdapter getAdapter(){
        HibernateJpaVendorAdapter adapter=new HibernateJpaVendorAdapter();
        return adapter;
    }
    @Bean
    public LocalContainerEntityManagerFactoryBean emf(){
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(getDataSource());
        emf.setJpaVendorAdapter(getAdapter());
        emf.setPackagesToScan("ivashproject"); //The packages to search for Entities, line required to avoid looking into the persistence.xm
        return emf;
    }
    @Bean
   public JpaTransactionManager getJPAManager(){
        JpaTransactionManager manager=new JpaTransactionManager();
        manager.setEntityManagerFactory(emf().getObject());
        return manager;
    }




}
