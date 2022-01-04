<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
    String webroot1=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT;
%>
<webui:setLayout name="iais-intranet"/>
<%@ include file="mainContent.jsp" %>
<script type="text/javascript" src="<%=webroot1%>js/bootstrap-suggest/bootstrap-suggest.js" ></script>
<script type="text/javascript">
    function submit(action){
        $("[name='crud_action_type']").val(action);
        $("#MasterCodeForm").submit();
    }

    $("#MC_Search").click(function() {
        showWaiting();
        submit('doSearch');
    });

    function doEdit(mcId){
        showWaiting();
        $("[name='crud_action_value']").val(mcId);
        submit('doEdit');
    }

    function doDeleteOrDeactivate(mcId){
        var fangDuoJi = $('#fangDuoJi').val();
        if(fangDuoJi != 'fangDuoJi'){
            $('#fangDuoJi').val('fangDuoJi');
            $("[name='crud_action_value']").val(mcId);
            submit('doDelete');
        }
    }

    function doCreate(){
        showWaiting();
        submit('doCreateCode');
    }
    function jumpToPagechangePage() {
        submit('changePage');
    }

    function sortRecords(sortFieldName, sortType) {
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        submit('sortRecords');
    }

    $("#MC_Clear").click(function () {
        $("[name='codeCategory']").val("");
        $("[name='codeDescription']").val("");
        $("[name='codeValue']").val("");
        $("[name='filterValue']").val("");
        $("[name='esd']").val("");
        $("[name='eed']").val("");
        $("#codeStatus option:first").prop("selected", 'selected').val("Please Select");
        $(".clearMC .current").text("Please Select");
        $(".error-msg").text("");
    });

    $("#MCUploadFile").click(function () {
        showWaiting();
        submit('doUpload');
    });

    function doCreateCategory(mcId) {
        showWaiting();
        $("[name='crud_action_value']").val(mcId);
        submit('createCode');
    }

    var admdirector = $("#suggestDescription").bsSuggest({
        indexId: 0,
        indexKey: 0,
        allowNoKeyword: false,
        multiWord: false,
        separator: ",",
        getDataMethod: "url",
        // effectiveFields:["codeDescription"],
        // effectiveFields:["name","ename","departName","jobtitle"],
        // effectiveFieldsAlias:{codeDescription: "codeDescription",ename:"英文名",departName:"部门",jobtitle:"职位"},
        // effectiveFieldsAlias:{codeDescription: "codeDescription"},
        showHeader: false,
        url: '${pageContext.request.contextPath}/suggest-code-description?description=',
        processData: function(json){
            var i, len, data = {value: []};
            if(!json || json.length == 0) {
                return false;
            }
            len = json.length;
            for (i = 0; i < len; i++) {
                data.value.push({
                    "codeDescription": json[i]
                });
            }
            console.log(data);
            return data;
        }
    });
</script>
