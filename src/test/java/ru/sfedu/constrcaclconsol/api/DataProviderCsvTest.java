package ru.sfedu.constrcaclconsol.api;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;

import org.junit.Before;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import ru.sfedu.constrcaclconsol.Constants;
import ru.sfedu.constrcaclconsol.TestBase;
import ru.sfedu.constrcaclconsol.bean.*;
import ru.sfedu.constrcaclconsol.enums.PriorityType;
import ru.sfedu.constrcaclconsol.enums.StatusOfCompletion;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DataProviderCsvTest extends TestBase {


    private static final Logger log = LogManager.getLogger(DataProviderCsvTest.class);


    public DataProviderCsvTest(){

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

    private static List<Materials> getCorrectMaterialTestData() throws IOException {
        List<Materials> listMaterials = new ArrayList<>();
        String material_str;
        String[] words;
        File file = new File(Constants.MATERIALS_ENVIROMENT_PATH);
        Properties properties = new Properties();
        properties.load(new FileReader(file));

        for (int i = 1; i <= Constants.MATERIALS_NUMBER; i++) {

            material_str = properties.getProperty(Constants.NAME_MATERIAL + i);
            words = material_str.split("\\|");
            Materials material;
            material = createMaterials(i,words[0],Long.parseLong(words[1]),Long.parseLong(words[2]));
            listMaterials.add(material);
        }
        return listMaterials;
    }

    private static List<Works> getCorrectWorksTestData() throws IOException {
        String work_str;
        String[] words;

        List<Works> listWorks = new ArrayList<>();

        File file = new File(Constants.MATERIALS_ENVIROMENT_PATH);
        Properties properties = new Properties();
        properties.load(new FileReader(file));
        int count =0;

        List<Materials> listMaterials = new ArrayList<>();
        listMaterials.addAll(getCorrectMaterialTestData());

        File fileWorks = new File(Constants.WORKS_ENVIROMENT_PATH);
        Properties propertiesWorks = new Properties();
        propertiesWorks.load(new FileReader(fileWorks));
        DataProviderCsv instance = new DataProviderCsv();

        for (int i = 1; i <= Constants.WORKS_NUMBER; i++) {
            List<Materials> listMaterialsForWorks = new ArrayList<>();

            work_str = propertiesWorks.getProperty(Constants.NAME_WORKS + i);
            words = work_str.split("\\|");

            listMaterialsForWorks.add(listMaterials.get(count));
            count++;
            listMaterialsForWorks.add(listMaterials.get(count));
            count++;

            String status=words[3];

            String priority=words[4];

            Works work = createWorks(i,words[0],Long.parseLong(words[1]),Long.parseLong(words[2]),status,priority,listMaterialsForWorks);

            listWorks.add(work);
        }
        return listWorks;

    }


//CRUD Materials
    @Test
    public void insertMaterialSuccess() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        System.out.println("insertMaterialSuccess");

        List<Materials> listMaterials = new ArrayList<>();
        listMaterials.addAll(getCorrectMaterialTestData());

        DataProviderCsv instance = new DataProviderCsv();
        instance.insertMaterials(listMaterials);

        assertEquals(listMaterials.get(0), instance.getMaterialsById(1));

    }

    @Test
    public void insertMaterialFail() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        System.out.println("insertMaterialFail");

        List<Materials> listMaterials = new ArrayList<>();
        listMaterials.addAll(getCorrectMaterialTestData());

        DataProviderCsv instance = new DataProviderCsv();
        instance.insertMaterials(listMaterials);

        assertNull(instance.getMaterialsById(Constants.MATERIALS_NUMBER +1));
    }

    @Test
    public void getMaterialByIdSuccess() throws IOException {
        System.out.println("getMaterialByIdSuccess");

        DataProviderCsv instance = new DataProviderCsv();

        System.out.println(instance.getMaterialsById(1));
    }

    @Test
    public void getMeterialByIdFail() throws IOException {
        System.out.println("getMaterialByIdFail");

        DataProviderCsv instance = new DataProviderCsv();

        assertNull(instance.getMaterialsById(21));
    }

    @Test
    public void deleteMaterialSuccess() throws IOException {

        System.out.println("deleteMaterialSuccess");

        long id = 20;

        DataProviderCsv instance = new DataProviderCsv();

        instance.deleteMaterialsById(id);

        assertNull(instance.getMaterialsById(id));
    }

    @Test
    public void deleteMaterialFail() throws IOException {
        System.out.println("deleteMaterialFail");

        long newId = 21;

        DataProviderCsv instance = new DataProviderCsv();

        instance.deleteMaterialsById(newId);
    }

    @Test
    public void updateMaterialSuccess() throws IOException {
        System.out.println("updateMaterialSuccess");

        DataProviderCsv instance = new DataProviderCsv();

        long idRewriteMaterial = 1;
        String newName = "RewriteName";
        long newNumber = 10;
        long newTotalPrice = 100;

        instance.rewriteMaterials(idRewriteMaterial,newName,newNumber,newTotalPrice);

        System.out.println(instance.getMaterialsById(idRewriteMaterial));
    }

    @Test
    public void updateMaterialFail() throws IOException {
        System.out.println("updateMaterialFail");

        DataProviderCsv instance = new DataProviderCsv();

        long idRewriteMaterial = Constants.MATERIALS_NUMBER+1;
        String newName = "RewriteName";
        long newNumber = 10;
        long newTotalPrice = 100;

        instance.rewriteMaterials(idRewriteMaterial,newName,newNumber,newTotalPrice);
    }



