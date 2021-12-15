package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.renewal.InstructionDto;
import sg.gov.moh.iais.egp.bsb.dto.renewal.ReviewDto;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.*;


@Data
@NoArgsConstructor
public class FacilityRegisterDto implements Serializable{
    private FacilitySelectionDto facilitySelectionDto;
    private FacilityProfileDto facilityProfileDto;
    private FacilityOperatorDto facilityOperatorDto;
    private FacilityAuthoriserDto facilityAuthoriserDto;
    private FacilityAdministratorDto facilityAdministratorDto;
    private FacilityOfficerDto facilityOfficerDto;
    private FacilityCommitteeDto facilityCommitteeDto;
    private Map<String, BiologicalAgentToxinDto> biologicalAgentToxinMap;
    private Collection<DocRecordInfo> docRecordInfos;
    private PreviewSubmitDto previewSubmitDto;
    private AuditTrailDto auditTrailDto;

    //renewal special dto
    private InstructionDto instructionDto;
    private ReviewDto reviewDto;

    /** Write the facRegRoot NodeGroup into a DTO, then send the DTO to save the data. */
    public static FacilityRegisterDto from(NodeGroup facRegRoot) {
        FacilityRegisterDto dto = new FacilityRegisterDto();
        dto.setFacilitySelectionDto((FacilitySelectionDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_SELECTION)).getValue());
        dto.setFacilityProfileDto((FacilityProfileDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_PROFILE)).getValue());
        dto.setFacilityOperatorDto((FacilityOperatorDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_OPERATOR)).getValue());
        dto.setFacilityAuthoriserDto((FacilityAuthoriserDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_AUTH)).getValue());
        dto.setFacilityAdministratorDto((FacilityAdministratorDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_ADMIN)).getValue());
        dto.setFacilityOfficerDto((FacilityOfficerDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_OFFICER)).getValue());
        dto.setFacilityCommitteeDto((FacilityCommitteeDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_COMMITTEE)).getValue());
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode) facRegRoot.at(NODE_NAME_PRIMARY_DOC)).getValue();
        dto.setDocRecordInfos(primaryDocDto.getSavedDocMap().values());
        dto.setPreviewSubmitDto((PreviewSubmitDto) ((SimpleNode) facRegRoot.at(NODE_NAME_PREVIEW_SUBMIT)).getValue());

        NodeGroup batGroup = (NodeGroup) facRegRoot.at(NODE_NAME_FAC_BAT_INFO);
        Map<String, BiologicalAgentToxinDto> batInfoMap = getBatInfoMap(batGroup);
        dto.setBiologicalAgentToxinMap(batInfoMap);
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
        SimpleNode facSelectionNode = new SimpleNode(facilitySelectionDto, NODE_NAME_FAC_SELECTION, new Node[0]);
        SimpleNode facProfileNode = new SimpleNode(facilityProfileDto, NODE_NAME_FAC_PROFILE, new Node[0]);
        SimpleNode facOperatorNode = new SimpleNode(facilityOperatorDto, NODE_NAME_FAC_OPERATOR, new Node[]{facProfileNode});
        SimpleNode facAuthNode = new SimpleNode(facilityAuthoriserDto, NODE_NAME_FAC_AUTH, new Node[]{facProfileNode, facOperatorNode});
        SimpleNode facAdminNode = new SimpleNode(facilityAdministratorDto, NODE_NAME_FAC_ADMIN, new Node[]{facProfileNode, facOperatorNode, facAuthNode});
        SimpleNode facOfficerNode = new SimpleNode(facilityOfficerDto, NODE_NAME_FAC_OFFICER, new Node[]{facProfileNode, facOperatorNode, facAuthNode, facAdminNode});
        SimpleNode facCommitteeNode = new SimpleNode(facilityCommitteeDto, NODE_NAME_FAC_COMMITTEE, new Node[]{facProfileNode, facOperatorNode, facAuthNode, facAdminNode, facOfficerNode});

        NodeGroup facInfoNodeGroup = new NodeGroup.Builder().name(NODE_NAME_FAC_INFO)
                .dependNodes(new Node[]{facSelectionNode})
                .addNode(facProfileNode)
                .addNode(facOperatorNode)
                .addNode(facAuthNode)
                .addNode(facAdminNode)
                .addNode(facOfficerNode)
                .addNode(facCommitteeNode)
                .build();

        NodeGroup.Builder batInfoNodeGroupBuilder = new NodeGroup.Builder().name(NODE_NAME_FAC_BAT_INFO)
                .dependNodes(new Node[]{facSelectionNode, facInfoNodeGroup});
        for (Map.Entry<String, BiologicalAgentToxinDto> entry : biologicalAgentToxinMap.entrySet()) {
            SimpleNode batNode = new SimpleNode(entry.getValue(), entry.getKey(), new Node[0]);
            batInfoNodeGroupBuilder.addNode(batNode);
        }
        NodeGroup batInfoNodeGroup = batInfoNodeGroupBuilder.build();

        Node companyInfoDto = new Node(FacRegisterConstants.NODE_NAME_COMPANY_INFO, new Node[0]);
        SimpleNode otherAppInfoNode = new SimpleNode(OtherApplicationInfoDto.getAllCheckedInstance(), NODE_NAME_OTHER_INFO, new Node[]{facSelectionNode, facInfoNodeGroup, batInfoNodeGroup});
        PrimaryDocDto primaryDocDto = new PrimaryDocDto();
        primaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(docRecordInfos, DocRecordInfo::getRepoId));
        SimpleNode primaryDocNode = new SimpleNode(primaryDocDto, NODE_NAME_PRIMARY_DOC, new Node[]{facSelectionNode, facInfoNodeGroup, batInfoNodeGroup, otherAppInfoNode});
        SimpleNode previewSubmitNode = new SimpleNode(previewSubmitDto, NODE_NAME_PREVIEW_SUBMIT, new Node[]{facSelectionNode, facInfoNodeGroup, batInfoNodeGroup, otherAppInfoNode, primaryDocNode});
        return new NodeGroup.Builder().name(name)
                .addNode(companyInfoDto)
                .addNode(facSelectionNode)
                .addNode(facInfoNodeGroup)
                .addNode(batInfoNodeGroup)
                .addNode(otherAppInfoNode)
                .addNode(primaryDocNode)
                .addNode(previewSubmitNode)
                .build();
    }

    /********************Renewal*********************/

    public NodeGroup toRenewalReviewRootGroup(String name){
        Node instructionNode = new Node(NODE_NAME_INSTRUCTION, new Node[0]);
        SimpleNode reviewNode = new SimpleNode(reviewDto, NODE_NAME_REVIEW, new Node[]{instructionNode});
        return new NodeGroup.Builder().name(name)
                .addNode(instructionNode)
                .addNode(reviewNode)
                .build();
    }

    public NodeGroup toRenewalFacRegRootGroup(String name) {
        SimpleNode facSelectionNode = new SimpleNode(facilitySelectionDto, NODE_NAME_FAC_SELECTION, new Node[0]);
        SimpleNode facProfileNode = new SimpleNode(facilityProfileDto, NODE_NAME_FAC_PROFILE, new Node[0]);
        SimpleNode facOperatorNode = new SimpleNode(facilityOperatorDto, NODE_NAME_FAC_OPERATOR, new Node[]{facProfileNode});
        SimpleNode facAuthNode = new SimpleNode(facilityAuthoriserDto, NODE_NAME_FAC_AUTH, new Node[]{facProfileNode, facOperatorNode});
        SimpleNode facAdminNode = new SimpleNode(facilityAdministratorDto, NODE_NAME_FAC_ADMIN, new Node[]{facProfileNode, facOperatorNode, facAuthNode});
        SimpleNode facOfficerNode = new SimpleNode(facilityOfficerDto, NODE_NAME_FAC_OFFICER, new Node[]{facProfileNode, facOperatorNode, facAuthNode, facAdminNode});
        SimpleNode facCommitteeNode = new SimpleNode(facilityCommitteeDto, NODE_NAME_FAC_COMMITTEE, new Node[]{facProfileNode, facOperatorNode, facAuthNode, facAdminNode, facOfficerNode});

        NodeGroup facInfoNodeGroup = new NodeGroup.Builder().name(NODE_NAME_FAC_INFO)
                .dependNodes(new Node[]{facSelectionNode})
                .addNode(facProfileNode)
                .addNode(facOperatorNode)
                .addNode(facAuthNode)
                .addNode(facAdminNode)
                .addNode(facOfficerNode)
                .addNode(facCommitteeNode)
                .build();

        NodeGroup.Builder batInfoNodeGroupBuilder = new NodeGroup.Builder().name(NODE_NAME_FAC_BAT_INFO)
                .dependNodes(new Node[]{facSelectionNode, facInfoNodeGroup});
        for (Map.Entry<String, BiologicalAgentToxinDto> entry : biologicalAgentToxinMap.entrySet()) {
            SimpleNode batNode = new SimpleNode(entry.getValue(), entry.getKey(), new Node[0]);
            batInfoNodeGroupBuilder.addNode(batNode);
        }
        NodeGroup batInfoNodeGroup = batInfoNodeGroupBuilder.build();

        Node companyInfoDto = new Node(FacRegisterConstants.NODE_NAME_COMPANY_INFO, new Node[0]);
        SimpleNode otherAppInfoNode = new SimpleNode(OtherApplicationInfoDto.getAllCheckedInstance(), NODE_NAME_OTHER_INFO, new Node[]{facSelectionNode, facInfoNodeGroup, batInfoNodeGroup});
        PrimaryDocDto primaryDocDto = new PrimaryDocDto();
        primaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(docRecordInfos, DocRecordInfo::getRepoId));
        SimpleNode primaryDocNode = new SimpleNode(primaryDocDto, NODE_NAME_PRIMARY_DOC, new Node[]{facSelectionNode, facInfoNodeGroup, batInfoNodeGroup, otherAppInfoNode});
        return new NodeGroup.Builder().name(name)
                .addNode(companyInfoDto)
                .addNode(facSelectionNode)
                .addNode(facInfoNodeGroup)
                .addNode(batInfoNodeGroup)
                .addNode(otherAppInfoNode)
                .addNode(primaryDocNode)
                .build();
    }
}
