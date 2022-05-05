<%@tag description="Company info tag of facility registration" pageEncoding="UTF-8" %>
<%@taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@attribute name="organizationAddress" required="true" type="sg.gov.moh.iais.egp.bsb.dto.info.common.OrgAddressInfo" %>
<%@attribute name="specialJsFrag" fragment="true" %>
<%@attribute name="dashboardFrag" fragment="true" %>
<%@attribute name="innerFooterFrag" fragment="true" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

<webui:setLayout name="iais-internet"/>

<link href="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common-node-group.js"></script>
<jsp:invoke fragment="specialJsFrag"/>

<jsp:invoke fragment="dashboardFrag"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">

    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@include file="/WEB-INF/jsp/iais/mainAppCommon/facRegistration/InnerNavTab.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div id="compInfoPanel" role="tabpanel">
                                    <div class="form-horizontal">
                                        <h3 class="col-12 pl-0" style="border-bottom: 1px solid black">Company Profile</h3>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>UEN</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <label>${organizationAddress.uen}</label>
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Company Name</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <label>${organizationAddress.compName}</label>
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Postal Code</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <label>${organizationAddress.postalCode}</label>
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Address Type</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <label><iais:code code="${organizationAddress.addressType}"/></label>
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Block / House No</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <label>${organizationAddress.blockNo}</label>
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Floor No.</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <label>${organizationAddress.floor}</label>
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Unit No.</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <label>${organizationAddress.unitNo}</label>
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Street Name</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <label>${organizationAddress.street}</label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <jsp:invoke fragment="innerFooterFrag"/>
                            </div>
                        </div>
                        <%@include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp" %>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>