package ru.sfedu.constrcaclconsol.bean;

import java.util.Objects;

/**
 * Class Executor
 */
public class Executor extends People {

  //
  // Fields
  //

  private Long numberOfCompletedProjects;
  private Long numberOfWorkers;

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
  public void setNumberOfCompletedProjects(Long newVar) {
    numberOfCompletedProjects = newVar;
  }

  /**
   * Get the value of numberOfCompletedProject
   * @return the value of numberOfCompletedProject
   */
  public Long getNumberOfCompletedProjects() {
    return numberOfCompletedProjects;
  }

  /**
   * Set the value of NumberOfWorkers
   * @param newVar the new value of NumberOfWorkers
   */
  public void setNumberOfWorkers (Long newVar) {
    numberOfWorkers = newVar;
  }

  /**
   * Get the value of NumberOfWorkers
   * @return the value of NumberOfWorkers
   */
  public Long getNumberOfWorkers () {
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
            "id=" + super.getId()+
            ", name=" + super.getName() +
            ", surname=" + super.getSurname() +
            ", mailbox=" + super.getMailbox() +
            ", numberOfCompletedProjects=" + numberOfCompletedProjects +
            ", NumberOfWorkers=" + numberOfWorkers +
            '}';
  }

  //
  // Other methods
  //

}
