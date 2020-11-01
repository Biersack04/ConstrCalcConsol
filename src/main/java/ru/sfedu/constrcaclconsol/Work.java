package ru.sfedu.constrcaclconsol;

public class Work {

        private String name;

        private long price;

        public Work()
        {}

        public Work(String name, long price) {

            this.name = name;

            this.price = price;

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
