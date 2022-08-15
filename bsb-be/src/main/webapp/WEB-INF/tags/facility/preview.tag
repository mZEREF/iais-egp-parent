<%@tag description="Preview page of facility registration" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@taglib prefix="fac" tagdir="/WEB-INF/tags/facility" %>

<%@attribute name="isCfJudge" required="true" type="java.lang.Boolean" %>
<%@attribute name="isUcfJudge" required="true" type="java.lang.Boolean" %>
<%@attribute name="isRfJudge" required="true" type="java.lang.Boolean" %>
<%@attribute name="isFifthRfJudge" required="true" type="java.lang.Boolean" %>
<%@attribute name="isPvRfJudge" required="true" type="java.lang.Boolean" %>

<%@attribute name="classification" required="true" type="java.lang.String" %>
<%@attribute name="activities" required="false" type="java.util.List<java.lang.String>" %>

<%@attribute name="compProfile" required="true" type="sg.gov.moh.iais.egp.bsb.dto.info.common.OrgAddressInfo" %>
<%@attribute name="facProfile" required="true" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityProfileDto" %>
<%@attribute name="facOperator" required="false" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityOperatorDto" %>
<%@attribute name="facAdminOfficer" required="true" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAdminAndOfficerDto" %>
<%@attribute name="facCommittee" required="false" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeDto" %>
<%@attribute name="facAuth" required="false" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserDto" %>
<%@attribute name="batList" required="false" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.register.bat.BiologicalAgentToxinDto>" %>
<%@attribute name="declarationConfigList" required="true" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.declaration.DeclarationItemMainInfo>" %>
<%@attribute name="declarationAnswerMap" required="true" type="java.util.Map<java.lang.String, java.lang.String>" %>
<%@attribute name="afc" required="false" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAfcDto" %>
<%@attribute name="docFrag" fragment="true" %>

<%@attribute name="facSelectJudge" type="java.lang.Boolean" %>
<%@attribute name="batSelectJudge" type="java.lang.Boolean" %>
<%@attribute name="docSelectJudge" type="java.lang.Boolean" %>

