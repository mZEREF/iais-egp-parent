<div class="tab-pane" id="tabApp" role="tabpanel">
    <div class="tab-search">
        <form class="form-inline" method="post" id="appForm" action=<%=process.runtime.continueURL()%>>
            <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
            <input type="hidden" name="crud_action_type" value="">
            <input type="hidden" name="app_action_type" value="">
            <input type="hidden" name="crud_action_value" value="">
            <div class="form-group">
                <label class="control-label" for="appType">Type</label>
                <div class="col-xs-12 col-md-8 col-lg-9">
                    <iais:select name="appType" id="appType" options="appTypeSelect" firstOption="Select an type"></iais:select>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label" for="appStatus">Status</label>
                <div class="col-xs-12 col-md-8 col-lg-9">
                    <iais:select name="appStatus" id="appStatus" options="appStatusSelect" firstOption="Select an status"></iais:select>
                </div>
            </div>
            <div class="form-group large right-side">
                <div class="search-wrap">
                    <iais:value>
                        <div class="input-group">
                            <input class="form-control" id="applicationAdvancedSearch" type="text" placeholder="Application no." name="applicationAdvancedSearch" aria-label="applicationAdvancedSearch"><span class="input-group-btn">
                                  <button class="btn btn-default buttonsearch" title="Search by keywords" onclick="searchByAppNo()"><em class="fa fa-search"></em></button></span>
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
                        <th>Application No.</th>
                        <th>Type</th>
                        <th>Service</th>
                        <th>Status</th>
                        <th>Date Submitted <span class="sort"></span></th>
                        <th  style="width:175px">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty appResult.rows}">
                            <tr>
                                <td colspan="6">
                                    No Record!!
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${appResult.rows}" var="app" varStatus="status">
                                <tr>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Application No.</p>
                                        <p><a href="#" onclick="doDraft('${app.applicationNo}')">${app.applicationNo}</a></p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Type</p>
                                        <p>${app.applicationType}</p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Service</p>
                                        <p>${app.serviceId}</p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Status</p>
                                        <p>${app.status}</p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Date Submitted</p>
                                        <p><fmt:formatDate value="${app.createdAt}" pattern="MM/dd/yyyy HH:mm:ss" /></p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title" for="appAction">Actions</p>
                                        <iais:select name="appAction" id="appAction"
                                                     options="selectApplication" firstOption="Select" ></iais:select>
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
                            <p class="count">${appResult.rowCount} out of ${appParam.pageNo}</p>
                        </div>
                        <div class="col-xs-6 col-md-8 text-right">
                            <div class="nav">
                                <ul class="appPagination">
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>