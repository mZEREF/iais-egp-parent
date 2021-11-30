package sg.gov.moh.iais.egp.bsb.dto.process.afc;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;

import java.io.Serializable;
import java.util.Collection;

@Data
@Slf4j
public class FacilityCertifierRegisterDto implements Serializable{
    private String appStatus;
    private OrganisationProfileDto profileDto;
    private CertifyingTeamDto certifyingTeamDto;
    private AdministratorDto administratorDto;
    private Collection<DocRecordInfo> docRecordInfos;
}
