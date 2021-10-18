

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
    <c:if test = "${not empty successMap}">
        <iais:success>
            <c:forEach items="${successMap}" var="map">
                ${map.key}  ${map.value} <br/>
            </c:forEach>
        </iais:success>
    </c:if>
    <iais:section title="demo List" id = "demoList">
        <iais:row>
            <iais:field value="Nirc No"/>
            <iais:value width="7">
                <input type="text" name="nric_no" value="${demoSearchParam.filters['nric_no']}" />
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="UEN No"/>
            <iais:value width="7">
                <input type="text" name="uen_no" value="${demoSearchParam.filters['uen_no']}" />
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="Create Date"/>
            <iais:value width="7">
                <iais:datePicker id = "fromDate" name = "fromDate" value="20/10/2019"></iais:datePicker>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="status"/>
            <iais:value width="7">
                <input type="checkbox" name="status" value="pending">Pending</input>
                <input type="checkbox" name="status" value="procing">Procing</input>
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
    <iais:pagination  param="demoSearchParam" result="demoSearchResult"/>
    <iais:searchSection title="Account List" onclick="" >
        <div class="table-responsive" id="no-more-tables">
            <table aria-describedby="" class="table table-bordered table-condensed cf alignctr shadow" id="tableId">
                <colgroup>
                    <col style="width: 10%;"/>
                    <col style="width: 20%;"/>
                    <col style="width: 20%;"/>
                    <col style="width: 20%;"/>
                    <col style="width: 20%;"/>
                    <col style="width: 10%;"/>
                </colgroup>
                <thead>
                <tr>
                    <th scope="col" ><input type="checkbox" id="allEntitiesCb1" name="allEntitiesCb1" onclick="sopCrud1.setAllCheckBox(this.checked)"></th>
                    <iais:sortableHeader needSort="false"  field="" value="No."></iais:sortableHeader>
                    <iais:sortableHeader needSort="true"   field="user_id" value="User Id"></iais:sortableHeader>
                    <iais:sortableHeader needSort="true"   field="NRIC_NO" value="Nuic Num"></iais:sortableHeader>
                    <iais:sortableHeader needSort="true"   field="UEN_NO" value="Uen No"></iais:sortableHeader>
                    <iais:sortableHeader needSort="false"  field="" value="Action"></iais:sortableHeader>
                </tr>
                </thead>
                <tbody style="text-align: center">
                 <c:choose>
                     <c:when test="${empty demoSearchResult.rows}">
                            <tr>
                                <td colspan="6">
                                     <iais:message key="ACK00001" escape="true"></iais:message>
                                    <!--No Record!!-->
                                </td>
                            </tr>
                     </c:when>
                     <c:otherwise>
                        <c:forEach var="demoQuery" items="${demoSearchResult.rows}" varStatus="status">
                            <tr>
                                <td><input type="checkbox" onclick="sopCrud1.updateAllCheckBox()" value="<iais:mask name="demo" value="${demoQuery.userId}"/>" name="demo" id="entityUids_${demoQuery.userId}"></td>
                                <td class="row_no">${(status.index + 1) + (demoSearchParam.pageNo - 1) * demoSearchParam.pageSize}</td>
                                <td>${demoQuery.userId}</td>
                                <td>${demoQuery.nuicNum}</td>
                                <td>${demoQuery.uenNo}</td>
                                <td>
                                    <a href="javascript:void(0);" onclick="javascript:doEdit('<iais:mask name="crud_action_value" value="${demoQuery.userId}"/>');">Edit</a>
                                    <iais:link icon="form_delete" title="Delete" onclick="javascript:doDelete('${demoQuery.userId}');"/>
                                </td>
                            </tr>
                        </c:forEach>
                     </c:otherwise>
                 </c:choose>
                </tbody>
            </table>
        </div>
    </iais:searchSection>
    <iais:pagination  param="searchParam" result="searchResult"/>
    <iais:action>
        <button class="btn btn-lg btn-login-submit" type="button" onclick="javascript:doCreat('${ORGANIZATION_ID}');">Create</button>
    </iais:action>
</iais:body>
</form>
<script type="text/javascript">
    function doSearch() {
       // submitForm("doSearch");
        ajaxTest();
        SOP.Crud.cfxSubmit("mainForm","doSearch");
    }
    function sortRecords(sortFieldName,sortType){
        SOP.Crud.cfxSubmit("mainForm","sortRecords",sortFieldName,sortType);
    }

    function jumpToPagechangePage(){
        SOP.Crud.cfxSubmit("mainForm","changePage");
    }
    function doDelete(userId){
        if(confirm('Are you sure you want to delete?')){
            SOP.Crud.cfxSubmit("mainForm","doDelete",userId);
        }
    }

    function doCreat(orgId) {
        SOP.Crud.cfxSubmit("mainForm","doCreate",orgId);
    }
    function doEdit(rowguid){
        SOP.Crud.cfxSubmit("mainForm","doEdit",rowguid);
    }

    function doClear() {
        $('input[name="nric_no"]').val("");
        $('input[name="uen_no"]').val("");
    }

    function ajaxTest(){
        $.ajax({
            data:"name=66",
            type:"GET",
            dataType: 'json',
            url:"/demo/orgUser/doSomething.do",
            error:function(data){
                alert("wrong:"+data.aaa);
            },
            success:function(data){
                alert("success:"+data.aaa);
            }
        });
    }


</script>