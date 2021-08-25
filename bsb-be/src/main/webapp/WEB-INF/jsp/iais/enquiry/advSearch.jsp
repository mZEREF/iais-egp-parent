<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-enquiry.js"></script>
<webui:setLayout name="iais-intranet"/>
<div class="main-content dashboard">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="action_type" value="">
        <input type="hidden" name="action_value" value="">
        <input type="hidden" name="action_additional" value="">
        <div class="center-content">
            <div class="intranet-content">
                <div class="bg-title col-xs-12 col-md-12">
                    <h2>Advanced Search Criteria</h2>
                </div>
                <div class="row">&nbsp;</div>
                <div class="panel-group " id="accordion" role="tablist" aria-multiselectable="true">
                    <div id="clearFilterForSearch" name="clearFilterForSearch">
                    <%@ include file="advancedFilter.jsp" %>
                    </div>
                    <div class="col-xs-12 col-md-12">
                        <iais:action style="text-align:right;">
                            <a style=" float:left;padding-top: 1.1%;text-decoration:none;" onclick="javascript:doBack()"><em class="fa fa-angle-left"> </em> Back</a>
                            <button class="btn btn-secondary" type="button"  onclick="javascript:doAdvClear()">Clear</button>
                            <button class="btn btn-primary" type="button"  onclick="javascript:doAdvSearch()">Search</button>
                        </iais:action>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>