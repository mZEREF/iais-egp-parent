<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2019/12/9
  Time: 9:40
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<%
  String webroot=IaisEGPConstant.BE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
  <form method="post" id="mainReviewForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="inspectorPreType" value="">
    <input type="hidden" id="actionValue" name="actionValue" value="<c:out value="${actionValue}"/>">
    <input type="hidden" id="processDec" name="processDec" value="<c:out value="${inspectionPreTaskDto.selectValue}"/>">
    <div class="main-content">
      <div class="row">
        <div class="col-lg-12 col-xs-12">
          <div class="center-content">
            <div class="intranet-content">
              <iais:body >
                <div class="container">
                  <div class="col-xs-12">
                    <div class="tab-gp dashboard-tab">
                      <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                        <li id="preInspTabInfo" class="active" role="presentation"><a href="#tabInfo" aria-controls="tabInfo" role="tab"
                                                                                      data-toggle="tab">Info</a></li>
                        <li id="preInspTabDocuments" class="complete" role="presentation"><a href="#tabDocuments"
                                                                                             aria-controls="tabDocuments" role="tab"
                                                                                             data-toggle="tab">Documents</a></li>
                        <li id="preInspTabInspection" class="complete" role="presentation" style="display: block"><a href="#tabInspection"
                                                                                                                     aria-controls="tabInspection" role="tab"
                                                                                                                     data-toggle="tab">Checklist</a></li>
                        <li id="preInspTabProcessing" class="incomplete" role="presentation"><a href="#tabProcessing"
                                                                                                aria-controls="tabProcessing" role="tab"
                                                                                                data-toggle="tab">Processing</a></li>
                      </ul>
                      <div class="tab-nav-mobile visible-xs visible-sm">
                        <div class="swiper-wrapper" role="tablist">
                          <div class="swiper-slide"><a href="#tabInfo" aria-controls="tabInfo" role="tab"
                                                       data-toggle="tab">Info</a></div>
                          <div class="swiper-slide"><a href="#tabDocuments" aria-controls="tabDocuments"
                                                       role="tab" data-toggle="tab">Documents</a></div>

                          <div class="swiper-slide"><a id = "preInspSelfChList" href="#tabInspection" aria-controls="tabInspection"
                                                       role="tab" data-toggle="tab">Checklist</a></div>
                          <div class="swiper-slide"><a id = "preInspProcess" href="#tabProcessing" aria-controls="tabProcessing"
                                                       role="tab" data-toggle="tab">Processing</a></div>
                        </div>
                        <div class="swiper-button-prev"></div>
                        <div class="swiper-button-next"></div>
                      </div>
                      <div class="tab-content">
                        <div class="tab-pane active" id="tabInfo" role="tabpanel">
                          <%@ include file="../hcsaLicence/applicationInfo.jsp" %>
                        </div>

                        <div class="tab-pane" id="tabDocuments" role="tabpanel">
                          <%@ include file="../inspectionncList/tabDocuments.jsp" %>
                          <br>
                          <div class="alert alert-info" role="alert">
                            <strong>
                              <h4>Past Inspection Detail</h4>
                            </strong>
                          </div>
                          <div class="row">
                            <div class="col-xs-12">
                              <div class="table-gp">
                                <table class="table">
                                  <thead>
                                  <tr>
                                    <th>S/N</th>
                                    <th>HCI Code</th>
                                    <th>HCI Name / Address</th>
                                    <th>Service Name</th>
                                    <th>Licence Period</th>
                                    <th>Last Inspection Date</th>
                                    <th>Compliance and Risk Assessment</th>
                                    <th>Inspector Lead</th>
                                    <th>Inspector</th>
                                    <th>Remarks</th>
                                  </tr>
                                  </thead>
                                  <tbody>
                                  <c:choose>
                                    <c:when test="${empty inspectionHistoryShowDtos}">
                                      <tr>
                                        <td colspan="12">
                                          <iais:message key="ACK018" escape="true"></iais:message>
                                          <!--No Record!!-->
                                        </td>
                                      </tr>
                                    </c:when>
                                    <c:otherwise>
                                      <c:forEach var="inspHistory" items="${inspectionHistoryShowDtos}" varStatus="status">
                                        <tr>
                                          <td class="row_no"><c:out value="${status.index + 1}"/></td>
                                          <td><c:out value="${inspHistory.hciCode}"/></td>
                                          <td><c:out value="${inspHistory.hciNameAddress}"/></td>
                                          <td><c:out value="${inspHistory.serviceName}"/></td>
                                          <td><c:out value="${inspHistory.licencePeriod}"/></td>
                                          <td>
                                            <c:if test="${inspHistory.inspDate != null}">
                                              <fmt:formatDate value='${inspHistory.inspDate}' pattern='dd/MM/yyyy'/>
                                            </c:if>
                                            <c:if test="${inspHistory.inspDate == null}">
                                              <c:out value="-"/>
                                            </c:if>
                                          </td>
                                          <td><iais:code code="${inspHistory.complianceRisk}"/></td>
                                          <td>
                                            <c:if test="${!empty inspHistory.inspLeads}">
                                              <c:forEach var="inspLead" items="${inspHistory.inspLeads}">
                                                <c:out value="${inspLead}"/>
                                              </c:forEach>
                                            </c:if>
                                            <c:if test="${empty inspHistory.inspLeads}">
                                              <c:out value="-"/>
                                            </c:if>
                                          </td>
                                          <td>
                                            <c:if test="${!empty inspHistory.inspectors}">
                                              <c:forEach var="inspector" items="${inspHistory.inspectors}">
                                                <c:out value="${inspector}"/>
                                              </c:forEach>
                                            </c:if>
                                            <c:if test="${empty inspHistory.inspectors}">
                                              <c:out value="-"/>
                                            </c:if>
                                          </td>
                                          <td><c:out value="${inspHistory.remark}"/></td>
                                        </tr>
                                      </c:forEach>
                                    </c:otherwise>
                                  </c:choose>
                                  </tbody>
                                </table>
                              </div>
                            </div>
                          </div>
                        </div>

                        <div class="tab-pane" id="tabInspection" role="tabpanel">
                          <div class="row">
                            <div class="col-xs-12">
                              <div class="center-content">
                                <c:forEach var = "item" items="${inspectionChecklistAttr}" varStatus="status">
                                  <c:if test="${item.common eq true}">
                                    <div class="bg-panel-heading">
                                      <h2>General Regulation</h2>
                                    </div>
                                  </c:if>
                                  <c:if test="${item.common eq false}">
                                    <c:choose>
                                      <c:when test="${empty item.svcSubType}">
                                        <h2>${item.svcName}</h2>
                                      </c:when>
                                      <c:otherwise>
                                        <h2>${item.svcName} | ${item.svcSubType}</h2>
                                      </c:otherwise>
                                    </c:choose>
                                  </c:if>
                                  <c:forEach var = "sec" items="${item.sectionDtos}">
                                    <div class="panel panel-default">
                                      <div class="panel-heading" id="headingPremise" role="tab">
                                        <h4 class="panel-title">${sec.section}</h4>
                                      </div>
                                      <div class="panel-collapse collapse in" id="collapsePremise" role="tabpanel" aria-labelledby="headingPremise">
                                        <div class="panel-body">
                                          <table class="table">
                                            <thead>
                                            <tr>
                                              <th width="10%">Regulation Clause Number</th>
                                              <th width="40%">Regulations</th>
                                              <th width="40%">Checklist Item</th>
                                              <th width="10%">Risk Level</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach var = "chklitem" items = "${sec.checklistItemDtos}" varStatus="status">
                                              <tr>
                                                <td>
                                                  <p>${chklitem.regulationClauseNo}</p>
                                                </td>
                                                <td>
                                                  <p>${chklitem.regulationClause}</p>
                                                </td>
                                                <td>
                                                  <p>${chklitem.checklistItem}</p>
                                                </td>
                                                <td>
                                                  <p><iais:code code="${chklitem.riskLevel}"></iais:code></p>
                                                </td>
                                              </tr>
                                            </c:forEach>
                                            </tbody>
                                          </table>
                                        </div>
                                      </div>
                                    </div>
                                  </c:forEach>
                                </c:forEach>
                                <c:if test="${!empty adhocCheckListAttr}">
                                  <div class="panel panel-default">
                                    <div class="panel-heading" role="tab">
                                      <h4 class="panel-title">Adhoc Item</h4>
                                    </div>
                                    <div class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingPremise">
                                      <div class="panel-body">
                                        <table class="table">
                                          <thead>
                                          <tr>
                                            <th width="50%">Checklist Item</th>
                                            <th width="40%">Answer Type</th>
                                            <th width="10%">Risk Level</th>
                                          </tr>
                                          </thead>
                                          <tbody>
                                          <c:forEach var = "adhocItem" items = "${adhocCheckListAttr.allAdhocItem}" varStatus="status">
                                            <tr>
                                              <td>
                                                <p>${adhocItem.question}</p>
                                              </td>
                                              <td>
                                                <p><iais:code code="${adhocItem.answerType}"></iais:code></p>
                                              </td>
                                              <td>
                                                <p><iais:code code="${adhocItem.riskLvl}"></iais:code></p>
                                              </td>
                                            </tr>
                                          </c:forEach>
                                          </tbody>
                                        </table>
                                      </div>
                                    </div>
                                  </div>
                                </c:if>
                                <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:doInspectionPreTaskEdit()">Update</button>
                              </div>
                            </div>
                          </div>
                        </div>

                        <div class="tab-pane" id="tabProcessing" role="tabpanel">
                          <div class="alert alert-info" role="alert">
                            <strong>
                              <h4>Processing Status Update</h4>
                            </strong>
                          </div>
                          <iais:section title="" id = "review_Task">
                            <iais:row>
                              <iais:field value="Current Status"/>
                              <iais:value width="7">
                                <p><span style="font-size: 16px"><iais:code code="${inspectionPreTaskDto.appStatus}"/></span></p>
                              </iais:value>
                            </iais:row>
                            <iais:row>
                              <iais:field value="Remarks"/>
                              <iais:value width="300">
                                <textarea maxlength="300" id="preInspecRemarks" name="preInspecRemarks" cols="60" rows="7" style="font-size:16px"><c:out value="${inspectionPreTaskDto.reMarks}"></c:out></textarea>
                                <br><span class="error-msg" name="iaisErrorMsg" id="error_reMarks"></span>
                              </iais:value>
                            </iais:row>
                            <iais:row>
                              <iais:field value="Processing Decision" required="true"/>
                              <iais:value width="7">
                                <iais:select name="selectValue" options="processDecOption" firstOption="Please Select" value="${inspectionPreTaskDto.selectValue}" onchange="javascript:doInspectionPreTaskChange(this.value)"></iais:select>
                              </iais:value>
                            </iais:row>
                            <iais:row id="rbCheckStage">
                              <iais:field value="Internal Route Back" required="true"/>
                              <iais:value width="7">
                                <iais:select name="checkRbStage" options="preInspRbOption" value="${inspectionPreTaskDto.checkRbStage}"></iais:select>
                              </iais:value>
                            </iais:row>
                            <iais:row id="rfiCheckBox">
                              <iais:field value="Request For Information" required="true"/>
                              <iais:value width="7">
                                <c:if test="${inspectionPreTaskDto.preInspRfiOption != null}">
                                  <c:forEach items="${inspectionPreTaskDto.preInspRfiOption}" var="name">
                                    <p>
                                      <input type="checkbox" name="preInspRfiCheck" id = "${name.value}PreInspRfiCheck"  value="<c:out value="${name.value}"/>"
                                            <c:forEach items="${inspectionPreTaskDto.preInspRfiCheck}" var="checkName">
                                              <c:if test="${name.value eq checkName}">checked="checked"</c:if>
                                            </c:forEach>
                                      />
                                      <span style="font-size: 16px"><c:out value="${name.text}"/></span>
                                    </p>
                                  </c:forEach>
                                  <span class="error-msg" name="iaisErrorMsg" id="error_preInspRfiCheck"></span>
                                </c:if>
                              </iais:value>
                            </iais:row>
                            <div id="preInspRfiComments" class="hidden">
                              <iais:row>
                                <iais:field value="Comments" required="false"  width="12"/>
                                <iais:value width="10">
                                  <textarea maxlength="300" id="preInspecComments" name="preInspecComments" cols="60" rows="7" style="font-size:16px"><c:out value="${inspectionPreTaskDto.reMarks}"></c:out></textarea>
                                </iais:value>
                              </iais:row>
                            </div>
                            <c:if test="${'APTY002' eq applicationViewDto.applicationDto.applicationType}">
                              <div class="row">
                                <div class="col-md-4">
                                  <label style="font-size: 16px">Licence Start Date</label>
                                </div>
                                <div class="col-md-6">
                                  <c:if test="${applicationViewDto.recomLiceStartDate != null}">
                                    <span style="font-size: 16px"><fmt:formatDate value='${applicationViewDto.recomLiceStartDate}' pattern='dd/MM/yyyy'/></span>
                                  </c:if>
                                  <c:if test="${applicationViewDto.recomLiceStartDate == null}">
                                    <span style="font-size: 16px">-</span>
                                  </c:if>
                                </div>
                              </div>
                              <p></p><br><br>
                            </c:if>
                            <div class="row">
                              <div class="col-md-4">
                                <label style="font-size: 16px">Fast Tracking?</label>
                              </div>
                              <div class="col-md-6">
                                <input disabled type="checkbox" <c:if test="${applicationViewDto.applicationDto.fastTracking}">checked="checked"</c:if>/>
                              </div>
                            </div>
                            <iais:action>
                              <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:doInspectionPreTaskSubmit()">Submit</button>
                            </iais:action>
                            <br><br><br>
                            <div class="alert alert-info" role="alert">
                              <strong>
                                <h4>Processing History</h4>
                              </strong>
                            </div>
                            <div class="row">
                              <div class="col-xs-12">
                                <div class="table-gp">
                                  <table class="table">
                                    <thead>
                                    <tr>
                                      <th>Username</th>
                                      <th>Working Group</th>
                                      <th>Status Update</th>
                                      <th>Remarks</th>
                                      <th>Last Updated</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach
                                            items="${applicationViewDto.appPremisesRoutingHistoryDtoList}"
                                            var="appPremisesRoutingHistoryDto">
                                      <tr>
                                        <td>
                                          <p><c:out
                                                  value="${appPremisesRoutingHistoryDto.actionby}"></c:out></p>
                                        </td>
                                        <td>
                                          <p><c:out
                                                  value="${appPremisesRoutingHistoryDto.workingGroup}"></c:out></p>
                                        </td>
                                        <td>
                                          <p><c:out
                                                  value="${appPremisesRoutingHistoryDto.processDecision}"></c:out></p>
                                        </td>
                                        <td>
                                          <p><c:out
                                                  value="${appPremisesRoutingHistoryDto.internalRemarks}"></c:out></p>
                                        </td>
                                        <td>
                                          <p><c:out
                                                  value="${appPremisesRoutingHistoryDto.updatedDt}"></c:out></p>
                                        </td>
                                      </tr>
                                    </c:forEach>
                                    </tbody>
                                  </table>
                                </div>
                              </div>
                            </div>
                          </iais:section>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </iais:body>
            </div>
          </div>
        </div>
      </div>
    </div>
  </form>
