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
        <h2>HCSA Configurator Module</h2>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-9">
          <h2 class="col-xs-0 col-md-7 component-title">Preview HCSA Service Edit</h2>
        </div>
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-0 col-md-7 control-label" for="serviceName">Service Name&nbsp;<span class="mandatory" >*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="serviceName" name="serviceName" maxlength="100" type="text" readonly value="${hcsaServiceDto.svcName}">
          </div>
        </div>

        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" for="description">Service Description&nbsp;<span class="mandatory" >*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="description" name="description" maxlength="255" readonly type="text" value="${hcsaServiceDto.svcDesc}">
          </div>
        </div>

        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" for="displayDescription">Service Display Description&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="displayDescription" name="displayDescription" maxlength="255" readonly type="text" value="${hcsaServiceDto.svcDisplayDesc}">
          </div>
        </div>

        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" for="serviceCode">Service Code&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="serviceCode" type="text" name="serviceCode" maxlength="3" readonly value="${hcsaServiceDto.svcCode}">
          </div>
        </div>

        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" for="ServiceType">Service Type&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4" style="margin-bottom: 15px;">
            <select  id="ServiceType" disabled name="ServiceType">
              <option value="">Select one</option>
              <c:forEach var="codeSelectOption" items="${codeSelectOptionList}">
                <option value="${codeSelectOption.value}" <c:if test="${hcsaServiceDto.svcType==codeSelectOption.value}">selected="selected"</c:if>>${codeSelectOption.text}</option>
              </c:forEach>
            </select>
          </div>
        </div>

        <div class="form-group" id="selectCategoryId" >
          <div class="col-xs-12 col-md-9" style="margin-bottom: 20px;">
            <label class="col-xs-12 col-md-7 control-label">Service Category&nbsp;<span class="mandatory">*</span></label>
            <div class="col-xs-12 col-md-4">
              <select name="selectCategoryId" disabled>
                <option value="">Please Select</option>
                <c:forEach items="${categoryDtos}" var="categoryDto">
                  <option  <c:if test="${hcsaServiceDto.categoryId==categoryDto.id}">selected</c:if>>${categoryDto.name}</option>
                </c:forEach>
              </select>
            </div>
          </div>
        </div>

        <div class="col-xs-12 col-md-10">
          <label class="col-xs-12 col-md-10 control-label" style="margin-bottom: 20px;">Mode of Service Delivery&nbsp;<span class="mandatory">*</span></label>
        </div>

        <div class="form-check-gp">
          <div class="row">
            <c:choose>
              <c:when test="${hcsaServiceDto.svcCode=='EAS' || hcsaServiceDto.svcCode=='MTS'}">
                <div class="col-xs-12 col-md-3">
                  <div class="form-check " style="left: 10%">
                    <c:set var="type" value="${PremisesType}"></c:set>
                    <input class="form-check-input" disabled  id="icon6checkboxSample" <c:if test="${fn:contains(type,'EASMTS')}">checked="checked"</c:if> type="checkbox" name="Onsite" aria-invalid="false">
                    <label class="form-check-label"  for="icon6checkboxSample"><span class="check-square"></span> Conveyance (in a mobile clinic / ambulance)</label>
                  </div>
                </div>
              </c:when>
              <c:otherwise>
                <div class="col-xs-12 col-md-3">
                  <div class="form-check " style="left: 10%">
                    <c:set var="type" value="${PremisesType}"></c:set>
                    <input class="form-check-input" disabled  id="icon3checkboxSample" <c:if test="${fn:contains(type,'ONSITE')}">checked="checked"</c:if> type="checkbox" name="Onsite" aria-invalid="false">
                    <label class="form-check-label"  for="icon3checkboxSample"><span class="check-square"></span>Premises</label>
                  </div>
                </div>
                <div class="col-xs-12 col-md-3">
                  <div class="form-check ">
                    <input class="form-check-input" disabled id="icon4checkboxSample"  <c:if test="${fn:contains(type,'OFFSITE')}">checked="checked"</c:if> type="checkbox" name="Offsite" aria-invalid="false">
                    <label class="form-check-label"  for="icon4checkboxSample"><span class="check-square"></span>Off-site</label>
                  </div>
                </div>
                <div class="col-xs-12 col-md-3">
                  <div class="form-check ">
                    <input class="form-check-input" disabled id="icon5checkboxSample"  <c:if test="${fn:contains(type,'CONVEYANCE')}">checked="checked"</c:if> type="checkbox" name="Conveyance" aria-invalid="false">
                    <label class="form-check-label" for="icon5checkboxSample"><span class="check-square"></span>Conveyance </label>
                  </div>
                </div>
              </c:otherwise>
            </c:choose>
          </div>
        </div>
      </div>

      <div class="form-group" style="display: none" id="Subsumption">
        <div class="col-xs-12 col-md-9"  style="margin-bottom: 10px">
          <label class="col-xs-12 col-md-7 control-label" ><iais:code code="CDN001"/> Subsumed Under&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <iais:multipleSelect name="Subsumption" selectValue="${selectSubsumption}" options="selsectBaseHcsaServiceDto" disabled="true"></iais:multipleSelect>
          </div>
        </div>
      </div>

      <div class="form-group" style="display: none" id="Pre-requisite">
        <div class="col-xs-12 col-md-9" style="margin-bottom: 10px">
          <label class="col-xs-12 col-md-7 control-label" >Pre-requisite <iais:code code="CDN001"/>&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <iais:multipleSelect name="Pre-requisite" selectValue="${selectPreRequisite}" options="selsectBaseHcsaServiceDto" disabled="true"></iais:multipleSelect>
          </div>
        </div>
      </div>

      <%@include file="common/persontTitle.jsp"%>

     <%@include file="common/deleteOrView/person.jsp"%>

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
            <div class="col-xs-12 col-md-12">
              <label class="col-xs-12 col-md-5 control-label" style="margin-right: 2%">Name of Info Field</label>
              <input type="hidden" value="${doc.id}" name="serviceDocId">
              <div class="col-xs-12 col-md-2">
                <input  type="text" name="descriptionServiceDoc" disabled maxlength="255" value="${doc.docTitle}">
              </div>
              <div class="col-xs-12 col-md-2 form-check" style="margin-top: 1%">
                <input type="hidden" name="serviceDocMandatory"<c:choose><c:when test="${doc.isMandatory}"> value="1"</c:when><c:otherwise> value="0"</c:otherwise></c:choose>>
                <input style="white-space: nowrap" class="form-check-input" disabled <c:if test="${doc.isMandatory}">checked</c:if>  type="checkbox" onclick="serviceCheckboxOnclick(this)" name="descriptionServiceDocMandatory">
                <label style="white-space: nowrap" class="form-check-label" ><span class="check-square"></span>Mandatory</label>
              </div>
              <div class="col-xs-12 col-md-2 form-check" style="margin-top: 1%">
                <input type="hidden" name="serviceDocPremises" <c:choose><c:when test="${doc.dupForPrem=='1'}">value="1"</c:when><c:otherwise>value="0"</c:otherwise></c:choose>>
                <input style="white-space: nowrap" class="form-check-input" disabled <c:if test="${doc.dupForPrem=='1'}">checked</c:if>  type="checkbox" onclick="serviceCheckboxOnclick(this)" name="descriptionServiceDocPremises">
                <label style="white-space: nowrap" class="form-check-label" ><span class="check-square"></span>To duplicate for individual mode of service delivery ?</label>
              </div>
              <div class="col-xs-12 col-md-3 form-check" style="margin-top: 1%">
                <select  disabled>
                  <option value="">To duplicate for the personnel?</option>
                  <option <c:if test="${doc.dupForPerson=='PO'}">selected</c:if> value="PO">Principal Officer (PO)</option>
                  <option <c:if test="${doc.dupForPerson=='DPO'}">selected</c:if> value="DPO">Nominee</option>
                  <option <c:if test="${doc.dupForPerson=='CGO'}">selected</c:if> value="CGO">Clinical Governance Officer (CGO)</option>
                  <option <c:if test="${doc.dupForPerson=='SVCPSN'}">selected</c:if> value="SVCPSN">Service Personnel</option>
                  <option <c:if test="${doc.dupForPerson=='MAP'}">selected</c:if> value="MAP">MedAlert Person </option>
                  <c:if test="${hcsaServiceDto.svcCode=='EAS' || hcsaServiceDto.svcCode=='MTS'}"><option <c:if test="${doc.dupForPerson=='CD'}">selected</c:if> value="CD">Clinical Director</option></c:if>
                </select>
              </div>
            </div>
          </div>
        </c:forEach>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-9">
          <label class="col-xs-12 col-md-7 control-label" for="Numberfields">Number of Service-Related General Info fields to
            be captured&nbsp;<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="Numberfields" readonly type="text" maxlength="2" value="${comDocSize}">
          </div>
        </div>
      </div>

      <div class="Numberfields">
        <c:forEach items="${comDoc}" var="doc">
          <div class="form-group">
            <div class="col-xs-12 col-md-12">
              <label class="col-xs-12 col-md-5 control-label" style="margin-right: 2%">Name of Info Field</label>
              <input type="hidden" value="${doc.id}" name="commDocId">
              <div class="col-xs-12 col-md-2">
                <input  type="text" name="descriptionCommDoc" disabled maxlength="255" value="${doc.docTitle}">
              </div>
              <div class="col-xs-12 col-md-2 form-check" style="margin-top: 1%">
                <input type="hidden" name="commDocMandatory"<c:choose><c:when test="${doc.isMandatory}"> value="1"</c:when><c:otherwise> value="0"</c:otherwise></c:choose>>
                <input style="white-space: nowrap" class="form-check-input" disabled <c:if test="${doc.isMandatory}">checked</c:if>  type="checkbox" onclick="checkboxOnclick(this)" name="descriptionCommDocMandatory">
                <label style="white-space: nowrap" class="form-check-label" ><span class="check-square"></span>Mandatory</label>
              </div>
              <div class="col-xs-12 col-md-2 form-check" style="margin-top: 1%">
                <input type="hidden" name="commDocPremises" <c:choose><c:when test="${doc.dupForPrem=='1'}"> value="1"</c:when><c:otherwise> value="0"</c:otherwise></c:choose> >
                <input style="white-space: nowrap" class="form-check-input" disabled <c:if test="${doc.dupForPrem=='1'}">checked</c:if>   type="checkbox" onclick="checkboxOnclick(this)" name="descriptionCommDocPremises">
                <label style="white-space: nowrap" class="form-check-label" ><span class="check-square"></span>To duplicate for individual mode of service delivery ?</label>
              </div>
            </div>
          </div>
        </c:forEach>
      </div>


      <div class="form-group">
        <div class="col-xs-12 col-md-9 marg-1">
          <label class="col-xs-12 col-md-7 control-label" >Service-Related Checklists</label>
          <div class="col-xs-10 col-md-3">
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
              <a class="btn btn-secondary " data-toggle="modal" data-target= "#riskScore" style="padding: 12px 60px"><span class="view">view</span></a>
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

      <%--<div class="form-group">
        <div class="col-xs-12 col-md-8 marg-1">
          <label class="col-xs-12 col-md-8 control-label" >Service Fees</label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary "  style="padding: 12px 60px"><span class="view">view</span></a>
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
      </div>--%>

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

      <c:forEach items="${routingStagess}" var="routingStages">

      <div class="form-group" style="display: none" id="${routingStages.key}">
        <div class="col-xs-12 col-md-12"  style="margin-top: 10px" >
          <table aria-describedby="" border="1px" style="text-align: center" valign="middle">
            <tr>
              <th scope="col" style="width: 10% ;height: 40px;text-align: center"> Application Type&nbsp;<span class="mandatory" >*</span></th>
              <th scope="col" style="width: 20% ;height: 40px;text-align: center"> Service Workflow Routing Stages&nbsp;<span class="mandatory" >*</span></th>
              <th scope="col" style="width: 30% ;height: 40px;text-align: center">Service Routing Scheme&nbsp;<span class="mandatory">*</span></th>
              <th scope="col" style="width: 15% ;height: 40px;text-align: center">Service Workload Manhours&nbsp;<span class="mandatory">*</span></th>
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

                  <div class="col-xs-12 col-md-6" style="margin-top: 1%;margin-bottom: 1%;text-align:left">
                    <select  disabled name="RoutingScheme${routingStage.stageCode}"  >
                      <option value="">Please Select</option>
                      <option value="common"
                              <c:if test="${routingStage.routingSchemeName=='common'}">selected="selected" </c:if>

                      >Common Pool</option>
                      <option value="round"
                              <c:if test="${routingStage.routingSchemeName=='round'}">selected="selected" </c:if>
                      >Round Robin</option>
                      <option value="assign" <c:if test="${routingStage.routingSchemeName=='assign'}">selected="selected" </c:if>
                      >Supervisor Assign</option>

                    </select>
                    <c:if test="${routingStage.stageCode=='INS'}">
                      <p>Inspector</p>
                    </c:if>
                    <c:if test="${routingStage.stageCode=='INS'}">
                      <c:forEach items="${routingStage.hcsaSvcSpeRoutingSchemeDtos}" var="hcsaSvcSpeRoutingSchemeDto">
                        <div style="margin-top: 1%">
                          <select  disabled name="RoutingScheme${routingStage.stageCode}${routingStages.key}${hcsaSvcSpeRoutingSchemeDto.insOder}"  >
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
                        </div>
                      </c:forEach>
                    </c:if>
                  </div>

                </td>

                <td>
                  <div class="col-xs-12 col-md-12">
                    <input style="margin: 0px 0px" disabled type="text" maxlength="2" name="WorkloadManhours${routingStage.stageCode}${routingStages.key}" value="${routingStage.manhours}" >
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
          <div class="col-xs-12 col-md-12">
            <c:forEach items="${hcsaSvcSubtypeOrSubsumedDto}" var="hcsaSvcSubtypeOrSubsumed">
              <div class="view col-xs-12 col-md-12">
                <div class="col-xs-12 col-md-4" style="padding-right: 20%;" >
                  <input class="add" type="text"  style="margin-left:0px" maxlength="100" name="subType" value="${hcsaSvcSubtypeOrSubsumed.name}">
                </div>
                <div class="value">
                  <input type="text" value="0" name="level" style="display: none" >
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
              <c:forEach items="${hcsaSvcSubtypeOrSubsumed.list}" var="hcsaSvcSubtypeOrSubsumed2">
                <div class="view col-xs-12 col-md-12">
                  <div class="col-xs-12 col-md-4" style="padding-right: 20%;" >
                    <input class="add" type="text"  style="margin-left:60px" maxlength="100" name="subType" value="${hcsaSvcSubtypeOrSubsumed2.name}">
                  </div>
                  <div class="value">
                    <input type="text" value="1" name="level" style="display: none" >
                  </div>
                  <div  class="col-xs-12 col-md-2" >
                    <a class="btn  btn-secondary  view" onclick="indents(this)"  >indent</a>
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
                <c:forEach items="${hcsaSvcSubtypeOrSubsumed2.list}" var="hcsaSvcSubtypeOrSubsumed3">
                  <div class="view col-xs-12 col-md-12">
                    <div class="col-xs-12 col-md-4" style="padding-right: 20%;" >
                      <input class="add" type="text" maxlength="100"  style="margin-left:120px" name="subType" value="${hcsaSvcSubtypeOrSubsumed3.name}">
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
          </div>
          <div class="col-xs-12 col-md-12">
            <a class="btn  btn-secondary " style="margin-right: 10px" id="addAsItem"> + </a><label for="addAsItem"> Add Item</label>
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
          <button class="btn btn-primary" value="<iais:mask name="crud_action_value"  value="${hcsaServiceDto.id}"/>" onclick="edit(this)">Update</button>
        </iais:action>
        <div class="bg-title" style="text-align: center">
          <p style="text-align: center">Version ${hcsaServiceDto.version}</p>
        </div>
      </div>
    </div>
  </form>
