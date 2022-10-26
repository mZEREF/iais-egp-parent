package sg.gov.moh.iais.egp.bsb.dto.renewal.defer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeferRenewDto implements Serializable {
    private String facilityId;

    private String facilityCode;

    private String classificationCode;

    private String facilityNo;

    private String facilityName;

    private Date deferDate;

    private String deferReason;

    private String check;
    // load from DB
    private List<DocRecordInfo> savedInfos;
    // doc that need validate
    private List<DocMeta> docMetas;
}
