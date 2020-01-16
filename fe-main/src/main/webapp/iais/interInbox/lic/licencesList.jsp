<form class="" method="post" id="licForm" action=<%=process.runtime.continueURL()%>>
<div class="tab-search">
        <input type="hidden" name="lic_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_additional" value="">
        <div class="row">
            <div class="col-md-12">
                <iais:value>
                    <label class="col-xs-5 col-md-5" for="licNoPath">Search By Licence No or Path of:</label>
                    <div class="col-xs-5 col-md-56">
                        <input id="licNoPath" name="licNoPath" type="text">
                    </div>
                </iais:value>
            </div>
        </div>
        <div class="row" style="margin-bottom: 14px">
            <div class="col-md-12">
                <iais:value>
                    <label class="col-xs-5 col-md-5" for="licType">Service Type:</label>
                    <div class="col-xs-5 col-md-5">
                        <iais:select name="licType" id="licType" options="licType" firstOption="All"></iais:select>
                    </div>
                </iais:value>
            </div>
        </div>
        <div class="row" style="margin-bottom: 14px">
            <div class="col-md-12">
                <iais:value>
                    <label class="col-xs-5 col-md-5" for="licStatus">Licence Status:</label>
                    <div class="col-xs-5 col-md-5">
                        <iais:select name="licStatus" id="licStatus" options="licStatus"
                                     firstOption="All"></iais:select>
                    </div>
                </iais:value>
            </div>
        </div>
        <div class="row" style="margin-bottom: 14px">
            <div class="col-md-12">
                <iais:value>
                    <label class="col-md-3" for="fStartDate">Licence Start Date:</label>
                    <div class="col-md-3">
                        <iais:datePicker id="fStartDate" name="fStartDate"/>
                    </div>
                    <div class="col-xs-1 col-md-1">
                        <span>TO</span>
                    </div>
                    <div class="col-md-3">
                        <iais:datePicker id="eStartDate" name="eStartDate"/>
                    </div>
                </iais:value>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <iais:value>
                    <label class="col-xs-3 col-md-3">Licence Expiry Date:</label>
                    <div class="col-xs-3 col-md-3">
                        <iais:datePicker id="fExpiryDate" name="fExpiryDate"/>
                    </div>
                </iais:value>
                <div class="col-xs-1 col-md-1">
                    <span>TO</span>
                </div>
                <iais:value>
                    <div class="col-xs-3 col-md-3">
                        <iais:datePicker id="eExpiryDate" name="eExpiryDate"/>
                    </div>
                </iais:value>
                <div class="col-xs-2 col-md-2">
                    <button type="button" class="btn btn-primary" onclick="doSearchLic()">SUBMIT</button>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="col-xs-2 col-md-2">
                    <button type="button" class="btn btn-primary" >Renew</button>
                </div>
                <div class="col-xs-2 col-md-2">
                    <button type="button" class="btn btn btn-secondary" id="lic-amend" >Amend</button>
                </div>
                <div class="col-xs-2 col-md-2">
                    <button type="button" class="btn btn btn-secondary" >Cease</button>
                </div>
            </div>
        </div>
        <iais:pagination  param="licParam" result="licResult"/>
</div>
<div class="row">
    <div class="col-xs-12">
        <div class="table-gp">
            <table class="table">
                <thead>
                <tr>
                    <iais:sortableHeader needSort="true"  field="LICENCE_NO" value="Licence No."></iais:sortableHeader>
                    <iais:sortableHeader needSort="true"  field="SVC_NAME" value="Type"></iais:sortableHeader>
                    <iais:sortableHeader needSort="true"  field="STATUS" value="Status"></iais:sortableHeader>
                    <iais:sortableHeader needSort="true"  field="PREMISE" value="Premises"></iais:sortableHeader>
                    <iais:sortableHeader needSort="true"  field="START_DATE" value="Start Date"></iais:sortableHeader>
                    <iais:sortableHeader needSort="true"  field="EXPIRY_DATE" value="Expiry Date"></iais:sortableHeader>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${empty licResult.rows}">
                        <tr>
                            <td colspan="6">
                                <p class="table-row-title" style="text-align:center">No Record !</p>
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
                                                class="check-square"></span><a
                                                href="#">${licenceQuery.licenceNo}</a></label>
                                        <input type="hidden" name="licenId${status.index}" value="<iais:mask name= "licenId${status.index}" value="${licenceQuery.id}" />"/>
                                    </div>
                                </td>
                                <td>
                                    <p class="visible-xs visible-sm table-row-title">Type</p>
                                    <p>${licenceQuery.svcName}</p>
                                </td>
                                <td>
                                    <p class="visible-xs visible-sm table-row-title">Status</p>
                                    <p>${licenceQuery.status}</p>
                                </td>
                                <td>
                                    <p class="visible-xs visible-sm table-row-title">Premises</p>
                                    <p>${licenceQuery.premise}</p>
                                </td>
                                <td>
                                    <p class="visible-xs visible-sm table-row-title">Start Date</p>
                                    <p><fmt:formatDate value="${licenceQuery.startDate}"
                                                       pattern="MM/dd/yyyy HH:mm:ss"/></p>
                                </td>
                                <td>
                                    <p class="visible-xs visible-sm table-row-title">Expiry Date</p>
                                    <p><fmt:formatDate value="${licenceQuery.expiryDate}"
                                                       pattern="MM/dd/yyyy HH:mm:ss"/></p>
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
    
</script>