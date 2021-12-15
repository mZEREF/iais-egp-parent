<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-facility-register.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-display-or-not.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-renewal-facility-register.js"></script>

<%@include file="dashboard.jsp"%>

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
                            <p>T07CX0118D</p>
                        </div>
                    </div>
                    <div class="form-group ">
                        <div class="col-sm-5 control-label">
                            <p>Company Name:</p>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <p>SGH LABORATORY</p>
                        </div>
                    </div>
                    <div class="form-group ">
                        <div class="col-sm-5 control-label">
                            <p>Block:</p>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <p>212</p>
                        </div>
                    </div>
                    <div class="form-group ">
                        <div class="col-sm-5 control-label">
                            <p>Street Name:</p>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <p>Street Name</p>
                        </div>
                    </div>
                    <div class="form-group ">
                        <div class="col-sm-5 control-label">
                            <p>Floor and Unit No.:</p>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <p>03-147</p>
                        </div>
                    </div>
                    <div class="form-group ">
                        <div class="col-sm-5 control-label">
                            <p>Postal Code:</p>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <p>460212</p>
                        </div>
                    </div>
                </div>
            </div>
            <br/>
            <div class="col-xs-12 col-sm-12" style="padding-top: 30px">
                <p class="assessment-title" style="border-bottom: 1px solid black; font-size:18px; padding-bottom: 10px; font-weight: bold">Before You Begin</p>
                <div>
                    <ul>
                        <li>In the next page, you will select the classification of the facility which you intend to register and
                            the type of activities which will be conducted in the facility. Before proceeding, you are advised
                            to refer to the information that is available on the <a href="#">MOH Biosafety website</a> to understand the
                            different options, to ensure selection of the correct facility classification. Please note that
                            selection of an incorrect facility classification may result in rejection of the application.
                        </li>
                        <li>This form will take approximately 10 mins to complete. You may save your progress at any
                            time and resume your application later
                        </li>
                    </ul>
                </div>
            </div>
            <br/>
            <div class="row">
                <div class="col-xs-12 col-md-3">
                    <a class="back" href="/bsb-fe/eservice/INTERNET/MohBSBInboxMsg"><em class="fa fa-angle-left"></em> Back</a>
                </div>
                <div class="col-xs-12 col-md-9">
                    <div class="text-right text-center-mobile">
                        <a class="btn btn-primary next" id="next" href="javascript:void(0);">START APPLICATION</a>
                    </div>
                </div>
            </div>

        </div>
    </div>
</form>
