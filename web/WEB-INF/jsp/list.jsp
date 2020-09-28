<%-- list.jsp: 게시글들의 목록을 보여줍니다. --%>
<%@ page import="board.post.Post" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setCharacterEncoding("utf-8");
    String userId = (String) session.getAttribute("user.id"); // 현재 세션의 ID

    int board = (int) request.getAttribute("board");
    int curPage = (int) request.getAttribute("curPage");
    boolean validLogin = (boolean) request.getAttribute("validLogin");

    int postCnt = (int) request.getAttribute("postCnt");
    int postCntStart = (int) request.getAttribute("postCntStart");
    List<Post> postList = (List<Post>) request.getAttribute("postList");
%>

<html>
<head>
    <!-- Bootstrap CDN -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
          integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous"/>

    <style>
        #login-error {
            margin: 10px 0px;
        }

        #welcome {
            margin: 10px 0px;
        }

        .lead-welcome {
            font-weight: lighter;
            font-size: 20px;
            line-height: 25px;
        }

        .welcome-btn-wrapper {
            margin: 10px 0;
        }

        .card-wrapper {
            margin: 10px 0;
        }

        .wrapper {
            display: flex;
            flex-direction: column;
        }

        .card-img-top {
            width: 100%;
            height: 20vm;
            object-fit: cover;
        }

        .show {
            opacity: 1;
        }

        .hide {
            opacity: 0;
            transition: opacity 400ms;
        }
    </style>

    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
    <title>인하대 인터넷 프로그래밍 프로젝트</title>
</head>
<body>
<div class="wrapper">
    <!-- Top Navigation Bar -->
    <nav id="navbar" class="navbar navbar-expand-md navbar-light bg-light sticky-top shadow">
        <!-- Brand -->
        <a class="navbar-brand" href="/list">인터넷 프로그래밍 과제</a>

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
                    <a class="nav-link board1" href="/list?board=1">자유 게시판</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link board2" href="/list?board=2">유머 게시판</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link board3" href="/list?board=3">정보 게시판</a>
                </li>
            </ul>

            <ul class="nav navbar-nav">
                <% if (userId == null) { %>
                <li>
                    <a data-toggle="modal" data-target="#login-popup" class="nav-link" href="#"
                       style="color: deepskyblue">로그인</a>
                </li>
                <% } else { %>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown">
                        <%=userId%>
                    </a>
                    <div class="dropdown-menu dropdown-menu-md-right">
                        <a class="dropdown-item" href="/input">글쓰기</a>
                        <a class="dropdown-item"
                           href="/search?searchType=4&searchedWord=<%=URLEncoder.encode(userId, StandardCharsets.UTF_8)%>">
                            작성글 보기
                        </a>
                        <div class="dropdown-divider"></div>
                        <a class="dropdown-item" href="/usermodify">정보 수정</a>
                        <a class="dropdown-item" href="/logout.do" style="color: #ff0000">로그아웃</a>
                    </div>
                </li>
                <% } %>
            </ul>
        </div>
    </nav>

    <!-- Content -->
    <div class="container">
        <% if (!validLogin) { %>
        <!-- 로그인 오류 -->
        <div id="login-error" class="alert alert-danger" role="alert">
            ID나 비밀번호가 일치하지 않습니다.
        </div>
        <% session.setAttribute("validLogin", true); %>
        <% } %>

        <% if (userId == null) { %>
        <!-- Welcome Jumbotron -->
        <div id="welcome" class="jumbotron appear my-4">
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
                <a class="btn btn-primary mr-2" href="register" role="button">회원가입</a>
                <a id="welcome-close" class="btn btn-danger" href="#" role="button">이 창 닫기</a>
            </div>
        </div>
        <% } %>

        <!-- Cards -->
        <div class="row my-2">
            <% for (Post post : postList) { %>
            <div class="card-wrapper col-md-6 col-lg-4">
                <div class="card shadow-sm rounded">
                    <a href="content?idx=<%=post.getIdx()%>">
                        <img src="<%=request.getContextPath()%>/static/img/<%=post.getImg()%>" class="card-img-top"/>
                    </a>
                    <div class="card-body">
                        <h5 class="card-title"><%=post.getTitle()%>
                        </h5>
                        <small class="text-muted">작성자:&nbsp;<%=post.getAuthor()%>
                        </small>
                        <hr/>
                        <p class="card-text card-info">
                            <%
                                String postContent = post.getContent().substring(0, Math.min(post.getContent().length(), 150));
                                if (post.getContent().length() > 150) postContent += "...";
                            %>
                            <%= postContent %>
                        </p>
                    </div>
                    <div class="card-footer">
                        <div class="text-muted">👤:&nbsp;<%=post.getRead_cnt()%>&nbsp;❤:&nbsp;<%=post.getUpvote_cnt()%>&nbsp;💬:&nbsp;<%=post.getReply_cnt()%>
                        </div>
                        <small class="text-muted">작성/수정
                            날짜:&nbsp;<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(post.getUpdated_at())%>
                        </small>
                        <br>
                        <%
                            int postBoardNum = post.getBoard();
                            String postBoard = "";
                            switch (postBoardNum) {
                                case 1:
                                    postBoard = "자유 게시판";
                                    break;
                                case 2:
                                    postBoard = "유머 게시판";
                                    break;
                                case 3:
                                    postBoard = "정보 게시판";
                                    break;
                            }
                        %>
                        <small class="text-muted"><%=postBoard%>
                        </small>
                    </div>
                </div>
            </div>
            <% } %>
        </div>
        <% if (postCnt > 12) { %>
        <nav class="d-flex justify-content-center">
            <ul class="pagination">
                <% if (postCntStart != 1) { %>
                <li class="page-item">
                    <a class="page-link, page-prev" href="list?board=<%=board%>?curPage=<%=postCntStart - 1%>?">이전</a>
                </li>
                <% } %>
                <% for (int i = postCntStart; i <= 1 + (postCnt - 1) / 12 && i <= postCntStart + 9; i++) { %>
                <% if (curPage == i) { %>
                <li class="page-item active">
                    <span class="page-link"><%=i%>
                        <span class="sr-only">(current)</span>
                    </span>
                </li>
                <% } else { %>
                <li class="page-item">
                    <a class="page-link" href="list?board=<%=board%>&curPage=<%=i%>"><%=i%>
                    </a>
                    <% } %>
                </li>
                <% } %>
                <% if (postCntStart + 10 <= 1 + (postCnt - 1) / 12) { %>
                <li class="page-item">
                    <a class="page-link page-next" href="list?board=<%=board%>&curPage=<%=postCntStart + 10%>">다음</a>
                </li>
                <% } %>
            </ul>
        </nav>
        <% } %>

        <!-- 검색창 -->
        <div>
            <form method="GET" action="/search" novalidate>
                <div class="row">
                    <div class="col-3 col-lg-2">
                        <select id="searchType" name="searchType" class="form-control">
                            <option selected value="1">제목 + 내용</option>
                            <option value="2">제목</option>
                            <option value="3">내용</option>
                            <option value="4">작성자</option>
                        </select>
                    </div>
                    <div class="input-group col-9 col-lg-10">
                        <input
                                type="text"
                                class="form-control"
                                id="search"
                                name="searchedWord"
                                maxlength="50"
                                placeholder="검색어 입력"
                                required
                        />
                        <div class="input-group-append">
                            <input id="search-btn" type="submit" class="btn btn-outline-secondary" value="검색"/>
                        </div>
                    </div>
                </div>

            </form>
        </div>
    </div>
