package ru.sfedu.constrcaclconsol.converter;

import com.opencsv.bean.AbstractBeanField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.constrcaclconsol.Constants;
import ru.sfedu.constrcaclconsol.bean.Customer;
import static ru.sfedu.constrcaclconsol.Constants.NULL;


public class CustomerConverter extends AbstractBeanField<Customer> {

    private static final Logger log = LogManager.getLogger(Customer.class);

    @Override
    protected Object convert(String s) {

      if (s.isEmpty()) {

          log.error(Constants.NULL);

          return null;
      }

      String indexString = s.substring(1, s.length() - 1);
      Customer customer = new Customer();

      if (!indexString.isEmpty()) {

          customer.setId(Long.parseLong(indexString));
      }

      return customer;
    }

    @Override
    protected String convertToWrite(Object value) {

        Customer customer = (Customer) value;
        StringBuilder builder = new StringBuilder(Constants.PEOPLE_START_SYMBOL);

        if (customer != null) {

            builder.append(customer.getId());
            builder.append(Constants.SPLIT);
            builder.delete(builder.length() - 1, builder.length());
        }

        builder.append(Constants.PEOPLE_END_SYMBOL);

        return builder.toString();
    }

}
