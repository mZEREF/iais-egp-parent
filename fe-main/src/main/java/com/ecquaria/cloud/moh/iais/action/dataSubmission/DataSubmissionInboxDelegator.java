package com.ecquaria.cloud.moh.iais.action.dataSubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.action.HalpAssessmentGuideDelegator;
import com.ecquaria.cloud.moh.iais.action.InterInboxDelegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxDataSubmissionQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.HalpSearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.service.client.LicenceInboxClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public final  static Map<String,String>  SUBMISSIONNO_STATUS = getSubmissionNoStatus();
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
	}

	public void clearSession(HttpServletRequest request){
		ParamUtil.setSessionAttr(request,InboxConst.DS_PARAM,null);
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
		interInboxDelegator.setNumInfoToRequest(request,(InterInboxUserDto) ParamUtil.getSessionAttr(request,InboxConst.INTER_INBOX_USER_INFO));
		if(StringUtil.isEmpty(ParamUtil.getString(request,ACTION_DS_BUTTON_SHOW))){
			SearchParam searchParam = HalpSearchResultHelper.gainSearchParam(request, InboxConst.DS_PARAM, InboxDataSubmissionQueryDto.class.getName(),"CREATED_DT",SearchParam.DESCENDING,false);
			QueryHelp.setMainSql( InboxConst.INBOX_QUERY,  InboxConst.INBOX_DS_QUERY,searchParam);
			ParamUtil.setSessionAttr(request, InboxConst.DS_RESULT,licenceInboxClient.searchLicence(searchParam).getEntity());
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
		toShowMessage(request,DELETE_DRAFT);
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
		if(StringUtil.isNotEmpty(getStatusBySubmissionNo(submissionNo,DELETE_DRAFT))){
			params.put("dsType",inboxDataSubmissionQueryDto.getType());
			params.put("draftNo",submissionNo);
			IaisEGPHelper.redirectUrl(response,request.getServerName(), "MohDsDraft",InboxConst.URL_LICENCE_WEB_MODULE,params);
		}else {
			//todo
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
		toShowMessage(request,AMENDED);
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
		toShowMessage(request,WITHDRAW);
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
		toShowMessage(request,UNLOCK);
		setLog(UNLOCK,false);
	}

	 private void toShowMessage(HttpServletRequest request,String actionValue){
	      if(showMessage(request,actionValue)){
	      	ParamUtil.setRequestAttr(request,ACTION_DS_BUTTON_SHOW,AppConsts.YES);
	      	ParamUtil.setRequestAttr(request,NEED_VALIDATOR_SIZE,ParamUtil.getString(request,NEED_VALIDATOR_SIZE));
		  }
	 }
    private  boolean showMessage(HttpServletRequest request,String actionValue){
		 String sizeString = ParamUtil.getString(request,NEED_VALIDATOR_SIZE);
		 List<String> submissionNos = ParamUtil.getListStrings(request,"submissionNo");
		 int size = -1;
		 if(StringUtil.isEmpty(sizeString)){
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
					 for (String submissionNo : submissionNos) {
						 if (submissionNo.equalsIgnoreCase(inboxDataSubmissionQueryDto.getSubmissionNo())) {
							 inboxDataSubmissionQueryDto.setSubmissionSelect(true);
							  if(StringUtil.isNotEmpty(getStatusBySubmissionNo(submissionNo,actionValue))){
								  actionInboxDataSubmissionQueryDtos.add(inboxDataSubmissionQueryDto);
							  }
							 break;
						 }
					 }
				 });
				 actionDoInboxDataSubmissionQueryDtos(actionInboxDataSubmissionQueryDtos,actionValue,size);
				 return actionInboxDataSubmissionQueryDtos.size() != size;
			 }

		 }

		 return false;
	}


	private void actionDoInboxDataSubmissionQueryDtos(List<InboxDataSubmissionQueryDto> actionInboxDataSubmissionQueryDtos,String actionValue,int actionSize){
		if(actionSize != actionInboxDataSubmissionQueryDtos.size()){
			return;
		}
		actionInboxDataSubmissionQueryDtos.forEach(obj->{
			if(DELETE_DRAFT.equalsIgnoreCase(actionValue)){
				//todo delete data
				licenceInboxClient.deleteArSuperDataSubmissionDtoDraftByDraftNo(obj.getSubmissionNo());
			}else if (AMENDED.equals(actionValue)){

			}else if(WITHDRAW.equals(actionValue)){

			}else if(UNLOCK.equals(actionValue)){

			}
		});
	}

	private static String getStatusBySubmissionNo(String submissionNo,String actionValue){
		if(StringUtil.isEmpty(submissionNo) || submissionNo.length() <10 ){
			return "";
		}
		String prefix = actionValue.equals(DELETE_DRAFT)? submissionNo.substring(0,2) : submissionNo.substring(0,4);
		return SUBMISSIONNO_STATUS.get(prefix);
	}

	private  static Map<String,String> getSubmissionNoStatus(){
		Map<String,String> stringStringMap = IaisCommonUtils.genNewHashMap(7);
		//todo
		stringStringMap.put("DS",DataSubmissionConsts.DS_STATUS_DRAFT);
		return stringStringMap;
	}

}
