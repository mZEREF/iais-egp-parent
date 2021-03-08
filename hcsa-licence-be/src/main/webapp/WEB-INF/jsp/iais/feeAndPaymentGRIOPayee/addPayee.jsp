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
<webui:setLayout name="iais-intranet"/>
<div class="main-content dashboard">
    <form method="post" id="mainForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
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
                                        <div class="col-sm-7 col-md-4 col-xs-10">
                                            <c:forEach items="${hciSession.rows}" var="hci">
                                                ${hci.hciCode}<br>
                                            </c:forEach>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="HCI Name(s) :"/>
                                        <div class="col-sm-7 col-md-4 col-xs-10">
                                            <c:forEach items="${hciSession.rows}" var="hci">
                                                ${hci.hciName}<br>
                                            </c:forEach>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Account Name :" mandatory="true"/>
                                        <div class="col-sm-7 col-md-4 col-xs-10">
                                            <label>
                                                <input type="text" maxlength="66" style=" font-weight:normal;" name="acctName" value="${acctName}" />
                                                <div><span style="font-weight:normal;" id="error_acctName" name="iaisErrorMsg" class="error-msg" ></span></div>
                                            </label>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Bank Code :"  mandatory="true"/>
                                        <div class="col-sm-7 col-md-4 col-xs-10">
                                            <label>
                                                <input type="text" maxlength="66" style=" font-weight:normal;" name="bankCode" value="${bankCode}" />
                                                <div><span style="font-weight:normal;" id="error_bankCode" name="iaisErrorMsg" class="error-msg" ></span></div>
                                            </label>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Branch Code :"  mandatory="true"/>
                                        <div class="col-sm-7 col-md-4 col-xs-10">
                                            <label>
                                                <input type="text" maxlength="66" style=" font-weight:normal;" name="branchCode" value="${branchCode}" />
                                                <div><span style="font-weight:normal;" id="error_branchCode" name="iaisErrorMsg" class="error-msg" ></span></div>
                                            </label>
                                        </div>
                                    </iais:row>

                                    <iais:row>
                                        <iais:field value="Bank Name :" mandatory="true"/>
                                        <div class="col-sm-7 col-md-4 col-xs-10">
                                            <label>
                                                <input type="text" maxlength="256" style=" font-weight:normal;" name="bankName" value="${bankName}" />
                                                <div><span style="font-weight:normal;" id="error_bankName" name="iaisErrorMsg" class="error-msg" ></span></div>
                                            </label>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Bank Account No. :" mandatory="true"/>
                                        <div class="col-sm-7 col-md-4 col-xs-10">
                                            <label>
                                                <input type="text" maxlength="66" style=" font-weight:normal;" name="bankAccountNo" value="${bankAccountNo}" />
                                                <div><span style="font-weight:normal;" id="error_bankAccountNo" name="iaisErrorMsg" class="error-msg" ></span></div>
                                            </label>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Customer Reference No. :" mandatory="true"/>
                                        <div class="col-sm-7 col-md-4 col-xs-10">
                                            <label>
                                                <input type="text" maxlength="12" style=" font-weight:normal;" name="cusRefNo" value="${cusRefNo}" />
                                                <div><span style="font-weight:normal;" id="error_cusRefNo" name="iaisErrorMsg" class="error-msg" ></span></div>
                                            </label>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="GIRO Form :" mandatory="true"/>
                                        <div class="col-sm-7 col-md-4 col-xs-10">
                                            <div class="file-upload-gp">
                                                <input class="hidden validFlag" type="hidden" name="commValidFlag" <c:if test="${docDto.passDocValidate}">value="Y"</c:if> <c:if test="${!docDto.passDocValidate}">value="N"</c:if>/>
                                                <input class="hidden delFlag" type="hidden" name="commDelFlag" value="Y"/>
                                                <span>
                                                <c:choose>
                                                    <c:when test="${docDto.passDocValidate}">
                                                        <a href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${docStatus.index}&fileRo${docStatus.index}=<iais:mask name="fileRo${docStatus.index}" value="${docDto.fileRepoId}"/>&fileRepoName=${docDto.docName}">${docDto.docName}</a>
                                                    </c:when>
                                                    <c:otherwise>
                                                        ${docDto.docName}
                                                    </c:otherwise>
                                                </c:choose>
                                            </span>
                                                <c:choose>
                                                    <c:when test="${docDto.docName == '' || docDto.docName == null }">
                                                        <span class="existFile delBtn "></span>
                                                    </c:when>
                                                    <c:otherwise>
                                                    <span class="existFile delBtn ">
                                                    &nbsp;&nbsp;<button type="button" class="btn btn-secondary btn-sm">Delete</button>
                                                </span>
                                                    </c:otherwise>
                                                </c:choose>
                                                <br/>
                                                <input class="selectedFile commDoc" id="commonDoc"
                                                       name="UploadFile" type="file"
                                                       style="display: none;" onclick="fileClicked(event)" onchange="fileChanged(event)"
                                                       aria-label="selectedFile">
                                                <a class="btn btn-file-upload btn-secondary" href="javascript:void(0);">Attachment</a><br>
                                                <span name="iaisErrorMsg" class="error-msg"
                                                      id="error_UploadFile"></span>
                                            </div>
                                        </div>
                                    </iais:row>
                                </iais:section>

                                <iais:action style="text-align:right;">
                                    <button class="btn btn-primary" type="button"  onclick="$('#mainForm').submit();">Preview and Submit</button>
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
    function getFileName(o) {
        var pos = o.lastIndexOf("\\");
        return o.substring(pos + 1);
    }

    $('.selectedFile').change(function () {
        var file = $(this).val();
        $(this).parent().children('span:eq(0)').html(getFileName(file));
        $(this).parent().children('span:eq(0)').next().html('&nbsp;&nbsp;<button type="button" class="btn btn-secondary btn-sm">Delete</button>');
        $(this).parent().children('span:eq(0)').next().removeClass("hidden");
        $(this).parent().children('input.validFlag').val('N');
    });

    $('.delBtn').click(function () {
        $(this).parent().children('span:eq(0)').html('');
        $(this).parent().children('span:eq(0)').next().html();
        $(this).parent().children('span:eq(0)').next().addClass("hidden");
        $(this).parent().children('input.selectedFile').val('');
        $(this).parent().children('input.validFlag').val('N');
        $(this).parent().children('input.delFlag').val('N');

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
