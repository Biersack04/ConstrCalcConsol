package ru.sfedu.constrcaclconsol.bean;
import java.util.*;


/**
 * Class Works
 */
public class Works {

  //
  // Fields
  //

  private Long id;
  private String name;
  private Long price;
  private PriorityType priority;
  private String startDate;
  private StatusOfCompletion status;
  private String stopDate;
  private List<Materials> listMaterials;


  //
  // Constructors
  //

  
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStopDate() {
        return stopDate;
    }

    public void setStopDate(String stopDate) {
        this.stopDate = stopDate;
    }

    public PriorityType getPriority() {
        return priority;
    }

    public void setPriority(PriorityType priority) {
        this.priority = priority;
    }

    public StatusOfCompletion getStatus() {
        return status;
    }

    public void setStatus(StatusOfCompletion status) {
        this.status = status;
    }

  public List<Materials> getListMaterials() {
    return listMaterials;
  }

  public void setListMaterials(List<Materials> listMaterials) {
    this.listMaterials = listMaterials;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Works works = (Works) o;
    return id.equals(works.id) &&
            name.equals(works.name) &&
            price.equals(works.price) &&
            priority == works.priority &&
            startDate.equals(works.startDate) &&
            status == works.status &&
            stopDate.equals(works.stopDate) &&
            listMaterials.equals(works.listMaterials);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, price, priority, startDate, status, stopDate, listMaterials);
  }

  @Override
  public String toString() {
    return "Works{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", price=" + price +
            ", priority=" + priority +
            ", startDate='" + startDate + '\'' +
            ", status=" + status +
            ", stopDate='" + stopDate + '\'' +
            ", listMaterials=" + listMaterials +
            '}';
  }


  public enum PriorityType{
  HIGH,
  MEDIUM,
  LOW
}


public enum StatusOfCompletion {
  CREATE,
  PROCESSING,
  COMPLETED
}
}