package sg.gov.moh.iais.egp.common.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

// todo: to be implemented in future
public class SortableHeaderTag extends SimpleTagSupport {
    private String field;
    private String value;
    private boolean needSort;
    private boolean isActiveUp;
    private boolean isActiveDown;
    private boolean isFE;
    private String jsFunc;
    private String customSpacing;

    @Override
    public void doTag() throws JspException, IOException {
        super.doTag();
    }


    public void setField(String field) {
        this.field = field;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setNeedSort(boolean needSort) {
        this.needSort = needSort;
    }

    public void setActiveUp(boolean activeUp) {
        isActiveUp = activeUp;
    }

    public void setActiveDown(boolean activeDown) {
        isActiveDown = activeDown;
    }

    public void setFE(boolean FE) {
        isFE = FE;
    }

    public void setJsFunc(String jsFunc) {
        this.jsFunc = jsFunc;
    }

    public void setCustomSpacing(String customSpacing) {
        this.customSpacing = customSpacing;
    }
}
