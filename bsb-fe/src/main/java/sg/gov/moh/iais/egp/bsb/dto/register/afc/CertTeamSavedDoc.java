package sg.gov.moh.iais.egp.bsb.dto.register.afc;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;

@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertTeamSavedDoc implements Serializable {
    private String docEntityId;
    private String docType;
    private String memberIdNo;
    private String filename;
    private long size;
    private String repoId;
    private Date submitDate;
    private String submitBy;
}
