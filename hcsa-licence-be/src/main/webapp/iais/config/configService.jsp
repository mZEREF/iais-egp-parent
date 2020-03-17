<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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

  .marg-1{
    margin-top: 1%;
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
            <input id="serviceName" type="text" name="serviceName" value="${hcsaServiceDto.svcName}">
            <span name="iaisErrorMsg" class="error-msg" id="error_svcName"></span>
            <span name="iaisErrorMsg" class="error-msg" id="error_Name"></span>
          </div>
        </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="description">Service Description<span class="mandatory" >*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="description" type="text" name="description" value="${hcsaServiceDto.svcDesc}">
            <span class="error-msg" name="iaisErrorMsg" id="error_svcDesc"></span>
          </div>
        </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="displayDescription">Service Display Description<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="displayDescription" type="text" name="displayDescription" value="${hcsaServiceDto.svcDisplayDesc}">
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
                <label class="form-check-label" for="icon3checkboxSample"><span class="check-square"></span>Onsite</label>
              </div>
            </div>
            <div class="col-xs-12 col-md-3">
              <div class="form-check ">
                <input class="form-check-input"  name="PremisesType" id="icon4checkboxSample"  <c:if test="${fn:contains(type,'OFFSITE')}">checked="checked"</c:if> type="checkbox" value="OFFSITE" name="Offsite" aria-invalid="false">
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
        </div>
      </div>


      <div class="form-group" style="display: none" id="Subsumption">
        <div class="col-xs-12 col-md-8"  style="margin-bottom: 10px">
          <label class="col-xs-12 col-md-6 control-label" >Subsumption Base Service:<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <select  name="Subsumption">
              <option value="">Select one</option>
              <c:forEach items="${hcsaServiceCategoryDtos}" var="hcsaServiceCategoryDto">
                <option value="${hcsaServiceCategoryDto.id}"
                        <c:if test="${hcsaServiceDto.svcType=='SVTP002'&&hcsaServiceDto.categoryId==hcsaServiceCategoryDto.id}">selected="selected"</c:if>
                >${hcsaServiceCategoryDto.name}</option>
              </c:forEach>
            </select>
            <span id="error_Subsumption" class="error-msg" name="iaisErrorMsg" ></span>
          </div>
        </div>
      </div>



      <div class="form-group" style="display: none" id="Pre-requisite">
        <div class="col-xs-12 col-md-8" style="margin-bottom: 10px">
          <label class="col-xs-12 col-md-6 control-label" >Pre-requisite Base Service:<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <select  name="Subsumption">
              <option value="">Select one</option>
              <c:forEach items="${hcsaServiceCategoryDtos}" var="hcsaServiceCategoryDto">
                <option value="${hcsaServiceCategoryDto.id}"
                        <c:if test="${hcsaServiceDto.svcType=='SVTP002'&&hcsaServiceDto.categoryId==hcsaServiceCategoryDto.id}">selected="selected"</c:if>
                >${hcsaServiceCategoryDto.name}</option>
              </c:forEach>
            </select>
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
            <input type="text" name="man-principalOfficer" maxlength="2" placeholder="minimum count" value="${PO.mandatoryCount}">
            <span class="error-msg" name="iaisErrorMsg" id="error_mandatoryCount0"></span>
          </div>
          <div class="col-xs-12 col-md-2">
            <input type="text" name="mix-principalOfficer" maxlength="2" placeholder="maximum count" value="${PO.maximumCount}">
            <span class="error-msg" name="iaisErrorMsg" id="error_maximumCount0"></span>
          </div>
          <div class="col-xs-12 col-md-2 form-check">   <input class="form-check-input"  type="checkbox" name="POMandatory" aria-invalid="false">
            <label class="form-check-label" ><span class="check-square"></span>Mandatory</label>
          </div>
          <span class="error-msg" name="iaisErrorMsg" id="error_psnType1"></span>
        </div>
      </div>


      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" >Deputy Principal Officer (DPO)<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="man-DeputyPrincipalOfficer" maxlength="2" placeholder="minimum count" value="${DPO.mandatoryCount}">
            <span class="error-msg" name="iaisErrorMsg" id="error_mandatoryCount1"></span>
          </div>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="mix-DeputyPrincipalOfficer" maxlength="2" placeholder="maximum count" value="${DPO.maximumCount}">
            <span class="error-msg" name="iaisErrorMsg" id="error_maximumCount1"></span>
          </div>

          <div class="col-xs-12 col-md-2 form-check">   <input class="form-check-input"  type="checkbox" name="DPOMandatory" aria-invalid="false">
            <label class="form-check-label" ><span class="check-square"></span>Mandatory</label>
          </div>
          <span class="error-msg" name="iaisErrorMsg" id="error_psnType2"></span>
        </div>
      </div>


      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" >Clinical Governance Officer (CGO)<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="man-ClinicalGovernanceOfficer" maxlength="2" placeholder="minimum count" value="${CGO.mandatoryCount}">
            <span class="error-msg" name="iaisErrorMsg" id="error_mandatoryCount2"></span>
          </div>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="mix-ClinicalGovernanceOfficer" maxlength="2"  placeholder="maximum count" value="${CGO.maximumCount}">
            <span class="error-msg" name="iaisErrorMsg" id="error_maximumCount2"></span>
          </div>
          <div class="col-xs-12 col-md-2 form-check">   <input class="form-check-input"  type="checkbox" name="CGOMandatory" aria-invalid="false">
            <label class="form-check-label" ><span class="check-square"></span>Mandatory</label>
          </div>
          <span class="error-msg" name="iaisErrorMsg" id="error_psnType3"></span>
        </div>
      </div>


      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" >Service Personnel<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="man-ServicePersonnel" maxlength="2" placeholder="minimum count" value="${SVCPSN.mandatoryCount}">
            <span class="error-msg" name="iaisErrorMsg" id="error_mandatoryCount3"></span>
          </div>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="mix-ServicePersonnel" maxlength="2"  placeholder="maximum count" value="${SVCPSN.maximumCount}">
            <span class="error-msg" name="iaisErrorMsg" id="error_maximumCount3"></span>
          </div>
          <div class="col-xs-12 col-md-2 form-check">   <input class="form-check-input"  type="checkbox" name="SVCPSNMandatory" aria-invalid="false">
            <label class="form-check-label" ><span class="check-square"></span>Mandatory</label>
          </div>
        </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" for="NumberDocument">Number of Service-Related Document to be
            uploaded<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="NumberDocument" type="text" maxlength="2" name="NumberDocument" value="${numberDocument}">
          </div>
          <div class="col-xs-12 col-md-2 form-check">   <input class="form-check-input" id="NumberDocumentMandatory" type="checkbox" name="NumberDocumentMandatory" aria-invalid="false">
            <label for="NumberDocumentMandatory"  class="form-check-label" ><span class="check-square"></span>Mandatory</label>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" for="DescriptionDocument">Description of each Service-Related Document to
            be Uploaded<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="DescriptionDocument" type="text" name="DescriptionDocument" value="${descriptionDocument}">
          </div>
          <div class="col-xs-12 col-md-2 form-check">   <input class="form-check-input" id="DescriptionDocumentMandatory"  type="checkbox" name="DescriptionDocumentMandatory" aria-invalid="false">
            <label for="DescriptionDocumentMandatory" class="form-check-label"><span class="check-square"></span>Mandatory</label>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" for="Numberfields">Number of Service-Related General Info fields to
            be captured<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="Numberfields" type="text" name="Numberfields" value="${numberfields}">
          </div>
          <div class="col-xs-12 col-md-2 form-check">   <input class="form-check-input"  type="checkbox" name="Conveyance" aria-invalid="false">
            <label class="form-check-label"><span class="check-square"></span>Mandatory</label>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" for="DescriptionGeneral">Description of each Service-Related General Info
            field to be captured*<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="DescriptionGeneral" type="text" name="DescriptionGeneral" value="${descriptionGeneral}">
          </div>

        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8 marg-1">
          <label class="col-xs-12 col-md-8 control-label" >Service Workload Manhours<span class="mandatory">*</span></label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary "><span class="view">Configure</span></a>
            </div>
          </div>
        </div>
        <div class="col-xs-12 col-md-8  marg-1">
          <label class="col-xs-12 col-md-8 control-label" >Service-Related Checklists<span class="mandatory">*</span></label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary " onclick="checklists()"><span class="view">Configure</span></a>
            </div>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8  marg-1">
          <label class="col-xs-12 col-md-8 control-label" >Service Risk Score<span class="mandatory">*</span></label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary " ><span class="view">Configure</span></a>
            </div>
          </div>
        </div>
      </div>




      <div class="form-group">
        <div class="col-xs-12 col-md-8 marg-1">
          <label class="col-xs-12 col-md-8 control-label" >Service KPI<span class="mandatory">*</span></label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary " onclick="kpi()" ><span class="view">Configure</span></a>
            </div>
          </div>
        </div>
      </div>

      <div class="form-group">
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

      <div class="form-group">
        <div class="col-xs-12 col-md-12" style="margin-top: 1%">
          <div class="col-xs-10 col-md-3">
            <div class="components">
              <a class="btn btn-secondary " onclick="showNEW()"><span class="view">NEW APPLICATION</span></a>
            </div>
          </div>
          <div class="col-xs-10 col-md-3">
            <div class="components">
              <a class="btn btn-secondary " onclick="showRENEW()"><span class="view">RENEW</span></a>
            </div>
          </div>
          <div class="col-xs-10 col-md-3">
            <div class="components">
              <a class="btn btn-secondary " onclick="showAPPEAL()"><span class="view">APPEAL</span></a>
            </div>
          </div>
          <div class="col-xs-10 col-md-3">
            <div class="components">
              <a class="btn btn-secondary " onclick="showRFC()"><span class="view">REQUEST FOR CHANGE</span></a>
            </div>
          </div>
        </div>
      </div>

      <div  class="form-group">
        <div class="col-xs-12 col-md-12" style="margin-top: 1%">
          <div class="col-xs-10 col-md-3">
            <div class="components">
              <a class="btn btn-secondary " onclick="showCESSATION()"><span class="view">CESSATION</span></a>
            </div>
          </div>
          <div class="col-xs-10 col-md-3">
            <div class="components">
              <a class="btn btn-secondary " onclick="showSUSPENSION()"><span class="view">SUSPENSION</span></a>
            </div>
          </div>

          <div class="col-xs-10 col-md-3">
            <div class="components">
              <a class="btn btn-secondary " onclick="showWITHDRAWAL()"><span class="view">WITHDRAWAL</span></a>
            </div>
          </div>
          <div class="col-xs-10 col-md-3">
            <div class="components">
              <a class="btn btn-secondary " onclick="showREVOCATION()"><span class="view">REVOCATION</span></a>
            </div>
          </div>

        </div>
      </div>



      <c:set var="index" value="0"></c:set>
      <c:forEach items="${routingStagess}" var="routingStages" varStatus="sta">
      <div class="form-group" style="display: none" id="${routingStages.key}" >
        <div class="col-xs-12 col-md-12"  style="margin-top: 10px">
      <table border="1px" style="text-align: center" >
        <tr>
          <th style="width: 10% ;height: 40px;text-align: center">application type<span class="mandatory" >*</span></th>
          <th  style="width: 20% ;height: 40px;text-align: center">Service Workflow Routing Stages<span class="mandatory" >*</span></th>
          <th  style="width:30% ;height: 40px;text-align: center">Service Routing Scheme<span class="mandatory">*</span></th>
          <th  style="width: 25% ;height: 40px;text-align: center">working group<span class="mandatory">*</span></th>
        </tr>
        <c:forEach items="${routingStages.value}" var="routingStage" varStatus="status">
      <tr>
        <td >${routingStage.appTypeName} </td>
        <td >${routingStage.stageName}</td>
        <td>
          <div class="col-xs-12 col-md-6" style="margin-top: 1%;margin-bottom: 1%">
            <select  name="RoutingScheme${routingStage.stageCode}${routingStages.key}"  >
              <option value="" >Select one</option>
              <option value="common"
                      <c:choose>
                        <c:when test="${routingStage.routingSchemeName=='common'}">
                          selected="selected"
                        </c:when>
                        <c:otherwise>
                          <c:if test="${routingStage.stageCode=='PSO'}">selected="selected" </c:if>
                        </c:otherwise>
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
                        <c:otherwise>
                          <c:if test="${routingStage.stageCode=='ASO'||routingStage.stageCode=='AO1'||routingStage.stageCode=='AO2'||routingStage.stageCode=='AO3'}">selected="selected"  </c:if>
                        </c:otherwise>
                      </c:choose>

              >Round Robin</option>
           </select>
            <span name="iaisErrorMsg" class="error-msg" id="error_schemeType${sta.index*6+status.index}"></span>
          </div>
          <div class="col-xs-12 col-md-6" style="margin-top: 1%;margin-bottom: 1%">
            <select name="isMandatory${routingStage.stageCode}${routingStages.key}">
              <option value="">Select one</option>
              <option value="mandatory" selected="selected">Mandatory</option>
              <option value="optional">Optional</option>
            </select>
          </div>
        </td>
        <td>
          <div class="col-xs-12 col-md-12">

            <select name="workingGroup${routingStage.stageCode}${routingStages.key}">
              <option value="">Select one</option><c:forEach items="${routingStage.workingGroup}" var="workingGroup">
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
            <label>Page name</label>
          </div >
          <div  class="col-xs-12 col-md-5"><input  type="text" value="Laboratory Disciplines" ></div>
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
                <input class="add" type="text"  style="margin-left:0px" name="subType" value="${hcsaSvcSubtypeOrSubsumed.name}">
              </div>
              <div class="value">
                <input type="text" value="0" name="level" style="display: none" >
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
                  <input class="add" type="text"  style="margin-left:60px" name="subType" value="${hcsaSvcSubtypeOrSubsumed2.name}">
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
                    <input class="add" type="text"  style="margin-left:120px" name="subType" value="${hcsaSvcSubtypeOrSubsumed3.name}">
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
            <a  class="btn  btn-secondary "   style="margin-right: 10px" id="addAsItem" onclick="addAsItem(this)"> + </a><label for="addAsItem"> Add as item</label>
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
          <input type="text" autocomplete="off" class="date_picker form-control form_datetime" name="EndDate" id="-20247433206800" data-date-start-date="01/01/1900" placeholder="dd/mm/yyyy" maxlength="10"><span id="error_EndDate" name="iaisErrorMsg" class="error-msg"></span>
        </div>
        <div class="clear"></div></div>
    </div>

      <div class="col-xs-12 col-md-8" style="left: 10%">
        <div class="row">
          <div class="col-xs-10 col-md-8">
            <div class="components">

              <a class="btn  btn-secondary"  onclick="cancel()">Cancel</a>

            </div>
          </div>
          <div class="col-xs-10 col-md-3">
            <div class="components">

              <a class="btn  btn-secondary " onclick="save()">Save</a>

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
    <%@ include file="/include/validation.jsp" %>
  </form>
</div>

<iais:confirm msg="sda" callBack="config" popupOrder="confirm" title="ssss"/>
<script type="text/javascript">

    function cancel() {

        SOP.Crud.cfxSubmit("mainForm","cancel","cancel","");
    }

    function kpi() {
        var b = confirm("Are you sure you want to leave this page");
        if(b==true){
            config();
        }else {

        }
    }
    function config() {
        var b = confirm("Are you sure you want to leave this page");
        if(b==true){
            location.href="https://egp.sit.intra.iais.com/hcsa-licence-web/eservice/INTRANET/MohKPIAndReminder";
        }else {

        }

    }

    function  checklists(){
        var b = confirm("Are you sure you want to leave this page");
        if(b==true){
            location.href="https://egp.sit.intra.iais.com/hcsa-licence-web/eservice/INTRANET/MohChecklistConfiguration";
        }

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

    function showSUSPENSION(){
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


</script>
</>
