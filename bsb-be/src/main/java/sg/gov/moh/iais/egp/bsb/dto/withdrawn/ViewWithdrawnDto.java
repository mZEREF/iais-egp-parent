package sg.gov.moh.iais.egp.bsb.dto.withdrawn;

import lombok.Data;

/**
 * @author tangtang
 * @date 2022/6/10 10:41
 */
@Data
public class ViewWithdrawnDto {
    private String appNo;
    private String reason;
    private String remarks;
}