</div>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<%@ include file="../inspectionncList/uploadFile.jsp" %>
<script type="text/javascript">
    $(document).ready(function() {
        var actionValue = $("#actionValue").val();
        if(actionValue == "edit"){
            inspectionPreTaskJump("edit");
        } else if(actionValue == "request" || actionValue == "routeB"){
            inspectionPreTaskJump("process");
        }
        var selectValue = $("#processDec").val();
        if("REDECI001" == selectValue){
            $("#rfiCheckBox").show();
            $("#rbCheckStage").hide();
        } else if("REDECI021" == selectValue){
            $("#rfiCheckBox").hide();
            $("#rbCheckStage").show();
        } else {
            $("#rfiCheckBox").hide();
            $("#rbCheckStage").hide();
        }
    });

    function inspectionPreTaskJump(value){
        $("#preInspTabInfo").removeClass('active');
        $("#preInspTabDocuments").removeClass('active');
        $("#preInspTabInspection").removeClass('active');
        $("#preInspTabProcessing").removeClass('active');
        if("edit" == value){
            $("#preInspSelfChList").click();
            $("#preInspTabInspection").addClass('active');
        } else {
            $("#preInspProcess").click();
            $("#preInspTabProcessing").addClass('active');
        }
    }

    $("#appPreInspRfiCheck").change(function inspPreRfiAppCheck() {
        var check = $("#appPreInspRfiCheck").prop("checked");
        if(check){
            showPopupWindow('/hcsa-licence-web/eservice/INTRANET/LicenceBEViewService?rfi=rfi');
            $("#comments").remove('hidden');
        } else {
            $("#comments").addClass('hidden');
        }
    });


    function inspectionPreTaskSubmit(action){
        $("[name='inspectorPreType']").val(action);
        var mainPoolForm = document.getElementById('mainReviewForm');
        mainPoolForm.submit();
    }

    function doInspectionPreTaskEdit() {
        showWaiting();
        $("#actionValue").val('edit');
        inspectionPreTaskSubmit('edit');
    }

    function doInspectionPreTaskSelfBack() {
        showWaiting();
        $("#actionValue").val('self');
        inspectionPreTaskSubmit('self');
    }

    function doInspectionPreTaskBack() {
        showWaiting();
        $("#actionValue").val('back');
    }

    function doInspectionPreTaskChange(value) {
        $("#processDec").val(value);
        if("REDECI001" == value){
            $("#rfiCheckBox").show();
            $("#rbCheckStage").hide();
        } else if("REDECI021" == value){
            $("#rfiCheckBox").hide();
            $("#rbCheckStage").show();
        } else {
            $("#rfiCheckBox").hide();
            $("#rbCheckStage").hide();
        }
    }

    function doInspectionPreTaskSubmit() {
        showWaiting();
        var actionValue = $("#processDec").val();
        if("REDECI002" == actionValue){
            $("#actionValue").val('approve');
            inspectionPreTaskSubmit("approve");
        } else if ("REDECI001" == actionValue){
            $("#actionValue").val('request');
            inspectionPreTaskSubmit("routeB");
        } else if("REDECI021" == actionValue){
            $("#actionValue").val('routeB');
            inspectionPreTaskSubmit("apso");
        } else {
            var errMsg = 'The field is mandatory.';
            $("#error_selectValue").text(errMsg);
            dismissWaiting();
        }
    }
</script>

