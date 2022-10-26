<%@tag description="Preview page of approval app" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>

<%@attribute name="facProfileDto" required="true" type="sg.gov.moh.iais.egp.bsb.dto.register.approval.FacProfileDto" %>
<%@attribute name="compareBatInfos" required="false" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.compare.CompareWrap>" %>
<%@attribute name="compareSathDto" required="false" type="sg.gov.moh.iais.egp.bsb.dto.compare.CompareWrap" %>
<%@attribute name="compareFacAuthorisers" required="false" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.compare.CompareWrap>" %>
<%@attribute name="compareWorkActivitys" required="false" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.compare.CompareWrap>" %>
<%@attribute name="processType" required="true" type="java.lang.String" %>

<%@attribute name="docFrag" fragment="true" %>

<%@attribute name="appSelectJudge" type="java.lang.Boolean" %>
<%@attribute name="docSelectJudge" type="java.lang.Boolean" %>

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
                            <div class="panel-main-content form-horizontal min-row">
                                <c:if test="${appSelectJudge}"><div class="text-right"><input type="checkbox" name="appSelect" value="true" <c:if test="${rfiSelectMap.get('appSelect')}">checked="checked"</c:if>/></div></c:if>
                                <div class="form-group">
                                    <div class="col-10"><strong>Facility Profile</strong></div>
                                    <div class="clear"></div>
                                </div>
                                <div>
                                    <div class="form-group">
                                        <label class="col-xs-4 control-label">Facility Name</label>
                                        <div class="col-xs-4"><p><c:out value="${facProfileDto.facilityName}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 control-label">Facility Classification:</label>
                                        <div class="col-xs-4"><p><iais:code code="${facProfileDto.facilityClassification}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 control-label">Existing Facility Activity Type Approval</label>
                                        <div class="col-xs-4"><c:forEach items="${facProfileDto.existFacActivityTypeApprovalList}" var="approvalActivity">
                                            <li><iais:code code="${approvalActivity}"/></li>
                                        </c:forEach></div>
                                        <div class="clear"></div>
                                    </div>
                                </div>
                            </div>
                            <c:choose>
                                <c:when test="${processType eq masterCodeConstants.PROCESS_TYPE_APPROVE_POSSESS}">
                                    <div class="panel-main-content form-horizontal min-row">
                                        <div class="form-group" style="margin-top: 10px">
                                            <div class="col-10"><strong>Details of Biological Agent / Toxin</strong></div>
                                            <div class="clear"></div>
                                        </div>
                                        <c:forEach var="compareWrap" items="${compareBatInfos}" varStatus="status">
                                            <c:set var="oldBATInfo" value="${compareWrap.oldDto}"/>
                                            <c:set var="newBATInfo" value="${compareWrap.newDto}"/>
                                            <%--@elvariable id="oldBATInfo" type="sg.gov.moh.iais.egp.bsb.dto.register.bat.BATInfo"--%>
                                            <%--@elvariable id="newBATInfo" type="sg.gov.moh.iais.egp.bsb.dto.register.bat.BATInfo>"--%>
                                            <c:set var="BAorT" value='${masterCodeConstants.FIFTH_SCHEDULE eq oldBATInfo.schedule ? "Toxin" : "Biological Agent"}'/>
                                            <div>
                                                <c:if test="${masterCodeConstants.FIFTH_SCHEDULE ne oldBATInfo.schedule}">
                                                    <div class="form-group">
                                                        <label class="col-xs-4 control-label">Schedule</label>
                                                        <div class="col-xs-4"><p><iais:code code="${oldBATInfo.schedule}"/></p></div>
                                                        <div class="clear"></div>
                                                    </div>
                                                </c:if>
                                                <div class="form-group">
                                                    <label class="col-xs-4 control-label">Name of ${BAorT}</label>
                                                    <div class="col-xs-4"><p><iais-bsb:bat-code code="${oldBATInfo.batName}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-4 control-label">Types of samples that will be handled</label>
                                                    <div class="col-xs-4" data-compare-old="oneSampleType${status.index}" data-val="<c:out value='${oldBATInfo.sampleTypeStr}'/>">
                                                        <c:forEach var="oneSampleType" items="${oldBATInfo.sampleType}">
                                                            <p><iais:code code="${oneSampleType}"/></p>
                                                        </c:forEach>
                                                    </div>
                                                    <div class="col-xs-4" data-compare-new="oneSampleType${status.index}" data-val="<c:out value='${newBATInfo.sampleTypeStr}'/>" class="compareTdStyle" style="display: none">
                                                        <c:forEach var="oneSampleType" items="${newBATInfo.sampleType}">
                                                            <p><iais:code code="${oneSampleType}"/></p>
                                                        </c:forEach>
                                                    </div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-4 control-label">Type of work that will be carried out involving the biological agent(s)/toxin(s)</label>
                                                    <div class="col-xs-4" data-compare-old="oneWorkType${status.index}" data-val="<c:out value='${oldBATInfo.workTypeStr}'/>">
                                                        <c:forEach var="oneWorkType" items="${oldBATInfo.workType}">
                                                            <p><iais:code code="${oneWorkType}"/></p>
                                                        </c:forEach>
                                                    </div>
                                                    <div class="col-xs-4" data-compare-new="oneWorkType${status.index}" data-val="<c:out value='${newBATInfo.workTypeStr}'/>" class="compareTdStyle" style="display: none">
                                                        <c:forEach var="oneWorkType" items="${newBATInfo.workType}">
                                                            <p><iais:code code="${oneWorkType}"/></p>
                                                        </c:forEach>
                                                    </div>
                                                    <div class="clear"></div>
                                                </div>
                                                <c:if test='${oldBATInfo.sampleType.contains(masterCodeConstants.SAMPLE_NATURE_OTHER) or oldBATInfo.workType.contains(masterCodeConstants.WORK_TYPE_BIOMANUFACTURING_INVOLVING_BAT) or oldBATInfo.workType.contains(masterCodeConstants.WORK_TYPE_OTHERS)
                                                    or newBATInfo.sampleType.contains(masterCodeConstants.SAMPLE_NATURE_OTHER) or newBATInfo.workType.contains(masterCodeConstants.WORK_TYPE_BIOMANUFACTURING_INVOLVING_BAT) or newBATInfo.workType.contains(masterCodeConstants.WORK_TYPE_OTHERS)}'>
                                                    <div class="form-group">
                                                        <label class="col-xs-4 control-label">Details regarding the type of samples that will be handled and the intended work</label>
                                                        <div class="col-xs-4"><p data-compare-old="sampleWorkDetail${status.index}" data-val="<c:out value='${oldBATInfo.sampleWorkDetail}'/>"><c:out value="${oldBATInfo.sampleWorkDetail}"/></p></div>
                                                        <div class="col-xs-4"><p data-compare-new="sampleWorkDetail${status.index}" data-val="<c:out value='${oldBATInfo.sampleWorkDetail}'/>" class="compareTdStyle" style="display: none"><c:out value="${newBATInfo.sampleWorkDetail}"/></p></div>
                                                        <div class="clear"></div>
                                                    </div>
                                                </c:if>

                                                <c:set var="oldBATDetail" value="${oldBATInfo.details}"/>
                                                <c:set var="newBATDetail" value="${newBATInfo.details}"/>
                                                    <%--@elvariable id="oldBATDetail" type="sg.gov.moh.iais.egp.bsb.dto.register.bat.ProcModeDetails"--%>
                                                    <%--@elvariable id="newBATDetail" type="sg.gov.moh.iais.egp.bsb.dto.register.bat.ProcModeDetails"--%>
                                                <div class="form-group">
                                                    <label class="col-xs-4 control-label">Mode of Procurement</label>
                                                    <div class="col-xs-4"><p data-compare-old="procurementMode${status.index}" data-val="<c:out value='${oldBATDetail.procurementMode}'/>"><iais:code code="${oldBATDetail.procurementMode}"/></p></div>
                                                    <div class="col-xs-4"><p data-compare-new="procurementMode${status.index}" data-val="<c:out value='${newBATDetail.procurementMode}'/>" class="compareTdStyle" style="display: none"><iais:code code="${newBATDetail.procurementMode}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <c:if test="${'BMOP001' eq newBATDetail.procurementMode}">
                                                    <div id="transferringFacilityDetailsInfo">
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label" style="font-weight: bold">Details of Transferring Facility</label>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Facility Name:</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.facNameT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Block No.</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.blockNoT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Floor & Unit</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.floorNoT}-${newBATDetail.unitNoT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Street</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.streetNameT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Postal Code</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.postalCodeT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label" style="font-weight: bold">Details of Contact Person from Transferring Facility</label>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Name</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.contactPersonNameT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Email address</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.emailAddressT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Contact No.</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.contactNoT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Expected Date of Transfer</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.expectedDateT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Name of Courier Service Provider</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.courierServiceProviderNameT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Remarks</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.remarksT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                    </div>
                                                </c:if>
                                                <c:if test="${'BMOP002' eq newBATDetail.procurementMode}">
                                                    <div id="exportingFacilityDetailsInfo">
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label" style="font-weight: bold">Details of Exporting Facility</label>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Facility Name</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.facNameE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Block No.</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.blockNoE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Floor & Unit</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.floorNoE}-${newBATDetail.unitNoE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Street</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.streetNameE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Postal Code</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.postalCodeE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label" style="font-weight: bold">Details of Contact Person from Exporting Facility</label>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Name</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.contactPersonNameE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Email address</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.emailAddressE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Contact No.</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.contactNoE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Expected Date of Export</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.expectedDateE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Name of Courier Service Provider</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.courierServiceProviderNameE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Remarks</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.remarksE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </c:when>
                                <c:when test="${processType eq masterCodeConstants.PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE}">
                                    <%--@elvariable id="batInfo" type="sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalToActivityDto"--%>
                                    <div class="panel-main-content form-horizontal min-row">
                                        <div class="form-group" style="margin-top: 10px">
                                            <div class="col-10"><strong>Facility Activity Type</strong></div>
                                            <div class="clear"></div>
                                        </div>
                                        <c:forEach var="activityType" items="${batInfo.facActivityTypes}">
                                            <div class="form-group">
                                                <div class="col-10">
                                                    <label for="facActivityTypes"></label><input type="checkbox" name="facActivityTypes" id="facActivityTypes" checked="checked" disabled/>
                                                    <iais:code code="${activityType}"/></div>
                                                <div class="clear"></div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </c:when>
                                <c:when test="${processType eq masterCodeConstants.PROCESS_TYPE_APPROVE_LSP}">
                                    <%--@elvariable id="batInfo" type="sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalToLargeDto"--%>
                                    <div class="panel-main-content form-horizontal min-row">
                                        <div class="form-group" style="margin-top: 10px">
                                            <div class="col-10"><strong>Details of Biological Agent</strong></div>
                                            <div class="clear"></div>
                                        </div>
                                        <c:forEach var="compareWrap" items="${compareBatInfos}" varStatus="status">
                                            <c:set var="oldBATInfo" value="${compareWrap.oldDto}"/>
                                            <c:set var="newBATInfo" value="${compareWrap.newDto}"/>
                                            <%--@elvariable id="oldBATInfo" type="sg.gov.moh.iais.egp.bsb.dto.register.bat.BATInfo"--%>
                                            <%--@elvariable id="newBATInfo" type="sg.gov.moh.iais.egp.bsb.dto.register.bat.BATInfo>"--%>
                                            <c:set var="BAorT" value='${masterCodeConstants.FIFTH_SCHEDULE eq oldBATInfo.schedule ? "Toxin" : "Biological Agent"}'/>
                                            <div>
                                                <c:if test="${masterCodeConstants.FIFTH_SCHEDULE ne oldBATInfo.schedule}">
                                                    <div class="form-group">
                                                        <label class="col-xs-4 control-label">Schedule</label>
                                                        <div class="col-xs-4"><p><iais:code code="${oldBATInfo.schedule}"/></p></div>
                                                        <div class="clear"></div>
                                                    </div>
                                                </c:if>
                                                <div class="form-group">
                                                    <label class="col-xs-4 control-label">Name of ${BAorT}</label>
                                                    <div class="col-xs-4"><p><iais-bsb:bat-code code="${oldBATInfo.batName}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-4 control-label">Estimated maximum volume (in litres) of production at any one time</label>
                                                    <div class="col-xs-4"><p data-compare-old="estimatedMaximumVolume${status.index}" data-val="<c:out value='${oldBATInfo.estimatedMaximumVolume}'/>"><c:out value="${oldBATInfo.estimatedMaximumVolume}"/></p></div>
                                                    <div class="col-xs-4"><p data-compare-new="estimatedMaximumVolume${status.index}" data-val="<c:out value='${newBATInfo.estimatedMaximumVolume}'/>" class="compareTdStyle" style="display: none"><c:out value="${newBATInfo.estimatedMaximumVolume}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-4 control-label">Method or system used for large scale production</label>
                                                    <div class="col-xs-4"><p data-compare-old="methodOrSystem${status.index}" data-val="<c:out value='${oldBATInfo.methodOrSystem}'/>"><c:out value="${oldBATInfo.methodOrSystem}"/></p></div>
                                                    <div class="col-xs-4"><p data-compare-new="methodOrSystem${status.index}" data-val="<c:out value='${newBATInfo.methodOrSystem}'/>" class="compareTdStyle" style="display: none"><c:out value="${newBATInfo.methodOrSystem}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <c:set var="oldBATDetail" value="${oldBATInfo.details}"/>
                                                <c:set var="newBATDetail" value="${newBATInfo.details}"/>
                                                    <%--@elvariable id="oldBATDetail" type="sg.gov.moh.iais.egp.bsb.dto.register.bat.ProcModeDetails"--%>
                                                    <%--@elvariable id="newBATDetail" type="sg.gov.moh.iais.egp.bsb.dto.register.bat.ProcModeDetails"--%>
                                                <div class="form-group">
                                                    <label class="col-xs-4 control-label">Mode of Procurement</label>
                                                    <div class="col-xs-4"><p data-compare-old="procurementMode${status.index}" data-val="<c:out value='${oldBATDetail.procurementMode}'/>"><iais:code code="${oldBATDetail.procurementMode}"/></p></div>
                                                    <div class="col-xs-4"><p data-compare-new="procurementMode${status.index}" data-val="<c:out value='${newBATDetail.procurementMode}'/>" class="compareTdStyle" style="display: none"><iais:code code="${newBATDetail.procurementMode}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <c:if test="${'BMOP001' eq newBATDetail.procurementMode}">
                                                    <div id="transferringFacilityDetailsInfo">
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label" style="font-weight: bold">Details of Transferring Facility</label>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Facility Name:</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.facNameT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Block No.</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.blockNoT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Floor & Unit</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.floorNoT}-${newBATDetail.unitNoT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Street</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.streetNameT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Postal Code</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.postalCodeT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label" style="font-weight: bold">Details of Contact Person from Transferring Facility</label>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Name</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.contactPersonNameT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Email address</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.emailAddressT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Contact No.</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.contactNoT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Expected Date of Transfer</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.expectedDateT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Name of Courier Service Provider</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.courierServiceProviderNameT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Remarks</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.remarksT}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                    </div>
                                                </c:if>
                                                <c:if test="${'BMOP002' eq newBATDetail.procurementMode}">
                                                    <div id="exportingFacilityDetailsInfo">
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label" style="font-weight: bold">Details of Exporting Facility</label>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Facility Name</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.facNameE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Block No.</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.blockNoE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Floor & Unit</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.floorNoE}-${newBATDetail.unitNoE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Street</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.streetNameE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Postal Code</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.postalCodeE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label" style="font-weight: bold">Details of Contact Person from Exporting Facility</label>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Name</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.contactPersonNameE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Email address</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.emailAddressE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Contact No.</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.contactNoE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Expected Date of Export</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.expectedDateE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Name of Courier Service Provider</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.courierServiceProviderNameE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Remarks</label>
                                                            <div class="col-xs-4"><p>${newBATDetail.remarksE}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                    </div>
                                                </c:if>
                                                <c:if test="${'BMOP003' eq newBATDetail.procurementMode}">
                                                    <c:set var="sourceFac" value="${facProfileDto.sourceFacDetails}"/>
                                                    <div id="exportingFacilityDetailsInfo">
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label" style="font-weight: bold">Details of Source Facility</label>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Facility Name</label>
                                                            <div class="col-xs-4"><p>${sourceFac.facilityName}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Block No.</label>
                                                            <div class="col-xs-4"><p>${sourceFac.blkNo}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Floor & Unit</label>
                                                            <div class="col-xs-4"><p>${sourceFac.floorNo}-${sourceFac.unitNo}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Street Name</label>
                                                            <div class="col-xs-4"><p>${sourceFac.streetName}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Postal Code</label>
                                                            <div class="col-xs-4"><p>${sourceFac.postalCode}</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </c:when>
                                <c:when test="${processType eq masterCodeConstants.PROCESS_TYPE_SP_APPROVE_HANDLE}">
                                    <div class="panel-main-content form-horizontal min-row">
                                        <div class="form-group" style="margin-top: 10px">
                                            <div class="col-10"><strong>Details of Biological Agent</strong></div>
                                            <div class="clear"></div>
                                        </div>
                                        <c:set var="oldSathDto" value="${compareSathDto.oldDto}"/>
                                        <c:set var="newSathDto" value="${compareSathDto.newDto}"/>
                                        <%--@elvariable id="oldSathDto" type="sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalToSpecialDto"--%>
                                        <%--@elvariable id="newSathDto" type="sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalToSpecialDto>"--%>
                                        <c:if test="${masterCodeConstants.FIFTH_SCHEDULE ne oldSathDto.schedule}">
                                            <div class="form-group">
                                                <label class="col-xs-4 control-label">Schedule</label>
                                                <div class="col-xs-4"><p><iais:code code="${oldSathDto.schedule}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                        </c:if>
                                        <c:set var="BAorT" value='${masterCodeConstants.FIFTH_SCHEDULE eq oldSathDto.schedule ? "Toxin" : "Biological Agent"}'/>
                                        <div class="form-group">
                                            <label class="col-xs-4 control-label">Name of ${BAorT}</label>
                                            <div class="col-xs-4"><p><iais-bsb:bat-code code="${oldSathDto.batName}"/></p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-4 control-label">Name of Project</label>
                                            <div class="col-xs-4"><p data-compare-old="procurementMode" data-val="<c:out value='${oldSathDto.projectName}'/>"><iais:code code="${oldSathDto.projectName}"/></p></div>
                                            <div class="col-xs-4"><p data-compare-new="procurementMode" data-val="<c:out value='${newSathDto.projectName}'/>" class="compareTdStyle" style="display: none"><iais:code code="${newSathDto.projectName}"/></p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-4 control-label">Name of Principal Investigator</label>
                                            <div class="col-xs-4"><p data-compare-old="investigatorName" data-val="<c:out value='${oldSathDto.principalInvestigatorName}'/>"><iais:code code="${oldSathDto.principalInvestigatorName}"/></p></div>
                                            <div class="col-xs-4"><p data-compare-new="investigatorName" data-val="<c:out value='${newSathDto.principalInvestigatorName}'/>" class="compareTdStyle" style="display: none"><iais:code code="${newSathDto.principalInvestigatorName}"/></p></div>
                                            <div class="clear"></div>
                                        </div>

                                        <c:forEach var="compareWrap" items="${compareWorkActivitys}" varStatus="status">
                                            <c:set var="oldWorkActivity" value="${compareWrap.oldDto}"/>
                                            <c:set var="newWorkActivity" value="${compareWrap.newDto}"/>
                                            <div class="form-group" style="margin-top: 10px">
                                                <div class="col-10"><span style="font-size: 15px;font-weight: 700">Work Activity ${status.index+1}</span></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-4 control-label">Intended Work Activity:</label>
                                                <div class="col-xs-4"><p data-compare-old="intendedWorkActivity${status.index}" data-val="<c:out value='${oldWorkActivity.intendedWorkActivity}'/>"><iais:code code="${oldWorkActivity.intendedWorkActivity}"/></p></div>
                                                <div class="col-xs-4"><p data-compare-new="intendedWorkActivity${status.index}" data-val="<c:out value='${newWorkActivity.intendedWorkActivity}'/>" class="compareTdStyle" style="display: none"><iais:code code="${newWorkActivity.intendedWorkActivity}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-4 control-label">Start Date:</label>
                                                <div class="col-xs-4"><p data-compare-old="startDate${status.index}" data-val="<c:out value='${oldWorkActivity.activityStartDt}'/>"><iais:code code="${oldWorkActivity.activityStartDt}"/></p></div>
                                                <div class="col-xs-4"><p data-compare-new="startDate${status.index}" data-val="<c:out value='${newWorkActivity.activityStartDt}'/>" class="compareTdStyle" style="display: none"><iais:code code="${newWorkActivity.activityStartDt}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-4 control-label">End Date:</label>
                                                <div class="col-xs-4"><p data-compare-old="endDate${status.index}" data-val="<c:out value='${oldWorkActivity.activityEndDt}'/>"><iais:code code="${oldWorkActivity.activityEndDt}"/></p></div>
                                                <div class="col-xs-4"><p data-compare-new="endDate${status.index}" data-val="<c:out value='${newWorkActivity.activityEndDt}'/>" class="compareTdStyle" style="display: none"><iais:code code="${newWorkActivity.activityEndDt}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-4 control-label">Remarks:</label>
                                                <div class="col-xs-4"><p data-compare-old="remarks${status.index}" data-val="<c:out value='${oldWorkActivity.activityRemarks}'/>"><iais:code code="${oldWorkActivity.activityRemarks}"/></p></div>
                                                <div class="col-xs-4"><p data-compare-new="remarks${status.index}" data-val="<c:out value='${newWorkActivity.activityRemarks}'/>" class="compareTdStyle" style="display: none"><iais:code code="${newWorkActivity.activityRemarks}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                        </c:forEach>
                                    </div>

                                    <c:forEach var="compareWrap" items="${compareFacAuthorisers}" varStatus="status">
                                        <c:set var="oldFacAuthorisers" value="${compareWrap.oldDto}"/>
                                        <c:set var="newFacAuthorisers" value="${compareWrap.newDto}"/>
                                        <%--@elvariable id="oldFacAuthorisers" type="sg.gov.moh.iais.egp.bsb.dto.entity.FacilityAuthoriserDto"--%>
                                        <%--@elvariable id="newFacAuthorisers" type="sg.gov.moh.iais.egp.bsb.dto.entity.FacilityAuthoriserDto>"--%>

                                        <div class="panel-main-content form-horizontal min-row">
                                            <div class="form-group" style="margin-top: 10px">
                                                <div class="col-10"><strong>Authorised Personnel ${status.index+1}</strong></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-4 control-label">Name:</label>
                                                <div class="col-xs-4"><p data-compare-old="name${status.index}" data-val="<c:out value='${oldFacAuthorisers.name}'/>"><iais:code code="${oldFacAuthorisers.name}"/></p></div>
                                                <div class="col-xs-4"><p data-compare-new="name${status.index}" data-val="<c:out value='${newFacAuthorisers.name}'/>" class="compareTdStyle" style="display: none"><iais:code code="${newFacAuthorisers.name}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-4 control-label">ID Type:</label>
                                                <div class="col-xs-4"><p data-compare-old="idType${status.index}" data-val="<c:out value='${oldFacAuthorisers.idNumber}'/>"><iais:code code="${oldFacAuthorisers.idNumber}"/></p></div>
                                                <div class="col-xs-4"><p data-compare-new="idType${status.index}" data-val="<c:out value='${newFacAuthorisers.idNumber}'/>" class="compareTdStyle" style="display: none"><iais:code code="${newFacAuthorisers.idNumber}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-4 control-label">Nationality:</label>
                                                <div class="col-xs-4"><p data-compare-old="nationality${status.index}" data-val="<c:out value='${oldFacAuthorisers.nationality}'/>"><iais:code code="${oldFacAuthorisers.nationality}"/></p></div>
                                                <div class="col-xs-4"><p data-compare-new="nationality${status.index}" data-val="<c:out value='${newFacAuthorisers.nationality}'/>" class="compareTdStyle" style="display: none"><iais:code code="${newFacAuthorisers.nationality}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-4 control-label">Designation:</label>
                                                <div class="col-xs-4"><p data-compare-old="designation${status.index}" data-val="<c:out value='${oldFacAuthorisers.designation}'/>"><iais:code code="${oldFacAuthorisers.designation}"/></p></div>
                                                <div class="col-xs-4"><p data-compare-new="designation${status.index}" data-val="<c:out value='${newFacAuthorisers.designation}'/>" class="compareTdStyle" style="display: none"><iais:code code="${newFacAuthorisers.designation}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-4 control-label">Contact No.:</label>
                                                <div class="col-xs-4"><p data-compare-old="contactNo${status.index}" data-val="<c:out value='${oldFacAuthorisers.contactNo}'/>"><iais:code code="${oldFacAuthorisers.contactNo}"/></p></div>
                                                <div class="col-xs-4"><p data-compare-new="contactNo${status.index}" data-val="<c:out value='${newFacAuthorisers.contactNo}'/>" class="compareTdStyle" style="display: none"><iais:code code="${newFacAuthorisers.contactNo}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-4 control-label">Email address:</label>
                                                <div class="col-xs-4"><p data-compare-old="emailAddress${status.index}" data-val="<c:out value='${oldFacAuthorisers.email}'/>"><iais:code code="${oldFacAuthorisers.email}"/></p></div>
                                                <div class="col-xs-4"><p data-compare-new="emailAddress${status.index}" data-val="<c:out value='${newFacAuthorisers.email}'/>" class="compareTdStyle" style="display: none"><iais:code code="${newFacAuthorisers.email}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-4 control-label">Employment Start Date:</label>
                                                <div class="col-xs-4"><p data-compare-old="employmentStartDate${status.index}" data-val="<c:out value='${oldFacAuthorisers.employmentStartDate}'/>"><iais:code code="${oldFacAuthorisers.employmentStartDate}"/></p></div>
                                                <div class="col-xs-4"><p data-compare-new="employmentStartDate${status.index}" data-val="<c:out value='${newFacAuthorisers.employmentStartDate}'/>" class="compareTdStyle" style="display: none"><iais:code code="${newFacAuthorisers.employmentStartDate}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-4 control-label">Security Clearance Date:</label>
                                                <div class="col-xs-4"><p data-compare-old="securityClearanceDate${status.index}" data-val="<c:out value='${oldFacAuthorisers.securityClearanceDate}'/>"><iais:code code="${oldFacAuthorisers.securityClearanceDate}"/></p></div>
                                                <div class="col-xs-4"><p data-compare-new="securityClearanceDate${status.index}" data-val="<c:out value='${newFacAuthorisers.securityClearanceDate}'/>" class="compareTdStyle" style="display: none"><iais:code code="${newFacAuthorisers.securityClearanceDate}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-4 control-label">Area of Work:</label>
                                                <div class="col-xs-4"><p data-compare-old="workArea${status.index}" data-val="<c:out value='${oldFacAuthorisers.workArea}'/>"><iais:code code="${oldFacAuthorisers.workArea}"/></p></div>
                                                <div class="col-xs-4"><p data-compare-new="workArea${status.index}" data-val="<c:out value='${newFacAuthorisers.workArea}'/>" class="compareTdStyle" style="display: none"><iais:code code="${newFacAuthorisers.workArea}"/></p></div>
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
                            <a class="collapsed" data-toggle="collapse" href="#previewDocs">Supporting Documents</a>
                        </h4>
                    </div>
                    <div id="previewDocs" class="panel-collapse collapse">
                        <div class="panel-body">
                            <div class="panel-main-content form-horizontal min-row">
                                <c:if test="${docSelectJudge}"><div class="text-right"><input type="checkbox" name="docSelect" value="true" <c:if test="${rfiSelectMap.get('docSelect')}">checked="checked"</c:if>/></div></c:if>
                                <jsp:invoke fragment="docFrag"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>