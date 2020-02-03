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
	private HcsaServiceCacheHelper(){throw new IllegalStateException("Util class");}

	// the key is hcsa service key(pk id)
	private static final HashMap<String, HcsaServiceDto> svcHashMap = new HashMap<>();

	public static HashMap<String, HcsaServiceDto> getServiceMapping() {
		return svcHashMap;
	}

	public static String getServiceNameById(String id){
		if (svcHashMap.containsKey(id)){
			return svcHashMap.get(id).getSvcName();
		}else {
			return null;
		}
	}

	public static String getServiceTypeById(String id){
		if (svcHashMap.containsKey(id)){
			return svcHashMap.get(id).getSvcType();
		}else {
			return null;
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
			List<HcsaServiceDto> serviceDtos = serviceClient.getActiveServices().getEntity();
			if (!IaisCommonUtils.isEmpty(serviceDtos)){
				serviceDtos.stream().forEach(i -> {
					svcHashMap.put(i.getId(), i);
				});
			}
		}
	}

}
