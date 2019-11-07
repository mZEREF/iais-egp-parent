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
                                <h2>YOUR LICENCES TO RENEW ARE LISTED BELOW</h2>
                                <div class="table-gp">
                                    <table class="table">
                                        <thead>
                                        <tr>
                                            <th>Licence No.</th>
                                            <th>Type</th>
                                            <th class="premises-info">Premises</th>
                                            <th>Start Date</th>
                                            <th>Expires On</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Licence No.</p>
                                                <p>LS-2017-00003</p>
                                            </td>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Type</p>
                                                <p>Clinical Laboratory</p>
                                            </td>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Premises</p>
                                                <ul>
                                                    <li>
                                                        <p><b>On-site:</b> 16 Raffles Quay # 01-03 <br class="hidden-xs hidden-sm">Hong Leong Building, 048581​​​​​​​</p>
                                                    </li>
                                                    <li>
                                                        <p><b>Conveyance:</b> 111 North Bridge Rd.<br class="hidden-xs hidden-sm"># 07-04, 179098</p>
                                                    </li>
                                                </ul>
                                            </td>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Start Date</p>
                                                <p>12 Dec 2017</p>
                                            </td>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Expires On</p>
                                                <p>14 Apr 2019</p>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Licence No.</p>
                                                <p>LS-2016-00001</p>
                                            </td>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Type</p>
                                                <p>Blood Banking</p>
                                            </td>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Premises</p>
                                                <ul>
                                                    <li>
                                                        <p><b>On-site:</b> 16 Raffles Quay # 01-03 <br class="hidden-xs hidden-sm">Hong Leong Building, 048581​​​​​​​</p>
                                                    </li>
                                                </ul>
                                            </td>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Start Date</p>
                                                <p>1 Nov 2016</p>
                                            </td>
                                            <td>
                                                <p class="visible-xs visible-sm table-row-title">Expires On</p>
                                                <p>1 Apr 2019</p>
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
    </div>
</form>
