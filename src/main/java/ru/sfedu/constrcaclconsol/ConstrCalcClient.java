package ru.sfedu.constrcaclconsol;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.constrcaclconsol.utils.ConfigurationUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConstrCalcClient {


    private static Logger log = LogManager.getLogger(ConstrCalcClient.class);

    public  ConstrCalcClient() {

        log.debug("LogClient : starting application.........");
    }

    public static void main(String[] args) throws IOException {
       /* new ConstrCalcClient() ;
        logBasicSystemInfo();
        log.info(Constants.TEST_CONST);
        log.info(ConfigurationUtil.getConfigurationEntry(Constants.ENV_CONST));
        log.info(String.format("%.5s",Constants.FORMAT_STRING));
        log.error("Error log");*/
        File file = new File("works.properties");
        Properties properties = new Properties();
        properties.load(new FileReader(file));

        String work_str = properties.getProperty("work" + 1);
        log.debug("work_str", work_str);




    }


   /* public static void logBasicSystemInfo() {
        log.info("Launching the application...");
        log.info("Operating System: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
        log.trace("JRE: " + System.getProperty("java.version"));
        log.debug("Java Launched From: " + System.getProperty("java.home"));
        log.info("Class Path: " + System.getProperty("java.class.path"));
        log.info("Library Path: " + System.getProperty("java.library.path"));
        log.warn("User Home Directory: " + System.getProperty("user.home"));
        log.info("User Working Directory: " + System.getProperty("user.dir"));
        log.info("Test INFO logging.");

    }*/
}

