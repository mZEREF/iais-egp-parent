package sg.gov.moh.iais.egp.bsb.dto.appview.afc;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;

import java.util.Collection;


@Data
@Slf4j
@NoArgsConstructor
public class FacilityCertifierRegisterDto {
    private OrganisationProfileDto profileDto;
    private CertifyingTeamDto certifyingTeamDto;
    private AdministratorDto administratorDto;
    private Collection<DocRecordInfo> docRecordInfos;
}
