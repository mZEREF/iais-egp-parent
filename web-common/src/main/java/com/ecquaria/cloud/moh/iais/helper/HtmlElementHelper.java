package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * HtmlElementHelper
 *
 * @author Jinhua
 * @date 2019/12/5 13:45
 */
public class HtmlElementHelper {
    public static String generateSelect(Map<String, String> attributes, List<SelectOption> selectOptions, String firstOption, String value, boolean handleOthers) {
        return generateSelect(attributes, selectOptions, firstOption, value, -1, handleOthers);
    }

    public static String generateSelect(Map<String, String> attributes, List<SelectOption> selectOptions, String firstOption, String value, int size) {
        return generateSelect(attributes, selectOptions, firstOption, value, -1, false);
    }

    public static String generateSelect(Map<String, String> attributes, List<SelectOption> selectOptions, String firstOption, String value, int size, boolean handleOthers) {
        Map<String, String> options = new LinkedHashMap<String, String>();
        if (selectOptions != null && selectOptions.size() > 0) {
            for (SelectOption selectOption : selectOptions) {
                if (selectOption != null) {
                    options.put(selectOption.getValue(), selectOption.getText());
                }
            }
        }
        return generateSelect(attributes, options, firstOption, value, size, handleOthers);
    }

    public static String generateSelect(Map<String, String> attributes, String codeCategoryId, String firstOption, String value, int size) throws Exception {
        return generateSelect(attributes, codeCategoryId, firstOption, value, size, false);
    }

    public static String generateSelect(Map<String, String> attributes, String codeCategoryId, String firstOption, String value, int size, boolean handleOthers) throws Exception {
        List<SelectOption> sos = MasterCodeUtil.retrieveOptionsByCate(codeCategoryId);
        return generateSelect(attributes, sos, firstOption, value, size, handleOthers);
    }

    public static String generateSelect(Map<String, String> attributes, Map<String, String> options, String firstOption, String value, int size) {
        return generateSelect(attributes, options, firstOption, value, size, false);
    }

    public static String generateSelect(Map<String, String> attributes, Map<String, String> options, String firstOption, String value, boolean handleOthers) {
        return generateSelect(attributes, options, firstOption, value, -1, handleOthers);
    }


    /**
     * generate select element
     *
     * @param attributes
     * @param options   - LinkedHashMap
     * @param value
     * @return
     */
    public static String generateSelect(Map<String, String> attributes, Map<String, String> options, String firstOption, String value, int size, boolean handleOthers) {
        StringBuffer html = new StringBuffer();
        html.append("<select");
        if (attributes != null) {
            for (Map.Entry<String, String> entry : attributes.entrySet()) {
                prepareAttribute(html, entry.getKey(), entry.getValue());
            }
        } else {
            return "";
        }
        html.append(">");
        if (!StringUtil.isEmpty(firstOption)) {
            html.append("<option value=\"\">").append(StringUtil.viewHtml(firstOption)).append("</option>");
        }
        boolean localHaveOthers = false;
        boolean otherSelected = true;
        if (options != null && handleOthers) {
            if (StringUtil.isEmpty(value)) {
                otherSelected = false;
            }
            for (Map.Entry<String, String> entry : options.entrySet()) {
                String val = entry.getKey();
                if (val.equals(value)) {
                    otherSelected = false;
                    break;
                }
            }
        }
        if (options != null) {
            for (Map.Entry<String, String> entry : options.entrySet()) {
                String val = entry.getKey();
                String txt = entry.getValue();
                String selected = val.equals(value) ? " selected" : "";
                if (handleOthers) {
                    if ("Others".equals(txt) || "Other".equals(txt)) {
                        localHaveOthers = true;
                        if (otherSelected) {
                            selected = "selected";
                        }
                    }
                }
                if (size < 0) {
                    html.append("<option value=\"" + StringUtil.viewNonNullHtml(val) + "\""
                            + selected + ">" + StringUtil.viewHtml(txt) + "</option>");
                } else {
                    html.append("<option value=\"" + StringUtil.viewNonNullHtml(val) + "\" title=\""
                            + StringUtil.viewHtml(txt) + "\"" + selected + ">" + StringUtil.viewHtml(StringUtil.getShortDots(txt, size))
                            + "</option>");
                }
            }
        }
        html.append("</select>");
        if (localHaveOthers && handleOthers) {
            html.append("<div id=\"otherLabel\" style=\"display: none;\">Please specify:</div><input id=\"others").append(attributes.get("name")).append("\"");
            html.append(" style=\"height:25px;\" name=\"others").append(attributes.get("name")).append("\" maxLength=255\"");
            if (otherSelected) {
                html.append(" value=\"").append(StringUtil.viewNonNullHtml(value)).append("\"");
            }
            html.append(" style=\"display: none;\"/>");
        }
        if ("required".equals(attributes.get("class"))) {
            html.append("<span style=\"color:#c00;\">*</span>");
        }
        if (!AppConsts.FALSE.equals(attributes.get("needErrorSpan"))) {
            html.append("<span id=\"error_").append(attributes.get("name")).append("\"");
            html.append(" name=\"iaisErrorMsg\" class=\"error-msg\"></span>");
        }
        if (AppConsts.TRUE.equals(attributes.get("needMask"))) {
            String name = attributes.get("name");
            MaskUtil.maskValue(name, null);
            if (options != null) {
                for (String key : options.keySet()) {
                    MaskUtil.maskValue(name, key);
                }
            }
        }

        return html.toString();
    }

    private static void prepareAttribute(StringBuffer html, String attr, String value) {
        if (!StringUtil.isEmpty(value)) {
            html.append(" ");
            html.append(attr);
            html.append("=\"");
            html.append(value);
            html.append("\"");
        }
    }
}
