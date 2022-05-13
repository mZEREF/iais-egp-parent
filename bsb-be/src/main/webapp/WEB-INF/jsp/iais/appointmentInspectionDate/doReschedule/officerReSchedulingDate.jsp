<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<%
  String webroot=IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-appointment.js"></script>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
  <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input type="hidden" name="mohOfficerReSchedulingType" value="">
    <input type="hidden" id="actionValue" name="actionValue" value="">
    <input type="hidden" id="processDec" name="processDec" value="">
    <div class="main-content">
      <div class="row">
        <div class="col-lg-12 col-xs-12">
          <div class="center-content">
            <div class="intranet-content">
              <iais:body >
                <%--@elvariable id="backUrl" type="java.lang.String"--%>
                <%--@elvariable id="officerRescheduleDto" type="sg.gov.moh.iais.egp.bsb.dto.appointment.doreschedule.OfficerRescheduleDto"--%>
                <iais:section title="">
                  <c:if test="${not empty officerRescheduleDto.availableDate}">
                    <div class="table-gp">
                      <table aria-describedby="" class="apptApp table">
                        <thead>
                        <tr >
                          <th scope="col" >Application No</th>
                          <th scope="col" >Application Status</th>
                          <th scope="col" >MOH Officer(s)</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:set var="appInfoShow" value="${officerRescheduleDto.apptAppInfoDto}"/>
                        <c:choose>
                          <c:when test="${empty appInfoShow}">
                            <tr>
                              <td colspan="7">
                                <iais:message key="GENERAL_ACK018" escape="true"/>
                              </td>
                            </tr>
                          </c:when>
                          <c:otherwise>
                              <tr>
                                <td><c:out value="${appInfoShow.appNo}"/></td>
                                <td><iais:code code="${appInfoShow.appStatus}"/></td>
                                <td>
                                  <c:if test="${appInfoShow.officers != null}">
                                    <c:forEach var="worker" items="${appInfoShow.officers}" varStatus="status">
                                      <c:out value="${worker}"/><br>
                                    </c:forEach>
                                  </c:if>
                                </td>
                              </tr>
                          </c:otherwise>
                        </c:choose>
                        </tbody>
                      </table>
                    </div>
                    <div id="apptThreeInspDate">
                      <div class="row" id = "apptDateTitle">
                        <div class="col-md-4">
                          <label style="font-size: 16px">Available Appointment Date</label>
                        </div>
                      </div>
                    </div>
                    <div class="row">
                      <div class="col-md-6">
                        <ul>
                          <c:forEach var="insepctionDate" items="${officerRescheduleDto.availableDate}">
                            <li class="apptInspScheduleUl"><span style="font-size: 16px"><c:out value="${insepctionDate}"/></span></li>
                          </c:forEach>
                        </ul>
                      </div>
                    </div>
                    <iais:action >
                      <a href="${backUrl}" class="back" id="Back" onclick="javascript:officerReSchedulingDateBack()" style="float:left"><em class="fa fa-angle-left"></em> Back</a>
                      <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:apptInspectionDateSpecific()">Find new Date</button>
                      <span style="float:right">&nbsp;</span>
                      <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:apptInspectionDateConfirm()">Confirm System-proposed Date</button>
                    </iais:action>
                  </c:if>
                  <%--
                    No Date, No Inspector Show
                  --%>
                  <c:if test="${empty officerRescheduleDto.availableDate}">
                    <div class="row">
                      <div class="col-md-6" style="font-size: 16px">
                        <iais:message key="OAPPT_ACK025" escape="true"/>
                      </div>
                    </div>
                    <br>
                    <br>
                    <br>
                    <br>
                    <br>
                    <br>
                    <br>
                    <br>
                    <br>
                    <iais:action>
                      <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:apptInspectionDateSpecific()">Find new Date</button>
                      <a href="${backUrl}" class="back" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
                    </iais:action>
                  </c:if>
                </iais:section>
              </iais:body>
            </div>
          </div>
        </div>
      </div>
    </div>
  </form>
</div>

