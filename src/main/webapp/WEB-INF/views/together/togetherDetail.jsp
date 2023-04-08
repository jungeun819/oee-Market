<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<jsp:include page="/WEB-INF/views/common/header.jsp">
	<jsp:param value="같이해요" name="title"/>
</jsp:include>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/together/together.css" />
<script>
/* 매너온도에 따른 색변화 */
window.addEventListener('load', (e) => {
	const temperature = document.querySelector(".temperature span");
	if(temperature.innerText < 30) temperature.style.color = '#3AB0FF'; 
	else if(temperature.innerText >= 30 && temperature.innerText < 50) temperature.style.color = '#56C271'; 
	else if(temperature.innerText >= 50) temperature.style.color = '#F94C66'; 
});

</script>
<sec:authentication property="principal" var="loginMember"/>
<div class="together-container">
	<div class="together-wrap">
		<!-- 글쓴이 프로필 -->
		<div class="writer-info-box">
			<div class="writer-box">
			
			<!-- -------------------------------------------------------------------------------------------------------------- -->		
			<!-- Button trigger modal -->
			<button type="button" class="btn1 btn-primary" data-bs-toggle="modal" data-bs-target="#exampleModal">
			<div class="profile-box">
					<img src="${pageContext.request.contextPath}/resources/upload/profile/${together.member.profileImg}" alt="사용자프로필" id="imagePreview">
				</div>
			</button>
			
			<!-- Modal -->
			<div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
			  <div class="modal-dialog modal-dialog-centered">
			    <div class="modal-content" id="modal-content">
			      <div class="modal-header" id="modal-header">
				      <div>
					      <div style="display:flex;">
					        <img src="${pageContext.request.contextPath}/resources/upload/profile/${together.member.profileImg}"  alt="프로필" name="profileImg" id="imagemodal">
					        <div>
					        <h4 class="modal-title1 fs-5" id="exampleModalLabel">
					        ${together.member.nickname}
					        </h4>
					        <h6 id="dong">${together.dong.dongName}</h6>
					        </div>
					      </div>
			      	</div>
					     <h6>${together.member.manner}℃</h6>
			        </div>
			      
			      <div class="modal-body" id="modal-body">
			      	<form:form name="salCriag1Frm" action="${pageContext.request.contextPath}/craig/mySalCraig1.do" method="GET">
				        <li>
				        <img src="${pageContext.request.contextPath}/resources/images/Cr.png" alt="" id="mypageimg"/>
				        <button type="submit" class="btn-list">중고거래</button>
				        <input type="hidden" name="memberId" value="${together.writer}"/>
				        </li>
			        </form:form>
			      	<form:form name="myLocal1Frm" action="${pageContext.request.contextPath}/local/myLocal1.do" method="GET">
				      	<li>
				        <img src="${pageContext.request.contextPath}/resources/images/Lo.png" alt="" id="mypageimg"/>
					        <button type="submit" class="btn-list">동네생활</button>
					        <input type="hidden" name="memberId" value="${together.writer}"/>
				        </li>
			        </form:form>
			      	<form:form name="myTogether1Frm" action="${pageContext.request.contextPath}/together/myTogether1.do" method="GET">
				        <li>
				        <img src="${pageContext.request.contextPath}/resources/images/To.png" alt="" id="mypageimg"/>
				        <button type="submit" class="btn-list">같이해요</button>
				        <input type="hidden" name="memberId" value="${together.writer}"/>
				        </li>
			        </form:form>
			      	<form:form name="myManner1Frm" action="${pageContext.request.contextPath}/manner/myManner1.do" method="GET">
				        <li>
				        <img src="${pageContext.request.contextPath}/resources/images/Ma.png" alt="" id="mypageimg"/>
				        <button type="submit" class="btn-list">받은매너</button>
				        <input type="hidden" name="memberId" value="${together.writer}"/>
				        </li>
			        </form:form>
			      </div>
			      <div class="modal-footer" id="modal-footer">
			        <button type="button" class="btn btn-secondary1" data-bs-dismiss="modal">Close</button>
			      </div>
			    </div>
			  </div>
			</div>
			</td>
		<!-- --------------------------------------------------------------------------------------------- -->
			
			
				
				<div class="detail-box">
					<p>${together.member.nickname}</p>
					<p>${together.dong.dongName}</p>
				</div>
			</div>
			<div class="manner-box">
				<div class="temperature">
					<span>${together.member.manner}</span>
					<c:if test="${together.member.manner lt 30}">
						<span style="color:#3AB0FF" >°C</span>
						<span style="position:relative; top:5px;" >😰</span>
						<div class="progress" style="width:80px; height: 10px;">
					  		<div class="progress-bar progress-bar-striped bg-info" role="progressbar" style="width: 30%; display: absolute;" aria-valuenow="30" aria-valuemin="0" aria-valuemax="100"></div>
						</div>
					</c:if>
					<c:if test="${together.member.manner ge 35 && together.member.manner lt 50}">
						<span style="color: #56C271">°C</span>
						<span style="position:relative; top:5px;" >☺️</span>
						<div class="progress" style="width:80px; height: 10px;">
							<div class="progress-bar" role="progressbar" style="width: 65%; background-color: rgb(86, 194, 113); float: right; margin-right: 0" aria-valuenow="65" aria-valuemin="0" aria-valuemax="100"></div>
						</div>
					</c:if>
					<c:if test="${together.member.manner ge 50}">
						<span style="color: red">°C</span>
						<span style="position:relative; top:5px;" >😍</span>
						<div class="progress" style="width:80px; height: 10px;">
							<div class="progress-bar bg-danger" role="progressbar" style="width: 90%; margin-right: 0;" aria-valuenow="90" aria-valuemin="0" aria-valuemax="80"></div>
						</div>
					</c:if>
				</div>
				<p>매너온도</p>
			</div>
		</div><!-- end writer-info-box -->
		<div class="category-box">
			<span>
				<c:if test="${together.categoryNo eq 1}">
					<i class="fa-solid fa-utensils"></i>
					${categorys[0].CATEGORY_NAME}
				</c:if>
				<c:if test="${together.categoryNo eq 2}">
					<i class="fa-solid fa-shoe-prints"></i>
					${categorys[1].CATEGORY_NAME}
				</c:if>
				<c:if test="${together.categoryNo eq 3}">
					<i class="fa-solid fa-book"></i>
					${categorys[2].CATEGORY_NAME}
				</c:if>
				<c:if test="${together.categoryNo eq 4}">
					<i class="fa-solid fa-palette"></i>
					${categorys[3].CATEGORY_NAME}
				</c:if>
				<c:if test="${together.categoryNo eq 5}">
					<i class="fa-solid fa-paw"></i>
					${categorys[4].CATEGORY_NAME}
				</c:if>
				<c:if test="${together.categoryNo eq 6}">
					<i class="fa-solid fa-bars"></i>
					${categorys[5].CATEGORY_NAME}
				</c:if>
			</span>
		</div><!-- end category-box -->
		<div class="header-box">
			<div class="info-box">
				<div class="status-box">
					<c:if test="${together.status eq 'Y'}">
					<h2 class="status ing">모집중</h2>
					</c:if>
					<c:if test="${together.status eq 'N'}">
						<h2 class="status end">모집완료</h2>
					</c:if>
				</div>
				<div class="title-box">
					<h2>${together.title}</h2>
				</div>
			</div>
			<div class="choose-box">
			<c:if test="${together.writer eq loginMember.memberId && together.status eq 'Y'}">
				<button class="btn" data-toggle="modal" data-target="#close-modal">모임종료</button>
			</c:if>
			<c:if test="${together.writer ne loginMember.memberId}">
				<button class="btn report">신고하기</button>
			</c:if>
			</div>
		</div><!-- end header-box -->
		<div class="content-box">
			<h4 class="to-h4">정보</h4>
			<div class="info required">
				<!-- 성별 선택 -->
				<i class="bi bi-people-fill"></i>
				<c:if test="${together.gender eq 'A'}">
					<span class="gender">성별무관</span>
				</c:if>
				<c:if test="${together.gender eq 'F'}">
					<span class="gender">여성</span>
				</c:if>
				<c:if test="${together.gender eq 'M'}">
					<span class="gender">남성</span>
				</c:if>
				&nbsp;&middot;&nbsp;
				<!-- 나이 선택 -->
				<c:if test="${together.age eq '100'}">
					<span class="age">나이무관</span>
				</c:if>
				<c:if test="${together.age ne '100'}">
					<span class="age">${together.age}대이상</span>
				</c:if>
			</div>
			<div class="info appointmen">
				<i class="bi bi-calendar4-week"></i>
				<p class="datetime">
					<fmt:parseDate value="${together.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="dateTime"/>
					<fmt:formatDate value="${dateTime}" pattern="MM월 dd일 E요일 HH시 mm분"/>
				</p>
			</div>
			<div class="info place">
				<i class="bi bi-geo-alt"></i>
				<p>${together.place}</p>
			</div>
			<div class="modify-box">
				<c:if test="${together.status eq 'Y'}">
					<button class="join btn" data-toggle="modal" data-target="#join-modal">참여하기</button>
				</c:if>
				<c:if test="${together.writer eq loginMember.memberId && together.status eq 'Y'}">
					<button class="btn modify">수정</button>
				</c:if>
				<c:if test="${together.writer eq loginMember.memberId}">
					<button class="btn delete" data-toggle="modal" data-target="#delete-modal">삭제</button>
				</c:if>
			</div>
		</div>
		<hr>
		<div class="content-detail-box">
			<h4 class="to-h4">상세내용</h4>
			<p>${together.content}</p>
		</div>
		<div class="join-member-box">
			<div class="join-member-title">
				<h4 class="to-h4">참여중인 이웃</h4>
				<div>
					<span>[&nbsp;</span>
					<span class="title-cnt">${joinCnt[0].joinCnt}</span>
					<span>&#47;</span>
					<span class="title-cnt2">${together.joinCnt}</span>
					<span>&nbsp;]</span>
				</div>
			</div>
			<div class="current-join-memberList">
				<c:forEach items="${joinMemberList}" var="joinMember">
				<div class="member-info">
					<img src="${pageContext.request.contextPath}/resources/upload/profile/${joinMember.member.profileImg}" alt="참여이웃프로필">
					<div class="info-txt">
						<c:if test="${joinMember.role eq 'A'}">
							<span class="badge badge-success">모임장</span>
						</c:if>
						<c:if test="${joinMember.role eq 'M'}">
							<span class="badge badge-secondary">참여자</span>
						</c:if>
						<span>${joinMember.member.nickname}</span>
					</div>
				</div>				
				</c:forEach>
			</div>
		</div>
	</div>
