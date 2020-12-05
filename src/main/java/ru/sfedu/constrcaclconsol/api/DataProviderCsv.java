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

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static ru.sfedu.constrcaclconsol.Constants.CANNOT_CREATE_FILE;


public class DataProviderCsv implements  DataProvider{

    private final String PATH = "csv_path";
    private final String FILE_EXTENSION = "csv";

    private static Logger log = LogManager.getLogger(DataProviderCsv.class);



    private <T> CSVReader getCsv(Class<T> tClass) throws IOException {
        File file = new File(ConfigurationUtil.getConfigurationEntry(PATH) + tClass.getSimpleName().toLowerCase() + ConfigurationUtil.getConfigurationEntry(FILE_EXTENSION));

        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException(ConfigurationUtil.getConfigurationEntry(CANNOT_CREATE_FILE));
            }
        }

        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        return new CSVReader(bufferedReader);
    }

    private <T> List<T> getFromCsv(Class<T> tClass) throws IOException {
        List<T> tList;

        try {
            CSVReader csvReader = getCsv(tClass);
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(csvReader)
                    .withType(tClass)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            tList = csvToBean.parse();
            csvReader.close();
        } catch (IOException e) {
            log.error(e);
            throw e;
        }
        return tList;
    }

    private <T> List<Materials> getMaterialList(Class<T> tClass, T object) throws IOException {

        try {
           List<Materials> objectmateriallist;

           Works works = (Works) object;

           objectmateriallist = works.getListMaterials();

           List<Materials> materialsList = getFromCsv(Materials.class);
           List<Long> idMaterialInWorks;

            idMaterialInWorks = objectmateriallist
                    .stream()
                    .map(value -> value.getId())
                    .collect(Collectors.toList());


            List<Materials>workListInProject;
            workListInProject =materialsList.stream()
                    .filter(workss -> idMaterialInWorks
                            .stream()
                            .anyMatch(workInProject -> workInProject.longValue() ==  workss.getId()))
                    .collect(Collectors.toList());


            return workListInProject;

        }catch(IOException e){
            log.error(e);
            return new ArrayList<>();
        }
    }

    private <T> People getCustomerInProject(Class<T> tClass, T object) throws IOException {

        Customer customerInProject;

        Project project = (Project) object;

        People customer = project.getCustomer();

        List<Customer> customerList = getFromCsv(Customer.class);

        customerInProject = customerList
                .stream()
                .filter(x -> x.getId() == customer.getId())
                .findAny()
                .orElse(null);


        return customerInProject;

    }

    private <T> People getExecutorInProject(Class<T> tClass, T object) throws IOException {
        Executor executorInProject;
        Project project = (Project) object;

        People executor = project.getExecutor();

        List<Executor> executorList = getFromCsv(Executor.class);
        executorInProject = executorList
                .stream()
                .filter(x -> x.getId() == executor.getId())
                .findAny()
                .orElse(null);

        return executorInProject;


    }

    private <T> List<Works> getWorksList(Class<T> tClass, T object) throws IOException {
        try {
            List<Works> objectworklist;

            Project project = (Project) object;

            objectworklist = project.getWorksList();

            List<Works> workList = getFromCsv(Works.class);

            List<Works> workListInProject;

            List<Long> listIdWorksInProject = new ArrayList<>();
            List<Works> worksInProjectWithMaterials = new ArrayList<>();



            listIdWorksInProject = objectworklist
                    .stream()
                    .map(value -> value.getId())
                     .collect(Collectors.toList());

            List<Long> finalListIdWorksInProject = listIdWorksInProject;
            workListInProject = workList.stream()
                    .filter(works -> finalListIdWorksInProject
                            .stream()
                            .anyMatch(workInProject -> workInProject.longValue() ==  works.getId()))
                    .collect(Collectors.toList());

           // Не заработал stream()
            for (int i =0;i<workListInProject.size();i++)
            {
                worksInProjectWithMaterials.add(getWorks(workListInProject.get(i).getId()).get());

            }

            return worksInProjectWithMaterials;

        }catch(IOException e){
            log.error(e);
            return new ArrayList<>();
        }
    }

    public <T> void insertClassIntoCsv(List<T> listClass) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        try{
            FileWriter writer = new FileWriter(ConfigurationUtil.getConfigurationEntry(PATH)
                    + listClass.get(0).getClass().getSimpleName().toLowerCase()
                    + ConfigurationUtil.getConfigurationEntry(FILE_EXTENSION));
            CSVWriter csvWriter = new CSVWriter(writer);
            StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(csvWriter)
                    .withApplyQuotesToAll(false)
                    .build();
            beanToCsv.write(listClass);
            csvWriter.close();
        }catch (IndexOutOfBoundsException e){
            log.error(e);
        }
    }


    //CRUD MATERIALS
    public void insertMaterials(List<Materials> listMaterials) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

        this.insertClassIntoCsv(listMaterials);

    }

    public Materials getMaterialsById(long materialId) throws IOException {
        List<Materials> listMaterials = getFromCsv(Materials.class);
        try {
            Materials materials = listMaterials.stream()
                    .filter(el -> el.getId() == materialId)
                    .findFirst().get();
            return materials;
        } catch (NoSuchElementException e) {
            log.error(e);
            return null;
        }

    }

    public void deleteMaterialsById(long id) throws IOException {
        List<Materials> listMaterials = getFromCsv(Materials.class);
        try {
            Materials materials = listMaterials.stream()
                    .filter(el -> el.getId() == id)
                    .findFirst().get();

            listMaterials.remove(materials);
            insertMaterials(listMaterials);
        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException | NoSuchElementException | IndexOutOfBoundsException e) {
            log.error(e);

        }

    }

    public void rewriteMaterials(long id, String name,Long number, Long totalPrice ) throws IOException {
        List<Materials> listMaterials = getFromCsv(Materials.class);

        int newId = Integer.parseInt(String.valueOf(id))-1;

        try {
            Materials newMaterials = new Materials();

            newMaterials.setId(id);
            newMaterials.setName(name);
            newMaterials.setNumber(number);
            newMaterials.setTotalPrice(totalPrice);

            listMaterials.set(newId,newMaterials);

            insertMaterials(listMaterials);

        } catch (CsvDataTypeMismatchException| NoSuchElementException | IndexOutOfBoundsException | CsvRequiredFieldEmptyException e) {
            log.error(e);

        }

    }


    //CRUD WORKS
    public void insertWork(List<Works> listWorks) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        this.insertClassIntoCsv(listWorks);
    }

    Optional<Works> getWorks(long id) {
        try {

            List<Works> workList = getFromCsv(Works.class);

            var optionalWorks = workList.stream()
                    .filter(task -> task.getId() == id)
                    .findAny();
            if (optionalWorks.isEmpty()) {
                return optionalWorks;
            }

            var works = optionalWorks.get();

            works.setListMaterials(getMaterialList(Works.class, works));

            return Optional.of(works);

        } catch (IOException | NoSuchElementException e) {
            log.error(e);
            return Optional.empty();
        }
    }

    public void deleteWorkById(long id) throws IOException {

        List<Works> listWorks = getFromCsv(Works.class);
        try {
            Works works = listWorks.stream()
                    .filter(el -> el.getId() == id)
                    .findFirst().get();

            listWorks.remove(works);
            insertWork(listWorks);
        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException | NoSuchElementException | IndexOutOfBoundsException e) {
            log.error(e);

        }
    }

    public void rewriteWork(Long id, String name, Long price, String priority,
                            Long daysToCompletedWorks, String status, List<Materials> listMaterials ) throws IOException {



        List<Works> listWorks = getFromCsv(Works.class);

        int newId = Integer.parseInt(String.valueOf(id))-1;

        try {
            Works newWorks = new Works();

            newWorks.setId(id);
            newWorks.setName(name);
            newWorks.setPrice(price);
            newWorks.setPriority(priority);
            newWorks.setDaysNeedToCompleted(daysToCompletedWorks);
            newWorks.setStatus(status);
            newWorks.setListMaterials(listMaterials);

            listWorks.set(newId,newWorks);

            insertWork(listWorks);

        } catch (CsvDataTypeMismatchException| NoSuchElementException | IndexOutOfBoundsException | CsvRequiredFieldEmptyException e) {
            log.error(e);

        }





    }


