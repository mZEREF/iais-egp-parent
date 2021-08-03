package com.ecquaria.cloud.moh.iais.tags;

import javax.servlet.jsp.tagext.BodyTagSupport;

public class DivTagSupport extends BodyTagSupport {
    private static final long serialVersionUID = -8473191841333084485L;

    protected String cssClass;
    protected String style;

    public DivTagSupport() {
        super();
        cleanFields();
    }

    protected void init() {
        cleanFields();
    }

    private void cleanFields() {
        setId(null);
        setCssClass(null);
        setStyle(null);
    }

    @Override
    public void release() {
        super.release();
        init();
    }
    @Override
    public void setId(String id) {
        this.id = id;
    }
    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }
    public void setStyle(String style) {
        this.style = style;
    }
}
