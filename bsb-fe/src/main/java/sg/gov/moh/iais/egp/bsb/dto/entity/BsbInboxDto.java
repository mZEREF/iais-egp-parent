package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BsbInboxDto extends BaseEntityDto {
    private String id;
    private String userId;
    private String subject;
    private String msgType;
    private String refNo;
    private String appType;
    private String msgContent;
    private String status;
}
