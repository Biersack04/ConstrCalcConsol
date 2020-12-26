package ru.sfedu.constrcaclconsol;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.constrcaclconsol.api.DataProvider;
import ru.sfedu.constrcaclconsol.api.DataProviderCsv;
import ru.sfedu.constrcaclconsol.api.DataProviderXml;
import ru.sfedu.constrcaclconsol.bean.Materials;
import ru.sfedu.constrcaclconsol.bean.Works;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static ru.sfedu.constrcaclconsol.Constants.*;


public class ConstrCalcClient {


    private static Logger log = LogManager.getLogger(ConstrCalcClient.class);

    public ConstrCalcClient(){
        log.debug(START_APP);
    }

    public static DataProviderCsv getDataProviderCsv(){
        return new DataProviderCsv();
    }

    public static DataProviderXml getDataProviderXml(){
        return new DataProviderXml();
    }

    public static DataProvider getSource(List<String> command)
    {
        if (command.size() == 0) return null;
        switch (command.remove(0)){
            case FILE_EXTENSION_CSV:
                return getDataProviderCsv();
            case FILE_EXTENSION_XML:
                return getDataProviderXml();
            default:
                return null;

        }
    }


    public static String getFile(List<String> command){
        if (command.size() == 0) return null;
        String file = command.remove(0);
        switch (file){
            case MATERIALS_COMMAND:
            case CUSTOMER_COMMAND:
            case EXECUTOR_COMMAND:
            case WORKS_COMMAND:
                return file;
            default:
                return null;
        }
    }
    public static String getAction(List<String> command){
        if (command.size() == 0) return null;
        String action = command.remove(0);
        switch (action){
            case CREATE_COMMAND:
            case DELETE_COMMAND:
            case GET_BY_ID_COMMAND:
                return action;
            default:
                return null;
        }
    }
    public static Long getId(List<String> command) {
        if (command.size() == 0) return null;
        try{
            return Long.valueOf(command.remove(0));
        } catch (Exception e) {
            return null;
        }

    }
    public static void getById(DataProvider dataProvider, String file, long id) throws Exception {
        Object object;
        switch (file){
            case MATERIALS_COMMAND:
                object = dataProvider.getMaterialById(id);
                break;
            case CUSTOMER_COMMAND:
                object = dataProvider.getCustomerById(id);
                break;
            case EXECUTOR_COMMAND:
                object = dataProvider.getExecutorById(id);
                break;
            case WORKS_COMMAND:
                object = dataProvider.getWork(id);
                break;
            default:
                object = null;
        }
        if (object != null) log.info(object.toString());
    }

    public static void delete(DataProvider dataProvider, String file, long id) throws Exception {
        switch (file){
            case MATERIALS_COMMAND:
                dataProvider.deleteMaterialById(id);
                break;
            case CUSTOMER_COMMAND:
                dataProvider.deleteCustomerById(id);
                break;
            case EXECUTOR_COMMAND:
                dataProvider.deleteExecutorById(id);
                break;
            case WORKS_COMMAND:
                dataProvider.deleteWorkById(id);
                break;
        }
    }

    public static List<Materials> getListMaterials(DataProvider dataProvider, String arguments) throws Exception {
        List<Long> argumentsList= new ArrayList<>();
        String[] masArguments;
        masArguments = arguments.split(DELIMETR);
        for (String s : masArguments) {
            argumentsList.add(Long.parseLong(s));
        }
        List<Materials> materialList = new ArrayList<>();
        for(long i : argumentsList){
            materialList.add(dataProvider.getMaterialById(i).get());
        }
        return materialList;
    }

    public static Materials getMaterials(DataProvider dataProvider, Integer arguments) throws Exception {
        return dataProvider.getMaterialById(arguments).get();
    }

