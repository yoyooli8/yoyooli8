var conf = 1; //控制服务
var srvMap = (function(){
    //var srcPref = ["../data/","../hn/action/"];
    var srcPref = ["../data/","http://localhost/testweb/"];
    var dataArray = [
         {

         },
         {

         }
    ];
    
    return {
        add: function(uid, mockSrc, srvSrc) {
            dataArray[0][uid] = srcPref[conf] + mockSrc;
            dataArray[1][uid] = srcPref[conf] + '' + srvSrc;         
        },
        get: function(uid) {
            return dataArray[conf][uid];
        },
        getBaseUrl: function() {
            return srcPref[conf];
        },
        dataArrays:function(){
            return dataArray[conf];
        }
    };
})(jQuery);
window.dataArray = srvMap.dataArrays();
