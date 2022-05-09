package sg.gov.moh.iais.egp.bsb.dto.inspection.afc;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.common.multipart.ByteArrayMultipartFile;

import java.io.Serializable;

@Data
public class CertificationDocDto implements Serializable {
    private CertificationDocDisPlayDto disPlayDto;
    private ByteArrayMultipartFile multipartFile;
}
