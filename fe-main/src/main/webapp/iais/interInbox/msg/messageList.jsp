<div class="tab-search">
    <form class="" method="post" id="msgForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="msg_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_additional" value="">
        <div class="row">
            <div class="col-md-4">
                <label class="col-md-3 control-label" for="inboxType" style="margin-top:5%;">Type</label>
                <div class="col-md-8">
                    <iais:select name="inboxType" id="inboxType" options="inboxTypeSelect"
                                 firstOption="Select a type"></iais:select>
                </div>
            </div>
            <div class="col-md-5">
                <label class="col-md-3 control-label" for="inboxService" style="margin-top:3%;">Service</label>
                <div class="col-md-8">
                    <iais:select name="inboxService" id="inboxService" options="inboxServiceSelect"
                                 firstOption="Select a service"></iais:select>
                </div>
            </div>
            <div class="col-md-3">
                <div class="search-wrap">
                    <iais:value>
                        <div class="input-group">
                            <input class="form-control" id="inboxAdvancedSearch" type="text"
                                   placeholder="Search Your Keywords" name="inboxAdvancedSearch"
                                   aria-label="inboxAdvancedSearch" maxlength="50"><span class="input-group-btn">
                                <button class="btn btn-default buttonsearch" title="Search by keywords"
                                        onclick="searchBySubject()"><em class="fa fa-search"></em></button></span>
                        </div>
                    </iais:value>
                </div>
            </div>
        </div>
        <iais:pagination param="inboxParam" result="inboxResult"/>
    </form>
</div>
<div class="row">
    <div class="col-xs-12">
        <div class="table-gp">
            <table class="table">
                <thead>
                <tr>
                    <iais:sortableHeader needSort="true" field="subject" value="Subject"></iais:sortableHeader>
                    <iais:sortableHeader needSort="true" field="message_type" value="Message Type"></iais:sortableHeader>
                    <iais:sortableHeader needSort="true" field="ref_no" value="Ref. No."></iais:sortableHeader>
                    <iais:sortableHeader needSort="true" field="service_id" value="Service"></iais:sortableHeader>
                    <iais:sortableHeader needSort="true" field="CREATED_DT" value="Date"></iais:sortableHeader>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${empty inboxResult.rows}">
                        <tr>
                            <td colspan="6">
                                <p class="table-row-title" style="text-align:center">No Record !</p>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="inboxQuery" items="${inboxResult.rows}" varStatus="status">
                                <c:choose>
                                    <c:when test="${inboxQuery.status == 'MSGRS001' || inboxQuery.status == 'MSGRS002'}">
                                        <tr style="font-weight:bold">
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                    </c:otherwise>
                                </c:choose>
                                <td>
                                    <p class="visible-xs visible-sm table-row-title">Subject</p>
                                    <p><a href="${inboxQuery.processUrl}">${inboxQuery.subject}</a></p>
                                </td>
                                <td>
                                    <p class="visible-xs visible-sm table-row-title">Message Type</p>
                                    <p><iais:code code="${inboxQuery.messageType}"></iais:code></p>
                                </td>
                            <td>
                                    <p class="visible-xs visible-sm table-row-title">Ref. No</p>
                                    <p>${inboxQuery.refNo}</p>
                                </td>
                                <td>
                                    <p class="visible-xs visible-sm table-row-title">Service</p>
                                    <p>${inboxQuery.serviceId}</p>
                                </td>
                                <td>
                                    <p class="visible-xs visible-sm table-row-title">Date</p>
                                    <p><fmt:formatDate value="${inboxQuery.createdAt}"
                                                       pattern="dd/MM/yyyy HH:mm:ss"/></p>
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