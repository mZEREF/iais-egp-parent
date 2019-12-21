<div class="tab-pane active" id="tabInbox" role="tabpanel">
    <div class="tab-search">
        <form class="form-inline" method="post" id="inboxForm" action=<%=process.runtime.continueURL()%>>
            <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
            <input type="hidden" name="crud_action_type" value="">
            <input type="hidden" name="crud_action_value" value="">
            <div class="form-group">
                <label class="control-label" for="inboxType">Type</label>
                <div class="col-xs-12 col-md-8 col-lg-9">
                    <iais:select name="inboxType" id="inboxType" options="inboxTypeSelect" firstOption="Select an type"></iais:select>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label" for="inboxService">Service</label>
                <div class="col-xs-12 col-md-8 col-lg-9">
                    <iais:select name="inboxService" id="inboxService" options="inboxServiceSelect" firstOption="Select an service"></iais:select>
                </div>
            </div>
            <div class="form-group large right-side">
                <div class="search-wrap">
                    <iais:value>
                        <div class="input-group">
                            <input class="form-control" id="inboxAdvancedSearch" type="text" placeholder="Message Subject." name="inboxAdvancedSearch" aria-label="inboxAdvancedSearch"><span class="input-group-btn">
                                <button class="btn btn-default buttonsearch" title="Search by keywords" onclick="searchBySubject()"><em class="fa fa-search"></em></button></span>
                        </div>
                    </iais:value>
                </div>
            </div>
        </form>
    </div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table class="table">
                    <thead>
                    <tr>
                        <th>Subject</th>
                        <th>Message Type</th>
                        <th>Ref. No.</th>
                        <th>Service</th>
                        <th>Date <span class="sort"></span></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty inboxResult.rows}">
                            <tr>
                                <td colspan="6">
                                    No Record!!
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var = "inboxQuery" items = "${inboxResult.rows}" varStatus="status">
                                <tr>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Subject</p>
                                        <p><a href="${inboxQuery.processUrl}">${inboxQuery.subject}</a></p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Message Type</p>
                                        <p>${inboxQuery.messageType}</p>
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
                                        <p><fmt:formatDate value="${inboxQuery.createdAt}" pattern="MM/dd/yyyy HH:mm:ss" /></p>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
                <div class="table-footnote">
                    <div class="row">
                        <div class="col-xs-6 col-md-4">
                            <p class="count">${inboxResult.rowCount} out of ${inboxParam.pageNo}</p>
                        </div>
                        <div class="col-xs-6 col-md-8 text-right">
                            <div class="nav">
                                <ul class="msgPagination">
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>