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
import ru.sfedu.constrcaclconsol.Constants;
import ru.sfedu.constrcaclconsol.bean.*;
import ru.sfedu.constrcaclconsol.utils.ConfigurationUtil;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import static ru.sfedu.constrcaclconsol.Constants.*;
import static ru.sfedu.constrcaclconsol.utils.ConfigurationUtil.getConfigurationEntry;


public class DataProviderCsv implements DataProvider{

    private static Logger log = LogManager.getLogger(DataProviderCsv.class);

    //Read
    private <T> CSVReader getCsvFileToRead(Class<T> tClass) throws IOException {

        File file = new File(getConfigurationEntry(PATH_CSV) + tClass.getSimpleName().toLowerCase()
                + getConfigurationEntry(FILE_EXTENSION_CSV));

        if (!file.exists()) {

            if (!file.createNewFile()) {

                throw new IOException(getConfigurationEntry(CANNOT_CREATE_FILE));
            }
        }

        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        return new CSVReader(bufferedReader);
    }

    private <T> List<T> getDataFromCsv(Class<T> tClass) throws IOException {

        List<T> tList;

        try {

            CSVReader csvReader = getCsvFileToRead(tClass);

            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(csvReader)
                    .withType(tClass)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            tList = csvToBean.parse();
            csvReader.close();

            log.debug(getConfigurationEntry(GET_FROM_CSV));

        } catch (IOException e) {

            log.error(e);

            throw e;
        }

        return tList;
    }

    //Write
    private <T> boolean insertListIntoCsv(List<T> listClass) throws IOException {

        try{

            FileWriter writer = new FileWriter(getConfigurationEntry(PATH_CSV)
                    + listClass.get(0).getClass().getSimpleName().toLowerCase()
                    + getConfigurationEntry(FILE_EXTENSION_CSV));

            CSVWriter csvWriter = new CSVWriter(writer);

            StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(csvWriter)
                    .withApplyQuotesToAll(false)
                    .build();
            beanToCsv.write(listClass);
            csvWriter.close();

            log.debug(getConfigurationEntry(Constants.WRITE_TO_CSV));

            return true;

        }catch (IndexOutOfBoundsException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e){

            log.error(e);

            return false;

        }
    }

    private <T> boolean writeToCsv (Class<?> tClass, List<T> object, boolean overwrite) throws IOException {

        List<T> fileObjectList;

        if (!overwrite) {

            fileObjectList = (List<T>) getDataFromCsv(tClass);

            fileObjectList.addAll(object);
        }
        else {

            fileObjectList = new ArrayList<>(object);
        }

        CSVWriter csvWriter;

        try {

            FileWriter writer = new FileWriter(ConfigurationUtil.getConfigurationEntry(Constants.PATH_CSV)
                    + tClass.getSimpleName().toLowerCase()
                    + ConfigurationUtil.getConfigurationEntry(Constants.FILE_EXTENSION_CSV));

            csvWriter = new CSVWriter(writer);

            StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(csvWriter)
                    .withApplyQuotesToAll(false)
                    .build();
            beanToCsv.write(fileObjectList);
            csvWriter.close();

            log.debug( ConfigurationUtil.getConfigurationEntry(WRITE_TO_CSV));

            log.info( ConfigurationUtil.getConfigurationEntry(WRITE_SUCCESS));

            return true;

        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException | IOException e ) {

            log.info( ConfigurationUtil.getConfigurationEntry(WRITE_FAIL));

            log.error(e);

            return false;
        }
    }

    private <T> boolean writeToCsv (T object) throws IOException {

        if (object == null) {

            log.error(getConfigurationEntry(NULL));

            return false;
        }

        return writeToCsv(object.getClass(), Collections.singletonList(object), false);
    }


