<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="headeranalytics.html" %>
<!-- 
MockStock Analytics Tool.
-->
<div id="content">
    <div id="trader">
        <h2>Market Index</h2>
        <div class="stockproduct">
            <div class="stockinfo">
                <p class="stockindex">Sun</p>
                <iframe id="analyticssun" src="analyticssun.jsp" scrolling="no" style="border-style: none; width: 100%; height: 170px;">
                <p>Your Browser is too dumb to show iframes.</p>
                </iframe>
            </div>
        </div>
        <div class="stockproduct">
            <div class="stockinfo">
                <p class="stockindex">Apple</p>
                <iframe id="analyticsapple" src="analyticsapple.jsp" scrolling="no" style="border-style: none; width: 100%; height: 170px;">
                <p>Your Browser is too dumb to show iframes.</p>
                </iframe>
            </div>
        </div>
        <div class="stockproduct">
            <div class="stockinfo">
                <p class="stockindex">IBM</p>
                <iframe id="analyticsibm" src="analyticsibm.jsp" scrolling="no" style="border-style: none; width: 100%; height: 170px;">
                <p>Your Browser is too dumb to show iframes.</p>
                </iframe>
            </div>
        </div>
    </div>
    <aside id="market">
        <h2>Portfolio: Shares in %</h2>
        <%@ include file="analyticsportfolio.jsp" %>
    </aside>
</div>
    <%@ include file="footer.html" %>