    public static void create(DataProvider dataProvider, String table, List<String> arguments) throws Exception {
        switch (table){
            case MATERIALS_COMMAND:
                List<Materials> materialsList = new ArrayList<>();
                Materials material = new Materials();
                material.setId(Long.parseLong(arguments.get(0)));
                material.setName(arguments.get(1));
                material.setNumber(Long.parseLong(arguments.get(2)));
                material.setPriceForOne(Long.parseLong(arguments.get(3)));
                materialsList.add(material);
                dataProvider.createMaterials(materialsList);
                break;
            case CUSTOMER_COMMAND:
                dataProvider.createCustomer(arguments.get(0),
                        arguments.get(1),
                        arguments.get(2),
                        arguments.get(3)
                       );
                break;
            case EXECUTOR_COMMAND:
                 dataProvider.createExecutor(arguments.get(0),
                        arguments.get(1),
                        arguments.get(2),
                        Long.parseLong(arguments.get(3)),
                         Long.parseLong(arguments.get(4)));
                break;
            case WORKS_COMMAND:
                List<Materials> listMaterials = new ArrayList<>();
                List<Works> worksList = new ArrayList<>();
                Works work = new Works();
                work.setId(Long.parseLong(arguments.get(0)));
                work.setName(arguments.get(1));
                work.setPrice(Long.parseLong(arguments.get(2)));
                work.setPriority(arguments.get(3));
                work.setNeedDaysToCompleted(Long.parseLong(arguments.get(4)));
                work.setStatus(arguments.get(5));

                listMaterials = getListMaterials(dataProvider,arguments.get(6));
                work.setListMaterials( listMaterials);
                worksList.add(work);
                dataProvider.createWork(worksList);

                break;
        }
    }
    public static void main(String[] args) throws Exception {
        new ConstrCalcClient();
        logBasicSystemInfo();
        log.info(LINE);

        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(Constants.PROPERTIES));
            properties.forEach((o, o2) -> System.setProperty((String) o, (String) o2));
        } catch (IOException e) {
            log.info(READ_FAIL);
            log.fatal(e);
            System.exit(1);
        }


        List<String> arguments = new ArrayList<>(Arrays.asList(args));
        DataProvider dataProvider = getSource(arguments);
        if (dataProvider == null) {
            log.error(SOURCE_FAIL + FILE_EXTENSION_CSV + COMMA + FILE_EXTENSION_XML);
        }

        String file = getFile(arguments);
        if (file == null) {
            log.error(FILE_FAIL
                    + MATERIALS_COMMAND + COMMA
                    + CUSTOMER_COMMAND + COMMA
                    + EXECUTOR_COMMAND + COMMA
                    + WORKS_COMMAND);
        }

        String action = getAction(arguments);
        if (action == null) {
            log.error(ACTION_FAIL
                    + CREATE_COMMAND + COMMA
                    + DELETE_COMMAND + COMMA
                    + GET_BY_ID_COMMAND + COMMA
                    );
            return;
        }

        if (action.equals(DELETE_COMMAND) || action.equals(GET_BY_ID_COMMAND) || action.equals(GET_ORDER_HISTORY_COMMAND) || action.equals(CALCULATE_COMMAND)){
            Long id = getId(arguments);
            if (id == null){
                log.error(BAD_ID);
                return;
            }
            switch (action) {
                case GET_BY_ID_COMMAND:
                    getById(dataProvider, file, id);
                    break;
                case DELETE_COMMAND:
                    delete(dataProvider, file, id);
                    break;
            }
        }

        if (action.equals(CREATE_COMMAND))
            create(dataProvider, file, arguments);
    }

    public static void logBasicSystemInfo() {
        log.info(LAUNCH_APP);
        log.info(OPERATING_SYS + System.getProperty(OS_NAME) + PROB + System.getProperty(OS_VERSION));
        log.info(JRE + System.getProperty(JAVA_VERSION));
        log.info(JAVA_LAUNCH + System.getProperty(JAVA_HOME));
        log.info(CLASS_PATH + System.getProperty(JAVA_CLASS));
        log.info(LIBRARY_PATH + System.getProperty(JAVA_LIBRARY));
        log.info(USER_HOME_DIRECTORY + System.getProperty(USER_HOME));
        log.info(USER_WORKING_DIRECTORY + System.getProperty(USER_DIR));
        log.info(TEST_INFO);

    }
}
