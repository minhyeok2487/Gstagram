/**
	2. 스토리 페이지
	(1) 스토리 로드하기
	(2) 스토리 스크롤 페이징하기
	(3) 좋아요, 안좋아요
	(4) 댓글쓰기
	(5) 댓글삭제
 */

let page = 0;

// (1) 스토리 로드하기
function storyLoad() {
	$.ajax({
		url: `/api/image?page=${page}`,
		dataType: 'json',
	}).done(res=>{
		console.log(res);
		res.data.content.forEach((image)=>{
        	let storyItem = getStoryItem(image);
        	$("#storyList").append(storyItem);
        });
	}).fail(error=>{
		console.log("오류",error);
	});
}

storyLoad();
function getStoryItem(image) {
	let item = `<div class="story-list__item">
                    <div class="sl__item__header">
                        <div>
                            <img class="profile-image" src="/upload/${image.user.profileImageUrl}"
                                 onerror="this.src='/images/person.jpeg'" />
                        </div>
                        <div>${image.user.username}</div>
                    </div>

                    <div class="sl__item__img">
                        <img src="/upload/${image.postImageUrl}" />
                    </div>

                    <div class="sl__item__contents">
                        <div class="sl__item__contents__icon">`;
    if(image.likeState) {
        item += `<button>
                    <i class="fas fa-heart active" id="storyLikeIcon-${image.id}" onclick="toggleLike(${image.id})"></i>
                </button>`;
    } else {
        item += `<button>
                    <i class="fa-heart far" id="storyLikeIcon-${image.id}" onclick="toggleLike(${image.id})"></i>
                </button>`;
    }
	item += `</div>
				<span class="like"><b id="storyLikeCount-${image.id}">${image.likeCount} </b>likes</span>
					<div class="sl__item__contents__tags">
						<p>#등산 #취미</p>
                    </div>

                <div class="sl__item__contents__content">
                    <p>${image.caption}</p>
                </div>

				<div id="storyCommentList-${image.id}">`;
	image.comments.forEach((comment) => {
		item += `<div class="sl__item__contents__comment" id="storyCommentItem-${comment.id}">
                     <p>
                         <b>${comment.user.username} :</b> ${comment.content}
                     </p>
                     <button>
                         <i class="fas fa-times"></i>
                     </button>
                 </div>`;
	});

    item += `</div>
	            <div class="sl__item__input">
	                <input type="text" placeholder="댓글 달기..." id="storyCommentInput-${image.id}" />
	                <button type="button" onClick="addComment(${image.id})">게시</button>
	            </div>
	        </div>`;
    return item;
}

// (2) 스토리 스크롤 페이징하기
$(window).scroll(() => {
	let checkNum = $(window).scrollTop() - ($(document).height() - $(window).height());
	if(checkNum < 1 && checkNum > -1) {
		page++;
		storyLoad();
	}
});


// (3) 좋아요, 안좋아요
function toggleLike(imageId) {
	let likeIcon = $(`#storyLikeIcon-${imageId}`);

	if (likeIcon.hasClass("far")) { // 좋아요
		$.ajax({
			type : "post",
			url: `/api/image/${imageId}/likes`,
			dataType : "json"
		}).done(res=>{
			let likeCountStr = $(`#storyLikeCount-${imageId}`).text();
			let likeCount = Number(likeCountStr) + 1;
			$(`#storyLikeCount-${imageId}`).text(likeCount);
			likeIcon.addClass("fas");
            likeIcon.addClass("active");
            likeIcon.removeClass("far");
		}).fail(error=>{
			console.log("오류",error)
		});

	} else { // 좋아요 취소
		$.ajax({
            type : "delete",
            url: `/api/image/${imageId}/likes`,
            dataType : "json"
        }).done(res=>{
            let likeCountStr = $(`#storyLikeCount-${imageId}`).text();
            let likeCount = Number(likeCountStr) -1;
            $(`#storyLikeCount-${imageId}`).text(likeCount);
            likeIcon.removeClass("fas");
            likeIcon.removeClass("active");
            likeIcon.addClass("far");
        }).fail(error=>{
            console.log("오류",error)
        });

	}
}

// (4) 댓글쓰기
function addComment(imageId) {

	let commentInput = $(`#storyCommentInput-${imageId}`);
	let commentList = $(`#storyCommentList-${imageId}`);

	let data = {
		imageId : imageId,
		content : commentInput.val()
	}

	console.log(data);
	if (data.content === "") {
		alert("댓글을 작성해주세요!");
		return;
	}

	$.ajax({
		type : "POST",
		url : "/api/comment",
		data : JSON.stringify(data),
		contentType : "application/json; charset=utf-8",
		dataType : "json"
	}).done(res=>{
		let content = `
              <div class="sl__item__contents__comment" id="storyCommentItem-${res.data.id}">
                <p>
                  <b>${res.data.user.username} :</b>
                  ${res.data.content}
                </p>
                <button><i class="fas fa-times"></i></button>
              </div>
	    `;
	    commentList.prepend(content);
	}).fail(error=>{
		console.log(error);
	})

	commentInput.val(""); // 인풋 필드를 깨끗하게 비워준다.
}

// (5) 댓글 삭제
function deleteComment() {

}







