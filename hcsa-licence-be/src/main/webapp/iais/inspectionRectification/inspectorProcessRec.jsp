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
    <br>
    <br>
    <br>
    <br>
    <br>
    <input type="hidden" name="InspectorProRectificationType" value="">
    <input type="hidden" id="actionValue" name="actionValue" value="">
    <input type="hidden" id="processDec" name="processDec" value="">

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

                <div class="swiper-slide"><a id="inspRectification" href="#tabInspection" aria-controls="tabInspection"
                                             role="tab" data-toggle="tab">Rectification</a></div>
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
                            <td class="col-xs-6" align="right">Application No. (Overall)
                            </td>
                            <td class="col-xs-6">${applicationViewDto.applicationNoOverAll}</td>
                          </tr>
                          <tr>
                            <td align="right">Application No.</td>
                            <td>${applicationViewDto.applicationDto.applicationNo}</td>
                          </tr>
                          <tr>
                            <td align="right">Application Type</td>
                            <td>${applicationViewDto.applicationType}</td>
                          </tr>
                          <tr>
                            <td align="right">Clinical Laboratory</td>
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
                  <a href="/hcsa-licence-web/eservice/INTRANET/LicenceBEViewService?appId=${applicationViewDto.applicationDto.id}" target="_blank">
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
                <div class="panel-group" id="accordion2" role="tablist" aria-multiselectable="true">
                  <h3>
                    <span>View & Mark Submitted Rectifications</span>
                  </h3>
                  <div class="panel panel-default">
                    <div class="panel-collapse collapse in" id="collapseTwo" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                      <div class="panel-body">
                        <div class="panel-main-content">
                          <iais:section title="" id = "retificationView">
                            <iais:row>
                              <iais:field value="Rectifications submitted by Applicant"/>
                            </iais:row>
                            <div class="table-gp">
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
                                  <c:when test="${empty cPoolSearchResult.rows}">
                                    <tr>
                                      <td colspan="7">
                                        <iais:message key="ACK018" escape="true"></iais:message>
                                      </td>
                                    </tr>
                                  </c:when>
                                  <c:otherwise>
                                    <c:forEach var="pool" items="${cPoolSearchResult.rows}">
                                      <tr>
                                        <td><c:out value="${pool.applicationNo}"/></td>
                                        <td><iais:code code="${pool.applicationType}"/></td>
                                        <td><c:out value="${pool.hciCode}"/></td>
                                        <td>
                                          <c:forEach var="file" items="${cPoolSearchResult.rows}">

                                          </c:forEach>
                                        </td>
                                      </tr>
                                    </c:forEach>
                                  </c:otherwise>
                                </c:choose>
                                </tbody>
                              </table>
                            </div>
                          </iais:section>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <div class="tab-pane" id="tabProcessing" role="tabpanel">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                  <h3>
                    <span>Process Rectification</span>
                  </h3>
                  <div class="panel panel-default">
                    <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                      <div class="panel-body">
                        <div class="panel-main-content">
                          <iais:section title="" id = "process_Rectification">
                            <iais:row>
                              <iais:field value="Current Status"/>
                              <iais:value width="7">
                                <label><iais:code code="${inspectionPreTaskDto.appStatus}"/></label>
                              </iais:value>
                            </iais:row>
                            <iais:row>
                              <iais:field value="Remarks"/>
                              <iais:value width="300">
                                <label><c:out value="${inspectionPreTaskDto.reMarks}"></c:out></label>
                              </iais:value>
                            </iais:row>
                            <iais:row>
                              <iais:field value="Processing Decision"/>
                              <iais:value width="7">
                                <iais:select name="selectValue" options="processDecOption" firstOption="Please select" value="${inspectionPreTaskDto.selectValue}" onchange="javascript:doInspectorProRecChange(this.value)"></iais:select>
                              </iais:value>
                            </iais:row>
                            <iais:row>
                              <iais:field value="Internal Remarks"/>
                              <iais:value width="4000">
                                <textarea id="internalRemarks" name="internalRemarks" cols="70" rows="7"><c:out value="${inspectionPreTaskDto.internalMarks}"></c:out></textarea>
                                <br><span class="error-msg" name="iaisErrorMsg" id="error_internalMarks"></span>
                              </iais:value>
                            </iais:row>
                            <iais:row id="indicateCondRemarks">
                              <iais:field value="Please indicate Licensing Terms and Conditions" required="true"/>
                              <iais:value width="4000">
                                <textarea id="condRemarks" name="condRemarks" cols="70" rows="7"><c:out value="${inspectionPreTaskDto.accCondMarks}"></c:out></textarea>
                                <br><span class="error-msg" name="iaisErrorMsg" id="error_accCondMarks"></span>
                              </iais:value>
                            </iais:row>
                            <iais:action style="text-align:center;">
                              <button class="btn btn-lg btn-login-submit" type="button" style="background:#2199E8; color: white" onclick="javascript:doInspectorProRecSubmit()">Submit</button>
                            </iais:action>
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
              </div>
            </div>
          </div>

        </div>
      </div>
    </iais:body>
  </form>
</div>
<%@ include file="/include/validation.jsp" %>
<script type="text/javascript">
    $(document).ready(function() {
        $("#indicateCondRemarks").hidden;
        var actionValue = $("#actionValue").val();
        if(actionValue == "view"){
            inspectorProRecJump();
        }
    })

    function inspectorProRecJump(){
        $("#inspRectification").click();
        $("#recInspTabInfo").removeClass('active');
        $("#recInspTabDocuments").removeClass('active');
        $("#recInspTabInspection").removeClass('active');
        $("#recInspTabProcessing").removeClass('active');
        $("#recInspTabInspection").addClass('active');

    }
    function inspectorProRecSubmit(action){
        $("[name='InspectorProRectificationType']").val(action);
        var mainPoolForm = document.getElementById('mainReviewForm');
        mainPoolForm.submit();
    }

    function doInspectorProRecChange(value) {
        $("#processDec").val(value);
        if("REDECI007" == value){
            $("#indicateCondRemarks").show();
        } else if ("REDECI007" != value){
            $("#indicateCondRemarks").hidden;
        }
    }

    function doInspectorProRecSubmit() {
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
