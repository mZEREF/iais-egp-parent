<%@include file="outSourceContent.jsp"%>
<br><br>
<iais:pagination  param="outSourceParam" result="outSourceResult"/>
<div class="main-content" style="width: 100%;!important;overflow-x: scroll;">
    <div class="col-lg-12 col-xs-12 col-md-12">
        <div class="intranet-content">
            <table aria-describedby="" class="table">
                <thead>
                <tr>
                    <iais:sortableHeader needSort="true" field="SVC_NAME" value="Serivce" style="width:15%;"/>
                    <iais:sortableHeader needSort="true" field="LICENCE_NO" value="Licence No." style="width:15%;" customSpacing="12"/>
                    <iais:sortableHeader needSort="true" field="BUSINESS_NAME" value="Business Name" style="width:15%;" customSpacing="12"/>
                    <iais:sortableHeader needSort="true" field="ADDRESS" value="Address" style="width:10%;" customSpacing="12"/>
                    <iais:sortableHeader needSort="true" field="EXPIRY_DATE" value="Licence Tenure" style="width:15%;" customSpacing="30"/>
                    <th style="width: 18%;">
                        <p style="margin-left: 16px;">
                            Date of Agreement<span class="mandatory">*</span>
                        </p>
                    </th>
                    <th style="width: 18%;">
                        <p style="margin-left: 16px;">
                            End Date of Agreement<span class="mandatory">*</span>
                        </p>
                    </th>
                    <th style="width: 10%;">
                        <p style="margin-left: 12px;">
                            Scope of Outsourcing
                        </p>
                    </th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="outSourceResult" items="${outSourceResult.rows}" varStatus="status">
                    <c:set var="prefix" value="${outSourceResult.id}"/>
                <tr>
                    <td>
                        <p class="visible-xs visible-sm table-row-title" style="width: 30px;!important;">Service</p>
                        <input name="${prefix}svcName" value="${outSourceResult.svcName}" type="hidden">
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
                        <iais:datePicker id="agreementStartDate" name="${prefix}agreementStartDate" value=""/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_${prefix}AgreementStartDate"></span>
                    </td>
                    <td>
                        <p class="visible-xs visible-sm table-row-title">End Date of Agreement</p>
                        <iais:datePicker id="agreementEndDate" name="${prefix}agreementEndDate" value=""/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_${prefix}AgreementEndDate"></span>
                    </td>
                    <td>
                        <p class="visible-xs visible-sm table-row-title">Scope of Outsourcing</p>
                        <iais:input maxLength="300" type="text" cssClass="scopeOfOutsourcing" name="${prefix}scopeOfOutsourcing" value=""/>
                    </td>
                    <td>
                        <input type="hidden" name="prefixVal" value="">
                        <button type="button" class="btn btn-default btn-sm btn-add" data-prefix="${prefix}">Add</button>
                    </td>
                </tr>
                </c:forEach>
                </tbody>
            </table>
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

                let $tag = $(this);
                let prefix = $tag.data('prefix');
                console.log("prefix:"+prefix);
                $('input[name="btnStep"]').val("add");
                $('input[name="pIds"]').val(prefix);
                $('input[name="prefixVal"]').val(prefix);
                let controlFormLi = $('#controlFormLi').val();
                submitForms('${serviceStepDto.currentStep.stepCode}',prefix,null,controlFormLi);
                let tr =this.parentNode.parentNode;
                tr.parentNode.removeChild(tr);
            };
        }
    }

</script>
