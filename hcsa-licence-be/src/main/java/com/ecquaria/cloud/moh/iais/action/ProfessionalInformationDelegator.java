package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.ProfessionalDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.ProfessionalInformationQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalParameterDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelWriter;
import com.ecquaria.cloud.moh.iais.service.HcsaChklService;
import com.ecquaria.cloud.moh.iais.service.OnlineEnquiriesService;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: yichen
 * @date time:2/13/2020 5:30 PM
 * @description:
 */
@Delegator(value = "professionalInformationDelegator")
@Slf4j
public class ProfessionalInformationDelegator {

	public static final String PROFESSIONAL_INFORMATION_SEARCH = "professionalInfoSearch";
	public static final String PROFESSIONAL_INFORMATION_RESULT = "professionalInfoResult";
	public static final String PROFESSIONAL_INFORMATION_DETAIL = "professionalInfo";
	@Value("${iais.hmac.keyId}")
	private String keyId;
	@Value("${iais.hmac.second.keyId}")
	private String secKeyId;
	@Value("${iais.hmac.secretKey}")
	private String secretKey;
	@Value("${iais.hmac.second.secretKey}")
	private String secSecretKey;
	@Value("${moh.halp.prs.enable}")
	private String prsFlag;
	@Autowired
	private BeEicGatewayClient beEicGatewayClient;
	private OnlineEnquiriesService onlineEnquiriesService;

	@Autowired
	public ProfessionalInformationDelegator(OnlineEnquiriesService onlineEnquiriesService, HcsaChklService hcsaChklService) {
		this.onlineEnquiriesService = onlineEnquiriesService;
	}

	private FilterParameter filterParameter = new FilterParameter.Builder()
			.clz(ProfessionalInformationQueryDto.class)
			.searchAttr(PROFESSIONAL_INFORMATION_SEARCH)
			.resultAttr(PROFESSIONAL_INFORMATION_RESULT)
			.sortField("id").sortType(SearchParam.ASCENDING).build();


	/**
	 * StartStep: preLoad
	 *
	 * @param bpc
	 * @throws
	 */
	public void preLoad(BaseProcessClass bpc){
		HttpServletRequest request = bpc.request;
		ParamUtil.setSessionAttr(request, PROFESSIONAL_INFORMATION_DETAIL, null);

		preSelectOption(request);

		SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);

		QueryHelp.setMainSql("onlineEnquiry", "searchByProfessionalInfo",searchParam);

		SearchResult<ProfessionalInformationQueryDto> results = onlineEnquiriesService.searchProfessionalInformation(searchParam);

