package sg.gov.moh.iais.egp.bsb.common.rfc;

import sg.gov.moh.iais.egp.bsb.dto.rfc.DiffContent;
import sg.gov.moh.iais.egp.bsb.constant.RfcFlowType;

import java.util.List;

public interface DecisionFlowType {
    RfcFlowType facRegFlowType(List<DiffContent> list);

    RfcFlowType approvalAppFlowType(List<DiffContent> list);
}
