<div class="tab-pane" id="tabApplication" role="tabpanel">
    <div class="tab-search">
        <form class="form-inline" method="post" id="inboxForm" action=<%=process.runtime.continueURL()%>>
            <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
            <input type="hidden" name="crud_action_type" value="">
            <div class="form-group">
                <label class="control-label" for="applicationType">Type</label>
                <div class="col-xs-12 col-md-8 col-lg-9">
                    <iais:select name="applicationType" id="applicationType"
                                 options="applicationType" firstOption="Select an type"></iais:select>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label" for="applicationStatus">Status</label>
                <div class="col-xs-12 col-md-8 col-lg-9">
                    <iais:select name="applicationStatus" id="applicationStatus"
                                 options="applicationStatus" firstOption="Select an status"></iais:select>
                </div>
            </div>
            <div class="form-group large right-side">
                <div class="search-wrap">
                    <div class="input-group">
                        <input class="form-control" id="applicationAdvancedSearch" type="text" placeholder="Application no." name="applicationAdvancedSearch" aria-label="applicationAdvancedSearch"><span class="input-group-btn">
                              <button class="btn btn-default buttonsearch" title="Search by keywords"><em class="fa fa-search"></em></button></span>
                    </div>
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
                        <th>Status</th>
                        <th>Service</th>
                        <th>Date Submitted <span class="sort"></span></th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${appResult}" var="list">
                        <tr>
                            <td>
                                <p class="visible-xs visible-sm table-row-title">Application No.</p>
                                <p><a href="#">${list.applicationNo}</a></p>
                            </td>
                            <td>
                                <p class="visible-xs visible-sm table-row-title">Type</p>
                                <p>${list.applicationType}</p>
                            </td>
                            <td>
                                <p class="visible-xs visible-sm table-row-title">Service</p>
                                <p>${list.serviceId}</p>
                            </td>
                            <td>
                                <p class="visible-xs visible-sm table-row-title">Status</p>
                                <p>${list.status}</p>
                            </td>
                            <td>
                                <p class="visible-xs visible-sm table-row-title">Date Submitted</p>
                                <p><fmt:formatDate value="${list.createdAt}" pattern="MM/dd/yyyy HH:mm:ss" /></p>
                            </td>
                            <td>
                                <p class="visible-xs visible-sm table-row-title" for="selectApplication1">Actions</p>
                                <iais:select name="selectApplication" id="selectApplication"
                                             options="selectApplication" firstOption="Select"></iais:select>
                            </td>
                        </tr>
                    </c:forEach>

                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>