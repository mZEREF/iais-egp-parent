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
    private static final long serialVersionUID = -8168656491060372310L;

    private String code;
    private boolean empty;
    private boolean viewEmptyStr;

    public MasterCodeTag() {
        super();
        init();
    }

    // resets local state
    protected void init() {
        code = "";
        empty = false;
        viewEmptyStr = false;
    }

    // Releases any resources we may have (or inherit)
    public void release() {
        super.release();
        init();
    }

    public int doStartTag() throws JspException {
        String description = MasterCodeUtil.getCodeDesc(code);

        if (StringUtil.isEmpty(description) && viewEmptyStr)
            description = AppConsts.EMPTY_STR;
        else if (StringUtil.isEmpty(description) && !empty)
            description = code;

        try {
            pageContext.getOut().print(StringUtil.viewNonNullHtml(description));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new JspException(e);
        }

        return SKIP_BODY;
    }

    public void setCode(String code) throws JspException {
        this.code = StringUtil.nullToEmpty(ExpressionEvaluatorManager.evaluate("code",
                code.toString(), Object.class, this, pageContext));
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public void setViewEmptyStr(boolean viewEmptyStr) {
        this.viewEmptyStr = viewEmptyStr;
    }
}
