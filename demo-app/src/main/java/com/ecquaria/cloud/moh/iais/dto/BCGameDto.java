package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.CustomMsg;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import net.sf.oval.constraint.Min;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;
import net.sf.oval.constraint.Past;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BCGameDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Id
    private String categoryId;

    @NotBlank(
            message = "GENERAL_ERR0006",
            profiles = {"add"}
    )
    @NotNull(
            message = "GENERAL_ERR0006",
            profiles = {"add"}
    )
    @CustomMsg(
            placeHolders = {"field"},
            replaceVals = {"gameName"}
    )
    private String gameName;

    private String gameDescription;

    @NotNull(
            message = "GENERAL_ERR0006",
            profiles = {"add"}
    )
    @Past
    private Date issueDate;

    @NotNull(
            message = "GENERAL_ERR0006",
            profiles = {"add"}
    )
    @Min(value = 0)
    private Double price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BCGameDto bcGameDto = (BCGameDto) o;
        return categoryId.equals(bcGameDto.categoryId) && gameName.equals(bcGameDto.gameName) && Objects.equals(gameDescription, bcGameDto.gameDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId, gameName, gameDescription);
    }
}
