<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<webui:setLayout name="iais-intranet"/>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-adhoc-checklist.js"></script>

<div class="main-content">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <br><br><br>

        <span id="error_customItemError" name="iaisErrorMsg" class="error-msg"></span>
        <br><br>
        <div class="tab-pane active" id="tabInbox" role="tabpanel">
            <div class="form-horizontal">

                <div class="form-group">
                    <div class="col-xs-12">
                        <iais:field value="Checklist Item" required="true"/>
                        <div class="col-xs-5 col-md-3">
                            <input type="text" name="checklistItem" value="" maxlength="500"/>
                            <span id="error_question" name="iaisErrorMsg" class="error-msg"></span>
                        </div>
                    </div>
                </div>

            </div>
        </div>
        <div class="col-xs-12 col-sm-6">
            <a id="cancelBtn" style="color: #337ab7"><em class="fa fa-angle-left"></em>Previous</a>
        </div>
        <div class="text-right text-center-mobile">
            <a id="customBtn" class="btn btn-primary">Custom</a>
        </div>

</div>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>