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

<form id="masterCodeEditForm" method="post" action=<%=process.runtime.continueURL()%>>
<input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
<input type="hidden" name="crud_action_type" value="">
<input type="hidden" name="crud_action_value" value="">
<input type="hidden" name="crud_action_additional" value="">

    <iais:body >
        <br/>
        <c:choose>
            <c:when test="${empty MasterCodeDto}">
                <h2>Master Code Create</h2>
            </c:when>
            <c:otherwise>
                <h2>Master Code Edit</h2>
            </c:otherwise>
        </c:choose>
        <iais:error>
            <c:if test = "${not empty errorMap}">
                <div class="error">
                    <c:forEach items="${errorMap}" var="map">
                        ${map.key}  ${map.value} <br/>
                    </c:forEach>
                </div>
            </c:if>
        </iais:error>
        <iais:section title="${MasterCodeEditTile}" id="MasterCodeEdit">
            <iais:row>
                <iais:field value="Code Description" required="true"></iais:field>
                <iais:value width="7">
                    <iais:select name="codeDescription" options="CodeDesOption" firstOption="Please select" value="${MasterCodeDto.codeDescription}" ></iais:select>
                </iais:value>
            </iais:row>
            <iais:row>
                <iais:field value="Status" required="true"></iais:field>
                <iais:value width="7">
                    <iais:select name="Status" options="StatusOption" firstOption="Please select" value="${MasterCodeDto.status}" ></iais:select>
                </iais:value>
            </iais:row>
        </iais:section>

            <iais:action>
                <button type="button" class="btn" onclick="javascript:doEdit(${MasterCodeDto.masterCodeId});">Update</button>
                <button type="button" class="btn" onclick="javascript:doCancel();">Cancel</button>
            </iais:action>
    </iais:body>
</form>


<script type="text/javascript">
msc

    function doEdit(mscId){
        if(confirm('Are sure you want to edit ? ')){
            SOP.Crud.cfxSubmit("masterCodeEditForm", "doEdit", mscId);
        }
    }

    function doCancel(){
        SOP.Crud.cfxSubmit("masterCodeEditForm","cancel");
    }

</script>
