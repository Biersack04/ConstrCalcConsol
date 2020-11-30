package ru.sfedu.constrcaclconsol.api;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.junit.After;
import org.junit.AfterClass;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import ru.sfedu.constrcaclconsol.Constants;
import ru.sfedu.constrcaclconsol.TestBase;
import ru.sfedu.constrcaclconsol.bean.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class DataProviderCSVTest  extends TestBase {

    List<Materials> listMaterials;

    public DataProviderCSVTest(){

    }


    @Before
    public void setUpClass()  {

        }


    @AfterClass
    public static void tearDownClass(){

    }


    @After
    public void tearDown(){

    }


 /*   @Test
   public void insertUserSuccess() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        System.out.println("insertUserSuccess");
        List<User> listUser = new ArrayList<>();
        DataProviderCSV instance = new DataProviderCSV();
        User user1 = createUser(1,"Name1");
        User user2 = createUser(2,"Name2");
        User user3 = createUser(3,"Name3");
        listUser.add(user1);
        listUser.add(user2);
        listUser.add(user3);
        instance.insertUser(listUser);
        assertEquals(user1,instance.getUserById(1));
    }
    @Test
  public void insertUserFail() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        System.out.println("insertUserFail");
        List<User> listUser = new ArrayList<>();
        DataProviderCSV instance = new DataProviderCSV();
        User user1 = createUser(1,"Name1");
        User user2 = createUser(2,"Name2");
        User user3 = createUser(3,"Name3");
        listUser.add(user1);
        listUser.add(user2);
        listUser.add(user3);
        instance.insertUser(listUser);
        assertNull(instance.getUserById(4));
    }
*/


    @Test
    public void insertMaterialSuccess() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        System.out.println(listMaterials);
         String material_str;
        String[] words;
        System.out.println("insertMaterialSuccess");
        File file = new File(Constants.MATERIALS_ENVIROMENT_PATH);
        Properties properties = new Properties();
        properties.load(new FileReader(file));


        List<Materials> listMaterials = new ArrayList<>();

        DataProviderCSV instance = new DataProviderCSV();

        for (int i = 1; i <= Constants.MATERIALS_NUMBER; i++) {

            material_str = properties.getProperty(Constants.NAME_MATERIAL + i);
            words = material_str.split("\\|");
            Materials material;
            material = createMaterials(i,words[0],Long.parseLong(words[1]),Long.parseLong(words[2]));
            listMaterials.add(material);
        }
        instance.insertMaterials(listMaterials);

        assertEquals(listMaterials.get(0), instance.getMaterialsById(1));

    }


    @Test
    public void insertMaterialFail() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        String material_str;
        String[] words;
        System.out.println("insertMaterialFail");
        File file = new File(Constants.MATERIALS_ENVIROMENT_PATH);
        Properties properties = new Properties();
        properties.load(new FileReader(file));

        List<Materials> listMaterials = new ArrayList<>();

        DataProviderCSV instance = new DataProviderCSV();

        for (int i = 1; i <= Constants.MATERIALS_NUMBER; i++) {

            material_str = properties.getProperty(Constants.NAME_MATERIAL + i);
            words = material_str.split("\\|");
            Materials material = createMaterials(i,words[0],Long.parseLong(words[1]),Long.parseLong(words[2]));
            listMaterials.add(material);
        }

        instance.insertMaterials(listMaterials);

        assertNull(instance.getMaterialsById(Constants.MATERIALS_NUMBER +1));
    }


    @Test
    public void deleteMaterialSuccess() throws IOException {

        System.out.println("deleteMaterialSuccess");
        DataProviderCSV instance = new DataProviderCSV();
        instance.deleteMaterialsById(20);
    }


    @Test
    public void deleteMaterialFail() throws IOException {
        System.out.println("deleteMaterialFail");
        DataProviderCSV instance = new DataProviderCSV();
        instance.deleteMaterialsById(21);
    }


    @Test
    public void rewriteMaterialSuccess() throws IOException {
        System.out.println("rewriteMaterialSuccess");
        DataProviderCSV instance = new DataProviderCSV();
        long number = 10;
        long totalPrice = 100;
        instance.rewriteMaterials(1,"rewriteName",number,totalPrice);

    }


    @Test
    public void rewriteMaterialFail() throws IOException {
        System.out.println("rewriteMaterialFail");
        DataProviderCSV instance = new DataProviderCSV();
        long number = 10;
        long totalPrice = 100;
        instance.rewriteMaterials(25,"rewriteName",number,totalPrice);

    }


    @Test
    public void insertWorksSuccess() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        String work_str;
        String[] words;
        System.out.println("insertWorkSuccess");

        List<Works> listWorks = new ArrayList<>();
        String material_str;
        String[] wordsMaterials;

        File file = new File(Constants.MATERIALS_ENVIROMENT_PATH);
        Properties properties = new Properties();
        properties.load(new FileReader(file));
        int count =0;
        List<Materials> listMaterials = new ArrayList<>();


        for (int i = 1; i <= Constants.MATERIALS_NUMBER; i++) {

            material_str = properties.getProperty(Constants.NAME_MATERIAL + i);
            wordsMaterials = material_str.split("\\|");
            Materials material = createMaterials(i,wordsMaterials[0],Long.parseLong(wordsMaterials[1]),Long.parseLong(wordsMaterials[2]));
            listMaterials.add(material);
        }

        File fileWorks = new File(Constants.WORKS_ENVIROMENT_PATH);
        Properties propertiesWorks = new Properties();
        propertiesWorks.load(new FileReader(fileWorks));
        DataProviderCSV instance = new DataProviderCSV();

        for (int i = 1; i <= Constants.WORKS_NUMBER; i++) {
            List<Materials> listMaterialsForWorks = new ArrayList<>();

            work_str = propertiesWorks.getProperty(Constants.NAME_WORKS + i);
            words = work_str.split("\\|");

            listMaterialsForWorks.add(listMaterials.get(count));
            count++;
            listMaterialsForWorks.add(listMaterials.get(count));
            count++;
            
            String status=words[4];
            Works.StatusOfCompletion statusWork =  Works.StatusOfCompletion.valueOf(status);

            String priority=words[5];
            Works.PriorityType priorityTypeWork = Works.PriorityType.valueOf(priority);

            Works work = createWorks(i,words[0],Long.parseLong(words[1]),words[2],words[3],statusWork,priorityTypeWork,listMaterialsForWorks);
            listWorks.add(work);
        }
        instance.insertWork(listWorks);
        assertEquals(listWorks.get(0), instance.getWorksById(1));

    }


    @Test
    public void insertWorksFail() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        String work_str;
        String[] words;
        System.out.println("insertWorkFail");

        List<Works> listWorks = new ArrayList<>();
        String material_str;
        String[] wordsMaterials;

        File file = new File(Constants.MATERIALS_ENVIROMENT_PATH);
        Properties properties = new Properties();
        properties.load(new FileReader(file));
        int count =0;
        List<Materials> listMaterials = new ArrayList<>();


        for (int i = 1; i <= Constants.MATERIALS_NUMBER; i++) {

            material_str = properties.getProperty(Constants.NAME_MATERIAL + i);
            wordsMaterials = material_str.split("\\|");
            Materials material = createMaterials(i,wordsMaterials[0],Long.parseLong(wordsMaterials[1]),Long.parseLong(wordsMaterials[2]));
            listMaterials.add(material);
        }

        File fileWorks = new File(Constants.WORKS_ENVIROMENT_PATH);
        Properties propertiesWorks = new Properties();
        propertiesWorks.load(new FileReader(fileWorks));
        DataProviderCSV instance = new DataProviderCSV();

        for (int i = 1; i <= Constants.WORKS_NUMBER; i++) {
            List<Materials> listMaterialsForWorks = new ArrayList<>();

            work_str = propertiesWorks.getProperty(Constants.NAME_WORKS + i);
            words = work_str.split("\\|");

            listMaterialsForWorks.add(listMaterials.get(count));
            count++;
            listMaterialsForWorks.add(listMaterials.get(count));
            count++;


            String status=words[4];
            Works.StatusOfCompletion statusWork =  Works.StatusOfCompletion.valueOf(status);

            String priority=words[5];
            Works.PriorityType priorityTypeWork = Works.PriorityType.valueOf(priority);


            Works work = createWorks(i,words[0],Long.parseLong(words[1]),words[2],words[3],statusWork,priorityTypeWork,listMaterialsForWorks);
            listWorks.add(work);
        }
        instance.insertWork(listWorks);
        assertNull(instance.getWorksById(15));

    }


    @Test
    public void deleteWorksSuccess() throws  IOException {

        System.out.println("deleteWorksSuccess");
        DataProviderCSV instance = new DataProviderCSV();
        instance.deleteWorkById(10);
    }


    @Test
    public void deleteWorksFail() throws  IOException {
        System.out.println("deleteWorksFail");
        DataProviderCSV instance = new DataProviderCSV();
        instance.deleteWorkById(11);
    }


    @Test
    public void rewriteWorksSuccess() throws IOException {
        System.out.println("rewriteWorksSuccess");
        DataProviderCSV instance = new DataProviderCSV();
        long price = 10;
        String status = "CREATE";
        String priority ="HIGH";
        Works.StatusOfCompletion statusWork =  Works.StatusOfCompletion.valueOf(status);
        Works.PriorityType priorityTypeWork = Works.PriorityType.valueOf(priority);
        List<Materials> listMaterials = new ArrayList<>();
        Materials newMaterial1 = new Materials();
        long number = 10;
        long totalPrice = 100;
        long newId = Integer.parseInt(String.valueOf(1));
        newMaterial1.setId(newId);
        newMaterial1.setName("rewriteNameInWork");
        newMaterial1.setNumber(number);
        newMaterial1.setTotalPrice(totalPrice);
        listMaterials.add(newMaterial1);
        Materials newMaterial2 = createMaterials(newId+1,"rewriteNameInWork2",number+100,totalPrice+100);
        listMaterials.add(newMaterial2);
        instance.rewriteWork((long) 1,"rewriteName",price,priorityTypeWork,"00.00.0000",statusWork,"11.11.1111",listMaterials);

    }


    @Test
    public void rewriteWorksFail() throws IOException {
        System.out.println("rewriteWorksSuccess");
        DataProviderCSV instance = new DataProviderCSV();
        long price = 10;
        String status = "CREATE";
        String priority ="HIGH";
        Works.StatusOfCompletion statusWork =  Works.StatusOfCompletion.valueOf(status);
        Works.PriorityType priorityTypeWork = Works.PriorityType.valueOf(priority);
        List<Materials> listMaterials = new ArrayList<>();
        Materials newMaterial1 = new Materials();
        long number = 10;
        long totalPrice = 100;
        long newId = Integer.parseInt(String.valueOf(1));
        newMaterial1.setId(newId);
        newMaterial1.setName("rewriteNameInWork");
        newMaterial1.setNumber(number);
        newMaterial1.setTotalPrice(totalPrice);
        listMaterials.add(newMaterial1);
        Materials newMaterial2 = createMaterials(newId+1,"rewriteNameInWork2",number+100,totalPrice+100);
        listMaterials.add(newMaterial2);
        instance.rewriteWork((long) 10,"rewriteName",price,priorityTypeWork,"00.00.0000",statusWork,"11.11.1111",listMaterials);


    }


    @Test
    public void insertPeopleSuccess() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        System.out.println("insertPeopleSuccess");
        List<People> listPeople = new ArrayList<>();
        DataProviderCSV instance = new DataProviderCSV();
        People people1 = createPeople(1,"Name1","Surname1","google@google.com");
        People people2 = createPeople(2,"Name2","Surname2","yandex@yandex.ru");
        People people3 = createPeople(3,"Name3","Surname3","rambler@rambler.com");
        listPeople.add(people1);
        listPeople.add(people2);
        listPeople.add(people3);
        instance.insertPeople(listPeople);
        assertEquals(people1,instance.getPeopleById(1));
    }


    @Test
    public void insertPeopleFail() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        System.out.println("insertPeopleFail");
        List<People> listPeople = new ArrayList<>();
        DataProviderCSV instance = new DataProviderCSV();
        People people1 = createPeople(1,"Name1","Surname1","google@google.com");
        People people2 = createPeople(2,"Name2","Surname2","yandex@yandex.ru");
        People people3 = createPeople(3,"Name3","Surname2","rambler@rambler.com");
        listPeople.add(people1);
        listPeople.add(people2);
        listPeople.add(people3);
        instance.insertPeople(listPeople);
        assertNull(instance.getPeopleById(4));
    }


    @Test
    public void deletePeopleSuccess() throws  IOException {

        System.out.println("deletePeopleSuccess");
        DataProviderCSV instance = new DataProviderCSV();
        instance.deletePeopleById(3);
    }


    @Test
    public void deletePeopleFail() throws  IOException {
        System.out.println("deletePeopleFail");
        DataProviderCSV instance = new DataProviderCSV();
        instance.deletePeopleById(10);
    }


    @Test
    public void rewritePeopleSuccess() throws IOException {
        System.out.println("rewritePeopleSuccess");
        DataProviderCSV instance = new DataProviderCSV();

        instance.rewritePeople(1,"rewriteName","reWriteSurname","reWriteMailbox");

    }


    @Test
    public void rewritePeopleFail() throws IOException {
        System.out.println("rewritePeopleFail");
        DataProviderCSV instance = new DataProviderCSV();

        instance.rewritePeople(4,"rewriteName","reWriteSurname","reWriteMailbox");



    }


    @Test
    public void insertCustomerSuccess() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        System.out.println("insertCustomerSuccess");
        List<Customer> listCustomer = new ArrayList<>();
        DataProviderCSV instance = new DataProviderCSV();
        Customer customer1 = createCustomer((long) 1,"name1","surname1","google@google.com","address1","telephone1");
        Customer customer2 = createCustomer((long) 2,"name2","surname2","yandex@yandex.ru","address2","telephone2");
        Customer customer3 = createCustomer((long) 3,"name3","surname3","rambler@rambler.com","address3","telephone3");
        listCustomer.add(customer1);
        listCustomer.add(customer2);
        listCustomer.add(customer3);
        instance.insertCustomer(listCustomer);
        assertEquals(customer1,instance.getCustomerById(1));
    }


    @Test
    public void insertCustomerFail() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        System.out.println("insertCustomerFail");
        List<People> listPeople = new ArrayList<>();
        DataProviderCSV instance = new DataProviderCSV();
        People people1 = createPeople(1,"Name1","Surname1","google@google.com");
        People people2 = createPeople(2,"Name2","Surname2","yandex@yandex.ru");
        People people3 = createPeople(3,"Name3","Surname2","rambler@rambler.com");
        listPeople.add(people1);
        listPeople.add(people2);
        listPeople.add(people3);
        instance.insertPeople(listPeople);
        assertNull(instance.getCustomerById(4));
    }


    @Test
    public void deleteCustomerSuccess() throws  IOException {

        System.out.println("deleteCustomerSuccess");
        DataProviderCSV instance = new DataProviderCSV();
        instance.deleteCustomerById(3);
    }


    @Test
    public void deleteCustomerFail() throws  IOException {
        System.out.println("deleteCustomerFail");
        DataProviderCSV instance = new DataProviderCSV();
        instance.deleteCustomerById(10);
    }


    @Test
    public void rewriteCustomerSuccess() throws IOException {
        System.out.println("rewriteCustomerSuccess");
        DataProviderCSV instance = new DataProviderCSV();

       // instance.rewriteCustomer(1,"rewriteName",number,totalPrice);

    }


    @Test
    public void rewriteCustomerFail() throws IOException {
        System.out.println("rewriteCustomerFail");
        DataProviderCSV instance = new DataProviderCSV();

       // instance.rewriteCustomer(25,"rewriteName",number,totalPrice);

    }


    @Test
    public void insertExecutorSuccess() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        System.out.println("insertExecutorSuccess");
        List<Executor> listExecutor = new ArrayList<>();
        DataProviderCSV instance = new DataProviderCSV();
        Executor executor1 = createExecutor((long) 4,"name4","surname4","google@google.com",4,40);
        Executor executor2 = createExecutor((long) 5,"name5","surname5","yandex@yandex.ru",5,50);
        Executor executor3 = createExecutor((long) 6,"name6","surname6","rambler@rambler.com",6,60);
        listExecutor.add(executor1);
        listExecutor.add(executor2);
        listExecutor.add(executor3);
        instance.insertExecutor(listExecutor);
        assertEquals(executor1,instance.getExecutorById(4));
    }


    @Test
    public void insertExecutorFail() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        System.out.println("insertExecutorFail");
        List<Executor> listExecutor = new ArrayList<>();
        DataProviderCSV instance = new DataProviderCSV();
        Executor executor1 = createExecutor((long) 4,"name4","surname4","google@google.com",4,40);
        Executor executor2 = createExecutor((long) 5,"name5","surname5","yandex@yandex.ru",5,50);
        Executor executor3 = createExecutor((long) 6,"name6","surname6","rambler@rambler.com",6,60);
        listExecutor.add(executor1);
        listExecutor.add(executor2);
        listExecutor.add(executor3);
        instance.insertExecutor(listExecutor);
        assertNull(instance.getCustomerById(7));
    }


    @Test
    public void deleteExecutorSuccess() throws  IOException {

        System.out.println("deleteExecutorSuccess");
        DataProviderCSV instance = new DataProviderCSV();
        instance.deleteExecutorById(6);
    }


    @Test
    public void deleteExecutorFail() throws  IOException {
        System.out.println("deleteExecutorFail");
        DataProviderCSV instance = new DataProviderCSV();
        instance.deleteExecutorById(10);
    }

    @Test
    public void rewriteExecutorSuccess() throws IOException {
        System.out.println("rewriteExecutorSuccess");
        DataProviderCSV instance = new DataProviderCSV();

       // instance.rewriteExecutor(1,"rewriteName",number,totalPrice);

    }


    @Test
    public void rewriteExecutorFail() throws IOException {
        System.out.println("rewriteExecutorFail");
        DataProviderCSV instance = new DataProviderCSV();

       // instance.rewriteExecutor(25,"rewriteName",number,totalPrice);

    }


}