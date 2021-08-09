<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.ecquaria.cloud.RedirectUtil" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.helper.MessageUtil" %>
<webui:setLayout name="iais-intranet"/>

<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
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
<div class="main-content">
  <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <input type="hidden" name="selectCategoryValue" value="">
    <input type="text" style="display: none" name="serviceSvcCode" id="serviceSvcCode" value="${hcsaServiceDto.svcCode}">
    <div class="col-lg-12 col-xs-12">
      <div class="bg-title" style="text-align: center;">
        <h2>HCSA Service</h2>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-9">
          <h2 class="col-xs-0 col-md-7 component-title">Add HCSA Service</h2>
          <input type="text" style="display: none" name="serviceIsUse" value="false">
        </div>
      </div>
     <%-- <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="serviceName">Service Category<span class="mandatory" >*</span></label>
          <div class="col-xs-12 col-md-4">
            <select>
              <option>Please Select</option>
            </select>
            <span name="iaisErrorMsg" class="error-msg" id="error_serviceCategory"></span>
          </div>
        </div>
      </div>--%>
      <div class="form-group">
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" for="serviceName">Service Name&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="serviceName" type="text" name="serviceName" maxlength="100" value="${hcsaServiceDto.svcName}">
            <span name="iaisErrorMsg" class="error-msg" id="error_svcName"></span>
          </div>
        </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" for="description">Service Description&nbsp;<span class="mandatory" >*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="description" type="text" name="description" maxlength="255" value="${hcsaServiceDto.svcDesc}">
            <span class="error-msg" name="iaisErrorMsg" id="error_svcDesc"></span>
          </div>
        </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" for="displayDescription">Service Display Description&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="displayDescription" type="text" name="displayDescription" maxlength="255" value="${hcsaServiceDto.svcDisplayDesc}">
            <span class="error-msg" name="iaisErrorMsg" id="error_svcDisplayDesc"></span>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" for="serviceCode">Service Code&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="serviceCode" type="text" name="serviceCode" maxlength="3" value="${hcsaServiceDto.svcCode}">
            <span class="error-msg" name="iaisErrorMsg" id="error_svcCode"></span>
            <span name="iaisErrorMsg" class="error-msg" id="error_code"></span>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-9" >
          <label class="col-xs-12 col-md-7 control-label" for="ServiceType">Service Type&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4" style="margin-bottom: 20px;">
            <select id="ServiceType" name="ServiceType" >
              <option value="">Please Select</option>
              <c:forEach var="codeSelectOption" items="${codeSelectOptionList}">
                <option value="${codeSelectOption.value}" <c:if test="${hcsaServiceDto.svcType==codeSelectOption.value}">selected="selected"</c:if>>${codeSelectOption.text}</option>
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
            <select name="selectCategoryId" >
              <option value="">Please Select</option>
              <c:forEach items="${categoryDtos}" var="categoryDto">
                <option value="${categoryDto.desc}" <c:if test="${hcsaServiceDto.categoryId==categoryDto.id}">selected</c:if>>${categoryDto.name}</option>
              </c:forEach>
            </select>
            <span id="error_serviceCategory" class="error-msg" name="iaisErrorMsg"></span>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-12">
          <label class="col-xs-12 col-md-12 control-label" style="margin-bottom: 20px;">Mode of Service Delivery&nbsp;<span class="mandatory">*</span></label>
          <span class="error-msg" name="iaisErrorMsg" id="error_premieseType"></span>
        </div>
      </div>

      <div class="form-group">
        <div class="form-check-gp">
          <div class="row">
            <div class="col-xs-12 col-md-3">
              <div class="form-check " style="left: 10%">
                <c:set var="type" value="${PremisesType}"></c:set>
                <input class="form-check-input" name="PremisesType" id="icon3checkboxSample" <c:if test="${fn:contains(type,'ONSITE')}">checked="checked"</c:if> type="checkbox" value="ONSITE" name="Onsite" aria-invalid="false">
                <label class="form-check-label" for="icon3checkboxSample"><span class="check-square"></span>Premises</label>
              </div>
            </div>
            <div class="col-xs-12 col-md-3">
              <div class="form-check ">
                <input class="form-check-input"  name="PremisesType" id="icon4checkboxSample"  <c:if test="${fn:contains(type,'OFFSITE')}">checked="checked"</c:if> type="checkbox" value="OFFSITE" name="Offsite" aria-invalid="false">
                <label class="form-check-label" for="icon4checkboxSample"><span class="check-square"></span>Off-site</label>
              </div>
            </div>
            <div class="col-xs-12 col-md-3">
              <div class="form-check ">
                <input class="form-check-input"  name="PremisesType" id="icon5checkboxSample"  <c:if test="${fn:contains(type,'CONVEYANCE')}">checked="checked"</c:if> type="checkbox" value="CONVEYANCE" name="Conveyance" aria-invalid="false">
                <label class="form-check-label" for="icon5checkboxSample"><span class="check-square"></span>Conveyance</label>
              </div>
            </div>
          </div>
        </div>
      </div>


      <div class="form-group" style="display: none" id="Subsumption">
        <div class="col-xs-12 col-md-9"  style="margin-bottom: 10px">
          <label class="col-xs-12 col-md-7 control-label" ><iais:code code="CDN001"/> Subsumed Under&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <iais:multipleSelect name="Subsumption" selectValue="${selectSubsumption}" options="selsectBaseHcsaServiceDto"></iais:multipleSelect>
            <span id="error_Subsumption" class="error-msg" name="iaisErrorMsg" ></span>
          </div>
        </div>
      </div>

      <div class="form-group" style="display: none" id="Pre-requisite">
        <div class="col-xs-12 col-md-9" style="margin-bottom: 10px">
          <label class="col-xs-12 col-md-7 control-label" >Pre-requisite <iais:code code="CDN001"/>&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <iais:multipleSelect name="Pre-requisite" selectValue="${selectPreRequisite}" options="selsectBaseHcsaServiceDto"></iais:multipleSelect>
            <span id="error_Prerequisite" class="error-msg" name="iaisErrorMsg" ></span>
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

      <div class="form-group">
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" >Business Name<span class="mandatory">*</span></label>
          <div class="cl-xs-12 col-md-4">
            <div class="col-xs-12 col-md-6 form-check">
              <input  type="radio" <c:if test="${businessName=='1'}"> checked</c:if> class="form-check-input other-lic co-location" name="business-name"  value="1" >
              <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
            </div>
            <div class="col-xs-12 col-md-6 form-check">
              <input  type="radio" <c:if test="${businessName=='0'}"> checked</c:if> class="form-check-input other-lic co-location" name="business-name"  value="0">
              <label class="form-check-label" ><span class="check-circle"></span>No</label>
            </div>
            <span class="error-msg" class="form-check-input other-lic co-location" name="iaisErrorMsg" id="error_businessName"></span>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" >Principal Officer (PO)&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-2">
            <input type="text" name="man-principalOfficer" maxlength="2" placeholder="minimum count" value="${PO.pageMandatoryCount}">
            <span class="error-msg" name="iaisErrorMsg" id="error_mandatoryCount0"></span>
          </div>
          <div class="col-xs-12 col-md-2">
            <input type="text" name="mix-principalOfficer" maxlength="2" placeholder="maximum count" value="${PO.pageMaximumCount}">
            <span class="error-msg" name="iaisErrorMsg" id="error_maximumCount0"></span>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" >Nominee&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="man-DeputyPrincipalOfficer" maxlength="2" placeholder="minimum count" value="${DPO.pageMandatoryCount}">
            <span class="error-msg" name="iaisErrorMsg" id="error_mandatoryCount1"></span>
          </div>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="mix-DeputyPrincipalOfficer" maxlength="2" placeholder="maximum count" value="${DPO.pageMaximumCount}">
            <span class="error-msg" name="iaisErrorMsg" id="error_maximumCount1"></span>
          </div>
        </div>
      </div>


      <div class="form-group">
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" >Clinical Director&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-2">
            <input type="text" name="man-clinical_director" maxlength="2" placeholder="minimum count" value="${CD.pageMandatoryCount}">
            <span class="error-msg" name="iaisErrorMsg" id="error_mandatoryCount5"></span>
          </div>
          <div class="col-xs-12 col-md-2">
            <input type="text" name="mix-clinical_director" maxlength="2" placeholder="maximum count" value="${CD.pageMaximumCount}">
            <span class="error-msg" name="iaisErrorMsg" id="error_maximumCount5"></span>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" >Clinical Governance Officer (CGO)&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="man-ClinicalGovernanceOfficer" maxlength="2" placeholder="minimum count" value="${CGO.pageMandatoryCount}">
            <span class="error-msg" name="iaisErrorMsg" id="error_mandatoryCount2"></span>
          </div>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="mix-ClinicalGovernanceOfficer" maxlength="2"  placeholder="maximum count" value="${CGO.pageMaximumCount}">
            <span class="error-msg" name="iaisErrorMsg" id="error_maximumCount2"></span>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" >Service Personnel&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="man-ServicePersonnel" maxlength="2" placeholder="minimum count" value="${SVCPSN.pageMandatoryCount}">
            <span class="error-msg" name="iaisErrorMsg" id="error_mandatoryCount3"></span>
          </div>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="mix-ServicePersonnel" maxlength="2"  placeholder="maximum count" value="${SVCPSN.pageMaximumCount}">
            <span class="error-msg" name="iaisErrorMsg" id="error_maximumCount3"></span>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" >Vehicles&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-2">
            <input type="text" name="man-vehicles" maxlength="2" placeholder="minimum count" value="${VEH.pageMandatoryCount}">
            <span class="error-msg" name="iaisErrorMsg" id="error_mandatoryCount6"></span>
          </div>
          <div class="col-xs-12 col-md-2">
            <input type="text" name="mix-vehicles" maxlength="2" placeholder="maximum count" value="${VEH.pageMaximumCount}">
            <span class="error-msg" name="iaisErrorMsg" id="error_maximumCount6"></span>
          </div>
        </div>
      </div>


      <div class="form-group">
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" >General Conveyance Charges<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-2">
            <input type="text" name="man-charges" maxlength="2" placeholder="minimum count" value="${CHA.pageMandatoryCount}">
            <span class="error-msg" name="iaisErrorMsg" id="error_mandatoryCount7"></span>
          </div>
          <div class="col-xs-12 col-md-2">
            <input type="text" name="mix-charges" maxlength="2" placeholder="maximum count" value="${CHA.pageMaximumCount}">
            <span class="error-msg" name="iaisErrorMsg" id="error_maximumCount7"></span>
          </div>
        </div>
      </div>


      <div class="form-group">
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" >Medical Equipment and Other Charges<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-2">
            <input type="text" name="man-other-charges" maxlength="2" placeholder="minimum count" value="${CHAO.pageMandatoryCount}">
            <span class="error-msg" name="iaisErrorMsg" id="error_mandatoryCount8"></span>
          </div>
          <div class="col-xs-12 col-md-2">
            <input type="text" name="mix-other-charges" maxlength="2" placeholder="maximum count" value="${CHAO.pageMaximumCount}">
            <span class="error-msg" name="iaisErrorMsg" id="error_maximumCount8"></span>
          </div>
        </div>
      </div>


      <div class="form-group">
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" >MedAlert Person&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-2">
            <input value="${MAP.id}" name="svcpsnId" style="display:none;" type="text" maxlength="2">
            <input  type="text" name="man-MedalertPerson" value="${MAP.pageMandatoryCount}" maxlength="2" placeholder="minimum count">
            <span class="error-msg" name="iaisErrorMsg" id="error_mandatoryCount4"></span>
          </div>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="mix-MedalertPerson" value="${MAP.pageMaximumCount}" maxlength="2"  placeholder="maximum count">
            <span class="error-msg" name="iaisErrorMsg" id="error_maximumCount4"></span>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" for="NumberDocument">Number of Service-Related Document to be uploaded&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="NumberDocument" type="text" maxlength="2" name="NumberDocument" value="${serviceDocSize}">
            <span class="error-msg" name="iaisErrorMsg" id="error_NumberDocument"></span>
          </div>
        </div>
      </div>

      <div class="serviceNumberfields">
        <c:forEach items="${serviceDoc}" var="doc" varStatus="sta">
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
                <select name="selectDocPerson">
                  <option value="">To duplicate for the personnel?</option>
                  <option <c:if test="${doc.dupForPerson=='PO'}">selected</c:if> value="PO">Principal Officer (PO)</option>
                  <option <c:if test="${doc.dupForPerson=='DPO'}">selected</c:if> value="DPO">Nominee</option>
                  <option <c:if test="${doc.dupForPerson=='CGO'}">selected</c:if> value="CGO">Clinical Governance Officer (CGO)</option>
                  <option <c:if test="${doc.dupForPerson=='SVCPSN'}">selected</c:if> value="SVCPSN">Service Personnel</option>
                  <option <c:if test="${doc.dupForPerson=='MAP'}">selected</c:if> value="MAP">MedAlert Person </option>
                  <c:if test="${hcsaServiceDto.svcCode=='EAS' || hcsaServiceDto.svcCode=='MTS'}"><option <c:if test="${doc.dupForPerson=='CD'}">selected</c:if> value="CD">Clinical Director</option></c:if>
                </select>
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
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" for="Numberfields">Number of Service-Related General Info fields to be captured&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="Numberfields" type="text" name="Numberfields" maxlength="2" value="${comDocSize}">
            <span class="error-msg" name="iaisErrorMsg" id="error_Numberfields"></span>
          </div>
        </div>
      </div>

      <div class="Numberfields">
        <c:forEach items="${comDoc}" var="doc" varStatus="sta">
          <div class="form-group">
            <div class="col-xs-12 col-md-12">
              <label class="col-xs-12 col-md-5 control-label" style="margin-right: 2%">Name of Info Field</label>
              <input type="hidden" value="${doc.id}" name="commDocId">
              <div class="col-xs-12 col-md-2">
                <input  type="text" name="descriptionCommDoc" maxlength="255" value="${doc.docDesc}">
              </div>
              <div class="col-xs-12 col-md-2 form-check" style="margin-top: 1%">
                <input type="hidden" name="commDocMandatory"<c:choose><c:when test="${doc.isMandatory}"> value="1"</c:when><c:otherwise> value="0"</c:otherwise></c:choose>>
                <input class="form-check-input" <c:if test="${doc.isMandatory}">checked</c:if>  type="checkbox" onclick="checkboxOnclick(this)" name="descriptionCommDocMandatory">
                <label class="form-check-label" ><span class="check-square"></span>Mandatory ?</label>
              </div>
              <div class="col-xs-12 col-md-2 form-check" style="margin-top: 1%">
                <input type="hidden" name="commDocPremises" <c:choose><c:when test="${doc.dupForPrem=='1'}"> value="1"</c:when><c:otherwise> value="0"</c:otherwise></c:choose> >
                <input style="white-space: nowrap" class="form-check-input" <c:if test="${doc.dupForPrem=='1'}">checked</c:if>   type="checkbox" onclick="checkboxOnclick(this)" name="descriptionCommDocPremises">
                <label style="white-space: nowrap" class="form-check-label" ><span class="check-square"></span>To duplicate for individual mode of service delivery ?</label>
              </div>
              <div class="col-xs-12 col-md-5" style="margin-right: 2%">
              </div>
              <div class="col-xs-12 col-md-4">
                <span class="error-msg" name="iaisErrorMsg" id="error_commonDoc${sta.index}"></span>
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
              <a class="btn btn-secondary " data-toggle="modal" data-target= "#checklists" ><span class="view">Configure</span></a>
            </div>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-9  marg-1">
          <label class="col-xs-12 col-md-7 control-label" >Service Risk Score</label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary "  data-toggle="modal" data-target= "#riskScore"><span class="view">Configure</span></a>
            </div>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-9 marg-1">
          <label class="col-xs-12 col-md-7 control-label" >Service KPI</label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary " data-toggle="modal" data-target= "#kpi" ><span class="view">Configure</span></a>
            </div>
          </div>
        </div>
      </div>

      <%--<div class="form-group">
        <div class="col-xs-12 col-md-8 marg-1">
          <label class="col-xs-12 col-md-8 control-label" >Service Fees</label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary "><span class="view">Configure</span></a>
            </div>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8  marg-1" >
          <label class="col-xs-12 col-md-8 control-label" >Service Fee Bundles</label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary "><span class="view">Configure</span></a>
            </div>
          </div>
        </div>
      </div>
