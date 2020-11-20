<%--
  Created by IntelliJ IDEA.
  User: ecquaria
  Date: 2019/12/24
  Time: 16:13
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iaia" uri="ecquaria/sop/egov-mc" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<%
    String webroot = IaisEGPConstant.BE_CSS_ROOT;
%>
<div class="main-content">
    <form class="" method="post" id="IntranetUserForm" enctype="multipart/form-data"
          action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_deactivate" value="">
        <input type="hidden" id="maskUserId" name="maskUserId" value="">
        <input type="hidden" id="importUserSelect" name="importUserSelect" value="">
        <input type="hidden" id="userFileSize" name="userFileSize" value="${userFileSize}">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="bg-title">
                            <h2>Intranet User View</h2>
                        </div>
                        <%@ include file="doSearch.jsp" %>
                        <%@ include file="userList.jsp" %>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript">
    function submitUser(action) {
        $("[name='crud_action_type']").val(action);
        $("#IntranetUserForm").submit();
    }

    function doCreate() {
        submitUser('doSave');
    }

    function doStatus() {
        submitUser('doStatus');
    }

    $("#IU_Search").click(function () {
        submitUser('doSearch');
    });
    $("#IU_Clear").click(function () {
        $('#userId').val("");
        $('#emailAddress').val("");
        $('#displayName').val("");
        $('#roleAssigned').val("");
        $('#privilegeAssigned').val("");
        $("#accountStatus option[text = 'Please Select']").val("selected", "selected");
        $("#accountStatus").val("");
        $(".form-horizontal .current").text("Please Select");
    });

    function doEdit(Id) {
        $('#maskUserId').val(Id)
        submitUser('doEdit');
    }

    function doDelete(Id) {
        $('#maskUserId').val(Id)
        $("[name='crud_action_value']").val(Id);
        submitUser('doDelete');
    }

    function doRole(Id) {
        $('#maskUserId').val(Id)
        $("[name='crud_action_value']").val(Id);
        submitUser('doRole');
    }

    function doExport() {
        if ($("input[type='checkbox']").is(':checked')) {
            $('#exportError').hide();
            var arr = [];
            var r =document.getElementsByName("userUid");
            for(var i=0;i<r.length;i++) {
                if (r[i].checked) {
                    arr.push(r[i].value);
                }
            }
            $.ajax({
                'url': '${pageContext.request.contextPath}/intranetUserAjax/userId',
                'dataType': 'json',
                'data': {userIds:arr},
                'type': 'POST',
                'traditional':true,
                'success': function (data) {
                },
                'error': function () {
                }
            });
            showPopupWindow('/system-admin-web/eservice/INTRANET/IntranetUserDownload');
        } else {
            $('#exportError').show();
        }
    }

    function doExportRole() {
        if ($("input[type='checkbox']").is(':checked')) {
            $('#exportError').hide();
            var arr = [];
            var r =document.getElementsByName("userUid");
            for(var i=0;i<r.length;i++) {
                if (r[i].checked) {
                    arr.push(r[i].value);
                }
            }
            $.ajax({
                'url': '${pageContext.request.contextPath}/intranetUserAjax/userId',
                'dataType': 'json',
                'data': {userIds:arr},
                'type': 'POST',
                'traditional':true,
                'success': function (data) {
                },
                'error': function () {
                }
            });
            showPopupWindow('/system-admin-web/eservice/INTRANET/IntrantUserRoleExport');
        } else {
            $('#exportError').show();
        }
    }

    function doImport() {
        showWaiting();
        const userFileSize = $("#userFileSize").val();
        const error = validateUploadSizeMaxOrEmpty(userFileSize, "inputFile");
        if (error == "N"){
            $('#error_userUploadFile').html('The file has exceeded the maximum upload size of '+ userFileSize + 'M.');
            dismissWaiting();
        } else {
            var fileName =  getFileName($("#inputFile").val());
            var fileType = "XML";
            var list = fileName.split(".");
            fileName = list[list.length-1];
            if(fileType.indexOf(fileName.toUpperCase()) == -1){
                $('#error_userUploadFile').html('Only files with the following extensions are allowed: XML. Please re-upload the file.');
                dismissWaiting();
            } else {
                submitUser('doImport');
            }
        }
    }

    function jumpToPagechangePage() {
        SOP.Crud.cfxSubmit("IntranetUserForm", "page");
    }
    function getFileName(o) {
        var pos = o.lastIndexOf("\\");
        return o.substring(pos + 1);
    }

    function sortRecords(sortFieldName, sortType) {
        SOP.Crud.cfxSubmit("IntranetUserForm", "sort", sortFieldName, sortType);
    }
</script>