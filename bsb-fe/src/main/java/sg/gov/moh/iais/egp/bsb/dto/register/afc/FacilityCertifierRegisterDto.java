package sg.gov.moh.iais.egp.bsb.dto.register.afc;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants;

import static sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants.*;


/**
 * @author : YiMing
 * @date :2021/9/30 8:13
 **/

@Data
@Slf4j
public class FacilityCertifierRegisterDto {
    private String appStatus;
    private OrganisationProfileDto profileDto;
    private CertifyingTeamDto certifyingTeamDto;
    private AdministratorDto administratorDto;
    private PrimaryDocDto primaryDocDto;
    private PreviewSubmitDto previewSubmitDto;

    public static FacilityCertifierRegisterDto from(NodeGroup facRegRoot){
        FacilityCertifierRegisterDto dto = new FacilityCertifierRegisterDto();
        dto.setProfileDto((OrganisationProfileDto) ((SimpleNode) facRegRoot.at( NODE_NAME_ORGANISATION_INFO+ facRegRoot.getPathSeparator() + NODE_NAME_ORG_PROFILE)).getValue());
        dto.setCertifyingTeamDto((CertifyingTeamDto) ((SimpleNode) facRegRoot.at(NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_CERTIFYING_TEAM)).getValue());
        dto.setAdministratorDto((AdministratorDto) ((SimpleNode) facRegRoot.at(NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_FAC_ADMINISTRATOR)).getValue());
        dto.setPreviewSubmitDto((PreviewSubmitDto) ((SimpleNode) facRegRoot.at(NODE_NAME_CER_PREVIEW_SUBMIT)).getValue());
        return dto;
    }

    public NodeGroup toFacilityCertRegister(String name){
        //新生成一个NodeGroup
        SimpleNode orgProfileNode = new SimpleNode(profileDto,NODE_NAME_ORG_PROFILE,new Node[0]);
        SimpleNode certifierTeamNode = new SimpleNode(certifyingTeamDto,NODE_NAME_ORG_CERTIFYING_TEAM,new Node[]{orgProfileNode});
        SimpleNode administratorNode = new SimpleNode(administratorDto,NODE_NAME_ORG_FAC_ADMINISTRATOR,new Node[]{orgProfileNode,certifierTeamNode});
        NodeGroup facCertInfoGroup = new NodeGroup.Builder().name(NODE_NAME_ORGANISATION_INFO)
                .dependNodes(new Node[0])
                .addNode(orgProfileNode)
                .addNode(certifierTeamNode)
                .addNode(administratorNode)
                .build();

        Node companyInfoDto = new Node(NODE_NAME_COMPANY_INFO, new Node[0]);
        SimpleNode primaryDocNode = new SimpleNode(new PrimaryDocDto(),NODE_NAME_FAC_PRIMARY_DOCUMENT,new Node[]{facCertInfoGroup});
        SimpleNode previewSubmitNode = new SimpleNode(previewSubmitDto,NODE_NAME_CER_PREVIEW_SUBMIT,new Node[]{facCertInfoGroup,primaryDocNode});
        return new NodeGroup.Builder().name(name)
                .dependNodes(new Node[0])
                .addNode(companyInfoDto)
                .addNode(facCertInfoGroup)
                .addNode(primaryDocNode)
                .addNode(previewSubmitNode)
                .build();
    }

}
