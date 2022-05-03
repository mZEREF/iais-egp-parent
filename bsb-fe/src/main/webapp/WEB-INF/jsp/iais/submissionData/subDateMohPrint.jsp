<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
    String webroot1 = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.FE_CSS_ROOT;
%>
<webui:setLayout name="iais-blank"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp">
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div class="row form-horizontal">
                                    <iais:row>
                                        <iais:field value="Name of Laboratory" width="11" required="true"/>
                                        <iais:value width="11">
                                            <iais:select cssClass="hciCodeDropdown" name="hciCode" id="hciCode"  options="personnelOptions" firstOption="Please Select" value="${laboratoryDevelopTestDto.hciCode}"/>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Name of LDT Test" width="11" required="true"/>
                                        <iais:value width="11">
                                            <input type="text" name="ldtTestName" id="ldtTestName"  maxlength="50" value="${laboratoryDevelopTestDto.ldtTestName}" disabled/>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Intended Purpose of Test" width="11" required="true"/>
                                        <iais:value width="11">
                                            <textarea id="intendedPurpose" style="width: 100%;margin-bottom: 15px;" rows="6" name="intendedPurpose"
                                                      maxlength="500" disabled>${laboratoryDevelopTestDto.intendedPurpose}</textarea>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Date LDT was made or will be made available" width="11" required="true"/>
                                        <iais:value width="11">
                                            <iais:datePicker disabled ="true"  id="ldtDate" name="ldtDate" dateVal="${laboratoryDevelopTestDto.ldtDate}"></iais:datePicker>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Person responsible for the test" width="11" required="true"/>
                                        <iais:value width="11">
                                            <input type="text" name="responsePerson" id="responsePerson"  value="${laboratoryDevelopTestDto.responsePerson}" disabled maxlength="66"/>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Designation" width="11" required="true"/>
                                        <iais:value width="11">
                                            <input type="text" name="designation" id="designation" value="${laboratoryDevelopTestDto.designation}" maxlength="20" disabled/>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <label class="col-xs-11 col-md-4 control-label">Status of Test <span style="color: red"> *</span>
                                        </label>
                                        <iais:value width="5">
                                            <input class="" id="testStatus" type="radio" name="testStatus" <c:if test="${laboratoryDevelopTestDto.testStatus == '1'}"> checked="checked"</c:if> aria-invalid="false" value="1" disabled> Active
                                        </iais:value>
                                        <iais:value width="5">
                                            <input class="" id="testStatus" type="radio" name="testStatus" <c:if test="${laboratoryDevelopTestDto.testStatus == '0'}"> checked="checked"</c:if> aria-invalid="false" value="0" disabled> Inactive
                                        </iais:value>
                                        <iais:value width="11" style="padding-top:12px">
                                        </iais:value>

                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Remarks" width="11" required="false"/>
                                        <iais:value width="11">
                                            <textarea id="remarks" style="width: 100%;margin-bottom: 15px;" rows="6" name="remarks"
                                                      maxlength="300" disabled>${laboratoryDevelopTestDto.remarks}</textarea>
                                        </iais:value>
                                    </iais:row>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <input hidden name="id" value="${user.id}">
    <input hidden name="action" id="action" value="">
</form>
<%@include file="/WEB-INF/jsp/include/validation.jsp"%>

<style>
    .mandatory {
        color: rgb(255, 0, 0);
    }

    .prelogin-title{
        padding-left: 90px;
    }

</style>
<script type="text/javascript">
    $(document).ready(function () {
        doPrint();

    });

    var userAgent = navigator.userAgent;
    var isChrome = userAgent.indexOf("Chrome") > -1 && userAgent.indexOf("Safari") > -1;


    var doPrint = function () {
        $('a').prop('disabled',true);
        if(isChrome){
            addPrintListener();
            window.print();
        }else{
            window.print();
            window.close();
        }
    }
    var addPrintListener = function () {
        if (window.matchMedia) {
            var mediaQueryList = window.matchMedia('print');
            mediaQueryList.addListener(function(mql) {
                if (mql.matches) {
                } else {
                    window.close();
                }
            });
        }
    }
</script>