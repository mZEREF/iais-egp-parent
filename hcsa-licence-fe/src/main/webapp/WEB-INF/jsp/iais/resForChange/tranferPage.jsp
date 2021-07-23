        <%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
            <%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
            <%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
            <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
                <%
        sop.webflow.rt.api.BaseProcessClass process =
                (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
    %>
            <webui:setLayout name="iais-internet"/>
            <br/>
            <%@include file="../common/dashboard.jsp" %>
             <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
                <div class="row">
                    <div class="container">
                 <div class="col-xs-12">
                         <iais:section title="">
                                 <iais:row>
                                     <iais:field width="7" value="Licence No."></iais:field>
                                     <iais:value width="10"><p>${prepareTranfer.licenceNo}</p></iais:value>
                                 </iais:row>
                                 <iais:row>
                                     <iais:field width="7" value="Service Name"></iais:field>
                                     <iais:value width="10"><p>${prepareTranfer.serviceName}</p></iais:value>
                                 </iais:row>
                           <c:if test="${prepareTranfer.groupLic}">
                             <iais:row>
                               <iais:field value="Select Mode of Service Delivery" mandatory="true"></iais:field>
                               <iais:value width="10">
                                 <p>
                                 <c:forEach items="${prepareTranfer.appGrpPremisesDtoList}" var="premises">
                                   <div class="form-check">
                                     <input class="form-check-input" id="premisesInput" type="checkbox" name="premisesInput" aria-invalid="false" value="${premises.premisesIndexNo}">
                                     <label class="form-check-label" for="premisesInput"><span class="check-square"></span>${premises.tranferSelect} </label>
                                   </div>
                                 </c:forEach>
                                 <span  class="error-msg" name="iaisErrorMsg" id="error_premisesError"></span>
                                 </p>
                               </iais:value>
                             </iais:row>
                           </c:if>
                                 <iais:row>
                                   <label class="col-xs-11 col-md-4 control-label">UEN of Licensee to transfer licence to <span style="color: red"> *</span>
                                     <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"
                                        title='Please refer to <a>www.uen.org</a> for the UEN of the licensee'
                                        style="z-index: 10"
                                        data-original-title="">i</a>
                                   </label>
                                     <iais:value width="10">
                                       <p>
                                        <input type="text" id ="uen" name="UEN" value="${UEN}" maxlength="10">
                                       <span  class="error-msg" name="iaisErrorMsg" id="error_uenError"></span>
                                       </p>
                                     </iais:value>
                                 </iais:row>
                                 <iais:row id = "uen">
                                       <iais:field width="5" mandatory="true" value="Please select the licensee to transfer to"/>
                                       <iais:value width="7" id = "uenDiv" cssClass="col-md-6 col-xs-6 other-charges-type-div">
                                         <iais:select name="subLicensee" firstOption="Please Select"
                                                       value=""/>
                                       </iais:value>
                                 </iais:row>

                         </iais:section>
                  </div>
                </div>
                 </div>
                 <div class="row">
                     <div class="container">
                         <div class="col-xs-12 col-md-6 text-left">
                             <a class="back" href="/hcsa-licence-web/eservice/INTERNET/MohRequestForChange?licenceId=<iais:mask name="licenceId" value="${prepareTranfer.licenceId}"/>&AmendTypeValue=0"><em class="fa fa-angle-left"></em> Back</a>
                         </div>
                         <div class="col-xs-12 col-md-6 text-right">
                             <a class="btn btn-primary next premiseId" id="Next" href="javascript:void(0);">Next</a>
                         </div>
                     </div>
                 </div>
               <br/>
               <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
                </form>




        <script>
            $(document).ready(function(){
                $("input[name='premisesInput']").each(function(){
                    if('${selectCheakboxs}'.indexOf($(this).val()) != -1){
                        $(this).prop("checked",true);
                    }
                });
            });
            $("#Next").click(function () {
                showWaiting();
                document.getElementById("mainForm").submit();
            });
            function  showWaiting(){
                $.blockUI({message: '<div style="padding:3px;">We are processing your request now; please do not click the Back or Refresh button in the browser.</div>',
                    css: {width: '25%', border: '1px solid #aaa'},
                    overlayCSS: {opacity: 0.2}});
            }

         function checkUEN(uen){
                alert(uen);
         }

            $(document).ready(function () {
                $("#uen").change(function () {
                    var uen = $("#uen").val();
                    var data = {
                        'uen':uen
                    };
                    showWaiting();
                    $.ajax({
                        'url':'${pageContext.request.contextPath}/check-uen',
                        'dataType':'json',
                        'data':data,
                        'type':'POST',
                        'success':function (data) {
                            if('200' == data.resCode){
                                $("#uenDiv").html(data.resultJson + '');
                                $("#subLicensee").niceSelect();
                            }
                        },
                        'error':function () {

                        }
                    });
                    dismissWaiting();
                })
            });

        </script>