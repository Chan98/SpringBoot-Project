package com.chanmi.book.springboot.service.posts;

import com.chanmi.book.springboot.domain.posts.Posts;
import com.chanmi.book.springboot.domain.posts.PostsRepository;
import com.chanmi.book.springboot.web.dto.PostsListResponseDto;
import com.chanmi.book.springboot.web.dto.PostsResponseDto;
import com.chanmi.book.springboot.web.dto.PostsSaveRequestDto;
import com.chanmi.book.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto){
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto){
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id){
        Posts entity = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }

    //트랜잭션 범위는 유지하되, 조회 기능만 남겨두어 조회 속도가 개선 => 등록/수정/삭제 기능이 전혀 없는 서비스 메소드에서 사용
    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc(){
        return postsRepository.findAllDesc().stream()
                //람다식 >> map(posts -> new PostsListResponseDto(posts))
                //postsRepository 결과로 넘어온 Posts의 Stream을 map을 통해 PostsListResponseDto 변환 -> List로 반환
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id){
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id= " + id));

        postsRepository.delete(posts);
    }
}
