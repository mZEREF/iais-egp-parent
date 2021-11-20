package sg.gov.moh.iais.egp.bsb.dto.file;

import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.common.multipart.ByteArrayMultipartFile;
import java.io.Serializable;
import java.util.Date;


@Data
@NoArgsConstructor
public class NewDocInfo implements Serializable {
    private String tmpId;
    private String docType;
    private String filename;
    private long size;
    private Date submitDate;
    private String submitBy;
    private ByteArrayMultipartFile multipartFile;
}
