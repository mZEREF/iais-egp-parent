<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<%@ include file="dashboard.jsp" %>
<%@ include file="mainContent.jsp" %>

<script type="text/javascript">

    function submit(pageTab,action){
        if(pageTab == "inbox"){
            $("[name='crud_action_type']").val(action);
            $("#inboxForm").submit();
        }else if (pageTab == "app") {
            $("[name='crud_action_type']").val(action);
            $("#appInboxForm").submit();
        }else{
            $("[name='crud_action_type']").val(action);
            $("#licenceForm").submit();
        }

    }

    $("#inboxType").change(function() {
        submit('inbox','doSearch');
    });

    $("#inboxService").change(function() {
        submit('inbox','doSearch');
    });

    $("#appType").change(function() {
        submit('app','doSearch')
    });

    $("#appStatus").change(function() {
        submit('app','doSearch')
    });

    $(function () {
        doPage(${inboxParam.pageNo});
        doPage(${appParam.pageNo});
        activeTab('${TAB_NO}');
    });

    function activeTab(tabNo){
        if(tabNo == 'inboxTab'){
            $('#'+tabNo+' a[href="#tabInbox"]').tab('show');
        }else if(tabNo == 'appTab'){
            $('#'+tabNo+' a[href="#tabApplication"]').tab('show');
        }else {
            $('#'+tabNo+' a[href="#tabLicence"]').tab('show');
        }

    }
    function getPageValue(pageNo){
        SOP.Crud.cfxSubmit("inboxForm","doPage",pageNo);
    }

    function doPage(pageNo){
        $(".pagination").empty() ;
        parseInt(pageNo);
        var tempStr = "";
        var pageCount = ${pageCount}
        if (pageNo > 1){
            tempStr += "<li><a href=\"#\" aria-label=\"Previous\" onclick=\"getPageValue("+(pageNo - 1)+")\"><span aria-hidden=\"true\"><em class=\"fa fa-chevron-left\"></em></span></a></li>";
        }else{
            tempStr += "<li class=\"active\"><a href=\"#\" aria-label=\"Previous\" onclick=\"getPageValue("+(pageNo - 1)+")\"><span aria-hidden=\"true\"><em class=\"fa fa-chevron-left\"></em></span></a></li>";
        }
        if(pageCount<3){
            for (var i = 1 ;i <= pageCount;i++) {
                if(pageNo == i){
                    tempStr += "<li class=\"active\"><a onclick='getPageValue("+ i +")'>"+i+"</a></li>";
                }else{
                    tempStr += "<li><a onclick='getPageValue("+ i +")'>"+i+"</a></li>";
                }
            }
        }else if(pageNo + 2 <= pageCount){
            for (var i = 0 ;i < 3;i++) {
                if(pageNo == pageNo + i){
                    tempStr += "<li class=\"active\"><a onclick='getPageValue("+ (pageNo + i) +")'>"+(pageNo + i)+"</a></li>";
                }else{
                    tempStr += "<li><a onclick='getPageValue("+ (pageNo + i) +")'>"+(pageNo + i)+"</a></li>";
                }
            }
        }else {
            for (var i = 2 ;i > -1;i--) {
                if (pageNo == pageCount - i) {
                    tempStr += "<li class=\"active\"><a onclick='getPageValue(" + (pageCount - i) + ")'>" + (pageCount - i) + "</a></li>";
                }else{
                    tempStr += "<li><a onclick='getPageValue(" + (pageCount - i) + ")'>" + (pageCount - i) + "</a></li>";
                }
            }
        }
        if(pageNo < pageCount){
            tempStr += "<li><a aria-label=\"Next\"><span aria-hidden=\"true\" onclick=\"getPageValue("+(pageNo + 1)+")\"><em class=\"fa fa-chevron-right\"></em></span></a></li>";
        }else{
            tempStr += "<li class=\"active\"><a aria-label=\"Next\"><span aria-hidden=\"true\" onclick=\"getPageValue("+(pageNo + 1)+")\"><em class=\"fa fa-chevron-right\"></em></span></a></li>";
        }
        $(".pagination").append(tempStr);
    }

    function searchLicenceNo(){
        SOP.Crud.cfxSubmit("inboxForm","doSearch")
    }


</script>