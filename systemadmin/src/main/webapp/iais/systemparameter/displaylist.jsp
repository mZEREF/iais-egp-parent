<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 9/4/2019
  Time: 4:15 PM
  To change this template use File | Settings | File Templates.
--%>
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

<form id = "spForm" method = "post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">

    <iais:body>
    <iais:section title="System Parameter List" id="spList">
        <iais:row>
            <iais:field value="Domain Type" required="true"></iais:field>
            <iais:value width="7">
                <iais:select name="domainType" id="domainType" options="domainTypeSelect" firstOption="Please select" onchange="displaySection()"></iais:select>
            </iais:value>

            <iais:field value="Module" required="false"></iais:field>
            <iais:value width="7">
                <iais:select name="module" id="module" options="moduleTypeSelect" firstOption="Please select"></iais:select>
            </iais:value>

            <iais:field value="Desciption" required="false"></iais:field>
            <iais:value width="7">
                <input type="text" name="desciption" value="" />
            </iais:value>

            <iais:field value="Status" required="false"></iais:field>
            <iais:value width="7">
                <iais:select name="status" id="status" options="statusTypeSelect" firstOption="Please select"></iais:select>
            </iais:value>
        </iais:row>

        <iais:action>
            <button type="button" class="search btn" onclick="javascript:doQuery();">Search</button>
        </iais:action>
    </iais:section>
    </br>

    <iais:error>
        <c:if test = "${not empty errorMap}">
            <div class="error">
                <c:forEach items="${errorMap}" var="map">
                    ${map.key}  ${map.value} <br/>
                </c:forEach>
            </div>
        </c:if>
    </iais:error>

    </br>
    <iais:pagination  param="systemParamSearch" result="systemParamSearchResult"/>
    <iais:searchSection title="" onclick="">
    <div class="table-responsive" id="no-more-tables">
        <table class="table table-bordered table-condensed cf alignctr shadow" id="tableId">
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
                <iais:sortableHeader needSort="false"  field="" value="No."></iais:sortableHeader>
                <iais:sortableHeader needSort="true"  field="domain_type" value="Domain Type"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="module" value="Module"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="description" value="Description"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="valueType" value="Value Of Type"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="value" value="Value"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="status" value="Status"></iais:sortableHeader>
            </tr>
            </thead>

            <tbody style="text-align: center">
            <c:choose>
                <c:when test="${empty systemParamSearchResult.rows}">
                    <tr>
                        <td colspan="6">
                            No Record!!
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <%-- message entity--%>
                    <c:forEach var = "resultRow" items = "${systemParamSearchResult.rows}" varStatus="status">
                        <tr>
                            <td class="row_no">${(status.index + 1) + (systemParamSearchParam.pageNo - 1) * systemParamSearchParam.pageSize}</td>
                            <td>${resultRow.domainType}</td>
                            <td>${resultRow.module}</td>
                            <td>${resultRow.description}</td>
                            <td>${resultRow.valueType}</td>
                            <td>${resultRow.value}</td>
                            <td>${resultRow.status}</td>
                            <td>
                                <iais:link icon="form_edit" title="Edit" onclick="javascript:prepareEdit('${resultRow.rowguid}');"/>
                                <iais:link icon="form_delete" title="Disable" onclick="javascript:disable('${resultRow.rowguid}');"/>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>

        </iais:searchSection>

        </iais:body>
</form>

<script type="text/javascript">
    function doQuery(){
        SOP.Crud.cfxSubmit("spForm", "doQuery");
    }

    function sortRecords(sortFieldName,sortType){
        SOP.Crud.cfxSubmit("mainForm","sortRecords",sortFieldName,sortType);
    }

    function jumpToPagechangePage(){
        SOP.Crud.cfxSubmit("mainForm","changePage");
    }

    function prepareEdit(rowguid){
        if(confirm('are sure you want to edit ? ')){
            SOP.Crud.cfxSubmit("messageForm", "prepareEdit", rowguid);
        }
    }

    function disable(rowguid){
        if(confirm('are sure you want to disable ? ')){
            SOP.Crud.cfxSubmit("messageForm", "disableStatus", rowguid);
        }
    }

    function displaySection(){
        var val = document.getElementById("domainType").value;
        if(val == null){
            return;
        }

        var msgTypeRow = document.getElementById("msgTypeRow");
        var moduleTypeRow = document.getElementById("moduleTypeRow");
        if(msgTypeRow && moduleTypeRow){
            msgTypeRow.style = "block";
            moduleTypeRow.style = "block";
        }
    }

</script>

