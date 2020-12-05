package ru.sfedu.constrcaclconsol.converter;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.constrcaclconsol.Constants;
import ru.sfedu.constrcaclconsol.bean.Executor;

public class ExecutorConverter extends AbstractBeanField<Executor> {

    private static final Logger log = LogManager.getLogger(Executor.class);
    @Override
    protected Object convert(String s) throws CsvDataTypeMismatchException, CsvConstraintViolationException {

        if (s.isEmpty()) {
            return null;
        }
        String indexString = s.substring(1, s.length() - 1);
        Executor executor = new Executor();

        if (!indexString.isEmpty()) {

            executor.setId(Long.parseLong(indexString));

        }

        return executor;

    }



    @Override
    protected String convertToWrite(Object value) {
        Executor executor = (Executor) value;
        StringBuilder builder = new StringBuilder(Constants.PEOPLE_START_SYMBOL);
        if (executor != null) {

            builder.append(executor.getId());
            builder.append(Constants.SPLIT);

            builder.delete(builder.length() - 1, builder.length());
        }
        builder.append(Constants.PEOPLE_END_SYMBOL);
        log.debug(builder.toString());
        return builder.toString();
    }

}