</div>
<!-- 모임종료 경고모달 -->
<div class="modal fade" id="close-modal" tabindex="-1" role="dialog" aria-labelledby="closModalLabel" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="closModalLabel">※ 모임 종료 ※</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
				<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				모임을 종료하면 이웃이 더 이상 일정에 참여할 수 없고, 
				<br/>
				게시글을 수정할 수 없어요. 종료하시겠어요?
			</div>
			<div class="modal-footer">
				<button type="button" class="btn to_close">종료</button>
				<button type="button" class="btn" data-dismiss="modal">취소</button>
			</div>
		</div>
	</div>
</div>
<!-- 삭제하기 모달 -->
<div class="modal fade" id="delete-modal" tabindex="-1" role="dialog" aria-labelledby="deleteModalLabel" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="deleteModalLabel">※ 게시글 삭제 ※</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
				<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				게시글을 삭제하면 대화방 또한 사라져요.
				<br/>
				정말 게시글을 삭제하시겠습니까?
			</div>
			<div class="modal-footer">
				<button type="button" class="btn to_delete">삭제</button>
				<button type="button" class="btn" data-dismiss="modal">취소</button>
			</div>
		</div>
	</div>
</div>
<!-- 참여하기 모달 -->
<div class="modal fade" id="join-modal" tabindex="-1" role="dialog" aria-labelledby="joinModalLabel" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="joinModalLabel">※ 참여하기 알림 ※</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
				<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="alert alert-warning alert-dismissible fade show" role="alert">
				<span class="text-danger-emphasis"><i class="bi bi-dash-circle"></i>&nbsp;모집이 마감된 대화방입니다.</span>
            </div>
			<div class="modal-body">
				☘️ ${together.title} ☘️
				</br>
				모임에 참여하시겠습니까?
			</div>
			<div class="modal-footer">
				<!-- 대화방에 입장한 경우 확인 -->
				<c:set var="hasEntered" value="false"/>
				<c:forEach items="${joinMemberList}" var="joinMember">
				    <c:if test="${joinMember.memberId eq loginMember.memberId}">
				        <button type="button" class="btn enter">대화방입장</button>
				        <c:set var="hasEntered" value="true"/>
				    </c:if>
				</c:forEach>
				<!-- 대화방에 입장하지 않은 경우 -->
				<c:if test="${hasEntered eq false}">
				    <button type="button" class="btn to_join">참여하기</button>
				    <button type="button" class="btn to_enter enter">대화방입장</button>
				</c:if>
				<button type="button" class="btn" data-dismiss="modal">취소</button>
			</div>
		</div>
	</div>
