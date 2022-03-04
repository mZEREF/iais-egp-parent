<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-display-or-not.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-facility-certifier-register.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-renewal-facility-certifier-register.js"></script>

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
                        <%@ include file="renewalInnerNavTab.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div id="reviewPanel" role="tabpanel">
                                    <div class="preview-gp">
                                        <div class="row">
                                            <div class="col-xs-12">
                                                <p>Please review your Facility Certifier Registration details.</p>
                                                <p>Click Edit to make necessary changes before renewal where applicable.</p>
                                                <br/>
                                                <div class="panel-group" role="tablist" aria-multiselectable="true">
                                                    <div class="panel panel-default">
                                                        <div class="panel-heading completed">
                                                            <h4 class="panel-title">
                                                                <a class="collapsed" data-toggle="collapse" href="#previewFacInfo">Facility Informations</a>
                                                            </h4>
                                                        </div>
                                                        <div id="previewFacInfo" class="panel-collapse collapse">
                                                            <div class="panel-body">
                                                                <div class="text-right app-font-size-16"><a href="#" review-edit-data-step-key="orgInfo_orgProfile"><em class="fa fa-pencil-square-o"></em>Edit</a></div>
                                                                <div class="panel-main-content form-horizontal min-row">
                                                                    <div class="form-group">
                                                                        <div class="col-10"><strong>Facility Profile</strong></div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Facility Name</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgProfile.orgName}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Facility Address</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgProfile.streetName} ${orgProfile.building} ${orgProfile.floor} - ${orgProfile.unitNo} ${orgProfile.postalCode}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Is the facility a Protected Place?</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="text-right app-font-size-16"><a href="#" review-edit-data-step-key="orgInfo_orgCerTeam"><em class="fa fa-pencil-square-o"></em>Edit</a></div>
                                                                <div class="panel-main-content form-horizontal min-row">
                                                                    <div class="form-group">
                                                                        <div class="col-10"><strong>Facility Certifier Team Member</strong></div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <c:forEach var="tMember" items="${orgCerTeam.certifierTeamMemberList}" varStatus="status">
                                                                        <div>
                                                                            <c:if test="${orgCerTeam.certifierTeamMemberList.size() > 1}">
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">Team Member ${status.index + 1}</label>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                            </c:if>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Name</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${tMember.memberName}</p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">NRIC/FIN</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${tMember.idType}</p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Date of Birth</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${tMember.birthDate}</p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Sex</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${tMember.sex}</p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Nationality</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${tMember.nationality}</p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Job Designation</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${tMember.jobDesignation}</p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Contect No.</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${tMember.telNo}</p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                        </div>
                                                                    </c:forEach>
                                                                </div>
                                                                <div class="text-right app-font-size-16"><a href="#" review-edit-data-step-key="orgInfo_orgAdmin"><em class="fa fa-pencil-square-o"></em>Edit</a></div>
                                                                <div class="panel-main-content form-horizontal min-row">
                                                                    <div class="form-group">
                                                                        <div class="col-10"><strong>Facility Administrator</strong></div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Main Administrator</label>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Name</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgAdmin.mainAdmin.adminName}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">NRIC/FIN</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgAdmin.mainAdmin.idNo}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Nationality</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgAdmin.mainAdmin.nationality}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Designation</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgAdmin.mainAdmin.designation}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Contect No.</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgAdmin.mainAdmin.contactNo}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Email Address</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgAdmin.mainAdmin.email}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Employment Start Date</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgAdmin.mainAdmin.employmentStartDate}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                    </div>
                                                                    <div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Alternative Administrator</label>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Name</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgAdmin.alternativeAdmin.adminName}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">NRIC/FIN</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgAdmin.alternativeAdmin.idNo}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Nationality</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgAdmin.alternativeAdmin.nationality}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Designation</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgAdmin.alternativeAdmin.designation}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Contect No.</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgAdmin.alternativeAdmin.contactNo}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Email Address</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgAdmin.alternativeAdmin.email}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Employment Start Date</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgAdmin.alternativeAdmin.employmentStartDate}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="panel panel-default">
                                                        <div class="panel-heading completed">
                                                            <h4 class="panel-title">
                                                                <a class="collapsed" data-toggle="collapse" href="#previewDocs">Primary Documents</a>
                                                            </h4>
                                                        </div>
                                                        <div id="previewDocs" class="panel-collapse collapse">
                                                            <div class="panel-body">
                                                                <div class="text-right app-font-size-16"><a href="#" review-edit-data-step-key="primaryDoc"><em class="fa fa-pencil-square-o"></em>Edit</a></div>
                                                                <div class="panel-main-content form-horizontal min-row">
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
                                                                                        <div class="col-10"><p><a href="/bsb-fe/ajax/doc/download/facCertifierReg/repo/${repoId}">${file.filename}</a>(<fmt:formatNumber value="${file.size/1024.0}" type="number" pattern="0.0"/>KB)</p></div>
                                                                                        <div class="clear"></div>
                                                                                    </div>
                                                                                </c:forEach>
                                                                                <c:forEach var="file" items="${newFileList}">
                                                                                    <c:set var="tmpId"><iais:mask name="file" value="${file.tmpId}"/></c:set>
                                                                                    <div class="form-group">
                                                                                        <div class="col-10"><p><a href="/bsb-fe/ajax/doc/download/facCertifierReg/new/${tmpId}">${file.filename}</a>(<fmt:formatNumber value="${file.size/1024.0}" type="number" pattern="0.0"/>KB)</p></div>
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
                                    <div class="text-right"><a id="edit" href="javascript:void(0)"><em class="fa fa-pencil-square-o"></em>Edit</a></div>
                                    <div class="form-horizontal" style="padding: 30px 20px 10px;">
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="remarks">Remarks</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <textarea class="col-xs-12" name="remarks" id="remarks" rows="5"><c:out value="${previewSubmit.remarks}"/></textarea>
                                                <span data-err-ind="remarks" class="error-msg"></span>
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="effectiveDateOfChange">Effective Date of Change</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input type="text" autocomplete="off" name="effectiveDateOfChange" id="effectiveDateOfChange" data-date-start-date="01/01/1900" value="<c:out value="${review.effectiveDateOfChange}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                <span data-err-ind="effectiveDateOfChange" class="error-msg"></span>
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <div class="col-xs-1" style="padding: 30px 0 20px 30px;">
                                                <input type="checkbox" name="require" id="require" value="Y" <c:if test="${previewSubmit.require eq 'Y'}">checked = checked</c:if>/>
                                            </div>
                                            <div class="col-xs-10 control-label" style="padding: 30px 0">
                                                <label for="require" >Declaration of compliance with MOH requirements including those stipulatedin the checklist</label>
                                                <span data-err-ind="require" class="error-msg"></span>
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <div class="col-xs-1" style="padding: 30px 0 0 30px;">
                                                <input type="checkbox" name="accuracy" id="accuracy" value="Y" <c:if test="${previewSubmit.accuracy eq 'Y'}">checked = checked</c:if>/>
                                            </div>
                                            <div class="col-xs-10 control-label" style="padding: 30px 0">
                                                <label for="accuracy">Declaration on the accuracy of submission</label>
                                                <span data-err-ind="accuracy" class="error-msg"></span>
                                            </div>
                                        </div>
                                        <div class="modal fade" id="submitDeclareModal" role="dialog" aria-labelledby="myModalLabel">
                                            <div class="modal-dialog modal-dialog-centered" role="document">
                                                <div class="modal-content">
                                                    <div class="modal-body">
                                                        <div class="row">
                                                            <div class="col-md-12"><span style="font-size: 2rem">Please check the declaration box</span></div>
                                                        </div>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-secondary btn-md" data-dismiss="modal">Close</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="application-tab-footer">
                                    <div class="row">
                                        <div class="col-xs-12 col-sm-6 ">
                                            <a class="back" id="back" href="#"><em class="fa fa-angle-left"></em> Previous</a>
                                        </div>
                                        <div class="col-xs-12 col-sm-6">
                                            <div class="button-group">
                                                <a class="btn btn-secondary" href="javascript:void(0);">PRINT</a>
                                                <a class="btn btn-secondary" id="saveDraft" >Save as Draft</a>
                                                <a class="btn btn-primary next" id="submit" >SUBMIT</a>
                                            </div>
                                        </div>
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