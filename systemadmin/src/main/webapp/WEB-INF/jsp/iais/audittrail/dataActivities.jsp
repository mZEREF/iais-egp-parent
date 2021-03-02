<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/9/1
  Time: 10:40
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<webui:setLayout name="iais-intranet"/>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<div class="main-content">
    <form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="collapseFlag" value="${param.collapseFlag}">
        <input type="hidden" name="operationType" value="${param.operationType}">
            <br><br><br>
            <tr height="100%">
                <td style="width: 100%;" class="first last">
                    <div id="control--printerFriendly--33" class="section control " style="overflow: visible;">
                        <div class="control-set-font control-font-header section-header">
                            <h2>Search Param</h2>
                        </div>
                        <div id="control--printerFriendly--33**errorMsg_section_top" class="error_placements"></div>
                        <span id="searchParam"></span>
                    </div>
                </td>
            </tr>
        <br>
            <tr height="100%">
                <td style="width: 100%;" class="first last">
                    <div id="control--printerFriendly--34" class="section control " style="overflow: visible;">
                        <div class="control-set-font control-font-header section-header">
                            <h2>Before Data</h2>
                        </div>
                        <div id="control--printerFriendly--34**errorMsg_section_top" class="error_placements"></div>
                        <span id="beforeValue"></span>
                    </div>
                </td>
            </tr>
            <br>
            <tr height="100%">
                <td style="width: 100%;" class="first last">
                    <div id="control--printerFriendly--35" class="section control " style="overflow: visible;">
                        <div class="control-set-font control-font-header section-header">
                            <h2>After Data</h2>
                        </div>

                        <span id="afterValue"></span>
                    </div>
                </td>
            </tr>

            <br>
            <tr height="100%">
                <td style="width: 100%;" class="first last">
                    <div  class="section control " style="overflow: visible;">
                        <div class="control-set-font control-font-header section-header">
                            <h2>Validation Fail Detail</h2>
                        </div>
                        <div class="error_placements"></div>
                        <span id="validationFail"></span>
                    </div>
                </td>
            </tr>

        <br>
        <a class="back" id="Back" onclick="doBack()"><em class="fa fa-angle-left"></em> Back</a>
    </>
</div>

<input hidden id="hbeforeValue" value="<c:out value="${viewAuditActionData.beforeAction}"/>"/>
<input hidden id="hafterValue" value="<c:out value="${viewAuditActionData.afterAction}"/>"/>
<input hidden id="hsearchParam" value="<c:out value="${viewAuditActionData.viewParams}"/>"/>
<input hidden id="hvalidationFail" value="<c:out value="${viewAuditActionData.validationFail}"/>"/>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
<script>
    $(document).ready(function() {
        let hbeforeValue = $("#hbeforeValue").val()
        let hafterValue = $("#hafterValue").val()
        let hsearchParam = $("#hsearchParam").val()
        let hvalidationFail = $("#hvalidationFail").val()
        let detailJson = '';
        if(hvalidationFail != null && hvalidationFail != "[]" && hvalidationFail != ''){
            let failDetail = JSON.parse(hvalidationFail)
            let failDetailLen = failDetail.length;
            detailJson = "{"
            failDetail.forEach((item,index,array)=>{
                for(var key in item){
                if(item.hasOwnProperty(key)){
                       //"key": "value"
                       detailJson += "\"" + key + "\"" + " : " + "\"" + item[key] + "\""
                       if(failDetailLen != index + 1){
                            detailJson += ","
                       }
                    }
                }
            })
            detailJson += "}"
        }
        jsonToHtmlTable(detailJson, 'validationFail')
        jsonToHtmlTable(hbeforeValue, 'beforeValue')
        jsonToHtmlTable(hafterValue, 'afterValue')
        jsonToHtmlTable(hsearchParam, 'searchParam')

    })

    function doBack() {
        $("input[name='switch_action_type']").val("doBack")
        $("#mainForm").submit()
    }
</script>