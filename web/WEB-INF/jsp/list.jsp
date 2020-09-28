<%-- index.jsp: 게시글들의 목록을 보여줍니다. --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <!-- Bootstrap CDN -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
          integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous"/>

    <!-- CSS File -->
    <link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/index.css"/>

    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
    <title>인하대 인터넷 프로그래밍 프로젝트</title>
</head>
<body>

<!-- Top Navigation Bar -->
<nav id="navbar" class="navbar navbar-expand-md navbar-dark bg-dark sticky-top">
    <!-- Brand -->
    <a class="navbar-brand" href="#">인터넷 프로그래밍 과제</a>

    <!-- Toggler Button -->
    <button
            class="navbar-toggler"
            type="button"
            data-toggle="collapse"
            data-target="#navbar-content"
    >
        <span class="navbar-toggler-icon"></span>
    </button>

    <!-- Navbar Content -->
    <div id="navbar-content" class="collapse navbar-collapse align-content-stretch justify-content-between">
        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link" href="#">자유 게시판</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#">유머 게시판</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#">정보 게시판</a>
            </li>
        </ul>

        <ul class="nav navbar-nav flex-row justify-content-md-start flex-nowrap">
            <li>
                <a href="login" class="btn btn-success">로그인</a>
            </li>
            <!--
              <li>
                <a class="nav-link" href="#">Username</a>
              </li>
              <li>
                <a href="/logout.do" class="btn btn-danger">로그아웃</a>
              </li>
            -->
        </ul>
    </div>
</nav>

<!-- Side Navigation Bar -->
<div id="sidebar" class="col-fixed navbar navbar-dark bg-dark px-2">
    <div>
        <a class="navbar-brand" href="#">인터넷 프로그래밍 과제</a>
        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link" href="#">자유 게시판</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#">유머 게시판</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#">정보 게시판</a>
            </li>
        </ul>
    </div>

    <div class="navbar-nav">
        <!--
          <a href="/logout.do" class="btn btn-danger sidebar-btn">
          로그인
          </a>
        -->

        <a class="nav-link sidebar-btn" href="#">Username</a>
        <a href="/logout.do" class="btn btn-danger sidebar-btn">
            로그아웃
        </a>
    </div>
</div>

