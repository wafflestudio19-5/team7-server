package com.wafflestudio.waflog.domain.save.service

import com.wafflestudio.waflog.domain.image.repository.ImageRepository
import com.wafflestudio.waflog.domain.post.service.PostService
import com.wafflestudio.waflog.domain.save.dto.SaveDto
import com.wafflestudio.waflog.domain.save.exception.InvalidSaveContentException
import com.wafflestudio.waflog.domain.save.exception.InvalidSaveTitleException
import com.wafflestudio.waflog.domain.save.exception.SaveNotFoundException
import com.wafflestudio.waflog.domain.save.model.Save
import com.wafflestudio.waflog.domain.save.model.SaveToken
import com.wafflestudio.waflog.domain.save.repository.SaveRepository
import com.wafflestudio.waflog.domain.save.repository.SaveTokenRepository
import com.wafflestudio.waflog.domain.user.model.User
import org.springframework.stereotype.Service
import java.util.*

@Service
class SaveService(
    private val postService: PostService,
    private val saveRepository: SaveRepository,
    private val imageRepository: ImageRepository,
    private val saveTokenRepository: SaveTokenRepository
) {
    fun writeSave(createRequest: SaveDto.CreateRequest, user: User) {
        val title = createRequest.title
        if (title.isBlank()) throw InvalidSaveTitleException("제목 또는 내용이 비어있습니다.")
        val content = createRequest.content
        if (content.isBlank()) throw InvalidSaveContentException("제목 또는 내용이 비어있습니다.")
        val images = postService.formatImageList(createRequest.images, user)

        val save = Save(
            user = user,
            title = title,
            content = content,
            images = mutableListOf(),
            saveTags = createRequest.tags
        )
        saveRepository.save(save)
            .also {
                val token = UUID.randomUUID().toString()
                saveTokenRepository.save(SaveToken(it, token))
                images.forEach { image ->
                    image.save = it
                    imageRepository.save(image)
                }
            }
    }

    fun getSave(token: String, user: User): Save {
        return saveTokenRepository.findByTokenAndSave_User(token, user)?.save
            ?: throw SaveNotFoundException("There is no save with token <$token>")
    }
}
