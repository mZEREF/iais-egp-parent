<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<footer class="footerlogin">
    <div class="container">
        <div class="row">
            <div class="col-xs-12 col-md-12 ">
                <div class="footer-link text-right">
                    <ul class="list-inline">
                        <%--                        <li><a href="javascript:void(0);" onclick="popup('<iais:code code="MRUS011"/>')">Rate This E-Service</a></li>--%>
                        <li><a href="javascript:void(0);" onclick="footPopup(this, '<iais:code code="MRUS023"/>')">Contact Us</a></li>
                        <li><a href="javascript:void(0);" onclick="footPopup(this, '<iais:code code="MRUS021"/>')">Feedback</a></li>
                        <li><a href="javascript:void(0);" onclick="footPopup(this, '<iais:code code="MRUS013"/>')">Share your views @ Reach</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <hr style="margin-top: 0; margin-bottom: 0" />
        <div class="row">
<%--            <div class="col-xs-12" style="margin-bottom: 5px;">--%>
<%--                <span style="color: #9a9a9a"> Best viewed using the current and previous release of Chrome and Safari  <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" title="" data-original-title="Chrome 87.0.4280.88 (Latest),<br> Chrome 86.0.4240.75 (2nd Latest),<br> Firefox 83.0 (Latest),<br> Firefox 82.0.3 (2nd Latest),<br> IE 11.0.19041.0 (Latest), <br>IE 11.0.18362.997 (2nd Latest),<br> Edge 87.0.664.57 (Latest),<br> Edge 87.0.664.55 (2nd Latest),<br> Safari 14.0.1 (Latest),<br> Safari 14.0 (2nd Latest)"><em  class="fa fa-question-circle"></em></a></span>--%>
<%--            </div>--%>
            <div class="col-xs-12 col-md-12">
                <div class="footer-link">
                    <ul class="list-inline">
                        <li><a href="javascript:void(0);" onclick="footPopup(this, '<iais:code code="MRUS019"/>')">HALP</a></li>
                        <li><a href="javascript:void(0);" onclick="footPopup( this, '<iais:code code="MRUS008"/>')">Who we are</a></li>
                        <li><a href="javascript:void(0);" onclick="openWins(this, '<iais:code code="MRUS009"/>')">Privacy Statement</a></li>
                        <li><a href="javascript:void(0);" onclick="openWins(this, '<iais:code code="MRUS010"/>')">Terms Of Use</a></li>
<%--                        <li><a href="javascript:void(0);" onclick="popup('<iais:code code="MRUS011"/>')">Rate This E-Service</a></li>--%>
<%--                        <li><a href="javascript:void(0);" onclick="popup('<iais:code code="MRUS012"/>')">Sitemap</a></li>--%>
                        <li><a href="javascript:void(0);" onclick="footPopup(this, '<iais:code code="RELURL001"/>')">About HCSA</a></li>
                        <li><a href="javascript:void(0);" onclick="footPopup(this, '<iais:code code="MRUS020"/>')">Report vulnerability</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-md-12 ">
                <div class="copyright">
                    <p class="text-right">	&copy; <span class="year">2020</span>  Government Of Singapore. Last Updated 01 Jul 2021.</p>
                </div>
            </div>
        </div>
    </div>
</footer>

<script type="text/javascript">
    function openWins(obj, section){
        var url ='${pageContext.request.contextPath}/eservice/INTERNET/'+section;
        window.open(url,'_blank');
        obj.style.color = '#ff6600';
    }

    function linkWins(section) {
        var url ='${pageContext.request.contextPath}/eservice/INTERNET/'+section;
        window.location.href= url;
    }

    function footPopup(obj, url) {
        popup(url);
        obj.style.color = '#ff6600';
    }
</script>

