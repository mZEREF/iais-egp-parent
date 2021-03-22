<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form class="form-horizontal" style="margin-left: 1%;margin-right:1%;width: 100%" method="post" id="ChangeStatusForm"
      action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" id="maskRoleId" name="maskRoleId" value="">
    <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
        <br><br><br><br><br><br>
        <h2>Role Management</h2>
        <iais:section title="" id="addRole">
            <iais:row>
                <iais:field value="UserId"/>
                <iais:value width="7">
                    <span style="font-size: 25px">${userIdName}</span>
                </iais:value>
            </iais:row>
            <iais:row>
                <iais:field value="Add Role" required="true"/>
                <div class="col-md-6">
                    <c:forEach items="${assignRoleOption}" var="role">
                        <c:choose>
                            <c:when test="${role=='Approval Officer 1'}">
                                <div class="form-check">
                                    <input class="form-check-input" id="ao1Check" onclick="checkRole()" type="checkbox"
                                           name="assignRoleAo1" value="${role}">
                                    <label class="form-check-label" for="ao1Check"><span
                                            class="check-square"></span></label>
                                    <span style="font-size: 18px">${role}</span>
                                </div>
                                <br/>
                                <div id="ao1" hidden>
                                    <c:forEach items="${ao1GroupOptions}" var="groupId">
                                        <input type="checkbox" value="${groupId.value}" name="ao1GroupSelect" <c:if test="${role eq groupId}">checked</c:if>/><c:out value="${groupId.text}"></c:out><br/>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:when test="${role=='Approval Officer 1 Leader'}">
                                <div class="form-check">
                                    <input class="form-check-input" id="ao1LeadCheck" onclick="checkRole()"
                                           type="checkbox" name="assignRoleAo1Lead" value="${role}">
                                    <label class="form-check-label" for="ao1LeadCheck"><span
                                            class="check-square"></span></label>
                                    <span style="font-size: 18px">${role}</span>
                                </div>
                                <br/>
                                <div id="ao1Lead" hidden>
                                    <c:forEach items="${ao1GroupOptions}" var="groupId">
                                        <input type="checkbox" value="${groupId.value}" name="ao1GroupLeadSelect" <c:if test="${role eq groupId}">checked</c:if>/><c:out value="${groupId.text}"></c:out><br/>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:when test="${role=='Inspector'}">
                                <div class="form-check">
                                    <input class="form-check-input" id="insCheck" onclick="checkRole()" type="checkbox"
                                           name="assignRoleIns" value="${role}">
                                    <label class="form-check-label" for="insCheck"><span
                                            class="check-square"></span></label>
                                    <span style="font-size: 18px">${role}</span>
                                </div>
                                <br/>
                                <div id="ins" hidden>
                                        <%--                                    <iais:multipleSelect name="insGroupSelect" selectValue="" options="insGroupOptions"></iais:multipleSelect>--%>
                                    <c:forEach items="${insGroupOptions}" var="groupId">
                                        <input type="checkbox" value="${groupId.value}" name="insGroupSelect" <c:if test="${role eq groupId}">checked</c:if>/><c:out value="${groupId.text}"></c:out><br/>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:when test="${role=='Inspector Leader'}">
                                <div class="form-check">
                                    <input class="form-check-input" id="insLeadCheck" onclick="checkRole()"
                                           type="checkbox" name="assignRoleInsLead" value="${role}">
                                    <label class="form-check-label" for="insLeadCheck"><span
                                            class="check-square"></span></label>
                                    <span style="font-size: 18px">${role}</span>
                                </div>
                                <br/>
                                <div id="insLead" hidden>
                                    <c:forEach items="${insGroupOptions}" var="groupId">
                                        <input type="checkbox" value="${groupId.value}" name="insGroupLeadSelect" <c:if test="${role eq groupId}">checked</c:if>/><c:out value="${groupId.text}"></c:out><br/>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:when test="${role=='Professional Screening Officer'}">
                                <div class="form-check">
                                    <input class="form-check-input" id="psoCheck" onclick="checkRole()" type="checkbox"
                                           name="assignRolePso" value="${role}">
                                    <label class="form-check-label" for="psoCheck"><span
                                            class="check-square"></span></label>
                                    <span style="font-size: 18px">${role}</span>
                                </div>
                                <br/>
                                <div id="pso" hidden>
                                    <c:forEach items="${psoGroupOptions}" var="groupId">
                                        <input type="checkbox" value="${groupId.value}" name="psoGroupSelect" <c:if test="${role eq groupId}">checked</c:if>/><c:out value="${groupId.text}"></c:out><br/>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:when test="${role=='Professional Screening Officer Leader'}">
                                <div class="form-check">
                                    <input class="form-check-input" id="psoLeadCheck" onclick="checkRole()"
                                           type="checkbox" name="assignRolePsoLead" value="${role}">
                                    <label class="form-check-label" for="psoLeadCheck"><span
                                            class="check-square"></span></label>
                                    <span style="font-size: 18px">${role}</span>
                                </div>
                                <br/>
                                <div id="psoLead" hidden>
                                    <c:forEach items="${psoGroupOptions}" var="groupId">
                                        <input type="checkbox" value="${groupId.value}" name="psoGroupLeadSelect" <c:if test="${role eq groupId}">checked</c:if>/><c:out value="${groupId.text}"></c:out><br/>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="form-check">
                                    <input class="form-check-input" id="roleId" type="checkbox" name="assignRoleOther"
                                           value="${role}">
                                    <label class="form-check-label" for="roleId"><span
                                            class="check-square"></span></label>
                                    <span style="font-size: 18px;font-weight: normal">${role}</span>
                                </div>
                                <br/>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    <span id="error_userId" name="iaisErrorMsg" class="error-msg"></span>
                </div>
            </iais:row>
            <iais:action>
                <a style="margin-left: 86%" class="btn btn-primary" href="#" onclick="submit('doDeactivate')">Add</a>
            </iais:action>
        </iais:section>

        <iais:section title="" id="removeRole">
            <iais:row>
                <iais:field value="Remove Role"/>
                <iais:value width="7">
                    <c:forEach items="${roleNameAndIdMap}" var="role">
                        <div class="form-check">
                            <input class="form-check-input" id="removeRoleId" type="checkbox" name="removeRole"
                                   value="<iais:mask name="maskRoleId" value="${role.value}"/>">
                            <label class="form-check-label" for="removeRoleId"><span
                                    class="check-square"></span></label>
                            <span style="font-size: 18px">${role.key}</span>
                        </div>
                        <div>
                            <c:forEach items="${insLeaderGroupOptionsExist}" var="insLeader">
                                <c:if test="${role.value eq insLeader.value}"><c:out value="${insLeader.text}"/><br/></c:if>
                            </c:forEach>
                            <c:forEach items="${psoLeaderGroupOptionsExist}" var="psoLeader">
                                <c:if test="${role.value eq psoLeader.value}"><c:out value="${psoLeader.text}"/><br/></c:if>
                            </c:forEach>
                            <c:forEach items="${ao1LeaderGroupOptionsExist}" var="ao1Leader">
                                <c:if test="${role.value eq ao1Leader.value}"><c:out value="${ao1Leader.text}"/><br/></c:if>
                            </c:forEach>
                            <c:forEach items="${insGroupOptionsExist}" var="insGroup">
                                <c:if test="${role.value eq insGroup.value}"><c:out value="${insGroup.text}"/><br/></c:if>
                            </c:forEach>
                            <c:forEach items="${psoGroupOptionsExist}" var="psoGroup">
                                <c:if test="${role.value eq psoGroup.value}"><c:out value="${psoGroup.text}"/><br/></c:if>
                            </c:forEach>
                            <c:forEach items="${ao1GroupOptionsExist}" var="ao1Group">
                                <c:if test="${role.value eq ao1Group.value}"><c:out value="${ao1Group.text}"/><br/></c:if>
                            </c:forEach>
                        </div>
                    </c:forEach>
                    <span id="error_roleLeader" name="iaisErrorMsg" class="error-msg"></span>
                </iais:value>
            </iais:row>
            <iais:action>
                <a style="margin-left: 0%" class="back" onclick="submit('back')"><em class="fa fa-angle-left"></em> Back</a>
                <a style="margin-left: 83%" class="btn btn-primary" href="#" onclick="submit('doDeactivate')">Remove</a>
            </iais:action>
        </iais:section>
    </div>
</form>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">
    function submit(action) {
        $("[name='crud_action_type']").val(action);
        $("#ChangeStatusForm").submit();
    }

    function checkRole() {
        if ($('#ao1Check').is(':checked')) {
            $("#ao1").show();
        } else {
            $("#ao1").hide();
        }
        if ($('#ao1LeadCheck').is(':checked')) {
            $("#ao1Lead").show();
        } else {
            $("#ao1Lead").hide();
        }
        if ($('#insCheck').is(':checked')) {
            $("#ins").show();
        } else {
            $("#ins").hide();
        }
        if ($('#insLeadCheck').is(':checked')) {
            $("#insLead").show();
        } else {
            $("#insLead").hide();
        }
        if ($('#psoCheck').is(':checked')) {
            $("#pso").show();
        } else {
            $("#pso").hide();
        }
        if ($('#psoLeadCheck').is(':checked')) {
            $("#psoLead").show();
        } else {
            $("#psoLead").hide();
        }
    }


</script>