<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 9/17/2019
  Time: 3:26 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<webui:setLayout name="iais-intranet"/>


<div class="main-content">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="collapseFlag" value="${param.collapseFlag}">
        <input type="hidden" name="auditId" id="auditId"/>


        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="intranet-content">
                    <div class="bg-title">
                        <h2>Audit Trail View</h2>
                    </div>
                </div>

                <div class="subcontent col-lg-11 col-sm-12">
                    <%@ include file="maincontent.jsp"%>
                </div>
            </div>
        </div>
    </form>
</div>


