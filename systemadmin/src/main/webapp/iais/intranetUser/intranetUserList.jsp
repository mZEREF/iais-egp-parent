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

    function doDeactivate(Id) {
        $("[name='crud_action_value']").val(Id);
        $("[name='crud_action_deactivate']").val('doDeactivate');
        submit('doDelete');
    }

    function doReactivate(Id) {
        $("[name='crud_action_value']").val(Id);
        $("[name='crud_action_deactivate']").val('doReactivate');
        submit('doDelete');
    }

    function doTerminate(Id) {
        $("[name='crud_action_value']").val(Id);
        $("[name='crud_action_deactivate']").val('doTerminate');
        submit('doDelete');
    }

    // function getPageValue(pageNo){
    //     SOP.Crud.cfxSubmit("IntranetUserForm","changePage",pageNo);
    // }

    <%--$(function () {--%>
    <%--    doPage(${IntranetUserSearchParam.pageNo});--%>
    <%--});--%>

    <%--function doPage(pageNo){--%>
    <%--    $(".pagination").empty() ;--%>
    <%--    parseInt(pageNo);--%>
    <%--    var tempStr = "";--%>
    <%--    var pageCount = ${pageCount}--%>
    <%--    if (pageNo > 1){--%>
    <%--        tempStr += "<li><a href=\"#\" aria-label=\"Previous\" onclick=\"getPageValue("+(pageNo - 1)+")\"><span aria-hidden=\"true\"><em class=\"fa fa-chevron-left\"></em></span></a></li>";--%>
    <%--    }else{--%>
    <%--        tempStr += "<li class=\"active\"><a href=\"#\" aria-label=\"Previous\" onclick=\"getPageValue("+(pageNo - 1)+")\"><span aria-hidden=\"true\"><em class=\"fa fa-chevron-left\"></em></span></a></li>";--%>
    <%--    }--%>
    <%--    if(pageCount<3){--%>
    <%--        for (var i = 1 ;i <= pageCount;i++) {--%>
    <%--            if(pageNo == i){--%>
    <%--                tempStr += "<li class=\"active\"><a onclick='getPageValue("+ i +")'>"+i+"</a></li>";--%>
    <%--            }else{--%>
    <%--                tempStr += "<li><a onclick='getPageValue("+ i +")'>"+i+"</a></li>";--%>
    <%--            }--%>
    <%--        }--%>
    <%--    }else if(pageNo + 2 <= pageCount){--%>
    <%--        for (var i = 0 ;i < 3;i++) {--%>
    <%--            if(pageNo == pageNo + i){--%>
    <%--                tempStr += "<li class=\"active\"><a onclick='getPageValue("+ (pageNo + i) +")'>"+(pageNo + i)+"</a></li>";--%>
    <%--            }else{--%>
    <%--                tempStr += "<li><a onclick='getPageValue("+ (pageNo + i) +")'>"+(pageNo + i)+"</a></li>";--%>
    <%--            }--%>
    <%--        }--%>
    <%--    }else {--%>
    <%--        for (var i = 2 ;i > -1;i--) {--%>
    <%--            if (pageNo == pageCount - i) {--%>
    <%--                tempStr += "<li class=\"active\"><a onclick='getPageValue(" + (pageCount - i) + ")'>" + (pageCount - i) + "</a></li>";--%>
    <%--            }else{--%>
    <%--                tempStr += "<li><a onclick='getPageValue(" + (pageCount - i) + ")'>" + (pageCount - i) + "</a></li>";--%>
    <%--            }--%>
    <%--        }--%>
    <%--    }--%>
    <%--    if(pageNo < pageCount){--%>
    <%--        tempStr += "<li><a aria-label=\"Next\"><span aria-hidden=\"true\" onclick=\"getPageValue("+(pageNo + 1)+")\"><em class=\"fa fa-chevron-right\"></em></span></a></li>";--%>
    <%--    }else{--%>
    <%--        tempStr += "<li class=\"active\"><a aria-label=\"Next\"><span aria-hidden=\"true\" onclick=\"getPageValue("+(pageNo + 1)+")\"><em class=\"fa fa-chevron-right\"></em></span></a></li>";--%>
    <%--    }--%>
    <%--    $(".pagination").append(tempStr);--%>
    <%--}--%>
</script>