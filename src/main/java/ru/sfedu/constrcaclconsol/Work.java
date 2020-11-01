package ru.sfedu.constrcaclconsol;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Work {
    private static Logger log = LogManager.getLogger(ConstrCalcClient.class);

    private long id;

    private String name;

    private long price;


    public Work() {

    }

    public Work(String name, long price) {

        this.name = name;

        this.price = price;

    }
    public long getId() {

        return this.id;

    }

    public void setId(long id){

        this.id = id;
    }

    public String getName() {

        return this.name;

    }

    public void setName(String name) {

        this.name = name;

    }

    public long getPrice() {

        return this.price;

    }

    public void setPrice(long price){

        this.price = price;
    }

}
