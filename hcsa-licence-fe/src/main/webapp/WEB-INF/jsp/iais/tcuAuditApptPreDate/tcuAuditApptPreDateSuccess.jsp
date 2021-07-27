<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2021/7/27
  Time: 9:45
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>

<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@include file="../common/dashboard.jsp"%>
<div class="container" style="margin-left: 320px;">
  <div class="component-gp">
    <br>
    <form method="post" id="tcuAuditApptForm" action=<%=process.runtime.continueURL()%>>
      <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
      <input type="hidden" name="feTcuAuditApptPreDateType" value="">
      <input type="hidden" id="actionValue" name="actionValue" value="">
      <div class="main-content">
        <div class="row">
          <div class="col-lg-12 col-xs-12">
            <div class="intranet-content">
              <iais:section title="" id = "tcu_audit_appt">
                <div class="bg-title">
                  <h3 style="border-bottom: 0px solid">Acknowledgement</h3>
                </div>
                <iais:row>
                  <iais:value width="7">
                    <p><label><iais:message key="LOLEV_ACK045" escape="true"></iais:message></label></p>
                  </iais:value>
                </iais:row>
                <iais:action >
                  <a class="btn btn-primary" style="float:right" href="/main-web/eservice/INTERNET/MohInternetInbox" >Go to Dashboard</a>
                </iais:action>
              </iais:section>
            </div>
          </div>
        </div>
      </div>
    </form>
  </div>
</div>