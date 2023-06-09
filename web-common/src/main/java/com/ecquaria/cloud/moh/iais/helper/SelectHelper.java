package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * SelectHelper
 *
 * @author suocheng
 * @date 4/28/2022
 */
@Slf4j
public class SelectHelper {

    private static final String END_OPT = "</option>";

    public static String genMutilSelectOpHtml(Map<String, String> attrMap, List<SelectOption> selectOptionList, String firestOption,
                                              List<String> checkedVals, boolean multiSelect, boolean isTransfer) {
        StringBuilder sBuffer = new StringBuilder(100);
        sBuffer.append("<div ");
        if(!isTransfer){
            sBuffer.append("class=\"col-md-12 col-xs-12 multi-select\"");
        }
        sBuffer.append("><select ");
        if (multiSelect) {
            sBuffer.append("multiple=\"multiple\" ");
        }
        String name = "";
        for (Map.Entry<String, String> entry : attrMap.entrySet()) {
            sBuffer.append(entry.getKey())
                    .append("=\"")
                    .append(entry.getValue())
                    .append('\"');
            if (StringUtil.isEmpty(name) && "name".equals(entry.getKey())) {
                name = entry.getValue();
            }
        }
        sBuffer.append(" >");
        if (!StringUtil.isEmpty(firestOption)) {
            sBuffer.append("<option value=\"\">")
                    .append(firestOption)
                    .append(END_OPT);
        }
        for (SelectOption sp : selectOptionList) {
            if (!IaisCommonUtils.isEmpty(checkedVals)) {
                if (checkedVals.contains(sp.getValue())) {
                    sBuffer.append("<option selected=\"selected\" value=\"")
                            .append(sp.getValue())
                            .append("\">")
                            .append(sp.getText())
                            .append(END_OPT);
                } else {
                    sBuffer.append("<option value=\"").append(sp.getValue()).append("\">").append(sp.getText()).append(END_OPT);
                }
            } else {
                sBuffer.append("<option value=\"").append(sp.getValue()).append("\">").append(sp.getText()).append(END_OPT);
            }
        }
        sBuffer.append("</select>").append("</div>");
        // error span
        sBuffer.append("<div class=\"col-md-12 col-xs-12 row\">")
                .append("<span class=\"error-msg \" name=\"iaisErrorMsg\" id=\"error_").append(name).append("\"></span>")
                .append("</div>");
        return sBuffer.toString();
    }
}
