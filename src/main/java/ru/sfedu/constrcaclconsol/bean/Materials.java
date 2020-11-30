package ru.sfedu.constrcaclconsol.bean;

import java.util.Objects;

/**
 * Class Materials
 */
public class Materials {

  //
  // Fields
  //

  private Long id;
  private String name;
  private Long number;
  private Long totalPrice;
  
  //
  // Constructors
  //
  public Materials () { };
  
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
   * Set the value of Number
   * @param newVar the new value of Number
   */
  public void setNumber (Long newVar) {
    number = newVar;
  }

  /**
   * Get the value of Number
   * @return the value of Number
   */
  public Long getNumber () {
    return number;
  }

  /**
   * Set the value of totalPrice
   * @param newVar the new value of totalPrice
   */
  public void setTotalPrice (Long newVar) {
    totalPrice = newVar;
  }

  /**
   * Get the value of totalPrice
   * @return the value of totalPrice
   */
  public Long getTotalPrice () {
    return totalPrice;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Materials materials = (Materials) o;
    return id.equals(materials.id) &&
            name.equals(materials.name) &&
            number.equals(materials.number) &&
            totalPrice.equals(materials.totalPrice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, number, totalPrice);
  }

  @Override
  public String toString() {
    return "Materials{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", number=" + number +
            ", totalPrice=" + totalPrice +
            '}';
  }


  //
  // Other methods
  //

}
