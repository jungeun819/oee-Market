<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/member.css" />
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<div class="sign-up-container">
	<form:form name="memberEnrollFrm" id="sign-up-form">
		<div class="sign-up-title">
			<h2>회원가입</h2>
		</div>
		<div class="sign-up-box">
			<h3 class="input-title">아이디</h3>
			<p>영문, 숫자를 포함한 5~15자의 아이디를 입력해주세요.</p>
			<input type="text" name="memberId" id="memberId" class="sign-up-input" required/>
			<button class="dupl-btn">중복확인</button>
			<span class="dupl-msg ok">사용 가능한 아이디입니다.</span>
			<span class="dupl-msg error">이미 사용중인 아이디입니다. 새로운 아이디를 입력해주세요.</span>
			<input type="hidden" id="idValid" value="0"/>
		</div>
		<div class="sign-up-box">
			<h3 class="input-title">비밀번호</h3>
			<p>영문, 숫자를 포함한 8자 이상의 비밀번호를 입력해주세요.</p>
			<input type="password" name="password" id="pwd" class="sign-up-input" required>
			<input type="hidden" id="pwdValid" value="0"/>
		</div>
		<div class="sign-up-box">
			<h3 class="input-title">비밀번호 확인</h3>
			<input type="password" id="pwdCheck" class="sign-up-input" required>
			<p class="check-msg">비밀번호가 일치하지 않습니다. 다시 입력해주세요.</p>
		</div>
		<div class="sign-up-box">
			<h3 class="input-title">닉네임</h3>
			<p>한글 2~8자의 닉네임을 입력해주세요.</p>
			<input type="text" name="nickname" id="nickname" class="sign-up-input" required>
		</div>
		<div class="sign-up-box">
			<h3 class="input-title">휴대폰번호</h3>
			<p>-를 제외한 휴대폰번호를 입력해주세요.</p>
			<input type="tel" name="phone" id="phone" class="sign-up-input" required>
		</div>
		<div class="sign-up-box">
			<h3 class="input-title">주소</h3>
			<p class="error-msg">모두 선택해주세요.</p>
			<select class="form-select" id="gu-select" name="gu" aria-label="Default select example">
				<option selected>구 선택</option>
					<c:forEach items="${guList}" var="gu">
	            		<option value="${gu.guNo}">${gu.guName}</option>
			  		</c:forEach> 
			</select>
			<select class="form-select" id="dong-select" name="dong" aria-label="Default select example">
				<option selected>동 선택</option>
				<c:forEach items="${dongList}" var="dong">
					<option value="${dong.dongNo}" class="dong-option ${dong.guNo}">${dong.dongName}</option>
				</c:forEach>
			</select>
			<select class="form-select" id="dong-range-select" name="dongRange" aria-label="Default select example">
				<option selected>범위 선택</option>
				<option value="N">근처동네 3개</option>
				<option value="F">근처동네 5개</option>
			</select>
		</div>
		<div class="sign-up-btn">
			<input type="submit" value="회원가입">
		</div>
	</form:form>
</div>
<script>


/* 선택한 구에 따른 동 변화 */
document.querySelector("#gu-select").addEventListener('change', (e) => {
	const dong = document.querySelector("#dong-select");
	dong.selectedIndex = 0; // 선택된 동 초기화
	
	const options = dong.getElementsByTagName("option");
	
	for (let i = 0; i < options.length; i++) {
		const option = options[i];
		// 해당 태그에 타겟과 일치하는 guNo의 존재유무에 따른 분기처리
		if (option.classList.contains(e.target.value))
			option.style.display = "block"; 
		else
			option.style.display = "none";
	}
	
});

const memberId = document.querySelector("#memberId");
const idValid = document.querySelector("#idValid");
const pwdValid = document.querySelector("#pwdValid");

/* 아이디 중복검사 */
document.querySelector(".dupl-btn").addEventListener('click', (e) => {
	e.preventDefault();

	const ok = document.querySelector(".ok");
	const error = document.querySelector(".error");
	
	// 영문, 숫자를 포함한 5~15자의 아이디를 입력해주세요.
	const regExpArr = [/^.{5,15}$/, /\d/, /[a-zA-Z]/];
	for(let i = 0; i < regExpArr.length; i++){
		if(!regExpArr[i].test(memberId.value)){
			memberId.previousElementSibling.style.color='#E64848';
			memberId.select();
			idValid.value = 0;
			return;
		}
	}
	memberId.previousElementSibling.style.color='#868B94';
	
	$.ajax({
		url : "${pageContext.request.contextPath}/member/checkIdDuplicate.do",
		data : {memberId : memberId.value},
		method : "GET",
		dataType : "json",
		success(data){
			console.log(data);
			const {memberId, available} = data;

			if(available){
				ok.style.visibility = "visible";
				error.style.visibility = "hidden";
				idValid.value = 1;
			}
			else {
				ok.style.visibility = "hidden";
				error.style.visibility = "visible";
				idValid.value = 0;
			}
			
		},
		error : console.log
	});
});

/* 비밀번호 유효성 검사 */
// 영문, 숫자를 포함한 8자 이상의 비밀번호를 입력해주세요.
document.querySelector("#pwd").addEventListener('blur', (e) => {
	e.preventDefault();
	
	const pwd = e.target.value;
	const regExpArr = [/^.{8,}$/, /\d/, /[a-zA-Z]/];
	
	for(let i = 0; i < regExpArr.length; i++){
		if(!regExpArr[i].test(pwd)){
			e.target.previousElementSibling.style.color="#E64848";
			pwdValid.value = 0;
			return;
		}
	}
	e.target.previousElementSibling.style.color="#868B94";
	pwdValid.value = 1;
});

/* 비멀번호 확인 */
document.querySelector("#pwdCheck").addEventListener('keyup', (e) => {
	const pwd = document.querySelector("#pwd").value;
	const checkPwd = e.target
	
	if(checkPwd.value != pwd){
		checkPwd.nextElementSibling.style.visibility = "visible";
		checkPwd.nextElementSibling.style.color = "#E64848";
		pwdValid.value = 0;
	}
	else{
		checkPwd.nextElementSibling.style.visibility = "hidden";		
		pwdValid.value = 1;
	}
});

/* 유효성 검사 */ 
document.memberEnrollFrm.onsubmit = (e) => {
	e.preventDefault();
	
	// 아이디, 비밀번호 
	if(idValid === 0 || pwdValid.value === 0)
		return false;
	
	// 닉네임
	const nickname = document.querySelector("#nickname");
	if(!/^.[가-힣]{2,}$/.test(nickname.value)){
		nickname.select();
		nickname.previousElementSibling.style.color='#E64848';
		return false;
	}
	else
		nickname.previousElementSibling.style.color='#868B94';
	
	// 전화번호
	const phone = document.querySelector("#phone");
	if(!/^.\d{10,11}$/.test(phone.value)){
		phone.select();
		phone.previousElementSibling.style.color="#E64848"
		return false;
	}
	else
		phone.previousElementSibling.style.color='#868B94';
	
	// 동네 설정
	const dong = document.querySelector("#dong-select");
	const errorMsg = document.querySelector(".error-msg");
	if(dong.value === '동 선택'){
		errorMsg.style.color="#E64848";
		dong.focus();
		return false;
	}
	
	const dongRange = document.querySelector("#dong-range-select");
	if(dongRange.value === '범위 선택'){
		errorMsg.style.color="#E64848";
		dong.focus();
		return false;
	}
	
	return true;
	
};

</script>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>