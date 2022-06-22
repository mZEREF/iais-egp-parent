package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;


@Data
@NoArgsConstructor
public class CompareDocRecordInfoDto {
    private DocRecordInfo oldDocRecordInfo;
    private DocRecordInfo newDocRecordInfo;

    public CompareDocRecordInfoDto(DocRecordInfo oldDocRecordInfo, DocRecordInfo newDocRecordInfo) {
        this.oldDocRecordInfo = oldDocRecordInfo;
        this.newDocRecordInfo = newDocRecordInfo;
    }
}
