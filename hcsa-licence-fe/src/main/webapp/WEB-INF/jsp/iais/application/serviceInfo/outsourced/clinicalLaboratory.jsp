<c:set var="cL" value="${currSvcInfoDto.appPremOutSourceLicenceDto}"/>
<input type="hidden" name="classSort" value="">
<div class="clService" style="margin-top: 100px;!important;">
    <hr>
    <iais:row>
        <div class="col-xs-12">
            <strong>Outsourced Service Provider(s)</strong>
        </div>
    </iais:row>

    <iais:row>
        <div class="col-xs-12">
            <p>Clinical Laboratory</p>
        </div>
    </iais:row>

    <div class="col-lg-12 col-xs-12 col-md-12">
        <div class="intranet-content">
            <table aria-describedby="" class="table">
                <thead>
                <tr>
                    <iais:sortableHeader needSort="true" field="LICENCE_NO" value="Licence No." style="width:15%;" customSpacing="12"/>
<%--                    <th style="width: 15%;">--%>
<%--                        <p style="margin-left: 12px;">Licence No.</p>--%>
<%--                    </th>--%>
                    <th style="width: 15%;">
                        <p style="margin-left: 12px;">
                            Business Name
                        </p>
                    </th>
                    <th style="width: 10%;">
                        <p style="margin-left: 12px;">
                            Address
                        </p>
                    </th>
                    <th style="width: 15%;">
                        <p style="margin-left: 12px;">
                            Licence Tenure
                        </p>
                    </th>
                    <th style="width: 15%;">
                        <p style="margin-left: 12px;">
                            Date of Agreement
                        </p>
                    </th>
                    <th style="width: 15%;">
                        <p style="margin-left: 12px;">
                            End Date of Agreement
                        </p>
                    </th>
                    <th style="width: 15%;">
                        <p style="margin-left: 12px;">
                            Scope of Outsourcing
                        </p>
                    </th>
                    <th></th>
                </tr>
                </thead>
                <tbody>

                <c:if test="${empty cL.clinicalLaboratoryList}">
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

                <c:if test="${!empty cL.clinicalLaboratoryList}">
                    <c:set var="clen" value="${cL.clinicalLaboratoryList.size()}"/>
                    <input name="clenght" value="${clen}" type="hidden">
                    <%@include file="cLDTableDentail.jsp"%>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>
<script>
    $(document).ready(function () {
        delCLDBtn();
    });
    function delCLDBtn(){
        console.log("del....")
        let allBtn = document.getElementsByClassName("btn-cldBtn");
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
        $("input[name='classSort']").val("cLDSort");
        let controlFormLi = $('#controlFormLi').val();
        submitForms('${serviceStepDto.currentStep.stepCode}',sortFieldName,sortType,controlFormLi);
    }
</script>