    /** Create the List of objects of the Materials class type
     * @param work the object of the class of Works to get a list of materials for
     * @return List of objects of the Materials class type
     * @throws IOException
     */
    private <T> List<Materials> getMaterialList( T work) throws IOException {

        try {

           List<Materials> materialList;

           Works works = (Works) work;

           materialList = works.getListMaterials();

           List<Materials> materialsList = getDataFromCsv(Materials.class);

           List<Long> idMaterialInWorks;

            idMaterialInWorks = materialList
                    .stream()
                    .map(Materials::getId)
                    .collect(Collectors.toList());

            List<Materials>workListInProject;

            workListInProject = materialsList.stream()
                    .filter(worksInProject -> idMaterialInWorks
                            .stream()
                            .anyMatch(workInProject -> workInProject.longValue() ==  worksInProject.getId()))
                    .collect(Collectors.toList());

            log.debug(getConfigurationEntry(GET_MATERIAL_LIST));

            return workListInProject;

        }catch(IOException e){

            log.error(e);

            return new ArrayList<>();
        }
    }

    /** Create the List of objects of the Works class type
     * @param projectObject the object of the class of Project to get a list of works for
     * @return List of objects of the Works class type
     * @throws IOException
     */
    private <T> List<Works> getWorksList(T projectObject) throws IOException {

        try {

            List<Works> objectWorkList;

            Project project = (Project) projectObject;

            objectWorkList = project.getWorksList();

            List<Works> workList = getDataFromCsv(Works.class);

            List<Works> workListInProject;

            List<Long> listIdWorksInProject;
            List<Works> worksInProjectWithMaterials = new ArrayList<>();

            listIdWorksInProject = objectWorkList
                    .stream()
                    .map(Works::getId)
                    .collect(Collectors.toList());

            List<Long> finalListIdWorksInProject = listIdWorksInProject;

            workListInProject = workList.stream()
                    .filter(works -> finalListIdWorksInProject
                            .stream()
                            .anyMatch(workInProject -> workInProject.longValue() ==  works.getId()))
                    .collect(Collectors.toList());


            workListInProject
                    .forEach(x -> worksInProjectWithMaterials.add(getWork(x.getId()).get()));

            log.debug(getConfigurationEntry(GET_WORKS_LIST));

            return worksInProjectWithMaterials;

        }catch(IOException e){

            log.error(e);

            return new ArrayList<>();
        }
    }

    /** Create the object of the Customer class type
     * @param customerObject the object of the class of Project to get a class object
     * @return objects of the People class type
     * @throws IOException
     */
    private <T> People getCustomerInProject(T customerObject) throws IOException {

        Customer customerInProject;

        Project project = (Project) customerObject;

        People customer = project.getCustomer();

        List<Customer> customerList = getDataFromCsv(Customer.class);

        customerInProject = customerList
                .stream()
                .filter(x -> x.getId().equals(customer.getId()))
                .findAny()
                .orElse(null);

        log.debug(getConfigurationEntry(GET_CUSTOMER_IN_PROJECT));

        return customerInProject;

    }

    /** Create the object of the Executor class type
     * @param executorObject the object of the class of Project to get a class object
     * @return objects of the People class type
     * @throws IOException
     */
    private <T> People getExecutorInProject(T executorObject) throws IOException {

        Executor executorInProject;
        Project project = (Project) executorObject;

        People executor = project.getExecutor();

        List<Executor> executorList = getDataFromCsv(Executor.class);
        executorInProject = executorList
                .stream()
                .filter(x -> x.getId().equals(executor.getId()))
                .findAny()
                .orElse(null);

        log.debug(getConfigurationEntry(GET_EXECUTOR_IN_PROJECT));

        return executorInProject;

    }

    private long getNextCustomerId() {

        try {

            List<Customer> customerList = getDataFromCsv(Customer.class);


            final long[] maxId = {-1};

            customerList
                    .forEach(
                            customer -> {
                                if (maxId[0] < customer.getId()) {
                                    maxId[0] = customer.getId();
                                }
                            }
                    );

            return maxId[0] +1;

        }catch (NullPointerException | IOException e){

            log.error(e);

            return 0;
        }
    }

