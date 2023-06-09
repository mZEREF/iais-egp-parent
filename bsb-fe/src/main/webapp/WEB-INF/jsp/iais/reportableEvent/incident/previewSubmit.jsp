<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-incident.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="common/dashboard.jsp" %>
<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <div class="main-content">
        <div class="form-horizontal">
            <div class="container">
                <div class="row">
                    <div class="col-xs-12">
                        <h3 style="border-bottom: 1px solid #BABABA">Preview & Submit</h3>
                        <div class="preview-gp">
                            <div class="row">
                                <div class="col-xs-12">
                                    <div class="panel-group" role="tablist" aria-multiselectable="true">
                                        <div class="panel panel-default">
                                            <div class="panel-heading completed">
                                                <h4 class="panel-title">Incident Form</h4>
                                            </div>
                                            <div id="previewFacInfo" class="panel-collapse in collapse">
                                                <div class="panel-body">
                                                    <div class="text-right app-font-size-16"><a href="#"><em
                                                            class="fa fa-pencil-square-o"></em>Edit</a>
                                                    </div>
                                                    <div class="panel-main-content form-horizontal min-row">
                                                        <div>
                                                            <div class="form-group">
                                                                <label class="col-xs-5 col-md-4 control-label">Incident Reference No.</label>
                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                    <p>${incidentInfo.referenceNo}</p>
                                                                </div>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-xs-5 col-md-4 control-label">Reporting of Incident</label>
                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                    <p>${incidentInfo.incidentReporting}</p>
                                                                </div>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-xs-5 col-md-4 control-label">Is the facility a Protected Place?</label>
                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                    <p>${incidentInfo.incidentType}</p>
                                                                </div>
                                                                <div class="clear"></div>
                                                            </div>
                                                        </div>
                                                        <div class="form-group">
                                                            <div class="col-10"><h3>Person Reporting The Adverse Incident</h3></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div>
                                                            <div class="form-group">
                                                                <label class="col-xs-5 col-md-4 control-label">Name (as per NRIC/FIN)</label>
                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                    <p>${reportingPerson.name}</p>
                                                                </div>
                                                                <div class="clear"></div>
                                                            </div>

                                                            <div class="form-group">
                                                                <label class="col-xs-5 col-md-4 control-label">Name of Organization</label>
                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                    <p>${reportingPerson.orgName}</p>
                                                                </div>
                                                                <div class="clear"></div>
                                                            </div>

                                                            <div class="form-group">
                                                                <label class="col-xs-5 col-md-4 control-label">Address</label>
                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                    <p>${reportingPerson.address}</p>
                                                                </div>
                                                                <div class="clear"></div>
                                                            </div>

                                                            <div class="form-group">
                                                                <label class="col-xs-5 col-md-4 control-label">Tel No. (Office)</label>
                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                    <p>${reportingPerson.officeTelNo}</p>
                                                                </div>
                                                                <div class="clear"></div>
                                                            </div>

                                                            <div class="form-group">
                                                                <label class="col-xs-5 col-md-4 control-label">Tel No. (Mobile)</label>
                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                    <p>${reportingPerson.mobileTelNo}</p>
                                                                </div>
                                                                <div class="clear"></div>
                                                            </div>

                                                            <div class="form-group">
                                                                <label class="col-xs-5 col-md-4 control-label">Email Address</label>
                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                    <p>${reportingPerson.email}</p>
                                                                </div>
                                                                <div class="clear"></div>
                                                            </div>

                                                            <div class="form-group">
                                                                <label class="col-xs-5 col-md-4 control-label">Role & Designation</label>
                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                    <p>${reportingPerson.roleDesignation}</p>
                                                                </div>
                                                                <div class="clear"></div>
                                                            </div>

                                                            <div class="form-group">
                                                                <label class="col-xs-5 col-md-4 control-label">Name of Agent or Toxin Involved</label>
                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                    <p><c:forEach var="item" items="${reportingPerson.batName}" varStatus="status">
                                                                        <c:choose>
                                                                            <c:when test="${status.index+1 eq reportingPerson.batName.size()}">
                                                                                <c:out value="${item}"/>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <c:out value="${item},"/>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </c:forEach></p>
                                                                </div>
                                                                <div class="clear"></div>
                                                            </div>

                                                            <div class="form-group">
                                                                <div class="col-sm-5 col-md-4 control-label">
                                                                    <label >Description of Incident</label>
                                                                    <a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="" data-original-title="<p>Note:The driver of hte conveyance must have a valid Hazardous Materials Transport Driver Permit</p>">i</a>
                                                                </div>
                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                    <p>${reportingPerson.incidentDesc}</p>
                                                                </div>
                                                                <div class="clear"></div>
                                                            </div>

                                                            <div class="form-group">
                                                                <label class="col-xs-5 col-md-4 control-label">Any possibility of BA/Toxin released beyond the containment facility?</label>
                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                    <p><c:choose>
                                                                        <c:when test="${reportingPerson.batReleasePossibility eq 'Y'}"><c:out value="Yes"/></c:when>
                                                                        <c:when test="${reportingPerson.batReleasePossibility eq 'N'}"><c:out value="No"/></c:when>
                                                                    </c:choose></p>
                                                                </div>
                                                                <div class="clear"></div>
                                                            </div>

                                                            <div class="form-group">
                                                                <label class="col-xs-5 col-md-4 control-label">Was any personnel involved in the incident?</label>
                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                    <p><c:choose>
                                                                        <c:when test="${reportingPerson.incidentPersonInvolved eq 'Y'}"><c:out value="Yes"/></c:when>
                                                                        <c:when test="${reportingPerson.incidentPersonInvolved eq 'N'}"><c:out value="No"/></c:when>
                                                                    </c:choose></p>
                                                                </div>
                                                                <div class="clear"></div>
                                                            </div>

                                                            <c:if test="${reportingPerson.incidentPersonInvolved ne null}">
                                                                <div class="form-group">
                                                                    <div class="col-sm-5 col-md-4 control-label">
                                                                        <label >Number of personnel involved or affected</label>
                                                                        <a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="" data-original-title="<p>Note:The driver of hte conveyance must have a valid Hazardous Materials Transport Driver Permit</p>">i</a>
                                                                    </div>
                                                                    <div class="col-sm-7 col-md-5 col-xs-7">
                                                                        <p>${reportingPerson.incidentPersonInvolvedCount}</p>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>
                                                            </c:if>


                                                            <c:forEach var="item" items="${involvedPerson.incidentPersons}" varStatus="status">
                                                            <div class="row">
                                                                <h3>Person ${status.index+1}</h3>
                                                                <div class="form-group">
                                                                    <div class="col-10"><h4>Person(s) Involved in the Adverse Incident</h4></div>
                                                                    <div class="clear"></div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label class="col-xs-5 col-md-4 control-label">Name (as per NRIC/FIN)</label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-7">
                                                                        <p>${item.name}</p>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>

                                                                <div class="form-group">
                                                                    <label class="col-xs-5 col-md-4 control-label">Tel No</label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-7">
                                                                        <p>${item.telNo}</p>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>

                                                                <div class="form-group">
                                                                    <label class="col-xs-5 col-md-4 control-label">Designation</label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-7">
                                                                        <p>${item.designation}</p>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>

                                                                <div class="form-group">
                                                                    <label class="col-xs-5 col-md-4 control-label">Was the personnel injured</label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-7">
                                                                        <p><c:choose>
                                                                            <c:when test="${item.personnelInjured eq 'Y'}"><c:out value="Yes"/></c:when>
                                                                            <c:when test="${item.personnelInjured eq 'N'}"><c:out value="No"/></c:when>
                                                                        </c:choose></p>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>

                                                                <div class="form-group">
                                                                    <label class="col-xs-5 col-md-4 control-label">Personnel Involvement</label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-7">
                                                                        <p><c:choose>
                                                                            <c:when test="${item.personnelInvolvement eq 'directly'}"><c:out value="Directly Involved"/></c:when>
                                                                            <c:when test="${item.personnelInvolvement eq 'indirectly'}"><c:out value="Indirectly Involved"/></c:when>
                                                                        </c:choose></p>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>

                                                                <div class="form-group">
                                                                    <label class="col-xs-5 col-md-4 control-label">Description of involvement e.g. type of injury, exposure to biological agent</label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-7">
                                                                        <p>${item.involvementDesc}</p>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>

                                                                <div class="form-group">
                                                                    <div class="col-10"><h4>Details Related to Medical Consultation/Treatment</h4></div>
                                                                    <div class="clear"></div>
                                                                </div>

                                                                <div class="form-group">
                                                                    <label class="col-xs-5 col-md-4 control-label">Name of Medical Practitioner</label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-7">
                                                                        <p>${item.practitionerName}</p>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>

                                                                <div class="form-group">
                                                                    <label class="col-xs-5 col-md-4 control-label">Name of Hospital/Clinic where medical consultation was sought</label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-7">
                                                                        <p>${item.hospitalName}</p>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>

                                                                <div class="form-group">
                                                                    <label class="col-xs-5 col-md-4 control-label">Description</label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-7">
                                                                        <p>${item.medicalDesc}</p>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>

                                                                <div class="form-group">
                                                                    <label class="col-xs-5 col-md-4 control-label">Is subsequent medical follow-up required/advised?</label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-7">
                                                                        <p><c:choose>
                                                                            <c:when test="${item.medicalFollowup eq 'Y'}"><c:out value="Yes"/></c:when>
                                                                            <c:when test="${item.medicalFollowup eq 'N'}"><c:out value="No"/></c:when>
                                                                        </c:choose></p>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>
                                                            </div>
                                                            </c:forEach>

                                                            <div class="form-group">
                                                                <div class="col-10"><h3>Attachments</h3></div>
                                                                <div class="clear"></div>
                                                            </div>

                                                            <c:forEach var="doc" items="${docSettings}">
                                                                <c:set var="savedFileList" value="${savedFiles.get(doc.type)}" />
                                                                <c:set var="newFileList" value="${newFiles.get(doc.type)}" />
                                                                <c:if test="${not empty savedFileList or not empty newFileList}">
                                                                    <div class="form-group">
                                                                        <div class="col-10"><strong>${doc.typeDisplay}</strong></div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div>
                                                                        <c:forEach var="file" items="${savedFileList}">
                                                                            <c:set var="repoId"><iais:mask name="file" value="${file.repoId}"/></c:set>
                                                                            <div class="form-group">
                                                                                <div class="col-10"><p><a href="/bsb-web/ajax/doc/download/incidentNotification/repo/${repoId}">${file.filename}</a>(<fmt:formatNumber value="${file.size/1024.0}" type="number" pattern="0.0"/>KB)</p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                        </c:forEach>
                                                                        <c:forEach var="file" items="${newFileList}">
                                                                            <c:set var="tmpId"><iais:mask name="file" value="${file.tmpId}"/></c:set>
                                                                            <div class="form-group">
                                                                                <div class="col-10"><p><a href="/bsb-web/ajax/doc/download/incidentNotification/new/${tmpId}">${file.filename}</a>(<fmt:formatNumber value="${file.size/1024.0}" type="number" pattern="0.0"/>KB)</p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                        </c:forEach>
                                                                    </div>
                                                                </c:if>
                                                            </c:forEach>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="application-tab-footer">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 ">
                                    <a class="back" id="back" href="#"><em class="fa fa-angle-left"></em> Back</a>
                                </div>
                                <div class="col-xs-12 col-sm-6">
                                    <div class="button-group">
                                        <a class="btn btn-secondary" id="saveDraft" >Save as Draft</a>
                                        <a class="btn btn-primary next" id="next" >Save</a>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <%@include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp"%>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>