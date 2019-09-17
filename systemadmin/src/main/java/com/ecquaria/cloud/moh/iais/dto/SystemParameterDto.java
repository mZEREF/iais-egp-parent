package com.ecquaria.cloud.moh.iais.dto;

/*
 *author: yichen
 *date time:9/4/2019 3:00 PM
 *description:
 */


import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import lombok.Getter;
import lombok.Setter;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;
import net.sf.oval.constraint.ValidateWithMethod;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Properties;


@Getter
@Setter
public class SystemParameterDto implements Serializable {
    private static final long serialVersionUID = 7811152537272121632L;
    private static Properties properties =  new Properties();

    static {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(MiscUtil.getClassRootPath() + "property/paramter.properties");
            properties.load(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    //paramter id
    Integer id;

    private String rowguid;

    @NotBlank(message = "can not is blank!", profiles = {"edit", "search"})
    @NotNull(message = "can not is null!", profiles = {"edit", "search"})
    private String domainType;

    private String module;

    private String description;

    private String valueType;

    @Length(profiles = {"edit"})
    @ValidateWithMethod(methodName = "validValueSize", parameterType = String.class, message = "Wrong size or length", profiles ="edit")
    private String value;

    private Character status;

    private String updatedBy;

    private Date updatedOn;


    /**
     * pass is true , else false
     * @param value
     * @return
     */
    private boolean validValueSize(String value){
        String valueType = this.valueType;
        int val = Integer.valueOf(value);
        switch (valueType){
            case "Page":
                return verifyPageSize(val);
            case "Size":
                return verifyFileUploadSize(val);
        }

        return false;
    }

    private boolean verifyPageSize(int value){
        return value < 10 || value > 50 ? false : true;
    }

    public boolean verifyFileUploadSize(int value){
        return value < 1 || value > 50 ? false : true;
    }
}
