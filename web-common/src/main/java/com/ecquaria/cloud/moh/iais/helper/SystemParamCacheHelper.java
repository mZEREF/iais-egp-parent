package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.client.IaisSystemClient;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * @author: yichen
 * @date time:2/8/2020 11:56 AM
 * @description:
 */

@Slf4j
public final class SystemParamCacheHelper {
	private static final String CACHE_NAME_SYSTEM_PARAM                    = "iaisSystemParamCache";

	private SystemParamCacheHelper(){throw new IllegalStateException("Util class");}

	public static final String AUDIT_TRAIL_TIME_LIMIT = "E418B2D1-AD35-EA11-BE7D-000C29F371DC";

	public static HashMap<String, SystemParameterDto> getSystemParamMapping() {
		return RedisCacheHelper.getInstance().get(CACHE_NAME_SYSTEM_PARAM);
	}

	public static void receiveAllSystemParam(){
		flush();
	}

	public static void flush(){
		IaisSystemClient iaisSystemClient = SpringContextHelper.getContext().getBean(IaisSystemClient.class);

		int status = iaisSystemClient.receiveAllSystemParam().getStatusCode();
		if (status != HttpStatus.SC_OK){
			return;
		}

		List<SystemParameterDto> list =  iaisSystemClient.receiveAllSystemParam().getEntity();
		if (IaisCommonUtils.isEmpty(list)){
			return ;
		}

		RedisCacheHelper redisCacheHelper = RedisCacheHelper.getInstance();
		list.stream().forEach(i -> redisCacheHelper.set(CACHE_NAME_SYSTEM_PARAM, i.getId(),
				i, RedisCacheHelper.NOT_EXPIRE));
	}

	public static String getParamValueById(String id){
		SystemParameterDto systemParameterDto = RedisCacheHelper.getInstance().get(CACHE_NAME_SYSTEM_PARAM, id);
		Optional<SystemParameterDto> optional = Optional.ofNullable(systemParameterDto);
		return optional.isPresent() ? optional.get().getValue() : null;
	}

}
