package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;

import java.util.List;
import java.util.Map;

/**
 * HtmlElementHelper
 *
 * @author Jinhua
 * @date 2019/12/5 13:45
 */
public class HtmlElementHelper {

    private static final String STYLE = "style";
    private static final String END_OPT = "</option>";
    private static final String START_LI = " <li data-value=\"";
    private static final String END_LI = "</li>";

    public static String generateSelect(Map<String, String> attributes, String codeCategoryId, String firstOption, String value, int size)  {
        return generateSelect(attributes, codeCategoryId, firstOption, value, size, false);
    }

    public static String generateSelect(Map<String, String> attributes, String codeCategoryId, String firstOption, String value, int size, boolean handleOthers) {
        List<SelectOption> sos = MasterCodeUtil.retrieveOptionsByCate(codeCategoryId);
        return generateSelect(attributes, sos, firstOption, value, size, handleOthers);
    }

    public static String generateSelect(Map<String, String> attributes, List<SelectOption> options, String firstOption, String value, int size) {
        return generateSelect(attributes, options, firstOption, value, size, false);
    }

    public static String generateSelect(Map<String, String> attributes, List<SelectOption> options, String firstOption, String value, boolean handleOthers) {
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
    public static String generateSelect(Map<String, String> attributes, List<SelectOption> options, String firstOption, String value, int size, boolean handleOthers) {
        if (attributes == null) {
            return "";
        }
        StringBuffer html = new StringBuffer();
        html.append("<select");
        if (attributes.get(STYLE) == null) {
            attributes.put(STYLE, "display:none;");
        } else {
            String style = attributes.get(STYLE);
            if (!style.contains("display")) {
                attributes.put(STYLE, "display:none;" + style);
            }
        }
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            prepareAttribute(html, entry.getKey(), entry.getValue());
        }
        html.append('>');
        if (!StringUtil.isEmpty(firstOption)) {
            html.append("<option value=\"\">").append(StringUtil.viewHtml(firstOption)).append(END_OPT);
        }

        boolean otherSelected = true;
        if (options != null && handleOthers) {
            if (StringUtil.isEmpty(value)) {
                otherSelected = false;
            }
            for (SelectOption entry : options) {
                String val = entry.getValue();
                if (val.equals(value)) {
                    otherSelected = false;
                    break;
                }
            }
        }
        boolean localHaveOthers = addOptions(options, value, size, handleOthers, html, otherSelected);
        html.append("</select>");
        // The Nice select css
        String clsName = StringUtil.isEmpty(attributes.get("id")) ? attributes.get("name") : attributes.get("id");
        String className = clsName + "Select";
        html.append("<div class=\"nice-select ").append(className).append("\" tabindex=\"0\">");
        addCurrent(options, firstOption, html);
        html.append("<ul class=\"list\">");
        addOptionLis(options, firstOption, html);

        html.append("</ul>")
                .append("</div>");
        if (localHaveOthers && handleOthers) {
            html.append("<div id=\"otherLabel\" style=\"display: none;\">Please specify:</div><input id=\"others").append(attributes.get("name")).append('\"');
            html.append(" style=\"height:25px;\" name=\"others").append(attributes.get("name")).append("\" maxLength=255\"");
            if (otherSelected) {
                html.append(" value=\"").append(StringUtil.viewNonNullHtml(value)).append('\"');
            }
            html.append(" style=\"display: none;\"/>");
        }
        addAdditional(attributes, options, html);

        return html.toString();
    }

    private static void addCurrent(List<SelectOption> options, String firstOption, StringBuffer html) {
        String current = "";
        if (!StringUtil.isEmpty(firstOption)) {
            current = firstOption;
        } else if (options != null && !options.isEmpty()) {
            current = StringUtil.getNonNull(options.get(0).getText());
        }
        html.append("<span class=\"current\">").append(current).append("</span>");
    }

    private static void addAdditional(Map<String, String> attributes, List<SelectOption> options, StringBuffer html) {
        if ("required".equals(attributes.get("class"))) {
            html.append("<span style=\"color:#c00;\">*</span>");
        }
        if (!AppConsts.FALSE.equals(attributes.get("needErrorSpan"))) {
            html.append("<span id=\"error_").append(attributes.get("name")).append('\"');
            html.append(" name=\"iaisErrorMsg\" class=\"error-msg\"></span>");
        }
        if (AppConsts.TRUE.equals(attributes.get("needMask"))) {
            String name = attributes.get("name");
            MaskUtil.maskValue(name, null);
            if (options != null) {
                for (SelectOption opt : options) {
                    MaskUtil.maskValue(name, opt.getValue());
                }
            }
        }
    }

    private static void addOptionLis(List<SelectOption> options, String firstOption, StringBuffer html) {
        if (!StringUtil.isEmpty(firstOption)) {
            html.append("<li data-value=\"\" class=\"option selected\">").append(firstOption).append(END_LI);
            for (SelectOption kv: options) {
                html.append(START_LI).append(kv.getValue()).append("\" class=\"option\">").append(kv.getText()).append(END_LI);
            }
        } else if (options !=null){
            for(int i = 0; i < options.size(); i++){
                SelectOption kv = options.get(i);
                if(i == 0){
                    html.append(START_LI).append(kv.getValue()).append("\" class=\"option selected\">").append(kv.getText()).append(END_LI);
                }else{
                    html.append(START_LI).append(kv.getValue()).append("\" class=\"option\">").append(kv.getText()).append(END_LI);
                }
            }
        }
    }

    private static boolean addOptions(List<SelectOption> options, String value, int size, boolean handleOthers,
            StringBuffer html, boolean otherSelected) {
        boolean localHaveOthers = false;
        if (options != null) {
            for (SelectOption entry : options) {
                String val = entry.getValue();
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
                    html.append("<option value=\"").append(StringUtil.viewNonNullHtml(val)).append('\"').append(selected)
                            .append('>').append(StringUtil.viewHtml(txt)).append(END_OPT);
                } else {
                    html.append("<option value=\"").append(StringUtil.viewNonNullHtml(val)).append("\" title=\"")
                            .append(StringUtil.viewHtml(txt)).append('\"').append(selected).append('>')
                            .append(StringUtil.viewHtml(StringUtil.getShortDots(txt, size))).append(END_OPT);
                }
            }
        }
        return localHaveOthers;
    }

    private static void prepareAttribute(StringBuffer html, String attr, String value) {
        if (!StringUtil.isEmpty(value)) {
            html.append(' ');
            html.append(attr);
            html.append("=\"");
            html.append(value);
            html.append('\"');
        }
    }
}
