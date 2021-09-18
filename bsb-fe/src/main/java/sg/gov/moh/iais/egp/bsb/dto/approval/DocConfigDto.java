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

    public DocConfigDto(String docType, Boolean isMandatory) {
        this.docType = docType;
        this.isMandatory = isMandatory;
    }
}
