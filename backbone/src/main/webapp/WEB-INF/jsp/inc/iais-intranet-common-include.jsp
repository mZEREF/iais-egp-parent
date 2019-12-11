
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.BE_CSS_ROOT;
    String webRootCommon = IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT;
%>
<meta charset="utf-8">
<meta http-equiv="x-ua-compatible" content="ie=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="keywords" content="">

<%@ include file="/WEB-INF/jsp/inc/iaisscript.jsp" %>

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
<link rel="stylesheet" href="<%=webroot%>css/cpl_intranet_style.css">

<script src="<%=webroot%>js/bootstrap.min.js"></script>
<script src="<%=webroot%>js/particles.js"></script>
<script src="<%=webroot%>js/app.js"></script>
<script src="<%=webroot%>js/lib/stats.js"></script>
<script src="<%=webroot%>js/scrollup.js"></script>
<script src="<%=webroot%>js/anchor.js"></script>
<script src="<%=webroot%>js/mynav.js"></script>
<script src="<%=webroot%>js/navbarscroll.js"></script>
<script src="<%=webroot%>js/dropdown.js"></script>
<script src="<%=webroot%>js/jquery.nice-select.js"></script>
<script src="<%=webroot%>js/swiper.js"></script>
<script src="<%=webroot%>js/jquery.mCustomScrollbar.js"></script>
<script src="<%=webroot%>js/cpl_app.js"></script>
<script src="<%=webroot%>js/cpl_custom_form_script.js"></script>



<script type="text/javascript">
    $(document).ready(function(){
        $('.date_picker').datepicker({
            format:"yyyy-mm-dd"
        });
    });
</script>