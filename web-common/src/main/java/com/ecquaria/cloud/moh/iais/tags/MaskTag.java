package com.ecquaria.cloud.moh.iais.tags;

import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * MaskTag
 *
 * @author Jinhua
 * @date 2019/10/16 15:42
 */
@Slf4j
public class MaskTag extends DivTagSupport {
    private static final long serialVersionUID = -8932893292865112591L;

    private String value;
    private String name;

    public MaskTag() {
        super();
        init();//NOSONAR
    }

    @Override
    protected void init() {
        try {
            setValue("");
        } catch (JspException e) {
            log.info(e.getMessage(),e);
        }
        try {
            setName("");
        } catch (JspException e) {
            log.info(e.getMessage(),e);
        }

    }

    // Releases any resources we may have (or inherit)
    @Override
    public void release() {
        super.release();
        init();
    }

    @Override
    public int doStartTag(){
        try {
            pageContext.getOut().print(MaskUtil.maskValue(name, value));
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new IaisRuntimeException("Mask Tag: " + ex.getMessage());
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
                value, Object.class, this, pageContext));
    }

    public void setName(String name) throws JspException {
        this.name = StringUtil.nullToEmpty(ExpressionEvaluatorManager.evaluate("name",
                name, Object.class, this, pageContext));
    }
}
