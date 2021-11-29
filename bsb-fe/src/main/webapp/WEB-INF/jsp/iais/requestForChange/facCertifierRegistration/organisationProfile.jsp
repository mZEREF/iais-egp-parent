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

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-rfc-facility-certifier-register.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="../common/dashboard.jsp"%>

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
                                        <div class="text-right"><a id="edit" href="javascript:void(0)"><em class="fa fa-pencil-square-o"></em>Edit</a></div>
                                        <h3 class="col-12 pl-0" style="border-bottom: 1px solid black">Organisation Profile</h3>
                                        <%--@elvariable id="facProfile" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityProfileDto"--%>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="orgName">Organisation Name</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input type="text" autocomplete="off" name="orgName" id="orgName" value='${orgProfile.orgName}'/>
                                                <span data-err-ind="orgName" class="error-msg"></span>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Address Type</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <div class="col-sm-4" style="margin-top: 8px">
                                                    <input type="radio" name="addressType" id="typeLocal" value="local" <c:if test="${orgProfile.addressType eq 'local'}">checked</c:if> />
                                                    <label for="typeLocal">Local</label>
                                                </div>
                                                <div class="col-sm-4" style="margin-top: 8px">
                                                    <input type="radio" name="addressType" id="typeOverseas" value="overseas" <c:if test="${orgProfile.addressType eq 'overseas'}">checked</c:if> />
                                                    <label for="typeOverseas">Overseas</label>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="floor">Floor and Unit</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-2">
                                                <input type="text" autocomplete="off" name="floor" id="floor" value='${orgProfile.floor}'/>
                                                <span data-err-ind="floor" class="error-msg"></span>
                                            </div>
                                            <div class="hidden-xs col-sm-1" style="text-align: center">
                                                <p>-</p>
                                            </div>
                                            <div class="col-sm-3 col-md-4">
                                                <input type="text" autocomplete="off" name="unitNo" id="unitNo" value='${orgProfile.unitNo}'/>
                                                <span data-err-ind="unitNo" class="error-msg"></span>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="building">Building</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input type="text" autocomplete="off" name="building" id="building" value='${orgProfile.building}'/>
                                                <span data-err-ind="building" class="error-msg"></span>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="streetName">Street Name</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input type="text" autocomplete="off" name="streetName" id="streetName" value='${orgProfile.streetName}'/>
                                                <span data-err-ind="streetName" class="error-msg"></span>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="address1">Address 1</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input type="text" autocomplete="off" name="address1" id="address1" value='${orgProfile.address1}'/>
                                                <span data-err-ind="address1" class="error-msg"></span>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="address2">Address 2</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input type="text" autocomplete="off" name="address2" id="address2" value='${orgProfile.address2}'/>
                                                <span data-err-ind="address2" class="error-msg"></span>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="address3">Address 3</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input type="text" autocomplete="off" name="address3" id="address3" value='${orgProfile.address3}'/>
                                                <span data-err-ind="address3" class="error-msg"></span>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="postalCode">Postal Code</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input type="text" autocomplete="off" name="postalCode" id="postalCode" value='${orgProfile.postalCode}'/>
                                                <span data-err-ind="postalCode" class="error-msg"></span>
                                            </div>
                                        </div>

                                        <div id="overseasCon"<c:if test="${orgProfile.addressType ne 'overseas'}">style="display: none"</c:if>>
                                            <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="city">City</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input type="text" autocomplete="off" name="city" id="city" value='${orgProfile.city}'/>
                                                <span data-err-ind="city" class="error-msg"></span>
                                            </div>
                                            </div>

                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label for="state">State</label>
                                                    <span class="mandatory otherQualificationSpan">*</span>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <input type="text" autocomplete="off" name="state" id="state" value='${orgProfile.state}'/>
                                                    <span data-err-ind="state" class="error-msg"></span>
                                                </div>
                                            </div>

                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label for="country">Country</label>
                                                    <span class="mandatory otherQualificationSpan">*</span>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <select name="country" id="country" >
                                                        <c:forEach items="${countryOps}" var="co">
                                                        <option value="${co.value}" <c:if test="${co.value eq orgProfile.country}">selected="selected"</c:if>>${co.text}</option>
                                                        </c:forEach>
                                                    </select>
                                                    <span data-err-ind="country" class="error-msg"></span>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="yearEstablished">Year Established</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input type="text" autocomplete="off" name="yearEstablished" id="yearEstablished" value='${orgProfile.yearEstablished}'/>
                                                <span data-err-ind="yearEstablished" class="error-msg"></span>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="email">Email</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input type="text" autocomplete="off" name="email" id="email" value='${orgProfile.email}'/>
                                                <span data-err-ind="email" class="error-msg"></span>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="contactNo">Contact No.</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input type="text" autocomplete="off" name="contactNo" id="contactNo" value='${orgProfile.contactNo}'/>
                                                <span data-err-ind="contactNo" class="error-msg"></span>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="contactPerson">Contact Person</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input type="text" autocomplete="off" name="contactPerson" id="contactPerson" value='${orgProfile.contactPerson}'/>
                                                <span data-err-ind="contactPerson" class="error-msg"></span>
                                            </div>
                                        </div>

                                    </div>
                                </div>

                                <%@ include file="../common/InnerFooter.jsp" %>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>