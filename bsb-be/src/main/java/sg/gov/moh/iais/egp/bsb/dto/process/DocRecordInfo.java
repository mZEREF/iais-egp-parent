package sg.gov.moh.iais.egp.bsb.dto.process;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class DocRecordInfo implements Serializable {
    private String docEntityId;
    private String docType;
    private String filename;
    private long size;
    private String repoId;
    private Date submitDate;
    private String submitBy;
}
