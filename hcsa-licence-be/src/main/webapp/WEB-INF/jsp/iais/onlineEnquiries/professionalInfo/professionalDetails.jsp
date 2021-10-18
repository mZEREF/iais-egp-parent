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
    <div class="bg-title"><h2>Details of Registration</h2></div>
    <div class="container">


      <div class="row">
        <div class="col-xs-12">
          <div class="tab-gp dashboard-tab">

            <div class="tab-content ">

              <div class="panel panel-default">
                <div class="row">
                  <div class="col-xs-12">
                    <div class="table-gp">
                      <table aria-describedby="" class="table table-bordered">
                        <thead style="display: none">
                        <tr>
                          <th scope="col"></th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                          <td class="col-xs-6" align="right">Registration Expiry date</td>
                          <td class="col-xs-6">&nbsp;<c:out value="${empty professionalInfo.regExpDate ? '-' : professionalInfo.regExpDate}"></c:out></td>
                        </tr>
                        <tr>
                          <td align="right">Registration Accreditations</td>
                          <td>&nbsp;<iais:code code="${empty professionalInfo.regDit ? '-' : professionalInfo.regDit}"></iais:code></td>
                        </tr>
                        <tr>
                          <td align="right">Disciplinary Records</td>
                          <td>&nbsp;<c:out value="${empty professionalInfo.dpRecords ? '-' : professionalInfo.dpRecords}"></c:out></td>
                        </tr>
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
          <a href="#" class="back" onclick="doCancel()();"><em class="fa fa-angle-left"></em> Back</a>
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