<%@tag description="Preview page of facility certifier registration" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>

<%@attribute name="companyProfile" required="true" type="sg.gov.moh.iais.egp.bsb.dto.register.afc.CompanyProfileDto" %>
<%@attribute name="companyCerTeam" required="true" type="sg.gov.moh.iais.egp.bsb.dto.register.afc.CertifyingTeamDto" %>
<%@attribute name="companyAdmin" required="true" type="sg.gov.moh.iais.egp.bsb.dto.register.afc.AdministratorDto" %>
<%@attribute name="docFrag" fragment="true" %>
<%@attribute name="editFrag" fragment="true" %>
<%@attribute name="profileEditJudge" type="java.lang.Boolean" %>
<%@attribute name="certTeamEditJudge" type="java.lang.Boolean" %>
<%@attribute name="adminEditJudge" type="java.lang.Boolean" %>
<%@attribute name="docEditJudge" type="java.lang.Boolean" %>

<jsp:invoke fragment="editFrag" var="editFragString"/>

<div class="preview-gp">
    <div class="row">
        <div class="col-xs-12">
            <div class="panel-group" role="tablist" aria-multiselectable="true">
                <div class="panel panel-default">
                    <div class="panel-heading completed">
                        <h4 class="panel-title">
                            <a class="collapsed" data-toggle="collapse" href="#previewFacInfo">Application Information</a>
                        </h4>
                    </div>
                    <div id="previewFacInfo" class="panel-collapse collapse">
                        <div class="panel-body">
                            <c:if test="${profileEditJudge}"><div class="text-right app-font-size-16">${fn:replace(editFragString, "REPLACE-STEP-KEY", "appInfo_companyProfile")}</div></c:if>
                            <div class="panel-main-content form-horizontal min-row">
                                <div class="form-group">
                                    <div class="col-10"><strong>Company Profile</strong></div>
                                    <div class="clear"></div>
                                </div>
                                <div>
                                    <div class="form-group">
                                        <label class="col-xs-5 col-md-4 control-label">Company Name</label>
                                        <div class="col-sm-7 col-md-5 col-xs-7"><p>${companyProfile.name}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-5 col-md-4 control-label">Year of Establishment</label>
                                        <div class="col-sm-7 col-md-5 col-xs-7"><p>${companyProfile.yearEstablished}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-5 col-md-4 control-label">UEN</label>
                                        <div class="col-sm-7 col-md-5 col-xs-7"><p>185412420D</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-5 col-md-4 control-label">Is the mailing address the same as the company address?</label>
                                        <div class="col-sm-7 col-md-5 col-xs-7"><p>
                                            <c:choose>
                                                <c:when test="${companyProfile.sameAddress eq 'Y'}">Yes</c:when>
                                                <c:otherwise>No</c:otherwise>
                                            </c:choose></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-5 col-md-4 control-label">Is the company registered in Singapore or overseas?</label>
                                        <div class="col-sm-7 col-md-5 col-xs-7"><p>${companyProfile.registered}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-5 col-md-4 control-label">Postal Code</label>
                                        <div class="col-sm-7 col-md-5 col-xs-7"><p>${companyProfile.postalCode}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-5 col-md-4 control-label">Address Type</label>
                                        <div class="col-sm-7 col-md-5 col-xs-7"><p>${companyProfile.addressType}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-5 col-md-4 control-label">Block / House No</label>
                                        <div class="col-sm-7 col-md-5 col-xs-7"><p>${companyProfile.block}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-5 col-md-4 control-label">Street Name</label>
                                        <div class="col-sm-7 col-md-5 col-xs-7"><p>${companyProfile.streetName}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-5 col-md-4 control-label">Building Name</label>
                                        <div class="col-sm-7 col-md-5 col-xs-7"><p>${companyProfile.building}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                </div>
                            </div>

                            <c:if test="${adminEditJudge}"><div class="text-right app-font-size-16">${fn:replace(editFragString, "REPLACE-STEP-KEY", "appInfo_companyAdmin")}</div></c:if>
                            <div class="panel-main-content form-horizontal min-row">
                                <div class="form-group">
                                    <div class="col-10"><strong>Main Administrator</strong></div>
                                    <div class="clear"></div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-5 col-md-4 control-label">Salutation</label>
                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${companyAdmin.mainAdmin.salutation}</p></div>
                                    <div class="clear"></div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-5 col-md-4 control-label">Name</label>
                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${companyAdmin.mainAdmin.name}</p></div>
                                    <div class="clear"></div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-5 col-md-4 control-label">Nationality</label>
                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${companyAdmin.mainAdmin.nationality}</p></div>
                                    <div class="clear"></div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-5 col-md-4 control-label">ID No.</label>
                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${companyAdmin.mainAdmin.idNumber}</p></div>
                                    <div class="clear"></div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-5 col-md-4 control-label">Designation</label>
                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${companyAdmin.mainAdmin.designation}</p></div>
                                    <div class="clear"></div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-5 col-md-4 control-label">Contact No.</label>
                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${companyAdmin.mainAdmin.contactNo}</p></div>
                                    <div class="clear"></div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-5 col-md-4 control-label">Email Address</label>
                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${companyAdmin.mainAdmin.email}</p></div>
                                    <div class="clear"></div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-5 col-md-4 control-label">Employment Start Date</label>
                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${companyAdmin.mainAdmin.employmentStartDt}</p></div>
                                    <div class="clear"></div>
                                </div>
                            </div>

                            <div class="panel-main-content form-horizontal min-row">
                                <div class="form-group">
                                    <div class="col-10"><strong>Alternate Administrator</strong></div>
                                    <div class="clear"></div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-5 col-md-4 control-label">Salutation</label>
                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${companyAdmin.alternativeAdmin.salutation}</p></div>
                                    <div class="clear"></div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-5 col-md-4 control-label">Name</label>
                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${companyAdmin.alternativeAdmin.name}</p></div>
                                    <div class="clear"></div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-5 col-md-4 control-label">Nationality</label>
                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${companyAdmin.alternativeAdmin.nationality}</p></div>
                                    <div class="clear"></div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-5 col-md-4 control-label">ID No.</label>
                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${companyAdmin.alternativeAdmin.idNumber}</p></div>
                                    <div class="clear"></div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-5 col-md-4 control-label">Designation</label>
                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${companyAdmin.alternativeAdmin.designation}</p></div>
                                    <div class="clear"></div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-5 col-md-4 control-label">Contact No.</label>
                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${companyAdmin.alternativeAdmin.contactNo}</p></div>
                                    <div class="clear"></div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-5 col-md-4 control-label">Email Address</label>
                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${companyAdmin.alternativeAdmin.email}</p></div>
                                    <div class="clear"></div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-5 col-md-4 control-label">Employment Start Date</label>
                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${companyAdmin.alternativeAdmin.employmentStartDt}</p></div>
                                    <div class="clear"></div>
                                </div>
                            </div>

                            <div class="panel-main-content form-horizontal min-row">
                                <div class="form-group">
                                    <div class="col-10"><strong>Details of Certifying Team</strong></div>
                                    <div class="clear"></div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-5 col-md-4 control-label">
                                        <a href="javascript:void(0)" onclick="expandFile('previewSubmit','bsbPreviewSubmit')">View Certifying Team Details</a></label>
                                    <div class="clear"></div>
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
                            <c:if test="${docEditJudge}"><div class="text-right app-font-size-16">${fn:replace(editFragString, "REPLACE-STEP-KEY", "primaryDoc")}</div></c:if>
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