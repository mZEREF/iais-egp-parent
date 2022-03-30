package sg.gov.moh.iais.egp.bsb.dto.register.afc;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.common.multipart.ByteArrayMultipartFile;

import java.io.Serializable;
import java.util.Date;

@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertTeamNewDoc implements Serializable {
    private String tmpId;
    private String memberDocKey;
    private String filename;
    private long size;
    private Date submitDate;
    private String submitBy;
    private ByteArrayMultipartFile multipartFile;
}
