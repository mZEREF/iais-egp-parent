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

<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">

<iais:body>
        <iais:section title="Message List" id="msgList">
            <iais:row>
                <iais:field value="Domain Type" required="true"></iais:field>
                <iais:value width="7">
                    <iais:select name="domainType" id="domainType" options="domainTypeSelect" firstOption="Please select" onchange="displaySection()"></iais:select>
                </iais:value>
            </iais:row>

            <iais:row id="msgTypeRow" style="display:none">
                <iais:field value="Msg Type" required="false"></iais:field>
                <iais:value width="7">
                    <iais:select name="msgType" options="msgTypeSelect" firstOption="Please select" ></iais:select>
                </iais:value>
            </iais:row>

            <iais:row id="moduleTypeRow" style="display:none">
                <iais:field value="Module" required="false"></iais:field>
                <iais:value width="7">
                    <iais:select name="module"  options="moduleTypeSelect" firstOption="Please select" ></iais:select>
                </iais:value>
            </iais:row>

            <iais:action>
                <button type="button" class="search btn" onclick="javascript:doSearch();">Search</button>
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

    <iais:pagination  param="msgSearchParam" result="msgSearchResult"/>
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
                <iais:sortableHeader needSort="true"   field="msg_type" value="Message Type"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="module" value="Module"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="description" value="Description"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="message" value="Message"></iais:sortableHeader>
            </tr>
            </thead>

            <tbody style="text-align: center">
            <c:choose>
                <c:when test="${empty msgSearchResult.rows}">
                    <tr>
                        <td colspan="6">
                            No Record!!
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                                                <%-- message entity--%>
                    <c:forEach var = "msgQuery" items = "${msgSearchResult.rows}" varStatus="status">
                        <tr>
                            <td class="row_no">${(status.index + 1) + (msgSearchParam.pageNo - 1) * msgSearchParam.pageSize}</td>
                            <td>${msgQuery.domainType}</td>
                            <td>${msgQuery.msgType}</td>
                            <td>${msgQuery.module}</td>
                            <td>${msgQuery.description}</td>
                            <td>${msgQuery.message}</td>
                            <td>
                                <iais:link icon="form_edit" title="Edit" onclick="javascript:prepareEdit('${msgQuery.id}');"/>
                                <iais:link icon="form_delete" title="Disable" onclick="javascript:disable('${msgQuery.id}');"/>
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
    function doSearch(){
        SOP.Crud.cfxSubmit("mainForm", "doSearch");
    }

    function sortRecords(sortFieldName,sortType){
        SOP.Crud.cfxSubmit("mainForm","sortRecords",sortFieldName,sortType);
    }

    function jumpToPagechangePage(){
        SOP.Crud.cfxSubmit("mainForm","changePage");
    }

    function prepareEdit(id){
        if(confirm('are sure you want to edit ? ')){
            SOP.Crud.cfxSubmit("mainForm", "prepareEdit", id);
        }
    }

    function disable(id){
        if(confirm('are sure you want to disable ? ')){
            SOP.Crud.cfxSubmit("mainForm", "disableStatus", id);
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
