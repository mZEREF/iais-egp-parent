<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--@elvariable id="organizationAddress" type="sg.gov.moh.iais.egp.bsb.dto.info.common.OrgAddressInfo"--%>
<%--@elvariable id="facProfile" type="sg.gov.moh.iais.egp.bsb.dto.appview.facility.FacilityProfileDto"--%>
<%--@elvariable id="facOperator" type="sg.gov.moh.iais.egp.bsb.dto.appview.facility.FacilityOperatorDto"--%>
<%--@elvariable id="facAdminOfficer" type="sg.gov.moh.iais.egp.bsb.dto.appview.facility.FacilityAdminAndOfficerDto"--%>
<%--@elvariable id="afc" type="sg.gov.moh.iais.egp.bsb.dto.appview.facility.FacilityAfcDto"--%>
<%--@elvariable id="containsBatListJudge" type="java.lang.Boolean"--%>
<%--@elvariable id="containsAfcJudge" type="java.lang.Boolean"--%>
<%--@elvariable id="declarationConfigList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.declaration.DeclarationItemMainInfo>"--%>
<%--@elvariable id="batList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.appview.facility.BiologicalAgentToxinDto>"--%>
<%--@elvariable id="declarationAnswerMap" type="java.util.Map<java.lang.String, java.lang.String>"--%>
<%--@elvariable id="docSettings" type="jjava.util.List<sg.gov.moh.iais.egp.bsb.entity.DocSetting>"--%>
<%--@elvariable id="savedFiles" type="java.util.Map<java.lang.String, java.util.List<sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo>>"--%>
<%--@elvariable id="newFiles" type="java.util.Map<java.lang.String, java.util.List<sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo>>"--%>
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
                        <div class="col-xs-6"><p>${organizationAddress.uen}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Company Name</label>
                        <div class="col-xs-6"><p>${organizationAddress.compName}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Postal Code</label>
                        <div class="col-xs-6"><p>${organizationAddress.postalCode}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Address Type</label>
                        <div class="col-xs-6"><p><iais:code code="${organizationAddress.addressType}"/></p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Blk / House No.</label>
                        <div class="col-xs-6"><p>${organizationAddress.blockNo}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Floor No.</label>
                        <div class="col-xs-6"><p>${organizationAddress.floor}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Unit No.</label>
                        <div class="col-xs-6"><p>${organizationAddress.unitNo}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Street Name</label>
                        <div class="col-xs-6"><p>${organizationAddress.street}</p></div>
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
                <div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Facility Name</label>
                        <div class="col-xs-6"><p>${facProfile.facName}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Is the Facility address the same as the company address?</label>
                        <div class="col-xs-6"><p>${facProfile.sameAddress}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Postal Code</label>
                        <div class="col-xs-6"><p>${facProfile.postalCode}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Address Type</label>
                        <div class="col-xs-6"><p>${facProfile.addressType}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Block / House No.</label>
                        <div class="col-xs-6"><p>${facProfile.block}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Floor and Unit No.</label>
                        <div class="col-xs-6"><p>${facProfile.floor}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Street Name</label>
                        <div class="col-xs-6"><p>${facProfile.streetName}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Is the facility a Protected Place?</label>
                        <div class="col-xs-6"><p>${facProfile.facilityProtected}</p></div>
                        <div class="clear"></div>
                    </div>
                </div>
            </div>
            <div class="panel-main-content form-horizontal min-row">
                <div class="form-group">
                    <div class="col-10"><strong>Facility Operator Profile</strong></div>
                    <div class="clear"></div>
                </div>
                <div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Designation of Facility Operator</label>
                        <div class="col-xs-6"><p>${facOperator.facOperator}</p></div>
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
                        <div class="col-xs-6"><p>${facOperator.designeeName}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">ID No.</label>
                        <div class="col-xs-6"><p>${facOperator.idNumber}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Nationality</label>
                        <div class="col-xs-6"><p><iais:code code="${facOperator.nationality}"/></p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Designation</label>
                        <div class="col-xs-6"><p>${facOperator.designation}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Contact No.</label>
                        <div class="col-xs-6"><p>${facOperator.contactNo}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Email</label>
                        <div class="col-xs-6"><p>${facOperator.email}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Employment Start Date</label>
                        <div class="col-xs-6"><p>${facOperator.employmentStartDt}</p></div>
                        <div class="clear"></div>
                    </div>
                </div>
            </div>
            <div class="panel-main-content form-horizontal min-row">
                <div class="form-group">
                    <div class="col-10"><strong>Facility Administrator/ Officer</strong></div>
                    <div class="clear"></div>
                </div>
                <div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Main Administrator</label>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Salutation</label>
                        <div class="col-xs-6"><p><iais:code code="${facAdminOfficer.mainAdmin.salutation}"/></p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Name</label>
                        <div class="col-xs-6"><p>${facAdminOfficer.mainAdmin.name}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Nationality</label>
                        <div class="col-xs-6"><p><iais:code code="${facAdminOfficer.mainAdmin.nationality}"/></p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">NRIC/FIN</label>
                        <div class="col-xs-6"><p>${facAdminOfficer.mainAdmin.idNumber}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Designation</label>
                        <div class="col-xs-6"><p>${facAdminOfficer.mainAdmin.designation}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Contact No.</label>
                        <div class="col-xs-6"><p>${facAdminOfficer.mainAdmin.contactNo}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Email Address</label>
                        <div class="col-xs-6"><p>${facAdminOfficer.mainAdmin.email}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Employment Start Date</label>
                        <div class="col-xs-6"><p>${facAdminOfficer.mainAdmin.employmentStartDt}</p></div>
                        <div class="clear"></div>
                    </div>
                </div>
                <div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Alternative Administrator</label>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Salutation</label>
                        <div class="col-xs-6"><p><iais:code code="${facAdminOfficer.alternativeAdmin.salutation}"/></p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Name</label>
                        <div class="col-xs-6"><p>${facAdminOfficer.alternativeAdmin.name}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Nationality</label>
                        <div class="col-xs-6"><p><iais:code code="${facAdminOfficer.alternativeAdmin.nationality}"/></p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">NRIC/FIN</label>
                        <div class="col-xs-6"><p>${facAdminOfficer.alternativeAdmin.idNumber}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Designation</label>
                        <div class="col-xs-6"><p>${facAdminOfficer.alternativeAdmin.designation}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Contact No.</label>
                        <div class="col-xs-6"><p>${facAdminOfficer.alternativeAdmin.contactNo}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Email Address</label>
                        <div class="col-xs-6"><p>${facAdminOfficer.alternativeAdmin.email}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Employment Start Date</label>
                        <div class="col-xs-6"><p>${facAdminOfficer.alternativeAdmin.employmentStartDt}</p></div>
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
                    <c:forEach var="facOfficer" items="${facAdminOfficer.officerList}">
                        <div class="form-group">
                            <label class="col-xs-6 control-label">Salutation</label>
                            <div class="col-xs-6"><p><iais:code code="${facOfficer.salutation}"/></p></div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-6 control-label">Name</label>
                            <div class="col-xs-6"><p>${facOfficer.name}</p></div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-6 control-label">Nationality</label>
                            <div class="col-xs-6"><p><iais:code code="${facOfficer.nationality}"/></p></div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-6 control-label">ID No.</label>
                            <div class="col-xs-6"><p>${facOfficer.idNumber}</p></div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-6 control-label">Designation</label>
                            <div class="col-xs-6"><p>${facOfficer.designation}</p></div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-6 control-label">Contact No.</label>
                            <div class="col-xs-6"><p>${facOfficer.contactNo}</p></div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-6 control-label">Email Address</label>
                            <div class="col-xs-6"><p>${facOfficer.email}</p></div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-6 control-label">Employment Start Date</label>
                            <div class="col-xs-6"><p>${facOfficer.employmentStartDt}</p></div>
                            <div class="clear"></div>
                        </div>
                    </c:forEach>
                </div>
            </div>
            <div class="panel-main-content form-horizontal min-row">
                <div class="form-group">
                    <div class="col-10"><strong>Biosafety Committee</strong></div>
                    <div class="clear"></div>
                </div>
                <div>
                    <a href="javascript:void(0)" onclick="expandFile('previewSubmit', 'bsbCommittee')">View Biosafety Committee Information</a>
                </div>
            </div>
            <div class="panel-main-content form-horizontal min-row">
                <div class="form-group">
                    <div class="col-10"><strong>Personnel Authorised to Access the Facility</strong></div>
                    <div class="clear"></div>
                </div>
                <div>
                    <a href="javascript:void(0)" onclick="expandFile('previewSubmit', 'facAuth')">View Authorised Personnel Information</a>
                </div>
            </div>
        </div>
    </div>
