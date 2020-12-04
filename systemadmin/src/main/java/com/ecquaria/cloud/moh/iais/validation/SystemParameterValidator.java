package com.ecquaria.cloud.moh.iais.validation;

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

/**
 * @author: yichen
 * @date time:2/20/2020 2:40 PM
 * @description:
 */
public class SystemParameterValidator implements CustomizeValidator {
	private final static String VALUE_TYPE_INT = "Int";
	private final static String VALUE_TYPE_STRING = "String";


	@Override
	public Map<String, String> validate(HttpServletRequest httpServletRequest) {
		Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
		SystemParameterDto editDto = (SystemParameterDto) ParamUtil.getSessionAttr(httpServletRequest, SystemParameterConstants.PARAMETER_REQUEST_DTO);
		if (editDto == null || StringUtils.isEmpty(editDto.getValue())){
			return errMap;
		}

		String value = editDto.getValue();
		String valueType = editDto.getValueType();
		int number = 0;
		if (VALUE_TYPE_INT.equalsIgnoreCase(valueType)){
			try {
				number = Integer.parseInt(value);
			}catch (NumberFormatException e){
				errMap.put("customErrorMessage", "SYSPAM_ERROR0008");
				return errMap;
			}
		}

		String propertiesKey = editDto.getPropertiesKey();
		switch (propertiesKey){
			case SystemParameterConstants.PARAM_KEY_PAGE_SIZE:
				verifyPageSize(errMap, value);
			break;
			case SystemParameterConstants.PARAM_KEY_UPLOAD_FILE_LIMIT:
				verifyFileUploadSize(errMap, number);
			break;
			case SystemParameterConstants.PARAM_KEY_AUDIT_TRAIL_SEARCH_WEEK:
				verifyAuditTrailWeek(errMap, number);
			break;
			case SystemParameterConstants.PARAM_KEY_UPLOAD_FILE_TYPE:
				verifyUploadFileType(errMap, value);
				break;
			case SystemParameterConstants.PARAM_KEY_INSPECTOR_TCU_REMINDER_WEEK:
				verifyWeekDay(errMap, number);
				break;
			default:
		}
		return errMap;
	}

	private void verifyWeekDay(Map<String, String> errorMap, int value){
		boolean hasError = value < 1 || value > 7 ? true : false;
		if (hasError){
			errorMap.put("customErrorMessage", MessageUtil.getMessageDesc("SYSPAM_ERROR0007"));
		}
	}

	private void verifyPageSize(Map<String, String> errorMap, String value){
		String[] values = IaisEGPHelper.getPageSizeByStrings(value);
		try {
			if (values.length != 5){
				errorMap.put("customErrorMessage", MessageUtil.replaceMessage("GENERAL_ERR0021", "5", "len"));
				return;
			}

			int[] to = new int[values.length];
			for (int i = 0; i < values.length; i++){
				int val = Integer.parseInt(values[i]);
				if (val < 10 || val > 100){
					errorMap.put("customErrorMessage", "SYSPAM_ERROR0003");
					return;
				}

				to[i] = val;
			}

			if (!IaisEGPHelper.isAsc(to)){
				errorMap.put("customErrorMessage", "SYSPAM_ERROR0008");
			}

		}catch (NumberFormatException e){
			errorMap.put("customErrorMessage", "SYSPAM_ERROR0008");
			return;
		}


	}

	private static void verifyUploadFileType(Map<String, String> errorMap, String value){
		String[] arr = FileUtils.fileTypeToArray(value);
		if (arr == null){
			errorMap.put("customErrorMessage", "SYSPAM_ERROR0004");
			return;
		}

		String[] include = getIncludeFileType();

		for (String i : arr){
			boolean in = false;
			for (String j : include){
				if (i.equalsIgnoreCase(j)){
					in = true;
				}
			}

			if (!in){
				errorMap.put("customErrorMessage", "SYSPAM_ERROR0004");
				break;
			}
		}
	}

	private static String[] getIncludeFileType(){
		return new String[]{"pdf", "xlsx", "xls", "docx", "doc", "png", "jpg", "jpeg", "csv"};
	}

	private void verifyAuditTrailWeek(Map<String, String> errorMap, int value){
		boolean hasError = value < 1 || (value > 52) ? true : false;
		if (hasError){
			errorMap.put("customErrorMessage", MessageUtil.getMessageDesc("SYSPAM_ERROR0002"));
		}
	}

	private void verifyFileUploadSize(Map<String, String> errorMap, int value){
		boolean hasError = value < 1 || value > 10 ? true : false;
		if (hasError){
			errorMap.put("customErrorMessage", MessageUtil.getMessageDesc("SYSPAM_ERROR0001"));
		}
	}
}
