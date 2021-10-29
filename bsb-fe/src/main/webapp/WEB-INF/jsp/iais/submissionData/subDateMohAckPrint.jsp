<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.ParamUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.StringUtil" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<%
    String webroot1=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT;
    String noContainCLB = ParamUtil.getRequestString(request,"noContainCLB");
    String msg = "Thank you for your submission.";
    if(!StringUtil.isEmpty(noContainCLB)){
        msg = noContainCLB;
    }

%>
<webui:setLayout name="iais-blank"/>

<div class="main-content">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="center-content">
                    <span style="font-size:2rem;"><%=msg%></span>
                </div>
            </div>
        </div>
    </div>
</div>
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