--%>

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
              <a class="btn btn-secondary width-70"  onclick="showNEW()"><span <c:if test="${errorMap['APTY002']!=null}">style="color: #ff0000" </c:if> class="view">NEW APPLICATION</span></a>
            </div>
          </div>
          <div class="col-xs-10 col-md-6">
            <div class="components width-center">
              <a class="btn btn-secondary width-70"  onclick="showRENEW()"><span <c:if test="${errorMap['APTY004']!=null}">style="color: #ff0000" </c:if> class="view">RENEW</span></a>
            </div>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-12" style="margin-top: 1%">
          <div class="col-xs-10 col-md-6">
            <div class="components width-center">
              <a class="btn btn-secondary width-70"  onclick="showAPPEAL()"><span <c:if test="${errorMap['APTY001']!=null}">style="color: #ff0000" </c:if> class="view">APPEAL</span></a>
            </div>
          </div>
          <div class="col-xs-10 col-md-6">
            <div class="components width-center">
              <a class="btn btn-secondary width-70"  onclick="showRFC()"><span <c:if test="${errorMap['APTY005']!=null}">style="color: #ff0000" </c:if> class="view">REQUEST FOR CHANGE</span></a>
            </div>
          </div>
        </div>
      </div>

      <div  class="form-group">
        <div class="col-xs-12 col-md-12" style="margin-top: 1%">
          <div class="col-xs-10 col-md-6">
            <div class="components  width-center">
              <a class="btn btn-secondary width-70"  onclick="showCESSATION()"><span <c:if test="${errorMap['APTY008']!=null}">style="color: #ff0000" </c:if> class="view">CESSATION</span></a>
            </div>
          </div>
         <%-- <div class="col-xs-10 col-md-6">
            <div class="components  width-center">
              <a class="btn btn-secondary width-70" onclick="showSUSPENSION()"><span class="view">SUSPENSION</span></a>
            </div>
          </div>--%>
          <div class="col-xs-10 col-md-6">
            <div class="components width-center">
              <a class="btn btn-secondary width-70" onclick="showWITHDRAWAL()"><span  <c:if test="${errorMap['APTY006']!=null}">style="color: #ff0000" </c:if> class="view">WITHDRAWAL</span></a>
            </div>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-12" style="margin-top: 1%">
          <%--  <div class="col-xs-10 col-md-6">
              <div class="components  width-center">
                <a class="btn btn-secondary width-70" onclick="showREVOCATION()"><span class="view">REVOCATION</span></a>
              </div>
            </div>--%>
        </div>
      </div>

      <c:set var="index" value="0"></c:set>
      <c:forEach items="${routingStagess}" var="routingStages" varStatus="sta">
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
            <select name="isMandatory${routingStage.stageCode}${routingStages.key}">
              <option value="">Please Select</option>
              <option value="mandatory" <c:if test="${routingStage.isMandatory=='true'}">selected="selected"</c:if>>Mandatory</option>
              <option value="optional" <c:if test="${routingStage.isMandatory=='false'}">selected="selected"</c:if>>Optional</option>
            </select>
            <span name="iaisErrorMsg" class="error-msg" id="error_isMandatory${routingStages.key}${status.index}"></span>
            <br>
            <c:if test="${routingStage.stageCode=='AO1'|| routingStage.stageCode=='AO2'}">
              <input type="hidden" value="${routingStage.canApprove}" name="canApprove${routingStage.stageCode}${routingStages.key}">
              <input type="checkbox"  <c:if test="${routingStage.canApprove=='1'}">checked</c:if> onclick="canApprove(this)" /><span>&nbsp;Can Approve ?</span>
            </c:if>

          </div>
          <div class="col-xs-12 col-md-6" style="margin-top: 1%;margin-bottom: 1%;text-align:left">
            <select  name="RoutingScheme${routingStage.stageCode}${routingStages.key}"  >
              <option value="" >Please Select</option>
              <option value="common"
                      <c:choose>
                        <c:when test="${routingStage.routingSchemeName=='common'}">
                          selected="selected"
                        </c:when>
                      </c:choose>
              >Common Pool</option>
              <option value="round"
                      <c:choose>
                        <c:when test="${routingStage.routingSchemeName=='round'}">
                          selected="selected"
                        </c:when>
                      </c:choose>

              >Round Robin</option>
              <option value="assign"
                      <c:if test="${routingStage.routingSchemeName=='assign'}">selected="selected" </c:if>
              >Supervisor Assign</option>

           </select>
            <c:if test="${routingStage.stageCode=='INS'}">
              <p>Inspector</p>
            </c:if>
            <c:if test="${routingStage.stageCode=='INS'}">
              <c:forEach items="${routingStage.hcsaSvcSpeRoutingSchemeDtos}" var="hcsaSvcSpeRoutingSchemeDto">
                <select  name="RoutingScheme${routingStage.stageCode}${routingStages.key}${hcsaSvcSpeRoutingSchemeDto.insOder}"  >
                  <option value="" >Please Select</option>
                  <option value="common"
                          <c:choose>
                            <c:when test="${hcsaSvcSpeRoutingSchemeDto.schemeType=='common'}">
                              selected="selected"
                            </c:when>
                          </c:choose>
                  >Common Pool</option>
                  <option value="round"
                          <c:choose>
                            <c:when test="${hcsaSvcSpeRoutingSchemeDto.schemeType=='round'}">
                              selected="selected"
                            </c:when>
                          </c:choose>
                  >Round Robin</option>
                  <option value="assign"
                          <c:if test="${hcsaSvcSpeRoutingSchemeDto.schemeType=='assign'}">selected="selected" </c:if>
                  >Supervisor Assign</option>
                </select>
                <c:if test="${hcsaSvcSpeRoutingSchemeDto.insOder==0}">
                  <p>Inspector AO1</p>
                </c:if>
                <c:if test="${hcsaSvcSpeRoutingSchemeDto.insOder==1}">
                  <p>Inspector Lead</p>
                </c:if>
              </c:forEach>
            </c:if>
            <span  name="iaisErrorMsg" class="error-msg" id="error_schemeType${routingStages.key}${status.index}"></span>
          </div>

        </td>
        <td>
          <div class="col-xs-12 col-md-12" style="text-align:left">
            <input style="margin: 0px 0px" type="text" maxlength="2" name="WorkloadManhours${routingStage.stageCode}${routingStages.key}" value="${routingStage.manhours}" >
            <span class="error-msg" name="iaisErrorMsg" id="error_manhourCount${routingStages.key}${status.index}"></span>
          </div>

        </td>
       <%-- <td>
          <div class="col-xs-12 col-md-12" style="text-align:left">
            <select name="workingGroup${routingStage.stageCode}${routingStages.key}" disabled>
              <option value="">Please Select</option><c:forEach items="${routingStage.workingGroup}" var="workingGroup">
              <option <c:if test="${routingStage.workingGroupId==workingGroup.id}">selected="selected"</c:if>value="${workingGroup.id}">${workingGroup.groupName}</option>
            </c:forEach>
            </select>
            <span name="iaisErrorMsg" class="error-msg" id="error_stageWorkGroupId${routingStages.key}${status.index}"></span>
          </div>
        </td>--%>
      </tr>
        </c:forEach>
      </table>
        </div>
      </div>
      </c:forEach>


      <div class="form-group">
        <div class="col-xs-12 col-md-12 Sub-Types" style="margin-top: 20px ;margin-bottom: 20px">
          <label class="col-xs-12 col-md-12 control-label">Service Sub-Types</label>
        </div>
        <div class="col-xs-12 col-md-9 marg-1">
          <label class="col-xs-12 col-md-7 control-label" >Page Name</label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <input type="text" maxlength="100" value="${pageName}" name="pageName">
              <span name="iaisErrorMsg" class="error-msg" id="error_pageName"></span>
            </div>
          </div>
        </div>
        <div class="col-xs-12 col-md-12 Sub-Types" style="margin-top: 20px ;margin-bottom: 20px">
          <div class="col-xs-12 col-md-12">

          </div>
          <c:set value="1" var="j"></c:set>
            <c:forEach items="${hcsaSvcSubtypeOrSubsumedDto}" var="hcsaSvcSubtypeOrSubsumed" varStatus="index">
              <div class="view col-xs-12 col-md-12" >
                <div class="col-xs-12 col-md-4" style="padding-right: 20%;" >
                  <input class="add" type="text"  style="margin-left:0px" name="subType" maxlength="100" value="${hcsaSvcSubtypeOrSubsumed.name}">
                  <span name="iaisErrorMsg" class="error-msg white-space" id="error_hcsaSvcSubtypeOrSubsumed${j}"></span>
                  <c:set value="${j+1}" var="j"></c:set>
                </div>
                <div class="value">
                  <input type="text" value="0" name="level" style="display: none">
                </div>
                <div  class="col-xs-12 col-md-2" >
                  <a class="btn  btn-secondary  view"  onclick="indents(this)"   >indent</a>
                </div>
                <div  class="col-xs-12 col-md-2">
                  <a class="btn  btn-secondary view"  onclick="outdent(this)" >outdent</a>
                </div>
                <div class="col-xs-12 col-md-2 up">
                  <a class="btn  btn-secondary up view" onclick="up(this)" style="margin-bottom: 10%;width:60%;">UP</a>
                  <a class="btn  btn-secondary down view" onclick="down(this)" style="margin-bottom: 10%;width:60%;">DOWN</a>
                </div>
                <div class="col-xs-12 col-md-2">
                  <a class="btn  btn-secondary view"  onclick="removeThis(this)" >-</a>
                </div>
              </div>
              <c:forEach items="${hcsaSvcSubtypeOrSubsumed.list}" var="hcsaSvcSubtypeOrSubsumed2">
                <div class="view col-xs-12 col-md-12">
                  <div class="col-xs-12 col-md-4" style="padding-right: 20%;" >
                    <input class="add" type="text"  style="margin-left:60px" maxlength="100" name="subType" value="${hcsaSvcSubtypeOrSubsumed2.name}">
                    <span style="margin-left:60px" name="iaisErrorMsg" class="error-msg white-space"  id="error_hcsaSvcSubtypeOrSubsumed${j}"></span>
                    <c:set value="${j+1}" var="j"></c:set>
                  </div>

                  <div class="value">
                    <input type="text" value="1" name="level" style="display: none" >
                  </div>
                  <div  class="col-xs-12 col-md-2" >
                    <a class="btn  btn-secondary  view" onclick="indents(this)"   >indent</a>
                  </div>
                  <div  class="col-xs-12 col-md-2">
                    <a class="btn  btn-secondary view"  onclick="outdent(this)" >outdent</a>
                  </div>
                  <div class="col-xs-12 col-md-2 up">
                    <a class="btn  btn-secondary up view"onclick="up(this)" style="margin-bottom: 10%;width:60%;">UP</a>
                    <a class="btn  btn-secondary down view" onclick="down(this)" style="margin-bottom: 10%;width:60%;">DOWN</a>
                  </div>
                  <div class="col-xs-12 col-md-2">
                    <a class="btn  btn-secondary view"  onclick="removeThis(this)" >-</a>
                  </div>
                </div>
                <c:forEach items="${hcsaSvcSubtypeOrSubsumed2.list}" var="hcsaSvcSubtypeOrSubsumed3">
                  <div class="view col-xs-12 col-md-12">
                    <div class="col-xs-12 col-md-4" style="padding-right: 20%;" >
                      <input class="add" type="text"  style="margin-left:120px" maxlength="100" name="subType" value="${hcsaSvcSubtypeOrSubsumed3.name}">
                      <span name="iaisErrorMsg" style="margin-left:120px" class="error-msg white-space" id="error_hcsaSvcSubtypeOrSubsumed${j}"></span>
                      <c:set value="${j+1}" var="j"></c:set>
                    </div>

                    <div class="value">
                      <input type="text" value="2" name="level" style="display: none" >
                    </div>
                    <div  class="col-xs-12 col-md-2" >
                      <a class="btn  btn-secondary  view" onclick="indents(this)"   >indent</a>
                    </div>
                    <div  class="col-xs-12 col-md-2">
                      <a class="btn  btn-secondary view"  onclick="outdent(this)" >outdent</a>
                    </div>
                    <div class="col-xs-12 col-md-2 up">
                      <a class="btn  btn-secondary up view" onclick="up(this)" style="margin-bottom: 10%;width:60%;">UP</a>
                      <a class="btn  btn-secondary down view" onclick="down(this)" style="margin-bottom: 10%;width:60%;">DOWN</a>
                    </div>
                    <div class="col-xs-12 col-md-2">
                      <a class="btn  btn-secondary view"  onclick="removeThis(this)" >-</a>
                    </div>
                  </div>
                </c:forEach>
              </c:forEach>
            </c:forEach>

          <div class="col-xs-12 col-md-12">
            <a  class="btn  btn-secondary "   style="margin-right: 10px" id="addAsItem" onclick="addAsItem(this)"> + </a><label for="addAsItem"> Add Item</label>
          </div>
        </div>
      </div>

      <div class="col-xs-12 col-md-9">
        <div class="form-group">
          <label class="col-xs-12 col-md-7 control-label">Effective Start Date&nbsp;<span class="mandatory">*</span></label>
          <div class=" col-xs-7 col-sm-4 col-md-3">
            <input type="text" value="${hcsaServiceDto.effectiveDate}" autocomplete="off" class="date_picker form-control form_datetime" name="StartDate" id="-20189532301300" data-date-start-date="01/01/1900" placeholder="dd/mm/yyyy" maxlength="10"><span id="error_StartDate" name="iaisErrorMsg" class="error-msg" ></span>
            <span class="error-msg" name="iaisErrorMsg" id="error_effectiveDate"></span>
          </div>
          <div class="clear"></div>
        </div>
      </div>

    <div class="col-xs-12 col-md-9" style="margin-bottom: 50px;">
      <div class="form-group">
        <label class="col-xs-12 col-md-7 control-label">Effective End Date</label>
        <div class=" col-xs-7 col-sm-4 col-md-3">
          <input type="text" autocomplete="off" value="<fmt:formatDate value="${hcsaServiceDto.endDate}" pattern="dd/MM/yyyy"/>" class="date_picker form-control form_datetime" name="EndDate" id="-20247433206800" data-date-start-date="01/01/1900" placeholder="dd/mm/yyyy" maxlength="10"><span id="error_EndDate" name="iaisErrorMsg" class="error-msg"></span>
          <span class="error-msg" name="iaisErrorMsg" id="error_effectiveEndDate"></span>
        </div>
        <div class="clear"></div></div>
    </div>
      <div class="col-lg-12 col-xs-12">
        <iais:action style="text-align:center;">
          <a class="btn btn-secondary" data-toggle="modal" data-target="#cancel">Cancel</a>
          <button class="btn btn-primary" onclick="save()">Save</button>
        </iais:action>
        <div class="bg-title" style="text-align: center">
          <p style="text-align: center">Version 1.00</p>
        </div>
      </div>
    </div>
    <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
  </form>
