<%--
  Created by IntelliJ IDEA.
  User: JiaHao_Chen
  Date: 2019/11/13
  Time: 16:29
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<webui:setLayout name="iais-intranet"/>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<div class="main-content">
<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.HcsaRiskLegislativeConfigDelegator"/>
    <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.HcsaRiskFinianceVadlidateDto"/>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">


            <div class="row">
                <div class="col-xs-12">
                    <div class="instruction-content center-content">
                        <h2>Risk Weightage Configuration</h2>
                            <div class="table-gp">
                                <table aria-describedby="" class="table">
                                    <thead>
                                    <tr>
                                        <th scope="col" >Service Name</th>
                                        <th scope="col" >Last Inspection</th>
                                        <th scope="col" >Second Last Inspection</th>
                                        <th scope="col" >Financial Scheme Audit</th>
                                        <th scope="col" >Leadership And Governance</th>
                                        <th scope="col" >Legislative Breaches</th>
                                        <th scope="col" width="15%">Effective Start Date</th>
                                        <th scope="col" width="15%">Effective End Date</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <span class="error-msg" id="error_All" name="iaisErrorMsg"></span>
                                    <c:forEach var="leg" items="${wightageDto.weightageDtoList}" varStatus="status">
                                        <tr>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${leg.edit}">
                                                        <p><strong>${leg.serviceName}</strong></p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p>${leg.serviceName}</p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <input type="text" id="<c:out value="${leg.serviceCode}"/>last" name="<c:out value="${leg.serviceCode}"/>last"value="<c:out value="${leg.doLastInp}"></c:out>" maxlength="4"  oninput="doChangeValFor(this)">
                                            </td>
                                            <td>
                                                <input type="text" id="<c:out value="${leg.serviceCode}"/>secLast" name="<c:out value="${leg.serviceCode}"/>secLast"value="<c:out value="${leg.doSecLastInp}"></c:out>" maxlength="4" oninput="doChangeValFor(this)">
                                            </td>
                                            <td>
                                                <input type="text" id="<c:out value="${leg.serviceCode}"/>fin" name="<c:out value="${leg.serviceCode}"/>fin"value="<c:out value="${leg.doFinancial}"></c:out>" maxlength="4" oninput="doChangeValFor(this)">
                                            </td>

                                            <td>
                                                <input type="text" id="<c:out value="${leg.serviceCode}"/>lea" name="<c:out value="${leg.serviceCode}"/>lea"value="<c:out value="${leg.doLeadship}"></c:out>" maxlength="4" oninput="doChangeValFor(this)">
                                            </td>
                                            <td>
                                                <input type="text" id="<c:out value="${leg.serviceCode}"/>leg" name="<c:out value="${leg.serviceCode}"/>leg"value="<c:out value="${leg.doLegislative}"></c:out>" maxlength="4" oninput="doChangeValFor(this)">
                                            </td>
                                            <td>
                                                <iais:datePicker id = "${leg.serviceCode}instartdate" name = "${leg.serviceCode}instartdate" value="${leg.doEffectiveDate}"></iais:datePicker>
                                            </td>
                                            <td>
                                                <iais:datePicker id = "${leg.serviceCode}inenddate" name = "${leg.serviceCode}inenddate" value="${leg.doEndDate}"></iais:datePicker>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="8">
                                                <c:set value = "error_${leg.serviceCode}lastInp" var = "lastInp"/>
                                                <span class="error-msg" id="<c:out value="${lastInp}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${leg.serviceCode}secLastInp" var = "secLastInp"/>
                                                <span class="error-msg" id="<c:out value="${secLastInp}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${leg.serviceCode}fin" var = "fin"/>
                                                <span class="error-msg" id="<c:out value="${fin}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${leg.serviceCode}lea" var = "lea"/>
                                                <span class="error-msg" id="<c:out value="${lea}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${leg.serviceCode}leg" var = "legis"/>
                                                <span class="error-msg" id="<c:out value="${legis}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${leg.serviceCode}inEffDate" var = "inEffDate"/>
                                                <span class="error-msg" id="<c:out value="${inEffDate}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${leg.serviceCode}inEndDate" var = "inEndDate"/>
                                                <span class="error-msg" id="<c:out value="${inEndDate}"/>" name="iaisErrorMsg"></span>
                                                <c:set value = "error_${leg.serviceCode}totalw" var = "totalw"/>
                                                <span class="error-msg" id="<c:out value="${totalw}"/>" name="iaisErrorMsg"></span>
                                        </td></tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                                <div class="table-footnote">
                                </div>
                            </div>
                        <div>
                          <c:if test="${backButtonNeed == 'Y'}">
                            <div style="float:left"> <span><a href="javascript:void(0);" onclick="javascript:doBack()"><em class="fa fa-angle-left"></em> Back</a></span></div>
                          </c:if>
                            <div style="float:right">
                                <button class="btn btn-primary next" type="button" onclick="javascript:doNext();">Submit</button>
                            </div>
                        </div>
                        </div>

                </div>
            </div>



