<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Hello World!</title>
</head>
<body>
Hello ${name}!
<br/><br/>

使用#list 遍历list集合<br/>
学生的个数:${stus?size}
<table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>钱包</td>
        <td>生日</td>
    </tr>
    <#list stus as stu>
        <tr>
            <td>${stu_index+1}</td>
            <td <#if stu.name == '小明'>style="color: red" </#if>>${stu.name}</td>
            <td>${stu.age}</td>
            <td <#if stu.money gt 300>style="color: aqua" </#if>>${stu.money}</td>
            <td>${(stu.birthday?string("YYYY年MM月DD日"))!""}</td>
        </tr>
    </#list>
</table>
<br/><br/>

遍历Map数据，一：使用“map['key'].属性”的方法，二：使用“map.key.属性”的方法
<br/>
姓名：${(stuMap['stu1'].name)!"没有使用缺省值"}<br/>
年龄：${(stuMap['stu1'].age)!"缺省"}<br/>
姓名：${stuMap.stu2.name}<br/>
年龄：${stuMap.stu2.age}<br/>
遍历Map的key，使用"#list stuMap?keys as k"
<br/>
<#list stuMap?keys as k>
姓名：${stuMap[k].name}<br/>
年龄：${stuMap[k].age}<br/>
</#list>
</body>
</html>
