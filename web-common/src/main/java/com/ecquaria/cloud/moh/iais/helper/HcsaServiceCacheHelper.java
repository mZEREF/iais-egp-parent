package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.client.HcsaServiceClient;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

/**
 * @author: yichen
 * @date time:1/20/2020 1:01 PM
 * @description:
 */

@Slf4j
public final class HcsaServiceCacheHelper {
	private static final String CACHE_NAME_HCSA_SERVICE                = "iaisHcsaServiceCache";
	private static final String KEY_NAME_HCSA_SERVICE_LIST             = "activeServiceList";

	private HcsaServiceCacheHelper(){throw new IllegalStateException("Util class");}

	public static String getServiceNameById(String id){
		HcsaServiceDto hcsaServiceDto = getServiceById(id);

		if (hcsaServiceDto == null){
			return null;
		}else {
			return hcsaServiceDto.getSvcName();
		}
	}

	public static String getServiceTypeById(String id){
		HcsaServiceDto hcsaServiceDto = getServiceById(id);

		if (hcsaServiceDto == null){
			return null;
		}else {
			return hcsaServiceDto.getSvcType();
		}
	}

	public static HcsaServiceDto getServiceById(String id) {
		RedisCacheHelper redisCacheHelper = RedisCacheHelper.getInstance();
		HcsaServiceDto hcsaServiceDto = redisCacheHelper.get(CACHE_NAME_HCSA_SERVICE, id);

		if (hcsaServiceDto == null){
			HcsaServiceClient serviceClient = SpringContextHelper.getContext().getBean(HcsaServiceClient.class);
			hcsaServiceDto = serviceClient.getHcsaServiceDtoByServiceId(id).getEntity();
			if (hcsaServiceDto != null) {
				redisCacheHelper.set(CACHE_NAME_HCSA_SERVICE, hcsaServiceDto.getId(),
						hcsaServiceDto, RedisCacheHelper.NOT_EXPIRE);
			}
		}

		return hcsaServiceDto;
	}

	public static HcsaServiceDto getServiceByCode(String code) {
		List<HcsaServiceDto> serviceDtos = receiveAllHcsaService();
		if (!IaisCommonUtils.isEmpty(serviceDtos)) {
			for (HcsaServiceDto s : serviceDtos) {
				if (s.getSvcCode().equals(code)) {
					return s;
				}
			}
		}

		return null;
	}

	public static HcsaServiceDto getServiceByServiceName(String svcName) {
		List<HcsaServiceDto> serviceDtos = receiveAllHcsaService();
		if (!IaisCommonUtils.isEmpty(serviceDtos)) {
			for (HcsaServiceDto s : serviceDtos) {
				if (s.getSvcName().equals(svcName)) {
					return s;
				}
			}
		}

		return null;
	}


	public static void receiveServiceMapping(){
		HcsaServiceClient serviceClient = SpringContextHelper.getContext().getBean(HcsaServiceClient.class);
		if (serviceClient == null){
			log.info("================== receive service on startup failed ==================");
			log.info(HcsaServiceCacheHelper.class.getName() +  " service client is null");
			return;
		}

		int status = serviceClient.getActiveServices().getStatusCode();

		log.info("HcsaServiceCacheHelper status  =====>" + status);

		if (status == HttpStatus.SC_OK){
			List<HcsaServiceDto> serviceList = serviceClient.getActiveServices().getEntity();
			RedisCacheHelper redisCacheHelper = RedisCacheHelper.getInstance();
			redisCacheHelper.set(CACHE_NAME_HCSA_SERVICE, KEY_NAME_HCSA_SERVICE_LIST, serviceList);
			serviceList.forEach(i -> redisCacheHelper.set(CACHE_NAME_HCSA_SERVICE, i.getId(),
					i, RedisCacheHelper.NOT_EXPIRE));
		}
	}

	public static List<HcsaServiceDto> receiveAllHcsaService(){
		RedisCacheHelper redisCacheHelper = RedisCacheHelper.getInstance();
		List<HcsaServiceDto> list  = redisCacheHelper.get(CACHE_NAME_HCSA_SERVICE, KEY_NAME_HCSA_SERVICE_LIST);

		if(IaisCommonUtils.isEmpty(list)){
			HcsaServiceClient serviceClient = SpringContextHelper.getContext().getBean(HcsaServiceClient.class);
			List<HcsaServiceDto> serviceList = serviceClient.getActiveServices().getEntity();
			redisCacheHelper.set(CACHE_NAME_HCSA_SERVICE, KEY_NAME_HCSA_SERVICE_LIST, serviceList);
			serviceList.forEach(i -> redisCacheHelper.set(CACHE_NAME_HCSA_SERVICE, i.getId(),
					i, RedisCacheHelper.NOT_EXPIRE));
		}


		return list;
	}

}
