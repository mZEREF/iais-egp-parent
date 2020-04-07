<form class="" method="post" id="licForm" action=<%=process.runtime.continueURL()%>>
    <div class="tab-search">
        <input type="hidden" name="lic_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_additional" value="">
        <input type="hidden" name="action_id_value" value="">
        <div id="clearBody">
            <div class="row">
                <div class="col-md-12">
                    <iais:value>
                        <label class="col-xs-4 col-md-4" for="licNoPath" style="text-align:left;margin-top: 1.5%">Search
                            by Licence No or Part of:</label>
                        <div class="col-xs-8 col-md-8">
                            <input id="licNoPath" name="licNoPath" type="text" maxlength="20"
                                   value="<%=request.getParameter("licNoPath")==null?"":request.getParameter("licNoPath")%>">
                        </div>
                    </iais:value>
                </div>
            </div>
            <div class="row" style="margin-bottom: 1.5%">
                <div class="col-md-12">
                    <iais:value>
                        <label class="col-xs-4 col-md-4" for="licType" style="text-align:left;margin-top: 1.5%">Service
                            Type:</label>
                        <div class="col-xs-8 col-md-8">
                            <%String licType = request.getParameter("licType");%>
                            <iais:select name="licType" id="licType" options="licType" value="<%=licType%>"></iais:select>
                        </div>
                    </iais:value>
                </div>
            </div>
            <div class="row" style="margin-bottom: 1.5%">
                <div class="col-md-12">
                    <iais:value>
                        <label class="col-xs-4 col-md-4" for="licStatus" style="text-align:left;margin-top: 1.5%">Licence
                            Status:</label>
                        <div class="col-xs-8 col-md-8">
                            <%String licStatus = request.getParameter("licStatus");%>
                            <iais:select name="licStatus" id="licStatus" options="licStatus" value="<%=licStatus%>"></iais:select>
                        </div>
                    </iais:value>
                </div>
            </div>
            <div class="row" style="margin-bottom: 1.5%">
                <div class="col-md-12">
                    <iais:value>
                        <label class="col-md-3" for="fStartDate" style="text-align:left;margin-top: 1.5%">Licence Start
                            Date:</label>
                        <div class="col-md-4">
                            <%Date fStartDate = Formatter.parseDate(request.getParameter("fStartDate"));%>
                            <iais:datePicker id="fStartDate" name="fStartDate" dateVal="<%=fStartDate%>"/>
                        </div>
                        <div class="col-xs-1 col-md-1" style="margin-top: 1.5%">
                            <span>To</span>
                        </div>
                        <div class="col-md-4">
                            <%Date eStartDate = Formatter.parseDate(request.getParameter("eStartDate"));%>
                            <iais:datePicker id="eStartDate" name="eStartDate" dateVal="<%=eStartDate%>"/>
                        </div>
                    </iais:value>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <iais:value>
                        <label class="col-xs-3 col-md-3" style="text-align:left;margin-top: 1.5%">Licence Expiry
                            Date:</label>
                        <div class="col-xs-4 col-md-4">
                            <%Date fExpiryDate = Formatter.parseDate(request.getParameter("fExpiryDate"));%>
                            <iais:datePicker id="fExpiryDate" name="fExpiryDate" dateVal="<%=fExpiryDate%>"/>
                        </div>
                    </iais:value>
                    <div class="col-xs-1 col-md-1" style="margin-top: 1.5%">
                        <span>To</span>
                    </div>
                    <iais:value>
                        <div class="col-xs-4 col-md-4">
                            <%Date eExpiryDate = Formatter.parseDate(request.getParameter("eExpiryDate"));%>
                            <iais:datePicker id="eExpiryDate" name="eExpiryDate" dateVal="<%=eExpiryDate%>"/>
                        </div>
                    </iais:value>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="licence-btns col-md-6" style="width: 49%">
                    <a class="btn btn-primary disabled" href="javascript:;" id="lic-renew">Renew</a>
                    <a class="btn btn-primary disabled" href="javascript:;" id="lic-cease">Cease</a>
                    <a class="btn btn-primary disabled" href="javascript:;" id="lic-amend">Amend</a>
                </div>
                <div class="col-md-6 text-right" style="width: 49%">
                    <button type="button" class="btn btn-primary" onclick="doPrint()">Print</button>
                    <button type="button" class="btn btn-secondary" onclick="doClearLic()">Clear</button>
                    <button type="button" class="btn btn-primary" onclick="doSearchLic()">Search</button>
                </div>
            </div>
        </div>
        <iais:pagination param="licParam" result="licResult"/>
    </div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table class="table">
                    <thead>
                    <tr>
                        <iais:sortableHeader needSort="false" field="" value=" " ></iais:sortableHeader>
                        <iais:sortableHeader needSort="true" field="LICENCE_NO"
                                             value="Licence No."></iais:sortableHeader>
                        <iais:sortableHeader needSort="true" field="SVC_NAME" value="Type"></iais:sortableHeader>
                        <iais:sortableHeader needSort="true" field="STATUS" value="Status" style="width:9%;"></iais:sortableHeader>
                        <iais:sortableHeader needSort="true" field="PREMISE" value="Premises"></iais:sortableHeader>
                        <iais:sortableHeader needSort="true" field="START_DATE"
                                             value="Start Date"></iais:sortableHeader>
                        <iais:sortableHeader needSort="true" field="EXPIRY_DATE"
                                             value="Expiry Date"></iais:sortableHeader>
                        <th style="width:15%">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty licResult.rows}">
                            <tr>
                                <td colspan="6">
                                    <iais:message key="ACK018" escape="true"></iais:message>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="licenceQuery" items="${licResult.rows}" varStatus="status">
                                <tr>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Licence No.</p>
                                        <div class="form-check">
                                            <input class="form-check-input licenceCheck" id="licence1" type="checkbox"
                                                   name="licenceNo" value="licenId${status.index}" aria-invalid="false">
                                            <label class="form-check-label" for="licence1"><span
                                                    class="check-square"></span>
                                            </label>
                                        </div>
                                    </td>
                                    <td>
                                        <a href="#" onclick="toLicView('<iais:mask name="action_id_value"
                                                                                   value="${licenceQuery.id}"/>')">${licenceQuery.licenceNo}</a>
                                        <input type="hidden" name="licenId${status.index}"
                                               value="<iais:mask name= "licenId${status.index}" value="${licenceQuery.id}"/>"/>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Type</p>
                                        <p>${licenceQuery.svcName}</p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Status</p>
                                        <p style="margin-right: 26px;">${licenceQuery.status}</p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Premises</p>
                                        <p>${licenceQuery.premise}</p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Start Date</p>
                                        <p><fmt:formatDate value="${licenceQuery.startDate}"
                                                           pattern="dd/MM/yyyy"/></p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Expiry Date</p>
                                        <p><fmt:formatDate value="${licenceQuery.expiryDate}"
                                                           pattern="dd/MM/yyyy"/></p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Actions</p>
                                        <iais:select name="licActions" id="licActions" options="licActions"
                                                     firstOption="Select"
                                                     onchange="doLicAppeal('${licenceQuery.licenceNo}')"></iais:select>
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
</form>
<script>
    $('#lic-amend').click(function () {
        doLicAmend();
    });

    $('#lic-renew').click(function () {
        doLicRenew();
    });
    $('#lic-cease').click(function () {
        doLicCease();
    });

</script>