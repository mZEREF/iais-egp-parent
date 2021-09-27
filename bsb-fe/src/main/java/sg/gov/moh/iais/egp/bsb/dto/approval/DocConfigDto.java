package sg.gov.moh.iais.egp.bsb.dto.approval;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : LiRan
 * @date : 2021/9/18
 */
@Data
public class DocConfigDto implements Serializable {
    private String docType;
    private Boolean isMandatory;
    private String approvalType;
    private String index;

    public DocConfigDto() {
    }

    public DocConfigDto(String docType, Boolean isMandatory, String approvalType, String index) {
        this.docType = docType;
        this.isMandatory = isMandatory;
        this.approvalType = approvalType;
        this.index = index;
    }
}
