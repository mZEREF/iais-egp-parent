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
<webui:setLayout name="iais-internet"/>
<form method="post" id="mainForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
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
                                    <iais:section title="" id="rfiDetail">
                                        <iais:row>
                                            <iais:field value="Title "/>
                                            <iais:value width="18">
                                                <label class="control-label">
                                                    <span >${licPreReqForInfoDto.officerRemarks}</span>
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Licence No. "/>
                                            <iais:value width="18">
                                                <label class="control-label">
                                                    <span>${licPreReqForInfoDto.licenceNo}</span>
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Due Date "/>
                                            <iais:value width="18">
                                                <span class="control-label"><fmt:formatDate value="${licPreReqForInfoDto.dueDateSubmission}"
                                                                      pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/></span>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:value width="18">
                                                <label>
                                                    <input type="checkbox" disabled name="reqType"
                                                           <c:if test="${not empty licPreReqForInfoDto.licPremisesReqForInfoReplyDtos}">checked</c:if> />Information
                                                </label>
                                                <label>
                                                    <input type="checkbox" disabled name="reqType"
                                                           <c:if test="${licPreReqForInfoDto.needDocument}">checked</c:if> />Supporting
                                                    Documents
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <H3></H3>
                                        <c:forEach items="${licPreReqForInfoDto.licPremisesReqForInfoReplyDtos}"
                                                   var="infoReply" varStatus="infoStatus">
                                            <iais:row style="text-align:center;">
                                                <div class="col-sm-7 col-md-10 col-xs-10">
                                                    <span>${infoReply.title}</span>
                                                </div>
                                            </iais:row>
                                            <iais:row style="text-align:center;">
                                                <iais:value width="18">
                                                    <label>
                                                        <textarea maxlength="1000" name="userReply${infoReply.id}"
                                                                  rows="8" style=" font-weight:normal;"
                                                                  cols="70">${infoReply.userReply}</textarea><span
                                                            id="error_userReply${infoReply.id}" name="iaisErrorMsg"
                                                            class="error-msg"></span>
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                        </c:forEach>
                                        <c:if test="${licPreReqForInfoDto.needDocument}">
                                            <c:forEach items="${licPreReqForInfoDto.licPremisesReqForInfoDocDto}"
                                                       var="rfiDoc" varStatus="docStatus">
                                                <iais:row>
                                                    <div class="col-sm-7 col-md-11 col-xs-10">
                                                        <h3>${rfiDoc.title}</h3>
                                                    </div>
                                                </iais:row>
                                                <iais:row>
                                                    <iais:value width="18">
                                                        <div class="file-upload-gp">
                                                            <div class="file-upload-gp">
                                                                <span><a href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${docStatus.index}&fileRo${docStatus.index}=<iais:mask name="fileRo${docStatus.index}" value="${rfiDoc.fileRepoId}"/>&fileRepoName=${rfiDoc.docName}">${rfiDoc.docName}</a></span>
                                                                <c:choose>
                                                                    <c:when test="${rfiDoc.docName == '' || rfiDoc.docName == null }">
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <%--<span class="existFile delBtn <c:if test="${!isClickEdit || AppSubmissionDto.onlySpecifiedSvc}">hidden</c:if>">--%>
                                                                        <span class="existFile delBtn ">
                                                                        &nbsp;&nbsp;<button type="button"
                                                                                            class="btn btn-danger btn-sm"><em
                                                                                class="fa fa-times"></em></button>
                                                                        </span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                                <br/>
                                                                <input class="selectedFile commDoc" id="commonDoc${rfiDoc.id}"
                                                                       name="UploadFile${rfiDoc.id}" type="file"
                                                                       style="display: none;"
                                                                       aria-label="selectedFile">
                                                                <span name="iaisErrorMsg" class="error-msg"
                                                                      id="error_UploadFile${rfiDoc.id}"></span><br>
                                                                <a class="btn btn-file-upload btn-secondary">Attachment</a><br/>
                                                            </div>
                                                            <br/>
                                                        </div>
                                                    </iais:value>
                                                </iais:row>
                                            </c:forEach>

                                        </c:if>
                                        <iais:action style="text-align:left;">
                                            <a onclick="javascript:doBack()">< Back</a>
                                        </iais:action>
                                        <iais:action style="text-align:right;">
                                            <button class="btn btn-primary" type="button"
                                                    onclick="javascript:doSubmit()">Proceed
                                                to Submit
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
<script type="text/javascript">
    function doBack() {
        SOP.Crud.cfxSubmit("mainForm", "back");
    }

    function doSubmit(reqInfoId) {
        SOP.Crud.cfxSubmit("mainForm", "submit");
    }

    function getFileName(o) {
        var pos = o.lastIndexOf("\\");
        return o.substring(pos + 1);
    }

    $('.selectedFile').change(function () {
        var file = $(this).val();
        $(this).parent().children('span:eq(0)').html(getFileName(file));
        $(this).parent().children('span:eq(0)').next().html('&nbsp;&nbsp;<button type="button" class="btn btn-secondary btn-sm">Delete</button>');
        $(this).parent().children('span:eq(0)').next().removeClass("hidden");
    });

    $('.delBtn').click(function () {
        $(this).parent().children('span:eq(0)').html('');
        $(this).parent().children('span:eq(0)').next().html();
        $(this).parent().children('span:eq(0)').next().addClass("hidden");
        $(this).parent().children('input.selectedFile').val('');

    });


</script>

