package sg.gov.moh.iais.egp.bsb.dto.incident.entity;

import lombok.Data;

import java.io.Serializable;


/**
 * @author YiMing
 * @version 2021/12/30 16:44
 **/
@Data
public class ProcessHistoryDto implements Serializable {
    private String userName;
    private String status;
    private String remarks;
    private String updateDate;

}
