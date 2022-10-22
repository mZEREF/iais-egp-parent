package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CounsellingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsCenterDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.AppAlignLicQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicSvcClinicalDirectorDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.MenuLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.monitoring.excel.MonitoringSheetsDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
public class LicenceInFallback implements LicenceClient {

    private FeignResponseEntity getFeignResponseEntity(Object... params) {
        return IaisEGPHelper.getFeignResponseEntity(params);
    }

    @Override
    public FeignResponseEntity<LicenceDto> getLicBylicId(String licenceId) {
        return IaisEGPHelper.getFeignResponseEntity("getLicBylicId",licenceId);
    }

    @Override
    public FeignResponseEntity<List<PremisesListQueryDto>> getPremises(String licenseeId) {
        return IaisEGPHelper.getFeignResponseEntity("getPremises",licenseeId);
    }

    @Override
    public FeignResponseEntity<List<PremisesListQueryDto>> getPremisesByLicneceId(String licenceId) {
        return IaisEGPHelper.getFeignResponseEntity("getPremisesByLicneceId",licenceId);
    }

    @Override
    public FeignResponseEntity<String> doUpdate(LicenceDto licenceDto) {
        return IaisEGPHelper.getFeignResponseEntity("doUpdate",licenceDto);
    }

    @Override
    public FeignResponseEntity<String> doSave(LicenceDto licenceDto) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<LicenceDto> getLicBylicNo(String licenceNo) {
        return IaisEGPHelper.getFeignResponseEntity("getLicBylicNo",licenceNo);
    }

    @Override
    public FeignResponseEntity<List<LicenceDto>> getLicDtosByLicNos(List<String> licenceNos) {
        return IaisEGPHelper.getFeignResponseEntity("getLicDtosByLicNos",licenceNos);
    }

    @Override
    public FeignResponseEntity<LicenceDto> getLicenceByAppId(String appId) {
        return IaisEGPHelper.getFeignResponseEntity("getLicenceByAppId",appId);
    }
    @Override
    public FeignResponseEntity<LicenceViewDto>  getLicenceViewByLicenceId(String licenceId) {
        return IaisEGPHelper.getFeignResponseEntity("getLicenceViewByLicenceId",licenceId);
    }

    @Override
    public FeignResponseEntity<List<PremisesDto>> getPremisesDto(String licenceId) {
        return IaisEGPHelper.getFeignResponseEntity("getPremisesDto",licenceId);
    }

    @Override
    public FeignResponseEntity<List<AppGrpPremisesDto>> getLatestAppPremisesByConds(String licenseeId, List<String> svcNames) {
        return IaisEGPHelper.getFeignResponseEntity(licenseeId, svcNames);
    }

    @Override
    public FeignResponseEntity<List<PremisesDto>> getLatestPremisesByConds(String licenseeId, List<String> svcNames,
            boolean loadAll) {
        return getFeignResponseEntity(licenseeId, svcNames, loadAll);
    }

    @Override
    public FeignResponseEntity<SearchResult<PersonnelQueryDto>> psnDoQuery(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("psnDoQuery",searchParam);
    }

    @Override
    public FeignResponseEntity<AppSubmissionDto> getExistBaseSvcInfo(List<String> licenceIds) {
        return IaisEGPHelper.getFeignResponseEntity("getExistBaseSvcInfo",licenceIds);
    }

    @Override
    public FeignResponseEntity<List<LicenceDto>> getLicenceDtoByLicenseeId(String licenseeId) {
        return IaisEGPHelper.getFeignResponseEntity("getLicenceDtoByLicenseeId",licenseeId);
    }

