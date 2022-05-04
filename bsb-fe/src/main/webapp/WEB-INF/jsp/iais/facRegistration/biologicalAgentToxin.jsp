<%@taglib prefix="fac" tagdir="/WEB-INF/tags/facility" %>
<%--@elvariable id="batInfo" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.BiologicalAgentToxinDto"--%>
<%--@elvariable id="addressTypeOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
<%--@elvariable id="nationalityOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
<%--@elvariable id="activeNodeKey" type="java.lang.String"--%>
<%--@elvariable id="activityTypes" type="java.util.List<java.lang.String>"--%>
<%--@elvariable id="firstScheduleOp" type="java.lang.String"--%>
<%--@elvariable id="scheduleBatMap" type="java.util.Map<java.lang.String, java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>>"--%>
<%--@elvariable id="scheduleOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
<fac:biologicalAgentToxin batInfo="${batInfo}" addressTypeOps="${addressTypeOps}" nationalityOps="${nationalityOps}" activeNodeKey="${activeNodeKey}"
                          activityTypes="${activityTypes}" firstScheduleOp="${firstScheduleOp}" scheduleBatMap="${scheduleBatMap}" scheduleOps="${scheduleOps}">
</fac:biologicalAgentToxin>