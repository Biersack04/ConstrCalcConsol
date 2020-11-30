package ru.sfedu.constrcaclconsol;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.constrcaclconsol.api.DataProviderCSV;

import ru.sfedu.constrcaclconsol.bean.Materials;
import ru.sfedu.constrcaclconsol.bean.Works;
import ru.sfedu.constrcaclconsol.utils.ConfigurationUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;

public class ConstrCalcClient {


    private static Logger log = LogManager.getLogger(ConstrCalcClient.class);
    private static List<Works> listOfWorks = new ArrayList<Works>();
    private static String work_str;
    private static String[] words;


    public  ConstrCalcClient() {

        log.debug("LogClient : starting application.........");
    }

    public static void main(String[] args) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        final String PATH = "csv_path";
        final String FILE_EXTENSION = "csv";


        Logger log = LogManager.getLogger(DataProviderCSV.class);
        String line = "";
        String cvsSplitBy = ",";
        long idparse;
        long idparse2;
        String material = "";
        int countm=-1;
        int k;
        //  String materialStplitBy="{";
        List<Works> listWorks = new ArrayList<>();
        List<Materials> listMaterials = new ArrayList<>();
        ArrayList<String> material3 = new ArrayList<>();
        String split = "Materials|,|=|id|name|\\]|number|totalPrice|\\[|\\{|\\}|\'|\"";

        FileReader fileReader = new FileReader(ConfigurationUtil.getConfigurationEntry(PATH) + Works.class.getSimpleName().toLowerCase() + ConfigurationUtil.getConfigurationEntry(FILE_EXTENSION));
        CSVReader csvReader = new CSVReader(fileReader);
        int countline=-1;

        /*CsvToBean<Works> csvToBean = new CsvToBeanBuilder<Works>(csvReader)
                .withType(Works.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        List<Works> listWorks = csvToBean.parse();*/
        try (BufferedReader br = new BufferedReader(new FileReader("D:\\ConstrCalcConsol\\src\\main\\resources\\data\\csv\\works.csv"))) {

            while ((line = br.readLine()) != null) {
                countline++;
                listMaterials = new ArrayList<>();

                String[] fields = line.split(cvsSplitBy);

                if (countline!=0) {

                        Works work = new Works();

                        work.setId(Long.parseLong(fields[0]));

                        String materialString = fields[1]+fields[2]+fields[3]+fields[4]+fields[5]+fields[6]+fields[7]+fields[8];
                        String[] material2 = materialString.split(split);
                        int count = 0;

                        material3 = new ArrayList<>();
                        for (String materialsField : material2) {
                            if (!materialsField.equals("") && !materialsField.equals(" ")) {
                                String help=materialsField.replaceAll(" ", "");
                                material3.add(help);
                                count++;
                            }
                        }

                        int count2 = 0;
                        for (k = 0; k < 2; k++) {

                        Materials materials = new Materials();

                        String value = material3.get(count2);

                        idparse2 = Long.parseLong(value);
                        materials.setId(idparse2);
                        count2++;

                        String value2 = material3.get(count2);
                        materials.setName(value2);
                        count2++;

                        String value3 = material3.get(count2);
                        Long number = Long.parseLong(value3);
                        materials.setNumber(number);
                        count2++;

                        String value4 = material3.get(count2);
                        Long totalPrice = Long.parseLong(value4);
                        materials.setTotalPrice(totalPrice);
                            count2++;
                        listMaterials.add(materials);

                    }
                            work.setListMaterials(listMaterials);

                            work.setName(fields[9]);

                            Long price = Long.parseLong(fields[10]);
                            work.setPrice(price);

                            String priority = fields[11];
                            Works.PriorityType priorityTypeWork = Works.PriorityType.valueOf(priority);
                            work.setPriority(priorityTypeWork);

                            work.setStartDate(fields[12]);

                            String status = fields[13];
                            Works.StatusOfCompletion statusWork = Works.StatusOfCompletion.valueOf(status);
                            work.setStatus(statusWork);

                            work.setStopDate(fields[14]);
                            listWorks.add(work);
                    }

                    }}
       // for(int f = 0; f<listWorks.size(); f++) {
           //  System.out.println(listWorks.get(f));}



    }


    }


