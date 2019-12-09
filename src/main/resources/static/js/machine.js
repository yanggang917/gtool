$(function(){
    getmachineList();
    initFormCheck();
});
var ligerDialogDiv = null;
function openmachineDialog(machineId, machineCode, machineName) {
    $("#machineForm").get(0).reset();
    ligerDialogDiv = $.ligerDialog.open({ target: $("#target1") ,height:400,width:500});

    //初始化支付卡选择列表
    // initSel();

    // alert(machineId+machineName+machineCode);
    ////走修改逻辑
    if (machineId != null){
        $("#machineId").val(machineId);
        $("#machineName").val(machineName);
        $("#machineCode").val(machineCode);
        $("#machineName").attr("readonly","readonly");
        $("#machineCode").attr("readonly","readonly");
        $("#tittle_id").html("续期");
    }else {
        //走新增逻辑
        $("#machineName").removeAttr("readonly");
        $("#machineCode").removeAttr("readonly");
        $("#machineId").val("");//清理id
        $("#tittle_id").html("添加机器码");
    }
}

function initSel() {
    var uid = getCookie("UID");
    isNoTLogin(uid);
    var dd = "<select style='width: 178px; height: 21px; ' id=\"payCode\">";

    $.ajax({
        url: "pay/query-list-for-usable?userId="+uid,
        type: "POST",
        cache:false,
        async:false,
        contentType: "application/json",
        success: function(resp){
            if(resp.success){
                var data = resp.retData;
                for(var i=0;i<data.length; i++){
                    var payTypeName;
                    if (data[i].payType ==1){
                        payTypeName = "日卡";
                    }
                    if (data[i].payType ==2) {
                        payTypeName = "周卡";
                    }
                    if (data[i].payType ==3) {
                        payTypeName = "月卡";
                    }

                    dd += "<option value="+data[i].payCode+"> "+data[i].payCode+" -- "+payTypeName+" </option>";
                }
            }
        }
    });

    dd += "</select>";

    $("#payCodeDiv").html(dd);

}

function initFormCheck()
{
    $.metadata.setType("attr", "validate");
    var v = $("form").validate({
        debug: false,
        errorPlacement: function (lable, element)
        {
            if (element.hasClass("l-textarea"))
            {
                element.ligerTip({ content: lable.html(), target: element[0] });
            }
            else if (element.hasClass("l-text-field"))
            {
                element.parent().ligerTip({ content: lable.html(), target: element[0] });
            }
            else
            {
                lable.appendTo(element.parents("td:first").next("td"));
            }
        },
        success: function (lable)
        {
            lable.ligerHideTip();
            lable.remove();
        },
        submitHandler: function ()
        {
            $("form .l-text,.l-textarea").ligerHideTip();
            addMachine();
        }
    });
    $("form").ligerForm();
    $("#l-button-close").click(function ()
    {
        $("input[type='text']").each(function(i,obj){
            $(obj).ligerHideTip();
        });
        ligerDialogDiv.hide();
    });

}

function deletemachine(id){
    $.ligerDialog.confirm('确定要删除此条机器码信息吗？', function (yes) {
        if(yes){
            $.ajax({
                url: "machine/removemachine",
                type: "POST",
                cache:false,
                data: {"machineId":id},
                dataType: "json",
                success: function(resp){
                    if(resp.success){
                        grid.reload();
                        var tip = $.ligerDialog.tip({ title: '提示信息', content: '记录已经删除！',height:150 });
                        setTimeout(function () { tip.close(); }, 2000);
                    }
                },
                error: function(e,r){
                    $.ligerDialog.error("删除机器码出错："+r);
                }
            });
        }
    });
}
var grid;
function getmachineList(){
    var coloModelList = [
        {display:"ID",name:'id',width:360,hide:true},
        {display:"机器名称",name:'machineName',width:200},
        {display:"机器码",name:'machineCode',width:200},
        {display:"创建时间",name:'createTime',width:150},
        {display:"到期时间",name:'endTime',width:150},
        {display:"操作",name:"caozuo",width:350,align:'center',render:function(item,index){
                var btns = "<input type='button' value='续期' class='l-button' style='width:120px' onclick='openmachineDialog("+item.id+",&quot;"+item.machineCode+"&quot;,&quot;"+item.machineName+"&quot;);'>";
                return btns;
            }
        }
    ];

    var uid = getCookie("UID");
    isNoTLogin(uid);
    grid = $("#machineTable").ligerGrid({
        url: "machine/query-list?userId="+uid,
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

///新增机器码或续期。。
function addMachine(){
    var id = $("#machineId").val();
    var uid = getCookie("UID");
    isNoTLogin(uid);

    var machineCode = $("#machineCode").ligerGetTextBoxManager().getValue();
    var machineName = $("#machineName").ligerGetTextBoxManager().getValue();
    var payCode = $("#payCode").ligerGetTextBoxManager().getValue();
    // var payCode = $("#payCode").val();

    if (payCode==null){
        $.ligerDialog.warn("您当前还没有可用的支付码！");
        return;
    }

    var data;
    var url = "";
    if(id!=null && id!=""){
        url = "machine/updateEndTime";
        data = {"machineId":id,"machineCode":machineCode,"payCode":payCode, "userId":uid, "machineName":machineName};
    }else{
        url = "machine/add";
        data = {"machineCode":machineCode,"payCode":payCode, "userId":uid, "machineName":machineName};
    }

    $.ajax({
        url: url,
        type: "POST",
        data: JSON.stringify(data),
        cache:false,
        contentType: "application/json",
        success: function(resp){
            if(resp.success){
                grid.reload();
                var tip = $.ligerDialog.tip({ title: '提示信息', content: '记录已经更新成功！',height:150 });
                setTimeout(function () { tip.close(); }, 5000);
                ligerDialogDiv.hide();
            }else {
                $.ligerDialog.warn(resp.retDesc);
            }
        },
        error: function(e,r){
            $.ligerDialog.error("添加或修改机器码出错："+r);
        }
    });
}



