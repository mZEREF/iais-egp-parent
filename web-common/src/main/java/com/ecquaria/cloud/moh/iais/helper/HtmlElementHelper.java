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
    public static String generateSelect(Map<String, String> attributes, String codeCategoryId, String firstOption, String value, int size) throws Exception {
        return generateSelect(attributes, codeCategoryId, firstOption, value, size, false);
    }

    public static String generateSelect(Map<String, String> attributes, String codeCategoryId, String firstOption, String value, int size, boolean handleOthers) throws Exception {
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
            for (SelectOption entry : options) {
                String val = entry.getValue();
                if (val.equals(value)) {
                    otherSelected = false;
                    break;
                }
            }
        }
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
        // The Nice select css
        String clsName = StringUtil.isEmpty(attributes.get("id")) ? attributes.get("name") : attributes.get("id");
        String className = clsName + "Select";
        html.append("<div class=\"nice-select "+className+"\" tabindex=\"0\">");
        if (!StringUtil.isEmpty(firstOption)) {
            html.append("<span class=\"current\">"+firstOption+"</span>");
        } else {
            html.append("<span class=\"current\">"+ options.get(0).getText() +"</span>");
        }
        html.append("<ul class=\"list mCustomScrollbar _mCS_2 mCS_no_scrollbar\">")
                .append("<div id=\"mCSB_2\" class=\"mCustomScrollBox mCS-light mCSB_vertical mCSB_inside\" tabindex=\"0\" style=\"max-height: none;\">")
                .append("<div id=\"mCSB_2_container\" class=\"mCSB_container mCS_y_hidden mCS_no_scrollbar_y\" style=\"position:relative; top:0; left:0;\" dir=\"ltr\">");
        if (!StringUtil.isEmpty(firstOption)) {
            html.append("<li data-value=\"\" class=\"option selected\">"+firstOption+"</li>");
            for (SelectOption kv: options) {
                html.append(" <li data-value=\""+kv.getValue()+"\" class=\"option\">"+kv.getText()+"</li>");
            }
        } else {
            for(int i = 0;i < options.size();i++){
                SelectOption kv = options.get(i);
                if(i == 0){
                    html.append(" <li data-value=\""+kv.getValue()+"\" class=\"option selected\">"+kv.getText()+"</li>");
                }else{
                    html.append(" <li data-value=\""+kv.getValue()+"\" class=\"option\">"+kv.getText()+"</li>");
                }
            }
        }

        html.append("</div>")
                .append("<div id=\"mCSB_2_scrollbar_vertical\" class=\"mCSB_scrollTools mCSB_2_scrollbar mCS-light mCSB_scrollTools_vertical\" style=\"display: none;\">")
                .append("<div class=\"mCSB_draggerContainer\">")
                .append("<div id=\"mCSB_2_dragger_vertical\" class=\"mCSB_dragger\" style=\"position: absolute; min-height: 30px; top: 0px; height: 0px;\">")
                .append("<div class=\"mCSB_dragger_bar\" style=\"line-height: 30px;\">")
                .append("</div>")
                .append("</div>")
                .append("<div class=\"mCSB_draggerRail\"></div>")
                .append("</div>")
                .append("</div>")
                .append("</div>")
                .append("</ul>")
                .append("</div>");
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
                for (SelectOption opt : options) {
                    MaskUtil.maskValue(name, opt.getValue());
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
