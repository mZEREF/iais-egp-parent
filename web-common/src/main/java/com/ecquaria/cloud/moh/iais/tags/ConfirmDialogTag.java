package com.ecquaria.cloud.moh.iais.tags;

import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * ConfirmDialogTag
 *
 * @author Jinhua
 * @date 2020/2/22 15:48
 */
@Slf4j
public class ConfirmDialogTag extends TagSupport {

    private static final long serialVersionUID = -7771126887123546978L;
    private String msg;
    private String callBack;
    private String popupOrder;
    private String title;
    private String yesBtnDesc;
    private String yesBtnCls;
    private boolean needCancel;
    private boolean needFungDuoJi;
    private String cancelBtnDesc;
    private String cancelBtnCls;
    private String cancelFunc;
    private boolean needEscapHtml;
    private boolean needHeader;

    public ConfirmDialogTag() throws JspException {
        super();
        init();
    }

    // resets local state
    private void init() throws JspException {
        setMsg("");
        setCallBack("");
        setPopupOrder("");
        setTitle("");
        setCancelFunc("");
        setYesBtnDesc("");
        setYesBtnCls("");
        setCancelBtnCls("");
        setCancelBtnDesc("");
        setNeedCancel(true);
        setNeedFungDuoJi(true);
        setNeedEscapHtml(true);
        setNeedHeader(false);
    }

    // Releases any resources we may have (or inherit)
    @Override
    public void release() {
        super.release();
        try {
            init();
        } catch (JspException e) {
            log.info(e.getMessage(),e);
        }
    }

    @Override
    public int doStartTag() {
        StringBuilder html = new StringBuilder();
        //html
        String divId = popupOrder;
        html.append("<div id=\"").append(divId).append('\"');
        html.append(" class=\"modal fade\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"").append(divId);
        html.append("\" aria-hidden=\"true\">");
        html.append("<div class=\"modal-dialog modal-dialog-centered\" role=\"document\"><div class=\"modal-content\">");
        if (needHeader) {
            html.append("<div class=\"modal-header\">");
            html.append("<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\"><span");
            html.append(" aria-hidden=\"true\">&times;</span></button>");
            if (StringUtil.isEmpty(title)) {
                setTitle("Confirmation Box");
            }
            html.append(" <div class=\"modal-title\" id=\"gridSystemModalLabel\" style=\"font-size: 2rem;\">").append(StringUtil.viewHtml(title)).append("</div></div>");
        }
        html.append("<div class=\"modal-body\"><div class=\"row\">");
        if (needFungDuoJi) {
            html.append("<input type=\"hidden\" name=\"fangDuoJi").append(divId).append("\" id=\"fangDuoJi").append(divId).append("\"/>");
        }
        html.append("<div class=\"col-md-12\"><span style=\"font-size: 2rem\">");
        if (needEscapHtml) {
            html.append(StringUtil.viewHtml(MessageUtil.getMessageDesc(msg)));
        } else {
            html.append(MessageUtil.getMessageDesc(msg));
        }
        html.append("</span></div></div></div>");
        html.append("<div class=\"modal-footer\">");
        if (needCancel) {
            if (StringUtil.isEmpty(cancelBtnDesc)) {
                setCancelBtnDesc("Cancel");
            }
            html.append("<button type=\"button\" class=\"");
            if (StringUtil.isEmpty(cancelBtnCls)) {
                setCancelBtnCls("btn btn-secondary");
            }
            html.append(cancelBtnCls).append("\" data-dismiss=\"modal\"");
            if (!StringUtil.isEmpty(cancelFunc)) {
                html.append(" onclick=\"javascript:").append(cancelFunc).append(";\"");
            }
            html.append('>').append(cancelBtnDesc).append("</button>");
        }
        html.append("<button type=\"button\" class=\"");
        if (StringUtil.isEmpty(yesBtnCls)) {
            setYesBtnCls("btn btn-primary");
        }
        html.append(yesBtnCls).append("\" onclick=\"javascript:");
        if (StringUtil.isEmpty(yesBtnDesc)) {
            setYesBtnDesc("OK");
        }
        html.append("tagConfirmCallback").append(popupOrder).append("();\">").append(yesBtnDesc).append("</button>");

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
            html.append('}');
        }
        html.append("}</script>");

        try {
            pageContext.getOut().print(html.toString());
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new IaisRuntimeException("LangSelectTag: " + ex.getMessage(),ex);
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
                msg, Object.class, this, pageContext));
    }

    public void setCallBack(String callBack) throws JspException {
        this.callBack = StringUtil.nullToEmpty(ExpressionEvaluatorManager.evaluate("callBack",
                callBack, Object.class, this, pageContext));;
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

    public void setCancelFunc(String cancelFunc) {
        this.cancelFunc = cancelFunc;
    }

    public void setYesBtnDesc(String yesBtnDesc) {
        this.yesBtnDesc = yesBtnDesc;
    }

    public void setCancelBtnDesc(String cancelBtnDesc) {
        this.cancelBtnDesc = cancelBtnDesc;
    }

    public void setYesBtnCls(String yesBtnCls) {
        this.yesBtnCls = yesBtnCls;
    }

    public void setCancelBtnCls(String cancelBtnCls) {
        this.cancelBtnCls = cancelBtnCls;
    }

    public void setNeedEscapHtml(boolean needEscapHtml) {
        this.needEscapHtml = needEscapHtml;
    }

    public void setNeedHeader(boolean needHeader) {
        this.needHeader = needHeader;
    }
}
