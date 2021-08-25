<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
        <div class="col-lg-12 col-xs-12">
            <div class="center-content">
                <div class="intranet-content">
                    <div class="row form-horizontal">
                        <div class="bg-title col-xs-12 col-md-12">
                            <h2>Advanced Search Criteria</h2>
                        </div>
                        <div class="form-group">
                            <div class="col-xs-12 col-md-12">
                                <div class="col-xs-12 col-md-12">
                                    <div class="components">
                                        <a class="btn btn-secondary" data-toggle="collapse"
                                           data-target="#searchCondition">Filter</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div id="searchCondition" class="collapse">
                            <div id="clearFilterForSearch" name="clearFilterForSearch">
                            <%@ include file="advancedFilter.jsp" %>
                            </div>
                            <div class="col-xs-12 col-md-12">
                                <iais:action style="text-align:right;">
                                    <a style=" float:left;padding-top: 1.1%;text-decoration:none;" onclick="javascript:doBack()"><em class="fa fa-angle-left"> </em> Back</a>
                                    <button class="btn btn-secondary" type="button"  onclick="javascript:doAdvClear()">Clear</button>
                                    <a class="btn btn-secondary" onclick="$(this).attr('class', 'btn btn-secondary disabled')" href="${pageContext.request.contextPath}/${download}">Export</a>
                                    <button class="btn btn-primary" type="button"  onclick="javascript:doAdvAfterSearch()">Search</button>
                                </iais:action>
                            </div>
                        </div>
                    </div>
                    <br>
                    <%@ include file="searchResults.jsp" %>
                </div>
            </div>
        </div>
    </form>
</div>

