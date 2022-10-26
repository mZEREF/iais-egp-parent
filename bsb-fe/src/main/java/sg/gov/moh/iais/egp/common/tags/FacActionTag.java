package sg.gov.moh.iais.egp.common.tags;

import sg.gov.moh.iais.egp.bsb.service.inbox.DeferRenewalJudge;
import sg.gov.moh.iais.egp.bsb.service.inbox.DeregisterJudge;
import sg.gov.moh.iais.egp.bsb.service.inbox.FacActionJudge;
import sg.gov.moh.iais.egp.bsb.service.inbox.FacApplyApprovalJudge;
import sg.gov.moh.iais.egp.bsb.service.inbox.FacRenewJudge;
import sg.gov.moh.iais.egp.bsb.service.inbox.FacRfcJudge;
import sg.gov.moh.iais.egp.bsb.service.inbox.IncidentReportingJudge;
import sg.gov.moh.iais.egp.bsb.service.inbox.InventoryNotificationDataSubmissionJudge;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FacActionTag extends SimpleTagSupport  {
    private String facClassification;
    private String facStatus;
    private Boolean renewable;
    private String attributeKey;

    @Override
    public void doTag() throws JspException, IOException {
        List<FacActionJudge> judgeList = Arrays.asList(
                new FacApplyApprovalJudge(facStatus, facClassification),
//                new InventoryNotificationDataSubmissionJudge(facStatus),
                new FacRenewJudge(facStatus, facClassification, renewable),
                new FacRfcJudge(facStatus, facClassification),
//                new IncidentReportingJudge(facStatus),
//                new DeregisterJudge(facStatus),
                new DeferRenewalJudge(facStatus, facClassification, renewable)
        );
        boolean actionAvailable = false;
        JspContext context = getJspContext();

        for (FacActionJudge judge : judgeList) {
            if (judge.judge()) {
                actionAvailable = true;
                context.setAttribute(judge.getClass().getSimpleName(), Boolean.TRUE);
            } else {
                context.setAttribute(judge.getClass().getSimpleName(), Boolean.FALSE);
            }
        }
        context.setAttribute(this.attributeKey, actionAvailable);
    }

    public void setFacClassification(String facClassification) {
        this.facClassification = facClassification;
    }

    public void setFacStatus(String facStatus) {
        this.facStatus = facStatus;
    }

    public void setRenewable(Boolean renewable) {
        this.renewable = renewable;
    }

    public void setAttributeKey(String attributeKey) {
        this.attributeKey = attributeKey;
    }
}
