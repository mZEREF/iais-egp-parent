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
                                                <div class="col-sm-6 col-md-7"><c:out value="${deRegistrationFacilityDto.facilityClassification}"/></div>
                                            </div>
                                            <div class="form-group ">
                                                <label class="col-sm-5 control-label">Reasons <span style="color: red">*</span></label>
                                                <div class="col-sm-6 col-md-7"><c:out value="${deRegistrationFacilityDto.reasons}"/></div>
                                            </div>
                                            <div class="form-group ">
                                                <label class="col-sm-5 control-label">Remarks</label>
                                                <div class="col-sm-6 col-md-7"><c:out value="${deRegistrationFacilityDto.remarks}"/></div>
                                            </div>
                                            <%@include file="../primaryDocuments.jsp" %>
                                            <div class="panel panel-default">
                                                <div class="panel-heading" style="text-align:center;"><strong>List of Approval(s)</strong></div>
                                                <div class="row">
                                                    <div class="col-xs-12">
                                                        <div class="table-gp">
                                                            <table aria-describedby="" class="table table-bordered" style="margin-bottom: 0">
                                                                <thead>
                                                                <tr>
                                                                    <th scope="col" style="text-align:center;width:5%">S/N</th>
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
                                                                            <p><iais:code code="${item.approvalType}"></iais:code></p>
                                                                        </td>
                                                                        <td>
                                                                            <p><c:out value="${item.biologicalAgentToxin}"/></p>
                                                                        </td>
                                                                        <td>
                                                                            <p><c:out value="${item.status}"/></p>
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
                                                            <input type="checkbox" name="deRegistrationFacilityDto.declaration1" id="deRegistrationFacilityDto.declaration1" value="Y" <c:if test="${deRegistrationFacilityDto.declaration1 eq 'Y'}">checked="checked"</c:if> />
                                                        </div>
                                                        <div class="col-xs-10 control-label">
                                                            <span>My facility meets the relevant requirements listed above for the deregistration of the type of facility indicated in the Deregistration Form.</span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group " style="z-index: 10">
                                                        <div class="col-xs-1" style="padding: 30px 0 20px 30px;">
                                                            <input type="checkbox" name="deRegistrationFacilityDto.declaration2" id="deRegistrationFacilityDto.declaration2" value="Y" <c:if test="${deRegistrationFacilityDto.declaration2 eq 'Y'}">checked="checked"</c:if> />
                                                        </div>
                                                        <div class="col-xs-10 control-label">
                                                            <span>My facility has been successfully degazetted as a Protected Place (gazette order included as supporting documents with this submission).</span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group " style="z-index: 10">
                                                        <div class="col-xs-1" style="padding: 30px 0 20px 30px;">
                                                            <input type="checkbox" name="deRegistrationFacilityDto.declaration3" id="deRegistrationFacilityDto.declaration3" value="Y" <c:if test="${deRegistrationFacilityDto.declaration3 eq 'Y'}">checked="checked"</c:if> />
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
                        <div class="application-tab-footer">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 ">
                                    <a class="back" id="back" href="#"><em class="fa fa-angle-left"></em> Back</a>
                                </div>
                                <div class="col-xs-12 col-sm-6">
                                    <div class="button-group">
                                        <a class="btn btn-secondary" id="saveDraft" >Save as Draft</a>
                                        <a class="btn btn-primary next" id="submit" >Next</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>