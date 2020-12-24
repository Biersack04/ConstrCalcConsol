package ru.sfedu.constrcaclconsol;

import ru.sfedu.constrcaclconsol.bean.*;

import java.util.List;

public class TestBase {

    public Project createProject (long id, String name, String createDate, String deadline, Integer numberOfWorks,
                                  List<Works> worksList, String address, People executor, People customer){

        Project project = new Project();

        project.setId(id);
        project.setName(name);
        project.setCreatedDate(createDate);
        project.setDeadline(deadline);
        project.setNumberOfWorkers(numberOfWorks);
        project.setWorksList(worksList);
        project.setProjectAddress(address);
        project.setExecutor(executor);
        project.setCustomer(customer);

        return project;
    }

  public static Works createWorks(long id, String name, long price,
                                  Long daysNeedToCompleted, String status,
                                  String priority, List<Materials> listOfMaterials){

        Works works = new Works();

        works.setId(id);
        works.setName(name);
        works.setPrice(price);
        works.setNeedDaysToCompleted(daysNeedToCompleted);
        works.setStatus(status);
        works.setPriority(priority);
        works.setListMaterials(listOfMaterials);

        return works;
    }


    public static Materials createMaterials(long id, String name, long number, long priceForOne){

        Materials materials = new Materials();

        materials.setId(id);
        materials.setName(name);
        materials.setNumber(number);
        materials.setPriceForOne(priceForOne);

        return materials;
    }



    public Customer createCustomer(String name, String surname, String mailbox, String telephone){


        Customer customer = new Customer();

       // customer.setId(id);
        customer.setName(name);
        customer.setSurname(surname);
        customer.setMailbox(mailbox);
        customer.setTelephone(telephone);

        return customer;
    }

    public Executor createExecutor(Long id, String name, String surname, String mailbox, Long numberOfCompletedProjects, Long NumberOfWorkers){

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
