<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-facility-register.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-display-or-not.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-renewal-facility-register.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="dashboard.jsp"%>

<%--@elvariable id="batInfo" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.BiologicalAgentToxinDto"--%>
<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input type="hidden" name="sectionIdx" value="${TableDisplayUtil.indexes(batInfo.batInfos.size())}">

    <input type="hidden" id="section_repeat_section_idx_name" value="sectionIdx" readonly disabled>
    <input type="hidden" id="section_repeat_section_id_prefix" value="batInfoSection" readonly disabled>
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
                                <div id="batInfoPanel" role="tabpanel">
                                    <div class="multiservice">
                                        <div class="tab-gp side-tab clearfix">
                                            <%@include file="../../mainAppCommon/facRegistration/sideNavTab.jsp"%>

                                            <div class="tab-content">
                                                <div role="tabpanel">
                                                    <div class="form-horizontal">
                                                        <div id="sectionGroup">
                                                            <div class="text-right"><a id="edit" href="javascript:void(0)"><em class="fa fa-pencil-square-o"></em>Edit</a></div>
                                                            <c:forEach var="info" items="${batInfo.batInfos}" varStatus="status">
                                                                <section id="batInfoSection--v--${status.index}">
                                                                    <div class="form-group ">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="schedule--v--${status.index}">Schedule</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6" style="z-index: 30;">
                                                                            <select name="schedule--v--${status.index}" id="schedule--v--${status.index}">
                                                                                <c:forEach items="${ScheduleOps}" var="schedule">
                                                                                    <option value="${schedule.value}" <c:if test="${info.schedule eq schedule.value}">selected="selected"</c:if>>${schedule.text}</option>
                                                                                </c:forEach>
                                                                            </select>
                                                                            <span data-err-ind="schedule--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                        <c:if test="${status.index gt 0}">
                                                                            <div class="col-sm-1"><h4 class="text-danger"><em data-current-idx="${status.index}" class="fa fa-times-circle del-size-36 cursorPointer removeBtn"></em></h4></div>
                                                                        </c:if>
                                                                    </div>
                                                                    <div class="form-group ">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="batName--v--${status.index}">Name of Biological Agent/Toxin</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6" style="z-index: 20;">
                                                                            <select name="batName--v--${status.index}" id="batName--v--${status.index}">
                                                                                <c:forEach items="${batNameOps}" var="name">
                                                                                    <option value="${name.value}" <c:if test="${info.batName eq name.value}">selected="selected"</c:if>>${name.text}</option>
                                                                                </c:forEach>
                                                                            </select>
                                                                            <span data-err-ind="batName--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group ">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label>Types of samples that will be handled</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6" style="z-index: 10;">
                                                                            <div class="self-assessment-checkbox-gp">
                                                                                <div class="form-check">
                                                                                    <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="sampleCultureIsolate--v--${status.index}" <c:if test="${info.sampleType.contains('BNOTS001')}">checked="checked"</c:if> value="BNOTS001"/>
                                                                                    <label for="sampleCultureIsolate--v--${status.index}" class="form-check-label"><span class="check-square"></span>Culture/isolate of biological agent(s)</label>
                                                                                </div>
                                                                                <div class="form-check">
                                                                                    <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="samplePureToxin--v--${status.index}" <c:if test="${info.sampleType.contains('BNOTS002')}">checked="checked"</c:if> value="BNOTS002"/>
                                                                                    <label for="samplePureToxin--v--${status.index}" class="form-check-label"><span class="check-square"></span>Pure toxin(s)</label>
                                                                                </div>
                                                                                <div class="form-check">
                                                                                    <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="sampleClinical--v--${status.index}" <c:if test="${info.sampleType.contains('BNOTS003')}">checked="checked"</c:if> value="BNOTS003"/>
                                                                                    <label for="sampleClinical--v--${status.index}" class="form-check-label"><span class="check-square"></span>Clinical samples e.g. blood, serum, respiratory swab, containing biological agent(s) or toxin(s)</label>
                                                                                </div>
                                                                                <div class="form-check">
                                                                                    <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="sampleAnimal--v--${status.index}" <c:if test="${info.sampleType.contains('BNOTS004')}">checked="checked"</c:if> value="BNOTS004"/>
                                                                                    <label for="sampleAnimal--v--${status.index}" class="form-check-label"><span class="check-square"></span>Animal samples containing biological agent(s) or toxin(s)</label>
                                                                                </div>
                                                                                <div class="form-check">
                                                                                    <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="sampleEnv--v--${status.index}" <c:if test="${info.sampleType.contains('BNOTS005')}">checked="checked"</c:if> value="BNOTS005"/>
                                                                                    <label for="sampleEnv--v--${status.index}" class="form-check-label"><span class="check-square"></span>Environmental samples containing biological agent(s) or toxin(s)</label>
                                                                                </div>
                                                                                <div class="form-check">
                                                                                    <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="sampleFood--v--${status.index}" <c:if test="${info.sampleType.contains('BNOTS006')}">checked="checked"</c:if> value="BNOTS006"/>
                                                                                    <label for="sampleFood--v--${status.index}" class="form-check-label"><span class="check-square"></span>Food samples containing biological agent(s) or toxin(s)</label>
                                                                                </div>
                                                                                <div class="form-check">
                                                                                    <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="sampleOthers--v--${status.index}" data-custom-ind="batOthersSampleType" <c:if test="${info.sampleType.contains('BNOTS007')}">checked="checked"</c:if> value="BNOTS007"/>
                                                                                    <label for="sampleOthers--v--${status.index}" class="form-check-label"><span class="check-square"></span>Others. Please specify under details.</label>
                                                                                </div>
                                                                                <div class="form-check" id="batOtherSampleTypeDiv--v--${status.index}" <c:if test="${not info.sampleType.contains('BNOTS007')}">style="display: none;"</c:if>>
                                                                                    <input type="text" autocomplete="off" name="otherSampleType--v--${status.index}" id="otherSampleType--v--${status.index}" value='<c:out value="${info.otherSampleType}"/>'/>
                                                                                </div>
                                                                            </div>
                                                                            <span data-err-ind="sampleType--v--${status.index}" class="error-msg"></span>
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
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>