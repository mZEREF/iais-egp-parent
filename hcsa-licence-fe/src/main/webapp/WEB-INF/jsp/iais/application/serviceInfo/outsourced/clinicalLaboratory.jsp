<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts" %>
<c:set var="cL" value="${currSvcInfoDto.appSvcOutsouredDto}"/>
<input type="hidden" name="classSort" value="">
<div class="clService" style="margin-top: 100px;!important;">
    <hr>
    <iais:row>
        <div class="col-xs-12">
            <strong>Outsourced Service Provider(s)</strong>
        </div>
    </iais:row>

    <c:set var="svcCodeItem" value="0"/>
    <c:if test="${!empty cL.svcCodeList}">
        <c:set var="svcCodeList" value="${cL.svcCodeList}"/>
        <c:forEach var="svcCode" items="${svcCodeList}">
            <c:if test="${svcCode eq AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY}">
                <c:set var="svcCodeItem" value="1" />
            </c:if>
        </c:forEach>
    </c:if>
    <input name="svc" value="${svcCodeItem}" type="hidden">
    <iais:row>
        <div class="col-xs-12 <c:if test="${svcCodeItem eq 1}">hidden</c:if>">
            <strong>Clinical Laboratory</strong>
        </div>
        <div class="col-xs-12">
            <span class="error-msg" name="iaisErrorMsg" id="error_clbList"></span>
        </div>
    </iais:row>

    <div class="col-lg-12 col-xs-12 col-md-12 <c:if test="${svcCodeItem eq 1}">hidden</c:if>">
        <div class="intranet-content">
            <table aria-describedby="" class="table">
                <thead>
                <tr>
                    <th class="sorting" style="width:15%;">
                        <span class="column-sort">
                            <a class="sort-up" href="javascript:sortCLDRecords('LICENCE_NO', 'ASC');" title="Sort up">
                                <span class="glyphicon glyphicon-chevron-up"></span>
                            </a>
                            <a class="sort-down " href="javascript:sortCLDRecords('LICENCE_NO', 'DESC');" title="Sort down">
                                <span class="glyphicon glyphicon-chevron-down" style="font-size: 10px"></span>
                            </a>
                        </span>
                        <p style="margin-left:12px;">Licence No.</p>
                    </th>
                    <th class="sorting" style="width:15%;">
                        <span class="column-sort">
                            <a class="sort-up" href="javascript:sortCLDRecords('BUSINESS_NAME', 'ASC');" title="Sort up">
                                <span class="glyphicon glyphicon-chevron-up"></span>
                            </a>
                            <a class="sort-down " href="javascript:sortCLDRecords('BUSINESS_NAME', 'DESC');" title="Sort down">
                                <span class="glyphicon glyphicon-chevron-down" style="font-size: 10px"></span>
                            </a>
                        </span>
                        <p style="margin-left:12px;">Business Name</p>
                    </th>
                    <th class="sorting" style="width:10%;">
                        <span class="column-sort">
                            <a class="sort-up" href="javascript:sortCLDRecords('ADDRESS', 'ASC');" title="Sort up">
                                <span class="glyphicon glyphicon-chevron-up"></span>
                            </a>
                            <a class="sort-down " href="javascript:sortCLDRecords('ADDRESS', 'DESC');" title="Sort down">
                                <span class="glyphicon glyphicon-chevron-down" style="font-size: 10px"></span>
                            </a>
                        </span>
                        <p style="margin-left:12px;">Address</p>
                    </th>
                    <th class="sorting" style="width:15%;">
                        <span class="column-sort">
                            <a class="sort-up" href="javascript:sortCLDRecords('EXPIRY_DATE', 'ASC');" title="Sort up">
                                <span class="glyphicon glyphicon-chevron-up"></span>
                            </a>
                            <a class="sort-down " href="javascript:sortCLDRecords('EXPIRY_DATE', 'DESC');" title="Sort down">
                                <span class="glyphicon glyphicon-chevron-down" style="font-size: 10px"></span>
                            </a>
                        </span>
                        <p style="margin-left:30px;">Licence Tenure</p>
                    </th>
                    <th class="sorting" style="width:15%;">
                        <span class="column-sort">
                            <a class="sort-up" href="javascript:sortCLDRecords('AGREEMENT_START_DATE', 'ASC');" title="Sort up">
                                <span class="glyphicon glyphicon-chevron-up"></span>
                            </a>
                            <a class="sort-down " href="javascript:sortCLDRecords('AGREEMENT_START_DATE', 'DESC');" title="Sort down">
                                <span class="glyphicon glyphicon-chevron-down" style="font-size: 10px"></span>
                            </a>
                        </span>
                        <p style="margin-left:12px;">Date of Agreement</p>
                    </th>
                    <th class="sorting" style="width:15%;">
                        <span class="column-sort">
                            <a class="sort-up" href="javascript:sortCLDRecords('AGREEMENT_END_DATE', 'ASC');" title="Sort up">
                                <span class="glyphicon glyphicon-chevron-up"></span>
                            </a>
                            <a class="sort-down " href="javascript:sortCLDRecords('AGREEMENT_END_DATE', 'DESC');" title="Sort down">
                                <span class="glyphicon glyphicon-chevron-down" style="font-size: 10px"></span>
                            </a>
                        </span>
                        <p style="margin-left:12px;">End Date of Agreement</p>
                    </th>
                    <th class="sorting" style="width:15%;">
                        <span class="column-sort">
                            <a class="sort-up" href="javascript:sortCLDRecords('OUTSTANDING_SCOPE', 'ASC');" title="Sort up">
                                <span class="glyphicon glyphicon-chevron-up"></span>
                            </a>
                            <a class="sort-down " href="javascript:sortCLDRecords('OUTSTANDING_SCOPE', 'DESC');" title="Sort down">
                                <span class="glyphicon glyphicon-chevron-down" style="font-size: 10px"></span>
                            </a>
                        </span>
                        <p style="margin-left:12px;">Scope of Outsourcing</p>
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
                if (${AppSubmissionDto.needEditController }){
                    $('a.outsourcedEdit').trigger('click');
                }
                let $tag = $(this);
                let prefix = $tag.data('prefix');
                let outsourcedIndexNo = $tag.data('group');
                console.log("del-prefix:"+prefix);
                console.log("outsourcedIndexNo:"+outsourcedIndexNo);
                $('input[name="btnStep"]').val("delete");
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
    function sortCLDRecords(sortFieldName,sortType){
        showWaiting();
        if (${AppSubmissionDto.needEditController }){
            $('a.outsourcedEdit').trigger('click');
        }
        $("input[name='btnStep']").val("sort");
        $("input[name='classSort']").val("cLDSort");
        let controlFormLi = $('#controlFormLi').val();
        submitForms('${serviceStepDto.currentStep.stepCode}',sortFieldName,sortType,controlFormLi);
    }
</script>


