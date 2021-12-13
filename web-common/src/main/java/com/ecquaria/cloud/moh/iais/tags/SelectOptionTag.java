package com.ecquaria.cloud.moh.iais.tags;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import java.util.List;

/**
 * MasterCodeTag
 *
 * @author wangyu
 * @date 2021/12/13
 */
@Slf4j
public class SelectOptionTag extends DivTagSupport {
    private static final long serialVersionUID = 1L;

    private String value;
    private String selectionOptions;

    public SelectOptionTag() {
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
        setValue("");
        setSelectionOptions("");
        setCssClass("");
        setStyle("");
    }

    // Releases any resources we may have (or inherit)
    @Override
    public void release() {
        super.release();
        init();
    }

    @Override
    public int doStartTag() throws JspException {
        List<SelectOption> sos = null;
        if (StringUtil.isNotEmpty(selectionOptions)) {
            sos = (List<SelectOption>) ParamUtil.getScopeAttr((HttpServletRequest) pageContext.getRequest(), selectionOptions);
        }
        String html = "-";
        if(IaisCommonUtils.isEmpty(sos)){
            html= value;
        }else {
            for (SelectOption selectOption : sos) {
                if (value.equals(selectOption.getValue())) {
                    html= selectOption.getText();
                    break;
                }
            }
        }
        try {
            pageContext.getOut().print(StringUtil.escapeSecurityScript(html));
        } catch (Exception ex) {
            throw new JspTagException("SelectOptionTag: " + ex.getMessage(),ex);
        }
        return SKIP_BODY;
    }


    public void setValue(String value) {
        this.value = value;
    }

    public void setSelectionOptions(String selectionOptions) {
        this.selectionOptions = selectionOptions;
    }
}
