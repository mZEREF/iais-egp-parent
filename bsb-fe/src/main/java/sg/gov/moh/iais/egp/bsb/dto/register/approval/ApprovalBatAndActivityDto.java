package sg.gov.moh.iais.egp.bsb.dto.register.approval;

import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;

import java.io.Serializable;


/**
 * @author : LiRan
 * @date : 2022/3/17
 */
@Data
@NoArgsConstructor
public class ApprovalBatAndActivityDto implements Serializable {
    private ApprovalSelectionDto approvalSelectionDto;
    private FacProfileDto facProfileDto;
    private ApprovalToPossessDto approvalToPossessDto;
    private ApprovalToLargeDto approvalToLargeDto;
    private ApprovalToSpecialDto approvalToSpecialDto;
    private ApprovalToActivityDto approvalToActivityDto;
    private PrimaryDocDto primaryDocDto;
    private PreviewDto previewDto;

    /** Convert data in this big DTO into a approvalAppRoot NodeGroup
     *  This is needed when we want to view the saved data or edit it */
    public NodeGroup toApprovalAppRootGroup(String name) {
        return null;
    }

    /** Write the approvalAppRoot NodeGroup into a DTO, then send the DTO to save the data. */
    public static ApprovalBatAndActivityDto from(NodeGroup approvalAppRoot) {
        return null;
    }
}