//CRUD Works
    @Test
    public void insertWorksSuccess() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        System.out.println("insertWorkSuccess");

        List<Works> listWorks = new ArrayList<>();
        listWorks.addAll(getCorrectWorksTestData());

        DataProviderCsv instance = new DataProviderCsv();

        instance.insertWork(listWorks);

        assertEquals(listWorks.get(1), instance.getWorks(2).get());

    }

    @Test
    public void insertWorksFail() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {

        System.out.println("insertWorkFail");

        List<Works> listWorks = new ArrayList<>();
        listWorks.addAll(getCorrectWorksTestData());

        DataProviderCsv instance = new DataProviderCsv();

        instance.insertWork(listWorks);

        assertEquals(Optional.empty(), instance.getWorks(15));


    }

    @Test
    public void getWorksByIdSuccess() throws  IOException {

        System.out.println("getWorksByIdSuccess");

        DataProviderCsv instance = new DataProviderCsv();

        System.out.println(instance.getWorks(3).get());
    }

    @Test
    public void getWorksByIdFail() throws  IOException {
        System.out.println("getWorksByIdFail");

        DataProviderCsv instance = new DataProviderCsv();

        assertEquals(Optional.empty(), instance.getWorks(15));

    }

    @Test
    public void updateWorksSuccess() throws IOException {
        System.out.println("rewriteWorksSuccess");

        DataProviderCsv instance = new DataProviderCsv();

        long price = 10;
        String status = "CREATE";
        String priority ="HIGH";

        List<Materials> listMaterials = new ArrayList<>();
        Materials newMaterial1 = new Materials();
        long number = 10;
        long totalPrice = 100;
        long newId = Integer.parseInt(String.valueOf(2));
        newMaterial1.setId(newId);
        newMaterial1.setName("rewriteNameInWork");
        newMaterial1.setNumber(number);
        newMaterial1.setTotalPrice(totalPrice);
        newMaterial1 = createMaterials(newId,"rewriteNameInWork",number,totalPrice);
        listMaterials.add(newMaterial1);
        Materials newMaterial2 = createMaterials(newId+1,"rewriteNameInWork2",number+100,totalPrice+100);
        listMaterials.add(newMaterial2);

        instance.rewriteWork(newId,"rewriteName",price,priority,(long)100,status,listMaterials);

        System.out.println(instance.getWorks(newId).get());
    }

    @Test
    public void updateWorksFail() throws IOException {
        System.out.println("rewriteWorksSuccess");

        DataProviderCsv instance = new DataProviderCsv();

        long price = 10;
        String status = "CREATE";
        String priority ="HIGH";

        List<Materials> listMaterials = new ArrayList<>();
        Materials newMaterial1 = new Materials();
        long number = 10;
        long totalPrice = 100;
        long newId = Integer.parseInt(String.valueOf(1));
        newMaterial1.setId(newId);
        newMaterial1.setName("rewriteNameInWork");
        newMaterial1.setNumber(number);
        newMaterial1.setTotalPrice(totalPrice);
        newMaterial1 = createMaterials(newId,"rewriteNameInWork",number,totalPrice);
        listMaterials.add(newMaterial1);
        Materials newMaterial2 = createMaterials(newId+1,"rewriteNameInWork2",number+100,totalPrice+100);
        listMaterials.add(newMaterial2);

        instance.rewriteWork((long) 10,"rewriteName",price,priority,(long)10,status,listMaterials);

    }

    @Test
    public void deleteWorksSuccess() throws  IOException {

        System.out.println("deleteWorksSuccess");

        DataProviderCsv instance = new DataProviderCsv();

        instance.deleteWorkById(10);

        assertEquals(Optional.empty(), instance.getWorks(10));
    }

    @Test
    public void deleteWorksFail() throws  IOException {
        System.out.println("deleteWorksFail");

        DataProviderCsv instance = new DataProviderCsv();

        instance.deleteWorkById(11);
    }


