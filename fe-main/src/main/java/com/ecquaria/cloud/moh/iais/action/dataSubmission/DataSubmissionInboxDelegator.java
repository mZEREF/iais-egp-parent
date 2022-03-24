package com.ecquaria.cloud.moh.iais.action.dataSubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.helper.ConfigHelper;
import com.ecquaria.cloud.moh.iais.action.HalpAssessmentGuideDelegator;
import com.ecquaria.cloud.moh.iais.action.InterInboxDelegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxDataSubmissionQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;
import com.ecquaria.cloud.moh.iais.common.helper.dataSubmission.DsHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.FeInboxHelper;
import com.ecquaria.cloud.moh.iais.helper.HalpSearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.service.client.LicenceInboxClient;
import com.ecquaria.cloud.privilege.Privilege;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wangyu
 * @date time:11/5/2021 2:07 PM
 * @description:
 */

@Delegator(value = "dataSubmissionInboxDelegator")
@Slf4j
public class DataSubmissionInboxDelegator {

	private final static  String NEED_VALIDATOR_SIZE = "needValidatorSize";
	private final static  String ACTION_DS_BUTTON_SHOW = "actionDsButtonShow";
	private final static  String DELETE_DRAFT          = "deleteDraft";
	private final static  String AMENDED                ="doRFC";
	private final static  String WITHDRAW               = "withdraw";
	private final static  String UNLOCK                 = "unlock";
	private final static  String DS_TYPES               = "dsTypes";
	private final static  String DS_STATUSES            = "dsStatuses";
	private final static  String SORT_INIT              = "UPDATED_DT";
    @Autowired
	private LicenceInboxClient licenceInboxClient;
    @Autowired
	private InterInboxDelegator interInboxDelegator;
	/**
	 * Step: doStart
	 *
	 * @param bpc
	 * @throws
	 */
	public void doStart(BaseProcessClass bpc){
		setLog("doStart");
		HttpServletRequest request = bpc.request;
		initData(request);
		AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INTERNAL_INBOX, AuditTrailConsts.FUNCTION_INBOX);
		setLog("doStart",false);
	}

	public void initData(HttpServletRequest request){
		clearSession(request);
		ParamUtil.setSessionAttr(request,DS_TYPES,(Serializable) FeInboxHelper.getDsTypes(AccessUtil.getLoginUser(request).getPrivileges().stream().map(Privilege::getId).collect(Collectors.toList())));
		ParamUtil.setSessionAttr(request,DS_STATUSES,(Serializable) FeInboxHelper.dataSubmissionStatusOptions);
	}

	public void clearSession(HttpServletRequest request){
		ParamUtil.clearSession(request,ACTION_DS_BUTTON_SHOW,InboxConst.DS_PARAM,InboxConst.DS_RESULT,DS_TYPES);
	}
	private void setLog(String sepName){
		setLog(sepName,true,null);
	}

	private void setLog(String sepName,boolean start){
		setLog(sepName,start,null);
	}
	private void setLog(String stepName, boolean start, Map<String,String> linkMap){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("----------------- ");
		if(StringUtil.isEmpty(stepName)){
			if(IaisCommonUtils.isNotEmpty(linkMap)){
				linkMap.forEach((key,value) -> stringBuilder.append(key).append(" : ").append(value).append(" "));
				stringBuilder.append("----------------- ");
			}
		}else {
			stringBuilder.append(stepName);
			if(start){
				stringBuilder.append(" start ");
			}else {
				stringBuilder.append(" end ");
			}
			stringBuilder.append("-----------------");
			log.info(stringBuilder.toString());
		}


	}

	/**
	 * Step: prepare
	 *
	 * @param bpc
	 * @throws
	 */
	public void prepare(BaseProcessClass bpc){
		setLog("prepare");
		HttpServletRequest request = bpc.request;
		InterInboxUserDto interInboxUserDto = (InterInboxUserDto) ParamUtil.getSessionAttr(request,InboxConst.INTER_INBOX_USER_INFO);
		interInboxDelegator.setNumInfoToRequest(request,interInboxUserDto);
		if(StringUtil.isEmpty(ParamUtil.getSessionAttr(request,ACTION_DS_BUTTON_SHOW))){
			if(StringUtil.isNotEmpty(interInboxUserDto.getLicenseeId()) || IaisCommonUtils.isEmpty((List<String>)ParamUtil.getSessionAttr(request,DS_TYPES))){
				SearchParam searchParam = HalpSearchResultHelper.gainSearchParam(request, InboxConst.DS_PARAM, InboxDataSubmissionQueryDto.class.getName(),SORT_INIT,SearchParam.DESCENDING,false);
				searchParam.addParam("fromCenter",ConfigHelper.getBoolean("halp.ds.tempCenter.enable",false));
				HalpAssessmentGuideDelegator.setParamByField(searchParam,"licenseeId",interInboxUserDto.getLicenseeId(),true);
				HalpAssessmentGuideDelegator.setParamByField(searchParam,"dsType",(List<String>) ParamUtil.getSessionAttr(request,DS_TYPES));
				QueryHelp.setMainSql(InboxConst.INBOX_QUERY, InboxConst.INBOX_DS_QUERY,searchParam);
				ParamUtil.setSessionAttr(request, InboxConst.DS_RESULT,licenceInboxClient.searchLicence(searchParam).getEntity());
				ParamUtil.setSessionAttr(request, InboxConst.DS_PARAM,searchParam);
			}else {
				ParamUtil.clearSession(request,InboxConst.DS_PARAM,InboxConst.DS_RESULT);
			}
		}else {
			ParamUtil.setRequestAttr(request,ACTION_DS_BUTTON_SHOW,ParamUtil.getSessionAttr(request,ACTION_DS_BUTTON_SHOW));
			ParamUtil.clearSession(request,ACTION_DS_BUTTON_SHOW);
			ParamUtil.reSetSession(request,InboxConst.DS_PARAM,InboxConst.DS_RESULT);
		}
		setLog("prepare",false);
	}

	/**
	 * Step: actionData
	 *
	 * @param bpc
	 * @throws
	 */
	public void actionData(BaseProcessClass bpc){
		setLog("actionData");
		HttpServletRequest request = bpc.request;
		setLog("actionData",false);
	}

	/**
	 * Step: search
	 *
	 * @param bpc
	 * @throws
	 */
	public void search(BaseProcessClass bpc){
		setLog("search");
		HttpServletRequest request = bpc.request;
		setSearchParam(request);
		setLog("search",false);
	}

	private void setSearchParam(HttpServletRequest request)  {
		SearchParam searchParam = HalpSearchResultHelper.gainSearchParam(request, InboxConst.DS_PARAM, InboxDataSubmissionQueryDto.class.getName(),SORT_INIT,SearchParam.DESCENDING,false);
		InboxDataSubmissionQueryDto inboxDataSubmissionQueryDto = ControllerHelper.get(request,InboxDataSubmissionQueryDto.class,"DataSubmission");
		HalpAssessmentGuideDelegator.setParamByField(searchParam,"submissionNo",inboxDataSubmissionQueryDto.getSubmissionNo(),true);
		//when status -> DS014 , status cannot be DS013,DS014.
		HalpAssessmentGuideDelegator.setParamByField(searchParam,"status",inboxDataSubmissionQueryDto.getStatus(),true,InboxConst.SEARCH_ALL);
		HalpAssessmentGuideDelegator.setParamByField(searchParam,"type",inboxDataSubmissionQueryDto.getType() ,true,InboxConst.SEARCH_ALL);
		setSearchParamDate(request,searchParam);
	}

	private void setSearchParamDate(HttpServletRequest request,SearchParam searchParam){
		HalpAssessmentGuideDelegator.setParamForDate(request,searchParam,"lastDateStart","lastDateStart");
		HalpAssessmentGuideDelegator.setParamForDate(request,searchParam,"lastDateEnd","lastDateEnd");
		Map<String, Object>  stringObjectHashMap = searchParam.getParams();
		if(IaisCommonUtils.isNotEmpty(stringObjectHashMap)){
			String dateStart = (String) stringObjectHashMap.get("lastDateStart");
			String dateEnd = (String) stringObjectHashMap.get("lastDateEnd");
			if(dateStart !=null && dateEnd != null && dateStart.compareTo(dateEnd) > 0){
					ParamUtil.setRequestAttr(request,"lastUpdateINBOX_ERR011",MessageUtil.getMessageDesc("INBOX_ERR011"));
					searchParam = null;
					log.info("------setSearchParamDate is no------------");
			}else {
					log.info("------setSearchParamDate is ok------------");
			}
		}
		ParamUtil.setSessionAttr(request, InboxConst.DS_PARAM,searchParam);
	}


	/**
	 * Step: page
	 *
	 * @param bpc
	 * @throws
	 */
	public void page(BaseProcessClass bpc){
		setLog("page");
		HttpServletRequest request = bpc.request;
		SearchParam searchParam = HalpSearchResultHelper.gainSearchParam(request, InboxConst.DS_PARAM, InboxDataSubmissionQueryDto.class.getName(),SORT_INIT,SearchParam.DESCENDING,false);
		HalpSearchResultHelper.doPage(request,searchParam);
		ParamUtil.setSessionAttr(request, InboxConst.DS_PARAM,searchParam);
		setLog("page",false);
	}

	/**
	 * Step: sort
	 *
	 * @param bpc
	 * @throws
	 */
	public void sort(BaseProcessClass bpc){
		setLog("sort");
		HttpServletRequest request = bpc.request;
		SearchParam searchParam = HalpSearchResultHelper.gainSearchParam(request, InboxConst.DS_PARAM, InboxDataSubmissionQueryDto.class.getName(),SORT_INIT,SearchParam.DESCENDING,false);
		HalpSearchResultHelper.doSort(request,searchParam);
		if(searchParam.getSortMap().containsKey("typeDesc".toUpperCase())){
			HalpSearchResultHelper.setMasterCodeForSearchParam(searchParam,"type","typeDesc",MasterCodeUtil.DATA_SUBMISSION_TYPE);
		}else if(searchParam.getSortMap().containsKey("statusDesc".toUpperCase())){
			HalpSearchResultHelper.setMasterCodeForSearchParam(searchParam,"status","statusDesc",MasterCodeUtil.DATA_SUBMISSION_STATUS);
		}
		ParamUtil.setSessionAttr(request, InboxConst.DS_PARAM,searchParam);
		setLog("sort",false);
	}

	/**
	 * Step: deleteDraft
	 *
	 * @param bpc
	 * @throws
	 */
	public void deleteDraft(BaseProcessClass bpc){
		setLog(DELETE_DRAFT);
		HttpServletRequest request = bpc.request;
		toShowMessage(request,null,DELETE_DRAFT);
		setLog(DELETE_DRAFT,false);
	}

	/**
	 * Step: view
	 *
	 * @param bpc
	 * @throws
	 */
	public void view(BaseProcessClass bpc){
		setLog("view");
		HttpServletRequest request = bpc.request;
		HttpServletResponse response = bpc.response;
        String submissionNo = ParamUtil.getString(request,"crud_type_action_submission_no");
		InboxDataSubmissionQueryDto inboxDataSubmissionQueryDto =  getInboxDataSubmissionQueryDtoBySubmissionNo(request,submissionNo);
		if(inboxDataSubmissionQueryDto == null){
			return;
		}
		Map<String,String> params = IaisCommonUtils.genNewHashMap(2);
		if(checkDataPassBySubmissionNo(submissionNo,DELETE_DRAFT)){
			params.put("dsType",inboxDataSubmissionQueryDto.getDsType());
			params.put("draftNo",submissionNo);
			IaisEGPHelper.redirectUrl(response,request, "MohDsDraft",InboxConst.URL_LICENCE_WEB_MODULE,params);
		}else {
			params.put("dsType",inboxDataSubmissionQueryDto.getDsType());
			params.put("type","preview");
			params.put("submissionNo",submissionNo);
			IaisEGPHelper.redirectUrl(response,request, "MohDsAction",InboxConst.URL_LICENCE_WEB_MODULE,params);
		}
		setLog("view",false);
	}

	private InboxDataSubmissionQueryDto getInboxDataSubmissionQueryDtoBySubmissionNo(HttpServletRequest request,String submissionNo){
		SearchResult<InboxDataSubmissionQueryDto> submissionQueryDtoSearchResult =(SearchResult<InboxDataSubmissionQueryDto>)ParamUtil.getSessionAttr(request, InboxConst.DS_RESULT);
		if(submissionQueryDtoSearchResult != null && IaisCommonUtils.isNotEmpty(submissionQueryDtoSearchResult.getRows())){
			for (InboxDataSubmissionQueryDto obj : submissionQueryDtoSearchResult.getRows()) {
				if( submissionNo.equalsIgnoreCase(obj.getSubmissionNo())){
					return obj;
				}
			}
		}
		return null;
	}



	/**
	 * Step: doRFC
	 *
	 * @param bpc
	 * @throws
	 */
	public void doRFC(BaseProcessClass bpc){
		setLog(AMENDED);
		HttpServletRequest request = bpc.request;
		HttpServletResponse response = bpc.response;
		toShowMessage(request,response,AMENDED);
		setLog(AMENDED,false);
	}

	/**
	 * Step: withdraw
	 *
	 * @param bpc
	 * @throws
	 */
	public void withdraw(BaseProcessClass bpc){
		setLog(WITHDRAW);
		HttpServletRequest request = bpc.request;
		HttpServletResponse response = bpc.response;
		toShowMessage(request,response,WITHDRAW);
		setLog(WITHDRAW,false);
	}

	/**
	 * Step: unlock
	 *
	 * @param bpc
	 * @throws
	 */
	public void unlock(BaseProcessClass bpc){
		setLog(UNLOCK);
		HttpServletRequest request = bpc.request;
		HttpServletResponse response = bpc.response;
		toShowMessage(request,response,UNLOCK);
		setLog(UNLOCK,false);
	}

	 private void toShowMessage(HttpServletRequest request,HttpServletResponse response,String actionValue){
	      if(showMessage(request,response,actionValue)){
	      	ParamUtil.setSessionAttr(request,ACTION_DS_BUTTON_SHOW,AppConsts.YES);
	      	ParamUtil.setRequestAttr(request,NEED_VALIDATOR_SIZE,ParamUtil.getString(request,NEED_VALIDATOR_SIZE));
	      	ParamUtil.setRequestAttr(request,"selectAllTypeSub",ParamUtil.getString(request,"selectAllTypeSub"));
	      	setShowPopMsg(request,actionValue);
		  }else {
	      	if(DELETE_DRAFT.equalsIgnoreCase(actionValue)){
				ParamUtil.setRequestAttr(request,"deleteDraftOk",AppConsts.YES);
			}
		  }
	 }

	 private void setShowPopMsg(HttpServletRequest request,String actionValue){
        if(WITHDRAW.equalsIgnoreCase(actionValue) || AMENDED.equalsIgnoreCase(actionValue)){
        	String msg=ParamUtil.getRequestString(request,"showPopFailMsg");
        	if(StringUtil.isEmpty(msg)){
				ParamUtil.setRequestAttr(request,"showPopFailMsg","DS_ERR022");
			}
		}
	 }

    private  boolean showMessage(HttpServletRequest request,HttpServletResponse response,String actionValue){
		 String sizeString = ParamUtil.getString(request,NEED_VALIDATOR_SIZE);
		 List<String> submissionNos = ParamUtil.getListStrings(request,"submissionNo");
		 int size = -1;
		 if(StringUtil.isNotEmpty(sizeString)){
			 try {
				 size = Integer.parseInt(sizeString);
			 } catch (Exception e) {
				 log.error(e.getMessage(),e);
			 }
		 }
		if(size <= 0){
			return true;
		}
		 if( IaisCommonUtils.isNotEmpty(submissionNos)){
			 SearchResult<InboxDataSubmissionQueryDto> submissionQueryDtoSearchResult =(SearchResult<InboxDataSubmissionQueryDto>)ParamUtil.getSessionAttr(request, InboxConst.DS_RESULT);
			 if(submissionQueryDtoSearchResult != null && IaisCommonUtils.isNotEmpty(submissionQueryDtoSearchResult.getRows())){
			 	List<InboxDataSubmissionQueryDto> inboxDataSubmissionQueryDtos = submissionQueryDtoSearchResult.getRows();
				 List<InboxDataSubmissionQueryDto> actionInboxDataSubmissionQueryDtos  = IaisCommonUtils.genNewArrayList(inboxDataSubmissionQueryDtos.size());
			 	inboxDataSubmissionQueryDtos.forEach( inboxDataSubmissionQueryDto -> {
			 		boolean submissionSelect = false;
					 for (String submissionNo : submissionNos) {
						 if (submissionNo.equalsIgnoreCase(inboxDataSubmissionQueryDto.getSubmissionNo())) {
							 submissionSelect = true;
							int switchResult =  checkDataPassBySubmissionNo(submissionNo,actionValue,inboxDataSubmissionQueryDto);
							switch (switchResult){
								case 1:actionInboxDataSubmissionQueryDtos.add(inboxDataSubmissionQueryDto);break;
								case 2:
									//Withdraw.Patient Information or Donor Sample
									ParamUtil.setRequestAttr(request,"showPopFailMsg","DS_ERR050");break;
								case 3:
									//Withdraw.completed
									ParamUtil.setRequestAttr(request,"showPopFailMsg","DS_ERR061");break;
								case 4:
									//Withdraw.locked
									ParamUtil.setRequestAttr(request,"showPopFailMsg","DS_ERR052");break;
								case 5:
									//Withdraw.Withdraw
									ParamUtil.setRequestAttr(request,"showPopFailMsg","DS_ERR054");break;
								case 6:
									//Withdraw.DRAFT
									ParamUtil.setRequestAttr(request,"showPopFailMsg",MessageUtil.getMessageDesc("DS_ERR058", Arrays.asList("field1", "field2"),Arrays.asList("\"Draft\"", "withdrawn")));break;
								case 7:
									//RFC.DRAFT
									ParamUtil.setRequestAttr(request,"showPopFailMsg",MessageUtil.getMessageDesc("DS_ERR058", Arrays.asList("field1", "field2"),Arrays.asList("\"Draft\"", "amended")));break;
								case 8:
									//UNLOCK.DRAFT
									ParamUtil.setRequestAttr(request,"showPopFailMsg",MessageUtil.getMessageDesc("DS_ERR058", Arrays.asList("field1", "field2"),Arrays.asList("\"Draft\"", "unlocked")));break;
								case 9:
									//UNLOCK.UNLOCK
									ParamUtil.setRequestAttr(request,"showPopFailMsg",MessageUtil.getMessageDesc("DS_ERR059", Collections.singletonList("field1"), Collections.singletonList("\"Locked\"")));break;
								case 10:
									//UNLOCK.Pend UNLOCK
									ParamUtil.setRequestAttr(request,"showPopFailMsg",MessageUtil.getMessageDesc("DS_ERR058", Arrays.asList("field1", "field2"),Arrays.asList("\"Pending Unlocked\"", "unlocked")));break;
								case 11:
									//RFC.LOCKED
									ParamUtil.setRequestAttr(request,"showPopFailMsg",MessageUtil.getMessageDesc("DS_ERR058", Arrays.asList("field1", "field2"),Arrays.asList("\"Locked\"", "amended")));break;
								default:
							}
							break;
						 }
					 }
					 inboxDataSubmissionQueryDto.setSubmissionSelect(submissionSelect);

				 });
				 actionDoInboxDataSubmissionQueryDtos(actionInboxDataSubmissionQueryDtos,actionValue,size,request,response);
				 ParamUtil.setSessionAttr(request, InboxConst.DS_RESULT,submissionQueryDtoSearchResult);
				 return actionInboxDataSubmissionQueryDtos.size() != size;
			 }

		 }

		 return false;
	}

	private void actionDoInboxDataSubmissionQueryDtos(List<InboxDataSubmissionQueryDto> actionInboxDataSubmissionQueryDtos,String actionValue,int actionSize,HttpServletRequest request,HttpServletResponse response){
		if(actionSize != actionInboxDataSubmissionQueryDtos.size()){
			return;
		}

		if(actionSize == 1 && !DELETE_DRAFT.equalsIgnoreCase(actionValue)){
			Map<String,String> params = IaisCommonUtils.genNewHashMap(2);
			InboxDataSubmissionQueryDto inboxDataSubmissionQueryDto = actionInboxDataSubmissionQueryDtos.get(0);
			if (AMENDED.equals(actionValue)){
				params.put("dsType",inboxDataSubmissionQueryDto.getDsType());
				params.put("type","rfc");
				params.put("submissionNo",inboxDataSubmissionQueryDto.getSubmissionNo());
				IaisEGPHelper.redirectUrl(response,request, "MohDsAction",InboxConst.URL_LICENCE_WEB_MODULE,params);
			}else if(UNLOCK.equals(actionValue)){
				actionInboxDataSubmissionQueryDtos.stream().forEach(obj->{
					//todo send email to be ar admin; if send email ok,update lockStatus
                     licenceInboxClient.updateDataSubmissionByIdChangeStatus(inboxDataSubmissionQueryDto.getId(),2);
				});
			}
		}

		if(WITHDRAW.equals(actionValue)){
			Map<String,String> params = IaisCommonUtils.genNewHashMap(2);
			params.put("dsType",actionInboxDataSubmissionQueryDtos.get(0).getDsType());

			List<String> submissionNos = ParamUtil.getListStrings(request,"submissionNo");
			ParamUtil.setSessionAttr(request,"submissionWithdrawalNos", (Serializable) submissionNos);
			IaisEGPHelper.redirectUrl(response,request, "MohArWithdrawal",InboxConst.URL_LICENCE_WEB_MODULE,params);
		} else if(DELETE_DRAFT.equalsIgnoreCase(actionValue)){
			actionInboxDataSubmissionQueryDtos.forEach(obj-> licenceInboxClient.deleteArSuperDataSubmissionDtoDraftByDraftNo(obj.getSubmissionNo()));
		}
	}

	private boolean checkDataPassBySubmissionNo(String submissionNo,String actionValue){
		if(StringUtil.isEmpty(submissionNo) || submissionNo.length() <10 ){
			return false;
		}
		if(actionValue.equals(DELETE_DRAFT)){
			String prefix = submissionNo.substring(0,2);
			return StringUtil.isNotEmpty(FeInboxHelper.SUBMISSIONNO_STATUS.get(prefix));
		}else{
			return true;
		}
	}


	private int checkDataPassBySubmissionNo(String submissionNo,String actionValue,InboxDataSubmissionQueryDto inboxDataSubmissionQueryDto){
		if(actionValue.equals(DELETE_DRAFT)){
			return checkDataPassBySubmissionNo(submissionNo, actionValue)?1:0;
		}else if(checkDataPassBySubmissionNo(submissionNo,DELETE_DRAFT)){
			switch (actionValue){
				case WITHDRAW: return 6;
				case AMENDED : return 7;
				case UNLOCK  : return 8;
				default: return 0;
			}
		} else if(actionValue.equals(WITHDRAW) ||actionValue.equals(AMENDED)){
			if(actionValue.equals(WITHDRAW)){
				if(inboxDataSubmissionQueryDto.getDsType().equals(DataSubmissionConsts.DS_AR)){
					ArSuperDataSubmissionDto arSuperDataSubmissionDto=licenceInboxClient.getArSuperDataSubmissionDto(submissionNo).getEntity();
					if(DsHelper.isCycleFinalStatus(arSuperDataSubmissionDto.getCycleDto().getStatus())&&arSuperDataSubmissionDto.getCycleDto().getDsType().equals(DataSubmissionConsts.DS_AR)){
						return 3;
					}
					List<DataSubmissionDto> dataSubmissionDtoList=licenceInboxClient.getAllDataSubmissionByCycleId(arSuperDataSubmissionDto.getDataSubmissionDto().getCycleId()).getEntity();
					List<ArSuperDataSubmissionDto> addWithdrawnDtoList= IaisCommonUtils.genNewArrayList();
					Map<String, ArSuperDataSubmissionDto> dataSubmissionDtoMap=IaisCommonUtils.genNewHashMap();
					for (DataSubmissionDto dataSubmissionDto:dataSubmissionDtoList
					) {
						if(!dataSubmissionDto.getSubmitDt().before(arSuperDataSubmissionDto.getDataSubmissionDto().getSubmitDt())){
							ArSuperDataSubmissionDto arSuperData = licenceInboxClient.getArSuperDataSubmissionDto(
									dataSubmissionDto.getSubmissionNo()).getEntity();
							dataSubmissionDtoMap.put(arSuperData.getDataSubmissionDto().getSubmissionNo(),arSuperData);
						}
					}
					for (Map.Entry<String, ArSuperDataSubmissionDto> dataSubmissionDtoEntry:dataSubmissionDtoMap.entrySet()
					) {
						addWithdrawnDtoList.add(dataSubmissionDtoEntry.getValue());
					}
					for (ArSuperDataSubmissionDto arWd:addWithdrawnDtoList
					) {
						if(DsHelper.isCycleFinalStatus(arWd.getCycleDto().getStatus())&&arWd.getCycleDto().getDsType().equals(DataSubmissionConsts.DS_AR)){
							return 3;
						}
						if(arWd.getDataSubmissionDto().getStatus().equals(DataSubmissionConsts.DS_STATUS_WITHDRAW)){
							addWithdrawnDtoList.remove(arWd);
						}
						if(arWd.getDataSubmissionDto().getStatus().equals(DataSubmissionConsts.DS_STATUS_LOCKED)){
							return 4;
						}
					}

					if("PATIENT".equals(arSuperDataSubmissionDto.getDataSubmissionDto().getCycleStage())){
						PatientDto patientDto=arSuperDataSubmissionDto.getPatientInfoDto().getPatient();
						List<CycleDto> cycleDtoList=licenceInboxClient.cycleByPatientCode(patientDto.getPatientCode()).getEntity();
						if(IaisCommonUtils.isNotEmpty(cycleDtoList)){
							return 2;
						}
					}
					if("DONOR".equals(arSuperDataSubmissionDto.getDataSubmissionDto().getCycleStage())){
						if(licenceInboxClient.hasDonorSampleUseCycleByDonorSampleId(arSuperDataSubmissionDto.getDonorSampleDto().getId()).getEntity()){
							return 2;
						}
					}
				}
			}

			//check x times,change check status is locked
			switch (inboxDataSubmissionQueryDto.getLockStatus()){
				case 1 :
				case 2 :
					return actionValue.equals(WITHDRAW)  ? 4 : 11;
				default: return 1;
			}
		}else if(actionValue.equals(UNLOCK)){
			// lockStatus == 0 : no need unlock,  1 : pass, 2 : pending unlock
			switch (inboxDataSubmissionQueryDto.getLockStatus()){
				case 0 : return 9;
				case 2 : return 10;
				default: return 1;
			}
		}
		return 0;
	}

}