    private long getNextExecutorId() {

        try {

            List<Executor> executorList = getDataFromCsv(Executor.class);

            final long[] maxId = {-1};

            executorList
                    .forEach(
                            customer -> {
                                if (maxId[0] < customer.getId()) {
                                    maxId[0] = customer.getId();
                                }
                            }
                    );

            return maxId[0] +1;

        }catch (NullPointerException | IOException e){

            log.error(e);

            return 0;
        }
    }

    private long getNextProjectId() {

        try {

            List<Project> projectList = getDataFromCsv(Project.class);

            final long[] maxId = {-1};

            projectList
                    .forEach(
                            customer -> {
                                if (maxId[0] < customer.getId()) {
                                    maxId[0] = customer.getId();
                                }
                            }
                    );

            return maxId[0] +1;

        }catch (NullPointerException | IOException e){

            log.error(e);

            return 0;
        }
    }

    private <T> boolean checkEmptyList(List<T> list) throws IOException {

        if (list.isEmpty()) {

            log.debug(getConfigurationEntry(LIST_IS_EMPTY));

            return false;

        } else{

            return true;
        }
    }

    private <T> boolean checkNull(T object) throws IOException{
        if (object == null) {

            log.debug(getConfigurationEntry(NULL_VALUE));

            return true;

        } else{

            return false;
        }
    }



    //CRUD MATERIALS

    @Override
    public boolean createMaterials(List<Materials> listMaterials) throws IOException {

        log.debug(getConfigurationEntry(CREATE_MATERIALS));

        try
        {
            if (checkEmptyList(listMaterials)){

                return insertListIntoCsv(listMaterials);

            }

            else
            {
                return false;
            }

        }catch (NullPointerException e){

            log.error(e);

            return false;
        }

    }

    @Override
    public Optional<Materials> getMaterialById(long materialId) throws IOException {

        List<Materials> listMaterials = getDataFromCsv(Materials.class);

        try {

            Materials materials = listMaterials.stream()
                    .filter(el -> el.getId() == materialId)
                    .findFirst().get();

            log.debug(getConfigurationEntry(GET_MATERIALS));

            return Optional.of(materials);

        } catch (NoSuchElementException e) {

            log.error(e);

            return Optional.empty();
        }

    }

    @Override
    public boolean deleteMaterialById(long id) throws IOException {

        List<Materials> listMaterials = getDataFromCsv(Materials.class);

        try {

            Materials materials = listMaterials.stream()
                    .filter(el -> el.getId() == id)
                    .findFirst().get();

            listMaterials.remove(materials);

            writeToCsv(Materials.class, listMaterials,true);

            log.debug(getConfigurationEntry(DELETE_MATERIALS));

            return true;

        } catch (NoSuchElementException | IndexOutOfBoundsException e) {

            log.error(e);

            return false;

        }

    }

    @Override
    public boolean updateMaterial(long id, String name, Long number, Long totalPrice) throws IOException {

        List<Materials> listMaterials = getDataFromCsv(Materials.class);

        int newId = Integer.parseInt(String.valueOf(id))-1;

        try {

            Materials newMaterials = new Materials();

            newMaterials.setId(id);
            newMaterials.setName(name);
            newMaterials.setNumber(number);
            newMaterials.setPriceForOne(totalPrice);

            listMaterials.set(newId,newMaterials);

            writeToCsv(Materials.class, listMaterials,true);

            log.debug(getConfigurationEntry(UPDATE_MATERIALS));

            return true;

        } catch (NoSuchElementException | IndexOutOfBoundsException e) {

            log.error(e);

            return false;

        }

    }


    //CRUD WORKS
    @Override
    public boolean createWork(List<Works> listWorks) throws IOException {

        log.debug(getConfigurationEntry(CREATE_WORKS));

        try
        {
            if (checkEmptyList(listWorks)){

            return insertListIntoCsv(listWorks);

            }
            else
            {
                return false;
            }

        }catch (NullPointerException e){

            log.error(e);

            return false;
        }

    }

