package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.client.IaisSystemClient;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;

/**
 * @author: yichen
 * @date time:2/8/2020 11:56 AM
 * @description:
 */

@Slf4j
public final class SystemParamCacheHelper {
	private SystemParamCacheHelper(){throw new IllegalStateException("Util class");}

	public static final String AUDIT_TRAIL_TIME_LIMIT = "E418B2D1-AD35-EA11-BE7D-000C29F371DC";

	// the key is hcsa service key(pk id)
	private static final HashMap<String, SystemParameterDto> cache = new HashMap<>();

	public static HashMap<String, SystemParameterDto> getSystemParamMapping() {
		return cache;
	}

	public static void receiveAllSystemParam(){
		flush();
	}

	public static void flush(){
		IaisSystemClient iaisSystemClient = SpringContextHelper.getContext().getBean(IaisSystemClient.class);
		List<SystemParameterDto> list =  iaisSystemClient.receiveAllSystemParam().getEntity();
		if (IaisCommonUtils.isEmpty(list)){
			return ;
		}

		list.stream().forEach(i -> cache.put(i.getId(), i));
	}

	public static String getParamValueById(String id){
		if (cache.containsKey(id)){
			return cache.get(id).getValue();
		}else {
			return null;
		}
	}

}
