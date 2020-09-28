<%@ page import="board.member.UserRegister" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% UserRegister.RegisterError errorType = (UserRegister.RegisterError) session.getAttribute("registerError");%>

<html>
<head>
    <!-- Bootstrap CDN -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
          integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous"/>

    <style>
        .card {
            margin: 10px;
        }
    </style>

    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
    <title>인하대 인터넷 프로그래밍 프로젝트</title>
</head>
<body>
<div class="container">
    <div class="register-background">
        <div class="card shadow-sm rounded">
            <div class="card-body">
                <% if (errorType != null) { %>
                <div id="register-error" class="alert alert-danger" role="alert">
                    <% if (errorType == UserRegister.RegisterError.ID_ERROR) { %>
                    이미 존재하는 ID입니다.
                    <% } else if (errorType == UserRegister.RegisterError.EMAIL_ERROR) { %>
                    이미 존재하는 이메일입니다.
                    <% } %>
                </div>
                <% errorType = null; %>
                <% } %>

                <form id="register-form" method="post" action="/register.do" class="needs-validation" novalidate>
                    <div class="form-group">
                        <label for="id">ID</label>
                        <input
                                type="text"
                                class="form-control"
                                id="id"
                                name="id"
                                maxlength="20"
                                pattern="^[A-Za-z0-9-_]{4,20}$"
                                required
                        />
                        <small id="id-help" class="form-text text-muted">
                            알파벳이나 숫자를 포함해 4자 이상 20자 이내로 입력해 주세요.
                        </small>
                    </div>
                    <div class="form-group">
                        <label for="email">이메일</label>
                        <input
                                type="email"
                                class="form-control"
                                id="email"
                                name="email"
                                maxlength="50"
                                required
                        />
                        <small id="email-help" class="form-text text-muted">
                            example@example.com 형태로 50자 이내로 입력해 주세요.
                        </small>
                    </div>
                    <div class="form-group">
                        <label for="pwd">비밀번호</label>
                        <input
                                type="password"
                                class="form-control"
                                id="pwd"
                                name="pwd"
                                maxlength="20"
                                pattern="^[A-Za-z0-9-_]{6,20}$"
                                required
                        />
                        <small id="pwd-help" class="form-text text-muted">
                            알파벳이나 숫자를 포함해 6자 이상 20자 이내로 입력해 주세요.
                        </small>
                    </div>
                    <div class="form-group d-flex flex-row-reverse">
                        <input id="submit-btn" type="submit" class="btn btn-outline-primary" value="회원가입"/>
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
</script>
</body>
</html>
