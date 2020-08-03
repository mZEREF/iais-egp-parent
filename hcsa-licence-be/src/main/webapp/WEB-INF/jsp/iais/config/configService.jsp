<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.ecquaria.cloud.RedirectUtil" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
</style>
<div class="main-content">
  <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">

    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="col-lg-12 col-xs-10">
      <div class="bg-title" style="text-align: center;height: 20%">
        <h2 style="margin-top: 5%">HCSA Service</h2>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-10">
          <h2 class="component-title">Add HCSA Service</h2>
        </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="serviceName">Service Name<span class="mandatory" >*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="serviceName" type="text" name="serviceName" maxlength="100" value="${hcsaServiceDto.svcName}">
            <span name="iaisErrorMsg" class="error-msg" id="error_svcName"></span>
            <span name="iaisErrorMsg" class="error-msg" id="error_Name"></span>
          </div>
        </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="description">Service Description<span class="mandatory" >*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="description" type="text" name="description" maxlength="255" value="${hcsaServiceDto.svcDesc}">
            <span class="error-msg" name="iaisErrorMsg" id="error_svcDesc"></span>
          </div>
        </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="displayDescription">Service Display Description<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="displayDescription" type="text" name="displayDescription" maxlength="255" value="${hcsaServiceDto.svcDisplayDesc}">
            <span class="error-msg" name="iaisErrorMsg" id="error_svcDisplayDesc"></span>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="serviceCode">Service Code<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="serviceCode" type="text" name="serviceCode" maxlength="3" value="${hcsaServiceDto.svcCode}">
            <span class="error-msg" name="iaisErrorMsg" id="error_svcCode"></span>
            <span name="iaisErrorMsg" class="error-msg" id="error_code"></span>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="ServiceType">Service Type<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">

            <select id="ServiceType" name="ServiceType">
              <option value="">Select one</option>
              <option value="SVTP001"
                      <c:choose>
                        <c:when test="${hcsaServiceDto.svcType=='SVTP001'}"> selected="selected"</c:when>
                        <c:otherwise>
                          selected="selected"
                        </c:otherwise>
                      </c:choose>
                     >Base</option>
              <option <c:if test="${hcsaServiceDto.svcType=='SVTP002'}">selected="selected"</c:if> value="SVTP002">Subsumed</option>
              <option <c:if test="${hcsaServiceDto.svcType=='SVTP003'}">selected="selected"</c:if> value="SVTP003">Specified</option>
            </select>
            <span class="error-msg" name="iaisErrorMsg" id="error_svcType"></span>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-10">
          <label class="col-xs-12 col-md-10 control-label" >Premises Type<span class="mandatory">*</span></label>

        </div>
      </div>

      <div class="form-group">
        <div class="form-check-gp">
          <div class="row">
            <div class="col-xs-12 col-md-3">
              <div class="form-check " style="left: 10%">
                <c:set var="type" value="${PremisesType}"></c:set>
                <input class="form-check-input" name="PremisesType" id="icon3checkboxSample" <c:if test="${fn:contains(type,'ONSITE')}">checked="checked"</c:if> type="checkbox" value="ONSITE" name="Onsite" aria-invalid="false">
                <label class="form-check-label" for="icon3checkboxSample"><span class="check-square"></span>Onsite</label>
              </div>
            </div>
            <div class="col-xs-12 col-md-3">
              <div class="form-check ">
                <input class="form-check-input"  name="PremisesType" id="icon4checkboxSample"  <c:if test="${fn:contains(type,'OFFSIET')}">checked="checked"</c:if> type="checkbox" value="OFFSIET" name="Offsite" aria-invalid="false">
                <label class="form-check-label" for="icon4checkboxSample"><span class="check-square"></span>Offsite</label>
              </div>
            </div>
            <div class="col-xs-12 col-md-3">
              <div class="form-check ">
                <input class="form-check-input"  name="PremisesType" id="icon5checkboxSample"  <c:if test="${fn:contains(type,'CONVEYANCE')}">checked="checked"</c:if> type="checkbox" value="CONVEYANCE" name="Conveyance" aria-invalid="false">
                <label class="form-check-label" for="icon5checkboxSample"><span class="check-square"></span>Conveyance</label>
              </div>
            </div>
          </div>
          <span class="error-msg" name="iaisErrorMsg" id="error_premieseType"></span>
        </div>
      </div>


      <div class="form-group" style="display: none" id="Subsumption">
        <div class="col-xs-12 col-md-8"  style="margin-bottom: 10px">
          <label class="col-xs-12 col-md-6 control-label" >Subsumption Base Service:<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <iais:multipleSelect name="Subsumption" selectValue="${selectSubsumption}" options="selsectBaseHcsaServiceDto"></iais:multipleSelect>
            <span id="error_Subsumption" class="error-msg" name="iaisErrorMsg" ></span>
          </div>
        </div>
      </div>



      <div class="form-group" style="display: none" id="Pre-requisite">
        <div class="col-xs-12 col-md-8" style="margin-bottom: 10px">
          <label class="col-xs-12 col-md-6 control-label" >Pre-requisite Base Service:<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <iais:multipleSelect name="Pre-requisite" selectValue="${selectPreRequisite}" options="selsectBaseHcsaServiceDto"></iais:multipleSelect>
            <span id="error_Prerequisite" class="error-msg" name="iaisErrorMsg" ></span>
          </div>
        </div>
      </div>

      <div class="form-group" >
        <div class="col-xs-12 col-md-8" style="margin-bottom: 10px">

          <div class="col-xs-12 col-md-4" style="margin-left: 50%">
            <label class="col-xs-12 col-md-6 control-label"  style="text-align: center">MINIMUM COUNT</label>
            <label class="col-xs-12 col-md-6 control-label" style="text-align: center" >MAXIMUM COUNT</label>
          </div>
        </div>
      </div>



      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" >Principal Officer (PO)<span class="mandatory">*</span></label>
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
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" >Deputy Principal Officer (DPO)<span class="mandatory">*</span></label>
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
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" >Clinical Governance Officer (CGO)<span class="mandatory">*</span></label>
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
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" >Service Personnel<span class="mandatory">*</span></label>
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
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" >Medalert Person<span class="mandatory">*</span></label>
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
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" for="NumberDocument">Number of Service-Related Document to be uploaded<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="NumberDocument" type="text" maxlength="2" name="NumberDocument" value="${serviceDocSize}">
          </div>
          <span class="error-msg" name="iaisErrorMsg" id="error_NumberDocument"></span>
        </div>
      </div>

      <div class="serviceNumberfields">
        <c:forEach items="${serviceDoc}" var="doc">
          <div class="form-group">
            <div class="col-xs-12 col-md-8">
              <label class="col-xs-12 col-md-6 control-label">Name of Info Field</label>
              <input type="hidden" value="${doc.id}" name="commDocId">
              <div class="col-xs-12 col-md-4">
                <input  type="text" name="descriptionServiceDoc" maxlength="255" value="${doc.docDesc}">
              </div>
              <div class="col-xs-12 col-md-2 form-check" style="margin-top: 1%">
                <input type="hidden" name="serviceDocMandatory"<c:choose><c:when test="${doc.isMandatory}"> value="1"</c:when><c:otherwise> value="0"</c:otherwise></c:choose>>
                <input class="form-check-input" <c:if test="${doc.isMandatory}">checked</c:if>  type="checkbox" onclick="serviceCheckboxOnclick(this)" name="descriptionServiceDocMandatory">
                <label class="form-check-label" ><span class="check-square"></span>Mandatory</label>
              </div>
            </div>
          </div>
        </c:forEach>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" for="Numberfields">Number of Service-Related General Info fields to be captured<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="Numberfields" type="text" name="Numberfields" maxlength="2" value="${comDocSize}">
          </div>
          <span class="error-msg" name="iaisErrorMsg" id="error_Numberfields"></span>
        </div>
      </div>

      <div class="Numberfields">
        <c:forEach items="${comDoc}" var="doc">
          <div class="form-group">
            <div class="col-xs-12 col-md-8">
              <label class="col-xs-12 col-md-6 control-label">Name of Info Field</label>
              <input type="hidden" value="${doc.id}" name="commDocId">
              <div class="col-xs-12 col-md-4">
                <input  type="text" name="descriptionCommDoc" maxlength="255" value="${doc.docDesc}">
              </div>
              <div class="col-xs-12 col-md-2 form-check" style="margin-top: 1%">
                <input type="hidden" name="commDocMandatory"<c:choose><c:when test="${doc.isMandatory}"> value="1"</c:when><c:otherwise> value="0"</c:otherwise></c:choose>>
                <input class="form-check-input" <c:if test="${doc.isMandatory}">checked</c:if>  type="checkbox" onclick="checkboxOnclick(this)" name="descriptionCommDocMandatory">
                <label class="form-check-label" ><span class="check-square"></span>Mandatory</label>
              </div>
            </div>
          </div>
        </c:forEach>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label">Will the doc be duplicated for individual premises?</label>
          <div class="col-xs-12 col-md-2">
            <input type="radio" class="form-check-input premTypeRadio" name="individualPremises" checked value="0"><label>No</label>
          </div>
          <div class="col-xs-12 col-md-2">
            <input type="radio" class="form-check-input premTypeRadio" name="individualPremises" value="1"><label>Yes</label>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8  marg-1">
          <label class="col-xs-12 col-md-8 control-label" >Service-Related Checklists<span class="mandatory">*</span></label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary " data-toggle="modal" data-target= "#checklists" ><span class="view">Configure</span></a>
            </div>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8  marg-1">
          <label class="col-xs-12 col-md-8 control-label" >Service Risk Score<span class="mandatory">*</span></label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary "  data-toggle="modal" data-target= "#riskScore"><span class="view">Configure</span></a>
            </div>
          </div>
        </div>
      </div>




      <div class="form-group">
        <div class="col-xs-12 col-md-8 marg-1">
          <label class="col-xs-12 col-md-8 control-label" >Service KPI<span class="mandatory">*</span></label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary " data-toggle="modal" data-target= "#kpi" ><span class="view">Configure</span></a>
            </div>
          </div>
        </div>
      </div>

      <%--<div class="form-group">
        <div class="col-xs-12 col-md-8 marg-1">
          <label class="col-xs-12 col-md-8 control-label" >Service Fees<span class="mandatory">*</span></label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary "><span class="view">Configure</span></a>
            </div>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8  marg-1" >
          <label class="col-xs-12 col-md-8 control-label" >Service Fee Bundles<span class="mandatory">*</span></label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary "><span class="view">Configure</span></a>
            </div>
          </div>
        </div>
      </div>
