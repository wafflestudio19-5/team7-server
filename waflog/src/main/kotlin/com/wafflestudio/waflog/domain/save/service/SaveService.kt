package com.wafflestudio.waflog.domain.save.service

import com.wafflestudio.waflog.domain.image.repository.ImageRepository
import com.wafflestudio.waflog.domain.post.service.PostService
import com.wafflestudio.waflog.domain.save.dto.SaveDto
import com.wafflestudio.waflog.domain.save.exception.InvalidSaveContentException
import com.wafflestudio.waflog.domain.save.exception.InvalidSaveTitleException
import com.wafflestudio.waflog.domain.save.model.Save
import com.wafflestudio.waflog.domain.save.repository.SaveRepository
import com.wafflestudio.waflog.domain.tag.model.SaveTag
import com.wafflestudio.waflog.domain.tag.model.Tag
import com.wafflestudio.waflog.domain.tag.repository.SaveTagRepository
import com.wafflestudio.waflog.domain.tag.repository.TagRepository
import com.wafflestudio.waflog.domain.user.model.User
import org.springframework.stereotype.Service

@Service
class SaveService(
    private val postService: PostService,
    private val tagRepository: TagRepository,
    private val saveRepository: SaveRepository,
    private val imageRepository: ImageRepository,
    private val saveTagRepository: SaveTagRepository
) {
    fun savePost(createRequest: SaveDto.CreateRequest, user: User) {
        val title = createRequest.title
        if (title.isBlank()) throw InvalidSaveTitleException("제목 또는 내용이 비어있습니다.")
        val content = createRequest.content
        if (content.isBlank()) throw InvalidSaveContentException("제목 또는 내용이 비어있습니다.")
        val images = postService.formatImageList(createRequest.images, user)
        val tags = createRequest.tags.map { postService.formatTagNameUrl(it) }.map { (name, url) ->
            tagRepository.findByUrl(url)
                ?: tagRepository.save(Tag(name, url))
        }

        val save = Save(
            user = user,
            title = title,
            content = content,
            images = mutableListOf()
        )
        saveRepository.save(save)
            .also {
                images.forEach { image ->
                    image.save = it
                    imageRepository.save(image)
                }
                tags.forEach { tag ->
                    saveTagRepository.save(SaveTag(it, tag))
                }
            }
    }
}
