package sg.gov.moh.iais.egp.common.tags;

import sg.gov.moh.iais.egp.bsb.dto.info.common.AppMainInfo;
import sg.gov.moh.iais.egp.bsb.service.inbox.AFCUploadReportJudge;
import sg.gov.moh.iais.egp.bsb.service.inbox.AppActionJudge;
import sg.gov.moh.iais.egp.bsb.service.inbox.AppWithdrawableJudge;
import sg.gov.moh.iais.egp.bsb.service.inbox.ApplicantUploadCertReportJudge;
import sg.gov.moh.iais.egp.bsb.service.inbox.DraftAppJudge;
import sg.gov.moh.iais.egp.bsb.service.inbox.InspectionAfcSelectionJudge;
import sg.gov.moh.iais.egp.bsb.service.inbox.InspectionAppointmentJudge;
import sg.gov.moh.iais.egp.bsb.service.inbox.InspectionChecklistJudge;
import sg.gov.moh.iais.egp.bsb.service.inbox.InspectionFollowUpJudge;
import sg.gov.moh.iais.egp.bsb.service.inbox.InspectionNCJudge;
import sg.gov.moh.iais.egp.bsb.service.inbox.RFIJudge;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AppActionTag extends SimpleTagSupport {
    private AppMainInfo info;
    private String attributeKey;

    @Override
    public void doTag() throws JspException, IOException {
        List<AppActionJudge> judgeList = Arrays.asList(
                new DraftAppJudge(info.getStatus()),
                new RFIJudge(info.getStatus()),
                new AppWithdrawableJudge(info.getAppType(), info.getStatus(), info.getStage()),
                new AFCUploadReportJudge(info.getAppType(), info.getStatus()),
                new ApplicantUploadCertReportJudge(info.getAppType(), info.getStatus()),
                new InspectionAppointmentJudge(info.getStatus()),
                new InspectionFollowUpJudge(info.getStatus()),
                new InspectionNCJudge(info.getStatus()),
                new InspectionChecklistJudge(info.getStatus()),
                new InspectionAfcSelectionJudge(info.getStatus()));
        boolean actionAvailable = false;
        JspContext context = getJspContext();
        for (AppActionJudge judge : judgeList) {
            if (judge.judge()) {
                actionAvailable = true;
                context.setAttribute(judge.getClass().getSimpleName(), Boolean.TRUE);
            } else {
                context.setAttribute(judge.getClass().getSimpleName(), Boolean.FALSE);
            }
        }
        context.setAttribute(this.attributeKey, actionAvailable);
    }

    public void setInfo(AppMainInfo info) {
        this.info = info;
    }

    public String getAttributeKey() {
        return attributeKey;
    }

    public void setAttributeKey(String attributeKey) {
        this.attributeKey = attributeKey;
    }
}
