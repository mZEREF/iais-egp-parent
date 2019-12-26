package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.LicenceFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.HcsaLicenceGroupFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
import com.ecquaria.cloud.moh.iais.service.AutoRenwalService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Wenkang
 * @date 2019/12/26 10:08
 */
@Service
public class AutoRenwalServiceImpl implements AutoRenwalService {

    @Autowired
    private HcsaLicenceClient hcsaLicenClient;

    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private ApplicationClient applicationClient;
    @Override
    public void startRenwal() {
        List<Integer> dayList=new ArrayList<>();
        dayList.add(30);
        dayList.add(45);
        dayList.add(60);
        dayList.add(90);
        dayList.add(120);
        dayList.add(150);
        dayList.add(180);
        List<JobRemindMsgTrackingDto> JobRemindMsgTrackingDto = hcsaLicenClient.listJob().getEntity();
        Map<String, List<LicenceDto>> entity = hcsaLicenClient.licenceRenwal(dayList).getEntity();
        Set<Map.Entry<String, List<LicenceDto>>> entries = entity.entrySet();
        for(Map.Entry entry:entries){
            List<LicenceDto> licenceDtos = entity.get(entry);
            licenceToRemove(licenceDtos,JobRemindMsgTrackingDto);
            for(int i=0;i<licenceDtos.size();i++){
                String licenceNo = licenceDtos.get(i).getLicenceNo();



            }

        }

    }


    /****************/

    private boolean isAutoOrNon(){


        return false;
    }

    private void licenceToRemove(List<LicenceDto>  applicationDtos ,  List<JobRemindMsgTrackingDto> JobRemindMsgTrackingDto ){
        for(int i=0;i< applicationDtos.size();i++){
            String originLicenceId = applicationDtos.get(i).getOriginLicenceId();
            for(JobRemindMsgTrackingDto e:JobRemindMsgTrackingDto){
                String refNo = e.getRefNo();
                if(originLicenceId.equals(refNo)){
                    applicationDtos.remove(i);
                    i--;
                  break;
                }
            }
        }
    }



    private void  isNoAuto(LicenceDto licenceDto){

        String svcName = licenceDto.getSvcName();

        Date expiryDate = licenceDto.getExpiryDate();

        String id = licenceDto.getId();




    }

    private void isAuto(LicenceDto licenceDto ){
        String svcName = licenceDto.getSvcName();
        Date expiryDate = licenceDto.getExpiryDate();
        Double total=0.0;
        String id = licenceDto.getId();
        List<String> list=new ArrayList<>();
        List<LicenceFeeDto> licenceFeeDtos=new ArrayList<>();
        list.add(id);
        List<HcsaLicenceGroupFeeDto> entity = hcsaLicenClient.retrieveHcsaLicenceGroupFee(list).getEntity();
        LicenceFeeDto licenceFeeDto=new LicenceFeeDto();
        licenceFeeDto.setLicenceId(id);
        if(!entity.isEmpty()){
            for(HcsaLicenceGroupFeeDto every:entity){
                double amount = every.getAmount();
                int count = every.getCount();
                Date expiryDate1 = every.getExpiryDate();
                String groupId = every.getGroupId();
                licenceFeeDto.setOldAmount(amount);
                licenceFeeDto.setRenewCount(count);
                licenceFeeDto.setGroupId(groupId);
                licenceDto.setExpiryDate(expiryDate1);
            }
            licenceFeeDtos.add(licenceFeeDto);
            FeeDto feeDto = hcsaConfigClient.newFee(licenceFeeDtos).getEntity();
             total = feeDto.getTotal();

        }


    }

}