<!-- Content -->
<div id="content" class="container">
    <!-- Welcome Jumbotron: 로그인하지 않은 경우에만 출력할 것 -->
    <div id="welcome" class="jumbotron">
        <h1 class="display-4">Welcome!</h1>
        <p class="lead-welcome">
            이 사이트는 인하대 인터넷 프로그래밍 과제를 위해 만들어진 샘플용
            사이트입니다.
        </p>
        <hr/>
        <p>
            이 사이트에서 글이나 댓글을 쓰기 위해서는 계정이 필요합니다. 계정이
            있으시다면 로그인해주시고, 없으시다면 아래 버튼을 눌러 회원가입을 해
            주세요.
        </p>
        <div class="welcome-btn-wrapper">
            <a class="btn btn-primary mr-2" href="#" role="button">회원가입</a>
            <a id="welcome-close" class="btn btn-danger" href="#" role="button"
            >이 창 닫기</a
            >
        </div>
    </div>

    <br/>

    <!-- Cards -->
    <!-- 무한 스크롤 구현 -->
    <div class="card-wrapper row m-nx-5">
        <div class="card shadow-sm col-sm-6 col-md-4">
            <div class="card-body">
                <h5 class="card-title">멋진 사진</h5>
                <h6 class="card-text">
                    작성자: someniceguy <br/>
                    작성일: 2020-04-05
                </h6>
                <hr/>
                <h6 class="card-text card-info">
                    조회수: 10, 댓글 수: 0, 추천 수: 2
                </h6>
                <div class="card-btn-wrapper">
                    <!-- 수정 버튼: 이 글의 작성자인 경우에만 표시-->
                    <a
                            class="btn btn-outline-primary mr-2"
                            href="/input.html"
                            role="button"
                    >수정</a
                    >
                    <a class="btn btn-outline-info" href="#" role="button">더 보기</a>
                </div>
            </div>
        </div>

        <div class="card shadow-sm col-sm-6 col-md-4">
            <div class="card-body">
                <h5 class="card-title">멋진 사진</h5>
                <h6 class="card-text">
                    작성자: someniceguy <br/>
                    작성일: 2020-04-05
                </h6>
                <hr/>
                <h6 class="card-text card-info">
                    조회수: 10, 댓글 수: 0, 추천 수: 2
                </h6>
                <div class="card-btn-wrapper">
                    <!-- 수정 버튼: 이 글의 작성자인 경우에만 표시-->
                    <a
                            class="btn btn-outline-primary mr-2"
                            href="/input.html"
                            role="button"
                    >수정</a
                    >
                    <a class="btn btn-outline-info" href="#" role="button">더 보기</a>
                </div>
            </div>
        </div>

        <div class="card shadow-sm col-sm-6 col-md-4">
            <div class="card-body">
                <h5 class="card-title">멋진 사진</h5>
                <h6 class="card-text">
                    작성자: someniceguy <br/>
                    작성일: 2020-04-05
                </h6>
                <hr/>
                <h6 class="card-text card-info">
                    조회수: 10, 댓글 수: 0, 추천 수: 2
                </h6>
                <div class="card-btn-wrapper">
                    <!-- 수정 버튼: 이 글의 작성자인 경우에만 표시-->
                    <a
                            class="btn btn-outline-primary mr-2"
                            href="/input.html"
                            role="button"
                    >수정</a
                    >
                    <a class="btn btn-outline-info" href="#" role="button">더 보기</a>
                </div>
            </div>
        </div>
        <div class="card shadow-sm col-sm-6 col-md-4">
            <div class="card-body">
                <h5 class="card-title">멋진 사진</h5>
                <h6 class="card-text">
                    작성자: someniceguy <br/>
                    작성일: 2020-04-05
                </h6>
                <hr/>
                <h6 class="card-text card-info">
                    조회수: 10, 댓글 수: 0, 추천 수: 2
                </h6>
                <div class="card-btn-wrapper">
                    <!-- 수정 버튼: 이 글의 작성자인 경우에만 표시-->
                    <a
                            class="btn btn-outline-primary mr-2"
                            href="/input.html"
                            role="button"
                    >수정</a
                    >
                    <a class="btn btn-outline-info" href="#" role="button">더 보기</a>
                </div>
            </div>
        </div>
        <div class="card shadow-sm col-sm-6 col-md-4">
            <div class="card-body">
                <h5 class="card-title">멋진 사진</h5>
                <h6 class="card-text">
                    작성자: someniceguy <br/>
                    작성일: 2020-04-05
                </h6>
                <hr/>
                <h6 class="card-text card-info">
                    조회수: 10, 댓글 수: 0, 추천 수: 2
                </h6>
                <div class="card-btn-wrapper">
                    <!-- 수정 버튼: 이 글의 작성자인 경우에만 표시-->
                    <a
                            class="btn btn-outline-primary mr-2"
                            href="/input.html"
                            role="button"
                    >수정</a>
                    <a class="btn btn-outline-info" href="#" role="button">더 보기</a>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS CDN-->
<script
        src="https://code.jquery.com/jquery-3.5.1.min.js"
        integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0="
        crossorigin="anonymous"
></script>

<!-- Popper.js CDN -->
<script
        src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
        integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
        crossorigin="anonymous"
></script>

<!-- jQuery CDN -->
<script
        src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"
        integrity="sha384-OgVRvuATP1z7JjHLkuOU7Xw704+h835Lr+6QL9UvYjZE3Ipu6Tp75j7Bh/kR0JKI"
        crossorigin="anonymous"
></script>

<script type="text/javascript">
    $(document).ready(() => {
        $("#welcome-close").click(() => {
            $("#welcome").fadeOut();
        });
    });
</script>
</body>
</html>

