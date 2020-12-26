package ru.sfedu.constrcaclconsol.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.jupiter.api.*;
import ru.sfedu.constrcaclconsol.Constants;
import ru.sfedu.constrcaclconsol.TestBase;
import ru.sfedu.constrcaclconsol.bean.*;
import ru.sfedu.constrcaclconsol.utils.ConfigurationUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DataProviderCsvTest extends TestBase {


    private static final Logger log = LogManager.getLogger(DataProviderCsvTest.class);
    private static final DataProvider dataProvider = new DataProviderCsv();

    private static <T> void deleteFile(Class<T> tClass) {
        try {
            log.debug(new File(ConfigurationUtil.getConfigurationEntry(Constants.PATH_CSV)
                    + tClass.getSimpleName().toLowerCase()
                    + ConfigurationUtil.getConfigurationEntry(Constants.FILE_EXTENSION_CSV)).delete());
        } catch (IOException e) {
            log.error(e);
        }
    }

    private static void deleteAll() {
        List<Class> classList = new ArrayList<>();
        classList.add(Customer.class);
        classList.add(Executor.class);
        classList.add(Materials.class);
        classList.add(People.class);
        classList.add(Project.class);
        classList.add(Works.class);
        classList.forEach(DataProviderCsvTest::deleteFile);
    }


    public DataProviderCsvTest(){

    }

    @BeforeAll
    static void init(){

        deleteAll();
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
    @org.junit.jupiter.api.Order(0)
    public void insertMaterialFail() throws Exception {

        log.info("insertMaterialFail");

        List<Materials> listMaterials = new ArrayList<>();

        Assertions.assertFalse(dataProvider.createMaterials(listMaterials));
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    public void getMaterialByIdSuccess() throws Exception {

        log.info("getMaterialByIdSuccess");

        Optional<Materials> optionalMaterial = dataProvider.getMaterialById(1);
        Assertions.assertEquals("paint", optionalMaterial.get().getName());

    }

    @Test
    @org.junit.jupiter.api.Order(1)
    public void getMeterialByIdFail() throws Exception {

        log.info("getMeterialByIdFail");

        Optional<Materials> optionalMaterial = dataProvider.getMaterialById(58);

        Assertions.assertFalse(optionalMaterial.isPresent());
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    public void deleteMaterialSuccess() throws Exception {

        log.info("deleteMaterialSuccess");

        long id = 1;

        Assertions.assertTrue(dataProvider.deleteMaterialById(id));

    }

    @Test
    @org.junit.jupiter.api.Order(2)
    public void deleteMaterialFail() throws Exception {

        log.info("deleteMaterialFail");

        long id = 21;

        Assertions.assertFalse(dataProvider.deleteMaterialById(id));
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    public void updateMaterialSuccess() throws Exception {

        log.info("updateMaterialSuccess");

        long idRewriteMaterial = 1;
        String newName = "RewriteName";
        long newNumber = 10;
        long newTotalPrice = 100;

        Assertions.assertTrue(dataProvider.updateMaterial(idRewriteMaterial,newName,newNumber,newTotalPrice));

    }

    @Test
    @org.junit.jupiter.api.Order(3)
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
    @org.junit.jupiter.api.Order(4)
    public void insertWorksSuccess() throws Exception {

        log.info("insertWorksSuccess");

        List<Works> listWorks = new ArrayList<>();
        listWorks.addAll(getCorrectWorksTestData());

        Assertions.assertTrue(dataProvider.createWork(listWorks));
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    public void insertWorksFail() throws Exception {

        log.info("insertWorksFail");

        List<Works> listWorks = new ArrayList<>();

        Assertions.assertFalse(dataProvider.createWork(listWorks));

    }

    @Test
    @org.junit.jupiter.api.Order(5)
    public void getWorksByIdSuccess() throws Exception {

        log.info("getWorksByIdSuccess");

        Optional<Works> optionalWork = dataProvider.getWork(3);
        Assertions.assertTrue(optionalWork.isPresent());
        Assertions.assertEquals("To lay pipeline", optionalWork.get().getName());

    }

    @Test
    @org.junit.jupiter.api.Order(5)
    public void getWorksByIdFail() throws Exception {

        log.info("getWorksByIdFail");

        Optional<Works> optionalWork = dataProvider.getWork(40);
        Assertions.assertFalse(optionalWork.isPresent());

    }

    @Test
    @org.junit.jupiter.api.Order(6)
    public void updateWorksSuccess() throws Exception {

        log.info("updateWorksSuccess");

        long price = 10;
        String status = "CREATE";
        String priority ="HIGH";

        List<Materials> listMaterials = new ArrayList<>();

        long newId = Integer.parseInt(String.valueOf(2));

        listMaterials.add(dataProvider.getMaterialById(5).get());
        listMaterials.add(dataProvider.getMaterialById(3).get());
        listMaterials.add(dataProvider.getMaterialById(4).get());

        Assertions.assertTrue(dataProvider.updateWork(newId,"rewriteName",price,priority,(long)100,status,listMaterials));
    }

    @Test
    @org.junit.jupiter.api.Order(6)
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

    }

    @Test
    @org.junit.jupiter.api.Order(7)
    public void deleteWorksSuccess() throws Exception {

        log.info("deleteWorksSuccess");

        Assertions.assertTrue(dataProvider.deleteWorkById(2));
    }

    @Test
    @org.junit.jupiter.api.Order(7)
    public void deleteWorksFail() throws Exception {

        log.info("deleteWorksFail");

        Assertions.assertFalse(dataProvider.deleteWorkById(45));
    }


    //CRUD Customer
    @Test
    @org.junit.jupiter.api.Order(8)
    public void insertCustomerSuccess() throws Exception {

        log.info("insertCustomerSuccess");

        Assertions.assertTrue(dataProvider.createCustomer("name1","surname1","google@google.com","telephone1"));
        Assertions.assertTrue(dataProvider.createCustomer("name2","surname2","yandex@yandex.ru","telephone2"));
        Assertions.assertTrue(dataProvider.createCustomer("name3","surname3","rambler@rambler.com","telephone3"));

    }


    @Test
    @org.junit.jupiter.api.Order(8)
    public void insertCustomerFail() throws Exception {

        log.info("insertCustomerFail");

        Assertions.assertFalse(dataProvider.createCustomer(null,"surname1","google@google.com","telephone1"));
        Assertions.assertFalse(dataProvider.createCustomer("name2",null,"yandex@yandex.ru","telephone2"));
        Assertions.assertFalse(dataProvider.createCustomer("name3","surname3",null,"telephone3"));
        Assertions.assertFalse(dataProvider.createCustomer("name4","surname4",null,"telephone4" ));
        Assertions.assertFalse(dataProvider.createCustomer("name5","surname5","gmail@gmail.com",null));

    }

    @Test
    @org.junit.jupiter.api.Order(9)
    public void getByIdCustomerSuccess() throws Exception {

        log.info("getByIdCustomerSuccess");

        log.debug(dataProvider.getCustomerById(1).get());
        Optional<Customer> optionalCustomer = dataProvider.getCustomerById(1);
        Assertions.assertTrue(optionalCustomer.isPresent());


    }


    @Test
    @org.junit.jupiter.api.Order(9)
    public void getByIdCustomerFail() throws Exception {

        log.info("getByIdCustomerFail");


        Assertions.assertEquals(dataProvider.getCustomerById(8),Optional.empty());
    }


    @Test
    @org.junit.jupiter.api.Order(10)
    public void deleteCustomerSuccess() throws Exception {

        log.info("deleteCustomerSuccess");

        Assertions.assertTrue(dataProvider.deleteCustomerById(1));

    }


    @Test
    @org.junit.jupiter.api.Order(10)
    public void deleteCustomerFail() throws Exception {

        log.info("deleteCustomerFail");

        Assertions.assertFalse(dataProvider.deleteCustomerById(10));
    }


    @Test
    @org.junit.jupiter.api.Order(11)
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
    @org.junit.jupiter.api.Order(11)
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
    @org.junit.jupiter.api.Order(12)
    public void insertExecutorSuccess() throws Exception {

        log.info("insertExecutorSuccess");

        Assertions.assertTrue(dataProvider.createExecutor("name4","surname4","google@google.com",(long)4,(long)40));
        Assertions.assertTrue(dataProvider.createExecutor("name5","surname5","google@google.com",(long)5,(long)50));
        Assertions.assertTrue(dataProvider.createExecutor("name6","surname6","google@google.com",(long)6,(long)60));

    }


    @Test
    @org.junit.jupiter.api.Order(12)
    public void insertExecutorFail() throws Exception {

        log.info("insertExecutorFail");

        Assertions.assertFalse(dataProvider.createExecutor(null,"surname4","google@google.com",(long)4,(long)40));
        Assertions.assertFalse(dataProvider.createExecutor("name5",null,"yandex@yandex.ru",(long)5,(long)50));
        Assertions.assertFalse(dataProvider.createExecutor("name6","surname6",null,(long)6,(long)60));

    }

    @Test
    @org.junit.jupiter.api.Order(13)
    public void getByIdExecutorSuccess() throws Exception {

        log.info("getByIdExecutorSuccess");

        log.debug(dataProvider.getExecutorById(1).get());

        Optional<Executor> optionalExecutor = dataProvider.getExecutorById(1);
        Assertions.assertTrue(optionalExecutor.isPresent());

    }


    @Test
    @org.junit.jupiter.api.Order(13)
    public void getByIdExecutorFail() throws Exception {

        log.info("getByIdExecutorFail");

        Assertions.assertEquals(dataProvider.getExecutorById(8),Optional.empty());
    }


    @Test
    @org.junit.jupiter.api.Order(14)
    public void deleteExecutorSuccess() throws Exception {

        log.info("deleteExecutorSuccess");

        Assertions.assertTrue(dataProvider.deleteExecutorById(2));
    }


    @Test
    @org.junit.jupiter.api.Order(14)
    public void deleteExecutorFail() throws Exception {

        log.info("deleteExecutorFail");

        Assertions.assertFalse(dataProvider.deleteCustomerById(10));
    }

    @Test
    @org.junit.jupiter.api.Order(15)
    public void updateExecutorSuccess() throws Exception {

        log.info("updateExecutorSuccess");

        long idRewriteExecutor = 0;
        String newName = "newName";
        String newSurname = "newSurname";
        String newMailbox = "newMailbox";
        long newNumberOfCompletedWork = 10;
        long newNumberOfWorkers = 100;

        Assertions.assertTrue(dataProvider.updateExecutor(idRewriteExecutor,newName,newSurname,newMailbox,newNumberOfCompletedWork,newNumberOfWorkers));

    }


    @Test
    @org.junit.jupiter.api.Order(15)
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
    @org.junit.jupiter.api.Order(16)
    public void insertProjectWithReportsSuccess() throws Exception {

        log.info("insertProjectSuccess");

        List<Works> listWorks1 = new ArrayList<>();;
        listWorks1.add(dataProvider.getWork(3).get());
        listWorks1.add(dataProvider.getWork(4).get());
        listWorks1.add(dataProvider.getWork(5).get());

        List<Works> listWorks2 = new ArrayList<>();
        listWorks2.add(dataProvider.getWork(6).get());
        listWorks2.add(dataProvider.getWork(7).get());
        listWorks2.add(dataProvider.getWork(8).get());
        listWorks2.add(dataProvider.getWork(9).get());
        listWorks2.add(dataProvider.getWork(10).get());

        People customer1 = dataProvider.getCustomerById(2).get();
        People executor1 =dataProvider.getExecutorById(0).get();

        People customer2 = dataProvider.getCustomerById(2).get();
        People executor2 = dataProvider.getExecutorById(1).get();


        boolean isCreateEstimateReport =true;
        boolean isCreateDeadlineReport=true;

        Assertions.assertTrue(dataProvider.createProject("Проект 3х этажного дома","12.11.20","12.12.21",10,listWorks1,"Ставропольский 14",executor1,customer1, isCreateEstimateReport,  isCreateDeadlineReport));
        Assertions.assertTrue(dataProvider.createProject("Проект 2х этажного дома","12.11.19","12.12.22",10,listWorks2,"Ставропольский 15",executor2,customer2, isCreateEstimateReport,  isCreateDeadlineReport));
        Assertions.assertTrue(dataProvider.createProject("Проект 4х этажного дома","10.11.18","12.12.22",10,listWorks1,"Ставропольский 17",executor2,customer2, isCreateEstimateReport,  isCreateDeadlineReport));

    }


    @Test
    @org.junit.jupiter.api.Order(16)
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
    @org.junit.jupiter.api.Order(17)
    public void insertProjectWithoutReportsSuccess() throws Exception {

        log.info("insertProjectSuccess");

        List<Works> listWorks1 = new ArrayList<>();;
        listWorks1.add(dataProvider.getWork(3).get());
        listWorks1.add(dataProvider.getWork(4).get());
        listWorks1.add(dataProvider.getWork(5).get());

        List<Works> listWorks2 = new ArrayList<>();
        listWorks2.add(dataProvider.getWork(6).get());
        listWorks2.add(dataProvider.getWork(7).get());
        listWorks2.add(dataProvider.getWork(8).get());
        listWorks2.add(dataProvider.getWork(9).get());
        listWorks2.add(dataProvider.getWork(10).get());

        People customer1 = dataProvider.getCustomerById(2).get();
        People executor1 =dataProvider.getExecutorById(0).get();

        People customer2 = dataProvider.getCustomerById(2).get();
        People executor2 = dataProvider.getExecutorById(1).get();

        boolean isCreateEstimateReport =false;
        boolean isCreateDeadlineReport=false;

        Assertions.assertTrue(dataProvider.createProject("Проект 3х этажного дома","12.11.20","12.12.21",10,listWorks1,"Ставропольский 14",executor1,customer1, isCreateEstimateReport,  isCreateDeadlineReport));
        Assertions.assertTrue(dataProvider.createProject("Проект 2х этажного дома","12.11.19","12.12.22",10,listWorks2,"Ставропольский 15",executor2,customer2, isCreateEstimateReport,  isCreateDeadlineReport));
        Assertions.assertTrue(dataProvider.createProject("Проект 4х этажного дома","10.11.18","12.12.22",10,listWorks1,"Ставропольский 17",executor2,customer2, isCreateEstimateReport,  isCreateDeadlineReport));

    }


    @Test
    @org.junit.jupiter.api.Order(17)
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
    @org.junit.jupiter.api.Order(18)
    public void getProjectByIdSuccess() throws Exception {

        log.info("getProjectByIdSuccess");

        log.debug(dataProvider.getProject(1));

        Optional<Project> optionalProject = dataProvider.getProject(1);
        Assertions.assertTrue(optionalProject.isPresent());

    }


    @Test
    @org.junit.jupiter.api.Order(18)
    public void getProjectByIdFail() throws Exception {

        log.info("getProjectByIdFail");

        Assertions.assertEquals(dataProvider.getProject(8),Optional.empty());

    }

    @Test
    @org.junit.jupiter.api.Order(19)
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
    @org.junit.jupiter.api.Order(19)
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
    @org.junit.jupiter.api.Order(20)
    public void deleteProjectWithReportSuccess() throws Exception {

        log.info("deleteProjectWithReportSuccess");

        Assertions.assertTrue(dataProvider.deleteProject(2, true));

    }

    @Test
    @org.junit.jupiter.api.Order(20)
    public void deleteProjectWithReportFail() throws Exception {

        log.info("deleteProjectWithReportFail");

        Assertions.assertFalse(dataProvider.deleteProject(8, true));

    }


    @Test
    @org.junit.jupiter.api.Order(21)
    public void deleteProjectWithoutReportSuccess() throws Exception {

        log.info("deleteProjectWithoutReportSuccess");

        Assertions.assertTrue(dataProvider.deleteProject(1, false));

    }

    @Test
    @org.junit.jupiter.api.Order(21)
    public void deleteProjectWithoutReportFail() throws Exception {

        log.info("deleteProjectWithoutReportFail");

        Assertions.assertFalse(dataProvider.deleteProject(10, false));

    }

    //Other Methods

    @Test
    @org.junit.jupiter.api.Order(24)
    public void  markTheStatusOfWorkSuccess() throws Exception {

        log.info("markTheStatusOfWorkSuccess");

        String status="DONE";
        long idOfWork =1;

        Assertions.assertTrue( dataProvider.markTheStatusOfWork(idOfWork,status));

        log.debug(dataProvider.getWork(idOfWork).get());


    }

    @Test
    @org.junit.jupiter.api.Order(24)
    public void  markTheStatusOfWorkFail() throws Exception {

        log.info("markTheStatusOfWorkSuccess");

        String status="";
        long idOfWork =48;

        Assertions.assertFalse( dataProvider.markTheStatusOfWork(idOfWork,status));


    }


    @Test
    @org.junit.jupiter.api.Order(25)
    public void  getProgressReportSuccess() throws Exception {

        log.info("getProgressReportSuccess");

        Assertions.assertNotNull(dataProvider.getProgressReport((long)1));

        log.debug(dataProvider.getProgressReport((long)1));
    }

    @Test
    @org.junit.jupiter.api.Order(25)
    public void  getProgressReportFail() throws Exception {

        log.info("getProgressReportSuccess");

        Assertions.assertNull(dataProvider.getProgressReport((long)10));

        log.debug(dataProvider.getProgressReport((long)10));
    }


    @Test
    @org.junit.jupiter.api.Order(26)
    public void getTheRemainingTimeToCompleteSuccess() throws Exception {

        log.info("getTheRemainingTimeToCompleteSuccess");

        Assertions.assertNotNull(dataProvider.getTheRemainingTimeToComplete((long)1));

        log.debug(dataProvider.getTheRemainingTimeToComplete((long)1));
    }

    @Test
    @org.junit.jupiter.api.Order(26)
    public void getTheRemainingTimeToCompleteFail() throws Exception {

        log.info("getTheRemainingTimeToCompleteSuccess");

        Assertions.assertNull(dataProvider.getTheRemainingTimeToComplete((long)10));

        log.debug(dataProvider.getTheRemainingTimeToComplete((long)10));
    }
    @Test
    @org.junit.jupiter.api.Order(27)
    public void GetTheCostOfWorksInProjectSuccess() throws Exception{

        log.info("GetTheCostOfWorksInProjectSuccess");
        Assertions.assertNotNull(dataProvider.getTheCostOfWorksInProject(1));



    }
    @Test
    @org.junit.jupiter.api.Order(27)
    public void GetTheCostOfWorksInProjectFail() throws Exception{

        log.info("GetTheCostOfWorksInProjectFail");
        Assertions.assertNull(dataProvider.getTheCostOfWorksInProject(10));

    }


    @Test
    @org.junit.jupiter.api.Order(28)
    public void GetTheCostOfMaterialsInProjectSuccess() throws Exception{

        log.info("GetTheCostOfMaterialsInProjectSuccess");
        Assertions.assertNotNull(dataProvider.getTheCostOfMaterialsInProject(1));


    }
    @Test
    @org.junit.jupiter.api.Order(28)
    public void GetTheCostOfMaterialsInProjectFail() throws Exception{

        log.info("GetTheCostOfMaterialsInProjectFail");
        Assertions.assertNull(dataProvider.getTheCostOfMaterialsInProject(10));

    }



}