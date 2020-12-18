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

  //
  // Constructors
  //

  public Executor(){};


  
  //
  // Methods
  //


  //
  // Accessor methods
  //

  /**
   * Set the value of numberOfCompletedProject
   * @param newNumberOfCompletedProjects the new value of numberOfCompletedProject
   */
  public void setNumberOfCompletedProjects(Long newNumberOfCompletedProjects) {
    numberOfCompletedProjects = newNumberOfCompletedProjects;
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
   * @param newNumberOfWorkers the new value of NumberOfWorkers
   */
  public void setNumberOfWorkers (Long newNumberOfWorkers) {
    numberOfWorkers = newNumberOfWorkers;
  }

  /**
   * Get the value of NumberOfWorkers
   * @return the value of NumberOfWorkers
   */
  public Long getNumberOfWorkers () {
    return numberOfWorkers;
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
