package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.CustomMsg;
import lombok.Data;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity
public class BCGameCategoryDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Override
    public String toString() {
        return "BCGameCategoryDto{" +
                "id='" + id + '\'' +
                ", categoryNo='" + categoryNo + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", categoryDescription='" + categoryDescription + '\'' +
                '}';
    }

    @MaxLength(
            value = 10,
            message = "GENERAL_ERR0041",
            profiles = {"edit","add"}
    )
    @NotBlank(
            message = "GENERAL_ERR0006",
            profiles = {"edit","add"}
    )
    @NotNull(
            message = "GENERAL_ERR0006",
            profiles = {"edit","add"}
    )
    @CustomMsg(
            placeHolders = {"field", "maxlength"},
            replaceVals = {"categoryNo", "10"}
    )
    private String categoryNo;

    @NotBlank(
            message = "GENERAL_ERR0006",
            profiles = {"edit","add"}
    )
    @NotNull(
            message = "GENERAL_ERR0006",
            profiles = {"edit","add"}
    )
    @CustomMsg(
            placeHolders = {"field"},
            replaceVals = {"categoryName"}
    )
    private String categoryName;

    private String categoryDescription;

}
