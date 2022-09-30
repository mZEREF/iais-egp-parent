package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.CustomMsg;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.sf.oval.constraint.MatchPattern;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: zourun
 * @Description: TODO
 * @DateTime: 2022/9/22
 **/
@Data
@Entity
public class ZrSampleGameDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private String categoryId;

    @NotNull(message = "The game name can not be null", profiles = "addGame")
    @CustomMsg(placeHolders = {"field"}, replaceVals = {"gameName"})
    @NotBlank(message = "The game name can not be null", profiles = "addGame")
    private String gameName;
    @NotNull(message = "The game description can not be null", profiles = "addGame")
    @CustomMsg(placeHolders = {"field"}, replaceVals = {"gameDescription"})
    private String gameDescription;

    @NotNull(message = "The data can not be null", profiles = "addGame")
    @CustomMsg(placeHolders = {"field"}, replaceVals = {"issueDate"})
    private Date issueDate;

    @CustomMsg(placeHolders = {"field"}, replaceVals = {"price"})
    @NotNull(message = "The price is empty or not a number", profiles = "addGame")
    @NotBlank(message = "The price can not be null", profiles = "addGame")
    private BigDecimal price;

}
