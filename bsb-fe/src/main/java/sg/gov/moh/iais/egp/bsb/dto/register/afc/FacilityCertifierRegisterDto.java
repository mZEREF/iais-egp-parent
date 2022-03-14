package sg.gov.moh.iais.egp.bsb.dto.register.afc;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.renewal.FacilityCertifierRegistrationReviewDto;
import sg.gov.moh.iais.egp.bsb.dto.renewal.InstructionDto;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;

import static sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants.*;

/**
 * @author : YiMing
 * @version :2021/9/30 8:13
 **/

@Data
@Slf4j
public class FacilityCertifierRegisterDto implements Serializable {
    private String appStatus;
    //this is used to saveDraft module
    private String appType;

    private CompanyProfileDto profileDto;
    private CertifyingTeamDto certifyingTeamDto;
    private AdministratorDto administratorDto;
    private Collection<DocRecordInfo> docRecordInfos;
    private PreviewSubmitDto previewSubmitDto;

    //renewal special dto
    private InstructionDto instructionDto;
    private FacilityCertifierRegistrationReviewDto facilityCertifierRegistrationReviewDto;

    public static FacilityCertifierRegisterDto from(NodeGroup facRegRoot){
        FacilityCertifierRegisterDto dto = new FacilityCertifierRegisterDto();
        dto.setProfileDto((CompanyProfileDto) ((SimpleNode) facRegRoot.at( NODE_NAME_ORGANISATION_INFO+ facRegRoot.getPathSeparator() + NODE_NAME_COMPANY_PROFILE)).getValue());
        dto.setCertifyingTeamDto((CertifyingTeamDto) ((SimpleNode) facRegRoot.at(NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_COMPANY_CERTIFYING_TEAM)).getValue());
        dto.setAdministratorDto((AdministratorDto) ((SimpleNode) facRegRoot.at(NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_COMPANY_FAC_ADMINISTRATOR)).getValue());
        dto.setPreviewSubmitDto((PreviewSubmitDto) ((SimpleNode) facRegRoot.at(NODE_NAME_CER_PREVIEW_SUBMIT)).getValue());
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_PRIMARY_DOCUMENT)).getValue();
        dto.setDocRecordInfos(primaryDocDto.getSavedDocMap().values());
        return dto;
    }

    public NodeGroup toFacilityCertRegister(String name){
        //generate new NodeGroup
        SimpleNode orgProfileNode = new SimpleNode(profileDto,NODE_NAME_COMPANY_PROFILE,new Node[0]);
        SimpleNode certifierTeamNode = new SimpleNode(certifyingTeamDto,NODE_NAME_COMPANY_CERTIFYING_TEAM,new Node[]{orgProfileNode});
        SimpleNode administratorNode = new SimpleNode(administratorDto,NODE_NAME_COMPANY_FAC_ADMINISTRATOR,new Node[]{orgProfileNode,certifierTeamNode});
        NodeGroup facCertInfoGroup = new NodeGroup.Builder().name(NODE_NAME_ORGANISATION_INFO)
                .dependNodes(new Node[0])
                .addNode(orgProfileNode)
                .addNode(certifierTeamNode)
                .addNode(administratorNode)
                .build();

        Node companyInfoDto = new Node(NODE_NAME_COMPANY_INFO, new Node[0]);
        PrimaryDocDto primaryDocDto = new PrimaryDocDto();
        primaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(docRecordInfos, DocRecordInfo::getRepoId));
        SimpleNode primaryDocNode = new SimpleNode(primaryDocDto,NODE_NAME_FAC_PRIMARY_DOCUMENT,new Node[]{facCertInfoGroup});
        SimpleNode previewSubmitNode = new SimpleNode(previewSubmitDto,NODE_NAME_CER_PREVIEW_SUBMIT,new Node[]{facCertInfoGroup,primaryDocNode});
        return new NodeGroup.Builder().name(name)
                .dependNodes(new Node[0])
                .addNode(companyInfoDto)
                .addNode(facCertInfoGroup)
                .addNode(primaryDocNode)
                .addNode(previewSubmitNode)
                .build();
    }

    /********************Renewal*********************/

    public NodeGroup toRenewalReviewRootGroup(String name){
        Node instructionNode = new Node(NODE_NAME_INSTRUCTION, new Node[0]);
        SimpleNode reviewNode = new SimpleNode(facilityCertifierRegistrationReviewDto, NODE_NAME_REVIEW, new Node[]{instructionNode});
        return new NodeGroup.Builder().name(name)
                .addNode(instructionNode)
                .addNode(reviewNode)
                .build();
    }

    public NodeGroup toRenewalFacCerRegRootGroup(String name) {
        SimpleNode orgProfileNode = new SimpleNode(profileDto, NODE_NAME_COMPANY_PROFILE, new Node[0]);
        SimpleNode certifierTeamNode = new SimpleNode(certifyingTeamDto, NODE_NAME_COMPANY_CERTIFYING_TEAM, new Node[]{orgProfileNode});
        SimpleNode administratorNode = new SimpleNode(administratorDto, NODE_NAME_COMPANY_FAC_ADMINISTRATOR, new Node[]{orgProfileNode,certifierTeamNode});
        NodeGroup facCertInfoGroup = new NodeGroup.Builder().name(NODE_NAME_ORGANISATION_INFO)
                .dependNodes(new Node[0])
                .addNode(orgProfileNode)
                .addNode(certifierTeamNode)
                .addNode(administratorNode)
                .build();

        Node companyInfoDto = new Node(NODE_NAME_COMPANY_INFO, new Node[0]);
        PrimaryDocDto primaryDocDto = new PrimaryDocDto();
        primaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(docRecordInfos, DocRecordInfo::getRepoId));
        SimpleNode primaryDocNode = new SimpleNode(primaryDocDto, NODE_NAME_FAC_PRIMARY_DOCUMENT, new Node[]{facCertInfoGroup});
        return new NodeGroup.Builder().name(name)
                .dependNodes(new Node[0])
                .addNode(companyInfoDto)
                .addNode(facCertInfoGroup)
                .addNode(primaryDocNode)
                .build();
    }

    public static FacilityCertifierRegisterDto fromRenewal(NodeGroup viewApprovalRoot, NodeGroup facRegRoot){
        FacilityCertifierRegisterDto dto = new FacilityCertifierRegisterDto();
        dto.setProfileDto((CompanyProfileDto) ((SimpleNode) facRegRoot.at( NODE_NAME_ORGANISATION_INFO+ facRegRoot.getPathSeparator() + NODE_NAME_COMPANY_PROFILE)).getValue());
        dto.setCertifyingTeamDto((CertifyingTeamDto) ((SimpleNode) facRegRoot.at(NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_COMPANY_CERTIFYING_TEAM)).getValue());
        dto.setAdministratorDto((AdministratorDto) ((SimpleNode) facRegRoot.at(NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_COMPANY_FAC_ADMINISTRATOR)).getValue());
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_PRIMARY_DOCUMENT)).getValue();
        dto.setDocRecordInfos(primaryDocDto.getSavedDocMap().values());
        dto.setFacilityCertifierRegistrationReviewDto((FacilityCertifierRegistrationReviewDto) ((SimpleNode) viewApprovalRoot.at(NODE_NAME_REVIEW)).getValue());
        return dto;
    }
}
