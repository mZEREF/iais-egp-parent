
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT;
    String webRootCommon = IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT;
%>
<meta charset="utf-8">
<meta http-equiv="x-ua-compatible" content="ie=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="keywords" content="">

<%@ include file="/WEB-INF/jsp/inc/iaisscript.jsp" %>

<link href="<%=webroot%>css/bs4-migrate-master/bs4-migrate-master.css" rel="stylesheet">
<link href="<%=webroot%>css/bootstrap.min.css" rel="stylesheet">
<link href="<%=webroot%>css/custom.css" rel="stylesheet">
<link href="<%=webroot%>css/responsiveindex.css" rel="stylesheet">
<link href="<%=webroot%>css/font-awesome.min.css" rel="stylesheet">
<link href="<%=webroot%>css/nice-select.css" rel="stylesheet">
<link href="<%=webroot%>css/swiper.css" rel="stylesheet">
<link href="<%=webroot%>css/jquery.mCustomScrollbar.css" rel="stylesheet">
<link href="<%=webRootCommon%>css/bootstrap-datepicker-1.9.0-dist/bootstrap-datepicker.min.css" rel="stylesheet">
<link href="<%=webRootCommon%>css/bootstrap-datepicker-1.9.0-dist/bootstrap-datepicker.standalone.min.css" rel="stylesheet">
<link href="<%=webRootCommon%>css/bootstrap-datepicker-1.9.0-dist/bootstrap-datepicker3.min.css" rel="stylesheet">
<link href="<%=webRootCommon%>css/bootstrap-datepicker-1.9.0-dist/bootstrap-datepicker3.standalone.min.css" rel="stylesheet">
<link href="<%=webRootCommon%>css/comm_style.css" rel="stylesheet">
<link rel="stylesheet" href="<%=webroot%>css/cpl_style.css">
<link rel="stylesheet" href="<%=webroot%>css/multipleSelect.css">
<link rel="stylesheet" href="<%=webroot%>css/multiselectdropdown-styles.css">

<script type="text/javascript" src="<%=webroot%>js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=webroot%>js/particles.js"></script>
<script type="text/javascript" src="<%=webroot%>js/app.js"></script>
<script type="text/javascript" src="<%=webroot%>js/lib/stats.js"></script>
<script type="text/javascript" src="<%=webroot%>js/scrollup.js"></script>
<script type="text/javascript" src="<%=webroot%>js/anchor.js"></script>
<script type="text/javascript" src="<%=webroot%>js/mynav.js"></script>
<script type="text/javascript" src="<%=webroot%>js/navbarscroll.js"></script>
<script type="text/javascript" src="<%=webroot%>js/dropdown.js"></script>
<script type="text/javascript" src="<%=webroot%>js/jquery.nice-select.js"></script>
<script type="text/javascript" src="<%=webroot%>js/swiper.js"></script>
<script type="text/javascript" src="<%=webroot%>js/jquery.mCustomScrollbar.js"></script>
<script type="text/javascript" src="<%=webroot%>js/cpl_app.js"></script>
<script type="text/javascript" src="<%=webroot%>js/cpl_custom_form_script.js"></script>
<script type="text/javascript" src="<%=webroot%>js/jquery.multi-select.min.js"></script>
<script type="text/javascript" src="<%=webRootCommon%>js/bootstrap-datepicker-1.9.0-dist/js/bootstrap-datepicker.min.js"></script>
<script type="text/javascript" src="<%=webRootCommon%>js/jquery.blockUI.js"></script>
<script type="text/javascript" src="<%=webRootCommon%>js/utils.js"></script>

<script type="text/javascript">
    var BASE_CONTEXT_PATH = '<%=request.getContextPath()%>';
    $(document).ready(function(){
        $('.date_picker').datepicker({
            format:"dd/mm/yyyy",
            autoclose:true,
            todayHighlight:true,
            orientation:'bottom'
        });
    });
    function popup(url){
        window.open(url,"_blank");
    }
</script>


