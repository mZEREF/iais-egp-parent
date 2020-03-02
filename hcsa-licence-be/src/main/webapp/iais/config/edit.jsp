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

<div class="main-content">
  <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="col-lg-12 col-xs-10">
      <div class="bg-title" style="text-align: center">
        <h2>  HCSA Configurator Module</h2>
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
            <input id="serviceName" name="serviceName" disabled type="text" value="${hcsaServiceDto.svcName}">
          </div>
        </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="description">Service Description<span class="mandatory" >*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="description" name="description" type="text" value="${hcsaServiceDto.svcDesc}">
          </div>
        </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="displayDescription">Service Display Description<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="displayDescription" name="displayDescription" type="text" value="${hcsaServiceDto.svcDisplayDesc}">
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="serviceCode">Service Code<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="serviceCode" name="serviceCode" disabled type="text"  value="${hcsaServiceDto.svcCode}">
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="ServiceType">Service Type<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <select id="ServiceType" disabled name="ServiceType">

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
              <option >Select one</option>
              <c:forEach items="${hcsaServiceCategoryDtos}" var="hcsaServiceCategoryDto">
                <option value="${hcsaServiceCategoryDto.id}">${hcsaServiceCategoryDto.name}</option>
              </c:forEach>
            </select>
          </div>
        </div>
      </div>



      <div class="form-group" style="display: none" id="Pre-requisite">
        <div class="col-xs-12 col-md-8" style="margin-bottom: 10px">
          <label class="col-xs-12 col-md-6 control-label" >Pre-requisite Base Service:<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <select  name="Subsumption">
              <option >Select one</option>
              <c:forEach items="${hcsaServiceCategoryDtos}" var="hcsaServiceCategoryDto">
                <option value="${hcsaServiceCategoryDto.id}">${hcsaServiceCategoryDto.name}</option>
              </c:forEach>
            </select>
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
            <input id="NumberDocument" type="text">
          </div>
          <div class="col-xs-12 col-md-2 form-check">   <input class="form-check-input"  type="checkbox" name="Conveyance" aria-invalid="false">
            <label class="form-check-label" ><span class="check-square"></span>Mandatory</label>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" for="DescriptionDocument">Description of each Service-Related Document to
            be Uploaded<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="DescriptionDocument" type="text">
          </div>
          <div class="col-xs-12 col-md-2 form-check">   <input class="form-check-input"  type="checkbox" name="Conveyance" aria-invalid="false">
            <label class="form-check-label"><span class="check-square"></span>Mandatory</label>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" for="Numberfields">Number of Service-Related General Info fields to
            be captured<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="Numberfields" type="text">
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
            <input id="DescriptionGeneral" type="text">
          </div>
          <div class="col-xs-12 col-md-2 form-check">   <input class="form-check-input"  type="checkbox" name="Conveyance" aria-invalid="false">
            <label class="form-check-label"><span class="check-square"></span>Mandatory</label>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" >Service-Related Checklists<span class="mandatory">*</span></label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary "><span class="view">Configure</span></a>
            </div>
          </div>
        </div>
      </div>
        <br>
      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" >Service Risk Score<span class="mandatory">*</span></label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary "><span class="view">Configure</span></a>
            </div>
          </div>
        </div>
      </div>
      <br>
      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" >Service KPI<span class="mandatory">*</span></label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary "><span class="view">Configure</span></a>
            </div>
          </div>
        </div>
      </div>
      <br>
      <div class="form-group">
        <div class="col-xs-12 col-md-8">
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
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" >Service Fee Bundles<span class="mandatory">*</span></label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary "><span class="view">Configure</span></a>
            </div>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary " onclick="showNEW()"><span class="view">NEW APPLICATION</span></a>

            </div>
          </div>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary " onclick="showRENEW()"><span class="view">RENEW</span></a>
            </div>
          </div>
        </div>
      </div>
      <c:forEach items="${routingStagess}" var="routingStages">

      <div class="form-group" style="display: none" id="${routingStages.key}" >
        <div class="col-xs-12 col-md-12"  style="margin-top: 10px">
          <table border="1px" style="text-align: center" >
            <tr>
              <th style="width: 15% ;height: 40px;text-align: center"> application type<span class="mandatory" >*</span></th>
              <th  style="width: 20% ;height: 40px;text-align: center"> Service Workflow Routing Stages<span class="mandatory" >*</span></th>
              <th  style="width: 20% ;height: 40px;text-align: center">Service Routing Scheme<span class="mandatory">*</span></th>
              <th  style="width: 15% ;height: 40px;text-align: center">Service Workload Manhours<span class="mandatory">*</span></th>
              <th  style="width: 30% ;height: 40px;text-align: center">working group<span class="mandatory">*</span></th>
            </tr>
            <c:forEach items="${routingStages.value}" var="routingStage" varStatus="status">
              <tr>
                <td >${routingStage.appTypeName}</td>
                <td >${routingStage.stageName}</td>
                <td>
                  <div class="col-xs-12 col-md-12">
                    <input type="text" name="stageId${routingStage.stageCode}${routingStages.key}" value="${routingStage.routingSchemeId}" style="display:none;">
                    <select  name="RoutingScheme${routingStage.stageCode}${routingStages.key}"   >
                      <option value="">Select one</option>
                      <option value="common"
                              <c:if test="${routingStage.stageCode=='PSO'}">selected="selected" </c:if>

                      >Common Pool</option>
                      <option value="assign">Supervisor Assign</option>
                      <option value="round"
                              <c:if test="${routingStage.stageCode=='ASO'||routingStage.stageCode=='AO1'||routingStage.stageCode=='AO2'||routingStage.stageCode=='AO3'}">selected="selected"  </c:if>
                      >Round Robin</option>
                    </select>
                  </div>
                </td>
                <td>
                  <div class="col-xs-12 col-md-12">
                    <input type="text" style="display:none;" value="${routingStage.workloadId}" name="workloadId${routingStage.stageCode}${routingStages.key}" >
                    <input  type="text" name="WorkloadManhours${routingStage.stageCode}${routingStages.key}"  value="${routingStage.manhours}">
                    <span class="error-msg" name="iaisErrorMsg" id="error_manhourCount${status.index}"></span>
                  </div>

                </td>
                <td>
                  <div class="col-xs-12 col-md-12">
                    <input name="workstageId${routingStage.stageCode}${routingStages.key}" type="text" style="display: none" value="${routingStage.workStageId}">
                    <select name="workingGroup${routingStage.stageCode}${routingStages.key}">
                      <option value="">Select one</option><c:forEach items="${routingStage.workingGroup}" var="workingGroup">
                      <option <c:if test="${routingStage.workingGroupId==workingGroup.id}">selected="selected"</c:if> value="${workingGroup.id}">${workingGroup.groupName}</option>
                    </c:forEach>
                    </select>
                  </div>
                </td>
              </tr>
            </c:forEach>

          </table>
        </div>
      </div>
      </c:forEach>


      <div class="form-group">
        <div class="col-xs-12 col-md-12"  style="margin-top: 10px">
          <label class="col-xs-12 col-md-10 control-label" >Service step<span class="mandatory">*</span></label>
          <span name="iaisErrorMsg" class="error-msg" id="error_serviceStep" style="display: block"></span>
        </div>
      </div>
      <c:set var="hcsaServiceStepScheme" value="${hcsaServiceStepSchemeDto}"></c:set>
      <div class="form-group">
        <div class="form-check-gp">
          <div class="row">
            <div class="col-xs-12 col-md-2">
              <div class="form-check " style="left: 10%">
                <input class="form-check-input" name="step" <c:if test="${fn:contains(hcsaServiceStepScheme, 'SVST001')}">checked="checked"</c:if>  type="checkbox" value="SVST001"  aria-invalid="false">
                <label class="form-check-label" for="icon3checkboxSample"><span class="check-square"></span>laboratorydisciplines</label>
              </div>
            </div>
            <div class="col-xs-12 col-md-2">
              <div class="form-check ">
                <input class="form-check-input"  name="step" <c:if test="${fn:contains(hcsaServiceStepScheme, 'SVST002')}">checked="checked"</c:if>   type="checkbox" value="SVST002" aria-invalid="false">
                <label class="form-check-label" for="icon4checkboxSample"><span class="check-square"></span>governanceofficers</label>
              </div>
            </div>
            <div class="col-xs-12 col-md-2">
              <div class="form-check ">
                <input class="form-check-input"  name="step" <c:if test="${fn:contains(hcsaServiceStepScheme, 'SVST003')}">checked="checked"</c:if>   type="checkbox" value="SVST003"  aria-invalid="false">
                <label class="form-check-label" for="icon5checkboxSample"><span class="check-square"></span>disciplineallocation</label>
              </div>
            </div>
            <div class="col-xs-12 col-md-2">
              <div class="form-check ">
                <input class="form-check-input"  name="step" <c:if test="${fn:contains(hcsaServiceStepScheme, 'SVST004')}">checked="checked"</c:if>  type="checkbox" value="SVST004" aria-invalid="false">
                <label class="form-check-label" for="icon5checkboxSample"><span class="check-square"></span>principalofficers</label>
              </div>
            </div>
            <div class="col-xs-12 col-md-2">
              <div class="form-check ">
                <input class="form-check-input"  name="step" <c:if test="${fn:contains(hcsaServiceStepScheme, 'SVST005')}">checked="checked"</c:if>  type="checkbox" value="SVST005"  aria-invalid="false">
                <label class="form-check-label" for="icon5checkboxSample"><span class="check-square"></span>documents</label>
              </div>
            </div>
            <div class="col-xs-12 col-md-1">
              <div class="form-check ">
                <input class="form-check-input" <c:if test="${fn:contains(hcsaServiceStepScheme, 'SVST006')}">checked="checked"</c:if> name="step"   type="checkbox" value="SVST006"  aria-invalid="false">
                <label class="form-check-label" for="icon5checkboxSample"><span class="check-square"></span>nuclearmedicineimaging</label>
              </div>
            </div>
          </div>
        </div>
      </div>





      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="Sub-Types">Service Sub-Types</label>
          <div class="col-xs-12 col-md-4">
            <input id="Sub-Types" type="text">
          </div>
        </div>
      </div>

      <div class="col-xs-12 col-md-8">
        <div class="form-group">
          <label class="col-xs-12 col-md-8 control-label">Effective Start Date<span class="mandatory">*</span></label>
          <div class=" col-xs-7 col-sm-4 col-md-3">
            <input type="text" disabled value="${hcsaServiceDto.effectiveDate}" autocomplete="off" class="date_picker form-control form_datetime" name="StartDate" id="-20189532301300" data-date-start-date="01/01/1900" placeholder="dd/mm/yyyy" maxlength="10"><span id="error_StartDate" name="iaisErrorMsg" class="error-msg"></span>
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
          <div class="col-xs-10 col-md-8">
            <div class="components">

              <a class="btn  btn-secondary" onclick="cancel()">Cancel</a>

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
              <input style="display: none" value="${hcsaServiceDto.version}" name="version" type="text">
              <p style="text-align: center" >Version ${hcsaServiceDto.version}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </form>
</div>
<style>
  .mandatory{
    color: red;
  }
  .view{
    color: #2199E8;
  }
</style>
<script type="text/javascript">

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
        location.href="https://egp.sit.intra.iais.com/hcsa-licence-web/eservice/INTRANET/MohServiceConfig";
    }

    function save() {
        SOP.Crud.cfxSubmit("mainForm","save","save","save");
    }


    function showNEW() {
        let jQuery = $('#APTY002').attr("style");
        $('#APTY001').attr("style","display: none");
        if(jQuery=='display: block'){
            $('#APTY002').attr("style","display: none");
        }else if(jQuery=='display: none'){
            $('#APTY002').attr("style","display: block");
        }
    }

    function showRENEW() {
        let jQuery = $('#APTY001').attr("style");
        $('#APTY002').attr("style","display: none");
        if(jQuery=='display: block'){
            $('#APTY001').attr("style","display: none");
        }else if(jQuery=='display: none'){
            $('#APTY001').attr("style","display: block");
        }
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

</script>
</>
