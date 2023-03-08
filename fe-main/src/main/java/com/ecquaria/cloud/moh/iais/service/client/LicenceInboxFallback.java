package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppLicBundleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DpSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DrugSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsCenterDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.AppAlignLicQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.KeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LaboratoryDevelopTestDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.MenuLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnlAssessQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.SelfPremisesListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxDataSubmissionQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageSearchDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-11-29 13:20
 **/
@Slf4j
@Component
public class LicenceInboxFallback implements LicenceInboxClient {

    private  FeignResponseEntity getEntity(){
        return IaisEGPHelper.getFeignResponseEntity("getEntity");
    }

    @Override
    public FeignResponseEntity<SearchResult<InboxLicenceQueryDto>> searchResultFromLicence(SearchParam searchParam){
        return IaisEGPHelper.getFeignResponseEntity("searchResultFromLicence",searchParam);
    }

    @Override
    public FeignResponseEntity<List<AppGrpPremisesDto>> getDistinctPremisesByLicenseeId(String licenseeId, String serviceName) {
        return IaisEGPHelper.getFeignResponseEntity("getDistinctPremisesByLicenseeId",licenseeId,serviceName);
    }

    @Override
    public FeignResponseEntity<List<PremisesListQueryDto>> getPremises(@RequestParam(value = "licenseeId" ) String licenseeId) {
        return IaisEGPHelper.getFeignResponseEntity("getPremises",licenseeId);
    }

    @Override
    public FeignResponseEntity<LicPremisesDto> getlicPremisesCorrelationsByPremises(String licCorreId) {
        return IaisEGPHelper.getFeignResponseEntity("getlicPremisesCorrelationsByPremises",licCorreId);
    }

    @Override
    public FeignResponseEntity<List<PersonnelListQueryDto>> getPersonnel(String licenseeId) {
        return IaisEGPHelper.getFeignResponseEntity("getPersonnel",licenseeId);
    }

    @Override
    public FeignResponseEntity<SearchResult<PersonnelQueryDto>> searchPsnInfo(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchPsnInfo",searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<PersonnlAssessQueryDto>> assessPsnDoQuery(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("assessPsnDoQuery",searchParam);
    }

    @Override
    public FeignResponseEntity<Integer> getLicActiveStatusNum(InterMessageSearchDto interMessageSearchDto) {
        return IaisEGPHelper.getFeignResponseEntity("InterMessageSearchDto",interMessageSearchDto);
    }

    @Override
    public FeignResponseEntity<LicenceDto> getLicBylicNo() {
        return IaisEGPHelper.getFeignResponseEntity("getLicBylicNo");
    }

    @Override
    public FeignResponseEntity<LicenceDto> getLicBylicId(String licenceId) {
        return IaisEGPHelper.getFeignResponseEntity("getLicBylicId",licenceId);
    }

    @Override
    public FeignResponseEntity<LicenceDto> getLicDtoById(String licenceId) {
        return IaisEGPHelper.getFeignResponseEntity("getLicDtoById",licenceId);
    }

    @Override
    public FeignResponseEntity<List<PremisesDto>> getPremisesDto(String licenceId) {
        return IaisEGPHelper.getFeignResponseEntity("getPremisesDto",licenceId);
    }

    @Override
    public FeignResponseEntity<SearchResult<SelfPremisesListQueryDto>> searchResultPremises(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchResultPremises",searchParam);
    }

    @Override
    public FeignResponseEntity<List<KeyPersonnelDto>> getKeyPersonnelByRole(List<String> roles) {
        return IaisEGPHelper.getFeignResponseEntity("getKeyPersonnelByRole",roles);
    }

    @Override
    public FeignResponseEntity<List<AppAlignLicQueryDto>> getAppAlignLicQueryDto(String licenseeId, String svcNameStr, String premTypeStr) {
        return IaisEGPHelper.getFeignResponseEntity("getAppAlignLicQueryDto",licenseeId);
    }

    @Override
    public FeignResponseEntity<SearchResult<MenuLicenceDto>> getMenuLicence(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("getMenuLicence",searchParam);
    }

