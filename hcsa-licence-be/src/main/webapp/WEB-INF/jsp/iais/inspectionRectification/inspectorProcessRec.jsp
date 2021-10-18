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
  String webroot=IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
  <form method="post" id="mainReviewForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
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
                <div class="">
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
                          <%@ include file="../hcsaLicence/applicationInfo.jsp" %>
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
                                <table aria-describedby="" class="table">
                                  <thead>
                                  <tr >
                                    <th scope="col" >Vehicle Number</th>
                                    <th scope="col" >NC Clause</th>
                                    <th scope="col" >Checklist Question</th>
                                    <th scope="col" >Remarks</th>
                                    <th scope="col" >Documents</th>
                                  </tr>
                                  </thead>
                                  <tbody>
                                  <c:choose>
                                    <c:when test="${empty inspectionPreTaskDto.inspecUserRecUploadDtos}">
                                      <tr>
                                        <td colspan="7">
                                          <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                                        </td>
                                      </tr>
                                    </c:when>
                                    <c:otherwise>
                                      <c:forEach var="proRec" items="${inspectionPreTaskDto.inspecUserRecUploadDtos}" varStatus="status">
                                        <tr>
                                          <td><c:out value="${proRec.vehicleNo}"/></td>
                                          <td><c:out value="${proRec.checkClause}"/></td>
                                          <td><iais:code code="${proRec.checkQuestion}"/></td>
                                          <td><c:out value="${proRec.uploadRemarks}"/></td>
                                          <td>
                                            <c:if test="${proRec.fileRepoDtos != null}">
                                              <c:forEach var="file" items="${proRec.fileRepoDtos}" varStatus="status">
                                                <div class="fileList ">
                                                  <span class="filename server-site" id="140">
                                                    <u><iais:downloadLink fileRepoIdName="fileRo${status.index}" fileRepoId="${file.id}" docName="${file.fileName}"/></u>
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
                                <a style="float:left;padding-top: 1.1%;" class="back" href="/main-web/eservice/INTRANET/MohHcsaBeDashboard?dashProcessBack=1"><em class="fa fa-angle-left"></em> Back</a>
                              </div>
                              <div class="table-gp" id = "processRecRfi">
                                <table aria-describedby="" class="table">
                                  <thead>
                                  <tr >
                                    <th scope="col" ><input type="checkbox" name="allNcItemCheck" id="allNcItemCheck" <c:if test="${'check' eq allNcItemCheck}">checked</c:if>
                                               onchange="javascript:doInspectorProRecCheckAll()" value="<c:out value="${allNcItemCheck}"/>"/></th>
                                    <th scope="col" >Vehicle Number</th>
                                    <th scope="col" >NC Clause</th>
                                    <th scope="col" >Checklist Question</th>
                                    <th scope="col" >Remarks</th>
                                    <th scope="col" >Documents</th>
                                  </tr>
                                  </thead>
                                  <tbody>
                                  <c:choose>
                                    <c:when test="${empty inspectionPreTaskDto.inspecUserRecUploadDtos}">
                                      <tr>
                                        <td colspan="7">
                                          <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
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
                                          <td><c:out value="${proRec.vehicleNo}"/></td>
                                          <td><c:out value="${proRec.checkClause}"/></td>
                                          <td><iais:code code="${proRec.checkQuestion}"/></td>
                                          <td><c:out value="${proRec.uploadRemarks}"/></td>
                                          <td>
                                            <c:if test="${proRec.fileRepoDtos != null}">
                                              <c:forEach var="file" items="${proRec.fileRepoDtos}" varStatus="status">
                                                <div class="fileList ">
                                                  <span class="filename server-site" id="130">
                                                    <u><iais:downloadLink fileRepoIdName="fileRo${status.index}" fileRepoId="${file.id}" docName="${file.fileName}"/></u>
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
                                <a style="float:left;padding-top: 1.1%;" class="back" href="/main-web/eservice/INTRANET/MohHcsaBeDashboard?dashProcessBack=1"><em class="fa fa-angle-left"></em> Back</a>
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
                            <iais:row>
                              <iais:field value="Processing Decision" required="true"/>
                              <iais:value width="7">
                                <iais:select name="selectValue" options="processDecOption" firstOption="Please Select" value="${inspectionPreTaskDto.selectValue}" onchange="javascript:doInspectorProRecChange(this.value)"></iais:select>
                              </iais:value>
                            </iais:row>
                            <iais:row id="indicateCondRemarks">
                              <div class="col-md-4" style="padding-right: 0px;">
                                <label style="font-size: 16px">Please indicate Licensing Terms and Conditions<span style="color: red"> *</span></label>
                              </div>
                              <iais:value width="4000">
                                <textarea id="condRemarks" name="condRemarks" maxlength="4000" cols="60" rows="7" style="font-size:16px"><c:out value="${inspectionPreTaskDto.accCondMarks}"></c:out></textarea>
                                <br><span class="error-msg" name="iaisErrorMsg" id="error_accCondMarks"></span>
                              </iais:value>
                            </iais:row>
                            <c:if test="${'APTY002' eq applicationViewDto.applicationDto.applicationType}">
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
                              <a style="float:left;padding-top: 1.1%;" class="back" href="/main-web/eservice/INTRANET/MohHcsaBeDashboard?dashProcessBack=1"><em class="fa fa-angle-left"></em> Back</a>
                              <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:doInspectorProRecSubmit()">Submit</button>
                            </iais:action>
                            <br><br><br>
                          </iais:section>
                          <%@include file="/WEB-INF/jsp/iais/inspectionncList/processHistory.jsp"%>
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
        } else {
            $("#indicateCondRemarks").hide();
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
            var errMsg = 'This field is mandatory';
            $("#error_selectValue").text(errMsg);
            dismissWaiting();
        }
    }
</script>
