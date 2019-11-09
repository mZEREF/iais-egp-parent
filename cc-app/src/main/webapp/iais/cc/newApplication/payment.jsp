<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>

<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");

%>
<webui:setLayout name="iais-internet"/>
<%@ include file="./dashboard.jsp" %>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="./navTabs.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane" id="paymentTab" role="tabpanel">
                                <h2>Payment Summary</h2>
                                <p>
                                    Total amount due:$XXXXX
                                </p>
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th>Service</th>
                                        <th>Application Type</th>
                                        <th>Application No.</th>
                                        <th>Amount</th>
                                    </tr>
                                    </thead>
                                    <tbody>
<%--                                    <c:forEach items="${testDtoList}" var="list">--%>

<%--                                    </c:forEach>--%>
                                    <tr>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Service</p>
                                            <p>Clinical Laboratory</p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Application Type</p>
                                            <p>LS-2017-00003</p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Application No.</p>
                                            <p>DL_2019_00000009_CR</p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title" >Amount</p>
                                            <p name="fee" >8888.88</p>
                                        </td>
                                    </tr>


                                    </tbody>
                                </table>
                                <h2>Payment Method</h2>
                                <iais:input type="radio">Credit/Debit Card</iais:input>
                                <iais:input type="radio">GIRO</iais:input>
                                <p class="visible-xs visible-sm table-row-title">Proceed</p>
                                <p class="text-right text-center-mobile"><a class="btn btn-primary" href="#">Proceed</a></p>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<script type="text/javascript">
    $(function () {
        var fee= $("p[name=fee]")
        var sumFee=$("p[name=sumFee]")
        for(var i=0;i<fee.length;i++){
            sumFee.innerText=sumFee.innerText+fee[i].innerText
        }
    })
</script>
