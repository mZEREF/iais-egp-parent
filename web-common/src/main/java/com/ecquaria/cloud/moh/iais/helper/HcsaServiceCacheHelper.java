package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.constant.RedisNameSpaceConstant;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.helper.RedisCacheHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.client.HcsaServiceClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.util.List;

/**
 * @author: yichen
 * @date time:1/20/2020 1:01 PM
 * @description:
 */

@Slf4j
public final class HcsaServiceCacheHelper {
	private HcsaServiceCacheHelper(){throw new IllegalStateException("Util class");}

	public static String getServiceNameById(String id){
		HcsaServiceDto hcsaServiceDto = getServiceById(id);
		log.debug(StringUtil.changeForLog("service info " + JsonUtil.parseToJson(hcsaServiceDto)));

		if (hcsaServiceDto == null || StringUtil.isEmpty(hcsaServiceDto.getSvcName())){
			return id;
		}else {
			return hcsaServiceDto.getSvcName();
		}
	}

	public static HcsaServiceDto getServiceById(String id) {
		RedisCacheHelper redisCacheHelper = SpringContextHelper.getContext().getBean(RedisCacheHelper.class);
		HcsaServiceDto hcsaServiceDto = redisCacheHelper.get(RedisNameSpaceConstant.CACHE_NAME_HCSA_SERVICE, id);

		if (hcsaServiceDto == null){
			HcsaServiceClient serviceClient = SpringContextHelper.getContext().getBean(HcsaServiceClient.class);
			hcsaServiceDto = serviceClient.getHcsaServiceDtoByServiceId(id).getEntity();
			if (hcsaServiceDto != null) {
				redisCacheHelper.set(RedisNameSpaceConstant.CACHE_NAME_HCSA_SERVICE, hcsaServiceDto.getId(),
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
		log.info(StringUtil.changeForLog("The service name param ==>> " + svcName));
		List<HcsaServiceDto> serviceDtos = receiveAllHcsaService();
		if (!IaisCommonUtils.isEmpty(serviceDtos)) {
			for (HcsaServiceDto s : serviceDtos) {
				log.info(StringUtil.changeForLog("service name " + s.getSvcName()));
				if (s.getSvcName().equals(svcName)) {
					return s;
				}
			}
		}

		return null;
	}


	public static void flushServiceMapping(){
		HcsaServiceClient serviceClient = SpringContextHelper.getContext().getBean(HcsaServiceClient.class);
		if (serviceClient != null){
			FeignResponseEntity<List<HcsaServiceDto>> result = serviceClient.getActiveServices();
			int status = result.getStatusCode();

			log.info(StringUtil.changeForLog("HcsaServiceCacheHelper status  =====>" + status));

			if (status == HttpStatus.SC_OK){
				List<HcsaServiceDto> serviceList = result.getEntity();
				RedisCacheHelper redisCacheHelper = SpringContextHelper.getContext().getBean(RedisCacheHelper.class);
				redisCacheHelper.clear(RedisNameSpaceConstant.CACHE_NAME_HCSA_SERVICE);
				redisCacheHelper.set(RedisNameSpaceConstant.CACHE_NAME_HCSA_SERVICE, RedisNameSpaceConstant.KEY_NAME_HCSA_SERVICE_LIST, serviceList);
				serviceList.forEach(i -> redisCacheHelper.set(RedisNameSpaceConstant.CACHE_NAME_HCSA_SERVICE, i.getId(),
						i, RedisCacheHelper.NOT_EXPIRE));
			}
		}
	}

	public static List<HcsaServiceDto> receiveAllHcsaService(){
		RedisCacheHelper redisCacheHelper = SpringContextHelper.getContext().getBean(RedisCacheHelper.class);
		List<HcsaServiceDto> list  = redisCacheHelper.get(RedisNameSpaceConstant.CACHE_NAME_HCSA_SERVICE, RedisNameSpaceConstant.KEY_NAME_HCSA_SERVICE_LIST);
		if(!IaisCommonUtils.isEmpty(list)){
			return list;
		}else {
			flushServiceMapping();
			return redisCacheHelper.get(RedisNameSpaceConstant.CACHE_NAME_HCSA_SERVICE, RedisNameSpaceConstant.KEY_NAME_HCSA_SERVICE_LIST);
		}
	}

}
