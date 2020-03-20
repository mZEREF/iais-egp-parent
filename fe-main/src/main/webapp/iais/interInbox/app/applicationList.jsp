<div class="tab-search">
    <form class="" method="post" id="appForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="app_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_additional" value="">
        <input type="hidden" name="action_no_value" value="">
        <input type="hidden" name="action_grp_value" value="">
        <input type="hidden" name="action_id_value" value="">
        <input type="hidden" name="action_type_value" value="">
        <div id="clearBody">
            <div class="row">
                <div class="col-md-6">
                    <iais:value>
                        <label class="col-xs-7 col-md-7" for="appNoPath" style="margin-top:1%;">Search by Application No
                            or Part of:</label>
                        <div class="col-xs-5 col-md-5">
                            <input id="appNoPath" name="appNoPath" type="text" maxlength="15"
                                   value="<%=request.getParameter("appNoPath")==null?"":request.getParameter("appNoPath")%>">
                        </div>
                    </iais:value>
                </div>
                <div class="col-md-6">
                    <iais:value>
                        <label class="col-xs-4 col-md-4" for="appServiceType" style="margin-top:3%;">Service
                            Type:</label>
                        <div class="col-xs-8 col-md-8">
                            <%String appServiceType = request.getParameter("appServiceType");%>
                            <iais:select name="appServiceType" id="appServiceType" cssClass="appServiceType"
                                         options="appServiceType" value="<%=appServiceType%>"></iais:select>
                        </div>
                    </iais:value>
                </div>
            </div>
            <div class="row" style="margin-bottom: 14px">
                <div class="col-md-6">
                    <iais:value>
                        <label class="col-xs-7 col-md-7" for="appTypeSelect" style="margin-top:3%;">Application
                            Type:</label>
                        <div class="col-xs-5 col-md-5">
                            <%String appTypeSelect = request.getParameter("appTypeSelect");%>
                            <iais:select name="appTypeSelect" id="appTypeSelect" cssClass="appTypeSelect"
                                         options="appTypeSelect" value="<%=appTypeSelect%>"></iais:select>
                        </div>
                    </iais:value>
                </div>
                <div class="col-md-6">
                    <iais:value>
                        <label class="col-xs-4 col-md-4" for="appStatusSelect" style="margin-top:3%;">Application
                            Status:</label>
                        <div class="col-xs-8 col-md-8">
                            <%String appStatusSelect = request.getParameter("appStatusSelect");%>
                            <iais:select options="appStatusSelect" cssClass="appStatusSelect" name="appStatusSelect"
                                         id="appStatusSelect" value="<%=appStatusSelect%>"></iais:select>
                        </div>
                    </iais:value>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <iais:value>
                        <label class="col-xs-7 col-md-7" for="esd" style="margin-top:3%;">Date Submitted:</label>
                        <div class="col-xs-5 col-md-5">
                            <%Date esd = Formatter.parseDate(request.getParameter("esd"));%>
                            <iais:datePicker id="esd" name="esd" dateVal="<%=esd%>"/>
                        </div>
                    </iais:value>
                </div>
                <div class="col-md-6">
                    <iais:value>
                        <label class="col-xs-4 col-md-4" for="appStatusSelect" style="margin-top:3%;">To</label>
                        <div class="col-xs-8 col-md-8">
                            <%Date eed = Formatter.parseDate(request.getParameter("eed"));%>
                            <iais:datePicker id="eed" name="eed" dateVal="<%=eed%>"/>
                        </div>
                    </iais:value>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="text-right text-center-mobile" style="margin-right:3%">
                <button type="button" class="btn btn-secondary" onclick="doAppClear()">Clear</button>
                <button type="button" class="btn btn-primary" onclick="doSearchApp()">Search</button>
            </div>
        </div>
        <iais:pagination param="appParam" result="appResult"/>
    </form>
</div>
<div class="row">
    <div class="col-xs-12">
        <div class="table-gp">
            <table class="table">
                <thead>
                <tr>
                    <iais:sortableHeader needSort="true" field="APPLICATION_NO"
                                         value="Application No."></iais:sortableHeader>
                    <iais:sortableHeader needSort="true" field="APP_TYPE" value="Type"></iais:sortableHeader>
                    <iais:sortableHeader needSort="true" field="SERVICE_ID" value="Service"></iais:sortableHeader>
                    <iais:sortableHeader needSort="true" field="STATUS" value="Status"></iais:sortableHeader>
                    <iais:sortableHeader needSort="true" field="CREATED_DT"
                                         value="Submission Date"></iais:sortableHeader>
                    <th style="width:15%">Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${empty appResult.rows}">
                        <tr>
                            <td colspan="6">
                                <p class="table-row-title" style="text-align:center">No Record !</p>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${appResult.rows}" var="app" varStatus="status">
                            <tr>
                                <td>
                                    <p class="visible-xs visible-sm table-row-title">Application No.</p>
                                    <p><a href="#"
                                          onclick="doDraft('<iais:mask name="action_no_value"
                                                                       value="${app.applicationNo}"/>','<iais:mask
                                                  name="action_type_value"
                                                  value="${app.applicationType}"/>','${app.status}')">${app.applicationNo}</a>
                                    </p>
                                </td>
                                <td>
                                    <p class="visible-xs visible-sm table-row-title">Type</p>
                                    <p><iais:code code="${app.applicationType}"></iais:code></p>
                                </td>
                                <td>
                                    <p class="visible-xs visible-sm table-row-title">Service</p>
                                    <p>${app.serviceId}</p>
                                </td>
                                <td>
                                    <p class="visible-xs visible-sm table-row-title">Status</p>
                                    <p><iais:code code="${app.status}"></iais:code></p>
                                </td>
                                <td>
                                    <p class="visible-xs visible-sm table-row-title">Date Submitted</p>
                                    <p style="width: 153px"><fmt:formatDate value="${app.createdAt}"
                                                                            pattern="dd/MM/yyyy HH:mm:ss"/></p>
                                </td>
                                <td>
                                    <p class="visible-xs visible-sm table-row-title" for="appAction">Actions</p>
                                    <c:choose>
                                        <c:when test="${app.status == 'APST008'}">
                                            <iais:select name="draftAction" id="draftAction"
                                                         options="selectDraftApplication" firstOption="Select"
                                                         onchange="doDraftAction('${app.applicationNo}',this.value)"></iais:select>
                                        </c:when>
                                        <c:otherwise>
                                            <iais:select name="appAction" id="appAction"
                                                         options="selectApplication"
                                                         firstOption="Select"
                                                         onchange="doAppAction('${app.id}','${app.applicationNo}',this.value)"></iais:select>
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
