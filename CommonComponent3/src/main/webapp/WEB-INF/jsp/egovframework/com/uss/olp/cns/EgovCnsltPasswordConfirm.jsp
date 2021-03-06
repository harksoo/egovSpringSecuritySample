<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
 /**
  * @Class Name : EgovCnsltPasswordConfirm.jsp
  * @Description : EgovCnsltPasswordConfirm 화면
  * @Modification Information
  * @
  * @  수정일             수정자                   수정내용
  * @ -------    --------    ---------------------------
  * @ 2009.02.01   박정규              최초 생성
  *
  *  @author 공통서비스팀 
  *  @since 2009.02.01
  *  @version 1.0
  *  @see
  *  
  */
%>

<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ko">
<head>
<base target="_self" >
<title>작성비밀번호 확인</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link type="text/css" rel="stylesheet" href="<c:url value='/css/egovframework/com/cmm/com.css' />">
<link type="text/css" rel="stylesheet" href="<c:url value='/css/egovframework/com/cmm/button.css' />">
<script type="text/javaScript" language="javascript">

/* ********************************************************
 * 초기화
 ******************************************************** */
function fn_egov_initl_cnsltdtls(){

	// 작성글 비밀번호 입력란에 포커스..
	document.CnsltManageForm.writngPassword.focus();

}

/* ********************************************************
 * 확인처리
 ******************************************************** */
function fn_egov_confirm_qnapassword(){

	/* var opener = window.dialogArguments; */
	var opener;
 
	if (window.dialogArguments) {
	    opener = window.dialogArguments; // Support IE
	} else {
	    opener = window.opener;    // Support Chrome, Firefox, Safari, Opera
	}

	//  현재화면의 작성 비밀번호
	var	ls_writngPassword = document.CnsltManageForm.writngPassword.value;
	
	// 부모화면으로 작성비밀번호를 넘긴다.
	opener.document.CnsltManageForm.writngPassword.value = ls_writngPassword;	
	

	// 부모화면으로 결과값을 True 넘긴다.
	window.returnValue = true;	
	window.close();	

		
}

/* ********************************************************
 * 취소처리
 ******************************************************** */
function fn_egov_cancel_qnapassword() {
	
	self.close();
			
}

</script>

</head>
<body onLoad="fn_egov_initl_cnsltdtls();">
<DIV id="content" style="width:270px">
<!-- 상단타이틀 -->
<form name="CnsltManageForm"  method="post" onsubmit="fn_egov_confirm_qnapassword();" action="">


<!-- 작성 비밀번호를 확인하기 위한. -->	  
	  
<!-- 상단 타이틀  영역 -->
<table width="100%" cellpadding="8" class="table-search" border="0">
 <tr>
  <td width="100%"class="title_left">
   <h1><img src="<c:url value='/images/egovframework/com/cmm/icon/tit_icon.gif' />" width="16" height="16" hspace="3" alt="">&nbsp;작성 비밀번호 확인</h1></td>
 </tr>
</table>

<!-- 줄간격조정  -->
<table width="100%" cellspacing="0" cellpadding="0" border="0">
<tr>
	<td height="3px"></td>
</tr>
</table>

<!-- 등록  폼 영역  -->
<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table-register">
<caption>작성비밀번호 확인</caption>
  <tr> 
    <th width="20%" height="23" class="required_text" nowrap ><label for="writngPassword">작성글 비밀번호</label>
    	<img src="<c:url value='/images/egovframework/com/cmm/icon/required.gif' />" width="15" height="15" alt="필수항목"></th>
    <td width="80%" nowrap>
    	<input name="writngPassword" type="password" size="20" value="" maxlength="20" title="작성글 비밀번호">   
    </td>
  </tr>
      
</table>

<!-- 줄간격조정  -->
<table width="100%" cellspacing="0" cellpadding="0" border="0">
<tr>
	<td height="10px"></td>
</tr>
</table>

<!-- 확인/취소버튼  -->
<table border="0" cellspacing="0" cellpadding="0" align="center">
<tr> 
  <td><span class="button"><a href="javascript:fn_egov_confirm_qnapassword();">확인</a></span> 
  </td>
  <td>&nbsp;</td>
  <td><span class="button"><a href="JavaScript:fn_egov_cancel_qnapassword();">취소</a></span> 
  </td>
</tr>
</table>

</form>
</DIV>

</body>
</html>
