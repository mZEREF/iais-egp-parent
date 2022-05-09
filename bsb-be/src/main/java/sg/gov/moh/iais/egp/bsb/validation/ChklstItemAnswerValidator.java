package sg.gov.moh.iais.egp.bsb.validation;

import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.constant.ChecklistConstants;
import sg.gov.moh.iais.egp.bsb.dto.chklst.ChklstItemAnswerDto;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Auther chenlei on 4/26/2022.
 */
@Slf4j
public class ChklstItemAnswerValidator implements CustomizeValidator {

    @Override
    public Map<String, String> validate(Object obj, String profile, HttpServletRequest request) {
        if (!(obj instanceof ChklstItemAnswerDto)) {
            return CustomizeValidator.super.validate(obj, profile, request);
        }
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ChklstItemAnswerDto answerDto = (ChklstItemAnswerDto) obj;
        try {
            String dueDate = answerDto.getDueDate();
            if (!CommonValidator.isDate(dueDate) || !CommonValidator.isFileDate(dueDate)) {
                errorMap.put("dueDate", MessageUtil.getMessageDesc("GENERAL_ERR0033"));
            } else if (!StringUtil.isEmpty(dueDate) && Formatter.compareDateByDay(dueDate) <= 0) {
                errorMap.put("dueDate", MessageUtil.replaceMessage("GENERAL_ERR0026", "Due Date", "field"));
            }
        } catch (Exception e) {
            log.info(StringUtil.changeForLog(e.getMessage()), e);
        }
        String messageCommon = MessageUtil.getMessageDesc("GENERAL_ERR0006");
        if (ChecklistConstants.ANSWER_NO.equals(answerDto.getAnswer())) {
            if (StringUtil.isEmpty(answerDto.getFindings())) {
                errorMap.put("findings", messageCommon);
            }
            if (StringUtil.isEmpty(answerDto.getActionRequired())) {
                errorMap.put("actionRequired", messageCommon);
            }
            if (answerDto.getRectified() == null) {
                errorMap.put("rectified", messageCommon);
            }
        } else {
            Boolean rectified = answerDto.getRectified();
            if (rectified != null && rectified) {
                errorMap.put("rectified", "Invalid value.");
            }
        }
        if (ChecklistConstants.ANSWER_YES.equals(answerDto.getFollowupItem())) {
            if (StringUtil.isEmpty(answerDto.getObserveFollowup())) {
                errorMap.put("observeFollowup", messageCommon);
            }
            if (StringUtil.isEmpty(answerDto.getFollowupAction())) {
                errorMap.put("followupAction", messageCommon);
            }
            if (StringUtil.isEmpty(answerDto.getDueDate())) {
                errorMap.put("dueDate", messageCommon);
            }
        } else if (!StringUtil.isEmpty(answerDto.getFollowupItem())) {
            if (!StringUtil.isEmpty(answerDto.getObserveFollowup())) {
                errorMap.put("observeFollowup", "The field must be empty when the Follow-Up Item is Yes or N/A.");
            }
            if (!StringUtil.isEmpty(answerDto.getFollowupAction())) {
                errorMap.put("followupAction", "The field must be empty when the Follow-Up Item is Yes or N/A.");
            }
        }
        return errorMap;
    }

}
