package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.risk.RiskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskFeSupportDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskFinanceMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskLeadershipMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskLeaderShipShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskLeaderShipService;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author: jiahao
 * @Date: 2019/12/23 13:53
 */
@Service
@Slf4j
public class HcsaRiskLeaderShipServiceImpl implements HcsaRiskLeaderShipService {
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
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
    public RiskLeaderShipShowDto getLeaderShowDto() {
        List<HcsaServiceDto> serviceDtoList = hcsaConfigClient.getActiveServices().getEntity();
        RiskLeaderShipShowDto showDto  = hcsaConfigClient.getRiskLeaderShipShow(serviceDtoList).getEntity();
        List<RiskAcceptiionDto> riskAcceptiionDtoList = IaisCommonUtils.genNewArrayList();
        RiskAcceptiionDto dto = new RiskAcceptiionDto();
        dto.setScvCode("CLB");
        riskAcceptiionDtoList.add(dto);
        List<RiskResultDto> riskList = hcsaConfigClient.getRiskResult(riskAcceptiionDtoList).getEntity();
        return showDto;
    }
    @Override
    public void getOneFinDto(HcsaRiskLeadershipMatrixDto lea, String prsource, String prthershold, String prleftmod, String prlefthigh, String prrightlow, String prrightmod, String insource, String inthershold, String inleftmod, String inlefthigh, String inrightlow, String inrightmod, String inStartDate, String inEndDate, String prStartDate, String prEndDate) {
        Integer isInEditNum = 0;
        Integer prInEditNum = 0;
        if(!StringUtil.isEmpty(lea.getLsSourse())) {
            if ("LGRAT001".equals(lea.getAdSource())) {
                if (!(lea.getBaseAdThershold() + "").equals(inthershold)) {
                    isInEditNum++;
                }
                lea.setAdThershold(inthershold);
                String baseRightLowCase = getRightLowCase(lea.getBaseAdLowCaseCounth());
                if (!baseRightLowCase.equals(inrightlow)) {
                    isInEditNum++;
                }
                lea.setAdRightLowCaseCounth(inrightlow);

                if (!lea.getBaseAdLowCaseCounth().equals(inleftmod)) {
                    isInEditNum++;
                }
                lea.setAdLeftModCaseCounth(inleftmod);
                if (!lea.getBaseAdHighCaseCounth().equals(inrightmod)) {
                    isInEditNum++;
                }
                lea.setAdRightModCaseCounth(inrightmod);
                String baseLeftHighCase = getLeftHighCase(lea.getBaseAdHighCaseCounth());
                if (!baseLeftHighCase.equals(inlefthigh)) {
                    isInEditNum++;
                }
                lea.setAdLeftHighCaseCounth(inlefthigh);
                if(!lea.getBaseAdEffectiveStartDate().equals(inStartDate) && !(StringUtil.isEmpty(lea.getBaseAdEffectiveStartDate()) && StringUtil.isEmpty(inStartDate))){
                    isInEditNum++;
                }
                lea.setAdEffectiveStartDate(inStartDate);
                if(!lea.getBaseAdEffectiveEndDate().equals(inEndDate) && !(StringUtil.isEmpty(lea.getBaseAdEffectiveEndDate()) && StringUtil.isEmpty(inEndDate))){
                    isInEditNum++;
                }
                lea.setAdEffectiveEndDate(inEndDate);
                if (isInEditNum >=1) {
                    lea.setAdIsEdit(true);
                } else {
                    lea.setAdIsEdit(false);
                }
            }
            if ("LGRAT002".equals(lea.getDpSource())) {
                if (!(lea.getBaseDpThershold() + "").equals(prthershold)) {
                    prInEditNum++;
                }
                lea.setDpThershold(prthershold);
                String baseRightLowCase = getRightLowCase(lea.getBaseDpLowCaseCounth());
                if (!baseRightLowCase.equals(prrightlow)) {
                    prInEditNum++;
                }
                lea.setDpRightLowCaseCounth(prrightlow);
                if (!lea.getBaseDpLowCaseCounth().equals(prleftmod)) {
                    prInEditNum++;
                }
                lea.setDpLeftModCaseCounth(prleftmod);
                if (!lea.getBaseDpHighCaseCounth().equals(prrightmod)) {
                    prInEditNum++;
                }
                lea.setDpRightModCaseCounth(prrightmod);
                String baseLeftHighCase = getLeftHighCase(lea.getBaseDpHighCaseCounth());
                if (!baseLeftHighCase.equals(prlefthigh)) {
                    prInEditNum++;
                }
                lea.setDpLeftHighCaseCounth(prlefthigh);
                if(!lea.getBaseDpEffectiveStartDate().equals(prStartDate) && !(StringUtil.isEmpty(lea.getBaseDpEffectiveStartDate()) && StringUtil.isEmpty(prStartDate))){
                    prInEditNum++;
                }
                lea.setDpEffectiveStartDate(prStartDate);
                if(!lea.getBaseDpEffectiveEndDate().equals(prEndDate)  && !(StringUtil.isEmpty(lea.getBaseDpEffectiveEndDate()) && StringUtil.isEmpty(prEndDate))){
                    prInEditNum++;
                }
                lea.setDpEffectiveEndDate(prEndDate);
                if (prInEditNum >= 1) {
                    lea.setDpIsEdit(true);
                } else {
                    lea.setDpIsEdit(false);
                }
            }
        }else {
            boolean isInEdit = isNoBaseSourceIsEdit(lea, insource, inthershold, inrightlow, inleftmod, inlefthigh, inrightmod,inStartDate,inEndDate);
            if(isInEdit){
                lea.setAdIsEdit(isInEdit);
                lea.setAdSource(insource);
                lea.setAdThershold(inthershold);
                lea.setAdRightLowCaseCounth(inrightlow);
                lea.setAdLeftModCaseCounth(inleftmod);
                lea.setAdLeftHighCaseCounth(inlefthigh);
                lea.setAdRightModCaseCounth(inrightmod);
                lea.setAdEffectiveStartDate(inStartDate);
                lea.setAdEffectiveEndDate(inEndDate);
            }

            boolean isPrEdit = isNoBaseSourceIsEdit(lea, prsource, prthershold, prrightlow, prleftmod, prlefthigh, prrightmod,prStartDate,prEndDate);
            if(isPrEdit){
                lea.setDpIsEdit(isPrEdit);
                lea.setDpSource(prsource);
                lea.setDpThershold(prthershold);
                lea.setDpRightLowCaseCounth(prrightlow);
                lea.setDpLeftModCaseCounth(prleftmod);
                lea.setDpLeftHighCaseCounth(prlefthigh);
                lea.setDpRightModCaseCounth(prrightmod);
                lea.setDpEffectiveStartDate(prStartDate);
                lea.setDpEffectiveEndDate(prEndDate);
            }
        }

    }

