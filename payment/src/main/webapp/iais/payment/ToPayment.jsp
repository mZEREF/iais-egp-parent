<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setAttribute name="title">
    <%=process.runtime.getCurrentComponentName()%>
</webui:setAttribute>

<form method="post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <h1>This is a dummy bank payment page</h1>
    <iais:section title="bank">
    <iais:row>
        <iais:field value="Card Holder Name"/>
        <iais:value width="7">
            <input type="text">
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field value="Card Number"/>
        <iais:value width="7">
            <input type="text">
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field value="Expiry Date"/>
        <iais:value width="7">
            <input type="text"> / <input type="text">
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field value="CVV No."/>
        <iais:value width="7">
            <input type="text">
        </iais:value>
    </iais:row>
    </iais:section>
    <button type="submit">Proceed to pay</button>
</form>