		ParamUtil.setSessionAttr(request, PROFESSIONAL_INFORMATION_SEARCH, searchParam);
		ParamUtil.setSessionAttr(request, PROFESSIONAL_INFORMATION_RESULT, results);
	}


	/**
	 * StartStep: startStep
	 *
	 * @param bpc
	 * @throws
	 */
	public void startStep(BaseProcessClass bpc){
		HttpServletRequest request = bpc.request;
		log.debug(StringUtil.changeForLog("Search by professional information start ...."));
		AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_SYSTEM_CONFIG,  AuditTrailConsts.FUNCTION_SEARCH_BY_PROF);

		ParamUtil.setSessionAttr(request, "name", null);
		ParamUtil.setSessionAttr(request, "profRegNo", null);
		ParamUtil.setSessionAttr(request, "postalCode", null);
		ParamUtil.setSessionAttr(request, "address", null);
		ParamUtil.setSessionAttr(request, "hciName", null);
		ParamUtil.setSessionAttr(request, "hciCode", null);
		ParamUtil.setSessionAttr(request, "hciPostalcode", null);
		ParamUtil.setSessionAttr(request, "practiceLocation", null);
		ParamUtil.setSessionAttr(request, "serviceName", null);
		ParamUtil.setSessionAttr(request, "designation", null);
		ParamUtil.setSessionAttr(request, "role", null);
		ParamUtil.setSessionAttr(request, PROFESSIONAL_INFORMATION_SEARCH, null);
		ParamUtil.setSessionAttr(request, PROFESSIONAL_INFORMATION_RESULT, null);
	}

	/**
	 * AutoStep: switchAction
	 * @param bpc
	 * @throws IllegalAccessException
	 */
	public void switchAction(BaseProcessClass bpc){
		HttpServletRequest request = bpc.request;

	}


	/**
	 * @author: yichen
	 * @description: the mehod download excel file by db genereate
	 * @param:
	 * @return:
	 */
	@GetMapping(value = "professional-information-file")
	public @ResponseBody
	void fileHandler(HttpServletRequest request, HttpServletResponse response) {
		log.debug(StringUtil.changeForLog("fileHandler start ...."));
		File file = null;

		SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
		searchParam.setPageNo(0);
		searchParam.setPageSize(Integer.MAX_VALUE);

		log.debug("indicates that a record has been selected ");

		QueryHelp.setMainSql("onlineEnquiry", "searchByProfessionalInfo",searchParam);

		SearchResult<ProfessionalInformationQueryDto> results = onlineEnquiriesService.searchProfessionalInformation(searchParam);

		if (!Objects.isNull(results)){
			List<ProfessionalInformationQueryDto> queryList = results.getRows();
			queryList.forEach(i -> i.setDesignation(MasterCodeUtil.getCodeDesc(i.getDesignation())));

			try {
				file = ExcelWriter.writerToExcel(queryList, ProfessionalInformationQueryDto.class, "Professional Information_Search_Template");
			} catch (Exception e) {
				log.error("=======>fileHandler error >>>>>", e);
			}
		}

		try {
			FileUtils.writeFileResponseContent(response, file);
			FileUtils.deleteTempFile(file);
		} catch (IOException e) {
			log.debug(e.getMessage());
		}
		log.debug(StringUtil.changeForLog("fileHandler end ...."));
	}

	/**
	 * AutoStep: preLoadProfessionalDetails
	 * @param bpc
	 * @throws IllegalAccessException
	 */
	public void preLoadProfessionalDetails(BaseProcessClass bpc){
		HttpServletRequest request = bpc.request;
		ProfessionalDetailsDto detailsDto = new ProfessionalDetailsDto();

		if("Y".equals(prsFlag)){
			Set<String> redNo=new HashSet<>();
			//  Set<String> idNoSet=new HashSet<>();
			String id = ParamUtil.getMaskedString(request, "prRegNo");
			String[] prsIdNoSplit=id.split("\\|");
			redNo.add(prsIdNoSplit[1]);
			// idNoSet.add(prsIdNoSplit[0]);
			ProfessionalParameterDto professionalParameterDto =new ProfessionalParameterDto();
			List<String> list = new ArrayList<>(redNo);
			professionalParameterDto.setRegNo(list);
			professionalParameterDto.setClientId("22222");
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String format = simpleDateFormat.format(new Date());
			professionalParameterDto.setTimestamp(format);
			professionalParameterDto.setSignature("2222");

			HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
			HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
			List<ProfessionalResponseDto> professionalResponseDtos = null;
			try {
				professionalResponseDtos = beEicGatewayClient.getProfessionalDetail(professionalParameterDto, signature.date(), signature.authorization(),
						signature2.date(), signature2.authorization()).getEntity();
				if(professionalResponseDtos!=null){
					detailsDto.setDpRecords(professionalResponseDtos.get(0).getQualification().get(0));
					detailsDto.setRegDit(professionalResponseDtos.get(0).getRegistration().get(0).getRegistrationType());
					detailsDto.setRegExpDate(professionalResponseDtos.get(0).getRegistration().get(0).getPcEndDate());
				}
			}catch (Throwable e){
				log.error(e.getMessage(),e);
				request.setAttribute("beEicGatewayClient","Not able to connect to professionalResponseDtos at this moment!");
				log.error("------>this have error<----- Not able to connect to professionalResponseDtos at this moment!");
			}
		}else {
			detailsDto.setDpRecords("-");
			detailsDto.setRegDit("-");
			detailsDto.setRegExpDate("-");
		}


		ParamUtil.setSessionAttr(request, PROFESSIONAL_INFORMATION_DETAIL, detailsDto);
	}

	private List<HcsaServiceDto> receiveHcsaService(){
		List<HcsaServiceDto> svcNames = HcsaServiceCacheHelper.receiveAllHcsaService();

		//TODO phase 1 value
		return svcNames.stream().filter(i ->
				i.getSvcCode().equals(AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY)
						|| i.getSvcCode().equals(AppServicesConsts.SERVICE_CODE_TISSUE_BANKING)
						|| i.getSvcCode().equals(AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES)
						|| i.getSvcCode().equals(AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_ASSAY)
						|| i.getSvcCode().equals(AppServicesConsts.SERVICE_CODE_BLOOD_BANKING)
						|| i.getSvcCode().equals(AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING)
						|| i.getSvcCode().equals(AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE)
						|| i.getSvcCode().equals(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE)
		).collect(Collectors.toList());
	}

	/**
	 * setup option to web page
	 * @param request
	 */
	private void preSelectOption(HttpServletRequest request){
		List<HcsaServiceDto> svcNames = receiveHcsaService();
		List<SelectOption> svcNameSelect = IaisCommonUtils.genNewArrayList();

		List<SelectOption> psnType =  IaisCommonUtils.genNewArrayList();
		psnType.add(new SelectOption(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO, ApplicationConsts.PERSONNEL_PSN_TYPE_CGO));
		psnType.add(new SelectOption(ApplicationConsts.PERSONNEL_PSN_TYPE_PO, ApplicationConsts.PERSONNEL_PSN_TYPE_PO));
		psnType.add(new SelectOption(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO, ApplicationConsts.PERSONNEL_PSN_TYPE_DPO));
		psnType.add(new SelectOption(ApplicationConsts.PERSONNEL_PSN_TYPE_MEDALERT, ApplicationConsts.PERSONNEL_PSN_TYPE_MEDALERT));

		ParamUtil.setRequestAttr(request, "psnType", psnType);

		for (HcsaServiceDto s : svcNames){
			svcNameSelect.add(new SelectOption(s.getSvcName(), s.getSvcName()));
		}

		ParamUtil.setRequestAttr(request, "svcNameSelect", svcNameSelect);

	}

	/**
	 * AutoStep: doSearch
	 * @param bpc
	 * @throws IllegalAccessException
	 */
	public void doSearch(BaseProcessClass bpc){
		HttpServletRequest request = bpc.request;

		String name = ParamUtil.getString(request, "name");
		String profRegNo = ParamUtil.getString(request, "profRegNo");
		String postalCode = ParamUtil.getString(request, "postalCode");
		String address = ParamUtil.getString(request, "address");
		String hciName = ParamUtil.getString(request, "hciName");
		String hciCode = ParamUtil.getString(request, "hciCode");
		String hciPostalcode = ParamUtil.getString(request, "hciPostalcode");
		String practiceLocation = ParamUtil.getString(request, "practiceLocation");
		String serviceName = ParamUtil.getString(request, "serviceName");
		String designation = ParamUtil.getString(request, "designation");
		String role = ParamUtil.getString(request, "role");

		ParamUtil.setSessionAttr(request, "name", name);
		ParamUtil.setSessionAttr(request, "profRegNo", profRegNo);
		ParamUtil.setSessionAttr(request, "postalCode", postalCode);
		ParamUtil.setSessionAttr(request, "address", address);
		ParamUtil.setSessionAttr(request, "hciName", hciName);
		ParamUtil.setSessionAttr(request, "hciCode", hciCode);
		ParamUtil.setSessionAttr(request, "hciPostalcode", hciPostalcode);
		ParamUtil.setSessionAttr(request, "practiceLocation", practiceLocation);
		ParamUtil.setSessionAttr(request, "serviceName", serviceName);
		ParamUtil.setSessionAttr(request, "designation", designation);
		ParamUtil.setSessionAttr(request, "role", role);

		ProfessionalInformationQueryDto dto = new ProfessionalInformationQueryDto();
		dto.setName(name);
		dto.setProfRegNo(profRegNo);
		dto.setPracticeLocation(practiceLocation);
		dto.setServiceName(serviceName);
		dto.setDesignation(designation);
		dto.setHciCode(hciCode);
		dto.setHciName(hciName);
		dto.setHciPostalode(hciPostalcode);
		dto.setPostalode(postalCode);
		dto.setAddress(address);
		dto.setRole(role);

		ValidationResult validationResult = WebValidationHelper.validateProperty(dto, "search");
		if(validationResult != null && validationResult.isHasErrors()){
			Map<String,String> errorMap = validationResult.retrieveAll();
			ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
			ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"N");
		}else {
			SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);

			if(!StringUtil.isEmpty(name)){
				searchParam.addFilter("name", name, true);
			}

			if(!StringUtil.isEmpty(profRegNo)){
				searchParam.addFilter("profRegNo", profRegNo, true);
			}

			if(!StringUtil.isEmpty(postalCode)){
				searchParam.addFilter("postalCode", postalCode, true);
			}

			if(!StringUtil.isEmpty(address)){
				searchParam.addFilter("address", address, true);
			}

			if(!StringUtil.isEmpty(hciName)){
				searchParam.addFilter("hciName", hciName, true);
			}

			if(!StringUtil.isEmpty(hciCode)){
				searchParam.addFilter("hciCode", hciCode, true);
			}

			if(!StringUtil.isEmpty(hciPostalcode)){
				searchParam.addFilter("hciPostalcode", hciPostalcode, true);
			}

			if(!StringUtil.isEmpty(practiceLocation)){
				searchParam.addFilter("practiceLocation", practiceLocation, true);
			}

			if(!StringUtil.isEmpty(serviceName)){
				searchParam.addFilter("serviceName", serviceName, true);
			}

			if(!StringUtil.isEmpty(designation)){
				searchParam.addFilter("designation", designation, true);
			}

			if(!StringUtil.isEmpty(role)){
				searchParam.addFilter("role", role, true);
			}

		}



	}

	/**
	 * AutoStep: sortRecords
	 * @param bpc
	 * @throws IllegalAccessException
	 */
	public void sortRecords(BaseProcessClass bpc){
		HttpServletRequest request = bpc.request;

		SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
		CrudHelper.doSorting(searchParam,bpc.request);
	}

	/**
	 * AutoStep: changePage
	 * @param bpc
	 * @throws IllegalAccessException
	 */
	public void changePage(BaseProcessClass bpc){
		HttpServletRequest request = bpc.request;

		SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
		CrudHelper.doPaging(searchParam,bpc.request);
	}

}
