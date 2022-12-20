<div class="col-md-12">
    <div class="col-lg-12 col-xs-12">
        <div class="center-content">
            <div class="intranet-content">
                <div class="row form-horizontal">
                    <div class="col-xs-12 col-md-12">
                        <iais:row>
                            <iais:field width="4" value="Licence Search"/>
                        </iais:row>
                        <hr>
                        <iais:row>
                            <iais:field width="4" value="Licence No."/>
                            <iais:value width="4" cssClass="col-md-4">
                                <input type="text" maxlength="24" id="licenceNo" name="licenceNo"
                                       value="${licenceEnquiryFilterDto.licenceNo}">
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field width="4" value="Service Name"/>
                            <iais:value width="4" cssClass="col-md-4">
                                <iais:select name="serviceName" options="licSvcTypeOption"
                                             firstOption="Please Select"
                                             value="${licenceEnquiryFilterDto.serviceName}"></iais:select>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field width="4" value="MOSD Type"/>
                            <iais:value width="4" cssClass="col-md-4">
                                <iais:select name="mosdType" id="mosdType" firstOption="Please Select"
                                             options="mosdTypeOption"
                                             cssClass="clearSel" value="${licenceEnquiryFilterDto.mosdType}"/>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field width="4" value="Business Name"/>
                            <iais:value width="4" cssClass="col-md-4">
                                <input type="text" maxlength="100" id="businessName" name="businessName"
                                       value="${licenceEnquiryFilterDto.businessName}">
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field width="4" value="Licence Status"/>
                            <iais:value width="4" cssClass="col-md-4">
                                <iais:select name="licenceStatus" codeCategory="CATE_ID_LICENCE_STATUS"
                                             firstOption="Please Select"
                                             value="${licenceEnquiryFilterDto.licenceStatus}"></iais:select>
                            </iais:value>
                        </iais:row>
                        
                        
                        <div class="col-xs-12 col-md-12">
                            <iais:action style="text-align:right;">
                                <button type="button" class="btn btn-secondary"
                                        onclick="javascript:doLicClear();">Clear
                                </button>
                                <button type="button" class="btn btn-primary"
                                        onclick="javascript:searchLic();">Search
                                </button>
                            </iais:action>
                        </div>
                    </div>
                </div>
                <br>
                <div class="components">

                    <iais:pagination param="licTabParam" result="licTabResult"/>
                    <div class="table-gp">
                        <table aria-describedby="" class="table table-responsive">
                            <thead>
                            <tr>
                                <iais:sortableHeader needSort="false"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field=""
                                                     value="S/N"/>
                                <iais:sortableHeader needSort="true"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="LICENCE_NO"
                                                     value="Licence No."/>
                                <iais:sortableHeader needSort="true"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="BUSINESS_NAME"
                                                     value="Business Name"/>
                                <iais:sortableHeader needSort="true"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="SVC_NAME"
                                                     value="Service Name"/>
                                <iais:sortableHeader needSort="true"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="PREMISES_TYPE"
                                                     value="MOSD Type"/>
                                <iais:sortableHeader needSort="true"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="ADDRESS"
                                                     value="MOSD Address"/>
                                <iais:sortableHeader needSort="true"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="START_DATE"
                                                     value="Licence Period"/>
                                <iais:sortableHeader needSort="true"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="LicSTATUS"
                                                     value="Licence Status"/>

                            </tr>
                            </thead>
                            <tbody class="form-horizontal">
                            <c:choose>
                                <c:when test="${empty licTabResult or empty licTabResult.rows}">
                                    <tr>
                                        <td colspan="11">
                                            <iais:message key="GENERAL_ACK018"
                                                          escape="true"/>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="licence"
                                               items="${licTabResult.rows}"
                                               varStatus="status">
                                        <tr>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">S/N</p>
                                                <c:out value="${status.index + 1+ (licTabParam.pageNo - 1) * licTabParam.pageSize}"/>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">Licence No.</p>
                                                <a href="#"
                                                   onclick="fullDetailsView('${MaskUtil.maskValue('licenceId', licence.licenceId)}')">${licence.licenceNo}</a>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">Business
                                                    Name</p>
                                                <c:out value="${licence.businessName}"/>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">Service
                                                    Name</p>
                                                <c:out value="${licence.serviceName}"/>
                                            </td>

                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">MOSD Type</p>
                                                <c:out value="${licence.mosdType}"/>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">MOSD
                                                    Address</p>
                                                <c:out value="${licence.mosdAddress}"/>
                                            </td>

                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">Licence
                                                    Period</p>
                                                <fmt:formatDate
                                                        value="${licence.startDate}"
                                                        pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/>-<fmt:formatDate
                                                    value="${licence.expiryDate}"
                                                    pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">Licence
                                                    Status</p>
                                                <iais:code code="${licence.licenceStatus}"/>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
                    </div>
                    <iais:action style="text-align:right;">
                        <a class="btn btn-secondary"
                           href="${pageContext.request.contextPath}/hcsa/enquiry/hcsa/Licensee-LicTab-SearchResults-Download">Download</a>
                    </iais:action>
                </div>

            </div>
        </div>
    </div>

</div>

<script>
    function doLicClear() {
        $('input[type="text"]').val("");
        $('input[type="checkbox"]').prop("checked", false);
        $("select option").prop("selected", false);
        $(".clearSel").children(".current").text("Please Select");

    }


    function jumpToPagechangePage() {
        search();
    }

    function searchLic() {
        $('input[name="pageJumpNoTextchangePage"]').val(1);
        search();
    }

    function search() {
        showWaiting();
        $("[name='crud_action_type']").val('searchLic');
        $('#mainForm').submit();
    }

    function sortRecords(sortFieldName, sortType) {
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        $("[name='crud_action_type']").val('searchLic');
        $('#mainForm').submit();
    }
    var fullDetailsView = function (submissionNo) {

        showWaiting();
        $("[name='crud_action_value']").val(submissionNo);
        $("[name='crud_action_type']").val('licInfo');
        $('#mainForm').submit();
    }
</script>