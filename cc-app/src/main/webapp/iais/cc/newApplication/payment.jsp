<%@ page import="java.util.Date" %>
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
                            <div class="tab-pane active" id="paymentTab" role="tabpanel">
                                <h2>Payment Summary</h2>
                                <p >
                                    Total amount due:<div name="totalAmount">$8888.88</div>
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
                                <input class="form-check-input premTypeRadio"  type="radio" name="premisesType" >
                                <label class="form-check-label" ><span class="check-circle"></span>Credit/Debit Card</label>&nbsp&nbsp&nbsp&nbsp
                                <input class="form-check-input premTypeRadio"  type="radio" name="premisesType" >
                                <label class="form-check-label" ><span class="check-circle"></span>GIRO</label><br>

                                &nbsp&nbsp&nbsp&nbsp<img src="<%=webroot1%>img/mastercard.jpg" width="40" height="25">&nbsp
                                <img src="<%=webroot1%>img/paymentVISA.jpg" width="66" height="25">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                                <img src="<%=webroot1%>img/payments.jpg" width="36" height="30">
                                <p class="visible-xs visible-sm table-row-title">Proceed</p>
                                <p class="text-right text-center-mobile"><iais:input type="button" cssClass="btn btn-primary" value="Proceed"></iais:input></p>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<script>
    $(function () {
        // alert("ok")
        $(":button").click(function () {
            //alert("ok")
            var paymentRequestDto={
                amount: $("div[name=totalAmount]"),
                payMethod: $("input[name=payMethod]"),
                reqDt: new Date(),
                reqRefNo: "string12345"
            }
            //alert($("form[name=myform]").serialize())
            $.ajax({
                type:"POST",
                url:"${pageContext.request.contextPath}/payment/duringThePayment",
                //data:"username=tom&age=22",
                data:paymentRequestDto,
                //data:$("form[name=myform]").serialize(),
                success:function (response) {
                    //alert(response)
                }
            })
        })
    })
</script>

