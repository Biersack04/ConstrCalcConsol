package ru.sfedu.constrcaclconsol;

public class Constants {
    public static final String SOURCE =  "source";

    static public final String WORKS_ENVIROMENT_PATH="./src/main/resources/works.properties";
    static public final String MATERIALS_ENVIROMENT_PATH="./src/main/resources/material.properties";
    static public final String CUSTOM_CONFIG_PATH = System.getProperty("configPath");

    static public final int WORKS_NUMBER=10;
    static public final String NAME_WORKS="work";
    static public final int MATERIALS_NUMBER=20;
    static public final String NAME_MATERIAL="material";
    static public final String SPLIT =",";
    static public final String LIST_START_SYMBOL ="[";
    static public final String LIST_END_SYMBOL ="]";
    static public final String PEOPLE_START_SYMBOL ="{";
    static public final String PEOPLE_END_SYMBOL ="}";
    static public final String CANNOT_CREATE_FILE = "CANNOT_CREATE_FILE";
    public static final String PATH_CSV ="csv_path";
    public static final String FILE_EXTENSION_CSV = "csv";
    public static final String PATH_XML ="xml_path";
    public static final String FILE_EXTENSION_XML = "xml";
    static public final String START_DAY="Start";
    static public final String END_DAY="End";
    static public final String SPACE=" ";
    static public final String CREATE="CREATE";
    static public final String NULL="NULL";
    static public final String LIST_IS_EMPTY = "list_is_empty";
    public static final String NULL_VALUE = "null_value";

    static public final String GET_FROM_CSV = "get_from_csv";
    static public final String GET_MATERIAL_LIST = "get_material_list";
    static public final String GET_CUSTOMER_IN_PROJECT = "get_customer_in_project";
    static public final String GET_EXECUTOR_IN_PROJECT = "get_executor_in_project";
    static public final String GET_WORKS_LIST = "get_works_list";
    static public final String WRITE_TO_CSV = "write_to_csv";
    static public final String WRITE_SUCCESS = "write_success";
    static public final String WRITE_FAIL = "write_to_fail";

    static public final String CREATE_MATERIALS = "create_materials";
    static public final String GET_MATERIALS = "get_materials";
    static public final String UPDATE_MATERIALS = "update_materials";
    static public final String DELETE_MATERIALS= "delete_materials";

    static public final String CREATE_WORKS = "create_works";
    static public final String GET_WORKS = "get_works";
    static public final String UPDATE_WORKS = "update_works";
    static public final String DELETE_WORKS= "delete_works";

    static public final String CREATE_EXECUTOR = "create_executor";
    static public final String GET_EXECUTOR = "get_executor";
    static public final String UPDATE_EXECUTOR = "update_executor";
    static public final String DELETE_EXECUTOR= "delete_executor";

    static public final String CREATE_CUSTOMER = "create_customer";
    static public final String GET_CUSTOMER = "get_customer";
    static public final String UPDATE_CUSTOMER = "update_customer";
    static public final String DELETE_CUSTOMER= "delete_customer";

    static public final String CREATE_PROJECT = "create_project";
    static public final String GET_PROJECT = "get_project";
    static public final String UPDATE_PROJECT = "update_project";
    static public final String DELETE_PROJECT= "delete_project";

    static public final String PROJECT_REPORT = "project_report";

    static public final String CREATED_DATE = "created_date";
    static public final String DEADLINE = "deadline2";
    static public final String NUMBER_OF_WORKS= "number_of_works";
    static public final String WORKS_LIST = "works_list";
    static public final String PROJECT_ADDRESS = "project_address";
    static public final String EXECUTOR = "executor";


    static public final String ESTIMATE_REPORT = "estimate_report";
    static public final String PRICE_FOR_WORKS = "price_for_works";
    static public final String PRICE_FOR_MATERIALS = "price_for_materials";
    static public final String ESTIMATE_TOTAL_PRICE = "estimate_total_price";

    static public final String DEADLINE_REPORT = "deadline_report";
    static public final String NEED_DAYS = "need_days";

    static public final String GET_THE_COST_OF_WORKS = "get_the_cost_of_works";
    static public final String GET_THE_COST_OF_MATERIALS= "get_the_cost_of_materials";
    static public final String CALCULATING_ESTIMATE= "calculating_estimate" ;
    static public final String CALCULATING_DEADLINE= "calculating_deadline";
    static public final String MARK_THE_STATUS_OF_WORK= "mark_the_status_of_work" ;
    static public final String GET_THE_PROGRESS_REPORT= "get_progress_report";
    static public final String GET_THE_REMAINING_TIME= "get_the_remaining_time" ;

    public static final String JDBC_DRIVER = "db_driver";
    public static final String DB_URL = "db_url";
    public static final String DB_USER = "db_user";
    public static final String DB_PASS = "db_pass";

    public static final String ERROR_ID = "error_id";

    public static final String DB_INSERT = "INSERT INTO %s (%s) VALUES (%s)";

