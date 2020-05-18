<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>后台首页</title>

    <!-- Bootstrap -->
    <link href="bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="http://cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="http://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <script src="js/jquery.min.js"></script>
    <script src="bootstrap/js/bootstrap.min.js"></script>
    <script src="js/hardphp.js"></script>
</head>
<body>

    <div class="myheading">
        <nav class="navbar navbar-inner">
            <div class="container-fluid">

                <div class="navbar-header">
                    <!--nav troggle-->
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#hard-navbar">
                        <span class="sr-only">导航切换</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <!--nav troggle-->
                    <!--brand-->
                    <a class="navbar-brand" href="#">办公自动系统</a>
                    <!--brand-->
                </div>

                <!--nav links-->
                <div class="collapse navbar-collapse" id="hard-navbar">

                    <ul class="nav navbar-nav navbar-right">
                        <li><a href="aa.html" class="atip" target="appiframe" data-toggle="tooltip" data-placement="bottom" data-title="首页"><span class="glyphicon glyphicon-home" aria-hidden="true"></span></a></li>
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">
                                <span class="glyphicon glyphicon-sunglasses" aria-hidden="true"></span> ${activeUser.username}
                            </a>
                            <ul class="dropdown-menu dropdown-menu-arrow-right" role="menu">
                                <li><a href="#"><span class="glyphicon glyphicon-edit" ></span> 修改资料</a></li>
                                <li><a href="logout"><span class="glyphicon glyphicon-log-out" ></span> 退出登录</a></li>
                            </ul>
                        </li>
                    </ul>
                </div>
                <!--nav links-->
            </div>
        </nav>
    </div>

    <!--main-->
    <div class="container-fluid mybody">
        <div class="main-wapper">
            <!--菜单-->
            <div id="siderbar" class="siderbar-wapper">

                <div class="panel-group menu" id="accordion" role="tablist" aria-multiselectable="true">

                    <div class="panel panel-inner">
                        <div class="panel-heading panel-heading-self" role="tab" id="headingOne">
                            <h4 class="panel-title">
                                <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                                    <span class="glyphicon glyphicon-list-alt"></span> 报销管理
                                </a>
                            </h4>
                        </div>
                        <div id="collapseOne" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingOne">
                            <div class="list-group list-group-self">
                                <a href="apply_leave.jsp" target="appiframe" class="list-group-item"><span class="glyphicon glyphicon-menu-right"></span>报销申请</a>
                                <a href="myBaoxiaoBill" target="appiframe" class="list-group-item"><span class="glyphicon glyphicon-menu-right"></span>我的报销单</a>
                                <a href="myTaskList" target="appiframe" class="list-group-item"><span class="glyphicon glyphicon-menu-right"></span>我的待办事务</a>
                            </div>
                        </div>
                    </div>

                    <div class="panel panel-inner">
                        <div class="panel-heading panel-heading-self" role="tab" id="headingSeven">
                            <h4 class="panel-title">
                                <a class="collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapseSeven" aria-expanded="false" aria-controls="collapseSeven">
                                    <span class="glyphicon glyphicon-th-large"></span> 流程管理
                                </a>
                            </h4>
                        </div>
                        <div id="collapseSeven" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingSeven">
                            <div class="list-group list-group-self">
                                <a href="add_process.jsp"  target="appiframe" class="list-group-item"><span class="glyphicon glyphicon-menu-right"></span>发布流程</a>
                                <a href="processDefinitionList"  target="appiframe" class="list-group-item"><span class="glyphicon glyphicon-menu-right"></span>查看流程</a>
                            </div>
                        </div>
                    </div>

                    <div class="panel panel-inner">
                        <div class="panel-heading panel-heading-self" role="tab" id="headingTwo">
                            <h4 class="panel-title">
                                <a class="collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
                                    <span class="glyphicon glyphicon-th"></span> 用户管理
                                </a>
                            </h4>
                        </div>
                        <div id="collapseTwo" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingTwo">
                            <div class="list-group list-group-self">
                                <a href="findUserList"  target="appiframe" class="list-group-item"><span class="glyphicon glyphicon-menu-right"></span> 用户列表</a>
                            </div>
                        </div>
                    </div>

                </div>

            </div>
            <!--菜单-->
            <!--内容-->
            <div id="content" class="main-content">

                <iframe src="welcome.html" style="width:100%;height: 100%;" id="appiframe" name="appiframe" frameborder="0"></iframe>

            </div>
            <!--内容-->
        </div>

    </div>

    <!--main-->

    <script type="text/javascript">
        $(function(){
            var options={
                animation:true,
                trigger:'hover' //触发tooltip的事件
            }
            $('.atip').tooltip(options);

        });



    </script>
</body>
</html>