package ru.sfedu.constrcaclconsol.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.constrcaclconsol.Constants;
import ru.sfedu.constrcaclconsol.bean.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static ru.sfedu.constrcaclconsol.Constants.*;
import static ru.sfedu.constrcaclconsol.utils.ConfigurationUtil.getConfigurationEntry;

public class DataProviderJdbc implements DataProvider {



    private static Logger log = LogManager.getLogger(DataProviderJdbc.class);


    private Connection connection;


    public DataProviderJdbc() {
        try {
            getConnection();

        } catch (ClassNotFoundException | SQLException | IOException e) {

            e.printStackTrace();
        }
    }

    /**
     * Создание всех таблиц
     */
    public void createTable() {
        try {
            String createDb = new String(Files.readAllBytes(Paths.get(Constants.DB_CREATE)));
            connection.createStatement().execute(createDb);


        }catch (NullPointerException | IOException | SQLException e) {
            log.error(e);
        }
    }

    /**
     * Установка соединения с БД
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException
     */
    public void getConnection() throws ClassNotFoundException, SQLException, IOException {
        try {

            Class.forName(getConfigurationEntry(JDBC_DRIVER));
            connection = DriverManager.getConnection(getConfigurationEntry(DB_URL), getConfigurationEntry(DB_USER), getConfigurationEntry(DB_PASS));


        } catch (SQLException | IOException | ClassNotFoundException e) {

            log.fatal(e);
            System.exit(1);
        }

    }


    private Statement createStatement() throws SQLException {
        if (!connection.isClosed()) {
            return connection.createStatement();
        } else {
            throw new SQLException();
        }
    }


