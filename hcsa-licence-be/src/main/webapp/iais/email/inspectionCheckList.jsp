<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp dashboard-tab">

                        <%@ include file="./navTabs.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane" id="tabCheckList" role="tabpanel">
                                <div class="alert alert-info" role="alert">
                                    <strong>
                                        <h4>Processing Status Update</h4>
                                    </strong>
                                </div>
                                <form method="post" action=<%=process.runtime.continueURL()%>>
                                    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table">
                                                    <tr>
                                                        <td class="col-xs-4"><p>Current Status:</p></td>
                                                        <td class="col-xs-8"><p>${applicationViewDto.currentStatus}</p></td>
                                                    </tr>
                                                    <tr>
                                                        <td><p>Internal Remarks:</p></td>
                                                        <td>
                                                            <div class="input-group">
                                                                <div class="ax_default text_area">
                                                                    <textarea name="internalRemarks" cols="70" rows="7"></textarea>
                                                                </div>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <p>Processing Decision:</p>
                                                        </td>
                                                        <td>
                                                            <select name="nextStage" >
                                                                <c:forEach items="${applicationViewDto.routingStage}" var="routingStageMap">
                                                                    <option  value="${routingStageMap.key}">${routingStageMap.value}</option>
                                                                </c:forEach>
                                                            </select>
                                                        </td>
                                                    </tr>
                                                </table>
                                                <div align="center">
                                                    <button type="submit" class="btn btn-primary">Submit</button>
                                                </div>
                                                <div>&nbsp;</div>
                                            </div>
                                        </div>
                                    </div>
                                </form>
                                <div class="alert alert-info" role="alert">
                                    <strong>
                                        <h4>Processing History</h4>
                                    </strong>
                                </div>
                                <div class="row">
                                    <div class="col-xs-12">
                                        <div class="table-gp">
                                            <table class="table">
                                                <thead>
                                                <tr>
                                                    <th>Username</th>
                                                    <th>Working Group</th>
                                                    <th>Status Update</th>
                                                    <th>Remarks</th>
                                                    <th>Last Updated</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <td>
                                                        <p>Tan Ah Ming (S1234567D)</p>
                                                    </td>
                                                    <td>
                                                        <p>Internet User</p>
                                                    </td>
                                                    <td>
                                                        <p>Submission</p>
                                                    </td>
                                                    <td>
                                                        <p>-</p>
                                                    </td>
                                                    <td>
                                                        <p>16-Oct-2018 01:20:13 PM</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p>Mr Tan</p>
                                                    </td>
                                                    <td>
                                                        <p>Internet User</p>
                                                    </td>
                                                    <td>
                                                        <p>Pending Admin Screen</p>
                                                    </td>
                                                    <td>
                                                        <p>-</p>
                                                    </td>
                                                    <td>
                                                        <p>16-Oct-2018 01:20:13 PM</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p></p>
                                                    </td>
                                                    <td>
                                                        <p></p>
                                                    </td>
                                                    <td>
                                                        <p>Verified</p>
                                                    </td>
                                                    <td>
                                                        <p>-</p>
                                                    </td>
                                                    <td>
                                                        <p>16-Oct-2018 01:20:13 PM</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p>Ms Lim</p>
                                                    </td>
                                                    <td>
                                                        <p>Internet User</p>
                                                    </td>
                                                    <td>
                                                        <p>Pending Professional Screening</p>
                                                    </td>
                                                    <td>
                                                        <p>-</p>
                                                    </td>
                                                    <td>
                                                        <p>16-Oct-2018 01:20:13 PM</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p></p>
                                                    </td>
                                                    <td>
                                                        <p></p>
                                                    </td>
                                                    <td>
                                                        <p>Verified</p>
                                                    </td>
                                                    <td>
                                                        <p>-</p>
                                                    </td>
                                                    <td>
                                                        <p>16-Oct-2018 01:20:13 PM</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p>Mrs Sim</p>
                                                    </td>
                                                    <td>
                                                        <p>Internet User</p>
                                                    </td>
                                                    <td>
                                                        <p>Pending Inspection</p>
                                                    </td>
                                                    <td>
                                                        <p>-</p>
                                                    </td>
                                                    <td>
                                                        <p>16-Oct-2018 01:20:13 PM</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p></p>
                                                    </td>
                                                    <td>
                                                        <p></p>
                                                    </td>
                                                    <td>
                                                        <p>Inspection Conducted</p>
                                                    </td>
                                                    <td>
                                                        <p>Recommend for Approval</p>
                                                    </td>
                                                    <td>
                                                        <p>16-Oct-2018 01:20:13 PM</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p>Mr Ong</p>
                                                    </td>
                                                    <td>
                                                        <p>Internet User</p>
                                                    </td>
                                                    <td>
                                                        <p>Pending Approval Officer 1</p>
                                                    </td>
                                                    <td>
                                                        <p>-</p>
                                                    </td>
                                                    <td>
                                                        <p>16-Oct-2018 01:20:13 PM</p>
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
        </div>
    </div>
</form>
<script type="text/javascript">
    // $(document).ready(function() {
    //     $('#tabCheckList').ready(function () {
    //         this.aria.expanded().val(true);
    //     })
    // });
</script>