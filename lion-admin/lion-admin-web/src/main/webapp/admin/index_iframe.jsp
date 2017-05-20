﻿<!DOCTYPE html>
<!--
This is a starter template page. Use this page to start your new project from
scratch. This page gets rid of all links and provides the needed markup only.
-->
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>SuperUI前端框架</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.6 -->
    <link rel="stylesheet" href="../content/ui/global/bootstrap/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link href="../content/ui/global/font-awesome/css/font-awesome.css" rel="stylesheet" />
    <!-- Theme style -->
    <link rel="stylesheet" href="../content/adminlte/dist/css/AdminLTE.css">
    <link rel="stylesheet" href="../content/adminlte/dist/css/skins/_all-skins.css">
    <link href="../content/min/css/supershopui.common.min.css" rel="stylesheet" />
    <style type="text/css">
         html {
            overflow: hidden;
        }
    </style>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<!--
BODY TAG OPTIONS:
=================
Apply one or more of the following classes to get the
desired effect
|---------------------------------------------------------|
| SKINS         | skin-blue                               |
|               | skin-black                              |
|               | skin-purple                             |
|               | skin-yellow                             |
|               | skin-red                                |
|               | skin-green                              |
|---------------------------------------------------------|
|LAYOUT OPTIONS | fixed                                   |
|               | layout-boxed                            |
|               | layout-top-nav                          |
|               | sidebar-collapse                        |
|               | sidebar-mini                            |
|---------------------------------------------------------|
-->
<body class="hold-transition skin-blue sidebar-mini fixed">
<div class="wrapper">
<!-- Main Header -->
<header class="main-header">
    <!-- Logo -->
    <a href="" class="logo">
        <!-- mini logo for sidebar mini 50x50 pixels -->
        <span class="logo-mini"><b>S</b>UI</span>
        <!-- logo for regular state and mobile devices -->
        <span class="logo-lg"><b>Super</b>Shop</span>
    </a>
   
    <!-- Header Navbar -->
    <nav class="navbar navbar-static-top" role="navigation">
        <!-- Sidebar toggle button-->
        <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
            <span class="sr-only">切换导航</span>
        </a>
        <div class="collapse navbar-collapse pull-left" id="navbar-collapse">
            <ul class="nav navbar-nav">
                <li><a href="#">平台 <span class="sr-only">(current)</span></a></li>
                <li><a href="#">订单</a></li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">会员 <span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="#">会员积分管理</a></li>
                        <li><a href="#">会员等级管理</a></li>
                        <li><a href="#">会员信息管理</a></li>
                        <li class="divider"></li>
                        <li><a href="#">会员营销管理</a></li>
                        <li class="divider"></li>
                        <li><a href="#">会员优惠券</a></li>
                    </ul>
                </li>
            </ul>
          
        </div>
        <!-- Navbar Right Menu -->
        <div class="navbar-custom-menu">
            <ul class="nav navbar-nav">
                <!-- Messages: style can be found in dropdown.less-->
                <li class="dropdown messages-menu">
                    <!-- Menu toggle button -->
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <i class="fa fa-envelope-o"></i>
                        <span class="label label-success">4</span>
                    </a>
                    <ul class="dropdown-menu">
                        <li class="header">你有4条提醒</li>
                        <li>
                            <!-- inner menu: contains the messages -->
                            <ul class="menu">
                                <li>
                                    <!-- start message -->
                                    <a href="#">
                                        <div class="pull-left">
                                            <!-- User Image -->
                                            <img src="../content/ui/img/photos/girl1.png" class="img-circle" alt="User Image">
                                        </div>
                                        <!-- Message title and timestamp -->
                                        <h4>
                                            新用户注册
                                            <small><i class="fa fa-clock-o"></i> 现在</small>
                                        </h4>
                                        <!-- The message -->
                                        <p>新用户注册!</p>
                                    </a>
                                </li>
                                <!-- end message -->
                            </ul>
                            <!-- /.menu -->
                        </li>
                        <li class="footer"><a href="#">查看所有提醒</a></li>
                    </ul>
                </li>
                <!-- /.messages-menu -->
                <!-- Notifications Menu -->
                <li class="dropdown notifications-menu">
                    <!-- Menu toggle button -->
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <i class="fa fa-bell-o"></i>
                        <span class="label label-warning">10</span>
                    </a>
                    <ul class="dropdown-menu">
                        <li class="header">你有10条消息</li>
                        <li>
                            <!-- Inner Menu: contains the notifications -->
                            <ul class="menu">
                                <li>
                                    <!-- start notification -->
                                    <a href="#">
                                        <i class="fa fa-users text-aqua"></i> 新用户注册!
                                    </a>
                                </li>
                                <!-- end notification -->
                            </ul>
                        </li>
                        <li class="footer"><a href="#">查看所有</a></li>
                    </ul>
                </li>
                <!-- Tasks Menu -->
                <li class="dropdown tasks-menu">
                    <!-- Menu Toggle Button -->
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <i class="fa fa-flag-o"></i>
                        <span class="label label-danger">9</span>
                    </a>
                    <ul class="dropdown-menu">
                        <li class="header">您有9条消息</li>
                        <li>
                            <!-- Inner menu: contains the tasks -->
                            <ul class="menu">
                                <li>
                                    <!-- Task item -->
                                    <a href="#">
                                        <!-- Task title and progress text -->
                                        <h3>
                                            superui第一版发布啦
                                            <small class="pull-right">20%</small>
                                        </h3>
                                        <!-- The progress bar -->
                                        <div class="progress xs">
                                            <!-- Change the css width attribute to simulate progress -->
                                            <div class="progress-bar progress-bar-aqua" style="width: 20%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                                                <span class="sr-only">20% 完成</span>
                                            </div>
                                        </div>
                                    </a>
                                </li>
                                <!-- end task item -->
                            </ul>
                        </li>
                        <li class="footer">
                            <a href="#">查看所有</a>
                        </li>
                    </ul>
                </li>
                <!-- User Account Menu -->
                <li class="dropdown user user-menu">
                    <!-- Menu Toggle Button -->
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <!-- The user image in the navbar-->
                        <img src="../content/ui/img/photos/girl1.png" class="user-image" alt="User Image">
                        <!-- hidden-xs hides the username on small devices so only the image appears. -->
                        <span class="hidden-xs">TZHSWEET</span>
                    </a>
                    <ul class="dropdown-menu">
                        <!-- The user image in the menu -->
                        <li class="user-header">
                            <img src="../content/ui/img/photos/girl1.png" class="img-circle" alt="User Image">
                            <p>
                                TZHSWEET的小妞
                                <small>2016年注册</small>
                            </p>
                        </li>
                        <!-- Menu Body -->
                        <li class="user-body">
                            <div class="row">
                                <div class="col-xs-4 text-center">
                                    <a href="#">个人信息</a>
                                </div>
                                <div class="col-xs-4 text-center">
                                    <a href="#">设置</a>
                                </div>
                                <div class="col-xs-4 text-center">
                                    <a href="#">主题</a>
                                </div>
                            </div>
                            <!-- /.row -->
                        </li>
                        <!-- Menu Footer-->
                        <li class="user-footer">
                            <div class="pull-left">
                                <a href="#" class="btn btn-default btn-flat">个人中心</a>
                            </div>
                            <div class="pull-right">
                                <a href="#" class="btn btn-default btn-flat">退出</a>
                            </div>
                        </li>
                    </ul>
                </li>
                <!-- Control Sidebar Toggle Button -->
                <li>
                    <a href="#" data-toggle="control-sidebar"><i class="fa fa-gears"></i></a>
                </li>
            </ul>
        </div>
    </nav>
