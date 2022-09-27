<iais:row>
    <div class="col-xs-12">
        <p>Radiological Service</p>
    </div>
</iais:row>

<div class="col-lg-12 col-xs-12 col-md-12">
    <div class="intranet-content">
        <table aria-describedby="" class="table">
            <thead>
            <tr>
                <th style="width: 15%;">
                    <p style="margin-left: 12px;">
                        Licence No.
                    </p>
                </th>
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
            <c:set var="cL" value="${currSvcInfoDto.appPremOutSourceLicenceDto}"/>
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
</script>