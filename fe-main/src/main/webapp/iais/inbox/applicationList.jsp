<style>
    .table-info-display {
        margin: 20px 0px 5px 0px;
        background: #efefef;
        padding: 8px;
        border-radius: 8px;
        -moz-border-radius: 8px;
        -webkit-border-radius: 8px;

    }
</style>
<div class="tab-pane" id="tabApp" role="tabpanel">
    <div class="tab-search">
        <form class="form-inline" method="post" id="appForm" action=<%=process.runtime.continueURL()%>>
            <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
            <input type="hidden" name="crud_action_type" value="">
            <input type="hidden" name="crud_action_value" value="">
            <input type="hidden" name="form_pageTab" value="">
            <input type="hidden" name="appFrom_pageNo" value="">
            <input type="hidden" name="appFrom_pageSize" value="">
            <div class="row">
                <div class="col-md-6">
                    <iais:value>
                        <label class="col-xs-6 col-md-6" for="appNoPath">Search By Application No or Path of:</label>
                        <div class="col-xs-6 col-md-6">
                            <input id="appNoPath" name="appNoPath" type="text">
                        </div>
                    </iais:value>
                </div>
                <div class="col-md-6">
                    <iais:value>
                        <label class="col-xs-6 col-md-6" for="appServiceType">Service Type:</label>
                        <div class="col-xs-6 col-md-appServiceType">
                            <iais:select name="appServiceType" id="appServiceType" options="appServiceType"
                                         firstOption="All"></iais:select>
                        </div>
                    </iais:value>
                </div>
            </div>
            <div class="row" style="margin-bottom: 14px">
                <div class="col-md-6">
                    <iais:value>
                        <label class="col-xs-6 col-md-6" for="appTypeSelect">Application Type:</label>
                        <div class="col-xs-6 col-md-6">
                            <iais:select name="appTypeSelect" id="appTypeSelect" options="appTypeSelect"
                                         firstOption="All"></iais:select>
                        </div>
                    </iais:value>
                </div>
                <div class="col-md-6">
                    <iais:value>
                        <label class="col-xs-6 col-md-6" for="appStatusSelect">Application Status:</label>
                        <div class="col-xs-6 col-md-6">
                            <iais:select name="appStatusSelect" id="appStatusSelect" options="appStatusSelect"
                                         firstOption="All"></iais:select>
                        </div>
                    </iais:value>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <iais:value>
                        <label class="col-xs-3 col-md-3">Date Submitted:</label>
                        <div class="col-xs-3 col-md-3">
                            <iais:datePicker id="esd" name="esd"/>
                        </div>
                    </iais:value>
                    <div class="col-xs-1 col-md-1">
                        <span>TO</span>
                    </div>
                    <iais:value>
                        <div class="col-xs-3 col-md-3">
                            <iais:datePicker id="eed" name="eed"/>
                        </div>
                    </iais:value>
                    <div class="col-xs-2 col-md-2">
                        <button type="button" class="btn btn-primary" onclick="doSearchApp()">SUBMIT</button>
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
                        <th>Service</th>
                        <th>Status</th>
                        <th style="width:175px">Date Submitted <span class="sort"></span></th>
                        <th style="width:175px">Actions</th>
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
                                        <p><a href="#"
                                              onclick="doDraft('${app.applicationNo}')">${app.applicationNo}</a></p>
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
                                        <p><fmt:formatDate value="${app.createdAt}" pattern="MM/dd/yyyy HH:mm:ss"/></p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title" for="appAction">Actions</p>
                                        <c:choose>
                                            <c:when test="${app.applicationNo.indexOf('DN') != -1 || app.applicationNo .indexOf('DR') != -1}">
                                                <iais:select name="draftAction" id="draftAction"
                                                             options="selectDraftApplication" firstOption="Select"
                                                             onchange="doDraftAction('${app.applicationNo}',this.value)"></iais:select>
                                            </c:when>
                                            <c:otherwise>
                                                <iais:select name="appAction" id="appAction"
                                                             options="selectApplication"
                                                             firstOption="Select"></iais:select>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                    <tfoot>
                        <div class="row table-info-display">
                            <div class="col-md-4 text-left">
                                <p class="col-md-5 count table-count" style="margin-top:7px;">${appResult.rowCount} out
                                    of ${appParam.pageNo}</p>
                                <div class="col-md-1">
                                    <select class="table-select" id="appContentSelect">
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
                                                    <i class="fa fa-chevron-left" onclick="doSubPageNo('app')"></i>
                                                </span>
                                            </a>
                                        </li>
                                        <li><a href="#">${appPageNo}</a></li>
                                        <c:if test="${appPageCount > 1}">
                                            <li><a href="#">${appPageNo + 1}</a></li>
                                        </c:if>
                                        <c:if test="${appPageCount > 2}">
                                            <li><a href="#">${appPageNo + 2}</a></li>
                                        </c:if>
                                        <li>
                                            <a href="#" aria-label="Next">
                                                <span aria-hidden="true">
                                                    <i class="fa fa-chevron-right" onclick="doAddPageNo('app')"></i>
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