/*
    @Test
    public void insertPeopleSuccess() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        System.out.println("insertPeopleSuccess");
        List<People> listPeople = new ArrayList<>();
        DataProviderCsv instance = new DataProviderCsv();
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
        DataProviderCsv instance = new DataProviderCsv();
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
        DataProviderCsv instance = new DataProviderCsv();
        instance.deletePeopleById(3);
    }


    @Test
    public void deletePeopleFail() throws  IOException {
        System.out.println("deletePeopleFail");
        DataProviderCsv instance = new DataProviderCsv();
        instance.deletePeopleById(10);
    }


    @Test
    public void rewritePeopleSuccess() throws IOException {
        System.out.println("rewritePeopleSuccess");
        DataProviderCsv instance = new DataProviderCsv();

        instance.rewritePeople(1,"rewriteName","reWriteSurname","reWriteMailbox");

    }


    @Test
    public void rewritePeopleFail() throws IOException {
        System.out.println("rewritePeopleFail");
        DataProviderCsv instance = new DataProviderCsv();

        instance.rewritePeople(4,"rewriteName","reWriteSurname","reWriteMailbox");



    }
*/


    //CRUD Customer
    @Test
    public void insertCustomerSuccess() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        System.out.println("insertCustomerSuccess");

        List<Customer> listCustomer = new ArrayList<>();

        DataProviderCsv instance = new DataProviderCsv();

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

        List<Customer> listCustomer = new ArrayList<>();

        DataProviderCsv instance = new DataProviderCsv();

        Customer customer1 = createCustomer((long) 1,"name1","surname1","google@google.com","address1","telephone1");
        Customer customer2 = createCustomer((long) 2,"name2","surname2","yandex@yandex.ru","address2","telephone2");
        Customer customer3 = createCustomer((long) 3,"name3","surname3","rambler@rambler.com","address3","telephone3");
        listCustomer.add(customer1);
        listCustomer.add(customer2);
        listCustomer.add(customer3);

        instance.insertCustomer(listCustomer);

        assertNull(instance.getCustomerById(4));
    }

    @Test
    public void getByIdCustomerSuccess() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        System.out.println("getByIdCustomerSuccess");

        DataProviderCsv instance = new DataProviderCsv();

        System.out.println(instance.getCustomerById(1));

    }


    @Test
    public void getByIdCustomerFail() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        System.out.println("getByIdCustomerFail");

        DataProviderCsv instance = new DataProviderCsv();

        assertNull(instance.getCustomerById(8));
    }


    @Test
    public void deleteCustomerSuccess() throws  IOException {

        System.out.println("deleteCustomerSuccess");

        DataProviderCsv instance = new DataProviderCsv();

        instance.deleteCustomerById(3);

        assertNull(instance.getCustomerById(3));
    }


    @Test
    public void deleteCustomerFail() throws  IOException {
        System.out.println("deleteCustomerFail");

        DataProviderCsv instance = new DataProviderCsv();

        instance.deleteCustomerById(10);
    }


    @Test
    public void updateCustomerSuccess() throws IOException {
        System.out.println("rewriteCustomerSuccess");

        DataProviderCsv instance = new DataProviderCsv();

        long idRewriteCustomer = 2;
        String newName = "newName";
        String newSurname = "newSurname";
        String newMailbox = "newMailbox";
        String orderAddress = "newOrderAddress";
        String telephone = "newTelephone";

        instance.rewriteCustomer(idRewriteCustomer,newName,newSurname,newMailbox,orderAddress,telephone);

        System.out.println(instance.getCustomerById(idRewriteCustomer));

    }


    @Test
    public void updateCustomerFail() throws IOException {
        System.out.println("rewriteCustomerFail");

        DataProviderCsv instance = new DataProviderCsv();

        long idRewriteCustomer = 20;
        String newName = "newName2";
        String newSurname = "newSurname2";
        String newMailbox = "newMailbox2";
        String orderAddress = "newOrderAddress2";
        String telephone = "newTelephone2";

        instance.rewriteCustomer(idRewriteCustomer,newName,newSurname,newMailbox,orderAddress,telephone);
    }



    //CRUD Executor
    @Test
    public void insertExecutorSuccess() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        System.out.println("insertExecutorSuccess");

        List<Executor> listExecutor = new ArrayList<>();

        DataProviderCsv instance = new DataProviderCsv();

        Executor executor1 = createExecutor((long) 4,"name4","surname4","google@google.com",(long)4,(long)40);
        Executor executor2 = createExecutor((long) 5,"name5","surname5","yandex@yandex.ru",(long)5,(long)50);
        Executor executor3 = createExecutor((long) 6,"name6","surname6","rambler@rambler.com",(long)6,(long)60);
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

        DataProviderCsv instance = new DataProviderCsv();

        Executor executor1 = createExecutor((long) 4,"name4","surname4","google@google.com",(long)4,(long)40);
        Executor executor2 = createExecutor((long) 5,"name5","surname5","yandex@yandex.ru",(long)5,(long)50);
        Executor executor3 = createExecutor((long) 6,"name6","surname6","rambler@rambler.com",(long)6,(long)60);
        listExecutor.add(executor1);
        listExecutor.add(executor2);
        listExecutor.add(executor3);

        instance.insertExecutor(listExecutor);

        assertNull(instance.getExecutorById(7));
    }

    @Test
    public void getByIdExecutorSuccess() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        System.out.println("getByIdExecutorSuccess");

        DataProviderCsv instance = new DataProviderCsv();

        System.out.println(instance.getExecutorById(4));

    }


    @Test
    public void getByIdExecutorFail() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        System.out.println("getByIdExecutorFail");

        DataProviderCsv instance = new DataProviderCsv();

        assertNull(instance.getExecutorById(10));
    }


    @Test
    public void deleteExecutorSuccess() throws  IOException {

        System.out.println("deleteExecutorSuccess");

        DataProviderCsv instance = new DataProviderCsv();

        instance.deleteExecutorById(6);
    }


    @Test
    public void deleteExecutorFail() throws  IOException {
        System.out.println("deleteExecutorFail");

        DataProviderCsv instance = new DataProviderCsv();

        instance.deleteExecutorById(10);
    }

    @Test
    public void updateExecutorSuccess() throws IOException {
        System.out.println("rewriteExecutorSuccess");

        DataProviderCsv instance = new DataProviderCsv();

        long idRewriteExecutor = 5;
        String newName = "newName";
        String newSurname = "newSurname";
        String newMailbox = "newMailbox";
        long newNumberOfCompletedWork = 10;
        long newNumberOfWorkers = 100;

        instance.rewriteExecutor(idRewriteExecutor,newName,newSurname,newMailbox,newNumberOfCompletedWork,newNumberOfWorkers);

        System.out.println(instance.getExecutorById(idRewriteExecutor));

    }


    @Test
    public void updateExecutorFail() throws IOException {
        System.out.println("rewriteExecutorFail");

        DataProviderCsv instance = new DataProviderCsv();

        long idRewriteExecutor = 10;
        String newName = "newName";
        String newSurname = "newSurname";
        String newMailbox = "newMailbox";
        long newNumberOfCompletedWork = 10;
        long newNumberOfWorkers = 100;

        instance.rewriteExecutor(idRewriteExecutor,newName,newSurname,newMailbox,newNumberOfCompletedWork,newNumberOfWorkers);


    }



