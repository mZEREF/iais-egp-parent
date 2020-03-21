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
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="inspectorPreType" value="">
    <input type="hidden" id="actionValue" name="actionValue" value="<c:out value="${actionValue}"/>">
    <input type="hidden" id="processDec" name="processDec" value="">
    <div class="main-content">
      <div class="row">
        <div class="col-lg-12 col-xs-12">
          <div class="center-content">
            <div class="intranet-content">
              <iais:body >
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

                      <div class="swiper-slide"><a id="preInspSelfChList" href="#tabInspection" aria-controls="tabInspection"
                                                   role="tab" data-toggle="tab">Checklist</a></div>
                      <div class="swiper-slide"><a href="#tabProcessing" aria-controls="tabProcessing"
                                                   role="tab" data-toggle="tab">Processing</a></div>
                    </div>
                    <div class="swiper-button-prev"></div>
                    <div class="swiper-button-next"></div>
                    </div>
                  <div class="tab-content">
                    <div class="tab-pane active" id="tabInfo" role="tabpanel">
                      <div class="panel panel-default">
                        <!-- Default panel contents -->
                        <div class="panel-heading"><strong>Submission Details</strong></div>
                        <div class="row">
                          <div class="col-xs-12">
                            <div class="table-gp">
                              <table class="table table-bordered">
                                <tbody>
                                <tr>
                                  <td align="right">Application Type</td>
                                  <td>${applicationViewDto.applicationType}</td>
                                </tr>
                                <tr>
                                  <td class="col-xs-6" align="right">Application No. (Overall)
                                  </td>
                                  <td class="col-xs-6">${applicationViewDto.applicationNoOverAll}</td>
                                </tr>
                                <tr>
                                  <td align="right">Application No.</td>
                                  <td>${applicationViewDto.applicationDto.applicationNo}</td>
                                </tr>
                                <tr>
                                  <td align="right">Service Type</td>
                                  <td>${applicationViewDto.serviceType}</td>
                                </tr>
                                <tr>
                                  <td align="right">Submission Date</td>
                                  <td>${applicationViewDto.submissionDate}</td>
                                </tr>
                                <tr>
                                  <td align="right">Current Status</td>
                                  <td>${applicationViewDto.currentStatus}</td>
                                </tr>
                                </tbody>
                              </table>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div align="center">
                        <a href="/hcsa-licence-web/eservice/INTRANET/LicenceBEViewService" target="_blank">
                          <button type="button" class="btn btn-primary">
                            View Application
                          </button>
                        </a>
                        <button type="button" class="btn btn-primary" onclick="javascript:doInspectionPreTaskSelfBack()">
                          Self-Assessment Checklists
                        </button>
                      </div>
                      <div>&nbsp</div>
                      <div class="panel panel-default">
                        <div class="panel-heading"><strong>Applicant Details</strong></div>
                        <div class="row">
                          <div class="col-xs-12">
                            <div class="table-gp">
                              <table class="table table-bordered">
                                <tbody>
                                <tr>
                                  <td class="col-xs-6" align="right">HCI Code</td>
                                  <td class="col-xs-6">-</td>
                                </tr>
                                <tr>
                                  <td align="right">HCI Name</td>
                                  <td>${applicationViewDto.hciName}</td>
                                </tr>
                                <tr>
                                  <td align="right">HCI Address</td>
                                  <td>${applicationViewDto.hciAddress}</td>
                                </tr>
                                <tr>
                                  <td align="right">Telephone</td>
                                  <td>${applicationViewDto.telephone}</td>
                                </tr>
                                <tr>
                                  <td align="right">Fax</td>
                                  <td>-</td>
                                </tr>
                                </tbody>
                              </table>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>

                    <div class="tab-pane" id="tabDocuments" role="tabpanel">
                      <div class="alert alert-info" role="alert"><strong>
                        <h4>Supporting Document</h4>
                      </strong></div>
                      <div id="u8522_text" class="text ">
                        <p><span>These are documents uploaded by the applicant or an officer on behalf of the applicant. Listed
                              documents are those defined for this digital service only.</span></p>
                      </div>
                      <div class="row">
                        <div class="col-xs-12">
                          <div class="table-gp">
                            <table class="table">
                              <thead>
                                <tr>
                                  <th>Document</th>
                                  <th>File</th>
                                  <th>Size</th>
                                  <th>Submitted By</th>
                                  <th>Date Submitted</th>
                                </tr>
                              </thead>
                              <tbody>
                              <c:forEach items="${applicationViewDto.appSupDocDtoList}"
                                         var="appSupDocDto">
                                <tr>
                                  <td>
                                    <p><c:out value="${appSupDocDto.file}"></c:out></p>
                                  </td>
                                  <td>
                                    <p><a href="#"><c:out value="${appSupDocDto.document}"></c:out></a></p>
                                  </td>
                                  <td>
                                    <p><c:out value="${appSupDocDto.size}"></c:out></p>
                                  </td>
                                  <td>
                                    <p><c:out value="${appSupDocDto.submittedBy}"></c:out></p>
                                  </td>
                                  <td>
                                    <p><c:out value="${appSupDocDto.dateSubmitted}"></c:out></p>
                                  </td>
                                </tr>
                              </c:forEach>
                              </tbody>
                            </table>
                            <div class="alert alert-info" role="alert">
                              <strong>
                                <h4>Internal Document</h4>
                              </strong>
                            </div>
                            <div class="text ">
                              <p><span>These are documents uploaded by an agency officer to support back office processing.</span>
                              </p>
                            </div>
                            <table class="table">
                              <thead>
                              <tr>
                                <th>Document</th>
                                <th>File</th>
                                <th>Size</th>
                                <th>Submitted By</th>
                                <th>Date Submitted</th>
                                <th>Action</th>
                              </tr>
                              </thead>
                              <tbody>
                              <tr>
                                <td colspan="5" align="center">
                                  <p>No record found.</p>
                                </td>
                              </tr>
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
                                          <th>Regulation Clause Number</th>
                                          <th>Regulations</th>
                                          <th>Checklist Item</th>
                                          <th>Risk Level</th>
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
                                <div class="panel-heading"  role="tab">
                                  <h4 class="panel-title">Adhoc Item</h4>
                                </div>
                                <div class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingPremise">
                                  <div class="panel-body">
                                    <table class="table">
                                      <thead>
                                      <tr>
                                        <th>Checklist Item</th>
                                        <th>Answer Type</th>
                                        <th>Risk Level</th>
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
                            <button class="btn btn-lg btn-login-edit" style="float:right" type="button" onclick="javascript:doInspectionPreTaskEdit();">Edit</button>
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
                            <p><label><iais:code code="${inspectionPreTaskDto.appStatus}"/></label></p>
                          </iais:value>
                        </iais:row>
                        <iais:row>
                          <iais:field value="Remarks"/>
                          <iais:value width="300">
                            <textarea maxlength="300" id="preInspecRemarks" name="preInspecRemarks" cols="70" rows="7" ><c:out value="${inspectionPreTaskDto.reMarks}"></c:out></textarea>
                            <br><span class="error-msg" name="iaisErrorMsg" id="error_reMarks"></span>
                          </iais:value>
                        </iais:row>
                        <iais:row>
                          <iais:field value="Processing Decision"/>
                          <iais:value width="7">
                            <iais:select name="selectValue" options="processDecOption" firstOption="Please select" value="${inspectionPreTaskDto.selectValue}" onchange="javascript:doInspectionPreTaskChange(this.value)"></iais:select>
                          </iais:value>
                        </iais:row>
                        <iais:action style="text-align:center;">
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
              </iais:body>
            </div>
          </div>
        </div>
      </div>
    </div>
  </form>
