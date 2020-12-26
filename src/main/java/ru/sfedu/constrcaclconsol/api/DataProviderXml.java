package ru.sfedu.constrcaclconsol.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import ru.sfedu.constrcaclconsol.Constants;
import ru.sfedu.constrcaclconsol.bean.*;
import ru.sfedu.constrcaclconsol.utils.ConfigurationUtil;
import ru.sfedu.constrcaclconsol.utils.WrapperXML;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static ru.sfedu.constrcaclconsol.Constants.*;
import static ru.sfedu.constrcaclconsol.utils.ConfigurationUtil.getConfigurationEntry;

public class DataProviderXml implements DataProvider {

    private static Logger log = LogManager.getLogger(DataProviderXml.class);



    /**
     * Запись в xml файл
     * @param listClass список объектов класса
     * @return успешний или не успешный результат в виде переменной типа boolean
     * @throws IOException
     */
    public <T> boolean insertListIntoXml(List<T> listClass) throws Exception {
        try {

            String path = ConfigurationUtil.getConfigurationEntry(Constants.PATH_XML) + listClass.get(0).getClass().getSimpleName().toLowerCase() + ConfigurationUtil.getConfigurationEntry(Constants.FILE_EXTENSION_XML);
            log.info(path);
            File targetFile = new File(ConfigurationUtil.getConfigurationEntry(Constants.PATH_XML), listClass.get(0).getClass().getSimpleName().toLowerCase() + ConfigurationUtil.getConfigurationEntry(Constants.FILE_EXTENSION_XML));
            //File file = new File (path);
            // file.createNewFile();
          /*  if (targetFile.createNewFile()) {
                System.out.println(path + " Файл создан");
            }**/
            // (new File(path)).createNewFile();
            // (new File(//  FileWriter writer = new FileWriter(ConfigurationUtil.getConfigurationEntry(Constants.PATH_XML)+ listClass.get(0).getClass().getSimpleName().toLowerCase()+ConfigurationUtil.getConfigurationEntry(Constants.FILE_EXTENSION_XML));
            FileWriter writer = new FileWriter(path, false);
            Serializer serializer = new Persister();

            //Определяем контейнер, в котором будут находиться все объекты
            WrapperXML xml = new WrapperXML();
            //Записываем список объектов в котнейнер
            xml.setList(listClass);

            //Записываем в файл
            serializer.write(xml, writer);

            return true;


        } catch (IndexOutOfBoundsException e) {

            log.error(e);

            return false;
        }
    }

    public <T> boolean writeToXml(Class<?> tClass, List<T> object, boolean overwrite) throws Exception {
        List<T> fileObjectList;
        if (!overwrite) {
            fileObjectList = (List<T>) readFromXml(tClass);
            fileObjectList.addAll(object);
        } else {
            fileObjectList = new ArrayList<>(object);
        }
        try {

            log.info(this.getFilePath(tClass));

            (new File(this.getFilePath(tClass))).createNewFile();
            FileWriter writer = new FileWriter(this.getFilePath(tClass), false);
            Serializer serializer = new Persister();

            WrapperXML xml = new WrapperXML();
            xml.setList(fileObjectList);
            serializer.write(xml, writer);

            log.debug(getConfigurationEntry(WRITE_SUCCESS));

            return true;
        } catch (IndexOutOfBoundsException e) {
            log.error(e);
            return false;
        }
    }

    /**
     * Получение данных из xml файла
     * @param cl
     * @param <T>
     * @return
     * @throws IOException
     */
    public <T> List<T> readFromXml(Class cl) throws Exception {
        try {

            FileReader fileReader = new FileReader(this.getFilePath(cl));

            Serializer serializer = new Persister();

            WrapperXML xml = serializer.read(WrapperXML.class, fileReader);

            if (xml.getList() == null) xml.setList(new ArrayList<T>());

            return xml.getList();
        } catch (IOException e) {

            log.error(e);

            return new ArrayList<>();
        }
    }

    /**
     * Поверка на не пустоту перед записью в файл
     * @param object
     * @param <T>
     * @return
     * @throws IOException
     */
    private <T> boolean writeToXml(T object) throws Exception {
        if (object == null) {
            log.info(ConfigurationUtil.getConfigurationEntry(NULL));
            return false;
        }
        return writeToXml(object.getClass(), Collections.singletonList(object), false);
    }

