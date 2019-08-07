<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>


<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>


<webui:setAttribute name="header-ext">
    <%
        /* You can add additional content (SCRIPT, STYLE elements)
         * which need to be placed inside HEAD element here.
         */
    %>
</webui:setAttribute>

<webui:setAttribute name="title">
    <%
        /* You can set your page title here. */
    %>

    <%=process.runtime.getCurrentComponentName()%>

</webui:setAttribute>
<!-- START: CSS -->

<!-- END: CSS -->

<form id = "messageForm" method = "post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">

    <iais:body>
    <h2>Message List Page</h2>

    <iais:section title="Message List" id="msgList">
        <iais:row>
            <iais:field value="Domain Type" required="true"></iais:field>
            <iais:value width="7">
                <iais:select name="domainType" options="domainTypeSelect" firstOption="Please select" otherOptionValue="${msgRequestDto.domainType}"></iais:select>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field value="Msg Type" required="true"></iais:field>
            <iais:value width="7">
                <iais:select name="msgType" options="msgTypeSelect" firstOption="Please select" otherOptionValue="${msgRequestDto.msgType}"></iais:select>
            </iais:value>
        </iais:row>

        <iais:row>
        <iais:field value="Module" required="false"></iais:field>
        <iais:value width="7">
            <iais:select name="module" options="moduleTypeSelect" firstOption="Please select" otherOptionValue="${msgRequestDto.module}"></iais:select>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field value="description" required="false"></iais:field>
        <iais:value width="7">
            <input type="text" name="description" value="${msgRequestDto.description}" />
        </iais:value>
    </iais:row>

        <iais:action>
            <button type="button" class="search btn" onclick="javascript:doEdit();">Update</button>
        </iais:action>
        <iais:action>
            <button type="button" class="search btn" onclick="javascript:doCancel();">Cancel</button>
        </iais:action>
    </iais:section>

    </br>

        </iais:body>
</form>

<script type="text/javascript">
    function doUpdate(){
        SOP.Crud.cfxSubmit("messageForm", "doEdit");
    }

    function doCancel(){
        SOP.Crud.cfxSubmit("mainForm","cancel");
    }

</script>
