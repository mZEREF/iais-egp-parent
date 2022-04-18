<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="egov-cloud" uri="ecquaria/sop/egov-cloud" %>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@include file="dashboard.jsp" %>
<form method="post" id="mainForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <input type="hidden" name="sysFileSize" id="sysFileSize" value="${sysFileSize}"/>
    <input type="hidden" name="uploadKey" value="1"/>

    <div id="selectFileDiv">
        <input id="selectedFile" class="selectedFile"  name="selectedFile" type="file" style="display: none;" onclick="fileClicked(event)" onchange="fileChangedLocal(this,event)" aria-label="selectedFile1">
    </div>
    <input type="hidden" id="fileMaxMBMessage" name="fileMaxMBMessage" value="<iais:message key="GENERAL_ERR0019" propertiesKey="iais.system.upload.file.limit" replaceName="sizeMax" />">
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
                                            <iais:field value="Reference No. "/>
                                            <iais:value width="7" cssClass="col-md-7" display="true">
                                                <c:out value="${licPreReqForInfoDto.facilityNo}"/>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Submission Type "/>
                                            <iais:value width="7" cssClass="col-md-7" display="true">
                                                <c:out value="${licPreReqForInfoDto.submissionType}"/>
                                            </iais:value>

                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Title "/>
                                            <iais:value width="7" cssClass="col-md-7" display="true">
                                                <c:out value="${licPreReqForInfoDto.title}"/>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Due Date "/>
                                            <iais:value width="7" cssClass="col-md-7" display="true">
                                                <iais-bsb:format-LocalDate localDate='${licPreReqForInfoDto.dueDate}'/>
                                            </iais:value>
                                        </iais:row>
                                        <c:if test="${ licPreReqForInfoDto.supportingDocRequired or not empty licPreReqForInfoDto.licPremisesReqForInfoReplyDtos }">
                                            <iais:row>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="checkbox" disabled name="reqType"
                                                               <c:if test="${licPreReqForInfoDto.informationRequired}">checked</c:if> />&nbsp;Information
                                                    </label>
                                                    <label>
                                                        <input type="checkbox" disabled name="reqType"
                                                               <c:if test="${licPreReqForInfoDto.supportingDocRequired}">checked</c:if> />&nbsp;Supporting
                                                        Documents
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                        </c:if>
                                        <H3></H3>
                                        <c:if test="${licPreReqForInfoDto.informationRequired}">
                                            <iais:row >
                                                <div class="col-sm-7 col-md-10 col-xs-10">
                                                    <strong>${licPreReqForInfoDto.titleOfInformationRequired}</strong>
                                                </div>
                                            </iais:row>
                                            <iais:row >
                                                <iais:value width="18">
                                                    <label>
                                                        <textarea  maxlength="1000" name="userReply"
                                                                   rows="8" style=" font-weight:normal;"
                                                                   cols="130">${licPreReqForInfoDto.suppliedInformation}</textarea>
                                                    </label>
                                                    <span id="error_userReply" name="iaisErrorMsg"
                                                          class="error-msg"></span>
                                                </iais:value>
                                            </iais:row>
                                        </c:if>

                                        <c:if test="${licPreReqForInfoDto.supportingDocRequired}">

                                            <c:set var="fileList" value="${rfiMultiFile.value}"/>
                                            <c:set var="configIndex" value="${rfiMultiFile.key}"/>
                                            <iais:row>
                                                <div class="col-sm-7 col-md-11 col-xs-10">
                                                    <strong>${licPreReqForInfoDto.titleOfSupportingDocRequired}</strong>
                                                </div>
                                            </iais:row>
                                            <iais:row>
                                                <div class="col-sm-7 col-md-12 col-xs-10">
                                                    <div class="document-upload-gp ">
                                                        <div class="document-upload-list">
                                                            <h3>Attachment</h3>
                                                            <div class="file-upload-gp">
                                                                <input type="hidden" name="configIndex" value="${configIndex}"/>
                                                                <div id="uploadFileBox${configIndex}" >
                                                                    <c:forEach var="file" items="${fileList}" varStatus="fileStat">
                                                                        <c:if test="${not empty file.docName }">
                                                                            <p class="fileList">
                                                                                <iais:downloadLink fileRepoIdName="fileRo0" fileRepoId="${file.fileRepoId}" docName="${file.docName}"/>
                                                                                &emsp;<button type="button" class="btn btn-secondary btn-sm" onclick="writeMessageDeleteFile('${file.id}','${configIndex}')">Delete</button>
                                                                                <input hidden name='fileSize' value='${file.docSize}'/>
                                                                            </p>
                                                                        </c:if>
                                                                    </c:forEach>
                                                                </div>
                                                                <ul class="upload-enclosure-ul">
                                                                </ul>
                                                                <br/>
                                                                <a class="btn file-upload btn-secondary"  href="javascript:void(0);">UPLOAD</a>
                                                                <br/>
                                                            </div>
                                                        </div>
                                                        <span name="iaisErrorMsg" class="error-msg" id="error_UploadFile${configIndex}"></span>
                                                    </div>
                                                    <br/>
                                                </div>
                                            </iais:row>

                                        </c:if>
                                        <iais:action>
                                            <a style="float:left;padding-top: 1.1%;text-decoration:none;" class="back" onclick="javascript:doBack()"><em class="fa fa-angle-left"></em> Back</a>
                                            <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:doSubmit()">Submit</button>
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
        $("[name='crud_action_type']").val("backList");
        $("#mainForm").submit();
        dismissWaiting();
    }

    function doSubmit() {
        showWaiting();
        $("[name='crud_action_type']").val("submit");
        $("#mainForm").submit();
        dismissWaiting();
    }
    $(document).ready(function () {

        $('.file-upload').click(function () {
            var index = $(this).closest('.file-upload-gp').find('input[name="configIndex"]').val();
            $('input[name="uploadKey"]').val(index);
            $('#selectFileDiv').html('<input id="selectedFile" class="selectedFile"  name="selectedFile" type="file" style="display: none;" onclick="fileClicked(event)" onchange="fileChangedLocal(this,event)" aria-label="selectedFile1">');
            $('input[type="file"]').click();
        });
    });

    function fileChangedLocal(obj, event) {
        var maxFileSize = $('#sysFileSize').val();
        var configIndex =$('input[name="uploadKey"]').val();
        var fileErrorId= '#error_UploadFile'+configIndex;
        console.log('maxFileSize : '+maxFileSize);
        var error = validateUploadSizeMaxOrEmpty(maxFileSize, 'selectedFile');
        console.log(error)
        if (error == "N"){
            $(fileErrorId).html($("#fileMaxMBMessage").val());
            $("#selectedFile").val('');
            callAjaxShowFile(configIndex);
        }else if(error == "Y"){
            callAjaxUploadFile();
            $(fileErrorId).html('');
        }
    }


    function writeMessageDeleteFile(deleteWriteMessageFileId,configIndex){
        showWaiting();
        console.log('deleteWriteMessageFileId : ' + deleteWriteMessageFileId);
        console.log('configIndex : ' + configIndex);
        var fileId= '#uploadFileBox'+configIndex;
        var fileErrorId= '#error_UploadFile'+configIndex;

        $.ajax({
            type: "post",
            url:  "${pageContext.request.contextPath}/deleteGiroFromFile",
            data: {deleteWriteMessageFileId:deleteWriteMessageFileId,
                configIndex:configIndex
            },
            dataType: "text",
            success: function (data) {
                $(fileId).html(data);
                $(fileErrorId).html('');
                dismissWaiting();
            },
            error: function (msg) {
                $("#selectedFile").val('');
                $(fileErrorId).html('');
                dismissWaiting();
            }
        });
    }

    function callAjaxUploadFile(){
        var formData = new FormData($("#mainForm")[0]);


        var configIndex =$('input[name="uploadKey"]').val();
        var fileId= '#uploadFileBox'+configIndex;
        var fileErrorId= '#error_UploadFile'+configIndex;
        console.log('uploadFileBox : ' + fileId);
        $.ajax({
            type: "post",
            url:  "${pageContext.request.contextPath}/uploadRfiFromFile",
            data: formData,
            async:true,
            processData: false,
            contentType: false,
            dataType: "text",
            success: function (data) {
                $(fileId).html(data);
                $(fileErrorId).html('');
            },
            error: function (msg) {
                $("#selectedFile").val('');
                $(fileErrorId).html('');
                dismissWaiting();
            }
        });
    }

    function callAjaxShowFile(configIndex){

        var fileId= '#uploadFileBox'+configIndex;
        console.log('uploadFileBox : ' + fileId);
        $.ajax({
            type: "post",
            url:  "${pageContext.request.contextPath}/showRfiFromFile",
            data: {
                showIndex:configIndex
            },
            dataType: "text",
            success: function (data) {
                $(fileId).html(data);
            },
            error: function (msg) {
                $("#selectedFile").val('');
                dismissWaiting();
            }
        });
    }

</script>

