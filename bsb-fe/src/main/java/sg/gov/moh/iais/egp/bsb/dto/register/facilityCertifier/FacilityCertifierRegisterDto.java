package sg.gov.moh.iais.egp.bsb.dto.register.facilityCertifier;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;

import static sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants.*;


/**
 * @author : YiMing
 * @date :2021/9/30 8:13
 **/

@Data
@Slf4j
public class FacilityCertifierRegisterDto {
    private OrganisationProfileDto profileDto;
    private CertifyingTeamDto certifyingTeamDto;
    private AdministratorDto administratorDto;
    private PrimaryDocDto primaryDocDto;
    private PreviewSubmitDto previewSubmitDto;

    public static FacilityCertifierRegisterDto from(NodeGroup facRegRoot){
        FacilityCertifierRegisterDto dto = new FacilityCertifierRegisterDto();
        dto.setProfileDto((OrganisationProfileDto) facRegRoot.at( NODE_NAME_ORGANISATION_INFO+ facRegRoot.getPathSeparator() + NODE_NAME_ORG_PROFILE));
        dto.setCertifyingTeamDto((CertifyingTeamDto) facRegRoot.at(NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_CERTIFYING_TEAM));
        dto.setAdministratorDto((AdministratorDto) facRegRoot.at(NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_FAC_ADMINISTRATOR));
        dto.setPreviewSubmitDto((PreviewSubmitDto) facRegRoot.at(NODE_NAME_CER_PREVIEW_SUBMIT));
        return dto;
    }

}
