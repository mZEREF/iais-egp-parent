<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.UrlConfig" %>
<%@ page import="com.ecquaria.cloud.helper.ConfigHelper" %>
<%@ page import="com.ecquaria.cloud.moh.iais.dto.LoginContext" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.ParamUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ page import="com.ecquaria.egov.core.agency.Agency" %>
<%
    String webrooth=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT;
    String internetWebSite = UrlConfig.getInstance().getInternetWebSite();
    String internetInbox = UrlConfig.getInstance().getInternetInbox();
//    String googleSearch = ConfigHelper.getString("halp.googlesearch.flag", "Y");
    String currentApp = ConfigHelper.getString("spring.application.name", "Y");
    String enableWogaa = ConfigHelper.getString("wogaa.enable", "N");
    LoginContext loginContext_attr = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
%>
<%--<script async src="https://cse.google.com/cse.js?cx=003171281875156206280:63zw-tveixa"></script>--%>
<%
    if ("Y".equals(enableWogaa)) {
%>
<script src="https://assets.dcube.cloud/scripts/wogaa.js"></script>
<%
    }
%>
<section class="logo">
    <div class="container" style="width: 100% !important; max-width: 100% !important; padding-left: 30px;">
        <div class="row">
            <div class="logo-ssg">
                <a  href="javascript:void(0);" onclick="popup('https://www.gov.sg/')"  rel="noreferrer" style="color:#484848; text-decoration: none">
                    <img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACYAAAAnCAYAAABjYToLAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAcASURBVFhH7Zh7bFN1FMfvq4/bdV1Lt8EYj8FQEIUpxMAfqBM0gkLkJcmUSNSoCAnKQxSVQFRiID4mMAbBBxBFYrIgAQkqb3EB55gJMh6Dscm6Utqtt+3a3tv23us5N7826x5th48/1E9y9rvn/O7W78753fP73VL/8z//VWgy3jKuwuK3OZp6maZoCwlpKJR6XaLUUyfD0qdlbY5qCEXAZG0yA25ZWEP+iGKTXnmHp5gyEqKccuzCiZB4YmuH7zD64wwGeUdIuAiXLjA/2N8jzFEwYhzFKLt5mhlJQhqSqop7gx2Hj0dC55Zn2yYU6XQTdRRtItOUX5EPR1S16nZn05fghsDSCuxrxlgwK1ghGfHDzWAGMAnsJpiwymIvGc5yg8fqDVM6iwyryqWTorQ4k9KmFOYaNHw2pdJL8TpGURyO9THps9GcfnnXrCHbA74n5psty400NdqnyFdBxPE9wcCV5RbrjPEG41S8J0qpoR0B/4LXfO4fwO0A61FcSmHaOtKpX/A0PZGEekWQ5aaZN2+seMDIBx7hTRPvNRpew0yhkFNieHdjNOJ9OjtncTyWThyWplfW2mxNOpouJm6P/CyJdR/7ffvX+NoPrLLaxiy05FTAEykua/d8UGrgS7IYJm8YpxuXzTDSh35hywM8fz+KG2swTK8Nxb5rUiLt8GewrEkwZLxl+rNsToRSms8MHLTuUixSB+vINZTTTSu35a5+xet5BzOJ992m00+eazJP/UjwbkAfxb1hy3kL/4TmdiGlsO/D4uxSp2Nuv5Yrz81xO1e65JiTTCUxkGF1+EHzTOay05K4H2NWli16z2Zfsd7vLdduAnCd2VkmqyEaORr3x7J8CVwm9UAkpbAxBkP+EEZ/DS6rsxTuiIVhsBclcT4abSzW6e14rafpohpRbNEmgAKWu6OEM476XYmdJiFqblb28+t8XmwbGgvNOQ/BgL+flLWUwvJoetHO/rnHHIXFn+PY9UnEDG4LCCenGPkZJNSNe3n9hJqwWENcKodhrVaGGRAv8SiD7m4YbGBG9OOkXWOiqobrI5IAi/zgGSm852g4tO+QGDy03tf++TSXc8NOe/9nsWx4b0hVRCyV9osEjqJzrkYjbcTVKDXyJW5FbsRrG8Pkw5ADpkc/TkphVR2+VwodjXMedrdUTnM7PgGrnNvWWv6kx7meoeljPxUULoyLQo6Gw5fuN5rGE7dXjAxtaVdkbMZxMFuZl3KRz1MPw1WwWrBfwc69brU2NRYOK3vVYtvVubTQUIUD4WA9Pn0kpHEtFmudAn2NuN3wyPJlcplEulKGwdxgDrA22CuHrTDbT1pp9gWc7MwSr3vXh/1yZxFXA/fQ9/3eE6P1+ntIqBv7Qh0XYAiC4ZaWIO0aA7SufLmgaCHPUrUMRQ/Wop2oDPiqZvHmAcVcsgDc2JdlW++CzGKvSlAdDl8YwulK8DSyOSicgRCuQVGbJGQiTDtz5bJcJXGTwM4Ppwf/TJN5HglpYLw+KjVM5k2Pk5AGZnFbJHgxl2WHlt10rYaQBwzXW1L3T7klIS2FxZONNP0pcZNojkUby33CwXf75T7DUnRi8cbjG+x5L3WOI2cj0jlKVpvqo5HdVaK/GUI3wLBxR3E+TtpjD2TrR9gvJxE3AS72FW2eT7bk5S/Crk/CWvwpj6tib37B0s5xBLMFO8gH1ZHQKXCvgOHZLEAsiZSlbB4yxNaTKGRrwLf37X72x7p++DpB+Hq7PX921zhS7vd+BaJwsWNzxVMtPlgorhsphbGyrsenCUsVVBQBtxwS0sCdwE/F2rrGcb1Nv9m6bn2g/Qi4l8CwdJglLF+P57GMFn9XDoZDdQuyLaXETdAci92Yx2ePJm5C0FR3y2ZSPuyHDWB4BktJujWmax80ottZaXzr9ZW1Awdrx5fOgODDPE01mWjavKTNc7xBkbAFYHZawfDJ84El9aveSJcxBV8kyHWCFlbBD+iGIxb1zPO6voGtqxxEYXawR8WzhMIyEoWkEybDllJBrhNMYvS46SbAJ3FB242NcFQ+IMsyZgiPSriWcDvD5pmxoDhp+9jOoL/5RbPlUSPNFJAQPvYOKFe7nWWH4YK/L9pRcdYv1MFU/IkTwLCMCtgtkVYYoI5kuJqRBsP8eLMs4riiTQHftjv1+qGPupwbnWKoQVXV32AKM4X7Xq+vZZmSkbBvpZBQqs9SB3PcgxiA7BnhHEVNdP2+VlDlJhCFZ6vrYNiTVLznz5JpuwhP97RsqhbFNcSn3KqWFPyBpUNReBL5y+hLH/ODuIqqYOBNfC/EEwLEsEFiT+rz4k5HJqWMgyWK7BeD5z0x9ZfTktTiUmOYLXwvhBf1v5a+fneB4D+D31fg2R7L16dvcf4JUGBfMv5vgKL+ABc6C2QpTdaQAAAAAElFTkSuQmCC" alt="...">
                    <span>A Singapore Government Agency Website</span>
                </a>
            </div>
        </div>
    </div>
