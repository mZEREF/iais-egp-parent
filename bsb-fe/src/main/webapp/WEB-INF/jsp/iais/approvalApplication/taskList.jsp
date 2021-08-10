<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
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
                <select name="taskList" value="">
                    <option>Please Select</option>
                    <option>Approval To Possess</option>
                    <option>Approval To LargeScaleProduce</option>
                    <option>Approval To Special</option>
                </select>
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