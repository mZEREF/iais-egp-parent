<div class="main-content">
    <form class="form-horizontal" method="post" id="TemplatesForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="bg-title">
                            <h2>Alert Notification Template View</h2>
                        </div>
                        <%@ include file="doSearchBody.jsp" %>
                        <table class="table">
                            <thead>
                            <tr>
                                <th>No.</th>
                                <th>Message Type</th>
                                <th>Template Name</th>
                                <th>Delivery Mode</th>
                                <th>Effective Start Date</th>
                                <th>Effective End Date</th>
                                <th>Action</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                            <c:when test="${empty MsgTemplateSearchResult.rows}">
                                <tr>
                                    <td colspan="12">
                                        No Record!!
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                            <c:forEach var="msgTemplateResult" items="${MsgTemplateSearchResult.rows}"
                                       varStatus="status">
                                <tr>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">No.</p>
                                        <p>#${(MsgTemplateSearchParam.pageNo - 1) * 10 + status.index + 1}</p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Message Type.</p>
                                        <p>${msgTemplateResult.messageType}</p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Template Name.</p>
                                        <p>${msgTemplateResult.templateName}</p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Delivery Mode.</p>
                                        <p>${msgTemplateResult.deliveryMode}</p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Effective Start Date.</p>
                                        <p><fmt:formatDate value="${msgTemplateResult.effectiveFrom}"
                                                           pattern="MM/dd/yyyy HH:mm:ss"/></p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Effective End Date</p>
                                        <p><fmt:formatDate value="${msgTemplateResult.effectiveTo}"
                                                           pattern="MM/dd/yyyy HH:mm:ss"/></p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Action</p>
                                        <button type="button" class="btn btn-default btn-sm" onclick="doPreview('${msgTemplateResult.id}')">Preview</button>
                                        <button type="button" class="btn btn-default btn-sm" onclick="doEdit('${msgTemplateResult.id}')">Edit</button>
                                    </td>
                                </tr>
                            </c:forEach>
                            </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>