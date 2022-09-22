<%@ page import="org.springframework.util.StringUtils" %>
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
</style>
<c:if test="${AppSubmissionDto.needEditController }">
    <c:if test="${(isRfc || isRenew) && !isRfi}">
        <iais:row>
            <div class="text-right app-font-size-16">
                <a class="back" id="RfcSkip" href="javascript:void(0);">
                    Skip<span style="display: inline-block;">&nbsp;</span><em class="fa fa-angle-right"></em>
                </a>
            </div>
        </iais:row>
    </c:if>
    <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.serviceEdit}"/>
</c:if>
<iais:row>
    <div class="col-xs-12">
        <h2 class="app-title">Outsourced Service(s)</h2>
        <p><h4><iais:message key="NEW_ACK41"/></h4></p>
        <p><span class="error-msg" name="iaisErrorMSg" id="error_psnMandatory"></span></p>
    </div>
</iais:row>

<div class="searchService clearTep">
    <input type="hidden" name="btnStep" value="">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <div class="col-md-12 col-xs-12">
            <div class="col-xs-6 col-md-6 svcNameSel">
                <iais:row>
                    <iais:field width="5" value="Service" required="true"/>
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
                        <%String name = request.getParameter("name");%>
                        <iais:input maxLength="100" type="text" cssClass="name" name="name" value="<%=name%>"/>
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
                        <iais:input maxLength="20" type="text" cssClass="licNo" name="licNo" value="<%=licNo%>"/>
                    </iais:value>
                </iais:row>
            </div>

            <div class="col-xs-6 col-md-6">
                <iais:row>
                    <iais:field width="5"  value="Postal Code"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <%String postalCode = request.getParameter("postalCode");%>
                        <iais:input maxLength="6" type="number" cssClass="postalCode" name="postalCode" value="<%=postalCode%>"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>

        <div class="col-xs-12 col-md-12 cSBtn">
            <div class="col-xs-6 col-md-6"></div>
            <div class="col-xs-6 col-md-6" style="padding-left: 200px;!important;">
                <a class="btn btn-secondary" id="ANT_Clearn">Clear</a>
                <a class="btn btn-primary" id="ANT_Search">Search</a>
            </div>
        </div>

        <%@include file="outsourceProvidesTable.jsp"%>
    </form>

</div>


<script>

    $("#ANT_Clearn").click(function () {
        $("[name='name']").val("");
        $("[name='licNo']").val("");
        $("[name='postalCode']").val("");
        $("#outsourcedServiceSelect option:first").prop("selected", 'selected').val("");
        $("#outsourcedServiceSelect").val("");
        $(".svcNameSel .current").text("Please Select");
    });

    $("#ANT_Search").click(function () {
        showWaiting();
        $("input[name='btnStep']").val("search");
        let controlFormLi = $('#controlFormLi').val();
        submitForms('${serviceStepDto.currentStep.stepCode}','search',null,controlFormLi);
    });

    function sortRecords(sortFieldName,sortType){
        showWaiting();
        $("input[name='btnStep']").val("sort");
        let controlFormLi = $('#controlFormLi').val();
        submitForms('${serviceStepDto.currentStep.stepCode}',sortFieldName,sortType,controlFormLi);
    }

    function jumpToPagechangePage(){
        showWaiting();
        $("input[name='btnStep']").val("changePage");
        let controlFormLi = $('#controlFormLi').val();
        submitForms('${serviceStepDto.currentStep.stepCode}',"changePage",null,controlFormLi);
    }

</script>

