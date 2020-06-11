<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="egov-cloud" uri="ecquaria/sop/egov-cloud" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="main-content">
        <br><br><br>
        <div class="container">
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
                                            <div class="col-sm-7 col-md-4 col-xs-10">
                                                <iais:datePicker name="Due_date" dateVal="${licPreReqForInfoDto.dueDateSubmission}"/>
                                            </div>
                                            <div class="col-sm-7 col-md-2 col-xs-10">
                                                <button class="btn btn-secondary" type="button"
                                                        onclick="javascript:doExtends('${MaskUtil.maskValue(IaisEGPConstant.CRUD_ACTION_VALUE,licPreReqForInfoDto.id)}')">
                                                    Extends
                                                </button>
                                            </div>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Status :"/>
                                            <div class="col-sm-7 col-md-4 col-xs-10">
                                                <iais:select id="rfiViewStatus" name="status"
                                                             options="salutationStatusList"></iais:select>
                                            </div>
                                        </iais:row>
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
                                        <H3></H3>
                                        <c:if test="${not empty licPreReqForInfoDto.licPremisesReqForInfoReplyDtos}">
                                            <c:forEach items="${licPreReqForInfoDto.licPremisesReqForInfoReplyDtos}"
                                                       var="rfiReply" varStatus="rfiReplyStatus">
                                                <iais:row style="text-align:center;">
                                                    <div class="col-sm-7 col-md-10 col-xs-10">
                                                        <span>${rfiReply.title}</span>
                                                    </div>
                                                </iais:row>
                                                <iais:row style="text-align:center;">
                                                    <iais:value width="18">
                                                        <label>
                                                            <textarea id="userReply" disabled rows="10"
                                                                      cols="100">${rfiReply.userReply}</textarea>
                                                        </label>
                                                    </iais:value>
                                                </iais:row>
                                            </c:forEach>
                                        </c:if>

                                        <c:if test="${licPreReqForInfoDto.needDocument}">
                                            <c:forEach items="${licPreReqForInfoDto.licPremisesReqForInfoDocDto}"
                                                       var="rfiDoc">
                                                <iais:row>
                                                    <div class="col-sm-7 col-md-11 col-xs-10">
                                                        <div class="pop-up">
                                                            <div class="pop-up-body">

                                                                <div class="field col-sm-11 control-label formtext">
                                                                    <label>${rfiDoc.title}</label></div>
                                                                <span class="fileType"
                                                                      style="display:none">Document1</span><span
                                                                    class="fileFilter"
                                                                    style="display:none">png</span><span
                                                                    class="fileMandatory"
                                                                    style="display:none">Yes</span>
                                                                <div class="control col-sm-5">
                                                                    <div class="fileList ">
                                                                    <span class="filename server-site" id="130">
                                                                        <a href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo1&fileRo1=<iais:mask name="fileRo1" value="${rfiDoc.fileRepoId}"/>&fileRepoName=${rfiDoc.docName}"
                                                                           title="Download"
                                                                           class="downloadFile">${rfiDoc.docName}<c:if
                                                                                test="${not empty rfiDoc.docSize}">(${rfiDoc.docSize} KB)</c:if></a>
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
                                                    onclick="javascript:doBack('${licPreReqForInfoDto.id}')">Cancel
                                            </button>
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
</form>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<%@include file="/WEB-INF/jsp/include/utils.jsp" %>
<script type="text/javascript">
    function doBack(reqInfoId) {
        var status = $('.rfiViewStatus').val();
        if (status === "RFI002") {
            showWaiting();
            SOP.Crud.cfxSubmit("mainForm", "cancel", reqInfoId);
        } else {
            showWaiting();
            SOP.Crud.cfxSubmit("mainForm", "back");
        }

    }

    function doExtends(reqInfoId) {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "update", reqInfoId);
    }


</script>

