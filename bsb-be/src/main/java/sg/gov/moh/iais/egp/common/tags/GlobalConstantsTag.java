package sg.gov.moh.iais.egp.common.tags;

import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

public class GlobalConstantsTag extends SimpleTagSupport {
    private String classFullName;
    private String attributeKey;

    @Override
    public void doTag() throws JspException, IOException {
        Map<String, Object> constantMap;

        try {
            Class<?> clazz = Class.forName(classFullName);
            Field[] fields = clazz.getFields();
            constantMap = Maps.newLinkedHashMapWithExpectedSize(fields.length);
            for (Field field : fields) {
                Object value = field.get(clazz);
                if (value != null) {
                    constantMap.put(field.getName(), value);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new JspTagException("Class not found: " + this.classFullName);
        } catch (IllegalAccessException e) {
            throw new JspTagException("Illegal access: " + this.classFullName);
        }

        if (!CollectionUtils.isEmpty(constantMap)) {
            JspContext context = getJspContext();
            context.setAttribute(this.attributeKey, constantMap);
        }
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
