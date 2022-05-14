package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.bat.BiologicalAgentToxinDto;
import sg.gov.moh.iais.egp.bsb.dto.renewal.InstructionDto;
import sg.gov.moh.iais.egp.bsb.dto.renewal.FacilityRegistrationReviewDto;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.*;


@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacilityRegisterDto implements Serializable{
    //this is used to saveDraft module
    private String appType;

    private FacilitySelectionDto facilitySelectionDto;
    private FacilityProfileDto facilityProfileDto;
    private FacilityOperatorDto facilityOperatorDto;
    private FacilityAdminAndOfficerDto facilityAdminAndOfficerDto;
    private FacilityCommitteeDto facilityCommitteeDto;
    private FacilityAuthoriserDto facilityAuthoriserDto;
    private Map<String, BiologicalAgentToxinDto> biologicalAgentToxinMap;
    private OtherApplicationInfoDto otherAppInfoDto;
    private Collection<DocRecordInfo> docRecordInfos;
    private FacilityAfcDto afcDto;
    private PreviewSubmitDto previewSubmitDto;

    //renewal special dto
    private InstructionDto instructionDto;
    private FacilityRegistrationReviewDto facilityRegistrationReviewDto;

    /** Write the facRegRoot NodeGroup into a DTO, then send the DTO to save the data. */
    public static FacilityRegisterDto from(NodeGroup facRegRoot) {
        FacilityRegisterDto dto = new FacilityRegisterDto();
        dto.setFacilitySelectionDto((FacilitySelectionDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_SELECTION)).getValue());
        dto.setFacilityProfileDto((FacilityProfileDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_PROFILE)).getValue());
        dto.setFacilityOperatorDto((FacilityOperatorDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_OPERATOR)).getValue());
        dto.setFacilityAdminAndOfficerDto((FacilityAdminAndOfficerDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_ADMIN_OFFICER)).getValue());
        dto.setFacilityCommitteeDto((FacilityCommitteeDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_COMMITTEE)).getValue());
        dto.setFacilityAuthoriserDto((FacilityAuthoriserDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_AUTH)).getValue());
        dto.setOtherAppInfoDto((OtherApplicationInfoDto) ((SimpleNode) facRegRoot.at(NODE_NAME_OTHER_INFO)).getValue());
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode) facRegRoot.at(NODE_NAME_PRIMARY_DOC)).getValue();
        dto.setPreviewSubmitDto((PreviewSubmitDto) ((SimpleNode) facRegRoot.at(NODE_NAME_PREVIEW_SUBMIT)).getValue());

        Collection<DocRecordInfo> docRecordInfos = new ArrayList<>(dto.getFacilityProfileDto().getSavedDocMap().size() + primaryDocDto.getSavedDocMap().size() + 2);
        docRecordInfos.addAll(dto.getFacilityProfileDto().getSavedDocMap().values());
        docRecordInfos.addAll(primaryDocDto.getSavedDocMap().values());
        if (dto.getFacilityCommitteeDto().getSavedFile() != null) {
            docRecordInfos.add(dto.getFacilityCommitteeDto().getSavedFile());
        }
        if (dto.getFacilityAuthoriserDto().getSavedFile() != null) {
            docRecordInfos.add(dto.getFacilityAuthoriserDto().getSavedFile());
        }
        dto.setDocRecordInfos(docRecordInfos);

        if (MasterCodeConstants.CERTIFIED_CLASSIFICATION.contains(dto.getFacilitySelectionDto().getFacClassification())) {
            dto.setAfcDto((FacilityAfcDto) ((SimpleNode) facRegRoot.at(NODE_NAME_AFC)).getValue());
        } else if (MasterCodeConstants.UNCERTIFIED_CLASSIFICATION.contains(dto.getFacilitySelectionDto().getFacClassification())) {
            NodeGroup batGroup = (NodeGroup) facRegRoot.at(NODE_NAME_FAC_BAT_INFO);
            Map<String, BiologicalAgentToxinDto> batInfoMap = getBatInfoMap(batGroup);
            dto.setBiologicalAgentToxinMap(batInfoMap);
        }
        return dto;
    }

    public static Map<String, BiologicalAgentToxinDto> getBatInfoMap(NodeGroup batNodeGroup) {
        Assert.notNull(batNodeGroup, "Biological Agent/Toxin node group must not be null!");
        Map<String, BiologicalAgentToxinDto> batMap = Maps.newLinkedHashMapWithExpectedSize(batNodeGroup.count());
        for (Node node : batNodeGroup.getAllNodes()) {
            assert node instanceof SimpleNode;
            batMap.put(node.getName(), (BiologicalAgentToxinDto) ((SimpleNode) node).getValue());
        }
        return batMap;
    }

    /** Convert data in this big DTO into a facRegRoot NodeGroup
     *  This is needed when we want to view the saved data or edit it */
    public NodeGroup toFacRegRootGroup(String name) {
        // split documents for profile
        Collection<DocRecordInfo> profileDocs = new ArrayList<>();
        Collection<DocRecordInfo> primaryDocs = new ArrayList<>();
        DocRecordInfo committeeDoc = null;
        DocRecordInfo authoriserDoc = null;
        for (DocRecordInfo info : docRecordInfos) {
            switch (info.getDocType()) {
                case DocConstants.DOC_TYPE_GAZETTE_ORDER:
                    profileDocs.add(info);
                    break;
                case DocConstants.DOC_TYPE_DATA_COMMITTEE:
                    committeeDoc = info;
                    break;
                case DocConstants.DOC_TYPE_DATA_AUTHORISER:
                    authoriserDoc = info;
                    break;
                default:
                    primaryDocs.add(info);
                    break;
            }
        }
        facilityProfileDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(profileDocs, DocRecordInfo::getRepoId));
        facilityCommitteeDto.setSavedFile(committeeDoc);
        facilityAuthoriserDto.setSavedFile(authoriserDoc);

        SimpleNode facSelectionNode = new SimpleNode(facilitySelectionDto, NODE_NAME_FAC_SELECTION, new Node[0]);

        SimpleNode facProfileNode = new SimpleNode(facilityProfileDto, NODE_NAME_FAC_PROFILE, new Node[0]);
        SimpleNode facOperatorNode = new SimpleNode(facilityOperatorDto, NODE_NAME_FAC_OPERATOR, new Node[]{facProfileNode});
        SimpleNode facAdminOfficerNode = new SimpleNode(facilityAdminAndOfficerDto, NODE_NAME_FAC_ADMIN_OFFICER, new Node[]{facProfileNode, facOperatorNode});
        SimpleNode facCommitteeNode = new SimpleNode(facilityCommitteeDto, NODE_NAME_FAC_COMMITTEE, new Node[]{facProfileNode, facOperatorNode, facAdminOfficerNode});
        SimpleNode facAuthNode = new SimpleNode(facilityAuthoriserDto, NODE_NAME_FAC_AUTH, new Node[]{facProfileNode, facOperatorNode, facAdminOfficerNode, facCommitteeNode});

        NodeGroup facInfoNodeGroup = new NodeGroup.Builder().name(NODE_NAME_FAC_INFO)
                .dependNodes(new Node[]{facSelectionNode})
                .addNode(facProfileNode)
                .addNode(facOperatorNode)
                .addNode(facAdminOfficerNode)
                .addNode(facCommitteeNode)
                .addNode(facAuthNode)
                .build();


        Node beforeBeginNode = new Node(NODE_NAME_BEFORE_BEGIN, new Node[0]);
        Node companyInfoNode = new Node(FacRegisterConstants.NODE_NAME_COMPANY_INFO, new Node[0]);
        PrimaryDocDto primaryDocDto = new PrimaryDocDto();
        primaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(primaryDocs, DocRecordInfo::getRepoId));

        boolean isCf = MasterCodeConstants.CERTIFIED_CLASSIFICATION.contains(facilitySelectionDto.getFacClassification());
        boolean isUcf = MasterCodeConstants.UNCERTIFIED_CLASSIFICATION.contains(facilitySelectionDto.getFacClassification());
        if (isCf) {
            SimpleNode otherAppInfoNode = new SimpleNode(otherAppInfoDto, NODE_NAME_OTHER_INFO, new Node[]{facSelectionNode, facInfoNodeGroup});
            SimpleNode supportingDocNode = new SimpleNode(primaryDocDto, NODE_NAME_PRIMARY_DOC, new Node[]{facSelectionNode, facInfoNodeGroup, otherAppInfoNode});
            SimpleNode afcNode = new SimpleNode(this.afcDto, NODE_NAME_AFC, new Node[]{facSelectionNode, facInfoNodeGroup, otherAppInfoNode, supportingDocNode});
            SimpleNode previewSubmitNode = new SimpleNode(this.previewSubmitDto, NODE_NAME_PREVIEW_SUBMIT, new Node[]{facSelectionNode, facInfoNodeGroup, otherAppInfoNode, supportingDocNode, afcNode});
            return new NodeGroup.Builder().name(name)
                    .addNode(beforeBeginNode)
                    .addNode(facSelectionNode)
                    .addNode(companyInfoNode)
                    .addNode(facInfoNodeGroup)
                    .addNode(otherAppInfoNode)
                    .addNode(supportingDocNode)
                    .addNode(afcNode)
                    .addNode(previewSubmitNode)
                    .build();
        } else if (isUcf) {
            NodeGroup.Builder batInfoNodeGroupBuilder = new NodeGroup.Builder().name(NODE_NAME_FAC_BAT_INFO)
                    .dependNodes(new Node[]{facSelectionNode, facInfoNodeGroup});
            for (Map.Entry<String, BiologicalAgentToxinDto> entry : biologicalAgentToxinMap.entrySet()) {
                SimpleNode batNode = new SimpleNode(entry.getValue(), entry.getKey(), new Node[0]);
                batInfoNodeGroupBuilder.addNode(batNode);
            }
            NodeGroup batInfoNodeGroup = batInfoNodeGroupBuilder.build();

            SimpleNode otherAppInfoNode = new SimpleNode(otherAppInfoDto, NODE_NAME_OTHER_INFO, new Node[]{facSelectionNode, facInfoNodeGroup, batInfoNodeGroup});
            SimpleNode supportingDocNode = new SimpleNode(primaryDocDto, NODE_NAME_PRIMARY_DOC, new Node[]{facSelectionNode, facInfoNodeGroup, batInfoNodeGroup, otherAppInfoNode});
            SimpleNode previewSubmitNode = new SimpleNode(this.previewSubmitDto, NODE_NAME_PREVIEW_SUBMIT, new Node[]{facSelectionNode, facInfoNodeGroup, batInfoNodeGroup, otherAppInfoNode, supportingDocNode});

            return new NodeGroup.Builder().name(name)
                    .addNode(beforeBeginNode)
                    .addNode(facSelectionNode)
                    .addNode(companyInfoNode)
                    .addNode(facInfoNodeGroup)
                    .addNode(batInfoNodeGroup)
                    .addNode(otherAppInfoNode)
                    .addNode(supportingDocNode)
                    .addNode(previewSubmitNode)
                    .build();
        } else {
            SimpleNode otherAppInfoNode = new SimpleNode(otherAppInfoDto, NODE_NAME_OTHER_INFO, new Node[]{facSelectionNode, facInfoNodeGroup});
            SimpleNode supportingDocNode = new SimpleNode(primaryDocDto, NODE_NAME_PRIMARY_DOC, new Node[]{facSelectionNode, facInfoNodeGroup, otherAppInfoNode});
            SimpleNode previewSubmitNode = new SimpleNode(this.previewSubmitDto, NODE_NAME_PREVIEW_SUBMIT, new Node[]{facSelectionNode, facInfoNodeGroup, otherAppInfoNode, supportingDocNode});

            return new NodeGroup.Builder().name(name)
                    .addNode(beforeBeginNode)
                    .addNode(facSelectionNode)
                    .addNode(companyInfoNode)
                    .addNode(facInfoNodeGroup)
                    .addNode(otherAppInfoNode)
                    .addNode(supportingDocNode)
                    .addNode(previewSubmitNode)
                    .build();
        }
    }

    /********************Renewal*********************/

    public NodeGroup toRenewalReviewRootGroup(String name){
        Node instructionNode = new Node(NODE_NAME_INSTRUCTION, new Node[0]);
        SimpleNode reviewNode = new SimpleNode(facilityRegistrationReviewDto, NODE_NAME_REVIEW, new Node[]{instructionNode});
        return new NodeGroup.Builder().name(name)
                .addNode(instructionNode)
                .addNode(reviewNode)
                .build();
    }

    public NodeGroup toRenewalFacRegRootGroup(String name) {
//        SimpleNode facSelectionNode = new SimpleNode(facilitySelectionDto, NODE_NAME_FAC_SELECTION, new Node[0]);
//        SimpleNode facProfileNode = new SimpleNode(facilityProfileDto, NODE_NAME_FAC_PROFILE, new Node[0]);
//        SimpleNode facOperatorNode = new SimpleNode(facilityOperatorDto, NODE_NAME_FAC_OPERATOR, new Node[]{facProfileNode});
//        SimpleNode facAuthNode = new SimpleNode(facilityAuthoriserDto, NODE_NAME_FAC_AUTH, new Node[]{facProfileNode, facOperatorNode});
//        SimpleNode facAdminNode = new SimpleNode(facilityAdminAndOfficerDto, NODE_NAME_FAC_ADMIN_OFFICER, new Node[]{facProfileNode, facOperatorNode, facAuthNode});
//        SimpleNode facOfficerNode = new SimpleNode(facilityOfficerDto, NODE_NAME_FAC_OFFICER, new Node[]{facProfileNode, facOperatorNode, facAuthNode, facAdminNode});
//        SimpleNode facCommitteeNode = new SimpleNode(facilityCommitteeDto, NODE_NAME_FAC_COMMITTEE, new Node[]{facProfileNode, facOperatorNode, facAuthNode, facAdminNode, facOfficerNode});
//
//        NodeGroup facInfoNodeGroup = new NodeGroup.Builder().name(NODE_NAME_FAC_INFO)
//                .dependNodes(new Node[]{facSelectionNode})
//                .addNode(facProfileNode)
//                .addNode(facOperatorNode)
//                .addNode(facAuthNode)
//                .addNode(facAdminNode)
//                .addNode(facOfficerNode)
//                .addNode(facCommitteeNode)
//                .build();
//
//        NodeGroup.Builder batInfoNodeGroupBuilder = new NodeGroup.Builder().name(NODE_NAME_FAC_BAT_INFO)
//                .dependNodes(new Node[]{facSelectionNode, facInfoNodeGroup});
//        for (Map.Entry<String, BiologicalAgentToxinDto> entry : biologicalAgentToxinMap.entrySet()) {
//            SimpleNode batNode = new SimpleNode(entry.getValue(), entry.getKey(), new Node[0]);
//            batInfoNodeGroupBuilder.addNode(batNode);
//        }
//        NodeGroup batInfoNodeGroup = batInfoNodeGroupBuilder.build();
//
//        Node companyInfoDto = new Node(FacRegisterConstants.NODE_NAME_COMPANY_INFO, new Node[0]);
//        SimpleNode otherAppInfoNode = new SimpleNode(OtherApplicationInfoDto.getAllCheckedInstance(), NODE_NAME_OTHER_INFO, new Node[]{facSelectionNode, facInfoNodeGroup, batInfoNodeGroup});
//        PrimaryDocDto primaryDocDto = new PrimaryDocDto();
//        primaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(docRecordInfos, DocRecordInfo::getRepoId));
//        SimpleNode primaryDocNode = new SimpleNode(primaryDocDto, NODE_NAME_PRIMARY_DOC, new Node[]{facSelectionNode, facInfoNodeGroup, batInfoNodeGroup, otherAppInfoNode});
//        return new NodeGroup.Builder().name(name)
//                .addNode(companyInfoDto)
//                .addNode(facSelectionNode)
//                .addNode(facInfoNodeGroup)
//                .addNode(batInfoNodeGroup)
//                .addNode(otherAppInfoNode)
//                .addNode(primaryDocNode)
//                .build();
        return null;
    }

    public static FacilityRegisterDto fromRenewal(NodeGroup viewApprovalRoot, NodeGroup facRegRoot) {
//        FacilityRegisterDto dto = new FacilityRegisterDto();
//        dto.setFacilitySelectionDto((FacilitySelectionDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_SELECTION)).getValue());
//        dto.setFacilityProfileDto((FacilityProfileDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_PROFILE)).getValue());
//        dto.setFacilityOperatorDto((FacilityOperatorDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_OPERATOR)).getValue());
//        dto.setFacilityAuthoriserDto((FacilityAuthoriserDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_AUTH)).getValue());
//        dto.setFacilityAdminAndOfficerDto((FacilityAdminAndOfficerDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_ADMIN_OFFICER)).getValue());
//        dto.setFacilityOfficerDto((FacilityOfficerDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_OFFICER)).getValue());
//        dto.setFacilityCommitteeDto((FacilityCommitteeDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_COMMITTEE)).getValue());
//        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode) facRegRoot.at(NODE_NAME_PRIMARY_DOC)).getValue();
//        dto.setDocRecordInfos(primaryDocDto.getSavedDocMap().values());
//
//        dto.setFacilityRegistrationReviewDto((FacilityRegistrationReviewDto) ((SimpleNode) viewApprovalRoot.at(NODE_NAME_REVIEW)).getValue());
//
//        NodeGroup batGroup = (NodeGroup) facRegRoot.at(NODE_NAME_FAC_BAT_INFO);
//        Map<String, BiologicalAgentToxinDto> batInfoMap = getBatInfoMap(batGroup);
//        dto.setBiologicalAgentToxinMap(batInfoMap);
//        return dto;
        return null;
    }
}
