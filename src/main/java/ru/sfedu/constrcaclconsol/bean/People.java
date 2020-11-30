package ru.sfedu.constrcaclconsol.bean;

import java.util.Objects;

/**
 * Class People
 */
public class People {

  //
  // Fields
  //

  private Long id;
  private String name;
  private String surname;
  private String mailbox;
  
  //
  // Constructors
  //
  public People() {

  }

  public People(Long id,String name, String surname, String mailbox) { };



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
   * Set the value of surname
   * @param newVar the new value of surname
   */
  public void setSurname (String newVar) {
    surname = newVar;
  }

  /**
   * Get the value of surname
   * @return the value of surname
   */
  public String getSurname () {
    return surname;
  }

  /**
   * Set the value of email
   * @param newVar the new value of email
   */
  public void setMailbox(String newVar) {
    mailbox = newVar;
  }

  /**
   * Get the value of email
   * @return the value of email
   */
  public String getMailbox() {
    return mailbox;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    People people = (People) o;
    return id.equals(people.id) &&
            name.equals(people.name) &&
            surname.equals(people.surname) &&
            mailbox.equals(people.mailbox);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, surname, mailbox);
  }

  @Override
  public String toString() {
    return "People{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", surname='" + surname + '\'' +
            ", mailbox='" + mailbox + '\'' +
            '}';
  }
}
