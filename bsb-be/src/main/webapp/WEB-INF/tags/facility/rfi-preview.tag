<%@tag description="Preview page of facility registration" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@taglib prefix="fac" tagdir="/WEB-INF/tags/facility" %>

<%@attribute name="isCfJudge" required="true" type="java.lang.Boolean" %>
<%@attribute name="isUcfJudge" required="true" type="java.lang.Boolean" %>
<%@attribute name="isRfJudge" required="true" type="java.lang.Boolean" %>
<%@attribute name="isFifthRfJudge" required="true" type="java.lang.Boolean" %>
<%@attribute name="isPvRfJudge" required="true" type="java.lang.Boolean" %>

<%@attribute name="classification" required="true" type="java.lang.String" %>
<%@attribute name="activities" required="false" type="java.util.List<java.lang.String>" %>

<%@attribute name="compProfile" required="true" type="sg.gov.moh.iais.egp.bsb.dto.info.common.OrgAddressInfo" %>
<%@attribute name="declarationConfigList" required="true" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.declaration.DeclarationItemMainInfo>" %>
<%@attribute name="declarationAnswerMap" required="true" type="java.util.Map<java.lang.String, java.lang.String>" %>
<%@attribute name="docFrag" fragment="true" %>

<%@attribute name="compareFacProfile" required="true" type="sg.gov.moh.iais.egp.bsb.dto.compare.CompareWrap" %>
<%@attribute name="compareFacOperator" required="true" type="sg.gov.moh.iais.egp.bsb.dto.compare.CompareWrap" %>
<%@attribute name="compareMainAdmin" required="true" type="sg.gov.moh.iais.egp.bsb.dto.compare.CompareWrap" %>
<%@attribute name="compareAlterAdmin" required="true" type="sg.gov.moh.iais.egp.bsb.dto.compare.CompareWrap" %>
<%@attribute name="compareOfficers" required="true" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.compare.CompareWrap>" %>
<%@attribute name="compareBioSafetyCommitteeIsDifferent" required="true" type="java.lang.Boolean" %>
<%@attribute name="compareAuthorizerIsDifferent" required="true" type="java.lang.Boolean" %>
<%@attribute name="compareAfc" required="true" type="sg.gov.moh.iais.egp.bsb.dto.compare.CompareWrap" %>
<%@attribute name="compareBatMap" required="true" type="java.util.Map<java.lang.String, java.util.List<sg.gov.moh.iais.egp.bsb.dto.compare.CompareWrap>>" %>

