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
    <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.HcsaRiskIndividualConfigDelegator"/>
    <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.HcsaInspectionValidateDto"/>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">



            <div class="row">
                <div class="col-xs-12">
                    <div class="instruction-content center-content">
                        <h2>Individual Compliance Risk Configuration</h2>
                            <div class="table-gp">
                                <table aria-describedby="" class="table">
                                    <thead>
                                    <tr>
                                        <th scope="col" width="8%">Service Name</th>
                                        <th scope="col" width="15%">Impact of Non Compliance (NC) on Patient Safety</th>
                                        <th scope="col" >Effective Start Date</th>
                                        <th scope="col" >Effective End Date</th>
                                        <th scope="col" width="8%">Minimum Number of NCs</th>
                                        <th scope="col" width="8%">Maximum Number of NCs</th>
                                        <th scope="col" >Compliance Risk</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <span class="error-msg" id="error_All" name="iaisErrorMsg"></span>
                                    <c:forEach var="fin" items="${inShowDto.inspectionDtoList}" varStatus="status">
                                        <tr>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${fin.caEdit}">
                                                        <p><strong>${fin.serviceName}</strong></p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p>${fin.serviceName}</p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <input type="hidden" id="<c:out value="${fin.svcCode}"></c:out>calevel" name="<c:out value="${fin.svcCode}"></c:out>calevel" value="C">
                                                <p>Critical</p>
                                            </td>
                                            <td><iais:datePicker id = "${fin.svcCode}castartdate" name = "${fin.svcCode}castartdate" value="${fin.doCaEffectiveDate}"></iais:datePicker>
                                            </td>
                                            <td><iais:datePicker id = "${fin.svcCode}caenddate" name = "${fin.svcCode}caenddate" value="${fin.doCaEndDate}"></iais:datePicker>
                                            </td>
                                            <td>
                                                <div><div style="width: 100px;">
                                                </div><div style="width: 100px;float: left">
                                                    <input type="text" disabled readonly maxlength="3" value="0" style="border-color: white;">
                                                </div>
                                                </div>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" style="color: <c:if test="${fin.doCaLeftModCountherr}">red</c:if>" id="<c:out value="${fin.svcCode}"/>caleftmod" name = "<c:out value="${fin.svcCode}"/>caleftmod" maxlength="3"value="${fin.doCaLeftModCounth == null ? "" : fin.doCaLeftModCounth}" onchange="doChangeMc('${fin.svcCode}','C',true)">
                                                </div>
                                                </div>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" style="border-color: white;color: <c:if test="${fin.doCaLeftHighCountherr}">red</c:if>" readonly id="<c:out value="${fin.svcCode}"/>calefthigh" name = "<c:out value="${fin.svcCode}"/>calefthigh" maxlength="3"value="${fin.doCaLeftHighCounth == null ? 1 : fin.doCaLeftHighCounth }">
                                                </div>
                                                </div>
                                            </td>
                                            <td>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text"  style="border-color: white;color: <c:if test="${fin.doCaRightLowCountherr}">red</c:if>" readonly id="<c:out value="${fin.svcCode}"/>carightlow" name = "<c:out value="${fin.svcCode}"/>carightlow"  maxlength="3" value="${fin.doCaRightLowCounth  == null ? 0 : fin.doCaRightLowCounth}">
                                                </div>

                                                </div>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" style="color: <c:if test="${fin.doCaRightModCountherr}">red</c:if>" id="<c:out value="${fin.svcCode}"/>carightmod" name = "<c:out value="${fin.svcCode}"/>carightmod"  maxlength="3"value="${fin.doCaRightModCounth  == null ? "" : fin.doCaRightModCounth}" onchange="doChangeMc('${fin.svcCode}','C',false)">
                                                </div>

                                                </div>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" disabled readonly  maxlength="3" value="999" style="border-color: white;">
                                                </div></div>
                                            </td>
                                            <td>
                                                <div style="width: 100px;margin-top: 15px;">Low</div>
                                                <div style="width: 100px;margin-top: 45px;">Moderate</div>
                                                <div style="width: 100px;margin-top: 45px;">High</div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td  colspan="7">
                                            <c:set value = "error_${fin.svcCode}caEffDate" var = "caEffdate"/>
                                            <span class="error-msg" id="<c:out value="${caEffdate}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${fin.svcCode}caEndDate" var = "caEnddate"/>
                                            <span class="error-msg" id="<c:out value="${caEnddate}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${fin.svcCode}caLeftModCaseCounth" var = "caleftmod"/>
                                            <span class="error-msg" id="<c:out value="${caleftmod}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${fin.svcCode}caLeftHighCaseCounth" var = "calefthigh"/>
                                            <span class="error-msg" id="<c:out value="${calefthigh}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${fin.svcCode}caRightLowCaseCounth" var = "carightlow"/>
                                            <span class="error-msg" id="<c:out value="${carightlow}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${fin.svcCode}caRightModCaseCounth" var = "carightmod"/>
                                            <span class="error-msg" id="<c:out value="${carightmod}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${fin.mjEdit}">
                                                        <p><strong>${fin.serviceName}</strong></p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p>${fin.serviceName}</p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <input type="hidden" id="<c:out value="${fin.svcCode}"></c:out>mjlevel" name="<c:out value="${fin.svcCode}"></c:out>mjlevel" value="SOURCE002">
                                                <p>Major</p>

                                            </td>
                                            <td><iais:datePicker id = "${fin.svcCode}mjstartdate" name = "${fin.svcCode}mjstartdate" value="${fin.doMjEffectiveDate}"></iais:datePicker>

                                            </td>
                                            <td><iais:datePicker id = "${fin.svcCode}mjenddate" name = "${fin.svcCode}mjenddate" value="${fin.doMjEndDate}"></iais:datePicker>
                                            </td>
                                            <td>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" disabled readonly maxlength="3" value="0" style="border-color: white;">
                                                </div></div>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" style="color: <c:if test="${fin.doMjLeftModCountherr}">red</c:if>" id="<c:out value="${fin.svcCode}"/>mjleftmod" name = "<c:out value="${fin.svcCode}"/>mjleftmod"  maxlength="3"value="${fin.doMjLeftModCounth == null ? "" : fin.doMjLeftModCounth}" onchange="doChangeMc('${fin.svcCode}','Ma',true)">
                                                </div>

                                                </div>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" readonly  style="border-color: white;color: <c:if test="${fin.doMjLeftHighCountherr}">red</c:if>" id="<c:out value="${fin.svcCode}"/>mjlefthigh" name = "<c:out value="${fin.svcCode}"/>mjlefthigh"  maxlength="3"value="${fin.doMjLeftHighCounth == null ? 1 : fin.doMjLeftHighCounth}">
                                                </div>

                                                </div>
                                            </td>
                                            <td>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" style="border-color: white;color: <c:if test="${fin.doMjRightLowCountherr}">red</c:if>" readonly  id="<c:out value="${fin.svcCode}"/>mjrightlow" name = "<c:out value="${fin.svcCode}"/>mjrightlow" maxlength="3" value="${fin.doMjRightLowCounth == null ? 0 :fin.doMjRightLowCounth}">
                                                </div>

                                                </div>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" style="color: <c:if test="${fin.doMjRightModCountherr}">red</c:if>" id="<c:out value="${fin.svcCode}"/>mjrightlmod" name = "<c:out value="${fin.svcCode}"/>mjrightmod" maxlength="3"value="${fin.doMjRightModCounth == null ? "" : fin.doMjRightModCounth}" onchange="doChangeMc('${fin.svcCode}','Ma',false)">
                                                </div>

                                                </div>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" disabled readonly maxlength="3" value="999" style="border-color: white;">
                                                </div></div>
                                            </td>
                                            <td>
                                                <div style="width: 100px;margin-top: 15px;">Low</div>
                                                <div style="width: 100px;margin-top: 45px;">Moderate</div>
                                                <div style="width: 100px;margin-top: 45px;">High</div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td  colspan="7">
                                            <c:set value = "error_${fin.svcCode}mjEffDate" var = "mjEffDate"/>
                                            <span class="error-msg" id="<c:out value="${mjEffDate}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${fin.svcCode}mjEndDate" var = "mjEndDate"/>
                                            <span class="error-msg" id="<c:out value="${mjEndDate}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${fin.svcCode}mjRightLowCaseCounth" var = "mjrightlow"/>
                                            <span class="error-msg" id="<c:out value="${mjrightlow}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${fin.svcCode}mjLeftModCaseCounth" var = "mjleftmod"/>
                                            <span class="error-msg" id="<c:out value="${mjleftmod}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${fin.svcCode}mjRightModCaseCounth" var = "mjrightmod"/>
                                            <span class="error-msg" id="<c:out value="${mjrightmod}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${fin.svcCode}mjLeftHighCaseCounth" var = "mjlefthigh"/>
                                            <span class="error-msg" id="<c:out value="${mjlefthigh}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${fin.miEdit}">
                                                        <p><strong>${fin.serviceName}</strong></p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p>${fin.serviceName}</p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <input type="hidden" id="<c:out value="${fin.svcCode}"></c:out>milevel" name="<c:out value="${fin.svcCode}"></c:out>milevel" value="SOURCE002">
                                                <p>Minor</p>
                                            </td>
                                            <td><iais:datePicker id = "${fin.svcCode}mistartdate" name = "${fin.svcCode}mistartdate" value="${fin.doMiEffectiveDate}"></iais:datePicker>
                                            </td>
                                            <td><iais:datePicker id = "${fin.svcCode}mienddate" name = "${fin.svcCode}mienddate" value="${fin.doMiEndDate}"></iais:datePicker>
                                            </td>
                                            <td>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" disabled readonly maxlength="3" value="0" style="border-color: white;">
                                                </div></div>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" style="color: <c:if test="${fin.doMiLeftModCountherr}">red</c:if>" id="<c:out value="${fin.svcCode}"/>mileftmod" name = "<c:out value="${fin.svcCode}"/>mileftmod"  maxlength="3"value="${fin.doMiLeftModCounth == null ? "" : fin.doMiLeftModCounth}" onchange="doChangeMc('${fin.svcCode}','Mi',true)">
                                                </div>

                                                </div>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" style="border-color: white;color: <c:if test="${fin.doMiLeftHighCountherr}">red</c:if>" readonly  id="<c:out value="${fin.svcCode}"/>milefthigh" name = "<c:out value="${fin.svcCode}"/>milefthigh"  maxlength="3"value="${fin.doMiLeftHighCounth == null ? 1 : fin.doMiLeftHighCounth}">
                                                </div>

                                                </div>
                                            </td>
                                            <td>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" style="border-color: white;color: <c:if test="${fin.doMiRightLowCountherr}">red</c:if>"  readonly id="<c:out value="${fin.svcCode}"/>mirightlow" name = "<c:out value="${fin.svcCode}"/>mirightlow" maxlength="3" value="${fin.doMiRightLowCounth == null ? 0 :fin.doMiRightLowCounth}">
                                                </div>

                                                </div>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" style="color: <c:if test="${fin.doMiRightModCountherr}">red</c:if>" id="<c:out value="${fin.svcCode}"/>mirightlmod" name = "<c:out value="${fin.svcCode}"/>mirightmod" maxlength="3"value="${fin.doMiRightModCounth == null ? "": fin.doMiRightModCounth}" onchange="doChangeMc('${fin.svcCode}','Mi',false)" >
                                                </div>

                                                </div>
                                                <div><div style="width: 100px;"></div><div style="width: 100px;float: left">
                                                    <input type="text" disabled readonly value="999" style="border-color: white;">
                                                </div></div>
                                            </td>
                                            <td>
                                                <div style="width: 100px;margin-top: 15px;">Low</div>
                                                <div style="width: 100px;margin-top: 45px;">Moderate</div>
                                                <div style="width: 100px;margin-top: 45px;">High</div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td  colspan="7">
                                            <c:set value = "error_${fin.svcCode}miEffDate" var = "miEffDate"/>
                                            <span class="error-msg" id="<c:out value="${miEffDate}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${fin.svcCode}miEndDate" var = "miEndDate"/>
                                            <span class="error-msg" id="<c:out value="${miEndDate}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${fin.svcCode}miRightLowCaseCounth" var = "mirightlow"/>
                                            <span class="error-msg" id="<c:out value="${mirightlow}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${fin.svcCode}miLeftModCaseCounth" var = "mileftmod"/>
                                            <span class="error-msg" id="<c:out value="${mileftmod}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${fin.svcCode}miRightModCaseCounth" var = "mirightmod"/>
                                            <span class="error-msg" id="<c:out value="${mirightmod}"/>" name="iaisErrorMsg"></span>
                                            <c:set value = "error_${fin.svcCode}miLeftHighCaseCounth" var = "milefthigh"/>
                                            <span class="error-msg" id="<c:out value="${milefthigh}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                        </tr>
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
<%@ include file="RiskModerateChange.jsp" %>
<script type="text/javascript">
    function doNext() {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm","next");
    }

    function doBack(){
        SOP.Crud.cfxSubmit("mainForm","backToMenu");
    }
    function  doChangeMc(svcCode,name,left) {
        var  moId = "#"+svcCode;
        var id = "#"+svcCode;
      if("C" == name){
          if(left){
              moId +="caleftmod";
              id  += "carightlow";
          }else {
              moId +="carightmod";
              id  += "calefthigh";
          }
          doChangeVal( moId,id,left);
      }else if ("Ma" == name){
          if(left){
              moId +="mjleftmod";
              id  += "mjrightlow";
          }else {
              moId +="mjrightlmod";
              id  += "mjlefthigh";
          }
          doChangeVal( moId,id,left);
      }else if("Mi" == name){
          if(left){
              moId +="mileftmod";
              id  += "mirightlow";
          }else {
              moId +="mirightlmod";
              id  += "milefthigh";
          }
          doChangeVal( moId,id,left);
      }

    }


</script>