</div>
<c:if test="${together.writer eq loginMember.memberId}">
<!-- 삭제하기 히든폼 -->
<form:form name="togetherDeleteFrm" action="${pageContext.request.contextPath}/together/togetherDelete.do" method="post">
	<input type="hidden" value="${together.no}" name="no">
</form:form>
</c:if>
<c:if test="${together.writer eq loginMember.memberId && together.status eq 'Y'}">
<!-- 모임종료 히든폼 -->
<form:form name="togetherStatusUpdateFrm" action="${pageContext.request.contextPath}/together/togetherStatusUpdate.do" method="post">
	<input type="hidden" value="${together.no}" name="no">
</form:form>
<!-- 👻 정은 시작 👻 -->
<script>
/* 같이해요 수정 */
document.querySelector(".modify").addEventListener('click', (e) => {
	const no = '${together.no}';
	location.href = '${pageContext.request.contextPath}/together/togetherUpdate.do?no=' + no;	
});

/* 모임 종료하기 */
document.querySelector(".to_close").addEventListener('click', (e) => {
	document.togetherStatusUpdateFrm.submit();
});
</script>
</c:if>
<c:if test="${together.writer eq loginMember.memberId}">
<script>
/* 같이해요 삭제 */
document.querySelector(".to_delete").addEventListener('click', (e) => {
	document.togetherDeleteFrm.submit();
});
</script>
</c:if>
<c:if test="${together.writer ne loginMember.memberId}">
<script>
/* 신고하기 */
document.querySelector(".report").addEventListener('click', (e) => {
	const reportType = 'TO';
	const boardNo = '${together.no}';
	const reportedId = '${together.writer}';
	location.href = '${pageContext.request.contextPath}/report/reportEnroll.do?reportType='+ reportType + '&boardNo=' + boardNo + '&reportedId=' + reportedId;
});
</script>
</c:if>
<!-- 정은 끝 👻 -->

