package com.ecquaria.cloud.moh.iais.tags;

import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.csrfguard.CsrfGuard;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.log4j.Logger;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

/**
 * <icms:processUrl value="CM_MasterCode" project="SYSTEM"/>
 *
 * @author lupeng
 */
public class ProcessUrlTag extends TagSupport {
    private static final long serialVersionUID = -8544224262301150921L;
    private static Logger log = Logger.getLogger(ProcessUrlTag.class.getName());

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
        value = "";
        project = "";
        processName = "";
        pathParams = "";
        hideUrl = true;
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
            throw new JspTagException("ProcessUrlTag: " + ex.getMessage());
        }
        release();

        return SKIP_BODY;
    }

    @Override
    public int doEndTag() {
        return EVAL_PAGE;
    }

    public void setValue(String value) throws JspException {
        this.value = StringUtil.nullToEmpty(ExpressionEvaluatorManager.evaluate("value",
                 value.toString(), Object.class, this, pageContext));
    }
    public void setProject(String project) {
        this.project = project;
    }
    public void setHideUrl(boolean hideUrl) {
        this.hideUrl = hideUrl;
    }
}
