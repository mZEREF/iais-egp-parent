<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
  String webrootDS=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT;
%>
<webui:setLayout name="iais-internet"/>

<c:set var="title" value="Data Submission" />

<%@ include file="assistedReproduction/common/arHeader.jsp" %>

<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
  <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
  <div class="row">
    <div class="container">
      <div class="col-xs-12">
        <h3>Please select the submission you wish to make</h3>
      </div>
    </div>
  </div>
  <div class="row" style=" height : 450px">
        <div class="container">
            <div class="col-xs-12">

                <div  class="u29" onclick="submit('AR')">
                  <div  class="u29_div" >
                  </div>
                  <div class="u29_text ">
                    <p><span style="text-decoration:none;">Assisted Reproduction</span></p>
                  </div>
                </div>
              <div id="u30" class="ax_default image" onclick="submit('AR')">
                <img id="u30_img" class="img " src="<%=webrootDS%>img/AR.jpg">
                <div id="u30_text" class="text " style="display:none; visibility: hidden">
                  <p></p>
                </div>
              </div>

              <div id="u17" class="ax_default box_1 disabled" onclick="submit('DP')">
                <div id="u17_div" class="disabled"></div>
                <div id="u17_text" class="text ">
                  <p><span style="text-decoration:none;">Drug Practices</span></p>
                </div>
              </div>
              <div id="u20" class="ax_default ellipse" onclick="submit('DP')">
                <img id="u20_img" class="img " src="<%=webrootDS%>img/DP.jpg">
                <div id="u20_text" class="text " style="display:none; visibility: hidden">
                  <p></p>
                </div>
              </div>

              <div id="u18" class="ax_default box_1" onclick="submit('LDT')">
                <div id="u18_div" class=""></div>
                <div id="u18_text" class="text ">
                  <p><span style="text-decoration:none;">Laboratory Developed Test</span></p>
                </div>
              </div>
              <div id="u19" class="ax_default icon" onclick="submit('LDT')">
                <img id="u19_img" class="img " src="<%=webrootDS%>img/LDT.jpg">
                <div id="u19_text" class="text " style="display:none; visibility: hidden">
                  <p></p>
                </div>
              </div>

              <div id="u16" class="ax_default box_1" onclick="submit('TP')">
                <div id="u16_div" class=""></div>
                <div id="u16_text" class="text ">
                  <p><span style="text-decoration:none;">Termination of Pregnancy</span></p>
                </div>
              </div>
              <div id="u24" class="ax_default image" onclick="submit('TP')">
                <img id="u24_img" class="img " src="<%=webrootDS%>img/TP.jpg">
                <div id="u24_text" class="text " style="display:none; visibility: hidden">
                  <p></p>
                </div>
              </div>

              <div id="u14" class="ax_default box_1" onclick="submit('VS')">
                <div id="u14_div" class=""></div>
                <div id="u14_text" class="text ">
                  <p><span style="text-decoration:none;">Voluntary Sterilisation</span></p>
                </div>
              </div>
              <div id="u15" class="ax_default icon" onclick="submit('VS')">
                <img id="u15_img" class="img " src="<%=webrootDS%>img/VS.jpg">
                <div id="u15_text" class="text " style="display:none; visibility: hidden">
                  <p></p>
                </div>
              </div>

            </div>
        </div>
  </div>
  <div class="row">
    <div class="container">
        <div class="col-xs-12">
        <div class="application-tab-footer">
          <div class="col-xs-12 col-sm-4 col-md-2 text-left">
            <a style="padding-left: 5px;" class="back" id="backBtn" href="/main-web">
              <em class="fa fa-angle-left">&nbsp;</em> Back
            </a>
          </div>
        </div>
      </div>
    </div>
  </div>


</form>

<link rel="stylesheet" href="<%=webrootDS%>css/data_submission.css">