</header>
<!-- Left side column. contains the logo and sidebar -->
<aside class="main-sidebar">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
        <!-- Sidebar user panel (optional) -->
        <div class="user-panel">
            <div class="pull-left image">
                <img src="../content/ui/img/photos/girl1.png" class="img-circle" alt="用户头像">
            </div>
            <div class="pull-left info">
                <p>TZHSWEET</p>
                <!-- Status -->
                <a href="#"><i class="fa fa-circle text-success"></i> 在线</a>
            </div>
        </div>
        <!-- search form (Optional) -->
        <form action="#" method="get" class="sidebar-form">
            <div class="input-group">
                <input type="text" name="q" class="form-control" placeholder="Search...">
                <span class="input-group-btn">
                            <button type="submit" name="search" id="search-btn" class="btn btn-flat">
                                <i class="fa fa-search"></i>
                            </button>
                        </span>
            </div>
        </form>
        <!-- /.search form -->
        <!-- Sidebar Menu -->
        <ul class="sidebar-menu">
           
        </ul>
        <!-- /.sidebar-menu -->
    </section>
    <!-- /.sidebar -->
</aside>
<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper"  id="content-wrapper">
    <div class="content-tabs">
        <button class="roll-nav roll-left tabLeft" onclick="scrollTabLeft()">
            <i class="fa fa-backward"></i>
        </button>
        <nav class="page-tabs menuTabs tab-ui-menu" id="tab-menu">
            <div class="page-tabs-content" style="margin-left: 0px;">

            </div>
        </nav>
        <button class="roll-nav roll-right tabRight" onclick="scrollTabRight()">
            <i class="fa fa-forward" style="margin-left: 3px;"></i>
        </button>
        <div class="btn-group roll-nav roll-right">
            <button class="dropdown tabClose" data-toggle="dropdown">
                页签操作<i class="fa fa-caret-down" style="padding-left: 3px;"></i>
            </button>
            <ul class="dropdown-menu dropdown-menu-right" style="min-width: 128px;">
                <li><a class="tabReload" href="javascript:refreshTab();">刷新当前</a></li>
                <li><a class="tabCloseCurrent" href="javascript:closeCurrentTab();">关闭当前</a></li>
                <li><a class="tabCloseAll" href="javascript:closeOtherTabs(true);">全部关闭</a></li>
                <li><a class="tabCloseOther" href="javascript:closeOtherTabs();">除此之外全部关闭</a></li>
            </ul>
        </div>
        <button class="roll-nav roll-right fullscreen" onclick="App.handleFullScreen()"><i class="fa fa-arrows-alt"></i></button>
    </div>
    <div class="content-iframe " style="background-color: #ffffff; ">
        <div class="tab-content " id="tab-content">

        </div>
    </div>
    <!-- /.content -->
