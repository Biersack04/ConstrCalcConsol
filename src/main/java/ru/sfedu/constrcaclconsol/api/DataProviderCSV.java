package ru.sfedu.constrcaclconsol.api;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.constrcaclconsol.bean.*;
import ru.sfedu.constrcaclconsol.utils.ConfigurationUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class DataProviderCSV {

    private final String PATH = "csv_path";
    private final String FILE_EXTENSION = "csv";
    private static Logger log = LogManager.getLogger(DataProviderCSV.class);

    public void insertMaterials(List<Materials> listMaterials) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

        try {
            FileWriter writer = new FileWriter(ConfigurationUtil.getConfigurationEntry(PATH) + listMaterials.get(0).getClass().getSimpleName().toLowerCase() + ConfigurationUtil.getConfigurationEntry(FILE_EXTENSION));
            CSVWriter csvWriter = new CSVWriter(writer);
            StatefulBeanToCsv<Materials> beanToCsv = new StatefulBeanToCsvBuilder<Materials>(csvWriter)
                    .withApplyQuotesToAll(false)
                    .build();
            beanToCsv.write(listMaterials);
            csvWriter.close();

        } catch (IndexOutOfBoundsException e) {

            log.error(e);

        }
    }

    public Materials getMaterialsById(long id) throws IOException {
        List<Materials> listMaterials = select(Materials.class);
        try {
            Materials materials = listMaterials.stream()
                    .filter(el -> el.getId() == id)
                    .findFirst().get();
            return materials;
        } catch (NoSuchElementException e) {
            log.error(e);
            return null;
        }

    }

    public void deleteMaterialsById(long id) throws IOException {
        List<Materials> listMaterials = select(Materials.class);
        try {
            Materials materials = listMaterials.stream()
                    .filter(el -> el.getId() == id)
                    .findFirst().get();

            listMaterials.remove(materials);
            insertMaterials(listMaterials);
        } catch (NoSuchElementException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            log.error(e);

        }

    }

    public void rewriteMaterials(long id, String name,Long number, Long totalPrice ) throws IOException {
        List<Materials> listMaterials = select(Materials.class);
        try {
            /*Materials materials = listMaterials.stream()
                    .filter(el -> el.getId() == id)
                    .findFirst().get();
*/
            Materials newMaterials = new Materials();

            newMaterials.setId(id);
            newMaterials.setName(name);
            newMaterials.setNumber(number);
            newMaterials.setTotalPrice(totalPrice);

            int newId = Integer.parseInt(String.valueOf(id))-1;

            listMaterials.set(newId,newMaterials);

            insertMaterials(listMaterials);
        } catch (NoSuchElementException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            log.error(e);

        }

    }

    public void insertWork(List<Works> listWorks) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

        try {
            FileWriter writer = new FileWriter(ConfigurationUtil.getConfigurationEntry(PATH) + listWorks.get(0).getClass().getSimpleName().toLowerCase() + ConfigurationUtil.getConfigurationEntry(FILE_EXTENSION));
            CSVWriter csvWriter = new CSVWriter(writer);
            StatefulBeanToCsv<Works> beanToCsv = new StatefulBeanToCsvBuilder<Works>(csvWriter)
                    .withApplyQuotesToAll(false)
                    .build();
            beanToCsv.write(listWorks);
            csvWriter.close();

        } catch (IndexOutOfBoundsException e) {

            log.error(e);

        }
    }

    public Works getWorksById(long id) throws IOException {
        String line = "";
        String cvsSplitBy = ",";
        long idparse2;
        int k;
        List<Works> listWorks = new ArrayList<>();
        List<Materials> listMaterials;
        ArrayList<String> material3;
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

            try {
                Works work = listWorks.stream()
                        .filter(el -> el.getId() == id)
                        .findFirst().get();
                return work;
            } catch (
                    NoSuchElementException e) {
                log.error(e);
                return null;
            }

        }

    public void deleteWorkById(long id) throws IOException {

        String line = "";
        String cvsSplitBy = ",";
        long idparse2;
        int k;
        List<Works> listWorks = new ArrayList<>();
        List<Materials> listMaterials;
        ArrayList<String> material3;
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

        try {
            Works work = listWorks.stream()
                    .filter(el -> el.getId() == id)
                    .findFirst().get();

            listWorks.remove(work);
            insertWork(listWorks);
        } catch (NoSuchElementException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            log.error(e);

        }

    }

    public void rewriteWork(Long id, String name, Long price2, Works.PriorityType priority2,
                            String startDate, Works.StatusOfCompletion status2, String stopDate, List<Materials> listMaterials2 ) throws IOException {
        String line = "";
        String cvsSplitBy = ",";
        long idparse2;
        int k;
        List<Works> listWorks = new ArrayList<>();
        List<Materials> listMaterials;
        ArrayList<String> material3;
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

        try {

            Works newWorks = new Works();

            newWorks.setId(id);
            newWorks.setName(name);
            newWorks.setPrice(price2);
            newWorks.setPriority(priority2);
            newWorks.setStartDate(startDate);
            newWorks.setStatus(status2);
            newWorks.setStopDate(stopDate);
            newWorks.setListMaterials(listMaterials2);

            int newId = Integer.parseInt(String.valueOf(id))-1;

            listWorks.set(newId,newWorks);
            insertWork(listWorks);

        } catch (NoSuchElementException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException | IndexOutOfBoundsException e) {
            log.error(e);

        }

    }


    public void insertPeople(List<People> listPeople) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

        try {
            FileWriter writer = new FileWriter(ConfigurationUtil.getConfigurationEntry(PATH) + listPeople.get(0).getClass().getSimpleName().toLowerCase() + ConfigurationUtil.getConfigurationEntry(FILE_EXTENSION));
            CSVWriter csvWriter = new CSVWriter(writer);
            StatefulBeanToCsv<People> beanToCsv = new StatefulBeanToCsvBuilder<People>(csvWriter)
                    .withApplyQuotesToAll(false)
                    .build();
            beanToCsv.write(listPeople);
            csvWriter.close();

        } catch (IndexOutOfBoundsException e) {

            log.error(e);

        }
    }

    public People getPeopleById(long id) throws IOException {
        List<People> listPeople = select(People.class);
        try {
            People people = listPeople.stream()
                    .filter(el -> el.getId() == id)
                    .findFirst().get();
            return people;
        } catch (NoSuchElementException e) {
            log.error(e);
            return null;
        }

    }

    public void deletePeopleById(long id) throws IOException {
        List<People> listPeople = select(People.class);
        try {
            People people = listPeople.stream()
                    .filter(el -> el.getId() == id)
                    .findFirst().get();

            listPeople.remove(people);
            insertPeople(listPeople);
        } catch (NoSuchElementException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            log.error(e);

        }

    }

    public void rewritePeople(long id, String name, String surname, String mailbox ) throws IOException {
        List<People> listPeople = select(People.class);
        try {

            People newPeople = new People();

            newPeople.setId(id);
            newPeople.setName(name);
            newPeople.setSurname(surname);
            newPeople.setMailbox(mailbox);

            int newId = Integer.parseInt(String.valueOf(id))-1;

            listPeople.set(newId,newPeople);

            insertPeople(listPeople);
        } catch (NoSuchElementException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException  | IndexOutOfBoundsException e) {
            log.error(e);

        }

    }

    public void insertCustomer(List<Customer> listCustomer) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

        try {
            FileWriter writer = new FileWriter(ConfigurationUtil.getConfigurationEntry(PATH) + listCustomer.get(0).getClass().getSimpleName().toLowerCase() + ConfigurationUtil.getConfigurationEntry(FILE_EXTENSION));
            CSVWriter csvWriter = new CSVWriter(writer);
            StatefulBeanToCsv<Customer> beanToCsv = new StatefulBeanToCsvBuilder<Customer>(csvWriter)
                    .withApplyQuotesToAll(false)
                    .build();
            beanToCsv.write(listCustomer);
            csvWriter.close();

        } catch (IndexOutOfBoundsException e) {

            log.error(e);

        }
    }

    public Customer getCustomerById(long id) throws IOException {
        List<Customer> listCustomer = select(Customer.class);
        try {
            Customer customer = listCustomer.stream()
                    .filter(el -> el.getId() == id)
                    .findFirst().get();
            return customer;
        } catch (NoSuchElementException e) {
            log.error(e);
            return null;
        }

    }

    public void deleteCustomerById(long id) throws IOException {
        List<Customer> listCustomer = select(Customer.class);
        try {
            Customer customer = listCustomer.stream()
                    .filter(el -> el.getId() == id)
                    .findFirst().get();

            listCustomer.remove(customer);
            insertCustomer(listCustomer);
        } catch (NoSuchElementException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            log.error(e);

        }

    }

    public void insertExecutor(List<Executor> listExecutor) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

        try {
            FileWriter writer = new FileWriter(ConfigurationUtil.getConfigurationEntry(PATH) + listExecutor.get(0).getClass().getSimpleName().toLowerCase() + ConfigurationUtil.getConfigurationEntry(FILE_EXTENSION));
            CSVWriter csvWriter = new CSVWriter(writer);
            StatefulBeanToCsv<Executor> beanToCsv = new StatefulBeanToCsvBuilder<Executor>(csvWriter)
                    .withApplyQuotesToAll(false)
                    .build();
            beanToCsv.write(listExecutor);
            csvWriter.close();

        } catch (IndexOutOfBoundsException e) {

            log.error(e);

        }
    }

    public Executor getExecutorById(long id) throws IOException {
        List<Executor> listExecutor = select(Executor.class);
        try {
            Executor executor = listExecutor.stream()
                    .filter(el -> el.getId() == id)
                    .findFirst().get();
            return executor;
        } catch (NoSuchElementException e) {
            log.error(e);
            return null;
        }

    }

    public void deleteExecutorById(long id) throws IOException {
        List<Executor> listExecutor = select(Executor.class);
        try {
            Executor executor = listExecutor.stream()
                    .filter(el -> el.getId() == id)
                    .findFirst().get();

            listExecutor.remove(executor);
            insertExecutor(listExecutor);
        } catch (NoSuchElementException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            log.error(e);

        }

    }



    public <T> List<T> select (Class cl) throws IOException {
        FileReader fileReader = new FileReader(ConfigurationUtil.getConfigurationEntry(PATH) + cl.getSimpleName().toLowerCase() + ConfigurationUtil.getConfigurationEntry(FILE_EXTENSION));
        CSVReader csvReader = new CSVReader(fileReader);
        CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(csvReader)
                .withType(cl)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        List<T> list = csvToBean.parse();
        return list;




    }


}
