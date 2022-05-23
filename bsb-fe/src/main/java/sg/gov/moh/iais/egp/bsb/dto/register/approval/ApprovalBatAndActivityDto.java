package sg.gov.moh.iais.egp.bsb.dto.register.approval;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.entity.FacilityAuthoriserDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.bat.BiologicalAgentToxinDto;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.*;


/**
 * @author : LiRan
 * @date : 2022/3/17
 */
@Data
@NoArgsConstructor
public class ApprovalBatAndActivityDto implements Serializable {
    private String appType;

    private ApprovalSelectionDto approvalSelectionDto;
    private BiologicalAgentToxinDto approvalToPossessDto;
    private ApprovalToLargeDto approvalToLargeDto;
    private ApprovalToSpecialDto approvalToSpecialDto;
    private ApprovalToActivityDto approvalToActivityDto;
    private FacAuthorisedDto facAuthorisedDto;
    private Collection<DocRecordInfo> docRecordInfos;
    private PreviewDto previewDto;

    /** Convert data in this big DTO into a approvalAppRoot NodeGroup
     *  This is needed when we want to view the saved data or edit it */
    public NodeGroup toApprovalAppRootGroup(String name) {
      String processType = this.approvalSelectionDto.getProcessType();

        if(!StringUtils.hasLength(processType)){
            throw new IllegalArgumentException("process type is null");
        }

      Node facProfileNode = new Node(NODE_NAME_FAC_PROFILE, new Node[0]);
        NodeGroup appInfoNodeGroup = null;
        switch (processType) {
            case MasterCodeConstants.PROCESS_TYPE_APPROVE_POSSESS:
                SimpleNode possessBatNode = new SimpleNode(approvalToPossessDto, NODE_NAME_POSSESS_BAT, new Node[]{facProfileNode});
                appInfoNodeGroup = new NodeGroup.Builder().name(NODE_NAME_APP_INFO).dependNodes(new Node[0]).addNode(facProfileNode).addNode(possessBatNode).build();
                break;
            case MasterCodeConstants.PROCESS_TYPE_APPROVE_LSP:
                SimpleNode largeBatNode = new SimpleNode(approvalToLargeDto, NODE_NAME_LARGE_BAT, new Node[]{facProfileNode});
                appInfoNodeGroup = new NodeGroup.Builder().name(NODE_NAME_APP_INFO).dependNodes(new Node[0]).addNode(facProfileNode).addNode(largeBatNode).build();
                break;
            case MasterCodeConstants.PROCESS_TYPE_SP_APPROVE_HANDLE:
                SimpleNode specialBatNode = new SimpleNode(approvalToSpecialDto, NODE_NAME_SPECIAL_BAT, new Node[]{facProfileNode});
                SimpleNode facAuthorisedNode = new SimpleNode(facAuthorisedDto, NODE_NAME_FAC_AUTHORISED, new Node[]{facProfileNode, specialBatNode});
                appInfoNodeGroup = new NodeGroup.Builder().name(NODE_NAME_APP_INFO)
                        .dependNodes(new Node[0]).addNode(facProfileNode)
                        .addNode(specialBatNode)
                        .addNode(facAuthorisedNode)
                        .build();
                break;
            case MasterCodeConstants.PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE:
                SimpleNode facActivityNode = new SimpleNode(approvalToActivityDto, NODE_NAME_FAC_ACTIVITY, new Node[]{facProfileNode});
                appInfoNodeGroup = new NodeGroup.Builder().name(NODE_NAME_APP_INFO).dependNodes(new Node[0]).addNode(facProfileNode).addNode(facActivityNode).build();
                break;
            default:
                break;
        }

        PrimaryDocDto primaryDocDto = new PrimaryDocDto();
        primaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(docRecordInfos,DocRecordInfo::getRepoId));

        SimpleNode primaryDocNode = new SimpleNode(primaryDocDto, NODE_NAME_PRIMARY_DOC, new Node[]{appInfoNodeGroup});
        SimpleNode previewNode = new SimpleNode(previewDto, NODE_NAME_PREVIEW, new Node[]{appInfoNodeGroup, primaryDocNode});

        return new NodeGroup.Builder().name(name)
                .addNode(appInfoNodeGroup)
                .addNode(primaryDocNode)
                .addNode(previewNode)
                .build();
    }

    /** Write the approvalAppRoot NodeGroup into a DTO, then send the DTO to save the data. */
    public static ApprovalBatAndActivityDto  from(ApprovalSelectionDto approvalSelectionDto,NodeGroup approvalAppRoot) {
        ApprovalBatAndActivityDto approvalBatAndActivityDto = new ApprovalBatAndActivityDto();
        approvalBatAndActivityDto.setApprovalSelectionDto(approvalSelectionDto);
       String processType  = approvalSelectionDto.getProcessType();

       if(!StringUtils.hasLength(processType)){
           throw new IllegalArgumentException("process type is null");
       }

        switch (processType) {
            case MasterCodeConstants.PROCESS_TYPE_APPROVE_POSSESS:
                approvalBatAndActivityDto.setApprovalToPossessDto((BiologicalAgentToxinDto) ((SimpleNode) approvalAppRoot.at(NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_POSSESS_BAT)).getValue());
                break;
            case MasterCodeConstants.PROCESS_TYPE_APPROVE_LSP:
                approvalBatAndActivityDto.setApprovalToLargeDto((ApprovalToLargeDto) ((SimpleNode) approvalAppRoot.at(NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_LARGE_BAT)).getValue());
                break;
            case MasterCodeConstants.PROCESS_TYPE_SP_APPROVE_HANDLE:
                approvalBatAndActivityDto.setApprovalToSpecialDto((ApprovalToSpecialDto) ((SimpleNode) approvalAppRoot.at(NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_SPECIAL_BAT)).getValue());
                FacAuthorisedDto facAuthorisedDto = (FacAuthorisedDto) ((SimpleNode) approvalAppRoot.at(NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_FAC_AUTHORISED)).getValue();
                approvalBatAndActivityDto.setFacAuthorisedDto(facAuthorisedDto);
                break;
            case MasterCodeConstants.PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE:
                approvalBatAndActivityDto.setApprovalToActivityDto((ApprovalToActivityDto) ((SimpleNode) approvalAppRoot.at(NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_FAC_ACTIVITY)).getValue());
                break;
            default:
                break;
        }
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode) approvalAppRoot.at(NODE_NAME_PRIMARY_DOC)).getValue();
        approvalBatAndActivityDto.setDocRecordInfos(primaryDocDto.getSavedDocMap().values());
        approvalBatAndActivityDto.setPreviewDto((PreviewDto) ((SimpleNode) approvalAppRoot.at(NODE_NAME_PREVIEW)).getValue());
        return approvalBatAndActivityDto;
    }
}