</div>
<!-- /.content-wrapper -->
<!-- Main Footer -->
<footer class="main-footer">
    <!-- To the right -->
    <div class="pull-right hidden-xs">
       SuperShop项目组
    </div>
    <!-- Default to the left -->
    版权所有 &copy;tzhsweet 2015-2018&nbsp;&nbsp;&nbsp;&nbsp;   粤ICP备16024545号-1
</footer>
<!-- Control Sidebar -->
<aside class="control-sidebar control-sidebar-dark">
    <!-- Create the tabs -->
    <ul class="nav nav-tabs nav-justified control-sidebar-tabs">
        <li><a href="#control-sidebar-home-tab" data-toggle="tab"><i class="fa fa-home"></i></a></li>
        <li><a href="#control-sidebar-settings-tab" data-toggle="tab"><i class="fa fa-gears"></i></a></li>
    </ul>
    <!-- Tab panes -->
    <div class="tab-content">
        <!-- Home tab content -->
        <div class="tab-pane" id="control-sidebar-home-tab">
            <h3 class="control-sidebar-heading">最近活动</h3>
            <ul class="control-sidebar-menu">
                <li>
                    <a href="javascript:void(0)">
                        <i class="menu-icon fa fa-birthday-cake bg-red"></i>
                        <div class="menu-info">
                            <h4 class="control-sidebar-subheading">Langdon's Birthday</h4>
                            <p>Will be 23 on April 24th</p>
                        </div>
                    </a>
                </li>
                <li>
                    <a href="javascript:void(0)">
                        <i class="menu-icon fa fa-user bg-yellow"></i>
                        <div class="menu-info">
                            <h4 class="control-sidebar-subheading">Frodo Updated His Profile</h4>
                            <p>New phone +1(800)555-1234</p>
                        </div>
                    </a>
                </li>
                <li>
                    <a href="javascript:void(0)">
                        <i class="menu-icon fa fa-envelope-o bg-light-blue"></i>
                        <div class="menu-info">
                            <h4 class="control-sidebar-subheading">Nora Joined Mailing List</h4>
                            <p>nora@example.com</p>
                        </div>
                    </a>
                </li>
                <li>
                    <a href="javascript:void(0)">
                        <i class="menu-icon fa fa-file-code-o bg-green"></i>
                        <div class="menu-info">
                            <h4 class="control-sidebar-subheading">Cron Job 254 Executed</h4>
                            <p>Execution time 5 seconds</p>
                        </div>
                    </a>
                </li>
            </ul>
            <!-- /.control-sidebar-menu -->
            <h3 class="control-sidebar-heading">Tasks Progress</h3>
            <ul class="control-sidebar-menu">
                <li>
                    <a href="javascript:void(0)">
                        <h4 class="control-sidebar-subheading">
                            Custom Template Design
                            <span class="label label-danger pull-right">70%</span>
                        </h4>
                        <div class="progress progress-xxs">
                            <div class="progress-bar progress-bar-danger" style="width: 70%"></div>
                        </div>
                    </a>
                </li>
                <li>
                    <a href="javascript:void(0)">
                        <h4 class="control-sidebar-subheading">
                            Update Resume
                            <span class="label label-success pull-right">95%</span>
                        </h4>
                        <div class="progress progress-xxs">
                            <div class="progress-bar progress-bar-success" style="width: 95%"></div>
                        </div>
                    </a>
                </li>
                <li>
                    <a href="javascript:void(0)">
                        <h4 class="control-sidebar-subheading">
                            Laravel Integration
                            <span class="label label-warning pull-right">50%</span>
                        </h4>
                        <div class="progress progress-xxs">
                            <div class="progress-bar progress-bar-warning" style="width: 50%"></div>
                        </div>
                    </a>
                </li>
                <li>
                    <a href="javascript:void(0)">
                        <h4 class="control-sidebar-subheading">
                            Back End Framework
                            <span class="label label-primary pull-right">68%</span>
                        </h4>
                        <div class="progress progress-xxs">
                            <div class="progress-bar progress-bar-primary" style="width: 68%"></div>
                        </div>
                    </a>
                </li>
            </ul>
            <!-- /.control-sidebar-menu -->
        </div>
        <!-- /.tab-pane -->
        <!-- Stats tab content -->
        <div class="tab-pane" id="control-sidebar-stats-tab">状态面板设置</div>
        <!-- /.tab-pane -->
        <!-- Settings tab content -->
        <div class="tab-pane" id="control-sidebar-settings-tab">
            <form method="post">
                <h3 class="control-sidebar-heading">常规设置</h3>
                <div class="form-group">
                    <label class="control-sidebar-subheading">
                        Report panel usage
                        <input type="checkbox" class="pull-right" checked>
                    </label>
                    <p>
                        Some information about this general settings option
                    </p>
                </div>
                <!-- /.form-group -->
                <div class="form-group">
                    <label class="control-sidebar-subheading">
                        Allow mail redirect
                        <input type="checkbox" class="pull-right" checked>
                    </label>
                    <p>
                        Other sets of options are available
                    </p>
                </div>
                <!-- /.form-group -->
                <div class="form-group">
                    <label class="control-sidebar-subheading">
                        Expose author name in posts
                        <input type="checkbox" class="pull-right" checked>
                    </label>
                    <p>
                        Allow the user to show his name in blog posts
                    </p>
                </div>
                <!-- /.form-group -->
                <h3 class="control-sidebar-heading">Chat Settings</h3>
                <div class="form-group">
                    <label class="control-sidebar-subheading">
                        Show me as online
                        <input type="checkbox" class="pull-right" checked>
                    </label>
                </div>
                <!-- /.form-group -->
                <div class="form-group">
                    <label class="control-sidebar-subheading">
                        Turn off notifications
                        <input type="checkbox" class="pull-right">
                    </label>
                </div>
                <!-- /.form-group -->
                <div class="form-group">
                    <label class="control-sidebar-subheading">
                        Delete chat history
                        <a href="javascript:void(0)" class="text-red pull-right"><i class="fa fa-trash-o"></i></a>
                    </label>
                </div>
                <!-- /.form-group -->
            </form>
        </div>
        <!-- /.tab-pane -->
    </div>
