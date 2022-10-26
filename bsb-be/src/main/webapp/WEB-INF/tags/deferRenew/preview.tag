<%@tag description="Preview page of facility registration" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="bsb" uri="http://www.ecq.com/iais-bsb" %>

<%@attribute name="deferRenewViewDto" required="true" type="sg.gov.moh.iais.egp.bsb.dto.appview.deferrenew.DeferRenewViewDto" %>
<%@attribute name="docFrag" fragment="true" %>
<%@attribute name="pageAppEditSelectDto" required="false" type="sg.gov.moh.iais.egp.bsb.dto.rfi.PageAppEditSelectDto" %>
<%@attribute name="viewType" required="true" type="java.lang.String" %>

<div class="preview-gp">
    <div class="row">
        <div class="col-xs-12">
            <div class="panel-group" role="tablist" aria-multiselectable="true">
                <div class="panel panel-default">
                    <div class="panel-body">
                        <div class="panel-main-content form-horizontal min-row">
                            <c:if test="${'rfi' eq viewType}"><div class="text-right"><input type="checkbox" id="appSelect" name="selectCheckbox" value="application" <c:if test="${pageAppEditSelectDto.appSelect}">checked="checked"</c:if>/></div></c:if>
                            <div>
                                <div class="form-group">
                                    <label class="col-xs-6 control-label">Active Facility Number</label>
                                    <div class="col-xs-6"><p><c:out value="${deferRenewViewDto.facilityNo}"/></p></div>
                                    <div class="clear"></div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-6 control-label">Facility Name</label>
                                    <div class="col-xs-6"><p><c:out value="${deferRenewViewDto.facilityName}"/></p></div>
                                    <div class="clear"></div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-6 control-label">Expected Date of Renewal</label>
                                    <div class="col-xs-6"><p><c:out value="${deferRenewViewDto.deferRenewDate}"/></p></div>
                                    <div class="clear"></div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-6 control-label">Reason for deferment</label>
                                    <div class="col-xs-6"><p><iais:code code="${deferRenewViewDto.deferRenewReason}"/></p></div>
                                    <div class="clear"></div>
                                </div>
                                <br/>
                                <br/>
                                <jsp:invoke fragment="docFrag"/>
                                <br/>
                                <br/>
                                <div class="form-group">
                                    <ol style="padding-left: 16px">
                                        <li class="col-xs-12">
                                            <div class="col-xs-8 form-group" style="padding-left: 0">I, hereby declare that all the information I have provided here is true and accurate. If any of the information given herein changes or becomes inaccurate in any way, I shall immediately notify MOH Biosafety Branch of such change or inaccuracy</div>
                                            <div class="form-check col-xs-2" style="text-align: right">
                                                <span class="fa fa-dot-circle-o"></span> Yes
                                            </div>
                                            <div class="form-check col-xs-2" style="text-align: right">
                                                <span class="fa fa-circle-o"></span> No
                                            </div>
                                        </li>
                                    </ol>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<c:if test="${'rfi' eq viewType}">
    <div class="hidden" id="errorMessage">
        <iais:error>
            <div class="error">
                <h2><iais:message key="PRF_ERR002" escape="true"/></h2>
            </div>
        </iais:error>
    </div>
    <div style="text-align: right"><button name="submitAppRfiBtn" id="submitAppRfiBtn" type="button" class="btn btn-primary">Submit</button></div>
</c:if>