    /**
     * Получение пути файла
     * @param cl
     * @return
     * @throws IOException
     */
    private String getFilePath(Class cl) throws IOException {
        return ConfigurationUtil.getConfigurationEntry(Constants.PATH_XML) + cl.getSimpleName().toLowerCase() + ConfigurationUtil.getConfigurationEntry(Constants.FILE_EXTENSION_XML);
    }

    /** Создает список материалов, относящиеся к конкретной работе
     * @param work объект класса Works
     * @return Список всех найденных материалов
     * @throws IOException
     */
    private <T> List<Materials> getMaterialList( T work) throws Exception {

        try {

            List<Materials> materialList;

            Works works = (Works) work;

            materialList = works.getListMaterials();

            List<Materials> materialsList = readFromXml(Materials.class);

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

            log.info(getConfigurationEntry(GET_MATERIAL_LIST));
            log.debug(workListInProject);

            return workListInProject;

        }catch(IOException e){

            log.error(e);

            return new ArrayList<>();
        }
    }

    /** Создает список объектов
     * @param projectObject - объект класса Project для получения списка работ, которые к нему относятся
     * @return список работ
     * @throws IOException
     */
    private <T> List<Works> getWorksList(T projectObject) {

        try {

            List<Works> objectWorkList;

            Project project = (Project) projectObject;

            objectWorkList = project.getWorksList();

            List<Works> workList = readFromXml(Works.class);

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
                    .forEach(x -> {
                        try {
                            worksInProjectWithMaterials.add(getWork(x.getId()).get());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });


            log.info(getConfigurationEntry(GET_WORKS_LIST));
            log.debug(workListInProject);

            return worksInProjectWithMaterials;

        }catch(Exception e){

            log.error(e);

            return new ArrayList<>();
        }
    }

    /** Получение экземпляра класса Customer
     * @param customerObject  - объект класса, который связан с конкретным проектом
     * @return экземпляр класса типа People
     * @throws IOException
     */
    private <T> People getCustomerInProject(T customerObject) throws Exception {

        Customer customerInProject;

        Project project = (Project) customerObject;

        People customer = project.getCustomer();

        List<Customer> customerList = readFromXml(Customer.class);

        customerInProject = customerList
                .stream()
                .filter(x -> x.getId().equals(customer.getId()))
                .findAny()
                .orElse(null);

        log.info(getConfigurationEntry(GET_CUSTOMER_IN_PROJECT));
        log.debug(customer);

        return customerInProject;

    }

    /** Получение экземпляра класса Executor
     * @param executorObject - объект класса, который связан с конкретным проектом
     * @return экземпляр класса типа People
     * @throws IOException
     */
    private <T> People getExecutorInProject(T executorObject) throws Exception {

        Executor executorInProject;
        Project project = (Project) executorObject;

        People executor = project.getExecutor();

        List<Executor> executorList = readFromXml(Executor.class);
        executorInProject = executorList
                .stream()
                .filter(x -> x.getId().equals(executor.getId()))
                .findAny()
                .orElse(null);

        log.info(getConfigurationEntry(GET_EXECUTOR_IN_PROJECT));
        log.debug(executor);


        return executorInProject;

    }

    /**
     * Получение следующего id для Customer
     * @return id
     */
    private long getNextCustomerId() throws Exception {

        try {

            List<Customer> customerList = readFromXml(Customer.class);


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

    /**
     * Получение следующего id для Executor
     * @return id
     */
    private long getNextExecutorId() {

        try {

            List<Executor> executorList = readFromXml(Executor.class);

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

        }catch (Exception e){

            log.error(e);

            return 0;
        }
    }

    /**
     * Получение следующего id для Project
     * @return id
     */
    private long getNextProjectId() throws Exception {

        try {

            List<Project> projectList = readFromXml(Project.class);

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

    /**
     * Проверка списка на пустоту
     * @param list список для проверки
     * @return boolean в зависимости от результата
     * @throws IOException
     */
    private <T> boolean checkEmptyList(List<T> list) throws IOException {

        if (list.isEmpty()) {

            log.debug(getConfigurationEntry(LIST_IS_EMPTY));

            return false;

        } else{

            return true;
        }
    }

    /**
     * Проверка экземпляра на пустоту
     * @param object объект для проверки
     * @return boolean в зависимости от результата
     * @throws IOException
     */
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

        log.info(getConfigurationEntry(CREATE_MATERIALS));

        try
        {
            if (checkEmptyList(listMaterials)){
                log.debug(listMaterials);

                return insertListIntoXml(listMaterials);

            }

            else
            {
                log.error(getConfigurationEntry(NULL_VALUE));
                return false;
            }

        }catch (Exception e){

            log.error(e);

            return false;
        }

    }

    @Override
    public Optional<Materials> getMaterialById(long materialId) throws Exception {

        List<Materials> listMaterials = readFromXml(Materials.class);

        try {

            Materials materials = listMaterials.stream()
                    .filter(el -> el.getId() == materialId)
                    .findFirst().get();

            log.info(getConfigurationEntry(GET_MATERIALS));
            log.debug(Optional.of(materials));

            return Optional.of(materials);

        } catch (NoSuchElementException e) {

            log.error(e);

            return Optional.empty();
        }

    }

    @Override
    public boolean deleteMaterialById(long id) throws Exception {

        List<Materials> listMaterials = readFromXml(Materials.class);

        try {

            Materials materials = listMaterials.stream()
                    .filter(el -> el.getId() == id)
                    .findFirst().get();

            listMaterials.remove(materials);

            writeToXml(Materials.class, listMaterials,true);

            log.info(getConfigurationEntry(DELETE_MATERIALS));
            log.debug(materials);

            return true;

        } catch (NoSuchElementException | IndexOutOfBoundsException e) {

            log.error(e);

            return false;

        }

    }

    @Override
    public boolean updateMaterial(long id, String name, Long number, Long priceForOne) throws Exception {

        List<Materials> listMaterials = readFromXml(Materials.class);

        int newId = Integer.parseInt(String.valueOf(id))-1;

        try {

            Materials newMaterials = new Materials();

            newMaterials.setId(id);
            newMaterials.setName(name);
            newMaterials.setNumber(number);
            newMaterials.setPriceForOne(priceForOne);

            listMaterials.set(newId,newMaterials);

            writeToXml(Materials.class, listMaterials,true);

            log.info(getConfigurationEntry(UPDATE_MATERIALS));
            log.debug(newMaterials);

            return true;

        } catch (NoSuchElementException | IndexOutOfBoundsException e) {

            log.error(e);

            return false;

        }

    }


    //CRUD WORKS
    @Override
    public boolean createWork(List<Works> listWorks) throws Exception {

        log.info(getConfigurationEntry(CREATE_WORKS));

        try
        {
            if (checkEmptyList(listWorks)){
                log.debug(listWorks);

                return insertListIntoXml(listWorks);

            }
            else
            {
                log.error(getConfigurationEntry(NULL_VALUE));
                return false;
            }

        }catch (NullPointerException e){

            log.error(e);

            return false;
        }

    }

    @Override
    public Optional<Works> getWork(long id) throws Exception {

        try {

            List<Works> workList = readFromXml(Works.class);

            var optionalWorks = workList.stream()
                    .filter(task -> task.getId() == id)
                    .findAny();

            if (optionalWorks.isEmpty()) {
                return optionalWorks;
            }

            var works = optionalWorks.get();

            works.setListMaterials(getMaterialList(works));

            log.info(getConfigurationEntry(GET_WORKS));
            log.debug(Optional.of(works));

            return Optional.of(works);

        } catch (IOException | NoSuchElementException e) {

            log.error(e);

            return Optional.empty();
        }
    }

    @Override
    public boolean deleteWorkById(long id) throws Exception {

        List<Works> listWorks = readFromXml(Works.class);

        try {

            Works works = listWorks.stream()
                    .filter(el -> el.getId() == id)
                    .findFirst().get();

            listWorks.remove(works);

            writeToXml(Works.class, listWorks,true);

            log.info(getConfigurationEntry(DELETE_WORKS));
            log.debug(works);

            return true;

        } catch (NoSuchElementException | IndexOutOfBoundsException e) {

            log.error(e);

            return false;

        }
    }

    @Override
    public boolean updateWork(Long id, String name, Long price, String priority,
                              Long daysToCompletedWorks, String status, List<Materials> listMaterials ) throws Exception {

        List<Works> listWorks = readFromXml(Works.class);

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

            writeToXml(Works.class, listWorks,true);

            log.info(getConfigurationEntry(UPDATE_WORKS));
            log.debug(newWorks);

            return true;

        } catch (NoSuchElementException | IndexOutOfBoundsException e) {

            log.error(e);

            return false;

        }

    }


    //CRUD CUSTOMER
    @Override
    public boolean createCustomer(String name, String surname, String mailbox,String telephone) throws Exception {

        log.info(getConfigurationEntry(CREATE_CUSTOMER));

        try
        {
            if ( checkNull(name) || checkNull(surname) || checkNull(mailbox) || checkNull(telephone))
            {
                log.error(getConfigurationEntry(NULL_VALUE));
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

                log.debug(customer);

                return writeToXml(customer);
            }

        }catch (NullPointerException e){

            log.error(e);

            return false;
        }

    }

    @Override
    public Optional<Customer> getCustomerById(long id) throws Exception {

        List<Customer> listCustomer = readFromXml(Customer.class);

        try {

            Customer customer = listCustomer.stream()
                    .filter(el -> el.getId() == id)
                    .findFirst().get();

            log.info(getConfigurationEntry(GET_CUSTOMER));
            log.debug(customer);

            return Optional.of(customer);

        } catch (NoSuchElementException e) {

            log.error(e);

            return Optional.empty();
        }

    }

    @Override
    public boolean deleteCustomerById(long id) throws Exception {

        List<Customer> listCustomer = readFromXml(Customer.class);

        try {

            Customer customer = listCustomer.stream()
                    .filter(el -> el.getId() == id)
                    .findFirst().get();

            listCustomer.remove(customer);

            writeToXml(Customer.class, listCustomer,true);

            log.info(getConfigurationEntry(DELETE_CUSTOMER));
            log.debug(customer);

            return true;

        } catch (NoSuchElementException e) {

            log.error(e);

            return false;

        }

    }

    @Override
    public boolean updateCustomer(long id, String name, String surname, String mailbox, String telephone ) throws Exception {

        List<Customer> listCustomer = readFromXml(Customer.class);
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

            writeToXml(Customer.class, listCustomer,true);

            log.info(getConfigurationEntry(UPDATE_CUSTOMER));
            log.debug(newCustomer);

            return true;

        } catch (NoSuchElementException | IndexOutOfBoundsException e) {

            log.error(e);

            return false;

        }
    }


    //CRUD EXECUTOR
    @Override
    public boolean createExecutor(String name, String surname, String mailbox, Long numberOfCompletedProjects, Long numberOfWorkers) throws Exception {

        log.info(getConfigurationEntry(CREATE_EXECUTOR));

        try
        {
            if ( checkNull(name) || checkNull(surname) || checkNull(mailbox) || checkNull(numberOfCompletedProjects) || checkNull(numberOfWorkers))
            {
                log.error(Constants.NULL_VALUE);

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

                log.debug(executor);

                return writeToXml(executor);
            }

        }catch (NullPointerException e){

            log.error(e);

            return false;
        }

    }

    @Override
    public Optional<Executor> getExecutorById(long id) throws Exception {

        List<Executor> listExecutor = readFromXml(Executor.class);

        try {

            Executor executor = listExecutor.stream()
                    .filter(el -> el.getId() == id)
                    .findFirst().get();

            log.info(getConfigurationEntry(GET_EXECUTOR));
            log.debug(executor);

            return Optional.of(executor);

        } catch (NoSuchElementException e) {

            log.error(e);

            return Optional.empty();
        }

    }

    @Override
    public boolean deleteExecutorById(long id) throws Exception {

        List<Executor> listExecutor = readFromXml(Executor.class);

        try {

            Executor executor = listExecutor.stream()
                    .filter(el -> el.getId() == id)
                    .findFirst().get();

            listExecutor.remove(executor);

            writeToXml(Executor.class, listExecutor,true);

            log.info(getConfigurationEntry(DELETE_EXECUTOR));
            log.debug(executor);

            return true;

        } catch (NoSuchElementException e) {

            log.error(e);

            return false;

        }

    }

    @Override
    public boolean updateExecutor(long id, String name, String surname, String mailbox, Long numberOfCompletedProject, Long numberOfWorkers) throws Exception {

        List<Executor> listExecutor = readFromXml(Executor.class);
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

            writeToXml(Executor.class, listExecutor,true);

            log.info(getConfigurationEntry(Constants.UPDATE_EXECUTOR));
            log.debug(newExecutor);

            return true;

        } catch (NoSuchElementException | IndexOutOfBoundsException e) {

            log.error(e);

            return false;
        }
    }

    public Long priceForWorks(List<Works> worksList){

        Long projectPriceForWorks;

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

        return  projectPriceForWorks;

    }

    public Long priceForMaterials(List<Works> worksList){

        Long projectPriceForMaterials;
        List<Long> priceForMaterials;

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

        return projectPriceForMaterials;
    }

    //CRUD PROJECT
    @Override
    public Long calculatingEstimate(List<Works> worksList, boolean createReport) throws Exception {
        try{

            Long projectPriceForWorks;
            Long projectPriceForMaterials;
            Long projectPrice;

            projectPriceForWorks =  priceForWorks(worksList);
            projectPriceForMaterials = priceForMaterials (worksList);

            projectPrice = projectPriceForWorks+projectPriceForMaterials;

            if (createReport) {

                log.info(createReportAboutEstimate(projectPriceForWorks, projectPriceForMaterials, projectPrice ));
            }

            log.info(getConfigurationEntry(Constants.CALCULATING_ESTIMATE));

            return projectPrice;

        } catch(IOException e){

            log.error(e);

            return null;

        }
    }



    @Override
    public Long calculatingDeadline(List<Works> worksList, long idProject, boolean createReport) throws Exception {
        try {

            log.info(getConfigurationEntry(Constants.CALCULATING_DEADLINE));


            Long projectNeedDays;


            List<Long> priceForWorks;

            priceForWorks = worksList
                    .stream()
                    .map(value -> value.getNeedDaysToCompleted())
                    .collect(Collectors.toList());

            projectNeedDays = priceForWorks.stream().map(value -> value.longValue()).filter(a -> a != null).mapToLong(a -> a).sum();


            if (createReport) {

                log.info(createDeadlineReport(idProject, projectNeedDays));

            }



            return projectNeedDays;

        }catch(IOException e)
        {
            log.error(e);

            return null;
        }
    }



    @Override
    public boolean createProject(String name, String createdDate,
                                 String deadline, Integer numberOfWorkers, List<Works> worksList, String address,
                                 People executor, People customer, boolean isCreateEstimateReport, boolean isCreateDeadlineReport ) throws Exception {

        log.info(getConfigurationEntry(CREATE_PROJECT));

        try
        {
            if ( checkNull(name) || checkNull(createdDate) || checkNull(deadline) || checkNull(numberOfWorkers ) ||
                    checkNull(worksList ) || checkNull(address ) || checkNull(executor ) || checkNull(customer ) )
            {
                log.error(Constants.NULL_VALUE);

                return false;
            }
            else
            {
                Project project = new Project();

                long idProject = getNextProjectId();

                project.setId(idProject);
                project.setName(name);
                project.setCreatedDate(createdDate);
                project.setDeadline(deadline);
                project.setNumberOfWorkers(numberOfWorkers);
                project.setWorksList(worksList);
                project.setProjectAddress(address);
                project.setExecutor(executor);
                project.setCustomer(customer);

                writeToXml(project);

                Long estimate = calculatingEstimate(worksList, isCreateEstimateReport);
                Long deadlining = calculatingDeadline(worksList, idProject, isCreateDeadlineReport);

                StringBuffer deadlineReport = new StringBuffer();

                deadlineReport
                        .append("\n"+ getConfigurationEntry(Constants.ESTIMATE) + SPACE + estimate  +"\n")
                        .append(getConfigurationEntry(DEADLINING) + SPACE + deadlining +"\n");

                log.debug(project);


                return true;
            }

        }catch (NullPointerException e){

            log.error(e);

            return false;
        }

    }

    @Override
    public Optional<Project> getProject(long id) throws Exception {
        try {

            List<Project> projectList = readFromXml(Project.class);

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

            log.info(getConfigurationEntry(GET_PROJECT));
            log.debug(Optional.of(project));

            return Optional.of(project);

        } catch (IOException e) {

            log.error(e);

            return Optional.empty();
        }
    }


    @Override
    public boolean updateProject(Long id, String name, String createdDate,
                                 String deadline, Integer numberOfWorkers, List<Works> worksList, String address,
                                 People executor, People customer ) throws Exception {

        List<Project> listProject = readFromXml(Project.class);

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

            writeToXml(Project.class,listProject,true);

            log.info(getConfigurationEntry(UPDATE_PROJECT));
            log.debug(newProject);

            return true;

        } catch (NoSuchElementException | IndexOutOfBoundsException e) {

            log.error(e);

            return false;

        }

    }


