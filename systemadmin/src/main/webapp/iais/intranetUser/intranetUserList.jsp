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
    <form class="" method="post" id="IntranetUserForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_deactivate" value="">
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
    function submitUser(action){
        $("[name='crud_action_type']").val(action);
        $("#IntranetUserForm").submit();
    }

    function doCreate(){
        submitUser('doSave');
    }

    function doStatus(){
        submitUser('doStatus');
    }

    $("#IU_Search").click(function() {
        submitUser('doSearch');
    });
    $("#IU_Clear").click(function() {
        $('#userId').val("");
        $('#emailAddress').val("");
        $('#displayName').val("");
        $('#roleAssigned').val("");
        $('#privilegeAssigned').val("");


    });

    function doEdit(Id){
        $("[name='crud_action_value']").val(Id);
        submitUser('doEdit');
    }

    function doDelete(Id){
        $("[name='crud_action_value']").val(Id);
        submitUser('doDelete');
    }


    function doExport() {
            submitUser('doExport');
    }

    function doImport() {
        // document.getElementById("inputFileAgent").value = document.getElementById("inputFile").value;
        var file=document.getElementById("inputFile").value;
        //var form1=document.getElementById("form1");
        var ext = file.slice(file.lastIndexOf(".")+1).toLowerCase();
        if ("xml" != ext) {
            alert("please import .xml");
            return false;
        }else {
            submitUser('doImport');
    }

    }

    function jumpToPagechangePage(){
        SOP.Crud.cfxSubmit("IntranetUserForm", "page");
    }

    function sortRecords(sortFieldName,sortType){
        SOP.Crud.cfxSubmit("IntranetUserForm","sort",sortFieldName,sortType);
    }
</script>