    public boolean isNoBaseSourceIsEdit(HcsaRiskLeadershipMatrixDto fin, String source, String thershold, String rightlow, String leftmod, String lefthigh, String rightmod, String StartDate, String endDate){
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

    @Override
    public void saveDto(RiskLeaderShipShowDto dto) {
        List<HcsaRiskLeadershipMatrixDto> dtoList = dto.getLeaderShipDtoList();
        List<HcsaRiskLeadershipMatrixDto> saveList = IaisCommonUtils.genNewArrayList();
        for(HcsaRiskLeadershipMatrixDto temp : dtoList){
            if(temp.isAdIsEdit()||temp.isDpIsEdit()){
                saveList.add(getFinDto(temp,true,true));
                saveList.add(getFinDto(temp,false,true));
                saveList.add(getFinDto(temp,true,false));
                saveList.add(getFinDto(temp,false,false));
            }
        }
        doUpdate(saveList,dtoList);
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        HcsaRiskFeSupportDto supportDto = new HcsaRiskFeSupportDto();
        supportDto.setRiskLeaderShipShowDto(dto);
        supportDto.setLeaderhsipFlag(true);
        supportDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        beEicGatewayClient.feCreateRiskData(supportDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
    }


    public void doUpdate(List<HcsaRiskLeadershipMatrixDto> updateList,List<HcsaRiskLeadershipMatrixDto> dtoList){
        //get last version form db
        for(HcsaRiskLeadershipMatrixDto temp:dtoList){
            //List<HcsaRiskFinanceMatrixDto> lastversionList= hcsaConfigClient.getFinianceRiskBySvcCode(temp.getServiceCode()).getEntity();
            List<HcsaRiskLeadershipMatrixDto> lastversionList = getLastversionList(temp);
            if(lastversionList!=null && !lastversionList.isEmpty()){
                for(HcsaRiskLeadershipMatrixDto lastversion:lastversionList){
                    if(RiskConsts.AUDIT.equals(lastversion.getLsSourse())&&lastversion.isAdIsEdit()){
                        updateLastVersion(temp,lastversion);
                    }else if(RiskConsts.DISCIPLINARY.equals(lastversion.getLsSourse())&&lastversion.isDpIsEdit()){
                        updateLastVersion(temp,lastversion);
                    }
                }
                hcsaConfigClient.updateLeadershipRiskMatrix(lastversionList);
            }
        }
        for(HcsaRiskLeadershipMatrixDto temp:updateList){
            temp.setId(null);
        }
        hcsaConfigClient.saveLeadershipRiskMatrix(updateList);
    }

    public List<HcsaRiskLeadershipMatrixDto> getLastversionList(HcsaRiskLeadershipMatrixDto temp){
        List<HcsaRiskLeadershipMatrixDto> lastversionList= hcsaConfigClient.getLeadershipRiskBySvcCode(temp.getSvcCode()).getEntity();
        List<HcsaRiskLeadershipMatrixDto> returnList = IaisCommonUtils.genNewArrayList();
        if(lastversionList!=null && !lastversionList.isEmpty()){
            for(HcsaRiskLeadershipMatrixDto fin:lastversionList){
                if(temp.isAdIsEdit()&&RiskConsts.AUDIT.equals(fin.getLsSourse())){
                    fin.setAdIsEdit(true);
                }
                if(temp.isDpIsEdit()&&RiskConsts.DISCIPLINARY.equals(fin.getLsSourse())){
                    fin.setDpIsEdit(true);
                }
                returnList.add(fin);
            }
        }
        return returnList;
    }
    public void updateLastVersion(HcsaRiskLeadershipMatrixDto newFin,HcsaRiskLeadershipMatrixDto dbFin){
        try {
            if(RiskConsts.AUDIT.equals(dbFin.getLsSourse())){
                if("CRRR003".equals(dbFin.getRiskRating())){
                    dbFin.setEndDate(Formatter.parseDate(newFin.getAdEffectiveStartDate()));
                    if(dbFin.getEndDate().getTime()<System.currentTimeMillis()){
                        dbFin.setStatus("CMSTAT003");
                    }
                }else if("CRRR001".equals(dbFin.getRiskRating())){
                    dbFin.setEndDate(Formatter.parseDate(newFin.getAdEffectiveStartDate()));
                    if(dbFin.getEndDate().getTime()<System.currentTimeMillis()){
                        dbFin.setStatus("CMSTAT003");
                    }
                }
            }else if(RiskConsts.DISCIPLINARY.equals(dbFin.getLsSourse())){
                if("CRRR003".equals(dbFin.getRiskRating())){
                    dbFin.setEndDate(Formatter.parseDate(newFin.getDpEffectiveStartDate()));
                    if(dbFin.getEndDate().getTime()<System.currentTimeMillis()){
                        dbFin.setStatus("CMSTAT003");
                    }
                }else if("CRRR001".equals(dbFin.getRiskRating())){
                    dbFin.setEndDate(Formatter.parseDate(newFin.getDpEffectiveStartDate()));
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
    public HcsaRiskLeadershipMatrixDto getFinDto(HcsaRiskLeadershipMatrixDto dto,boolean isLow,boolean isIn){
        HcsaRiskLeadershipMatrixDto finDto = new HcsaRiskLeadershipMatrixDto();
        finDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        finDto.setSvcCode(dto.getSvcCode());
        finDto.setStatus("CMSTAT001");
        finDto.setAdIsEdit(dto.isAdIsEdit());
        finDto.setDpIsEdit(dto.isDpIsEdit());
        if(StringUtil.isEmpty(dto.getVersion())){
            finDto.setVersion(1);
        }else{
            finDto.setVersion(dto.getVersion()+1);
        }
        Date effDate = null;
        Date endDate = null;
        if(isIn){
            try {
                effDate = Formatter.parseDate(dto.getAdEffectiveStartDate());
                endDate = Formatter.parseDate(dto.getAdEffectiveEndDate());
            }catch (Exception e){
                log.error(e.getMessage(), e);
            }
            finDto.setEffectiveDate(effDate);
            finDto.setEndDate(endDate);
            if(isLow){
                finDto.setLsSourse(RiskConsts.AUDIT);
                finDto.setThershold(Integer.parseInt(dto.getAdThershold()));
                finDto.setCaseCounth(Integer.parseInt(dto.getAdLeftModCaseCounth()));
                finDto.setRiskRating(RiskConsts.LOW);
            }else{
                finDto.setLsSourse(RiskConsts.AUDIT);
                finDto.setThershold(Integer.parseInt(dto.getAdThershold()));
                finDto.setCaseCounth(Integer.parseInt(dto.getAdRightModCaseCounth()));
                finDto.setRiskRating(RiskConsts.HIGH);
            }
        }else{
            try {
                effDate = Formatter.parseDate(dto.getDpEffectiveStartDate());
                endDate = Formatter.parseDate(dto.getDpEffectiveEndDate());
            }catch (Exception e){
                log.error(e.getMessage(), e);
            }
            finDto.setEffectiveDate(effDate);
            finDto.setEndDate(endDate);
            if(isLow){
                finDto.setLsSourse(RiskConsts.DISCIPLINARY);
                finDto.setThershold(Integer.parseInt(dto.getDpThershold()));
                finDto.setCaseCounth(Integer.parseInt(dto.getDpLeftModCaseCounth()));
                finDto.setRiskRating(RiskConsts.LOW);
            }else{
                finDto.setLsSourse(RiskConsts.DISCIPLINARY);
                finDto.setThershold(Integer.parseInt(dto.getDpThershold()));
                finDto.setCaseCounth(Integer.parseInt(dto.getDpRightModCaseCounth()));
                finDto.setRiskRating(RiskConsts.HIGH);
            }
        }
        return finDto;
    }
}
