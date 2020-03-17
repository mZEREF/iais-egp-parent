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
  <form method="post" id="mainInspDateForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <br>
    <br>
    <br>
    <br>
    <br>
    <input type="hidden" name="apptInspectionDateType" value="">
    <input type="hidden" id="actionValue" name="actionValue" value="">
    <input type="hidden" id="processDec" name="processDec" value="">
    <iais:body >
      <div class="container">
        <div class="col-xs-12">
          <div class="tab-gp dashboard-tab">
            <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
              <li id="ApptInspTabInfo" class="active" role="presentation"><a href="#tabInfo" aria-controls="tabInfo" role="tab"
                                                                            data-toggle="tab">Info</a></li>
              <li id="ApptInspTabDocuments" class="complete" role="presentation"><a href="#tabDocuments"
                                                                                   aria-controls="tabDocuments" role="tab"
                                                                                   data-toggle="tab">Documents</a></li>
              <li id="ApptInspTabProcessing" class="incomplete" role="presentation"><a href="#tabProcessing"
                                                                                      aria-controls="tabProcessing" role="tab"
                                                                                      data-toggle="tab">Processing</a></li>
            </ul>
            <div class="tab-nav-mobile visible-xs visible-sm">
              <div class="swiper-wrapper" role="tablist">
                <div class="swiper-slide"><a href="#tabInfo" aria-controls="tabInfo" role="tab"
                                             data-toggle="tab">Info</a></div>
                <div class="swiper-slide"><a href="#tabDocuments" aria-controls="tabDocuments"
                                             role="tab" data-toggle="tab">Documents</a></div>
                <div class="swiper-slide"><a id="apptInspectionDate" href="#tabProcessing" aria-controls="tabProcessing"
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

              <div class="tab-pane" id="tabProcessing" role="tabpanel">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                  <h3>
                    <span>Appointment Scheduling (Inspection)</span>
                  </h3>
                  <div class="panel panel-default">
                    <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                      <div class="panel-body">
                        <div class="panel-main-content">
                          <iais:section title="" id = "inspection_date">
                            <div class="table-gp">
                              <table class="table">
                                <thead>
                                <tr align="center">
                                  <th>Application No</th>
                                  <th>Application Status</th>
                                  <th>Responsible Person</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                  <c:when test="${empty apptInspectionDateDto.applicationInfoShow}">
                                    <tr>
                                      <td colspan="7">
                                        <iais:message key="ACK018" escape="true"></iais:message>
                                      </td>
                                    </tr>
                                  </c:when>
                                  <c:otherwise>
                                    <c:forEach var="appInfoShow" items="${apptInspectionDateDto.applicationInfoShow}">
                                      <tr>
                                        <td><c:out value="${appInfoShow.key.applicationNo}"/></td>
                                        <td><iais:code code="${appInfoShow.key.status}"/></td>
                                        <td>
                                          <c:if test="${appInfoShow.value != null}">
                                            <c:forEach var="worker" items="${appInfoShow.value}" varStatus="status">
                                              <c:out value="${worker}"/><br>
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
                            <c:if test="${'SUCCESS' eq apptInspectionDateDto.actionButtonFlag}">
                              <iais:row>
                                <iais:field value="Available Appointment Dates"/>
                              </iais:row>
                              <iais:row>
                                <iais:value width="7">
                                  <c:if test="${apptInspectionDateDto.inspectionDate != null}">
                                    <ul>
                                    <c:forEach items="${apptInspectionDateDto.inspectionDate}" var="inspectionDate">
                                      <li><label><c:out value="${inspectionDate}"/></label></li>
                                    </c:forEach>
                                    </ul>
                                  </c:if>
                                </iais:value>
                              </iais:row>
                              <iais:action >
                                <button class="btn btn-lg btn-login-submit" style="float:right" type="button" onclick="javascript:apptInspectionDateSpecific()">Assign Specific Date</button>
                                <span style="float:right">&nbsp;</span>
                                <button class="btn btn-lg btn-login-submit" style="float:right" type="button" onclick="javascript:apptInspectionDateConfirm()">Allow System to Propose Dates</button>
                              </iais:action>
                            </c:if>
                            <br>
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
<script type="text/javascript">
  function apptInspectionDateSubmit(action){
    $("[name='apptInspectionDateType']").val(action);
    var mainPoolForm = document.getElementById('mainInspDateForm');
    mainPoolForm.submit();
  }

  function apptInspectionDateConfirm() {
      $("#actionValue").val('success');
      $("#processDec").val('REDECI017');
      apptInspectionDateSubmit("success");
  }

  function apptInspectionDateSpecific() {
      $("#actionValue").val('confirm');
      $("#processDec").val('REDECI018');
      apptInspectionDateSubmit("confirm");
  }
</script>


