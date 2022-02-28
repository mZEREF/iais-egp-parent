package sg.gov.moh.iais.egp.common.tags;

import sg.gov.moh.iais.egp.bsb.dto.info.common.AppMainInfo;
import sg.gov.moh.iais.egp.bsb.service.inbox.*;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AppActionTag extends SimpleTagSupport {
    private AppMainInfo info;


    @Override
    public void doTag() throws JspException, IOException {
        List<AppActionJudge> judgeList = Arrays.asList(
                new AppEditableJudge(info.getStatus(), info.getAssigned()),
                new DraftAppJudge(info.getStatus()),
                new SelfAssessJudge(info.getStatus()),
                new InsReportJudge(info.getStatus()),
                new AppWithdrawableJudge(info.getAppType(), info.getStatus()));
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
        context.setAttribute("actionAvailable", actionAvailable);
    }



    public void setInfo(AppMainInfo info) {
        this.info = info;
    }
}