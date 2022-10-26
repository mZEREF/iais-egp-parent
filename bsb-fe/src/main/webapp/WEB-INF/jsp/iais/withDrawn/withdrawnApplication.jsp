<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/css/bsb/bsb-withdrawn.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-withdrawn.js"></script>
<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp" %>
<%@include file="common/dashboard.jsp" %>
<div class="container">
    <form method="post" id="mainForm" enctype="multipart/form-data" action="<%=process.runtime.continueURL()%>" onsubmit="return validateOtherDocType();">
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="action_type" value="">
        <input type="hidden" name="action_value" value="">
        <input type="hidden" name="action_additional" value="">
        <input type="hidden" name="withdraw_app_list" value="">
        <input type="hidden" name="print_action_type" value="">
<%--        <input type="hidden" id="deleteNewFiles" name="deleteNewFiles" value="">--%>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <%--@elvariable id="withdrawnDto" type="sg.gov.moh.iais.egp.bsb.dto.withdrawn.AppSubmitWithdrawnDto"--%>
        <%--@elvariable id="addWithdrawnAppNos" type="java.util.List<java.lang.String>"--%>
        <div class="navigation-gp">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="internet-content">
                        <div class="center-content">
                            <h2>You are submitting a withdrawal for:</h2>
                            <div class="row">
                                <div class="col-lg-8 col-xs-12">
                                    <c:if test="${withdrawnDto.initialAppNo ne null}">
                                        <div class="withdraw-content-box">
                                            <div class="withdraw-info-gp">
                                                <div class="withdraw-info-row">
                                                    <div class="withdraw-info">
                                                        <p><a href="javascript:void(0);" class="appNo">${withdrawnDto.initialAppNo}</a></p>
                                                    </div>
                                                    <div class="withdraw-delete"></div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:if>
                                    <c:forEach items="${addWithdrawnAppNos}" var="addAppNo">
                                        <div class="withdraw-content-box">
                                            <div class="withdraw-info-gp">
                                                <div class="withdraw-info-row">
                                                    <div class="withdraw-info">
                                                        <p><a href="javascript:void(0);" class="appNo">${addAppNo}</a></p>
                                                    </div>
                                                    <div class="withdraw-delete">
                                                        <p><a href="javascript:void(0);" onclick="deleteWithdraw(this)"><em class="fa fa-trash-o"></em>Delete</a></p>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                    <span data-err-ind="withdrawnAppNos" class="error-msg"></span>
                                </div>
                                <div class="col-lg-4 col-xs-12">
                                    <div class="withdraw-addmore gradient-light-grey">
                                        <a  href="#newappModal" data-toggle="modal" data-target="#newappModal"><h4>
                                            <em class="fa fa-plus-circle"></em> Add more applications</h4>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div id="newappModal" class="modal fade" tabindex="-1" role="dialog" style="top:10px">
                            <div class="modal-dialog modal-dialog-centered" role="document">
                                <!-- Modal content-->
                                <div class="modal-content" style="top:30px">
                                    <div class="modal-header">
                                        <div class="modal-title" id="gridSystemModalLabel" style="font-size: 2rem;">Select application for withdrawal</div>
                                        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span></button>
                                    </div>
                                    <div id="withdrawPagDiv"></div>
                                    <table aria-describedby="" class="table">
                                        <thead style="display: none">
                                            <tr>
                                                <th scope="col" ></th>
                                            </tr>
                                        </thead>
                                        <tbody id="withdrawBodyDiv"></tbody>
                                    </table>
                                    <div class="modal-footer">
                                        <a  class="btn btn-primary withdraw-next" href="javascript:void(0);">Done</a>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="center-content">
                            <h3>Reason for Withdrawal<span style="color: #ff0000"> *</span></h3>
                            <div class="row">
                                <div class="col-md-7">
                                    <iais:select name="reason" cssClass="resonDropdown" id="reason" value="${withdrawnDto.reason}" codeCategory="CATE_ID_BSB_REASON_FOR_WITHDRAWN" firstOption="Please Select" onchange="isRemarksMandatory()"/>
                                    <span data-err-ind="reason" class="error-msg"></span>
                                </div>
                            </div>
                        </div>
                        <div id="remarksDiv" <c:if test="${withdrawnDto.reason ne 'WDREASN005' || withdrawnDto.reason == null}">style="display: none"</c:if>>
                            <div class="row">
                                <div class="center-content">
                                    <label class="col-md-4" style="font-size:2rem">Supporting Remarks<span style="color: #ff0000"> *</span></label>
                                </div>
                            </div>
                            <div class="row">
                                <div class="center-content">
                                    <div class="col-md-6">
                                        <div class="file-upload-gp">
                                            <textarea name="remarks" id="remarks" maxlength="1000" cols="100" rows="10">${withdrawnDto.remarks}</textarea>
                                        </div>
                                        <span data-err-ind="remarks" class="error-msg"></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="center-content">
                            <%@include file="common/supportingDocuments.jsp" %>
                        </div>

                        <div class="center-content">
                            <div class="application-tab-footer">
                                <div class="row" style="padding-top: 10px;">
                                    <div class="col-xs-12 col-sm-6">
                                        <span style="padding-right: 10%" class="components">
                                            <a class="back" href="${backUrl}"><em class="fa fa-angle-left"></em> Previous</a>
                                        </span>
                                    </div>
                                    <a class="btn btn-primary" style="float:right" onclick="doSubmit()" href="javascript:void(0);">Submit</a>
                                    <span style="float:right">&nbsp;</span>
                                    <a href="${backUrl}" type="button" class="btn btn-secondary" style="float:right">CANCEL</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>