    public void execute(String sql) {
        try {
            log.debug(sql);
            Statement statement = createStatement();
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            log.error(e);
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


    //CRUD Materials
    @Override
    public boolean createMaterials(List<Materials> listMaterials) throws SQLException, IOException {
        PreparedStatement pst;
        try
        {
            if (checkEmptyList(listMaterials)){

                log.info(getConfigurationEntry(Constants.CREATE_MATERIALS));

                pst = connection.prepareStatement(MATERIALS_INSERT);

                for (Materials a : listMaterials) {
                    pst.setObject(1, a.getId());
                    pst.setObject(2, a.getName());
                    pst.setObject(3, a.getNumber());
                    pst.setObject(4, a.getPriceForOne());
                    pst.execute();
                }
                pst.close();

                log.debug(listMaterials);

                return true;
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
    public  Optional<Materials> getMaterialById(long id) throws IOException, SQLException, ClassNotFoundException {

        try {
            log.info(getConfigurationEntry(GET_MATERIALS));


            Statement statement = createStatement();
            ResultSet resultSet = statement.executeQuery(String.format(DB_SELECT, Materials.class.getSimpleName().toLowerCase(), id));

           if (resultSet != null && resultSet.next()) {

               Materials material = new Materials();
               material.setId(resultSet.getLong(Constants.ID));
               material.setName(resultSet.getString(Constants.NAME));
               material.setNumber(Long.valueOf(resultSet.getString(Constants.NUMBER)));
               material.setPriceForOne(Long.valueOf(resultSet.getString(Constants.PRICE_FOR_ONE)));

               log.debug(material);
               statement.close();

               return Optional.of(material);

           } else {

               log.error(getConfigurationEntry(Constants.ERROR_ID));


               return Optional.empty();
           }

       }catch (SQLException e) {

           log.error(e);
           
           return Optional.empty();
       }
    }

    @Override
    public boolean deleteMaterialById(long id) {

        try {
            
            log.info(getConfigurationEntry(DELETE_MATERIALS));

            if (!(getMaterialById(id).get()).equals(Optional.empty())) {

                Statement statement = createStatement();
                statement.executeUpdate(String.format(DB_DELETE, Materials.class.getSimpleName().toLowerCase(), id));

                log.debug(statement);

                log.info(DELETED);
                statement.close();


                return true;
            }
            else{

                log.error(getConfigurationEntry(NULL_VALUE));

                return false;
            }

        }catch (NullPointerException | IOException | SQLException | ClassNotFoundException | NoSuchElementException e){

            log.error(e);

            log.info(NOT_DELETED);

            return false;
        }

     
    }

    @Override
    public boolean updateMaterial(long id, String name, Long number, Long priceForOne) throws IOException, SQLException, ClassNotFoundException {

        if (getMaterialById(id).equals(Optional.empty())){

            log.error(getConfigurationEntry(NULL_VALUE));

            return false;
        }

        try {

            Materials newMaterials = new Materials();
            newMaterials.setId(id);
            newMaterials.setName(name);
            newMaterials.setNumber(number);
            newMaterials.setPriceForOne(priceForOne);

            this.execute(String.format(DB_UPDATE_MATERIALS, newMaterials.getName(), newMaterials.getNumber(), newMaterials.getPriceForOne(), id));

            log.info(getConfigurationEntry(UPDATE_MATERIALS));


            return true;

        } catch (NoSuchElementException | IndexOutOfBoundsException e) {

            log.error(e);

            return false;

        }

    }


    //CRUD WORKS

    /**
     * Создание таблицы, в которой показаня связь между материалами и работами
     * @param idWork
     * @param idMaterial
     */
    public void createListMaterials(long idWork, long idMaterial) {

        this.execute(String.format(DB_INSERT, WORK_LIST_MATERIAL, LIST_MATERIAL_FIELDS,

                String.format(LIST_FORMAT,idWork,idMaterial)));

    }

    /**
     * Получение списка id материалов для конкретной работы
     * @param idWork - id работы
     * @return
     * @throws SQLException
     */
    public List<Long> getListMaterialsById(long idWork) throws SQLException {

        try {

            Statement statement = createStatement();
            ResultSet resultSet = statement.executeQuery(String.format(DB_SELECT_LIST_MATERIALS, WORK_LIST_MATERIAL, idWork));


            if (resultSet != null ) {
                List<Long> list = new ArrayList<>();
                while(resultSet.next()) {
                    list.add(resultSet.getLong(ID_MATERIAL));
                }

                log.debug(list);

                statement.close();

                return list;
            } else {

                statement.close();

                log.error(getConfigurationEntry(Constants.ERROR_ID));

                return null;
            }
        } catch (SQLException | IOException e) {

            log.error(e);

            return null;

        }
    }

    /**
     * Удаление материалов для конкретной работы
     * @param idWork
     * @return
     * @throws SQLException
     */
    public boolean deleteListMaterials(long idWork) throws SQLException {
        try{
            if (getListMaterialsById(idWork) == null){

                log.info(getConfigurationEntry(NULL_VALUE));

                return false;
            }


            Statement statement = createStatement();
            statement.executeUpdate(String.format(DB_DELETE_LIST_MATERIALS, WORK_LIST_MATERIAL, idWork));


            statement.close();

            return true;

        }catch (NullPointerException | SQLException | IOException e){

            log.error(e);

            return false;
        }
    }

    @Override
    public boolean createWork(List<Works> listWorks) throws Exception {
        PreparedStatement pst = null;

        log.info(getConfigurationEntry(CREATE_WORKS));


        List<Materials> listMaterials;

        try
        {
            if (checkEmptyList(listWorks)){


                pst = connection.prepareStatement(WORKS_INSERT);

                for (Works a : listWorks) {
                    pst.setObject(1, a.getName());
                    pst.setObject(2, a.getPrice());
                    pst.setObject(3, a.getPriority());
                    pst.setObject(4, a.getNeedDaysToCompleted());
                    pst.setObject(5, a.getStatus());

                    listMaterials = a.getListMaterials();

                    for (int i=0; i<listMaterials.size();i++)
                    {

                        createListMaterials(a.getId(),listMaterials.get(i).getId());
                    }


                    pst.execute();
                }

                pst.close();

                return true;

            }
            else
            {
                log.error(getConfigurationEntry(NULL_VALUE));
                pst.close();


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

            log.info(getConfigurationEntry(GET_WORKS));

            Statement statement = createStatement();
            ResultSet resultSet = statement.executeQuery(String.format(DB_SELECT, Works.class.getSimpleName().toLowerCase(), id));



            if (resultSet != null && resultSet.next()) {

                Works newWorks = new Works();
                newWorks.setId(resultSet.getLong(ID));
                newWorks.setName(resultSet.getString(NAME));
                newWorks.setPrice(resultSet.getLong(PRICE));
                newWorks.setPriority(resultSet.getString(PRIORITY));
                newWorks.setNeedDaysToCompleted(resultSet.getLong(NEED_DAYS_TO_COMPL));
                newWorks.setStatus(resultSet.getString(STATUS));

                List<Long> listIdMaterial=new ArrayList<>();
                listIdMaterial = getListMaterialsById(id);
                List<Materials> materials = new ArrayList<>();

                log.debug(listIdMaterial);


                for (int i = 0;i<listIdMaterial.size();i++)
                {

                    materials.add((getMaterialById(listIdMaterial.get(i).longValue())).get());
                }

                newWorks.setListMaterials(materials);

                statement.close();

                log.debug(Optional.of(newWorks));


                return Optional.of(newWorks);

            } else {

                statement.close();

                log.error(getConfigurationEntry(Constants.ERROR_ID));

                return Optional.empty();
            }

        }catch (SQLException e) {

            log.error(e);

            return Optional.empty();
        }
    }

    @Override
    public boolean deleteWorkById(long id) throws Exception {
        try{

            if ((getWork(id).get()).equals(Optional.empty()) ){

                return false;

            }else{

                log.info(getConfigurationEntry(DELETE_WORKS));

                Statement statement = createStatement();
                statement.executeUpdate(String.format(DB_DELETE, Works.class.getSimpleName().toLowerCase(), id));

                deleteListMaterials(id);

                log.info(DELETED);

                statement.close();

                return true;
            }



        }catch (NullPointerException | SQLException | NoSuchElementException e){

            log.info(NOT_DELETED);

            log.error(e);

            return false;
        }
    }

    @Override
    public boolean updateWork(Long id, String name, Long price, String priority, Long daysToCompletedWorks, String status, List<Materials> listMaterials) throws Exception {
        try {

            if (getWork(id).equals(Optional.empty())){
                log.info(getConfigurationEntry(NULL_VALUE));
                return false;
            }

            Works newWorks = new Works();

            newWorks.setId(id);
            newWorks.setName(name);
            newWorks.setPrice(price);
            newWorks.setPriority(priority);
            newWorks.setNeedDaysToCompleted(daysToCompletedWorks);
            newWorks.setStatus(status);
            newWorks.setListMaterials(listMaterials);

            deleteListMaterials(id);


            for (int i=0; i<listMaterials.size();i++)
            {

                createListMaterials(id,listMaterials.get(i).getId());
            }

            log.debug(listMaterials);
            log.debug(newWorks);

            execute(String.format(DB_UPDATE_WORK, newWorks.getName(), newWorks.getPrice(), newWorks.getPriority(), newWorks.getNeedDaysToCompleted(), newWorks.getStatus(), id));


            return true;

        } catch (NullPointerException | NoSuchElementException | IndexOutOfBoundsException e) {

            log.error(e);

            return false;
        }
    }


    //CRUD Customer
    @Override
    public boolean createCustomer(String name, String surname, String mailbox, String telephone) throws IOException, ClassNotFoundException {

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
                customer.setName(name);
                customer.setSurname(surname);
                customer.setMailbox(mailbox);
                customer.setTelephone(telephone);


                this.execute(String.format(DB_INSERT, customer.getClass().getSimpleName().toLowerCase(), CUSTOMER_FIELDS,
                        String.format(CUSTOMER_INSERT_FORMAT,customer.getName(), customer.getSurname(), customer.getMailbox(),customer.getTelephone())));

                String nameCustomer = customer.getName();


                Statement statement = createStatement();
                ResultSet set = statement.executeQuery(String.format(GET_CUSTOMER_ID + nameCustomer + QUOTATION_MARK));
                log.debug(statement);

                if (set != null && set.next()) {
                    log.debug(set.getLong(Constants.ID));
                }
                customer.setId(set.getLong(Constants.ID));


                statement.close();

                return true;
            }
        }catch (NullPointerException | SQLException e){

            log.error(e);

            return false;
        }
    }

    @Override
    public Optional<Customer> getCustomerById(long id) throws SQLException, IOException, ClassNotFoundException {

        try {

            Statement statement = createStatement();
            ResultSet resultSet = statement.executeQuery(String.format(DB_SELECT, Customer.class.getSimpleName().toLowerCase(), id));


            if (resultSet != null && resultSet.next()) {

                log.info(getConfigurationEntry(GET_CUSTOMER));

                Customer customer = new Customer();
                customer.setId(resultSet.getLong(Constants.ID));
                customer.setName(resultSet.getString(Constants.NAME));
                customer.setSurname(resultSet.getString(Constants.SURNAME));
                customer.setMailbox(resultSet.getString(Constants.MAILBOX));
                customer.setTelephone(resultSet.getString(Constants.TELEPHONE));

                statement.close();

                log.debug(Optional.of(customer));

                return Optional.of(customer);

            } else {

                statement.close();


                log.error(getConfigurationEntry(Constants.ERROR_ID));

                return Optional.empty();
            }
        }catch (SQLException e) {

            log.error(e);
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteCustomerById(long id) throws IOException, ClassNotFoundException {
        try {

            if ((getCustomerById(id).get()).equals(Optional.empty()) ){

                log.error(getConfigurationEntry(NULL_VALUE));
                return false;

            }else {

                Statement statement = createStatement();
                statement.executeUpdate(String.format(DB_DELETE, Customer.class.getSimpleName().toLowerCase(), id));

                statement.close();

                log.info(DELETED);

                return true;
            }
        }catch (NullPointerException | SQLException | NoSuchElementException e){

            log.info(NOT_DELETED);

            log.error(e);

            return false;
        }
    }

    @Override
    public boolean updateCustomer(long id, String name, String surname, String mailbox, String telephone ) throws IOException {
        try {
            if (getCustomerById(id).equals(Optional.empty())){

                log.error(getConfigurationEntry(NULL_VALUE));

                return false;
            }
            Customer newCustomer = new Customer();

            newCustomer.setId(id);
            newCustomer.setName(name);
            newCustomer.setSurname(surname);
            newCustomer.setMailbox(mailbox);
            newCustomer.setTelephone(telephone);

            log.info(getConfigurationEntry(UPDATE_CUSTOMER));
            log.debug(newCustomer);

            this.execute(String.format(DB_UPDATE_CUSTOMER, newCustomer.getName(), newCustomer.getSurname(), newCustomer.getMailbox(),newCustomer.getTelephone(), id));

            return true;

        } catch (NoSuchElementException | IndexOutOfBoundsException | NullPointerException | SQLException | ClassNotFoundException e) {

            log.error(e);

            return false;
        }
    }

    @Override
    public boolean createExecutor(String name, String surname, String mailbox, Long numberOfCompletedProjects, Long numberOfWorkers) throws Exception {

        log.info(getConfigurationEntry(CREATE_EXECUTOR));


        try
        {
            if ( checkNull(name) || checkNull(surname) || checkNull(mailbox) || checkNull(numberOfCompletedProjects) || checkNull(numberOfWorkers) )
            {
                return false;
            }
            else
            {
                Executor executor = new Executor();
                executor.setName(name);
                executor.setSurname(surname);
                executor.setMailbox(mailbox);
                executor.setNumberOfCompletedProjects(numberOfCompletedProjects);
                executor.setNumberOfWorkers(numberOfWorkers);

                this.execute(String.format(DB_INSERT, executor.getClass().getSimpleName().toLowerCase(), EXECUTOR_FIELDS,
                        String.format(EXECUTOR_INSERT_FORMAT,executor.getName(), executor.getSurname(), executor.getMailbox(), executor.getNumberOfCompletedProjects(), executor.getNumberOfWorkers())));

                String nameExecutor =executor.getName();


                Statement statement = createStatement();
                ResultSet set = statement.executeQuery(String.format(GET_EXECUTOR_ID + nameExecutor + QUOTATION_MARK));
                log.error(statement);


                if (set != null && set.next()) {
                    log.error(set.getLong(Constants.ID));
                }
                executor.setId(set.getLong(Constants.ID));

                log.debug(executor);

                statement.close();


                return true;
            }
        }catch (NullPointerException | SQLException e){

            log.error(e);

            return false;
        }
    }

    @Override
    public Optional<Executor> getExecutorById(long id) throws Exception {
        try {

            Statement statement = createStatement();
            ResultSet set = statement.executeQuery(String.format(DB_SELECT, Executor.class.getSimpleName().toLowerCase(), id));


            if (set != null && set.next()) {

                log.debug(getConfigurationEntry(GET_EXECUTOR));

                Executor executor = new Executor();
                executor.setId(set.getLong(Constants.ID));
                executor.setName(set.getString(Constants.NAME));
                executor.setSurname(set.getString(Constants.SURNAME));
                executor.setMailbox(set.getString(Constants.MAILBOX));
                executor.setNumberOfCompletedProjects(set.getLong(Constants.NUMBER_OF_COMPL_PROJECT));
                executor.setNumberOfWorkers(set.getLong(NUMBER_OF_WORKERS));


                statement.close();

                log.debug(Optional.of(executor));

                return Optional.of(executor);

            } else {

                statement.close();

                log.error(getConfigurationEntry(Constants.ERROR_ID));

                return Optional.empty();
            }
        }catch (SQLException e) {

            log.error(e);
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteExecutorById(long id) throws Exception {
        try {

            if ((getExecutorById(id).get()).equals(Optional.empty()) ){

                log.error(getConfigurationEntry(NULL_VALUE));
                return false;

            }else {
                Statement statement = createStatement();
                statement.executeUpdate(String.format(DB_DELETE, Executor.class.getSimpleName().toLowerCase(), id));


                statement.close();


                log.info(DELETED);

                return true;
            }
        }catch (NullPointerException | SQLException | NoSuchElementException e){

            log.info(NOT_DELETED);

            log.error(e);

            return false;
        }
    }

    @Override
    public boolean updateExecutor(long id, String name, String surname, String mailbox, Long numberOfCompletedProject, Long numberOfWorkers) throws Exception {
        try {
            if (getExecutorById(id).equals(Optional.empty())){

                log.error(getConfigurationEntry(NULL_VALUE));

                return false;
            }
            Executor newExecutor = new Executor();

            newExecutor.setId(id);
            newExecutor.setName(name);
            newExecutor.setSurname(surname);
            newExecutor.setMailbox(mailbox);
            newExecutor.setNumberOfCompletedProjects(numberOfCompletedProject);
            newExecutor.setNumberOfWorkers(numberOfWorkers);

            log.info(getConfigurationEntry(UPDATE_EXECUTOR));

            this.execute(String.format(DB_UPDATE_EXECUTOR, newExecutor.getName(), newExecutor.getSurname(), newExecutor.getMailbox(), newExecutor.getNumberOfCompletedProjects(), newExecutor.getNumberOfWorkers(), id));

            log.debug(newExecutor);
            return true;

        } catch (NoSuchElementException | IndexOutOfBoundsException | NullPointerException | SQLException | ClassNotFoundException e) {

            log.error(e);

            return false;
        }
    }



    //CRUD PROJECT
    public void createListWorks(long idProject, long idWork) {

        this.execute(String.format(DB_INSERT, PROJECT_LIST_WORK, LIST_WORK_FIELDS,

                String.format(LIST_FORMAT,idProject,idWork)));

    }

    public List<Long> getListWorkById(long idProject) throws SQLException {

        try {

            Statement statement = createStatement();
            ResultSet set = statement.executeQuery(String.format(String.format(DB_SELECT_LIST_WORKS, PROJECT_LIST_WORK, idProject)));



            if (set != null ) {
                List<Long> list = new ArrayList<>();
                while(set.next()) {
                    list.add(set.getLong(ID_WORK));
                }

                log.debug(list);


                statement.close();

                return list;
            } else {

                statement.close();


                log.error(getConfigurationEntry(Constants.ERROR_ID));

                return null;
            }
        } catch (SQLException | IOException e) {

            log.error(e);

            return null;

        }
    }

    public boolean deleteListWorks(long idProject) throws SQLException {
        try{
            if (getListMaterialsById(idProject) == null){

                log.info(getConfigurationEntry(NULL_VALUE));

                return false;
            }
            Statement statement = createStatement();
            statement.executeUpdate(String.format(DB_DELETE_LIST_WORKS, PROJECT_LIST_WORK, idProject));


            statement.close();


            return true;

        }catch (NullPointerException | SQLException | IOException e){

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

            log.debug(getConfigurationEntry(Constants.CALCULATING_DEADLINE));


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
    public boolean createProject(String name, String createdDate, String deadline, Integer numberOfWorkers,
                                 List<Works> worksList, String address, People executor, People customer, boolean isCreateEstimateReport, boolean isCreateDeadlineReport) throws Exception {

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
                project.setName(name);
                project.setCreatedDate(createdDate);
                project.setDeadline(deadline);
                project.setNumberOfWorkers(numberOfWorkers);
                project.setWorksList(worksList);
                project.setProjectAddress(address);
                project.setExecutor(executor);
                project.setCustomer(customer);


                this.execute(String.format(DB_INSERT, project.getClass().getSimpleName().toLowerCase(), PROJECT_FIELDS,
                        String.format(PROJECT_INSERT_FORMAT,project.getName(), project.getCreatedDate(), project.getDeadline(),project.getNumberOfWorkers(), project.getProjectAddress(),project.getCustomer().getId(),project.getExecutor().getId())));


                String nameProject = project.getName();
                Statement statement = createStatement();

                ResultSet set = statement.executeQuery(String.format(GET_PROJECT_ID + nameProject + QUOTATION_MARK));

                log.error(statement);


                if (set != null && set.next()) {

                    log.debug(set.getLong(Constants.ID));
                }
                project.setId(set.getLong(Constants.ID));



                for (int i=0; i<worksList.size();i++)
                {

                    createListWorks(project.getId(),worksList.get(i).getId());
                }


                Long estimate = calculatingEstimate(worksList, isCreateEstimateReport);
                Long deadlining = calculatingDeadline(worksList, set.getLong(Constants.ID), isCreateDeadlineReport);

                StringBuffer deadlineReport = new StringBuffer();

                deadlineReport
                        .append("\n"+ getConfigurationEntry(Constants.ESTIMATE) + SPACE + estimate  +"\n")
                        .append(getConfigurationEntry(DEADLINING) + SPACE + deadlining +"\n");



                statement.close();

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

            log.info(getConfigurationEntry(GET_PROJECT));

            Statement statement = createStatement();
            ResultSet set = statement.executeQuery(String.format(DB_SELECT, Project.class.getSimpleName().toLowerCase(), id));



            if (set != null && set.next()) {

                Project newProject = new Project();
                newProject.setId(set.getLong(ID));
                newProject.setName(set.getString(NAME));
                newProject.setCreatedDate(set.getString(CREATED__DATE));
                newProject.setDeadline(set.getString(DEADLINE_INSERT));
                newProject.setNumberOfWorkers(Math.toIntExact(set.getLong(NUMBER_OF_WORKERS)));
                newProject.setProjectAddress(set.getString(PROJECTADDRESS));


                List<Long> listIdWork;
                listIdWork = getListWorkById(id);
                List<Works> works = new ArrayList<>();

                log.error(listIdWork);


                for (int i = 0;i<listIdWork.size();i++)
                {

                    works.add((getWork(listIdWork.get(i))).get());
                }


                newProject.setWorksList(works);

                int customerId = set.getInt(ID_CUSTOMER);
                int executorId = set.getInt(ID_EXECUTOR);

                log.debug(customerId);
                log.debug(executorId);

                newProject.setCustomer(getCustomerById(customerId).get());
                newProject.setExecutor(getExecutorById(executorId).get());


                statement.close();


                return Optional.of(newProject);

            } else {

                statement.close();

                log.error(getConfigurationEntry(Constants.ERROR_ID));

                return Optional.empty();
            }

        }catch (SQLException e) {

            log.error(e);
            return Optional.empty();
        }
    }


    @Override
    public boolean updateProject(Long id, String name, String createdDate, String deadline, Integer numberOfWorkers, List<Works> worksList, String address, People executor, People customer) throws Exception {
        try {

            if (getProject(id).equals(Optional.empty())){
                log.info(getConfigurationEntry(NULL_VALUE));
                return false;
            }

            Project project = new Project();
            project.setId(id);
            project.setName(name);
            project.setCreatedDate(createdDate);
            project.setDeadline(deadline);
            project.setNumberOfWorkers(numberOfWorkers);
            project.setWorksList(worksList);
            project.setProjectAddress(address);
            project.setExecutor(executor);
            project.setCustomer(customer);

            deleteListWorks(id);


            for (int i=0; i<worksList.size();i++)
            {

                log.error(id);
                log.error(worksList.get(i).getId());

                createListWorks(id,worksList.get(i).getId());
            }

            log.error(worksList);

            log.info(getConfigurationEntry(UPDATE_PROJECT));



            execute(String.format(DB_UPDATE_PROJECT, project.getName(), project.getCreatedDate(), project.getDeadline(),project.getNumberOfWorkers(), project.getProjectAddress(),project.getCustomer().getId(),project.getExecutor().getId(), id));

            return true;

        } catch (NullPointerException | NoSuchElementException | IndexOutOfBoundsException e) {

            log.error(e);

            return false;
        }
    }


    @Override
    public boolean deleteProject(long id, boolean createReport) throws Exception {
        try{
            log.error(getProject(id).get());

            if ((getProject(id).get()).equals(Optional.empty()) ){

                return false;

            }else{

                if (createReport)
                {
                    log.debug(createReportAboutProject(id));
                }

                log.debug(getConfigurationEntry(DELETE_PROJECT));


                Statement statement = createStatement();

                statement.executeUpdate(String.format(DB_DELETE, Project.class.getSimpleName().toLowerCase(), id));

                log.error(statement);


                deleteListWorks(id);

                log.info(DELETED);

                statement.close();


                return true;
            }



        }catch (NullPointerException | SQLException | NoSuchElementException e){

            log.info(NOT_DELETED);

            log.error(e);

            return false;
        }
    }

    //Other methods
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

                List<Long> listIdWork;
                listIdWork = getListWorkById(idProject);
                List<Works> works = new ArrayList<>();


                for (int i = 0; i < listIdWork.size(); i++) {

                    works.add((getWork(listIdWork.get(i))).get());
                }


                Long projectPriceForWorks;


                List<Long> priceForWorks;

                priceForWorks = works
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

                Long projectPriceForMaterials;
                List<Long> priceForMaterials;


                List<Long> listIdWork;
                listIdWork = getListWorkById(idProject);
                List<Works> works = new ArrayList<>();


                for (int i = 0; i < listIdWork.size(); i++) {

                    works.add((getWork(listIdWork.get(i))).get());
                }


                List<Materials> materilsListInWorks;

                //Список всех материалов в работах
                materilsListInWorks = works.stream()
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
            }

        }catch(IOException e){

            log.error(e);
            return null;
        }
    }


    @Override
    public boolean markTheStatusOfWork (Long idWork, String status) throws Exception {
        try {

            if (getWork(idWork).equals(Optional.empty())){

                log.info(getConfigurationEntry(NULL_VALUE));

                return false;
            }else {
                Works newWorks;

                log.error(getWork(idWork).get());
                newWorks = getWork(idWork).get();

                newWorks.setStatus(status);

                execute(String.format(DB_UPDATE_WORK, newWorks.getName(), newWorks.getPrice(), newWorks.getPriority(), newWorks.getNeedDaysToCompleted(), newWorks.getStatus(), idWork));


                return true;
            }
        } catch (NullPointerException | NoSuchElementException | IndexOutOfBoundsException | IOException e) {
       ;
            log.error(e);

            return false;


        }

    }

    @Override
    public HashMap<Integer, String> getProgressReport(Long idProject) throws Exception {
        try {
            if (getProject(idProject).equals(Optional.empty())){
                log.info(getConfigurationEntry(NULL_VALUE));
                return null;
            } else {

                List<Long> listIdWork;
                listIdWork = getListWorkById(idProject);
                List<Works> works = new ArrayList<>();


                for (int i = 0; i < listIdWork.size(); i++) {

                    works.add((getWork(listIdWork.get(i))).get());
                }

                HashMap<Integer, String> workAndStatus = new HashMap<>();

                for (int i = 0; i < works.size(); i++) {
                    workAndStatus.put(Math.toIntExact(works.get(i).getId()), works.get(i).getStatus());
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
            if (getProject(idProject).equals(Optional.empty())){
                log.info(getConfigurationEntry(NULL_VALUE));
                return null;
            } else {

                List<Long> listIdWork;
                listIdWork = getListWorkById(idProject);
                List<Works> works = new ArrayList<>();


                for (int i = 0; i < listIdWork.size(); i++) {
                    works.add((getWork(listIdWork.get(i))).get());
                }


                List<Long> NotCompletedWork;
                List<Works> worksListNotCompleted;

                worksListNotCompleted = works
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



