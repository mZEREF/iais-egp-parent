<div class="tab-search">
    <form class="" method="post" id="appForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="app_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_additional" value="">
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
                    <div class="col-xs-3 col-md-3" style="margin-left: -7px">
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
        <iais:pagination  param="appParam" result="appResult"/>
    </form>
</div>
<div class="row">
    <div class="col-xs-12">
        <div class="table-gp">
            <table class="table">
                <thead>
                <tr>
                    <iais:sortableHeader needSort="true"  field="APPLICATION_NO" value="Application No."></iais:sortableHeader>
                    <iais:sortableHeader needSort="true"  field="APP_TYPE" value="Type"></iais:sortableHeader>
                    <iais:sortableHeader needSort="true"  field="SERVICE_ID" value="Service"></iais:sortableHeader>
                    <iais:sortableHeader needSort="true"  field="STATUS" value="Status"></iais:sortableHeader>
                    <iais:sortableHeader needSort="true"  field="CREATED_DT" value="Submission Date"></iais:sortableHeader>
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
                                    <p style="width: 153px"><fmt:formatDate value="${app.createdAt}" pattern="MM/dd/yyyy HH:mm:ss"/></p>
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
            </table>
        </div>
    </div>
</div>