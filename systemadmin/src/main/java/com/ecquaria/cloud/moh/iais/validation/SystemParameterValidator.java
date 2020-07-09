package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.message.MessageCodeKey;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemParameterConstants;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * @author: yichen
 * @date time:2/20/2020 2:40 PM
 * @description:
 */
public class SystemParameterValidator implements CustomizeValidator {


	@Override
	public Map<String, String> validate(HttpServletRequest httpServletRequest) {
		Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
		SystemParameterDto editDto = (SystemParameterDto) ParamUtil.getSessionAttr(httpServletRequest, SystemParameterConstants.PARAMETER_REQUEST_DTO);
		if (editDto == null){
			return errMap;
		}

		String paramType = editDto.getParamType();

		switch (paramType){
			case SystemParameterConstants.PARAM_TYPE_REMINDER:
			case SystemParameterConstants.PARAM_TYPE_MONTH:
				break;
			case SystemParameterConstants.PARAM_TYPE_PAGE_SIZE:
				verifyPageSize(errMap, editDto.getValue());
			break;
			case SystemParameterConstants.PARAM_TYPE_MAX_FILE_SIZE:
				verifyFileUploadSize(errMap, Integer.parseInt(editDto.getValue()));
			break;
			case SystemParameterConstants.PARAM_TYPE_AUDIT_TRAIL_SEARCH_WEEK:
				verifyAuditTrailWeek(errMap, Integer.parseInt(editDto.getValue()));
			break;
			case SystemParameterConstants.PARAM_TYPE_FILE_TYPE_FOR_UPLOADING:
				verifyUploadFileType(errMap, editDto.getValue());
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

	private void verifyPageSize(Map<String, String> errorMap, String value){
		if (StringUtils.isEmpty(value)){
			return;
		}

		Matcher m = IaisEGPHelper.matcherByRegex(value, "\\{([^}]*)\\}");
		if (m.matches()){
			String[] values = IaisEGPHelper.getPageSizeByStrings(value);
			try {
				if (values.length != 5){
					errorMap.put(MessageCodeKey.CUSTOM_ERROR_MESSAGE_KEY, MessageUtil.replaceMessage("GENERAL_ERR0021", "5", "len"));
					return;
				}

				int[] to = new int[values.length];
				for (int i = 0; i < values.length; i++){
					int val = Integer.parseInt(values[i]);
					if (val < 10 || val > 100){
						errorMap.put(MessageCodeKey.CUSTOM_ERROR_MESSAGE_KEY, "GENERAL_ERR0022");
						break;
					}

					to[i] = val;
				}

				if (!IaisEGPHelper.isAsc(to)){
					errorMap.put(MessageCodeKey.CUSTOM_ERROR_MESSAGE_KEY, "UC_CHKLMD001_ERR003");
				}

			}catch (NumberFormatException e){
				errorMap.put(MessageCodeKey.CUSTOM_ERROR_MESSAGE_KEY, "UC_CHKLMD001_ERR003");
				return;
			}
		}else {
			errorMap.put(MessageCodeKey.CUSTOM_ERROR_MESSAGE_KEY, "UC_CHKLMD001_ERR003");
		}

	}

	private static void verifyUploadFileType(Map<String, String> errorMap, String value){
		String[] arr = FileUtils.fileTypeToArray(value);
		if (arr == null){
			errorMap.put(MessageCodeKey.CUSTOM_ERROR_MESSAGE_KEY, "SYSPAM_ERROR0004");
		}
	}

	private void verifyAuditTrailWeek(Map<String, String> errorMap, int value){
		boolean hasError = value < 1 || (value > 52) ? true : false;
		if (hasError){
			errorMap.put(MessageCodeKey.CUSTOM_ERROR_MESSAGE_KEY, MessageUtil.getMessageDesc("SYSPAM_ERROR0002"));
		}
	}

	private void verifyFileUploadSize(Map<String, String> errorMap, int value){
		boolean hasError = value < 1 || value > 10 ? true : false;
		if (hasError){
			errorMap.put(MessageCodeKey.CUSTOM_ERROR_MESSAGE_KEY, MessageUtil.getMessageDesc("SYSPAM_ERROR0001"));
		}
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
