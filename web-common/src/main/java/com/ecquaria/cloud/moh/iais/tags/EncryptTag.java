package com.ecquaria.cloud.moh.iais.tags;

import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

import javax.servlet.jsp.JspException;
import java.io.IOException;

/**
 * @Description EncryptTag <iais:encrypt var="" value="${...}" /> or <iais:encrypt value="${...}" />
 * @Auther chenlei on 9/28/2021.
 */
@Slf4j
public class EncryptTag extends DivTagSupport {

    private String value;
    private String var;
    private String scope;

    @Override
    protected void init() {
        super.init();
        try {
            setValue("");
        } catch (JspException e) {
            log.info(e.getMessage(), e);
        }
        setVar(null);
        setScope(null);
    }

    @Override
    public int doStartTag() throws JspException {
        String encrypted = StringUtil.getNonNull(StringUtil.obscured(value));
        if (!StringUtil.isEmpty(var)) {
            if ("requst".equals(scope)) {
                pageContext.getRequest().setAttribute(var, encrypted);
            } else if ("session".equals(scope)) {
                pageContext.getSession().setAttribute(var, encrypted);
            } else {
                pageContext.setAttribute(var, encrypted);
            }
        }
        try {
            pageContext.getOut().print(encrypted);
        } catch (IOException e) {
            log.error(StringUtil.changeForLog(e.getMessage()), e);
            throw new IaisRuntimeException("Encrypt Tag: " + e.getMessage(), e);
        }
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public void setValue(String value) throws JspException {
        this.value = StringUtil.nullToEmpty(ExpressionEvaluatorManager.evaluate("value",
                value, Object.class, this, pageContext));
    }

}
