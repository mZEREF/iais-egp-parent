package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.CustomMsg;
import lombok.Data;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @ClassName: HuangKunRoomDto
 * @author: haungkun
 * @date: 2022/7/14 16:28
 */
@Data
@Entity
public class HuangKunRoomDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotBlank(
            message = "GENERAL_ERR0006",
            profiles = {"edit", "add"}
    )
    @NotNull(
            message = "GENERAL_ERR0006",
            profiles = {"edit", "add"}
    )
    @CustomMsg(
            placeHolders = {"field"},
            replaceVals = {"roomType"}
    )
    private String roomType;

    @MaxLength(
            value = 4,
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
            replaceVals = {"roomNO", "4"}
    )
    private String roomNO;

}
