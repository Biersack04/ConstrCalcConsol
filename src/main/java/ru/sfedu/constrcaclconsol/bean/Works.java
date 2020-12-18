package ru.sfedu.constrcaclconsol.bean;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import ru.sfedu.constrcaclconsol.converter.MaterialsConverter;
import java.io.Serializable;
import java.util.*;


/**
 * Class Works
 */
public class Works implements Serializable {

  //
  // Fields
  //
  @CsvBindByName
  private Long id;

  @CsvBindByName
  private String name;

  @CsvBindByName
  private Long price;

  @CsvBindByName
  private String priority;

  @CsvBindByName
  private Long needDaysToCompleted;

  @CsvBindByName
  private String status;

  @CsvCustomBindByName(converter = MaterialsConverter.class)
  private List<Materials> listMaterials;


  //
  // Constructors
  //
  public Works () { };
  
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
   * Set the value of price
   * @param newPrice the new value of price
   */
  public void setPrice (Long newPrice) {
    price = newPrice;
  }

  /**
   * Get the value of price
   * @return the value of price
   */
  public Long getPrice () {
    return price;
  }

  /** Set the value of daysNeedToCompleted
   * @param newDaysNeedToCompleted the value of daysNeedToCompleted
   */
  public void setNeedDaysToCompleted(Long newDaysNeedToCompleted ) {
    this.needDaysToCompleted = newDaysNeedToCompleted ;
  }

  /**
   * Get the value of daysNeedToCompleted
   * @return the value of daysNeedToCompleted
   */
  public Long getNeedDaysToCompleted() { return needDaysToCompleted; }

  /**Set the value of listMaterials
   * @param newListMaterials the value of listMaterials
   */
  public void setListMaterials(List<Materials> newListMaterials) {
    this.listMaterials = newListMaterials;
  }


  /** Get the value of listMaterials
   * @return the value of listMaterials
   */
  public List<Materials> getListMaterials() {
    return listMaterials;
  }

  /** Set the value of priority
   * @param newPriority the value of priority
   */
  public void setPriority(String newPriority) {
    this.priority = newPriority;
  }


  /** Get the value of priority
   * @return the value of priority
   */
    public String getPriority() {
      return priority;
    }

  /** Set the value of status
   * @param newStatus the value of status
   */
  public void setStatus(String newStatus) {
    this.status = newStatus;
  }

  /** Get the value of status
   * @return the value of status
   */
    public String getStatus() {
      return status;
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
    Works works = (Works) o;
    return id.equals(works.id) && name.equals(works.name) && price.equals(works.price) && priority.equals(works.priority) && needDaysToCompleted.equals(works.needDaysToCompleted) && status.equals(works.status) && listMaterials.equals(works.listMaterials);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, price, priority, needDaysToCompleted, status, listMaterials);
  }

  @Override
  public String toString() {
    return "Works{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", price=" + price +
            ", priority='" + priority + '\'' +
            ", daysNeedToCompleted='" + needDaysToCompleted + '\'' +
            ", status='" + status + '\'' +
            ", listMaterials=" + listMaterials +
            '}';
  }
}