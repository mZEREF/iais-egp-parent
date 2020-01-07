<div class="tab-pane active" id="tabInbox" role="tabpanel">
    <div class="tab-search">
        <form class="form-inline" method="post" id="inboxForm" action=<%=process.runtime.continueURL()%>>
            <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
            <input type="hidden" name="crud_action_type" value="">
            <input type="hidden" name="form_pageTab" value="">
            <input type="hidden" name="inboxFrom_pageNo" value="">
            <input type="hidden" name="inboxFrom_pageSize" value="">
            <input type="hidden" name="crud_action_value" value="">
            <div class="form-group">
                <label class="control-label" for="inboxType">Type</label>
                <div class="col-xs-12 col-md-8 col-lg-9">
                    <iais:select name="inboxType" id="inboxType" options="inboxTypeSelect"
                                 firstOption="Select an type"></iais:select>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label" for="inboxService">Service</label>
                <div class="col-xs-12 col-md-8 col-lg-9">
                    <iais:select name="inboxService" id="inboxService" options="inboxServiceSelect"
                                 firstOption="Select an service"></iais:select>
                </div>
            </div>
            <div class="form-group large right-side">
                <div class="search-wrap">
                    <iais:value>
                        <div class="input-group">
                            <input class="form-control" id="inboxAdvancedSearch" type="text"
                                   placeholder="Message Subject." name="inboxAdvancedSearch"
                                   aria-label="inboxAdvancedSearch"><span class="input-group-btn">
                                <button class="btn btn-default buttonsearch" title="Search by keywords"
                                        onclick="searchBySubject()"><em class="fa fa-search"></em></button></span>
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
                            <c:forEach var="inboxQuery" items="${inboxResult.rows}" varStatus="status">
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
                                        <p><fmt:formatDate value="${inboxQuery.createdAt}"
                                                           pattern="MM/dd/yyyy HH:mm:ss"/></p>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                    <tfoot>
                    <div class="row table-info-display">
                        <div class="col-md-4 text-left">
                            <p class="col-md-5 count table-count" style="margin-top:7px;">${inboxResult.rowCount} out
                                of ${inboxParam.pageNo}</p>
                            <div class="col-md-1">
                                <select class="table-select" id="inboxContentSelect">
                                    <option value="5">5</option>
                                    <option value="10">10</option>
                                    <option value="20">20</option>
                                    <option value="30">30</option>
                                    <option value="50">50</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-8 text-right">
                            <div class="nav">
                                <ul class="pagination" style="margin-top:7px;">
                                    <li>
                                        <a href="#" aria-label="Previous">
                                                    <span aria-hidden="true">
                                                        <i class="fa fa-chevron-left" onclick="doSubPageNo('inbox')"></i>
                                                    </span>
                                        </a>
                                    </li>
                                    <li><a href="#">${inboxPageNo}</a></li>
                                    <c:if test="${inboxPageCount > 1}">
                                        <li><a href="#">${inboxPageNo + 1}</a></li>
                                    </c:if>
                                    <c:if test="${inboxPageCount > 2}">
                                        <li><a href="#">${inboxPageNo + 2}</a></li>
                                    </c:if>
                                    <li>
                                        <a href="#" aria-label="Next">
                                                    <span aria-hidden="true">
                                                        <i class="fa fa-chevron-right" onclick="doAddPageNo('inbox')"></i>
                                                    </span>
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    </tfoot>
                </table>
            </div>
        </div>
    </div>
</div>