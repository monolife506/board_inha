<%-- content.jsp: 게시글의 내용을 보여줍니다. --%>

<%@ page import="board.like.Like" %>
<%@ page import="board.post.Post" %>
<%@ page import="board.reply.Reply" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Objects" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String userId = (String) session.getAttribute("user.id"); // 현재 세션의 ID
    Post post = (Post) request.getAttribute("post"); // 현재 표시하는 글
    Like likeStatus = (Like) request.getAttribute("like"); // 현재 유저의 Like 정보

    int curPage = (int) request.getAttribute("curPage");
    int replyCnt = (int) request.getAttribute("replyCnt");
    int replyCntStart = (int) request.getAttribute("replyCntStart");
    List<Reply> replyList = (List<Reply>) request.getAttribute("replyList");
%>

<html>
<head>
    <!-- Bootstrap CDN -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
          integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">

    <style>
        .wrapper {
            display: flex;
            flex-direction: column;
        }

        .card {
            margin: 10px 0px;
        }

        .delete-form, .modify-form, .upvote-form {
            display: inline;
        }

        .noresize {
            resize: none;
        }
    </style>
    <meta charset="utf-8"/>
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
        <div class="card shadow-sm rounded">
            <div class="card-body">
                <h3 class="card-title"><%=post.getTitle()%>
                </h3>
                <a class="text-muted"
                   href="/search?searchType=4&searchedWord=<%=URLEncoder.encode(post.getAuthor(), StandardCharsets.UTF_8)%>">작성자:&nbsp;<%=post.getAuthor()%>
                </a>
                <hr/>
                <!-- 사진  -->
                <div class="photo-group">
                    <div class="main-photo">
                        <a href="<%=request.getContextPath()%>/static/img/<%=post.getImg()%>">
                            <img src="<%=request.getContextPath()%>/static/img/<%=post.getImg()%>" id="main-img"
                                 class="card-img-top" alt="Main Photo"/>
                        </a>
                    </div>
                    <% if (post.getSub_img() != null) { %>
                    <div class="sub-photo">
                        <a href="<%=request.getContextPath()%>/static/img/<%=post.getSub_img()%>">
                            <img src="<%=request.getContextPath()%>/static/img/<%=post.getSub_img()%>" id="sub-img"
                                 class="card-img-top my-2" alt="Sub Photo"/>
                        </a>
                    </div>
                    <% } %>
                </div>
                <hr/>

                <!-- 내용 -->
                <div class="content">
                    <p class="card-text"><%=post.getContent()%>
                    </p>
                </div>


                <% if (Objects.equals(post.getAuthor(), userId) || userId != null) { %>
                <hr/>
                <!-- 수정/삭제 -->
                <% if (Objects.equals(post.getAuthor(), userId)) { %>
                <form class="modify-form" method="post" action="/modify">
                    <input type="hidden" id="modify-idx" name="idx" value="<%=post.getIdx()%>">
                    <input id="modify" type="submit" class="btn btn-outline-info" value="수정">
                </form>
                <form class="delete-form" method="post" action="/delete.do">
                    <input type="hidden" id="delete-idx" name="idx" value="<%=post.getIdx()%>">
                    <input type="hidden" id="author" name="author" value="<%=post.getAuthor()%>">
                    <input id="delete" type="submit" class="btn btn-outline-danger" value="삭제">
                </form>
                <% } %>

                <% if (userId != null) { %>
                <!-- 좋아요 -->
                <%
                    String upvoteValue;
                    if (likeStatus.getToggle()) upvoteValue = "좋아요 취소";
                    else upvoteValue = "좋아요";
                %>
                <form class="upvote-form" method="post" action="/like.do">
                    <input type="hidden" id="like-idx" name="idx" value="<%=post.getIdx()%>">
                    <input type="hidden" id="like-add" name="add" value="<%=!likeStatus.getToggle()%>">
                    <input id="upvote" type="submit" class="btn btn-outline-secondary" value="<%=upvoteValue%>">
                </form>
                <% } %>
                <% } %>
            </div>
            <div class="card-footer">
                <div class="text-muted">👤:&nbsp;<%=post.getRead_cnt()%>&nbsp;❤:&nbsp;<%=post.getUpvote_cnt()%>&nbsp;💬:&nbsp;<%=post.getReply_cnt()%>
                </div>
                <small class="text-muted">작성/수정
                    날짜:&nbsp;<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(post.getUpdated_at())%>
                </small><br/>
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

        <% if (userId != null) { %>
        <!-- 댓글 쓰기 -->
        <div class="card shadow-sm rounded">
            <div class="card-body">
                <form class="needs-validation-reply-add" method="POST" action="/inputreply.do" novalidate>
                    <input type="hidden" name="idx" value="<%=post.getIdx()%>"/>
                    <div class="form-group">
                        <textarea id="scontent-add" class="form-control noresize" name="content" rows="8" required
                                  maxlength="500"></textarea>
                        <small class="form-text text-muted">댓글의 내용을 입력해 주세요.</small>
                    </div>
                    <div class="form-group">
                        <input type="submit" class="btn btn-outline-primary" value="작성"/>
                    </div>
                </form>
            </div>
        </div>
        <% } %>

        <!-- 댓글들 -->
        <% for (Reply reply : replyList) { %>
        <div class="card shadow-sm rounded">
            <div class="card-body">
                <p id="reply<%=reply.getIdx()%>-content" class="card-text card-info m-2"><%=reply.getContent()%>
                </p>
                <% if (reply.getAuthor().equals(userId)) { %>
                <hr/>
                <div>
                    <a type="button" id="reply<%=reply.getIdx()%>" class="reply-edit btn btn-outline-info"
                       data-toggle="modal"
                       data-target="#modify-reply-popup" href="#">수정</a>
                    <form class="delete-form" method="post" action="/deletereply.do">
                        <input type="hidden" name="postIdx" value="<%=post.getIdx()%>">
                        <input type="hidden" name="idx" value="<%=reply.getIdx()%>">
                        <input type="hidden" name="author" value="<%=reply.getAuthor()%>">
                        <input id="delete-reply" type="submit" class="btn btn-outline-danger" value="삭제">
                    </form>
                </div>

                <% } %>
            </div>
            <div class="card-footer">
                <div class="text-muted">작성자: <%=reply.getAuthor()%>
                </div>
                <small class="text-muted">작성/수정
                    날짜:&nbsp;<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(reply.getUpdated_at())%>
                </small>
            </div>
        </div>
        <% } %>

        <!-- 페이지 목록 -->
        <% if (replyCnt > 10) { %>
        <nav class="d-flex justify-content-center">
            <ul class="pagination">
                <% if (replyCntStart != 1) { %>
                <li class="page-item">
                    <a class="page-link, page-prev"
                       href="content?idx=<%=post.getIdx()%>&curPage=<%=replyCntStart - 1%>">이전</a>
                </li>
                <% } %>
                <% for (int i = replyCntStart; i <= 1 + (replyCnt - 1) / 10 && i <= replyCntStart + 9; i++) { %>
                <% if (curPage == i) { %>
                <li class="page-item active">
                    <span class="page-link"><%=i%>
                        <span class="sr-only">(current)</span>
                    </span>
                </li>
                <% } else { %>
                <li class="page-item">
                    <a class="page-link" href="content?idx=<%=post.getIdx()%>&curPage=<%=i%>"><%=i%>
                    </a>
                </li>
                <% } %>

                <% } %>
                <% if (replyCntStart + 10 <= 1 + (replyCnt - 1) / 10) { %>
                <li class="page-item">
                    <a class="page-link page-next"
                       href="content?idx=<%=post.getIdx()%>&curPage=<%=replyCntStart + 10%>">다음</a>
                </li>
                <% } %>
            </ul>
        </nav>
        <% } %>
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

