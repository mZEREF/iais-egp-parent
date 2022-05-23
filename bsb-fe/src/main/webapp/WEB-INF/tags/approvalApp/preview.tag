<%@tag description="Preview page of approval app" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>

<%@attribute name="facProfileDto" required="true" type="sg.gov.moh.iais.egp.bsb.dto.register.approval.FacProfileDto" %>
<%@attribute name="batInfo" required="true" type="java.lang.Object" %>
<%@attribute name="facAuthorisedList" required="false" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.register.approval.FacAuthorisedDto>" %>
<%@attribute name="processType" type="java.lang.String" %>

<%@attribute name="docFrag" fragment="true" %>
<%@attribute name="editFrag" fragment="true" %>
<%@attribute name="facProfileEditJudge" type="java.lang.Boolean" %>
<%@attribute name="batListEditJudge" type="java.lang.Boolean" %>
<%@attribute name="docEditJudge" type="java.lang.Boolean" %>

<jsp:invoke fragment="editFrag" var="editFragString"/>

<iais-bsb:global-constants classFullName="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" attributeKey="masterCodeConstants"/>
<%--@elvariable id="masterCodeConstants" type="java.util.Map<java.lang.String, java.lang.Object>"--%>
<div class="preview-gp">
    <div class="row">
        <div class="col-xs-12">
            <div class="panel-group" role="tablist" aria-multiselectable="true">
                <div class="panel panel-default">
                    <div class="panel-heading completed">
                        <h4 class="panel-title">
                            <a class="collapsed" data-toggle="collapse" href="#previewBatInfo">Application Information</a>
                        </h4>
                    </div>
                    <div id="previewBatInfo" class="panel-collapse collapse">
                        <div class="panel-body">
                            <c:if test="${facProfileEditJudge}"><div class="text-right app-font-size-16">${fn:replace(editFragString, "REPLACE-STEP-KEY", "appInfo_facProfile")}</div></c:if>
                            <div class="panel-main-content form-horizontal min-row">
                                <div class="form-group">
                                    <div class="col-10"><strong>Facility Profile</strong></div>
                                    <div class="clear"></div>
                                </div>
                                <div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Facility Name</label>
                                        <div class="col-xs-6"><p>${facProfileDto.facilityName}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Facility Classification:</label>
                                        <div class="col-xs-6"><p>${facProfileDto.facilityClassification}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Existing Facility Activity Type Approval</label>
                                        <div class="col-xs-6"><c:forEach items="${facProfileDto.existFacActivityTypeApprovalList}" var="approvalActivity">
                                            <li><iais:code code="${approvalActivity}"/></li>
                                        </c:forEach></div>
                                        <div class="clear"></div>
                                    </div>
                                </div>
                            </div>
                            <c:choose>
                                <c:when test="${processType eq masterCodeConstants.PROCESS_TYPE_APPROVE_POSSESS}">
                                    <c:if test="${batListEditJudge}"><div class="text-right app-font-size-16">${fn:replace(editFragString, "REPLACE-STEP-KEY", "appInfo_possessBat")}</div></c:if>
                                    <%--@elvariable id="batInfo" type="sg.gov.moh.iais.egp.bsb.dto.register.bat.BiologicalAgentToxinDto"--%>
                                    <div class="panel-main-content form-horizontal min-row">
                                        <div class="form-group" style="margin-top: 10px">
                                            <div class="col-10"><strong>Details of Biological Agent / Toxin</strong></div>
                                            <div class="clear"></div>
                                        </div>
                                        <c:forEach var="info" items="${batInfo.batInfos}">
                                            <c:set var="BAorT" value='${masterCodeConstants.FIFTH_SCHEDULE eq info.schedule ? "Toxin" : "Biological Agent"}'/>
                                            <div>
                                                <c:if test="${masterCodeConstants.FIFTH_SCHEDULE ne info.schedule}">
                                                    <div class="form-group">
                                                        <label class="col-xs-6 control-label">Schedule</label>
                                                        <div class="col-xs-6"><p><iais:code code="${info.schedule}"/></p></div>
                                                        <div class="clear"></div>
                                                    </div>
                                                </c:if>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Name of ${BAorT}</label>
                                                    <div class="col-xs-6"><p><iais-bsb:bat-code code="${info.batName}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Types of samples that will be handled</label>
                                                    <div class="col-xs-6">
                                                        <c:forEach var="oneSampleType" items="${info.sampleType}">
                                                            <p><iais:code code="${oneSampleType}"/></p>
                                                        </c:forEach>
                                                    </div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Type of work that will be carried out involving the ${fn:toLowerCase(BAorT)}</label>
                                                    <div class="col-xs-6">
                                                        <c:forEach var="oneWorkType" items="${info.workType}">
                                                            <p><iais:code code="${oneWorkType}"/></p>
                                                        </c:forEach>
                                                    </div>
                                                    <div class="clear"></div>
                                                </div>
                                                <c:if test='${info.sampleType.contains(masterCodeConstants.SAMPLE_NATURE_OTHER) or info.workType.contains(masterCodeConstants.WORK_TYPE_BIOMANUFACTURING_INVOLVING_BAT) or info.workType.contains(masterCodeConstants.WORK_TYPE_OTHERS)}'>
                                                    <div class="form-group">
                                                        <label class="col-xs-6 control-label">Details regarding the type of samples that will be handled and the intended work</label>
                                                        <div class="col-xs-6"><p>${info.sampleWorkDetail}</p></div>
                                                        <div class="clear"></div>
                                                    </div>
                                                </c:if>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Mode of Procurement</label>
                                                    <div class="col-xs-6"><p><iais:code code="${info.details.procurementMode}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <c:if test="${'BMOP001' eq info.details.procurementMode}">
                                                    <div id="transferringFacilityDetailsInfo">
                                                        <div class="form-group">
                                                            <label class="col-xs-6 control-label" style="font-weight: bold">Details of Transferring Facility</label>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-6 control-label">Block No.</label>
                                                            <div class="col-xs-6"><p>${info.details.blockNoT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-6 control-label">Floor & Unit</label>
                                                            <div class="col-xs-6"><p>${info.details.floorNoT}-${info.details.unitNoT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-6 control-label">Street</label>
                                                            <div class="col-xs-6"><p>${info.details.streetNameT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-6 control-label">Postal Code</label>
                                                            <div class="col-xs-6"><p>${info.details.postalCodeT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-6 control-label" style="font-weight: bold">Details of Contact Person from Transferring Facility</label>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-6 control-label">Name</label>
                                                            <div class="col-xs-6"><p>${info.details.contactPersonNameT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-6 control-label">Email address</label>
                                                            <div class="col-xs-6"><p>${info.details.emailAddressT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-6 control-label">Contact No.</label>
                                                            <div class="col-xs-6"><p>${info.details.contactNoT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-6 control-label">Expected Date of Transfer</label>
                                                            <div class="col-xs-6"><p>${info.details.expectedDateT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-6 control-label">Name of Courier Service Provider</label>
                                                            <div class="col-xs-6"><p>${info.details.courierServiceProviderNameT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-6 control-label">Remarks</label>
                                                            <div class="col-xs-6"><p>${info.details.remarksT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                    </div>
                                                </c:if>
                                                <c:if test="${'BMOP002' eq info.details.procurementMode}">
                                                    <div id="exportingFacilityDetailsInfo">
                                                        <div class="form-group">
                                                            <label class="col-xs-6 control-label" style="font-weight: bold">Details of Exporting Facility</label>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-6 control-label">Facility Name</label>
                                                            <div class="col-xs-6"><p>${info.details.facNameE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-6 control-label">Block No.</label>
                                                            <div class="col-xs-6"><p>${info.details.blockNoE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-6 control-label">Floor & Unit</label>
                                                            <div class="col-xs-6"><p>${info.details.floorNoE}-${info.details.unitNoE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-6 control-label">Street</label>
                                                            <div class="col-xs-6"><p>${info.details.streetNameE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-6 control-label">Postal Code</label>
                                                            <div class="col-xs-6"><p>${info.details.postalCodeE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-6 control-label" style="font-weight: bold">Details of Contact Person from Exporting Facility</label>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-6 control-label">Name</label>
                                                            <div class="col-xs-6"><p>${info.details.contactPersonNameE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-6 control-label">Email address</label>
                                                            <div class="col-xs-6"><p>${info.details.emailAddressE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-6 control-label">Contact No.</label>
                                                            <div class="col-xs-6"><p>${info.details.contactNoE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-6 control-label">Expected Date of Export</label>
                                                            <div class="col-xs-6"><p>${info.details.expectedDateE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-6 control-label">Name of Courier Service Provider</label>
                                                            <div class="col-xs-6"><p>${info.details.courierServiceProviderNameE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-6 control-label">Remarks</label>
                                                            <div class="col-xs-6"><p>${info.details.remarksE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </c:when>
                                <c:when test="${processType eq masterCodeConstants.PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE}">
                                    <c:if test="${batListEditJudge}"><div class="text-right app-font-size-16">${fn:replace(editFragString, "REPLACE-STEP-KEY", "appInfo_facActivity")}</div></c:if>
                                    <%--@elvariable id="batInfo" type="sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalToActivityDto"--%>
                                    <div class="panel-main-content form-horizontal min-row">
                                        <div class="form-group" style="margin-top: 10px">
                                            <div class="col-10"><strong>Facility Activity Type</strong></div>
                                            <div class="clear"></div>
                                        </div>
                                        <c:forEach var="activityType" items="${batInfo.facActivityTypes}">
                                            <div class="form-group">
                                                <div class="col-10">
                                                    <label for="facActivityTypes"></label><input type="checkbox" name="facActivityTypes" id="facActivityTypes" checked="checked" readonly/>
                                                    <iais:code code="${activityType}"/></div>
                                                <div class="clear"></div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </c:when>
                                <c:when test="${processType eq masterCodeConstants.PROCESS_TYPE_APPROVE_LSP}">
                                    <c:if test="${batListEditJudge}"><div class="text-right app-font-size-16">${fn:replace(editFragString, "REPLACE-STEP-KEY", "appInfo_largeBat")}</div></c:if>
                                    <%--@elvariable id="batInfo" type="sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalToLargeDto"--%>
                                    <div class="panel-main-content form-horizontal min-row">
                                            <div class="form-group" style="margin-top: 10px">
                                                <div class="col-10"><strong>Details of Biological Agent</strong></div>
                                                <div class="clear"></div>
                                            </div>
                                            <c:forEach var="info" items="${batInfo.batInfos}">
                                                <%--@elvariable id="info" type="sg.gov.moh.iais.egp.bsb.dto.register.bat.BATInfo" --%>
                                                <c:set var="BAorT" value='${masterCodeConstants.FIFTH_SCHEDULE eq info.schedule ? "Toxin" : "Biological Agent"}'/>
                                                <div>
                                                    <c:if test="${masterCodeConstants.FIFTH_SCHEDULE ne info.schedule}">
                                                        <div class="form-group">
                                                            <label class="col-xs-6 control-label">Schedule</label>
                                                            <div class="col-xs-6"><p><iais:code code="${info.schedule}"/></p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                    </c:if>
                                                    <div class="form-group">
                                                        <label class="col-xs-6 control-label">Name of ${BAorT}</label>
                                                        <div class="col-xs-6"><p><iais-bsb:bat-code code="${info.batName}"/></p></div>
                                                        <div class="clear"></div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="col-xs-6 control-label">Estimated maximum volume (in litres) of production at any one time</label>
                                                        <div class="col-xs-6"><c:out value="${info.estimatedMaximumVolume}"/></div>
                                                        <div class="clear"></div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="col-xs-6 control-label">Method or system used for large scale production</label>
                                                        <div class="col-xs-6"><c:out value="${info.methodOrSystem}"/></div>
                                                        <div class="clear"></div>
                                                    </div>
                                                    <c:set var="detail" value="${info.details}"/>
                                                    <div class="form-group">
                                                        <label class="col-xs-6 control-label">Mode of Procurement</label>
                                                        <div class="col-xs-6"><p><iais:code code="${detail.procurementMode}"/></p></div>
                                                        <div class="clear"></div>
                                                    </div>
                                                    <c:if test="${'BMOP001' eq detail.procurementMode}">
                                                        <div id="transferringFacilityDetailsInfo">
                                                            <div class="form-group">
                                                                <label class="col-xs-6 control-label" style="font-weight: bold">Details of Transferring Facility</label>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-xs-6 control-label">Block No.</label>
                                                                <div class="col-xs-6"><p>${detail.blockNoT}</p></div>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-xs-6 control-label">Floor & Unit</label>
                                                                <div class="col-xs-6"><p>${detail.floorNoT}-${detail.unitNoT}</p></div>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-xs-6 control-label">Street</label>
                                                                <div class="col-xs-6"><p>${detail.streetNameT}</p></div>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-xs-6 control-label">Postal Code</label>
                                                                <div class="col-xs-6"><p>${detail.postalCodeT}</p></div>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-xs-6 control-label" style="font-weight: bold">Details of Contact Person from Transferring Facility</label>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-xs-6 control-label">Name</label>
                                                                <div class="col-xs-6"><p>${detail.contactPersonNameT}</p></div>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-xs-6 control-label">Email address</label>
                                                                <div class="col-xs-6"><p>${detail.emailAddressT}</p></div>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-xs-6 control-label">Contact No.</label>
                                                                <div class="col-xs-6"><p>${detail.contactNoT}</p></div>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-xs-6 control-label">Expected Date of Transfer</label>
                                                                <div class="col-xs-6"><p>${detail.expectedDateT}</p></div>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-xs-6 control-label">Name of Courier Service Provider</label>
                                                                <div class="col-xs-6"><p>${detail.courierServiceProviderNameT}</p></div>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-xs-6 control-label">Remarks</label>
                                                                <div class="col-xs-6"><p>${detail.remarksT}</p></div>
                                                                <div class="clear"></div>
                                                            </div>
                                                        </div>
                                                    </c:if>
                                                    <c:if test="${'BMOP002' eq detail.procurementMode}">
                                                        <div id="exportingFacilityDetailsInfo">
                                                            <div class="form-group">
                                                                <label class="col-xs-6 control-label" style="font-weight: bold">Details of Exporting Facility</label>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-xs-6 control-label">Facility Name</label>
                                                                <div class="col-xs-6"><p>${detail.facNameE}</p></div>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-xs-6 control-label">Block No.</label>
                                                                <div class="col-xs-6"><p>${detail.blockNoE}</p></div>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-xs-6 control-label">Floor & Unit</label>
                                                                <div class="col-xs-6"><p>${detail.floorNoE}-${detail.unitNoE}</p></div>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-xs-6 control-label">Street</label>
                                                                <div class="col-xs-6"><p>${detail.streetNameE}</p></div>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-xs-6 control-label">Postal Code</label>
                                                                <div class="col-xs-6"><p>${detail.postalCodeE}</p></div>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-xs-6 control-label" style="font-weight: bold">Details of Contact Person from Exporting Facility</label>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-xs-6 control-label">Name</label>
                                                                <div class="col-xs-6"><p>${detail.contactPersonNameE}</p></div>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-xs-6 control-label">Email address</label>
                                                                <div class="col-xs-6"><p>${detail.emailAddressE}</p></div>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-xs-6 control-label">Contact No.</label>
                                                                <div class="col-xs-6"><p>${detail.contactNoE}</p></div>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-xs-6 control-label">Expected Date of Export</label>
                                                                <div class="col-xs-6"><p>${detail.expectedDateE}</p></div>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-xs-6 control-label">Name of Courier Service Provider</label>
                                                                <div class="col-xs-6"><p>${detail.courierServiceProviderNameE}</p></div>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-xs-6 control-label">Remarks</label>
                                                                <div class="col-xs-6"><p>${detail.remarksE}</p></div>
                                                                <div class="clear"></div>
                                                            </div>
                                                        </div>
                                                    </c:if>
                                                    <c:set var="possess" value="${info.inPossessionInfo}"/>
                                                    <c:if test="${'BMOP003' eq detail.procurementMode}">
                                                        <div id="exportingFacilityDetailsInfo">
                                                            <div class="form-group">
                                                                <label class="col-xs-6 control-label" style="font-weight: bold">Details of Source Facility</label>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-xs-6 control-label">Facility Name</label>
                                                                <div class="col-xs-6"><p>${possess.facNameS}</p></div>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-xs-6 control-label">Block No.</label>
                                                                <div class="col-xs-6"><p>${possess.blockNoS}</p></div>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-xs-6 control-label">Floor & Unit</label>
                                                                <div class="col-xs-6"><p>${possess.floorNoS}-${possess.unitNoS}</p></div>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-xs-6 control-label">Street Name</label>
                                                                <div class="col-xs-6"><p>${possess.streetNameS}</p></div>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-xs-6 control-label">Postal Code</label>
                                                                <div class="col-xs-6"><p>${possess.postalCodeS}</p></div>
                                                                <div class="clear"></div>
                                                            </div>
                                                        </div>
                                                    </c:if>
                                                </div>
                                            </c:forEach>
                                        </div>
                                </c:when>
                                <c:when test="${processType eq masterCodeConstants.PROCESS_TYPE_SP_APPROVE_HANDLE}">
                                    <%--@elvariable id="batInfo" type="sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalToSpecialDto"--%>
                                    <c:if test="${batListEditJudge}"><div class="text-right app-font-size-16">${fn:replace(editFragString, "REPLACE-STEP-KEY", "appInfo_specialBat")}</div></c:if>
                                    <div class="panel-main-content form-horizontal min-row">
                                        <div class="form-group" style="margin-top: 10px">
                                            <div class="col-10"><strong>Details of Biological Agent</strong></div>
                                            <div class="clear"></div>
                                        </div>
                                        <c:if test="${masterCodeConstants.FIFTH_SCHEDULE ne batInfo.schedule}">
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">Schedule</label>
                                                <div class="col-xs-6"><p><iais:code code="${batInfo.schedule}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                        </c:if>
                                        <c:set var="BAorT" value='${masterCodeConstants.FIFTH_SCHEDULE eq batInfo.schedule ? "Toxin" : "Biological Agent"}'/>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Name of ${BAorT}</label>
                                            <div class="col-xs-6"><p><iais-bsb:bat-code code="${batInfo.batName}"/></p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Name of Project</label>
                                            <div class="col-xs-6"><p><c:out value="${batInfo.projectName}"/></p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Name of Principal Investigator</label>
                                            <div class="col-xs-6"><p><c:out value="${batInfo.principalInvestigatorName}"/></p></div>
                                            <div class="clear"></div>
                                        </div>

                                        <c:forEach var="activity" items="${batInfo.workActivities}" varStatus="status">
                                            <div class="form-group" style="margin-top: 10px">
                                                <div class="col-10"><span style="font-size: 15px;font-weight: 700">Work Activity ${status.index+1}</span></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">Intended Work Activity:</label>
                                                <div class="col-xs-6"><p><c:out value="${activity.intendedWorkActivity}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">Start Date:</label>
                                                <div class="col-xs-6"><p><c:out value="${activity.activityStartDate}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">End Date:</label>
                                                <div class="col-xs-6"><p><c:out value="${activity.activityEndDate}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">Remarks:</label>
                                                <div class="col-xs-6"><p><c:out value="${activity.activityRemarks}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                        </c:forEach>



                                    </div>

                                    <%--@elvariable id="facAuthorised" type="sg.gov.moh.iais.egp.bsb.dto.entity.FacilityAuthoriserDto"--%>
                                    <c:forEach var="facAuthorised" items="${facAuthorisedList}" varStatus="status">
                                        <div class="panel-main-content form-horizontal min-row">
                                            <div class="form-group" style="margin-top: 10px">
                                                <div class="col-10"><strong>Authorised Personnel ${status.index+1}</strong></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">Name:</label>
                                                <div class="col-xs-6"><p><c:out value="${facAuthorised.name}"/> </p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">ID Type:</label>
                                                <div class="col-xs-6"><p><c:out value="${facAuthorised.idNumber}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">Nationality:</label>
                                                <div class="col-xs-6"><p><c:out value="${facAuthorised.nationality}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">Designation:</label>
                                                <div class="col-xs-6"><p><c:out value="${facAuthorised.designation}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">Contact No.:</label>
                                                <div class="col-xs-6"><p><c:out value="${facAuthorised.contactNo}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">Email address:</label>
                                                <div class="col-xs-6"><p><c:out value="${facAuthorised.email}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">Employment Start Date:</label>
                                                <div class="col-xs-6"><p><c:out value="${facAuthorised.employmentStartDate}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">Security Clearance Date:</label>
                                                <div class="col-xs-6"><p><c:out value="${facAuthorised.securityClearanceDate}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">Area of Work:</label>
                                                <div class="col-xs-6"><p><c:out value="${facAuthorised.workArea}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </c:when>
                            </c:choose>
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
                            <c:if test="${docEditJudge}"><div class="text-right app-font-size-16">${fn:replace(editFragString, "REPLACE-STEP-KEY", "primaryDocs")}</div></c:if>
                            <div class="panel-main-content form-horizontal min-row">
                                <jsp:invoke fragment="docFrag"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>