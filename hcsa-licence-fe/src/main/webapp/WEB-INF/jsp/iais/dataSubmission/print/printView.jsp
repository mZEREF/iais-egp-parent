<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
    String continueURL = "";
    if (process != null && process.runtime != null && process.runtime.getBaseProcessClass() != null) {
        continueURL = process.runtime.continueURL();
    }
    String webrootPrint = IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT;
%>

<webui:setLayout name="iais-blank"/>

<script type="text/javascript" src="<%=webrootPrint%>js/dataSubmission/ds_print.js"></script>

<style>
    .form-check input.form-check-input:checked + .form-check-label span.check-circle:before,
    .form-check input.form-check-input:active + .form-check-label span.check-circle:before {
        color: #147aab !important;
        background-color: #FFF;
        content: "\f111";
        font-family: FontAwesome, sans-serif;
        position: absolute;
        font-size: 12px;
        top: 38%;
        left: 48%;
    }

    .form-group {
        max-width: 100%;
        position: relative;
        width: 100%;
        padding-right: 10px;
        padding-left: 10px;
        clear:both;
    }
    @media all and (min-width: 429px) {
        label.control-label {
            width: 40% !important;
        }
        div.col-sm-7.col-md-5.col-xs-7 {
            width: 60% !important;
        }
    }
</style>

<form method="post" id="mainForm" action=<%=continueURL%>>
    <div class="main-content">
        <div class="container center-content">
            <div class="col-xs-12">
                <div class="row">
                    <div class="col-xs-12 col-md-10">
                        <%--<h3>Preview</h3>--%>
                    </div>
                </div>
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <c:choose>
                    <c:when test="${printflag == 'PTART'}">
                        <%@include file="/WEB-INF/jsp/iais/dataSubmission/print/printPatientAR.jsp" %>
                    </c:when>
                    <c:when test="${printflag == 'ART'}">
                        <jsp:include page="/WEB-INF/jsp/iais/dataSubmission/print/printAR.jsp" />
                    </c:when>
                    </c:choose>

                </div>
            </div>
        </div>
    </div>
</form>