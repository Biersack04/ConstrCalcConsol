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
  private Long daysNeedToCompleted;
  @CsvBindByName
  private String status;

  @CsvCustomBindByName(converter = MaterialsConverter.class)
  private List<Materials> listMaterials;


  //
  // Constructors
  //
/*private String priority;
  @CsvBindByName
  private String startDate;
  @CsvBindByName
  private String status;*/
  
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
   * Set the value of price
   * @param newVar the new value of price
   */
  public void setPrice (Long newVar) {
    price = newVar;
  }

  /**
   * Get the value of price
   * @return the value of price
   */
  public Long getPrice () {
    return price;
  }
    public Long getDaysNeedToCompleted() {
        return daysNeedToCompleted;
    }

    public void setDaysNeedToCompleted(Long daysNeedToCompleted) {
        this.daysNeedToCompleted = daysNeedToCompleted;
    }


  public List<Materials> getListMaterials() {
    return listMaterials;
  }

  public void setListMaterials(List<Materials> listMaterials) {
    this.listMaterials = listMaterials;
  }



    public String getPriority() {
      return priority;
    }

    public void setPriority(String priority) {
      this.priority = priority;
    }

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Works works = (Works) o;
    return id.equals(works.id) && name.equals(works.name) && price.equals(works.price) && priority.equals(works.priority) && daysNeedToCompleted.equals(works.daysNeedToCompleted) && status.equals(works.status) && listMaterials.equals(works.listMaterials);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, price, priority, daysNeedToCompleted, status, listMaterials);
  }

  @Override
  public String toString() {
    return "Works{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", price=" + price +
            ", priority='" + priority + '\'' +
            ", daysNeedToCompleted='" + daysNeedToCompleted + '\'' +
            ", status='" + status + '\'' +
            ", listMaterials=" + listMaterials +
            '}';
  }
}