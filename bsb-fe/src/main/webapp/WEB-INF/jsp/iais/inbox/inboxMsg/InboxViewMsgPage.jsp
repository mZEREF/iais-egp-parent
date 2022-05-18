<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-inbox.js"></script>


<%@include file="../dashboard/dashboard.jsp"%>


<div class="main-content">
  <div class="container">
    <div class="row">
      <div class="col-xs-12">
        <div class="tab-gp dashboard-tab" style="margin-left: 6px;margin-right: -8px;">
          <%@ include file="../InnerNavBar.jsp"%>

          <div class="tab-content">
            <div class="panel panel-info">
              <h3 class="panel-heading">Message Content</h3>
              <div class="panel-body">
                <c:out value="${MSG_CONTENT}" escapeXml="false"/>
              </div>
            </div>
            <div class="row" style="margin-top: 1.5%">
              <div class="col-md-12">
                <div class="col-md-2">
                  <a href="/bsb-web/eservice/INTERNET/MohBSBInboxMsg"><em class="fa fa-angle-left"></em> Back</a>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>