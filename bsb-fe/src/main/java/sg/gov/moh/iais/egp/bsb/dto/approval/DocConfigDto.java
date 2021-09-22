package sg.gov.moh.iais.egp.bsb.dto.approval;

import lombok.Data;

/**
 * @author : LiRan
 * @date : 2021/9/18
 */
@Data
public class DocConfigDto {
    private String docType;
    private Boolean isMandatory;
    private String approvalType;
    private String index;

    public DocConfigDto(String docType, Boolean isMandatory, String approvalType, String index) {
        this.docType = docType;
        this.isMandatory = isMandatory;
        this.approvalType = approvalType;
        this.index = index;
    }
}
