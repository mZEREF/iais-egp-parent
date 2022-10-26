<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
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
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-cancel-de-reg-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-de-registration-facility.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>
<%@include file="../dashboard.jsp"%>
<div class="main-content">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="instruction-content center-content">
                    <form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
                        <div class="row form-horizontal">
                            <input type="hidden" name="action_type" value="">
                            <input type="hidden" name="action_value" value="">
                            <div class="col-lg-12 col-xs-12 cesform-box">
                                <div class="row">
                                    <div class="col-lg-12 col-xs-12">
                                        <div class="table-gp tablebox">
                                            <div class="form-group ">
                                                <label class="col-sm-5 control-label">Facility Name</label>
                                                <div class="col-sm-6 col-md-7"><c:out value="${deRegistrationFacilityDto.facilityName}"/></div>
                                            </div>
                                            <div class="form-group ">
                                                <label class="col-sm-5 control-label">Facility Address</label>
                                                <div class="col-sm-6 col-md-7"><c:out value="${deRegistrationFacilityDto.facilityAddress}"/></div>
                                            </div>
                                            <div class="form-group ">
                                                <label class="col-sm-5 control-label">Facility Classification</label>
                                                <div class="col-sm-6 col-md-7"><iais:code code="${deRegistrationFacilityDto.facilityClassification}"/></div>
                                            </div>
                                            <div class="form-group ">
                                                <label class="col-sm-5 control-label">Reasons</label>
                                                <div class="col-sm-6 col-md-7"><iais:code code="${deRegistrationFacilityDto.reasons}"/></div>
                                            </div>
                                            <div class="form-group ">
                                                <label class="col-sm-5 control-label">Remarks</label>
                                                <div class="col-sm-6 col-md-7"><c:out value="${deRegistrationFacilityDto.remarks}"/></div>
                                            </div>
                                            <%@include file="../previewDocuments.jsp" %>
                                            <br>
                                            <div class="panel panel-default">
                                                <div class="panel-heading" style="text-align:center; background-color: #c6dff1"><strong>List of Approval(s)</strong></div>
                                                <div class="row">
                                                    <div class="col-xs-12">
                                                        <div class="table-gp">
                                                            <table aria-describedby="" class="table table-bordered" style="margin-bottom: 0">
                                                                <thead>
                                                                <tr>
                                                                    <th scope="col" style="text-align:center;">S/N</th>
                                                                    <th scope="col" style="text-align:center;">Approval Type</th>
                                                                    <th scope="col" style="text-align:center;">Biological Agent/Toxin</th>
                                                                    <th scope="col" style="text-align:center;">Status</th>
                                                                    <th scope="col" style="text-align:center;">Physical Possession of BA/T in Facility</th>
                                                                </tr>
                                                                </thead>
                                                                <tbody style="text-align:center;">
                                                                <c:forEach var="item" items="${deRegistrationFacilityDto.approvalInfoList}" varStatus="status">
                                                                    <tr>
                                                                        <td>
                                                                            <p><c:out value="${status.index + 1}"/></p>
                                                                        </td>
                                                                        <td>
                                                                            <p><iais:code code="${item.approvalType}"/></p>
                                                                        </td>
                                                                        <td>
                                                                            <p><c:out value="${item.biologicalAgentToxin}"/></p>
                                                                        </td>
                                                                        <td>
                                                                            <p><iais:code code="${item.status}"/></p>
                                                                        </td>
                                                                        <td>
                                                                            <p><c:out value="${item.physicalPossession}"/></p>
                                                                        </td>
                                                                    </tr>
                                                                </c:forEach>
                                                                </tbody>
                                                            </table>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="panel-body">
                                                <div class="row">
                                                    <br>
                                                    <p>I, hereby declare and agree, the following:</p>
                                                    <br>
                                                    <div class="form-group " style="z-index: 10">
                                                        <div class="col-xs-1" style="padding: 30px 0 20px 30px;">
                                                            <input type="checkbox" name="declarationReadOnly" value="Y" <c:if test="${deRegistrationFacilityDto.declaration1 eq 'Y'}">checked="checked"</c:if> />
                                                        </div>
                                                        <div class="col-xs-10 control-label">
                                                            <span>My facility meets the relevant requirements listed above for the deregistration of the type of facility indicated in the Deregistration Form.</span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group " style="z-index: 10">
                                                        <div class="col-xs-1" style="padding: 30px 0 20px 30px;">
                                                            <input type="checkbox" name="declarationReadOnly" value="Y" <c:if test="${deRegistrationFacilityDto.declaration2 eq 'Y'}">checked="checked"</c:if> />
                                                        </div>
                                                        <div class="col-xs-10 control-label">
                                                            <span>My facility has been successfully degazetted as a Protected Place (gazette order included as supporting documents with this submission).</span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group " style="z-index: 10">
                                                        <div class="col-xs-1" style="padding: 30px 0 20px 30px;">
                                                            <input type="checkbox" name="declarationReadOnly" value="Y" <c:if test="${deRegistrationFacilityDto.declaration3 eq 'Y'}">checked="checked"</c:if> />
                                                        </div>
                                                        <div class="col-xs-10 control-label">
                                                            <span for="deRegistrationFacilityDto.declaration3">All the information provided in this application is true and accurate.</span>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <%@ include file="../previewInnerFooter.jsp" %>
                        <%@ include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp" %>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>