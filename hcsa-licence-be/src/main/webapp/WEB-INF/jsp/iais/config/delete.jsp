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
        <div class="col-xs-12 col-md-9">
          <h2 class="col-xs-0 col-md-7 component-title">Delete HCSA Service</h2>
        </div>
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" for="serviceName">Service Name&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="serviceName" type="text" maxlength="100"  value="${hcsaServiceDto.svcName}" name="serviceName" disabled>
          </div>
        </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" for="description">Service Description&nbsp;<span class="mandatory" >*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="description" type="text" maxlength="255" value="${hcsaServiceDto.svcDesc}" disabled>
          </div>
        </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" for="displayDescription">Service Display Description&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="displayDescription" type="text" maxlength="255" value="${hcsaServiceDto.svcDisplayDesc}" disabled>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" for="serviceCode">Service Code&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="serviceCode" type="text" maxlength="3"  value="${hcsaServiceDto.svcCode}" disabled>
          </div>
        </div>
      </div>

      <div class="col-xs-12 col-md-9">
        <label class="col-xs-12 col-md-7 control-label" for="ServiceType">Service Type&nbsp;<span class="mandatory">*</span></label>
        <div class="col-xs-12 col-md-4" style="margin-bottom: 15px;">
          <select  id="ServiceType" disabled name="ServiceType">
            <option value="">Select one</option>
            <option value="SVTP001" <c:if test="${hcsaServiceDto.svcType=='SVTP001'}">selected="selected"</c:if>>Base</option>
            <option value="SVTP002" <c:if test="${hcsaServiceDto.svcType=='SVTP002'}">selected="selected"</c:if>>Subsumed</option>
            <option value="SVTP003" <c:if test="${hcsaServiceDto.svcType=='SVTP003'}">selected="selected"</c:if> >Specified</option>
          </select>
        </div>
      </div>


      <div class="form-group" id="selectCategoryId" >
        <div class="col-xs-12 col-md-9" style="margin-bottom: 20px;">
          <label class="col-xs-12 col-md-7 control-label">Service Category&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <select name="selectCategoryId" disabled >
              <option value="">Please Select</option>
              <c:forEach items="${categoryDtos}" var="categoryDto">
                <option value="${categoryDto.id}" <c:if test="${hcsaServiceDto.categoryId==categoryDto.id}">selected</c:if>>${categoryDto.name}</option>
              </c:forEach>
            </select>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-10">
          <label class="col-xs-12 col-md-10 control-label" style="margin-bottom: 20px;">Premises Type&nbsp;<span class="mandatory">*</span></label>
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
        <div class="col-xs-12 col-md-9"  style="margin-bottom: 10px">
          <label class="col-xs-12 col-md-7 control-label" >Base Service Subsumed Under&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <iais:multipleSelect name="Subsumption" selectValue="${selectSubsumption}" options="selsectBaseHcsaServiceDto"></iais:multipleSelect>
          </div>
        </div>
      </div>

      <div class="form-group" style="display: none" id="Pre-requisite">
        <div class="col-xs-12 col-md-9" style="margin-bottom: 10px">
          <label class="col-xs-12 col-md-7 control-label" >Pre-requisite Base Service&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <iais:multipleSelect name="Pre-requisite" selectValue="${selectPreRequisite}" options="selsectBaseHcsaServiceDto"></iais:multipleSelect>
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

      <div class="form-group" >
        <div class="col-xs-12 col-md-9" >
          <label class="col-xs-12 col-md-7 control-label" >Principal Officer (PO)&nbsp;<span class="mandatory">*</span></label>
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
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" >Deputy Principal Officer (DPO)&nbsp;<span class="mandatory">*</span></label>
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
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" >Clinical Governance Officer (CGO)&nbsp;<span class="mandatory">*</span></label>
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
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" >Service Personnel&nbsp;<span class="mandatory">*</span></label>
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
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" for="NumberDocument">Number of Service-Related Document to be
            uploaded&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="NumberDocument"  readonly  type="text" maxlength="2" value="${serviceDocSize}">
          </div>
        </div>
      </div>


      <div class="serviceNumberfields">
        <c:forEach items="${serviceDoc}" var="doc">
          <div class="form-group">
            <div class="col-xs-12 col-md-9">
              <label class="col-xs-12 col-md-7 control-label">Name of Info Field</label>
              <input type="hidden" value="${doc.id}" name="serviceDocId">
              <div class="col-xs-12 col-md-3">
                <input  type="text" name="descriptionServiceDoc" disabled maxlength="255" value="${doc.docTitle}">
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
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" for="Numberfields">Number of Service-Related General Info fields to be captured&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input readonly id="Numberfields" maxlength="2" type="text" value="${comDocSize}">
          </div>
        </div>
      </div>

      <div class="Numberfields">
        <c:forEach items="${comDoc}" var="doc">
          <div class="form-group">
            <div class="col-xs-12 col-md-9">
              <label class="col-xs-12 col-md-7 control-label">Name of Info Field</label>
              <input type="hidden" value="${doc.id}" name="commDocId">
              <div class="col-xs-12 col-md-3">
                <input  type="text" name="descriptionCommDoc" disabled maxlength="255" value="${doc.docTitle}">
              </div>
              <div class="col-xs-12 col-md-2 form-check" style="margin-top: 1%">
                <input type="hidden" name="commDocMandatory"<c:choose><c:when test="${doc.isMandatory}"> value="1"</c:when><c:otherwise> value="0"</c:otherwise></c:choose>>
                <input class="form-check-input" disabled <c:if test="${doc.isMandatory}">checked</c:if>  type="checkbox" onclick="checkboxOnclick(this)" name="descriptionCommDocMandatory">
                <label class="form-check-label" ><span class="check-square"></span>Mandatory</label>
              </div>
            </div>
          </div>
        </c:forEach>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label">Will the doc be duplicated for individual premises?</label>
          <div class="col-xs-12 col-md-2">
            <input type="radio" class="form-check-input premTypeRadio" name="individualPremises" checked value="0" disabled>&nbsp;&nbsp;<span style="font-size: 16px">No</span>
          </div>
          <div class="col-xs-12 col-md-2">
            <input type="radio" class="form-check-input premTypeRadio" name="individualPremises"  value="1" disabled>&nbsp;&nbsp;<span style="font-size: 16px">Yes</span>
          </div>
        </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-9 marg-1">
          <label class="col-xs-12 col-md-7 control-label" >Service-Related Checklists</label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary " data-toggle="modal" data-target= "#checklists" style="padding: 12px 60px"><span class="view">view</span></a>
            </div>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-9 marg-1">
          <label class="col-xs-12 col-md-7 control-label" >Service Risk Score</label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary" data-toggle="modal" data-target= "#riskScore" style="padding: 12px 60px"><span class="view">view</span></a>
            </div>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-9 marg-1">
          <label class="col-xs-12 col-md-7 control-label" >Service KPI</label>
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
          <div class="col-xs-10 col-md-6">
            <div class="components width-center">
              <a class="btn btn-secondary width-70" onclick="showWITHDRAWAL()"><span class="view">WITHDRAWAL</span></a>
            </div>
          </div>
        </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-12" style="margin-top: 1%">

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
                <th style="width: 10% ;height: 40px;text-align: center">Application Type&nbsp;<span class="mandatory" >*</span></th>
                <th  style="width: 20% ;height: 40px;text-align: center">Service Workflow Routing Stages&nbsp;<span class="mandatory" >*</span></th>
                <th  style="width: 30% ;height: 40px;text-align: center">Service Routing Scheme&nbsp;<span class="mandatory">*</span></th>
                <th  style="width: 15% ;height: 40px;text-align: center">Service Workload Manhours&nbsp;<span class="mandatory">*</span></th>
              </tr>
              <c:forEach items="${routingStages.value}" var="routingStage" varStatus="status">
                <tr>
                  <td >${routingStage.appTypeName}</td>
                  <td >${routingStage.stageName}</td>
                  <td>
                    <div class="col-xs-12 col-md-6"  style="margin-top: 1%;margin-bottom: 1%;text-align:left">
                      <select disabled name="isMandatory${routingStage.stageCode}${routingStages.key}">
                        <option value="">Please Select</option>
                        <option value="mandatory" <c:if test="${routingStage.isMandatory=='true'}">selected="selected"</c:if> >Mandatory</option>
                        <option value="optional" <c:if test="${routingStage.isMandatory=='false'}">selected="selected"</c:if>>Optional</option>
                      </select>
                      <br>
                      <c:if test="${routingStage.stageCode=='AO1'|| routingStage.stageCode=='AO2'}">
                        <input type="hidden" value="${routingStage.canApprove}" name="canApprove${routingStage.stageCode}${routingStages.key}">
                        <input type="checkbox" disabled <c:if test="${routingStage.canApprove=='1'}">checked</c:if> onclick="canApprove(this)" /><span>&nbsp;Can Approve ?</span>
                      </c:if>
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
              <input type="text" disabled maxlength="100" value="${pageName}" name="pageName">
            </div>
          </div>
        </div>
        <div class="col-xs-12 col-md-12 Sub-Types" style="margin-top: 20px ;margin-bottom: 20px">
          <c:forEach items="${hcsaSvcSubtypeOrSubsumedDto}" var="hcsaSvcSubtypeOrSubsumed">
            <div class="view  col-xs-12 col-md-12">
              <div class="col-xs-12 col-md-4" style="padding-right: 20%;" >
                <input class="add" type="text"  style="margin-left:0px" maxlength="100" readonly name="subType" value="${hcsaSvcSubtypeOrSubsumed.name}">
              </div>
              <div class="value">
                <input type="text" value="0" name="level" style="display: none" >
              </div>
              <div  class="col-xs-12 col-md-2" >
                <a class="btn  btn-secondary  view"   >indent</a>
              </div>
              <div  class="col-xs-12 col-md-2">
                <a class="btn  btn-secondary view"   >outdent</a>
              </div>
              <div class="col-xs-12 col-md-2 up">
                <a class="btn  btn-secondary up view" onclick="up(this)">UP</a>
                <a class="btn  btn-secondary down view" onclick="down(this)">DOWN</a>
              </div>
              <div class="col-xs-12 col-md-2">
                <a class="btn  btn-secondary view"  onclick="removeThis(this)" >-</a>
              </div>
            </div>
            <c:forEach items="${hcsaSvcSubtypeOrSubsumed.list}" var="hcsaSvcSubtypeOrSubsumed2">
              <div class="view  col-xs-12 col-md-12">
                <div class="col-xs-12 col-md-4" style="padding-right: 20%;" >
                  <input class="add" type="text"  style="margin-left:60px" maxlength="100" readonly name="subType" value="${hcsaSvcSubtypeOrSubsumed2.name}">
                </div>
                <div class="value">
                  <input type="text" value="1" name="level" style="display: none" >
                </div>
                <div  class="col-xs-12 col-md-2" >
                  <a class="btn  btn-secondary  view"    >indent</a>
                </div>
                <div  class="col-xs-12 col-md-2">
                  <a class="btn  btn-secondary view"  >outdent</a>
                </div>
                <div class="col-xs-12 col-md-2 up">
                  <a class="btn  btn-secondary up view" onclick="up(this)">UP</a>
                  <a class="btn  btn-secondary down view" onclick="down(this)">DOWN</a>
                </div>
                <div class="col-xs-12 col-md-2">
                  <a class="btn  btn-secondary view"  onclick="removeThis(this)" >-</a>
                </div>
              </div>
              <c:forEach items="${hcsaSvcSubtypeOrSubsumed2.list}" var="hcsaSvcSubtypeOrSubsumed3">
                <div class="view  col-xs-12 col-md-12">
                  <div class="col-xs-12 col-md-4" style="padding-right: 20%;" >
                    <input class="add" type="text"  style="margin-left:120px" maxlength="100" readonly name="subType" value="${hcsaSvcSubtypeOrSubsumed3.name}">
                  </div>
                  <div class="value">
                    <input type="text" value="2" name="level" style="display: none" >
                  </div>
                  <div  class="col-xs-12 col-md-2" >
                    <a class="btn  btn-secondary  view"    >indent</a>
                  </div>
                  <div  class="col-xs-12 col-md-2">
                    <a class="btn  btn-secondary view"   >outdent</a>
                  </div>
                  <div class="col-xs-12 col-md-2 up">
                    <a class="btn  btn-secondary up view" onclick="up(this)">UP</a>
                    <a class="btn  btn-secondary down view" onclick="down(this)">DOWN</a>
                  </div>
                  <div class="col-xs-12 col-md-2">
                    <a class="btn  btn-secondary view"  onclick="removeThis(this)" >-</a>
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



      <div class="col-xs-12 col-md-9">
        <div class="form-group">
          <label class="col-xs-12 col-md-7 control-label">Effective Start Date&nbsp;<span class="mandatory">*</span></label>
          <div class=" col-xs-7 col-sm-4 col-md-3">
            <input type="text" disabled value="${hcsaServiceDto.effectiveDate}" autocomplete="off" class="date_picker form-control form_datetime" name="StartDate" id="-20189532301300" data-date-start-date="01/01/1900" placeholder="dd/mm/yyyy" maxlength="10"><span id="error_StartDate" name="iaisErrorMsg" class="error-msg"></span>
          </div>
          <div class="clear"></div></div>
      </div>

      <div class="col-xs-12 col-md-9" style="margin-bottom: 50px;">
        <div class="form-group">
          <label class="col-xs-12 col-md-7 control-label">Effective End Date</label>
          <div class=" col-xs-7 col-sm-4 col-md-3">
            <input type="text" disabled autocomplete="off" value="<fmt:formatDate value="${hcsaServiceDto.endDate}" pattern="dd/MM/yyyy"/>" class="date_picker form-control form_datetime" name="EndDate" id="-20247433206800" data-date-start-date="01/01/1900" placeholder="dd/mm/yyyy" maxlength="10"><span id="error_EndDate" name="iaisErrorMsg" class="error-msg"></span>
          </div>
          <div class="clear"></div></div>
      </div>

      <div class="col-lg-12 col-xs-12">
        <iais:action style="text-align:center;">
          <a class="btn btn-secondary"data-toggle="modal" data-target= "#cancel">Cancel</a>
          <button class="btn btn-secondary" id="deleteConfirm" value="${hcsaServiceDto.id}" onclick="confirmDelete(this)" ><span class="mandatory">CONFIRM DELETE</span></button>
        </iais:action>
        <div class="bg-title" style="text-align: center">
          <p style="text-align: center">Version ${hcsaServiceDto.version}</p>
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

    $(document).ready(function () {
        let val = $("select[name='ServiceType']").val();
        if("SVTP001"==val){
            $('#selectCategoryId').attr("style","display:block");
        } else if("SVTP002"==val){
            $('#Subsumption').attr("style","display:block");
            $('#Pre-requisite').attr("style","display:none");
            $('#selectCategoryId').attr("style","display:none");
        }else  if("SVTP003"==val){
            $('#Pre-requisite').attr("style","display:block");
            $('#Subsumption').attr("style","display:none");
            $('#selectCategoryId').attr("style","display:block");
        }else {
            $('#Subsumption').attr("style","display:none");
            $('#Pre-requisite').attr("style","display:none");
            $('#selectCategoryId').attr("style","display:none");
        }
        a();
    });

    var  a = function upDown() {
        let length = $('#addAsItem').closest("div").closest("div.Sub-Types").children("div.view").length;
        if (length == 1) {
            $('#addAsItem').closest("div").closest("div.Sub-Types").children("div.view").children('.up').attr("style","display: none");
        } else {
            $('#addAsItem').closest("div").closest("div.Sub-Types").children("div.view").children('.up').removeAttr("style");
        }
    }
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
        $('#APTY010').attr("style","display: none");
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
        $('#APTY010').attr("style","display: none");
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
        $('#APTY010').attr("style","display: none");
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
        $('#APTY010').attr("style","display: none");
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
        $('#APTY010').attr("style","display: none");
        $('#APTY005').attr("style","display: none");
        $('#APTY001').attr("style","display: none");
        if(jQuery=='display: block'){
            $('#APTY008').attr("style","display: none");
        }else if(jQuery=='display: none'){
            $('#APTY008').attr("style","display: block");
        }
    }

    /*function showSUSPENSION(){
        let jQuery = $('#APTY010').attr("style");
        $('#APTY002').attr("style","display: none");
        $('#APTY004').attr("style","display: none");
        $('#APTY006').attr("style","display: none");
        $('#APTY008').attr("style","display: none");
        $('#APTY005').attr("style","display: none");
        $('#APTY001').attr("style","display: none");
        if(jQuery=='display: block'){
            $('#APTY010').attr("style","display: none");
        }else if(jQuery=='display: none'){
            $('#APTY010').attr("style","display: block");
        }
    }
*/
    function  showWITHDRAWAL(){
        let jQuery = $('#APTY006').attr("style");
        $('#APTY002').attr("style","display: none");
        $('#APTY004').attr("style","display: none");
        $('#APTY010').attr("style","display: none");
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
