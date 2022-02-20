package sg.gov.moh.iais.egp.bsb.dto.approval;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.ApprovalAppConstants.*;

/**
 * @author : LiRan
 * @date : 2021/10/21
 */
@Data
@NoArgsConstructor
public class ApprovalAppDto implements Serializable {
    //this is used to saveDraft module
    private String appType;

    private ActivityDto activityDto;
    private Map<String, ApprovalProfileDto> approvalProfileMap;
    private Collection<DocRecordInfo> docRecordInfos;

    /** Write the approvalAppRoot NodeGroup into a DTO, then send the DTO to save the data. */
    public static ApprovalAppDto from(NodeGroup approvalAppRoot) {
        ApprovalAppDto dto = new ApprovalAppDto();
        dto.setActivityDto((ActivityDto) ((SimpleNode) approvalAppRoot.at(NODE_NAME_ACTIVITY)).getValue());
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode) approvalAppRoot.at(NODE_NAME_PRIMARY_DOC)).getValue();
        dto.setDocRecordInfos(primaryDocDto.getSavedDocMap().values());
        NodeGroup approvalProfileNodeGroup = (NodeGroup) approvalAppRoot.at(NODE_NAME_APPROVAL_PROFILE);
        Map<String, ApprovalProfileDto> approvalProfileMap = getApprovalProfileMap(approvalProfileNodeGroup);
        dto.setApprovalProfileMap(approvalProfileMap);
        return dto;
    }

    public static Map<String, ApprovalProfileDto> getApprovalProfileMap(NodeGroup approvalProfileNodeGroup) {
        Assert.notNull(approvalProfileNodeGroup, "Approval profile node group must not be null!");
        Map<String, ApprovalProfileDto> approvalProfileMap = Maps.newLinkedHashMapWithExpectedSize(approvalProfileNodeGroup.count());
        for (Node node : approvalProfileNodeGroup.getAllNodes()) {
            assert node instanceof SimpleNode;
            approvalProfileMap.put(node.getName(), (ApprovalProfileDto) ((SimpleNode) node).getValue());
        }
        return approvalProfileMap;
    }

    /** Convert data in this big DTO into a approvalAppRoot NodeGroup
     *  This is needed when we want to view the saved data or edit it */
    public NodeGroup toApprovalAppRootGroup(String name) {
        SimpleNode activityNode = new SimpleNode(activityDto,NODE_NAME_ACTIVITY,new Node[0]);

        NodeGroup.Builder approvalProfileNodeGroupBuilder = new NodeGroup.Builder().name(NODE_NAME_APPROVAL_PROFILE)
                .dependNodes(new Node[]{activityNode});
        for (Map.Entry<String, ApprovalProfileDto> entry : approvalProfileMap.entrySet()) {
            SimpleNode approvalProfileNode = new SimpleNode(entry.getValue(), entry.getKey(), new Node[0]);
            approvalProfileNodeGroupBuilder.addNode(approvalProfileNode);
        }
        NodeGroup approvalProfileNodeGroup = approvalProfileNodeGroupBuilder.build();

        Node companyInfoDto = new Node(NODE_NAME_COMPANY_INFO, new Node[0]);
        PrimaryDocDto primaryDocDto = new PrimaryDocDto();
        primaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(docRecordInfos, DocRecordInfo::getRepoId));
        SimpleNode primaryDocNode = new SimpleNode(primaryDocDto, NODE_NAME_PRIMARY_DOC, new Node[]{activityNode,approvalProfileNodeGroup});
        Node previewSubmitNode = new Node(NODE_NAME_PREVIEW_SUBMIT, new Node[]{activityNode,approvalProfileNodeGroup,primaryDocNode});
        return new NodeGroup.Builder().name(name)
                .addNode(companyInfoDto)
                .addNode(activityNode)
                .addNode(approvalProfileNodeGroup)
                .addNode(primaryDocNode)
                .addNode(previewSubmitNode)
                .build();
    }

}
