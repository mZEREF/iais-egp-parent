package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.LicenceFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.HcsaLicenceGroupFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
import com.ecquaria.cloud.moh.iais.service.AutoRenwalService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private OrganizationClient organizationClient;
    @Autowired
    private SystemBeLicClient systemBeLicClient;
    @Override
    public void startRenwal() {
        List<Integer> dayList=new ArrayList<>();
        dayList.add(30);
        dayList.add(45);
        dayList.add(60);
        dayList.add(90);
        dayList.add(120);
        dayList.add(150);
        dayList.add(1800);
        List<JobRemindMsgTrackingDto> JobRemindMsgTrackingDto = systemBeLicClient.listJob().getEntity();
        Map<String, List<LicenceDto>> entity = hcsaLicenClient.licenceRenwal(dayList).getEntity();
        entity.forEach((k, v) -> {
            licenceToRemove(v,JobRemindMsgTrackingDto);
            for(int i=0;i<v.size();i++){

                String licenceNo = v.get(i).getLicenceNo();

                boolean autoOrNon = isAutoOrNon(licenceNo);

                if(!autoOrNon){

                    isAuto(v.get(i));

                }else {

                    isNoAuto(v.get(i));

                }


            }
        });

    }




    /****************/
/*
* use organization id
* */
    private OrganizationDto getOrganizationBy(String id){

        return  organizationClient.getOrganizationById(id).getEntity();

    }


    /*
    * if true is auto
    * else non auto
    * */
    private boolean isAutoOrNon(String licenceId){


        return false;
    }
/*
*  is sended to user or orgainization
*
* */
    private void licenceToRemove(List<LicenceDto>  applicationDtos ,  List<JobRemindMsgTrackingDto> JobRemindMsgTrackingDto ){
        if(applicationDtos!=null){
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

    }


    /*
    * non auto to send
    *
    * */
    private void  isNoAuto(LicenceDto licenceDto){

        String svcName = licenceDto.getSvcName();

        OrganizationDto organizationBy = getOrganizationBy(licenceDto.getOrganizationId());

        Date expiryDate = licenceDto.getExpiryDate();

        String id = licenceDto.getId();

        List<String> list = useLicenceIdFindHciNameAndAddress(id);

        for(String every:list){




        }


    }
    /*
    *
    * auto to send
    * */
    private void isAuto(LicenceDto licenceDto ){
        /*Name of Service*/

        OrganizationDto organizationBy = getOrganizationBy(licenceDto.getOrganizationId());

        String svcName = licenceDto.getSvcName();
        Date expiryDate = licenceDto.getExpiryDate();
        String licenceNo = licenceDto.getLicenceNo();
        String[] split = licenceNo.split("/");

        Double total=0.0;
        String id = licenceDto.getId();

        List<String> useLicenceIdFindHciNameAndAddress = useLicenceIdFindHciNameAndAddress(id);

        Boolean isMigrated = licenceDto.isMigrated();
        List<String> list=new ArrayList<>();
        List<LicenceFeeDto> licenceFeeDtos=new ArrayList<>();
        list.add(id);
        List<HcsaLicenceGroupFeeDto> entity = hcsaLicenClient.retrieveHcsaLicenceGroupFee(list).getEntity();

        List<PremisesDto> premisesDtoList = hcsaLicenClient.getPremisess(id).getEntity();
        List<String> premises=new ArrayList<>();
        for(PremisesDto premisesDto:premisesDtoList){
            premises.add(premisesDto.getPremisesType());
        }

        if(!entity.isEmpty()){

            for(HcsaLicenceGroupFeeDto every:entity){
                LicenceFeeDto licenceFeeDto=new LicenceFeeDto();
                licenceFeeDto.setLicenceId(id);
                double amount = every.getAmount();
                int count = every.getCount();
                Date expiryDate1 = every.getExpiryDate();
                String groupId = every.getGroupId();

                licenceFeeDto.setBaseService(split[4]);
                licenceFeeDto.setServiceCode(split[4]);
                licenceFeeDto.setServiceName(svcName);
                licenceFeeDto.setPremises(premises);
                licenceFeeDto.setRenewCount(count);

                licenceFeeDto.setExpiryDate(expiryDate1);

          /*    if(isMigrated){
                  licenceFeeDto.setMigrated(true);
                  licenceFeeDto.setGroupId(groupId);
                  licenceFeeDto.setOldAmount(amount);
              }else  if(!isMigrated){*/
                  licenceFeeDto.setMigrated(false);
                licenceFeeDto.setOldAmount(amount);
          /*    }*/
                licenceFeeDtos.add(licenceFeeDto);
            }

            FeeDto feeDto = hcsaConfigClient.renewFee(licenceFeeDtos).getEntity();
             total = feeDto.getTotal();

        }

        for(String every:useLicenceIdFindHciNameAndAddress){


        }


    }

    /*
    * remind sended email
    *
    * */

    private void saveMailJob(){
        JobRemindMsgTrackingDto jobRemindMsgTrackingDto=new JobRemindMsgTrackingDto();

        Void entity = systemBeLicClient.saveSendMailJob(jobRemindMsgTrackingDto).getEntity();

    }

        /*
        * message id
        * */

        private String messageId(String numberOfMonth){
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append("Email01");
            stringBuilder.append(numberOfMonth);
            return stringBuilder.toString();
        }


       /*
       * licence id  hci name and address
       * */

       private  List<String> useLicenceIdFindHciNameAndAddress(String licenceId){
           List<String> nameAndAddress=new ArrayList<>();
           if(licenceId==null){
             return    nameAndAddress;
           }
           List<PremisesDto> entity = hcsaLicenClient.getPremisess(licenceId).getEntity();
            for(PremisesDto every:entity){
                StringBuilder stringBuilder=new StringBuilder();
                String hciName = every.getHciName();
                String blkNo = every.getBlkNo();
                String streetName = every.getStreetName();
                String buildingName = every.getBuildingName();

                String floorNo = every.getFloorNo();
                String unitNo = every.getUnitNo();
                String postalCode = every.getPostalCode();

                stringBuilder.append(hciName+"\n");
                stringBuilder.append(blkNo+" ");
                stringBuilder.append(streetName+" ");
                stringBuilder.append(buildingName+" # ");
                stringBuilder.append(floorNo+"-");
                stringBuilder.append(unitNo+",");
                stringBuilder.append(postalCode);
                nameAndAddress.add(stringBuilder.toString());
            }
        return nameAndAddress;
       }
}
