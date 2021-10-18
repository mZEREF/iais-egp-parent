<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="egov-cloud" uri="ecquaria/sop/egov-cloud" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="dashboard" >
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_additional" value="">
        <div class="main-content">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="center-content">
                        <div class="intranet-content">
                            <br><br><br>
                            <div class="col-xs-12">
                                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                                    <br><br>
                                    <div class="panel panel-default">
                                        <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel"
                                             aria-labelledby="headingOne" aria-expanded="true" style="">
                                            <div class="panel-body">
                                                <div class="panel-main-content">
                                                    <iais:section title="" id="supPoolList">
                                                        <iais:row>
                                                            <iais:field value="Title :"/>
                                                            <iais:value width="18">
                                                                <label class="control-label">
                                                                    <span>${licPreReqForInfoDto.officerRemarks}</span>
                                                                </label>
                                                            </iais:value>
                                                        </iais:row>
                                                        <iais:row>
                                                            <iais:field value="Licence No :"/>
                                                            <iais:value width="18">
                                                                <label class="control-label">
                                                                    <span>${licPreReqForInfoDto.licenceNo}</span>
                                                                </label>
                                                            </iais:value>
                                                        </iais:row>
                                                        <iais:row>
                                                            <iais:field value="Due Date :"/>
                                                            <c:choose>
                                                                <c:when test="${licPreReqForInfoDto.status=='RFIST004'}">
                                                                    <iais:value width="18">
                                                                        <label class="control-label">
                                                                            <fmt:formatDate value="${licPreReqForInfoDto.dueDateSubmission}" pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/></label>
                                                                    </iais:value>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <div class="col-sm-7 col-md-4 col-xs-10">
                                                                        <iais:datePicker name="Due_date" dateVal="${licPreReqForInfoDto.dueDateSubmission}"/>
                                                                    </div>
                                                                </c:otherwise>
                                                            </c:choose>

                                                            <c:if test="${licPreReqForInfoDto.status!='RFIST002'&&licPreReqForInfoDto.status!='RFIST004'}">
                                                                <div class="col-sm-7 col-md-2 col-xs-10">
                                                                    <button class="btn btn-secondary" type="button" data-toggle="modal" data-target= "#extendsDueDate">
                                                                        Extend
                                                                    </button>
                                                                </div>
                                                            </c:if>
                                                        </iais:row>
                                                        <iais:row>
                                                            <iais:field value="Status :"/>
                                                            <div class="col-sm-7 col-md-4 col-xs-10">
                                                                <iais:select id="rfiViewStatus" cssClass="nice-select status" name="status"
                                                                             options="salutationStatusList" value="${licPreReqForInfoDto.status}"></iais:select>
                                                            </div>
                                                        </iais:row>
                                                        <c:if test="${ licPreReqForInfoDto.needDocument or not empty licPreReqForInfoDto.licPremisesReqForInfoReplyDtos }">
                                                            <iais:row>
                                                                <iais:value width="18">
                                                                    <label>
                                                                        <input type="checkbox" disabled name="reqType"
                                                                               <c:if test="${not empty licPreReqForInfoDto.licPremisesReqForInfoReplyDtos}">checked</c:if>/>&nbsp;Information
                                                                    </label>
                                                                </iais:value>
                                                            </iais:row>
                                                            <iais:row>
                                                                <iais:value width="18">
                                                                    <label>
                                                                        <input type="checkbox" disabled name="reqType"
                                                                               <c:if test="${licPreReqForInfoDto.needDocument}">checked</c:if> />&nbsp;Supporting
                                                                        Documents
                                                                    </label>
                                                                </iais:value>
                                                            </iais:row>
                                                        </c:if>
                                                        <H3></H3>
                                                        <c:if test="${not empty licPreReqForInfoDto.licPremisesReqForInfoReplyDtos}">
                                                            <c:forEach items="${licPreReqForInfoDto.licPremisesReqForInfoReplyDtos}"
                                                                       var="rfiReply" varStatus="rfiReplyStatus">
                                                                <iais:row >
                                                                    <div class="field col-sm-11 control-label formtext">
                                                                        <label>${rfiReply.title}</label>
                                                                    </div>
                                                                    <label class="field col-sm-11 control-label formtext">
                                                                                <textarea id="userReply" disabled rows="10" style=" font-weight:normal;"
                                                                                          cols="100">${rfiReply.userReply}</textarea>
                                                                    </label>
                                                                </iais:row>
                                                            </c:forEach>
                                                        </c:if>

                                                        <c:if test="${licPreReqForInfoDto.needDocument}">
                                                            <c:forEach items="${licPreReqForInfoDto.licPremisesReqForInfoMultiFileDto}"
                                                                       var="rfiMultiFile">
                                                                <iais:row>
                                                                    <div class="col-sm-7 col-md-11 col-xs-10">
                                                                        <div class="pop-up">
                                                                            <div class="pop-up-body">
                                                                                <div class="field control-label formtext">
                                                                                    <label>${rfiMultiFile.value.get(0).title}</label>
                                                                                </div>
                                                                                <div class="control ">
                                                                                    <div class="fileList ">
                                                                                        <span class="filename server-site" >
                                                                                            <c:forEach items="${rfiMultiFile.value}"
                                                                                                       var="rfiDoc" varStatus="docStatus">
                                                                                                <iais:downloadLink fileRepoIdName="fileRo${docStatus.index}" fileRepoId="${rfiDoc.fileRepoId}" docName="${rfiDoc.docName}"/>
                                                                                                <br>
                                                                                            </c:forEach>
                                                                                        </span>
                                                                                    </div>
                                                                                </div>

                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </iais:row>
                                                            </c:forEach>
                                                        </c:if>


                                                        <iais:action style="text-align:right;">
                                                            <button class="btn btn-secondary" type="button"
                                                                    onclick="javascript:doBack()">Cancel
                                                            </button>
                                                            <c:if test="${licPreReqForInfoDto.status=='RFIST002'}">
                                                                <button class="btn btn-primary" type="button"
                                                                        onclick="javascript:doSubmit('${MaskUtil.maskValue(IaisEGPConstant.CRUD_ACTION_VALUE,licPreReqForInfoDto.id)}')">Submit
                                                                </button>
                                                            </c:if>
                                                        </iais:action>
                                                    </iais:section>
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
        </div>
    </form>
</div>
<div class="modal fade" id="extendsDueDate" tabindex="-1" role="dialog" aria-labelledby="extendsDueDate">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
<%--            <div class="modal-header">--%>
<%--                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span--%>
<%--                        aria-hidden="true">&times;</span></button>--%>
<%--                <div class="modal-title" id="gridSystemModalLabel" style="font-size:2rem;">Confirmation Box</div>--%>
<%--            </div>--%>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-12"><span style="font-size: 2rem">Are you sure you want to extend the due date?</span>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">No</button>
                <button type="button" class="btn btn-primary" onclick="javascript:doExtends('${MaskUtil.maskValue(IaisEGPConstant.CRUD_ACTION_VALUE,licPreReqForInfoDto.id)}')">Yes</button>
            </div>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<%@include file="/WEB-INF/jsp/include/utils.jsp" %>
<script type="text/javascript">
    function doBack() {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "back");

    }

    function doSubmit(reqInfoId) {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "update", reqInfoId);
    }

    function doExtends(reqInfoId) {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "cancel", reqInfoId);
    }


</script>

