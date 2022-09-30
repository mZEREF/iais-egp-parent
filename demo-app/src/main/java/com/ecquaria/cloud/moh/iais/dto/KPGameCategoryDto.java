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
public class KPGameCategoryDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @MaxLength(
            value = 10,
            message = "0000000000",
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
            replaceVals = {"categoryNO", "4"}
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
            replaceVals = {"categoryDescription"}
    )
    private String categoryDescription;

}
