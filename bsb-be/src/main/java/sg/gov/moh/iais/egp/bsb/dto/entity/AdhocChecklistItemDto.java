package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.googlecode.jmapper.annotations.JGlobalMap;
import com.googlecode.jmapper.annotations.JMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JGlobalMap(excluded = "adhocChecklistConfigId")
public class AdhocChecklistItemDto extends BaseEntityDto {

    private String id;

    @JMap(value = "${adhocChecklistConfig.id}")
    private String adhocChecklistConfigId;

    private String sectionId;

    private String itemId;

    private String regulationId;

    private Integer order;

    @NotBlank(
            message = "GENERAL_ERR0006",
            profiles = {"customAdd"}
    )
    @NotNull(
            message = "GENERAL_ERR0006",
            profiles = {"customAdd"}
    )
    @MaxLength(
            value = 500,
            message = "CHKL_ERR015",
            profiles = {"customAdd"}
    )
    private String question;

    private String answer;

    private Boolean nonCompliant;

    private Boolean rectified;

    private String followupItem;
    private String observeFollowup;
    private String followupAction;
    private String dueDate;
}
