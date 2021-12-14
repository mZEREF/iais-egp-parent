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

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-facility-certifier-register.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-facility-certifier-register.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="dashboard.jsp"%>

<%--@elvariable id="facAuth" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserDto"--%>
<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input type="hidden" name="sectionIdx" value="${TableDisplayUtil.indexes(orgCerTeam.certifierTeamMemberList.size())}">

    <input type="hidden" id="section_repeat_section_idx_name" value="sectionIdx" readonly disabled>
    <input type="hidden" id="section_repeat_section_id_prefix" value="cerTeamSection" readonly disabled>
    <input type="hidden" id="section_repeat_header_title_prefix" value="Certifying Team Member " readonly disabled>
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
                                <div id="facInfoPanel" role="tabpanel">
                                    <%@include file="subStepNavTab.jsp"%>

                                    <div class="form-horizontal">
                                        <h3 class="col-12 pl-0" style="border-bottom: 1px solid black">Certifying Team Member</h3>

                                        <div id="sectionGroup">
                                        <c:forEach var="cer" items="${orgCerTeam.certifierTeamMemberList}" varStatus="status">
                                            <section id="cerTeamSection--v--${status.index}">
                                                <c:if test="${orgCerTeam.certifierTeamMemberList.size() > 1}">
                                                    <div class="form-group">
                                                        <h3 class="col-xs-9 col-sm-10 col-md-11" style="border-bottom: 1px solid black">Certifying Team Member ${status.index + 1}</h3>
                                                        <c:if test="${status.index gt 0}">
                                                            <div class="col-sm-1"><h4 class="text-danger"><em data-current-idx="${status.index}" class="fa fa-times-circle del-size-36 cursorPointer removeBtn"></em></h4></div>
                                                        </c:if>
                                                    </div>
                                                </c:if>
                                                <div class="form-group ">
                                                    <div class="col-sm-5 control-label">
                                                        <label for="memberName--v--${status.index}">Name</label>
                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                    </div>
                                                    <div class="col-sm-6 col-md-7">
                                                        <input type="text" autocomplete="off" name="memberName--v--${status.index}" id="memberName--v--${status.index}" value='<c:out value="${cer.memberName}"/>' maxlength="132"/>
                                                        <span data-err-ind="memberName--v--${status.index}" class="error-msg"></span>
                                                    </div>
                                                </div>
                                                <div class="form-group ">
                                                    <div class="col-sm-5 control-label">
                                                        <label for="idNo--v--${status.index}">ID No.</label>
                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <select name="idType--v--${status.index}" id="idType--v--${status.index}">
                                                            <option value="IDTYPE001" <c:if test="${cer.idType eq 'IDTYPE001'}">selected="selected"</c:if>>NRIC</option>
                                                            <option value="IDTYPE002" <c:if test="${cer.idType eq 'IDTYPE002'}">selected="selected"</c:if>>FIN</option>
                                                            <option value="IDTYPE003" <c:if test="${cer.idType eq 'IDTYPE003'}">selected="selected"</c:if>>Passport</option>
                                                        </select>
                                                        <span data-err-ind="idType--v--${status.index}" class="error-msg"></span>
                                                    </div>
                                                    <div class="col-sm-3 col-md-4">
                                                        <input type="text" autocomplete="off" name="idNo--v--${status.index}" id="idNo--v--${status.index}" maxlength="10" value='<c:out value="${cer.idNo}"/>'/>
                                                        <span data-err-ind="idNo--v--${status.index}" class="error-msg"></span>
                                                    </div>
                                                </div>

                                                <div class="form-group ">
                                                    <div class="col-sm-5 control-label">
                                                        <label for="birthDate--v--${status.index}">Date of Birth</label>
                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                    </div>
                                                    <div class="col-sm-6 col-md-7">
                                                        <input type="text" autocomplete="off" name="birthDate--v--${status.index}" id="birthDate--v--${status.index}" value='<c:out value="${cer.birthDate}"/>' data-date-start-date="01/01/1900"  placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                        <span data-err-ind="birthDate--v--${status.index}" class="error-msg"></span>
                                                    </div>
                                                </div>

                                                <div class="form-group ">
                                                    <div class="col-sm-5 control-label">
                                                        <label>Sex</label>
                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                    </div>
                                                    <div class="col-sm-6 col-md-7">
                                                        <div class="col-sm-5" style="margin-top: 8px">
                                                            <input type="radio" name="sex--v--${status.index}" id="male--v--${status.index}" value="male" <c:if test="${cer.sex eq 'male'}">checked="checked"</c:if> />
                                                            <label for="male--v--${status.index}">Male</label>
                                                        </div>
                                                        <div class="col-sm-5" style="margin-top: 8px">
                                                            <input type="radio" name="sex--v--${status.index}" id="female--v--${status.index}" value="female" <c:if test="${cer.sex eq 'female'}">checked="checked"</c:if> />
                                                            <label for="female--v--${status.index}">Female</label>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="form-group ">
                                                    <div class="col-sm-5 control-label">
                                                        <label for="nationality--v--${status.index}">Nationality</label>
                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                    </div>
                                                    <div class="col-sm-6 col-md-7">
                                                        <select name="nationality--v--${status.index}" id="nationality--v--${status.index}">
                                                            <c:forEach items="${nationalityOps}" var="na">
                                                                <option value="${na.value}" <c:if test="${cer.nationality eq na.value}">selected="selected"</c:if>>${na.text}</option>
                                                            </c:forEach>
                                                        </select>
                                                        <span data-err-ind="nationality--v--${status.index}" class="error-msg"></span>
                                                    </div>
                                                </div>

                                                <div class="form-group ">
                                                    <div class="col-sm-5 control-label">
                                                        <label for="telNo--v--${status.index}">Tel No</label>
                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                    </div>
                                                    <div class="col-sm-6 col-md-7">
                                                        <input type="text" autocomplete="off" name="telNo--v--${status.index}" id="telNo--v--${status.index}" maxlength="20" value='<c:out value="${cer.telNo}"/>'/>
                                                        <span data-err-ind="telNo--v--${status.index}" class="error-msg"></span>
                                                    </div>
                                                </div>

                                                <div class="form-group ">
                                                    <div class="col-sm-5 control-label">
                                                        <label for="jobDesignation--v--${status.index}">Job Designation</label>
                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                    </div>
                                                    <div class="col-sm-6 col-md-7">
                                                        <input type="text" autocomplete="off" name="jobDesignation--v--${status.index}" id="jobDesignation--v--${status.index}" maxlength="66" value='<c:out value="${cer.jobDesignation}"/>'/>
                                                        <span data-err-ind="jobDesignation--v--${status.index}" class="error-msg"></span>
                                                    </div>
                                                </div>

                                                <div class="form-group ">
                                                    <div class="col-sm-5 control-label">
                                                        <label>Lead Certifier</label>
                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                    </div>
                                                    <div class="col-sm-6 col-md-7">
                                                        <div class="col-sm-5" style="margin-top: 8px">
                                                            <label for="lead--v--${status.index}">Yes</label>
                                                            <input type="radio" name="leadCertifier--v--${status.index}" id="lead--v--${status.index}" <c:if test="${cer.leadCertifier eq 'leader'}">checked="checked"</c:if> onchange="showLeader(this)" value="leader" />
                                                        </div>
                                                        <div class="col-sm-5" style="margin-top: 8px">
                                                            <label for="common--v--${status.index}">No</label>
                                                            <input type="radio" name="leadCertifier--v--${status.index}" id="common--v--${status.index}" <c:if test="${cer.leadCertifier eq 'common'}">checked="checked"</c:if> onchange="hideLeader(this)" value="common" />
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="form-group" <c:if test="${cer.expertiseArea ne 'leader'}">style="display: none"</c:if>>
                                                    <div class="col-sm-5 control-label">
                                                        <label for="expertiseArea--v--${status.index}">Area of Expertise (Position)</label>
                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                    </div>
                                                    <div class="col-sm-6 col-md-7">
                                                        <select name="expertiseArea--v--${status.index}" id="expertiseArea--v--${status.index}">
                                                            <c:forEach items="${positionOps}" var="pos">
                                                                <option value="${pos.value}" <c:if test="${cer.expertiseArea eq pos.value}">selected="selected"</c:if>>${pos.text}</option>
                                                            </c:forEach>
                                                        </select>
                                                        <span data-err-ind="expertiseArea--v--${status.index}" class="error-msg"></span>
                                                    </div>
                                                </div>


                                                <div class="form-group ">
                                                    <div class="col-sm-5 control-label">
                                                        <label for="certBSL3Exp--v--${status.index}">Experience in certification of a BSL-3 Facility.</label>
                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                    </div>
                                                    <div class="col-sm-6 col-md-7">
                                                        <textarea autocomplete="off" name="certBSL3Exp--v--${status.index}" id="certBSL3Exp--v--${status.index}" maxlength="1000" style="width: 100%">
                                                            <c:out value="${cer.certBSL3Exp}"/>
                                                        </textarea>
                                                        <span data-err-ind="certBSL3Exp--v--${status.index}" class="error-msg"></span>
                                                    </div>
                                                </div>

                                                <div class="form-group ">
                                                    <div class="col-sm-5 control-label">
                                                        <label for="commBSL34Exp--v--${status.index}">Experience in commissioning of a BSL-3/4 facility</label>
                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                    </div>
                                                    <div class="col-sm-6 col-md-7">
                                                        <textarea autocomplete="off" name="commBSL34Exp--v--${status.index}" id="commBSL34Exp--v--${status.index}" maxlength="1000" style="width: 100%">
                                                            <c:out value="${cer.commBSL34Exp}"/>
                                                        </textarea>
                                                        <span data-err-ind="commBSL34Exp--v--${status.index}" class="error-msg"></span>
                                                    </div>
                                                </div>

                                                <div class="form-group ">
                                                    <div class="col-sm-5 control-label">
                                                        <label for="otherBSL34Exp--v--${status.index}">Experience in other BSL-3/4 related activities</label>
                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                    </div>
                                                    <div class="col-sm-6 col-md-7">
                                                        <textarea autocomplete="off" name="otherBSL34Exp--v--${status.index}" id="otherBSL34Exp--v--${status.index}" maxlength="1000" style="width: 100%">
                                                            <c:out value="${cer.otherBSL34Exp}"/>
                                                        </textarea>
                                                        <span data-err-ind="otherBSL34Exp--v--${status.index}" class="error-msg"></span>
                                                    </div>
                                                </div>

                                                <div class="form-group ">
                                                    <div class="col-sm-5 control-label">
                                                        <label for="eduBackground--v--${status.index}">Education Background</label>
                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                    </div>
                                                    <div class="col-sm-6 col-md-7">
                                                        <input type="text" autocomplete="off" name="eduBackground--v--${status.index}" id="eduBackground--v--${status.index}" maxlength="500" value='<c:out value="${cer.eduBackground}"/>' />
                                                        <span data-err-ind="eduBackground--v--${status.index}" class="error-msg"></span>
                                                    </div>
                                                </div>

                                                <div class="form-group ">
                                                    <div class="col-sm-5 control-label">
                                                        <label for="proActivities--v--${status.index}">Position/Professional activities (facility related)</label>
                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                    </div>
                                                    <div class="col-sm-6 col-md-7">
                                                        <input type="text" autocomplete="off" name="proActivities--v--${status.index}" id="proActivities--v--${status.index}" maxlength="500" value='<c:out value="${cer.proActivities}"/>'/>
                                                        <span data-err-ind="proActivities--v--${status.index}" class="error-msg"></span>
                                                    </div>
                                                </div>

                                                <div class="form-group ">
                                                    <div class="col-sm-5 control-label">
                                                        <label for="proRegAndCert--v--${status.index}">Relevant professional registration and certificates</label>
                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                    </div>
                                                    <div class="col-sm-6 col-md-7">
                                                        <input type="text" autocomplete="off" name="proRegAndCert--v--${status.index}" id="proRegAndCert--v--${status.index}" maxlength="500" value='<c:out value="${cer.proRegAndCert}"/>'/>
                                                        <span data-err-ind="proRegAndCert--v--${status.index}" class="error-msg"></span>
                                                    </div>
                                                </div>

                                                <div class="form-group ">
                                                    <div class="col-sm-5 control-label">
                                                        <label for="facRelatedPub--v--${status.index}">Facility related publications\/researches</label>
                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                    </div>
                                                    <div class="col-sm-6 col-md-7">
                                                        <input type="text" autocomplete="off" name="facRelatedPub--v--${status.index}" id="facRelatedPub--v--${status.index}" maxlength="500" value='<c:out value="${cer.facRelatedPub}"/>'/>
                                                        <span data-err-ind="jobDesignation--v--${status.index}" class="error-msg"></span>
                                                    </div>
                                                </div>
                                            </section>
                                        </c:forEach>
                                        </div>
                                        <div class="form-group">
                                            <div class="col-12">
                                                <a id="addNewSection" style="text-decoration: none" href="javascript:void(0)">+ Add New Team Member</a>
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