</div>
<%@ include file="/include/validation.jsp" %>
<script type="text/javascript">
    $(document).ready(function() {
        var actionValue = $("#actionValue").val();
        if(actionValue == "edit"){
            inspectionPreTaskJump();
        }
    });

    function inspectionPreTaskJump(){
        $("#preInspSelfChList").click();
        $("#preInspTabInfo").removeClass('active');
        $("#preInspTabDocuments").removeClass('active');
        $("#preInspTabInspection").removeClass('active');
        $("#preInspTabProcessing").removeClass('active');
        $("#preInspTabInspection").addClass('active');

    }

    function inspectionPreTaskSubmit(action){
        $("[name='inspectorPreType']").val(action);
        var mainPoolForm = document.getElementById('mainReviewForm');
        mainPoolForm.submit();
    }

    function doInspectionPreTaskEdit() {
        $("#actionValue").val('edit');
        inspectionPreTaskSubmit('edit');
    }

    function doInspectionPreTaskSelfBack() {
        $("#actionValue").val('self');
        inspectionPreTaskSubmit('self');
    }

    function doInspectionPreTaskBack() {
        $("#actionValue").val('back');
    }

    function doInspectionPreTaskChange(value) {
        $("#processDec").val(value);
    }

    function doInspectionPreTaskSubmit() {
        var actionValue = $("#processDec").val();
        if("REDECI002" == actionValue){
            $("#actionValue").val('approve');
            inspectionPreTaskSubmit("approve");
        } else if ("REDECI001" == actionValue){
            $("#actionValue").val('routeB');
            inspectionPreTaskSubmit("routeB");
        } else {
            var errMsg = 'The field is mandatory.';
            $("#error_selectValue").text(errMsg);
            dismissWaiting();
        }
    }
</script>

