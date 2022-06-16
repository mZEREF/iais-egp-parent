package sg.gov.moh.iais.egp.common.tags;

import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import org.springframework.util.CollectionUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Map;

public class CompareTdStyleDisplayTag extends SimpleTagSupport {
    private Map<String, String> diffMap;
    private String key;
    private String replaceWith;
    private boolean needUnMaskedCode;

    @Override
    public void doTag() throws JspException, IOException {
        String outVal;
        if (CollectionUtils.isEmpty(diffMap)) {
            // different map is null
            outVal = "no different";
        } else {
            // map isn't null
            if (!diffMap.containsKey(key)) {
                // map don't have key
                outVal = "no different";
            } else {
                // map have key
                String val = diffMap.get(key);
                if (val == null || val.equals("")) {
                    // value is null or "", replace with "replaceWith"
                    outVal = replaceWith;
                } else if (needUnMaskedCode) {
                    // value isn't null, and need unmasked code
                    outVal = MasterCodeUtil.getCodeDesc(val);
                } else {
                    // value isn't null
                    outVal = StringUtil.viewNonNullHtml(val);
                }
            }
        }
        getJspContext().getOut().print(outVal);
    }

    public Map<String, String> getDiffMap() {
        return diffMap;
    }

    public void setDiffMap(Map<String, String> diffMap) {
        this.diffMap = diffMap;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getReplaceWith() {
        return replaceWith;
    }

    public void setReplaceWith(String replaceWith) {
        this.replaceWith = replaceWith;
    }

    public boolean isNeedUnMaskedCode() {
        return needUnMaskedCode;
    }

    public void setNeedUnMaskedCode(boolean needUnMaskedCode) {
        this.needUnMaskedCode = needUnMaskedCode;
    }
}
