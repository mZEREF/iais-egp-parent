package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.message.MessageCodeKey;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemParameterConstants;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: yichen
 * @date time:2/20/2020 2:40 PM
 * @description:
 */
public class SystemParameterValidator implements CustomizeValidator {


	@Override
	public Map<String, String> validate(HttpServletRequest httpServletRequest) {
		Map<String, String> errMap = new HashMap<>();
		SystemParameterDto editDto = (SystemParameterDto) ParamUtil.getSessionAttr(httpServletRequest, SystemParameterConstants.PARAMETER_REQUEST_DTO);
		if (editDto == null){
			return errMap;
		}

		String paramType = editDto.getParamType();
		int value = Integer.parseInt(editDto.getValue());
		switch (paramType){
			case SystemParameterConstants.PARAM_TYPE_REMINDER:

			break;
			case SystemParameterConstants.PARAM_TYPE_PAGE_SIZE:
				errMap = verifyPageSize(errMap, value);
			break;
			case SystemParameterConstants.PARAM_TYPE_MAX_FILE_SIZE:
				errMap = verifyFileUploadSize(errMap, value);
			break;
			case SystemParameterConstants.PARAM_TYPE_AUDIT_TRAIL_SEARCH_WEEK:
				errMap = verifyAuditTrailWeek(errMap, value);
			break;

			case SystemParameterConstants.PARAM_TYPE_MONTH:
			break;

			/*case SystemParameterConstants.PARAM_TYPE_YES:
				pass = verifyYes(value);
			break;

			case SystemParameterConstants.PARAM_TYPE_NO:
				pass = verifyNo(value);
			break;*/

			default:
		}
		return errMap;
	}

	private Map<String, String> verifyPageSize(Map<String, String> errorMap, int value){
		boolean hasError = value < 10 || (value > 50) ? true : false;
		if (hasError){
			errorMap.put(MessageCodeKey.CUSTOM_ERROR_MESSAGE_KEY, "The page size must be between 10 and 50");
		}
		return errorMap;
	}

	private Map<String, String> verifyAuditTrailWeek(Map<String, String> errorMap, int value){
		boolean hasError = value < 1 || (value > 52) ? true : false;
		if (hasError){
			errorMap.put(MessageCodeKey.CUSTOM_ERROR_MESSAGE_KEY, "The Audit trail week must be between 1 and 52");
		}
		return errorMap;
	}

	private Map<String, String> verifyFileUploadSize(Map<String, String> errorMap, int value){
		boolean hasError = value < 1 || value > 50 ? true : false;
		if (hasError){
			errorMap.put(MessageCodeKey.CUSTOM_ERROR_MESSAGE_KEY, "The file upload size must be between 1 and 50");
		}
		return errorMap;
	}

	/*private HashMap<String, String> verifyYes(HashMap<String, String> errorMap, int value){
		boolean hasError = value == 1;

		if (hasError){
			errorMap.put(MessageCodeKey.CUSTOM_ERROR_MESSAGE_KEY, "");
		}

		return errorMap;
	}

	private boolean verifyNo(int value){
		return value == 0;
	}*/
}
