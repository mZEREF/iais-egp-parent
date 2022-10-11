<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts" %>
<c:set var="cL" value="${currSvcInfoDto.appPremOutSourceLicenceDto}"/>
<iais:row>
    <div class="col-xs-12 <c:if test="${AppSubmissionDto.appLicBundleDtoList[0].svcCode eq AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES}">hidden</c:if>" style="margin-top: 20px;!important;">
        <p>Radiological Service</p>
    </div>
</iais:row>

<div class="col-lg-12 col-xs-12 col-md-12 <c:if test="${AppSubmissionDto.appLicBundleDtoList[0].svcCode eq AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES}">hidden</c:if>">
    <div class="intranet-content">
        <table aria-describedby="" class="table">
            <thead>
            <tr>
                <iais:sortableHeader needSort="true" field="LICENCE_NO" value="Licence No." style="width:15%;" customSpacing="12"/>
                <iais:sortableHeader needSort="true" field="BUSINESS_NAME" value="Business Name" style="width:15%;" customSpacing="12"/>
                <iais:sortableHeader needSort="true" field="ADDRESS" value="Address" style="width:10%;" customSpacing="12"/>
                <iais:sortableHeader needSort="true" field="EXPIRY_DATE" value="Licence Tenure" style="width:15%;" customSpacing="30"/>
                <iais:sortableHeader needSort="true" field="AGREEMENT_START_DATE" value="Date of Agreement" style="width:15%;" customSpacing="12"/>
                <iais:sortableHeader needSort="true" field="AGREEMENT_END_DATE" value="End Date of Agreement" style="width:15%;" customSpacing="12"/>
                <iais:sortableHeader needSort="true" field="OUTSTANDING_SCOPE" value="Scope of Outsourcing" style="width:15%;" customSpacing="12"/>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <c:if test="${empty cL.radiologicalServiceList}">
                <tr>
                    <td>
                        <p class="visible-xs visible-sm table-row-title">Licence No.</p>
                    </td>
                    <td>
                        <p class="visible-xs visible-sm table-row-title">Business Name</p>
                    </td>
                    <td>
                        <p class="visible-xs visible-sm table-row-title">Address</p>
                    </td>
                    <td>
                        <p class="visible-xs visible-sm table-row-title">Licence Tenure</p>
                    </td>
                    <td>
                        <p class="visible-xs visible-sm table-row-title">Date of Agreement</p>
                    </td>
                    <td>
                        <p class="visible-xs visible-sm table-row-title">End Date of Agreement</p>
                    </td>
                    <td>
                        <p class="visible-xs visible-sm table-row-title">Scope of Outsourcing</p>
                    </td>
                    <td>
                    </td>
                </tr>
            </c:if>

            <c:if test="${!empty cL.radiologicalServiceList}">
                <c:set var="rlen" value="${cL.radiologicalServiceList.size()}"/>
                <input name="rlenght" value="${rlen}" type="hidden">
                <%@include file="radiologicalServiceTableDentail.jsp"%>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<script>
    $(document).ready(function () {
        delRSBtn();
    });
    function delRSBtn(){
        console.log("del....")
        let allBtn = document.getElementsByClassName("btn-rSBtn");
        for (let i = 0; i < allBtn.length; i++) {
            allBtn[i].onclick = function (){
                showWaiting();
                let $tag = $(this);
                let prefix = $tag.data('prefix');
                console.log("prefix:"+prefix);
                $('input[name="btnStep"]').val("delete");
                $('input[name="pIds"]').val(prefix);
                $('input[name="prefixVal"]').val(prefix);
                let controlFormLi = $('#controlFormLi').val();
                submitForms('${serviceStepDto.currentStep.stepCode}',prefix,null,controlFormLi);
                let tr =this.parentNode.parentNode;
                tr.parentNode.removeChild(tr);
            };
        }
    }
    function sortRecords(sortFieldName,sortType){
        showWaiting();
        $("input[name='btnStep']").val("sort");
        $("input[name='classSort']").val("rdsSort");
        let controlFormLi = $('#controlFormLi').val();
        submitForms('${serviceStepDto.currentStep.stepCode}',sortFieldName,sortType,controlFormLi);
    }
</script>