package ru.sfedu.constrcaclconsol.bean;

import java.util.Objects;

/**
 * Class Executor
 */
public class Executor extends People {

  //
  // Fields
  //

  private Integer numberOfCompletedProjects;
  private Integer numberOfWorkers;

  public Executor(){};

/*  public Executor(Long id, String name, String surname, String mailbox) {
    super(id, name, surname, mailbox);
  }*/

  //
  // Constructors
  //
  /*public Executor () {
    super(id);
  };*/
  
  //
  // Methods
  //


  //
  // Accessor methods
  //

  /**
   * Set the value of numberOfCompletedProject
   * @param newVar the new value of numberOfCompletedProject
   */
  public void setNumberOfCompletedProjects(Integer newVar) {
    numberOfCompletedProjects = newVar;
  }

  /**
   * Get the value of numberOfCompletedProject
   * @return the value of numberOfCompletedProject
   */
  public Integer getNumberOfCompletedProjects() {
    return numberOfCompletedProjects;
  }

  /**
   * Set the value of NumberOfWorkers
   * @param newVar the new value of NumberOfWorkers
   */
  public void setNumberOfWorkers (Integer newVar) {
    numberOfWorkers = newVar;
  }

  /**
   * Get the value of NumberOfWorkers
   * @return the value of NumberOfWorkers
   */
  public Integer getNumberOfWorkers () {
    return numberOfWorkers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Executor executor = (Executor) o;
    return numberOfCompletedProjects.equals(executor.numberOfCompletedProjects) &&
            numberOfWorkers.equals(executor.numberOfWorkers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(numberOfCompletedProjects, numberOfWorkers);
  }

  @Override
  public String toString() {
    return "Executor{" +
            "numberOfCompletedProjects=" + numberOfCompletedProjects +
            ", NumberOfWorkers=" + numberOfWorkers +
            '}';
  }

  //
  // Other methods
  //

}