--%>
      <div class="form-group">
        <div class="col-xs-12 col-md-12" style="margin-top: 1%">
          <div class="col-xs-10 col-md-6">
            <div class="components width-center">
              <a class="btn btn-secondary width-70" onclick="showNEW()"><span class="view">NEW APPLICATION</span></a>
            </div>
          </div>
          <div class="col-xs-10 col-md-6">
            <div class="components width-center">
              <a class="btn btn-secondary width-70" onclick="showRENEW()"><span class="view">RENEW</span></a>
            </div>
          </div>

        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-12" style="margin-top: 1%">
          <div class="col-xs-10 col-md-6">
            <div class="components width-center">
              <a class="btn btn-secondary width-70" onclick="showAPPEAL()"><span class="view">APPEAL</span></a>
            </div>
          </div>
          <div class="col-xs-10 col-md-6">
            <div class="components width-center">
              <a class="btn btn-secondary width-70" onclick="showRFC()"><span class="view">REQUEST FOR CHANGE</span></a>
            </div>
          </div>
        </div>
      </div>

      <div  class="form-group">
        <div class="col-xs-12 col-md-12" style="margin-top: 1%">
          <div class="col-xs-10 col-md-6">
            <div class="components  width-center">
              <a class="btn btn-secondary width-70" onclick="showCESSATION()"><span class="view">CESSATION</span></a>
            </div>
          </div>
         <%-- <div class="col-xs-10 col-md-6">
            <div class="components  width-center">
              <a class="btn btn-secondary width-70" onclick="showSUSPENSION()"><span class="view">SUSPENSION</span></a>
            </div>
          </div>--%>


        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-12" style="margin-top: 1%">
          <div class="col-xs-10 col-md-6">
            <div class="components width-center">
              <a class="btn btn-secondary width-70" onclick="showWITHDRAWAL()"><span class="view">WITHDRAWAL</span></a>
            </div>
          </div>
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
      <table border="1px" style="text-align: center" >
        <tr>
          <th style="width: 10% ;height: 40px;text-align: center">Application Type<span class="mandatory" >*</span></th>
          <th  style="width: 20% ;height: 40px;text-align: center">Service Workflow Routing Stages<span class="mandatory" >*</span></th>
          <th  style="width:30% ;height: 40px;text-align: center">Service Routing Scheme<span class="mandatory">*</span></th>
          <th  style="width: 15% ;height: 40px;text-align: center">Service Workload Manhours<span class="mandatory">*</span></th>
          <th  style="width: 25% ;height: 40px;text-align: center">Working Group<span class="mandatory">*</span></th>
        </tr>
        <c:forEach items="${routingStages.value}" var="routingStage" varStatus="status">
      <tr>
        <td >${routingStage.appTypeName} </td>
        <td >${routingStage.stageName}</td>
        <td>

          <div class="col-xs-12 col-md-6" style="margin-top: 1%;margin-bottom: 1%">
            <select name="isMandatory${routingStage.stageCode}${routingStages.key}">
              <option value="">Please Select</option>
              <option value="mandatory" >Mandatory</option>
              <option value="optional">Optional</option>
            </select>
          </div>

          <div class="col-xs-12 col-md-6" style="margin-top: 1%;margin-bottom: 1%">
            <select  name="RoutingScheme${routingStage.stageCode}${routingStages.key}"  >
              <option value="" >Please Select</option>
              <option value="common"
                      <c:choose>
                        <c:when test="${routingStage.routingSchemeName=='common'}">
                          selected="selected"
                        </c:when>
                      </c:choose>
              >Common Pool</option>
              <option value="assign"
                      <c:if test="${routingStage.routingSchemeName=='assign'}">selected="selected" </c:if>
              >Supervisor Assign</option>
              <option value="round"
                      <c:choose>
                        <c:when test="${routingStage.routingSchemeName=='round'}">
                          selected="selected"
                        </c:when>
                      </c:choose>

              >Round Robin</option>
           </select>
            <span name="iaisErrorMsg" class="error-msg" id="error_schemeType${sta.index*6+status.index}"></span>
          </div>

        </td>
        <td>
          <div class="col-xs-12 col-md-12">
            <input style="margin: 8px 0px 8px" type="text" maxlength="2" name="WorkloadManhours${routingStage.stageCode}${routingStages.key}" value="${routingStage.manhours}" >
            <span class="error-msg" name="iaisErrorMsg" id="error_manhourCount${status.index}"></span>
          </div>

        </td>
        <td>
          <div class="col-xs-12 col-md-12">

            <select name="workingGroup${routingStage.stageCode}${routingStages.key}">
              <option value="">Please Select</option><c:forEach items="${routingStage.workingGroup}" var="workingGroup">
              <option <c:if test="${routingStage.workingGroupId==workingGroup.id}">selected="selected"</c:if>value="${workingGroup.id}">${workingGroup.groupName}</option>
            </c:forEach>
            </select>
            <span name="iaisErrorMsg" class="error-msg" id="error_stageWorkGroupId${sta.index*6+status.index}"></span>
          </div>
        </td>

      </tr>
        </c:forEach>

      </table>
        </div>
      </div>
      </c:forEach>


      <div class="form-group">
        <div class="col-xs-12 col-md-6" style="margin-top: 20px ;margin-bottom: 20px">
          <label class="col-xs-12 col-md-8 control-label" >Service Sub-Types</label>

          <div class="col-xs-12 col-md-7">
            <label>Page Name</label>
          </div >
          <div  class="col-xs-12 col-md-5"><input  type="text" maxlength="100" value="" ></div>
          <span name="iaisErrorMsg" class="error-msg" id="error_hcsaSvcSubtypeOrSubsumed"></span>
          <div class="form-group"  id="add">
            <div class="col-xs-12 col-md-8" style="margin-bottom: 10px">

              <div class="col-xs-12 col-md-4" style="margin-left: 100%">
                <label class="col-xs-12 col-md-12 control-label"  style="text-align: center">UP/DOWN</label>
              </div>
            </div>
          </div>

          <c:forEach items="${hcsaSvcSubtypeOrSubsumedDto}" var="hcsaSvcSubtypeOrSubsumed">
            <div class="view">
              <div class="col-xs-12 col-md-7" style="padding-right: 20%;" >
                <input class="add" type="text"  style="margin-left:0px" name="subType" maxlength="100" value="${hcsaSvcSubtypeOrSubsumed.name}">
              </div>
              <div class="value">
                <input type="text" value="0" name="level" style="display: none">
              </div>
              <div  class="col-xs-12 col-md-3" >
                <a class="btn  btn-secondary  view" onclick="indents(this)"   >indent</a>
              </div>
              <div  class="col-xs-12 col-md-2">
                <a class="btn  btn-secondary view"  onclick="outdent(this)" >outdent</a>
              </div>
            </div>
            <c:forEach items="${hcsaSvcSubtypeOrSubsumed.list}" var="hcsaSvcSubtypeOrSubsumed2">
              <div class="view">
                <div class="col-xs-12 col-md-7" style="padding-right: 20%;" >
                  <input class="add" type="text"  style="margin-left:60px" maxlength="100" name="subType" value="${hcsaSvcSubtypeOrSubsumed2.name}">
                </div>
                <div class="value">
                  <input type="text" value="1" name="level" style="display: none" >
                </div>
                <div  class="col-xs-12 col-md-3" >
                  <a class="btn  btn-secondary  view" onclick="indents(this)"   >indent</a>
                </div>
                <div  class="col-xs-12 col-md-2">
                  <a class="btn  btn-secondary view"  onclick="outdent(this)" >outdent</a>
                </div>
              </div>
              <c:forEach items="${hcsaSvcSubtypeOrSubsumed2.list}" var="hcsaSvcSubtypeOrSubsumed3">
                <div class="view">
                  <div class="col-xs-12 col-md-7" style="padding-right: 20%;" >
                    <input class="add" type="text"  style="margin-left:120px" maxlength="100" name="subType" value="${hcsaSvcSubtypeOrSubsumed3.name}">
                  </div>
                  <div class="value">
                    <input type="text" value="2" name="level" style="display: none" >
                  </div>
                  <div  class="col-xs-12 col-md-3" >
                    <a class="btn  btn-secondary  view" onclick="indents(this)"   >indent</a>
                  </div>
                  <div  class="col-xs-12 col-md-2">
                    <a class="btn  btn-secondary view"  onclick="outdent(this)" >outdent</a>
                  </div>
                </div>
              </c:forEach>
            </c:forEach>
          </c:forEach>

          <div class="col-xs-12 col-md-6">
            <a  class="btn  btn-secondary "   style="margin-right: 10px" id="addAsItem" onclick="addAsItem(this)"> + </a><label for="addAsItem"> Add as Item</label>
          </div>
        </div>

      </div>




      <div class="col-xs-12 col-md-8">
        <div class="form-group">
          <label class="col-xs-12 col-md-8 control-label">Effective Start Date<span class="mandatory">*</span></label>
          <div class=" col-xs-7 col-sm-4 col-md-3">
            <input type="text" value="${hcsaServiceDto.effectiveDate}" autocomplete="off" class="date_picker form-control form_datetime" name="StartDate" id="-20189532301300" data-date-start-date="01/01/1900" placeholder="dd/mm/yyyy" maxlength="10"><span id="error_StartDate" name="iaisErrorMsg" class="error-msg" ></span>
          </div>
          <span class="error-msg" name="iaisErrorMsg" id="error_effectiveDate"></span>
          <div class="clear"></div></div>
      </div>


    <div class="col-xs-12 col-md-8">
      <div class="form-group">
        <label class="col-xs-12 col-md-8 control-label">Effective End Date</label>
        <div class=" col-xs-7 col-sm-4 col-md-3">
          <input type="text" autocomplete="off" value="<fmt:formatDate value="${hcsaServiceDto.endDate}" pattern="dd/MM/yyyy"/>" class="date_picker form-control form_datetime" name="EndDate" id="-20247433206800" data-date-start-date="01/01/1900" placeholder="dd/mm/yyyy" maxlength="10"><span id="error_EndDate" name="iaisErrorMsg" class="error-msg"></span>
        </div>
        <span class="error-msg" name="iaisErrorMsg" id="error_effectiveEndDate"></span>
        <div class="clear"></div></div>
    </div>

      <div class="col-xs-12 col-md-8" style="left: 10%">
        <div class="row">
          <div class="col-xs-10 col-md-8">
            <div class="components">
              <a class="btn  btn-secondary"data-toggle="modal" data-target= "#cancel">Cancel</a>
            </div>
          </div>
          <div class="col-xs-10 col-md-3">
            <div class="components">
              <a class="btn btn-primary"  onclick="save()">Save</a>

            </div>
          </div>
        </div>
      </div>
      <div class="col-xs-12 col-md-12" style="text-align: center">
        <div class="row">
          <div class="col-xs-10 col-md-8">
            <div class="components">
              <p style="text-align: center">Version 1</p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
  </form>
