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
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-incident.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="common/dashboard.jsp"%>

<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input type="hidden" name="sectionIdx" value="${TableDisplayUtil.indexes(involvedPerson.incidentPersons.size())}">

    <input type="hidden" id="section_repeat_section_idx_name" value="sectionIdx" readonly disabled>
    <input type="hidden" id="section_repeat_section_id_prefix" value="invPersonSection" readonly disabled>
    <input type="hidden" id="section_repeat_header_title_prefix" value="Person " readonly disabled>
    <input type="hidden" id="section_repeat_section_group_id" value="sectionGroup" readonly disabled>
    <input type="hidden" id="section_repeat_separator" value="--v--" readonly disabled>

    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp dashboard-tab">
                        <%@include file="common/InnerNavTab.jsp"%>
                        <div class="tab-content">
                            <div class="tab-pane fade in active" id="tabInvolvedPerson" role="tabpanel" style="background-color: rgba(255, 255, 255, 1);border-radius: 15px;box-shadow: 0 0 15px #00000059;">
                                <div class="panel panel-default">
                                    <div class="form-horizontal">
                                        <div class="container">
                                            <div class="row">
                                                <div class="col-xs-12 col-md-10" style="margin-top: 20px">
                                                    <div id="sectionGroup">
                                                        <c:forEach var="item" items="${involvedPerson.incidentPersons}" varStatus="status">
                                                            <section id="invPersonSection--v--${status.index}">
                                                                <c:if test="${involvedPerson.incidentPersons.size() > 1}">
                                                                    <div class="form-group">
                                                                        <h3 class="col-xs-9 col-sm-10 col-md-11" style="border-bottom: 1px solid black">Person${status.index + 1}</h3>
                                                                        <c:if test="${status.index gt 0}">
                                                                            <div class="col-sm-1"><h4 class="text-danger"><em data-current-idx="${status.index}" class="fa fa-times-circle del-size-36 cursorPointer removeBtn"></em></h4></div>
                                                                        </c:if>
                                                                    </div>
                                                                </c:if>
                                                                <h3>Person(s) Involved in the Adverse Incident</h3>
                                                                <div class="form-group ">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="name--v--${status.index}">Name</label>
                                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" autocomplete="off" name="name--v--${status.index}" id="name--v--${status.index}" value='<c:out value="${item.name}"/>'/>
                                                                        <span data-err-ind="name--v--${status.index}" class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group ">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label>Gender</label>
                                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <div class="col-sm-5" style="margin-top: 8px">
                                                                            <input type="radio" name="gender--v--${status.index}" id="male--v--${status.index}" value="male" <c:if test="${item.gender eq 'male'}">checked="checked"</c:if> />
                                                                            <label for="male--v--${status.index}">Male</label>
                                                                        </div>
                                                                        <div class="col-sm-5" style="margin-top: 8px">
                                                                            <input type="radio" name="gender--v--${status.index}" id="female--v--${status.index}" value="female" <c:if test="${item.gender eq 'female'}">checked="checked"</c:if> />
                                                                            <label for="female--v--${status.index}">Female</label>
                                                                        </div>
                                                                    </div>
                                                                </div>

                                                                <div class="form-group ">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="telNo--v--${status.index}">Tel No</label>
                                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" autocomplete="off" name="telNo--v--${status.index}" id="telNo--v--${status.index}" value='<c:out value="${item.telNo}"/>'/>
                                                                        <span data-err-ind="telNo--v--${status.index}" class="error-msg"></span>
                                                                    </div>
                                                                </div>

                                                                <div class="form-group ">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="designation--v--${status.index}">Designation</label>
                                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" autocomplete="off" name="designation--v--${status.index}" id="designation--v--${status.index}" value='<c:out value="${item.designation}"/>'/>
                                                                        <span data-err-ind="designation--v--${status.index}" class="error-msg"></span>
                                                                    </div>
                                                                </div>

                                                                <div class="form-group ">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label>Was the personnel injured</label>
                                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <div class="col-sm-5" style="margin-top: 8px">
                                                                            <input type="radio" name="personnelInjured--v--${status.index}" id="injuredY--v--${status.index}" value="Y" <c:if test="${item.personnelInjured eq 'Y'}">checked="checked"</c:if> />
                                                                            <label for="injuredY--v--${status.index}">Yes</label>
                                                                        </div>
                                                                        <div class="col-sm-5" style="margin-top: 8px">
                                                                            <input type="radio" name="personnelInjured--v--${status.index}" id="injuredN--v--${status.index}" value="N" <c:if test="${item.personnelInjured eq 'N'}">checked="checked"</c:if> />
                                                                            <label for="injuredN--v--${status.index}">No</label>
                                                                        </div>
                                                                    </div>
                                                                </div>

                                                                <div class="form-group ">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label>Personnel Involvement</label>
                                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <div class="col-sm-5" style="margin-top: 8px">
                                                                            <input type="radio" name="personnelInvolvement--v--${status.index}" id="directly--v--${status.index}" value="directly" <c:if test="${item.personnelInvolvement eq 'directly'}">checked="checked"</c:if> />
                                                                            <label for="directly--v--${status.index}">Directly Involved</label>
                                                                        </div>
                                                                        <div class="col-sm-5" style="margin-top: 8px">
                                                                            <input type="radio" name="personnelInvolvement--v--${status.index}" id="indirectly--v--${status.index}" value="indirectly" <c:if test="${item.personnelInvolvement eq 'indirectly'}">checked="checked"</c:if> />
                                                                            <label for="indirectly--v--${status.index}">Indirectly Involved</label>
                                                                        </div>
                                                                    </div>
                                                                </div>

                                                                <div class="form-group ">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="involvementDesc--v--${status.index}">Description of involvement e.g. type of injury, exposure to biological agent</label>
                                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" autocomplete="off" name="involvementDesc--v--${status.index}" id="involvementDesc--v--${status.index}" value='<c:out value="${item.involvementDesc}"/>'/>
                                                                        <span data-err-ind="involvementDesc--v--${status.index}" class="error-msg"></span>
                                                                    </div>
                                                                </div>

                                                                <div class="form-group ">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label>Was the person involved sent for medical consultation/treatment</label>
                                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <div class="col-sm-5" style="margin-top: 8px">
                                                                            <input type="radio" name="medicalPerson--v--${status.index}" id="medicalY--v--${status.index}" value="Y" <c:if test="${item.medicalPerson eq 'Y'}">checked="checked"</c:if> />
                                                                            <label for="medicalY--v--${status.index}">Yes</label>
                                                                        </div>
                                                                        <div class="col-sm-5" style="margin-top: 8px">
                                                                            <input type="radio" name="medicalPerson--v--${status.index}" id="medicalN--v--${status.index}" value="N" <c:if test="${item.medicalPerson eq 'N'}">checked="checked"</c:if> />
                                                                            <label for="medicalN--v--${status.index}">No</label>
                                                                        </div>
                                                                    </div>
                                                                </div>

                                                                <h3>Details Related to Medical Consultation/Treatment</h3>

                                                                <div class="form-group ">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="practitionerName--v--${status.index}">Name of Medical Practitioner</label>
                                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" autocomplete="off" name="practitionerName--v--${status.index}" id="practitionerName--v--${status.index}" value='<c:out value="${item.practitionerName}"/>'/>
                                                                        <span data-err-ind="practitionerName--v--${status.index}" class="error-msg"></span>
                                                                    </div>
                                                                </div>

                                                                <div class="form-group ">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="hospitalName--v--${status.index}">Name of Hospital/Clinic where medical consultation was sought</label>
                                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" autocomplete="off" name="hospitalName--v--${status.index}" id="hospitalName--v--${status.index}" value='<c:out value="${item.hospitalName}"/>'/>
                                                                        <span data-err-ind="hospitalName--v--${status.index}" class="error-msg"></span>
                                                                    </div>
                                                                </div>

                                                                <div class="form-group ">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="medicalDesc--v--${status.index}">Description</label>
                                                                        <a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="" data-original-title="<p>MOH Biosafety Branch is to be informed as soon as possible of positive test results which exposure or infection.</p>">i</a>
                                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" autocomplete="off" name="medicalDesc--v--${status.index}" id="medicalDesc--v--${status.index}" value='<c:out value="${item.medicalDesc}"/>'/>
                                                                        <span data-err-ind="medicalDesc--v--${status.index}" class="error-msg"></span>
                                                                    </div>
                                                                </div>

                                                                <div class="form-group ">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label>Is subsequent medical follow-up required/advised? </label>
                                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <div class="col-sm-5" style="margin-top: 8px">
                                                                            <input type="radio" name="medicalFollowup--v--${status.index}" id="medFollowupY--v--${status.index}" value="Y" <c:if test="${item.medicalFollowup eq 'Y'}">checked="checked"</c:if> />
                                                                            <label for="medFollowupY--v--${status.index}">Yes</label>
                                                                        </div>
                                                                        <div class="col-sm-5" style="margin-top: 8px">
                                                                            <input type="radio" name="medicalFollowup--v--${status.index}" id="medFollowupN--v--${status.index}" value="N" <c:if test="${item.medicalFollowup eq 'N'}">checked="checked"</c:if> />
                                                                            <label for="medFollowupN--v--${status.index}">No</label>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </section>
                                                        </c:forEach>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <div class="col-12">
                                                    <a id="addNewSection" style="text-decoration: none" href="javascript:void(0)">Add Involved Personel</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <%@ include file="common/InnerFooter.jsp" %>

                            <%@include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp"%>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>