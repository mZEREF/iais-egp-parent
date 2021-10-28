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
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-approval-app.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="dashboard.jsp"%>

<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input type="hidden" name="sectionAmt" value="${approvalProfile.batInfos.size()}">

    <input type="hidden" id="section_repeat_amt_input_name" value="sectionAmt" readonly disabled>
    <input type="hidden" id="section_repeat_section_id_prefix" value="approvalProfileSection" readonly disabled>
    <input type="hidden" id="section_repeat_section_group_id" value="sectionGroup" readonly disabled>
    <input type="hidden" id="section_repeat_separator" value="--v--" readonly disabled>

    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="InnerNavTab.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div id="approvalProfilePanel" role="tabpanel">
                                    <div class="multiservice">
                                        <div class="tab-gp side-tab clearfix">
                                            <%@include file="sideNavTab.jsp"%>
                                            <div class="tab-content">
                                                <div role="tabpanel">
                                                    <div class="form-horizontal">
                                                        <div id="sectionGroup">
                                                            <c:forEach var="info" items="${approvalProfile.batInfos}" varStatus="status">
                                                                <section id="approvalProfileSection--v--${status.index}">
                                                                    <div class="form-group ">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="batId--v--${status.index}">List of Agents or Toxins</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <select name="batId--v--${status.index}" id="batId--v--${status.index}">
                                                                                <c:forEach items="${batIdOps}" var="name">
                                                                                    <option value="${name.value}" <c:if test="${info.batId eq name.value}">selected="selected"</c:if>>${name.text}</option>
                                                                                </c:forEach>
                                                                            </select>
                                                                            <span data-err-ind="batId--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="prodMaxVolumeLitres--v--${status.index}">Estimated maximum volume (in litres) of production at any one time</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input maxLength="10" type="number" name="prodMaxVolumeLitres--v--${status.index}" id="prodMaxVolumeLitres--v--${status.index}" value="${info.prodMaxVolumeLitres}">
                                                                            <span data-err-ind="prodMaxVolumeLitres--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="lspMethod--v--${status.index}">Method or system used for large scale production</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input maxLength="500" type="text" name="lspMethod--v--${status.index}" id="lspMethod--v--${status.index}" value="${info.lspMethod}">
                                                                            <span data-err-ind="lspMethod--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <%--1--%>
                                                                    <div class="form-group ">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="procurementMode--v--${status.index}">Mode of Procurement</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <select name="procurementMode--v--${status.index}" id="procurementMode--v--${status.index}">
                                                                                <option value="BMOP001" <c:if test="${info.procurementMode eq 'BMOP001'}">selected="selected"</c:if>>Local Transfer</option>
                                                                                <option value="BMOP002" <c:if test="${info.procurementMode eq 'BMOP002'}">selected="selected"</c:if>>Import</option>
                                                                            </select>
                                                                            <span data-err-ind="procurementMode--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="facilityNameOfTransfer--v--${status.index}">Transfer From Facility Name</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input maxLength="250" type="text" name="facilityNameOfTransfer--v--${status.index}" id="facilityNameOfTransfer--v--${status.index}" value="${info.facilityNameOfTransfer}">
                                                                            <span data-err-ind="facilityNameOfTransfer--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="expectedDateOfImport--v--${status.index}">Expected Date of Transfer</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input type="text" autocomplete="off" name="expectedDateOfImport--v--${status.index}" id="expectedDateOfImport--v--${status.index}" data-date-start-date="01/01/1900" value="<c:out value="${info.expectedDateOfImport}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                                            <span data-err-ind="expectedDateOfImport--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="contactPersonNameOfTransfer--v--${status.index}">Contact Person from Transferring Facility</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input maxLength="132" type="text" name="contactPersonNameOfTransfer--v--${status.index}" id="contactPersonNameOfTransfer--v--${status.index}" value="${info.contactPersonNameOfTransfer}">
                                                                            <span data-err-ind="contactPersonNameOfTransfer--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="impCtcPersonNo--v--${status.index}">Contact No of Contact Person from Transferring Facility</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input maxLength="20" type="text" name="impCtcPersonNo--v--${status.index}" id="impCtcPersonNo--v--${status.index}" value="${info.impCtcPersonNo}">
                                                                            <span data-err-ind="impCtcPersonNo--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="contactPersonEmailOfTransfer--v--${status.index}">Email Address of Contact Person from Transferring Facility</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input maxLength="66" type="text" name="contactPersonEmailOfTransfer--v--${status.index}" id="contactPersonEmailOfTransfer--v--${status.index}" value="${info.contactPersonEmailOfTransfer}">
                                                                            <span data-err-ind="contactPersonEmailOfTransfer--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="transferFacAddr1--v--${status.index}">Facility Address 1</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input maxLength="35" type="text" name="transferFacAddr1--v--${status.index}" id="transferFacAddr1--v--${status.index}" value="${info.transferFacAddr1}">
                                                                            <span data-err-ind="transferFacAddr1--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="transferFacAddr2--v--${status.index}">Facility Address 2</label>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input maxLength="35" type="text" name="transferFacAddr2--v--${status.index}" id="transferFacAddr2--v--${status.index}" value="${info.transferFacAddr2}">
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="transferFacAddr3--v--${status.index}">Facility Address 3</label>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input maxLength="35" type="text" name="transferFacAddr3--v--${status.index}" id="transferFacAddr3--v--${status.index}" value="${info.transferFacAddr3}">
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group ">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="transferCountry--v--${status.index}">Country</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <select name="transferCountry--v--${status.index}" id="transferCountry--v--${status.index}">
                                                                                <option value="COUNTRY001" <c:if test="${info.transferCountry eq 'COUNTRY001'}">selected="selected"</c:if>>Singapore</option>
                                                                                <option value="COUNTRY002" <c:if test="${info.transferCountry eq 'COUNTRY002'}">selected="selected"</c:if>>Others</option>
                                                                            </select>
                                                                            <span data-err-ind="transferCountry--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="transferCity--v--${status.index}">City</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input maxLength="30" type="text" name="transferCity--v--${status.index}" id="transferCity--v--${status.index}" value="${info.transferCity}">
                                                                            <span data-err-ind="transferCity--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="transferState--v--${status.index}">State</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input maxLength="66" type="text" name="transferState--v--${status.index}" id="transferState--v--${status.index}" value="${info.transferState}">
                                                                            <span data-err-ind="transferState--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="transferPostalCode--v--${status.index}">Postal Code</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input maxLength="15" type="text" name="transferPostalCode--v--${status.index}" id="transferPostalCode--v--${status.index}" value="${info.transferPostalCode}">
                                                                            <span data-err-ind="transferPostalCode--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="courierServiceProviderName--v--${status.index}">Name of Courier Service Provider</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input maxLength="100" type="text" name="courierServiceProviderName--v--${status.index}" id="courierServiceProviderName--v--${status.index}" value="${info.courierServiceProviderName}">
                                                                            <span data-err-ind="courierServiceProviderName--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="remarks--v--${status.index}">Remarks</label>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input maxLength="1000" type="text" name="remarks--v--${status.index}" id="remarks--v--${status.index}" value="${info.remarks}">
                                                                        </div>
                                                                    </div>
                                                                </section>
                                                            </c:forEach>
                                                        </div>
                                                        <div class="form-group">
                                                            <div class="col-12">
                                                                <a id="addNewBatSection" style="text-decoration: none" href="javascript:void(0)">+ Add New Biological Agent/Toxins</a>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
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