</section>
<header>
    <div class="container">
        <div class="row">
            <div class="col-xs-12 col-lg-6">
                <div class="logo-img"><a href="<%=internetWebSite%>"><img src="<%=webrooth%>img/moh-logo.svg" alt="Ministry of Health" width="235" height="64"></a>
                    <a href="<%=internetInbox%>"><p class="logo-img"><img src="<%=webrooth%>img/HALP-log.png" alt="HALP" width="235" height="64"></p></a></div>
            </div>
<%--            <div class="col-xs-2 col-lg-6">--%>
<%--                <ul class="list-inline hidden-xs hidden-sm">--%>
<%--                    <li class="site-fontsizer-cont"><a class="decrease-font fontsizer" onclick="zoomin();">A-</a></li>--%>
<%--                    <li class="site-fontsizer-cont"><a class="decrease-font fontsizer" onclick="zoomout();">A+</a></li>--%>
<%--                </ul>--%>
<%--            </div>--%>
            <div class="col-xs-12 col-lg-6 text-right">
                <div class="gcse-search" style="width:50%;float:right;"></div>
                <%
                  if ("main-web".equals(currentApp) && loginContext_attr != null) {
                %>
                <div class="visible-xs visible-sm visible-md"><a class="menu-icon" href="javascript:;"><span class="icon-bar"></span></a></div>
                <%}%>
            </div>
        </div>
    </div>
</header>
<script type="text/javascript">

    var size = 1.0;
    function zoomout() {
        size = size + 0.1;
        set();
    }


    function zoomin() {
        size = size - 0.1;
        set();
    }


    function set() {
        document.body.style.zoom = size;
        document.body.style.cssText += '; -moz-transform: scale(' + size + ');-moz-transform-origin: 0 0; ';     //
    }
</script>

