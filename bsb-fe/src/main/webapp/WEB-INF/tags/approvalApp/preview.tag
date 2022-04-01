<%@tag description="Preview page of approval app" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>

<%@attribute name="facProfileDto" required="true" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.register.approval.FacProfileDto>" %>
<%@attribute name="approvalToPossessDto" required="false" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalToPossessDto>" %>
<%@attribute name="approvalToLargeDto" required="false" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalToLargeDto>" %>
<%@attribute name="approvalToSpecialDto" required="false" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalToSpecialDto>" %>
<%@attribute name="approvalToActivityDto" required="false" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalToActivityDto>" %>
<%@attribute name="facAuthorisedDto" required="false" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.register.approval.FacAuthorisedDto>" %>

<%@attribute name="docFrag" fragment="true" %>
<%@attribute name="editFrag" fragment="true" %>
<%@attribute name="approvalProfileListEditJudge" type="java.lang.Boolean" %>
<%@attribute name="docEditJudge" type="java.lang.Boolean" %>

<jsp:invoke fragment="editFrag" var="editFragString"/>

<div class="preview-gp">
    <div class="row">
        <div class="col-xs-12">
            <div class="panel-group" role="tablist" aria-multiselectable="true">
                <div class="panel panel-default">
                    <div class="panel-heading completed">
                        <h4 class="panel-title">
                            <a class="collapsed" data-toggle="collapse" href="#previewBatInfo">Biological Agents &amp; Toxins</a>
                        </h4>
                    </div>
                    <div id="previewBatInfo" class="panel-collapse collapse">
                        <div class="panel-body">
                            <c:if test="${approvalProfileListEditJudge}"><div class="text-right app-font-size-16">${fn:replace(editFragString, "REPLACE-STEP-KEY", "approvalProfile")}</div></c:if>
                            <c:forEach var="approvalProfile" items="${approvalProfileList}">
                                <div class="panel-main-content form-horizontal min-row">
                                    <div class="form-group">
                                        <div class="col-10"><strong><iais:code code="${approvalProfile.schedule}"/></strong></div>
                                        <div class="clear"></div>
                                    </div>
                                    <c:forEach var="info" items="${approvalProfile.batInfos}">
                                        <div>
                                            <div class="form-group">
                                                <label class="col-xs-5 col-md-4 control-label">List of Agents or Toxins</label>
                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.batName}</p></div>
                                                <div class="clear"></div>
                                            </div>
                                        </div>
                                        <div>
                                            <div class="form-group">
                                                <label class="col-xs-5 col-md-4 control-label">Estimated maximum volume (in litres) of production at any one time</label>
                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.prodMaxVolumeLitres}</p></div>
                                                <div class="clear"></div>
                                            </div>
                                        </div>
                                        <div>
                                            <div class="form-group">
                                                <label class="col-xs-5 col-md-4 control-label">Method or system used for large scale production</label>
                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.lspMethod}</p></div>
                                                <div class="clear"></div>
                                            </div>
                                        </div>
                                        <div>
                                            <div class="form-group">
                                                <label class="col-xs-5 col-md-4 control-label">Mode of Procurement</label>
                                                <div class="col-sm-7 col-md-5 col-xs-7"><p><iais:code code="${info.procurementMode}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                        </div>
                                        <div>
                                            <div class="form-group">
                                                <label class="col-xs-5 col-md-4 control-label">Transfer From Facility Name</label>
                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.facilityNameOfTransfer}</p></div>
                                                <div class="clear"></div>
                                            </div>
                                        </div>
                                        <div>
                                            <div class="form-group">
                                                <label class="col-xs-5 col-md-4 control-label">Expected Date of Transfer</label>
                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.expectedDateOfImport}</p></div>
                                                <div class="clear"></div>
                                            </div>
                                        </div>
                                        <div>
                                            <div class="form-group">
                                                <label class="col-xs-5 col-md-4 control-label">Contact Person from Transferring Facility</label>
                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.contactPersonNameOfTransfer}</p></div>
                                                <div class="clear"></div>
                                            </div>
                                        </div>
                                        <div>
                                            <div class="form-group">
                                                <label class="col-xs-5 col-md-4 control-label">Contact No of Contact Person from Transferring Facility</label>
                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.impCtcPersonNo}</p></div>
                                                <div class="clear"></div>
                                            </div>
                                        </div>
                                        <div>
                                            <div class="form-group">
                                                <label class="col-xs-5 col-md-4 control-label">Email Address of Contact Person from Transferring Facility</label>
                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.contactPersonEmailOfTransfer}</p></div>
                                                <div class="clear"></div>
                                            </div>
                                        </div>
                                        <div>
                                            <div class="form-group">
                                                <label class="col-xs-5 col-md-4 control-label">Facility Address 1</label>
                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.transferFacAddr1}</p></div>
                                                <div class="clear"></div>
                                            </div>
                                        </div>
                                        <div>
                                            <div class="form-group">
                                                <label class="col-xs-5 col-md-4 control-label">Facility Address 2</label>
                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.transferFacAddr2}</p></div>
                                                <div class="clear"></div>
                                            </div>
                                        </div>
                                        <div>
                                            <div class="form-group">
                                                <label class="col-xs-5 col-md-4 control-label">Facility Address 3</label>
                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.transferFacAddr3}</p></div>
                                                <div class="clear"></div>
                                            </div>
                                        </div>
                                        <div>
                                            <div class="form-group">
                                                <label class="col-xs-5 col-md-4 control-label">Country</label>
                                                <div class="col-sm-7 col-md-5 col-xs-7"><p><iais:code code="${info.transferCountry}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                        </div>
                                        <div>
                                            <div class="form-group">
                                                <label class="col-xs-5 col-md-4 control-label">City</label>
                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.transferCity}</p></div>
                                                <div class="clear"></div>
                                            </div>
                                        </div>
                                        <div>
                                            <div class="form-group">
                                                <label class="col-xs-5 col-md-4 control-label">State</label>
                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.transferState}</p></div>
                                                <div class="clear"></div>
                                            </div>
                                        </div>
                                        <div>
                                            <div class="form-group">
                                                <label class="col-xs-5 col-md-4 control-label">Postal Code</label>
                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.transferPostalCode}</p></div>
                                                <div class="clear"></div>
                                            </div>
                                        </div>
                                        <div>
                                            <div class="form-group">
                                                <label class="col-xs-5 col-md-4 control-label">Name of Courier Service Provider</label>
                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.courierServiceProviderName}</p></div>
                                                <div class="clear"></div>
                                            </div>
                                        </div>
                                        <div>
                                            <div class="form-group">
                                                <label class="col-xs-5 col-md-4 control-label">Remarks</label>
                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.remarks}</p></div>
                                                <div class="clear"></div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:forEach>
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