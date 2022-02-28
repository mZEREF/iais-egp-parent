<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.lang.String"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-inspection-follow-up-items.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-inspection-rectify-file.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="dashboard.jsp"%>

<form method="post" id="mainForm" enctype="multipart/form-data" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" id="deleteNewFiles" name="deleteNewFiles" value="">
    <input type="hidden" id="deleteExistFiles" name="deleteExistFiles" value="">
    <div id="fileUploadInputDiv" style="display: none"></div>

    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-horizontal">
                        <div class="col-md-10">
                            <div class="row" style="font-weight: 700;text-align: center">
                                <div class="col-md-1">S/N</div>
                                <div class="col-md-7">Follow-Up Item</div>
                                <div class="col-md-2">Due Date</div>
                                <div class="col-md-2">Remarks</div>
                            </div>
                            <div class="row" style="text-align: center;border-top:1px solid #D1D1D1;padding: 10px 0 ">
                                <div class="col-md-1">1</div>
                                <div class="col-md-7"><c:out value="${rectifyItemDto.itemText}"/></div>
                                <div class="col-md-2"><c:out value="${rectifyItemDto.deadline}"/></div>
                                <div class="col-md-2"><c:out value="${rectifyItemDto.remarks}"/></div>
                            </div>
                            <c:if test="${action_value eq 'extension'}">
                                <%@ include file="extension.jsp"%>
                            </c:if>
                            <c:if test="${action_value eq 'upload'}">
                                <%@ include file="upload.jsp"%>
                            </c:if>
                            <h3>Remarks</h3>
                            <div class="form-group">
                                <div class="col-md-12 col-sm-12">
                                    <label for="remarks"></label><textarea autocomplete="off" class="col-xs-12" name="remarks" id="remarks" maxlength="1000" style="width: 100%"><c:out value="${rectifyItemSaveDto.remarks}"/></textarea>
                                    <span data-err-ind="remarks" class="error-msg"></span>
                                </div>
                            </div>
                            <div class="application-tab-footer">
                                <div class="row">
                                    <div class="col-xs-12">
                                        <div class="button-group">
                                            <a class="btn btn-primary" id="cancelBtn" >Cancel</a>
                                            <a class="btn btn-primary" id="saveBtn" >Save</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
</form>