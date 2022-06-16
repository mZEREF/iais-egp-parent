package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.RedisNameSpaceConstant;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageSearchDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserRoleAccessMatrixDto;
import com.ecquaria.cloud.moh.iais.common.helper.RedisCacheHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.service.client.HcsaServiceClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
			return "-";
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

	public static List<SelectOption> getAllServiceSelectOptions(){
		List<SelectOption> selectOptions = receiveAllHcsaService().stream().map(obj-> new SelectOption(obj.getSvcCode(),obj.getSvcName())).collect(Collectors.toList());
		selectOptions.add(0,new SelectOption(AppServicesConsts.SERVICE_MATRIX_ALL,AppServicesConsts.SERVICE_MATRIX_ALL_NAME));
		return selectOptions;
	}

	// 0 -> msg, serviceCodes= serviceCodes+@ ; 1-> app, serviceCodes= serviceCodes+@ ; 2 -> lic serviceCodes = serviceNames; 3->serviceCodes = serviceCodes
	public static InterMessageSearchDto controlServices(int searchDataTab,String licenseeId, List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos){
		InterMessageSearchDto interMessageSearchDto = new InterMessageSearchDto();
		interMessageSearchDto.setLicenseeId(licenseeId);
		interMessageSearchDto.setSearchTable(searchDataTab);
		if(searchDataTab == 0){
			interMessageSearchDto.setSearchSql(3);
		}
       if(IaisCommonUtils.isNotEmpty( userRoleAccessMatrixDtos)){
		   interMessageSearchDto.setServiceCodes(controlServices(searchDataTab,userRoleAccessMatrixDtos));
	   }
       return interMessageSearchDto;
	}

	public static List<String> getAccessSvcNames(HttpServletRequest request) {
		LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
		List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = loginContext.getRoleMatrixes()
				.get(RoleConsts.USER_ROLE_ORG_USER);
		List<String> list = controlServices(2, userRoleAccessMatrixDtos);
		if (list == null) {
			list = IaisCommonUtils.genNewArrayList();
		}
		return list;
	}

	public static List<String> getAccessSvcCodes(HttpServletRequest request) {
		LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
		List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = loginContext.getRoleMatrixes()
				.get(RoleConsts.USER_ROLE_ORG_USER);
		List<String> list = controlServices(3, userRoleAccessMatrixDtos);
		if (list == null) {
			list = IaisCommonUtils.genNewArrayList();
		}
		return list;
	}

	// 0 -> msg, serviceCodes= serviceCodes+@ ; 1-> app, serviceCodes= serviceCodes+@ ; 2 -> lic serviceCodes = serviceNames; 3->serviceCodes = serviceCodes
	public static List<String> controlServices(int searchDataTab, List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos){
		if(IaisCommonUtils.isNotEmpty( userRoleAccessMatrixDtos)){
			for (UserRoleAccessMatrixDto obj: userRoleAccessMatrixDtos) {
				if(AppServicesConsts.SERVICE_MATRIX_ALL.equalsIgnoreCase(obj.getMatrixValue())){
					if(searchDataTab == 0 || searchDataTab == 1){
						return receiveAllHcsaService().stream().map(hcsaServiceDto ->hcsaServiceDto.getSvcCode()+ "@").collect(Collectors.toList());
					}
					if(searchDataTab == 2){
						return receiveAllHcsaService().stream().map(HcsaServiceDto::getSvcName).collect(Collectors.toList());
					}
					if(searchDataTab == 3){
						return receiveAllHcsaService().stream().map(HcsaServiceDto::getSvcCode).collect(Collectors.toList());
					}
				}
			}
			if(searchDataTab == 0 || searchDataTab == 1){
				return userRoleAccessMatrixDtos.stream().map(userRoleAccessMatrixDto -> userRoleAccessMatrixDto.getMatrixValue()+ "@").collect(Collectors.toList());
			}

			if(searchDataTab == 2){
				Map<String,String> map = receiveAllHcsaService().stream().collect(Collectors.toMap(HcsaServiceDto::getSvcCode, HcsaServiceDto::getSvcName, (v1, v2) -> v1));
				return userRoleAccessMatrixDtos.stream().map(userRoleAccessMatrixDto -> map.get(userRoleAccessMatrixDto.getMatrixValue())).collect(Collectors.toList());
			}
			if(searchDataTab == 3){
				return userRoleAccessMatrixDtos.stream().map(UserRoleAccessMatrixDto::getMatrixValue).collect(Collectors.toList());
			}

		}
		return IaisCommonUtils.genNewArrayList();
	}

}
