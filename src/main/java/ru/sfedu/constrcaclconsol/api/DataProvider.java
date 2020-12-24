package ru.sfedu.constrcaclconsol.api;


import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import ru.sfedu.constrcaclconsol.bean.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


/**
 * The interface Data provider.
 */
public interface DataProvider {

    //Materials

    /** This method gets a list of Materials that it uses to create objects of the Materials class
     * @param listMaterials the value of a list of the class Materials
     * @return a boolean value depending on the execution outcome
     * @throws IOException
     * @throws CsvDataTypeMismatchException
     * @throws CsvRequiredFieldEmptyException
     */
    boolean createMaterials(List<Materials> listMaterials) throws Exception;

    /** This method finds the required instance of the class by id and gets all the information about it
     * @param materialId value of the id of the object of the class to get
     * @return specific material
     * @throws IOException
     */
    Optional<Materials> getMaterialById(long materialId) throws Exception;

    /** This method finds a specific material by id and removes it from the list
     * @param id value of the material id to delete
     * @return a boolean value depending on the execution outcome
     * @throws IOException
     */
    boolean deleteMaterialById(long id) throws Exception;

    /** This method changes the existing information about the material that is found by the specified id
     * @param id the value of the id of the material
     * @param name the value of the name of the material
     * @param number the value of the number of the material
     * @param priceForOne the value of the totalPrice of the material
     * @return a boolean value depending on the execution outcome
     * @throws IOException
     */
    boolean updateMaterial(long id, String name, Long number, Long priceForOne) throws Exception;


    //Works

    /** This method gets a list of Works that it uses to create objects of the Works class
     * @param listWorks the value of a list of the class Works
     * @return a boolean value depending on the execution outcome
     * @throws IOException
     * @throws CsvDataTypeMismatchException
     * @throws CsvRequiredFieldEmptyException
     */
    boolean createWork(List<Works> listWorks) throws Exception;

    /** This method finds the required instance of the class by id and gets all the information about it
     * @param id value of the id of the object of the class to get
     * @return specific work
     */
    Optional<Works> getWork(long id) throws Exception;

    /** This method finds a specific works by id and removes it from the list
     * @param id value of the work id to delete
     * @return a boolean value depending on the execution outcome
     * @throws IOException
     */
    boolean deleteWorkById(long id) throws Exception;

    /** This method changes the existing information about the works that is found by the specified id
     * @param id the value of id of the work
     * @param name the value of name of the work
     * @param price the value of price of the work
     * @param priority the value of  priority of the work
     * @param daysToCompletedWorks the value of days to completed of the work
     * @param status the value of status of the work
     * @param listMaterials the value of list materials of the work
     * @return a boolean value depending on the execution outcome
     * @throws IOException
     */
    boolean updateWork(Long id, String name, Long price, String priority,
                       Long daysToCompletedWorks, String status, List<Materials> listMaterials ) throws Exception;


    //Customer

    /** This method create objects of the Customer class
     *
     * @param name the value of name of the customer
     * @param surname the value of surname of the customer
     * @param mailbox the value of mailbox of the customer
     * @param telephone the value of telephone of the customer
     * @return a boolean value depending on the execution outcome
     * @throws IOException
     * @throws CsvDataTypeMismatchException
     * @throws CsvRequiredFieldEmptyException
     */
    boolean createCustomer(String name, String surname, String mailbox, String telephone) throws Exception;

        /** This method finds the required instance of the class by id and gets all the information about it
         * @param id value of the id of the object of the class to get
         * @return specific customer
         * @throws IOException
         */
    Optional<Customer> getCustomerById(long id) throws Exception;

    /** This method finds a specific customer by id and removes it from the list
     * @param id value of the customer id to delete
     * @return a boolean value depending on the execution outcome
     * @throws IOException
     */
    boolean deleteCustomerById(long id) throws Exception;

    /** This method changes the existing information about the customer that is found by the specified id
     * @param id the value of id of the customer
     * @param name the value of name of the customer
     * @param surname the value of surname of the customer
     * @param mailbox the value of mailbox of the customer
     * @param telephone the value of telephone of the customer
     * @return a boolean value depending on the execution outcome
     * @throws IOException
     */
    boolean updateCustomer(long id, String name, String surname, String mailbox, String telephone ) throws Exception;


    //Executor

    /** This method create objects of the Executor class
     *
     * @param name the value of name of the executor
     * @param surname the value of surname of the executor
     * @param mailbox the value of mailbox of the executor
     * @param numberOfCompletedWork the value of number of completed work of the executor
     * @param numberOfWorkers the value of number of workers of the executor
     * @return a boolean value depending on the execution outcome
     * @throws IOException
     * @throws CsvDataTypeMismatchException
     * @throws CsvRequiredFieldEmptyException
     */
    boolean createExecutor( String name, String surname, String mailbox, Long numberOfCompletedWork, Long numberOfWorkers) throws Exception;

    /** This method finds the required instance of the class by id and gets all the information about it
     * @param id value of the id of the object of the class to get
     * @return specific executor
     * @throws IOException
     */
    Optional<Executor> getExecutorById(long id) throws Exception;

    /** This method finds a specific executor by id and removes it from the list
     * @param id value of the executor id to delete
     * @return a boolean value depending on the execution outcome
     * @throws IOException
     */
    boolean deleteExecutorById(long id) throws Exception;

