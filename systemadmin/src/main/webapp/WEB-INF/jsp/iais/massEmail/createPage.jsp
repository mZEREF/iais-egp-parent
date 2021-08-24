<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<style>
    .btn.btn-sm {
        font-size: .775rem;
        font-weight: 500;
        padding: 6px 10px;
        text-transform: uppercase;
        border-radius: 30px;
    }
</style>
<div class="main-content">
    <form class="form-horizontal" method="post" id="mainForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_additional" value="">
        <input type="hidden" id="fileMaxMBMessage" name="fileMaxMBMessage" value="<iais:message key="GENERAL_ERR0019" propertiesKey="iais.system.upload.file.limit" replaceName="sizeMax" />">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="bg-title">
                            <h2>${title} Distribution List</h2>
                        </div>

                        <div class="form-group">
                            <iais:field value="Distribution Name" required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="name" type="text" name="name" maxlength="500" value="${distribution.getDisname()}">
                                    <span id="error_disname" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>

                        <div class="form-group" >
                            <iais:field value="Service" required="true"/>

                            <iais:value>
                                <iais:value width="10">
                                    <iais:select name="service" id="service" options="serviceSelection"
                                                 firstOption="Please Select"  value="${distribution.getService()}"></iais:select>
                                </iais:value>
                            </iais:value>
                            <span id="error_service" name="iaisErrorMsg" class="error-msg"></span>
                        </div>

                        <div class="form-group" id="serviceDivByrole">
                            <iais:field value="Distribution List" required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <iais:select name="role" options="roleSelection" value="${distribution.getRole()}" firstOption="Please Select" disabled=""></iais:select>
                                    <span id="error_role" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>

                        <div class="form-group" id="addEmail" <c:if test="${distribution.mode eq 'SMS'}">hidden</c:if>>
                            <label class="col-xs-4 col-md-4 control-label" >Add Email Addresses</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <textarea cols="50" rows="10" style="width: 100%" name="email" class="textarea" id="email" title="content">${emailAddress}</textarea>
                                    <span id="error_addr" name="iaisErrorMsg" class="error-msg">${emailErr}</span>
                                </div>
                            </iais:value>
                        </div>

                        <div class="form-group" id="addMobile" <c:if test="${empty distribution.mode or  distribution.mode eq 'Email'}">hidden</c:if>>
                            <label class="col-xs-4 col-md-4 control-label" >Add Mobile</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <textarea style="width: 100%" rows="10" name="mobile" class="textarea" id="mobile" title="content">${emailAddress}</textarea>
                                    <span id="error_mobileNo" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>

                        <div class="form-group">
                            <iais:field value="Mode of Delivery" required="true"/>
                            <c:choose>
                                <c:when test="${'1'.equals(distributionCanEdit)}">
                                    <iais:value width="10">
                                        <iais:select firstOption="Please Select" name="mode" id="mode" options="mode" value="${distribution.getMode()}" ></iais:select>
                                    </iais:value>
                                </c:when>
                                <c:otherwise>
                                    <div class="col-xs-8 col-sm-6 col-md-5">
                                        <input id="mode" type="text" name="mode" maxlength="500" value="${distribution.getMode()}" readonly>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-4 col-md-4 control-label">Attachments</label>
                            <div class="document-upload-gp col-xs-8 col-md-8">
                                <div class="document-upload-list">
                                    <div class="file-upload-gp">
                                        <div class="filename fileNameDisplay">
                                            <c:out value="${fileName}"/>
                                            <c:if test="${!empty fileName}">
                                                &emsp;<button type='button' class='btn btn-secondary btn-sm' onclick='deleteFile()'>Delete</button>
                                            </c:if>
                                        </div>
                                            <input id="selectedFile" name="selectedFile" type="file" style="display: none;" aria-label="selectedFile1">
                                            <a class="btn btn-file-upload btn-secondary" href="#">Upload</a>
                                    </div>
                                </div>
                                <span name="iaisErrorMsg" class="error-msg" id="error_file"></span><br>
                            </div>

                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-sm-6">
                            <a href="#" class="back" id="back"><em class="fa fa-angle-left"></em> Back</a>
                        </div>
                        <div class="col-xs-12 col-sm-6">
                            <div class="text-right text-center-mobile">
                                <button id="saveDis" type="button" class="btn btn-primary">SAVE</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <input hidden name="distributionId" value="<c:out value="${distribution.getId()}"/>">
    </form>
    <%@include file="/WEB-INF/jsp/include/validation.jsp"%>
</div>



<script type="text/javascript">
    $('#saveDis').click(function(){
        SOP.Crud.cfxSubmit("mainForm","save");
    });

    $("#back").click(function () {
        SOP.Crud.cfxSubmit("mainForm","back");
    })

    $("#service").change(function () {
        $.ajax({
            data:{
                serviceCode:$(this).children('option:selected').val()
            },
            type:"POST",
            dataType: 'json',
            url:'/system-admin-web/emailAjax/recipientsRoles.do',
            error:function(data){

            },
            success:function(data){
                var html = '<label class="col-xs-4 col-md-4 control-label" >Distribution List<span style="color: red"> *</span></label><div class="col-xs-8 col-sm-6 col-md-5">';
                html += data.roleSelect;
                html += ' <span id="error_role" name="iaisErrorMsg" class="error-msg"></span></div>';
                $("#serviceDivByrole").html(html);
                /*$("div.premSelect->ul").mCustomScrollbar({
                        advanced:{
                            updateOnContentResize: true
                        }
                    }
                );*/
            }
        });
    })


    $("#mode").change(function () {
        if($(this).val() =="Email"){
            $("#addEmail").show();
            $("#addMobile").hide();
        }else{
            $("#addEmail").hide();
            $("#addMobile").show();
        }
    })
    function deleteFile(){
        $.ajax({
            data:{email:$("#email").val(),
                mobile:$("#mobile").val()},
            type:"POST",
            dataType:"json",
            url:"/system-admin-web/emailAjax/recoverTextarea",
            success:function (data) {
                console.log(data)
                $(".filename").html("");
                $("#mobile").val(data.mobile);
                $("#email").val(data.email);
                $("#error_file").hide();
            }
        })

    }

    $('#selectedFile').change(function (event) {
        var maxFileSize = 10;
        var error = validateUploadSizeMaxOrEmpty(maxFileSize, 'selectedFile');
        console.log(error)
        if (error == "N"){
            $("#selectedFile").val('');
            $('#error_file').html($("#fileMaxMBMessage").val());
        }else{
            var files = event.target.files;
            $(".filename").html("");
            for(var i=0;i<files.length;i++){
                $(".filename").append("<div class='fileNameDisplay'>"+files[i].name+"&emsp;<button type='button' class='btn btn-secondary btn-sm' onclick='deleteFile()'>Delete</button></div>");
            }
            SOP.Crud.cfxSubmit("mainForm","upload");
            $('#error_file').html('');
        }


    });

</script>