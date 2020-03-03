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

<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">

    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="instruction-content center-content">
                        <h2>Audit Inspection</h2>
                        <div class="gray-content-box">
                            <div class="input-group">
                                <div class="ax_default text_area">
                                    <span style="font-size: 18px"><strong>Service Name:</strong></span><br>
                                    <input type="text"  name="svcNmae" id="svcName"/>
                                </div>
                            </div>

                            <div class="input-group">
                                <div class="ax_default text_area">
                                    <span style="font-size: 18px"><strong>Postal Code/Region:</strong></span><br>
                                    <input type="text"  name="postcode" id="postcode"/>
                                </div>
                            </div>


                            <div class="input-group">
                                <div class="ax_default text_area">
                                    <span  style="font-size: 18px"><strong>Last Inspection done before(Start):</strong></span>
                                    <iais:datePicker id="inspectionStartDate" name = "inspectionStartDate"  value=""></iais:datePicker>
                                </div>
                            </div>

                            <div class="input-group">
                                <div class="ax_default text_area">
                                    <span  style="font-size: 18px"><strong>Last Inspection done before(End):</strong></span>
                                    <iais:datePicker id="inspectionEndDate" name = "inspectionEndDate"  value=""></iais:datePicker>
                                </div>
                            </div>


                            <div class="input-group">
                                <div class="ax_default text_area">
                                    <span  style="font-size: 18px"><strong>HCI Code:</strong></span>
                                    <iais:select name="hclCode" options="hclCodeOp" firstOption="Please select" value=""></iais:select>
                                </div>
                            </div>

                            <div class="input-group">
                                <div class="ax_default text_area">
                                    <span  style="font-size: 18px"><strong>Premises Type:</strong></span>
                                    <iais:select name="premType" options="premTypeOp" firstOption="Please select" value=""></iais:select>
                                </div>
                            </div>

                            <div class="input-group">
                                <div class="ax_default text_area">
                                    <span  style="font-size: 18px"><strong>Type of Risk:</strong></span>
                                    <iais:select name="riskType" options="riskTypeOp" firstOption="Please select" value=""></iais:select>
                                </div>
                            </div>

                            <div class="input-group">
                                <div class="ax_default text_area">
                                    <span style="font-size: 18px"><strong>How many candidates to generate::</strong></span><br>
                                    <input type="text"  name="genNum" id="genNum"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="application-tab-footer">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6">
                                <div class="text-right text-center-mobile"><a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: doNext();">Generate Audit List</a></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<script type="text/javascript">
    function doNext() {
        SOP.Crud.cfxSubmit("mainForm","next");
    }

    function doBack(){
        SOP.Crud.cfxSubmit("mainForm","backToMenu");
    }
</script>
