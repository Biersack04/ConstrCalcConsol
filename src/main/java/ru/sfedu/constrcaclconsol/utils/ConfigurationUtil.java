package ru.sfedu.constrcaclconsol.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static ru.sfedu.constrcaclconsol.Constants.CUSTOM_CONFIG_PATH;

/**
 * Configuration utility. Allows to get configuration properties from the
 * default configuration file
 *
 * @author Boris Jmailov
 */
public class ConfigurationUtil {

    private static final String DEFAULT_CONFIG_PATH = "./src/main/resources/enviroment.properties";
   // private static final String DEFAULT_CONFIG_PATH = "./enviroment.properties";
    private static final Properties configuration = new Properties();

    private static final String CUSTOM_CONFIG_PATH = System.getProperty("configPath");
    /**
     * Hides default constructor
     */

    public ConfigurationUtil() {
    }
    
    private static Properties getConfiguration() throws IOException {
        if(configuration.isEmpty()){
            loadConfiguration();
        }
        return configuration;
    }

    /**
     * Loads configuration from <code>DEFAULT_CONFIG_PATH</code>
     * @throws IOException In case of the configuration file read failure
     */
    private static void loadConfiguration() throws IOException{
        //File nf = new File(DEFAULT_CONFIG_PATH);
        File nf;
        if (CUSTOM_CONFIG_PATH!=null) {
            nf = new File(CUSTOM_CONFIG_PATH);
        }
        else{
            nf = new File(DEFAULT_CONFIG_PATH);
        };
        InputStream in = new FileInputStream(nf);// DEFAULT_CONFIG_PATH.getClass().getResourceAsStream(DEFAULT_CONFIG_PATH);
        try {
            configuration.load(in);
        } catch (IOException ex) {
            throw new IOException(ex);
        } finally{
            in.close();
        }
    }
    /**
     * Gets configuration entry value
     * @param key Entry key
     * @return Entry value by key
     * @throws IOException In case of the configuration file read failure
     */
    public static String getConfigurationEntry(String key) throws IOException{
        return getConfiguration().getProperty(key);
    }
    
}