    public static final String DB_SELECT = "SELECT * FROM %s WHERE id=%d";
    public static final String DB_DELETE = "DELETE FROM %s WHERE id=%d";
    public static final String DROP_TABLES ="DROP TABLE Customer, Executor, Materials, Works, listMaterials, Project, listWorks";

    public static final String REGEX = "\\|";

    static public final String ID = "id";
    static public final String NAME = "name";
    static public final String NUMBER = "number";
    static public final String PRICE_FOR_ONE= "price_for_one";


    public static final String MATERIALS_INSERT = "insert into Materials(id,name,number,price_for_one) values(?, ?, ?, ? )";
    public static final String NOT_DELETED = "NOT_DELETED";
    public static final String DELETED = "DELETED";
    public static final String DB_CREATE = "src/main/resources/create.sql";

    public static final String DB_UPDATE_MATERIALS = "UPDATE Materials SET name='%s', number='%s', price_for_one='%s' WHERE id=%d";

    static public final String CUSTOMER = "customer";
    static public final String SURNAME = "surname";
    static public final String MAILBOX = "mailbox";
    static public final String TELEPHONE = "telephone";
    static public final String NUMBER_OF_COMPL_PROJECT = "numberOfCompletedProjects";
    static public final String NUMBER_OF_WORKERS = "numberOfWorkers";

    public static final String CUSTOMER_FIELDS = NAME+","+SURNAME+","+MAILBOX+","+TELEPHONE;
    public static final String CUSTOMER_INSERT_FORMAT ="'%s','%s','%s','%s'";

    public static final String DB_UPDATE_CUSTOMER = "UPDATE Customer SET name='%s', surname='%s', mailbox='%s', telephone='%s' WHERE id=%d";


    public static final String EXECUTOR_FIELDS = NAME+","+SURNAME+","+MAILBOX+","+NUMBER_OF_COMPL_PROJECT+","+NUMBER_OF_WORKERS;
    public static final String EXECUTOR_INSERT_FORMAT ="'%s','%s','%s','%s','%s'";

    public static final String DB_UPDATE_EXECUTOR = "UPDATE EXECUTOR SET name='%s', surname='%s', mailbox='%s', numberOfCompletedProjects='%s', numberOfWorkers='%s' WHERE id=%d";

    public static final String WORKS_INSERT = "insert into WORKS(name,price,priority,needDaysToCompleted,status) values(?, ?, ?, ?, ?)";
    public static final String ID_MATERIAL ="id_material";
    public static final String ID_WORK ="id_work";
    public static final String LIST_MATERIAL_FIELDS =ID_WORK+","+ID_MATERIAL;
    public static final String LIST_FORMAT ="'%s','%s'";
    public static final String DB_SELECT_LIST_MATERIALS = "SELECT DISTINCT id_material FROM %s WHERE id_work=%d ORDER BY id_material";

    public static final String WORK_LIST_MATERIAL ="listMaterials";

    public static final String QUOTATION_MARK = "'";
    public static final String GET_CUSTOMER_ID = "SELECT id FROM Customer WHERE name = '";
    public static final String GET_EXECUTOR_ID = "SELECT id FROM Executor WHERE name = '";
    public static final String GET_PROJECT_ID = "SELECT id FROM Project WHERE name = '";
    public static final String PROJECT_LIST_WORK ="listWorks";
    public static final String ID_PROJECT ="id_project";
    public static final String LIST_WORK_FIELDS =ID_PROJECT+","+ID_WORK;
    public static final String DB_SELECT_LIST_WORKS = "SELECT DISTINCT id_work FROM %s WHERE id_project=%d ORDER BY id_work";
    public static final String CREATED__DATE = "createdDate";
    static public final String DEADLINE_INSERT = "deadline";
    static public final String NUMBER_WORKS= "numberOfWorkers";
    static public final String PROJECTADDRESS = "projectAddress";

    public static final String ID_CUSTOMER ="id_customer";
    public static final String ID_EXECUTOR ="id_executor";

    public static final String PROJECT_FIELDS = NAME+","+CREATED__DATE+","+DEADLINE_INSERT+","+NUMBER_WORKS+","+ PROJECTADDRESS +","+ ID_CUSTOMER +","+ID_EXECUTOR;
    public static final String PROJECT_INSERT_FORMAT ="'%s','%s','%s','%s','%s','%s','%s'";

    public static final String PRICE="price";
    public static final String PRIORITY ="priority";
    public static final String NEED_DAYS_TO_COMPL ="needDaysToCompleted";
    public static final String STATUS ="status";


    public static final String DB_DELETE_LIST_MATERIALS = "DELETE FROM %s WHERE id_work=%d";
    public static final String DB_UPDATE_WORK = "UPDATE WORKS SET name='%s', price='%s', priority='%s', needDaysToCompleted='%s', status='%s' WHERE id=%d";

    public static final String DB_DELETE_LIST_WORKS = "DELETE FROM %s WHERE id_project=%d";
    public static final String DB_UPDATE_PROJECT = "UPDATE PROJECT SET name='%s', createdDate='%s', deadline='%s', numberOfWorkers='%s', projectAddress='%s', id_customer='%s', id_executor='%s' WHERE id=%d";



}
