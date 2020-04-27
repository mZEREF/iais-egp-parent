<div class="main-content">
    <form method="post" id="TemplatesForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
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
                        <iais:pagination  param="MsgTemplateSearchParam" result="MsgTemplateSearchResult"/>
                            <thead>
                            <tr>
                                <iais:sortableHeader needSort="false" field="subject" value="S/N"></iais:sortableHeader>
                                <iais:sortableHeader needSort="true" field="message_type" value="Message Type"></iais:sortableHeader>
                                <iais:sortableHeader needSort="true" field="template_name" value="Template Name"></iais:sortableHeader>
                                <iais:sortableHeader needSort="true" field="delivery_mode" value="Delivery Mode"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false" field="process" value="Process"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false" field="delivery_mode" value="To Recipient"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false" field="delivery_mode" value="CC Recipient"></iais:sortableHeader>
                                <iais:sortableHeader needSort="true" field="effective_from" value="Effective Start Date"></iais:sortableHeader>
                                <iais:sortableHeader needSort="true" field="effective_to" value="Effective End Date"></iais:sortableHeader>
                                <th>Action</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                            <c:when test="${empty MsgTemplateSearchResult.rows}">
                                <tr>
                                    <td colspan="12">
                                        <iais:message key="ACK018" escape="true"></iais:message>
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                            <c:forEach var="msgTemplateResult" items="${MsgTemplateSearchResult.rows}"
                                       varStatus="status">
                                <tr>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">S/N</p>
                                        <p>${(MsgTemplateSearchParam.pageNo - 1) * MsgTemplateSearchParam.pageSize + status.index + 1}</p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Message Type</p>
                                        <p>${msgTemplateResult.messageType}</p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Template Name</p>
                                        <p>${msgTemplateResult.templateName}</p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Delivery Mode</p>
                                        <p>${msgTemplateResult.deliveryMode}</p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Process</p>
                                        <p>${msgTemplateResult.process}</p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">To Recipient</p>
                                        <p>${msgTemplateResult.recipient}</p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">CC Recipient</p>
                                        <p>${msgTemplateResult.recipient}</p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Effective Start Date</p>
                                        <p><fmt:formatDate value="${msgTemplateResult.effectiveFrom}"
                                                           pattern="dd/MM/yyyy"/></p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Effective End Date</p>
                                        <p><fmt:formatDate value="${msgTemplateResult.effectiveTo}"
                                                           pattern="dd/MM/yyyy"/></p>
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