package sg.gov.moh.iais.egp.bsb.dto.file;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocRecordInfo implements Serializable {
    private String docEntityId;
    private String docType;
    private String docSubType;
    private String filename;
    private long size;
    private String repoId;
    private Date submitDate;
    private String submitBy;
    private String submitByName;
}