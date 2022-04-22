package sg.gov.moh.iais.egp.bsb.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProcessHistoryDto implements Serializable {
    private String userName;
    private String statusUpdate;
    private String remarks;
    private String lastUpdated;
}
