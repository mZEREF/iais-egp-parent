<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="main-content">
        <br><br><br>

        <div class="container">
            <div class="col-xs-12">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <br><br><br><br>
                    <div class="bg-title">
                        <h2>
                            Your appointment is being re-assigned. You will be alerted to select a date soon.
                        </h2>
                    </div>
                    <iais:section title="" id = "supPoolList">

                        <iais:action style="text-align:right;">
                            <a class="btn btn-secondary" type="button"   href="/hcsa-licence-web/eservice/INTERNET/MohClientRescheduling" >OK</a>
                        </iais:action>
                    </iais:section>
                </div>
            </div>
        </div>
    </div>
</form>