    //Other Methods
    @Override
    public boolean deleteProject(long id, boolean createReport) throws Exception {

        List<Project> listProject = readFromXml(Project.class);

        try {
            if(getProject(id).equals(Optional.empty()))
            {
                return false;
            }
            else{
                log.info(getConfigurationEntry(Constants.DELETE_PROJECT));
                Project project = listProject.stream()
                        .filter(el -> el.getId() == id)
                        .findFirst().get();


                if (createReport)
                {
                    log.debug(createReportAboutProject(id));
                }

                listProject.remove(project);

                writeToXml(Project.class,listProject,true);

                return true;

            }



        } catch (NoSuchElementException | IndexOutOfBoundsException e) {

            log.error(e);

            return false;
        }


    }

    @Override
    public StringBuffer createReportAboutProject(long idProject) throws Exception {

        log.info(getConfigurationEntry(Constants.PROJECT_REPORT));
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

        log.info(getConfigurationEntry(Constants.ESTIMATE_REPORT));
        StringBuffer reportAboutProject = new StringBuffer();

        reportAboutProject
                .append("\n"+ getConfigurationEntry(Constants.ESTIMATE_REPORT) +"\n")
                .append(getConfigurationEntry(Constants.PRICE_FOR_WORKS) + Constants.SPACE + workPrice + "\n")
                .append(getConfigurationEntry(Constants.PRICE_FOR_MATERIALS) + Constants.SPACE + materialPrice + "\n")
                .append(getConfigurationEntry(Constants.ESTIMATE_TOTAL_PRICE) + Constants.SPACE + totalPrice +  "\n");

        return  reportAboutProject;
    }

