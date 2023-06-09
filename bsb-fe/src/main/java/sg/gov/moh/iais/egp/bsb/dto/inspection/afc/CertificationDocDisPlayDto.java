package sg.gov.moh.iais.egp.bsb.dto.inspection.afc;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CertificationDocDisPlayDto implements Serializable {
    private String id;
    private String repoId;
    private String maskedRepoId;
    private String docName;
    private String docType;
    private String submitBy;
    private String submitterRole;
    private Date uploadDate;
    private long size;
    private Integer roundOfReview;
    private String afcMarkFinal;
    private String applicantMarkFinal;
    private String mohMarkFinal;
}
