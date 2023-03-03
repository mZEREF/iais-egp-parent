<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts" %>
<%@include file="outSourceContent.jsp"%>
<br><br>
<iais:pagination  param="outSourceParam" result="outSourceResult"/>
<div class="main-content" style="width: 100%;!important;overflow-x: scroll;">
    <div class="col-lg-12 col-xs-12 col-md-12">
        <div class="intranet-content">
            <table aria-describedby="" class="table">
                <thead>
                <tr>
                    <iais:sortableHeader needSort="true" field="SVC_NAME" value="Service" style="width:15%;"/>
                    <iais:sortableHeader needSort="true" field="LICENCE_NO" value="Licence No." style="width:15%;" customSpacing="12"/>
                    <iais:sortableHeader needSort="true" field="BUSINESS_NAME" value="Business Name" style="width:15%;" customSpacing="12"/>
                    <iais:sortableHeader needSort="true" field="ADDRESS" value="Address" style="width:10%;" customSpacing="12"/>
                    <iais:sortableHeader needSort="true" field="EXPIRY_DATE" value="Licence Tenure" style="width:15%;" customSpacing="42"/>
                    <th style="width: 18%;">
                        <p style="margin-left: 16px;width: 100%;">
                            Date of Agreement <span class="mandatory">*</span>
                        </p>
                    </th>
                    <th style="width: 18%;">
                        <p style="margin-left: 16px;width: 100%;">
                            End Date of Agreement <span class="mandatory">*</span>
                        </p>
                    </th>
                    <th style="width: 10%;">
                        <p style="margin-left: 16px;width: 100%;">
                            Scope of Outsourcing <span class="mandatory">*</span>
                        </p>
                    </th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="outSourceResult" items="${outSourceResult.rows}" varStatus="status">
                    <c:set var="prefix" value="${outSourceResult.id}"/>
                    <c:set var="outsourcedIndexNo" value="${outSourceResult.licenceNo}"/>
                <tr>
                    <td>
                        <p class="visible-xs visible-sm table-row-title" style="width: 30px;!important;">Service</p>
                        <input name="${prefix}svcName" class="svcName" value="${outSourceResult.svcName}" type="hidden" data-prefix="${prefix}">
                        <p style="width: 200px;">${outSourceResult.svcName}</p>
                    </td>
                    <td>
                        <p class="visible-xs visible-sm table-row-title">Licence No.</p>
                        <input name="${prefix}licNo" value="${outSourceResult.licenceNo}" type="hidden">
                        <p>${outSourceResult.licenceNo}</p>
                    </td>
                    <td>
                        <p class="visible-xs visible-sm table-row-title">Business Name</p>
                        <input name="${prefix}bName" value="${outSourceResult.businessName}" type="hidden">
                        <p style="width: 220px;">${outSourceResult.businessName}</p>
                    </td>
                    <td>
                        <p class="visible-xs visible-sm table-row-title">Address</p>
                        <input name="${prefix}address" value="${outSourceResult.address}" type="hidden">
                        <p style="width: 260px">${outSourceResult.address}</p>
                    </td>
                    <td>
                        <p class="visible-xs visible-sm table-row-title">Licence Tenure</p>
                        <input name="${prefix}expiryDate" value="${outSourceResult.expiryDate}" type="hidden">
                        <p>${outSourceResult.expiryDate}</p>
                    </td>
                    <td>
                        <p class="visible-xs visible-sm table-row-title">Date of Agreement</p>
                        <c:if test="${currSvcInfoDto.appSvcOutsouredDto.prefixVal eq prefix}">
                            <iais:datePicker id="agreementStartDate" name="${prefix}agreementStartDate" value="${currSvcInfoDto.appSvcOutsouredDto.searchOutsourced.startDateStr}"/>
                        </c:if>
                        <c:if test="${currSvcInfoDto.appSvcOutsouredDto.prefixVal != prefix}">
                            <iais:datePicker id="agreementStartDate" name="${prefix}agreementStartDate" value=""/>
                        </c:if>
                    </td>
                    <td>
                        <p class="visible-xs visible-sm table-row-title">End Date of Agreement</p>
                        <c:if test="${currSvcInfoDto.appSvcOutsouredDto.prefixVal eq prefix}">
                            <iais:datePicker id="agreementEndDate" name="${prefix}agreementEndDate" value="${currSvcInfoDto.appSvcOutsouredDto.searchOutsourced.endDateStr}"/>
                        </c:if>
                        <c:if test="${currSvcInfoDto.appSvcOutsouredDto.prefixVal != prefix}">
                            <iais:datePicker id="agreementEndDate" name="${prefix}agreementEndDate" value=""/>
                        </c:if>
                    </td>
                    <td>
                        <p class="visible-xs visible-sm table-row-title">Scope of Outsourcing</p>
                        <c:if test="${currSvcInfoDto.appSvcOutsouredDto.prefixVal eq prefix}">
                            <textarea maxlength="3000" class="outsourcing" name="${prefix}outstandingScope" style="resize: none;">
                                    ${currSvcInfoDto.appSvcOutsouredDto.searchOutsourced.appPremOutSourceLicenceDto.outstandingScope}
                            </textarea>
                        </c:if>
                        <c:if test="${currSvcInfoDto.appSvcOutsouredDto.prefixVal != prefix}">
                             <textarea maxlength="3000" class="outsourcing" name="${prefix}outstandingScope" style="resize: none;"></textarea>
                        </c:if>
                        <br><span class="error-msg" name="iaisErrorMsg" id="error_${prefix}outstandingScope"></span>
                    </td>
                    <td>
                        <c:if test="${outSourceResult.svcName eq AppServicesConsts.SERVICE_NAME_CLINICAL_LABORATORY}">
                            <input type="hidden" name="prefixVal" value="${prefix}">
                            <c:if test="${!empty currSvcInfoDto.appSvcOutsouredDto.clinicalLaboratoryList}">
                                <button type="button" class="btn btn-default btn-sm btn-add
                                    <c:if test="${currSvcInfoDto.appSvcOutsouredDto.clinicalLaboratoryList.size() >= 5}">hidden</c:if> "
                                        data-prefix="${prefix}" data-group="${outsourcedIndexNo}">Add</button>
                            </c:if>
                            <c:if test="${empty currSvcInfoDto.appSvcOutsouredDto.clinicalLaboratoryList}">
                                <button type="button" class="btn btn-default btn-sm btn-add"
                                    data-prefix="${prefix}" data-group="${outsourcedIndexNo}">Add</button>
                            </c:if>
                        </c:if>
                        <c:if test="${outSourceResult.svcName eq AppServicesConsts.SERVICE_NAME_RADIOLOGICAL_SERVICES}">
                            <input type="hidden" name="prefixVal" value="${prefix}">
                            <c:if test="${!empty currSvcInfoDto.appSvcOutsouredDto.radiologicalServiceList}">
                                <button type="button" class="btn btn-default btn-sm btn-add
                                    <c:if test="${currSvcInfoDto.appSvcOutsouredDto.radiologicalServiceList.size() >= 5}">hidden</c:if> "
                                        data-prefix="${prefix}" data-group="${outsourcedIndexNo}">Add</button>
                            </c:if>
                            <c:if test="${empty currSvcInfoDto.appSvcOutsouredDto.radiologicalServiceList}">
                                <button type="button" class="btn btn-default btn-sm btn-add"
                                    data-prefix="${prefix}" data-group="${outsourcedIndexNo}">Add</button>
                            </c:if>
                        </c:if>
                    </td>
                </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div>
            <c:if test="${outSourceResult.rows.size() eq 0 || empty outSourceResult.rows}">
                <span id="noRecord" name="noRecord">No record found.</span>
            </c:if>
        </div>
    </div>
</div>
<script>
    $(document).ready(function () {
       addAllBtn();
    });

    function addAllBtn(){
        console.log("add....")
        let allBtn = document.getElementsByClassName("btn-add");
        for (let i = 0; i < allBtn.length; i++) {
            allBtn[i].onclick = function (){
                showWaiting();
                let $tag = $(this);
                let prefix = $tag.data('prefix');
                let outsourcedIndexNo = $tag.data('group');
                console.log("add-prefix:"+prefix);
                console.log("outsourcedIndexNo:"+outsourcedIndexNo);
                $('input[name="btnStep"]').val("add");
                $('input[name="pIds"]').val(prefix);
                $('input[name="prefixVal"]').val(prefix);
                $('input[name="outsourcedIndexNo"]').val(outsourcedIndexNo);
                let controlFormLi = $('#controlFormLi').val();
                submitForms('${serviceStepDto.currentStep.stepCode}',prefix,null,controlFormLi);
                let tr =this.parentNode.parentNode;
                tr.parentNode.removeChild(tr);
            };
        }
    }
</script>
