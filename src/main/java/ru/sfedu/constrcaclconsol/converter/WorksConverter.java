package ru.sfedu.constrcaclconsol.converter;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.constrcaclconsol.Constants;
import ru.sfedu.constrcaclconsol.bean.Materials;
import ru.sfedu.constrcaclconsol.bean.Works;

import java.util.ArrayList;
import java.util.List;

public class WorksConverter extends AbstractBeanField<Materials>{

    private static final Logger log = LogManager.getLogger(Materials.class);


      @Override
        protected Object convert(String s) throws CsvDataTypeMismatchException, CsvConstraintViolationException {

                if (s.isEmpty()) {
                    return null;
                }
            String indexString = s.substring(1, s.length() - 1);
                String[] unparsedIndexList = indexString.split(
                   Constants.SPLIT);
            List<Works> indexMaterialList = new ArrayList<>();
            for (String strIndex : unparsedIndexList) {

                if (!strIndex.isEmpty()) {
                    Works works = new Works();
                    works.setId(Long.parseLong(strIndex));

                    indexMaterialList.add(works);
                }
            }
            return indexMaterialList;
        }

        @Override
        protected String convertToWrite(Object value) {
            List<Works> worksList = (List<Works>) value;
            StringBuilder builder = new StringBuilder(Constants.LIST_START_SYMBOL);
            if (worksList.size() != 0) {
                for (Works modificationRecord : worksList) {
                    builder.append(modificationRecord.getId());
                    builder.append(Constants.SPLIT);
                }
                builder.delete(builder.length() - 1, builder.length());
            }
            builder.append(Constants.LIST_END_SYMBOL);
            log.debug(builder.toString());
            return builder.toString();
        }




    }

