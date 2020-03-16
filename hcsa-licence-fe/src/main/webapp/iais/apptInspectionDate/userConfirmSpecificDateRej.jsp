<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2020/2/17
  Time: 16:46
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

<div class="container">
  <div class="component-gp">
    <br>
    <form method="post" id="mainReConfirmForm" action=<%=process.runtime.continueURL()%>>
      <%@ include file="/include/formHidden.jsp" %>
      <br>
      <br>
      <br>
      <br>
      <br>
      <input type="hidden" name="userComfireInspDateType" value="">
      <input type="hidden" id="actionValue" name="actionValue" value="">
      <iais:body >
        <div class="container">
          <div class="col-xs-12">
            <div class="center-content">
              <div class="intranet-content">
                <div class="bg-title">
                  <h2>
                    <span>The appointment date has been reject.</span>
                  </h2>
                </div>
              </div>
              <div align="left"><span><a href="/main-web/eservice/INTERNET/MohInternetInbox">Back</a></span></div>
            </div>
          </div>
        </div>
      </iais:body>
    </form>
  </div>
</div>