    @Override
    public FeignResponseEntity<LicenceDto> getLicdtoByOrgId(String orgId) {
        return IaisEGPHelper.getFeignResponseEntity("getLicdtoByOrgId",orgId);
    }

    @Override
    public FeignResponseEntity<Boolean> checkIsNewLicsee(String licenseeId) {
        return IaisEGPHelper.getFeignResponseEntity("checkIsNewLicsee",licenseeId);
    }

    @Override
    public FeignResponseEntity<LicenceViewDto> getLicenceViewByLicenceId(String licenceId) {
        return IaisEGPHelper.getFeignResponseEntity("getLicenceViewByLicenceId",licenceId);
    }

    @Override
    public FeignResponseEntity<LicenceViewDto> getAllStatusLicenceByLicenceId(String licenceId) {
        return IaisEGPHelper.getFeignResponseEntity("getAllStatusLicenceByLicenceId",licenceId);
    }

    @Override
    public FeignResponseEntity<List<PersonnelListDto>> getPersonnelListAssessment(List<String> idNos,String orgId) {
        return IaisEGPHelper.getFeignResponseEntity("getPersonnelListAssessment",idNos);
    }

    @Override
    public FeignResponseEntity<List<PremisesDto>> getPremisesByLicseeIdAndSvcName(String licenseeId, List<String> svcNames) {
        return IaisEGPHelper.getFeignResponseEntity("getPremisesByLicseeIdAndSvcName",licenseeId);
    }

    @Override
    public FeignResponseEntity<List<LicenceDto>> isNewApplication(String application) {
        return IaisEGPHelper.getFeignResponseEntity("isNewApplication",application);
    }

    @Override
    public FeignResponseEntity<List<LicenceDto>> isNewLicence(String licenceId) {
        return IaisEGPHelper.getFeignResponseEntity("isNewLicence",licenceId);
    }

    @Override
    public FeignResponseEntity<List<LicenceDto>> getLicenceDtosByLicenseeId(String licenseeId) {
        return IaisEGPHelper.getFeignResponseEntity("getLicenceDtosByLicenseeId",licenseeId);
    }

    @Override
    public FeignResponseEntity<LicenceDto> getFirstLicenceDtosByLicenseeId(String licenseeId) {
        return getEntity();
    }

    @Override
    public FeignResponseEntity<List<MenuLicenceDto>> setPremAdditionalInfo(List<MenuLicenceDto> menuLicenceDtos) {
        return IaisEGPHelper.getFeignResponseEntity("setPremAdditionalInfo",menuLicenceDtos);
    }
    @Override
    public FeignResponseEntity<LaboratoryDevelopTestDto> saveLaboratoryDevelopTest(LaboratoryDevelopTestDto laboratoryDevelopTestDto) {
        return IaisEGPHelper.getFeignResponseEntity("saveLaboratoryDevelopTest",laboratoryDevelopTestDto);
    }

    @Override
    public FeignResponseEntity<List<LicenceDto>> getLicenceDtoByHciCode(String hciCode, String licenseeId) {
        return IaisEGPHelper.getFeignResponseEntity("getLicenceDtoByHciCode",licenseeId);
    }

    @Override
    public FeignResponseEntity<List<LicenceDto>> getBundleLicence(LicenceDto licenceDto) {
        return IaisEGPHelper.getFeignResponseEntity("getBundleLicence",licenceDto);
    }

    @Override
    public FeignResponseEntity<LicenceDto> getRootLicenceDtoByOrgId(String orgId) {
        return IaisEGPHelper.getFeignResponseEntity("getRootLicenceDtoByOrgId",orgId);
    }

    @Override
    public FeignResponseEntity<List<SubLicenseeDto>> getIndividualSubLicensees(String orgId) {
        return IaisEGPHelper.getFeignResponseEntity("getIndividualSubLicensees",orgId);
    }

    @Override
    public FeignResponseEntity<List<LicenceDto>> getBaseOrSpecLicence(String licenceId) {
        return IaisEGPHelper.getFeignResponseEntity("getBaseOrSpecLicence",licenceId);
    }

