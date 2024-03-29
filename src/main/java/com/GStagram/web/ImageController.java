package com.GStagram.web;

import com.GStagram.config.auth.PrincipalDetails;
import com.GStagram.domain.Image.Image;
import com.GStagram.domain.user.User;
import com.GStagram.handler.ex.CustomValidationApiException;
import com.GStagram.handler.ex.CustomValidationException;
import com.GStagram.service.ImageService;
import com.GStagram.web.dto.image.ImageUploadDto;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ImageController {

	private final ImageService imageService;

	@GetMapping({"/","/image/story"})
	public String story() {
		return "image/story";
	}

	@GetMapping("/image/popular")
	public String popular(Model model) {
		List<Image> images = imageService.popular();
		model.addAttribute("images", images);
		return "image/popular";
	}

	@GetMapping("/image/upload")
	public String upload() {
		return "image/upload";
	}

	@PostMapping("/image")
	public String imageUpload(ImageUploadDto imageUploadDto, @AuthenticationPrincipal
		PrincipalDetails principalDetails) {
		if(imageUploadDto.getFile().isEmpty()) {
			throw new CustomValidationException("이미지가 첨부되지 않았습니다.",null);
		}
		// 서비스 호출
		imageService.uploadImage(imageUploadDto, principalDetails);

		return "redirect:/user/"+principalDetails.getUser().getId();
	}
}
