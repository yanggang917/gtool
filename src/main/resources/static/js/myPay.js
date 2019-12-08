$(function(){
    getpayList();
});

var grid;
function getpayList(){
    var coloModelList = [
        {display:"ID",name:'id',width:360,hide:true},
        {display:"支付码",name:'payCode',width:300},
        {display:"卡类",width:100,render:function(item,index){
                if (item.payType == 1){
                    return "日卡";
                }
                if (item.payType == 2){
                    return "周卡";
                }
                if (item.payType == 3){
                    return "月卡";
                }
            }},
        {display:"时效（天）",name:'dayLength',width:100},
        {display:"生成时间",name:'createTime',width:150},
        {display:"是否使用",name:'isUsed',width:120, render:function(item,index){
                if (item.isUsed == 1){
                    return "<span style='color: red'>已使用</span>";
                }else {
                    return "<span style='color: green'>未使用</span>";
                }
            }},
        {display:"使用时间",name:'useTime',width:150, render:function(item,index){
                if (item.useTime == null){
                    return "--";
                }else {
                    return item.useTime;
                }
            }},
        {display:"机器名称",name:'machineName',width:100, render:function(item,index){
                if (item.machineName == ''){
                    return "--";
                }else {
                    return item.machineName;
                }
            }}


    ];
    var uid = getCookie("UID");
    grid = $("#payTable").ligerGrid({
        url: "pay/query-list?userId="+uid,
        columns: coloModelList,
        pageSize:10,
        newPage:0,
        pageParmName: "page",
        pagesizeParmName: "size",
        dataAction:'server',
        root:"retData",//返回的数据源名称
        record:'total',
        method:"GET",
        //where : f_getWhere(),
        isScroll:false,
        frozen:false,
        allowAdjustColWidth:true,
        width: '100%',
        height:'90%',
        rownumbers:true
    });
}