    @Override
    public Optional<Works> getWork(long id) {

        try {

            List<Works> workList = getDataFromCsv(Works.class);

            var optionalWorks = workList.stream()
                    .filter(task -> task.getId() == id)
                    .findAny();

            if (optionalWorks.isEmpty()) {
                return optionalWorks;
            }

            var works = optionalWorks.get();

            works.setListMaterials(getMaterialList(works));

            log.debug(getConfigurationEntry(GET_WORKS));

            return Optional.of(works);

        } catch (IOException | NoSuchElementException e) {

            log.error(e);

            return Optional.empty();
        }
    }

    @Override
    public boolean deleteWorkById(long id) throws IOException {

        List<Works> listWorks = getDataFromCsv(Works.class);

        try {

            Works works = listWorks.stream()
                    .filter(el -> el.getId() == id)
                    .findFirst().get();

            listWorks.remove(works);

            writeToCsv(Works.class, listWorks,true);

            log.debug(getConfigurationEntry(DELETE_WORKS));

            return true;

        } catch (NoSuchElementException | IndexOutOfBoundsException e) {

            log.error(e);

            return false;

        }
    }

    @Override
    public boolean updateWork(Long id, String name, Long price, String priority,
                              Long daysToCompletedWorks, String status, List<Materials> listMaterials ) throws IOException {

        List<Works> listWorks = getDataFromCsv(Works.class);

        int newId = Integer.parseInt(String.valueOf(id))-1;

        try {

            Works newWorks = new Works();

            newWorks.setId(id);
            newWorks.setName(name);
            newWorks.setPrice(price);
            newWorks.setPriority(priority);
            newWorks.setNeedDaysToCompleted(daysToCompletedWorks);
            newWorks.setStatus(status);
            newWorks.setListMaterials(listMaterials);

            listWorks.set(newId,newWorks);

            writeToCsv(Works.class, listWorks,true);

            log.debug(getConfigurationEntry(UPDATE_WORKS));

            return true;

        } catch (NoSuchElementException | IndexOutOfBoundsException e) {

            log.error(e);

            return false;

        }

    }


    //CRUD CUSTOMER
    @Override
    public boolean createCustomer(String name, String surname, String mailbox,String telephone) throws IOException {

        log.debug(getConfigurationEntry(CREATE_CUSTOMER));

        try
        {
            if ( checkNull(name) || checkNull(surname) || checkNull(mailbox) || checkNull(telephone))
            {
                return false;
            }
            else
            {
                Customer customer = new Customer();
                customer.setId(getNextCustomerId());
                customer.setName(name);
                customer.setSurname(surname);
                customer.setMailbox(mailbox);
                customer.setTelephone(telephone);

                return writeToCsv(customer);
            }

        }catch (NullPointerException e){

            log.error(e);

            return false;
        }

    }

    @Override
    public Optional<Customer> getCustomerById(long id) throws IOException {

        List<Customer> listCustomer = getDataFromCsv(Customer.class);

        try {

            Customer customer = listCustomer.stream()
                    .filter(el -> el.getId() == id)
                    .findFirst().get();

            log.debug(getConfigurationEntry(GET_CUSTOMER));

            return Optional.of(customer);

        } catch (NoSuchElementException e) {

            log.error(e);

            return Optional.empty();
        }

    }

    @Override
    public boolean deleteCustomerById(long id) throws IOException {

        List<Customer> listCustomer = getDataFromCsv(Customer.class);

        try {

            Customer customer = listCustomer.stream()
                    .filter(el -> el.getId() == id)
                    .findFirst().get();

            listCustomer.remove(customer);

            writeToCsv(Customer.class, listCustomer,true);

            log.debug(getConfigurationEntry(DELETE_CUSTOMER));

            return true;

        } catch (NoSuchElementException e) {

            log.error(e);

            return false;

        }

    }

