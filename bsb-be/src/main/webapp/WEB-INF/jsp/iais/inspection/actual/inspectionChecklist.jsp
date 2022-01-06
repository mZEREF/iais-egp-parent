<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>1
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-inspection.js"></script>

<div class="dashboard">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="action_type" value="">

        <div class="main-content">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="center-content">
                        <div class="intranet-content">
                            <div class="bg-title">
                                <h2>
                                    <span>Inspection Checklists</span>
                                </h2>
                            </div>
                            <span class="error-msg"><c:out value="${error_message}"/></span>
                            <%@include file="../../chklst/checkListAnswer.jsp"%>
                        </div>
                    </div>
                    <div>
                        <div class="alignctr" style="text-align: left">
                            <a id="back" href="javascript:void(0)"><em class="fa fa-angle-left"> </em> Back</a>
                        </div>
                        <div class="text-right text-center-mobile">
                            <a class="btn btn-secondary" id="clear" href="javascript:void(0);">Clear</a>
                            <a class="btn btn-primary" id="save" href="javascript:void(0);">Submit</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>