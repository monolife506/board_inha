<%@ page import="board.post.Post" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String userId = (String) session.getAttribute("user.id");
    Post post = (Post) request.getAttribute("post"); // 현재 표시하는 글
%>

<html lang="ko">
<head>
    <!-- Bootstrap CDN -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
          integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous"/>

    <style>
        .wrapper {
            display: flex;
            flex-direction: column;
        }

        .card {
            margin: 10px 0px;
        }

        #sub-img-preview {
            display: none;
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
                    <a data-toggle="modal" data-target="#login-popup" class="nav-link" href="/list?board=3"
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
        <div class="card shadow-sm p-3">
            <fieldset>
                <h2>게시글 수정</h2>
                <hr/>
                <form action="/modify.do" class="needs-validation" method="POST" enctype="multipart/form-data"
                      novalidate>
                    <!-- 제목과 업로드할 게시판 입력 -->
                    <div class="row">
                        <div class="form-group col-md-8">
                            <label for="title">제목</label>
                            <input
                                    type="text"
                                    class="form-control"
                                    id="title"
                                    name="title"
                                    placeholder="글의 제목"
                                    maxlength="50"
                                    value="<%=post.getTitle()%>"
                                    required
                            />
                            <small class="form-text text-muted"
                            >글을 잘 설명할 수 있는 제목을 입력해 주세요. (50자 이하)</small
                            >
                        </div>
                        <div class="form-group col-md-4">
                            <label for="sboard">게시판</label>
                            <select id="sboard" name="board" class="form-control">
                                <option id="board1" value="1">자유 게시판</option>
                                <option id="board2" value="2">유머 게시판</option>
                                <option id="board3" value="3">정보 게시판</option>
                            </select>
                            <small class="form-text text-muted">글을 올릴 게시판을 선택해 주세요.</small>
                        </div>
                    </div>
                    <hr/>

                    <!-- 사진 업로드 - 사진 미리보기까지 -->
                    <div class="form-group">
                        <label for="simg">사진</label>
                        <div class="photo-preview">
                            <img src="<%=request.getContextPath()%>/static/img/<%=post.getImg()%>" id="img-preview"
                                 class="card-img-top" alt="Image"/>
                            <img src="<%=request.getContextPath()%>/static/img/<%=post.getSub_img()%>"
                                 id="sub-img-preview"
                                 class="card-img-top my-2" alt="Image"/>
                        </div>
                        <div class="form-group custom-file">
                            <input type="file" class="custom-file-input" id="simg" name="img"
                                   accept="image/*"/>
                            <small class="form-text text-muted custom-file-label" data-browse="업로드"><%=post.getImg()%>
                            </small>
                        </div>
                        <div id="sub-file" class="form-group custom-file">
                            <input type="file" class="custom-file-input" id="sub-img" name="sub-img" accept="image/*"/>
                            <small id="sub-file-label" class="form-text text-muted custom-file-label" data-browse="업로드">추가로
                                업로드할 사진을 선택해
                                주세요.</small>
                        </div>
                    </div>
                    <hr/>

                    <!-- 내용 입력 -->
                    <div class="form-group">
                        <label for="scontent">내용</label>
                        <textarea id="scontent" class="form-control" name="content" rows="8" required
                                  maxlength="65536"><%=post.getContent()%></textarea>
                        <small class="form-text text-muted">글의 내용을 입력해 주세요.</small>
                    </div>
                    <hr/>

                    <input type="hidden" id="idx" name="idx" value="<%=post.getIdx()%>">

                    <!-- 수정 버튼 -->
                    <div class="form-group">
                        <input type="submit" class="btn btn-outline-info" value="수정"/>
                    </div>
                </form>
            </fieldset>
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

<script>
    // 입력 여부 확인
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

    // 알맞는 게시판 선택
    <% switch (post.getBoard()) {
       case 1: %>
    document.querySelector("#board1").setAttribute("selected", "");
    <% break;
       case 2: %>
    document.querySelector("#board2").setAttribute("selected", "");
    <% break;
       case 3: %>
    document.querySelector("#board3").setAttribute("selected", "");
    <% break;
    } %>

    // 이미지가 변경되었을 때, 사진과 파일명 변경
    let fileInput = document.querySelector("#simg");
    fileInput.addEventListener("change", (event) => {
        let fileList = fileInput.files;
        let fileReader = new FileReader();

        fileReader.readAsDataURL(fileList[0]);
        fileReader.addEventListener("load", (event) => {
            document.querySelector(".custom-file-label").innerHTML = fileList[0].name;
            document.querySelector("#img-preview").src = fileReader.result;
        })
    });

    let subFileInput = document.querySelector("#sub-img");
    subFileInput.addEventListener("change", (event) => {
        let subFileList = subFileInput.files;
        let subFileReader = new FileReader();
        document.querySelector("#sub-img-preview").style.display = "block";

        subFileReader.readAsDataURL(subFileList[0]);
        subFileReader.addEventListener("load", (event) => {
            document.querySelector("#sub-file-label").innerHTML = subFileList[0].name;
            document.querySelector("#sub-img-preview").src = subFileReader.result;
        })
    });

    <% if (post.getSub_img() != null) { %>
    document.querySelector("#sub-file-label").innerHTML = "<%=post.getSub_img()%>"
    document.querySelector("#sub-img-preview").style.display = "block";
    <% } %>
</script>

</body>
</html>
