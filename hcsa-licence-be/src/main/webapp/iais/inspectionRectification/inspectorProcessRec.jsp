<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2019/12/18
  Time: 14:37
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
    <input type="hidden" name="InspectorProRectificationType" value="">
    <input type="hidden" id="actionValue" name="actionValue" value="">
    <input type="hidden" id="processDec" name="processDec" value="${inspectionPreTaskDto.selectValue}">
    <input type="hidden" id="validateShowPage" name="validateShowPage" value="${validateShowPage}">
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
                        <li id="recInspTabInfo" class="active" role="presentation"><a href="#tabInfo" aria-controls="tabInfo" role="tab"
                                                                                      data-toggle="tab">Info</a></li>
                        <li id="recInspTabDocuments" class="complete" role="presentation"><a href="#tabDocuments"
                                                                                             aria-controls="tabDocuments" role="tab"
                                                                                             data-toggle="tab">Documents</a></li>
                        <li id="recInspTabInspection" class="complete" role="presentation" style="display: block"><a href="#tabInspection"
                                                                                                                     aria-controls="tabInspection" role="tab"
                                                                                                                     data-toggle="tab">Rectification</a></li>
                        <li id="recInspTabProcessing" class="incomplete" role="presentation"><a href="#tabProcessing"
                                                                                                aria-controls="tabProcessing" role="tab"
                                                                                                data-toggle="tab">Processing</a></li>
                      </ul>
                      <div class="tab-nav-mobile visible-xs visible-sm">
                        <div class="swiper-wrapper" role="tablist">
                          <div class="swiper-slide"><a href="#tabInfo" aria-controls="tabInfo" role="tab"
                                                       data-toggle="tab">Info</a></div>
                          <div class="swiper-slide"><a href="#tabDocuments" aria-controls="tabDocuments"
                                                       role="tab" data-toggle="tab">Documents</a></div>

                          <div class="swiper-slide"><a id="recInspTabInspectionClick" href="#tabInspection" aria-controls="tabInspection"
                                                       role="tab" data-toggle="tab">Rectification</a></div>
                          <div class="swiper-slide"><a id="recInspTabProcessingClick" href="#tabProcessing" aria-controls="tabProcessing"
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
                            <button type="button" class="btn btn-primary" onclick="javascript:doInspectorProRecView()">
                              View Checklist
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
                          <%@ include file="../inspectionncList/tabDocuments.jsp" %>
                        </div>

                        <div class="tab-pane" id="tabInspection" role="tabpanel">
                          <div class="panel-group" id="accordion2" role="tablist" aria-multiselectable="true">
                            <div class="alert alert-info" role="alert">
                              <strong>
                                <h4>View & Mark Submitted Rectifications</h4>
                              </strong>
                            </div>
                            <iais:section title="" id = "retificationView">
                              <iais:row>
                                <iais:field value="Rectifications submitted by Applicant"/>
                              </iais:row>
                              <div class="table-gp" id = "processRec">
                                <table class="table">
                                  <thead>
                                  <tr align="center">
                                    <th>NC Clause</th>
                                    <th>Checklist Question</th>
                                    <th>Remarks</th>
                                    <th>Documents</th>
                                  </tr>
                                  </thead>
                                  <tbody>
                                  <c:choose>
                                    <c:when test="${empty inspectionPreTaskDto.inspecUserRecUploadDtos}">
                                      <tr>
                                        <td colspan="7">
                                          <iais:message key="ACK018" escape="true"></iais:message>
                                        </td>
                                      </tr>
                                    </c:when>
                                    <c:otherwise>
                                      <c:forEach var="proRec" items="${inspectionPreTaskDto.inspecUserRecUploadDtos}" varStatus="status">
                                        <tr>
                                          <td><c:out value="${proRec.checkClause}"/></td>
                                          <td><iais:code code="${proRec.checkQuestion}"/></td>
                                          <td><c:out value="${proRec.uploadRemarks}"/></td>
                                          <td>
                                            <c:if test="${proRec.fileRepoDtos != null}">
                                              <c:forEach var="file" items="${proRec.fileRepoDtos}" varStatus="status">
                                                <div class="fileList ">
                                                  <span class="filename server-site" id="140">
                                                    <u><a href="${pageContext.request.contextPath}/file-repo-popup?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}" value="${file.id}"/>&fileRepoName=${file.fileName}" title="Download" class="downloadFile">${file.fileName}</a></u>
                                                  </span>
                                                </div>
                                              </c:forEach>
                                            </c:if>
                                          </td>
                                        </tr>
                                      </c:forEach>
                                    </c:otherwise>
                                  </c:choose>
                                  </tbody>
                                </table>
                              </div>
                              <div class="table-gp" id = "processRecRfi">
                                <table class="table">
                                  <thead>
                                  <tr align="center">
                                    <th><input type="checkbox" name="allNcItemCheck" id="allNcItemCheck" <c:if test="${'check' eq allNcItemCheck}">checked</c:if>
                                               onchange="javascript:doInspectorProRecCheckAll()" value="<c:out value="${allNcItemCheck}"/>"/></th>
                                    <th>NC Clause</th>
                                    <th>Checklist Question</th>
                                    <th>Remarks</th>
                                    <th>Documents</th>
                                  </tr>
                                  </thead>
                                  <tbody>
                                  <c:choose>
                                    <c:when test="${empty inspectionPreTaskDto.inspecUserRecUploadDtos}">
                                      <tr>
                                        <td colspan="7">
                                          <iais:message key="ACK018" escape="true"></iais:message>
                                        </td>
                                      </tr>
                                    </c:when>
                                    <c:otherwise>
                                      <c:forEach var="proRec" items="${inspectionPreTaskDto.inspecUserRecUploadDtos}" varStatus="status">
                                        <tr>
                                          <td>
                                            <input type="checkbox" name="ncItemCheck" id="ncItemCheck${status.index}"
                                                    <c:if test="${!empty inspectionPreTaskDto.checkRecRfiNcItems}">
                                                      <c:forEach items="${inspectionPreTaskDto.checkRecRfiNcItems}" var="checkNcItemId">
                                                        <c:if test="${proRec.appPremisesPreInspectionNcItemDto.id eq checkNcItemId}">checked="checked"</c:if>
                                                      </c:forEach>
                                                    </c:if>
                                                   onchange="javascript:doInspectorProRecCheck()" value="<c:out value="${proRec.appPremisesPreInspectionNcItemDto.id}"/>"/>
                                          </td>
                                          <td><c:out value="${proRec.checkClause}"/></td>
                                          <td><iais:code code="${proRec.checkQuestion}"/></td>
                                          <td><c:out value="${proRec.uploadRemarks}"/></td>
                                          <td>
                                            <c:if test="${proRec.fileRepoDtos != null}">
                                              <c:forEach var="file" items="${proRec.fileRepoDtos}" varStatus="status">
                                                <div class="fileList ">
                                                  <span class="filename server-site" id="130">
                                                    <a href="${pageContext.request.contextPath}/file-repo-popup?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}" value="${file.id}"/>&fileRepoName=${file.fileName}" title="Download" class="downloadFile">${file.fileName}</a>
                                                  </span>
                                                </div>
                                              </c:forEach>
                                            </c:if>
                                          </td>
                                        </tr>
                                      </c:forEach>
                                    </c:otherwise>
                                  </c:choose>
                                  </tbody>
                                  <span class="error-msg" name="iaisErrorMsg" id="error_checkRecRfiNcItems"></span>
                                </table>
                              </div>
                            </iais:section>
                          </div>
                        </div>

                        <div class="tab-pane" id="tabProcessing" role="tabpanel">
                          <div class="alert alert-info" role="alert">
                            <strong>
                              <h4>Processing Status Update</h4>
                            </strong>
                          </div>
                          <iais:section title="" id = "process_Rectification">
                            <iais:row>
                              <iais:field value="Current Status"/>
                              <iais:value width="7">
                                <p><span style="font-size: 16px"><iais:code code="${inspectionPreTaskDto.appStatus}"/></span></p>
                              </iais:value>
                            </iais:row>
                            <iais:row>
                              <iais:field value="Internal Remarks"/>
                              <iais:value width="4000">
                                <textarea id="internalRemarks" name="internalRemarks" cols="60" rows="7" style="font-size:16px"><c:out value="${inspectionPreTaskDto.internalMarks}"></c:out></textarea>
                                <br><span class="error-msg" name="iaisErrorMsg" id="error_internalMarks"></span>
                              </iais:value>
                            </iais:row>
                            <iais:row id="indicateCondRemarks">
                              <iais:field value="Please indicate Licensing Terms and Conditions" required="true"/>
                              <iais:value width="4000">
                                <textarea id="condRemarks" name="condRemarks" cols="60" rows="7" style="font-size:16px"><c:out value="${inspectionPreTaskDto.accCondMarks}"></c:out></textarea>
                                <br><span class="error-msg" name="iaisErrorMsg" id="error_accCondMarks"></span>
                              </iais:value>
                            </iais:row>
                            <iais:row id="indicateCondRemarks">
                              <iais:field value="Processing Decision" required="true"/>
                              <iais:value width="7">
                                <iais:select name="selectValue" options="processDecOption" firstOption="Please Select" value="${inspectionPreTaskDto.selectValue}" onchange="javascript:doInspectorProRecChange(this.value)"></iais:select>
                              </iais:value>
                            </iais:row>
                            <div class="row">
                              <div class="col-md-4">
                                <label style="font-size: 16px">Licence Start Date</label>
                              </div>
                              <div class="col-md-6">
                                <c:if test="${applicationViewDto.recomLiceStartDate != null}">
                                  <span style="font-size: 16px"><fmt:formatDate value='${applicationViewDto.recomLiceStartDate}' pattern='dd/MM/yyyy' /></span>
                                </c:if>
                                <c:if test="${applicationViewDto.recomLiceStartDate == null}">
                                  <span style="font-size: 16px">-</span>
                                </c:if>
                              </div>
                            </div>
                            <p></p><br><br>
                            <div class="row">
                              <div class="col-md-4">
                                <label style="font-size: 16px">Fast Tracking</label>
                              </div>
                              <div class="col-md-6">
                                <input disabled type="checkbox" <c:if test="${applicationViewDto.applicationDto.fastTracking}">checked="checked"</c:if>/>
                              </div>
                            </div>
                            <iais:action>
                              <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:doInspectorProRecSubmit()">Submit</button>
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
<%@ include file="/include/validation.jsp" %>
<%@ include file="../inspectionncList/uploadFile.jsp" %>
<script type="text/javascript">
    $(document).ready(function() {
        var value = $("#processDec").val();
        if("REDECI007" == value){
            $("#indicateCondRemarks").show();
        } else if ("REDECI007" != value){
            $("#indicateCondRemarks").hide();
        }
        if ("REDECI001" == value){
            $("#processRec").hide();
            $("#processRecRfi").show();
        } else {
            $("#processRec").show();
            $("#processRecRfi").hide();
        }
        var selectValue = $("#selectValue").val();
        if(selectValue != "REDECI007") {
            $("#indicateCondRemarks").hide();
        } else {
            $("#indicateCondRemarks").show();
        }
        var validateShowPage = $("#validateShowPage").val();
        if(validateShowPage == "ack"){
            inspectorProRecJump(validateShowPage);
        } else if (validateShowPage == "request") {
            inspectorProRecJump(validateShowPage);
        }
        doInspectorProRecCheck();
    })

    function inspectorProRecJump(ackValue){
        $("#recInspTabInfo").removeClass('active');
        $("#recInspTabDocuments").removeClass('active');
        $("#recInspTabInspection").removeClass('active');
        $("#recInspTabProcessing").removeClass('active');
        if(ackValue == "ack") {
            $("#recInspTabProcessingClick").click();
            $("#recInspTabProcessing").addClass('active');
        } else if(ackValue == "request") {
            $("#recInspTabInspectionClick").click();
            $("#recInspTabInspection").addClass('active');
        }
    }

    function doInspectorProRecCheckAll(){
        if ($('#allNcItemCheck').is(':checked')) {
            $("input[name = 'ncItemCheck']").attr("checked","true");
        } else {
            $("input[name = 'ncItemCheck']").removeAttr("checked");
        }
    }

    function doInspectorProRecCheck(){
        var flag = true;
        var allNcItemCheck = document.getElementById("allNcItemCheck");
        var ncItemCheckList = document.getElementsByName("ncItemCheck");
        for (var x = 0; x < ncItemCheckList.length; x++) {
            if(ncItemCheckList[x].checked==false){
                flag = false;
                break;
            }
        }
        if(flag){
            allNcItemCheck.checked = true;
        }else{
            allNcItemCheck.checked = false;
        }
    }

    function inspectorProRecSubmit(action){
        $("[name='InspectorProRectificationType']").val(action);
        var mainPoolForm = document.getElementById('mainReviewForm');
        mainPoolForm.submit();
    }

    function doInspectorProRecChange(value) {
        $("#processDec").val(value);
        if("REDECI006" == value){
            $("#indicateCondRemarks").hide();
            $("#processRec").show();
            $("#processRecRfi").hide();
        } else if ("REDECI001" == value){
            $("#indicateCondRemarks").hide();
            $("#processRec").hide();
            $("#processRecRfi").show();
        } else if("REDECI007" == value) {
            $("#indicateCondRemarks").show();
            $("#processRec").show();
            $("#processRecRfi").hide();
        }
    }

    function doInspectorProRecView() {
        showWaiting();
        $("#actionValue").val('view');
        inspectorProRecSubmit('view');
    }

    function doInspectorProRecSubmit() {
        showWaiting();
        var processDec = $("#processDec").val();
        if("REDECI006" == processDec){
            $("#actionValue").val('accept');
            inspectorProRecSubmit("accept");
        } else if ("REDECI001" == processDec){
            $("#actionValue").val('request');
            inspectorProRecSubmit("request");
        } else if("REDECI007" == processDec) {
            $("#actionValue").val('acccond');
            inspectorProRecSubmit("acccond");
        } else {
            var errMsg = 'The field is mandatory.';
            $("#error_selectValue").text(errMsg);
            dismissWaiting();
        }
    }
</script>
