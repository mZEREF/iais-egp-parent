package sg.gov.moh.iais.egp.common.tags;

import sg.gov.moh.iais.egp.bsb.dto.info.approval.ApprovalBasicInfo;
import sg.gov.moh.iais.egp.bsb.service.inbox.AppActionJudge;
import sg.gov.moh.iais.egp.bsb.service.inbox.ApprovalUpdateJudge;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ApprovalActionTag extends SimpleTagSupport {
    private ApprovalBasicInfo info;
    private String attributeKey;

    @Override
    public void doTag() throws JspException, IOException {
        List<AppActionJudge> judgeList = Arrays.asList(
                new ApprovalUpdateJudge(info.getProcessType(), info.getStatus())
//                new ApprovalCancelJudge(info.getProcessType(), info.getStatus())
        );
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

    public void setInfo(ApprovalBasicInfo info) {
        this.info = info;
    }

    public String getAttributeKey() {
        return attributeKey;
    }

    public void setAttributeKey(String attributeKey) {
        this.attributeKey = attributeKey;
    }
}
