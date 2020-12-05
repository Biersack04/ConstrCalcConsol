package ru.sfedu.constrcaclconsol.bean;

import java.util.Objects;

/**
 * Class Customer
 */
public class Customer extends People {

  //
  // Fields
  //

  private String orderAddress;

  private String telephone;
  
  //
  // Constructors
  //
  public Customer(){

  }

 /*

  public Customer(Long id, String name, String surname, String mailbox, String address,String telephone) {
    super(id,name,surname,mailbox);
    this.address=address;
    this.telephone=telephone;
  }
*/

  //
  // Methods
  //


  //
  // Accessor methods
  //

  /**
   * Set the value of adress
   * @param newVar the new value of adress
   */
  public void setOrderAddress(String newVar) {
    orderAddress = newVar;
  }

  /**
   * Get the value of adress
   * @return the value of adress
   */
  public String getOrderAddress() {
    return orderAddress;
  }

  /**
   * Set the value of telephone
   * @param newVar the new value of telephone
   */
  public void setTelephone (String newVar) {
    telephone = newVar;
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
    return orderAddress.equals(customer.orderAddress) && telephone.equals(customer.telephone);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), orderAddress, telephone);
  }

  @Override
  public String toString() {
    return "Customer{" +
            "id=" + super.getId()+
            ", name=" + super.getName() +
            ", surname=" + super.getSurname() +
            ", mailbox=" + super.getMailbox() +
            ",orderAddress='" + orderAddress + '\'' +
            ", telephone='" + telephone + '\'' +
            '}';
  }


  //
  // Other methods
  //

}
