<%--
  Created by IntelliJ IDEA.
  User: mjy
  Date: 2021/3/2
  Time: 16:36
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<style>
    .btn.btn-sm {
        font-size: .775rem;
        font-weight: 500;
        padding: 6px 10px;
        text-transform: uppercase;
        border-radius: 30px;
    }
</style>
<webui:setLayout name="iais-intranet"/>
<div class="main-content dashboard">
    <form method="post" id="mainForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" id="configFileSize" value="${configFileSize}"/>
        <input type="hidden" id="fileMaxMBMessage" name="fileMaxMBMessage" value="<iais:message key="GENERAL_ERR0019" propertiesKey="iais.system.upload.file.limit" replaceName="sizeMax" />">
        <div class="center-content">
            <div class="intranet-content">
                <div class="row form-horizontal">
                    <div class="bg-title col-xs-12 col-md-12">
                        <h2>
                            <span>Add a GIRO Payee</span>
                        </h2>
                    </div>
                    <div class="col-xs-12 col-md-12">
                        <iais:row>
                            <div class=" col-xs-12 col-md-12">
                                Note: This function is to add a GIRO Payee who submitted a manual application.
                            </div>
                        </iais:row>
                        <iais:row>
                            <div class=" col-xs-12 col-md-12">
                                The GIRO arrangement must be approved by the bank, otherwise GIRO deductions for that payee will fail.
                            </div>
                        </iais:row>
                        <div class="row">&nbsp;</div>
                        <div class="row"><h3>Enter GIRO Payee Details</h3></div>
                        <div class="panel-body">
                            <div class="panel-main-content">
                                <iais:section title="" id = "supPoolList">
                                    <iais:row>
                                        <iais:field value="HCI Code(s) :"/>
                                        <div class="col-sm-7 col-md-6 col-xs-10">
                                            <c:forEach items="${hciSession.rows}" var="hci">
                                                ${hci.hciCode}<br>
                                            </c:forEach>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="HCI Name(s) :"/>
                                        <div class="col-sm-7 col-md-6 col-xs-10">
                                            <c:forEach items="${hciSession.rows}" var="hci">
                                                ${hci.hciName}<br>
                                            </c:forEach>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Account Name :" mandatory="true"/>
                                        <div class="col-sm-7 col-md-6 col-xs-10">
                                            <label>
                                                <input type="text" maxlength="66" style=" font-weight:normal;" name="acctName" value="${acctName}" />
                                                <div><span style="font-weight:normal;" id="error_acctName" name="iaisErrorMsg" class="error-msg" ></span></div>
                                            </label>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Bank Code :"  mandatory="true"/>
                                        <div class="col-sm-7 col-md-6 col-xs-10">
                                            <label>
                                                <input type="text" maxlength="66" style=" font-weight:normal;" name="bankCode" value="${bankCode}" />
                                                <div><span style="font-weight:normal;" id="error_bankCode" name="iaisErrorMsg" class="error-msg" ></span></div>
                                            </label>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Branch Code :"  mandatory="true"/>
                                        <div class="col-sm-7 col-md-6 col-xs-10">
                                            <label>
                                                <input type="text" maxlength="66" style=" font-weight:normal;" name="branchCode" value="${branchCode}" />
                                                <div><span style="font-weight:normal;" id="error_branchCode" name="iaisErrorMsg" class="error-msg" ></span></div>
                                            </label>
                                        </div>
                                    </iais:row>

                                    <iais:row>
                                        <iais:field value="Bank Name :" mandatory="true"/>
                                        <div class="col-sm-7 col-md-6 col-xs-10">
                                            <label>
                                                <input type="text" maxlength="256" style=" font-weight:normal;" name="bankName" value="${bankName}" />
                                                <div><span style="font-weight:normal;" id="error_bankName" name="iaisErrorMsg" class="error-msg" ></span></div>
                                            </label>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Bank Account No. :" mandatory="true"/>
                                        <div class="col-sm-7 col-md-6 col-xs-10">
                                            <label>
                                                <input type="text" maxlength="66" style=" font-weight:normal;" name="bankAccountNo" value="${bankAccountNo}" />
                                                <div><span style="font-weight:normal;" id="error_bankAccountNo" name="iaisErrorMsg" class="error-msg" ></span></div>
                                            </label>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Customer Reference No. :" mandatory="true"/>
                                        <div class="col-sm-7 col-md-6 col-xs-10">
                                            <label>
                                                <input type="text" maxlength="12" style=" font-weight:normal;" name="cusRefNo" value="${cusRefNo}" />
                                                <div><span style="font-weight:normal;" id="error_cusRefNo" name="iaisErrorMsg" class="error-msg" ></span></div>
                                            </label>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <label class="col-xs-0 col-md-4 ">GIRO Form :<span class="mandatory">*</span></label>
                                        <div class="document-upload-gp col-sm-7 col-md-6 col-xs-10">
                                            <div class="document-upload-list">
                                                <div class="file-upload-gp">
                                                    <input id="selectFile" name="selectFile" type="file" class="iptFile" style="display: none;">
                                                    <div id="uploadFileBox" class="file-upload-gp">
                                                        <c:forEach var="attachmentDto" items="${giroAcctFileDto.attachmentDtos}"
                                                                   varStatus="status">
                                                            <p class="fileList">
                                                                <a href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${docStatus.index}&fileRo${docStatus.index}=<iais:mask name="fileRo${docStatus.index}" value="${attachmentDto.id}"/>&fileRepoName=${attachmentDto.docName}">${attachmentDto.docName}</a>
                                                                &emsp;<button type="button" class="btn btn-secondary btn-sm" onclick="writeMessageDeleteFile('${attachmentDto.id}')">Delete</button><input hidden name='fileSize' value='${attachmentDto.docSize}'/></p>
                                                        </c:forEach>
                                                    </div>
                                                    <a class="btn btn-file-upload btn-secondary" href="#">Upload</a><br>
                                                    <span name="iaisErrorMsg" class="error-msg"
                                                          id="error_UploadFile"></span>
                                                    <ul class="upload-enclosure-ul">
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </iais:row>
                                </iais:section>

                                <iais:action style="text-align:right;">
                                    <a style=" float:left;padding-top: 1.1%;text-decoration:none;" onclick="javascript:doBack()"><em class="fa fa-angle-left"> </em> Back</a>

                                    <button class="btn btn-primary" type="button"  onclick="javascript:doSubmit()">Preview and Submit</button>
                                </iais:action>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">

    function doBack(){
        showWaiting();
        $("[name='crud_action_type']").val("back");
        $("#mainForm").submit();
    }

    function doSubmit(){
        showWaiting();
        $("[name='crud_action_type']").val("next");
        $("#mainForm").submit();
    }

    $('#selectFile').change(function (event) {
        var maxFileSize = $('#configFileSize').val();
        console.log('maxFileSize : '+maxFileSize);
        var error = validateUploadSizeMaxOrEmpty(maxFileSize, 'selectFile');
        console.log(error)
        if (error == "N"){
            $('#error_UploadFile').html($("#fileMaxMBMessage").val());
            $("#selectFile").val('');
            $(".filename").html("");
        }else if(error == "Y"){
            if("Y" == validateAllFileSize()){
                callAjaxUploadFile();
                $('#error_UploadFile').html('');
            }else{
                $('#error_UploadFile').html($("#fileMaxMBMessage").val());
                $("#selectFile").val('');
            }
        }
    });

    function validateAllFileSize(){
        var maxSize = $('#configFileSize').val();
        var fileSize = (Math.floor(getAllFileSize() / 1024));
        console.log('all file size : ' + fileSize);
        if(fileSize >= maxSize){
            return "N";
            console.log('validate all fileSize flag : N');
        }
        console.log('validate all fileSize flag : Y');
        return "Y";
    }

    function getAllFileSize(){
        var allSize = 0;
        $('input[name="fileSize"]').each(function(){
            allSize += Math.round($(this).val());
        });
        var fileId= '#selectFile';
        var fileV = $(fileId).val();
        var file = $(fileId).get(0).files[0];
        console.log(fileV)
        console.log(file)
        console.log(file.size / (1024))
        var currentFileSize = 0;
        if(fileV == null || fileV == "" ||file==null|| file==undefined){
            currentFileSize = 0;
        }else{
            currentFileSize = Math.round(file.size / (1024)) + Math.round(allSize);
            console.log('test currentFileSize1 : ' + currentFileSize);
        }
        console.log('currentFileSize2 : ' + currentFileSize);
        console.log('all size : ' + allSize);
        return currentFileSize;
    }

    function writeMessageDeleteFile(deleteWriteMessageFileId){
        showWaiting();
        console.log(deleteWriteMessageFileId)
        $.ajax({
            type: "post",
            url:  "${pageContext.request.contextPath}/deleteGiroFromFile",
            data: {deleteWriteMessageFileId:deleteWriteMessageFileId},
            dataType: "text",
            success: function (data) {
                $('#uploadFileBox').html(data);
                dismissWaiting();
            },
            error: function (msg) {
                alert("error");
            }
        });
    }

    function callAjaxUploadFile(){
        var formData = new FormData($("#mainForm")[0]);
        $.ajax({
            type: "post",
            url:  "${pageContext.request.contextPath}/uploadGiroFromFile",
            data: formData,
            async:true,
            processData: false,
            contentType: false,
            dataType: "text",
            success: function (data) {
                $('#uploadFileBox').html(data);
            },
            error: function (msg) {
                alert("error");
            }
        });
    }
</script>
