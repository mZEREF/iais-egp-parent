<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 10/6/2019
  Time: 3:10 PM
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

<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">


<%--    <input type="hidden" name="itemId" value="">
    <input type="hidden" name="regulationClause" value="">
    <input type="hidden" name="regulationDesc" value="">
    <input type="hidden" name="checklistItem" value="">
    <input type="hidden" name="riskLevel" value="">
    <input type="hidden" name="status" value="">--%>

    <iais:body>
    <iais:section title="Checklist Item View" id="checklistItemSection">
        <iais:row>
            <iais:field value="Regulation Clause Number" required="false"></iais:field>
            <iais:value width="7">
                <iais:value width="7">
                    <input type="text" name="regulationClauseNo" value="" />
                </iais:value>
            </iais:value>

            <iais:field value="Regulation" required="false"></iais:field>
            <iais:value width="7">
                <iais:value width="7">
                    <input type="text" name="regulationClause" value="" />
                </iais:value>
            </iais:value>

            <iais:field value="Checklist Item" required="false"></iais:field>
            <iais:value width="7">
                <iais:value width="7">
                    <input type="text" name="checklistItem" value="" />
                </iais:value>
            </iais:value>

            <iais:field value="Risk Level" required="false"></iais:field>
            <iais:value width="7">
                <iais:select name="riskLevel" id="riskLevel" codeCategory="CATE_ID_RISK_LEVEL" firstOption="Select Risk Level"></iais:select>
            </iais:value>

            <iais:field value="Status" required="false"></iais:field>
            <iais:value width="7">
                <iais:select name="status" id="status" codeCategory="CATE_ID_COMMON_STATUS" firstOption="Select Status"></iais:select>
            </iais:value>

            <iais:field value="Answer Type" required="false"></iais:field>
            <iais:value width="7">
                <iais:select name="answerType" id="answerType" codeCategory="CATE_ID_ANSWER_TYPE" firstOption="Select Answer Type"></iais:select>
            </iais:value>
        </iais:row>


        <iais:action>
            <button type="button" class="btn btn-lg btn-login-submit" onclick="javascript:doSearch();">Search</button>
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

    <iais:pagination  param="checklistItemSearch" result="checklistItemResult"/>
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
                <td></td>
                <iais:sortableHeader needSort="true"   field="regulationClauseNo" value="Regulation Clause Number"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="regulationClause" value="Regulations"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="checklistItem" value="Checklist Item"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="riskLevel" value="Rusk Level"></iais:sortableHeader>
                <iais:sortableHeader needSort="false"   field="status" value="Status"></iais:sortableHeader>
            </tr>
            </thead>

            <tbody style="text-align: center">
            <c:choose>
                <c:when test="${empty checklistItemResult.rows}">
                    <tr>
                        <td colspan="6">
                            No Record!!
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <%-- message entity--%>

                    <c:forEach var = "item" items = "${checklistItemResult.rows}" varStatus="status">
                        <tr>
                            <td class="row_no">${(status.index + 1) + (checklistItemSearch.pageNo - 1) * checklistItemSearch.pageSize}</td>
                            <td><input name="itemCheckbox" id="itemCheckbox" type="checkbox" value="${item.itemId}" /></td>
                            <td>${item.regulationClauseNo}</td>
                            <td>${item.regulationClause}</td>
                            <td>${item.checklistItem}</td>
                            <td>${item.riskLevel}</td>
                            <td>${item.status}</td>
                            <td>
                                <iais:link icon="form_edit" title="Edit" onclick="javascript:prepareEditItem('${item.itemId}');"/>
                                <iais:link icon="form_delete" title="Disable" onclick="javascript:disable('${item.itemId}');"/>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>


        </table>

        </iais:searchSection>

        <iais:action>
              <button type="button" class="btn btn-lg btn-login-submit" onclick="javascript:prepareAddItem();">Add</button>
        </iais:action>

        <iais:action>
        <button type="button" class="btn btn-lg btn-login-submit" onclick="javascript:prepareClone();">Clone</button>
        </iais:action>

        </iais:body>
</form>

<script type="text/javascript">
    function doSearch(){
        SOP.Crud.cfxSubmit("mainForm", "doSearch");
    }

    function prepareAddItem(){
        SOP.Crud.cfxSubmit("mainForm", "prepareAddItem");
    }

    function prepareClone(){

        console.log("==========1=>>>>>>>>>" + $("#itemCheckbox").val());
        console.log("==========2=>>>>>>>>>" +document.getElementsByName("itemCheckbox").values());


        SOP.Crud.cfxSubmit("mainForm", "viewCloneData");
    }

    function prepareEditItem(id){
        SOP.Crud.cfxSubmit("mainForm", "prepareEditItem", id);
    }

    function doCancel(){
        SOP.Crud.cfxSubmit("mainForm","doCancel");
    }
</script>