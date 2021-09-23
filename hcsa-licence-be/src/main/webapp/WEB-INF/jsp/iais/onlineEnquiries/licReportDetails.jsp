<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="dashboard" >
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="preInspReport" value="1">
        <div class="main-content">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="center-content">
                        <div class="intranet-content">
                            <iais:body >
                                <div class="col-xs-12">
                                    <div class="tab-gp dashboard-tab">
                                        <br><br><br>
                                        <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                            <li class="complete" role="presentation"><a href="#tabLicenseeDetails"
                                                                                        aria-controls="tabLicenseeDetails"
                                                                                        role="tab"
                                                                                        data-toggle="tab">Licensee
                                                Details</a>
                                            </li>
                                            <li class="complete" role="presentation"><a href="#tabPersonnelDetails"
                                                                                        aria-controls="tabPersonnelDetails"
                                                                                        role="tab"
                                                                                        data-toggle="tab">Personnel
                                                Details</a>
                                            </li>
                                            <li class="active" role="presentation"><a href="#tabInspectionReport"
                                                                                      aria-controls="tabInspectionReport"
                                                                                      role="tab"
                                                                                      data-toggle="tab">Inspection
                                                Report</a>
                                            </li>
                                        </ul>
                                        <div class="tab-nav-mobile visible-xs visible-sm">
                                            <div class="swiper-wrapper" role="tablist">
                                                <div class="swiper-slide"><a href="#tabLicenseeDetails"
                                                                             aria-controls="tabLicenseeDetails" role="tab"
                                                                             data-toggle="tab">Licensee Details</a></div>
                                                <div class="swiper-slide"><a href="#tabPersonnelDetails"
                                                                             aria-controls="tabPersonnelDetails" role="tab"
                                                                             data-toggle="tab">Personnel Details</a></div>
                                                <div class="swiper-slide"><a href="#tabInspectionReport"
                                                                             aria-controls="tabInspectionReport" role="tab"
                                                                             data-toggle="tab">Compliance History</a></div>
                                            </div>
                                            <div class="swiper-button-prev"></div>
                                            <div class="swiper-button-next"></div>
                                        </div>

                                        <div class="tab-content ">
                                            <div class="tab-pane" id="tabLicenseeDetails" role="tabpanel">
                                                <%@include file="licenseeDetails.jsp" %>
                                            </div>

                                            <div class="tab-pane" id="tabPersonnelDetails" role="tabpanel">
                                                <%@include file="personnelDetails.jsp" %>
                                            </div>
                                            <div class="tab-pane active" id="tabInspectionReport" role="tabpanel">
                                                <%@include file="reportViewDetails.jsp"%>
                                            </div>
                                        </div>
                                    </div>
                                    <a href="#" onclick="javascript:SOP.Crud.cfxSubmit('mainForm');"><em class="fa fa-angle-left"> </em> Back</a>
                                </div>
                            </iais:body>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
