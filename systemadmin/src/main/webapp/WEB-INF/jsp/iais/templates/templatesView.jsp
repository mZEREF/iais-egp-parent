<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
    String webroot = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.COMMON_CSS_ROOT;
%>
<webui:setLayout name="iais-intranet"/>
<%@ include file="mainContent.jsp" %>
<%@ include file="../mastercode/suggestJs.jsp" %>
<script src="<%=webroot%>js/tinymce/tinymce.min.js"></script>
<script src="<%=webroot%>js/initTinyMce.js"></script>
<script>
    function submit(action){
        $("[name='crud_action_type']").val(action);
        $("#TemplatesForm").submit();
    }

    function doPreview(msgId) {
        $("[name='crud_action_value']").val(msgId);
        submit("preview");
    }
    $("#ANT_Search").click(function () {
        submit("search");
    });

    function doEdit(msgId){
        $("[name='crud_action_value']").val(msgId);
        submit('edit');
    }
    function jumpToPagechangePage() {
        submit('page');
    }

    function sortRecords(sortFieldName, sortType) {
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        submit('sort');
    }

    $("#ANT_Clearn").click(function () {
        $("[name='templateName']").val("");
        $("[name='esd']").val("");
        $("[name='eed']").val("");
        $("#tepProcess option:first").prop("selected", 'selected').val("Please Select");
        $("#msgType option:first").prop("selected", 'selected').val("Please Select");
        $("#deliveryMode option:first").prop("selected", 'selected').val("Please Select");
        $(".clearTep .current").text("Please Select");
    });

    var admdirector = $("#templateName").bsSuggest({
        indexId: 0,
        indexKey: 0,
        allowNoKeyword: false,
        multiWord: false,
        separator: ",",
        getDataMethod: "url",
        showHeader: false,
        url: '${pageContext.request.contextPath}/suggest-template-description?description=',
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


