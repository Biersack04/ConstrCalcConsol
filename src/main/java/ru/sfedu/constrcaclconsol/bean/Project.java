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
  private Integer numberOfWorks;
  @CsvCustomBindByName(converter = WorksConverter.class)
  private List<Works> worksList;
  @CsvBindByName
  private String address;
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
   * @param newVar the new value of id
   */
  public void setId (Long newVar) {
    id = newVar;
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
   * @param newVar the new value of name
   */
  public void setName (String newVar) {
    name = newVar;
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
   * @param newVar the new value of createdDate
   */
  public void setCreatedDate (String newVar) {
    createdDate = newVar;
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
   * @param newVar the new value of deadline
   */
  public void setDeadline (String newVar) {
    deadline = newVar;
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
   * @param newVar the new value of numberOfWorks
   */
  public void setNumberOfWorks (Integer newVar) {
    numberOfWorks = newVar;
  }

  /**
   * Get the value of numberOfWorks
   * @return the value of numberOfWorks
   */
  public Integer getNumberOfWorks () {
    return numberOfWorks;
  }

  /**
   * Set the value of worksList
   * @param newVar the new value of worksList
   */
  public void setWorksList (List<Works> newVar) {
    worksList = newVar;
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
   * @param newVar the new value of adress
   */
  public void setAddress(String newVar) {
    address = newVar;
  }

  /**
   * Get the value of adress
   * @return the value of adress
   */
  public String getAddress() {
    return address;
  }

  /**
   * Set the value of executor
   * @param newVar the new value of executor
   */
  public void setExecutor (People newVar) {
    executor = newVar;
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
   * @param newVar the new value of customer
   */
  public void setCustomer (People newVar) {
    customer = newVar;
  }

  /**
   * Get the value of customer
   * @return the value of customer
   */
  public People getCustomer () {
    return customer;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Project project = (Project) o;
    return id.equals(project.id) &&
            name.equals(project.name) &&
            createdDate.equals(project.createdDate) &&
            deadline.equals(project.deadline) &&
            numberOfWorks.equals(project.numberOfWorks) &&
            worksList.equals(project.worksList) &&
            address.equals(project.address) &&
            executor.equals(project.executor) &&
            customer.equals(project.customer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, createdDate, deadline, numberOfWorks, worksList, address, executor, customer);
  }

  @Override
  public String toString() {
    return "Project{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", createdDate='" + createdDate + '\'' +
            ", deadline='" + deadline + '\'' +
            ", numberOfWorks=" + numberOfWorks +
            ", worksList=" + worksList +
            ", address='" + address + '\'' +
            ", executor=" + executor +
            ", customer=" + customer +
            '}';
  }

  //
  // Other methods
  //

}
