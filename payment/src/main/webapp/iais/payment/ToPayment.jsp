<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setAttribute name="title">
    <%=process.runtime.getCurrentComponentName()%>
</webui:setAttribute>
<webui:setLayout name="iais-internet"/>

<form method="post" style="margin: 10%;" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <h2 style="border-bottom: none">This is a dummy bank payment page</h2>
    <iais:section title="">
    <iais:row>
        <iais:field value="Card Holder Name"/>
        <iais:value width="3">
            <input type="text">
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field value="Card Number"/>
        <iais:value width="3">
            <input type="text">
        </iais:value>
    </iais:row>
        <div class="form-group">
            <label class="col-xs-12 col-md-4 control-label">
                Expiry Date
            </label>
            <div class="col-xs-9 col-sm-5 col-md-6">
                <input type="text" style="width: 60px;margin-right: 2%"/> / <input type="text" style="width: 60px;margin-right: 2%" />
            </div>
            </div>
        </div>
    <iais:row>
        <iais:field value="CVV No."/>
        <iais:value width="1">
            <input type="text">
        </iais:value>
    </iais:row>
    </iais:section>
    <iais:action style="margin-left: 10%">
        <div style="margin-left: 10%">
            <button type="submit">Proceed to pay</button>
        </div>
    </iais:action>
    <br/><br/>
</form>
