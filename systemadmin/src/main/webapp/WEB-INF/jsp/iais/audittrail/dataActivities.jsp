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
            <br><br><br>
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


        <a class="back" id="Back" onclick="doBack()"><em class="fa fa-angle-left"></em> Back</a>
    </>
</div>

<input hidden id="hbeforeValue" value="<c:out value="${viewAuditActionData.beforeAction}"/>"/>
<input hidden id="hafterValue" value="<c:out value="${viewAuditActionData.afterAction}"/>"/>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
<script>
    $(document).ready(function() {
        let hbeforeValue = $("#hbeforeValue").val()
        let hafterValue = $("#hafterValue").val()
        jsonToHtmlTable(hbeforeValue, 'beforeValue')
        jsonToHtmlTable(hafterValue, 'afterValue')
    })

    function doBack() {
        $("input[name='switch_action_type']").val("doBack");
        $("#mainForm").submit();
    }
</script>