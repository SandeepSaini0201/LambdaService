package com.crptm.lambdaservice.utils;

import com.crptm.lambdaservice.constants.ErrorConstants;
import com.crptm.lambdaservice.enums.EnumEnvironment;
import com.crptm.lambdaservice.exception.PropertyInitializeException;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class PropertiesUtils {

    private static Properties props;

    public static Properties getProps() {
        return props;
    }

    public static void initializeProperties(final EnumEnvironment environment) {
        try {
            InputStream in = PropertiesUtils.class.getClassLoader()
                    .getResourceAsStream("application-" + environment.getEnvironmentName() + ".properties");
            Properties properties = new Properties();
            properties.load(in);
            props = properties;
        } catch (Exception ignored) {
            String errorMsg = String.format(ErrorConstants.PROPERTY_INITIALIZE_FAILURE, environment.name());
            log.error(errorMsg);
            throw new PropertyInitializeException(errorMsg);
        }
    }
}
