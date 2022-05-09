package sg.gov.moh.iais.egp.bsb.dto.inspection.afc;

import lombok.Data;

import java.util.List;

@Data
public class AFCSaveDto {
    private String appId;
    private List<CertificationDocDisPlayDto> newCertDocDtoList;
    private List<CertificationDocDisPlayDto> savedCertDocDtoList;
    private boolean uploadNewDoc;
}
