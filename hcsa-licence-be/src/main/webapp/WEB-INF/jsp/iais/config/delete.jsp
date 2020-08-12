<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.RedirectUtil" %>
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
        <h2> HCSA Service</h2>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-10">
          <h2 class="component-title">Delete HCSA Service</h2>
        </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="serviceName">Service Name<span class="mandatory" >*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="serviceName" type="text" maxlength="100"  value="${hcsaServiceDto.svcName}" name="serviceName" disabled>
          </div>
        </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="description">Service Description<span class="mandatory" >*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="description" type="text" maxlength="255" value="${hcsaServiceDto.svcDesc}" disabled>
          </div>
        </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="displayDescription">Service Display Description<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="displayDescription" type="text" maxlength="255" value="${hcsaServiceDto.svcDisplayDesc}" disabled>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="serviceCode">Service Code<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="serviceCode" type="text" maxlength="3"  value="${hcsaServiceDto.svcCode}" disabled>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="ServiceType">Service Type<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <select id="ServiceType" name="ServiceType"  disabled>

              <option>Select one</option>
              <option selected="selected" value="SVTP001">Base</option>
              <option value="SVTP002">Subsumed</option>
              <option value="SVTP003">Specified</option>
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
              <div class="form-check " style="left: 10%;">
                <c:set var="type" value="${PremisesType}"></c:set>
                <input class="form-check-input" disabled="disabled" name="PremisesType"<c:if test="${fn:contains(type,'ONSITE')}">checked="checked"</c:if> id="icon3checkboxSample" type="checkbox" name="Onsite" value="ONSITE"  aria-invalid="false">
                <label class="form-check-label" for="icon3checkboxSample"><span class="check-square"></span>Onsite</label>
              </div>
            </div>
            <div class="col-xs-12 col-md-3">
              <div class="form-check ">
                <input class="form-check-input" disabled="disabled" name="PremisesType"<c:if test="${fn:contains(type,'OFFSITE')}">checked="checked"</c:if> id="icon4checkboxSample" type="checkbox" name="Offsite"  value="OFFSITE" aria-invalid="false">
                <label class="form-check-label" for="icon4checkboxSample"><span class="check-square"></span>Offsite</label>
              </div>
            </div>
            <div class="col-xs-12 col-md-3">
              <div class="form-check ">
                <input class="form-check-input" disabled="disabled" name="PremisesType"<c:if test="${fn:contains(type,'CONVEYANCE')}">checked="checked"</c:if> id="icon5checkboxSample" type="checkbox" value="CONVEYANCE" name="Conveyance" aria-invalid="false">
                <label class="form-check-label" for="icon5checkboxSample"><span class="check-square"></span>Conveyance</label>
              </div>
            </div>
          </div>
        </div>
      </div>


      <div class="form-group" style="display: none" id="Subsumption">
        <div class="col-xs-12 col-md-8"  style="margin-bottom: 10px">
          <label class="col-xs-12 col-md-6 control-label" >Base Service Subsumed Under<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <iais:multipleSelect name="Subsumption" selectValue="${selectSubsumption}" options="selsectBaseHcsaServiceDto"></iais:multipleSelect>
          </div>
        </div>
      </div>


      <div class="form-group" style="display: none" id="Pre-requisite">
        <div class="col-xs-12 col-md-8" style="margin-bottom: 10px">
          <label class="col-xs-12 col-md-6 control-label" >Pre-requisite Base Service:<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <iais:multipleSelect name="Pre-requisite" selectValue="${selectPreRequisite}" options="selsectBaseHcsaServiceDto"></iais:multipleSelect>
          </div>
        </div>
      </div>


      <div class="form-group" >
        <div class="col-xs-12 col-md-8" >
          <label class="col-xs-12 col-md-6 control-label" >Principal Officer (PO)<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-2" >
            <input type="text"  disabled name="man-principalOfficer" maxlength="2" value="${PO.mandatoryCount}" placeholder="mandatory count">
            <input type="text" disabled name="poId" style="display: none" maxlength="2" value="${PO.id}">
          </div>
          <div class="col-xs-12 col-md-2" >
            <input type="text" disabled name="mix-principalOfficer" maxlength="2" value="${PO.maximumCount}" placeholder="maximum count">
          </div>
        </div>
      </div>



      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" >Deputy Principal Officer (DPO)<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-2">
            <input  value="${DPO.id}" name="dpoId" style="display:none;" type="text">
            <input  readonly type="text" name="man-DeputyPrincipalOfficer" maxlength="2" value="${DPO.mandatoryCount}" placeholder="mandatory count">
          </div>
          <div class="col-xs-12 col-md-2">
            <input  type="text" disabled name="mix-DeputyPrincipalOfficer" maxlength="2" value="${DPO.maximumCount}"  placeholder="maximum count">
          </div>
        </div>
      </div>


      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" >Clinical Governance Officer (CGO)<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-2">
            <input value="${CGO.id}" name="cgoId" style="display:none;" type="text">
            <input readonly  type="text" name="man-ClinicalGovernanceOfficer" maxlength="2" value="${CGO.mandatoryCount}" placeholder="mandatory count">
          </div>
          <div class="col-xs-12 col-md-2">
            <input disabled type="text" name="mix-ClinicalGovernanceOfficer" maxlength="2" value="${CGO.maximumCount}"  placeholder="maximum count">
          </div>
        </div>
      </div>


      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" >Service Personnel<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-2">
            <input value="${SVCPSN.id}" name="svcpsnId" style="display:none;" maxlength="2" type="text">
            <input  readonly type="text" name="man-ServicePersonnel" maxlength="2" value="${SVCPSN.mandatoryCount}" placeholder="mandatory count">
          </div>
          <div class="col-xs-12 col-md-2">
            <input  type="text" name="mix-ServicePersonnel" maxlength="2" disabled value="${SVCPSN.maximumCount}"  placeholder="maximum count">
          </div>
        </div>
      </div>


      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" for="NumberDocument">Number of Service-Related Document to be
            uploaded<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="NumberDocument"  readonly  type="text" maxlength="2" value="${serviceDocSize}">
          </div>
        </div>
      </div>


      <div class="serviceNumberfields">
        <c:forEach items="${serviceDoc}" var="doc">
          <div class="form-group">
            <div class="col-xs-12 col-md-8">
              <label class="col-xs-12 col-md-6 control-label">Name of Info Field</label>
              <input type="hidden" value="${doc.id}" name="serviceDocId">
              <div class="col-xs-12 col-md-4">
                <input  type="text" name="descriptionServiceDoc" disabled maxlength="255" value="${doc.docDesc}">
              </div>
              <div class="col-xs-12 col-md-2 form-check" style="margin-top: 1%">
                <input type="hidden" name="serviceDocMandatory"<c:choose><c:when test="${doc.isMandatory}"> value="1"</c:when><c:otherwise> value="0"</c:otherwise></c:choose>>
                <input class="form-check-input" disabled <c:if test="${doc.isMandatory}">checked</c:if>  type="checkbox" onclick="serviceCheckboxOnclick(this)" name="descriptionServiceDocMandatory">
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
            <input readonly id="Numberfields" maxlength="2" type="text" value="${comDocSize}">
          </div>
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
        <div class="col-xs-12 col-md-8 marg-1">
          <label class="col-xs-12 col-md-8 control-label" >Service-Related Checklists</label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary " data-toggle="modal" data-target= "#checklists" style="padding: 12px 60px"><span class="view">view</span></a>
            </div>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8 marg-1">
          <label class="col-xs-12 col-md-8 control-label" >Service Risk Score</label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary "  data-toggle="modal" data-target= "#riskScore" style="padding: 12px 60px"><span class="view">view</span></a>
            </div>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8 marg-1">
          <label class="col-xs-12 col-md-8 control-label" >Service KPI</label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary " data-toggle="modal" data-target= "#kpi" style="padding: 12px 60px"><span class="view">view</span></a>
            </div>
          </div>
        </div>
      </div>

     <%-- <div class="form-group">
        <div class="col-xs-12 col-md-8 marg-1">
          <label class="col-xs-12 col-md-8 control-label" >Service Fees</label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary " style="padding: 12px 60px"><span class="view">view</span></a>
            </div>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8 marg-1">
          <label class="col-xs-12 col-md-8 control-label" >Service Fee Bundles</label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary " style="padding: 12px 60px"><span class="view">view</span></a>
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
            <div class="components width-center">
              <a class="btn btn-secondary width-70" onclick="showCESSATION()"><span class="view">CESSATION</span></a>
            </div>
          </div>
          <%-- <div class="col-xs-10 col-md-6">
             <div class="components width-center">
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
          <%-- <div class="col-xs-10 col-md-6">
             <div class="components width-center">
               <a class="btn btn-secondary width-70" onclick="showREVOCATION()"><span class="view">REVOCATION</span></a>
             </div>
           </div>--%>
        </div>
      </div>

      <c:forEach items="${routingStagess}" var="routingStages" varStatus="sta">

        <div class="form-group" style="display: none" id="${routingStages.key}" >
          <div class="col-xs-12 col-md-12"  style="margin-top: 10px">
            <table border="1px" style="text-align: center" >
              <tr>
                <th style="width: 10% ;height: 40px;text-align: center">Application Type<span class="mandatory" >*</span></th>
                <th  style="width: 20% ;height: 40px;text-align: center">Service Workflow Routing Stages<span class="mandatory" >*</span></th>
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

                    <div class="col-xs-12 col-md-6"  style="margin-top: 1%;margin-bottom: 1%" >
                      <input type="text" name="stageId${routingStage.stageCode}${routingStages.key}" value="${routingStage.routingSchemeId}" style="display:none;">

                      <select  disabled name="RoutingScheme${routingStage.stageCode}${routingStages.key}"   >
                        <option value="">Please Select</option>
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
                    </div>


                  </td>
                  <td>
                    <div class="col-xs-12 col-md-12">
                      <input style="margin: 8px 0px 8px" disabled="disabled" type="text" maxlength="2" name="WorkloadManhours${routingStage.stageCode}${routingStages.key}" value="${routingStage.manhours}" >
                      <span class="error-msg" name="iaisErrorMsg" id="error_manhourCount${status.index}"></span>
                    </div>

                  </td>
                  <td>
                    <div class="col-xs-12 col-md-12">
                      <input name="workstageId${routingStage.stageCode}${routingStages.key}" type="text" style="display: none" value="${routingStage.workStageId}">
                      <select disabled="disabled" name="workingGroup${routingStage.stageCode}${routingStages.key}">
                        <option value="">Please Select</option>
                        <c:forEach items="${routingStage.workingGroup}" var="workingGroup">
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
            <a  class="btn  btn-secondary "   style="margin-right: 10px" id="addAsItem" > + </a><label for="addAsItem"> Add Item</label>
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


      <div class="col-xs-12 col-md-12" style="text-align: center">
        <div class="row">
          <div class="col-xs-10 col-md-5">
            <div class="components">
              <a class="btn  btn-secondary"data-toggle="modal" data-target= "#cancel">Cancel</a>
            </div>
          </div>
          <div class="col-xs-10 col-md-5">
            <div class="components">
              <button class="btn  btn-secondary" id="deleteConfirm" value="${hcsaServiceDto.id}"  onclick="confirmDelete(this)" ><span class="mandatory">CONFIRM DELETE</span></button>
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
<iais:confirm msg="Are you sure you want to leave this page ?" callBack="kpi()" popupOrder="kpi" ></iais:confirm>

