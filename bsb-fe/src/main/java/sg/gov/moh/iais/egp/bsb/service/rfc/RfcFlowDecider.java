package sg.gov.moh.iais.egp.bsb.service.rfc;

import sg.gov.moh.iais.egp.bsb.constant.RfcFlowType;

import java.util.Collection;

public interface RfcFlowDecider {
    RfcFlowType decide4Facility(Collection<String> changedKeys);
    RfcFlowType decide4Approval(Collection<String> changedKeys);
}
