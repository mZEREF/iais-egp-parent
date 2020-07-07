<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %><%--
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
    <input type="hidden" name="prRegNo" value="">
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <div class="bg-title"><h2>Professional Details</h2></div>
    <div class="container">


      <div class="row">
        <div class="col-xs-12">
          <div class="tab-gp dashboard-tab">

            <div class="tab-content ">

              <div class="panel panel-default">
                <div class="panel-heading"><strong><iais:code code="${empty professionalInfo.licKeyPersonnelDto.psnType ? '-' : professionalInfo.licKeyPersonnelDto.psnType}"></iais:code></strong></div>
                <div class="row">
                  <div class="col-xs-12">
                    <div class="table-gp">
                      <table class="table table-bordered">
                        <tbody>
                        <tr>
                          <td class="col-xs-6" align="right">Name</td>
                          <td class="col-xs-6">&nbsp;<c:out value="${empty professionalInfo.keyPersonnelDto.name ? '-' : professionalInfo.keyPersonnelDto.name}"></c:out></td>
                        </tr>
                        <tr>
                          <td align="right">Salutation</td>
                          <td>&nbsp; <iais:code code="${empty professionalInfo.keyPersonnelDto.salutation ? '-' : professionalInfo.keyPersonnelDto.salutation}"></iais:code></td>
                        </tr>
                        <tr>
                          <td align="right">ID Type</td>
                          <td>&nbsp;<c:out value="${empty professionalInfo.keyPersonnelDto.idType ? '-' : professionalInfo.keyPersonnelDto.idType}"></c:out></td>
                        </tr>

                        <tr>
                          <td align="right">ID No</td>
                          <td>&nbsp;<c:out value="${empty professionalInfo.keyPersonnelDto.idNo ? '-' : professionalInfo.keyPersonnelDto.idNo}"></c:out></td>
                        </tr><tr>
                          <td align="right">Designation</td>
                          <td>&nbsp;<iais:code code="${empty professionalInfo.keyPersonnelDto.idNo ? '-' : professionalInfo.keyPersonnelDto.designation}"></iais:code></td>
                        </tr>
                        <tr>
                          <td align="right">Mobile No</td>
                          <td>&nbsp;<c:out value="${empty professionalInfo.keyPersonnelDto.mobileNo ? '-' : professionalInfo.keyPersonnelDto.mobileNo}"></c:out></td>
                        </tr>
                        <tr>
                          <td align="right">Email Address</td>
                          <td>&nbsp;<c:out value="${empty professionalInfo.keyPersonnelDto.emailAddr ? '-' : professionalInfo.keyPersonnelDto.emailAddr}"></c:out></td>
                        </tr>


                        <c:if test="${professionalInfo.licKeyPersonnelDto.psnType eq 'CGO'}">
                          <tr>
                            <td align="right">Professional Registration No</td>
                            <td>&nbsp;<c:out value="${empty professionalInfo.keyPersonnelExtDto.profRegNo ? '-' : professionalInfo.keyPersonnelExtDto.profRegNo}"></c:out></td>
                          </tr>

                          <tr>
                            <td align="right">Specialty</td>
                            <td>&nbsp;<c:out value="${empty professionalInfo.keyPersonnelExtDto.speciality ? '-' : professionalInfo.keyPersonnelExtDto.speciality}"></c:out></td>
                          </tr>

                          <tr>
                            <td align="right">Subspecialty and relevant qualification</td>
                            <td>&nbsp;<c:out value="${empty professionalInfo.keyPersonnelExtDto.subSpeciality ? '-' : professionalInfo.keyPersonnelExtDto.subSpeciality}"></c:out></td>
                          </tr>

                        </c:if>


                        <c:if test="${professionalInfo.licKeyPersonnelDto.psnType eq 'MAP'}">
                          <tr>
                            <td align="right">Preferred Mode of Receiving MedAlert</td>
                            <td>&nbsp;<c:out value="${empty professionalInfo.keyPersonnelExtDto.preferredMode ? '-' : professionalInfo.keyPersonnelExtDto.preferredMode}"></c:out></td>
                          </tr>
                        </c:if>

                        </tbody>
                      </table>
                    </div>
                  </div>
                </div>
              </div>



            </div>






          </div>
        </div>


        <iais:action style="text-align:left;">
          <a class="back" onclick="doCancel()();"><em class="fa fa-angle-left"></em> Back</a>
        </iais:action>

      </div>

    </div>
  </form>
</div>


<script type="text/javascript">
  function doCancel(){
    SOP.Crud.cfxSubmit("mainForm","doCancel");
  }
</script>