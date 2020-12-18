package ru.sfedu.constrcaclconsol.bean;

import java.util.Objects;

/**
 * Class Customer
 */
public class Customer extends People {

  //
  // Fields
  //




  private String telephone;
  
  //
  // Constructors
  //
  public Customer(){

  }



  //
  // Methods
  //


  //
  // Accessor methods
  //


  /**
   * Set the value of telephone
   * @param newTelephone the new value of telephone
   */
  public void setTelephone (String newTelephone) {
    telephone = newTelephone;
  }

  /**
   * Get the value of telephone
   * @return the value of telephone
   */
  public String getTelephone () {
    return telephone;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    Customer customer = (Customer) o;
    return Objects.equals(telephone, customer.telephone);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), telephone);
  }

  /** Get the Object value
   * @param o
   * And compare the equality of the current object
   * with the object of same type
   * @return the boolean value of comparisons
   */



  //
  // Other methods
  //

}
