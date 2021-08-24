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
<%--<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-enquiry.js"></script>--%>
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
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="prRegNo" value="">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input type="hidden" name="searchChk" id="searchChk" value="${count}"/>
    <div class="bg-title"><h2>Details of Application NO</h2></div>
    <div class="container">

      <div class="row">
        <div class="col-xs-10" style="height: 100%;margin: 0 auto">
          <div class="table-gp">
            <table class="table table-bordered">
              <tbody>
              <tr>
                <td class="col-xs-6" align="right">S/N</td>
                <td class="col-xs-6" style="padding-left: 15px;">1</td>
              </tr>
              <tr>
                <td align="right">Application No</td>
                <td style="padding-left: 15px;">EDQHD312Q</td>
              </tr>
              <tr>
                <td align="right">Application Type</td>
                <td style="padding-left: 15px;">Renewal</td>
              </tr>
              <tr>
                <td align="right">Application Submission Date </td>
                <td style="padding-left: 15px;">06/17/2021</td>
              </tr>
              <tr>
                <td align="right">Approval Date</td>
                <td style="padding-left: 15px;">06/13/2021</td>
              </tr>
              <tr>
                <td align="right">Facility Classification</td>
                <td style="padding-left: 15px;">Certified Facility</td>
              </tr>
              <tr>
                <td align="right">Facility Type</td>
                <td style="padding-left: 15px;">Certified Facility (HCF)</td>
              </tr>
              <tr>
                <td align="right">Facility Name</td>
                <td style="padding-left: 15px;">BBC</td>
              </tr>
              <tr>
                <td align="right">Biological Agent/Toxin</td>
                <td style="padding-left: 15px;">List of BA/T</td>
              </tr>
              <tr>
                <td align="right">Risk Level of the Biological Agent/Toxin</td>
                <td style="padding-left: 15px;">Enhanced risk</td>
              </tr>
              <tr>
                <td align="right">Process Type</td>
                <td style="padding-left: 15px;">Facility Registration</td>
              </tr>
              <tr>
                <td align="right">Verified By DO (Date time)</td>
                <td style="padding-left: 15px;">06/18/2021</td>
              </tr>
              <tr>
                <td align="right">Verified By AO (Date time)</td>
                <td style="padding-left: 15px;">06/19/2021</td>
              </tr>
              <tr>
                <td align="right">Verified By AO (Date time)</td>
                <td style="padding-left: 15px;">06/20/2021</td>
              </tr>
              <tr>
                <td align="right">Action</td>
                <td style="padding-left: 15px;">Revoke</td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
      <a onclick="javascript:doBack()"><em
              class="fa fa-angle-left"> </em> Back</a>
    </div>
  </form>
</div>
<script>
  function doBack() {
    SOP.Crud.cfxSubmit('mainForm', 'back');
  }
</script>
