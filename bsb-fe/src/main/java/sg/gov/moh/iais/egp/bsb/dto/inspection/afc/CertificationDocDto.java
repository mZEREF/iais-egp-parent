package sg.gov.moh.iais.egp.bsb.dto.inspection.afc;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CertificationDocDto implements Serializable {
    private String repoId;
    private String docName;
    private String docType;
    private String uploadBy;
    private Date uploadDate;
    private Integer roundOfReview;
    private String afcMarkFinal;
    private String applicantMarkFinal;
    private String mohMarkFinal;
}
