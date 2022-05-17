package com.ecquaria.cloud.moh.iais.tags;

import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.csrfguard.CsrfGuard;
import lombok.extern.slf4j.Slf4j;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * <icms:processUrl value="CM_MasterCode" project="SYSTEM"/>
 *
 * @author lupeng
 */
@Slf4j
public class ProcessUrlTag extends TagSupport {
    private static final long serialVersionUID = 1L;
    private String value;
    private String project;
    private String processName;
    private String pathParams;
    private boolean hideUrl;

    public ProcessUrlTag() {
        super();
        init();
    }

    // resets local state
    private void init() {
        setValue("");
        setProject("");
        setProcessName("");
        setPathParams("");
        setHideUrl(true);
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public void setPathParams(String pathParams) {
        this.pathParams = pathParams;
    }

    // Releases any resources we may have (or inherit)
    @Override
    public void release() {
        super.release();
        init();
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            String html = "https://"  + pageContext.getRequest().getServerName()
                    + pageContext.getRequest().getServletContext().getContextPath() + "/eservice";
            html += "/" + project + "/" + processName;
            CsrfGuard csrf = CsrfGuard.getInstance();
            if (!StringUtil.isEmpty(pathParams)) {
                html += "?" + pathParams;
            }
            String showUrl = html;
            if (hideUrl) {
                showUrl = MiscUtil.generateUrlForView(html);
            } else {
                MaskUtil.maskUrlParams(value);
            }
            pageContext.getOut().print(showUrl);
        } catch (Exception ex) {
            log.error("", ex);
            throw new JspTagException("ProcessUrlTag: " + ex.getMessage(),ex);
        }
        release();

        return SKIP_BODY;
    }

    @Override
    public int doEndTag() {
        return EVAL_PAGE;
    }

    public void setValue(String value) {
        try {
            this.value = StringUtil.nullToEmpty(ExpressionEvaluatorManager.evaluate("value",
                     value, Object.class, this, pageContext));
        } catch (JspException e) {
            log.error(e.getMessage());
        }
    }
    public void setProject(String project) {
        this.project = project;
    }
    public void setHideUrl(boolean hideUrl) {
        this.hideUrl = hideUrl;
    }
}
