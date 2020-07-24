<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<webui:setLayout name="iais-intranet"/>

<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

<div class="main-content">
  <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="col-lg-12 col-xs-10">
    <div class="bg-title" style="text-align: center">
      <h2>HCSA Configurator Module</h2>
    </div>
      <div class="form-group">
      <div class="col-xs-12 col-md-10">
        <h2 class="component-title">Preview HCSA Service Edit</h2>
      </div>
      </div>
      <div class="form-group">
      <div class="col-xs-12 col-md-8">
        <label class="col-xs-12 col-md-8 control-label" for="serviceName">Service Name<span class="mandatory" >*</span></label>
        <div class="col-xs-12 col-md-4">
          <input id="serviceName" name="serviceName" maxlength="100" type="text" readonly value="${hcsaServiceDto.svcName}">
        </div>
      </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="description">Service Description<span class="mandatory" >*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="description" name="description" maxlength="255" readonly type="text" value="${hcsaServiceDto.svcDesc}">
          </div>
        </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="displayDescription">Service Display Description<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="displayDescription" name="displayDescription" maxlength="255" readonly type="text" value="${hcsaServiceDto.svcDisplayDesc}">
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="serviceCode">Service Code<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="serviceCode" type="text" name="serviceCode" maxlength="3" readonly value="${hcsaServiceDto.svcCode}">
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="ServiceType">Service Type<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">

            <select  id="ServiceType" disabled name="ServiceType">
              <option value="">Select one</option>
              <option value="SVTP001" <c:if test="${hcsaServiceDto.svcType=='SVTP001'}">selected="selected"</c:if>>Base</option>
              <option value="SVTP002" <c:if test="${hcsaServiceDto.svcType=='SVTP002'}">selected="selected"</c:if>>Subsumed</option>
              <option value="SVTP003" <c:if test="${hcsaServiceDto.svcType=='SVTP003'}">selected="selected"</c:if> >Specified</option>
            </select>
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
              <input class="form-check-input" readonly  id="icon3checkboxSample" <c:if test="${fn:contains(type,'ONSITE')}">checked="checked"</c:if> type="checkbox" name="Onsite" aria-invalid="false">
              <label class="form-check-label"  for="icon3checkboxSample"><span class="check-square"></span>Onsite</label>
            </div>
          </div>
          <div class="col-xs-12 col-md-3">
            <div class="form-check ">
              <input class="form-check-input" readonly id="icon4checkboxSample"  <c:if test="${fn:contains(type,'OFFSIET')}">checked="checked"</c:if> type="checkbox" name="Offsite" aria-invalid="false">
              <label class="form-check-label"  for="icon4checkboxSample"><span class="check-square"></span>Offsite</label>
            </div>
          </div>
          <div class="col-xs-12 col-md-3">
            <div class="form-check ">
              <input class="form-check-input" readonly id="icon5checkboxSample"  <c:if test="${fn:contains(type,'CONVEYANCE')}">checked="checked"</c:if> type="checkbox" name="Conveyance" aria-invalid="false">
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
                <option value="${hcsaServiceCategoryDto.id}" <c:if test="${hcsaServiceDto.categoryId==hcsaServiceCategoryDto.id}">selected</c:if>>${hcsaServiceCategoryDto.name}</option>
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
              <option value="">Select one</option>
              <c:forEach items="${hcsaServiceCategoryDtos}" var="hcsaServiceCategoryDto">
                <option value="${hcsaServiceCategoryDto.id}" <c:if test="${hcsaServiceDto.categoryId==hcsaServiceCategoryDto.id}">selected</c:if>>${hcsaServiceCategoryDto.name}</option>
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


      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" >Principal Officer (PO)<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-2">
            <input type="text" name="man-principalOfficer" maxlength="2" readonly value="${PO.mandatoryCount}" placeholder="mandatory count">
          </div>
          <div class="col-xs-12 col-md-2">
            <input type="text" name="mix-principalOfficer" maxlength="2"  readonly value="${PO.maximumCount}" placeholder="maximum count">
          </div>
        </div>
      </div>


      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label">Deputy Principal Officer (DPO)<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="man-DeputyPrincipalOfficer" maxlength="2" readonly value="${DPO.mandatoryCount}" placeholder="mandatory count">
          </div>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="mix-DeputyPrincipalOfficer" maxlength="2" readonly  value="${DPO.maximumCount}"  placeholder="maximum count">
          </div>
        </div>
      </div>


      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" >Clinical Governance Officer (CGO)<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="man-ClinicalGovernanceOfficer" maxlength="2"  readonly value="${CGO.mandatoryCount}" placeholder="mandatory count">
          </div>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="mix-ClinicalGovernanceOfficer" maxlength="2"  readonly value="${CGO.maximumCount}"  placeholder="maximum count">
          </div>
        </div>
      </div>


      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" >Service Personnel<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="man-ServicePersonnel" maxlength="2" readonly value="${SVCPSN.mandatoryCount}" placeholder="mandatory count">
          </div>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="mix-ServicePersonnel" maxlength="2" readonly value="${SVCPSN.maximumCount}" placeholder="maximum count">
          </div>
        </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" >Medalert Person<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-2">
            <input value="${MAP.id}" name="svcpsnId" maxlength="2" style="display:none;" type="text">
            <input  type="text" name="man-MedalertPerson" maxlength="2" value="${MAP.mandatoryCount}" placeholder="minimum count">
          </div>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="mix-MedalertPerson" maxlength="2" value="${MAP.maximumCount}"  placeholder="maximum count">
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" for="NumberDocument">Number of Service-Related Document to be
            uploaded<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="NumberDocument"  readonly  type="text" maxlength="2" value="${NumberDocument}">
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" for="DescriptionDocument">Description of each Service-Related Document to
            be Uploaded<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="DescriptionDocument" maxlength="255"  value="${DescriptionDocument}" readonly type="text">
          </div>

        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" for="Numberfields">Number of Service-Related General Info fields to
            be captured<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="Numberfields" readonly type="text" maxlength="2" value="${comDocConfigDtoSize}">
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" for="DescriptionGeneral">Description of each Service-Related General Info field to be captured<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="DescriptionGeneral" readonly type="text" maxlength="255" value="${comDocConfigDtosTitle}">
          </div>
        </div>
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
        <div class="col-xs-12 col-md-8 marg-1">
          <label class="col-xs-12 col-md-8 control-label" >Service-Related Checklists<span class="mandatory">*</span></label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
             <a class="btn btn-secondary " data-toggle="modal" data-target= "#checklists" style="padding: 12px 60px"><span class="view">view</span></a>
            </div>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8 marg-1">
          <label class="col-xs-12 col-md-8 control-label" >Service Risk Score<span class="mandatory">*</span></label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary " data-toggle="modal" data-target= "#riskScore" style="padding: 12px 60px"><span class="view">view</span></a>
            </div>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8 marg-1">
          <label class="col-xs-12 col-md-8 control-label" >Service KPI<span class="mandatory">*</span></label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary " data-toggle="modal" data-target= "#kpi" style="padding: 12px 60px"><span class="view">view</span></a>
            </div>
          </div>
        </div>
      </div>

      <%--<div class="form-group">
        <div class="col-xs-12 col-md-8 marg-1">
          <label class="col-xs-12 col-md-8 control-label" >Service Fees<span class="mandatory">*</span></label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary "  style="padding: 12px 60px"><span class="view">view</span></a>
            </div>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8 marg-1">
          <label class="col-xs-12 col-md-8 control-label" >Service Fee Bundles<span class="mandatory">*</span></label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary " style="padding: 12px 60px"><span class="view">view</span></a>
            </div>
          </div>
        </div>
      </div>--%>

      <div class="form-group">
        <div class="col-xs-12 col-md-12" style="margin-top: 1%">
          <div class="col-xs-10 col-md-3">
            <div class="components width-center">
              <a class="btn btn-secondary width-70 " onclick="showNEW()"><span class="view">NEW APPLICATION</span></a>
            </div>
          </div>
          <div class="col-xs-10 col-md-3">
            <div class="components width-center">
              <a class="btn btn-secondary width-70" onclick="showRENEW()"><span class="view">RENEW</span></a>
            </div>
          </div>
          <div class="col-xs-10 col-md-3">
            <div class="components width-center">
              <a class="btn btn-secondary width-70" onclick="showAPPEAL()"><span class="view">APPEAL</span></a>
            </div>
          </div>
          <div class="col-xs-10 col-md-3">
            <div class="components width-center" style="text-align: center;width: 100%">
              <a class="btn btn-secondary width-70" style="width: 70%" onclick="showRFC()"><span class="view">REQUEST FOR CHANGE</span></a>
            </div>
          </div>
        </div>
      </div>

      <div  class="form-group">
        <div class="col-xs-12 col-md-12" style="margin-top: 1%">
          <div class="col-xs-10 col-md-3">
            <div class="components width-center">
              <a class="btn btn-secondary width-70" onclick="showCESSATION()"><span class="view">CESSATION</span></a>
            </div>
          </div>
         <%-- <div class="col-xs-10 col-md-3">
            <div class="components width-center">
              <a class="btn btn-secondary width-70" onclick="showSUSPENSION()"><span class="view">SUSPENSION</span></a>
            </div>
          </div>--%>

          <div class="col-xs-10 col-md-3">
            <div class="components width-center">
              <a class="btn btn-secondary width-70" onclick="showWITHDRAWAL()"><span class="view">WITHDRAWAL</span></a>
            </div>
          </div>
         <%-- <div class="col-xs-10 col-md-3">
            <div class="components width-center">
              <a class="btn btn-secondary width-70 " onclick="showREVOCATION()"><span class="view">REVOCATION</span></a>
            </div>
          </div>--%>

        </div>
      </div>



      <c:forEach items="${routingStagess}" var="routingStages">

      <div class="form-group" style="display: none" id="${routingStages.key}">
        <div class="col-xs-12 col-md-12"  style="margin-top: 10px" >
          <table border="1px" style="text-align: center" >
            <tr>
              <th style="width: 10% ;height: 40px;text-align: center"> Application Type<span class="mandatory" >*</span></th>
              <th  style="width: 20% ;height: 40px;text-align: center"> Service Workflow Routing Stages<span class="mandatory" >*</span></th>
              <th  style="width: 30% ;height: 40px;text-align: center">Service Routing Scheme<span class="mandatory">*</span></th>
              <th  style="width: 15% ;height: 40px;text-align: center">Service Workload Manhours<span class="mandatory">*</span></th>
              <th  style="width: 25% ;height: 40px;text-align: center">Working Group<span class="mandatory">*</span></th>
            </tr>
            <c:forEach items="${routingStages.value}" var="routingStage" varStatus="status">
              <tr>
                <td >${routingStage.appTypeName}</td>
                <td >${routingStage.stageName}</td>
                <td>
                  <div class="col-xs-12 col-md-6"  style="margin-top: 1%;margin-bottom: 1%">
                    <select disabled name="isMandatory${routingStage.stageCode}${routingStages.key}">
                      <option value="">Please Select</option>
                      <option value="mandatory" selected="selected">Mandatory</option>
                      <option value="optional">Optional</option>
                    </select>
                  </div>

                  <div class="col-xs-12 col-md-6" style="margin-top: 1%;margin-bottom: 1%">
                    <select  disabled name="RoutingScheme${routingStage.stageCode}"  >
                      <option value="">Please Select</option>
                      <option value="common"
                              <c:if test="${routingStage.routingSchemeName=='common'}">selected="selected" </c:if>

                      >Common Pool</option>
                      <option value="assign" <c:if test="${routingStage.routingSchemeName=='assign'}">selected="selected" </c:if>
                      >Supervisor Assign</option>
                      <option value="round"
                              <c:if test="${routingStage.routingSchemeName=='round'}">selected="selected" </c:if>
                      >Round Robin</option>
                    </select>
                  </div>

                </td>

                <td>
                  <div class="col-xs-12 col-md-12">
                    <input style="margin: 8px 0px 8px" disabled type="text" maxlength="2" name="WorkloadManhours${routingStage.stageCode}${routingStages.key}" value="${routingStage.manhours}" >
                    <span class="error-msg" name="iaisErrorMsg" id="error_manhourCount${status.index}"></span>
                  </div>

                </td>
                <td>
                  <div class="col-xs-12 col-md-12">
                    <div class="col-xs-12 col-md-12" style="margin-top: 1%;margin-bottom: 1%">
                      <select  disabled name="workingGroup${routingStage.stageCode}">
                        <option value="">Please Select</option><c:forEach items="${routingStage.workingGroup}" var="workingGroup">
                        <option <c:if test="${routingStage.workingGroupId==workingGroup.id}">selected="selected"</c:if> value="${workingGroup.id}">${workingGroup.groupName}</option>
                      </c:forEach>
                      </select>
                    </div>
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
        <div class="col-xs-12 col-md-6" style="margin-top: 20px ;margin-bottom: 20px">
          <label class="col-xs-12 col-md-8 control-label" >Service Sub-Types</label>
          <div class="col-xs-12 col-md-7">
            <label>Page Name</label>
          </div >
          <div  class="col-xs-12 col-md-5"><input  type="text" maxlength="100" value="" ></div>

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
                <input class="add" type="text"  style="margin-left:0px" maxlength="100" readonly name="subType" value="${hcsaSvcSubtypeOrSubsumed.name}">
              </div>
              <div class="value">
                <input type="text" value="0" name="level" style="display: none" >
              </div>
              <div  class="col-xs-12 col-md-3" >
                <a class="btn  btn-secondary  view"   >indent</a>
              </div>
              <div  class="col-xs-12 col-md-2">
                <a class="btn  btn-secondary view"   >outdent</a>
              </div>
            </div>
            <c:forEach items="${hcsaSvcSubtypeOrSubsumed.list}" var="hcsaSvcSubtypeOrSubsumed2">
              <div class="view">
                <div class="col-xs-12 col-md-7" style="padding-right: 20%;" >
                  <input class="add" type="text"  style="margin-left:60px" maxlength="100" readonly name="subType" value="${hcsaSvcSubtypeOrSubsumed2.name}">
                </div>
                <div class="value">
                  <input type="text" value="1" name="level" style="display: none" >
                </div>
                <div  class="col-xs-12 col-md-3" >
                  <a class="btn  btn-secondary  view"    >indent</a>
                </div>
                <div  class="col-xs-12 col-md-2">
                  <a class="btn  btn-secondary view"  >outdent</a>
                </div>
              </div>
              <c:forEach items="${hcsaSvcSubtypeOrSubsumed2.list}" var="hcsaSvcSubtypeOrSubsumed3">
                <div class="view">
                  <div class="col-xs-12 col-md-7" style="padding-right: 20%;" >
                    <input class="add" type="text"  style="margin-left:120px" maxlength="100" readonly name="subType" value="${hcsaSvcSubtypeOrSubsumed3.name}">
                  </div>
                  <div class="value">
                    <input type="text" value="2" name="level" style="display: none" >
                  </div>
                  <div  class="col-xs-12 col-md-3" >
                    <a class="btn  btn-secondary  view"    >indent</a>
                  </div>
                  <div  class="col-xs-12 col-md-2">
                    <a class="btn  btn-secondary view"   >outdent</a>
                  </div>
                </div>
              </c:forEach>
            </c:forEach>
          </c:forEach>

          <div class="col-xs-12 col-md-6">
            <a  class="btn  btn-secondary "   style="margin-right: 10px" id="addAsItem" > + </a><label for="addAsItem"> Add as Item</label>
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
            <input type="text" disabled autocomplete="off" value="<fmt:formatDate value="${hcsaServiceDto.endDate}" pattern="dd/MM/yyyy"/>" class="date_picker form-control form_datetime" name="EndDate" id="-20247433206800" data-date-start-date="01/01/1900" placeholder="dd/mm/yyyy" maxlength="10"><span id="error_EndDate" name="iaisErrorMsg" class="error-msg"></span>
          </div>
          <div class="clear"></div></div>
      </div>


      <div class="col-xs-12 col-md-8" style="left: 10%">
        <div class="row">
          <div class="col-xs-10 col-md-8">
            <div class="components">
              <button class="btn  btn-secondary" value="<iais:mask name="crud_action_value"  value="${hcsaServiceDto.id}"/>" onclick="edit(this)">Update</button>
            </div>
          </div>
          <div class="col-xs-10 col-md-3">
            <div class="components">

              <a class="btn  btn-primary " onclick="save()">Save</a>

            </div>
          </div>
        </div>
      </div>
      <div class="col-xs-12 col-md-12" style="text-align: center">
        <div class="row">
          <div class="col-xs-10 col-md-8">
            <div class="components">
              <p style="text-align: center">Version ${hcsaServiceDto.version}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </form>
