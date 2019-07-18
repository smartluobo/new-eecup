package com.ibay.tea.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "api.sys")
@Component
public class ApiSysProperties {
    private static String staticCertPath;

    private String certPath;

    private String environment;


    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
        staticCertPath = certPath;
    }

    public static String getStaticCertPath(){
        return staticCertPath;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}
