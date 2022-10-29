package com.crptm.lambdaservice.client;

import com.crptm.lambdaservice.constants.ErrorConstants;
import com.crptm.lambdaservice.dao.entity.RefundTxnEntity;
import com.crptm.lambdaservice.dao.entity.UserTxnEntity;
import com.crptm.lambdaservice.exception.ErrorOpeningSessionException;
import com.crptm.lambdaservice.utils.PropertiesUtils;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Properties;

@Component
@Slf4j
public class MySQLClient {

    public SessionFactory sessionFactory;
    private final String username = PropertiesUtils.getProps().getProperty("hibernate.datasource.username");
    private final String password = PropertiesUtils.getProps().getProperty("hibernate.datasource.password");
    private final String datasourceUrl = PropertiesUtils.getProps().getProperty("hibernate.datasource.url");
    private final String driverName = PropertiesUtils.getProps().getProperty("hibernate.datasource.driver-class-name");
    private final String dialect = PropertiesUtils.getProps().getProperty("hibernate.datasource.dialect");
    private final String timeZone = PropertiesUtils.getProps().getProperty("hibernate.jdbc.time-zone");

    @PostConstruct
    public void initiateSessionFactory() {
        try {
            Properties properties = new Properties();
            properties.put(Environment.DRIVER, driverName);
            properties.put(Environment.URL, datasourceUrl);
            properties.put(Environment.USER, username);
            properties.put(Environment.PASS, password);
            properties.put(Environment.DIALECT, dialect);
            properties.put(Environment.JDBC_TIME_ZONE, timeZone);
            properties.put(Environment.SHOW_SQL, "true");
            sessionFactory = new Configuration().addProperties(properties)
                    .addAnnotatedClass(UserTxnEntity.class)
                    .addAnnotatedClass(RefundTxnEntity.class)
                    .buildSessionFactory();
        } catch (Exception e) {
            String errorMsg = ErrorConstants.ERROR_OPENING_HIBERNATE_SESSION;
            log.error(errorMsg, e);
            throw new ErrorOpeningSessionException(errorMsg);
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
