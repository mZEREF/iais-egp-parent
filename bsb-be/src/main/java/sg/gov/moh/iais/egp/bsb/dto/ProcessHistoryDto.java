package sg.gov.moh.iais.egp.bsb.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : LiRan
 * @date : 2022/1/20
 */
@Data
public class ProcessHistoryDto implements Serializable {
    private String userName;
    private String statusUpdate;
    private String remarks;
    private String lastUpdated;
}
