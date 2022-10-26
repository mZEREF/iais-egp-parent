<%@tag description="Preview page of facility registration" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>

<%@attribute name="docFrag" fragment="true" %>
<%@attribute name="compareWrap" required="true" type="sg.gov.moh.iais.egp.bsb.dto.compare.CompareWrap" %>

<div class="preview-gp">
    <div class="row">
        <div class="col-xs-12">
            <div class="panel-group" role="tablist" aria-multiselectable="true">
                <div class="panel panel-default">
                    <div class="panel-body">
                        <div class="panel-main-content form-horizontal min-row">
                        <c:set var="oldDeferDto" value="${compareWrap.oldDto}"/>
                        <c:set var="newDeferDto" value="${compareWrap.newDto}"/>
                        <%--@elvariable id="oldDeferDto" type="sg.gov.moh.iais.egp.bsb.dto.appview.deferrenew.DeferRenewViewDto"--%>
                        <%--@elvariable id="newDeferDto" type="sg.gov.moh.iais.egp.bsb.dto.appview.deferrenew.DeferRenewViewDto"--%>
                        <div>
                            <div class="form-group">
                                <label class="col-xs-4 control-label">Active Facility Number</label>
                                <div class="col-xs-4"><p data-compare-old="facProfileFacName" data-val="<c:out value='${oldDeferDto.facilityNo}'/>"><c:out value="${oldDeferDto.facilityNo}"/></p></div>
                                <div class="col-xs-4"><p data-compare-new="facProfileFacName" data-val="<c:out value='${newDeferDto.facilityNo}'/>" class="compareTdStyle" style="display: none"><c:out value="${newDeferDto.facilityNo}"/></p></div>
                                <div class="clear"></div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-4 control-label">Facility Name</label>
                                <div class="col-xs-4"><p data-compare-old="facProfileSameAddress" data-val="<c:out value='${oldDeferDto.facilityName}'/>"><c:out value="${oldDeferDto.facilityName}"/></p></div>
                                <div class="col-xs-4"><p data-compare-new="facProfileSameAddress" data-val="<c:out value='${newDeferDto.facilityName}'/>" class="compareTdStyle" style="display: none"><c:out value="${newDeferDto.facilityName}"/></p></div>
                                <div class="clear"></div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-4 control-label">Expected Date of Renewal</label>
                                <div class="col-xs-4"><p data-compare-old="facProfilePostalCode" data-val="<c:out value='${oldDeferDto.deferRenewDate}'/>"><c:out value="${oldDeferDto.deferRenewDate}"/></p></div>
                                <div class="col-xs-4"><p data-compare-new="facProfilePostalCode" data-val="<c:out value='${newDeferDto.deferRenewDate}'/>" class="compareTdStyle" style="display: none"><c:out value="${newDeferDto.deferRenewDate}"/></p></div>
                                <div class="clear"></div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-4 control-label">Reason for deferment</label>
                                <div class="col-xs-4"><p data-compare-old="facProfileAddressType" data-val="<c:out value='${oldDeferDto.deferRenewReason}'/>"><iais:code code="${oldDeferDto.deferRenewReason}"/></p></div>
                                <div class="col-xs-4"><p data-compare-new="facProfileAddressType" data-val="<c:out value='${newDeferDto.deferRenewReason}'/>" class="compareTdStyle" style="display: none"><iais:code code="${newDeferDto.deferRenewReason}"/></p></div>
                                <div class="clear"></div>
                            </div>
                            <jsp:invoke fragment="docFrag"/>
                            <div class="form-group" style="margin: 0">
                                <ol style="padding-left: 16px">
                                    <li class="col-xs-12">
                                        <div class="col-xs-8 form-group" style="padding-left: 0">I, hereby declare that all the information I have provided here is true and accurate. If any of the information given herein changes or becomes inaccurate in any way, I shall immediately notify MOH Biosafety Branch of such change or inaccuracy</div>
                                        <div class="form-check col-xs-2" style="text-align: right">
                                            <span class="fa fa-circle-o"></span> Yes
                                        </div>
                                        <div class="form-check col-xs-2" style="text-align: right">
                                            <span class="fa fa-dot-circle-o"></span> No
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