</div>

<iais:confirm msg="Are you sure you want to leave this page!" callBack="kpi()" popupOrder="kpi" ></iais:confirm>

<iais:confirm msg="Are you sure you want to leave this page!" callBack="checklists()" popupOrder="checklists" ></iais:confirm>

<iais:confirm msg="Are you sure you want to leave this page!" callBack="riskScore()" popupOrder="riskScore" ></iais:confirm>

<iais:confirm msg="Are you sure you want to cancel?" yesBtnDesc="NO" cancelBtnDesc="YES" yesBtnCls="btn btn-secondary" cancelBtnCls="btn btn-primary" cancelFunc="cancel()" callBack="displays()" popupOrder="cancel"></iais:confirm>
<script type="text/javascript">

    function cancel() {

        SOP.Crud.cfxSubmit("mainForm","cancel","cancel","");
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
        if("SVTP002"==val){
            $('#Subsumption').attr("style","display:block");
            $('#Pre-requisite').attr("style","display:none");
        }else  if("SVTP003"==val){
            $('#Pre-requisite').attr("style","display:block")
            $('#Subsumption').attr("style","display:none");

        }else {
            $('#Subsumption').attr("style","display:none");
            $('#Pre-requisite').attr("style","display:none");
        }
    });

  function showNEW() {
      let jQuery = $('#APTY002').attr("style");
      $('#APTY001').attr("style","display: none");
      $('#APTY004').attr("style","display: none");
      $('#APTY005').attr("style","display: none");
      $('#APTY006').attr("style","display: none");
      $('#APTY007').attr("style","display: none");
      $('#APTY008').attr("style","display: none");
      if(jQuery=='display: block'){
          $('#APTY002').attr("style","display: none");
      }else if(jQuery=='display: none'){
          $('#APTY002').attr("style","display: block");
      }
  }

  function showRENEW() {
      let jQuery = $('#APTY004').attr("style");
      $('#APTY001').attr("style","display: none");
      $('#APTY005').attr("style","display: none");
      $('#APTY006').attr("style","display: none");
      $('#APTY007').attr("style","display: none");
      $('#APTY002').attr("style","display: none");
      $('#APTY008').attr("style","display: none");
      if(jQuery=='display: block'){
          $('#APTY004').attr("style","display: none");
      }else if(jQuery=='display: none'){
          $('#APTY004').attr("style","display: block");
      }
  }

    function showAPPEAL(){
        let jQuery = $('#APTY001').attr("style");
        $('#APTY002').attr("style","display: none");
        $('#APTY004').attr("style","display: none");
        $('#APTY006').attr("style","display: none");
        $('#APTY007').attr("style","display: none");
        $('#APTY008').attr("style","display: none");
        $('#APTY005').attr("style","display: none");
        if(jQuery=='display: block'){
            $('#APTY001').attr("style","display: none");
        }else if(jQuery=='display: none'){
            $('#APTY001').attr("style","display: block");
        }
    }

    function showRFC(){
        let jQuery = $('#APTY005').attr("style");
        $('#APTY002').attr("style","display: none");
        $('#APTY004').attr("style","display: none");
        $('#APTY006').attr("style","display: none");
        $('#APTY007').attr("style","display: none");
        $('#APTY008').attr("style","display: none");
        $('#APTY001').attr("style","display: none");
        if(jQuery=='display: block'){
            $('#APTY005').attr("style","display: none");
        }else if(jQuery=='display: none'){
            $('#APTY005').attr("style","display: block");
        }

    }

    function showCESSATION(){
        let jQuery = $('#APTY008').attr("style");
        $('#APTY002').attr("style","display: none");
        $('#APTY004').attr("style","display: none");
        $('#APTY006').attr("style","display: none");
        $('#APTY007').attr("style","display: none");
        $('#APTY005').attr("style","display: none");
        $('#APTY001').attr("style","display: none");
        if(jQuery=='display: block'){
            $('#APTY008').attr("style","display: none");
        }else if(jQuery=='display: none'){
            $('#APTY008').attr("style","display: block");
        }
    }

  /*  function showSUSPENSION(){
        let jQuery = $('#APTY007').attr("style");
        $('#APTY002').attr("style","display: none");
        $('#APTY004').attr("style","display: none");
        $('#APTY006').attr("style","display: none");
        $('#APTY008').attr("style","display: none");
        $('#APTY005').attr("style","display: none");
        $('#APTY001').attr("style","display: none");
        if(jQuery=='display: block'){
            $('#APTY007').attr("style","display: none");
        }else if(jQuery=='display: none'){
            $('#APTY007').attr("style","display: block");
        }
    }
*/
    function  showWITHDRAWAL(){
        let jQuery = $('#APTY006').attr("style");
        $('#APTY002').attr("style","display: none");
        $('#APTY004').attr("style","display: none");
        $('#APTY007').attr("style","display: none");
        $('#APTY008').attr("style","display: none");
        $('#APTY005').attr("style","display: none");
        $('#APTY001').attr("style","display: none");
        if(jQuery=='display: block'){
            $('#APTY006').attr("style","display: none");
        }else if(jQuery=='display: none'){
            $('#APTY006').attr("style","display: block");
        }
    }

    function showREVOCATION(){

    }

    $('#ServiceType').change(function () {

        let val = $('#ServiceType').val();
        if("SVTP002"==val){
            $('#Subsumption').attr("style","display:block");
            $('#Pre-requisite').attr("style","display:none");
        }else  if("SVTP003"==val){
            $('#Pre-requisite').attr("style","display:block")
            $('#Subsumption').attr("style","display:none");

        }else {
            $('#Subsumption').attr("style","display:none");
            $('#Pre-requisite').attr("style","display:none");
        }

    });



    function addAsItem(obj) {
        $(obj).closest("div").prev("div").append(" <div class=\"view\">\n" +
            "          <div class=\"col-xs-12 col-md-7\" style=\"padding-right: 20%;\" >\n" +
            "            <input class=\"add\" type=\"text\"  style=\"\" name=\"subType\">\n" +
            "          </div>\n" +
            "            <div class=\"value\">\n" +
            "              <input type=\"text\" value=\"0\" name=\"level\"  style=\"display: none\">\n" +
            "            </div>\n" +
            "          <div  class=\"col-xs-12 col-md-3\" >\n" +
            "            <a class=\"btn  btn-secondary  view\" onclick=\"indents(this)\"   >indent</a>\n" +
            "          </div>\n" +
            "          <div  class=\"col-xs-12 col-md-2\">\n" +
            "            <a class=\"btn  btn-secondary view\"  onclick=\"outdent(this)\" >outdent</a>\n" +
            "          </div>\n" +
            "          </div>");

    }

    function indents(obj) {
        let jQuery = $(obj).closest('div.view').children("div.col-md-7").children();
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
        let jQuery = $(obj).closest('div.view').children("div.col-md-7").children();
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
                $(jQuery).attr("style","margin-left:"+0+"px")
                jQuery2.val(0);
            }else {
                $(jQuery).attr("style","margin-left:"+a+"px")
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
        let number = parseInt(val);
        let jQuery = $(this).closest("div.form-group").next(".Numberfields").children();
        let number1 = parseInt(jQuery.length);
        if(number-number1>0){
            for(var i=0;i<number-number1;i++){
                $(this).closest("div.form-group").next(".Numberfields").append(" <div class=\"form-group\">\n" +
                    "            <div class=\"col-xs-12 col-md-8\">\n" +
                    "           <input type=\"hidden\" value=\"\" name=\"commDocId\">\n" +
                    "              <label class=\"col-xs-12 col-md-6 control-label\">Name of Info Field</label>\n" +
                    "              <div class=\"col-xs-12 col-md-4\">\n" +
                    "                <input  type=\"text\" name=\"descriptionCommDoc\" maxlength=\"255\">\n" +
                    "              </div>\n" +
                    "              <div class=\"col-xs-12 col-md-2 form-check\" style=\"margin-top: 1%\">\n" +
                    "                <input type=\"hidden\" name=\"commDocMandatory\" value=\"0\">\n" +
                    "                <input class=\"form-check-input\"  type=\"checkbox\" onclick=\"checkboxOnclick(this)\" name=\"descriptionCommDocMandatory\">\n" +
                    "                <label class=\"form-check-label\" ><span class=\"check-square\"></span>Mandatory</label>\n" +
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
        let number = parseInt(val);
        let jQuery = $(this).closest("div.form-group").next(".serviceNumberfields").children();
        let number1 = parseInt(jQuery.length);
        if(number-number1>0){
            for(var i=0;i<number-number1;i++){
                $(this).closest("div.form-group").next(".serviceNumberfields").append(" <div class=\"form-group\">\n" +
                    "            <div class=\"col-xs-12 col-md-8\">\n" +
                    "           <input type=\"hidden\" value=\"\" name=\"serviceDocId\">\n" +
                    "              <label class=\"col-xs-12 col-md-6 control-label\">Name of Info Field</label>\n" +
                    "              <div class=\"col-xs-12 col-md-4\">\n" +
                    "                <input  type=\"text\" name=\"descriptionServiceDoc\" maxlength=\"255\">\n" +
                    "              </div>\n" +
                    "              <div class=\"col-xs-12 col-md-2 form-check\" style=\"margin-top: 1%\">\n" +
                    "                <input type=\"hidden\" name=\"serviceDocMandatory\" value=\"0\">\n" +
                    "                <input class=\"form-check-input\"  type=\"checkbox\" onclick=\"serviceCheckboxOnclick(this)\" name=\"descriptionServiceDocMandatory\">\n" +
                    "                <label class=\"form-check-label\" ><span class=\"check-square\"></span>Mandatory</label>\n" +
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

</script>
</>
