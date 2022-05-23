<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>

<%@attribute name="organizationAddress" required="true" type="sg.gov.moh.iais.egp.bsb.dto.info.common.OrgAddressInfo" %>
<%@attribute name="specialJsFrag" fragment="true" %>
<%@attribute name="dashboardFrag" fragment="true" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<jsp:invoke fragment="specialJsFrag"/>

<jsp:invoke fragment="dashboardFrag"/>

<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <div class="container">
        <div class="component-gp col-xs-12 col-sm-11 col-md-10 col-lg-8">
            <br/>
            <div class="col-xs-12 col-sm-12" style="background-color: rgba(242, 242, 242, 1); padding: 20px 30px 10px 30px; border-radius: 15px;">
                <p class="assessment-title" style="border-bottom: 1px solid black; font-size:18px; padding-bottom: 10px; font-weight: bold">Company Info</p>
                <div>
                    <div class="form-group ">
                        <div class="col-sm-5 control-label">
                            <p>UEN Number:</p>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <p>${organizationAddress.uen}</p>
                        </div>
                    </div>
                    <div class="form-group ">
                        <div class="col-sm-5 control-label">
                            <p>Company Name:</p>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <p>${organizationAddress.compName}</p>
                        </div>
                    </div>
                    <div class="form-group ">
                        <div class="col-sm-5 control-label">
                            <p>Block:</p>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <p>${organizationAddress.blockNo}</p>
                        </div>
                    </div>
                    <div class="form-group ">
                        <div class="col-sm-5 control-label">
                            <p>Street Name:</p>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <p>${organizationAddress.street}</p>
                        </div>
                    </div>
                    <div class="form-group ">
                        <div class="col-sm-5 control-label">
                            <p>Floor and Unit No.:</p>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <p>${organizationAddress.floor} - ${organizationAddress.unitNo}</p>
                        </div>
                    </div>
                    <div class="form-group ">
                        <div class="col-sm-5 control-label">
                            <p>Postal Code:</p>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <p>${organizationAddress.postalCode}</p>
                        </div>
                    </div>
                </div>
            </div>
            <br/>
            <div class="col-xs-12 col-sm-12" style="padding-top: 30px">
                <p class="assessment-title" style="border-bottom: 1px solid black; font-size:18px; padding-bottom: 10px; font-weight: bold">Before You Begin</p>
                <div>
                    <ul>
                        <li>This form will take approximately 10 mins to complete. You may save your progress at any time and
                            resume your application later
                        </li>
                    </ul>
                </div>
            </div>
            <br/>
            <div class="row">
                <div class="col-xs-12 col-md-3">
                    <a class="back" id="back" href="#"><em class="fa fa-angle-left"></em> Previous</a>
                </div>
                <div class="col-xs-12 col-md-9">
                    <div class="text-right text-center-mobile">
                        <a class="btn btn-primary next" id="next">START APPLICATION</a>
                    </div>
                </div>
            </div>

        </div>
    </div>
</form>