    @Override
    public boolean updateCustomer(long id, String name, String surname, String mailbox, String telephone ) throws IOException {

        List<Customer> listCustomer = getDataFromCsv(Customer.class);
        Customer customer;

        customer = listCustomer
                .stream()
                .filter(value -> value.getId() == id)
                .findAny()
                .orElse(null);

        try {

            int idPosition = listCustomer.indexOf(customer);

            Customer newCustomer = new Customer();

            newCustomer.setId(id);
            newCustomer.setName(name);
            newCustomer.setSurname(surname);
            newCustomer.setMailbox(mailbox);
            newCustomer.setTelephone(telephone);
            listCustomer.set(idPosition,newCustomer);

            writeToCsv(Customer.class, listCustomer,true);

            log.debug(getConfigurationEntry(UPDATE_CUSTOMER));

            return true;

        } catch (NoSuchElementException | IndexOutOfBoundsException e) {

            log.error(e);

            return false;

        }
    }


    //CRUD EXECUTOR
    @Override
    public boolean createExecutor(String name, String surname, String mailbox, Long numberOfCompletedProjects, Long numberOfWorkers) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

        log.debug(getConfigurationEntry(CREATE_EXECUTOR));

        try
        {
            if ( checkNull(name) || checkNull(surname) || checkNull(mailbox) || checkNull(numberOfCompletedProjects) || checkNull(numberOfWorkers))
            {
                log.debug(Constants.NULL_VALUE);

                return false;
            }
            else
            {
                Executor executor = new Executor();
                executor.setId(getNextExecutorId());
                executor.setName(name);
                executor.setSurname(surname);
                executor.setMailbox(mailbox);
                executor.setNumberOfCompletedProjects(numberOfCompletedProjects);
                executor.setNumberOfWorkers(numberOfWorkers);

                return writeToCsv(executor);
            }

        }catch (NullPointerException e){

            log.error(e);

            return false;
        }

    }

    @Override
    public Optional<Executor> getExecutorById(long id) throws IOException {

        List<Executor> listExecutor = getDataFromCsv(Executor.class);

        try {

            Executor executor = listExecutor.stream()
                    .filter(el -> el.getId() == id)
                    .findFirst().get();

            log.debug(getConfigurationEntry(GET_EXECUTOR));

            return Optional.of(executor);

        } catch (NoSuchElementException e) {

            log.error(e);

            return Optional.empty();
        }

    }

    @Override
    public boolean deleteExecutorById(long id) throws IOException {

        List<Executor> listExecutor = getDataFromCsv(Executor.class);

        try {

            Executor executor = listExecutor.stream()
                    .filter(el -> el.getId() == id)
                    .findFirst().get();

            listExecutor.remove(executor);

            writeToCsv(Executor.class, listExecutor,true);

            log.debug(getConfigurationEntry(DELETE_EXECUTOR));

            return true;

        } catch (NoSuchElementException e) {

            log.error(e);

            return false;

        }

    }

    @Override
    public boolean updateExecutor(long id, String name, String surname, String mailbox, Long numberOfCompletedProject, Long numberOfWorkers) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

        List<Executor> listExecutor = getDataFromCsv(Executor.class);
        Executor executor;

        executor= listExecutor
                .stream()
                .filter(value -> value.getId() == id)
                .findAny()
                .orElse(null);

        try {

            int idPosition = listExecutor.indexOf(executor);

            Executor newExecutor = new Executor();

            newExecutor.setId(id);
            newExecutor.setName(name);
            newExecutor.setSurname(surname);
            newExecutor.setMailbox(mailbox);
            newExecutor.setNumberOfCompletedProjects(numberOfCompletedProject);
            newExecutor.setNumberOfWorkers(numberOfWorkers);

            listExecutor.set(idPosition,newExecutor);

            writeToCsv(Executor.class, listExecutor,true);

            log.debug(getConfigurationEntry(Constants.UPDATE_EXECUTOR));

            return true;

        } catch (NoSuchElementException | IndexOutOfBoundsException e) {

            log.error(e);

            return false;
        }
    }


    //CRUD PROJECT
    @Override
    public boolean createProject(String name, String createdDate,
                                 String deadline, Integer numberOfWorkers, List<Works> worksList, String address,
                                 People executor, People customer ) throws IOException {

        log.debug(getConfigurationEntry(CREATE_PROJECT));

        try
        {
            if ( checkNull(name) || checkNull(createdDate) || checkNull(deadline) || checkNull(numberOfWorkers ) ||
                    checkNull(worksList ) || checkNull(address ) || checkNull(executor ) || checkNull(customer ) )
            {
                log.debug(Constants.NULL_VALUE);

                return false;
            }
            else
            {
                Project project = new Project();

                project.setId(getNextProjectId());
                project.setName(name);
                project.setCreatedDate(createdDate);
                project.setDeadline(deadline);
                project.setNumberOfWorkers(numberOfWorkers);
                project.setWorksList(worksList);
                project.setProjectAddress(address);
                project.setExecutor(executor);
                project.setCustomer(customer);

                return writeToCsv(project);
            }

        }catch (NullPointerException e){

            log.error(e);

            return false;
        }

    }

    @Override
    public Optional<Project> getProject(long id) {
        try {

            List<Project> projectList = getDataFromCsv(Project.class);

            var optionalProject = projectList.stream()
                    .filter(task -> task.getId() == id)
                    .findAny();

            if (optionalProject.isEmpty()) {
                return optionalProject;
            }

            var project = optionalProject.get();

            project.setWorksList(getWorksList(project));
            project.setCustomer(getCustomerInProject(project));
            project.setExecutor(getExecutorInProject(project));

            log.debug(getConfigurationEntry(GET_PROJECT));

            return Optional.of(project);

        } catch (IOException e) {

            log.error(e);

            return Optional.empty();
        }
    }


    @Override
    public boolean updateProject(Long id, String name, String createdDate,
                                 String deadline, Integer numberOfWorkers, List<Works> worksList, String address,
                                 People executor, People customer ) throws IOException {

        List<Project> listProject = getDataFromCsv(Project.class);

        int newId = Integer.parseInt(String.valueOf(id))-1;

        try {

            Project newProject = new Project();

            newProject.setId(id);
            newProject.setName(name);
            newProject.setCreatedDate(createdDate);
            newProject.setDeadline(deadline);
            newProject.setNumberOfWorkers(numberOfWorkers);
            newProject.setWorksList(worksList);
            newProject.setProjectAddress(address);
            newProject.setExecutor(executor);
            newProject.setCustomer(customer);

            listProject.set(newId,newProject);

            writeToCsv(Project.class,listProject,true);

            log.debug(getConfigurationEntry(UPDATE_PROJECT));

            return true;

        } catch (NoSuchElementException | IndexOutOfBoundsException e) {

            log.error(e);

            return false;

        }

    }


