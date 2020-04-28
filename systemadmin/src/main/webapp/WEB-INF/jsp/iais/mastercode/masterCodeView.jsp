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
    function jumpToPagechangePage() {
        submit('changePage');
    }

    function sortRecords(sortFieldName, sortType) {
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        submit('appSort');
    }

    $('.selectedFile').change(function () {
        $("[name='crud_action_type']").val('doUpload');
        $("#MasterCodeFileForm").submit();
    });

    $("#MC_Clear").click(function () {
        $("[name='codeCategory']").val("");
        $("[name='codeDescription']").val("");
        $("[name='esd']").val("");
        $("[name='eed']").val("");
        $("#codeStatus option:first").prop("selected", 'selected').val("Please Select");
        $(".clearMC .current").text("Please Select");
    });

</script>