/*
                        int l = 0;
                        int f = 0;
                        int count2 = 0;
                        //Materials materials = new Materials();
                        for (int k = 0; i < 2; k++) {
                            Materials materials = new Materials();
                            String value = material3.get(count2);
                            System.out.println(value);

                            idparse2 = Long.parseLong(value);
                            materials.setId(idparse2);
                            count2++;
                            String value2 = material3.get(count2);
                            materials.setName(value2);
                            count2++;
                            String value3 = material3.get(count2);
                            Long number = Long.parseLong(value3);
                            materials.setNumber(number);
                            count2++;
                            String value4 = material3.get(count2);
                            Long totalPrice = Long.parseLong(value4);
                            materials.setTotalPrice(totalPrice);

                            listMaterials.add(materials);
                        }
                        work.setListMaterials(listMaterials);

                        work.setName(fields[2]);

                        Long price = Long.parseLong(fields[3]);
                        work.setPrice(price);

                        String priority = fields[4];
                        Works.PriorityType priorityTypeWork = Works.PriorityType.valueOf(priority);
                        work.setPriority(priorityTypeWork);

                        work.setStartDate(fields[5]);

                        String status = fields[6];
                        Works.StatusOfCompletion statusWork = Works.StatusOfCompletion.valueOf(status);
                        work.setStatus(statusWork);

                        work.setStopDate(fields[7]);
                        listWorks.add(work);*/











  /*      String materials="[Materials{id=1, name='paint', number=10, totalPrice=30000}, Materials{id=2, name='brush', number=5, totalPrice=500}]";
        String split="Materials|,|=|id|name|\\]|number|totalPrice|\\[|\\{|\\}|\'";
        String split2=",";

        for (String materialsField : materials.split(split)) {
            System.out.println(materialsField);
        }
        String [] material2 = materials.split(split);
        int count =0;
        ArrayList<String> material3  = new ArrayList<>();
       // String replaceString = materials.replaceAll("[Materil{}d=]","")
        for (String materialsField : material2) {
            if (!materialsField.equals("") && !materialsField.equals(" "))
            {
                material3.add(materialsField);
                count++;
            }
        }
        for(int i = 0; i<material3.size(); i++) {
            String value = material3.get(i);
            System.out.println(value);
        }*/









       /* new ConstrCalcClient() ;
        logBasicSystemInfo();
        log.info(Constants.TEST_CONST);
        log.info(ConfigurationUtil.getConfigurationEntry(Constants.ENV_CONST));
        log.info(String.format("%.5s",Constants.FORMAT_STRING));
        log.error("Error log");*/

      /*  File file = new File(Constants.WORKS_ENVIROMENT_PATH);
        Properties properties = new Properties();
        properties.load(new FileReader(file));

        //Возвращает список всех ключей
        for (String key : properties.stringPropertyNames())
        {
            log.info(properties.get(key));
        }

        //Создание списка работ
        for (int i=1;i<=Constants.WORKS_NUMBER;i++) {

            work_str = properties.getProperty("work" + i);
            words = work_str.split("\\|");

                Work work = new Work();
                work.setId(i);
                work.setName(words[0]);
                work.setPrice(Long.parseLong(words[1]));
                listOfWorks.add(work);
            }*/

        // через стримы
        //Список работ
        //посмотреть как это сделать
       // listOfWorks.stream().forEach(e -> {});
    /*    //Проверка содержимого списка
        for(int i = 0; i< listOfWorks.size(); i++) {
            log.info(listOfWorks.get(i).getId());
            log.info(listOfWorks.get(i).getName());
            log.info(listOfWorks.get(i).getPrice());
        }*/

     /*   User user = new User();
        user.setId(1);
        user.setName("Name1");
        List<User> listUser = new ArrayList<>();
//List - абстрактное- интерфейс, arrayList - реализация
        listUser.add(user);
        DataProviderCSV providerCSV = new DataProviderCSV();
        providerCSV.insertUser(listUser);

            }*/



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
//}