</div>
<c:if test="${containsBatListJudge}">
    <div class="panel panel-default">
        <div class="panel-heading completed">
            <h4 class="panel-title">
                <a class="collapsed" data-toggle="collapse" href="#previewBatInfo">Biological Agents &amp; Toxins</a>
            </h4>
        </div>
        <div id="previewBatInfo" class="panel-collapse collapse">
            <div class="panel-body">
                <c:forEach var="bat" items="${batList}">
                    <div class="panel-main-content form-horizontal min-row">
                        <div class="form-group">
                            <div class="col-10"><strong><iais:code code="${bat.activityType}"/></strong></div>
                            <div class="clear"></div>
                        </div>
                        <c:forEach var="info" items="${bat.batInfos}">
                            <div>
                                <div class="form-group">
                                    <label class="col-xs-6 control-label"><iais:code code="${info.schedule}"/></label>
                                    <div class="clear"></div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-6 control-label">Name of Biological Agent/Toxin</label>
                                    <div class="col-xs-6"><p>${info.batName}</p></div>
                                    <div class="clear"></div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-6 control-label">Types of samples that will be handled</label>
                                    <div class="col-xs-6"><p>${info.sampleType}</p></div>
                                    <div class="clear"></div>
                                </div>
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
                    <div class="col-10"><strong>Declaration</strong></div>
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
                    <p>The following is a non-exhaustive list of supporting documents that the facility is required to provide for the application. Some of these may not be available at point of application submission but must be provided subsequently, when available. Please note that incomplete submissions may result in delays to processing or rejection of the application.</p>
                    <span style="text-decoration: underline; font-weight: bold">Supporting Documents</span>
                    <ol class="no-margin-list" style="padding-left: 20px">
                        <li>Application letter containing the following information:
                            <ul class="no-margin-list">
                                <li>The name of the Facility Operator designee (hyperlink to BATA FO responsibilities);</li>
                                <li>Address of the facility where the intended work will be conducted;</li>
                                <li>The reason for the application; and</li>
                                <li>The justification of how and why the work involving the biological agent and/or toxin can be carried out safely and securely in the intended facility. This may include facility design, the use of laboratory safety equipment, personal protective equipment, good microbiological practices and procedures, as well as reliable and competent personnel.</li>
                            </ul>
                        </li>
                        <li>Details of the facility's biorisk management programme.</li>
                        <li>Documentation of approval from the Biosafety Committee for the intended work.</li>
                        <li>Documentation of endorsement from the Genetic Modification Advisory Committee (if the intended work involves genetic modification of microorganism(s) or handling of genetically modified microorganism(s).</li>
                        <li>Documentation of successful completion of the required biosafety training for the Biosafety Coordinator.</li>
                        <li>Facility Administrative Oversight Plan.</li>
                        <li>Facility layout/floorplan.</li>
                        <li>Gazette Order (if the facility is a Protected Place under the Infrastructure Protection Act).</li>
                        <li>List of all location(s) in the facility where the biological agent(s)/toxin(s) will be handled (including storage) and specify the corresponding work activities that will be carried out at each location (mapped to facility floorplan, as provided in #7). The information can be provided in a table format.</li>
                        <li>Risk assessments for the intended work conducted/reviewed/endorsed by the Biosafety Committee.</li>
                        <li>Safety and security records related to facility certification, inspection, accreditation, if any.</li>
                    </ol>
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
                                    <div class="col-10"><p><a href="/bsb-fe/ajax/doc/download/facReg/repo/${repoId}">${file.filename}</a>(<fmt:formatNumber value="${file.size/1024.0}" type="number" pattern="0.0"/>KB)</p></div>
                                    <div class="clear"></div>
                                </div>
                            </c:forEach>
                            <c:forEach var="file" items="${newFileList}">
                                <c:set var="tmpId"><iais:mask name="file" value="${file.tmpId}"/></c:set>
                                <div class="form-group">
                                    <div class="col-10"><p><a href="/bsb-fe/ajax/doc/download/facReg/new/${tmpId}">${file.filename}</a>(<fmt:formatNumber value="${file.size/1024.0}" type="number" pattern="0.0"/>KB)</p></div>
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
<c:if test="${containsAfcJudge}">
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
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Select Approved Facility Certifier</label>
                        <div class="col-xs-6"><p><iais:code code="${afc.afc}"/></p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-6 control-label">Reasons for choosing this AFC</label>
                        <div class="col-xs-6"><p>${afc.selectReason}</p></div>
                        <div class="clear"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</c:if>