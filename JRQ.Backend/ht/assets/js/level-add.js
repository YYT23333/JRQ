function checkRate(input) {
    var re = /^[0-9]+.?[0-9]*$/; //判断字符串是否为数字 //判断正整数 /^[1-9]+[0-9]*]*$/
    var nubmer = document.getElementById(input).value;

    if (!re.test(nubmer)) {
        alert(input+"请输入数字");
        document.getElementById(input).value = "";
        return false;
    }
    return true;
}
document.getElementById("ad").onclick=function() {
    if(checkRate("cardLimit")&&checkRate("price")) {
        var name = $("#name").val();
        var cardLimit = $("#cardLimit").val();
        var price = $("#price").val();
        var url = getUrl();
        $.ajax(
            {
                url: url + "/addLevel",
                data: {
                    name: name,
                    cardLimit: cardLimit,
                    price: price
                },
                success: function (data) {
                    alert("添加成功");
                    window.location.href = "level.html";
                },
                error: function (xhr) {
                    alert('动态页有问题噶！\n\n' + xhr.responseText);
                },
                traditional: true,
            }
        )
    }

}