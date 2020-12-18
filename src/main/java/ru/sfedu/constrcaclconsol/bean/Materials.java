package ru.sfedu.constrcaclconsol.bean;

import com.opencsv.bean.CsvBindByName;
import java.io.Serializable;
import java.util.Objects;

/**
 * Class Materials
 */
public class Materials implements Serializable {

  //
  // Fields
  //
  @CsvBindByName
  private Long id;

  @CsvBindByName
  private String name;

  @CsvBindByName
  private Long number;

  @CsvBindByName
  private Long priceForOne;
  
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
   * Set the value of Number
   * @param newNumber the new value of Number
   */
  public void setNumber (Long newNumber) {
    number = newNumber;
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
   * @param newTotalPrice the new value of totalPrice
   */
  public void setPriceForOne(Long newTotalPrice) {
    priceForOne = newTotalPrice;
  }

  /**
   * Get the value of totalPrice
   * @return the value of totalPrice
   */
  public Long getPriceForOne() {
    return priceForOne;
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
    Materials materials = (Materials) o;
    return id.equals(materials.id) &&
            name.equals(materials.name) &&
            number.equals(materials.number) &&
            priceForOne.equals(materials.priceForOne);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, number, priceForOne);
  }

  @Override
  public String toString() {
    return "Materials{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", number=" + number +
            ", totalPrice=" + priceForOne +
            '}';
  }


  //
  // Other methods
  //

}
