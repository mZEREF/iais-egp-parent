package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.risk.RiskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskFeSupportDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskFinanceMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskFinancialShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskService;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private BeEicGatewayClient beEicGatewayClient;
    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;
    @Override
    public RiskFinancialShowDto getfinancialShowDto(){
        List<HcsaServiceDto> serviceDtoList = hcsaConfigClient.getActiveServices().getEntity();
        RiskFinancialShowDto showDto  = hcsaConfigClient.getRiskFinShow(serviceDtoList).getEntity();
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
    public void saveDto(RiskFinancialShowDto dto) {
        List<HcsaRiskFinanceMatrixDto> dtoList = dto.getFinanceList();
        List<HcsaRiskFinanceMatrixDto> saveList = new ArrayList<>();
        List<HcsaRiskFinanceMatrixDto> updateList = new ArrayList<>();
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
        doUpdate(saveList,dtoList);
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        HcsaRiskFeSupportDto supportDto = new HcsaRiskFeSupportDto();
        supportDto.setFinancialShowDto(dto);
        supportDto.setFinFlag(true);
        beEicGatewayClient.feCreateRiskData(supportDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());

    }
    public void doSave(List<HcsaRiskFinanceMatrixDto> saveList){
        for(HcsaRiskFinanceMatrixDto temp:saveList){
            temp.setId(null);
        }
        hcsaConfigClient.saveFinRiskMatrix(saveList);
        //save
    }

    public void doUpdate(List<HcsaRiskFinanceMatrixDto> updateList,List<HcsaRiskFinanceMatrixDto> dtoList){
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
        hcsaConfigClient.saveFinRiskMatrix(updateList);
    }

    public List<HcsaRiskFinanceMatrixDto> getLastversionList(HcsaRiskFinanceMatrixDto temp){
        List<HcsaRiskFinanceMatrixDto> lastversionList= hcsaConfigClient.getFinianceRiskBySvcCode(temp.getServiceCode()).getEntity();
        List<HcsaRiskFinanceMatrixDto> returnList = new ArrayList<>();
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
            e.printStackTrace();
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
                e.printStackTrace();
            }
            finDto.setEffectiveDate(effDate);
            finDto.setEndDate(endDate);
            if(isLow){
                finDto.setFinSource(RiskConsts.INSTITUTION);
                finDto.setThershold(Integer.parseInt(dto.getInThershold()));
                finDto.setCaseCountTh(Integer.parseInt(dto.getInLeftModCaseCounth()));
                finDto.setRiskRating(RiskConsts.LOW);
            }else{
                finDto.setFinSource(RiskConsts.INSTITUTION);
                finDto.setThershold(Integer.parseInt(dto.getInThershold()));
                finDto.setCaseCountTh(Integer.parseInt(dto.getInRightModCaseCounth()));
                finDto.setRiskRating(RiskConsts.HIGH);
            }
        }else{
            try {
                effDate = Formatter.parseDate(dto.getPrEffectiveStartDate());
                endDate = Formatter.parseDate(dto.getPrEffectiveEndDate());
            }catch (Exception e){
                e.printStackTrace();
            }
            finDto.setEffectiveDate(effDate);
            finDto.setEndDate(endDate);
            if(isLow){
                finDto.setFinSource(RiskConsts.PRACTITIONER);
                finDto.setThershold(Integer.parseInt(dto.getPrThershold()));
                finDto.setCaseCountTh(Integer.parseInt(dto.getPrLeftModCaseCounth()));
                finDto.setRiskRating(RiskConsts.LOW);
            }else{
                finDto.setFinSource(RiskConsts.PRACTITIONER);
                finDto.setThershold(Integer.parseInt(dto.getPrThershold()));
                finDto.setCaseCountTh(Integer.parseInt(dto.getPrRightModCaseCounth()));
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
                if(!fin.getBaseInEffectiveStartDate().equals(inStartDate)){
                    isInEditNum++;
                }
                fin.setInEffectiveStartDate(inStartDate);
                if(!fin.getBaseInEffectiveEndDate().equals(inEndDate)){
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
                if(!fin.getBasePrEffectiveStartDate().equals(prStartDate)){
                    prInEditNum++;
                }
                fin.setPrEffectiveStartDate(prStartDate);
                if(!fin.getBasePrEffectiveEndDate().equals(prEndDate)){
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
            num = Integer.parseInt(lowCaseCount);
            num = num-1;
        } catch (Exception e) {
            return "";
        }
        return num+"";
    }
    public String getLeftHighCase(String highCaseCount){
        Integer num = 0;
        try {
            num = Integer.parseInt(highCaseCount);
            num = num+1;
        } catch (Exception e) {
            return "";
        }
        return num+"";
    }
}
