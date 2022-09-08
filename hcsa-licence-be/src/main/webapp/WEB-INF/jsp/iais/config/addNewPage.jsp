<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.ecquaria.cloud.RedirectUtil" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ServiceConfigConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.helper.MessageUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.ParamUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<webui:setLayout name="iais-intranet"/>

<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
  String titleShow = request.getAttribute("title") == null ? "Add HCSA Service" : (String)request.getAttribute("title");
%>
<style>
  .mandatory{
    color: #ff0000;
  }
  .view{
    color: #2199E8;
  }
  .width-center{
    text-align: center;width: 100%
  }
  .marg-1{
    margin-top: 1%;
  }
  .width-70{
    width: 70%;
  }
  .white-space{
    white-space: nowrap
  }
</style>
<div class="main-content readonly" >
  <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <input type="hidden" name="selectCategoryValue" value="">
    <c:if test="${isEdit}">
      <input  type="hidden" name="svcName" value="${hcsaServiceConfigDto.hcsaServiceDto.svcName}">
      <input  type="hidden" name="svcCode" value="${hcsaServiceConfigDto.hcsaServiceDto.svcCode}">
      <input  type="hidden" name="svcType"  value="${hcsaServiceConfigDto.hcsaServiceDto.svcType}">
    </c:if>
    <input type="text" style="display: none" name="serviceSvcCode" id="serviceSvcCode" value="${hcsaServiceDto.svcCode}">
    <div class="col-lg-12 col-xs-12">
      <div class="bg-title" style="text-align: center;">
        <h2>HCSA Service</h2>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-9">
          <h2 class="col-xs-0 col-md-7 component-title"><%=titleShow%></h2>
          <input type="text" style="display: none" name="serviceIsUse" value="false">
        </div>
      </div>

      <div class="form-group editReadonly">
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" for="serviceName">Service Name&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="serviceName" type="text" name="svcName" maxlength="100" value="${hcsaServiceConfigDto.hcsaServiceDto.svcName}">
            <span name="iaisErrorMsg" class="error-msg" id="error_svcName"></span>
          </div>
        </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" for="description">Service Description&nbsp;<span class="mandatory" >*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="description" type="text" name="svcDesc" maxlength="255" value="${hcsaServiceConfigDto.hcsaServiceDto.svcDesc}">
            <span class="error-msg" name="iaisErrorMsg" id="error_svcDesc"></span>
          </div>
        </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" for="displayDescription">Service Display Description&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="displayDescription" type="text" name="svcDisplayDesc" maxlength="255" value="${hcsaServiceConfigDto.hcsaServiceDto.svcDisplayDesc}">
            <span class="error-msg" name="iaisErrorMsg" id="error_svcDisplayDesc"></span>
          </div>
        </div>
      </div>

      <div class="form-group editReadonly">
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" for="serviceCode">Service Code&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="serviceCode" type="text" name="svcCode" maxlength="3" value="${hcsaServiceConfigDto.hcsaServiceDto.svcCode}">
            <span class="error-msg" name="iaisErrorMsg" id="error_svcCode"></span>
          </div>
        </div>
      </div>

      <div class="form-group editReadonly">
        <div class="col-xs-12 col-md-9" >
          <label class="col-xs-12 col-md-7 control-label" for="ServiceType">Service Type&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4" style="margin-bottom: 20px;">
            <select id="ServiceType" name="svcType" >
              <option value="">Please Select</option>
              <c:forEach var="codeSelectOption" items="${codeSelectOptionList}">
                <option value="${codeSelectOption.value}" <c:if test="${hcsaServiceConfigDto.hcsaServiceDto.svcType==codeSelectOption.value}">selected="selected"</c:if>>${codeSelectOption.text}</option>
              </c:forEach>
            </select>
            <span class="error-msg" name="iaisErrorMsg"  id="error_svcType"></span>
          </div>
        </div>
      </div>


      <div class="form-group" id="selectCategoryId" >
        <div class="col-xs-12 col-md-9" style="margin-bottom: 20px;">
          <label class="col-xs-12 col-md-7 control-label">Service Category&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <select name="categoryId" >
              <option value="">Please Select</option>
              <c:forEach items="${categoryDtos}" var="categoryDto">
                <option value="${categoryDto.id}" <c:if test="${hcsaServiceConfigDto.hcsaServiceDto.categoryId==categoryDto.id}">selected</c:if>>${categoryDto.name}</option>
              </c:forEach>
            </select>
            <span id="error_categoryId" class="error-msg" name="iaisErrorMsg"></span>
          </div>
        </div>
      </div>

      <div id ="admndAndNotifactionFlow">
        <div class="form-group" >
          <div class="col-xs-12 col-md-9" style="margin-bottom: 10px">
            <label class="col-xs-12 col-md-7 control-label"></label>
            <div class="col-xs-12 col-md-4">
              <label class="col-xs-12 col-md-6 control-label" style="padding-left: 0px;">Amendment Flow</label>
              <label class="col-xs-12 col-md-6 control-label">Notification Flow</label>
            </div>
          </div>
        </div>

        <div class="form-group">
          <div class="col-xs-12 col-md-9">
            <label class="col-xs-12 col-md-7 control-label" >Addition&nbsp;<span class="mandatory">*</span></label>
            <div class="cl-xs-12 col-md-4">
              <div class="col-xs-12 col-md-6 form-check">
                <input  type="radio" <c:if test="${hcsaServiceConfigDto.hcsaServiceDto.additionFlow=='1'}"> checked</c:if> class="form-check-input other-lic co-location" name="additionFlow"  value="1" >
                <label class="form-check-label" ><span class="check-circle"></span></label>
              </div>
              <div class="col-xs-12 col-md-6 form-check">
                <input  type="radio" <c:if test="${hcsaServiceConfigDto.hcsaServiceDto.additionFlow=='0'}"> checked</c:if> class="form-check-input other-lic co-location" name="additionFlow"  value="0">
                <label class="form-check-label" ><span class="check-circle"></span></label>
              </div>
              <span class="error-msg" class="form-check-input other-lic co-location" name="iaisErrorMsg" id="error_additionFlow"></span>
            </div>
          </div>
        </div>
        <div class="form-group">
          <div class="col-xs-12 col-md-9">
            <label class="col-xs-12 col-md-7 control-label" >Removal&nbsp;<span class="mandatory">*</span></label>
            <div class="cl-xs-12 col-md-4">
              <div class="col-xs-12 col-md-6 form-check">
                <input  type="radio" <c:if test="${hcsaServiceConfigDto.hcsaServiceDto.removalFlow=='1'}"> checked</c:if> class="form-check-input other-lic co-location" name="removalFlow"  value="1" >
                <label class="form-check-label" ><span class="check-circle"></span></label>
              </div>
              <div class="col-xs-12 col-md-6 form-check">
                <input  type="radio" <c:if test="${hcsaServiceConfigDto.hcsaServiceDto.removalFlow=='0'}"> checked</c:if> class="form-check-input other-lic co-location" name="removalFlow"  value="0">
                <label class="form-check-label" ><span class="check-circle"></span></label>
              </div>
              <span class="error-msg" class="form-check-input other-lic co-location" name="iaisErrorMsg" id="error_removalFlow"></span>
            </div>
          </div>
        </div>
        <div class="form-group">
          <div class="col-xs-12 col-md-9">
            <label class="col-xs-12 col-md-7 control-label" for="displayDescription">Description Label</label>
            <div class="col-xs-12 col-md-4">
              <input id="descriptionLabel" type="text" name="descriptionLabel" maxlength="255" value="${hcsaServiceConfigDto.hcsaServiceDto.descriptionLabel}">
              <span class="error-msg" name="iaisErrorMsg" id="error_descriptionLabel"></span>
            </div>
          </div>
        </div>
      </div>
      <div id ="baseAndSpeci">
      <div id ="msd">
        <div class="form-group">
          <div class="col-xs-12 col-md-12">
            <label class="col-xs-12 col-md-12 control-label" style="margin-bottom: 20px;">Mode of Service Delivery&nbsp;<span class="mandatory">*</span></label>
            <span class="error-msg" name="iaisErrorMsg" id="error_premisesTypes"></span>
          </div>
        </div>

        <div class="form-group">
          <div class="form-check-gp">
            <div class="row">
              <div class="col-xs-12 col-md-3">
                <div class="form-check " style="left: 10%">
                  <c:set var="type" value="${hcsaServiceConfigDto.getPremisesTypesForPage()}"></c:set>
                  <input class="form-check-input" name="premisesTypes" id="icon3checkboxSample" onclick="premisesSelect();"
                         <c:if test="${fn:contains(type,'PERMANENT')}">checked="checked"</c:if> type="checkbox" value="PERMANENT"  aria-invalid="false">
                  <label class="form-check-label" for="icon3checkboxSample"><span class="check-square"></span>Permanent Premises</label>
                </div>
              </div>
              <div class="col-xs-12 col-md-3">
                <div class="form-check ">
                  <input class="form-check-input"  name="premisesTypes" id="icon5checkboxSample" onclick="premisesSelect();"
                         <c:if test="${fn:contains(type,'CONVEYANCE')}">checked="checked"</c:if> type="checkbox" value="CONVEYANCE" aria-invalid="false">
                  <label class="form-check-label" for="icon5checkboxSample"><span class="check-square"></span>Conveyance</label>
                </div>
              </div>
              <div class="col-xs-12 col-md-3">
                <div class="form-check ">
                  <input class="form-check-input"  name="premisesTypes" id="icon4checkboxSample" onclick="premisesSelect();"
                         <c:if test="${fn:contains(type,'MOBILE')}">checked="checked"</c:if> type="checkbox" value="MOBILE"  aria-invalid="false">
                  <label class="form-check-label" for="icon4checkboxSample"><span class="check-square"></span>Mobile Delivery</label>
                </div>
              </div>
              <div class="col-xs-12 col-md-3">
                <div class="form-check ">
                  <input class="form-check-input"  name="premisesTypes" id="icon6checkboxSample" onclick="premisesSelect();"
                         <c:if test="${fn:contains(type,'REMOTE')}">checked="checked"</c:if> type="checkbox" value="REMOTE"  aria-invalid="false">
                  <label class="form-check-label" for="icon4checkboxSample"><span class="check-square"></span>Remote Delivery</label>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

        <div class="form-group" id ="businessInformation">
          <div class="col-xs-12 col-md-9">
            <label class="col-xs-12 col-md-7 control-label" >Business Information <span class="mandatory">*</span></label>
            <div class="cl-xs-12 col-md-4">
              <div class="col-xs-12 col-md-6 form-check">
                <input  type="radio" <c:if test="${hcsaServiceConfigDto.getBusinessInformation()}"> checked</c:if> class="form-check-input other-lic co-location" name="businessInformation"  value="1" >
                <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
              </div>
              <div class="col-xs-12 col-md-6 form-check">
                <input  type="radio" <c:if test="${!hcsaServiceConfigDto.getBusinessInformation()}"> checked</c:if> class="form-check-input other-lic co-location" name="businessInformation"  value="0">
                <label class="form-check-label" ><span class="check-circle"></span>No</label>
              </div>
              <span class="error-msg" class="form-check-input other-lic co-location" name="iaisErrorMsg" id="error_businessInformation"></span>
            </div>
          </div>
          <div class="col-xs-12 col-md-9">
            <label class="col-xs-12 col-md-7 control-label" >Other Information <span class="mandatory">*</span></label>
            <div class="cl-xs-12 col-md-4">
              <div class="col-xs-12 col-md-6 form-check">
                <input  type="radio" <c:if test="${hcsaServiceConfigDto.getOtherInformation()}"> checked</c:if> class="form-check-input other-lic co-location" name="otherInformation"  value="1" >
                <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
              </div>
              <div class="col-xs-12 col-md-6 form-check">
                <input  type="radio" <c:if test="${!hcsaServiceConfigDto.getOtherInformation()}"> checked</c:if> class="form-check-input other-lic co-location" name="otherInformation"  value="0">
                <label class="form-check-label" ><span class="check-circle"></span>No</label>
              </div>
              <span class="error-msg" class="form-check-input other-lic co-location" name="iaisErrorMsg" id="error_otherInformation"></span>
            </div>
          </div>
          <div class="col-xs-12 col-md-9">
            <label class="col-xs-12 col-md-7 control-label" >Supplementary Form <span class="mandatory">*</span></label>
            <div class="cl-xs-12 col-md-4">
              <div class="col-xs-12 col-md-6 form-check">
                <input  type="radio" <c:if test="${hcsaServiceConfigDto.getSupplementaryForm()}"> checked</c:if> class="form-check-input other-lic co-location" name="supplementaryForm" onclick="toSupplementaryForm(true);" value="1" >
                <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
              </div>
              <div class="col-xs-12 col-md-6 form-check">
                <input  type="radio" <c:if test="${!hcsaServiceConfigDto.getSupplementaryForm()}"> checked</c:if> class="form-check-input other-lic co-location" name="supplementaryForm" onclick="toSupplementaryForm(true);" value="0">
                <label class="form-check-label" ><span class="check-circle"></span>No</label>
              </div>
              <span class="error-msg" class="form-check-input other-lic co-location" name="iaisErrorMsg" id="error_supplementaryForm"></span>
            </div>
          </div>
          <div class="col-xs-12 col-md-9">
            <label class="col-xs-12 col-md-7 control-label" >Special Services Information <span class="mandatory">*</span></label>
            <div class="cl-xs-12 col-md-4">
              <div class="col-xs-12 col-md-6 form-check">
                <input  type="radio" <c:if test="${hcsaServiceConfigDto.getSpecialServicesInformation()}"> checked</c:if> class="form-check-input other-lic co-location" name="specialServicesInformation"  value="1" >
                <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
              </div>
              <div class="col-xs-12 col-md-6 form-check">
                <input  type="radio" <c:if test="${!hcsaServiceConfigDto.getSpecialServicesInformation()}"> checked</c:if> class="form-check-input other-lic co-location" name="specialServicesInformation"  value="0">
                <label class="form-check-label" ><span class="check-circle"></span>No</label>
              </div>
              <span class="error-msg" class="form-check-input other-lic co-location" name="iaisErrorMsg" id="error_specialServicesInformation"></span>
            </div>
          </div>
          <div class="col-xs-12 col-md-9">
            <label class="col-xs-12 col-md-7 control-label" >Outsourced Providers <span class="mandatory">*</span></label>
            <div class="cl-xs-12 col-md-4">
              <div class="col-xs-12 col-md-6 form-check">
                <input  type="radio" <c:if test="${hcsaServiceConfigDto.getOutsourcedProviders()}"> checked</c:if> class="form-check-input other-lic co-location" name="outsourcedProviders"  value="1" >
                <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
              </div>
              <div class="col-xs-12 col-md-6 form-check">
                <input  type="radio" <c:if test="${!hcsaServiceConfigDto.getOutsourcedProviders()}"> checked</c:if> class="form-check-input other-lic co-location" name="outsourcedProviders"  value="0">
                <label class="form-check-label" ><span class="check-circle"></span>No</label>
              </div>
              <span class="error-msg" class="form-check-input other-lic co-location" name="iaisErrorMsg" id="error_outsourcedProviders"></span>
            </div>
          </div>
        </div>

      <div class="form-group" >
        <div class="col-xs-12 col-md-9" style="margin-bottom: 10px">
          <label class="col-xs-12 col-md-7 control-label"></label>
          <div class="col-xs-12 col-md-4">
            <label class="col-xs-12 col-md-6 control-label" style="padding-left: 0px;">MINIMUM COUNT</label>
            <label class="col-xs-12 col-md-6 control-label">MAXIMUM COUNT</label>
          </div>
        </div>
      </div>

      <div id = "basePersionCount1">
        <div class="form-group">
          <div class="col-xs-12 col-md-9">
            <label class="col-xs-12 col-md-7 control-label" >Principal Officer (PO)&nbsp;<span class="mandatory">*</span></label>
            <div class="col-xs-12 col-md-2">
              <input type="text" name="man-principalOfficer" maxlength="2" placeholder="minimum count" value="${PO.pageMandatoryCount}">
              <span class="error-msg" name="iaisErrorMsg" id="error_man-principalOfficer"></span>
            </div>
            <div class="col-xs-12 col-md-2">
              <input type="text" name="mix-principalOfficer" maxlength="2" placeholder="maximum count" value="${PO.pageMaximumCount}">
              <span class="error-msg" name="iaisErrorMsg" id="error_mix-principalOfficer"></span>
            </div>
          </div>
        </div>

        <div class="form-group">
          <div class="col-xs-12 col-md-9">
            <label class="col-xs-12 col-md-7 control-label" >Nominee&nbsp;<span class="mandatory">*</span></label>
            <div class="col-xs-12 col-md-2">
              <input  type="text" name="man-DeputyPrincipalOfficer" maxlength="2" placeholder="minimum count" value="${DPO.pageMandatoryCount}">
              <span class="error-msg" name="iaisErrorMsg" id="error_man-DeputyPrincipalOfficer"></span>
            </div>
            <div class="col-xs-12 col-md-2">
              <input  type="text" name="mix-DeputyPrincipalOfficer" maxlength="2" placeholder="maximum count" value="${DPO.pageMaximumCount}">
              <span class="error-msg" name="iaisErrorMsg" id="error_mix-DeputyPrincipalOfficer"></span>
            </div>
          </div>
        </div>

        <div class="form-group">
          <div class="col-xs-12 col-md-9">
            <label class="col-xs-12 col-md-7 control-label" >Key Appointment Holder (KAH)&nbsp;<span class="mandatory">*</span></label>
            <div class="col-xs-12 col-md-2">
              <iais:input type="hidden" name="kahId" value="${KAH.id}"/>
              <iais:input maxLength="2" type="text" name="man-KAH" value="${KAH.mandatoryCount}"
                          placeholder="minimum count" needErrorSpan="false"/>
              <span class="error-msg" name="iaisErrorMsg" id="error_man-KAH"></span>
            </div>
            <div class="col-xs-12 col-md-2">
              <iais:input maxLength="2" type="text" name="mix-KAH" value="${KAH.maximumCount}"
                          placeholder="maximum count" needErrorSpan="false"/>
              <span class="error-msg" name="iaisErrorMsg" id="error_mix-KAH"></span>
            </div>
          </div>
        </div>


        <div class="form-group" id ="cdDiv">
          <div class="col-xs-12 col-md-9">
            <label class="col-xs-12 col-md-7 control-label" >Clinical Director&nbsp;<span class="mandatory">*</span></label>
            <div class="col-xs-12 col-md-2">
              <input type="text" name="man-clinical_director" maxlength="2" placeholder="minimum count" value="${CD.pageMandatoryCount}">
              <span class="error-msg" name="iaisErrorMsg" id="error_man-clinical_director"></span>
            </div>
            <div class="col-xs-12 col-md-2">
              <input type="text" name="mix-clinical_director" maxlength="2" placeholder="maximum count" value="${CD.pageMaximumCount}">
              <span class="error-msg" name="iaisErrorMsg" id="error_mix-clinical_director"></span>
            </div>
          </div>
        </div>
      </div>

      <div class="form-group" id ="cgoDiv">
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" >Clinical Governance Officer (CGO)&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="man-ClinicalGovernanceOfficer" maxlength="2" placeholder="minimum count" value="${CGO.pageMandatoryCount}">
            <span class="error-msg" name="iaisErrorMsg" id="error_man-ClinicalGovernanceOfficer"></span>
          </div>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="mix-ClinicalGovernanceOfficer" maxlength="2"  placeholder="maximum count" value="${CGO.pageMaximumCount}">
            <span class="error-msg" name="iaisErrorMsg" id="error_mix-ClinicalGovernanceOfficer"></span>
          </div>
        </div>
      </div>

      <div id ="basePersionCount2">
        <div class="form-group">
          <div class="col-xs-12 col-md-9">
            <label class="col-xs-12 col-md-7 control-label" >Service Personnel&nbsp;<span class="mandatory">*</span></label>
            <div class="col-xs-12 col-md-2">
              <input  type="text" name="man-ServicePersonnel" maxlength="2" placeholder="minimum count" value="${SVCPSN.pageMandatoryCount}">
              <span class="error-msg" name="iaisErrorMsg" id="error_man-ServicePersonnel"></span>
            </div>
            <div class="col-xs-12 col-md-2">
              <input  type="text" name="mix-ServicePersonnel" maxlength="2"  placeholder="maximum count" value="${SVCPSN.pageMaximumCount}">
              <span class="error-msg" name="iaisErrorMsg" id="error_mix-ServicePersonnel"></span>
            </div>
          </div>
        </div>

        <div class="form-group">
          <div class="col-xs-12 col-md-9">
            <label class="col-xs-12 col-md-7 control-label" >Vehicles&nbsp;<span class="mandatory">*</span></label>
            <div class="col-xs-12 col-md-2">
              <input type="text" name="man-vehicles" maxlength="2" placeholder="minimum count" value="${VEH.pageMandatoryCount}">
              <span class="error-msg" name="iaisErrorMsg" id="error_man-vehicles"></span>
            </div>
            <div class="col-xs-12 col-md-2">
              <input type="text" name="mix-vehicles" maxlength="2" placeholder="maximum count" value="${VEH.pageMaximumCount}">
              <span class="error-msg" name="iaisErrorMsg" id="error_mix-vehicles"></span>
            </div>
          </div>
        </div>


        <div class="form-group">
          <div class="col-xs-12 col-md-9">
            <label class="col-xs-12 col-md-7 control-label" >General Conveyance Charges<span class="mandatory">*</span></label>
            <div class="col-xs-12 col-md-2">
              <input type="text" name="man-charges" maxlength="2" placeholder="minimum count" value="${CHA.pageMandatoryCount}">
              <span class="error-msg" name="iaisErrorMsg" id="error_man-charges"></span>
            </div>
            <div class="col-xs-12 col-md-2">
              <input type="text" name="mix-charges" maxlength="2" placeholder="maximum count" value="${CHA.pageMaximumCount}">
              <span class="error-msg" name="iaisErrorMsg" id="error_mix-charges"></span>
            </div>
          </div>
        </div>


        <div class="form-group">
          <div class="col-xs-12 col-md-9">
            <label class="col-xs-12 col-md-7 control-label" >Medical Equipment and Other Charges<span class="mandatory">*</span></label>
            <div class="col-xs-12 col-md-2">
              <input type="text" name="man-other-charges" maxlength="2" placeholder="minimum count" value="${CHAO.pageMandatoryCount}">
              <span class="error-msg" name="iaisErrorMsg" id="error_man-other-charges"></span>
            </div>
            <div class="col-xs-12 col-md-2">
              <input type="text" name="mix-other-charges" maxlength="2" placeholder="maximum count" value="${CHAO.pageMaximumCount}">
              <span class="error-msg" name="iaisErrorMsg" id="error_mix-other-charges"></span>
            </div>
          </div>
        </div>


        <div class="form-group">
          <div class="col-xs-12 col-md-9">
            <label class="col-xs-12 col-md-7 control-label" >MedAlert Person&nbsp;<span class="mandatory">*</span></label>
            <div class="col-xs-12 col-md-2">
              <input value="${MAP.id}" name="svcpsnId" style="display:none;" type="text" maxlength="2">
              <input  type="text" name="man-MedalertPerson" value="${MAP.pageMandatoryCount}" maxlength="2" placeholder="minimum count">
              <span class="error-msg" name="iaisErrorMsg" id="error_man-MedalertPerson"></span>
            </div>
            <div class="col-xs-12 col-md-2">
              <input  type="text" name="mix-MedalertPerson" value="${MAP.pageMaximumCount}" maxlength="2"  placeholder="maximum count">
              <span class="error-msg" name="iaisErrorMsg" id="error_mix-MedalertPerson"></span>
            </div>
          </div>
        </div>
      </div>

        <div id="specialisedSuppFormOnly">
          <div class="form-group">
            <div class="col-xs-12 col-md-9">
              <label class="col-xs-12 col-md-7 control-label" >Emergency Department Director &nbsp;<span class="mandatory">*</span></label>
              <div class="col-xs-12 col-md-2">
                <iais:input type="hidden" name="eddId" value="${EDD.id}"/>
                <iais:input maxLength="2" type="text" name="man-EDD" value="${EDD.pageMandatoryCount}"
                            placeholder="minimum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_man-EDD"></span>
              </div>
              <div class="col-xs-12 col-md-2">
                <iais:input maxLength="2" type="text" name="mix-EDD" value="${EDD.pageMaximumCount}"
                            placeholder="maximum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_mix-EDD"></span>
              </div>
            </div>
          </div>

          <div class="form-group">
            <div class="col-xs-12 col-md-9">
              <label class="col-xs-12 col-md-7 control-label" >Emergency Department Nursing Director &nbsp;<span class="mandatory">*</span></label>
              <div class="col-xs-12 col-md-2">
                <iais:input type="hidden" name="edndId" value="${EDND.id}"/>
                <iais:input maxLength="2" type="text" name="man-EDND" value="${EDND.mandatoryCount}"
                            placeholder="minimum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_man-EDND"></span>
              </div>
              <div class="col-xs-12 col-md-2">
                <iais:input maxLength="2" type="text" name="mix-EDND" value="${EDND.maximumCount}"
                            placeholder="maximum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_mix-EDND"></span>
              </div>
            </div>
          </div>
        </div>

        <div class="form-group">
          <div class="col-xs-12 col-md-9">
            <label class="col-xs-12 col-md-7 control-label" >Section Leader&nbsp;<span class="mandatory">*</span></label>
            <div class="col-xs-12 col-md-2">
              <iais:input type="hidden" name="secldrId" value="${SECLDR.id}"/>
              <iais:input maxLength="2" type="text" name="man-SectionLeader" value="${SECLDR.mandatoryCount}"
                          placeholder="minimum count" needErrorSpan="false"/>
              <span class="error-msg" name="iaisErrorMsg" id="error_man-SectionLeader"></span>
            </div>
            <div class="col-xs-12 col-md-2">
              <iais:input maxLength="2" type="text" name="mix-SectionLeader" value="${SECLDR.maximumCount}"
                          placeholder="maximum count" needErrorSpan="false"/>
              <span class="error-msg" name="iaisErrorMsg" id="error_mix-SectionLeader"></span>
            </div>
          </div>
        </div>

        <div id = "specialisedPersionnelOnly">
          <div class="form-group">
            <div class="col-xs-12 col-md-9">
              <label class="col-xs-12 col-md-7 control-label" >Nurse In Charge&nbsp;<span class="mandatory">*</span></label>
              <div class="col-xs-12 col-md-2">
                <iais:input type="hidden" name="secldrId" value="${SPPT004.id}"/>
                <iais:input maxLength="2" type="text" name="man-NurseInCharge" value="${SPPT004.mandatoryCount}"
                            placeholder="minimum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_man-NurseInCharge"></span>
              </div>
              <div class="col-xs-12 col-md-2">
                <iais:input maxLength="2" type="text" name="mix-NurseInCharge" value="${SPPT004.maximumCount}"
                            placeholder="maximum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_mix-NurseInCharge"></span>
              </div>
            </div>
          </div>
          <div class="form-group">
            <div class="col-xs-12 col-md-9">
              <label class="col-xs-12 col-md-7 control-label" >Radiation Safety Officer (RSO)&nbsp;<span class="mandatory">*</span></label>
              <div class="col-xs-12 col-md-2">
                <iais:input type="hidden" name="secldrId" value="${SPPT003.id}"/>
                <iais:input maxLength="2" type="text" name="man-RadiationSafetyOfficer" value="${SPPT003.mandatoryCount}"
                            placeholder="minimum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_man-RadiationSafetyOfficer"></span>
              </div>
              <div class="col-xs-12 col-md-2">
                <iais:input maxLength="2" type="text" name="mix-RadiationSafetyOfficer" value="${SPPT003.maximumCount}"
                            placeholder="maximum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_mix-RadiationSafetyOfficer"></span>
              </div>
            </div>
          </div>
          <div class="form-group">
            <div class="col-xs-12 col-md-9">
              <label class="col-xs-12 col-md-7 control-label" >Diagnostic Radiographer&nbsp;<span class="mandatory">*</span></label>
              <div class="col-xs-12 col-md-2">
                <iais:input type="hidden" name="secldrId" value="${SPPT006.id}"/>
                <iais:input maxLength="2" type="text" name="man-DiagnosticRadiographer" value="${SPPT006.mandatoryCount}"
                            placeholder="minimum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_man-DiagnosticRadiographer"></span>
              </div>
              <div class="col-xs-12 col-md-2">
                <iais:input maxLength="2" type="text" name="mix-DiagnosticRadiographer" value="${SPPT006.maximumCount}"
                            placeholder="maximum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_mix-DiagnosticRadiographer"></span>
              </div>
            </div>
          </div>
          <div class="form-group">
            <div class="col-xs-12 col-md-9">
              <label class="col-xs-12 col-md-7 control-label" >Medical Physicist&nbsp;<span class="mandatory">*</span></label>
              <div class="col-xs-12 col-md-2">
                <iais:input type="hidden" name="secldrId" value="${SPPT002.id}"/>
                <iais:input maxLength="2" type="text" name="man-MedicalPhysicist" value="${SPPT002.mandatoryCount}"
                            placeholder="minimum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_man-MedicalPhysicist"></span>
              </div>
              <div class="col-xs-12 col-md-2">
                <iais:input maxLength="2" type="text" name="mix-MedicalPhysicist" value="${SPPT002.maximumCount}"
                            placeholder="maximum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_mix-MedicalPhysicist"></span>
              </div>
            </div>
          </div>
          <div class="form-group">
            <div class="col-xs-12 col-md-9">
              <label class="col-xs-12 col-md-7 control-label" >Radiation Physicist&nbsp;<span class="mandatory">*</span></label>
              <div class="col-xs-12 col-md-2">
                <iais:input type="hidden" name="secldrId" value="${SPPT001.id}"/>
                <iais:input maxLength="2" type="text" name="man-RadiationPhysicist" value="${SPPT001.mandatoryCount}"
                            placeholder="minimum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_man-RadiationPhysicist"></span>
              </div>
              <div class="col-xs-12 col-md-2">
                <iais:input maxLength="2" type="text" name="mix-RadiationPhysicist" value="${SPPT001.maximumCount}"
                            placeholder="maximum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_mix-RadiationPhysicist"></span>
              </div>
            </div>
          </div>
          <div class="form-group">
            <div class="col-xs-12 col-md-9">
              <label class="col-xs-12 col-md-7 control-label" >NM Technologist&nbsp;<span class="mandatory">*</span></label>
              <div class="col-xs-12 col-md-2">
                <iais:input type="hidden" name="secldrId" value="${SPPT005.id}"/>
                <iais:input maxLength="2" type="text" name="man-NMTechnologist" value="${SPPT005.mandatoryCount}"
                            placeholder="minimum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_man-NMTechnologist"></span>
              </div>
              <div class="col-xs-12 col-md-2">
                <iais:input maxLength="2" type="text" name="mix-NMTechnologist" value="${SPPT005.maximumCount}"
                            placeholder="maximum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_mix-NMTechnologist"></span>
              </div>
            </div>
          </div>
       </div>

        <div id ="basePersonnelAndSupplementary">
          <div class="form-group">
            <div class="col-xs-12 col-md-9">
              <label class="col-xs-12 col-md-7 control-label" >Service Personnel</label>
            </div>
          </div>

          <div class="form-group">
            <div class="col-xs-12 col-md-9">
              <label class="col-xs-12 col-md-7 control-label" >Embryologist&nbsp;<span class="mandatory">*</span></label>
              <div class="col-xs-12 col-md-2">
                <iais:input type="hidden" name="secldrId" value="${SP001.id}"/>
                <iais:input maxLength="2" type="text" name="man-SP001" value="${SP001.mandatoryCount}"
                            placeholder="minimum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_man-SP001"></span>
              </div>
              <div class="col-xs-12 col-md-2">
                <iais:input maxLength="2" type="text" name="mix-SP001" value="${SP001.maximumCount}"
                            placeholder="maximum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_mix-SP001"></span>
              </div>
            </div>
          </div>

          <div class="form-group">
            <div class="col-xs-12 col-md-9">
              <label class="col-xs-12 col-md-7 control-label" >AR Practitioner&nbsp;<span class="mandatory">*</span></label>
              <div class="col-xs-12 col-md-2">
                <iais:input type="hidden" name="secldrId" value="${SP002.id}"/>
                <iais:input maxLength="2" type="text" name="man-SP002" value="${SP002.mandatoryCount}"
                            placeholder="minimum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_man-SP002"></span>
              </div>
              <div class="col-xs-12 col-md-2">
                <iais:input maxLength="2" type="text" name="mix-SP002" value="${SP002.maximumCount}"
                            placeholder="maximum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_mix-SP002"></span>
              </div>
            </div>
          </div>

          <div class="form-group">
            <div class="col-xs-12 col-md-9">
              <label class="col-xs-12 col-md-7 control-label" >Nurses&nbsp;<span class="mandatory">*</span></label>
              <div class="col-xs-12 col-md-2">
                <iais:input type="hidden" name="secldrId" value="${SP003.id}"/>
                <iais:input maxLength="2" type="text" name="man-SP003" value="${SP003.mandatoryCount}"
                            placeholder="minimum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_man-SP003"></span>
              </div>
              <div class="col-xs-12 col-md-2">
                <iais:input maxLength="2" type="text" name="mix-SP003" value="${SP003.maximumCount}"
                            placeholder="maximum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_mix-SP003"></span>
              </div>
            </div>
          </div>

          <div id ="supplementaryForm">
          <div class="form-group">
            <div class="col-xs-12 col-md-9">
              <label class="col-xs-12 col-md-7 control-label" >Supplementary Form</label>
            </div>
          </div>
          <div class="form-group">
            <div class="col-xs-12 col-md-9">
              <label class="col-xs-12 col-md-7 control-label" >Person managing the Special Care Service&nbsp;<span class="mandatory">*</span></label>
              <div class="col-xs-12 col-md-2">
                <iais:input maxLength="2" type="text" name="man-SPMSC" value="${SPMSC.mandatoryCount}"
                            placeholder="minimum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_man-SPMSC"></span>
              </div>
              <div class="col-xs-12 col-md-2">
                <iais:input maxLength="2" type="text" name="mix-SPMSC" value="${SPMSC.maximumCount}"
                            placeholder="maximum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_mix-SPMSC"></span>
              </div>
            </div>
          </div>
          <div class="form-group">
              <div class="col-xs-12 col-md-9">
                <label class="col-xs-12 col-md-7 control-label" >Medical / Dental Practition&nbsp;<span class="mandatory">*</span></label>
                <div class="col-xs-12 col-md-2">
                  <iais:input maxLength="2" type="text" name="man-SMDP" value="${SMDP.mandatoryCount}"
                              placeholder="minimum count" needErrorSpan="false"/>
                  <span class="error-msg" name="iaisErrorMsg" id="error_man-SMDP"></span>
                </div>
                <div class="col-xs-12 col-md-2">
                  <iais:input maxLength="2" type="text" name="mix-SMDP" value="${SMDP.maximumCount}"
                              placeholder="maximum count" needErrorSpan="false"/>
                  <span class="error-msg" name="iaisErrorMsg" id="error_mix-SMDP"></span>
                </div>
              </div>
            </div>
          <div class="form-group">
              <div class="col-xs-12 col-md-9">
                <label class="col-xs-12 col-md-7 control-label" >Renal Physician&nbsp;<span class="mandatory">*</span></label>
                <div class="col-xs-12 col-md-2">
                  <iais:input maxLength="2" type="text" name="man-SRP" value="${SRP.mandatoryCount}"
                              placeholder="minimum count" needErrorSpan="false"/>
                  <span class="error-msg" name="iaisErrorMsg" id="error_man-SRP"></span>
                </div>
                <div class="col-xs-12 col-md-2">
                  <iais:input maxLength="2" type="text" name="mix-SRP" value="${SRP.maximumCount}"
                              placeholder="maximum count" needErrorSpan="false"/>
                  <span class="error-msg" name="iaisErrorMsg" id="error_mix-SRP"></span>
                </div>
              </div>
            </div>
          <div class="form-group">
            <div class="col-xs-12 col-md-9">
              <label class="col-xs-12 col-md-7 control-label" >Nurse in Charge&nbsp;<span class="mandatory">*</span></label>
              <div class="col-xs-12 col-md-2">
                <iais:input type="hidden" name="secldrId" value="${SNIC.id}"/>
                <iais:input maxLength="2" type="text" name="man-SNIC" value="${SNIC.mandatoryCount}"
                            placeholder="minimum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_man-SNIC"></span>
              </div>
              <div class="col-xs-12 col-md-2">
                <iais:input maxLength="2" type="text" name="mix-SNIC" value="${SNIC.maximumCount}"
                            placeholder="maximum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_mix-SNIC"></span>
              </div>
            </div>
          </div>
          <div class="form-group">
            <div class="col-xs-12 col-md-9">
              <label class="col-xs-12 col-md-7 control-label" >Dialysis Trained Registered Nurse&nbsp;<span class="mandatory">*</span></label>
              <div class="col-xs-12 col-md-2">
                <iais:input maxLength="2" type="text" name="man-SDTRN" value="${SDTRN.mandatoryCount}"
                            placeholder="minimum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_man-SDTRN"></span>
              </div>
              <div class="col-xs-12 col-md-2">
                <iais:input maxLength="2" type="text" name="mix-SDTRN" value="${SDTRN.maximumCount}"
                            placeholder="maximum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_mix-SDTRN"></span>
              </div>
            </div>
          </div>
          <div class="form-group">
            <div class="col-xs-12 col-md-9">
              <label class="col-xs-12 col-md-7 control-label" >Registered Nurses not Dialysis Trained&nbsp;<span class="mandatory">*</span></label>
              <div class="col-xs-12 col-md-2">
                <iais:input maxLength="2" type="text" name="man-SRNNDT" value="${SRNNDT.mandatoryCount}"
                            placeholder="minimum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_man-SRNNDT"></span>
              </div>
              <div class="col-xs-12 col-md-2">
                <iais:input maxLength="2" type="text" name="mix-SRNNDT" value="${SRNNDT.maximumCount}"
                            placeholder="maximum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_mix-SRNNDT"></span>
              </div>
            </div>
          </div>
          <div class="form-group">
            <div class="col-xs-12 col-md-9">
              <label class="col-xs-12 col-md-7 control-label" >Enrolled Nurse&nbsp;<span class="mandatory">*</span></label>
              <div class="col-xs-12 col-md-2">
                <iais:input maxLength="2" type="text" name="man-SEN" value="${SEN.mandatoryCount}"
                            placeholder="minimum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_man-SEN"></span>
              </div>
              <div class="col-xs-12 col-md-2">
                <iais:input maxLength="2" type="text" name="mix-SEN" value="${SEN.maximumCount}"
                            placeholder="maximum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_mix-SEN"></span>
              </div>
            </div>
          </div>
          <div class="form-group">
            <div class="col-xs-12 col-md-9">
              <label class="col-xs-12 col-md-7 control-label" >Acupuncturist&nbsp;<span class="mandatory">*</span></label>
              <div class="col-xs-12 col-md-2">
                <iais:input maxLength="2" type="text" name="man-SA" value="${SA.mandatoryCount}"
                            placeholder="minimum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_man-SA"></span>
              </div>
              <div class="col-xs-12 col-md-2">
                <iais:input maxLength="2" type="text" name="mix-SA" value="${SA.maximumCount}"
                            placeholder="maximum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_mix-SA"></span>
              </div>
            </div>
          </div>
          <div class="form-group">
            <div class="col-xs-12 col-md-9">
              <label class="col-xs-12 col-md-7 control-label" >Anaesthetist (Medical Service)&nbsp;<span class="mandatory">*</span></label>
              <div class="col-xs-12 col-md-2">
                <iais:input maxLength="2" type="text" name="man-SAM" value="${SAM.mandatoryCount}"
                            placeholder="minimum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_man-SAM"></span>
              </div>
              <div class="col-xs-12 col-md-2">
                <iais:input maxLength="2" type="text" name="mix-SAM" value="${SAM.maximumCount}"
                            placeholder="maximum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_mix-SAM"></span>
              </div>
            </div>
          </div>
          <div class="form-group">
            <div class="col-xs-12 col-md-9">
              <label class="col-xs-12 col-md-7 control-label" >Trained Nurses (Medical Service)&nbsp;<span class="mandatory">*</span></label>
              <div class="col-xs-12 col-md-2">
                <iais:input maxLength="2" type="text" name="man-STNM" value="${STNM.mandatoryCount}"
                            placeholder="minimum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_man-STNM"></span>
              </div>
              <div class="col-xs-12 col-md-2">
                <iais:input maxLength="2" type="text" name="mix-STNM" value="${STNM.maximumCount}"
                            placeholder="maximum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_mix-STNM"></span>
              </div>
            </div>
          </div>
          <div class="form-group">
            <div class="col-xs-12 col-md-9">
              <label class="col-xs-12 col-md-7 control-label" >Practicing Doctor&nbsp;<span class="mandatory">*</span></label>
              <div class="col-xs-12 col-md-2">
                <iais:input type="hidden" name="secldrId" value="${SPDO.id}"/>
                <iais:input maxLength="2" type="text" name="man-SPDO" value="${SPDO.mandatoryCount}"
                            placeholder="minimum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_man-SPDO"></span>
              </div>
              <div class="col-xs-12 col-md-2">
                <iais:input maxLength="2" type="text" name="mix-SPDO" value="${SPDO.maximumCount}"
                            placeholder="maximum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_mix-SPDO"></span>
              </div>
            </div>
          </div>
          <div class="form-group">
            <div class="col-xs-12 col-md-9">
              <label class="col-xs-12 col-md-7 control-label" >Anaesthetist (Dental Service)&nbsp;<span class="mandatory">*</span></label>
              <div class="col-xs-12 col-md-2">
                <iais:input maxLength="2" type="text" name="man-SAD" value="${SAD.mandatoryCount}"
                            placeholder="minimum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_man-SAD"></span>
              </div>
              <div class="col-xs-12 col-md-2">
                <iais:input maxLength="2" type="text" name="mix-SAD" value="${SAD.maximumCount}"
                            placeholder="maximum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_mix-SAD"></span>
              </div>
            </div>
          </div>
          <div class="form-group">
          <div class="col-xs-12 col-md-9">
            <label class="col-xs-12 col-md-7 control-label" >Trained Nurses (Dental Service)&nbsp;<span class="mandatory">*</span></label>
            <div class="col-xs-12 col-md-2">
              <iais:input maxLength="2" type="text" name="man-STND" value="${STND.mandatoryCount}"
                          placeholder="minimum count" needErrorSpan="false"/>
              <span class="error-msg" name="iaisErrorMsg" id="error_man-STND"></span>
            </div>
            <div class="col-xs-12 col-md-2">
              <iais:input maxLength="2" type="text" name="mix-STND" value="${STND.maximumCount}"
                          placeholder="maximum count" needErrorSpan="false"/>
              <span class="error-msg" name="iaisErrorMsg" id="error_mix-STND"></span>
            </div>
          </div>
        </div>
          <div class="form-group">
            <div class="col-xs-12 col-md-9">
              <label class="col-xs-12 col-md-7 control-label" >Practicing Dentist &nbsp;<span class="mandatory">*</span></label>
              <div class="col-xs-12 col-md-2">
                <iais:input type="hidden" name="secldrId" value="${SPDE.id}"/>
                <iais:input maxLength="2" type="text" name="man-SPDE" value="${SPDE.mandatoryCount}"
                            placeholder="minimum count" needErrorSpan="true"/>
              </div>
              <div class="col-xs-12 col-md-2">
                <iais:input maxLength="2" type="text" name="mix-SPDE" value="${SPDE.maximumCount}"
                            placeholder="maximum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_mix-SPDE"></span>
              </div>
            </div>
          </div>
          <div class="form-group">
            <div class="col-xs-12 col-md-9">
              <label class="col-xs-12 col-md-7 control-label" >Trained Dental Hygienist/ Dental Therapist / Oral Healthcare Therapist &nbsp;<span class="mandatory">*</span></label>
              <div class="col-xs-12 col-md-2">
                <iais:input type="hidden" name="secldrId" value="${SOHT.id}"/>
                <iais:input maxLength="2" type="text" name="man-SOHT" value="${SOHT.mandatoryCount}"
                            placeholder="minimum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_man-SOHT"></span>
              </div>
              <div class="col-xs-12 col-md-2">
                <iais:input maxLength="2" type="text" name="mix-SOHT" value="${SOHT.maximumCount}"
                            placeholder="maximum count" needErrorSpan="false"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_mix-SOHT"></span>
              </div>
            </div>
          </div>
          </div>
        </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" for="NumberDocument">Number of Service-Related Document (for <span id ="serviceTypeShow">specialised</span> service) to be uploaded&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="NumberDocument" type="text" maxlength="2" name="serviceDocSize" value="${hcsaServiceConfigDto.serviceDocSize}">
            <span class="error-msg" name="iaisErrorMsg" id="error_serviceDocSize"></span>
          </div>
        </div>
      </div>

      <div class="serviceNumberfields" id ="serviceNumberfields">
        <c:forEach items="${hcsaServiceConfigDto.hcsaSvcDocConfigDtos}" var="doc" varStatus="sta">
          <div class="form-group">
            <div class="col-xs-12 col-md-12">
              <label class="col-xs-12 col-md-5 control-label" style="margin-right: 2%">Name of Info Field</label>
              <input type="hidden" value="${doc.id}" name="commDocId">
              <div class="col-xs-12 col-md-2">
                <input  type="text" name="descriptionServiceDoc" maxlength="255" value="${doc.docDesc}">
              </div>
              <div class="col-xs-12 col-md-2 form-check" style="margin-top: 1%">
                  <input type="hidden" name="serviceDocMandatory"<c:choose><c:when test="${doc.isMandatory}"> value="1"</c:when><c:otherwise>value="0"</c:otherwise></c:choose>>
                  <input style="white-space: nowrap" class="form-check-input" <c:if test="${doc.isMandatory}">checked</c:if>  type="checkbox" onclick="serviceCheckboxOnclick(this)" name="descriptionServiceDocMandatory">
                  <label style="white-space: nowrap" class="form-check-label" ><span class="check-square"></span>Mandatory ?</label>
              </div>
              <div class="col-xs-12 col-md-2 form-check" style="margin-top: 1%">
                  <input type="hidden" name="serviceDocPremises" <c:choose><c:when test="${doc.dupForPrem=='1'}">value="1"</c:when><c:otherwise>value="0"</c:otherwise></c:choose>>
                  <input style="white-space: nowrap" class="form-check-input" <c:if test="${doc.dupForPrem=='1'}">checked</c:if>  type="checkbox" onclick="serviceCheckboxOnclick(this)" name="descriptionServiceDocPremises">
                  <label style="white-space: nowrap" class="form-check-label" ><span class="check-square"></span>To duplicate for individual mode of service delivery ?</label>
              </div>
              <div class="col-xs-12 col-md-3 form-check" style="margin-top: 1%">
                <iais:select name="selectDocPerson"
                             options="serviceDocPersonnelsOption" firstOption="To duplicate for the personnel?" value="${doc.dupForPerson}" />
              </div>
              <div class="col-xs-12 col-md-5" style="margin-right: 2%"></div>
              <div class="col-xs-12 col-md-4">
                <span class="error-msg" name="iaisErrorMsg" id="error_serviceDoc${sta.index}"></span>
              </div>
            </div>
          </div>
        </c:forEach>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-9  marg-1">
          <label class="col-xs-12 col-md-7 control-label" >Service-Related Checklists</label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary " data-toggle="modal" data-target= "<c:if test="${!isView}">#checklists</c:if>" ><span class="view">Configure</span></a>
            </div>
          </div>
        </div>
      </div>

      <div id ="baseKpiConfig">
        <div class="form-group">
          <div class="col-xs-12 col-md-9  marg-1">
            <label class="col-xs-12 col-md-7 control-label" >Service Risk Score</label>
            <div class="col-xs-10 col-md-4">
              <div class="components">
                <a class="btn btn-secondary "  data-toggle="modal" data-target= "<c:if test="${!isView}">#riskScore</c:if>"><span class="view">Configure</span></a>
              </div>
            </div>
          </div>
        </div>

        <div class="form-group">
          <div class="col-xs-12 col-md-9 marg-1">
            <label class="col-xs-12 col-md-7 control-label" >Service KPI</label>
            <div class="col-xs-10 col-md-4">
              <div class="components">
                <a class="btn btn-secondary " data-toggle="modal" data-target= "<c:if test="${!isView}">#kpi</c:if>" ><span class="view">Configure</span></a>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div id ="baseFlowConfig">
        <div class="form-group">
          <div class="col-xs-12 col-md-12" style="margin-left: 10%">
            <span class="error-msg"><c:if test="${errorMap['APTY002']!=null}"><%=MessageUtil.getMessageDesc("SC_ERR014")%></c:if></span>
            <br>
            <span class="error-msg"><c:if test="${errorMap['APTY004']!=null}"><%=MessageUtil.getMessageDesc("SC_ERR015")%></c:if></span>
            <br>
            <span class="error-msg"><c:if test="${errorMap['APTY001']!=null}"><%=MessageUtil.getMessageDesc("SC_ERR016")%></c:if></span>
            <br>
            <span class="error-msg"><c:if test="${errorMap['APTY005']!=null}"><%=MessageUtil.getMessageDesc("SC_ERR017")%></c:if></span>
            <br>
            <span class="error-msg"><c:if test="${errorMap['APTY008']!=null}"><%=MessageUtil.getMessageDesc("SC_ERR018")%></c:if></span>
            <br>
            <span class="error-msg"><c:if test="${errorMap['APTY006']!=null}"><%=MessageUtil.getMessageDesc("SC_ERR019")%></c:if></span>
          </div>

          <div class="col-xs-12 col-md-12" style="margin-top: 1%">
            <div class="col-xs-10 col-md-6">
              <div class="components width-center">
                <a class="btn btn-secondary width-70"  onclick="showRoutingStages('APTY002')"><span <c:if test="${errorMap['APTY002']!=null}">style="color: #ff0000" </c:if> class="view">NEW APPLICATION</span></a>
              </div>
            </div>
            <div class="col-xs-10 col-md-6">
              <div class="components width-center">
                <a class="btn btn-secondary width-70"  onclick="showRoutingStages('APTY004')"><span <c:if test="${errorMap['APTY004']!=null}">style="color: #ff0000" </c:if> class="view">RENEW</span></a>
              </div>
            </div>
          </div>
        </div>

        <div class="form-group">
          <div class="col-xs-12 col-md-12" style="margin-top: 1%">
            <div class="col-xs-10 col-md-6">
              <div class="components width-center">
                <a class="btn btn-secondary width-70"  onclick="showRoutingStages('APTY001')"><span <c:if test="${errorMap['APTY001']!=null}">style="color: #ff0000" </c:if> class="view">APPEAL</span></a>
              </div>
            </div>
            <div class="col-xs-10 col-md-6">
              <div class="components width-center">
                <a class="btn btn-secondary width-70"  onclick="showRoutingStages('APTY005')"><span <c:if test="${errorMap['APTY005']!=null}">style="color: #ff0000" </c:if> class="view">REQUEST FOR CHANGE</span></a>
              </div>
            </div>
          </div>
        </div>

        <div  class="form-group">
          <div class="col-xs-12 col-md-12" style="margin-top: 1%">
            <div class="col-xs-10 col-md-6">
              <div class="components  width-center">
                <a class="btn btn-secondary width-70"  onclick="showRoutingStages('APTY008')"><span <c:if test="${errorMap['APTY008']!=null}">style="color: #ff0000" </c:if> class="view">CESSATION</span></a>
              </div>
            </div>
            <div class="col-xs-10 col-md-6">
              <div class="components width-center">
                <a class="btn btn-secondary width-70" onclick="showRoutingStages('APTY006')"><span  <c:if test="${errorMap['APTY006']!=null}">style="color: #ff0000" </c:if> class="view">WITHDRAWAL</span></a>
              </div>
            </div>
          </div>
        </div>

        <div id = "routingStages">
        <c:set var="index" value="0"></c:set>
        <c:forEach items="${hcsaServiceConfigDto.hcsaConfigPageDtoMap}" var="routingStages" varStatus="sta">
          <div class="form-group" style="display: none" id="${routingStages.key}" >
            <div class="col-xs-12 col-md-12"  style="margin-top: 10px">
                <table aria-describedby="" border="1px" style="text-align: center" valign="middle">
                    <tr>
                      <th scope="col" style="width: 10% ;height: 40px;text-align: center">Application Type&nbsp;<span class="mandatory">*</span></th>
                      <th scope="col" style="width: 20% ;height: 40px;text-align: center">Service Workflow Routing Stages&nbsp;<span class="mandatory">*</span></th>
                      <th scope="col" style="width:30% ;height: 40px;text-align: center">Service Routing Scheme&nbsp;<span class="mandatory">*</span></th>
                      <th scope="col" style="width: 15% ;height: 40px;text-align: center">Service Workload Manhours&nbsp;<span class="mandatory">*</span></th>
                      <%--<th scope="col" style="width: 25% ;height: 40px;text-align: center">Working Group&nbsp;<span class="mandatory">*</span></th>--%>
                    </tr>
                  <c:forEach items="${routingStages.value}" var="routingStage" varStatus="status">
                    <tr>
                      <td >${routingStage.appTypeName} </td>
                      <td >${routingStage.stageName}</td>
                      <td>
                        <div class="col-xs-12 col-md-6" style="margin-top: 1%;margin-bottom: 1%;text-align:left">
                            <iais:select name="isMandatory${routingStage.stageCode}${routingStages.key}"
                                         options="selectOptions" firstOption="Please Select" value="${routingStage.isMandatory}"/>
                          <br>
                          <c:if test="${routingStage.stageCode=='AO1'|| routingStage.stageCode=='AO2'}">
                            <input type="hidden" value="${routingStage.canApprove}" name="canApprove${routingStage.stageCode}${routingStages.key}">
                            <input type="checkbox"  <c:if test="${routingStage.canApprove=='1'}">checked</c:if> onclick="canApprove(this)" /><span>&nbsp;Can Approve ?</span>
                          </c:if>
                        </div>

                        <div class="col-xs-12 col-md-6" style="margin-top: 1%;margin-bottom: 1%;text-align:left">
                          <iais:select name="routingSchemeName${routingStage.stageCode}${routingStages.key}"
                                       options="routingStagesOption" firstOption="Please Select" value="${routingStage.routingSchemeName}" />

                          <c:if test="${routingStage.stageCode=='INS'}">
                            <p>Inspector</p>
                          </c:if>
                          <c:if test="${routingStage.stageCode=='INS'}">
                            <c:forEach items="${routingStage.hcsaSvcSpeRoutingSchemeDtos}" var="hcsaSvcSpeRoutingSchemeDto">
                              <iais:select name="schemeType${routingStage.stageCode}${routingStages.key}${hcsaSvcSpeRoutingSchemeDto.insOder}"
                                           options="routingStagesOption" firstOption="Please Select" value="${hcsaSvcSpeRoutingSchemeDto.schemeType}" />

                              <p>${hcsaSvcSpeRoutingSchemeDto.getInsOderName()}</p>
                            </c:forEach>
                          </c:if>
                        </div>
                      </td>

                      <td>
                        <div class="col-xs-12 col-md-12" style="text-align:left">
                          <input style="margin: 0px 0px" type="text" maxlength="2" name="manhours${routingStage.stageCode}${routingStages.key}" value="${routingStage.manhours}" >
                          <span class="error-msg" name="iaisErrorMsg" id="error_manhours${routingStage.stageCode}${routingStages.key}"></span>
                        </div>
                      </td>

                    </tr>
                  </c:forEach>
                </table>
            </div>
          </div>
        </c:forEach>
        </div>
      </div>

        <div class="form-group" id ="baseSubService">
          <div class="col-xs-12 col-md-12 Sub-Types" style="margin-top: 20px ;margin-bottom: 20px">
            <label class="col-xs-12 col-md-12 control-label">Specialised Services</label>
          </div>
          <div class="col-xs-12 col-md-9 marg-1">
            <label class="col-xs-12 col-md-7 control-label" >Category / Discipline (Section Header)<span class="mandatory">*</span></label>
            <div class="col-xs-10 col-md-4">
              <div class="components">
                <input type="text" maxlength="100"
                       value="${hcsaServiceConfigDto.disciplineSectionHeader}" name="disciplineSectionHeader">
                <span name="iaisErrorMsg" class="error-msg" id="error_disciplineSectionHeader"></span>
              </div>
            </div>
          </div>
          <div class="col-xs-12 col-md-9 marg-1">
            <label class="col-xs-12 col-md-7 control-label" >Specialised Services (Section Header)<span class="mandatory">*</span></label>
            <div class="col-xs-10 col-md-4">
              <div class="components">
                <input type="text" maxlength="100"
                       value="${hcsaServiceConfigDto.specialisedSectionHeader}" name="specialisedSectionHeader">
                <span name="iaisErrorMsg" class="error-msg" id="error_specialisedSectionHeader"></span>
              </div>
            </div>
          </div>

          <c:forEach items="<%=ServiceConfigConstant.PREMISES_TYPE_MAP%>" var = "premTypeMap">
            <div class="col-xs-12 col-md-12 Sub-Types" style="margin-top: 20px ;margin-bottom: 20px" id ="${premTypeMap.key}-SVTP003">
              <div class="col-xs-12 col-md-12">
              </div>
              <div class="col-xs-12 col-md-12">
                <label class="col-xs-12 col-md-12 control-label">For ${premTypeMap.value}</label>
              </div>

              <c:choose>
                <c:when test="${hcsaServiceConfigDto.hcsaServiceCategoryDisciplineDtoMap !=null}">
                  <c:choose>
                    <c:when test="${hcsaServiceConfigDto.hcsaServiceCategoryDisciplineDtoMap[premTypeMap.key].categoryDisciplineDtos.size() == 0}">
                      <c:if test="${!isView}">
                        <div class="add col-xs-12 col-md-9 marg-1">
                          <label class="col-xs-12 col-md-7 control-label" >Category / Discipline</label>
                          <div class="col-xs-10 col-md-4">
                            <input type="text" maxlength="100" value="" name="${premTypeMap.key}-categoryDisciplines">
                            <span class="error-msg" ></span>
                          </div>
                          <div class="col-xs-12 col-md-1">
                            <a class="btn  btn-secondary view" onclick="removeThis(this)" >-</a>
                          </div>
                        </div>
                      </c:if>
                    </c:when>
                    <c:otherwise>
                      <c:forEach items="${hcsaServiceConfigDto.hcsaServiceCategoryDisciplineDtoMap[premTypeMap.key].categoryDisciplineDtos}" var = "categoryDisciplineDto">
                        <div class="add col-xs-12 col-md-9 marg-1">
                          <label class="col-xs-12 col-md-7 control-label" >Category / Discipline</label>
                          <div class="col-xs-10 col-md-4">
                            <input type="text" maxlength="100" value="${categoryDisciplineDto.categoryDiscipline}" name="${premTypeMap.key}-categoryDisciplines">
                            <span class="error-msg" >${categoryDisciplineDto.errorMsg}</span>
                          </div>
                          <div class="col-xs-12 col-md-1">
                            <a class="btn  btn-secondary view" <c:if test="${!isView}"> onclick="removeThis(this)"</c:if> >-</a>
                          </div>
                        </div>
                      </c:forEach>
                    </c:otherwise>
                  </c:choose>
                </c:when>
                <c:otherwise>
                  <c:if test="${!isView}">
                    <div class="add col-xs-12 col-md-9 marg-1">
                      <label class="col-xs-12 col-md-7 control-label" >Category / Discipline</label>
                      <div class="col-xs-10 col-md-4">
                        <input type="text" maxlength="100" value="" name="${premTypeMap.key}-categoryDisciplines">
                        <span class="error-msg" ></span>
                      </div>
                      <div class="col-xs-12 col-md-1">
                        <a class="btn  btn-secondary view" onclick="removeThis(this)" >-</a>
                      </div>
                    </div>
                  </c:if>
                </c:otherwise>
              </c:choose>
              <div class="col-xs-12 col-md-12">
                <a  class="btn  btn-secondary "   style="margin-right: 10px"
                        <c:if test="${!isView}"> onclick="addCategory(this,'${premTypeMap.key}-categoryDisciplines')" </c:if> > + </a><label > Add Item</label>
              </div>

              <c:choose>
                <c:when test="${hcsaServiceConfigDto.specHcsaServiceSubServicePageDtoMap !=null}">
                  <c:choose>
                    <c:when test="${hcsaServiceConfigDto.specHcsaServiceSubServicePageDtoMap[premTypeMap.key].hcsaServiceSubServiceErrorsDtos.size() == 0}">
                      <c:if test="${!isView}">
                        <div class="add col-xs-12 col-md-12"  style="margin-top: 20px ;margin-bottom: 20px">
                          <div class="col-xs-12 col-md-5" style="padding-right: 20%;margin-left:${hcsaServiceSubServiceErrorsDto.marginLeft}px" >
                            <iais:select name="${premTypeMap.key}-SVTP003-subServiceCodes" options="specHcsaServiceOptions" firstOption="Please Select"
                                         value=""/>
                            <span class="error-msg" ></span>
                          </div>
                          <div class="value">
                            <input type="text" value="0" name="${premTypeMap.key}-SVTP003-levels" style="display: none">
                          </div>
                          <div  class="col-xs-12 col-md-2" style="padding-left: 3%;" >
                            <a class="btn  btn-secondary  view" <c:if test="${!isView}"> onclick="indents(this)" </c:if>  >indent</a>
                          </div>
                          <div  class="col-xs-12 col-md-2" >
                            <a class="btn  btn-secondary view" <c:if test="${!isView}"> onclick="outdent(this)" </c:if> >outdent</a>
                          </div>
                          <div class="col-xs-12 col-md-1">
                            <a class="btn  btn-secondary view" <c:if test="${!isView}"> onclick="removeThis(this)" </c:if> >-</a>
                          </div>
                        </div>
                      </c:if>
                    </c:when>
                    <c:otherwise>
                      <c:forEach items="${hcsaServiceConfigDto.specHcsaServiceSubServicePageDtoMap[premTypeMap.key].hcsaServiceSubServiceErrorsDtos}" var = "hcsaServiceSubServiceErrorsDto">
                        <div class="add col-xs-12 col-md-12"  style="margin-top: 20px ;margin-bottom: 20px">
                          <div class="col-xs-12 col-md-5" style="padding-right: 20%;margin-left:${hcsaServiceSubServiceErrorsDto.marginLeft}px" >
                            <iais:select name="${premTypeMap.key}-SVTP003-subServiceCodes" options="specHcsaServiceOptions" firstOption="Please Select"
                                         value="${hcsaServiceSubServiceErrorsDto.subServiceCode}"/>
                            <span class="error-msg" >${hcsaServiceSubServiceErrorsDto.errorMsg}</span>
                          </div>
                          <div class="value">
                            <input type="text" value="${hcsaServiceSubServiceErrorsDto.level}" name="${premTypeMap.key}-SVTP003-levels" style="display: none">
                          </div>
                          <div  class="col-xs-12 col-md-2" style="padding-left: 3%;" >
                            <a class="btn  btn-secondary  view" <c:if test="${!isView}"> onclick="indents(this)" </c:if>  >indent</a>
                          </div>
                          <div  class="col-xs-12 col-md-2" >
                            <a class="btn  btn-secondary view" <c:if test="${!isView}"> onclick="outdent(this)" </c:if> >outdent</a>
                          </div>
                          <div class="col-xs-12 col-md-1">
                            <a class="btn  btn-secondary view" <c:if test="${!isView}"> onclick="removeThis(this)" </c:if> >-</a>
                          </div>
                        </div>
                      </c:forEach>
                    </c:otherwise>
                  </c:choose>
                </c:when>
                <c:otherwise>
                  <c:if test="${!isView}">
                    <div class="add col-xs-12 col-md-12"  style="margin-top: 20px ;margin-bottom: 20px">
                      <div class="col-xs-12 col-md-5" style="padding-right: 20%;margin-left:${hcsaServiceSubServiceErrorsDto.marginLeft}px" >
                        <iais:select name="${premTypeMap.key}-SVTP003-subServiceCodes" options="specHcsaServiceOptions" firstOption="Please Select"
                                     value=""/>
                        <span class="error-msg" ></span>
                      </div>
                      <div class="value">
                        <input type="text" value="0" name="${premTypeMap.key}-SVTP003-levels" style="display: none">
                      </div>
                      <div  class="col-xs-12 col-md-2" style="padding-left: 3%;" >
                        <a class="btn  btn-secondary  view" <c:if test="${!isView}"> onclick="indents(this)" </c:if>  >indent</a>
                      </div>
                      <div  class="col-xs-12 col-md-2" >
                        <a class="btn  btn-secondary view" <c:if test="${!isView}"> onclick="outdent(this)" </c:if> >outdent</a>
                      </div>
                      <div class="col-xs-12 col-md-1">
                        <a class="btn  btn-secondary view" <c:if test="${!isView}"> onclick="removeThis(this)" </c:if> >-</a>
                      </div>
                    </div>
                  </c:if>
                </c:otherwise>
              </c:choose>

              <div class="col-xs-12 col-md-12">
                <a  class="btn  btn-secondary "   style="margin-right: 10px" <c:if test="${!isView}">onclick="addAsItem(this,'${premTypeMap.key}','SVTP003')"</c:if>> + </a><label > Add Item</label>
              </div>
            </div>
          </c:forEach>

          <div class="col-xs-12 col-md-12 Sub-Types" style="margin-top: 20px ;margin-bottom: 20px">
            <label class="col-xs-12 col-md-12 control-label">Other Services</label>
          </div>

          <c:forEach items="<%=ServiceConfigConstant.PREMISES_TYPE_MAP%>" var = "premTypeMap">
            <div class="col-xs-12 col-md-12 Sub-Types" style="margin-top: 20px ;margin-bottom: 20px" id ="${premTypeMap.key}-SVTP005">
              <div class="col-xs-12 col-md-12">
              </div>
              <div class="col-xs-12 col-md-12">
                <label class="col-xs-12 col-md-12 control-label">For ${premTypeMap.value}</label>
              </div>

              <c:choose>
                <c:when test="${hcsaServiceConfigDto.otherHcsaServiceSubServicePageDtoMap !=null}">
                  <c:choose>
                    <c:when test="${hcsaServiceConfigDto.otherHcsaServiceSubServicePageDtoMap[premTypeMap.key].hcsaServiceSubServiceErrorsDtos.size() == 0}">
                      <c:if test="${!isView}">
                        <div class="add col-xs-12 col-md-12"  style="margin-top: 20px ;margin-bottom: 20px">
                          <div class="col-xs-12 col-md-5" style="padding-right: 20%;margin-left:${hcsaServiceSubServiceErrorsDto.marginLeft}px" >
                            <iais:select name="${premTypeMap.key}-SVTP005-subServiceCodes" options="otherHcsaServiceOptions" firstOption="Please Select"
                                         value=""/>
                            <span class="error-msg" ></span>
                          </div>
                          <div class="value">
                            <input type="text" value="0" name="${premTypeMap.key}-SVTP005-levels" style="display: none">
                          </div>
                          <div  class="col-xs-12 col-md-2" style="padding-left: 3%;" >
                            <a class="btn  btn-secondary  view" <c:if test="${!isView}"> onclick="indents(this)" </c:if>  >indent</a>
                          </div>
                          <div  class="col-xs-12 col-md-2" >
                            <a class="btn  btn-secondary view" <c:if test="${!isView}">  onclick="outdent(this)" </c:if> >outdent</a>
                          </div>
                          <div class="col-xs-12 col-md-1">
                            <a class="btn  btn-secondary view"  <c:if test="${!isView}"> onclick="removeThis(this)" </c:if> >-</a>
                          </div>
                        </div>
                      </c:if>
                    </c:when>
                    <c:otherwise>
                      <c:forEach items="${hcsaServiceConfigDto.otherHcsaServiceSubServicePageDtoMap[premTypeMap.key].hcsaServiceSubServiceErrorsDtos}" var = "hcsaServiceSubServiceErrorsDto">
                        <div class="add col-xs-12 col-md-12"  style="margin-top: 20px ;margin-bottom: 20px">
                          <div class="col-xs-12 col-md-5" style="padding-right: 20%;margin-left:${hcsaServiceSubServiceErrorsDto.marginLeft}px" >
                            <iais:select name="${premTypeMap.key}-SVTP005-subServiceCodes" options="otherHcsaServiceOptions" firstOption="Please Select"
                                         value="${hcsaServiceSubServiceErrorsDto.subServiceCode}"/>
                            <span class="error-msg" >${hcsaServiceSubServiceErrorsDto.errorMsg}</span>
                          </div>
                          <div class="value">
                            <input type="text" value="${hcsaServiceSubServiceErrorsDto.level}" name="${premTypeMap.key}-SVTP005-levels" style="display: none">
                          </div>
                          <div  class="col-xs-12 col-md-2" style="padding-left: 3%;" >
                            <a class="btn  btn-secondary  view" <c:if test="${!isView}"> onclick="indents(this)" </c:if>  >indent</a>
                          </div>
                          <div  class="col-xs-12 col-md-2" >
                            <a class="btn  btn-secondary view" <c:if test="${!isView}">  onclick="outdent(this)" </c:if> >outdent</a>
                          </div>
                          <div class="col-xs-12 col-md-1">
                            <a class="btn  btn-secondary view"  <c:if test="${!isView}"> onclick="removeThis(this)" </c:if> >-</a>
                          </div>
                        </div>
                      </c:forEach>
                    </c:otherwise>
                  </c:choose>
                </c:when>
                <c:otherwise>
                  <c:if test="${!isView}">
                    <div class="add col-xs-12 col-md-12"  style="margin-top: 20px ;margin-bottom: 20px">
                      <div class="col-xs-12 col-md-5" style="padding-right: 20%;margin-left:${hcsaServiceSubServiceErrorsDto.marginLeft}px" >
                        <iais:select name="${premTypeMap.key}-SVTP005-subServiceCodes" options="otherHcsaServiceOptions" firstOption="Please Select"
                                     value=""/>
                        <span class="error-msg" ></span>
                      </div>
                      <div class="value">
                        <input type="text" value="0" name="${premTypeMap.key}-SVTP005-levels" style="display: none">
                      </div>
                      <div  class="col-xs-12 col-md-2" style="padding-left: 3%;" >
                        <a class="btn  btn-secondary  view" <c:if test="${!isView}"> onclick="indents(this)" </c:if>  >indent</a>
                      </div>
                      <div  class="col-xs-12 col-md-2" >
                        <a class="btn  btn-secondary view" <c:if test="${!isView}">  onclick="outdent(this)" </c:if> >outdent</a>
                      </div>
                      <div class="col-xs-12 col-md-1">
                        <a class="btn  btn-secondary view"  <c:if test="${!isView}"> onclick="removeThis(this)" </c:if> >-</a>
                      </div>
                    </div>
                  </c:if>
                </c:otherwise>
              </c:choose>

              <div class="col-xs-12 col-md-12">
                <a  class="btn  btn-secondary "   style="margin-right: 10px"
                    <c:if test="${!isView}">onclick="addAsItem(this,'${premTypeMap.key}','SVTP005')"</c:if> > + </a><label > Add Item</label>
              </div>
            </div>
          </c:forEach>

        </div>
      </div>
        <div class="col-xs-12 col-md-9">
          <div class="form-group">
            <label class="col-xs-12 col-md-7 control-label">Effective Start Date&nbsp;<span class="mandatory">*</span></label>
            <div class=" col-xs-7 col-sm-4 col-md-3">
              <input type="text" value="${hcsaServiceConfigDto.hcsaServiceDto.effectiveDate}" autocomplete="off" class="date_picker form-control form_datetime"
                     name="effectiveDate" id="-20189532301300" data-date-start-date="01/01/1900" placeholder="dd/mm/yyyy" maxlength="10">
              <span id="error_StartDate" name="iaisErrorMsg" class="error-msg" ></span>
              <span class="error-msg" name="iaisErrorMsg" id="error_effectiveDate"></span>
            </div>
            <div class="clear"></div>
          </div>
        </div>

      <div class="col-xs-12 col-md-9" style="margin-bottom: 50px;">
        <div class="form-group">
          <label class="col-xs-12 col-md-7 control-label">Effective End Date</label>
          <div class=" col-xs-7 col-sm-4 col-md-3">
            <input type="text" autocomplete="off" value="${hcsaServiceConfigDto.hcsaServiceDto.endDate}"
                   class="date_picker form-control form_datetime" name="endDate" id="-20247433206800"
                   data-date-start-date="01/01/1900" placeholder="dd/mm/yyyy" maxlength="10">
            <span id="error_endDate" name="iaisErrorMsg" class="error-msg"></span>
            <span class="error-msg" name="iaisErrorMsg" id="error_effectiveEndDate"></span>
          </div>
          <div class="clear"></div></div>
      </div>
      <div class="col-lg-12 col-xs-12 canClick">
        <iais:action style="text-align:center;">
          <a class="btn btn-secondary" data-toggle="modal" data-target="#cancel">Cancel</a>
          <c:if test="${!isView}">
            <a class="btn btn-primary" onclick="save()">Save</a>
          </c:if>

          <c:if test="${isDelete}">
            <a class="btn btn-secondary"data-toggle="modal" style="color: #ff0000" data-target= "#deleteConfirmYesOrNo">CONFIRM DELETE</a>
            <input type="hidden" id="deleteConfirm" value="${hcsaServiceConfigDto.hcsaServiceDto.id}">
          </c:if>

          <c:if test="${isEidtAndView}">
            <button class="btn btn-primary"  onclick="submitPage('edit','<iais:mask name="crud_action_value"  value="${hcsaServiceConfigDto.hcsaServiceDto.id}"/>')">Update</button>
          </c:if>

          <c:if test="${isEdit}">
            <a class="btn btn-secondary" onclick="saveAsNewVersion()">Select as New Version</a>
          </c:if>

        </iais:action>
          <div class="col-xs-12 col-md-9">
            <div class="form-group" style="display: none" id="versionSelect">
              <label class="col-xs-12 col-md-7 control-label"> </label>
              <div class=" col-xs-7 col-sm-4 col-md-4">
                <select name="versionSelect" id="version">
                  <option value="">Select one</option>
                  <c:forEach items="${hcsaServiceConfigDto.hcsaServiceDtosVersion}" var="hcsaServiceDtosVer">
                    <option  value="<iais:mask name="crud_action_value"  value="${hcsaServiceDtosVer.id}"/>">${hcsaServiceDtosVer.version}</option>
                  </c:forEach>
                </select>
              </div>
            </div>
          </div>
          <div class="col-lg-12 col-xs-12" style="margin-bottom: 200px;">
            <div class="bg-title" style="text-align: center">
              <input style="display: none" value="${hcsaServiceConfigDto.hcsaServiceDto.version}" name="version" type="text">
              <p style="text-align: center">Version ${hcsaServiceConfigDto.hcsaServiceDto.version}</p>
            </div>
          </div>

       <%-- <div class="bg-title" style="text-align: center">
          <p style="text-align: center">Version 1.00</p>
        </div>--%>
      </div>
    </div>
    <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
  </form>
