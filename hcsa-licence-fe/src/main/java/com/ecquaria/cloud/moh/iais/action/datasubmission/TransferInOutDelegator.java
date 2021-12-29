package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TransferInOutStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Delegator("transferInOutDelegator")
@Slf4j
public class TransferInOutDelegator extends CommonDelegator {
    public static final String WHAT_WAS_TRANSFERREDs = "transferreds";
    @Override
    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,WHAT_WAS_TRANSFERREDs, (Serializable) MasterCodeUtil.retrieveByCategory(MasterCodeUtil.WHAT_WAS_TRANSFERRED));

    }

    @Override
    public void preparePage(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);
    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        TransferInOutStageDto transferInOutStageDto = arSuperDataSubmissionDto.getTransferInOutStageDto();
        PatientInventoryDto patientInventoryDto = DataSubmissionHelper.getCurrentPatientInventory(bpc.request);
        List<String> transferredList = transferInOutStageDto.getTransferredList();
        for (String transferred : transferredList){
            if(transferred.equals(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_OOCYTES)){
                if(transferInOutStageDto.getOocyteNum() !=null){
                    patientInventoryDto.setChangeFrozenOocytes(-1*transferInOutStageDto.getOocyteNum());
                }
            }
            if(transferred.equals(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_EMBRYOS)){
                if(transferInOutStageDto.getEmbryoNum() !=null){
                    patientInventoryDto.setChangeFrozenEmbryos(-1*transferInOutStageDto.getEmbryoNum());
                }
            }
            if(transferred.equals(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_SPERM)){
                if(transferInOutStageDto.getSpermVialsNum() !=null){
                    patientInventoryDto.setChangeFrozenSperms(-1*transferInOutStageDto.getSpermVialsNum());
                }
            }
        }

    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Cycle Stages</strong>");
        HttpServletRequest request = bpc.request;
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);
    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        arSuperDataSubmissionDto = arSuperDataSubmissionDto  == null ? new ArSuperDataSubmissionDto() : arSuperDataSubmissionDto;
        TransferInOutStageDto transferInOutStageDto = arSuperDataSubmissionDto.getTransferInOutStageDto() == null ? new TransferInOutStageDto() : arSuperDataSubmissionDto.getTransferInOutStageDto();
        String[] transferredList = ParamUtil.getStrings(request,"transferredList");
        String oocyteNo =  ParamUtil.getString(request,"oocyteNum");
        String embryoNo =  ParamUtil.getString(request,"embryoNum");
        String spermVialsNo =  ParamUtil.getString(request,"spermVialsNum");
        ControllerHelper.get(request,transferInOutStageDto);
        transferInOutStageDto.setOocyteNo(oocyteNo);
        transferInOutStageDto.setEmbryoNo(embryoNo);
        transferInOutStageDto.setSpermVialsNo(spermVialsNo);
        String fromDonor = ParamUtil.getString(request,"fromDonor");
        transferInOutStageDto.setFromDonor("true".equalsIgnoreCase(fromDonor));
        if( !IaisCommonUtils.isEmpty(transferredList)){
            transferInOutStageDto.setTransferredList(Arrays.asList(transferredList));
        }else{
            transferInOutStageDto.setTransferredList(null);
        }
        arSuperDataSubmissionDto.setTransferInOutStageDto(transferInOutStageDto);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);
        validatePageData(request, transferInOutStageDto,"save",ACTION_TYPE_CONFIRM);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);
    }
}