    @Override
    public FeignResponseEntity<SearchResult<MenuLicenceDto>> getMenuLicence(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("getMenuLicence",searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<AppAlignLicQueryDto>> getBundleLicence(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("getBundleLicence",searchParam);
    }

    @Override
    public FeignResponseEntity<List<String>> getAppIdsByLicId(String licId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppIdsByLicId",licId);
    }

    @Override
    public FeignResponseEntity<List<LicenceDto>> getLicenceDtosBypremisesId(String premisesId) {
        return IaisEGPHelper.getFeignResponseEntity("getLicenceDtosBypremisesId",premisesId);
    }

    @Override
    public FeignResponseEntity<SearchResult<PremisesListQueryDto>> getPremises(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("getPremises",searchParam);
    }

    @Override
    public FeignResponseEntity<List<PersonnelListDto>> getPersonnelListDto(PersonnelTypeDto personnelTypeDto) {
        return IaisEGPHelper.getFeignResponseEntity("getPersonnelListDto",personnelTypeDto);
    }

    @Override
    public FeignResponseEntity<List<PersonnelsDto>> getPersonnelDtoByLicId(String licId) {
        return IaisEGPHelper.getFeignResponseEntity("getPersonnelDtoByLicId",licId);
    }

    @Override
    public FeignResponseEntity<List<String>> getSpecLicIdsByLicIds(List<String> licenceIds) {
        return IaisEGPHelper.getFeignResponseEntity("getSpecLicIdsByLicIds",licenceIds);
    }

    @Override
    public FeignResponseEntity<PremisesDto> getLicPremisesDtoById(String id) {
        return IaisEGPHelper.getFeignResponseEntity("getLicPremisesDtoById",id);
    }

    @Override
    public FeignResponseEntity<List<String>> getActSpecIdByActBaseId(String licId) {
        return IaisEGPHelper.getFeignResponseEntity("getActSpecIdByActBaseId",licId);
    }

    @Override
    public FeignResponseEntity<List<AppAlignLicQueryDto>> getAppAlignLicQueryDto(String licenseeId, String svcNameStr,String premTypeStr) {
        return IaisEGPHelper.getFeignResponseEntity("getAppAlignLicQueryDto",licenseeId,svcNameStr,premTypeStr);
    }

    @Override
    public FeignResponseEntity<Boolean> checkIsNewLicsee(String licenseeId) {
        return IaisEGPHelper.getFeignResponseEntity("checkIsNewLicsee",licenseeId);
    }

    @Override
    public FeignResponseEntity<LicenceDto> getLicDtoById(String licenceId) {
        return IaisEGPHelper.getFeignResponseEntity("getLicDtoById",licenceId);
    }

    @Override
    public FeignResponseEntity<LicenceDto> getLicenceDtoByLicNo(String licenceNo) {
        return IaisEGPHelper.getFeignResponseEntity("getLicenceDtoByLicNo",licenceNo);
    }

    @Override
    public FeignResponseEntity<Boolean> existingOnSiteOrConveLic(String svcName, String licenseeId) {
        return IaisEGPHelper.getFeignResponseEntity("existingOnSiteOrConveLic",svcName,licenseeId);
    }

    @Override
    public FeignResponseEntity<List<MenuLicenceDto>> setPremAdditionalInfo(List<MenuLicenceDto> menuLicenceDtos) {
        return IaisEGPHelper.getFeignResponseEntity("setPremAdditionalInfo",menuLicenceDtos);
    }

    @Override
    public FeignResponseEntity<PremisesDto> getPremiseDtoByHciCodeOrName(String hciCodeName) {
        return IaisEGPHelper.getFeignResponseEntity("getPremiseDtoByHciCodeOrName",hciCodeName);
    }

    @Override
    public FeignResponseEntity<List<String>> listHciNames() {
        return IaisEGPHelper.getFeignResponseEntity("listHciNames");
    }

    @Override
    public FeignResponseEntity<List<GiroAccountInfoDto>> getGiroAccountByHciCodeAndOrgId(List<String> hciCodeList, String orgId) {
        return IaisEGPHelper.getFeignResponseEntity("getGiroAccountByHciCodeAndOrgId",hciCodeList,orgId);
    }

    @Override
    public FeignResponseEntity<Boolean> getBundleLicence(String hciCode, String licenseeId, List<String> svcNameList) {
        return IaisEGPHelper.getFeignResponseEntity("getBundleLicence",hciCode,licenseeId,svcNameList);
    }

    @Override
    public FeignResponseEntity<List<LicSvcClinicalDirectorDto>> getLicSvcClinicalDirectorDtoByIdNos(List<String> ids) {
        return IaisEGPHelper.getFeignResponseEntity("getLicSvcClinicalDirectorDtoByIdNos",ids);
    }

    @Override
    public FeignResponseEntity<List<LicenceDto>> getLicenceDtoByPremCorreIds(List<String> premCorreIds) {
        return IaisEGPHelper.getFeignResponseEntity("getLicenceDtoByPremCorreIds",premCorreIds);
    }

    @Override
    public FeignResponseEntity<LicenceDto> getLicBylicIdIncludeMigrated(String licenceId) {
        return IaisEGPHelper.getFeignResponseEntity("getLicBylicIdIncludeMigrated",licenceId);
    }
    @Override
    public FeignResponseEntity<List<SubLicenseeDto>> getSubLicensees(String orgId, String licenseeType) {
        return IaisEGPHelper.getFeignResponseEntity("getSubLicensees",orgId,licenseeType);
    }
    @Override
    public FeignResponseEntity<SubLicenseeDto> getSubLicenseesById(String id) {
        return IaisEGPHelper.getFeignResponseEntity("getSubLicenseesById",id);
    }

    @Override
    public FeignResponseEntity<SearchResult<GiroAccountInfoQueryDto>> searchGiroInfoByParam(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchGiroInfoByParam",searchParam);
    }

    @Override
    public FeignResponseEntity<PremisesDto> getPremisesDtoForBusinessName(String licenceId) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<List<AppSubmissionDto>> getAlginAppSubmissionDtos(String licenceId, Boolean checkSpec) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<MonitoringSheetsDto> getMonitoringLicenceSheetsDto() {
        return IaisEGPHelper.getFeignResponseEntity("getMonitoringLicenceSheetsDto");
    }

    @Override
    public FeignResponseEntity<List<LicenceDto>> getApproveLicenceDtoByLicenseeId(String licenseeId) {
        return IaisEGPHelper.getFeignResponseEntity("getApproveLicenceDtoByLicenseeId",licenseeId);
    }


    @Override
    public FeignResponseEntity<List<LicenceDto>> getLicenceDtosByLicenseeId(String licenseeId) {
        return getFeignResponseEntity(licenseeId);
    }

    @Override
    public FeignResponseEntity<List<DsCenterDto>> getDsCenterDtosByOrgIdAndCentreType(String orgId, String centerType) {
        return getFeignResponseEntity(orgId, centerType);
    }

    @Override
    public FeignResponseEntity<List<DsCenterDto>> getCenterDtosByCentreType(String centerType) {
        return getFeignResponseEntity(centerType);
    }

    @Override
    public FeignResponseEntity<DsCenterDto> getArCenter(String orgId, String hciCode) {
        return  getFeignResponseEntity(orgId, hciCode);
    }

    @Override
    public FeignResponseEntity<List<CounsellingDto>> getCounsellingDtos() {
        return  getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<DataSubmissionDto> getDataSubmissionDto(String id) {
        return  getFeignResponseEntity();
    }
}
