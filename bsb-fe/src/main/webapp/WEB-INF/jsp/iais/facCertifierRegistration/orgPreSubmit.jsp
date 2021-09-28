<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ page import="sg.gov.moh.iais.egp.bsb.util.AddressUtil"%>
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
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-facility-register.js"></script>

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
                                <div id="previewSubmitPanel" role="tabpanel">
                                    <div class="preview-gp">
                                        <div class="row">
                                            <div class="col-xs-12">
                                                <div class="panel-group" role="tablist" aria-multiselectable="true">
                                                    <div class="panel panel-default">
                                                        <div class="panel-heading completed">
                                                            <h4 class="panel-title">
                                                                <a class="collapsed" data-toggle="collapse" href="#previewFacInfo">Facility Informations</a>
                                                            </h4>
                                                        </div>
                                                        <div id="previewFacInfo" class="panel-collapse collapse">
                                                            <div class="panel-body">
                                                                <div class="text-right app-font-size-16"><a href="#" data-step-key="orgInfo_orgProfile"><em class="fa fa-pencil-square-o"></em>Edit</a></div>
                                                                <div class="panel-main-content form-horizontal min-row">
                                                                    <div class="form-group">
                                                                        <div class="col-10"><strong>Facility Profile</strong></div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Facility Name</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Facility Address</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Is the facility a Protected Place?</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="panel-main-content form-horizontal min-row">
                                                                    <div class="form-group">
                                                                        <div class="col-10"><strong>Facility Operator</strong></div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Facility Operator</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Facility Operator Designee Name</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">NRIC/FIN</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Nationality</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Designation</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Contect No.</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Email</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Employment Start Date</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="panel-main-content form-horizontal min-row">
                                                                    <div class="form-group">
                                                                        <div class="col-10"><strong>Personnel Authorised to Access the Facility</strong></div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <c:forEach var="personnel" items="${facAuth.facAuthPersonnelList}" varStatus="status">
                                                                        <div>
                                                                            <c:if test="${facAuth.facAuthPersonnelList.size() > 1}">
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Personnel ${status.index + 1}</label>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            </c:if>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Name</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">NRIC/FIN</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Nationality</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Designation</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Contect No.</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Email Address</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Employment Start Date</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Employment Period</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${personnel.employmentPeriod}</p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Security Clearance Date</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Area of Work</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                        </div>
                                                                    </c:forEach>
                                                                </div>
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
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">NRIC/FIN</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Nationality</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Designation</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Contect No.</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Email Address</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Employment Start Date</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
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
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">NRIC/FIN</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Nationality</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Designation</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Contect No.</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Email Address</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Employment Start Date</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="panel-main-content form-horizontal min-row">
                                                                    <div class="form-group">
                                                                        <div class="col-10"><strong>Facility Officer</strong></div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Name</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">NRIC/FIN</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Nationality</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Designation</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Contect No.</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Email Address</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Employment Start Date</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="panel-main-content form-horizontal min-row">
                                                                    <div class="form-group">
                                                                        <div class="col-10"><strong>Biosafety Committee</strong></div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <c:forEach var="personnel" items="${facCommittee.facCommitteePersonnelList}" varStatus="status">
                                                                        <div>
                                                                            <c:if test="${facCommittee.facCommitteePersonnelList.size() > 1}">
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">Committee ${status.index + 1}</label>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                            </c:if>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Name</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">NRIC/FIN</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Nationality</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Designation</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Contect No.</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>}</p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Email Address</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Employment Start Date</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Area of Expertise</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Role</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Is this person is Employee of the Company?</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                        </div>
                                                                    </c:forEach>
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
                                                                <div class="text-right app-font-size-16"><a href="#" data-step-key="primaryDocs"><em class="fa fa-pencil-square-o"></em>Edit</a></div>
                                                                <div class="panel-main-content form-horizontal min-row">
                                                                    <div class="form-group">
                                                                        <div class="col-10"><strong>Uploaded Documents</strong></div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-horizontal" style="padding: 30px 20px 10px;">
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="remarks">Remarks</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <textarea class="col-xs-12" name="remarks" id="remarks" rows="5"><c:out value="${1}"/></textarea>
                                                <span data-err-ind="remarks" class="error-msg"></span>
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <div class="col-xs-1" style="padding: 30px 0 20px 30px;">
                                                <input type="checkbox" name="declare" id="chkDeclare"/>
                                            </div>
                                            <div class="col-xs-10 control-label">
                                                <label for="chkDeclare">Declaration of compliance with MOH requirements including those stipulatedin the checklist</label>
                                                <span data-err-ind="chkDeclare" class="error-msg"></span>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-xs-1" style="padding: 30px 0 20px 30px;">
                                                <input type="checkbox" name="declare" id="accDeclare"/>
                                            </div>
                                            <div class="col-xs-10 control-label">
                                                <label for="accDeclare">Declaration on the accuracy of submission</label>
                                                <span data-err-ind="accDeclare" class="error-msg"></span>
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