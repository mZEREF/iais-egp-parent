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
    <form class="form-horizontal" method="post" id="IntranetUserForm" action=<%=process.runtime.continueURL()%>>
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
                        <iais:pagination  param="IntranetUserSearchParam" result="IntranetUserSearchResult"/>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript">
    function submit(action){
        $("[name='crud_action_type']").val(action);
        $("#IntranetUserForm").submit();
    }

    function doCreate(){
        submit('doSave');
    }

    function doStatus(){
        submit('doStatus');
    }

    $("#IU_Search").click(function() {
        submit('doSearch');
    });

    function doEdit(Id){
        $("[name='crud_action_value']").val(Id);
        submit('doEdit');
    }

    function doDelete(Id){
        $("[name='crud_action_value']").val(Id);
        submit('doDelete');
    }


    function doExport(action) {
        // $("[name='crud_action_value']").val(Id);
        //$("[name='crud_action_type']").val(action);
        submit('port');
    }

    function jumpToPagechangePage(){
        SOP.Crud.cfxSubmit("IntranetUserForm", "page");
    }

    function sortRecords(sortFieldName,sortType){
        SOP.Crud.cfxSubmit("IntranetUserForm","sort",sortFieldName,sortType);
    }
</script>