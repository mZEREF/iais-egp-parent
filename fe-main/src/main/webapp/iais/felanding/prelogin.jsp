<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 2/28/2020
  Time: 10:42 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.Formatter" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java"%>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<div class="main-content">
  <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <div class="prelogin" style="background-image: url('/web/themes/fe/img/prelogin-masthead-banner.jpg');">
      <div class="container">
        <div class="row">
          <div class="col-xs-12">
            <div class="prelogin-title">
              <h1>Integrated Application and <br class="hidden-xs"> Inspection System (IAIS)</h1>
              <p class="component-desc">Manage all licence-related matters associated with your healthcare services.</p>
            </div>
            <div class="prelogin-content">
              <div class="white-content-box login-IAIS" style="height: 274px;">
                <h3>Login to IAIS</h3>
                <div class="left-content">
                  <ul>
                    <li>
                      <p>Apply for a new licence</p>
                    </li>
                    <li>
                      <p>Check the status of your applications</p>
                    </li>
                    <li>
                      <p>Manage your existing licences</p>
                    </li>
                    <li>
                      <p>Manage your account profile</p>
                    </li>
                    <li>
                      <p>View messages &amp; notifications from MOH</p>
                    </li>
                  </ul>
                </div>
                <div class="right-content login-btns"><a class="btn btn-primary" href="javascript:void(0)" onclick="Utils.submit('mainForm','croppassLogin')">LOGIN USING CorpPass</a>
                  <p class="text-center"><a href="javascript:void(0)" onclick="Utils.submit('mainForm','registry')">Don't have a CorpPass?</a></p>
                </div>
              </div>
              <div class="white-content-box hcsa" style="height: 274px;">
                <h3>Healthcare Services Act (HCSA)</h3>
                <ul>
                  <li>
                    <p><a href="#">About HCSA</a></p>
                  </li>
                  <li>
                    <p><a href="#">Bill phases &amp; timelines</a></p>
                  </li>
                  <li>
                    <p><a href="#">Services under IAIS today</a></p>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </form>
</div>

<%@include file="/include/utils.jsp"%>