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
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-file.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>
<%@include file="../dashboard.jsp"%>
<div class="main-content">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="instruction-content center-content">
                    <form method="post" id="mainForm" enctype="multipart/form-data" action="<%=process.runtime.continueURL()%>">
                        <div class="row form-horizontal">
                            <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
                            <input type="hidden" name="action_type" value="">
                            <input type="hidden" name="action_value" value="">
                            <input id="multiUploadTrigger" type="file" multiple="multiple" style="display: none"/>
                            <input id="echoReloadTrigger" type="file" style="display: none"/>
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
                                                <label class="col-sm-5 control-label">Reasons <span style="color: red">*</span></label>
                                                <div class="col-sm-6 col-md-7">
                                                    <select name="reasons" id="reasons">
                                                        <option value="">Please Select</option>
                                                        <option value="BSBRFFD001" <c:if test="${deRegistrationFacilityDto.reasons eq 'BSBRFFD001'}">selected="selected"</c:if>>Moving to a different location</option>
                                                        <option value="BSBRFFD002" <c:if test="${deRegistrationFacilityDto.reasons eq 'BSBRFFD002'}">selected="selected"</c:if>>No longer handling First Schedule biological agent(s)</option>
                                                        <option value="BSBRFFD003" <c:if test="${deRegistrationFacilityDto.reasons eq 'BSBRFFD003'}">selected="selected"</c:if>>No longer wants to maintain certified facility status</option>
                                                        <option value="BSBRFFD004" <c:if test="${deRegistrationFacilityDto.reasons eq 'BSBRFFD004'}">selected="selected"</c:if>>No longer wants to large scale produce biological agent</option>
                                                        <option value="BSBRFFD005" <c:if test="${deRegistrationFacilityDto.reasons eq 'BSBRFFD005'}">selected="selected"</c:if>>No longer handling poliovirus potentially infectious materials</option>
                                                        <option value="BSBRFFD006" <c:if test="${deRegistrationFacilityDto.reasons eq 'BSBRFFD006'}">selected="selected"</c:if>>No longer handling toxin</option>
                                                        <option value="BSBRFFD007" <c:if test="${deRegistrationFacilityDto.reasons eq 'BSBRFFD007'}">selected="selected"</c:if>>Termination of business</option>
                                                        <option value="BSBRFFD008" <c:if test="${deRegistrationFacilityDto.reasons eq 'BSBRFFD008'}">selected="selected"</c:if>>Others, please provide details in Remarks field</option>
                                                    </select>
                                                    <span data-err-ind="reasons" class="error-msg"></span>
                                                </div>
                                            </div>
                                            <div class="form-group ">
                                                <label class="col-sm-5 control-label">Remarks</label>
                                                <div class="col-sm-6 col-md-7">
                                                    <textarea maxLength="300" class="col-xs-12" name="remarks" id="remarks" rows="5"><c:out value="${deRegistrationFacilityDto.remarks}"/></textarea>
                                                    <span data-err-ind="remarks" class="error-msg"></span>
                                                </div>
                                            </div>
                                            <%@include file="../primaryDocuments.jsp" %>
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
                                                            <input type="checkbox" name="declaration1" id="declaration1" value="Y" <c:if test="${deRegistrationFacilityDto.declaration1 eq 'Y'}">checked="checked"</c:if> />
                                                        </div>
                                                        <div class="col-xs-10 control-label">
                                                            <span>My facility meets the relevant requirements listed above for the deregistration of the type of facility indicated in the Deregistration Form.</span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group " style="z-index: 10">
                                                        <div class="col-xs-1" style="padding: 30px 0 20px 30px;">
                                                            <input type="checkbox" name="declaration2" id="declaration2" value="Y" <c:if test="${deRegistrationFacilityDto.declaration2 eq 'Y'}">checked="checked"</c:if> />
                                                        </div>
                                                        <div class="col-xs-10 control-label">
                                                            <span>My facility has been successfully degazetted as a Protected Place (gazette order included as supporting documents with this submission).</span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group " style="z-index: 10">
                                                        <div class="col-xs-1" style="padding: 30px 0 20px 30px;">
                                                            <input type="checkbox" name="declaration3" id="declaration3" value="Y" <c:if test="${deRegistrationFacilityDto.declaration3 eq 'Y'}">checked="checked"</c:if> />
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
                        <%@ include file="../prepareInnerFooter.jsp" %>
                        <%@ include file="../submitDeclareModal.jsp" %>
                        <%@ include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp" %>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>