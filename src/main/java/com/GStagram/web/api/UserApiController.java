package com.GStagram.web.api;

import com.GStagram.config.auth.PrincipalDetails;
import com.GStagram.domain.subscribe.Subscribe;
import com.GStagram.domain.user.User;
import com.GStagram.handler.ex.CustomValidationApiException;
import com.GStagram.handler.ex.CustomValidationException;
import com.GStagram.service.SubscribeService;
import com.GStagram.service.UserService;
import com.GStagram.web.dto.CMRespDto;
import com.GStagram.web.dto.subscribe.SubscribeDto;
import com.GStagram.web.dto.user.UserUpdateDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class UserApiController {

	private final UserService userService;
	private final SubscribeService subscribeService;


	@GetMapping("api/user/getUserId")
	public ResponseEntity<?> getUserId(@AuthenticationPrincipal PrincipalDetails principalDetails){
		return new ResponseEntity<>(new CMRespDto<>(1, "유저 ID 가져오기 성공", principalDetails.getUser().getId()), HttpStatus.OK);
	}

	@GetMapping("api/user/{pageUserId}/subscribe")
	public ResponseEntity<?> subscribeList(@PathVariable int pageUserId, @AuthenticationPrincipal PrincipalDetails principalDetails){
		List<SubscribeDto> subscribeDto = subscribeService.구독리스트(principalDetails.getUser().getId(), pageUserId);
		return new ResponseEntity<>(new CMRespDto<>(1, "구독자 정보 리스트 가져오기 성공", subscribeDto), HttpStatus.OK);
	}

	@PutMapping("/api/user/{id}")
	public CMRespDto<?> update(
		@PathVariable int id,
		@Valid UserUpdateDto userUpdateDto,
		BindingResult bindingResult, // 꼭 @Valid가 적혀있는 파라메터 다음 적어야함
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		if(bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
			for(FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			throw new CustomValidationApiException("유효성 검사 실패",errorMap);
		} else {
			User userEntity = userService.회원수정(id, userUpdateDto.toEntity());
			principalDetails.setUser(userEntity); // 세션정보 변경
			return new CMRespDto<>(1, "회원정보 수정 완료", userEntity);
			// 응답시에 userEntity의 모든 getter 함수가 호출되고 JSON으로 파싱하여 응답한다.
		}
	}

	@PutMapping("/api/user/{principalId}/profileImageUrl")
	public ResponseEntity<?> profileImageUrlUpdate(
		@PathVariable int principalId,
		MultipartFile profileImageFile, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		User userEntity = userService.updateProfileImage(principalId, profileImageFile);
		principalDetails.setUser(userEntity); // 세션 변경
		return  new ResponseEntity<>(new CMRespDto<>(1, "프로필사진 변경 성공", null), HttpStatus.OK);
	}
}
