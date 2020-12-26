package ru.sfedu.constrcaclconsol.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import ru.sfedu.constrcaclconsol.Constants;
import ru.sfedu.constrcaclconsol.TestBase;
import ru.sfedu.constrcaclconsol.bean.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DataProviderJdbcTest extends TestBase  {

    private static final Logger log = LogManager.getLogger(DataProviderJdbcTest.class);

    private static final DataProviderJdbc dataProvider = new DataProviderJdbc();

    private static void deleteAll() {
        dataProvider.execute(Constants.DROP_TABLES);
    }


    @BeforeAll
    static void init() {
        deleteAll();
        dataProvider.createTable();

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
           words = material_str.split(Constants.REGEX);
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

        for (int i = 1; i <= Constants.WORKS_NUMBER; i++) {
            List<Materials> listMaterialsForWorks = new ArrayList<>();

            work_str = propertiesWorks.getProperty(Constants.NAME_WORKS + i);
            words = work_str.split(Constants.REGEX);

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
    @org.junit.jupiter.api.Order(0)
    public void insertMaterialSuccess() throws Exception {

        log.info("insertMaterialSuccess");

        List<Materials> listMaterials = new ArrayList<>(getCorrectMaterialTestData());

        Assertions.assertTrue(dataProvider.createMaterials(listMaterials));

    }

    @Test
    @org.junit.jupiter.api.Order(1)
    public void insertMaterialFail() throws Exception {

        log.info("insertMaterialFail");

        List<Materials> listMaterials = new ArrayList<>();

        Assertions.assertFalse(dataProvider.createMaterials(listMaterials));
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    public void getMaterialByIdSuccess() throws Exception {

        log.info("getMaterialByIdSuccess");

        Optional<Materials> optionalMaterial = dataProvider.getMaterialById(1);
        Assertions.assertTrue(optionalMaterial.isPresent());
        Assertions.assertEquals("paint", optionalMaterial.get().getName());

    }

    @Test
    @org.junit.jupiter.api.Order(3)
    public void getMeterialByIdFail() throws Exception {

        log.info("getMeterialByIdFail");

        Optional<Materials> optionalMaterial = dataProvider.getMaterialById(58);

        Assertions.assertFalse(optionalMaterial.isPresent());
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    public void deleteMaterialSuccess() throws SQLException {

        log.info("deleteMaterialSuccess");

        long id = 1;

        Assertions.assertTrue(dataProvider.deleteMaterialById(id));

    }

    @Test
    @org.junit.jupiter.api.Order(5)
    public void deleteMaterialFail() throws IOException, SQLException, ClassNotFoundException {

        log.info("deleteMaterialFail");

        long id = 21;

        Assertions.assertFalse(dataProvider.deleteMaterialById(id));
        Assertions.assertEquals(dataProvider.getMaterialById(id), Optional.empty());
    }

    @Test
    @org.junit.jupiter.api.Order(6)
    public void updateMaterialSuccess() throws Exception {

        log.info("updateMaterialSuccess");

        long idRewriteMaterial = 3;
        String newName = "RewriteName2";
        long newNumber = 10;
        long newTotalPrice = 100;

        Assertions.assertTrue(dataProvider.updateMaterial(idRewriteMaterial,newName,newNumber,newTotalPrice));

    }

    @Test
    @org.junit.jupiter.api.Order(7)
    public void updateMaterialFail() throws Exception {

        log.info("updateMaterialFail");

        long idRewriteMaterial = Constants.MATERIALS_NUMBER+1;
        String newName = "RewriteName";
        long newNumber = 10;
        long newTotalPrice = 100;

        Assertions.assertFalse(dataProvider.updateMaterial(idRewriteMaterial,newName,newNumber,newTotalPrice));

    }


    //CRUD Works
    @Test
    @org.junit.jupiter.api.Order(8)
    public void insertWorksSuccess() throws Exception {

        log.info("insertWorksSuccess");

        List<Works> listWorks = new ArrayList<>();
        listWorks.addAll(getCorrectWorksTestData());

        Assertions.assertTrue(dataProvider.createWork(listWorks));

        List<Long> list1 = new ArrayList<>();
        list1.add((long) 1);
        list1.add((long) 2);
        Assertions.assertEquals(list1, dataProvider.getListMaterialsById(1));
        List<Long> list2 = new ArrayList<>();
        list2.add((long) 3);
        list2.add((long) 4);
        Assertions.assertEquals(list2, dataProvider.getListMaterialsById(2));


    }

    @Test
    @org.junit.jupiter.api.Order(9)
    public void insertWorksFail() throws Exception {

        log.info("insertWorksFail");

        List<Works> listWorks = new ArrayList<>();

        Assertions.assertFalse(dataProvider.createWork(listWorks));


        //dataProvider.createListMaterials(1,30);
        //dataProvider.createListMaterials(21,30);
        List<Long> list = new ArrayList<>();
        //Assertions.assertEquals(list, dataProvider.getListMaterialsById(21));
        Assertions.assertEquals(list, dataProvider.getListMaterialsById(30));


    }

    @Test
    @org.junit.jupiter.api.Order(10)
    public void getWorksByIdSuccess() throws Exception {

        log.info("getWorksByIdSuccess");

        Optional<Works> optionalWork = dataProvider.getWork(2);

        List<Long> list1 = new ArrayList<>();
        list1.add((long) 3);
        list1.add((long) 4);

        Assertions.assertTrue(optionalWork.isPresent());

        log.debug(optionalWork.get());

        Assertions.assertEquals("To paste wallpaper", optionalWork.get().getName());

        Assertions.assertEquals(list1, dataProvider.getListMaterialsById(2));



    }

    @Test
    @org.junit.jupiter.api.Order(11)
    public void getWorksByIdFail() throws Exception {

        log.info("getWorksByIdFail");

        Optional<Works> optionalWork = dataProvider.getWork(40);
        Assertions.assertFalse(optionalWork.isPresent());

        List<Long> list = new ArrayList<>();
        Assertions.assertEquals(list, dataProvider.getListMaterialsById(40));


    }


    @Test
    @org.junit.jupiter.api.Order(12)
    public void updateWorksSuccess() throws Exception {

        log.info("updateWorksSuccess");

        long price = 10;
        String status = "CREATE";
        String priority ="HIGH";

        List<Materials> listMaterials = new ArrayList<>();

        long newId = Integer.parseInt(String.valueOf(8));

        listMaterials.add(dataProvider.getMaterialById(5).get());
        listMaterials.add(dataProvider.getMaterialById(9).get());


        Assertions.assertTrue(dataProvider.updateWork(newId,"rewriteName",price,priority,(long)100,status,listMaterials));

    }

    @Test
    @org.junit.jupiter.api.Order(13)
    public void updateWorksFail() throws Exception {

        log.info("updateWorksFail");

        long price = 10;
        String status = "CREATE";
        String priority ="HIGH";

        List<Materials> listMaterials = new ArrayList<>();

        long newId = Integer.parseInt(String.valueOf(30));

        listMaterials.add(dataProvider.getMaterialById(5).get());
        listMaterials.add(dataProvider.getMaterialById(3).get());
        listMaterials.add(dataProvider.getMaterialById(4).get());

        Assertions.assertFalse(dataProvider.updateWork(newId,"rewriteName",price,priority,(long)100,status,listMaterials));
        List<Long> list = new ArrayList<>();
        Assertions.assertEquals(list, dataProvider.getListMaterialsById(30));

    }


    @Test
    @org.junit.jupiter.api.Order(14)
    public void deleteWorksSuccess() throws Exception {

        log.info("deleteWorksSuccess");

        Assertions.assertTrue(dataProvider.deleteWorkById(5));
       // Assertions.assertTrue(dataProvider.deleteListMaterials(5));

    }

    @Test
    @org.junit.jupiter.api.Order(15)
    public void deleteWorksFail() throws Exception {

        log.info("deleteWorksFail");

        Assertions.assertFalse(dataProvider.deleteWorkById(45));

    }


    //CRUD Customer
    @Test
    @org.junit.jupiter.api.Order(16)
    public void insertCustomerSuccess() throws Exception {
        log.info("insertCustomerSuccess");

        Assertions.assertTrue(dataProvider.createCustomer("name1","surname1","google@google.com","telephone1"));
        Assertions.assertTrue(dataProvider.createCustomer("name2","surname2","yandex@yandex.ru","telephone2"));
        Assertions.assertTrue(dataProvider.createCustomer("name3","surname3","rambler@rambler.com","telephone3"));

    }


    @Test
    @org.junit.jupiter.api.Order(17)
    public void insertCustomerFail() throws Exception {

        log.info("insertCustomerFail");

        Assertions.assertFalse(dataProvider.createCustomer(null,"surname1","google@google.com","telephone1"));
        Assertions.assertFalse(dataProvider.createCustomer("name2",null,"yandex@yandex.ru","telephone2"));
        Assertions.assertFalse(dataProvider.createCustomer("name3","surname3",null,"telephone3"));
        Assertions.assertFalse(dataProvider.createCustomer("name4","surname4",null,"telephone4" ));
        Assertions.assertFalse(dataProvider.createCustomer("name5","surname5","gmail@gmail.com",null));

    }

    @Test
    @org.junit.jupiter.api.Order(18)
    public void getByIdCustomerSuccess() throws Exception {

        log.info("getByIdCustomerSuccess");

        log.debug(dataProvider.getCustomerById(1));

    }


    @Test
    @org.junit.jupiter.api.Order(19)
    public void getByIdCustomerFail() throws Exception {

        log.info("getByIdCustomerFail");

        log.debug(dataProvider.getCustomerById(8));
    }


    @Test
    @org.junit.jupiter.api.Order(20)
    public void deleteCustomerSuccess() throws Exception {

        log.info("deleteCustomerSuccess");

        Assertions.assertTrue(dataProvider.deleteCustomerById(1));

    }


    @Test
    @org.junit.jupiter.api.Order(21)
    public void deleteCustomerFail() throws Exception {

        log.info("deleteCustomerFail");

        Assertions.assertFalse(dataProvider.deleteCustomerById(10));
    }


    @Test
    @org.junit.jupiter.api.Order(22)
    public void updateCustomerSuccess() throws Exception {

        log.info("updateCustomerSuccess");

        long idRewriteCustomer = 2;
        String newName = "newName";
        String newSurname = "newSurname";
        String newMailbox = "newMailbox";
        String telephone = "newTelephone";

        Assertions.assertTrue(dataProvider.updateCustomer(idRewriteCustomer,newName,newSurname,newMailbox,telephone));

    }


    @Test
    @org.junit.jupiter.api.Order(23)
    public void updateCustomerFail() throws Exception {

        log.info("updateCustomerFail");

        long idRewriteCustomer = 20;
        String newName = "newName2";
        String newSurname = "newSurname2";
        String newMailbox = "newMailbox2";
        String telephone = "newTelephone2";

        Assertions.assertFalse(dataProvider.updateCustomer(idRewriteCustomer,newName,newSurname,newMailbox,telephone));
    }




    //CRUD Executor
    @Test
    @org.junit.jupiter.api.Order(24)
    public void insertExecutorSuccess() throws Exception {

        log.info("insertExecutorSuccess");

        Assertions.assertTrue(dataProvider.createExecutor("name4","surname4","google@google.com",(long)4,(long)40));
        Assertions.assertTrue(dataProvider.createExecutor("name5","surname5","google@google.com",(long)5,(long)50));
        Assertions.assertTrue(dataProvider.createExecutor("name6","surname6","google@google.com",(long)6,(long)60));

    }


    @Test
    @org.junit.jupiter.api.Order(25)
    public void insertExecutorFail() throws Exception {

        log.info("insertExecutorFail");

        Assertions.assertFalse(dataProvider.createExecutor(null,"surname4","google@google.com",(long)4,(long)40));
        Assertions.assertFalse(dataProvider.createExecutor("name5",null,"yandex@yandex.ru",(long)5,(long)50));
        Assertions.assertFalse(dataProvider.createExecutor("name6","surname6",null,(long)6,(long)60));

    }

    @Test
    @org.junit.jupiter.api.Order(26)
    public void getByIdExecutorSuccess() throws Exception {

        log.info("getByIdExecutorSuccess");

        log.debug(dataProvider.getExecutorById(4));

    }


    @Test
    @org.junit.jupiter.api.Order(27)
    public void getByIdExecutorFail() throws Exception {

        log.info("getByIdExecutorFail");

        log.debug(dataProvider.getExecutorById(10));
    }


    @Test
    @org.junit.jupiter.api.Order(28)
    public void deleteExecutorSuccess() throws Exception {

        log.info("deleteExecutorSuccess");

        Assertions.assertTrue(dataProvider.deleteExecutorById(2));
    }


    @Test
    @org.junit.jupiter.api.Order(29)
    public void deleteExecutorFail() throws Exception {

        log.info("deleteExecutorFail");

        Assertions.assertFalse(dataProvider.deleteCustomerById(10));
    }

    @Test
    @org.junit.jupiter.api.Order(30)
    public void updateExecutorSuccess() throws Exception {

        log.info("updateExecutorSuccess");

        long idRewriteExecutor = 1;
        String newName = "newName";
        String newSurname = "newSurname";
        String newMailbox = "newMailbox";
        long newNumberOfCompletedWork = 10;
        long newNumberOfWorkers = 100;

        Assertions.assertTrue(dataProvider.updateExecutor(idRewriteExecutor,newName,newSurname,newMailbox,newNumberOfCompletedWork,newNumberOfWorkers));

    }


    @Test
    @org.junit.jupiter.api.Order(31)
    public void updateExecutorFail() throws Exception {

        log.info("updateExecutorFail");

        long idRewriteExecutor = 10;
        String newName = "newName";
        String newSurname = "newSurname";
        String newMailbox = "newMailbox";
        long newNumberOfCompletedWork = 10;
        long newNumberOfWorkers = 100;

        Assertions.assertFalse(dataProvider.updateExecutor(idRewriteExecutor,newName,newSurname,newMailbox,newNumberOfCompletedWork,newNumberOfWorkers));

    }




//CRUD Project

    @Test
    @org.junit.jupiter.api.Order(32)
    public void insertProjectWithReportsSuccess() throws Exception {

        log.info("insertProjectSuccess");

        List<Works> listWorks1 = new ArrayList<>();;
        listWorks1.add(dataProvider.getWork(3).get());
        listWorks1.add(dataProvider.getWork(4).get());
        listWorks1.add(dataProvider.getWork(6).get());

        List<Works> listWorks2 = new ArrayList<>();
        listWorks2.add(dataProvider.getWork(6).get());
        listWorks2.add(dataProvider.getWork(7).get());
        listWorks2.add(dataProvider.getWork(8).get());
        listWorks2.add(dataProvider.getWork(9).get());
        listWorks2.add(dataProvider.getWork(10).get());

        People customer1 = dataProvider.getCustomerById(2).get();
        People executor1 =dataProvider.getExecutorById(1).get();

        People customer2 = dataProvider.getCustomerById(2).get();
        People executor2 = dataProvider.getExecutorById(1).get();


        boolean isCreateEstimateReport =true;
        boolean isCreateDeadlineReport=true;

        Assertions.assertTrue(dataProvider.createProject("Проект 3х этажного дома","12.11.20","12.12.21",10,listWorks1,"Ставропольский 14",executor1,customer1, isCreateEstimateReport,  isCreateDeadlineReport));
        Assertions.assertTrue(dataProvider.createProject("Проект 2х этажного дома","12.11.19","12.12.22",10,listWorks2,"Ставропольский 15",executor2,customer2, isCreateEstimateReport,  isCreateDeadlineReport));
        Assertions.assertTrue(dataProvider.createProject("Проект 4х этажного дома","10.11.18","12.12.22",10,listWorks1,"Ставропольский 17",executor2,customer2, isCreateEstimateReport,  isCreateDeadlineReport));

    }


    @Test
    @org.junit.jupiter.api.Order(33)
    public void insertProjectWithReportsFail() throws Exception {

        log.info("insertProjectFail");

        List<Works> listWorks1 = new ArrayList<>();

        List<Works> listWorks2 = new ArrayList<>();
        listWorks2.add(dataProvider.getWork(6).get());
        listWorks2.add(dataProvider.getWork(7).get());
        listWorks2.add(dataProvider.getWork(8).get());
        listWorks2.add(dataProvider.getWork(9).get());
        listWorks2.add(dataProvider.getWork(10).get());

        People customer1 = dataProvider.getCustomerById(2).get();
        People executor1 =dataProvider.getExecutorById(1).get();

        People customer2 = dataProvider.getCustomerById(2).get();

        boolean isCreateEstimateReport =true;
        boolean isCreateDeadlineReport=true;


        Assertions.assertFalse(dataProvider.createProject(null,"12.11.20","12.12.21",10,listWorks1,"Ставропольский 14",executor1,customer1, isCreateEstimateReport,  isCreateDeadlineReport));
        Assertions.assertFalse(dataProvider.createProject("Проект 2х этажного дома","12.11.19","12.12.22",10,listWorks2,"Ставропольский 15",null,customer2, isCreateEstimateReport,  isCreateDeadlineReport));

    }


    @Test
    @org.junit.jupiter.api.Order(34)
    public void insertProjectWithoutReportsSuccess() throws Exception {

        log.info("insertProjectSuccess");

        List<Works> listWorks1 = new ArrayList<>();;
        listWorks1.add(dataProvider.getWork(3).get());
        listWorks1.add(dataProvider.getWork(4).get());
        listWorks1.add(dataProvider.getWork(6).get());

        List<Works> listWorks2 = new ArrayList<>();
        listWorks2.add(dataProvider.getWork(6).get());
        listWorks2.add(dataProvider.getWork(7).get());
        listWorks2.add(dataProvider.getWork(8).get());
        listWorks2.add(dataProvider.getWork(9).get());
        listWorks2.add(dataProvider.getWork(10).get());

        People customer1 = dataProvider.getCustomerById(2).get();
        People executor1 =dataProvider.getExecutorById(1).get();

        People customer2 = dataProvider.getCustomerById(2).get();
        People executor2 = dataProvider.getExecutorById(1).get();
        boolean isCreateEstimateReport =false;
        boolean isCreateDeadlineReport=false;

        Assertions.assertTrue(dataProvider.createProject("Проект 3х этажного дома","12.11.20","12.12.21",10,listWorks1,"Ставропольский 14",executor1,customer1, isCreateEstimateReport,  isCreateDeadlineReport));
        Assertions.assertTrue(dataProvider.createProject("Проект 2х этажного дома","12.11.19","12.12.22",10,listWorks2,"Ставропольский 15",executor2,customer2, isCreateEstimateReport,  isCreateDeadlineReport));
        Assertions.assertTrue(dataProvider.createProject("Проект 4х этажного дома","10.11.18","12.12.22",10,listWorks1,"Ставропольский 17",executor2,customer2, isCreateEstimateReport,  isCreateDeadlineReport));

    }


    @Test
    @org.junit.jupiter.api.Order(35)
    public void insertProjectWithoutReportsFail() throws Exception {

        log.info("insertProjectFail");

        List<Works> listWorks1 = new ArrayList<>();

        List<Works> listWorks2 = new ArrayList<>();
        listWorks2.add(dataProvider.getWork(6).get());
        listWorks2.add(dataProvider.getWork(7).get());
        listWorks2.add(dataProvider.getWork(8).get());
        listWorks2.add(dataProvider.getWork(9).get());
        listWorks2.add(dataProvider.getWork(10).get());

        People customer1 = dataProvider.getCustomerById(2).get();
        People executor1 =dataProvider.getExecutorById(1).get();

        People customer2 = dataProvider.getCustomerById(2).get();

        boolean isCreateEstimateReport =false;
        boolean isCreateDeadlineReport=false;


        Assertions.assertFalse(dataProvider.createProject(null,"12.11.20","12.12.21",10,listWorks1,"Ставропольский 14",executor1,customer1, isCreateEstimateReport,  isCreateDeadlineReport));
        Assertions.assertFalse(dataProvider.createProject("Проект 2х этажного дома","12.11.19","12.12.22",10,listWorks2,"Ставропольский 15",null,customer2, isCreateEstimateReport,  isCreateDeadlineReport));

    }




    @Test
    @org.junit.jupiter.api.Order(36)
    public void getProjectByIdSuccess() throws Exception {

        log.info("getProjectByIdSuccess");

        Optional<Project> optionalProject = dataProvider.getProject(1);

        Assertions.assertTrue(optionalProject.isPresent());

        log.debug(optionalProject.get());

        Assertions.assertEquals("Проект 3х этажного дома", optionalProject.get().getName());


    }


    @Test
    @org.junit.jupiter.api.Order(37)
    public void getProjectByIdFail() throws Exception {

        log.info("getProjectByIdFail");

        log.debug(dataProvider.getProject(10));

        Assertions.assertEquals(Optional.empty(),dataProvider.getProject(10));

    }

    @Test
    @org.junit.jupiter.api.Order(38)
    public void updateProjectByIdSuccess() throws Exception {

        log.info("updateProjectByIdSuccess");

        int newId = 1;

        People customer = dataProvider.getCustomerById(2).get();
        People executor = dataProvider.getExecutorById(1).get();

        List<Works> listWorks = new ArrayList<>();
        listWorks.add(dataProvider.getWork(6).get());
        listWorks.add(dataProvider.getWork(7).get());
        listWorks.add(dataProvider.getWork(8).get());
        listWorks.add(dataProvider.getWork(9).get());
        listWorks.add(dataProvider.getWork(10).get());

        Assertions.assertTrue(dataProvider.updateProject((long) newId,"Проект 3х этажного дома","11.11.11","12.12.12",10,listWorks,"Ставропольский 14",executor,customer));

    }


    @Test
    @org.junit.jupiter.api.Order(39)
    public void updateProjectByIdFail() throws Exception {

        log.info("updateProjectByIdFail");

        int newId = 11;

        People customer = dataProvider.getCustomerById(2).get();
        People executor = dataProvider.getExecutorById(1).get();

        List<Works> listWorks = new ArrayList<>();
        listWorks.add(dataProvider.getWork(6).get());
        listWorks.add(dataProvider.getWork(7).get());
        listWorks.add(dataProvider.getWork(8).get());
        listWorks.add(dataProvider.getWork(9).get());
        listWorks.add(dataProvider.getWork(10).get());

        Assertions.assertFalse(dataProvider.updateProject((long) newId,"Проект 3х этажного дома","11.11.11","12.12.12",10,listWorks,"Ставропольский 14",executor,customer));

    }



    @Test
    @org.junit.jupiter.api.Order(40)
    public void deleteProjectWithReportSuccess() throws Exception {

        log.info("deleteProjectWithReportSuccess");

        Assertions.assertTrue(dataProvider.deleteProject(2, true));

    }

    @Test
    @org.junit.jupiter.api.Order(41)
    public void deleteProjectWithReportFail() throws Exception {

        log.info("deleteProjectWithReportFail");

        Assertions.assertFalse(dataProvider.deleteProject(10, true));

    }


    @Test
    @org.junit.jupiter.api.Order(42)
    public void deleteProjectWithoutReportSuccess() throws Exception {

        log.info("deleteProjectWithoutReportSuccess");

        Assertions.assertTrue(dataProvider.deleteProject(1, false));

    }

    @Test
    @org.junit.jupiter.api.Order(43)
    public void deleteProjectWithoutReportFail() throws Exception {

        log.info("deleteProjectWithoutReportFail");

        Assertions.assertFalse(dataProvider.deleteProject(10, false));

    }

    @Test
    @org.junit.jupiter.api.Order(44)
    public void  markTheStatusOfWorkSuccess() throws Exception {

        log.info("markTheStatusOfWorkSuccess");

        String status="DONE";
        long idOfWork =10;

        Assertions.assertTrue( dataProvider.markTheStatusOfWork(idOfWork,status));

        log.debug(dataProvider.getWork(idOfWork).get());


    }


    @Test
    @org.junit.jupiter.api.Order(45)
    public void  getProgressReportSuccess() throws Exception {

        log.info("getProgressReportSuccess");

        Assertions.assertNotNull(dataProvider.getProgressReport((long)3));

        log.debug(dataProvider.getProgressReport((long)3));
    }

    @Test
    @org.junit.jupiter.api.Order(46)
    public void getTheRemainingTimeToCompleteSuccess() throws Exception {

        log.info("getTheRemainingTimeToCompleteSuccess");

        Assertions.assertNotNull(dataProvider.getTheRemainingTimeToComplete((long)3));

        log.debug(dataProvider.getTheRemainingTimeToComplete((long)3));
    }
    @Test
    @org.junit.jupiter.api.Order(47)
    public void GetTheCostOfWorksInProjectSuccess() throws Exception{

        log.info("GetTheCostOfWorksInProjectSuccess");
        dataProvider.getTheCostOfWorksInProject(1);



    }
    @Test
    @org.junit.jupiter.api.Order(48)
    public void GetTheCostOfWorksInProjectFail() throws Exception{

        log.info("GetTheCostOfWorksInProjectFail");
        dataProvider.getTheCostOfWorksInProject(10);

    }


    @Test
    @org.junit.jupiter.api.Order(49)
    public void GetTheCostOfMaterialsInProjectSuccess() throws Exception{

        log.info("GetTheCostOfMaterialsInProjectSuccess");
        dataProvider.getTheCostOfMaterialsInProject(1);


    }
    @Test
    @org.junit.jupiter.api.Order(50)
    public void GetTheCostOfMaterialsInProjectFail() throws Exception{

        log.info("GetTheCostOfMaterialsInProjectFail");
        dataProvider.getTheCostOfMaterialsInProject(10);

    }
}


