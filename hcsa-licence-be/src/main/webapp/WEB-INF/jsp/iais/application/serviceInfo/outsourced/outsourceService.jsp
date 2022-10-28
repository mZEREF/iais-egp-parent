<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<style>
    .table-info-display {
        margin: 30px 0px 5px 0px;
        background: #efefef;
        padding: 8px;
        border-radius: 8px;
        height: 66px;
        display: flex!important;
        -moz-border-radius: 8px;
        -webkit-border-radius: 8px;
    }
    .table-count {
        float: left;
        margin-top: 10px;
    }
    .nice-select.table-select {
        padding: 4px 10px;
        border-radius: 7px;
        min-width: 110px;
        margin-top: 9px;!important;
    }

    .cSBtn{
        margin-bottom: 55px;
    }
    .btn-outsourced-search a:not(:last-child) {
        margin-right: 20px;
    }
    .side-point {
        margin-left: 3px !important;
    }
    .side-point:before {
        content: "";
        width: 4px;
        height: 4px;
        background-color: #333333;
        border-radius: 3px;
        position: absolute;
        top: 9px;
        left: 0;
    }
</style>
<iais:row>
    <div class="col-xs-12">
        <h2 class="app-title">Outsourced Service(s)</h2>
        <p><h4><iais:message key="NEW_ACK41"/></h4></p>
        <p><span class="error-msg" name="iaisErrorMSg" id="error_psnMandatory"></span></p>
    </div>
</iais:row>

<div class="searchService clearTep outsourced-content">
    <input type="hidden" class ="isPartEdit" name="isPartEdit" value="0"/>
    <input type="hidden" class="outsourcedIndexNo" name="outsourcedIndexNo" value=""/>
    <div class="col-md-12 col-xs-12">
        <div class="edit-content">
            <c:if test="${canEdit}">
                <div class="text-right app-font-size-16">
                    <a class="edit outsourcedEdit" href="javascript:void(0);">
                        <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                    </a>
                </div>
            </c:if>
        </div>
    </div>
    <input type="hidden" name="btnStep" value="">
    <input type="hidden" name="pIds" value="">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <div class="col-xs-12 col-md-12" style="margin-top: 30px;margin-left: 30px;!important;">
            <div class="col-xs-8 col-md-8 side-point">
                <span>Clinical Laboratory</span>
            </div>
        </div>
        <div class="col-xs-12 col-md-12" style="margin-top:15px;margin-left: 30px;!important;">
            <div class="col-xs-8 col-md-8 side-point">
                <span>Radiological Service</span>
            </div>
        </div>
        <div class="col-md-12 col-xs-12" style="margin-top: 30px;!important;">
            <div class="col-xs-6 col-md-6 svcNameSel">
                <iais:row>
                    <iais:field width="5" value="Service" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <%String outSourceSel = request.getParameter("serviceCode");%>
                        <iais:select id="serviceCode" name="serviceCode" options="outsourcedServiceSelectOpts" firstOption="Please Select" value="<%=outSourceSel%>"/>
                    </iais:value>
                </iais:row>
                <span id="error_serviceCode" name="iaisErrorMsg" class="error-msg"></span>
            </div>

            <div class="col-xs-6 col-md-6">
                <iais:row>
                    <iais:field width="5"  value="Business Name"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <%String businessName = request.getParameter("businessName");%>
                        <iais:input maxLength="100" type="text" cssClass="businessName" name="businessName" value="<%=businessName%>"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>

        <div class="col-md-12 col-xs-12">
            <div class="col-xs-6 col-md-6">
                <iais:row>
                    <iais:field width="5"  value="Licence No. "/>
                    <iais:value width="7" cssClass="col-md-7">
                        <%String licNo = request.getParameter("licNo");%>
                        <iais:input maxLength="24" type="text" cssClass="licNo" name="licNo" value="<%=licNo%>"/>
                    </iais:value>
                </iais:row>
            </div>

            <div class="col-xs-6 col-md-6">
                <iais:row>
                    <iais:field width="5"  value="Postal Code"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <%String postalCode = request.getParameter("postalCode");%>
                        <iais:input maxLength="6" type="text" cssClass="postalCode" name="postalCode" value="<%=postalCode%>"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>

        <div class="row cSBtn">
            <div class="col-xs-12 col-sm-7"></div>
            <div class="col-xs-12 col-sm-5" style="text-align: center;">
                <div class="button-group">
                    <a class="btn btn-secondary btn-outsourced-clear" id="ANT_Clearn">Clear</a>
                    <a class="btn btn-primary btn-outsourced-search" id="ANT_Search">Search</a>
                </div>
            </div>
        </div>
        <c:if test="${!empty outSourceParam}">
            <%@include file="outsourceProvidesTable.jsp"%>
            <c:if test="${outSourceResult.rows.size() eq 0}">
                <span id="noRecord" name="noRecord" class="error-msg">No record found.</span>
            </c:if>
        </c:if>



    </form>

</div>


<script>

    $("#ANT_Clearn").click(function () {
        $("[name='businessName']").val("");
        $("[name='licNo']").val("");
        $("[name='postalCode']").val("");
        $("#outsourcedServiceSelect option:first").prop("selected", 'selected').val("");
        $("#outsourcedServiceSelect").val("");
        $(".svcNameSel .current").text("Please Select");
    });

    $("#ANT_Search").click(function () {
        showWaiting();
        if (${AppSubmissionDto.needEditController }){
            $('a.outsourcedEdit').trigger('click');
        }
        $("input[name='btnStep']").val("search");
        let controlFormLi = $('#controlFormLi').val();
        submitForms('${serviceStepDto.currentStep.stepCode}','search',null,controlFormLi);
    });

    function sortRecords(sortFieldName,sortType){
        showWaiting();
        if (${AppSubmissionDto.needEditController }){
            $('a.outsourcedEdit').trigger('click');
        }
        $("input[name='btnStep']").val("sort");
        let controlFormLi = $('#controlFormLi').val();
        submitForms('${serviceStepDto.currentStep.stepCode}',sortFieldName,sortType,controlFormLi);
    }

    function jumpToPagechangePage(){
        showWaiting();
        if (${AppSubmissionDto.needEditController }){
            $('a.outsourcedEdit').trigger('click');
        }
        $("input[name='btnStep']").val("changePage");
        let controlFormLi = $('#controlFormLi').val();
        submitForms('${serviceStepDto.currentStep.stepCode}',"changePage",null,controlFormLi);
    }

</script>

