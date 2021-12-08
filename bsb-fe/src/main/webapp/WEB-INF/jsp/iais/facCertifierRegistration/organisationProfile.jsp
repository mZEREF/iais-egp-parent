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
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-facility-certifier-register.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-facility-certifier-register.js"></script>

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
                                        <h3 class="col-12 pl-0" style="border-bottom: 1px solid black">Organisation Profile</h3>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="orgName">Organisation Name</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input type="text" autocomplete="off" name="orgName" id="orgName" value='${orgProfile.orgName}' maxlength="250"/>
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
                                                <input type="text" autocomplete="off" name="floor" id="floor" value='${orgProfile.floor}' maxlength="4"/>
                                                <span data-err-ind="floor" class="error-msg"></span>
                                            </div>
                                            <div class="hidden-xs col-sm-1" style="text-align: center">
                                                <p>-</p>
                                            </div>
                                            <div class="col-sm-3 col-md-4">
                                                <input type="text" autocomplete="off" name="unitNo" id="unitNo" value='${orgProfile.unitNo}' maxlength="4"/>
                                                <span data-err-ind="unitNo" class="error-msg"></span>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="building">Building</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input type="text" autocomplete="off" name="building" id="building" value='${orgProfile.building}' maxlength="10"/>
                                                <span data-err-ind="building" class="error-msg"></span>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="streetName">Street Name</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input type="text" autocomplete="off" name="streetName" id="streetName" value='${orgProfile.streetName}' maxlength="32"/>
                                                <span data-err-ind="streetName" class="error-msg"></span>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="address1">Address 1</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input type="text" autocomplete="off" name="address1" id="address1" value='${orgProfile.address1}' maxlength="35"/>
                                                <span data-err-ind="address1" class="error-msg"></span>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="address2">Address 2</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input type="text" autocomplete="off" name="address2" id="address2" value='${orgProfile.address2}' maxlength="35"/>
                                                <span data-err-ind="address2" class="error-msg"></span>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="address3">Address 3</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input type="text" autocomplete="off" name="address3" id="address3" value='${orgProfile.address3}' maxlength="35"/>
                                                <span data-err-ind="address3" class="error-msg"></span>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="postalCode">Postal Code</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input type="number" autocomplete="off" name="postalCode" id="postalCode" maxlength="15" value='${orgProfile.postalCode}' oninput="value=value.replace(/[^\d]/g,'')"/>
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
                                                <input type="text" autocomplete="off" name="city" id="city" value='${orgProfile.city}' maxlength="30"/>
                                                <span data-err-ind="city" class="error-msg"></span>
                                            </div>
                                            </div>

                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label for="state">State</label>
                                                    <span class="mandatory otherQualificationSpan">*</span>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <input type="text" autocomplete="off" name="state" id="state" maxlength="66" value='${orgProfile.state}'/>
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
                                                <input type="number" autocomplete="off" name="yearEstablished" id="yearEstablished" oninput="if(value.length>4) value=value.slice(0,4)" value='${orgProfile.yearEstablished}'/>
                                                <span data-err-ind="yearEstablished" class="error-msg"></span>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="email">Email</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input type="text" autocomplete="off" name="email" id="email" maxlength="66" value='${orgProfile.email}'/>
                                                <span data-err-ind="email" class="error-msg"></span>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="contactNo">Contact No.</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input type="text" autocomplete="off" name="contactNo" id="contactNo" maxlength="20" value='${orgProfile.contactNo}'/>
                                                <span data-err-ind="contactNo" class="error-msg"></span>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="contactPerson">Contact Person</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input type="text" autocomplete="off" name="contactPerson" id="contactPerson" maxlength="132" value='${orgProfile.contactPerson}'/>
                                                <span data-err-ind="contactPerson" class="error-msg"></span>
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