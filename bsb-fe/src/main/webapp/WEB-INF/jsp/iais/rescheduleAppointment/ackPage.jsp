<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@include file="dashboard.jsp"%>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <div class="main-content">
        <br><br><br>

        <div class="container">
            <div class="col-xs-12">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <br><br><br><br>
                    <div class="bg-title">
                        <h2>${ackMsg}</h2>
                    </div>
                    <iais:section title="" id = "supPoolList">
                        <iais:action style="text-align:right;">
                            <a class="btn btn-secondary" type="button" href="${backUrl}" >DONE</a>
                        </iais:action>
                    </iais:section>
                </div>
            </div>
        </div>
    </div>
</form>
