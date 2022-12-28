package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremSubSvcRelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpecifiedCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.LicenceViewPrintService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceCommonClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaServiceClient;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * LicenceViewServiceImpl
 *
 * @author suocheng
 * @date 12/27/2022
 */
@Service
@Slf4j
public class LicenceViewServicePrintImpl implements LicenceViewPrintService {

    @Autowired
    private HcsaLicenceCommonClient hcsaLicenceCommonClient;
    @Autowired
    private HcsaServiceClient hcsaServiceClient;
    @Override
    public LicenceViewDto getLicenceViewDtoByLicenceId(String licenceId) {
        LicenceViewDto licenceViewDto = hcsaLicenceCommonClient.getAllStatusLicenceByLicenceId(licenceId).getEntity();
        List<LicPremisesScopeDto> licPremisesScopeDtos = licenceViewDto.getLicPremisesScopeDtos();
        List<LicPremSubSvcRelDto> licPremSubSvcRelDtos = licenceViewDto.getLicPremSubSvcRelDtos();
        List<HcsaSvcSpecifiedCorrelationDto> hcsaSvcSpecifiedCorrelationDtos = hcsaServiceClient.getHcsaSvcSpecifiedCorrelationDtos(
                licenceViewDto.getLicenceDto().getSvcName(),
                licenceViewDto.getLicenceDto().getServiceId(),
                licenceViewDto.getPremisesType()).getEntity();
        List<InnerLicenceViewData> innerLicenceViewDataList = tidyInnerLicenceViewData(licPremisesScopeDtos, licPremSubSvcRelDtos,hcsaSvcSpecifiedCorrelationDtos);
        List<String> disciplinesSpecifieds = IaisCommonUtils.genNewArrayList();
        if (IaisCommonUtils.isNotEmpty(innerLicenceViewDataList)) {
            StringBuilder str = new StringBuilder();
            int eachPage = 14;
            for (int i = 0; i < innerLicenceViewDataList.size(); i++) {
                int d = (i + 1) % eachPage;
                str.append("<li>").append(StringUtil.viewNonNullHtml(innerLicenceViewDataList.get(i).getValue()));
                List<String> innerLicenceViewDatas = innerLicenceViewDataList.get(i).getInnerLicenceViewDatas();
                if (IaisCommonUtils.isNotEmpty(innerLicenceViewDatas)) {
                    str.append("<br></br>");
                    for (int j = 0; j < innerLicenceViewDatas.size(); j++) {
                        str.append("- ").append(StringUtil.viewNonNullHtml(innerLicenceViewDatas.get(j)));
                        if (j != innerLicenceViewDatas.size() - 1) {
                            str.append("<br></br>");
                        }
                    }
                }
                if (d == 0) {
                    str.append("</li>");
                    disciplinesSpecifieds.add(str.toString());
                    str = new StringBuilder();
                } else if (i == innerLicenceViewDataList.size() - 1) {
                    str.append("</li>");
                    disciplinesSpecifieds.add(str.toString());
                } else {
                    str.append("</li>");
                }
            }
        }
        licenceViewDto.setDisciplinesSpecifieds(disciplinesSpecifieds);
        return licenceViewDto;
    }
    @Getter
    @Setter
    static class InnerLicenceViewData {

        String value;
        List<String> innerLicenceViewDatas;

    }

    private String getHcsaServiceSubTypeDisplayName(List<HcsaServiceSubTypeDto> hcsaServiceSubTypeDtos, String id) {
        String result = "";
        for (HcsaServiceSubTypeDto hcsaServiceSubTypeDto : hcsaServiceSubTypeDtos) {
            if (hcsaServiceSubTypeDto.getId().equals(id)) {
                result = hcsaServiceSubTypeDto.getSubtypeName();
                break;
            }
        }
        return result;
    }

