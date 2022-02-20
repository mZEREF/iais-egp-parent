<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-approval-app.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-display-or-not.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-rfc-approval-app.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="../common/dashboard.jsp"%>

<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input type="hidden" name="sectionIdx" value="${TableDisplayUtil.indexes(approvalProfile.batInfos.size())}">

    <input type="hidden" id="section_repeat_section_idx_name" value="sectionIdx" readonly disabled>
    <input type="hidden" id="section_repeat_section_id_prefix" value="approvalProfileSection" readonly disabled>
    <input type="hidden" id="section_repeat_section_group_id" value="sectionGroup" readonly disabled>
    <input type="hidden" id="section_repeat_separator" value="--v--" readonly disabled>

    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="../../mainAppCommon/approvalapp/InnerNavTab.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div id="approvalProfilePanel" role="tabpanel">
                                    <div class="multiservice">
                                        <div class="tab-gp side-tab clearfix">
                                            <%@include file="../../mainAppCommon/approvalapp/sideNavTab.jsp"%>
                                            <div class="tab-content">
                                                <div role="tabpanel">
                                                    <div class="form-horizontal">
                                                        <div id="sectionGroup">
                                                            <div class="text-right"><a id="edit" href="javascript:void(0)"><em class="fa fa-pencil-square-o"></em>Edit</a></div>
                                                            <c:forEach var="info" items="${approvalProfile.batInfos}" varStatus="status">
                                                                <input type="hidden" name="facilityBiologicalAgentEntityId--v--${status.index}" value="${MaskUtil.maskValue('facilityBiologicalAgentEntityId',info.facilityBiologicalAgentEntityId)}">
                                                                <section id="approvalProfileSection--v--${status.index}">
                                                                    <div class="form-group ">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="batId--v--${status.index}">List of Agents/Toxins</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6">
                                                                            <select name="batId--v--${status.index}" id="batId--v--${status.index}">
                                                                                <c:forEach items="${batIdOps}" var="name">
                                                                                    <option value="${name.value}" <c:if test="${info.batId eq name.value}">selected="selected"</c:if>>${name.text}</option>
                                                                                </c:forEach>
                                                                            </select>
                                                                            <span data-err-ind="batId--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                        <c:if test="${status.index gt 0}">
                                                                            <div class="col-sm-1"><h4 class="text-danger"><em data-current-idx="${status.index}" class="fa fa-times-circle del-size-36 cursorPointer removeBtn"></em></h4></div>
                                                                        </c:if>
                                                                    </div>
                                                                    <c:if test="${processType eq 'PROTYPE002' or processType eq 'PROTYPE003'}">
                                                                        <div class="form-group">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="prodMaxVolumeLitres--v--${status.index}">Estimated maximum volume (in litres) of production at any one time</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input oninput="if(value.length>10) value=value.slice(0,10)" type="number" name="prodMaxVolumeLitres--v--${status.index}" id="prodMaxVolumeLitres--v--${status.index}" value="${info.prodMaxVolumeLitres}">
                                                                                <span data-err-ind="prodMaxVolumeLitres--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="lspMethod--v--${status.index}">Method or system used for large scale production</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxLength="500" type="text" name="lspMethod--v--${status.index}" id="lspMethod--v--${status.index}" value="${info.lspMethod}">
                                                                                <span data-err-ind="lspMethod--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                    </c:if>
                                                                    <div class="form-group ">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="procurementMode--v--${status.index}">Mode of Procurement</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6">
                                                                            <div class="col-sm-6 col-md-3" style="margin-top: 8px">
                                                                                <label for="localMode--v--${status.index}">Local</label>
                                                                                <input type="radio" name="procurementMode--v--${status.index}" id="localMode--v--${status.index}" value="BMOP001" <c:if test="${info.procurementMode eq 'BMOP001'}">checked="checked"</c:if> />
                                                                            </div>
                                                                            <div class="col-sm-6 col-md-3" style="margin-top: 8px">
                                                                                <label for="importMode--v--${status.index}">Import</label>
                                                                                <input type="radio" name="procurementMode--v--${status.index}" id="importMode--v--${status.index}" value="BMOP002" <c:if test="${info.procurementMode eq 'BMOP002'}">checked="checked"</c:if> />
                                                                            </div>
                                                                            <span data-err-ind="procurementMode--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <c:if test="${processType eq 'PROTYPE002' or processType eq 'PROTYPE003'}">
                                                                        <div class="form-group">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="facilityNameOfTransfer--v--${status.index}">Transfer From Facility Name</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxLength="250" type="text" name="facilityNameOfTransfer--v--${status.index}" id="facilityNameOfTransfer--v--${status.index}" value="${info.facilityNameOfTransfer}">
                                                                                <span data-err-ind="facilityNameOfTransfer--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="expectedDateOfImport--v--${status.index}">Expected Date of Transfer</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input type="text" autocomplete="off" name="expectedDateOfImport--v--${status.index}" id="expectedDateOfImport--v--${status.index}" data-date-start-date="01/01/1900" value="<c:out value="${info.expectedDateOfImport}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                                                <span data-err-ind="expectedDateOfImport--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="contactPersonNameOfTransfer--v--${status.index}">Contact Person from Transferring Facility</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxLength="132" type="text" name="contactPersonNameOfTransfer--v--${status.index}" id="contactPersonNameOfTransfer--v--${status.index}" value="${info.contactPersonNameOfTransfer}">
                                                                                <span data-err-ind="contactPersonNameOfTransfer--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="impCtcPersonNo--v--${status.index}">Contact No of Contact Person from Transferring Facility</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxLength="20" type="text" name="impCtcPersonNo--v--${status.index}" id="impCtcPersonNo--v--${status.index}" value="${info.impCtcPersonNo}">
                                                                                <span data-err-ind="impCtcPersonNo--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="contactPersonEmailOfTransfer--v--${status.index}">Email Address of Contact Person from Transferring Facility</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxLength="66" type="text" name="contactPersonEmailOfTransfer--v--${status.index}" id="contactPersonEmailOfTransfer--v--${status.index}" value="${info.contactPersonEmailOfTransfer}">
                                                                                <span data-err-ind="contactPersonEmailOfTransfer--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="transferFacAddr1--v--${status.index}">Facility Address 1</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxLength="35" type="text" name="transferFacAddr1--v--${status.index}" id="transferFacAddr1--v--${status.index}" value="${info.transferFacAddr1}">
                                                                                <span data-err-ind="transferFacAddr1--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="transferFacAddr2--v--${status.index}">Facility Address 2</label>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxLength="35" type="text" name="transferFacAddr2--v--${status.index}" id="transferFacAddr2--v--${status.index}" value="${info.transferFacAddr2}">
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="transferFacAddr3--v--${status.index}">Facility Address 3</label>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxLength="35" type="text" name="transferFacAddr3--v--${status.index}" id="transferFacAddr3--v--${status.index}" value="${info.transferFacAddr3}">
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="transferCountry--v--${status.index}">Country</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <select name="transferCountry--v--${status.index}" id="transferCountry--v--${status.index}">
                                                                                    <c:forEach items="${countryOps}" var="country">
                                                                                        <option value="${country.value}" <c:if test="${info.transferCountry eq country.value}">selected="selected"</c:if>>${country.text}</option>
                                                                                    </c:forEach>
                                                                                </select>
                                                                                <span data-err-ind="transferCountry--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="transferCity--v--${status.index}">City</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxLength="30" type="text" name="transferCity--v--${status.index}" id="transferCity--v--${status.index}" value="${info.transferCity}">
                                                                                <span data-err-ind="transferCity--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="transferState--v--${status.index}">State</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxLength="66" type="text" name="transferState--v--${status.index}" id="transferState--v--${status.index}" value="${info.transferState}">
                                                                                <span data-err-ind="transferState--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="transferPostalCode--v--${status.index}">Postal Code</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxLength="15" type="text" name="transferPostalCode--v--${status.index}" id="transferPostalCode--v--${status.index}" value="${info.transferPostalCode}" oninput="value=value.replace(/[^\d]/g,'')">
                                                                                <span data-err-ind="transferPostalCode--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="courierServiceProviderName--v--${status.index}">Name of Courier Service Provider</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxLength="100" type="text" name="courierServiceProviderName--v--${status.index}" id="courierServiceProviderName--v--${status.index}" value="${info.courierServiceProviderName}">
                                                                                <span data-err-ind="courierServiceProviderName--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                    </c:if>
                                                                    <c:if test="${processType eq 'PROTYPE004'}">
                                                                        <div class="form-group">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="prjName--v--${status.index}">Name of Project</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxLength="300" type="text" name="prjName--v--${status.index}" id="prjName--v--${status.index}" value="${info.prjName}">
                                                                                <span data-err-ind="prjName--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="principalInvestigatorName--v--${status.index}">Name of Principal Investigator</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxLength="100" type="text" name="principalInvestigatorName--v--${status.index}" id="principalInvestigatorName--v--${status.index}" value="${info.principalInvestigatorName}">
                                                                                <span data-err-ind="principalInvestigatorName--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="workActivityIntended--v--${status.index}">Intended Work Activity</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxLength="1000" type="text" name="workActivityIntended--v--${status.index}" id="workActivityIntended--v--${status.index}" value="${info.workActivityIntended}">
                                                                                <span data-err-ind="workActivityIntended--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="startDate--v--${status.index}">Start Date</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input type="text" autocomplete="off" name="startDate--v--${status.index}" id="startDate--v--${status.index}" data-date-start-date="01/01/1900" value="<c:out value="${info.startDate}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                                                <span data-err-ind="startDate--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="endDate--v--${status.index}">End Date</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input type="text" autocomplete="off" name="endDate--v--${status.index}" id="endDate--v--${status.index}" data-date-start-date="01/01/1900" value="<c:out value="${info.endDate}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                                                <span data-err-ind="endDate--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                    </c:if>
                                                                    <div class="form-group">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="remarks--v--${status.index}">Remarks</label>
                                                                        </div>
                                                                        <div class="col-sm-6">
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
                                <%@ include file="../common/InnerFooter.jsp" %>
                            </div>
                        </div>
                        <%@include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp"%>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>