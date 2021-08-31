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
    .glyphicon {
        position: relative;
        top: 15px;
        display: inline-block;
        font-family: 'Glyphicons Halflings',sans-serif;
        font-style: normal;
        font-weight: normal;
        line-height: 1;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
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
                            <span>Enter GIRO Payee Details</span>
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
                        <div class="panel-body">
                            <div class="panel-main-content">
                                <iais:section title="" id = "supPoolList">
                                    <div class="table-responsive">
                                        <div class="table-gp">
                                            <table aria-describedby="" class="table">
                                                <thead>
                                                <tr >
                                                    <th scope="col" style="display: none"></th>
                                                    <iais:sortableHeader needSort="true"
                                                                         field="UEN_NO"
                                                                         value="UEN"/>
                                                    <iais:sortableHeader needSort="true" field="LICENCE_NO"
                                                                         value="Licence No."/>
                                                    <iais:sortableHeader needSort="true" field="SVC_NAME"
                                                                         value="Service Type"/>
                                                    <iais:sortableHeader needSort="true" field="LICENSEE_NAME"
                                                                         value="Licensee"/>
                                                </tr>
                                                </thead>
                                                <tbody id="sortLicSession" class="form-horizontal">
                                                <c:forEach var="pool"
                                                           items="${hciSession.rows}"
                                                           varStatus="status">
                                                    <tr>
                                                        <td >
                                                            <c:out value="${pool.uenNo}"/>
                                                        </td>
                                                        <td>
                                                            <c:out value="${pool.licenceNo}"/>
                                                        </td>
                                                        <td >
                                                            <c:out value="${pool.svcName}"/>
                                                        </td>
                                                        <td>
                                                            <c:out value="${pool.licenseeName}"/>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>

                                    </div>

                                    <iais:row>
                                        <iais:field value="Account Name " mandatory="true"/>
                                        <div class="col-sm-7 col-md-4 col-xs-10">
                                            <label style="width:180%;font-weight:normal;">
                                                <input type="text" maxlength="60" onkeypress="keyAlphanumericPress()" style=" font-weight:normal;" name="acctName" value="${acctName}" />
                                                <div><span  id="error_acctName" name="iaisErrorMsg" class="error-msg" ></span></div>
                                            </label>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Bank Name " mandatory="true"/>
                                        <div class="col-sm-7 col-md-4 col-xs-10">
                                            <iais:select name="bankName" options="bankNameSelectOptions" firstOption="Please Select" value="${bankName}" needSort="true" ></iais:select>
                                            <span  id="error_bankName" name="iaisErrorMsg" class="error-msg" ></span>
                                        </div >
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Bank Code "  mandatory="true"/>
                                        <div class="col-sm-7 col-md-4 col-xs-10">
                                            <label style="width:180%;font-weight:normal;">
                                                <input type="text" maxlength="4" onkeypress="keyNumericPress()"  style=" font-weight:normal;" name="bankCode" value="${bankCode}" />
                                                <div><span  id="error_bankCode" name="iaisErrorMsg" class="error-msg" ></span></div>
                                            </label>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Branch Code "  mandatory="true"/>
                                        <div class="col-sm-7 col-md-4 col-xs-10">
                                            <label style="width:180%;font-weight:normal;">
                                                <input type="text" maxlength="3" onkeypress="keyNumericPress()"  style=" font-weight:normal;" name="branchCode" value="${branchCode}" />
                                                <div><span  id="error_branchCode" name="iaisErrorMsg" class="error-msg" ></span></div>
                                            </label>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Bank Account No. " mandatory="true"/>
                                        <div class="col-sm-7 col-md-4 col-xs-10">
                                            <label style="width:180%;font-weight:normal;">
                                                <input type="text" maxlength="10" onkeypress="keyAlphanumericPress()"  style=" font-weight:normal;" name="bankAccountNo" value="${bankAccountNo}" />
                                                <div><span  id="error_bankAccountNo" name="iaisErrorMsg" class="error-msg" ></span></div>
                                            </label>
                                        </div>
                                    </iais:row>

                                    <iais:row>
                                        <iais:field value="GIRO Form " mandatory="true"/>
                                        <div class="document-upload-gp col-sm-7 col-md-4 col-xs-10">
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
                                    <iais:row>
                                        <iais:field value="Internal Remarks " />
                                        <div class="col-sm-7 col-md-4 col-xs-10">
                                            <label style="width:180%;font-weight:normal;">
                                                <textarea rows="7" cols="70" maxlength="4000"  style=" font-weight:normal;" name="remarks" >${remarks}</textarea>
                                                <div><span  id="error_remarks" name="iaisErrorMsg" class="error-msg" ></span></div>
                                            </label>
                                        </div>
                                    </iais:row>
                                </iais:section>

                                <iais:action style="text-align:right;">
                                    <a style=" float:left;padding-top: 1.1%;text-decoration:none;" href="#" onclick="javascript:doBack()"><em class="fa fa-angle-left"> </em> Back</a>

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
    function keyAlphanumericPress() {
        var keyCode = event.keyCode;
        if (keyCode >= 48 && keyCode <= 57 || keyCode >= 65 && keyCode <= 90 || keyCode >= 97 && keyCode <= 122) {
            event.returnValue = true;
        }else {
            event.returnValue = false;
        }
    }

    function keyNumericPress() {
        var keyCode = event.keyCode;
        if (keyCode >= 48 && keyCode <= 57 ) {
            event.returnValue = true;
        }else {
            event.returnValue = false;
        }
    }

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

    function sortRecords(sortFieldName, sortType) {
        $.post(
            '${pageContext.request.contextPath}/sort-licence-session',
            {
                crud_action_value : sortFieldName,
                crud_action_additional : sortType
            },
            function (data) {
                if(data == null){
                    return;
                }
                let res = data.orgPremResult;
                let html = '';
                console.log(res.rowCount);
                for (let i = 0; i < res.rowCount; i++) {

                    html +=
                        '<tr><td>' + res.rows[i].uenNo + '</td>' +
                        '<td>' + res.rows[i].licenceNo + '</td>' +
                        '<td>' + res.rows[i].svcName + '</td>' +
                        '<td>' + res.rows[i].licenseeName + '</td>' +
                        '</tr>';
                }
                console.log(html);
                $('#sortLicSession').html(html)
            }
        );
    }
</script>
