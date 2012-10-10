/**
 * author: kenny lienhard
 * js mockstock admin
 */


$(document).ready(function(){
    
    $('#marketproducts input[type=text]').click(function() {
  $(this).focus().val("");
});
    
    if ($("#userList").length == 1) {
        $("#userList").tablesorter({
            sortList: [[0,0]],
            headers: { 
                // assign the secound column (we start counting zero) 
                5: { 
                    // disable it by setting the property sorter to false 
                    sorter: false 
                }
            }
        });
    }
    
    //        $('p.erroradmform').each(function(index) {
    //        if($(this).html()) {
    //            $(this).css('padding', '8px');  
    //        }
    //    });
    
    //set active link
    var span = $('<span/>');
    span.html('› ');
    span.addClass('activeadm');

    var path = window.location.pathname.split('/');
    var pathname = path[path.length-1];
    pathname = pathname.replace('.xhtml', '');

    if (pathname === 'adminadd') {
        $('#adminheader ul').eq(1).children().children().eq(1).prepend(span);
    } else if (pathname === 'admintraderlist') {
        $('#adminheader ul').eq(1).children().children().eq(0).prepend(span);
    } else if (pathname === 'adminedit') {
        $('#adminheader ul').eq(1).children().children().eq(0).prepend(span);
    } else if (pathname === 'analytics') {
        $('#adminheader ul').eq(0).children().children().eq(1).prepend(span);
    } else {
        $('#adminheader ul').eq(0).children().children().eq(0).prepend(span);
    }
    
    if ($('#transactions li').length <= 3) {
        $('.commands input[type=submit]').first().attr('value','› Last 3');
    } else {
        $('.commands input[type=submit]').last().attr('value','› All');
    }
    
    if ($('#analytics'.length == 1)) {
        $('#analytics').fadeIn(1000);
    }
    
    getPrices();
    
    setInterval(function () {
        getPrices();
    }
    , 6000);
    
    
    function getPrices() {
        $('#marketprices').load('marketprices.xhtml', null, function() {
            $('#marketprices').fadeIn(450);
        })
    }
    
    $('#gettweets').load('../tweets.xhtml');

});