    @Override
    public StringBuffer createDeadlineReport(Long idProject, Long needDays) throws Exception {
        log.info(getConfigurationEntry(Constants.DEADLINE_REPORT));
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
    public Long getTheCostOfWorksInProject(long idProject) throws Exception {

        try {
            if (getProject(idProject).equals(Optional.empty())){
                log.info(getConfigurationEntry(NULL_VALUE));
                return null;
            } else {
                Project project;


                project = getProject(idProject).get();

                Long projectPriceForWorks;

                List<Works> worksList;
                worksList = getWorksList(project);

                projectPriceForWorks = priceForWorks(worksList);

                log.debug(getConfigurationEntry(Constants.GET_THE_COST_OF_WORKS));

                return projectPriceForWorks;
            }
        }
        catch (IOException e)
        {
            log.error(e);
            return null;
        }

    }

    @Override
    public Long getTheCostOfMaterialsInProject(long idProject) throws Exception {
        try {
            if (getProject(idProject).equals(Optional.empty())){
                log.info(getConfigurationEntry(NULL_VALUE));
                return null;
            } else {
                Project project;

                project = getProject(idProject).get();

                Long projectPriceForMaterials;


                List<Works> worksList;
                worksList = getWorksList(project);
                projectPriceForMaterials = priceForMaterials(worksList);


                log.debug(getConfigurationEntry(Constants.GET_THE_COST_OF_MATERIALS));

                return projectPriceForMaterials;
            }

        }catch(IOException e){

            log.error(e);
            return null;
        }
    }

    @Override
    public boolean markTheStatusOfWork (Long idWork, String status) throws Exception {
        try {
            if (!(getProject(idWork)).equals(Optional.empty()) && !status.equals("") )
            {
                List<Works> listWorks = readFromXml(Works.class);

                int newId = (int) (idWork - 1);

                Works newWorks;

                newWorks = getWork(idWork).get();

                newWorks.setStatus(status);

                listWorks.set(newId, newWorks);

                createWork(listWorks);

                log.debug(getConfigurationEntry(Constants.MARK_THE_STATUS_OF_WORK));

                return true;
            }
            else {

                return false;
            }


        }catch (IOException | NoSuchElementException e)
        {
            log.error(e);
            return false;
        }

    }

    @Override
    public HashMap<Integer, String> getProgressReport(Long idProject) throws Exception {
        try {
            if(getProject(idProject).equals(Optional.empty()))
            {
                return null;
            }
            else {
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
            }
        } catch (IOException e) {

            log.error(e);

            return null;
        }
    }

    @Override
    public Long getTheRemainingTimeToComplete(Long idProject) throws Exception {
        try {
            if(getProject(idProject).equals(Optional.empty()))
            {
                return null;
            }
            else {
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
        }
        catch(IOException e)
        {
            log.error(e);
            return null;
        }
    }

}