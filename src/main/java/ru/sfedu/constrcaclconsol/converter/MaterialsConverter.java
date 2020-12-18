package ru.sfedu.constrcaclconsol.converter;

import com.opencsv.bean.AbstractBeanField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.constrcaclconsol.Constants;
import ru.sfedu.constrcaclconsol.bean.Materials;
import java.util.ArrayList;
import java.util.List;

import static ru.sfedu.constrcaclconsol.Constants.NULL;

public class MaterialsConverter extends AbstractBeanField<Materials>{

    private static final Logger log = LogManager.getLogger(Materials.class);

    @Override
    protected Object convert(String s) {

        if (s.isEmpty()) {

            log.error(Constants.NULL);

            return null;
        }

        String indexString = s.substring(1, s.length() - 1);
        String[] unparsedIndexList = indexString.split(Constants.SPLIT);

        List<Materials> indexMaterialList = new ArrayList<>();

        for (String strIndex : unparsedIndexList) {

            if (!strIndex.isEmpty()) {

                Materials materials = new Materials();
                materials.setId(Long.parseLong(strIndex));
                indexMaterialList.add(materials);
            }
        }

        return indexMaterialList;
    }

    @Override
    protected String convertToWrite(Object value) {

        List<Materials> materialList = (List<Materials>) value;
        StringBuilder builder = new StringBuilder(Constants.LIST_START_SYMBOL);

        if (materialList.size() != 0) {

            for (Materials materials : materialList) {

                builder.append(materials.getId());
                builder.append(Constants.SPLIT);
            }

            builder.delete(builder.length() - 1, builder.length());
        }

        builder.append(Constants.LIST_END_SYMBOL);

        return builder.toString();
    }

}

