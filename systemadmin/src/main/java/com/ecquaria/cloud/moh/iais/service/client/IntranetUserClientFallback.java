package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserRoleDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.WorkloadCalculationDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

import java.util.List;

/**
 * @author weilu
 * @date 2019/12/25 15:45
 */
public class IntranetUserClientFallback implements IntranetUserClient{

    @Override
    public FeignResponseEntity<Void> createOrgUserDto(OrgUserDto user) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<OrgUserDto> createIntrenetUser(OrgUserDto user) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<Void> createOrgUserDtos(List<OrgUserDto> orgUserDtos) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<List<FeUserDto>> getUserListByNricAndIdType(String nric, String idType) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<SearchResult<OrgUserQueryDto>> doQuery(SearchParam searchParam) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<OrgUserDto> updateOrgUserDto(OrgUserDto orgUserDto) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<Void> delOrgUser(String id) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<OrgUserDto> findIntranetUserById(String id) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<List<OrganizationDto>> getUenList() {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<List<LicenseeDto>> findLicenseesFe() {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<SearchResult<TaskDto>> getTaskListBySearchParam(SearchParam searchParam) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<OrgUserDto> retrieveOneOrgUserAccount(String user_id) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<SearchResult<WorkingGroupQueryDto>> getWorkingGroupBySearchParam(SearchParam searchParam) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<List<TaskDto>> getCorrIdsByWorkGroupId(String workGroupId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<List<UserGroupCorrelationDto>> getUserGroupCorreByUserId(String userId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<OrgUserDto> getOrgUserAccountByUserId(String userId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<List<OrgUserRoleDto>> assignRole(List<OrgUserRoleDto> orgUserRoleDtos) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<String> removeRole(List<OrgUserRoleDto> orgUserRoleDtos) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<String> removeRoleByAccount(String userAccId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<List<OrgUserRoleDto>> retrieveRolesByuserAccId(String userAccId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<List<OrgUserRoleDto>> getUserRoleByIds(List<String> ids) {
        return null;
    }

    @Override
    public FeignResponseEntity<List<UserGroupCorrelationDto>> createUserGroupCorrelation(List<UserGroupCorrelationDto> userGroupCorrelationDtos) {
        return null;
    }

    @Override
    public FeignResponseEntity<List<UserGroupCorrelationDto>> getUserGroupsByUserId(String userId) {
        return null;
    }

    @Override
    public FeignResponseEntity<WorkingGroupDto> getWrkGrpById(String workGroupId) {
        return null;
    }

    @Override
    public FeignResponseEntity<Boolean> checkRoleIsExist(OrgUserRoleDto orgUserRoleDto) {
        return null;
    }

    @Override
    public FeignResponseEntity<List<UserGroupCorrelationDto>> getUserGroupsByUserIdAndWorkGroups(UserGroupCorrelationDto userGroupCorrelationDto) {
        return null;
    }

    @Override
    public FeignResponseEntity<List<String>> retrieveUserRoles(String userId) {
        return null;
    }

    @Override
    public FeignResponseEntity<List<OrgUserDto>> searchActiveBeUser() {
        return null;
    }

    @Override
    public FeignResponseEntity<List<OrgUserDto>> searchInActiveBeUser() {
        return null;
    }

    @Override
    public FeignResponseEntity<Boolean> workloadCalculation(WorkloadCalculationDto workloadCalculationDto) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<OrganizationDto> getByUenNoAndStatus(String uenNo, String status) {
       return getFeignResponseEntity();
    }

    private FeignResponseEntity getFeignResponseEntity(){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
