package com.ecquaria.cloud.moh.iais.tags;

/*
 *author: yichen
 *date time:12/17/2019 7:09 PM
 *description: <input type="hidden" name="valCurrentResourceName" id="valCurrentResourceName" value="<iais:page request="${pageContext.request}"/>"/>
 */

import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspTagException;

@Setter
@Getter
@Slf4j
public final class ResourceNameTag extends DivTagSupport{
    private static final long serialVersionUID = 1L;

    HttpServletRequest request;

    public ResourceNameTag(){
        super();
        init();
    }

    // resets local state
    @Override
    protected void init() {
        request = null;
    }

    // Releases any resources we may have (or inherit)
    @Override
    public void release() {
        super.release();
        init();
    }

    @Override
    public int doStartTag() throws JspTagException {
        StringBuilder html = new StringBuilder();
        String url = request.getRequestURI();
        if (url.isEmpty()){
            return SKIP_BODY;
        }

        int len = url.length() - 1;
        for (int i = len; i > 0; i--){
            if (url.charAt(i) == '/'){
                String cut = url.substring(i + 1);
                html.append(cut);
                break;
            }
        }

        try {
            pageContext.getOut().print(StringUtil.escapeSecurityScript(html.toString().trim()));
        } catch (Exception ex) {
            throw new JspTagException("ResourceNameTag: " + ex.getMessage(),ex);
        }

        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() {
        init();
        return EVAL_PAGE;
    }
}
