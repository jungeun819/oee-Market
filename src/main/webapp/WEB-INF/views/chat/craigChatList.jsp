<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.6.0.js"
	integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk="
	crossorigin="anonymous"></script>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css"
	integrity="sha384-9gVQ4dYFwwWSjIDZnLEWnxCjeSWFphJiwGPXr1jddIhOegiu1FwO5qRGvFXOdJZ4"
	crossorigin="anonymous">
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"
	integrity="sha384-uefMccjFJAIv6A+rW+L4AHf99KvxDjWSu1z9VI8SKNVmz4sk7buKt/6v9KI65qnm"
	crossorigin="anonymous"></script>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath }/resources/css/chat/craigChatList.css" />
</head>

<body>
	<div class="chat">
		<div class="card" style="min-height: 760px; max-height: 100%; min-width: 500px;">
			<!-- 게시글정보 start -->
			<div id="craig_bar">
				<div class="craig_info_wrap">
					<c:if test="${craigImg[0] == null}">	
						<img style="width: 60px; height: 60px;" src="${pageContext.request.contextPath}/resources/images/OEE-LOGO2.png" alt="" />
					</c:if>
					<c:if test="${craigImg[0] != null}">
						<img style="width: 60px; height: 60px;" src="${pageContext.request.contextPath}/resources/upload/craig/${craigImg[0].reFilename}"alt="" />
					</c:if>
					<div class="craig_text">
						<p class="craig_status">
							<c:choose>
								<c:when test="${craig.state eq 'CR1'}">
							예약중
							</c:when>
								<c:when test="${craig.state eq 'CR2'}">
							판매중
							</c:when>
								<c:when test="${craig.state eq 'CR3'}">
							판매완료
							</c:when>
							</c:choose>
						</p>
						<p class="craig_name">${craig.title}</p>
						<span class="price"> <fmt:formatNumber
								value="${craig.price}" pattern="#,###" />원
						</span>
					</div>
				</div>
			</div>
			<!-- 게시글정보 end -->
			
			<!-- 채팅목록 start  -->
			<div id="chatList" class="d-flex flex-column align-items-stretch flex-shrink-0 bg-white">
				<div class="list-group list-group-flush border-bottom scrollarea">
					<c:forEach items="${chatModel}" var="chatMember">
						<a id="toChatroom" href="#" class="list-group-item list-group-item-action py-3 lh-sm" aria-current="true">
							<input type="hidden" name="chatroomId" value="${chatMember.lastChat.chatroomId}"/>
							<div id="chatList-wrap">
        	 					<img src="${pageContext.request.contextPath}/resources/upload/profile/${chatMember.otherProf}" alt="profileImg"/>
        						<div class="chatList-header">
        							<div id="chatUserInfo">
		        		       			<strong class="mb-1">${chatMember.otherName}</strong>
					          			<small>${chatMember.otherDong}</small>
				        				<div class="last-chatting">${chatMember.lastChat.content}</div>
        							</div>
        						</div>
        					</div>
						</a>
					</c:forEach>
				</div>


			</div>
			<!-- 채팅목록 end  -->
		</div>
	</div>

<script>	
document.querySelector("#toChatroom").addEventListener('click', (e) => {
const craigNo = ${craig.no};
const memberId = `${craig.writer}`;
const chatroomId = document.querySelector("#toChatroom input").value;

const url = `${pageContext.request.contextPath}/chat/craigChat.do?chatroomId=\${chatroomId}&memberId=\${memberId}&craigNo=\${craigNo}`;
const name = "craigChatroom";
openPopup(url, name); 

});
	


function openPopup(url, name){
	let win;
	win = window.open(url, name, 'scrollbars=yes,width=500,height=790,status=no,resizable=no');
	win.opener.self;
}


</script>
</body>
</html>