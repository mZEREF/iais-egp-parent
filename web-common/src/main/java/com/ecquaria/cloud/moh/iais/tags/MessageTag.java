package com.ecquaria.cloud.moh.iais.tags;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.SystemParamHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import java.util.HashMap;
import java.util.Objects;


/**
 * Message Tag
 *
 *
 * @date        7/24/2019
 * @author      suocheng
 */
public final class MessageTag extends DivTagSupport {
    private static final long serialVersionUID = 4071660568914826715L;

    private String key;
    private String params;
    private boolean escape;
    private String propertiesKey;
    private String replaceName;


    public MessageTag() {
        super();
        init();
    }

    // resets local state
    @Override
    protected void init() {
        setKey(null);
        setParams(null);
        setEscape(true);
        setPropertiesKey(null);
        setReplaceName(null);
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
            HashMap paramMap = null;
            if (!StringUtil.isEmpty(params)) {
                paramMap = (HashMap) ParamUtil.getScopeAttr((HttpServletRequest) pageContext.getRequest(), params);
            }else if (!StringUtil.isEmpty(propertiesKey)){
                paramMap = new HashMap<>();
                Objects.requireNonNull(replaceName);
                paramMap.put(replaceName, SystemParamHelper.getConfigValueByKey(propertiesKey));
            }

            String message = null;
            if (paramMap != null) {
                message = MessageUtil.getMessageDesc(key, paramMap);
            } else {
                message = MessageUtil.getMessageDesc(key);
            }

            if (escape) {
                pageContext.getOut().print(StringUtil.escapeJavascript(message));
            } else {
                pageContext.getOut().print(message);
            }
        } catch (Exception ex) {
            throw new JspTagException("MessageTag: " + ex.getMessage(),ex);
        }
        return SKIP_BODY;
    }
    @Override
    public int doEndTag() {
        init();
        return EVAL_PAGE;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public void setParams(String params) {
        this.params = params;
    }
    public boolean isEscape() {
        return escape;
    }
    public void setEscape(boolean escape) {
        this.escape = escape;
    }

    public void setReplaceName(String replaceName) {
        this.replaceName = replaceName;
    }

    public void setPropertiesKey(String propertiesKey) {
        this.propertiesKey = propertiesKey;
    }
}
