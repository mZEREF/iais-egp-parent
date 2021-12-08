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
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-facility-register.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-facility-register.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="dashboard.jsp"%>

<%--@elvariable id="facCommittee" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeDto"--%>
<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input type="hidden" name="sectionIdx" value="${TableDisplayUtil.indexes(facCommittee.facCommitteePersonnelList.size())}">

    <input type="hidden" id="section_repeat_section_idx_name" value="sectionIdx" readonly disabled>
    <input type="hidden" id="section_repeat_section_id_prefix" value="committeeSection" readonly disabled>
    <input type="hidden" id="section_repeat_header_title_prefix" value="Biosafety Committee " readonly disabled>
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
                                        <h3 class="col-12 pl-0" style="border-bottom: 1px solid black">Biosafety Committee</h3>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Multiple Uploading</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <div class="col-sm-5" style="margin-top: 8px">
                                                    <input type="radio" name="inputMethod" id="inputMethodUpload" value="upload" <c:if test="${facCommittee.inputMethod eq 'upload'}">checked="checked"</c:if> />
                                                    <label for="inputMethodUpload">Yes</label>
                                                </div>
                                                <div class="col-sm-5" style="margin-top: 8px">
                                                    <input type="radio" name="inputMethod" id="inputMethodManual" value="manual" <c:if test="${facCommittee.inputMethod eq 'manual'}">checked="checked"</c:if> />
                                                    <label for="inputMethodManual">No</label>
                                                </div>
                                            </div>
                                        </div>

                                        <div id="sectionGroup">
                                            <c:forEach var="personnel" items="${facCommittee.facCommitteePersonnelList}" varStatus="status">
                                                <section id="committeeSection--v--${status.index}">
                                                    <c:if test="${facCommittee.facCommitteePersonnelList.size() > 1}">
                                                        <div class="form-group">
                                                            <h3 class="col-xs-9 col-sm-10 col-md-11" style="border-bottom: 1px solid black">Biosafety Committee ${status.index + 1}</h3>
                                                            <c:if test="${status.index gt 0}">
                                                                <div class="col-sm-1"><h4 class="text-danger"><em data-current-idx="${status.index}" class="fa fa-times-circle del-size-36 cursorPointer removeBtn"></em></h4></div>
                                                            </c:if>
                                                        </div>
                                                    </c:if>
                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="name--v--${status.index}">Name</label>
                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <input maxLength="132" type="text" autocomplete="off" name="name--v--${status.index}" id="name--v--${status.index}" value='<c:out value="${personnel.name}"/>'/>
                                                            <span data-err-ind="name--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="idNumber--v--${status.index}">ID No.</label>
                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                        </div>
                                                        <div class="col-sm-3">
                                                            <select name="idType--v--${status.index}" id="idType--v--${status.index}">
                                                                <option value="IDTYPE001" <c:if test="${personnel.idType eq 'IDTYPE001'}">selected="selected"</c:if>>NRIC</option>
                                                                <option value="IDTYPE002" <c:if test="${personnel.idType eq 'IDTYPE002'}">selected="selected"</c:if>>FIN</option>
                                                                <option value="IDTYPE003" <c:if test="${personnel.idType eq 'IDTYPE003'}">selected="selected"</c:if>>Passport</option>
                                                            </select>
                                                            <span data-err-ind="idType--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                        <div class="col-sm-3 col-md-4">
                                                            <input maxLength="9" type="text" autocomplete="off" name="idNumber--v--${status.index}" id="idNumber--v--${status.index}" value='<c:out value="${personnel.idNumber}"/>'/>
                                                            <span data-err-ind="idNumber--v--${status.index}" class="error-msg"></span>
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
                                                                    <option value="${na.value}" <c:if test="${personnel.nationality eq na.value}">selected="selected"</c:if>>${na.text}</option>
                                                                </c:forEach>
                                                            </select>
                                                            <span data-err-ind="nationality--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="designation--v--${status.index}">Designation</label>
                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <input maxLength="66" type="text" autocomplete="off" name="designation--v--${status.index}" id="designation--v--${status.index}" value='<c:out value="${personnel.designation}"/>'/>
                                                            <span data-err-ind="designation--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="contactNo--v--${status.index}">Contact No.</label>
                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <input maxLength="20" type="text" autocomplete="off" name="contactNo--v--${status.index}" id="contactNo--v--${status.index}" value='<c:out value="${personnel.contactNo}"/>'/>
                                                            <span data-err-ind="contactNo--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="email--v--${status.index}">Email Address</label>
                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <input maxLength="66" type="text" autocomplete="off" name="email--v--${status.index}" id="email--v--${status.index}" value='<c:out value="${personnel.email}"/>'/>
                                                            <span data-err-ind="email--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="employmentStartDate--v--${status.index}">Employment Start Date</label>
                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <input type="text" autocomplete="off" name="employmentStartDate--v--${status.index}" id="employmentStartDate--v--${status.index}" data-date-start-date="01/01/1900" value="<c:out value="${personnel.employmentStartDate}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                            <span data-err-ind="employmentStartDate--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="expertiseArea--v--${status.index}">Area of Expertise</label>
                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <input maxLength="100" type="text" autocomplete="off" name="expertiseArea--v--${status.index}" id="expertiseArea--v--${status.index}" value='<c:out value="${personnel.expertiseArea}"/>'/>
                                                            <span data-err-ind="expertiseArea--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="role--v--${status.index}">Nationality</label>
                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <select name="role--v--${status.index}" id="role--v--${status.index}">
                                                                <c:forEach items="${personnelRoleOps}" var="role">
                                                                    <option value="${role.value}" <c:if test="${personnel.role eq role.value}">selected="selected"</c:if>>${role.text}</option>
                                                                </c:forEach>
                                                            </select>
                                                            <span data-err-ind="role--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label>Is this person is Employee of the Company?</label>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <div class="col-sm-4 col-md-2" style="margin-top: 8px">
                                                                <label for="isAnEmployee--v--${status.index}">Yes</label>
                                                                <input type="radio" name="employee--v--${status.index}" id="isAnEmployee--v--${status.index}" data-custom-ind="committeePersonnelIsEmployee" value="Y" <c:if test="${personnel.employee eq 'Y'}">checked="checked"</c:if> />
                                                            </div>
                                                            <div class="col-sm-4 col-md-2" style="margin-top: 8px">
                                                                <label for="notAnEmployee--v--${status.index}">No</label>
                                                                <input type="radio" name="employee--v--${status.index}" id="notAnEmployee--v--${status.index}" data-custom-ind="committeePersonnelIsEmployee" value="N" <c:if test="${personnel.employee eq 'N'}">checked="checked"</c:if> />
                                                            </div>
                                                            <span data-err-ind="employee--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group" id="committeeExternalCompNameDiv--v--${status.index}" <c:if test="${personnel.employee ne 'N'}">style="display: none"</c:if>>
                                                        <div class="col-sm-5 control-label">
                                                            <label for="externalCompName--v--${status.index}">Name of External Company</label>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <input type="text" autocomplete="off" name="externalCompName--v--${status.index}" id="externalCompName--v--${status.index}" value='<c:out value="${personnel.externalCompName}"/>'/>
                                                            <span data-err-ind="externalCompName--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>
                                                </section>
                                            </c:forEach>
                                        </div>
                                        <div class="form-group">
                                            <div class="col-12">
                                                <a id="addNewSection" style="text-decoration: none" href="javascript:void(0)">+ Add New Biosafety Committee</a>
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