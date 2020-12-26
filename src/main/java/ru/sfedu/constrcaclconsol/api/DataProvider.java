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

    /** Создание списка материалов
     * @param listMaterials список материалов
     * @return возвращает булевское значеие в зависсимости от результата выполнения
     * @throws IOException
     * @throws CsvDataTypeMismatchException
     * @throws CsvRequiredFieldEmptyException
     */
    boolean createMaterials(List<Materials> listMaterials) throws Exception;

    /** Получение экземпляра материалов по его id
     * @param materialId id материала
     * @return объект класса Material
     * @throws IOException
     */
    Optional<Materials> getMaterialById(long materialId) throws Exception;

    /** Удаление материала из списка материалов по его id
     * @param id id материала
     * @return результат выполнения
     * @throws IOException
     */
    boolean deleteMaterialById(long id) throws Exception;

    /** Обновление экземпляра класса Material
     * @param id id
     * @param name название
     * @param number количество этого материала, которое необходимо
     * @param priceForOne цена за штуку
     * @return  возвращает булевское значеие в зависсимости от результата выполнения
     * @throws IOException
     */
    boolean updateMaterial(long id, String name, Long number, Long priceForOne) throws Exception;


    //Works

    /** Создание списка работ для проекта
     * @param listWorks список работ
     * @return возвращает булевское значеие в зависсимости от результата выполнения
     * @throws IOException
     * @throws CsvDataTypeMismatchException
     * @throws CsvRequiredFieldEmptyException
     */
    boolean createWork(List<Works> listWorks) throws Exception;

    /** Нахождение работы по ее id
     * @param id id работы
     * @return  экземпляр класса, который нашел
     */
    Optional<Works> getWork(long id) throws Exception;

    /** Удаление работы по id
     * @param id id
     * @return  возвращает булевское значеие в зависсимости от результата выполнения
     * @throws IOException
     */
    boolean deleteWorkById(long id) throws Exception;

    /** Обновление всей информации у экземпляра класса
     * @param id id
     * @param name название
     * @param price цена за выполнение
     * @param priority приоритет выполнения
     * @param daysToCompletedWorks  количество дней, необходимых для выполнения
     * @param status  статус работы
     * @param listMaterials  список материалов
     * @return возвращает булевское значеие в зависсимости от результата выполнения
     * @throws IOException
     */
    boolean updateWork(Long id, String name, Long price, String priority,
                       Long daysToCompletedWorks, String status, List<Materials> listMaterials ) throws Exception;


    //Customer

    /** Создание Заказчика
     *
     * @param name Имя
     * @param surname Фамилия
     * @param mailbox email
     * @param telephone телефон
     * @return возвращает булевское значеие в зависсимости от результата выполнения
     * @throws IOException
     * @throws CsvDataTypeMismatchException
     * @throws CsvRequiredFieldEmptyException
     */
    boolean createCustomer(String name, String surname, String mailbox, String telephone) throws Exception;

        /** Нахождение по id Заказчика
         * @param id id
         * @return возвращает булевское значеие в зависсимости от результата выполнения
         * @throws IOException
         */
    Optional<Customer> getCustomerById(long id) throws Exception;

    /** Удаляет Заказчика по id
     * @param id id
     * @return возвращает булевское значеие в зависсимости от результата выполнения
     * @throws IOException
     */
    boolean deleteCustomerById(long id) throws Exception;

    /** Обновляет всю информацию по Заказчику
     * @param id id
     * @param name имя
     * @param surname фамилия
     * @param mailbox email
     * @param telephone телефон
     * @return возвращает булевское значеие в зависсимости от результата выполнения
     * @throws IOException
     */
    boolean updateCustomer(long id, String name, String surname, String mailbox, String telephone ) throws Exception;


    //Executor

    /** Создание Исполнителя
     *
     * @param name имя
     * @param surname фамилия
     * @param mailbox email
     * @param numberOfCompletedProjects количество законченных работ
     * @param numberOfWorkers количество работников
     * @return возвращает булевское значеие в зависсимости от результата выполнения
     * @throws IOException
     * @throws CsvDataTypeMismatchException
     * @throws CsvRequiredFieldEmptyException
     */
    boolean createExecutor( String name, String surname, String mailbox, Long numberOfCompletedProjects, Long numberOfWorkers) throws Exception;

    /** Находит Исполнителя по id
     * @param id id Исполнителя
     * @return экземляр класса Executor
     * @throws IOException
     */
    Optional<Executor> getExecutorById(long id) throws Exception;

    /** Удаляет Исполнителя по его id
     * @param id id Исполнителя
     * @return возвращает булевское значеие в зависсимости от результата выполнения
     * @throws IOException
     */
    boolean deleteExecutorById(long id) throws Exception;

    /** Обновление все информации у Исполнителя по его id
     * @param id the value of id of the executor
     * @param name имя
     * @param surname фамилия
     * @param mailbox email
     * @param numberOfCompletedProjects количество законченных проектов
     * @param numberOfWorkers количество работников
     * @return возвращает булевское значеие в зависсимости от результата выполнения
     * @throws IOException
     */
    boolean updateExecutor(long id, String name, String surname, String mailbox, Long numberOfCompletedProjects, Long numberOfWorkers ) throws Exception;


    //Project

    /**
     *Создание проекта, на основе созланного заранее списка работ и материалов, указывается информация о заказчике и исполнителе, производятся расчеты
     * сметы и сроков выполнения, при необходимости создаются отчеты
     * @param name название
     * @param createdDate дата создания
     * @param deadline дата окончания
     * @param numberOfWorkers количество работников
     * @param worksList список работ
     * @param address адресс
     * @param executor Исполнитель
     * @param customer Заказчик
     * @param isCreateEstimateReport  флаг необходимости создания отчета по смете
     * @param isCreateDeadlineReport флаг необходимости создания отчета по времени выполнения
     * @return возвращает булевское значеие в зависсимости от результата выполнения
     * @throws Exception
     */
    boolean createProject(String name, String createdDate,
                          String deadline, Integer numberOfWorkers, List<Works> worksList, String address,
                          People executor, People customer, boolean isCreateEstimateReport, boolean isCreateDeadlineReport ) throws Exception;

    /** Находит проект по его id
     * @param id  id проекта
     * @return
     */
    Optional<Project> getProject(long id) throws Exception;

    /** Удаление проекта с созданием или без создания отчета
     * @param id проекта
     *  @param createReport флаг создания отчета
     * @return возвращает булевское значеие в зависсимости от результата выполнения
     * @throws IOException
     */
    boolean deleteProject(long id, boolean createReport) throws Exception;

    /** Обновление всей информации о проекте
     * @param id
     * @param name название
     * @param createdDate дата создания
     * @param deadline дата окончания
     * @param numberOfWorkers количество работников
     * @param worksList список работ
     * @param address адресс
     * @param executor Исполнитель
     * @param customer Заказчик
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
    Long getTheCostOfWorksInProject(long idProject) throws Exception;

    /**Получение стоимости материалов, которые будут необходимы для выполнения работ в  проекте
     * @param idProject - id проекта
     * @return Long materialPrice
     * @throws IOException
     */
    Long getTheCostOfMaterialsInProject(long idProject) throws Exception;

    //CRUD PROJECT
    Long calculatingEstimate(List<Works> worksList, boolean createReport) throws Exception;

    /**Расчёт необходимого количества дней для выполнения проекта
     * @param createReport - флаг для вызова дополнительного метода создания отчета
     * @return Long projectDeadline - итоговое количество дней
     * @throws Exception
     */

    Long calculatingDeadline(List<Works> worksList, long idProject, boolean createReport) throws Exception;

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
