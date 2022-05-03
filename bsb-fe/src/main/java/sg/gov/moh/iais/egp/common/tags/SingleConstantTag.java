package sg.gov.moh.iais.egp.common.tags;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.lang.reflect.Field;

public class SingleConstantTag extends SimpleTagSupport {
    private String constantName;
    private String classFullName;
    private String attributeKey;

    @Override
    public void doTag() throws JspException, IOException {
        Object value;
        try {
            Class<?> clazz = Class.forName(classFullName);
            Field field = clazz.getField(constantName);
            value = field.get(clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new JspTagException("Class not found: " + this.classFullName);
        } catch (IllegalAccessException e) {
            throw new JspTagException("Illegal access: " + this.classFullName);
        } catch (NoSuchFieldException e) {
            throw new JspTagException("No such field: " + this.constantName);
        }

        JspContext context = getJspContext();
        context.setAttribute(this.attributeKey, value);
    }

    public String getConstantName() {
        return constantName;
    }

    public void setConstantName(String constantName) {
        this.constantName = constantName;
    }

    public String getClassFullName() {
        return classFullName;
    }

    public void setClassFullName(String classFullName) {
        this.classFullName = classFullName;
    }

    public String getAttributeKey() {
        return attributeKey;
    }

    public void setAttributeKey(String attributeKey) {
        this.attributeKey = attributeKey;
    }
}
