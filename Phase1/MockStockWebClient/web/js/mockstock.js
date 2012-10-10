// JavaScript to call marketprices.jsp: Update UI with new market prices from publisher
// 
// old load function
//$(document).ready(function(){
//    setInterval(function () {
//        $('#marketprices').attr('src', 'marketprices.jsp');        
//    }, 6000);
//});


$(document).ready(function(){
    
    //getPrices();
    
    setInterval(function () {
        getPrices();
    }
    , 6500);
    
    $("a.setstockprices").click(function() {
        getPrices();
    });
    
    function getPrices() {
        $('#marketprices').load('marketprices.jsp', null, function() {
            $('#marketprices').fadeIn(450);
        })
    }

});