</form>
</div>
<%@ include file="validationForRisk.jsp" %>
<script type="text/javascript">
    function doNext() {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm","next");
    }
    function doBack(){
        SOP.Crud.cfxSubmit("mainForm","backToMenu");
    }

    function doChangeValFor(obj) {
        if( $(obj).val() == null || $(obj).val() == "")
            return;
        var isInput = false;
        var max = 1;
        var input = "";
        if($(obj).val().length == 1 &&($(obj).val() == 1 || $(obj).val() ==0)){
            isInput = true;
        }else if($(obj).val().length == 2 && ($(obj).val().indexOf(".") == 1)){
            isInput = true;
        }else if(($(obj).val().length == 3 ) && ($(obj).val().indexOf(".") == 1)){
         try {
             var s = $(obj).val().split(".");
             if(s.length != 2){
                 $(obj).val(input);
                 return;
             }
             var isN1 = isNaN(s[0]);
             var isN2 = isNaN(s[1]);
             var num =  parseInt(s[0]);
             var num1 =  parseInt(s[1]);
             if( !isN1 && num > 1){
                 isInput = false;
             }else if( (!isN1 && num == 1) && (!isN2 && num1 >0)){
                 isInput = false;
                 input = max;
             }else if( !isN1 && !isN2){
                 isInput = true;
                 $(obj).val((isNaN(num) ? "" : num) +"."+ (isNaN(num1)? "" : num1 ));
             }
         }catch (e) {
             isInput = false;
         }
        }else if($(obj).val().length == 4 && ($(obj).val().indexOf(".") == 1)){
            try {
                var s = $(obj).val().split(".");
                if(s.length != 2){
                    $(obj).val(input);
                    return;
                }
                var isN1 = isNaN(s[0]);
                var isN2 = isNaN(s[1]);
                var num =  parseInt(s[0]);
                var num1 =  parseInt(s[1]);
                if( !isN1 && num > 1){
                    isInput = false;
                }else if( (!isN1 && num == 1) && (!isN2 && num1 >0)){
                    isInput = false;
                    max = 1;
                }else if( !isN1 && !isN2){
                    isInput = true;
                    $(obj).val((isNaN(num) ? "" : num) +"."+ (isNaN(num1)? "" : ( s[1].indexOf("0") == 0 ? "0" + num1 : num1)));
                }
            }catch (e) {
                isInput = false;
            }
        }
        if(!isInput){
            $(obj).val( input);
        }
    }

    function isNaN(arg) {
            if (arg.trim == ""){
                return true;
            }
            argChar = "0123456789";
            for(var i = 0;i<arg.length;i++){
            if(argChar.indexOf(arg.substring(i, i + 1)) == -1) {
                return true;
            }
           }
          return false;
    }
</script>
