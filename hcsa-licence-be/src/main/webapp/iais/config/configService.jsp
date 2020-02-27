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
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="ServiceType">Service Type<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">

            <select id="ServiceType" name="ServiceType">
              <option value="">Select one</option>
              <option value="SVTP001" selected="selected">Base</option>
              <option value="SVTP002">Subsumed</option>
              <option value="SVTP003">Specified</option>
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


      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" >Subsumption Base Service:<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <select  name="Subsumption">
              <option >Select one</option>
              <option>Acute Hospital</option>
              <option>Community Hospital</option>
            </select>
          </div>
        </div>
      </div>



      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" >Pre-requisite Base Service:<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <select  name="Subsumption">
              <option >Select one</option>
              <option>Acute Hospital</option>
              <option>Community Hospital</option>
            </select>
          </div>
        </div>
      </div>



      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label">Principal Officer (PO)<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-2">
            <input type="text" name="man-principalOfficer" placeholder="mandatory count" value="${PO.mandatoryCount}">
          </div>
          <div class="col-xs-12 col-md-2">
            <input type="text" name="mix-principalOfficer" placeholder="maximum count" value="${PO.maximumCount}">
          </div>
          <div class="col-xs-12 col-md-2 form-check">   <input class="form-check-input"  type="checkbox" name="Conveyance" aria-invalid="false">
            <label class="form-check-label" ><span class="check-square"></span>Mandatory</label>
          </div>
          <span class="error-msg" name="iaisErrorMsg" id="error_psnType1"></span>
        </div>
      </div>


      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" >Deputy Principal Officer (DPO)<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="man-DeputyPrincipalOfficer" placeholder="mandatory count" value="${DPO.mandatoryCount}">
          </div>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="mix-DeputyPrincipalOfficer"  placeholder="maximum count" value="${DPO.maximumCount}">
          </div>

          <div class="col-xs-12 col-md-2 form-check">   <input class="form-check-input"  type="checkbox" name="Conveyance" aria-invalid="false">
            <label class="form-check-label" ><span class="check-square"></span>Mandatory</label>
          </div>
          <span class="error-msg" name="iaisErrorMsg" id="error_psnType2"></span>
        </div>
      </div>


      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" >Clinical Governance Officer (CGO)<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="man-ClinicalGovernanceOfficer" placeholder="mandatory count" value="${CGO.mandatoryCount}">
          </div>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="mix-ClinicalGovernanceOfficer"  placeholder="maximum count" value="${CGO.maximumCount}">
          </div>
          <div class="col-xs-12 col-md-2 form-check">   <input class="form-check-input"  type="checkbox" name="Conveyance" aria-invalid="false">
            <label class="form-check-label" ><span class="check-square"></span>Mandatory</label>
          </div>
          <span class="error-msg" name="iaisErrorMsg" id="error_psnType3"></span>
        </div>
      </div>


      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" >Service Personnel<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="man-ServicePersonnel" placeholder="mandatory count" value="${SVCPSN.mandatoryCount}">
          </div>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="mix-ServicePersonnel"  placeholder="maximum count" value="${SVCPSN.maximumCount}">
          </div>
          <div class="col-xs-12 col-md-2 form-check">   <input class="form-check-input"  type="checkbox" name="Conveyance" aria-invalid="false">
            <label class="form-check-label" ><span class="check-square"></span>Mandatory</label>
          </div>
        </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" for="NumberDocument">Number of Service-Related Document to be
            uploaded<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="NumberDocument" type="text" name="NumberDocument">
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
            <input id="DescriptionDocument" type="text" name="DescriptionDocument">
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
            <input id="Numberfields" type="text" name="Numberfields">
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
            <input id="DescriptionGeneral" type="text" name="DescriptionGeneral">
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
          <th style="width: 15% ;height: 40px;text-align: center">application type<span class="mandatory" >*</span></th>
          <th  style="width: 20% ;height: 40px;text-align: center">Service Workflow Routing Stages<span class="mandatory" >*</span></th>
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
            <select  name="RoutingScheme${routingStage.stageCode}${routingStages.key}"  >
              <option >Select one</option>
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
            <input  type="text" name="WorkloadManhours${routingStage.stageCode}${routingStages.key}" value="${routingStage.manhours}" >
            <span class="error-msg" name="iaisErrorMsg" id="error_manhourCount${status.index}"></span>
          </div>

        </td>
        <td>
          <div class="col-xs-12 col-md-12">

            <select name="workingGroup${routingStage.stageCode}${routingStages.key}">
              <option value="">Select one</option><c:forEach items="${routingStage.workingGroup}" var="workingGroup">
              <option <c:if test="${routingStage.workingGroupId==workingGroup.id}">selected="selected"</c:if>value="${workingGroup.id}">${workingGroup.groupName}</option>
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

      <div class="form-group">
        <div class="form-check-gp">
          <div class="row">
            <div class="col-xs-12 col-md-2">
              <div class="form-check " style="left: 10%">
                <input class="form-check-input" name="step"   type="checkbox" value="SVST001"  aria-invalid="false">
                <label class="form-check-label" for="icon3checkboxSample"><span class="check-square"></span>laboratorydisciplines</label>
              </div>
            </div>
            <div class="col-xs-12 col-md-2">
              <div class="form-check ">
                <input class="form-check-input"  name="step"    type="checkbox" value="SVST002" aria-invalid="false">
                <label class="form-check-label" for="icon4checkboxSample"><span class="check-square"></span>governanceofficers</label>
              </div>
            </div>
            <div class="col-xs-12 col-md-2">
              <div class="form-check ">
                <input class="form-check-input"  name="step"   type="checkbox" value="SVST003"  aria-invalid="false">
                <label class="form-check-label" for="icon5checkboxSample"><span class="check-square"></span>disciplineallocation</label>
              </div>
            </div>
            <div class="col-xs-12 col-md-2">
              <div class="form-check ">
                <input class="form-check-input"  name="step"   type="checkbox" value="SVST004" aria-invalid="false">
                <label class="form-check-label" for="icon5checkboxSample"><span class="check-square"></span>principalofficers</label>
              </div>
            </div>
            <div class="col-xs-12 col-md-2">
              <div class="form-check ">
                <input class="form-check-input"  name="step"   type="checkbox" value="SVST005"  aria-invalid="false">
                <label class="form-check-label" for="icon5checkboxSample"><span class="check-square"></span>documents</label>
              </div>
            </div>
            <div class="col-xs-12 col-md-1">
              <div class="form-check ">
                <input class="form-check-input"  name="step"   type="checkbox" value="SVST006"  aria-invalid="false">
                <label class="form-check-label" for="icon5checkboxSample"><span class="check-square"></span>nuclearmedicineimaging</label>
              </div>
            </div>
          </div>
        </div>
      </div>




      <div class="form-group">
        <div class="col-xs-12 col-md-8" style="margin-top: 10px">
          <label class="col-xs-12 col-md-8 control-label" for="Sub-Types">Service Sub-Types</label>
          <div class="col-xs-12 col-md-4">
            <input id="Sub-Types" type="text" name="Sub-Types">
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
<style>
  .mandatory{
    color: red;
  }
  .view{
    color: #2199E8;
  }
</style>
<script type="text/javascript">

    function cancel() {

        SOP.Crud.cfxSubmit("mainForm","");
    }

    function save() {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm","save");
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
</script>
</>