<c:if test="${together.status eq 'Y' && hasEntered eq false}">
<script>
/* 현재 대화방 참여자가 아닌 경우 */
document.querySelector(".to_join").addEventListener('click', (e) => {
	const no = '${together.no}';
	const joinCnt = '${together.joinCnt}';
	const currJoinCnt = '${joinCnt[0].joinCnt}';
	const loginMember = '${loginMember.memberId}';
	const writer = '${together.writer}';
	const cntTag = document.querySelector(".title-cnt");
	const alert = document.querySelector(".alert-warning");
	const enterBtn = document.querySelector(".to_enter");
	
	console.log(loginMember, writer);

	/* 정원이 다 찼을 경우 그리고 글쓴이가 아닐 경우 */
	if(currJoinCnt == joinCnt && loginMember != writer){
		alert.style.display = 'block';
		return false;
	}
	console.log('다찼으면 여기오면 안됨 또는 작성자는 무조건 넘어와야 해');

	/* 참여하기 클릭시 게시글에서 참여자 수 증가 처리 */
	const csrfHeader = "${_csrf.headerName}";
	const csrfToken = "${_csrf.token}";
	const headers = {};
	headers[csrfHeader] = csrfToken;
	
	/* 
		아마 여기에서 채팅방에 멤버 넣기를 하면 되지 않을까싶어요,,, 현재 url은 임시로 작성해놨습니다..!! 
		나중에 url는 수정해주세요!!🥺 
		int result = chatService.insertTogetehrChat 이런 느낌의 메소드 하시게 되면
		result 값 또한 넘겨주면 아마 알아서
		회원 정보가 잘 들어가고 채팅방에 열렸을때,,? 참여자 인원을 +1 해줄 예정입니다..!
		근데 고민이 있다면,, 이미 참여한 대화방에 입장할때는 인원수를 증가시키면 안되기때문에 버튼을 분기처리 해뒀습니다..!
		아래에 현재 대화방 참여자인 경우로 한번 만들어 봤습니다🫠
	*/
/******************* 효정 시작 *********************/

	$.ajax({
		url : `${pageContext.request.contextPath}/chat/togetherChat/\${no}`,
		method : 'GET',
		dataType : "json",
		success(data){
			const {memberId, chatroomId} = data;
			
			const url = `${pageContext.request.contextPath}/chat/craigChat.do?chatroomId=\${chatroomId}&memberId=\${memberId}&craigNo=\${craigNo}`;
			const name = "craigChatroom";
			openPopup(url, name);
		},
		error : console.log
		});		
	
});

/* 팝업열기 */
function openPopup(url, name){
	let win;
	win = window.open(url, name, 'scrollbars=yes,width=500,height=790,status=no,resizable=no');
	win.opener.self;
}
/******************* 효정 끝 *********************/
</script>
</c:if>

<c:if test="${together.status eq 'Y' && hasEntered eq true}">
<script>
/* 현재 대화방 참여자인 경우 */
document.querySelector(".enter").addEventListener('click', (e) => {
	// 아마 여기서는 현재 입장중인 채팅방을 불러오면 될것같아요🫠
	console.log('확인');
});
</script>
</c:if>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>