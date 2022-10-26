package sg.gov.moh.iais.egp.bsb.dto.datarecovery;

import lombok.Data;

@Data
public class DataRecoveryDisplayDto {
    private String id;
    private String moduleName;
    private String functionName;
    private String userName;
    private String createDate;
}
