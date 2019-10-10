package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.AppGrpPremisesDocDto;
import com.ecquaria.cloud.moh.iais.dto.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.AppGrpPremisesService;
import com.ecquaria.cloud.moh.iais.tags.SelectOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sop.iwe.SessionManager;
import sop.rbac.user.User;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * NewApplicationDelegator
 *
 * @author suocheng
 * @date 9/23/2019
 */
@Delegator("newApplicationDelegator")
@Slf4j
public class NewApplicationDelegator {
    private static final String APPGRPPREMISESDTO = "appGrpPremisesDto";
    private static final String APPGRPPREMISESDOCDTO = "appGrpPremisesDocDto";

    @Autowired
    private AppGrpPremisesService appGrpPremisesService;

    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do Start start ...."));
        ParamUtil.setSessionAttr(bpc.request,APPGRPPREMISESDTO,null);
        ParamUtil.setSessionAttr(bpc.request,APPGRPPREMISESDOCDTO,null);
        AuditTrailHelper.auditFunction("iais-cc", "premises create");
        //for loading the draft by appId
        String appId = "C120481C-09C3-45A8-A8ED-F71B38BD1768";
        List appGrpPremisesDtoList = appGrpPremisesService.getAppGrpPremisesDtosByAppId(appId);
        if(appGrpPremisesDtoList != null && appGrpPremisesDtoList.size()>0){
            List<AppGrpPremisesDto> appGrpPremisesDtoList1 = RestApiUtil.transferListContent(appGrpPremisesDtoList,AppGrpPremisesDto.class);
            AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtoList1.get(0);
            ParamUtil.setSessionAttr(bpc.request,APPGRPPREMISESDTO,appGrpPremisesDto);
        }
        log.debug(StringUtil.changeForLog("the do Start end ...."));
    }

    /**
     * StartStep: Prepare
     *
     * @param bpc
     * @throws
     */
    public void prepare(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prepare start ...."));
        String action = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
        if(StringUtil.isEmpty(action)){
            action = (String)ParamUtil.getRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
            if(StringUtil.isEmpty(action)){
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE,"premises");
            }
        }
        log.debug(StringUtil.changeForLog("the do prepare end ...."));
    }
    /**
     * StartStep: PreparePremises
     *
     * @param bpc
     * @throws
     */
    public void preparePremises(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do preparePremises start ...."));
        List premisesSelect = new ArrayList<SelectOption>();
        User user = SessionManager.getInstance(bpc.request).getCurrentUser();
        //String loginId = user.getIdentityNo();
        String loginId="internet";
        //?
        List<AppGrpPremisesDto> list = appGrpPremisesService.getAppGrpPremisesDtoByLoginId(loginId);
        SelectOption sp0 = new SelectOption("-1","Select One");
        premisesSelect.add(sp0);
        SelectOption sp1 = new SelectOption("newPremise","Add a new premises");
        premisesSelect.add(sp1);
        if(list !=null){
            for (AppGrpPremisesDto item : list){
              if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(item.getPremisesType())){
                  SelectOption sp2 = new SelectOption(item.getId().toString(),item.getAddress());
                  premisesSelect.add(sp2);
              }
            }
        }
        ParamUtil.setRequestAttr(bpc.request,"premisesSelect",premisesSelect);
        log.debug(StringUtil.changeForLog("the do preparePremises end ...."));
    }
    /**
     * StartStep: PrepareDocuments
     *
     * @param bpc
     * @throws
     */
    public void prepareDocuments(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prepareDocuments start ...."));

        log.debug(StringUtil.changeForLog("the do prepareDocuments end ...."));
    }
    /**
     * StartStep: PrepareForms
     *
     * @param bpc
     * @throws
     */
    public void prepareForms(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prepareForms start ...."));

        log.debug(StringUtil.changeForLog("the do prepareForms end ...."));
    }
    /**
     * StartStep: PreparePreview
     *
     * @param bpc
     * @throws
     */
    public void preparePreview(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do preparePreview start ...."));

        log.debug(StringUtil.changeForLog("the do preparePreview end ...."));
    }
    /**
     * StartStep: PreparePayment
     *
     * @param bpc
     * @throws
     */
    public void preparePayment(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do preparePayment start ...."));

        log.debug(StringUtil.changeForLog("the do preparePayment end ...."));
    }
    /**
     * StartStep: DoPremises
     *
     * @param bpc
     * @throws
     */
    public void doPremises(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doPremises start ...."));
        AppGrpPremisesDto appGrpPremisesDto =
                ParamUtil.getSessionAttr(bpc.request,APPGRPPREMISESDTO) == null ? new AppGrpPremisesDto():
                        (AppGrpPremisesDto)ParamUtil.getSessionAttr(bpc.request,APPGRPPREMISESDTO);
         String appGrpId = appGrpPremisesDto.getAppGrpId();
         appGrpPremisesDto = (AppGrpPremisesDto)MiscUtil.generateDtoFromParam(bpc.request,appGrpPremisesDto);
        appGrpPremisesDto.setAppGrpId(appGrpId);
        ParamUtil.setSessionAttr(bpc.request,APPGRPPREMISESDTO,appGrpPremisesDto);
        log.debug(StringUtil.changeForLog("the do doPremises end ...."));
    }
    /**
     * StartStep: DoDocument
     *
     * @param bpc
     * @throws
     */
    public void doDocument(BaseProcessClass bpc) throws IOException {
        log.debug(StringUtil.changeForLog("the do doDocument start ...."));
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String crud_action_type =  mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);

        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE,crud_action_type);

        AppGrpPremisesDocDto appGrpPremisesDocDto = new AppGrpPremisesDocDto();
        MultipartFile file = null;
        for (Iterator<String> en = mulReq.getFileNames(); en.hasNext(); ) {
            String name = en.next();
            file = mulReq.getFile(name);
        }

        if(file != null && !StringUtil.isEmpty(file.getOriginalFilename())){
            appGrpPremisesDocDto.setFileName(file.getOriginalFilename());
            appGrpPremisesDocDto.setFile(file);
        }
        ParamUtil.setSessionAttr(bpc.request,APPGRPPREMISESDOCDTO,appGrpPremisesDocDto);
        log.debug(StringUtil.changeForLog("the do doDocument end ...."));
    }
    /**
     * StartStep: doFormsSubmit
     *
     * @param bpc
     * @throws
     */
    public void doFormsSubmit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doFormsSubmit start ...."));

        log.debug(StringUtil.changeForLog("the do doFormsSubmit end ...."));
    }
    /**
     * StartStep: doPreview
     *
     * @param bpc
     * @throws
     */
    public void doPreview(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doPreview start ...."));

        log.debug(StringUtil.changeForLog("the do doPreview end ...."));
    }
    /**
     * StartStep: doPreview
     *
     * @param bpc
     * @throws
     */
    public void doPayment(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doPayment start ...."));

        log.debug(StringUtil.changeForLog("the do doPayment end ...."));
    }

    /**
     * StartStep: doSaveDraft
     *
     * @param bpc
     * @throws
     */
    public void doSaveDraft(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doSaveDraft start ...."));
        AppGrpPremisesDto appGrpPremisesDto = (AppGrpPremisesDto)ParamUtil.getSessionAttr(bpc.request,APPGRPPREMISESDTO);
        appGrpPremisesDto = appGrpPremisesService.saveAppGrpPremises(appGrpPremisesDto);
        ParamUtil.setSessionAttr(bpc.request,APPGRPPREMISESDTO,appGrpPremisesDto);
        log.debug(StringUtil.changeForLog("the do doSaveDraft end ...."));
    }
    /**
     * StartStep: ControlSwitch
     *
     * @param bpc
     * @throws
     */
    public void controlSwitch(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do controlSwitch start ...."));
        String switch2 = "loading";
        String crud_action_value = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE);
        if("saveDraft".equals(crud_action_value) || "ack".equals(crud_action_value)){
            switch2 = crud_action_value;
        }
        ParamUtil.setRequestAttr(bpc.request,"Switch2",switch2);
        log.debug(StringUtil.changeForLog("the do controlSwitch end ...."));

    }
    /**
     * StartStep: PrepareAckPage
     *
     * @param bpc
     * @throws
     */
    public void prepareAckPage(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prepareAckPage start ...."));

        log.debug(StringUtil.changeForLog("the do prepareAckPage end ...."));
    }

//    //=============================================================================
//    //private method
//    //=============================================================================
//    private void getValueFromPage(AppGrpPremisesDto appGrpPremisesDto, HttpServletRequest request) {
//        String premisesType = ParamUtil.getString(request,"premisesType");
//        String hciName = ParamUtil.getString(request,"hciName");
//        appGrpPremisesDto.setHciName(hciName);
//        MiscUtil.generateDtoFromParam(request,AppGrpPremisesDto.class);
//    }
}