//CRUD Project

    @Test
    public void insertProjectSuccess() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        System.out.println("insertProjectSuccess");

        List<Project> listProject = new ArrayList<>();

        List<Works> listWorks = new ArrayList<>();
        listWorks.addAll(getCorrectWorksTestData());


        People customer1 = createCustomer((long) 1,"name1","surname1","google@google.com","address1","telephone1");
        People executor1 = createExecutor((long) 4,"name4","surname4","google@google.com",(long)4,(long)40);

        People customer2 = createCustomer((long) 2,"name2","surname2","google@google.com","address2","telephone2");
        People executor2 = createExecutor((long) 5,"name5","surname5","google@google.com",(long)4,(long)40);

        Project project1 = createProject(1,"Проект 3х этажного дома","12.11.20","12.12.21",10,listWorks,"Ставропольский 14",executor1,customer1);
        Project project2 =  createProject(2,"Проект 2х этажного дома","12.11.19","12.12.22",10,listWorks,"Ставропольский 14",executor2,customer2);

        listProject.add(project1);
        listProject.add(project2);

        DataProviderCsv instance = new DataProviderCsv();

        instance.insertProject(listProject);
        instance.getProject(1).get();
        assertEquals(listProject.get(0), instance.getProject(1).get());


    }


    @Test
    public void insertProjectFail() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {

        List<Project> listProject = new ArrayList<>();

        List<Works> listWorks = new ArrayList<>();
        listWorks.addAll(getCorrectWorksTestData());


        People customer1 = createCustomer((long) 1,"name1","surname1","google@google.com","address1","telephone1");
        People executor1 = createExecutor((long) 4,"name4","surname4","google@google.com",(long)4,(long)40);

        People customer2 = createCustomer((long) 2,"name2","surname2","google@google.com","address2","telephone2");
        People executor2 = createExecutor((long) 5,"name5","surname5","google@google.com",(long)4,(long)40);

        Project project1 = createProject(1,"Проект 3х этажного дома","12.11.20","12.12.21",10,listWorks,"Ставропольский 14",executor1,customer1);
        Project project2 =  createProject(2,"Проект 2х этажного дома","12.11.19","12.12.22",10,listWorks,"Ставропольский 14",executor2,customer2);

        listProject.add(project1);
        listProject.add(project2);

        DataProviderCsv instance = new DataProviderCsv();

        instance.insertProject(listProject);

        assertEquals(Optional.empty(), instance.getProject(3));

    }


    @Test
    public void getProjectByIdSuccess() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        System.out.println("getProjectByIdSuccess");

        DataProviderCsv instance = new DataProviderCsv();

        System.out.println(instance.getProject(1).get());

    }


    @Test
    public void getProjectByIdFail() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        System.out.println("getProjectByIdFail");

        DataProviderCsv instance = new DataProviderCsv();

        assertEquals(Optional.empty(), instance.getProject(5));

    }


    @Test
    public void deleteProjectByIdSuccess() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {

        System.out.println("deleteProjectSuccess");

        DataProviderCsv instance = new DataProviderCsv();

        instance.deleteProjectById(2);

        assertEquals(Optional.empty(), instance.getProject(2));
    }


    @Test
    public void deleteProjectByIdFail() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        System.out.println("deleteProjectByIdFail");

        DataProviderCsv instance = new DataProviderCsv();

        instance.deleteProjectById(2);

    }


    @Test
    public void updateProjectByIdSuccess() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        System.out.println("updateProjectByIdSuccess");
        List<Works> listWorks = new ArrayList<>();
        listWorks.addAll(getCorrectWorksTestData());

        DataProviderCsv instance = new DataProviderCsv();

        int newId = 1;

        People customer = createCustomer((long) 1,"newname1","newsurname1","google@google.com","newaddress1","newtelephone1");
        People executor = createExecutor((long) 4,"newname4","newsurname4","google@google.com",(long)4,(long)40);


        instance.rewriteProject((long) newId,"Проект 3х этажного дома","11.11.11","12.12.12",10,listWorks,"Ставропольский 14",executor,customer);

        System.out.println(instance.getProject(newId).get());

    }


    @Test
    public void updateProjectByIdFail() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        System.out.println("updateProjectByIdFail");

        List<Works> listWorks = new ArrayList<>();
        listWorks.addAll(getCorrectWorksTestData());

        DataProviderCsv instance = new DataProviderCsv();

        int newId = 10;

        People customer = createCustomer((long) 1,"newname1","newsurname1","google@google.com","newaddress1","newtelephone1");
        People executor = createExecutor((long) 4,"newname4","newsurname4","google@google.com",(long)4,(long)40);

        instance.rewriteProject((long) newId,"Проект 3х этажного дома","11.11.11","12.12.12",10,listWorks,"Ставропольский 14",executor,customer);

    }

    @Test
    public void  calculatingEstimateWithReportSuccess() throws IOException {

        DataProviderCsv instance = new DataProviderCsv();
        boolean createReport = true;
        System.out.println(instance.calculatingEstimate((long)1, createReport));
    }

    @Test
    public void  calculatingEstimateWithoutReportSuccess() throws IOException {

        DataProviderCsv instance = new DataProviderCsv();
        boolean createReport = false;
        System.out.println(instance.calculatingEstimate((long)1, createReport));
    }


    @Test
    public void  calculatingDeadlineSuccess() throws IOException {

        DataProviderCsv instance = new DataProviderCsv();
        System.out.println(instance.calculatingDeadline((long)1));
    }



    @Test
    public void  markTheStatusOfWorkSuccess() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

        DataProviderCsv instance = new DataProviderCsv();

        String status="DONE";
        long idOfWork =1;

        instance.markTheStatusOfWork(idOfWork,status);

        System.out.println(instance.getWorks(idOfWork).get());

    }

    @Test
    public void  deadlineReportSuccess() throws IOException {

        DataProviderCsv instance = new DataProviderCsv();
        System.out.println( instance.deadlineReport((long)1));

    }

    @Test
    public void  getProgressReportSuccess() throws IOException {

        DataProviderCsv instance = new DataProviderCsv();
        System.out.println(instance.getProgressReport((long)1));
    }

    @Test
    public void getTheRemainingTimeToCompleteSuccess() throws IOException {
        DataProviderCsv instance = new DataProviderCsv();
        System.out.println(instance.getTheRemainingTimeToComplete((long)1));
    }

}