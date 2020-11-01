package ru.sfedu.constrcaclconsol;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.constrcaclconsol.utils.ConfigurationUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConstrCalcClient {


    private static Logger log = LogManager.getLogger(ConstrCalcClient.class);
    private static List<Work> listOfWorks = new ArrayList<Work>();
    private static String work_str;
    private static String[] words;

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

        File file = new File(Constants.WORKS_ENVIROMENT_PATH);
        Properties properties = new Properties();
        properties.load(new FileReader(file));

        //Возвращает список всех ключей
        for (String key : properties.stringPropertyNames())
        {
            log.info(properties.get(key));
        }

        for (int i=1;i<=Constants.WORKS_NUMBER;i++) {

            work_str = properties.getProperty("work" + i);
            log.info(work_str);
            words = work_str.split("\\|");
            for (String word : words) {

                //  log.info(word);
            }
                Work work = new Work();
                work.setId(i);
                work.setName(words[0]);
                work.setPrice(Long.parseLong(words[1]));
                log.info(work.getId());
                log.info(work.getName());
                log.info(work.getPrice());
                listOfWorks.add(work);
            }

        for(int i = 0; i< listOfWorks.size(); i++) {
            log.info(listOfWorks.get(i).getId());
            log.info(listOfWorks.get(i).getName());
            log.info(listOfWorks.get(i).getPrice());
        }
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

