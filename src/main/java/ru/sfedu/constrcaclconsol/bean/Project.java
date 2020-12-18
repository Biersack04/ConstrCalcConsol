package ru.sfedu.constrcaclconsol.bean;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import ru.sfedu.constrcaclconsol.converter.CustomerConverter;
import ru.sfedu.constrcaclconsol.converter.ExecutorConverter;
import ru.sfedu.constrcaclconsol.converter.WorksConverter;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Class Project
 */
public class Project implements Serializable {

  //
  // Fields
  //
  @CsvBindByName
  private Long id;

  @CsvBindByName
  private String name;

  @CsvBindByName
  private String createdDate;

  @CsvBindByName
  private String deadline;

  @CsvBindByName
  private Integer numberOfWorkers;

  @CsvCustomBindByName(converter = WorksConverter.class)
  private List<Works> worksList;

  @CsvBindByName
  private String projectAddress;

  @CsvCustomBindByName(converter = ExecutorConverter.class)
  private People executor;

  @CsvCustomBindByName(converter = CustomerConverter.class)
  private People customer;
  
  //
  // Constructors
  //
  public Project () { };
  
  //
  // Methods
  //


  //
  // Accessor methods
  //

  /**
   * Set the value of id
   * @param newId the new value of id
   */
  public void setId (Long newId) {
    id = newId;
  }

  /**
   * Get the value of id
   * @return the value of id
   */
  public Long getId () {
    return id;
  }

  /**
   * Set the value of name
   * @param newName the new value of name
   */
  public void setName (String newName) {
    name = newName;
  }

  /**
   * Get the value of name
   * @return the value of name
   */
  public String getName () {
    return name;
  }

  /**
   * Set the value of createdDate
   * @param newCreatedDate the new value of createdDate
   */
  public void setCreatedDate (String newCreatedDate) {
    createdDate = newCreatedDate;
  }

  /**
   * Get the value of createdDate
   * @return the value of createdDate
   */
  public String getCreatedDate () {
    return createdDate;
  }

  /**
   * Set the value of deadline
   * @param newDeadline the new value of deadline
   */
  public void setDeadline (String newDeadline) {
    deadline = newDeadline;
  }

  /**
   * Get the value of deadline
   * @return the value of deadline
   */
  public String getDeadline () {
    return deadline;
  }

  /**
   * Set the value of numberOfWorks
   * @param newNumberOfWorks  the new value of numberOfWorks
   */
  public void setNumberOfWorkers(Integer newNumberOfWorks ) {
    numberOfWorkers = newNumberOfWorks ;
  }

  /**
   * Get the value of numberOfWorks
   * @return the value of numberOfWorks
   */
  public Integer getNumberOfWorkers() {
    return numberOfWorkers;
  }

  /**
   * Set the value of worksList
   * @param newWorksList the new value of worksList
   */
  public void setWorksList (List<Works> newWorksList) {
    worksList = newWorksList;
  }

  /**
   * Get the value of worksList
   * @return the value of worksList
   */
  public List<Works> getWorksList () {
    return worksList;
  }

  /**
   * Set the value of adress
   * @param newAddress the new value of adress
   */
  public void setProjectAddress(String newAddress) {
    projectAddress = newAddress;
  }

  /**
   * Get the value of adress
   * @return the value of adress
   */
  public String getProjectAddress() {
    return projectAddress;
  }

  /**
   * Set the value of executor
   * @param newExecutor the new value of executor
   */
  public void setExecutor (People newExecutor) {
    executor = newExecutor;
  }

  /**
   * Get the value of executor
   * @return the value of executor
   */
  public People getExecutor () {
    return executor;
  }

  /**
   * Set the value of customer
   * @param newCustomer  the new value of customer
   */
  public void setCustomer (People newCustomer ) {
    customer = newCustomer ;
  }

  /**
   * Get the value of customer
   * @return the value of customer
   */
  public People getCustomer () {
    return customer;
  }

  /** Get the Object value
   * @param o
   * And compare the equality of the current object
   * with the object of same type
   * @return the boolean value of comparisons
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Project project = (Project) o;
    return id.equals(project.id) &&
            name.equals(project.name) &&
            createdDate.equals(project.createdDate) &&
            deadline.equals(project.deadline) &&
            numberOfWorkers.equals(project.numberOfWorkers) &&
            worksList.equals(project.worksList) &&
            projectAddress.equals(project.projectAddress) &&
            executor.equals(project.executor) &&
            customer.equals(project.customer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, createdDate, deadline, numberOfWorkers, worksList, projectAddress, executor, customer);
  }

  @Override
  public String toString() {
    return "Project{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", createdDate='" + createdDate + '\'' +
            ", deadline='" + deadline + '\'' +
            ", numberOfWorks=" + numberOfWorkers +
            ", worksList=" + worksList +
            ", address='" + projectAddress + '\'' +
            ", executor=" + executor +
            ", customer=" + customer +
            '}';
  }

  //
  // Other methods
  //

}
