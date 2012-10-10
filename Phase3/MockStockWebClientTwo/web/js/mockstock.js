/**
 * author: kenny lienhard
 * js mockstock user
 */


$(document).ready(function(){
    
    $("#trader div.stockproduct").last().css('border-bottom', 'none');
    
//    $('p.errorform').each(function(index) {
//        if($(this).html()) {
//            $(this).css('padding', '8px');  
//        }
//    });
    
    $('p.errorhome').each(function(index) {
        if($(this).html()) {
            $(this).css('padding', '8px');  
        }
    });
    
    $('#marketproducts input[type=text]').click(function() {
  $(this).focus().val("");
});
    
    //set active link
    var span = $('<span/>');
    span.html('› ');
    span.addClass('active');

    var path = window.location.pathname.split('/');
    var pathname = path[path.length-1];
    pathname = pathname.replace('.xhtml', '');

    if (pathname === 'analytics') {
        $('#header a.nav').eq(0).prepend(span);
    } else if (pathname === 'settings') {
        $('#header input[type=submit]').eq(1).attr('value','› Settings');
    } else {
        $('#header a.nav').eq(1).prepend(span);
    }
    
    if ($('#analytics'.length == 1)) {
        $('#analytics').fadeIn(1000);
    }
    
    
    if ($('#transactions li').length <= 3) {
        $('.commands input[type=submit]').first().attr('value','› Last 3');
    } else {
        $('.commands input[type=submit]').last().attr('value','› All');
    }
    
    getPrices();
    
    setInterval(function () {
        getPrices();
    }
    , 6000);
    
    $('#gettweets').load('../tweets.xhtml');
    
    
    function getPrices() {
        $('#marketprices').load('marketprices.xhtml', null, function() {
            $('#marketprices').fadeIn(450);
        })
    }


});
