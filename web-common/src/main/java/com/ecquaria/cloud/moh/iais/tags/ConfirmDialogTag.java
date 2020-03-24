package com.ecquaria.cloud.moh.iais.tags;

import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

/**
 * ConfirmDialogTag
 *
 * @author Jinhua
 * @date 2020/2/22 15:48
 */
@Slf4j
public class ConfirmDialogTag extends TagSupport {
    private static final long serialVersionUID = -712640936845353103L;

    private String msg;
    private String callBack;
    private String popupOrder;
    private String title;
    private boolean needCancel;
    private boolean needFungDuoJi;

    public ConfirmDialogTag() {
        super();
        init();
    }

    // resets local state
    private void init() {
        msg = "";
        callBack = "";
        popupOrder = "";
        title = "";
        needCancel = true;
        needFungDuoJi = true;
    }

    // Releases any resources we may have (or inherit)
    @Override
    public void release() {
        super.release();
        init();
    }

    @Override
    public int doStartTag() throws JspException {
        StringBuilder html = new StringBuilder();
        //html
        String divId = popupOrder;
        html.append("<div id=\"").append(divId).append("\"");
        html.append(" class=\"modal fade\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"").append(divId);
        html.append("\" style=\"left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%;");
        html.append("overflow: visible;bottom: inherit;right: inherit;\">");
        html.append("<div class=\"modal-dialog\" role=\"document\"><div class=\"modal-content\">");
        html.append("<div class=\"modal-header\">");
        html.append("<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\"><span");
        html.append(" aria-hidden=\"true\">&times;</span></button>");
        html.append(" <h5 class=\"modal-title\" id=\"gridSystemModalLabel\">Confirmation Box</h5></div>");
        html.append("<div class=\"modal-body\"><div class=\"row\">");
        if (needFungDuoJi) {
            html.append("<input type=\"hidden\" name=\"fangDuoJi").append(divId).append("\" id=\"fangDuoJi").append(divId).append("\"/>");
        }
        html.append("<div class=\"col-md-8 col-md-offset-2\"><span style=\"font-size: 2rem\">");
        html.append(MessageUtil.getMessageDesc(msg));
        html.append("</span></div></div></div>");
        html.append("<div class=\"modal-footer\">");
        html.append("<button type=\"button\" class=\"btn btn-primary\" onclick=\"javascript:");
        html.append("tagConfirmCallback").append(popupOrder).append("();\">OK</button>");
        if (needCancel) {
            html.append("<button type=\"button\" class=\"btn btn-secondary\" data-dismiss=\"modal\">Cancel</button>");
        }
        html.append("</div></div></div></div>");
        //javascript
        html.append("<script type=\"text/javascript\">");
        html.append("function tagConfirmCallback").append(popupOrder).append("() {");
        if (needFungDuoJi) {
            html.append("var fangDuoJi = $('#fangDuoJi").append(divId).append("').val();");
            html.append("if(fangDuoJi != 'fangDuoJi'){");
            html.append("$('#fangDuoJi").append(divId).append("').val('fangDuoJi');");
        }
        html.append(callBack);
        if (needFungDuoJi) {
            html.append("}");
        }
        html.append("}</script>");

        try {
            pageContext.getOut().print(html.toString());
        } catch (Exception ex) {
            log.error("", ex);
            throw new JspTagException("LangSelectTag: " + ex.getMessage());
        }
        release();

        return SKIP_BODY;
    }

    @Override
    public int doEndTag() {
        return EVAL_PAGE;
    }

    public void setMsg(String msg) throws JspException {
        this.msg = StringUtil.nullToEmpty(ExpressionEvaluatorManager.evaluate("msg",
                msg.toString(), Object.class, this, pageContext));
    }

    public void setCallBack(String callBack) throws JspException {
        this.callBack = StringUtil.nullToEmpty(ExpressionEvaluatorManager.evaluate("callBack",
                callBack.toString(), Object.class, this, pageContext));;
    }

    public void setPopupOrder(String popupOrder) {
        this.popupOrder = popupOrder;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNeedCancel(boolean needCancel) {
        this.needCancel = needCancel;
    }

    public void setNeedFungDuoJi(boolean needFungDuoJi) {
        this.needFungDuoJi = needFungDuoJi;
    }
}
