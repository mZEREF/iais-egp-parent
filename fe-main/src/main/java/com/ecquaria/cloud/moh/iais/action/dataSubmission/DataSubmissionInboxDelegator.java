package com.ecquaria.cloud.moh.iais.action.dataSubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.action.HalpAssessmentGuideDelegator;
import com.ecquaria.cloud.moh.iais.action.InterInboxDelegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxDataSubmissionQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.client.LicenceInboxClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

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
		setLog("doStart",false);
	}

	public void initData(HttpServletRequest request){
		clearSession(request);
		LoginContext loginContext = AccessUtil.getLoginUser(request);
		List<String> types = FeInboxHelper.getDsTypes(loginContext.getRoleIds());
		ParamUtil.setSessionAttr(request,DS_TYPES,(Serializable) types);
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
			if(StringUtil.isNotEmpty(interInboxUserDto.getLicenseeId())){
				SearchParam searchParam = HalpSearchResultHelper.gainSearchParam(request, InboxConst.DS_PARAM, InboxDataSubmissionQueryDto.class.getName(),"CREATED_DT",SearchParam.DESCENDING,false);
				HalpAssessmentGuideDelegator.setParamByField(searchParam,"licenseeId",interInboxUserDto.getLicenseeId(),true);
				HalpAssessmentGuideDelegator.setParamByField(searchParam,"dsType",(List<String>) ParamUtil.getSessionAttr(request,DS_TYPES));
				QueryHelp.setMainSql( InboxConst.INBOX_QUERY, InboxConst.INBOX_DS_QUERY,searchParam);
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

	private void setSearchParam(HttpServletRequest request){
		SearchParam searchParam = HalpSearchResultHelper.gainSearchParam(request, InboxConst.DS_PARAM, InboxDataSubmissionQueryDto.class.getName(),"CREATED_DT",SearchParam.DESCENDING,false);
		InboxDataSubmissionQueryDto inboxDataSubmissionQueryDto = ControllerHelper.get(request,InboxDataSubmissionQueryDto.class,"DataSubmission");
		HalpAssessmentGuideDelegator.setParamByField(searchParam,"submissionNo",inboxDataSubmissionQueryDto.getSubmissionNo(),true);
		HalpAssessmentGuideDelegator.setParamByField(searchParam,"status",inboxDataSubmissionQueryDto.getStatus(),true,InboxConst.SEARCH_ALL);
		HalpAssessmentGuideDelegator.setParamByField(searchParam,"type",inboxDataSubmissionQueryDto.getType() ,true,InboxConst.SEARCH_ALL);
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
		SearchParam searchParam = HalpSearchResultHelper.gainSearchParam(request, InboxConst.DS_PARAM, InboxDataSubmissionQueryDto.class.getName(),"CREATED_DT",SearchParam.DESCENDING,false);
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
		SearchParam searchParam = HalpSearchResultHelper.gainSearchParam(request, InboxConst.DS_PARAM, InboxDataSubmissionQueryDto.class.getName(),"CREATED_DT",SearchParam.DESCENDING,false);
		HalpSearchResultHelper.doSort(request,searchParam);
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
		  }else {
	      	if(DELETE_DRAFT.equalsIgnoreCase(actionValue)){
				ParamUtil.setRequestAttr(request,"deleteDraftOk",AppConsts.YES);
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
							  if(checkDataPassBySubmissionNo(submissionNo,actionValue)){
								  actionInboxDataSubmissionQueryDtos.add(inboxDataSubmissionQueryDto);
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
			}else if(WITHDRAW.equals(actionValue)){

			}else if(UNLOCK.equals(actionValue)){

			}
		}

		actionInboxDataSubmissionQueryDtos.forEach(obj->{
			if(DELETE_DRAFT.equalsIgnoreCase(actionValue)){
				licenceInboxClient.deleteArSuperDataSubmissionDtoDraftByDraftNo(obj.getSubmissionNo());
			}
		});
	}

	private static boolean checkDataPassBySubmissionNo(String submissionNo,String actionValue){
		if(StringUtil.isEmpty(submissionNo) || submissionNo.length() <10 ){
			return false;
		}
		String prefix = actionValue.equals(DELETE_DRAFT)? submissionNo.substring(0,2) : submissionNo.substring(0,3);
		return StringUtil.isNotEmpty(FeInboxHelper.SUBMISSIONNO_STATUS.get(prefix));
	}



}
