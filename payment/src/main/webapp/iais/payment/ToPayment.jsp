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

<form method="post" style="margin: 10%;" id="bank" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <h2 style="border-bottom: none">This is a dummy bank payment page</h2>
    <iais:section title="">
        <iais:row>
            <iais:field value="Card Holder Name"/>
            <iais:value width="3">
                <input type="text" id = "cardHolderName">
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="Card Number"/>
            <iais:value width="3">
                <input type="text" id = "cardNumber">
            </iais:value>
        </iais:row>
        <div class="form-group">
            <label class="col-xs-0 col-md-4 control-label">
                Expiry Date
            </label>
            <div class="col-sm-4 col-md-2 col-xs-3">
                <div>
                    <input type="text" id="startDate" style="width: 42%;margin-right: 2%"/>&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;<input type="text" id="endDate" style="width: 42%;margin-right: 2%" />
                </div>
            </div>
        </div>

        <iais:row>
            <iais:field value="CVV No."/>
            <iais:value width="3">
                <input type="text" id="cvvNo">
            </iais:value>
        </iais:row>
    </iais:section>
    <iais:action style="margin-left: 0%">
        <a class="btn btn-primary" href="#" onclick="submit()">Proceed to pay</a>
    </iais:action>
    <br/><br/>
</form>
<script type="text/javascript">
    function submit() {
        $("#bank").submit();
    }

</script>
