package com.ecquaria.cloud.moh.iais.tags;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

import javax.servlet.jsp.JspException;
import java.io.IOException;

/**
 * MasterCodeTag
 *
 * @author Jinhua
 * @date 2019/10/18 14:42
 */
@Slf4j
public class MasterCodeTag extends DivTagSupport {
    private static final long serialVersionUID = 8838025778803558930L;

    private String code;
    private boolean empty;
    private boolean viewEmptyStr;
    private boolean needLowerCase;
    private boolean needEscapHtml;

    public MasterCodeTag() {
        super();
        cleanFields();
    }

    // resets local state
    @Override
    protected void init() {
        super.init();
        cleanFields();
    }

    private void cleanFields() {
        try {
            setCode("");
        } catch (JspException e) {
            log.info(e.getMessage(),e);
        }
        setEmpty(false);
        setViewEmptyStr(false);
        setNeedEscapHtml(true);
    }

    // Releases any resources we may have (or inherit)
    @Override
    public void release() {
        super.release();
        init();
    }

    @Override
    public int doStartTag() throws JspException {
        String description = MasterCodeUtil.getCodeDesc(code);

        if (StringUtil.isEmpty(description) && viewEmptyStr)
            description = AppConsts.EMPTY_STR;
        else if (StringUtil.isEmpty(description) && !empty)
            description = code;
        if(!StringUtil.isEmpty(description) && needLowerCase)
            description = description.toLowerCase();
        if (needEscapHtml) {
            description = StringUtil.viewNonNullHtml(description);
        }
        try {
            pageContext.getOut().print(description);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new JspException(e);
        }

        return SKIP_BODY;
    }

    public void setCode(String code) throws JspException {
        this.code = StringUtil.nullToEmpty(ExpressionEvaluatorManager.evaluate("code",
                code, Object.class, this, pageContext));
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public void setViewEmptyStr(boolean viewEmptyStr) {
        this.viewEmptyStr = viewEmptyStr;
    }
    public void setNeedLowerCase(boolean needLowerCase){this.needLowerCase = needLowerCase;}
    public void setNeedEscapHtml(boolean needEscapHtml) {
        this.needEscapHtml = needEscapHtml;
    }
}
