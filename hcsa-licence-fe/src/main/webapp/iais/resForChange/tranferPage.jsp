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
                                     <iais:field value="Licence No."></iais:field>
                                     <iais:value width="10"><p>${prepareTranfer.licenceNo}</p></iais:value>
                                 </iais:row>
                                 <iais:row>
                                     <iais:field value="Service Name"></iais:field>
                                     <iais:value width="10"><p>${prepareTranfer.serviceName}</p></iais:value>
                                 </iais:row>
                                 <iais:row>
                                     <iais:field value="Select Premises"></iais:field>
                                     <iais:value width="10">
                                         <p>
                                         <c:forEach items="${prepareTranfer.appGrpPremisesDtoList}"
                                                    var="premises">
                                             <input type="checkbox" name="premisesInput" value="${premises.premisesIndexNo}">${premises.tranferSelect}&nbsp;
                                         </c:forEach>
                                         </p>
                                     </iais:value>
                                 </iais:row>
                                 <iais:row>
                                     <iais:field value="UEN of Licence to transfer licence to"></iais:field>
                                     <iais:value width="10"><input type="text" name="UNID"></iais:value>
                                 </iais:row>
                         </iais:section>
                     <p><span class="error-msg">${ErrorMsg}</span></p>
                  </div>
                </div>
                 </div>
                 <div class="row">
                     <div class="container">
                         <div class="col-xs-12 col-md-6 text-left">
                             <a class="back" href="/hcsa-licence-web/eservice/INTERNET/MohRequestForChange?licenceId=${prepareTranfer.licenceId}"><em class="fa fa-angle-left"></em> Back</a>
                         </div>
                         <div class="col-xs-12 col-md-6 text-right">
                             <a class="btn btn-primary next premiseId" id="Next">Next</a>
                         </div>
                     </div>
                 </div>
                </form>




        <script>
            $(document).ready(function(){

            });
            $("#Next").click(function () {
                showWaiting();
                document.getElementById("mainForm").submit();
            });
            function  showWaiting(){
                $.blockUI({message: '<div style="padding:3px;">We are processing your request now, please do not click the Back or Refresh buttons in the browser.</div>',
                    css: {width: '25%', border: '1px solid #aaa'},
                    overlayCSS: {opacity: 0.2}});
            }
        </script>