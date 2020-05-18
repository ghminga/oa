<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>报销任务办理详情</title>

    <!-- Bootstrap -->
    <link href="bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="css/content.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="http://cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="http://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <script src="js/jquery.min.js"></script>
    <script src="bootstrap/js/bootstrap.min.js"></script>
</head>
<body>

<!--路径导航-->
<ol class="breadcrumb breadcrumb_nav">
    <li>首页</li>
    <li>报销管理</li>
    <li class="active">我的报销单</li>
</ol>
<!--路径导航-->

<div class="page-content">
    <div class="panel panel-default">
        <div class="panel-heading">报销任务办理详情</div>
        <div class="panel-body">
            <form action="submitTask" method="post">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-md-8">
                            <div class="form-group">
                                <label>标题</label>
								<input type="text" class="form-control" id="title" readonly="readonly" name="title" value="${baoxiaoBill.title}">
                            </div>
                            <div class="form-group">
                                <label for="col_name">金额</label>
                                <input type="text" class="form-control" id="money" name="money" readonly="readonly" value="${baoxiaoBill.money}">
                            </div>
                            <div class="form-group">
                                <label for="seo_title">申请事由</label>
                                <textarea class="form-control" rows="5" cols="10" id="remark" readonly="readonly" name="remark">${baoxiaoBill.remark}</textarea>
                            </div>
                        </div>
                </div>                
            </form>
        </div>
    </div>

     <div class="panel panel-default">
            <div class="panel-heading">报销批注信息列表</div>
            
            <div class="table-responsive">
               <c:if test="${fn:length(commentList)>0}">
	                <table class="table table-striped table-hover">
	                    <thead>
	                    <tr>
	                        <th width="15%">时间</th>
	                        <th width="10%">批注人</th>
	                        <th width="75%">批注信息</th>
	                    </tr>
	                    </thead>
	                    <tbody>
						<c:forEach var="comment" items="${commentList}">
		                    <tr>
		                        <td>
		                        	<fmt:formatDate value="${comment.time}" pattern="yyyy-MM-dd HH:mm:ss"/>
		                        </td>
		                        <td>${comment.userId}</td>
		                        <td>
		                        	${comment.fullMessage}
		                        </td>
		                    </tr>
						</c:forEach>
	                    </tbody>
	                </table>
                </c:if>
                <c:if test="${fn:length(commentList)==0}">
                	<table class="table table-striped table-hover">
                		<tr>
                			<td>暂无批注信息</td>
                		</tr>
                	</table>
                </c:if>
            </div>
        </div>
</div>



</body>
</html>