package ru.sfedu.constrcaclconsol;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;


public class ConstrCalcClient {


    private static Logger log = LogManager.getLogger(ConstrCalcClient.class);


    public ConstrCalcClient() {

        log.debug("LogClient : starting application.........");
        log.error("Launching the application...");
        log.debug("Operating System: " + System.getProperty("os.name") + " " +
                System.getProperty("os.version"));
        log.info("JRE: " + System.getProperty("java.version"));
        log.info("Java Launched From: " + System.getProperty("java.home"));
        log.info("Class Path: " + System.getProperty("java.class.path"));
        log.info("Library Path: " + System.getProperty("java.library.path"));
        log.info("User Home Directory: " + System.getProperty("user.home"));
        log.info("User Working Directory: " + System.getProperty("user.dir"));
        log.fatal("Test INFO logging.");

    }

    public static void main(String[] args) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

    new ConstrCalcClient();
    }
}
