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
      <div class="bg-title" style="text-align: center">
        <h2>  HCSA Configurator Module</h2>
      </div>
      <div class="components">
        <a class="btn btn-secondary" onclick="back()" > Back</a>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-10">
          <h2 class="component-title">Edit HCSA Service</h2>
          <input type="text" style="display: none" name="serviceId" value="${hcsaServiceDto.id}">
        </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="serviceName">Service Name<span class="mandatory" >*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="serviceName" name="serviceName" maxlength="100" readonly type="text" value="${hcsaServiceDto.svcName}">
          </div>
        </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="description">Service Description<span class="mandatory" >*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="description" name="description" maxlength="255" type="text" value="${hcsaServiceDto.svcDesc}">
          </div>
        </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="displayDescription">Service Display Description<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="displayDescription" name="displayDescription" maxlength="255" type="text" value="${hcsaServiceDto.svcDisplayDesc}">
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="serviceCode">Service Code<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="serviceCode" name="serviceCode" maxlength="3" readonly type="text"  value="${hcsaServiceDto.svcCode}">
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="ServiceType">Service Type<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <select id="ServiceType"  name="ServiceType">

              <option>Select one</option>
              <option <c:if test="${hcsaServiceDto.svcType=='SVTP001'}">selected="selected"</c:if> value="SVTP001">Base</option>
              <option <c:if test="${hcsaServiceDto.svcType=='SVTP002'}">selected="selected"</c:if> value="SVTP002">Subsumed</option>
              <option <c:if test="${hcsaServiceDto.svcType=='SVTP003'}">selected="selected"</c:if> value="SVTP003">Specified</option>
            </select>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-10">
          <label class="col-xs-12 col-md-10 control-label" >Premises Type<span class="mandatory">*</span></label>
        </div>
      </div>
      <br>

      <div class="form-group">
        <div class="form-check-gp">
          <div class="row">
            <div class="col-xs-12 col-md-3">
              <div class="form-check " style="left: 10%;">
                <c:set var="type" value="${PremisesType}"></c:set>
                <input class="form-check-input"  name="PremisesType"<c:if test="${fn:contains(type,'ONSITE')}">checked="checked"</c:if> id="icon3checkboxSample" type="checkbox" name="Onsite" value="ONSITE"  aria-invalid="false">
                <label class="form-check-label" for="icon3checkboxSample"><span class="check-square"></span>Onsite</label>
              </div>
            </div>
            <div class="col-xs-12 col-md-3">
              <div class="form-check ">
                <input class="form-check-input" name="PremisesType"<c:if test="${fn:contains(type,'OFFSITE')}">checked="checked"</c:if> id="icon4checkboxSample" type="checkbox" name="Offsite"  value="OFFSITE" aria-invalid="false">
                <label class="form-check-label" for="icon4checkboxSample"><span class="check-square"></span>Offsite</label>
              </div>
            </div>
            <div class="col-xs-12 col-md-3">
              <div class="form-check ">
                <input class="form-check-input" name="PremisesType"<c:if test="${fn:contains(type,'CONVEYANCE')}">checked="checked"</c:if> id="icon5checkboxSample" type="checkbox" value="CONVEYANCE" name="Conveyance" aria-invalid="false">
                <label class="form-check-label" for="icon5checkboxSample"><span class="check-square"></span>Conveyance</label>
              </div>
            </div>
          </div>
        </div>
      </div>
      <br>

      <div class="form-group" style="display: none" id="Subsumption">
        <div class="col-xs-12 col-md-8"  style="margin-bottom: 10px">
          <label class="col-xs-12 col-md-6 control-label" >Subsumption Base Service:<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <select  name="Subsumption">
              <option value="" >Select one</option>
              <c:forEach items="${hcsaServiceCategoryDtos}" var="hcsaServiceCategoryDto">
                <option value="${hcsaServiceCategoryDto.id}">${hcsaServiceCategoryDto.name}</option>
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
            <select  name="Pre-requisite">
              <option value="">Select one</option>
              <c:forEach items="${hcsaServiceCategoryDtos}" var="hcsaServiceCategoryDto">
                <option value="${hcsaServiceCategoryDto.id}">${hcsaServiceCategoryDto.name}</option>
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


      <div class="form-group" >
        <div class="col-xs-12 col-md-8" >
          <label class="col-xs-12 col-md-6 control-label" >Principal Officer (PO)<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-2" >
            <input type="text" name="man-principalOfficer" value="${PO.mandatoryCount}" placeholder="minimum count">
            <input type="text" name="poId" style="display: none" value="${PO.id}">
          </div>
          <div class="col-xs-12 col-md-2" >
            <input type="text" name="mix-principalOfficer" value="${PO.maximumCount}" placeholder="maximum count">
          </div>
          <div class="col-xs-12 col-md-2 form-check" >   <input class="form-check-input"  type="checkbox" name="Conveyance" aria-invalid="false">
            <label class="form-check-label"><span class="check-square"></span>Mandatory</label>
          </div>
        </div>
      </div>


      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" >Deputy Principal Officer (DPO)<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-2">
            <input value="${DPO.id}" name="dpoId" style="display:none;" type="text">
            <input  type="text" name="man-DeputyPrincipalOfficer" value="${DPO.mandatoryCount}" placeholder="minimum count">
          </div>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="mix-DeputyPrincipalOfficer" value="${DPO.maximumCount}"  placeholder="maximum count">
          </div>
          <div class="col-xs-12 col-md-2 form-check">   <input class="form-check-input"  type="checkbox" name="Conveyance" aria-invalid="false">
            <label class="form-check-label"><span class="check-square"></span>Mandatory</label>
          </div>
        </div>
      </div>


      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" >Clinical Governance Officer (CGO)<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-2">
            <input value="${CGO.id}" name="cgoId" style="display:none;" type="text">
            <input  type="text" name="man-ClinicalGovernanceOfficer" value="${CGO.mandatoryCount}" placeholder="minimum count">
          </div>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="mix-ClinicalGovernanceOfficer" value="${CGO.maximumCount}"  placeholder="maximum count">
          </div>
          <div class="col-xs-12 col-md-2 form-check">   <input class="form-check-input"  type="checkbox" name="Conveyance" aria-invalid="false">
            <label class="form-check-label"><span class="check-square"></span>Mandatory</label>
          </div>
        </div>
      </div>


      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" >Service Personnel<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-2">
            <input value="${SVCPSN.id}" name="svcpsnId" style="display:none;" type="text">
            <input  type="text" name="man-ServicePersonnel" value="${SVCPSN.mandatoryCount}" placeholder="minimum count">
          </div>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="mix-ServicePersonnel" value="${SVCPSN.maximumCount}"  placeholder="maximum count">
          </div>
          <div class="col-xs-12 col-md-2 form-check">   <input class="form-check-input"  type="checkbox" name="Conveyance" aria-invalid="false">
            <label class="form-check-label"><span class="check-square"></span>Mandatory</label>
          </div>
        </div>
      </div>


      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" for="NumberDocument">Number of Service-Related Document to be
            uploaded<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="NumberDocument" type="text" value="${NumberDocument}" name="NumberDocument" maxlength="2">
          </div>
          <div class="col-xs-12 col-md-2 form-check">   <input class="form-check-input" <c:if test="${documentMandatory==true}"> checked="checked"</c:if> id="NumberDocumentMandatory"  type="checkbox"  name="NumberDocumentMandatory" aria-invalid="false">
            <label for="NumberDocumentMandatory" class="form-check-label" ><span class="check-square"></span>Mandatory</label>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" for="DescriptionDocument">Description of each Service-Related Document to
            be Uploaded<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="DescriptionDocument" type="text" value="${DescriptionDocument}" name="DescriptionDocument" maxlength="255">
          </div>
          <div class="col-xs-12 col-md-2 form-check">   <input class="form-check-input" <c:if test="${documentMandatory==true}"> checked="checked"</c:if> id="DescriptionDocumentMandatory" type="checkbox" name="DescriptionDocumentMandatory" aria-invalid="false">
            <label for="DescriptionDocumentMandatory" class="form-check-label"><span class="check-square"></span>Mandatory</label>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" for="Numberfields">Number of Service-Related General Info fields to
            be captured<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="Numberfields" type="text" name="Numberfields" maxlength="2">
          </div>
          <div class="col-xs-12 col-md-2 form-check">   <input class="form-check-input"  type="checkbox" name="NumberfieldsMandatory" aria-invalid="false">
            <label class="form-check-label"><span class="check-square"></span>Mandatory</label>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" for="DescriptionGeneral">Description of each Service-Related General Info
            field to be captured*<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="DescriptionGeneral" type="text" name="DescriptionGeneral" maxlength="255">
          </div>
          <div class="col-xs-12 col-md-2 form-check">   <input class="form-check-input"  type="checkbox" name="Conveyance" aria-invalid="false">
            <label class="form-check-label"><span class="check-square"></span>Mandatory</label>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8 marg-1">
          <label class="col-xs-12 col-md-8 control-label" >Service Workload Manhours<span class="mandatory">*</span></label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary " onclick="manhours()"><span class="view">Configure</span></a>
            </div>
          </div>
        </div>
        <div class="col-xs-12 col-md-8 marg-1">
          <label class="col-xs-12 col-md-8 control-label" >Service-Related Checklists<span class="mandatory">*</span></label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary " onclick="checklists()"><span class="view">Configure</span></a>
            </div>
          </div>
        </div>
      </div>
        <br>
      <div class="form-group">
        <div class="col-xs-12 col-md-8 marg-1">
          <label class="col-xs-12 col-md-8 control-label" >Service Risk Score<span class="mandatory">*</span></label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary "><span class="view">Configure</span></a>
            </div>
          </div>
        </div>
      </div>



      <div class="form-group">
        <div class="col-xs-12 col-md-8 marg-1">
          <label class="col-xs-12 col-md-8 control-label" >Service KPI<span class="mandatory">*</span></label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary " onclick="kpi()"><span class="view">Configure</span></a>
            </div>
          </div>
        </div>
      </div>
      <br>
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
      <br>
      <div class="form-group">
        <div class="col-xs-12 col-md-8 marg-1">
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

      <c:forEach items="${routingStagess}" var="routingStages" varStatus="sta">

      <div class="form-group" style="display: none" id="${routingStages.key}" >
        <div class="col-xs-12 col-md-12"  style="margin-top: 10px">
          <table border="1px" style="text-align: center" >
            <tr>
              <th style="width: 10% ;height: 40px;text-align: center;padding: 1% 0% 1% "> application type<span class="mandatory" >*</span></th>
              <th  style="width: 20% ;height: 40px;text-align: center"> Service Workflow Routing Stages<span class="mandatory" >*</span></th>
              <th  style="width: 30% ;height: 40px;text-align: center">Service Routing Scheme<span class="mandatory">*</span></th>
              <th  style="width: 15% ;height: 40px;text-align: center">Service Workload Manhours<span class="mandatory">*</span></th>
              <th  style="width: 25% ;height: 40px;text-align: center">working group<span class="mandatory">*</span></th>
            </tr>
            <c:forEach items="${routingStages.value}" var="routingStage" varStatus="status">
              <tr >
                <td >${routingStage.appTypeName}</td>
                <td >${routingStage.stageName}</td>
                <td >
                  <div class="col-xs-12 col-md-6" style="margin-top: 1%;margin-bottom: 1%" >
                    <input type="text" name="stageId${routingStage.stageCode}${routingStages.key}" value="${routingStage.routingSchemeId}" style="display:none;">

                    <select  name="RoutingScheme${routingStage.stageCode}${routingStages.key}"   >
                      <option value="">Select one</option>
                      <option value="common"
                              <c:if test="${routingStage.routingSchemeName=='common'}">selected="selected" </c:if>
                      >Common Pool</option>
                      <option value="assign"
                              <c:if test="${routingStage.routingSchemeName=='assign'}">selected="selected" </c:if>
                      >Supervisor Assign</option>
                      <option value="round"
                              <c:if test="${routingStage.routingSchemeName=='round'}">selected="selected" </c:if>
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
                    <input style="margin: 8px 0px 8px" type="text" maxlength="2" name="WorkloadManhours${routingStage.stageCode}${routingStages.key}" value="${routingStage.manhours}" >
                    <span class="error-msg" name="iaisErrorMsg" id="error_manhourCount${status.index}"></span>
                  </div>

                </td>
                <td>
                  <div class="col-xs-12 col-md-12">
                    <input name="workstageId${routingStage.stageCode}${routingStages.key}" type="text" style="display: none" value="${routingStage.workStageId}">
                    <select name="workingGroup${routingStage.stageCode}${routingStages.key}">
                      <option value="">Select one</option>
                      <c:forEach items="${routingStage.workingGroup}" var="workingGroup">
                        <option <c:if test="${routingStage.workingGroupId==workingGroup.id}">selected="selected"</c:if> value="${workingGroup.id}">${workingGroup.groupName}</option>
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
          <span name="iaisErrorMsg" class="error-msg" id="error_hcsaSvcSubtypeOrSubsumed"></span>
          <div  class="col-xs-12 col-md-5"><input  type="text" value="Laboratory Disciplines" ></div>

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
                <a class="btn  btn-secondary  view" onclick="indents(this)"  >indent</a>
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
            <input type="text"  value="${hcsaServiceDto.effectiveDate}" autocomplete="off" class="date_picker form-control form_datetime" name="StartDate" id="-20189532301300" data-date-start-date="01/01/1900" placeholder="dd/mm/yyyy" maxlength="10"><span id="error_StartDate" name="iaisErrorMsg" class="error-msg"></span>
          </div>
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
          <div class="col-xs-10 col-md-3">
            <div class="components">

              <a class="btn  btn-secondary" onclick="cancel()">Cancel</a>

            </div>
          </div>
          <div class="col-xs-10 col-md-3">
            <div class="components">

              <a class="btn  btn-primary " onclick="save()">Save</a>
            </div>
          </div>

          <div class="col-xs-10 col-md-3">
            <div class="components">
              <a class="btn  btn-secondary  " onclick="saveAsNewVersion()">Select as New Version</a>
            </div>
          </div>
          <div class="col-xs-10 col-md-3"  style="margin-left: 50%;margin-top: 1%;">
            <div class="components" style="display: none" id="versionSelect">
              <select name="versionSelect" id="version">
                <option value="">Select one</option>
                <c:forEach items="${hcsaServiceDtosVersion}" var="hcsaServiceDtosVer">
                  <option  value="${hcsaServiceDtosVer.id}">${hcsaServiceDtosVer.version}</option>
                </c:forEach>
              </select>
            </div>
          </div>
        </div>
      </div>
      <div class="col-xs-12 col-md-12" style="text-align: center">
        <div class="row">
          <div class="col-xs-10 col-md-8">
            <div class="components">
              <input style="display: none" value="${hcsaServiceDto.version}" name="version" type="text">
              <p style="text-align: center" >Version ${hcsaServiceDto.version}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <%@ include file="/include/validation.jsp" %>
  </form>