</aside>
<!-- /.control-sidebar -->
<!-- Add the sidebar's background. This div must be placed
immediately after the control sidebar -->
<div class="control-sidebar-bg"></div>
</div>
    <!-- ./wrapper -->
    <!-- REQUIRED JS SCRIPTS -->
    <!-- jQuery 2.2.3 -->
    <script src="../content/ui/global/jQuery/jquery.min.js"></script>
    <!-- Bootstrap 3.3.6 -->
<script src="../content/ui/global/bootstrap/js/bootstrap.min.js"></script>
    <script src="../content/min/js/supershopui.common.js"></script>


<script type="text/javascript">
    $(function() {
        addTabs(({ id: '10008', title: '欢迎页', close: false, url: '../admin/dashboard.html' }));
        App.fixIframeCotent();
        var menus = [
             { id: "10010", text: "我的工作台", isHeader: true },
              {
                  id: "10020",
                  isOpen: false,
                  text: "SuperShopUI",
                  icon: "fa fa-bookmark-o",
                  children: [
                      {
                          id: "10021",
                          text: "页面加载",
                          isOpen: false,
                          icon: "fa fa-circle-o",
                          children: [
                              { id: "10022", text: "iframe加载", url: "../admin/index_iframe.html", targetType: "blank", icon: "fa fa-spinner" },
                              { id: "10023", text: "ajax加载", url: "../admin/index.html", targetType: "blank", icon: "fa fa-refresh" },
                               { id: "10023", text: "原生页面加载", url: "../admin/index_page.html", targetType: "blank", icon: "fa fa-refresh" }

                          ]
                      }
                  ]
              },
             
              {
                  id: "10001",
                  text: "基础UI",
                
                  icon: "fa fa-circle-o",
                  children: [
                       { id: "10004", text: "按钮", url: "../components/buttons.html", targetType: "iframe-tab", icon: "fa fa-square" },
                      { id: "10003", text: "常用组件", url: "../components/general.html", targetType: "iframe-tab", icon: "fa fa-list-alt" },
                     
                      { id: "10012", text: "图标库", url: "../components/icons.html", targetType: "iframe-tab", icon: "fa fa-circle-o" },
                      {
                          id: "10203",
                          text: "表单组件",
                          url: "../forms/general.html",
                          targetType: "iframe-tab",
                          icon: "fa fa-plus-square-o"
                      },
                       {
                           id: "10204",
                           text: "表单扩展组件",
                           url: "../forms/advanced.html",
                           targetType: "iframe-tab",
                           icon: "fa fa-plus-square-o"
                       },
                       { id: "10005", text: "Block UI", url: "../components/blockui.html", targetType: "iframe-tab", icon: "fa fa-spinner" },
                      { id: "10013", text: "sliders组件",tips:5, url: "../components/sliders.html", targetType: "iframe-tab", icon: "fa fa-list-ol" },
                      { id: "10204", text: "switch按钮", targetType: "iframe-tab", url: "../components/bootstrapswitch.html", icon: "fa fa-toggle-on" },
                      { id: "10017", text: "面板", targetType: "iframe-tab", url: "../components/widgets.html", icon: "fa fa-circle-o" }
                  ]
              },
         
              {
                  id: "10202",
                  text: "插件",

                  targetType: "iframe-tab",
                  icon: "fa fa-circle-o",
                  children: [
                       { id: "10026", text: "layer弹出层", targetType: "iframe-tab",  url: "../components/layer.html", icon: "fa fa-circle-o" },
                      { id: "10006", text: "日历选择控件", targetType: "iframe-tab", url: "../component-extend/calendar.html", icon: "fa fa-circle-o" },
                          { id: "10014", text: "时间轴", targetType: "iframe-tab", url: "../component-extend/timeline.html", icon: "fa fa-circle-o" },
                      { id: "10010", text: "页面加载效果", targetType: "iframe-tab", url: "../component-extend/pageprogress.html", icon: "fa fa-circle-o" },
                      { id: "10016", text: "树", targetType: "iframe-tab", url: "../component-extend/jstree.html", icon: "fa fa-circle-o" },
                      //{ id: "10241", text: "下拉框", targetType: "iframe-tab", url: "../component-extend/bootstrap_select.html", icon: "fa fa-minus-square-o" },
                  
                        { id: "10014", text: "日起选择组件", targetType: "iframe-tab", url: "../component-extend/datetimepickers.html", icon: "fa fa-calendar" },
                      { id: "10242", text: "select2下拉框", targetType: "iframe-tab", url: "../component-extend/select2.html", icon: "fa fa-circle-o" },
                      { id: "10205", text: "多选框", targetType: "iframe-tab", url: "../component-extend/bootstraptagsinput.html", icon: " fa fa-check-square-o" },
                      { id: "10206", text: "多文件上传组件", targetType: "iframe-tab", url: "../component-extend/formfileupload.html", icon: "  fa fa-circle-o " }
                  ]

              },
              {
                  id: "10208",
                  text: "表格组件",

                  icon: "fa fa-circle-o",
                  children: [
                      { id: "10211", text: "bootstraptable表格", targetType: "iframe-tab", url: "../tables/basetable.html", icon: "fa fa-table" },
                      { id: "10212", text: "管理表格", targetType: "iframe-tab", url: "../tables/managetable.html", icon: "fa fa-table" },
                        { id: "10213", text: "jqgrid表格", targetType: "iframe-tab", url: "../tables/jqgrid.html", icon: "fa fa-table" }
                  ]
              },
               {
                   id: "10209", text: "通用模板", isOpen: false,  icon: "fa fa-circle-o", children: [
                   { id: "10214", text: "企业站", targetType: "blank", url: "http://www.supermgr.cn", icon: "fa fa-circle-o" }//,
                   //{ id: "10215", text: "微信端", targetType: "blank", url: "../template/test2.html", icon: "fa fa-circle-o" }
                   ]

               },
                
                 {
                     id: "20209", text: "布局", isOpen: false, icon: "fa fa-circle-o", children: [
                     { id: "20214", text: "盒式布局", targetType: "blank", url: "../layout/boxed.html", icon: "fa fa-circle-o" },
                     { id: "20215", text: "自适应布局", targetType: "blank", url: "../layout/fixed.html", icon: "fa fa-circle-o" },
                       { id: "20216", text: "顶部菜单", targetType: "blank", url: "../layout/top-nav.html", icon: "fa fa-circle-o" },
                           { id: "20217", text: "左侧菜单收缩", targetType: "blank", url: "../layout/collapsed-sidebar.html", icon: "fa fa-circle-o" }
                     ]

                 },
                {
                    id: "30209", text: "图表", isOpen: false, icon: "fa fa-circle-o", children: [
                    { id: "30214", text: "chart图表", targetType: "iframe-tab", url: "../charts/chartjs.html", icon: "fa fa-circle-o" },
                    { id: "30215", text: "flot图表", targetType: "iframe-tab", url: "../charts/flot.html", icon: "fa fa-circle-o" },
                      { id: "30216", text: "inline图表", targetType: "iframe-tab", url: "../charts/inline.html", icon: "fa fa-circle-o" },
                          { id: "30217", text: "morris图表", targetType: "iframe-tab", url: "../charts/morris.html", icon: "fa fa-circle-o" }
                    ]

                },
                 {
                     id: "30209", text: "页面实例", isOpen: false, icon: "fa fa-circle-o", children: [
                   {
                       id: "30208",
                       text: "邮件管理器",

                       icon: "fa fa-circle-o",
                       children: [
                           { id: "30211", text: "邮件管理", targetType: "iframe-tab", url: "../pages/mailbox/mailbox.html", icon: "fa fa-table" },
                           { id: "30212", text: "阅读邮件", targetType: "iframe-tab", url: "../pages/mailbox/read-mail.html", icon: "fa fa-table" },
                             { id: "30213", text: "发送邮件", targetType: "iframe-tab", url: "../pages/mailbox/compose.html", icon: "fa fa-table" }
                       ]
                   },
                   {
                       id: "40208",
                       text: "SuperMgr后台Demo",
                       icon: "fa fa-circle-o",
                       targetType: "blank", url: "../pages/supermgr/index.html"
                   }
                     ]

                 }
        ];
        $('.sidebar-menu').sidebarMenu({ data: menus, param: { strUser: 'admin' } });

       
    });
</script>
</body>
</html>