</div>
<%@ include file="configRepeatJs.jsp" %>
<iais:confirm msg="Are you sure you want to leave this page ?" callBack="kpi()" popupOrder="kpi" ></iais:confirm>

<iais:confirm msg="Are you sure you want to leave this page ?" callBack="checklists()" popupOrder="checklists" ></iais:confirm>

<iais:confirm msg="Are you sure you want to leave this page ?" callBack="riskScore()" popupOrder="riskScore" ></iais:confirm>

<iais:confirm msg="Are you sure you want to cancel?" yesBtnDesc="NO" cancelBtnDesc="YES" yesBtnCls="btn btn-secondary" cancelBtnCls="btn btn-primary" cancelFunc="cancel()" callBack="displays()" popupOrder="cancel"></iais:confirm>
<script type="text/javascript">
    $(document).ready(function () {
        a();
    });
    function cancel() {

        SOP.Crud.cfxSubmit("mainForm","back","back","");
    }

    function kpi() {

        location.href='https://${pageContext.request.serverName}/${pageContext.request.contextPath}<%=RedirectUtil.appendCsrfGuardToken("/eservice/INTRANET/MohKPIAndReminder",request)%>';

    }
    function  displays() {
      $('#cancel').modal('hide');
    }

    function  checklists(){
        location.href='https://${pageContext.request.serverName}/${pageContext.request.contextPath}<%=RedirectUtil.appendCsrfGuardToken("/eservice/INTRANET/MohChecklistConfiguration",request)%>';
    }

    function riskScore(){
        location.href='https://${pageContext.request.serverName}/${pageContext.request.contextPath}<%=RedirectUtil.appendCsrfGuardToken("/eservice/INTRANET/MohRiskConigMenu",request)%>';

    }

    function manhours(){
        location.href="";
    }

    function save() {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm","save");
    }

    $(document).ready(function () {
        let val = $('#ServiceType').val();
        if("SVTP001"==val){
            $('#selectCategoryId').attr("style","display:block");
            $('#Subsumption').attr("style","display:none");
        } else if("SVTP002"==val){
            $('#Subsumption').attr("style","display:block");
            $('#Pre-requisite').attr("style","display:none");
            $('#selectCategoryId').attr("style","display:none");
        }else  if("SVTP003"==val){
            $('#Pre-requisite').attr("style","display:block");
            $('#Subsumption').attr("style","display:none");
            $('#selectCategoryId').attr("style","display:block");
          $("select[name='selectCategoryId']").next().find('.current').html('Special Licensable Healthcare Services');
          $("select[name='selectCategoryId']").next().attr('class','nice-select disabled');
          $("select[name='selectCategoryId']").val('Special Licensable Service')
        }else {
            $('#Subsumption').attr("style","display:none");
            $('#Pre-requisite').attr("style","display:none");
            $('#selectCategoryId').attr("style","display:none");
            $("select[name='selectCategoryId']").next().attr('class','nice-select');
        }
    });




    $('#ServiceType').change(function () {
        var val = $('#ServiceType').val();
        if("SVTP001"==val){
            $('#selectCategoryId').attr("style","display:block");
            $('#Subsumption').attr("style","display:none");
            $('#Pre-requisite').attr("style","display:none");
          $("select[name='selectCategoryId']").next().attr('class','nice-select');
        } else if("SVTP002"==val){
            $('#Subsumption').attr("style","display:block");
            $('#Pre-requisite').attr("style","display:none");
            $('#selectCategoryId').attr("style","display:none");
          $("select[name='selectCategoryId']").next().attr('class','nice-select');
        }else  if("SVTP003"==val){
            $('#Pre-requisite').attr("style","display:block");
            $('#Subsumption').attr("style","display:none");
            $('#selectCategoryId').attr("style","display:block");
            $("select[name='selectCategoryId']").next().find('.current').html('Special Licensable Healthcare Services');
            $("select[name='selectCategoryId']").next().attr('class','nice-select disabled');
            $("select[name='selectCategoryId']").val('Special Licensable Service')
        }else {
            $('#Subsumption').attr("style","display:none");
            $('#Pre-requisite').attr("style","display:none");
            $('#selectCategoryId').attr("style","display:none");
          $("select[name='selectCategoryId']").next().attr('class','nice-select');
        }

    });

    function removeThis(obj) {
        $(obj).closest("div").closest("div.view").remove();
        a();
    }

    function up(obj) {
        let val = $(obj).closest("div").closest("div.view").children('div.col-xs-12.col-md-4').children("input");
        let val1 = $(obj).closest("div").closest("div.view").prev("div.view").children('div.col-xs-12.col-md-4').children("input");
        if("undefined" !=typeof val1.val()){
            let upValue=val.val();
            let upValue1=val1.val();
            val.val(upValue1);
            val1.val(upValue);
            val.html(upValue1);
            val1.html(upValue);
        }
    }

    function down(obj) {
        let val = $(obj).closest("div").closest("div.view").children('div.col-xs-12.col-md-4').children("input");
        let val1 = $(obj).closest("div").closest("div.view").next("div.view").children('div.col-xs-12.col-md-4').children("input");
        if("undefined" !=typeof val1.val()){
            let upValue=val.val();
            let upValue1=val1.val();
            val.val(upValue1);
            val1.val(upValue);
            val.html(upValue1);
            val1.html(upValue);
        }
    }
    function addAsItem(obj) {
        $(obj).closest("div").prev("div").after(" <div class=\"view col-xs-12 col-md-12\">\n" +
            "          <div class=\"col-xs-12 col-md-4\" style=\"padding-right: 20%;\" >\n" +
            "            <input class=\"add\" type=\"text\"  style=\"\" name=\"subType\">\n" +
            "          </div>\n" +
            "            <div class=\"value\">\n" +
            "              <input type=\"text\" value=\"0\" name=\"level\"  style=\"display: none\">\n" +
            "            </div>\n" +
            "          <div  class=\"col-xs-12 col-md-2\" >\n" +
            "            <a class=\"btn  btn-secondary  view\" onclick=\"indents(this)\"   >indent</a>\n" +
            "          </div>\n" +
            "          <div  class=\"col-xs-12 col-md-2\">\n" +
            "            <a class=\"btn  btn-secondary view\"  onclick=\"outdent(this)\" >outdent</a>\n" +
            "          </div>\n" +
            "           <div class=\"col-xs-12 col-md-2 up\">\n" +
            "                    <a class=\"btn  btn-secondary up view\" onclick=\"up(this)\" style=\"margin-bottom: 10%;width:60%;\">UP</a>\n" +
            "                    <a class=\"btn  btn-secondary down view\" onclick=\"down(this)\" style=\"margin-bottom: 10%;width:60%;\">DOWN</a>\n" +
            "                  </div>\n" +
            "                  <div class=\"col-xs-12 col-md-2\">\n" +
            "                    <a class=\"btn  btn-secondary view\"  onclick=\"removeThis(this)\" >-</a>\n" +
            "                  </div>"+
            "          </div>");
          a();
    }

   var  a = function upDown() {
        let length = $('#addAsItem').closest("div").closest("div.Sub-Types").children("div.view").length;
        if (length == 1) {
            $('#addAsItem').closest("div").closest("div.Sub-Types").children("div.view").children('.up').attr("style","display: none");
        } else {
            $('#addAsItem').closest("div").closest("div.Sub-Types").children("div.view").children('.up').removeAttr("style");
        }
    }
    function indents(obj) {
        let jQuery = $(obj).closest('div.view').children("div.col-md-4").children();
        let jQuery2 = $(obj).closest('div.view').children("div.value").children();
        var jQuery1 = jQuery.attr("style");
        if(jQuery1!=""){
            var length=jQuery1.split(":")[1];
            var a;
          if(length.length==5){
              a=   jQuery1.split(":")[1].substring(0,3);
          }else if(length.length==4){
              a=   jQuery1.split(":")[1].substring(0,2);
          }else if(length.length==3){
              a=   jQuery1.split(":")[1].substring(0,1);
          }

          a= parseInt(a)+60;
          if(a>=120){

              $(jQuery).attr("style","margin-left:"+120+"px");

              jQuery2.val(2);
          }else {

              $(jQuery).attr("style","margin-left:"+a+"px");

              jQuery2.val(parseInt(jQuery2.val())+1);

             ;
          }
        }else {
            jQuery2.val(parseInt(jQuery2.val())+1);
            $(jQuery).attr("style","margin-left:60px");

        }
    }

    function outdent(obj) {
        let jQuery = $(obj).closest('div.view').children("div.col-md-4").children();
        let jQuery2 = $(obj).closest('div.view').children("div.value").children();
        var jQuery1 = jQuery.attr("style");
        if(jQuery1!=""){
            var length=jQuery1.split(":")[1];
            var a;
            if(length.length==6){
                a=   jQuery1.split(":")[1].substring(0,4);
            }else if(length.length==5){
                a=   jQuery1.split(":")[1].substring(0,3);
            }else if(length.length==4){
                a=   jQuery1.split(":")[1].substring(0,2);
            }else if(length.length==3){
                a=   jQuery1.split(":")[1].substring(0,1);
            }
            a= parseInt(a)-60;
            if(a<=0){
                $(jQuery).attr("style","margin-left:"+0+"px");
                jQuery2.val(0);
            }else {
                $(jQuery).attr("style","margin-left:"+a+"px");
                jQuery2.val(parseInt(jQuery2.val())-1);
            }

        }else {
            $(jQuery).attr("style","")
        }

    }


    function checkboxOnclick(checkbox) {
        if (checkbox.checked == true) {
            $(checkbox).prev().val("1");
        } else {
            $(checkbox).prev().val("0");
        }
    }

        $('#NumberDocumentMandatory').click(function () {
        let jQuery = $("#NumberDocumentMandatory").prop("checked");
        let jQuery1 = $("#DescriptionDocumentMandatory").prop("checked");
        if(jQuery==true){
            $("#DescriptionDocumentMandatory").prop("checked",true);
            $("#NumberDocumentMandatory").prop("checked",true);
        }else if(jQuery==false){
            $("#DescriptionDocumentMandatory").prop("checked",false);
            $("#NumberDocumentMandatory").prop("checked",false);
        }
    });
    $('#DescriptionDocumentMandatory').click(function () {

        let jQuery = $("#NumberDocumentMandatory").prop("checked");
        let jQuery1 = $("#DescriptionDocumentMandatory").prop("checked");
        if(jQuery1==true){
            $("#DescriptionDocumentMandatory").prop("checked",true);
            $("#NumberDocumentMandatory").prop("checked",true);
        }else if(jQuery1==false){
            $("#DescriptionDocumentMandatory").prop("checked",false);
            $("#NumberDocumentMandatory").prop("checked",false);
        }

    });


    $('#Numberfields').keyup(function () {
        let val = $('#Numberfields').val();
        if(val==''){
          val='0';
        }
        let number = parseInt(val);
        let jQuery = $(this).closest("div.form-group").next(".Numberfields").children();
        let number1 = parseInt(jQuery.length);
        if(number-number1>0){
            for(var i=0;i<number-number1;i++){
                $(this).closest("div.form-group").next(".Numberfields").append(" <div class=\"form-group\">\n" +
                    "            <div class=\"col-xs-12 col-md-12\">\n" +
                    "           <input type=\"hidden\" value=\"\" name=\"commDocId\">\n" +
                    "              <label class=\"col-xs-12 col-md-5 control-label\" style=\"margin-right: 2%\">Name of Info Field</label>\n" +
                    "              <div class=\"col-xs-12 col-md-2\">\n" +
                    "                <input  type=\"text\" name=\"descriptionCommDoc\" maxlength=\"255\">\n" +
                    "              </div>\n" +
                    "              <div class=\"col-xs-12 col-md-2 form-check\" style=\"margin-top: 1%\">\n" +
                    "                <input type=\"hidden\" name=\"commDocMandatory\" value=\"0\">\n" +
                    "                <input  style=\"white-space: nowrap\" class=\"form-check-input\"  type=\"checkbox\" onclick=\"checkboxOnclick(this)\" name=\"descriptionCommDocMandatory\">\n" +
                    "                <label  style=\"white-space: nowrap\" class=\"form-check-label\" ><span class=\"check-square\"></span>Mandatory ?</label>\n" +
                    "              </div>\n" +
                    "              <div class=\"col-xs-12 col-md-2 form-check\" style=\"margin-top: 1%\">\n" +
                    "                <input type=\"hidden\" name=\"commDocPremises\" value=\"0\">\n" +
                    "                <input  style=\"white-space: nowrap\" class=\"form-check-input\"  type=\"checkbox\" onclick=\"checkboxOnclick(this)\" name=\"descriptionCommDocPremises\">\n" +
                    "                <label  style=\"white-space: nowrap\" class=\"form-check-label\" ><span class=\"check-square\"></span>To duplicate for individual mode of service delivery ?</label>\n" +
                    "              </div>\n" +
                    "            </div>\n" +
                    "          </div>");
            }
        }else if(number1-number>0){
            for(var i=0;i<number1-number;i++){
                $(this).closest("div.form-group").next(".Numberfields").children().last().remove();
            }
        }

    });

    $('#NumberDocument').keyup(function () {
        let val = $('#NumberDocument').val();
        if(val==''){
          val='0';
        }
        let number = parseInt(val);
        let jQuery = $(this).closest("div.form-group").next(".serviceNumberfields").children();
        let number1 = parseInt(jQuery.length);
        let svcCd=$('#serviceSvcCode').val();
        let cd='';
        let cd1='';
        if(svcCd=='EAS'||svcCd=='MTS'){
          cd="                   <option value=\"CD\">Clinical Director ?</option>\n";
          cd1="     <li data-value=\"CD\" class=\"option\">Clinical Director </li>\n";
        }
        if(number-number1>0){
            for(var i=0;i<number-number1;i++){
                $(this).closest("div.form-group").next(".serviceNumberfields").append(" <div class=\"form-group\">\n" +
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
                    "                <select name=\"selectDocPerson\" style=\"display: none;\">\n" +
                    "                   <option value=\"\">To duplicate for the personnel?</option>\n" +
                    "                   <option value=\"PO\">Principal Officer (PO)?</option>\n" +
                    "                   <option value=\"DPO\">Nominee?</option>\n" +
                    "                   <option value=\"CGO\">Clinical Governance Officer (CGO)?</option>\n" +
                    "                   <option value=\"SVCPSN\">Service Personnel ?</option>\n" +
                    "                   <option value=\"MAP\">MedAlert Person ?</option>\n" +
                        cd+
                    "                 </select>\n" +
                    "  <div class=\"nice-select\" tabindex=\"0\">\n"+
                    "   <span class=\"current\">To duplicate for the personnel?</span>\n"+
                    "   <ul class=\"list\">\n"+
                    "     <li data-value=\"\" class=\"option selected\">To duplicate for the personnel?</li>\n"+
                    "     <li data-value=\"PO\" class=\"option\">Principal Officer (PO)</li>\n"+
                    "     <li data-value=\"DPO\" class=\"option\">Nominee</li>\n"+
                     "    <li data-value=\"CGO\" class=\"option\">Clinical Governance Officer (CGO)</li>\n"+
                    "     <li data-value=\"SVCPSN\" class=\"option\">Service Personnel</li>\n"+
                    "     <li data-value=\"MAP\" class=\"option\">MedAlert Person </li>\n"+
                        cd1+
                    "   </ul>\n"+
                    "  </div>\n"+
                    "              </div>\n" +
                    "            </div>\n" +
                    "          </div>");
            }
        }else if(number1-number>0){
            for(var i=0;i<number1-number;i++){
                $(this).closest("div.form-group").next(".serviceNumberfields").children().last().remove();
            }
        }

    });


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
</script>
</>
