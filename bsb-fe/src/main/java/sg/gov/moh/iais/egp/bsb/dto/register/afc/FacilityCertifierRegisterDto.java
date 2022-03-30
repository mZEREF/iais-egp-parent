package sg.gov.moh.iais.egp.bsb.dto.register.afc;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.renewal.FacilityCertifierRegistrationReviewDto;
import sg.gov.moh.iais.egp.bsb.dto.renewal.InstructionDto;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
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
    private Collection<CertTeamSavedDoc> certTeamSavedDocs;
    private PreviewSubmitDto previewSubmitDto;

    //renewal special dto
    private InstructionDto instructionDto;
    private FacilityCertifierRegistrationReviewDto facilityCertifierRegistrationReviewDto;

    public static FacilityCertifierRegisterDto from(NodeGroup facRegRoot){
        FacilityCertifierRegisterDto dto = new FacilityCertifierRegisterDto();
        dto.setProfileDto((CompanyProfileDto) ((SimpleNode) facRegRoot.at( NODE_NAME_APPLICATION_INFO+ facRegRoot.getPathSeparator() + NODE_NAME_COMPANY_PROFILE)).getValue());
        CertifyingTeamDto  certifyingTeamDto = (CertifyingTeamDto) ((SimpleNode) facRegRoot.at(NODE_NAME_APPLICATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_CERTIFYING_TEAM_DETAIL)).getValue();
        dto.setCertifyingTeamDto(certifyingTeamDto);
        dto.setAdministratorDto((AdministratorDto) ((SimpleNode) facRegRoot.at(NODE_NAME_APPLICATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ADMINISTRATOR)).getValue());
        dto.setPreviewSubmitDto((PreviewSubmitDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FACILITY_CERTIFIER_PREVIEW_SUBMIT)).getValue());
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode) facRegRoot.at(NODE_NAME_SUPPORTING_DOCUMENT)).getValue();
        Collection<DocRecordInfo> docRecordInfos = new ArrayList<>(primaryDocDto.getSavedDocMap().size() + 1);
        Collection<CertTeamSavedDoc> certTeamSavedDocs = new ArrayList<>(primaryDocDto.getCertTeamSavedDocMap().size()+1);
        docRecordInfos.addAll(primaryDocDto.getSavedDocMap().values());
        if (certifyingTeamDto.getSavedFile() != null) {
            docRecordInfos.add(certifyingTeamDto.getSavedFile());
        }
        dto.setDocRecordInfos(docRecordInfos);
        certTeamSavedDocs.addAll(primaryDocDto.getCertTeamSavedDocMap().values());
        dto.setCertTeamSavedDocs(certTeamSavedDocs);
        return dto;
    }

    public NodeGroup toFacilityCertRegisterGroup(String name){
        Collection<DocRecordInfo> primaryDocs = new ArrayList<>();
        DocRecordInfo certifyTeamDoc = null;
        for (DocRecordInfo info : docRecordInfos) {
            if(DocConstants.DOC_TYPE_DATA_CERTIFYING_TEAM.equals(info.getDocType())){
                certifyTeamDoc = info;
            }else{
                primaryDocs.add(info);
            }
        }
        certifyingTeamDto.setSavedFile(certifyTeamDoc);
        //generate new NodeGroup
        SimpleNode orgProfileNode = new SimpleNode(profileDto,NODE_NAME_COMPANY_PROFILE,new Node[0]);
        SimpleNode administratorNode = new SimpleNode(administratorDto,NODE_NAME_ADMINISTRATOR,new Node[]{orgProfileNode});
        SimpleNode certifierTeamNode = new SimpleNode(certifyingTeamDto,NODE_NAME_CERTIFYING_TEAM_DETAIL,new Node[]{orgProfileNode,administratorNode});
        NodeGroup facCertInfoGroup = new NodeGroup.Builder().name(NODE_NAME_APPLICATION_INFO)
                .dependNodes(new Node[0])
                .addNode(orgProfileNode)
                .addNode(administratorNode)
                .addNode(certifierTeamNode)
                .build();

        Node beginFacCertNode = new Node(NODE_NAME_BEGIN_FACILITY_CERTIFIER, new Node[0]);
        PrimaryDocDto primaryDocDto = new PrimaryDocDto();
        primaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(primaryDocs, DocRecordInfo::getRepoId));
        primaryDocDto.setCertTeamSavedDocMap(CollectionUtils.uniqueIndexMap(certTeamSavedDocs, CertTeamSavedDoc::getRepoId));
        SimpleNode primaryDocNode = new SimpleNode(primaryDocDto,NODE_NAME_SUPPORTING_DOCUMENT,new Node[]{facCertInfoGroup});
        SimpleNode previewSubmitNode = new SimpleNode(previewSubmitDto,NODE_NAME_FACILITY_CERTIFIER_PREVIEW_SUBMIT,new Node[]{facCertInfoGroup,primaryDocNode});
        return new NodeGroup.Builder().name(name)
                .dependNodes(new Node[0])
                .addNode(beginFacCertNode)
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
        SimpleNode orgProfileNode = new SimpleNode(profileDto,NODE_NAME_COMPANY_PROFILE,new Node[0]);
        SimpleNode administratorNode = new SimpleNode(administratorDto,NODE_NAME_ADMINISTRATOR,new Node[]{orgProfileNode});
        SimpleNode certifierTeamNode = new SimpleNode(certifyingTeamDto,NODE_NAME_CERTIFYING_TEAM_DETAIL,new Node[]{orgProfileNode,administratorNode});
        NodeGroup facCertInfoGroup = new NodeGroup.Builder().name(NODE_NAME_APPLICATION_INFO)
                .dependNodes(new Node[0])
                .addNode(orgProfileNode)
                .addNode(administratorNode)
                .addNode(certifierTeamNode)
                .build();

        Node beginFacCertNode = new Node(NODE_NAME_BEGIN_FACILITY_CERTIFIER, new Node[0]);
        PrimaryDocDto primaryDocDto = new PrimaryDocDto();
        primaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(docRecordInfos, DocRecordInfo::getRepoId));
        SimpleNode primaryDocNode = new SimpleNode(primaryDocDto,NODE_NAME_SUPPORTING_DOCUMENT,new Node[]{facCertInfoGroup});
        SimpleNode previewSubmitNode = new SimpleNode(previewSubmitDto,NODE_NAME_FACILITY_CERTIFIER_PREVIEW_SUBMIT,new Node[]{facCertInfoGroup,primaryDocNode});
        return new NodeGroup.Builder().name(name)
                .dependNodes(new Node[0])
                .addNode(beginFacCertNode)
                .addNode(facCertInfoGroup)
                .addNode(primaryDocNode)
                .addNode(previewSubmitNode)
                .build();
    }

    public static FacilityCertifierRegisterDto fromRenewal(NodeGroup viewApprovalRoot, NodeGroup facRegRoot){
        FacilityCertifierRegisterDto dto = new FacilityCertifierRegisterDto();
        dto.setProfileDto((CompanyProfileDto) ((SimpleNode) facRegRoot.at( NODE_NAME_APPLICATION_INFO+ facRegRoot.getPathSeparator() + NODE_NAME_COMPANY_PROFILE)).getValue());
        dto.setCertifyingTeamDto((CertifyingTeamDto) ((SimpleNode) facRegRoot.at(NODE_NAME_APPLICATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_CERTIFYING_TEAM_DETAIL)).getValue());
        dto.setAdministratorDto((AdministratorDto) ((SimpleNode) facRegRoot.at(NODE_NAME_APPLICATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ADMINISTRATOR)).getValue());
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode) facRegRoot.at(NODE_NAME_SUPPORTING_DOCUMENT)).getValue();
        dto.setDocRecordInfos(primaryDocDto.getSavedDocMap().values());
        dto.setFacilityCertifierRegistrationReviewDto((FacilityCertifierRegistrationReviewDto) ((SimpleNode) viewApprovalRoot.at(NODE_NAME_REVIEW)).getValue());
        return dto;
    }
}