//Other Methods
    @Override
    public boolean deleteProject(long id, boolean createReport) throws IOException {

    List<Project> listProject = getDataFromCsv(Project.class);

    try {

        Project project = listProject.stream()
                .filter(el -> el.getId() == id)
                .findFirst().get();

        if (createReport)
        {
            log.debug(createReportAboutProject(id));
        }

        listProject.remove(project);

        writeToCsv(Project.class,listProject,true);

        log.debug(getConfigurationEntry(Constants.DELETE_PROJECT));

        return true;

    } catch (NoSuchElementException | IndexOutOfBoundsException e) {

        log.error(e);

        return false;
    }


}

    @Override
    public StringBuffer createReportAboutProject(long idProject)throws IOException{

        Project projectForReport = getProject(idProject).get();

        StringBuffer reportAboutProject = new StringBuffer();

        reportAboutProject

                .append("\n" + getConfigurationEntry(Constants.PROJECT_REPORT) + "\n" )
                .append(getConfigurationEntry(Constants.ID) + Constants.SPACE + projectForReport.getId() + "\n")
                .append(getConfigurationEntry(Constants.NAME) + Constants.SPACE + projectForReport.getName() + "\n")
                .append(getConfigurationEntry(Constants.CREATED_DATE) + Constants.SPACE + projectForReport.getCreatedDate()+ "\n")
                .append(getConfigurationEntry(Constants.DEADLINE) + Constants.SPACE + projectForReport.getDeadline() + "\n")
                .append(getConfigurationEntry(Constants.NUMBER_OF_WORKS) + Constants.SPACE + projectForReport.getNumberOfWorkers() + "\n")
                .append(getConfigurationEntry(Constants.WORKS_LIST) + Constants.SPACE + projectForReport.getWorksList() + "\n")
                .append(getConfigurationEntry(Constants.PROJECT_ADDRESS) + Constants.SPACE + projectForReport.getProjectAddress() + "\n")
                .append(getConfigurationEntry(Constants.CUSTOMER) + Constants.SPACE + projectForReport.getCustomer() + "\n")
                .append(getConfigurationEntry(Constants.EXECUTOR) + Constants.SPACE + projectForReport.getExecutor() + "\n");

        return reportAboutProject;
    }

    @Override
    public StringBuffer createReportAboutEstimate(long workPrice, long materialPrice, long totalPrice)throws Exception{

        StringBuffer reportAboutProject = new StringBuffer();

        reportAboutProject
                .append("\n"+ getConfigurationEntry(Constants.ESTIMATE_REPORT) +"\n")
                .append(getConfigurationEntry(Constants.PRICE_FOR_WORKS) + Constants.SPACE + workPrice + "\n")
                .append(getConfigurationEntry(Constants.PRICE_FOR_MATERIALS) + Constants.SPACE + materialPrice + "\n")
                .append(getConfigurationEntry(Constants.ESTIMATE_TOTAL_PRICE) + Constants.SPACE + totalPrice +  "\n");

        return  reportAboutProject;
    }

    @Override
    public StringBuffer createDeadlineReport(Long idProject, Long needDays) throws IOException {
        Project project;

        project = getProject(idProject).get();

        String startDate = project.getCreatedDate();
        String endDate = project.getDeadline();

        StringBuffer deadlineReport = new StringBuffer();

        deadlineReport
                .append("\n"+ getConfigurationEntry(Constants.DEADLINE_REPORT) +"\n")
                .append(getConfigurationEntry(Constants.NEED_DAYS) + SPACE + needDays +"\n")
                .append(Constants.START_DAY + Constants.SPACE + startDate + Constants.SPACE + Constants.END_DAY + Constants.SPACE + endDate);

        return deadlineReport;

    }

    @Override
    public Long GetTheCostOfWorksInProject(long idProject)  throws IOException{

       try {
           Project project;

           project = getProject(idProject).get();

           Long projectPriceForWorks;

           List<Works> worksList;
           worksList = getWorksList(project);

           List<Long> priceForWorks;

           priceForWorks = worksList
                   .stream()
                   .map(value -> value.getPrice())
                   .collect(Collectors.toList());

           projectPriceForWorks = priceForWorks
                   .stream()
                   .map(value -> value.longValue())
                   .filter(a -> a != null)
                   .mapToLong(a -> a).sum();

           log.debug(getConfigurationEntry(Constants.GET_THE_COST_OF_WORKS));

           return projectPriceForWorks;
       }
       catch (IOException e)
       {
           log.error(e);
           return null;
       }

    }

    @Override
    public Long GetTheCostOfMaterialsInProject(long idProject)  throws IOException{
       try {
           Project project;

           project = getProject(idProject).get();

           Long projectPriceForMaterials;
           List<Long> priceForMaterials;

           List<Works> worksList;
           worksList = getWorksList(project);

           List<Materials> materilsListInWorks;

           //Список всех материалов в работах
           materilsListInWorks = worksList.stream()
                   .flatMap(value -> value.getListMaterials().stream())
                   .collect(Collectors.toList());

           priceForMaterials = materilsListInWorks
                   .stream()
                   .map(value -> value.getPriceForOne() * value.getNumber())
                   .collect(Collectors.toList());

           projectPriceForMaterials = priceForMaterials
                   .stream()
                   .map(value -> value.longValue())
                   .filter(a -> a != null)
                   .mapToLong(a -> a).sum();

           log.debug(getConfigurationEntry(Constants.GET_THE_COST_OF_MATERIALS));

           return projectPriceForMaterials;

       }catch(IOException e){

           log.error(e);
           return null;
       }
    }

    @Override
    public Long calculatingEstimate(Long idProject, boolean createReport) throws Exception {
        try{

            Long projectPriceForWorks;
            Long projectPriceForMaterials;
            Long projectPrice;

            projectPriceForWorks = GetTheCostOfWorksInProject(idProject);
            projectPriceForMaterials = GetTheCostOfMaterialsInProject(idProject);

            projectPrice = projectPriceForWorks+projectPriceForMaterials;

            if (createReport) {

                log.info(createReportAboutEstimate(projectPriceForWorks, projectPriceForMaterials, projectPrice ));
            }

            log.debug(getConfigurationEntry(Constants.CALCULATING_ESTIMATE));

            return projectPrice;

        } catch(IOException e){

            log.error(e);

            return null;

        }
    }

    @Override
    public Long calculatingDeadline(Long idProject, boolean createReport) {
       try {
           Project project;

           project = getProject(idProject).get();

           Long projectNeedDays;


           List<Works> worksList;
           worksList = getWorksList(project);
           List<Long> priceForWorks;

           priceForWorks = worksList
                   .stream()
                   .map(value -> value.getNeedDaysToCompleted())
                   .collect(Collectors.toList());

           projectNeedDays = priceForWorks.stream().map(value -> value.longValue()).filter(a -> a != null).mapToLong(a -> a).sum();

           if (createReport) {
               log.info(createDeadlineReport(idProject, projectNeedDays));

           }

           log.debug(getConfigurationEntry(Constants.CALCULATING_DEADLINE));

           return projectNeedDays;

       }catch(IOException e)
       {
           log.error(e);

           return null;
       }
    }

    @Override
    public boolean markTheStatusOfWork (Long idWork, String status) {
    try {
        List<Works> listWorks = getDataFromCsv(Works.class);

        int newId = (int) (idWork - 1);

        Works newWorks;

        newWorks = getWork(idWork).get();

        newWorks.setStatus(status);

        listWorks.set(newId, newWorks);

        createWork(listWorks);

        log.debug(getConfigurationEntry(Constants.MARK_THE_STATUS_OF_WORK));

        return true;

    }catch (IOException e)
    {
        log.error(e);
        return false;
    }

    }

    @Override
    public HashMap<Integer, String> getProgressReport(Long idProject) {
        try {
            Project project;

            project = getProject(idProject).get();

            List<Works> worksList;
            worksList = getWorksList(project);
            HashMap<Integer, String> workAndStatus = new HashMap<>();

            for (int i = 0; i < worksList.size(); i++) {
                workAndStatus.put(Math.toIntExact(worksList.get(i).getId()), worksList.get(i).getStatus());
            }

            log.debug(getConfigurationEntry(Constants.GET_THE_PROGRESS_REPORT));

            return workAndStatus;

        } catch (IOException e) {

            log.error(e);

            return null;
        }
    }

    @Override
    public Long getTheRemainingTimeToComplete(Long idProject) {
       try {
           Project project;

           project = getProject(idProject).get();

           List<Works> worksList;
           worksList = getWorksList(project);

           List<Long> NotCompletedWork;
           List<Works> worksListNotCompleted;

           worksListNotCompleted = worksList
                   .stream()
                   .filter(value -> value.getStatus().equals(CREATE))
                   .collect(Collectors.toList());

           NotCompletedWork = worksListNotCompleted
                   .stream()
                   .map(value -> value.getNeedDaysToCompleted())
                   .collect(Collectors.toList());


           Long projectNeedDays = NotCompletedWork.stream().map(value -> value.longValue()).filter(a -> a != null).mapToLong(a -> a).sum();

           log.debug(getConfigurationEntry(Constants.GET_THE_REMAINING_TIME));

           return projectNeedDays;
       }
       catch(IOException e)
       {
           log.error(e);
           return null;
       }
    }

}




