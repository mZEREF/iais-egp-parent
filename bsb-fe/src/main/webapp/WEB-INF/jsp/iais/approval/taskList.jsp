<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
<div style="height:700px">
    <table style="width: 50%;margin:auto">
        <tr>
            <td>
                <iais:select name="taskList" disabled="false" codeCategory="CATE_ID_BSB_APPROVAL_TYPE"></iais:select>
            </td>
        </tr>
        <tr>
            <td>
                <button type="submit">Submit</button>
            </td>
        </tr>
    </table>
</div>
</form>