</div>



<script type="text/javascript">


    function kpi() {
        var b = confirm("Are you sure you want to leave this page");
        if(b==true){
            config();
        }else {

        }
    }

    function config() {
        location.href="https://egp.sit.intra.iais.com/hcsa-licence-web/eservice/INTRANET/MohKPIAndReminder";
    }

    function  checklists(){

        var b = confirm("Are you sure you want to leave this page");
        if(b==true){
            location.href="https://egp.sit.intra.iais.com/hcsa-licence-web/eservice/INTRANET/MohKPIAndReminder";
        }else {

        }
    }
    function manhours(){
        location.href="";
    }
    $('#versionSelect').change(function () {

        SOP.Crud.cfxSubmit("mainForm","version","version",$('#version').val());

    });

    function saveAsNewVersion() {
        $('#versionSelect').attr("style","display: block");
    }

    $(document).ready(function(){
        let val = $("select[name='ServiceType']").val();
        if("SVTP002"==val){
          $('#Subsumption').attr("style","display:style");
          $('#Pre-requisite').attr("style","display:none");
        }else if("SVTP003"==val){
            $('#Subsumption').attr("style","display:none");
            $('#Pre-requisite').attr("style","display:style")
        }else {
            $('#Subsumption').attr("style","display:none");
            $('#Pre-requisite').attr("style","display:none");
        }

    });

    function cancel() {
        SOP.Crud.cfxSubmit("mainForm", "cancel","cancel","");
    }

    function save() {
        $('#ServiceType').removeAttr("disabled");
        $("input[name='StartDate']").removeAttr("disabled");

        SOP.Crud.cfxSubmit("mainForm","save","save","save");
    }


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