<bsb:global-constants classFullName="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" attributeKey="masterCodeConstants"/>
<%--@elvariable id="masterCodeConstants" type="java.util.Map<java.lang.String, java.lang.Object>"--%>
<div class="preview-gp">
    <div class="row">
        <div class="col-xs-12">
            <div class="panel-group" role="tablist" aria-multiselectable="true">
                <div class="panel panel-default">
                    <div class="panel-heading completed">
                        <h4 class="panel-title">
                            <a class="collapsed" data-toggle="collapse" href="#previewCompInfo">Company Information</a>
                        </h4>
                    </div>
                    <div id="previewCompInfo" class="panel-collapse collapse">
                        <div class="panel-body">
                            <div class="panel-main-content form-horizontal min-row">
                                <div class="form-group">
                                    <div class="col-10"><strong>Company Profile</strong></div>
                                    <div class="clear"></div>
                                </div>
                                <div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">UEN</label>
                                        <div class="col-xs-6"><p><c:out value="${compProfile.uen}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Company Name</label>
                                        <div class="col-xs-6"><p><c:out value="${compProfile.compName}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Postal Code</label>
                                        <div class="col-xs-6"><p><c:out value="${compProfile.postalCode}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Address Type</label>
                                        <div class="col-xs-6"><p><iais:code code="${compProfile.addressType}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Blk / House No.</label>
                                        <div class="col-xs-6"><p><c:out value="${compProfile.blockNo}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Floor No.</label>
                                        <div class="col-xs-6"><p><c:out value="${compProfile.floor}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Unit No.</label>
                                        <div class="col-xs-6"><p><c:out value="${compProfile.unitNo}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Street Name</label>
                                        <div class="col-xs-6"><p><c:out value="${compProfile.street}"/></p></div>
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
                            <a class="collapsed" data-toggle="collapse" href="#previewFacInfo">Facility Information</a>
                        </h4>
                    </div>
                    <div id="previewFacInfo" class="panel-collapse collapse">
                        <div class="panel-body">
                            <c:if test="${facSelectJudge}"><div class="text-right"><input type="checkbox" name="facSelect" value="true" <c:if test="${rfiSelectMap.get('facSelect')}">checked="checked"</c:if>/></div></c:if>
                            <div class="panel-main-content form-horizontal min-row">
                                <div>
                                    <c:forEach var="profileInfo" items="${facProfile.infoList}">
                                        <div class="form-group">
                                            <div class="col-10"><strong>Facility Profile</strong></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Facility Name</label>
                                            <div class="col-xs-6"><p><c:out value="${profileInfo.facName}"/></p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Is the Facility address the same as the company address?</label>
                                            <div class="col-xs-6"><p><c:out value="${bsb:displayYN(profileInfo.sameAddress)}"/></p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Postal Code</label>
                                            <div class="col-xs-6"><p><c:out value="${profileInfo.postalCode}"/></p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Address Type</label>
                                            <div class="col-xs-6"><p><iais:code code="${profileInfo.addressType}"/></p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Block / House No.</label>
                                            <div class="col-xs-6"><p><c:out value="${profileInfo.block}"/></p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Floor and Unit No.</label>
                                            <div class="col-xs-6"><p><c:out value="${profileInfo.floor} - ${profileInfo.unitNo}"/></p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Street Name</label>
                                            <div class="col-xs-6"><p><c:out value="${profileInfo.streetName}"/></p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Is the facility a Protected Place?</label>
                                            <div class="col-xs-6"><p><c:out value="${bsb:displayYN(profileInfo.facilityProtected)}"/></p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <c:if test="${isRfJudge}">
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label" style="font-weight: bold">Person In Charge</label>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">Name</label>
                                                <div class="col-xs-6"><p><c:out value="${profileInfo.inChargePersonName}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">Desingation</label>
                                                <div class="col-xs-6"><p><c:out value="${profileInfo.inChargePersonDesignation}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">Email</label>
                                                <div class="col-xs-6"><p><c:out value="${profileInfo.inChargePersonEmail}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">Contect No.</label>
                                                <div class="col-xs-6"><p><c:out value="${profileInfo.inChargePersonContactNo}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                        </c:if>
                                        <c:if test="${isPvRfJudge}">
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label" style="font-weight: bold">Inventory Information</label>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">OPV/Sabin 1 IM</label>
                                                <div class="form-check col-xs-2" style="margin: 0; padding-left: 15px;">
                                                    <span class="fa <c:choose><c:when test="${masterCodeConstants.YES eq profileInfo.opvSabin1IM}">fa-dot-circle-o</c:when><c:otherwise>fa-circle-o</c:otherwise></c:choose>"></span> Yes
                                                </div>
                                                <div class="form-check col-xs-2" style="margin: 0;">
                                                    <span class="fa <c:choose><c:when test="${masterCodeConstants.NO eq profileInfo.opvSabin1IM}">fa-dot-circle-o</c:when><c:otherwise>fa-circle-o</c:otherwise></c:choose>"></span> No
                                                </div>
                                                <div class="clear"></div>
                                            </div>
                                            <c:if test="${masterCodeConstants.YES eq profileInfo.opvSabin1IM}">
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Expected destruction date</label>
                                                    <div class="col-xs-6"><p><c:out value="${profileInfo.opvSabin1IMExpectedDestructDt}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Reason for Retention</label>
                                                    <div class="col-xs-6"><p><c:out value="${profileInfo.opvSabin1IMRetentionReason}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                            </c:if>
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">OPV/Sabin 2 IM</label>
                                                <div class="form-check col-xs-2" style="margin: 0; padding-left: 15px;">
                                                    <span class="fa <c:choose><c:when test="${masterCodeConstants.YES eq profileInfo.opvSabin2IM}">fa-dot-circle-o</c:when><c:otherwise>fa-circle-o</c:otherwise></c:choose>"></span> Yes
                                                </div>
                                                <div class="form-check col-xs-2" style="margin: 0;">
                                                    <span class="fa <c:choose><c:when test="${masterCodeConstants.NO eq profileInfo.opvSabin2IM}">fa-dot-circle-o</c:when><c:otherwise>fa-circle-o</c:otherwise></c:choose>"></span> No
                                                </div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">OPV/Sabin 3 IM</label>
                                                <div class="form-check col-xs-2" style="margin: 0; padding-left: 15px;">
                                                    <span class="fa <c:choose><c:when test="${masterCodeConstants.YES eq profileInfo.opvSabin3IM}">fa-dot-circle-o</c:when><c:otherwise>fa-circle-o</c:otherwise></c:choose>"></span> Yes
                                                </div>
                                                <div class="form-check col-xs-2" style="margin: 0;">
                                                    <span class="fa <c:choose><c:when test="${masterCodeConstants.NO eq profileInfo.opvSabin3IM}">fa-dot-circle-o</c:when><c:otherwise>fa-circle-o</c:otherwise></c:choose>"></span> No
                                                </div>
                                                <div class="clear"></div>
                                            </div>
                                            <c:if test="${masterCodeConstants.YES eq profileInfo.opvSabin3IM}">
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Expected destruction date</label>
                                                    <div class="col-xs-6"><p><c:out value="${profileInfo.opvSabin3IMExpectedDestructDt}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Reason for Retention</label>
                                                    <div class="col-xs-6"><p><c:out value="${profileInfo.opvSabin3IMRetentionReason}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                            </c:if>
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">OPV/Sabin 1 PIM</label>
                                                <div class="form-check col-xs-2" style="margin: 0; padding-left: 15px;">
                                                    <span class="fa <c:choose><c:when test="${masterCodeConstants.YES eq profileInfo.opvSabin1PIM}">fa-dot-circle-o</c:when><c:otherwise>fa-circle-o</c:otherwise></c:choose>"></span> Yes
                                                </div>
                                                <div class="form-check col-xs-2" style="margin: 0;">
                                                    <span class="fa <c:choose><c:when test="${masterCodeConstants.NO eq profileInfo.opvSabin1PIM}">fa-dot-circle-o</c:when><c:otherwise>fa-circle-o</c:otherwise></c:choose>"></span> No
                                                </div>
                                                <div class="clear"></div>
                                            </div>
                                            <c:if test="${masterCodeConstants.YES eq profileInfo.opvSabin1PIM}">
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Risk level</label>
                                                    <div class="col-xs-6"><p><iais:code code="${profileInfo.opvSabin1PIMRiskLevel}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Reason for Retention</label>
                                                    <div class="col-xs-6"><p><c:out value="${profileInfo.opvSabin1PIMRetentionReason}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                            </c:if>
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">OPV/Sabin 2 PIM</label>
                                                <div class="form-check col-xs-2" style="margin: 0; padding-left: 15px;">
                                                    <span class="fa <c:choose><c:when test="${masterCodeConstants.YES eq profileInfo.opvSabin2PIM}">fa-dot-circle-o</c:when><c:otherwise>fa-circle-o</c:otherwise></c:choose>"></span> Yes
                                                </div>
                                                <div class="form-check col-xs-2" style="margin: 0;">
                                                    <span class="fa <c:choose><c:when test="${masterCodeConstants.NO eq profileInfo.opvSabin2PIM}">fa-dot-circle-o</c:when><c:otherwise>fa-circle-o</c:otherwise></c:choose>"></span> No
                                                </div>
                                                <div class="clear"></div>
                                            </div>
                                            <c:if test="${masterCodeConstants.YES eq profileInfo.opvSabin2PIM}">
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Risk level</label>
                                                    <div class="col-xs-6"><p><iais:code code="${profileInfo.opvSabin2PIMRiskLevel}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Reason for Retention</label>
                                                    <div class="col-xs-6"><p><c:out value="${profileInfo.opvSabin2PIMRetentionReason}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                            </c:if>
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">OPV/Sabin 3 PIM</label>
                                                <div class="form-check col-xs-2" style="margin: 0; padding-left: 15px;">
                                                    <span class="fa <c:choose><c:when test="${masterCodeConstants.YES eq profileInfo.opvSabin3PIM}">fa-dot-circle-o</c:when><c:otherwise>fa-circle-o</c:otherwise></c:choose>"></span> Yes
                                                </div>
                                                <div class="form-check col-xs-2" style="margin: 0;">
                                                    <span class="fa <c:choose><c:when test="${masterCodeConstants.NO eq profileInfo.opvSabin3PIM}">fa-dot-circle-o</c:when><c:otherwise>fa-circle-o</c:otherwise></c:choose>"></span> No
                                                </div>
                                                <div class="clear"></div>
                                            </div>
                                            <c:if test="${masterCodeConstants.YES eq profileInfo.opvSabin3PIM}">
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Risk level</label>
                                                    <div class="col-xs-6"><p><iais:code code="${profileInfo.opvSabin3PIMRiskLevel}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Reason for Retention</label>
                                                    <div class="col-xs-6"><p><c:out value="${profileInfo.opvSabin3PIMRetentionReason}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                            </c:if>
                                        </c:if>
                                    </c:forEach>
                                </div>
                            </div>
                            <c:if test="${not isRfJudge}">
                            <div class="panel-main-content form-horizontal min-row">
                                <div class="form-group">
                                    <div class="col-10"><strong>Facility Operator Profile</strong></div>
                                    <div class="clear"></div>
                                </div>
                                <div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Designation of Facility Operator</label>
                                        <div class="col-xs-6"><p><c:out value="${facOperator.facOperator}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label" style="font-weight: bold">Facility Operator Designee</label>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Salutation</label>
                                        <div class="col-xs-6"><p><iais:code code="${facOperator.salutation}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Name</label>
                                        <div class="col-xs-6"><p><c:out value="${facOperator.designeeName}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">ID No.</label>
                                        <div class="col-xs-6"><p><c:out value="${facOperator.idNumber}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Nationality</label>
                                        <div class="col-xs-6"><p><iais:code code="${facOperator.nationality}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Designation</label>
                                        <div class="col-xs-6"><p><c:out value="${facOperator.designation}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Contact No.</label>
                                        <div class="col-xs-6"><p><c:out value="${facOperator.contactNo}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Email</label>
                                        <div class="col-xs-6"><p><c:out value="${facOperator.email}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Employment Start Date</label>
                                        <div class="col-xs-6"><p><c:out value="${facOperator.employmentStartDt}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                </div>
                            </div>
                            </c:if>
                            <div class="panel-main-content form-horizontal min-row">
                                <div class="form-group">
                                    <div class="col-10"><strong>Facility Administrator/ Officer</strong></div>
                                    <div class="clear"></div>
                                </div>
                                <div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label"><strong>Main Administrator</strong></label>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Salutation</label>
                                        <div class="col-xs-6"><p><iais:code code="${facAdminOfficer.mainAdmin.salutation}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Name</label>
                                        <div class="col-xs-6"><p><c:out value="${facAdminOfficer.mainAdmin.name}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Nationality</label>
                                        <div class="col-xs-6"><p><iais:code code="${facAdminOfficer.mainAdmin.nationality}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">NRIC/FIN</label>
                                        <div class="col-xs-6"><p><c:out value="${facAdminOfficer.mainAdmin.idNumber}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Designation</label>
                                        <div class="col-xs-6"><p><c:out value="${facAdminOfficer.mainAdmin.designation}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Contact No.</label>
                                        <div class="col-xs-6"><p><c:out value="${facAdminOfficer.mainAdmin.contactNo}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Email Address</label>
                                        <div class="col-xs-6"><p><c:out value="${facAdminOfficer.mainAdmin.email}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Employment Start Date</label>
                                        <div class="col-xs-6"><p><c:out value="${facAdminOfficer.mainAdmin.employmentStartDt}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                </div>
                                <div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label"><strong>AlternateAdministrator</strong></label>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Salutation</label>
                                        <div class="col-xs-6"><p><iais:code code="${facAdminOfficer.alternativeAdmin.salutation}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Name</label>
                                        <div class="col-xs-6"><p><c:out value="${facAdminOfficer.alternativeAdmin.name}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Nationality</label>
                                        <div class="col-xs-6"><p><iais:code code="${facAdminOfficer.alternativeAdmin.nationality}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">NRIC/FIN</label>
                                        <div class="col-xs-6"><p><c:out value="${facAdminOfficer.alternativeAdmin.idNumber}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Designation</label>
                                        <div class="col-xs-6"><p><c:out value="${facAdminOfficer.alternativeAdmin.designation}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Contact No.</label>
                                        <div class="col-xs-6"><p><c:out value="${facAdminOfficer.alternativeAdmin.contactNo}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Email Address</label>
                                        <div class="col-xs-6"><p><c:out value="${facAdminOfficer.alternativeAdmin.email}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Employment Start Date</label>
                                        <div class="col-xs-6"><p><c:out value="${facAdminOfficer.alternativeAdmin.employmentStartDt}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                </div>
                            </div>
                            <c:if test="${facAdminOfficer.officerList.size() > 0}">
                            <div class="panel-main-content form-horizontal min-row">
                                <div class="form-group">
                                    <div class="col-10"><strong>Facility Officer</strong></div>
                                    <div class="clear"></div>
                                </div>
                                <div>
                                    <c:forEach var="facOfficer" items="${facAdminOfficer.officerList}">
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Salutation</label>
                                            <div class="col-xs-6"><p><iais:code code="${facOfficer.salutation}"/></p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Name</label>
                                            <div class="col-xs-6"><p><c:out value="${facOfficer.name}"/></p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Nationality</label>
                                            <div class="col-xs-6"><p><iais:code code="${facOfficer.nationality}"/></p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">ID No.</label>
                                            <div class="col-xs-6"><p><c:out value="${facOfficer.idNumber}"/></p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Designation</label>
                                            <div class="col-xs-6"><p><c:out value="${facOfficer.designation}"/></p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Contact No.</label>
                                            <div class="col-xs-6"><p><c:out value="${facOfficer.contactNo}"/></p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Email Address</label>
                                            <div class="col-xs-6"><p><c:out value="${facOfficer.email}"/></p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Employment Start Date</label>
                                            <div class="col-xs-6"><p><c:out value="${facOfficer.employmentStartDt}"/></p></div>
                                            <div class="clear"></div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                            </c:if>
                            <c:if test="${not isRfJudge}">
                                <div class="panel-main-content form-horizontal min-row">
                                    <div class="form-group">
                                        <div class="col-10"><strong>Biosafety Committee</strong></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div>
                                        <a href="javascript:void(0)" onclick="expandFile('previewSubmit', 'bsbCommittee')">View Biosafety Committee Information</a>
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${not isPvRfJudge}">
                                <div class="panel-main-content form-horizontal min-row">
                                    <div class="form-group">
                                        <div class="col-10"><strong>Personnel Authorised to Access the Facility</strong></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div>
                                        <a href="javascript:void(0)" onclick="expandFile('previewSubmit', 'facAuth')">View Authorised Personnel Information</a>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
                <c:if test="${isUcfJudge or isFifthRfJudge}">
                <div class="panel panel-default">
                    <div class="panel-heading completed">
                        <h4 class="panel-title">
                            <a class="collapsed" data-toggle="collapse" href="#previewBatInfo">Biological Agents &amp; Toxins</a>
                        </h4>
                    </div>
                    <div id="previewBatInfo" class="panel-collapse collapse">
                        <div class="panel-body">
                            <c:if test="${batSelectJudge}"><div class="text-right"><input type="checkbox" name="batSelect" value="true" <c:if test="${rfiSelectMap.get('batSelect')}">checked="checked"</c:if>/></div></c:if>
                            <c:forEach var="bat" items="${batList}">
                                <c:set var="isLsp" value="${masterCodeConstants.ACTIVITY_LSP_FIRST_THIRD_SCHEDULE eq bat.activityType}"/>
                                <div class="panel-main-content form-horizontal min-row">
                                    <div class="form-group" style="margin-top: 10px">
                                        <div class="col-10"><strong><iais:code code="${bat.activityType}"/></strong></div>
                                        <div class="clear"></div>
                                    </div>
                                    <c:forEach var="info" items="${bat.batInfos}">
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
                                                <div class="col-xs-6"><p><bsb:bat-code code="${info.batName}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <c:if test="${not isLsp}">
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
                                                        <div class="col-xs-6"><p><c:out value="${info.sampleWorkDetail}"/></p></div>
                                                        <div class="clear"></div>
                                                    </div>
                                                </c:if>
                                            </c:if>
                                            <c:if test="${isLsp}">
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Estimated maximum volume (in litres) of production at any one time</label>
                                                    <div class="col-xs-6"><p><c:out value="${info.estimatedMaximumVolume}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Method or system used for large scale production</label>
                                                    <div class="col-xs-6"><p><c:out value="${info.methodOrSystem}"/></p></div>
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
                                                    <div class="col-xs-6"><p><c:out value="${info.details.blockNoT}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Floor & Unit</label>
                                                    <div class="col-xs-6"><p><c:out value="${info.details.floorNoT} - ${info.details.unitNoT}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Street</label>
                                                    <div class="col-xs-6"><p><c:out value="${info.details.streetNameT}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Postal Code</label>
                                                    <div class="col-xs-6"><p><c:out value="${info.details.postalCodeT}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label" style="font-weight: bold">Details of Contact Person from Transferring Facility</label>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Name</label>
                                                    <div class="col-xs-6"><p><c:out value="${info.details.contactPersonNameT}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Email address</label>
                                                    <div class="col-xs-6"><p><c:out value="${info.details.emailAddressT}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Contact No.</label>
                                                    <div class="col-xs-6"><p><c:out value="${info.details.contactNoT}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Expected Date of Transfer</label>
                                                    <div class="col-xs-6"><p><c:out value="${info.details.expectedDateT}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Name of Courier Service Provider</label>
                                                    <div class="col-xs-6"><p><c:out value="${info.details.courierServiceProviderNameT}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Remarks</label>
                                                    <div class="col-xs-6"><p><c:out value="${info.details.remarksT}"/></p></div>
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
                                                    <div class="col-xs-6"><p><c:out value="${info.details.facNameE}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Block No.</label>
                                                    <div class="col-xs-6"><p><c:out value="${info.details.blockNoE}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Floor & Unit</label>
                                                    <div class="col-xs-6"><p><c:out value="${info.details.floorNoE} - ${info.details.unitNoE}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Street</label>
                                                    <div class="col-xs-6"><p><c:out value="${info.details.streetNameE}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Postal Code</label>
                                                    <div class="col-xs-6"><p><c:out value="${info.details.postalCodeE}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label" style="font-weight: bold">Details of Contact Person from Exporting Facility</label>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Name</label>
                                                    <div class="col-xs-6"><p><c:out value="${info.details.contactPersonNameE}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Email address</label>
                                                    <div class="col-xs-6"><p><c:out value="${info.details.emailAddressE}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Contact No.</label>
                                                    <div class="col-xs-6"><p><c:out value="${info.details.contactNoE}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Expected Date of Export</label>
                                                    <div class="col-xs-6"><p><c:out value="${info.details.expectedDateE}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Name of Courier Service Provider</label>
                                                    <div class="col-xs-6"><p><c:out value="${info.details.courierServiceProviderNameE}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Remarks</label>
                                                    <div class="col-xs-6"><p><c:out value="${info.details.remarksE}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                            </div>
                                            </c:if>
                                            <c:if test="${'BMOP003' eq info.details.procurementMode}">
                                                <c:set var="firstProfile" value="${facProfile.firstProfile()}" />
                                                <%--@elvariable id="firstProfile" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityProfileInfo"--%>
                                                <div id="sourceFacilityDetailsInfo">
                                                    <div class="form-group">
                                                        <label class="col-xs-6 control-label" style="font-weight: bold">Details of Source Facility</label>
                                                        <div class="clear"></div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="col-xs-6 control-label">Facility Name</label>
                                                        <div class="col-xs-6"><p><c:out value="${firstProfile.facName}"/></p></div>
                                                        <div class="clear"></div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="col-xs-6 control-label">Block No.</label>
                                                        <div class="col-xs-6"><p><c:out value="${firstProfile.block}"/></p></div>
                                                        <div class="clear"></div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="col-xs-6 control-label">Floor & Unit</label>
                                                        <div class="col-xs-6"><p><c:out value="${firstProfile.floor} - ${firstProfile.unitNo}"/></p></div>
                                                        <div class="clear"></div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="col-xs-6 control-label">Street</label>
                                                        <div class="col-xs-6"><p><c:out value="${firstProfile.streetName}"/></p></div>
                                                        <div class="clear"></div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="col-xs-6 control-label">Postal Code</label>
                                                        <div class="col-xs-6"><p><c:out value="${firstProfile.postalCode}"/></p></div>
                                                        <div class="clear"></div>
                                                    </div>
                                                </div>
                                            </c:if>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
                </c:if>
                <div class="panel panel-default">
                    <div class="panel-heading completed">
                        <h4 class="panel-title">
                            <a class="collapsed" data-toggle="collapse" href="#previewOtherAppInfo">Other Application & Information</a>
                        </h4>
                    </div>
                    <div id="previewOtherAppInfo" class="panel-collapse collapse">
                        <div class="panel-body">
                            <div class="panel-main-content form-horizontal min-row">
                                <div class="form-group">
                                    <div class="col-10" style="padding-bottom: 15px;"><strong>Declaration</strong></div>
                                    <div class="clear"></div>
                                </div>
                                <div class="form-group" style="margin: 0">
                                    <p style="font-size: 16px">I, hereby declare the following:</p>
                                    <br/>
                                    <ol style="padding-left: 16px">
                                        <c:forEach var="item" items="${declarationConfigList}">
                                            <li class="col-xs-12">
                                                <div class="col-xs-8 form-group" style="padding-left: 0">${item.statement}</div>
                                                <div class="form-check col-xs-2" style="text-align: right">
                                                    <span class="fa <c:choose><c:when test="${'Y' eq declarationAnswerMap.get(item.id)}">fa-dot-circle-o</c:when><c:otherwise>fa-circle-o</c:otherwise></c:choose>"></span> Yes
                                                </div>
                                                <div class="form-check col-xs-2" style="text-align: right">
                                                    <span class="fa <c:choose><c:when test="${'N' eq declarationAnswerMap.get(item.id)}">fa-dot-circle-o</c:when><c:otherwise>fa-circle-o</c:otherwise></c:choose>"></span> No
                                                </div>
                                            </li>
                                        </c:forEach>
                                    </ol>
                                </div>
                            </div>
                            <div class="panel-main-content form-horizontal min-row">
                                <div class="form-group">
                                    <div class="col-10"><strong>Other Information</strong></div>
                                    <div class="clear"></div>
                                </div>
                                <div class="form-group" style="margin: 0">
                                    <fac:supportingDocInfo classification="${classification}" activities="${activities}" masterCodeConstants="${masterCodeConstants}"/>
                                </div>
                            </div>
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
                                <div class="form-group">
                                    <div class="col-10" style="padding-bottom: 15px;"><strong>Uploaded Documents</strong></div>
                                    <div class="clear"></div>
                                </div>
                                <c:if test="${docSelectJudge}"><div class="text-right"><input type="checkbox" name="docSelect" value="true" <c:if test="${rfiSelectMap.get('docSelect')}">checked="checked"</c:if>/></div></c:if>
                                <jsp:invoke fragment="docFrag"/>
                            </div>
                        </div>
                    </div>
                </div>
                <c:if test="${isCfJudge}">
                <div class="panel panel-default">
                    <div class="panel-heading completed">
                        <h4 class="panel-title">
                            <a class="collapsed" data-toggle="collapse" href="#previewAfc">Approved Facility Certifier</a>
                        </h4>
                    </div>
                    <div id="previewAfc" class="panel-collapse collapse">
                        <div class="panel-body">
                            <div class="panel-main-content form-horizontal min-row">
                                <div class="form-group">
                                    <label class="col-xs-6 control-label">Has the facility appointed an Approved Facility Certifier</label>
                                    <div class="col-xs-6">
                                        Yes <span class="fa <c:choose><c:when test="${afc.appointed eq 'Y'}">fa-dot-circle-o</c:when><c:otherwise>fa-circle-o</c:otherwise></c:choose>"></span>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                        No <span class="fa <c:choose><c:when test="${afc.appointed eq 'N'}">fa-dot-circle-o</c:when><c:otherwise>fa-circle-o</c:otherwise></c:choose>"></span>
                                    </div>
                                    <div class="clear"></div>
                                </div>
                                <c:if test="${afc.appointed eq 'Y'}">
                                <div class="form-group">
                                    <label class="col-xs-6 control-label">Select Approved Facility Certifier</label>
                                    <div class="col-xs-6"><p><iais:code code="${afc.afc}"/></p></div>
                                    <div class="clear"></div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-6 control-label">Reasons for choosing this AFC</label>
                                    <div class="col-xs-6"><p><c:out value="${afc.selectReason}"/></p></div>
                                    <div class="clear"></div>
                                </div>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
                </c:if>
            </div>
        </div>
    </div>
</div>