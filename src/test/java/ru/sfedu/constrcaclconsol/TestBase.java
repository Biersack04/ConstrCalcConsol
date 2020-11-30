package ru.sfedu.constrcaclconsol;

import ru.sfedu.constrcaclconsol.bean.*;

import java.util.List;

public class TestBase {

 /*   public Project createProject (long id, String name, Date createDate, Date deadline, Integer numberOfWorks,
                                  List<Works> worksList, String address, People executor, People customer){

        Project project = new Project();

        project.setId(id);
        project.setName(name);
        project.setCreatedDate(createDate);
        project.setDeadline(deadline);
        project.setNumberOfWorks(numberOfWorks);
        project.setWorksList(worksList);
        project.setAddress(address);
        project.setExecutor(executor);
        project.setCustomer(customer);

        return project;
    }
*/
  public Works createWorks(long id, String name, long price,
                             String startDate, String endDate, Works.StatusOfCompletion status,
                             Works.PriorityType priority, List<Materials> listOfMaterials){

        Works works = new Works();

        works.setId(id);
        works.setName(name);
        works.setPrice(price);
        works.setStartDate(startDate);
        works.setStopDate(endDate);
        works.setStatus(status);
        works.setPriority(priority);
        works.setListMaterials(listOfMaterials);

        return works;
    }


    public static Materials createMaterials(long id, String name, long number, long totalPrice){

        Materials materials = new Materials();

        materials.setId(id);
        materials.setName(name);
        materials.setNumber(number);
        materials.setTotalPrice(totalPrice);

        return materials;
    }

       public People createPeople(long id, String name, String surname, String mailbox){

       // People people = new People(id,name,surname, mailbox);
           People people = new People();

        people.setId(id);
        people.setName(name);
        people.setSurname(surname);
        people.setMailbox(mailbox);

        return people;

    }
/*
    public Executor createExecutor (Integer numberOfCompletedProjects, Integer NumberOfWorkers){

        Executor executor = new Executor();

        executor.setNumberOfCompletedProjects(numberOfCompletedProjects);
        executor.setNumberOfWorkers(NumberOfWorkers);

        return executor;

    }
*/
    public Customer createCustomer(Long id, String name, String surname, String mailbox, String address, String telephone){

        //Customer customer = new Customer(id, name, surname, mailbox, address, telephone);
        Customer customer = new Customer();

        customer.setId(id);
        customer.setName(name);
        customer.setSurname(surname);
        customer.setMailbox(mailbox);
        customer.setOrderAddress(address);
        customer.setTelephone(telephone);

        return customer;
    }

    public Executor createExecutor(Long id, String name, String surname, String mailbox, int numberOfCompletedProjects, int NumberOfWorkers){

        //Customer customer = new Customer(id, name, surname, mailbox, address, telephone);
        Executor executor = new Executor();

        executor.setId(id);
        executor.setName(name);
        executor.setSurname(surname);
        executor.setMailbox(mailbox);
        executor.setNumberOfCompletedProjects(numberOfCompletedProjects);
        executor.setNumberOfWorkers(NumberOfWorkers);

        return executor;
    }



}
