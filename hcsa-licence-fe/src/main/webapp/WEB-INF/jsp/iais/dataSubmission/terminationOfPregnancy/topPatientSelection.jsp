<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
    String continueURL = "";
    if (process != null && process.runtime != null && process.runtime.getBaseProcessClass() != null) {
        continueURL = process.runtime.continueURL();
    }
%>
<webui:setLayout name="iais-internet"/>

<%@ include file="common/topPiHeader.jsp" %>

<%--<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/cycleStageSelection.js"></script>--%>

<form method="post" id="mainForm" action=<%=continueURL%>>
    <div class="main-content">
        <div class="container center-content">
            <div class="col-xs-12">
                <h3>Please key in a valid patient ID No. to continue</h3>
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <%@include file="section/topPatientSelectionSection.jsp" %>
                </div>
                <%@include file="common/topPiFooter.jsp" %>
            </div>
        </div>
    </div>
</form>
<iais:confirm msg="DS_MSG003" callBack="$('#noFoundDiv').modal('hide');" popupOrder="noFoundDiv"  yesBtnDesc="Close"
              cancelBtnDesc="Register Patient" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary"
              cancelFunc="submit('patient', 'patient');" needFungDuoJi="false"/>
<iais:confirm msg="DS_MSG007" callBack="$('#validatePT').modal('hide');" popupOrder="validatePT" yesBtnDesc="Close"
              yesBtnCls="btn btn-secondary" needCancel="false" needFungDuoJi="false"/>
<input type="hidden" id="showValidatePT" name="showValidatePT" value="${showValidatePT}"/>