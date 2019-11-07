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
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th>Application Group No.</th>
                                        <th>Application No.</th>
                                        <th>Service</th>
                                        <th>Tote Fee</th>
                                    </tr>
                                    </thead>
                                    <tbody>
<%--                                    <c:forEach items="${testDtoList}" var="list">--%>

<%--                                    </c:forEach>--%>
                                    <tr>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Application Group No.</p>
                                            <p>LS-2017-00003</p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Application No.</p>
                                            <p>AS-20070-00002</p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Service</p>
                                            <p>Clinical Laboratory</p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Tote Fee</p>
                                            <p>8888.88</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Application Group No.</p>
                                            <p></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Application No.</p>
                                            <p></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Service</p>
                                            <p></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Tote Fee</p>
                                            <p>lump sum fee:8888.88</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Credit Card</p>
                                            <p class="text-right text-center-mobile"><a class="btn btn-primary" href="#">Credit Card</a></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Paybal</p>
                                            <p class="text-right text-center-mobile"><a class="btn btn-primary" href="#">Paybal</a></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Paylah</p>
                                            <p class="text-right text-center-mobile"><a class="btn btn-primary" href="#">Paylah</a></p>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