</div>
<%@ include file="configRepeatJs.jsp" %>
<iais:confirm msg="Are you sure you want to leave this page ?" callBack="kpi()" popupOrder="kpi" ></iais:confirm>

<iais:confirm msg="Are you sure you want to leave this page ?" callBack="checklists()" popupOrder="checklists" ></iais:confirm>

<iais:confirm msg="Are you sure you want to leave this page ?" callBack="riskScore()" popupOrder="riskScore" ></iais:confirm>
<iais:confirm msg="Are you sure you want to cancel?" yesBtnDesc="NO" cancelBtnDesc="YES" yesBtnCls="btn btn-secondary" cancelBtnCls="btn btn-primary" cancelFunc="cancel()" callBack="displays()" popupOrder="cancel"></iais:confirm>
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
    $(document).ready(function () {
        a();
    });
    function kpi() {
        location.href='https://${pageContext.request.serverName}/${pageContext.request.contextPath}<%=RedirectUtil.appendCsrfGuardToken("/eservice/INTRANET/MohKPIAndReminder",request)%>';
    }


    function  checklists(){
        location.href='https://${pageContext.request.serverName}/${pageContext.request.contextPath}<%=RedirectUtil.appendCsrfGuardToken("/eservice/INTRANET/MohChecklistConfiguration",request)%>';
    }

    function riskScore(){
        location.href='https://${pageContext.request.serverName}/${pageContext.request.contextPath}<%=RedirectUtil.appendCsrfGuardToken("/eservice/INTRANET/MohRiskConigMenu",request)%>';
    }

    $(document).ready(function(){
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
    });

    function displays() {
        $('#cancel').modal('hide');
    }
    function cancel() {
        SOP.Crud.cfxSubmit("mainForm","back","back","");
    }
    function edit(obj) {

        SOP.Crud.cfxSubmit("mainForm","edit",$(obj).val(),"");
    }

  function save() {
      SOP.Crud.cfxSubmit("mainForm","save","update","");
  }

    var  a = function upDown() {
        let length = $('#addAsItem').closest("div").closest("div.Sub-Types").children("div.view").length;
        if (length == 1) {
            $('#addAsItem').closest("div").closest("div.Sub-Types").children("div.view").children('.up').attr("style","display: none");
        } else {
            $('#addAsItem').closest("div").closest("div.Sub-Types").children("div.view").children('.up').removeAttr("style");
        }
    }
</script>
</>
