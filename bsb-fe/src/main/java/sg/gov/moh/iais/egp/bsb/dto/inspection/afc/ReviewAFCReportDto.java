package sg.gov.moh.iais.egp.bsb.dto.inspection.afc;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ReviewAFCReportDto implements Serializable {
    private String id;
    private String appId;
    private String appNo;
    private String appStatus;
    private String facName;
    private String facAddress;
    private Date insDate;
    private List<CertificationDocDisPlayDto> certificationDocDisPlayDtos;
    private List<DocMeta> docMetas;
    private String profile;
    private Integer maxRound;
    private boolean actionOnOld;
}
