package sg.gov.moh.iais.egp.common.tags;

import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxAppResultDto;
import sg.gov.moh.iais.egp.bsb.dto.info.common.AppMainInfo;
import sg.gov.moh.iais.egp.bsb.service.inbox.AFCUploadReportJudge;
import sg.gov.moh.iais.egp.bsb.service.inbox.AppActionJudge;
import sg.gov.moh.iais.egp.bsb.service.inbox.AppEditableJudge;
import sg.gov.moh.iais.egp.bsb.service.inbox.AppWithdrawableJudge;
import sg.gov.moh.iais.egp.bsb.service.inbox.ApplicantUploadCertReportJudge;
import sg.gov.moh.iais.egp.bsb.service.inbox.DraftAppJudge;
import sg.gov.moh.iais.egp.bsb.service.inbox.InsAppointmentJudge;
import sg.gov.moh.iais.egp.bsb.service.inbox.InsReportJudge;
import sg.gov.moh.iais.egp.bsb.service.inbox.InspectionFollowUpJudge;
import sg.gov.moh.iais.egp.bsb.service.inbox.RfiJudge;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AppActionTag extends SimpleTagSupport {
    private InboxAppResultDto info;
    private String attributeKey;

    @Override
    public void doTag() throws JspException, IOException {
        List<AppActionJudge> judgeList = Arrays.asList(
                new AppEditableJudge(info.getAppStatus(), info.getAssigned()),
                new DraftAppJudge(info.getAppStatus()),
                new InsReportJudge(info.getAppStatus()),
                new AppWithdrawableJudge(info.getAppType(), info.getAppStatus()),
                new InsAppointmentJudge(info.getAppType(), info.getAppStatus()),
                new AFCUploadReportJudge(info.getAppType(), info.getAppStatus()),
                new ApplicantUploadCertReportJudge(info.getAppType(), info.getAppStatus()),
                new RfiJudge(info.getAppStatus()),
                new InspectionFollowUpJudge(info.getAppType(), info.getAppStatus()));
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

    public void setInfo(InboxAppResultDto info) {
        this.info = info;
    }

    public String getAttributeKey() {
        return attributeKey;
    }

    public void setAttributeKey(String attributeKey) {
        this.attributeKey = attributeKey;
    }
}