<iais-bsb:global-constants classFullName="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" attributeKey="masterCodeConstants"/>
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
                            <div class="panel-main-content form-horizontal min-row">
                                <div class="form-group">
                                    <div class="col-10"><strong>Facility Profile</strong></div>
                                    <div class="clear"></div>
                                </div>
                                <c:set var="oldFacProfile" value="${compareFacProfile.oldDto}"/>
                                <c:set var="newFacProfile" value="${compareFacProfile.newDto}"/>
                                <div>
                                    <div class="form-group">
                                        <label class="col-xs-4 control-label">Facility Name</label>
                                        <div class="col-xs-4"><p data-compare-old="facProfileFacName" data-val="<c:out value='${oldFacProfile.facName}'/>"><c:out value="${oldFacProfile.facName}"/></p></div>
                                        <div class="col-xs-4"><p data-compare-new="facProfileFacName" data-val="<c:out value='${newFacProfile.facName}'/>" class="compareTdStyle" style="display: none"><c:out value="${newFacProfile.facName}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 control-label">Is the Facility address the same as the company address?</label>
                                        <div class="col-xs-4"><p data-compare-old="facProfileSameAddress" data-val="<c:out value='${oldFacProfile.sameAddress}'/>"><c:out value="${oldFacProfile.sameAddress}"/></p></div>
                                        <div class="col-xs-4"><p data-compare-new="facProfileSameAddress" data-val="<c:out value='${newFacProfile.sameAddress}'/>" class="compareTdStyle" style="display: none"><c:out value="${newFacProfile.sameAddress}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 control-label">Postal Code</label>
                                        <div class="col-xs-4"><p data-compare-old="facProfilePostalCode" data-val="<c:out value='${oldFacProfile.postalCode}'/>"><c:out value="${oldFacProfile.postalCode}"/></p></div>
                                        <div class="col-xs-4"><p data-compare-new="facProfilePostalCode" data-val="<c:out value='${newFacProfile.postalCode}'/>" class="compareTdStyle" style="display: none"><c:out value="${newFacProfile.postalCode}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 control-label">Address Type</label>
                                        <div class="col-xs-4"><p data-compare-old="facProfileAddressType" data-val="<c:out value='${oldFacProfile.addressType}'/>"><iais:code code="${oldFacProfile.addressType}"/></p></div>
                                        <div class="col-xs-4"><p data-compare-new="facProfileAddressType" data-val="<c:out value='${newFacProfile.addressType}'/>" class="compareTdStyle" style="display: none"><iais:code code="${newFacProfile.addressType}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 control-label">Block / House No.</label>
                                        <div class="col-xs-4"><p data-compare-old="facProfileBlock" data-val="<c:out value='${oldFacProfile.block}'/>"><c:out value="${oldFacProfile.block}"/></p></div>
                                        <div class="col-xs-4"><p data-compare-new="facProfileBlock" data-val="<c:out value='${newFacProfile.block}'/>" class="compareTdStyle" style="display: none"><c:out value="${newFacProfile.block}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 control-label">Floor and Unit No.</label>
                                        <div class="col-xs-4"><p data-compare-old="facProfileFloor" data-val="<c:out value='${oldFacProfile.floor}${oldFacProfile.unitNo}'/>"><c:out value="${oldFacProfile.floor} - ${oldFacProfile.unitNo}"/></p></div>
                                        <div class="col-xs-4"><p data-compare-new="facProfileFloor" data-val="<c:out value='${newFacProfile.floor}${newFacProfile.unitNo}'/>" class="compareTdStyle" style="display: none" data-val="<c:out value='${newFacProfile.floor}${newFacProfile.unitNo}'/>"><c:out value="${newFacProfile.floor} - ${newFacProfile.unitNo}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 control-label">Street Name</label>
                                        <div class="col-xs-4"><p data-compare-old="facProfileStreetName" data-val="<c:out value='${oldFacProfile.streetName}'/>"><c:out value="${oldFacProfile.streetName}"/></p></div>
                                        <div class="col-xs-4"><p data-compare-new="facProfileStreetName" data-val="<c:out value='${newFacProfile.streetName}'/>" class="compareTdStyle" style="display: none"><c:out value="${newFacProfile.streetName}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 control-label">Is the facility a Protected Place?</label>
                                        <div class="col-xs-4"><p data-compare-old="facProfileFacilityProtected" data-val="<c:out value='${oldFacProfile.facilityProtected}'/>"><c:out value="${oldFacProfile.facilityProtected}"/></p></div>
                                        <div class="col-xs-4"><p data-compare-new="facProfileFacilityProtected" data-val="<c:out value='${newFacProfile.facilityProtected}'/>" class="compareTdStyle" style="display: none"><c:out value="${newFacProfile.facilityProtected}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                </div>
                            </div>
                            <c:if test="${not isRfJudge}">
                                <div class="panel-main-content form-horizontal min-row">
                                    <div class="form-group">
                                        <div class="col-10"><strong>Facility Operator Profile</strong></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div>
                                        <c:set var="oldFacOperator" value="${compareFacOperator.oldDto}"/>
                                        <c:set var="newFacOperator" value="${compareFacOperator.newDto}"/>
                                        <div class="form-group">
                                            <label class="col-xs-4 control-label">Designation of Facility Operator</label>
                                            <div class="col-xs-4"><p data-compare-old="facOperatorFacOperator" data-val="<c:out value='${oldFacOperator.facOperator}'/>"><c:out value="${oldFacOperator.facOperator}"/></p></div>
                                            <div class="col-xs-4"><p data-compare-new="facOperatorFacOperator" data-val="<c:out value='${newFacOperator.facOperator}'/>" class="compareTdStyle" style="display: none"><c:out value="${newFacOperator.facOperator}"/></p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-4 control-label" style="font-weight: bold">Facility Operator Designee</label>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-4 control-label">Salutation</label>
                                            <div class="col-xs-4"><p data-compare-old="facOperatorSalutation" data-val="<c:out value='${oldFacOperator.salutation}'/>"><iais:code code="${oldFacOperator.salutation}"/></p></div>
                                            <div class="col-xs-4"><p data-compare-new="facOperatorSalutation" data-val="<c:out value='${newFacOperator.salutation}'/>" class="compareTdStyle" style="display: none"><iais:code code="${newFacOperator.salutation}"/></p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-4 control-label">Name</label>
                                            <div class="col-xs-4"><p data-compare-old="facOperatorDesigneeName" data-val="<c:out value='${oldFacOperator.designeeName}'/>"><c:out value="${oldFacOperator.designeeName}"/></p></div>
                                            <div class="col-xs-4"><p data-compare-new="facOperatorDesigneeName" data-val="<c:out value='${newFacOperator.designeeName}'/>" class="compareTdStyle" style="display: none"><c:out value="${newFacOperator.designeeName}"/></p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-4 control-label">ID No.</label>
                                            <div class="col-xs-4"><p data-compare-old="facOperatorIdNumber" data-val="<c:out value='${oldFacOperator.idNumber}'/>"><c:out value="${oldFacOperator.idNumber}"/></p></div>
                                            <div class="col-xs-4"><p data-compare-new="facOperatorIdNumber" data-val="<c:out value='${newFacOperator.idNumber}'/>" class="compareTdStyle" style="display: none"><c:out value="${newFacOperator.idNumber}"/></p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-4 control-label">Nationality</label>
                                            <div class="col-xs-4"><p data-compare-old="facOperatorNationality" data-val="<c:out value='${oldFacOperator.nationality}'/>"><iais:code code="${oldFacOperator.nationality}"/></p></div>
                                            <div class="col-xs-4"><p data-compare-new="facOperatorNationality" data-val="<c:out value='${newFacOperator.nationality}'/>" class="compareTdStyle" style="display: none"><iais:code code="${newFacOperator.nationality}"/></p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-4 control-label">Designation</label>
                                            <div class="col-xs-4"><p data-compare-old="facOperatorDesignation" data-val="<c:out value='${oldFacOperator.designation}'/>"><c:out value="${oldFacOperator.designation}"/></p></div>
                                            <div class="col-xs-4"><p data-compare-new="facOperatorDesignation" data-val="<c:out value='${newFacOperator.designation}'/>" class="compareTdStyle" style="display: none"><c:out value="${newFacOperator.designation}"/></p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-4 control-label">Contact No.</label>
                                            <div class="col-xs-4"><p data-compare-old="facOperatorContactNo" data-val="<c:out value='${oldFacOperator.contactNo}'/>"><c:out value="${oldFacOperator.contactNo}"/></p></div>
                                            <div class="col-xs-4"><p data-compare-new="facOperatorContactNo" data-val="<c:out value='${newFacOperator.contactNo}'/>" class="compareTdStyle" style="display: none"><c:out value="${newFacOperator.contactNo}"/></p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-4 control-label">Email</label>
                                            <div class="col-xs-4"><p data-compare-old="facOperatorEmail" data-val="<c:out value='${oldFacOperator.email}'/>"><c:out value="${oldFacOperator.email}"/></p></div>
                                            <div class="col-xs-4"><p data-compare-new="facOperatorEmail" data-val="<c:out value='${newFacOperator.email}'/>" class="compareTdStyle" style="display: none"><c:out value="${newFacOperator.email}"/></p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-4 control-label">Employment Start Date</label>
                                            <div class="col-xs-4"><p data-compare-old="facOperatorEmploymentStartDt" data-val="<c:out value='${oldFacOperator.employmentStartDt}'/>"><c:out value="${oldFacOperator.employmentStartDt}"/></p></div>
                                            <div class="col-xs-4"><p data-compare-new="facOperatorEmploymentStartDt" data-val="<c:out value='${newFacOperator.employmentStartDt}'/>" class="compareTdStyle" style="display: none"><c:out value="${newFacOperator.employmentStartDt}"/></p></div>
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
                                    <c:set var="oldMainAdmin" value="${compareMainAdmin.oldDto}"/>
                                    <c:set var="newMainAdmin" value="${compareMainAdmin.newDto}"/>
                                    <div class="form-group">
                                        <label class="col-xs-4 control-label"><strong>Main Administrator</strong></label>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 control-label">Salutation</label>
                                        <div class="col-xs-4"><p data-compare-old="mainAdminSalutation" data-val="<c:out value='${oldMainAdmin.salutation}'/>"><iais:code code="${oldMainAdmin.salutation}"/></p></div>
                                        <div class="col-xs-4"><p data-compare-new="mainAdminSalutation" data-val="<c:out value='${newMainAdmin.salutation}'/>" class="compareTdStyle" style="display: none"><iais:code code="${newMainAdmin.salutation}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 control-label">Name</label>
                                        <div class="col-xs-4"><p data-compare-old="mainAdminName" data-val="<c:out value='${oldMainAdmin.name}'/>"><c:out value="${oldMainAdmin.name}"/></p></div>
                                        <div class="col-xs-4"><p data-compare-new="mainAdminName" data-val="<c:out value='${newMainAdmin.name}'/>" class="compareTdStyle" style="display: none"><c:out value="${newMainAdmin.name}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 control-label">Nationality</label>
                                        <div class="col-xs-4"><p data-compare-old="mainAdminNationality" data-val="<c:out value='${oldMainAdmin.nationality}'/>"><iais:code code="${oldMainAdmin.nationality}"/></p></div>
                                        <div class="col-xs-4"><p data-compare-new="mainAdminNationality" data-val="<c:out value='${newMainAdmin.nationality}'/>" class="compareTdStyle" style="display: none"><iais:code code="${newMainAdmin.nationality}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 control-label">NRIC/FIN</label>
                                        <div class="col-xs-4"><p data-compare-old="mainAdminIdNumber" data-val="<c:out value='${oldMainAdmin.idNumber}'/>"><c:out value="${oldMainAdmin.idNumber}"/></p></div>
                                        <div class="col-xs-4"><p data-compare-new="mainAdminIdNumber" data-val="<c:out value='${newMainAdmin.idNumber}'/>" class="compareTdStyle" style="display: none"><c:out value="${newMainAdmin.idNumber}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 control-label">Designation</label>
                                        <div class="col-xs-4"><p data-compare-old="mainAdminDesignation" data-val="<c:out value='${oldMainAdmin.designation}'/>"><c:out value="${oldMainAdmin.designation}"/></p></div>
                                        <div class="col-xs-4"><p data-compare-new="mainAdminDesignation" data-val="<c:out value='${newMainAdmin.designation}'/>" class="compareTdStyle" style="display: none"><c:out value="${newMainAdmin.designation}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 control-label">Contact No.</label>
                                        <div class="col-xs-4"><p data-compare-old="mainAdminContactNo" data-val="<c:out value='${oldMainAdmin.contactNo}'/>"><c:out value="${oldMainAdmin.contactNo}"/></p></div>
                                        <div class="col-xs-4"><p data-compare-new="mainAdminContactNo" data-val="<c:out value='${newMainAdmin.contactNo}'/>" class="compareTdStyle" style="display: none"><c:out value="${newMainAdmin.contactNo}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 control-label">Email Address</label>
                                        <div class="col-xs-4"><p data-compare-old="mainAdminEmail" data-val="<c:out value='${oldMainAdmin.email}'/>"><c:out value="${oldMainAdmin.email}"/></p></div>
                                        <div class="col-xs-4"><p data-compare-new="mainAdminEmail" data-val="<c:out value='${newMainAdmin.email}'/>" class="compareTdStyle" style="display: none"><c:out value="${newMainAdmin.email}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 control-label">Employment Start Date</label>
                                        <div class="col-xs-4"><p data-compare-old="mainAdminEmploymentStartDt" data-val="<c:out value='${oldMainAdmin.employmentStartDt}'/>"><c:out value="${oldMainAdmin.employmentStartDt}"/></p></div>
                                        <div class="col-xs-4"><p data-compare-new="mainAdminEmploymentStartDt" data-val="<c:out value='${newMainAdmin.employmentStartDt}'/>" class="compareTdStyle" style="display: none"><c:out value="${newMainAdmin.employmentStartDt}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                </div>
                                <div>
                                    <c:set var="oldAlterAdmin" value="${compareAlterAdmin.oldDto}"/>
                                    <c:set var="newAlterAdmin" value="${compareAlterAdmin.newDto}"/>
                                    <div class="form-group">
                                        <label class="col-xs-4 control-label"><strong>AlternateAdministrator</strong></label>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 control-label">Salutation</label>
                                        <div class="col-xs-4"><p data-compare-old="alterAdminSalutation" data-val="<c:out value='${oldAlterAdmin.salutation}'/>"><iais:code code="${oldAlterAdmin.salutation}"/></p></div>
                                        <div class="col-xs-4"><p data-compare-new="alterAdminSalutation" data-val="<c:out value='${newAlterAdmin.salutation}'/>" class="compareTdStyle" style="display: none"><iais:code code="${newAlterAdmin.salutation}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 control-label">Name</label>
                                        <div class="col-xs-4"><p data-compare-old="alterAdminName" data-val="<c:out value='${oldAlterAdmin.name}'/>"><c:out value="${oldAlterAdmin.name}"/></p></div>
                                        <div class="col-xs-4"><p data-compare-new="alterAdminName" data-val="<c:out value='${newAlterAdmin.name}'/>" class="compareTdStyle" style="display: none"><c:out value="${newAlterAdmin.name}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 control-label">Nationality</label>
                                        <div class="col-xs-4"><p data-compare-old="alterAdminNationality" data-val="<c:out value='${oldAlterAdmin.nationality}'/>"><iais:code code="${oldAlterAdmin.nationality}"/></p></div>
                                        <div class="col-xs-4"><p data-compare-new="alterAdminNationality" data-val="<c:out value='${newAlterAdmin.nationality}'/>" class="compareTdStyle" style="display: none"><iais:code code="${newAlterAdmin.nationality}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 control-label">NRIC/FIN</label>
                                        <div class="col-xs-4"><p data-compare-old="alterAdminIdNumber" data-val="<c:out value='${oldAlterAdmin.idNumber}'/>"><c:out value="${oldAlterAdmin.idNumber}"/></p></div>
                                        <div class="col-xs-4"><p data-compare-new="alterAdminIdNumber" data-val="<c:out value='${newAlterAdmin.idNumber}'/>" class="compareTdStyle" style="display: none"><c:out value="${newAlterAdmin.idNumber}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 control-label">Designation</label>
                                        <div class="col-xs-4"><p data-compare-old="alterAdminDesignation" data-val="<c:out value='${oldAlterAdmin.designation}'/>"><c:out value="${oldAlterAdmin.designation}"/></p></div>
                                        <div class="col-xs-4"><p data-compare-new="alterAdminDesignation" data-val="<c:out value='${newAlterAdmin.designation}'/>" class="compareTdStyle" style="display: none"><c:out value="${newAlterAdmin.designation}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 control-label">Contact No.</label>
                                        <div class="col-xs-4"><p data-compare-old="alterAdminContactNo" data-val="<c:out value='${oldAlterAdmin.contactNo}'/>"><c:out value="${oldAlterAdmin.contactNo}"/></p></div>
                                        <div class="col-xs-4"><p data-compare-new="alterAdminContactNo" data-val="<c:out value='${newAlterAdmin.contactNo}'/>" class="compareTdStyle" style="display: none"><c:out value="${newAlterAdmin.contactNo}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 control-label">Email Address</label>
                                        <div class="col-xs-4"><p data-compare-old="alterAdminEmail" data-val="<c:out value='${oldAlterAdmin.email}'/>"><c:out value="${oldAlterAdmin.email}"/></p></div>
                                        <div class="col-xs-4"><p data-compare-new="alterAdminEmail" data-val="<c:out value='${newAlterAdmin.email}'/>" class="compareTdStyle" style="display: none"><c:out value="${newAlterAdmin.email}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 control-label">Employment Start Date</label>
                                        <div class="col-xs-4"><p data-compare-old="alterAdminEmploymentStartDt" data-val="<c:out value='${oldAlterAdmin.employmentStartDt}'/>"><c:out value="${oldAlterAdmin.employmentStartDt}"/></p></div>
                                        <div class="col-xs-4"><p data-compare-new="alterAdminEmploymentStartDt" data-val="<c:out value='${newAlterAdmin.employmentStartDt}'/>" class="compareTdStyle" style="display: none"><c:out value="${newAlterAdmin.employmentStartDt}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                </div>
                            </div>
                            <c:if test="${compareOfficers.size() > 0}">
                                <div class="panel-main-content form-horizontal min-row">
                                    <div class="form-group">
                                        <div class="col-10"><strong>Facility Officer</strong></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div>
                                        <c:forEach var="compareWrap" items="${compareOfficers}" varStatus="status">
                                            <c:set var="oldFacOfficer" value="${compareWrap.oldDto}"/>
                                            <c:set var="newFacOfficer" value="${compareWrap.newDto}"/>
                                            <div class="form-group">
                                                <label class="col-xs-4 control-label">Salutation</label>
                                                <div class="col-xs-4"><p data-compare-old="facOfficer${status.index}salutation" data-val="<c:out value='${oldFacOfficer.salutation}'/>"><iais:code code="${oldFacOfficer.salutation}"/></p></div>
                                                <div class="col-xs-4"><p data-compare-new="facOfficer${status.index}salutation" data-val="<c:out value='${newFacOfficer.salutation}'/>" class="compareTdStyle" style="display: none"><iais:code code="${newFacOfficer.salutation}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-4 control-label">Name</label>
                                                <div class="col-xs-4"><p data-compare-old="facOfficer${status.index}name" data-val="<c:out value='${oldFacOfficer.name}'/>"><c:out value="${oldFacOfficer.name}"/></p></div>
                                                <div class="col-xs-4"><p data-compare-new="facOfficer${status.index}name" data-val="<c:out value='${newFacOfficer.name}'/>" class="compareTdStyle" style="display: none"><c:out value="${newFacOfficer.name}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-4 control-label">Nationality</label>
                                                <div class="col-xs-4"><p data-compare-old="facOfficer${status.index}nationality" data-val="<c:out value='${oldFacOfficer.nationality}'/>"><iais:code code="${oldFacOfficer.nationality}"/></p></div>
                                                <div class="col-xs-4"><p data-compare-new="facOfficer${status.index}nationality" data-val="<c:out value='${newFacOfficer.nationality}'/>" class="compareTdStyle" style="display: none"><iais:code code="${newFacOfficer.nationality}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-4 control-label">ID No.</label>
                                                <div class="col-xs-4"><p data-compare-old="facOfficer${status.index}idNumber" data-val="<c:out value='${oldFacOfficer.idNumber}'/>"><c:out value="${oldFacOfficer.idNumber}"/></p></div>
                                                <div class="col-xs-4"><p data-compare-new="facOfficer${status.index}idNumber" data-val="<c:out value='${newFacOfficer.idNumber}'/>" class="compareTdStyle" style="display: none"><c:out value="${newFacOfficer.idNumber}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-4 control-label">Designation</label>
                                                <div class="col-xs-4"><p data-compare-old="facOfficer${status.index}designation" data-val="<c:out value='${oldFacOfficer.designation}'/>"><c:out value="${oldFacOfficer.designation}"/></p></div>
                                                <div class="col-xs-4"><p data-compare-new="facOfficer${status.index}designation" data-val="<c:out value='${newFacOfficer.designation}'/>" class="compareTdStyle" style="display: none"><c:out value="${newFacOfficer.designation}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-4 control-label">Contact No.</label>
                                                <div class="col-xs-4"><p data-compare-old="facOfficer${status.index}contactNo" data-val="<c:out value='${oldFacOfficer.contactNo}'/>"><c:out value="${oldFacOfficer.contactNo}"/></p></div>
                                                <div class="col-xs-4"><p data-compare-new="facOfficer${status.index}contactNo" data-val="<c:out value='${newFacOfficer.contactNo}'/>" class="compareTdStyle" style="display: none"><c:out value="${newFacOfficer.contactNo}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-4 control-label">Email Address</label>
                                                <div class="col-xs-4"><p data-compare-old="facOfficer${status.index}email" data-val="<c:out value='${oldFacOfficer.email}'/>"><c:out value="${oldFacOfficer.email}"/></p></div>
                                                <div class="col-xs-4"><p data-compare-new="facOfficer${status.index}email" data-val="<c:out value='${newFacOfficer.email}'/>" class="compareTdStyle" style="display: none"><c:out value="${newFacOfficer.email}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-4 control-label">Employment Start Date</label>
                                                <div class="col-xs-4"><p data-compare-old="facOfficer${status.index}employmentStartDt" data-val="<c:out value='${oldFacOfficer.employmentStartDt}'/>"><c:out value="${oldFacOfficer.employmentStartDt}"/></p></div>
                                                <div class="col-xs-4"><p data-compare-new="facOfficer${status.index}employmentStartDt" data-val="<c:out value='${newFacOfficer.employmentStartDt}'/>" class="compareTdStyle" style="display: none"><c:out value="${newFacOfficer.employmentStartDt}"/></p></div>
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
                                    <div class="form-group">
                                        <div class="col-xs-6">
                                            <a href="javascript:void(0)" onclick="expandFile('previewSubmit', 'bsbCommittee')">View Biosafety Committee Information</a>
                                        </div>
                                        <div class="col-xs-6">
                                            <c:if test="${compareBioSafetyCommitteeIsDifferent}">
                                                <p class="compareTdStyle">Biosafety Information Updated</p>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                                <div class="panel-main-content form-horizontal min-row">
                                    <div class="form-group">
                                        <div class="col-10"><strong>Personnel Authorised to Access the Facility</strong></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <div class="col-xs-6">
                                            <a href="javascript:void(0)" onclick="expandFile('previewSubmit', 'facAuth')">View Authorised Personnel Information</a>
                                        </div>
                                        <div class="col-xs-6">
                                            <c:if test="${compareAuthorizerIsDifferent}">
                                                <p class="compareTdStyle">Personnel Authorised Information Updated</p>
                                            </c:if>
                                        </div>
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
                                <c:forEach var="batMap" items="${compareBatMap}">
                                    <c:set var="isLsp" value="${masterCodeConstants.ACTIVITY_LSP_FIRST_THIRD_SCHEDULE eq batMap.key}"/>
                                    <div class="panel-main-content form-horizontal min-row">
                                        <div class="form-group" style="margin-top: 10px">
                                            <div class="col-10"><strong><iais:code code="${batMap.key}"/></strong></div>
                                            <div class="clear"></div>
                                        </div>
                                        <c:forEach var="compareWrap" items="${batMap.value}" varStatus="status">
                                            <c:set var="oldBATInfo" value="${compareWrap.oldDto}"/>
                                            <c:set var="newBATInfo" value="${compareWrap.newDto}"/>
                                            <div>
                                                <div class="form-group">
                                                    <label class="col-xs-4 control-label">Schedule</label>
                                                    <div class="col-xs-4"><p data-compare-old="${batMap.key}schedule${status.index}" data-val="<c:out value='${oldBATInfo.schedule}'/>"><iais:code code="${oldBATInfo.schedule}"/></p></div>
                                                    <div class="col-xs-4"><p data-compare-new="${batMap.key}schedule${status.index}" data-val="<c:out value='${newBATInfo.schedule}'/>" class="compareTdStyle" style="display: none"><iais:code code="${newBATInfo.schedule}"/></p></div>
                                                    <div class="clear"></div>

                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-4 control-label">Name of Biological Agent/Toxin</label>
                                                    <div class="col-xs-4"><p data-compare-old="${batMap.key}batName${status.index}" data-val="<c:out value='${oldBATInfo.schedule}'/>"><iais-bsb:bat-code code="${oldBATInfo.batName}"/></p></div>
                                                    <div class="col-xs-4"><p data-compare-new="${batMap.key}batName${status.index}" data-val="<c:out value='${oldBATInfo.schedule}'/>" class="compareTdStyle" style="display: none"><iais-bsb:bat-code code="${newBATInfo.batName}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <c:if test="${not isLsp}">
                                                    <div class="form-group">
                                                        <label class="col-xs-4 control-label">Types of samples that will be handled</label>
                                                        <div class="col-xs-4" data-compare-old="${batMap.key}oneSampleType${status.index}" data-val="<c:out value='${oldBATInfo.schedule}'/>">
                                                            <c:forEach var="oneSampleType" items="${oldBATInfo.sampleType}">
                                                                <p><iais:code code="${oneSampleType}"/></p>
                                                            </c:forEach>
                                                        </div>
                                                        <div class="col-xs-4" data-compare-new="${batMap.key}oneSampleType${status.index}" data-val="<c:out value='${oldBATInfo.schedule}'/>" class="compareTdStyle" style="display: none">
                                                            <c:forEach var="oneSampleType" items="${newBATInfo.sampleType}">
                                                                <p><iais:code code="${oneSampleType}"/></p>
                                                            </c:forEach>
                                                        </div>
                                                        <div class="clear"></div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="col-xs-4 control-label">Type of work that will be carried out involving the biological agent(s)/toxin(s)</label>
                                                        <div class="col-xs-4" data-compare-old="${batMap.key}oneWorkType${status.index}" data-val="<c:out value='${oldBATInfo.workType}'/>">
                                                            <c:forEach var="oneWorkType" items="${oldBATInfo.workType}">
                                                                <p><iais:code code="${oneWorkType}"/></p>
                                                            </c:forEach>
                                                        </div>
                                                        <div class="col-xs-4" data-compare-new="${batMap.key}oneWorkType${status.index}" data-val="<c:out value='${oldBATInfo.workType}'/>" class="compareTdStyle" style="display: none">
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
                                                            <div class="col-xs-4"><p data-compare-old="${batMap.key}sampleWorkDetail${status.index}" data-val="<c:out value='${oldBATInfo.sampleWorkDetail}'/>"><c:out value="${oldBATInfo.sampleWorkDetail}"/></p></div>
                                                            <div class="col-xs-4"><p data-compare-new="${batMap.key}sampleWorkDetail${status.index}" data-val="<c:out value='${oldBATInfo.sampleWorkDetail}'/>" class="compareTdStyle" style="display: none"><c:out value="${newBATInfo.sampleWorkDetail}"/></p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                    </c:if>
                                                </c:if>
                                                <c:if test="${isLsp}">
                                                    <div class="form-group">
                                                        <label class="col-xs-4 control-label">Estimated maximum volume (in litres) of production at any one time</label>
                                                        <div class="col-xs-4"><p data-compare-old="${batMap.key}estimatedMaximumVolume${status.index}" data-val="<c:out value='${oldBATInfo.estimatedMaximumVolume}'/>"><c:out value="${oldBATInfo.estimatedMaximumVolume}"/></p></div>
                                                        <div class="col-xs-4"><p data-compare-new="${batMap.key}estimatedMaximumVolume${status.index}" data-val="<c:out value='${newBATInfo.estimatedMaximumVolume}'/>" class="compareTdStyle" style="display: none"><c:out value="${newBATInfo.estimatedMaximumVolume}"/></p></div>
                                                        <div class="clear"></div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="col-xs-4 control-label">Method or system used for large scale production</label>
                                                        <div class="col-xs-4"><p data-compare-old="${batMap.key}methodOrSystem${status.index}" data-val="<c:out value='${oldBATInfo.methodOrSystem}'/>"><c:out value="${oldBATInfo.methodOrSystem}"/></p></div>
                                                        <div class="col-xs-4"><p data-compare-new="${batMap.key}methodOrSystem${status.index}" data-val="<c:out value='${newBATInfo.methodOrSystem}'/>" class="compareTdStyle" style="display: none"><c:out value="${newBATInfo.methodOrSystem}"/></p></div>
                                                        <div class="clear"></div>
                                                    </div>
                                                </c:if>
                                                <div class="form-group">
                                                    <label class="col-xs-4 control-label">Mode of Procurement</label>
                                                    <div class="col-xs-4"><p data-compare-old="${batMap.key}procurementMode${status.index}" data-val="<c:out value='${oldBATInfo.details.procurementMode}'/>"><iais:code code="${oldBATInfo.details.procurementMode}"/></p></div>
                                                    <div class="col-xs-4"><p data-compare-new="${batMap.key}procurementMode${status.index}" data-val="<c:out value='${newBATInfo.details.procurementMode}'/>" class="compareTdStyle" style="display: none"><iais:code code="${newBATInfo.details.procurementMode}"/></p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <c:if test="${'BMOP001' eq newBATInfo.details.procurementMode}">
                                                    <div id="transferringFacilityDetailsInfo">
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label" style="font-weight: bold">Details of Transferring Facility</label>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Block No.</label>
                                                            <div class="col-xs-4"><p><c:out value="${newBATInfo.details.blockNoT}"/></p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Floor & Unit</label>
                                                            <div class="col-xs-4"><p><c:out value="${newBATInfo.details.floorNoT} - ${newBATInfo.details.unitNoT}"/></p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Street</label>
                                                            <div class="col-xs-4"><p><c:out value="${newBATInfo.details.streetNameT}"/></p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Postal Code</label>
                                                            <div class="col-xs-4"><p><c:out value="${newBATInfo.details.postalCodeT}"/></p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label" style="font-weight: bold">Details of Contact Person from Transferring Facility</label>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Name</label>
                                                            <div class="col-xs-4"><p><c:out value="${newBATInfo.details.contactPersonNameT}"/></p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Email address</label>
                                                            <div class="col-xs-4"><p><c:out value="${newBATInfo.details.emailAddressT}"/></p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Contact No.</label>
                                                            <div class="col-xs-4"><p><c:out value="${newBATInfo.details.contactNoT}"/></p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Expected Date of Transfer</label>
                                                            <div class="col-xs-4"><p><c:out value="${newBATInfo.details.expectedDateT}"/></p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Name of Courier Service Provider</label>
                                                            <div class="col-xs-4"><p><c:out value="${newBATInfo.details.courierServiceProviderNameT}"/></p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="col-xs-4 control-label">Remarks</label>
                                                            <div class="col-xs-4"><p><c:out value="${newBATInfo.details.remarksT}"/></p></div>
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
                                <div class="col-xs-12 form-group">
                                    <h4 style="font-size: 16px">I, hereby declare the following:</h4>
                                    <br/>
                                    <ol style="padding-left: 16px">
                                        <c:forEach var="item" items="${declarationConfigList}">
                                            <li class="col-xs-12">
                                                <div class="col-xs-9 form-group" style="padding-left: 0">${item.statement}</div>
                                                <div class="form-check col-xs-2">
                                                    <span class="fa <c:choose><c:when test="${'Y' eq declarationAnswerMap.get(item.id)}">fa-dot-circle-o</c:when><c:otherwise>fa-circle-o</c:otherwise></c:choose>"></span> Yes
                                                </div>
                                                <div class="form-check col-xs-1">
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
                                <div class="col-xs-12 form-group">
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
                        <c:set var="oldAfc" value="${compareAfc.oldDto}"/>
                        <c:set var="newAfc" value="${compareAfc.newDto}"/>
                        <div id="previewAfc" class="panel-collapse collapse">
                            <div class="panel-body">
                                <div class="panel-main-content form-horizontal min-row">
                                    <div class="form-group">
                                        <label class="col-xs-4 control-label">Has the facility appointed an Approved Facility Certifier</label>
                                        <div class="col-xs-4">
                                            Yes <span class="fa <c:choose><c:when test="${oldAfc.appointed eq 'Y'}">fa-dot-circle-o</c:when><c:otherwise>fa-circle-o</c:otherwise></c:choose>"></span>
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            No <span class="fa <c:choose><c:when test="${oldAfc.appointed eq 'N'}">fa-dot-circle-o</c:when><c:otherwise>fa-circle-o</c:otherwise></c:choose>"></span>
                                        </div>
                                        <c:if test="${newAfc.appointed ne null}">
                                            <div class="col-xs-4 compareTdStyle">
                                                Yes <span class="fa <c:choose><c:when test="${newAfc.appointed eq 'Y'}">fa-dot-circle-o</c:when><c:otherwise>fa-circle-o</c:otherwise></c:choose>"></span>
                                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                No <span class="fa <c:choose><c:when test="${newAfc.appointed eq 'N'}">fa-dot-circle-o</c:when><c:otherwise>fa-circle-o</c:otherwise></c:choose>"></span>
                                            </div>
                                        </c:if>
                                        <div class="clear"></div>
                                    </div>
                                    <c:if test="${oldAfc.appointed eq 'Y' || newAfc.appointed eq 'Y'}">
                                        <div class="form-group">
                                            <label class="col-xs-4 control-label">Select Approved Facility Certifier</label>
                                            <div class="col-xs-4"><p data-compare-new="afcAfc" data-val="<c:out value='${oldAfc.afc}'/>"><iais:code code="${oldAfc.afc}"/></p></div>
                                            <div class="col-xs-4"><p data-compare-new="afcAfc" data-val="<c:out value='${newAfc.estimatedMaximumVolume}'/>" class="compareTdStyle" style="display: none"><iais:code code="${newAfc.afc}"/></p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-4 control-label">Reasons for choosing this AFC</label>
                                            <div class="col-xs-4"><p data-compare-new="afcSelectReason" data-val="<c:out value='${oldAfc.selectReason}'/>"><c:out value="${oldAfc.selectReason}"/></p></div>
                                            <div class="col-xs-4"><p data-compare-new="afcSelectReason" data-val="<c:out value='${newAfc.selectReason}'/>" class="compareTdStyle" style="display: none"><c:out value="${newAfc.selectReason}"/></p></div>
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