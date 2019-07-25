

<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>


<%

    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<webui:setAttribute name="header-ext">
    <%
        /* You can add additional content (SCRIPT, STYLE elements)
         * which need to be placed inside HEAD element here.
         */
    %>
</webui:setAttribute>

<webui:setAttribute name="title">
    <%
        /* You can set your page title here. */
    %>

    <%=process.runtime.getCurrentComponentName()%>

</webui:setAttribute>
<!-- START: CSS -->

<!-- END: CSS -->
<form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
<iais:body >

    <br/>
    <h2>demo List</h2>
    <iais:section title="demo List" id = "demoList">
        <iais:row>
            <iais:field value="Nirc Name"/>
            <iais:value width="7">
                <input type="text" name="nric_no" value="${searchParam.filters['nric_no']}" />
            </iais:value>
        </iais:row>
        <iais:action >
            <button class="btn btn-lg btn-login-clear" type="button" onclick="javascript:doClear()">Clear</button>
            <button class="btn btn-lg btn-login-submit" type="button" onclick="javascript:doSearch()">Search</button>
        </iais:action>
    </iais:section>

    <div class="clear"></div>
    <br/>
    <br/>
    <iais:pagination  param="searchParam" result="searchResult"/>
    <iais:searchSection title="Account List" onclick="" >
        <div class="table-responsive" id="no-more-tables">
            <table class="table table-bordered table-condensed cf alignctr shadow" id="tableId">
                <colgroup>
                    <col width="5%"/>
                    <col width="15%"/>
                    <col width="15%"/>
                    <col width="15%"/>
                    <col width="8%"/>
                </colgroup>
                <thead>
                <tr>
                    <iais:sortableHeader needSort="false"  field="" value="No."></iais:sortableHeader>
                    <iais:sortableHeader needSort="true"   field="user_id" value="User Id"></iais:sortableHeader>
                    <iais:sortableHeader needSort="true"   field="NRIC_NO" value="Nuic Num"></iais:sortableHeader>
                    <iais:sortableHeader needSort="true"   field="UEN_NO" value="Uen No"></iais:sortableHeader>
                    <iais:sortableHeader needSort="false"  field="" value="Action"></iais:sortableHeader>
                </tr>
                </thead>
                <tbody style="text-align: center">

                        <c:forEach var="demoQuery" items="${searchResult.rows}" varStatus="status">
                            <tr>
                                <td class="row_no">${(status.index + 1) + (searchParam.pageNo - 1) * searchParam.pageSize}</td>
                                <td>${demoQuery.userId}</td>
                                <td>${demoQuery.nuicNum}</td>
                                <td>${demoQuery.uenNo}</td>
                                <td>
                                    <iais:link icon="form_edit" title="Edit" onclick="javascript:doEdit('${demoQuery.userId}');"/>
                                    <iais:link icon="form_delete" title="Delete" onclick="javascript:doDelete('${demoQuery.userId}');"/>
                                </td>
                            </tr>
                        </c:forEach>

                </tbody>
            </table>
        </div>
    </iais:searchSection>
    <iais:pagination  param="searchParam" result="searchResult"/>
    <iais:action>
        <button class="btn btn-lg btn-login-submit" type="button" onclick="javascript:doCreat('${demoQuery.orgId}');">Create</button>
    </iais:action>
</iais:body>
</form>
<script type="text/javascript">
    function doSearch() {
       // submitForm("doSearch");
        SOP.Crud.cfxSubmit("mainForm","doSearch");
    }
    function sortRecords(sortFieldName,sortType){
        SOP.Crud.cfxSubmit("mainForm","sortRecords",sortFieldName,sortType);
    }

    function jumpToPagechangePage(){
        SOP.Crud.cfxSubmit("mainForm","changePage");
    }
    function doDelete(userId){
        SOP.Crud.cfxSubmit("mainForm","doDelete",userId);
    }

    function doCreat(orgId) {
        OP.Crud.cfxSubmit("mainForm","doCreate",userId);
    }

    function doEdit(guid) {
        $('#guid').val(guid);
        submitAction("doPrepareEdit");
    }

    function doAddPremises(guid) {
        $('#guid').val(guid);
        submitAction("doPrepareEditPremise");
    }

    function changePage(pageNo) {
        $('#pageNo').val(pageNo);
        submitAction('page');
    }

    function doSort(field, type) {
        $('input[name="sortField"]').val(field);
        $('input[name="sortType"]').val(type);
        submitAction('sort');
    }

    function changePageSize() {
        submitAction('pageSize');
    }

    function doClear() {
        $('input[name="searchname"]').val("");
    }
</script>