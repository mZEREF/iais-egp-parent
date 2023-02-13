<div class="col-md-12">
    <div class="col-lg-12 col-xs-12">
        <div class="center-content">
            <div class="intranet-content">
                <div class="row form-horizontal normal-label">
                    <div class="col-xs-12 col-md-12">
                        <iais:row style="margin-bottom: 0">
                            <iais:field width="4" value="Licence Search" style="font-weight:bold"/>
                        </iais:row>
                        <hr style="margin-top: 0px">
                        <iais:row>
                            <label class="col-xs-3 col-md-3 control-label">Licence No.</label>
                            <iais:value width="5" cssClass="col-md-5">
                                <input type="text" maxlength="24" id="licenceNo" name="licenceNo"
                                       value="${licenceEnquiryFilterDto.licenceNo}">
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <label class="col-xs-3 col-md-3 control-label">Service Name</label>
                            <iais:value width="5" cssClass="col-md-5">
                                <iais:select cssClass="clearSel" name="serviceName"
                                             multiValues="${licenceEnquiryFilterDto.serviceName}"
                                             options="licSvcTypeOption" needErrorSpan="false" multiSelect="true"/>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <label class="col-xs-3 col-md-3 control-label">MOSD Type</label>
                            <iais:value width="5" cssClass="col-md-5">
                                <iais:select name="mosdType" id="mosdType" firstOption="All"
                                             options="mosdTypeOption"
                                             cssClass="clearSel" value="${licenceEnquiryFilterDto.mosdType}"/>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <label class="col-xs-3 col-md-3 control-label">Business Name</label>
                            <iais:value width="5" cssClass="col-md-5">
                                <input type="text" maxlength="100" id="businessName" name="businessName"
                                       value="${licenceEnquiryFilterDto.businessName}">
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <label class="col-xs-3 col-md-3 control-label">Licence Status</label>
                            <iais:value width="5" cssClass="col-md-5">
                                <iais:select name="licenceStatus" codeCategory="CATE_ID_LICENCE_STATUS"
                                             firstOption="All"
                                             cssClass="clearSel"  value="${licenceEnquiryFilterDto.licenceStatus}"/>
                            </iais:value>
                        </iais:row>

                        <iais:row>
                            <div class="col-xs-3 col-md-5 control-label">
                                <span class="error-msg " name="iaisErrorMsg" id="error_checkAllFileds"></span>
                            </div>
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
                <h3>
                    <span>Search Results</span>
                </h3>
                <div class="components">
                    <iais:pagination param="licTabParam" result="licTabResult"/>
                    <div class="table-gp table-responsive">
                        <table aria-describedby="" class="table "
                               style="border-collapse:collapse;">
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
                    <input type="hidden" name="Search" value="0">
                </div>

            </div>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script>
    $(function (){
        var serviceName = "${licenceEnquiryFilterDto.serviceName}";
        if(serviceName ==null || serviceName=="[]"|| serviceName==""){
            $(".multi-select-button").html("All");
            $('.multi-select-menuitem input:checkbox').prop('checked',false)
        }
        $('.multi-select-menuitem input:checkbox').on('change',checkOption)
    })
    function checkOption() {
        let flag = true;
        $('.multi-select-menuitem input:checkbox').each(function (k,v){
            let isSelected = $(this).prop('checked');
            if (isSelected){
                flag = false;
                return;
            }
        })
        if (flag){
            $(".multi-select-button").html("All");
        }
    }

    function doLicClear() {
        $('input[type="text"]').val("");
        $('input[type="checkbox"]').prop("checked", false);
        $("select option").prop("selected", false);
        $(".clearSel").children(".current").text("All");
        $(".multi-select-button").html("All");
        $('.multi-select-menuitem input:checkbox').prop('checked',false)

    }


    function jumpToPagechangePage() {
        search();
    }

    function searchLic() {
        $('input[name="pageJumpNoTextchangePage"]').val(1);
        $('input[name="Search"]').val(1);
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