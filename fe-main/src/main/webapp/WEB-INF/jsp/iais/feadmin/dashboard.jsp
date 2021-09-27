<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
  String webroot1=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot1%>img/Masthead-banner.jpg')">
  <div class="container">
    <div class="navigation-gp">
      <div class="row d-flex">
        <%@ include file="./dashboardDropDown.jsp" %>
        <div class="col-xs-12">
          <div class="dashboard-page-title">
            <h1>Create HCSA Internet User Account</h1>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
