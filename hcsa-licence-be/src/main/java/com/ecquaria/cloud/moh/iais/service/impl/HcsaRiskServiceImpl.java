package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.risk.RiskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskFeSupportDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskFinanceMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskFinancialShowDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskService;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskSupportBeService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import java.util.Date;
import java.util.List;

import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: jiahao
 * @Date: 2019/11/13 16:42
 */
@Service
@Slf4j
public class HcsaRiskServiceImpl implements HcsaRiskService {
    @Autowired
    HcsaConfigClient hcsaConfigClient;
    @Autowired
    private HcsaRiskSupportBeService hcsaRiskSupportBeService;
    @Override
    public RiskFinancialShowDto getfinancialShowDto(){
        RiskFinancialShowDto showDto  = hcsaConfigClient.getRiskFinShow(hcsaRiskSupportBeService.getNameSortHcsaServiceDtos()).getEntity();
       for(HcsaRiskFinanceMatrixDto temp:showDto.getFinanceList()){
            if(!StringUtil.isEmpty(temp.getId())){
                temp.setInEffectiveEndDate(temp.getBaseInEffectiveEndDate());
                temp.setInEffectiveStartDate(temp.getBaseInEffectiveStartDate());
                temp.setPrEffectiveEndDate(temp.getBasePrEffectiveEndDate());
                temp.setPrEffectiveStartDate(temp.getBasePrEffectiveStartDate());
            }
        }
        return showDto;
    }