</div>
<iais:confirm msg="Are you sure you want to leave this page!" callBack="kpi()" popupOrder="kpi" ></iais:confirm>

<iais:confirm msg="Are you sure you want to leave this page!" callBack="checklists()" popupOrder="checklists" ></iais:confirm>

<iais:confirm msg="Are you sure you want to leave this page!" callBack="riskScore()" popupOrder="riskScore" ></iais:confirm>
<style>
  .mandatory{
    color: red;
  }
  .view{
    color: #2199E8;
  }

  .marg-1{
    margin-top: 1%;
  }
  .width-center{
    text-align: center;width: 100%
  }
  .width-70{
    width: 70%;
  }

</style>
<script type="text/javascript">
    function kpi() {

        location.href="https://egp.sit.intra.iais.com/hcsa-licence-web/eservice/INTRANET/MohKPIAndReminder";
    }


    function  checklists(){

        location.href="https://egp.sit.intra.iais.com/hcsa-licence-web/eservice/INTRANET/MohChecklistConfiguration";
    }

    function riskScore(){
        location.href="https://egp.sit.intra.iais.com/hcsa-licence-web/eservice/INTRANET/MohRiskConigMenu";
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

    function edit(obj) {

        SOP.Crud.cfxSubmit("mainForm","edit",$(obj).val(),"");
    }

  function save() {
      SOP.Crud.cfxSubmit("mainForm","save","update","");
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



</script>
</>
