<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%

sop.webflow.rt.api.BaseProcessClass process =
(sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setAttribute name="header-ext">
<%

%>
</webui:setAttribute>

<webui:setAttribute name="title">
<%
/* You can set your page title here. */
%>

<%=process.runtime.getCurrentComponentName()%>

</webui:setAttribute>
<form method="post" class="__egovform" action=<%=process.runtime.continueURL()%>>
<input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
<input type="hidden" name="crud_action_type" value="">
<input type="hidden" name="crud_action_value" value="">
<input type="hidden" name="crud_action_additional" value="">
    <iais:body >
        <h2> Master Code View</h2>
        <c:if test = "${not empty successMap}">
            <iais:success>
                <c:forEach items="${successMap}" var="map">
                    ${map.key}  ${map.value} <br/>
                </c:forEach>
            </iais:success>
        </c:if>
        <iais:section title="Search MasterCode" id = "masterCodeList">
            <iais:row>
                <iais:field value="Master Code Key"/>
                <iais:value width="8">
                    <input type="text" name="master_code_key" value="${MasterCodeSearchParam.filters['masterCodeValue']}" />
                </iais:value>
            </iais:row>
            <iais:row>
                <iais:field value="Code Value"/>
                <iais:value width="8">
                    <input type="text" name="code_value" value="${MasterCodeSearchParam.filters['codeValue']}" />
                </iais:value>
            </iais:row>
            <iais:row>
                <iais:field value="Status"/>
                <iais:value width="7">
                    <input type="checkbox" name="status" value="pending">Pending</input>
                    <input type="checkbox" name="status" value="procing">Procing</input>
                </iais:value>
            </iais:row>
            <br>
            <iais:action >
                <button class="btn btn-lg btn-login-clear" type="button" onclick="javascript:doClear()">Clear</button>
                <button class="btn btn-lg btn-login-submit" type="button" onclick="javascript:doSearch()">Search</button>
            </iais:action>
        </iais:section>

        <div class="clear"></div>
        <br/>

        <iais:searchSection title="MasterCode List" onclick="" >
            <div class="table-responsive" id="no-more-tables">
                <table class="table table-bordered table-condensed cf alignctr shadow" id="tableId">
                    <colgroup>
                        <col width="5%"/>
                        <col width="20%"/>
                        <col width="20%"/>
                        <col width="15%"/>
                        <col width="30%"/>
                    </colgroup>
                    <thead>
                    <tr>
                        <th><input type="checkbox" id="allEntitiesCb1" name="allEntitiesCb1" onclick="sopCrud1.setAllCheckBox(this.checked)"></th>
                        <iais:sortableHeader needSort="false"  field="" value="No."></iais:sortableHeader>
                        <iais:sortableHeader needSort="true"   field="masterCodeKey" value="MasterCode Key"></iais:sortableHeader>
                        <iais:sortableHeader needSort="true"   field="codeValue" value="Code Value"></iais:sortableHeader>
                        <iais:sortableHeader needSort="true"   field="status" value="Status"></iais:sortableHeader>
                        <iais:sortableHeader needSort="false"  field="" value="Action"></iais:sortableHeader>
                    </tr>
                    </thead>
                    <tbody style="text-align: center">
                    <c:choose>
                        <c:when test="${empty MasterCodeSearchResult.rows}">
                            <tr>
                                <td colspan="6">
<%--                                    <iais:message key="ACK00001" escape="true"></iais:message>--%>
                                    <sapn>No Record</sapn>
                                    <!--No Record!!-->
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="masterCodeQuery" items="${MasterCodeSearchResult.rows}" varStatus="status">
                                <tr>
                                    <td><input type="checkbox" onclick="sopCrud1.updateAllCheckBox()" value="${masterCodeQuery.masterCodeId}" name="mastercode" id="entityUids_${masterCodeQuery.masterCodeId}"></td>
                                    <td class="row_no">${(status.index + 1) + (MasterCodeSearchParam.pageNo - 1) * MasterCodeSearchParam.pageSize}</td>
                                    <td>${masterCodeQuery.masterCodeKey}</td>
                                    <td>${masterCodeQuery.codeValue}</td>
                                    <td>${masterCodeQuery.status}</td>
                                    <td>
                                        <iais:link icon="form_edit" title="Edit" onclick="javascript:doEdit('${masterCodeQuery.rowguid}');"/>
                                        <iais:link icon="form_delete" title="Delete" onclick="javascript:doDelete('${masterCodeQuery.masterCodeId}');"/>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </iais:searchSection>
        <iais:pagination  param="MasterCodeSearchParam" result="MasterCodeSearchResult"/>
        <iais:pagination  param="searchParam" result="searchResult"/>
        <iais:action>
            <button class="btn btn-lg btn-login-submit" type="button" onclick="javascript:doCreat('${master_code_id}');">Create</button>
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
    function doDelete(masterCodeId){
        if(confirm('Are you sure you want to delete?')){
            SOP.Crud.cfxSubmit("mainForm","doDelete",masterCodeId);
        }
    }

    function doCreat(master_code_id) {
        SOP.Crud.cfxSubmit("mainForm","doCreate",master_code_id);
    }
    function doEdit(rowguid){
        SOP.Crud.cfxSubmit("mainForm","doEdit",rowguid);
    }

    function doClear() {
        $('input[name="master_code_key"]').val("");
        $('input[name="code_value"]').val("");
    }
</script>