    /** This method changes the existing information about the executor that is found by the specified id
     * @param id the value of id of the executor
     * @param name the value of name of the executor
     * @param surname the value of surname of the executor
     * @param mailbox the value of mailbox of the executor
     * @param numberOfCompletedProjects the value of number of completed work of the executor
     * @param numberOfWorkers the value of number of workers of the executor
     * @return a boolean value depending on the execution outcome
     * @throws IOException
     */
    boolean updateExecutor(long id, String name, String surname, String mailbox, Long numberOfCompletedProjects, Long numberOfWorkers ) throws Exception;


    //Project

    /** This method create objects of the Project class
     *
     * @param name the value of name of the project
     * @param createdDate the value of created date of the project
     * @param deadline the value of deadline of the project
     * @param numberOfWorkers the value of number of workers of the project
     * @param worksList the value of list of works of the project
     * @param address the value of address of the project
     * @param executor the value of executor of the project
     * @param customer the value of customer of the project
     * @return
     * @throws IOException
     * @throws CsvDataTypeMismatchException
     * @throws CsvRequiredFieldEmptyException
     */
    boolean createProject(String name, String createdDate,
                          String deadline, Integer numberOfWorkers, List<Works> worksList, String address,
                          People executor, People customer ) throws Exception;

    /** This method finds the required instance of the class by id and gets all the information about it
     * @param id value of the id of the object of the class to get
     * @return specific project
     */
    Optional<Project> getProject(long id) throws Exception;

    /** This method finds a specific project by id and removes it from the list and depending on the flag create or not report
     * @param id value of the project id to delete
     *  @param createReport flag to create report about project
     * @return a boolean value depending on the execution outcome
     * @throws IOException
     */
    boolean deleteProject(long id, boolean createReport) throws Exception;

    /** This method changes the existing information about the project that is found by the specified id
     * @param id the value of id of the project
     * @param name the value of name of the project
     * @param createdDate the value created date  of of the project
     * @param deadline the value of deadline of the project
     * @param numberOfWorkers the value of  number of workers of the project
     * @param worksList the value of work list of the project
     * @param address the value of address of the project
     * @param executor the value of executor of the project
     * @param customer the value of customer of the project
     * @return a boolean value depending on the execution outcome
     * @throws IOException
     */
    boolean updateProject(Long id, String name, String createdDate,
                          String deadline, Integer numberOfWorkers, List<Works> worksList, String address,
                          People executor, People customer ) throws Exception;

    //Other methods

    /** Отчет о проекте
     * @param idProject - id проекта
     * @return StringBuffer
     * @throws Exception
     */
    StringBuffer createReportAboutProject(long idProject) throws Exception;

    /**
     * Создание отчета по расчету сметы
     * @param workPrice - цена за все работы
     * @param materialPrice - цена за все материалы
     * @param totalPrice - общая стоимость
     * @return StringBuffer (стоимость работ, стоимость материалов и общая стоимость)
     * @throws Exception
     */
    StringBuffer createReportAboutEstimate(long workPrice, long materialPrice, long totalPrice)throws Exception;

    /**
     * Создание отчета на основе расчетов о необходимом времени выполнения
     * @param idProject - id проекта
     * @param needDays - количество дней, необходимых до завершения проекта
     * @return StringBuffer (начало проекта – предполагаемая дата окончания, и необходимое количество дней для его завершения)
     * @throws IOException
     */
    StringBuffer createDeadlineReport(Long idProject, Long needDays) throws Exception;

    /**
     * Получение стоимости работ, которые будут выполнятся в проекте
     * @param idProject - id проекта
     * @return Long workPrice
     * @throws IOException
     */
    Long GetTheCostOfWorksInProject(long idProject) throws Exception;

    /**Получение стоимости материалов, которые будут необходимы для выполнения работ в  проекте
     * @param idProject - id проекта
     * @return Long materialPrice
     * @throws IOException
     */
    Long GetTheCostOfMaterialsInProject(long idProject) throws Exception;

    /**Происходит расчет итоговой стоимости
     * @param idProject  - id проекта
     * @param createReport - флаг для вызова дополнительного метода создания отчета
     * @return  Long projectPrice - итоговая стоимость
     * @throws Exception
     */
    Long calculatingEstimate(Long idProject, boolean createReport) throws Exception;

    /**Расчёт необходимого количества дней для выполнения проекта
     * @param idProject - id проекта
     * @param createReport - флаг для вызова дополнительного метода создания отчета
     * @return Long projectDeadline - итоговое количество дней
     * @throws Exception
     */
    Long calculatingDeadline(Long idProject, boolean createReport) throws Exception;

    /** Изменение статуса выполнения работы в проекте
     *
     * @param idWork - id работы, для которой необходимо установить новый статус
     * @param status - новый статус
     * @return
     * @throws Exception
     */
    boolean markTheStatusOfWork (Long idWork, String status) throws Exception;

    /** Возможность получить список работ со статусом их выполнения (сделано/ в процессе/ завершено)
     *
     * @param idProject - id проекта
     * @return  HashMap<Integer, String>
     * @throws Exception
     */
    HashMap<Integer, String> getProgressReport(Long idProject) throws Exception;

    /**
     * Получение оставшегося времени до завершения проекта на основе еще не выполненных задач и суммы необходимого на их дней
     * @param idProject
     * @return
     * @throws Exception
     */
    Long getTheRemainingTimeToComplete(Long idProject) throws Exception;



}
