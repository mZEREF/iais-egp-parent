package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.client.HcsaServiceClient;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.List;

/**
 * @author: yichen
 * @date time:1/20/2020 1:01 PM
 * @description:
 */

@Slf4j
public final class HcsaServiceCacheHelper {
	private static final String CACHE_NAME_HCSA_SERVICE                = "iaisHcsaServiceCache";
	private static final String KEY_NAME_HCSA_SERVICE_LIST                = "serviceList";

	private HcsaServiceCacheHelper(){throw new IllegalStateException("Util class");}

	public static String getServiceNameById(String id){
		RedisCacheHelper redisCacheHelper = RedisCacheHelper.getInstance();
		HcsaServiceDto hcsaServiceDto = redisCacheHelper.get(CACHE_NAME_HCSA_SERVICE, id);

		if (hcsaServiceDto == null){
			return null;
		}else {
			return hcsaServiceDto.getSvcName();
		}
	}

	public static String getServiceTypeById(String id){
		RedisCacheHelper redisCacheHelper = RedisCacheHelper.getInstance();
		HcsaServiceDto hcsaServiceDto = redisCacheHelper.get(CACHE_NAME_HCSA_SERVICE, id);

		if (hcsaServiceDto == null){
			return null;
		}else {
			return hcsaServiceDto.getSvcType();
		}
	}

	public static HcsaServiceDto getServiceById(String id){
		RedisCacheHelper redisCacheHelper = RedisCacheHelper.getInstance();
		HcsaServiceDto hcsaServiceDto = redisCacheHelper.get(CACHE_NAME_HCSA_SERVICE, id);

		if (hcsaServiceDto == null){
			return null;
		}else {
			return hcsaServiceDto;
		}
	}


	public static void receiveServiceMapping(){
		HcsaServiceClient serviceClient = SpringContextHelper.getContext().getBean(HcsaServiceClient.class);
		if (serviceClient == null){
			log.info("================== receive service on startup failed ==================");
			log.info(HcsaServiceCacheHelper.class.getName() +  " service client is null");
		}

		int status = serviceClient.getActiveServices().getStatusCode();
		if (status == HttpStatus.SC_OK){
			List<HcsaServiceDto> serviceList = serviceClient.getActiveServices().getEntity();
			RedisCacheHelper redisCacheHelper = RedisCacheHelper.getInstance();
			redisCacheHelper.set(CACHE_NAME_HCSA_SERVICE, KEY_NAME_HCSA_SERVICE_LIST, serviceList);
			if (!IaisCommonUtils.isEmpty(serviceList)){
				serviceList.stream().forEach(i -> redisCacheHelper.set(CACHE_NAME_HCSA_SERVICE, i.getId(), i));
			}
		}
	}

	public static List<HcsaServiceDto> receiveAllHcsaService(){
		RedisCacheHelper redisCacheHelper = RedisCacheHelper.getInstance();
		return redisCacheHelper.get(CACHE_NAME_HCSA_SERVICE, KEY_NAME_HCSA_SERVICE_LIST);
	}

}