</div>

<!-- 로그인 창 (숨겨진 상태) -->
<div class="modal fade" id="login-popup">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <!-- Modal Header -->
            <div class="modal-header">
                <h4 class="modal-title">로그인</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>

            <!-- Modal body -->
            <div class="modal-body">
                <form class="needs-validation" method="POST" action="/login.do" novalidate>
                    <div class="form-group">
                        <label for="id">ID</label>
                        <input
                                type="text"
                                class="form-control"
                                id="id"
                                name="id"
                                maxlength="20"
                                required
                        />
                        <div class="invalid-feedback">
                            ID를 입력해 주세요.
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="pwd">비밀번호</label>
                        <input
                                type="password"
                                class="form-control"
                                id="pwd"
                                name="pwd"
                                maxlength="20"
                                required
                        />
                        <div class="invalid-feedback">
                            비밀번호를 입력해 주세요.
                        </div>
                    </div>
                    <div class="form-group d-flex flex-row-reverse">
                        <input type="submit" class="btn btn-outline-success" value="로그인"/>
                    </div>
                </form>
            </div>

            <!-- Modal footer -->
            <div class="modal-footer d-flex flex-column">
                <a href="register" style="color: #007BFF">계정이 없으신가요? 여기를 눌러 회원가입을 해주세요.</a>
            </div>
        </div>
    </div>
</div>

<!-- jQuery CDN -->
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

<!-- Bootstrap JS CDN-->
<script
        src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"
        integrity="sha384-OgVRvuATP1z7JjHLkuOU7Xw704+h835Lr+6QL9UvYjZE3Ipu6Tp75j7Bh/kR0JKI"
        crossorigin="anonymous"
></script>

<script type="text/javascript">

    <% if (userId == null) { %>
    // Fadeout - Jquery 사용 안함
    let jumbotron = document.querySelector("#welcome");
    document.querySelector("#welcome-close").addEventListener("click", () => {
        jumbotron.classList.add('hide');
        jumbotron.classList.remove('show');
        setTimeout(() => jumbotron.style.display = "none", 500)
    })
    <% } %>

    // 로그인창 입력 확인
    window.addEventListener("load", () => {
        let forms = document.getElementsByClassName("needs-validation");
        Array.prototype.filter.call(forms, (form) => {
            form.addEventListener("submit", (event) => {
                if (form.checkValidity() === false) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add("was-validated");
            }, false);
        });
    }, false);

    // 현재 게시판 하이라이트
    const navbutton = document.querySelectorAll(".board<%=board%>");
    navbutton.forEach((currentValue, currentIndex, listObj) => {
        currentValue.classList.add("active");
    })

    // 검색어 인코딩
    const searchBtn = document.querySelector("#search-btn");
    searchBtn.addEventListener("click", () => {
        let curWord = document.querySelector("#search").value;
        document.querySelector("#search").value = encodeURI(curWord);
    })
</script>
</body>
</html>
