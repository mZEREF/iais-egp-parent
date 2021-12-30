package sg.gov.moh.iais.egp.bsb.dto.incident.entity;

import lombok.Data;


/**
 * @author YiMing
 * @version 2021/12/30 16:44
 **/
@Data
public class ProcessHistoryDto {
    private String userName;
    private String status;
    private String remarks;
    private String updateDate;

}
