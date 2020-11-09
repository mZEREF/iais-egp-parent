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
    <input type="hidden" name="sysFileSize" id="sysFileSize" value="${sysFileSize}"/>

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
                                                <label class="control-label"><fmt:formatDate value="${licPreReqForInfoDto.dueDateSubmission}"
                                                                      pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/></label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:value width="18">
                                                <label>
                                                    <input type="checkbox" disabled name="reqType"
                                                           <c:if test="${not empty licPreReqForInfoDto.licPremisesReqForInfoReplyDtos}">checked</c:if> />&nbsp;Information
                                                </label>
                                                <label>
                                                    <input type="checkbox" disabled name="reqType"
                                                           <c:if test="${licPreReqForInfoDto.needDocument}">checked</c:if> />&nbsp;Supporting
                                                    Documents
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <H3></H3>
                                        <c:forEach items="${licPreReqForInfoDto.licPremisesReqForInfoReplyDtos}"
                                                   var="infoReply" varStatus="infoStatus">
                                            <iais:row style="text-align:center;">
                                                <div class="col-sm-7 col-md-10 col-xs-10">
                                                    <strong>${infoReply.title}</strong>
                                                </div>
                                            </iais:row>
                                            <iais:row >
                                                <iais:value width="18">
                                                    <label>
                                                        <textarea  maxlength="1000" name="userReply${infoReply.id}"
                                                                  rows="8" style=" font-weight:normal;"
                                                                  cols="130">${infoReply.userReply}</textarea>
                                                    </label>
                                                    <span id="error_userReply${infoReply.id}" name="iaisErrorMsg"
                                                            class="error-msg"></span>
                                                </iais:value>
                                            </iais:row>
                                        </c:forEach>
                                        <c:if test="${licPreReqForInfoDto.needDocument}">
                                            <c:forEach items="${licPreReqForInfoDto.licPremisesReqForInfoDocDto}"
                                                       var="rfiDoc" varStatus="docStatus">
                                                <iais:row>
                                                    <div class="col-sm-7 col-md-11 col-xs-10">
                                                        <strong>${rfiDoc.title}</strong>
                                                    </div>
                                                </iais:row>
                                                <iais:row>
                                                    <iais:value width="18">
                                                        <div class="file-upload-gp">
                                                            <input class="hidden delFlag" type="hidden" name="commDelFlag${rfiDoc.id}" value="Y"/>
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
                                                    </iais:value>
                                                </iais:row>
                                            </c:forEach>

                                        </c:if>
                                        <iais:action style="text-align:left;">
                                            <a onclick="javascript:doBack()"><em class="fa fa-angle-left"> </em> Back</a>
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
        showWaiting();
        $("[name='crud_action_type']").val("back");
        $("#mainForm").submit();
        dismissWaiting();
    }

    function doSubmit(reqInfoId) {
        showWaiting();
        $("[name='crud_action_type']").val("submit");
        $("#mainForm").submit();
        dismissWaiting();
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
        $(this).parent().children('input delFlag').val('N');
    });

    $('.delBtn').click(function () {
        $(this).parent().children('span:eq(0)').html('');
        $(this).parent().children('span:eq(0)').next().html();
        $(this).parent().children('span:eq(0)').next().addClass("hidden");
        $(this).parent().children('input.selectedFile').val('');
        $(this).parent().children('input.delFlag').val('Y');

    });

    $('.commDoc').change(function () {
        var maxFileSize = $('#sysFileSize').val();
        var error = validateUploadSizeMaxOrEmpty(maxFileSize, $(this));
        if (error == "N"){
            $(this).closest('.file-upload-gp').find('.error-msg').html('The file has exceeded the maximum upload size of '+ maxFileSize + 'M.');
            $(this).closest('.file-upload-gp').find('span.delBtn').trigger('click');
            dismissWaiting();
        }else{
            $(this).closest('.file-upload-gp').find('.error-msg').html('');
            dismissWaiting();
        }

    });

    function validateUploadSizeMaxOrEmpty(maxSize,$fileEle) {
        var fileV = $fileEle.val();
        var file = $fileEle.get(0).files[0];
        if(fileV == null || fileV == "" ||file==null|| file==undefined){
            return "E";
        }
        var fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString();
        fileSize = parseInt(fileSize);
        if(fileSize>= maxSize){
            return "N";
        }
        return "Y";
    }

</script>