<!-- 댓글 수정 창 (숨겨진 상태) -->
<div class="modal fade" id="modify-reply-popup">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <!-- Modal Header -->
            <div class="modal-header">
                <h4 class="modal-title">댓글 수정</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>

            <!-- Modal body -->
            <div class="modal-body">
                <form class="needs-validation-reply-modify" method="POST" action="/modifyreply.do" novalidate>
                    <div class="form-group">
                        <textarea id="content-modify" class="form-control" name="content" rows="8" required
                                  maxlength="1000"></textarea>
                        <small class="form-text text-muted">댓글의 내용을 입력해 주세요.</small>
                    </div>
                    <div class="form-group d-flex flex-row-reverse">
                        <input type="hidden" name="postIdx" value="<%=post.getIdx()%>">
                        <input type="hidden" id="modifyIdx" name="idx">
                        <input type="submit" class="btn btn-outline-info" value="수정"/>
                    </div>
                </form>
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

<script>
    // 입력 확인
    window.addEventListener("load", () => {
        let login_forms = document.getElementsByClassName("needs-validation");
        Array.prototype.filter.call(login_forms, (form) => {
            form.addEventListener("submit", (event) => {
                if (form.checkValidity() === false) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add("was-validated");
            }, false);
        });

        let reply_add_forms = document.getElementsByClassName("needs-validation-reply-add");
        Array.prototype.filter.call(reply_add_forms, (form) => {
            form.addEventListener("submit", (event) => {
                if (form.checkValidity() === false) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add("was-validated");
            }, false);
        });

        let reply_modify_forms = document.getElementsByClassName("needs-validation-reply-modify");
        Array.prototype.filter.call(reply_modify_forms, (form) => {
            form.addEventListener("submit", (event) => {
                if (form.checkValidity() === false) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add("was-validated");
            }, false);
        });
    }, false);

    // 댓글 수정 내용
    document.querySelectorAll(".reply-edit").forEach((currentValue) => {
        currentValue.addEventListener("click", () => {
            let replyIdx = Number(currentValue.id.substring(5));
            let replyId = "#" + currentValue.id + "-content";
            let content = document.querySelector(replyId).innerHTML;
            document.querySelector("#content-modify").innerHTML = content.replace(/(<br>|<br\/>|<br \/>)/g, '');
            document.querySelector("#modifyIdx").value = replyIdx;
        })
    })


    // 삭제
    let deleteButton = document.querySelector("#delete");
    deleteButton.addEventListener("click", (event) => {
        if (!confirm("글을 정말로 지우시겠습니까?\n지운 글은 복구할 수 없습니다.")) {
            event.stopPropagation();
            event.preventDefault();
        }
    });

</script>
</body>
</html>