</div>
<%--<%@ include file="configRepeatJs.jsp" %>--%>
<iais:confirm msg="Are you sure you want to leave this page ?" callBack="kpi()" popupOrder="kpi" ></iais:confirm>

<iais:confirm msg="Are you sure you want to leave this page ?" callBack="checklists()" popupOrder="checklists" ></iais:confirm>

<iais:confirm msg="Are you sure you want to leave this page ?" callBack="riskScore()" popupOrder="riskScore" ></iais:confirm>

<iais:confirm msg="Are you sure you want to cancel?" yesBtnDesc="NO" cancelBtnDesc="YES" yesBtnCls="btn btn-secondary" cancelBtnCls="btn btn-primary" cancelFunc="cancel()" callBack="displays()" popupOrder="cancel"></iais:confirm>

<iais:confirm msg="Are you sure to delete ?" yesBtnDesc="NO" cancelBtnDesc="YES" yesBtnCls="btn btn-secondary" cancelBtnCls="btn btn-primary" cancelFunc="deleteConfirmYesOrNo()" callBack="cancelDelete()" popupOrder="deleteConfirmYesOrNo" ></iais:confirm>
<script type="text/javascript">
    $(document).ready(function () {
        premisesSelect();
        serviceTypeChange();
        toSupplementaryForm(false);
        controlEAS();
    });
    function cancel() {

        SOP.Crud.cfxSubmit("mainForm","back",'<iais:mask name="crud_action_value"  value="${hcsaServiceConfigDto.hcsaServiceDto.id}"/>',"");
    }

    function kpi() {

        location.href='https://${pageContext.request.serverName}/${pageContext.request.contextPath}<%=RedirectUtil.appendCsrfGuardToken("/eservice/INTRANET/MohKPIAndReminder",request)%>';

    }
    function deleteConfirmYesOrNo(){
        var val = $('#deleteConfirm').val();
        SOP.Crud.cfxSubmit("mainForm","delete",val,"");
    }
    function cancelDelete(){
       // SOP.Crud.cfxSubmit("mainForm","delete","","");
        $('#deleteConfirmYesOrNo').modal('hide');
    }
    function  displays() {
        $('#cancel').modal('hide');
    }

    $('#versionSelect').change(function () {
        if($('#version').val()==''){
        }else {
            SOP.Crud.cfxSubmit("mainForm","version",$('#version').val(),$('#version').val());
        }

    });

    function saveAsNewVersion() {
        $('#versionSelect').attr("style","display: block");
    }
    function  checklists(){
        location.href='https://${pageContext.request.serverName}/${pageContext.request.contextPath}<%=RedirectUtil.appendCsrfGuardToken("/eservice/INTRANET/MohChecklistConfiguration",request)%>';
    }

    function riskScore(){
        location.href='https://${pageContext.request.serverName}/${pageContext.request.contextPath}<%=RedirectUtil.appendCsrfGuardToken("/eservice/INTRANET/MohRiskConigMenu",request)%>';

    }


    function save() {
        showWaiting();
        submitPage("save",'<iais:mask name="crud_action_value"  value="${hcsaServiceConfigDto.hcsaServiceDto.id}"/>');
    }
    function submitPage(action,value) {
        SOP.Crud.cfxSubmit("mainForm",action,value,"");
    }


    $('#ServiceType').change(function () {
        serviceTypeChange();
        serviceDocpersonnel(true);
    });

    $('#serviceCode').change(function(){
        controlEAS();
    });
    function serviceTypeChange(){
        var val = $('#ServiceType').val();
        //for other service
        if("SVTP005"==val){
            forOtherService();
        //for bacse Service
        } else if("SVTP001"==val){
            forBaseService();
        //for specified service
        }else  if("SVTP003"==val){
            forSpecifiedService()
        // do not select,default show base
        }else {
            forBaseService();
            //forSpecifiedService()
        }
    }
    function forBaseService(){
        $('#selectCategoryId').show();
        $('#admndAndNotifactionFlow').hide();
        $('#baseAndSpeci').show();
        $('#msd').show();
        $('#businessInformation').show();
        $('#basePersionCount1').show();
        $('#basePersionCount2').show();
        $('#serviceTypeShow').html('licensable');
        $('#baseKpiConfig').show();
        $('#baseFlowConfig').show();
        $('#baseSubService').show();
        $('#specialisedSuppFormOnly').hide();
        $('#specialisedPersionnelOnly').hide();
        $('#basePersonnelAndSupplementary').show();


    }
    function forSpecifiedService(){
        $('#selectCategoryId').hide();
        $('#admndAndNotifactionFlow').show();
        $('#baseAndSpeci').show();
        $('#msd').hide();
        $('#businessInformation').hide();
        $('#basePersionCount1').hide();
        $('#basePersionCount2').hide();
        $('#serviceTypeShow').html('specialised');
        $('#baseKpiConfig').hide();
        $('#baseFlowConfig').hide();
        $('#baseSubService').hide();
        $('#specialisedSuppFormOnly').show();
        $('#specialisedPersionnelOnly').show();
        $('#basePersonnelAndSupplementary').hide();
    }
    function forOtherService(){
        $('#selectCategoryId').hide();
        $('#admndAndNotifactionFlow').show();
        $('#baseAndSpeci').hide();
    }

    function removeThis(obj) {
        $(obj).closest("div").closest("div.add").remove();
    }


    function addCategory(obj,name) {
        $(obj).closest("div").prev("div").after("<div class=\"add col-xs-12 col-md-9 marg-1\">\n" +
            "            <label class=\"col-xs-12 col-md-7 control-label\" >Category / Discipline</label>\n" +
            "            <div class=\"col-xs-10 col-md-4\">\n" +
            "              <input type=\"text\" maxlength=\"100\" value=\"\" name=\""+name+ "\">\n" +
            "            </div>\n" +
            "            <div class=\"col-xs-12 col-md-1\">\n" +
            "              <a class=\"btn  btn-secondary view\"  onclick=\"removeThis(this)\" >-</a>\n" +
            "            </div>\n" +
            "          </div>");
    }

    function addAsItem(obj,premisType,specOrOthers) {
        showWaiting();
        var data = {
            'premisType':premisType,
            'specOrOthers':specOrOthers
        };
        var levelName = premisType+"-"+specOrOthers+"-levels";
        $.ajax({
            'url':'${pageContext.request.contextPath}/getDropdownSelect',
            'dataType':'json',
            'data':data,
            'type':'POST',
            'success':function (data) {
                if('<%=AppConsts.AJAX_RES_CODE_SUCCESS%>' == data.resCode){
                    $(obj).closest("div").prev("div").after("<div class=\"add col-xs-12 col-md-12\"  style=\"margin-top: 20px ;margin-bottom: 20px\">\n" +
                        "              <div class=\"col-xs-12 col-md-5\" style=\"padding-right: 20%;\" >\n" + data.resultJson +
                        "              </div>\n" +
                        "              <div class=\"value\">\n" +
                        "                <input type=\"text\" value=\"0\" name=\""+levelName+"\" style=\"display: none\">\n" +
                        "              </div>\n" +
                        "              <div  class=\"col-xs-12 col-md-2\" style=\"padding-left: 3%;\" >\n" +
                        "                <a class=\"btn  btn-secondary  view\"  onclick=\"indents(this)\"   >indent</a>\n" +
                        "              </div>\n" +
                        "              <div  class=\"col-xs-12 col-md-2\" >\n" +
                        "                <a class=\"btn  btn-secondary view\"  onclick=\"outdent(this)\" >outdent</a>\n" +
                        "              </div>\n" +
                        "              <div class=\"col-xs-12 col-md-1\">\n" +
                        "                <a class=\"btn  btn-secondary view\"  onclick=\"removeThis(this)\" >-</a>\n" +
                        "              </div>\n" +
                        "            </div>");
                    $(obj).closest("div").prev("div").children("div").children("div").children("select").niceSelect();
                }else if('<%=AppConsts.AJAX_RES_CODE_VALIDATE_ERROR%>' == data.resCode){

                }else if('<%=AppConsts.AJAX_RES_CODE_ERROR%>' == data.resCode){

                }
            },
            'error':function () {

            }
        });
        dismissWaiting();

    }

    function indents(obj) {
        let serviceDropdown = $(obj).closest('div.add').children("div.col-md-5");
        var level = $(obj).closest('div.add').children("div.value").children();
        var levelValue = parseInt($(level).val());
        console.log(levelValue);
        if(levelValue <2){
            var length = 60 + 60*levelValue;
            $(serviceDropdown).attr("style","padding-right: 20%;margin-left:"+length+"px");
            $(level).attr("value",levelValue+1);
        }
    }

    function outdent(obj) {
        var serviceDropdown = $(obj).closest('div.add').children("div.col-md-5");
        var level = $(obj).closest('div.add').children("div.value").children();
        var levelValue = parseInt($(level).val());
        console.log(levelValue);
        if(levelValue >0){
            levelValue = levelValue-1;
            var length =  60*levelValue;
            $(serviceDropdown).attr("style","padding-right: 20%;margin-left:"+length+"px");
            $(level).attr("value",levelValue);

        }
    }
    $('#NumberDocument').keyup(function () {
        serviceDocpersonnel(false);
    });
    function serviceDocpersonnel(isReset){
        let val = $('#NumberDocument').val();
        if(val==''){
            val='0';
        }
        let number = parseInt(val);
        let number1 = parseInt($("#serviceNumberfields").children().length);
        if(isReset){
            number1 =0;
            $("#serviceNumberfields").html("");
        }


        let serviceType=$('#ServiceType').val();
        let serviceCode=$('#serviceCode').val();
        var suppFormSelect = $("input[name='supplementaryForm']:checked").val();
        showWaiting();
        var data = {
            'serviceType':serviceType,
            'serviceCode':serviceCode,
            'suppFormSelect':suppFormSelect
        };
        $.ajax({
            'url':'${pageContext.request.contextPath}/getSelectOptionForServiceDocPersonnel',
            'dataType':'json',
            'data':data,
            'type':'POST',
            'success':function (data) {
                if('<%=AppConsts.AJAX_RES_CODE_SUCCESS%>' == data.resCode){
                    var  selectOption =data.resultJson;
                    if(number-number1>0){
                        for(var i=0;i<number-number1;i++){
                            $("#serviceNumberfields").append(" <div class=\"form-group\">\n" +
                                "            <div class=\"col-xs-12 col-md-12\">\n" +
                                "             <input type=\"hidden\" value=\"\" name=\"serviceDocId\">\n" +
                                "              <label class=\"col-xs-12 col-md-5 control-label\" style=\"margin-right: 2%\">Name of Info Field</label>\n" +
                                "              <div class=\"col-xs-12 col-md-2\">\n" +
                                "                <input  type=\"text\" name=\"descriptionServiceDoc\" maxlength=\"255\">\n" +
                                "              </div>\n" +
                                "              <div class=\"col-xs-12 col-md-2 form-check\" style=\"margin-top: 1%\">\n" +
                                "                <input type=\"hidden\" name=\"serviceDocMandatory\" value=\"0\">\n" +
                                "                <input style=\"white-space: nowrap\" class=\"form-check-input\"  type=\"checkbox\" onclick=\"serviceCheckboxOnclick(this)\" name=\"descriptionServiceDocMandatory\">\n" +
                                "                <label style=\"white-space: nowrap\" class=\"form-check-label\" ><span class=\"check-square\"></span>Mandatory ?</label>\n" +
                                "              </div>\n" +
                                "              <div class=\"col-xs-12 col-md-2 form-check\" style=\"margin-top: 1%\">\n" +
                                "                <input type=\"hidden\" name=\"serviceDocPremises\" value=\"0\">\n" +
                                "                <input style=\"white-space: nowrap\" class=\"form-check-input\"  type=\"checkbox\" onclick=\"serviceCheckboxOnclick(this)\" name=\"descriptionServiceDocPremises\">\n" +
                                "                <label style=\"white-space: nowrap\" class=\"form-check-label\" ><span class=\"check-square\"></span>To duplicate for individual mode of service delivery ?</label>\n" +
                                "              </div>\n" +
                                "              <div class=\"col-xs-12 col-md-3 form-check\" style=\"margin-top: 1%\">\n" +
                                selectOption+
                                "              </div>\n" +
                                "            </div>\n" +
                                "          </div>");
                        }
                        $("select[name='selectDocPerson']").niceSelect();
                    }else if(number1-number>0){
                        for(var i=0;i<number1-number;i++){
                            $("#serviceNumberfields").children().last().remove();
                        }
                    }
                }
            },
            'error':function () {
            }
        });
        dismissWaiting();
    }



    function serviceCheckboxOnclick(checkbox) {
        if (checkbox.checked == true) {
            $(checkbox).prev().val("1");
        } else {
            $(checkbox).prev().val("0");
        }
    }

    function canApprove(obj) {

      if(obj.checked ==true){
          $(obj).prev().val('1');
      }else {
          $(obj).prev().val('0');
      }
    }

    function showRoutingStages(type){
        $('#routingStages>div').each(function (index,domEle){
            if($(domEle).attr('id')==type){
                $(domEle).show();
            }else{
                $(domEle).hide();
            }
        });
    }

    function premisesSelect(){
        $("input[name='premisesTypes']").each(function () {
            var values = $(this).val();
            //console.log(values);
            var specId = values+"-SVTP003";
            var otherId = values+"-SVTP005";
            //console.log($(this).prop('checked'));
          if($(this).prop('checked')){
            $('#'+specId).show();
            $('#'+otherId).show();
          }else{
              $('#'+specId).hide();
              $('#'+otherId).hide();
          }
        })
    }
    function  toSupplementaryForm(IsClick){
        var radioValue = $("input[name='supplementaryForm']:checked").val();
        if('1' == radioValue){
          $('#supplementaryForm').show();
        }else{
            $('#supplementaryForm').hide();
        }
        serviceDocpersonnel(IsClick);
    }

    function controlEAS() {
        var serviceCode = $('#serviceCode').val();
        if("EAS"==serviceCode || "MTS" == serviceCode){
            $('#cdDiv').show();
            $('#cgoDiv').hide();
        }else{
            $('#cdDiv').hide();
            $('#cgoDiv').show();
        }
    }
</script>
</>
