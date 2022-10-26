package sg.gov.moh.iais.egp.common.tags;


import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author YiMing
 * @version 2022/2/21 15:34
 **/
public class LocalDateFormatTag extends SimpleTagSupport {
    private LocalDate localDate;
    private String pattern;

    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void doTag() throws JspException, IOException {
        if (localDate == null) {
            getJspContext().getOut().print("");
        } else {
            DateTimeFormatter formatter;
            if (pattern == null || "".equals(pattern)) {
                formatter = DEFAULT_FORMATTER;
            } else {
                formatter = DateTimeFormatter.ofPattern(pattern);
            }
            String s = localDate.format(formatter);
            getJspContext().getOut().print(s);
        }

    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
