<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<%@ include file="mainContent.jsp" %>

<script type="text/javascript">
    function submit(action){
        $("[name='crud_action_type']").val(action);
        $("#MasterCodeForm").submit();
    }

    $("#MC_Search").click(function() {
        submit('doSearch');
    });

    function doEdit(mcId){
        $("[name='crud_action_value']").val(mcId);
        submit('doEdit');
    }

    function doDelete(mcId){
        $("[name='crud_action_value']").val(mcId);
        submit('doDelete');
    }
    
    function doDeactivate(mcId) {
        $("[name='crud_action_value']").val(mcId);
        $("[name='crud_action_deactivate']").val('doDeactivate');
        submit('doDelete');
    }

    function doCreate(){
        submit('doCreate');
    }

    $(function () {
        doPage(${MasterCodeSearchParam.pageNo});
    });

    function getPageValue(pageNo){
        SOP.Crud.cfxSubmit("MasterCodeForm","changePage",pageNo);
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
</script>
