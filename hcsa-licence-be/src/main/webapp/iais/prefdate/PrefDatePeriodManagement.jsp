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
  <div class="main-content">


    <span id="error_numberError" name="iaisErrorMsg" class="error-msg"></span>



    <br><br>
    <iais:section title="" id = "demoList">
      <iais:row>
      <iais:field value="Service Name"/>
      <iais:value width="18">
        <iais:select name="svcName" firstOption="Please Select" options="svcNameSelect"
                     value="${svcName}"></iais:select>
        <span id="error_svcName" name="iaisErrorMsg" class="error-msg"></span>
      </iais:value>
      </iais:row>

      <iais:row>
      <iais:field value="Block-out Period after Application "/> wks
        <iais:value width="18">
          <input type="text" name="periodAfterApp" value="${periodAfterApp}">
        </iais:value>
      </iais:row>

      <iais:row>
       <iais:field value="Block-out Period before Expiry"/> wks
        <iais:value width="18">
          <input type="text" name="periodBeforeExp" value="${periodBeforeExp}">
        </iais:value>
      </iais:row>

      <iais:row>
      <iais:field value="Non-reply Notification Window"/> working day
      <iais:value width="18">
        <input type="text" name="nonReplyWindow" value="${nonReplyWindow}">
      </iais:value>
      </iais:row>



      <iais:action style="text-align:center;">
        <button class="btn btn-lg btn-login-search" type="button" value="doSearch" style="background:#2199E8; color: white ">Search</button>
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
                <iais:pagination  param="prefPeriodSearch" result="prefPeriodResult"/>
                <div class="table-gp">
                  <table class="table">
                    <thead>
                    <tr>
                      <td></td>
                      <iais:sortableHeader needSort="false"   field="index" value="S/N"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="SVC_NAME" value="Service Name"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="SVC_TYPE" value="Service Type"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="PERIOD_AFTER_APP" value="Block-out Period after Application"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="PERIOD_BEFORE_EXP" value="Block-out Period before Expiry"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="NON_REPLY_WINDOW" value="Non-reply Notification Window"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false"   field="action" value="Action"></iais:sortableHeader>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                      <c:when test="${empty prefPeriodResult.rows}">
                        <tr>
                          <td colspan="6">
                            No Record!!
                          </td>
                        </tr>
                      </c:when>
                      <c:otherwise>
                        <c:forEach var="prefItem" items="${prefPeriodResult.rows}" varStatus="status">
                          <tr>
                            <td></td>
                            <td>${status.index + 1}</td>

                            <td>${prefItem.svcName}</td>

                            <td><iais:code code="${prefItem.svcType}"></iais:code></td>
                            <td>${prefItem.periodAfterApp}</td>
                            <td>${prefItem.periodBeforeExp}</td>
                            <td>${prefItem.nonReplyWindow}</td>
                            <td><a class="btn btn-secondary" id="prefItemId" name="prefItemId"
                                   onclick="preUpdateData('${prefItem.id}')">Update</a></td>
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
    function preUpdateData(id) {
        SOP.Crud.cfxSubmit("mainForm", "preUpdateData", id);
    }
</script>