<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-facility-register.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="dashboard.jsp"%>

<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="InnerNavTab.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div id="facInfoPanel" role="tabpanel">
                                    <%@include file="subStepNavTab.jsp"%>

                                    <div class="form-horizontal">
                                        <h3 class="col-12" style="border-bottom: 1px solid black">Facility Profile</h3>
                                        <%--@elvariable id="facProfile" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityProfileDto"--%>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="facName">Facility Name</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input maxLength="250" type="text" autocomplete="off" name="facName" id="facName" value='<c:out value="${facProfile.facName}"/>'/>
                                                <span data-err-ind="facName" class="error-msg"></span>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Facility Address:</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <button type="button" id="facAddr" class="btn btn-secondary" style="margin-bottom: 15px">Retrieve Address</button>
                                                <span data-err-ind="facAddr" class="error-msg"></span>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="block">Block</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input maxLength="10" type="text" autocomplete="off" name="block" id="block" value='<c:out value="${facProfile.block}"/>'/>
                                                <span data-err-ind="block" class="error-msg"></span>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="streetName">Street Name</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input maxLength="32" type="text" autocomplete="off" name="streetName" id="streetName" value='<c:out value="${facProfile.streetName}"/>'/>
                                                <span data-err-ind="streetName" class="error-msg"></span>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="floor">Floor and Unit No.</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-2">
                                                <input maxLength="4" type="text" autocomplete="off" name="floor" id="floor" value='<c:out value="${facProfile.floor}"/>'/>
                                                <span data-err-ind="floor" class="error-msg"></span>
                                            </div>
                                            <div class="hidden-xs col-sm-1" style="text-align: center">
                                                <p>-</p>
                                            </div>
                                            <div class="col-sm-3 col-md-4">
                                                <input maxLength="4" type="text" autocomplete="off" name="unitNo" id="unitNo" value='<c:out value="${facProfile.unitNo}"/>'/>
                                                <span data-err-ind="unitNo" class="error-msg"></span>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="postalCode">Postal Code</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input maxLength="6" type="text" autocomplete="off" name="postalCode" id="postalCode" value='<c:out value="${facProfile.postalCode}"/>'/>
                                                <span data-err-ind="postalCode" class="error-msg"></span>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Is the facility a Protected Place</label>
                                                <a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="" data-original-title="<p>Refers to a facility that has been gazetted as a Protected Place under the Infrastructure Protection Act</p>">i</a>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <div class="col-sm-4 col-md-2" style="margin-top: 8px">
                                                    <label for="isAProtectedPlace">Yes</label>
                                                    <input type="radio" name="protectedPlace" id="isAProtectedPlace" value="Y" <c:if test="${facProfile.isFacilityProtected eq 'Y'}">checked="checked"</c:if> />
                                                </div>
                                                <div class="col-sm-4 col-md-2" style="margin-top: 8px">
                                                    <label for="notAProtectedPlace">No</label>
                                                    <input type="radio" name="protectedPlace" id="notAProtectedPlace" value="N" <c:if test="${facProfile.isFacilityProtected eq 'N'}">checked="checked"</c:if> />
                                                </div>
                                                <span data-err-ind="isFacilityProtected" class="error-msg"></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <%@ include file="InnerFooter.jsp" %>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>