package sg.gov.moh.iais.egp.bsb.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javers.common.collections.Sets;
import org.springframework.stereotype.Service;
import sg.gov.moh.iais.egp.bsb.constant.RfcFlowType;
import sg.gov.moh.iais.egp.bsb.service.rfc.RfcFlowDecider;

import java.util.Collection;
import java.util.Set;


@Slf4j
@Service
@RequiredArgsConstructor
public class RfcService {
    private final RfcFlowDecider flowDecider;


    public RfcFlowType decideFacilityFlow(Collection<String> changedKeys) {
        return flowDecider.decide4Facility(changedKeys);
    }

    public RfcFlowType decideFlow4PartOfFacility(String keyPrefix, Set<String> changedKeys) {
        return flowDecider.decide4Facility(Sets.transform(changedKeys, k -> keyPrefix + "." + k));
    }

    public RfcFlowType decideApprovalFlow(Collection<String> changedKeys) {
        return flowDecider.decide4Approval(changedKeys);
    }

    public RfcFlowType decideFlow4PartOfApproval(String keyPrefix, Set<String> changedKeys) {
        return flowDecider.decide4Approval(Sets.transform(changedKeys, k -> keyPrefix + "." + k));
    }
}