    private String getHcsaServiceDtoDisplayName(List<HcsaServiceDto> hcsaServiceDtos, String code) {
        String result = "";
        for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtos) {
            if (hcsaServiceDto.getSvcCode().equals(code)) {
                result = hcsaServiceDto.getSvcName();
                break;
            }
        }
        return result;
    }

    private boolean isLever0(List<HcsaSvcSpecifiedCorrelationDto> hcsaSvcSpecifiedCorrelationDtos, String svcCode){
        boolean result = false;
        if(IaisCommonUtils.isNotEmpty(hcsaSvcSpecifiedCorrelationDtos) && StringUtil.isNotEmpty(svcCode)){
            for(HcsaSvcSpecifiedCorrelationDto hcsaSvcSpecifiedCorrelationDto : hcsaSvcSpecifiedCorrelationDtos){
                if(svcCode.equals(hcsaSvcSpecifiedCorrelationDto.getSpecifiedSvcId())){
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    private List<InnerLicenceViewData> tidyInnerLicenceViewData(List<LicPremisesScopeDto> licPremisesScopeDtos,
                                                                List<LicPremSubSvcRelDto> licPremSubSvcRelDtos,List<HcsaSvcSpecifiedCorrelationDto> hcsaSvcSpecifiedCorrelationDtos ) {
        List<InnerLicenceViewData> result = IaisCommonUtils.genNewArrayList();
        if (IaisCommonUtils.isNotEmpty(licPremisesScopeDtos)) {
            List<String> ids = IaisCommonUtils.genNewArrayList();
            for (LicPremisesScopeDto licPremisesScopeDto : licPremisesScopeDtos) {
                ids.add(licPremisesScopeDto.getSubTypeId());
            }
            List<HcsaServiceSubTypeDto> hcsaServiceSubTypeDtos = hcsaServiceClient.getHcsaServiceSubTypeDtosByIds(ids).getEntity();
            for (LicPremisesScopeDto licPremisesScopeDto : licPremisesScopeDtos) {
                String subTypeDisplayName =  getHcsaServiceSubTypeDisplayName(hcsaServiceSubTypeDtos, licPremisesScopeDto.getSubTypeId());
                if(StringUtil.isNotEmpty(subTypeDisplayName)){
                    InnerLicenceViewData innerLicenceViewData = new InnerLicenceViewData();
                    innerLicenceViewData.setValue(subTypeDisplayName );
                    result.add(innerLicenceViewData);
                }
            }
        }
        if (IaisCommonUtils.isNotEmpty(licPremSubSvcRelDtos)) {
            List<String> svcCodes = IaisCommonUtils.genNewArrayList();
            for (LicPremSubSvcRelDto licPremSubSvcRelDto : licPremSubSvcRelDtos) {
                svcCodes.add(licPremSubSvcRelDto.getSvcCode());
            }
            List<HcsaServiceDto> hcsaServiceDtos = hcsaServiceClient.getHcsaServiceDtoByCode(svcCodes).getEntity();
            InnerLicenceViewData innerLicenceViewData;
            List<String> innerLicenceViewDataList = IaisCommonUtils.genNewArrayList();
            for (LicPremSubSvcRelDto licPremSubSvcRelDto : licPremSubSvcRelDtos) {
                if (isLever0(hcsaSvcSpecifiedCorrelationDtos,licPremSubSvcRelDto.getSvcCode())) {
                    innerLicenceViewData = new InnerLicenceViewData();
                    innerLicenceViewData.setValue(getHcsaServiceDtoDisplayName(hcsaServiceDtos, licPremSubSvcRelDto.getSvcCode()));
                    innerLicenceViewDataList = IaisCommonUtils.genNewArrayList();
                    innerLicenceViewData.setInnerLicenceViewDatas(innerLicenceViewDataList);
                    result.add(innerLicenceViewData);
                } else {
                    innerLicenceViewDataList.add(getHcsaServiceDtoDisplayName(hcsaServiceDtos, licPremSubSvcRelDto.getSvcCode()));
                }
            }
        }
        return result;
    }
}
