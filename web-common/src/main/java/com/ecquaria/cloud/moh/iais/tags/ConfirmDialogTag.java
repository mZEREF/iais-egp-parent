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
        String divId = "popupMessageDiv" + popupOrder;
        html.append("<div id=\"").append(divId).append("\" style=\"display: none;\">");
        if (needFungDuoJi) {
            html.append("<input type=\"hidden\" name=\"fangDuoJi").append(divId).append("\" id=\"fangDuoJi").append(divId).append("\"/>");
        }
        html.append("<p>").append(MessageUtil.getMessageDesc(msg)).append("</p>");
        html.append("</div>");
        //css
        html.append("<style>");
        html.append("#").append(divId).append(" p {padding-left:30px;}");
        html.append("#").append(divId).append("{padding-top: 25px;}");
        html.append("</style>");
        //javascript
        html.append("<script type=\"text/javascript\">");
        html.append("$(document).ready(function(){");
        html.append("$('#").append(divId).append("').dialog({");
        html.append("autoOpen:false,width:550,height:240,resizable: false, modal : true,");
        html.append("dialogClass: 'no-close success-dialog',title:'");
        html.append(StringUtil.isEmpty(title) ? "Confirmation" : title);
        html.append("',");
        html.append("open: function(event, ui) {jQuery('.ui-dialog-titlebar-close').hide();");
        html.append("jQuery('.ui-dialog-titlebar').show();");
        html.append("$('.ui-dialog').css('z-index',2103).css('position','fixed').css('top','50px');");
        html.append("$(\".ui-dialog-titlebar\").css({\"height\":\"auto\",\"padding-top\":\"20px\",\"padding-bottom\":\"5px\"});");
        html.append("$('.ui-widget-overlay').css('z-index',2102);},");
        html.append("buttons: {\"OK\":{text:'OK',class:'btn btn-primary',click:function() {");
        if (needFungDuoJi) {
            html.append("var fangDuoJi = $('#fangDuoJi").append(divId).append("').val();");
            html.append("if(fangDuoJi != 'fangDuoJi'){");
            html.append("$('#fangDuoJi").append(divId).append("').val('fangDuoJi');");
        }
        html.append(callBack);
        if (needFungDuoJi) {
            html.append("}");
        }
        html.append(";}}");
        if (needCancel) {
            html.append(",\"Cancel\":{text:'Cancel',class:'btn',click:function() {closePop");
            html.append(popupOrder).append("();}}");
        }
        html.append("}");
        html.append("}).parent().appendTo($('form:first'));").append("});");
        html.append("function ").append(popupOrder).append("(){");
        html.append("$('#").append(divId).append("').dialog(\"open\");");
        html.append("$('#").append(divId).append("').scrollTop(0);").append("}");
        html.append("function closePop").append(popupOrder).append("(){");
        if (needFungDuoJi) {
            html.append("var fangDuoJi = $('#fangDuoJi").append(divId).append("').val();");
            html.append("if(fangDuoJi != 'fangDuoJi'){");
        }
        html.append("$('#").append(divId).append("').dialog(\"close\");}");
        if (needFungDuoJi) {
            html.append("}");
        }
        html.append("</script>");
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
