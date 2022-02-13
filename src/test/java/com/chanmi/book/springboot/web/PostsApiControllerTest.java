package com.chanmi.book.springboot.web;

import com.chanmi.book.springboot.domain.posts.Posts;
import com.chanmi.book.springboot.domain.posts.PostsRepository;
import com.chanmi.book.springboot.web.dto.PostsSaveRequestDto;
import com.chanmi.book.springboot.web.dto.PostsUpdateRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
//@SpringBootTest에서 MockMvc를 사용하기 위한 추가
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    //@WebMvcTest 사용하지 않고
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    //@SpringBootTest에서 MockMvc를 사용하기 위한 =========
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    //매번 테스트 시작 전에 MockMvc 인스턴스 생성
    @Before
    public void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }



    @After
    public void tearDown() throws Exception{
        postsRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER")//임의 사용자 인증 추가
    public void save_Posts() throws Exception{
        //given
        String title = "title";
        String content = "content";

        //데이터를 넣어서 DTO 하나 생성
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("author")
                .build();

        String url = "http://localhost:" + port + "api/v1/posts";

        //when, 생성한 Dto가지고 url로 post/Long으로 반환받음
        //ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class); //MockMvc 사용 추가 후 주석 처리
        //MockMvc 사용 추가
        //생성된 MockMvc를 통해 API 테스트, 본문(Body) 영역을 ObjectMapper를 통해 문자열 JSON으로 변환
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        //MockMvc 사용 추가 후 주석 처리
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);//바디 > 0L

        //데이터 잘 등록됐는지 확인
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);

    }

    @Test
    @WithMockUser(roles = "USER")//임의 사용자 인증 추가
    public void update_posts() throws Exception{
        //근데 내가 빡대갈이라 그러는데, 여기서 Posts 클래스로 만들었네..?왜..?
        //given
        Posts savedPosts = postsRepository.save(Posts.builder()
        .title("title")
        .content("content")
        .author("author")
        .build());

        Long updateId = savedPosts.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        //when
//        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class); //MockMvc 사용 추가 후 주석 처리
        //MockMvc 사용 추가
        //생성된 MockMvc를 통해 API 테스트, 본문(Body) 영역을 ObjectMapper를 통해 문자열 JSON으로 변환
        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        //MockMvc 사용 추가 후 주석 처리
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }
}