<iais:confirm msg="Are you sure you want to leave this page ?" callBack="checklists()" popupOrder="checklists" ></iais:confirm>

<iais:confirm msg="Are you sure you want to leave this page ?" callBack="riskScore()" popupOrder="riskScore" ></iais:confirm>
<iais:confirm msg="Are you sure you want to cancel?" yesBtnDesc="NO" cancelBtnDesc="YES" yesBtnCls="btn btn-secondary" cancelBtnCls="btn btn-primary" cancelFunc="cancel()" callBack="displays()" popupOrder="cancel"></iais:confirm>
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

  .width-center{
    text-align: center;width: 100%
  }
  .width-70{
    width: 70%;
  }

</style>
<script type="text/javascript">


    function kpi() {

        location.href='https://${pageContext.request.serverName}/${pageContext.request.contextPath}<%=RedirectUtil.appendCsrfGuardToken("/eservice/INTRANET/MohKPIAndReminder",request)%>';

    }


    function  checklists(){
        location.href='https://${pageContext.request.serverName}/${pageContext.request.contextPath}<%=RedirectUtil.appendCsrfGuardToken("/eservice/INTRANET/MohChecklistConfiguration",request)%>';
    }

    function riskScore(){
        location.href='https://${pageContext.request.serverName}/${pageContext.request.contextPath}<%=RedirectUtil.appendCsrfGuardToken("/eservice/INTRANET/MohRiskConigMenu",request)%>';

    }

    function confirmDelete(obj) {
        var r=confirm("Are you sure to delete!");
        if(r==true){
            SOP.Crud.cfxSubmit("mainForm","delete",$(obj).val(),"");
        }else {

            SOP.Crud.cfxSubmit("mainForm","delete","","");
        }

    }
    function displays() {
        $('#cancel').modal('hide');
    }
    function deleteConfirm() {

        SOP.Crud.cfxSubmit("mainForm","delete",$('#deleteConfirm').val(),"");


    }
    function cancel() {
        SOP.Crud.cfxSubmit("mainForm","delete","back","");
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

    /*function showSUSPENSION(){
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


</script>
</>