    @Override
    public boolean compareVersionsForRiskFinancial(RiskFinancialShowDto needSaveDto, RiskFinancialShowDto dbSearchDto) {
        if(needSaveDto == null || IaisCommonUtils.isEmpty(needSaveDto.getFinanceList())){
            return false;
        }else {
            if( dbSearchDto == null ||  IaisCommonUtils.isEmpty(dbSearchDto.getFinanceList())){
                return true;
            }
            List<HcsaRiskFinanceMatrixDto> financeList =  needSaveDto.getFinanceList();
            List<HcsaRiskFinanceMatrixDto> financeListDb =  dbSearchDto.getFinanceList();
            for(HcsaRiskFinanceMatrixDto hcsaRiskFinanceMatrixDto : financeList){
                for(HcsaRiskFinanceMatrixDto financeMatrixDtoDb : financeListDb){
                    if(hcsaRiskFinanceMatrixDto.getServiceCode().equalsIgnoreCase(financeMatrixDtoDb.getServiceCode())){
                        if(!hcsaRiskSupportBeService.versionSameForRisk(hcsaRiskFinanceMatrixDto.getVersion() ,financeMatrixDtoDb.getVersion())){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void saveDto(RiskFinancialShowDto dto) {
        List<HcsaRiskFinanceMatrixDto> dtoList = dto.getFinanceList();
        List<HcsaRiskFinanceMatrixDto> saveList = IaisCommonUtils.genNewArrayList();
        List<HcsaRiskFinanceMatrixDto> updateList = IaisCommonUtils.genNewArrayList();
        for(HcsaRiskFinanceMatrixDto temp : dtoList){
            if(temp.isInIsEdit()||temp.isPrIsEdit()){
                //in
                saveList.add(getFinDto(temp,true,true));
                saveList.add(getFinDto(temp,false,true));
                //pr
                saveList.add(getFinDto(temp,true,false));
                saveList.add(getFinDto(temp,false,false));
            }
        }
        FeignResponseEntity<List<HcsaRiskFinanceMatrixDto>> responseEntity = doUpdate(saveList,dtoList);
        HcsaRiskFeSupportDto supportDto = new HcsaRiskFeSupportDto();
        supportDto.setFinancialShowDto(dto);
        supportDto.setFinFlag(true);
        supportDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        hcsaRiskSupportBeService.sysnRiskSaveEic(responseEntity.getStatusCode(),supportDto);
    }
    public void doSave(List<HcsaRiskFinanceMatrixDto> saveList){
        for(HcsaRiskFinanceMatrixDto temp:saveList){
            temp.setId(null);
        }
        hcsaConfigClient.saveFinRiskMatrix(saveList);
        //save
    }

    public  FeignResponseEntity<List<HcsaRiskFinanceMatrixDto>> doUpdate(List<HcsaRiskFinanceMatrixDto> updateList,List<HcsaRiskFinanceMatrixDto> dtoList){
        //get last version form db
        for(HcsaRiskFinanceMatrixDto temp:dtoList){
            //List<HcsaRiskFinanceMatrixDto> lastversionList= hcsaConfigClient.getFinianceRiskBySvcCode(temp.getServiceCode()).getEntity();
            List<HcsaRiskFinanceMatrixDto> lastversionList = getLastversionList(temp);
            if(lastversionList!=null && !lastversionList.isEmpty()){
                for(HcsaRiskFinanceMatrixDto lastversion:lastversionList){
                    //panduan
                    if(RiskConsts.INSTITUTION.equals(lastversion.getFinSource())&&lastversion.isInIsEdit()){
                        updateLastVersion(temp,lastversion);
                    }else if(RiskConsts.PRACTITIONER.equals(lastversion.getFinSource())&&lastversion.isPrIsEdit()){
                        updateLastVersion(temp,lastversion);
                    }
                }
                hcsaConfigClient.updateFinRiskMatrix(lastversionList);
            }
        }
        for(HcsaRiskFinanceMatrixDto temp:updateList){
            temp.setId(null);
        }
       return hcsaConfigClient.saveFinRiskMatrix(updateList);
    }

    public List<HcsaRiskFinanceMatrixDto> getLastversionList(HcsaRiskFinanceMatrixDto temp){
        List<HcsaRiskFinanceMatrixDto> lastversionList= hcsaConfigClient.getFinianceRiskBySvcCode(temp.getServiceCode()).getEntity();
        List<HcsaRiskFinanceMatrixDto> returnList = IaisCommonUtils.genNewArrayList();
        if(lastversionList!=null && !lastversionList.isEmpty()){
            for(HcsaRiskFinanceMatrixDto fin:lastversionList){

                if(RiskConsts.INSTITUTION.equals(fin.getFinSource())&&temp.isInIsEdit()){
                    fin.setInIsEdit(true);
                }else if(RiskConsts.PRACTITIONER.equals(fin.getFinSource())&&temp.isPrIsEdit()){
                    fin.setPrIsEdit(true);
                }
                returnList.add(fin);

            }
        }
        return returnList;
    }
    public void updateLastVersion(HcsaRiskFinanceMatrixDto newFin,HcsaRiskFinanceMatrixDto dbFin){
        try {
            if("SOURCE001".equals(dbFin.getFinSource())){
                if("CRRR003".equals(dbFin.getRiskRating())){
                    dbFin.setEndDate(Formatter.parseDate(newFin.getInEffectiveStartDate()));
                    if(dbFin.getEndDate().getTime()<System.currentTimeMillis()){
                        dbFin.setStatus("CMSTAT003");
                    }
                }else if("CRRR001".equals(dbFin.getRiskRating())){
                    dbFin.setEndDate(Formatter.parseDate(newFin.getInEffectiveStartDate()));
                    if(dbFin.getEndDate().getTime()<System.currentTimeMillis()){
                        dbFin.setStatus("CMSTAT003");
                    }
                }
            }else if("SOURCE002".equals(dbFin.getFinSource())){
                if("CRRR003".equals(dbFin.getRiskRating())){
                    dbFin.setEndDate(Formatter.parseDate(newFin.getPrEffectiveStartDate()));
                    if(dbFin.getEndDate().getTime()<System.currentTimeMillis()){
                        dbFin.setStatus("CMSTAT003");
                    }
                }else if("CRRR001".equals(dbFin.getRiskRating())){
                    dbFin.setEndDate(Formatter.parseDate(newFin.getPrEffectiveStartDate()));
                    if(dbFin.getEndDate().getTime()<System.currentTimeMillis()){
                        dbFin.setStatus("CMSTAT003");
                    }
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }

    }

    public boolean isNeedUpdatePreviouds(HcsaRiskFinanceMatrixDto dto,boolean isIn){
        try {
            if(isIn){
                Date inEffDate = Formatter.parseDate(dto.getInEffectiveStartDate());
                Date inEndDate = Formatter.parseDate(dto.getInEffectiveEndDate());
                Date BaseInEffDate = Formatter.parseDate(dto.getBaseInEffectiveStartDate());
                Date BaseInEndDate = Formatter.parseDate(dto.getBaseInEffectiveEndDate());
                if(inEndDate.getTime()<BaseInEndDate.getTime()){
                    dto.setBaseInEffectiveEndDate(dto.getInEffectiveStartDate());
                    return true;
                }else{
                    return false;
                }
            }else{
                Date prEffDate = Formatter.parseDate(dto.getPrEffectiveStartDate());
                Date prEndDate = Formatter.parseDate(dto.getPrEffectiveEndDate());
                Date BasePrEffDate = Formatter.parseDate(dto.getBasePrEffectiveStartDate());
                Date BasePrEndDate = Formatter.parseDate(dto.getBasePrEffectiveEndDate());
                if(prEndDate.getTime()<BasePrEndDate.getTime()){
                    dto.setBasePrEffectiveEndDate(dto.getPrEffectiveStartDate());
                    return true;
                }else{
                    return false;
                }
            }


        }catch (Exception e){

            return false;
        }
    }
    public HcsaRiskFinanceMatrixDto getFinDto(HcsaRiskFinanceMatrixDto dto,boolean isLow,boolean isIn){
        HcsaRiskFinanceMatrixDto finDto = new HcsaRiskFinanceMatrixDto();
        finDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        finDto.setServiceCode(dto.getServiceCode());
        finDto.setInIsEdit(dto.isInIsEdit());
        finDto.setPrIsEdit(dto.isPrIsEdit());
        finDto.setStatus("CMSTAT001");
        if(StringUtil.isEmpty(dto.getVersion())){
            finDto.setVersion(1);
        }else{
            finDto.setVersion(dto.getVersion()+1);
        }
        Date effDate = null;
        Date endDate = null;
        if(isIn){
            try {
                effDate = Formatter.parseDate(dto.getInEffectiveStartDate());
                endDate = Formatter.parseDate(dto.getInEffectiveEndDate());
            }catch (Exception e){
                log.error(e.getMessage(), e);
            }
            finDto.setEffectiveDate(effDate);
            finDto.setEndDate(endDate);
            if(isLow){
                finDto.setFinSource(RiskConsts.INSTITUTION);
                finDto.setThershold(Integer.valueOf(dto.getInThershold()));
                finDto.setCaseCountTh(Integer.valueOf(dto.getInLeftModCaseCounth()));
                finDto.setRiskRating(RiskConsts.LOW);
            }else{
                finDto.setFinSource(RiskConsts.INSTITUTION);
                finDto.setThershold(Integer.valueOf(dto.getInThershold()));
                finDto.setCaseCountTh(Integer.valueOf(dto.getInRightModCaseCounth()));
                finDto.setRiskRating(RiskConsts.HIGH);
            }
        }else{
            try {
                effDate = Formatter.parseDate(dto.getPrEffectiveStartDate());
                endDate = Formatter.parseDate(dto.getPrEffectiveEndDate());
            }catch (Exception e){
                log.error(e.getMessage(), e);
            }
            finDto.setEffectiveDate(effDate);
            finDto.setEndDate(endDate);
            if(isLow){
                finDto.setFinSource(RiskConsts.PRACTITIONER);
                finDto.setThershold(Integer.valueOf(dto.getPrThershold()));
                finDto.setCaseCountTh(Integer.valueOf(dto.getPrLeftModCaseCounth()));
                finDto.setRiskRating(RiskConsts.LOW);
            }else{
                finDto.setFinSource(RiskConsts.PRACTITIONER);
                finDto.setThershold(Integer.valueOf(dto.getPrThershold()));
                finDto.setCaseCountTh(Integer.valueOf(dto.getPrRightModCaseCounth()));
                finDto.setRiskRating(RiskConsts.HIGH);
            }
        }
        return finDto;
    }
    @Override
    public void getOneFinDto(HcsaRiskFinanceMatrixDto fin,String prsource,String prthershold,
                             String prleftmod,String prlefthigh,String prrightlow,String prrightmod,
                             String insource,String inthershold,String inleftmod,String inlefthigh,String inrightlow,String inrightmod,
                             String inStartDate,String inEndDate,String prStartDate,String prEndDate){
        Integer isInEditNum = 0;
        Integer prInEditNum = 0;
        if(!StringUtil.isEmpty(fin.getFinSource())) {
            if ("SOURCE001".equals(fin.getInSource())) {
                if (!(fin.getBaseInThershold() + "").equals(inthershold)) {
                    isInEditNum++;
                }
                fin.setInThershold(inthershold);
                String baseRightLowCase = getRightLowCase(fin.getBaseInLowCaseCountth());
                if (!baseRightLowCase.equals(inrightlow)) {
                    isInEditNum++;
                }
                fin.setInRightLowCaseCounth(inrightlow);

                if (!fin.getBaseInLowCaseCountth().equals(inleftmod)) {
                    isInEditNum++;
                }
                fin.setInLeftModCaseCounth(inleftmod);
                if (!fin.getBaseInHighCaseCountth().equals(inrightmod)) {
                    isInEditNum++;
                }
                fin.setInRightModCaseCounth(inrightmod);
                String baseLeftHighCase = getLeftHighCase(fin.getBaseInHighCaseCountth());
                if (!baseLeftHighCase.equals(inlefthigh)) {
                    isInEditNum++;
                }
                fin.setInLeftHighCaseCount(inlefthigh);
                if(!fin.getBaseInEffectiveStartDate().equals(inStartDate) && !(StringUtil.isEmpty(fin.getBaseInEffectiveStartDate()) && StringUtil.isEmpty(inStartDate))){
                    isInEditNum++;
                }
                fin.setInEffectiveStartDate(inStartDate);
                if(!fin.getBaseInEffectiveEndDate().equals(inEndDate)&& !(StringUtil.isEmpty(fin.getBaseInEffectiveEndDate()) && StringUtil.isEmpty(inEndDate))){
                    isInEditNum++;
                }
                fin.setInEffectiveEndDate(inEndDate);
                if (isInEditNum >=1) {
                    fin.setInIsEdit(true);
                } else {
                    fin.setInIsEdit(false);
                }
            }
            if ("SOURCE002".equals(fin.getPrSource())) {
                if (!(fin.getBasePrThershold() + "").equals(prthershold)) {
                    prInEditNum++;
                }
                fin.setPrThershold(prthershold);
                String baseRightLowCase = getRightLowCase(fin.getBasePrLowCaseCountth());
                if (!baseRightLowCase.equals(prrightlow)) {
                    prInEditNum++;
                }
                fin.setPrRightLowCaseCounth(prrightlow);
                if (!fin.getBasePrLowCaseCountth().equals(prleftmod)) {
                    prInEditNum++;
                }
                fin.setPrLeftModCaseCounth(prleftmod);
                if (!fin.getBasePrHighCaseCountth().equals(prrightmod)) {
                    prInEditNum++;
                }
                fin.setPrRightModCaseCounth(prrightmod);
                String baseLeftHighCase = getLeftHighCase(fin.getBasePrHighCaseCountth());
                if (!baseLeftHighCase.equals(prlefthigh)) {
                    prInEditNum++;
                }
                fin.setPrLeftHighCaseCount(prlefthigh);
                if(!fin.getBasePrEffectiveStartDate().equals(prStartDate) && !(StringUtil.isEmpty(fin.getBasePrEffectiveStartDate()) && StringUtil.isEmpty(prStartDate))){
                    prInEditNum++;
                }
                fin.setPrEffectiveStartDate(prStartDate);
                if(!fin.getBasePrEffectiveEndDate().equals(prEndDate) && !(StringUtil.isEmpty(fin.getBasePrEffectiveEndDate()) && StringUtil.isEmpty(prEndDate))){
                    prInEditNum++;
                }
                fin.setPrEffectiveEndDate(prEndDate);
                if (prInEditNum >= 1) {
                    fin.setPrIsEdit(true);
                } else {
                    fin.setPrIsEdit(false);
                }
            }
        }else {
            boolean isInEdit = isNoBaseSourceIsEdit(fin, insource, inthershold, inrightlow, inleftmod, inlefthigh, inrightmod,inStartDate,inEndDate);
            if(isInEdit){
                fin.setInIsEdit(isInEdit);
                fin.setInSource(insource);
                fin.setInThershold(inthershold);
                fin.setInRightLowCaseCounth(inrightlow);
                fin.setInLeftModCaseCounth(inleftmod);
                fin.setInLeftHighCaseCount(inlefthigh);
                fin.setInRightModCaseCounth(inrightmod);
                fin.setInEffectiveStartDate(inStartDate);
                fin.setInEffectiveEndDate(inEndDate);
            }

            boolean isPrEdit = isNoBaseSourceIsEdit(fin, prsource, prthershold, prrightlow, prleftmod, prlefthigh, prrightmod,prStartDate,prEndDate);
            if(isPrEdit){
                fin.setPrIsEdit(isPrEdit);
                fin.setPrSource(prsource);
                fin.setPrThershold(prthershold);
                fin.setPrRightLowCaseCounth(prrightlow);
                fin.setPrLeftModCaseCounth(prleftmod);
                fin.setPrLeftHighCaseCount(prlefthigh);
                fin.setPrRightModCaseCounth(prrightmod);
                fin.setPrEffectiveStartDate(prStartDate);
                fin.setPrEffectiveEndDate(prEndDate);
            }
        }

    }

    public boolean isNoBaseSourceIsEdit(HcsaRiskFinanceMatrixDto fin,String source,String thershold,String rightlow,String leftmod,String lefthigh,String rightmod,String StartDate,String endDate){
        if(StringUtil.isEmpty(thershold)&&StringUtil.isEmpty(rightlow)&&StringUtil.isEmpty(leftmod)&&StringUtil.isEmpty(lefthigh)&&StringUtil.isEmpty(rightmod)) {
            return false;
        }
        return true;
    }
    public String getRightLowCase(String lowCaseCount){
        Integer num = 0;
        try {
            num = Integer.valueOf(lowCaseCount);
            num = num-1;
        } catch (Exception e) {
            return "";
        }
        return num+"";
    }
    public String getLeftHighCase(String highCaseCount){
        Integer num = 0;
        try {
            num = Integer.valueOf(highCaseCount);
            num = num+1;
        } catch (Exception e) {
            return "";
        }
        return num+"";
    }
}