    @Override
    public FeignResponseEntity<String> refreshSubLicenseeInfo(LicenseeDto licenseeDto) {
        return IaisEGPHelper.getFeignResponseEntity("refreshSubLicenseeInfo",licenseeDto);
    }

    @Override
    public FeignResponseEntity<SearchResult<InboxDataSubmissionQueryDto>> searchLicence(SearchParam searchParam) {
        return getEntity();
    }

    @Override
    public FeignResponseEntity<Void> deleteArSuperDataSubmissionDtoDraftByDraftNo(String draftNo) {
        return getEntity();
    }

    @Override
    public FeignResponseEntity<List<LicenceDto>> getActiveLicencesByLicenseeId(String licenseeId) {
        return getEntity();
    }


    @Override
    public FeignResponseEntity<Integer> getRfcCountByCycleId(String cycleId) {
        return getEntity();
    }

    @Override
    public FeignResponseEntity<ArSuperDataSubmissionDto> getArSuperDataSubmissionDto(String submissionNo) {
        return IaisEGPHelper.getFeignResponseEntity("getArSuperDataSubmissionDto",submissionNo);
    }

    @Override
    public FeignResponseEntity<List<CycleDto>> cycleByPatientCode(String patientCode) {
        return IaisEGPHelper.getFeignResponseEntity("cycleByPatientCode",patientCode);
    }

    @Override
    public FeignResponseEntity<List<DataSubmissionDto>> getAllDataSubmissionByCycleId(String cycleId) {
        return IaisEGPHelper.getFeignResponseEntity("getAllDataSubmissionByCycleId",cycleId);
    }

    @Override
    public FeignResponseEntity<List<DsCenterDto>> getDsCenterDtosByLicenseeId(String licenseeId) {
        return IaisEGPHelper.getFeignResponseEntity(licenseeId);
    }

    @Override
    public FeignResponseEntity<List<DsCenterDto>> getDsCenterDtosByOrganizationId(String organizationId) {
        return IaisEGPHelper.getFeignResponseEntity("getDsCenterDtosByOrganizationId",organizationId);
    }

    @Override
    public FeignResponseEntity<Void> updateDataSubmissionByIdChangeStatus(String id,Integer lockStatus) {
        return getEntity();
    }

    @Override
    public FeignResponseEntity<Integer> dssDraftNum(InterMessageSearchDto interMessageSearchDto) {
        return getEntity();
    }

    @Override
    public FeignResponseEntity<Boolean> hasDonorSampleUseCycleByDonorSampleId(String donorSampleId) {
        return getEntity();
    }

    @Override
    public FeignResponseEntity<DpSuperDataSubmissionDto> getDpSuperDataSubmissionDto(String submissionNo) {
        return getEntity();
    }

    @Override
    public FeignResponseEntity<List<DrugSubmissionDto>> getDrugSubmissionDtosBySubmissionNo( String submissionNo){
        return getEntity();
    }

    @Override
    public FeignResponseEntity<DataSubmissionDraftDto> getDataSubmissionDraftDtoBySubmissionId(String submissionId) {
        return getEntity();
    }

    @Override
    public FeignResponseEntity<Void> deleteDraftBySubmissionId(String submissionId) {
        return getEntity();
    }

    @Override
    public FeignResponseEntity<List<DsCenterDto>> updateBeDsCenterStatus() {
        return getEntity();
    }

    @Override
    public FeignResponseEntity<List<LicenceDto>> getAllBundleLicences(List<String> licIds) {
        return IaisEGPHelper.getFeignResponseEntity(licIds);
    }

    @Override
    public FeignResponseEntity<List<LicenceDto>> getApproveLicenceDtoByLicenseeId(String licenseeId) {
        return getEntity();
    }

    @Override
    public FeignResponseEntity<SearchResult<AppAlignLicQueryDto>> getBundleLicence(SearchParam searchParam) {
        return getEntity();
    }

    @Override
    public FeignResponseEntity<List<AppLicBundleDto>> getActiveGroupAppLicBundlesByLicId(String licenceId, boolean withCurrLic) {
        return getEntity();
    }

}