//CRUD People
/*
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
        List<People> listPeople = selectFromCsv(People.class);
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
        List<People> listPeople = selectFromCsv(People.class);
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
        List<People> listPeople = selectFromCsv(People.class);
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
*/


    //CRUD CUSTOMER
    public void insertCustomer(List<Customer> listCustomer) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

        this.insertClassIntoCsv(listCustomer);
    }

    public Customer getCustomerById(long id) throws IOException {
        List<Customer> listCustomer = getFromCsv(Customer.class);
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
        List<Customer> listCustomer = getFromCsv(Customer.class);
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

    public void rewriteCustomer(long id, String name,String surname, String mailbox,String orderAddress, String telephone ) throws IOException {
        List<Customer> listCustomer = getFromCsv(Customer.class);

        int idPosition = -1;

        for (int i =0;i< listCustomer.size();i++)
            if (listCustomer.get(i).getId() == id)
            {
                idPosition=i;
            }

        try {
            Customer newCustomer = new Customer();

            newCustomer.setId(id);
            newCustomer.setName(name);
            newCustomer.setSurname(surname);
            newCustomer.setMailbox(mailbox);
            newCustomer.setOrderAddress(orderAddress);
            newCustomer.setTelephone(telephone);
            listCustomer.set(idPosition,newCustomer);

            insertCustomer(listCustomer);

        } catch (CsvDataTypeMismatchException| NoSuchElementException | IndexOutOfBoundsException | CsvRequiredFieldEmptyException e) {
            log.error(e);

        }
    }


    //CRUD EXECUTOR
    public void insertExecutor(List<Executor> listExecutor) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

        this.insertClassIntoCsv(listExecutor);
    }

    public Executor getExecutorById(long id) throws IOException {
        List<Executor> listExecutor = getFromCsv(Executor.class);
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
        List<Executor> listExecutor = getFromCsv(Executor.class);
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

    public void rewriteExecutor(long id, String name,String surname, String mailbox,Long newNumberOfCompletedWork, Long newNumberOfWorkers ) throws IOException {
        List<Executor> listExecutor = getFromCsv(Executor.class);

        int idPosition = -1;
        
        for (int i =0;i< listExecutor.size();i++)
            if (listExecutor.get(i).getId() == id)
            {
                idPosition=i;
            }

        try {
            Executor newExecutor = new Executor();

            newExecutor.setId(id);
            newExecutor.setName(name);
            newExecutor.setSurname(surname);
            newExecutor.setMailbox(mailbox);
            newExecutor.setNumberOfCompletedProjects(newNumberOfCompletedWork);
            newExecutor.setNumberOfWorkers(newNumberOfWorkers);

            listExecutor.set(idPosition,newExecutor);

            insertExecutor(listExecutor);

        } catch (CsvDataTypeMismatchException| NoSuchElementException | IndexOutOfBoundsException | CsvRequiredFieldEmptyException e) {
            log.error(e);

        }

    }

//CRUD PROJECT
    public void insertProject(List<Project> listProject) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

        this.insertClassIntoCsv(listProject);
    }

    Optional<Project> getProject(long id) {
        try {

            List<Project> projectList = getFromCsv(Project.class);

            var optionalProject = projectList.stream()
                    .filter(task -> task.getId() == id)
                    .findAny();
            if (optionalProject.isEmpty()) {
                return optionalProject;
            }

            var project = optionalProject.get();

            project.setWorksList(getWorksList(Project.class, project));

            project.setCustomer(getCustomerInProject(Project.class, project));
            project.setExecutor(getExecutorInProject(Project.class, project));


            return Optional.of(project);

        } catch (IOException e) {
            log.error(e);
            return Optional.empty();
        }
    }

    public void deleteProjectById(long id) throws IOException {

        List<Project> listProject = getFromCsv(Project.class);
        try {
            Project project = listProject.stream()
                    .filter(el -> el.getId() == id)
                    .findFirst().get();

            listProject.remove(project);
            insertProject(listProject);
        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException | NoSuchElementException | IndexOutOfBoundsException e) {
            log.error(e);

        }
    }


    public void rewriteProject(Long id, String name, String createdDate,
                            String deadline, Integer numberOfWorkers, List<Works> worksList,String address,
                               People executor, People customer ) throws IOException {



        List<Project> listProject = getFromCsv(Project.class);

        int newId = Integer.parseInt(String.valueOf(id))-1;

        try {
            Project newProject = new Project();

            newProject.setId(id);
            newProject.setName(name);
            newProject.setCreatedDate(createdDate);
            newProject.setDeadline(deadline);
            newProject.setNumberOfWorks(numberOfWorkers);
            newProject.setWorksList(worksList);
            newProject.setAddress(address);
            newProject.setExecutor(executor);
            newProject.setCustomer(customer);

            listProject.set(newId,newProject);

            insertProject(listProject);

        } catch (CsvDataTypeMismatchException| NoSuchElementException | IndexOutOfBoundsException | CsvRequiredFieldEmptyException e) {
            log.error(e);

        }

    }



    public Long calculatingEstimate(Long idProject, boolean createReport) throws IOException {

        Project project = new Project();

        project = getProject(idProject).get();

        Long projectPriceForWorks = null;


        List<Works> worksList = new ArrayList<>();
        worksList = getWorksList(Project.class, project);
        List<Long> priceForWorks = new ArrayList<>();

        priceForWorks = worksList
                .stream()
                .map(value -> value.getPrice())
                .collect(Collectors.toList());

        projectPriceForWorks = priceForWorks.stream().map(value -> value.longValue()).filter(a -> a != null).mapToLong(a -> a).sum();


        if (createReport) {
            StringBuffer sb = new StringBuffer();
            sb.append(worksList);
            System.out.println(worksList);

        } else {


        }
        return projectPriceForWorks;
    }


    public Long calculatingDeadline(Long idProject) throws IOException {
        Project project = new Project();

        project = getProject(idProject).get();

        Long deadline = null;

        Long projectNeedDays = null;


        List<Works> worksList = new ArrayList<>();
        worksList = getWorksList(Project.class, project);
        List<Long> priceForWorks = new ArrayList<>();

        priceForWorks = worksList
                .stream()
                .map(value -> value.getDaysNeedToCompleted())
                .collect(Collectors.toList());

        projectNeedDays = priceForWorks.stream().map(value -> value.longValue()).filter(a -> a != null).mapToLong(a -> a).sum();

        return projectNeedDays;

    }

    public void markTheStatusOfWork (Long idWork, String status) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

        List<Works> listWorks = getFromCsv(Works.class);

        int newId = (int) (idWork-1);

            Works newWorks = new Works();
            newWorks =getWorks(idWork).get();

            newWorks.setStatus(status);

            listWorks.set(newId,newWorks);

            insertWork(listWorks);

    }

    public String deadlineReport(Long idProject) throws IOException {
        Project project = new Project();

        project = getProject(idProject).get();

        String startDate="";
        String endDate="";

        startDate = project.getCreatedDate();
        endDate = project.getDeadline();

        String deadline = "Start"+" " + startDate + " " +"End"+ " " + endDate;

        return deadline;

    }


    public HashMap<Integer, String> getProgressReport(Long idProject) throws IOException {

        Project project = new Project();

        project = getProject(idProject).get();

        Long projectPriceForWorks = null;


        List<Works> worksList = new ArrayList<>();
        worksList = getWorksList(Project.class, project);
        HashMap<Integer, String> workAndStatus = new HashMap<>();

        //Сздать через stream
        for(int i=0;i<worksList.size();i++){
            workAndStatus.put(Math.toIntExact(worksList.get(i).getId()),worksList.get(i).getStatus());

        }


        return workAndStatus;
    }

    public Long getTheRemainingTimeToComplete(Long idProject) throws IOException {
        Project project = new Project();

        project = getProject(idProject).get();

        Long deadline = null;

        Long projectNeedDays = null;


        List<Works> worksList = new ArrayList<>();
        worksList = getWorksList(Project.class, project);
        List<Long> NotCompletedWork = new ArrayList<>();
        List<Works> worksListNotCompleted = new ArrayList<>();

        worksListNotCompleted = worksList
                .stream()
                .filter(value -> value.getStatus().equals("CREATE"))
                .collect(Collectors.toList());



        NotCompletedWork = worksListNotCompleted
                .stream()
                .map(value -> value.getDaysNeedToCompleted())
                .collect(Collectors.toList());


        projectNeedDays = NotCompletedWork.stream().map(value -> value.longValue()).filter(a -> a != null).mapToLong(a -> a).sum();

        return projectNeedDays;

    }


}




