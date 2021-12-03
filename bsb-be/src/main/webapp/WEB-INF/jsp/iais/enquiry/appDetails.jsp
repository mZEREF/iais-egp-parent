<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %><%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 2/13/2020
  Time: 5:59 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-enquiry.js"></script>
<webui:setLayout name="iais-intranet"/>

<%
  String webroot= IaisEGPConstant.CSS_ROOT+ IaisEGPConstant.FE_CSS_ROOT;
  String webRootCommon = IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT;
%>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<div class="main-content">
  <form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input type="hidden" name="searchChk" value="${count}">
    <div class="container">
      <div class="row" style="margin-left: 80px;width: 800px">
      <div class="panel panel-default">
        <div class="panel-heading"><strong>Submission Details</strong></div>
        <div class="row">
          <div class="col-xs-12">
            <div class="table-gp">
              <table aria-describedby="" class="table table-bordered" style="margin-bottom: 0">
                <tbody>
                <tr>
                  <th scope="col"></th>
                </tr>
                <tr>
                  <td class="col-xs-6" align="right">Application No.</td>
                  <td class="col-xs-6" style="padding-left : 20px">${applicationInfo.applicationNo}</td>
                </tr>
                <tr>
                  <td align="right">Application Type</td>
                  <td style="padding-left : 20px"><iais:code code="${applicationInfo.appType}"></iais:code></td>
                </tr>
                <tr>
                  <td align="right">Process Type</td>
                  <td style="padding-left : 20px"><iais:code code="${applicationInfo.processType}"></iais:code></td>
                </tr>
                <tr>
                  <td align="right">Facility Type</td>
                  <td style="padding-left : 20px"><iais:code code="${applicationInfo.facility.activeType}"></iais:code></td>
                </tr>
                <tr>
                  <td align="right">Facility Name/Address</td>
                  <td style="padding-left : 20px">${applicationInfo.facility.facilityName} / Block ${applicationInfo.facility.blkNo} ${applicationInfo.facility.streetName} ${applicationInfo.facility.floorNo}-${applicationInfo.facility.unitNo} Singapore ${applicationInfo.facility.postalCode}</td>
                </tr>
                <tr>
                  <td align="right">Submission Date</td>
                  <td style="padding-left : 20px"><fmt:formatDate value='${applicationInfo.applicationDt}' pattern='dd/MM/yyyy'/></td>
                </tr>
                <tr>
                  <td align="right">Application Status</td>
                  <td style="padding-left : 20px"><iais:code code="${applicationInfo.status}"></iais:code></td>
                </tr>
                <tr>
                  <td align="right">Facility/Approval Expiry Date</td>
                  <td style="padding-left : 20px"><fmt:formatDate value='${applicationInfo.facility.expiryDt}' pattern='dd/MM/yyyy'/></td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
      <div>&nbsp</div>
      <div class="panel panel-default">
        <div class="panel-heading"><strong>Applicant Details</strong></div>
        <div class="row">
          <div class="col-xs-12">
            <div class="table-gp">
              <table aria-describedby="" class="table table-bordered" style="margin-bottom: 0">
                <tbody>
                <tr>
                  <th scope="col"></th>
                </tr>
                <tr>
                  <td class="col-xs-6" align="right">Facility/Organisation Name</td>
                  <td class="col-xs-6" style="padding-left : 20px">ABC</td>
                </tr>
                <tr>
                  <td align="right">Facility/Organisation Address</td>
                  <td style="padding-left : 20px">Lot 10,Tao Payoh,Jalan 1,106780,Singapore</td>
                </tr>
                <tr>
                  <td align="right">Facility/Organisation Admin</td>
                  <td style="padding-left : 20px">Mr Admin</td>
                </tr>
                <tr>
                  <td align="right">Telephone</td>
                  <td style="padding-left : 20px">64825525</td>
                </tr>
                <tr>
                  <td align="right">Email</td>
                  <td style="padding-left : 20px">Facility@yahoo.com</td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
<%--      <div align="center">--%>
<%--        <a href="javascript:void(0);" onclick="javascript:doOpenApp()">--%>
<%--          <button type="button" class="btn btn-primary">--%>
<%--            View Application--%>
<%--          </button>--%>
<%--        </a>--%>
<%--      </div>--%>
      <div>&nbsp</div>
      <div class="panel panel-default">
        <div class="panel-heading"><strong>List of Agent / Toxin</strong></div>
        <div class="row">
          <div class="col-xs-12">
            <div class="table-gp">
              <table aria-describedby="" class="table table-bordered" style="margin-bottom: 0">
                <thead>
                <tr>
                  <th scope="col" style="text-align:center;width:5%" align="center">S/N</th>
                  <th scope="col" style="text-align:center;">Schedule</th>
                  <th scope="col" style="text-align:center;">Biological Agent / Toxin</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="item" items="${applicationInfo.biologicalList}" varStatus="status">
                  <tr>
                    <td align="center">
                      <p><c:out value="${status.index + 1}"/></p>
                    </td>
                    <td align="center">
                      <p><iais:code code="${item.schedule}"></iais:code></p>
                    </td>
                    <td align="center">
                      <p><c:out value="${item.name}"/></p>
                    </td>
                  </tr>
                </c:forEach>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
      <div align="left">
        <a style="float:left;padding-top: 1.1%;" class="back" id="back" onclick="javascript:doBack()"><em class="fa fa-angle-left"></em> Back</a>
      </div>
    </div>
  </form>
</div>
</div>