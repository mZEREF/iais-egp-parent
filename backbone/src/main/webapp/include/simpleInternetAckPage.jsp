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

<div class="main-content" >
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/include/formHidden.jsp" %>
        <div >
            <div class="container">
                <div class="row">
                    <div class="col-xs-12">
                        <br><br>
                        <div class="prelogin-title">
                            <div class="panel-heading"><h2><strong>Acknowledgement</strong></h2></div>
                        </div>

                        <p><c:out value="${ackMsg}"></c:out></p>

                    </div>
                </div>
            </div>

            <div class="application-tab-footer">
                <div class="row">
                    <div class="col-xs-12 col-sm-5">
                        <p></p>
                    </div>
                    <div class="col-xs-12 col-sm-6">
                        <div class="button-group">
                            <a class="btn btn-primary next" onclick="Utils.submit('mainForm', 'doCancel')">Done</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>

<br><br>

<%@include file="/include/utils.jsp"%>