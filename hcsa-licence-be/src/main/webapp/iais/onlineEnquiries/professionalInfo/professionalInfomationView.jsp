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
<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="prRegNo" value="">
  <%@ include file="/include/formHidden.jsp" %>
  <div class="bg-title"><h2>Professional information</h2></div>
  <div class="main-content">


    <span id="error_checkBoxError" name="iaisErrorMsg" class="error-msg"></span>


    <iais:section title="" id = "demoList">

      <table class="table">
        <tbody>
        <tr>
          <td>
            <iais:field  value="Name.(Professional, CGO, PO & etc)"/>
            <iais:value width="18">
              <input type="text" name="name" value="${name}" />
              <span id="error_name" name="iaisErrorMsg" class="error-msg"></span>
            </iais:value>
          </td>

          <td>
            <iais:field value="Professional RgnNo(MCR,DCR)"/>
            <iais:value width="18">
              <input type="text" name="profRegNo" value="${profRegNo}" />
              <span id="error_profRegNo" name="iaisErrorMsg" class="error-msg"></span>
            </iais:value>
          </td>

        </tr>


        <tr>
          <td>
            <iais:field  value="Postal Code"/>
            <iais:value width="18">
              <input type="text" name="postalCode" value="${postalCode}" />
              <span id="error_postalCode" name="iaisErrorMsg" class="error-msg"></span>
            </iais:value>
          </td>

          <td>
            <iais:field value="Address"/>
            <iais:value width="18">
              <input type="text" name="address" value="${address}" />
              <span id="error_address" name="iaisErrorMsg" class="error-msg"></span>
            </iais:value>
          </td>

        </tr>

        <tr>
          <td>
            <iais:field  value="Hci Name"/>
            <iais:value width="18">
              <input type="text" name="hciName" value="${hciName}" />
              <span id="error_hciName" name="iaisErrorMsg" class="error-msg"></span>
            </iais:value>
          </td>

          <td>
            <iais:field value="Hci Code"/>
            <iais:value width="18">
              <input type="text" name="hciCode" value="${hciCode}" />
              <span id="error_hciCode" name="iaisErrorMsg" class="error-msg"></span>
            </iais:value>
          </td>

        </tr>

        <tr>
          <td>
            <iais:field  value="Hci Postalode"/>
            <iais:value width="18">
              <input type="text" name="hciPostalcode" value="${hciPostalcode}" />
              <span id="error_hciPostalcode" name="iaisErrorMsg" class="error-msg"></span>
            </iais:value>
          </td>

          <td>
            <iais:field value="Hci Address"/>
            <iais:value width="18">
              <input type="text" name="practiceLocation" value="${practiceLocation}" />
              <span id="error_practiceLocation" name="iaisErrorMsg" class="error-msg"></span>
            </iais:value>
          </td>

        </tr>

        <tr>
          <td>
            <iais:field  value="Service Name"/>
            <iais:value width="18">
              <iais:select name="serviceName" id="serviceName" options = "svcNameSelect" firstOption="Please Select" value="${serviceName}"></iais:select>
              <span id="error_serviceName" name="iaisErrorMsg" class="error-msg"></span>
            </iais:value>
          </td>

          <td>
            <iais:field value="Designation Name"/>
            <iais:value width="18">
              <iais:select name="designation" id="designation" codeCategory="CATE_ID_DESIGNATION" firstOption="Please Select" value="${designation}"></iais:select>
              <span id="error_designation" name="iaisErrorMsg" class="error-msg"></span>
            </iais:value>
          </td>

        </tr>

        <tr>
          <td>
            <iais:field  value="Service Personnal Role"/>
            <iais:value width="18">
              <iais:select name="role" id="role"  codeCategory="CATE_ID_DESIGNATION" firstOption="Please Select"
                           value="${role}"></iais:select>
              <span id="error_role" name="iaisErrorMsg" class="error-msg"></span>
            </iais:value>
          </td>

        </tr>

        </tbody>
      </table>


      <iais:action style="text-align:center;">
        <button class="btn btn-lg btn-login-search" type="button" value="doSearch" style="background:#2199E8; color: white ">Search</button>
        <a class="btn btn-lg btn-login-export" type="button" onclick="Utils.doExport('${pageContext.request.contextPath}/professional-information-file')" style="background:#2199E8; color: white" >Export</a>
        <button class="btn btn-lg btn-login-clear" type="button" style="background:#2199E8; color: white">Clear</button>
      </iais:action>
    </iais:section>


    <br><br>
    <div>
      <div class="tab-pane active" id="tabInbox" role="tabpanel">
        <div class="tab-content">
          <div class="row">
            <br><br>
            <div class="col-xs-12">
              <div class="components">
                <iais:pagination  param="professionalInfoSearch" result="professionalInfoResult"/>
                <div class="table-gp">
                  <table class="table">
                    <thead>
                    <tr>
                      <td></td>
                      <iais:sortableHeader needSort="false"   field="index" value="S/N"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="name" value="Name"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="prof_reg_no" value="Professional Registration No"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="practive_location" value="Practive_Location"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="licence_no" value="Active Licence"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="service_name" value="Service"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="designation" value="Designation Name"></iais:sortableHeader>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                      <c:when test="${empty professionalInfoResult.rows}">
                        <tr>
                          <td colspan="6">
                            No Record!!
                          </td>
                        </tr>
                      </c:when>
                      <c:otherwise>
                        <c:forEach var="info" items="${professionalInfoResult.rows}" varStatus="status">
                          <tr>
                            <td></td>
                            <td>${status.index + 1}</td>
                            <td>${info.name}</td>
                            <td><a onclick="viewPfDetails('<iais:mask name="prRegNo" value="${info.profRegNo}"/>')" >${info.profRegNo}</a> </td>
                            <td>${info.practiceLocation}</td>
                            <td>${info.licence}</td>
                            <td>${info.serviceName}</td>
                            <td><iais:code code="${info.designation}"></iais:code></td>
                            <td>
                          </tr>
                        </c:forEach>


                      </c:otherwise>
                    </c:choose>
                    </tbody>
                  </table>
                  <div class="table-footnote">
                    <div class="row">
                      <div class="col-xs-6 col-md-8 text-right">
                        <br><br>


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



  </div>



</form>

<%@include file="/include/validation.jsp"%>
<script src="<%=webRootCommon%>js/iaisUtils.js"></script>

<script>
  function viewPfDetails(val) {
      SOP.Crud.cfxSubmit("mainForm", "viewPfDetails");
  }

  
</script>