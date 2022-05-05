<div class="main-content" style="min-height: 73vh;">
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
                        <h3>
                            <span>Search Results</span>
                        </h3>
                        <table aria-describedby="" class="table">
                        <iais:pagination  param="MsgTemplateSearchParam" result="MsgTemplateSearchResult"/>
                            <thead>
                            <tr><th scope="col" style="display: none"></th>
                                <iais:sortableHeader needSort="false" field="subject" value="S/N" style="width:5%;"/>
                                <iais:sortableHeader needSort="true" field="message_type" value="Message Type" style="width:10%;" customSpacing="12"/>
                                <iais:sortableHeader needSort="true" field="template_name" value="Template Name" style="width:15%;" customSpacing="12"/>
                                <iais:sortableHeader needSort="true" field="delivery_mode_desc" value="Delivery Mode" style="width:10%;" customSpacing="12"/>
                                <iais:sortableHeader needSort="true" field="process_desc" value="Process" style="width:10%;" customSpacing="12"/>
                                <iais:sortableHeader needSort="true" field="RECSort" value="To Recipients" style="width:10%;" customSpacing="12"/>
                                <iais:sortableHeader needSort="true" field="CCSort" value="CC Recipients" style="width:10%;" customSpacing="12"/>
                                <iais:sortableHeader needSort="true" field="BCCSort" value="BCC Recipients" style="width:10%;" customSpacing="12"/>
                                <iais:sortableHeader needSort="true" field="effective_from" value="Effective Start Date" style="width:10%;" customSpacing="12"/>
                                <iais:sortableHeader needSort="true" field="effective_to" value="Effective End Date" style="width:10%;" customSpacing="12"/>
                                <iais:sortableHeader needSort="false" field="" value="Action"/>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                            <c:when test="${empty MsgTemplateSearchResult.rows}">
                                <tr>
                                    <td colspan="12">
                                        <iais:message key="GENERAL_ACK018" escape="true"/>
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
                                        <p class="visible-xs visible-sm table-row-title">To Recipients</p>
                                        <p>${msgTemplateResult.rec}</p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">CC Recipients</p>
                                        <p>${msgTemplateResult.cc}</p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">BCC Recipients</p>
                                        <p>${msgTemplateResult.bcc}</p>
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