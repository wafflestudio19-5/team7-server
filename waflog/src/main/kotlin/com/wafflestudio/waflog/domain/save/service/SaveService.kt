package com.wafflestudio.waflog.domain.save.service

import com.wafflestudio.waflog.domain.image.dto.ImageDto
import com.wafflestudio.waflog.domain.image.model.Image
import com.wafflestudio.waflog.domain.image.repository.ImageRepository
import com.wafflestudio.waflog.domain.image.service.ImageService
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
    private val saveTokenRepository: SaveTokenRepository,
    private val imageService: ImageService
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

    fun putSave(putRequest: SaveDto.PutRequest, user: User) {
        val token = putRequest.token
        val title = putRequest.title
        if (title.isBlank()) throw InvalidSaveTitleException("제목 또는 내용이 비어있습니다.")
        val content = putRequest.content
        if (content.isBlank()) throw InvalidSaveContentException("제목 또는 내용이 비어있습니다.")

        saveTokenRepository.findByTokenAndSave_User(token, user)?.save
            ?.apply {
                this.title = title
                this.content = putRequest.content
                this.saveTags = putRequest.tags
            }
            ?.also {
                modifyImageList(putRequest.images, it, user)
                    .map { image ->
                        image.save = it
                        imageRepository.save(image)
                    }
            }
    }

    fun deleteSave(token: String, user: User) {
        saveTokenRepository.findByTokenAndSave_User(token, user)
            ?.let {
                deleteSaveImage(it.save, user)
                saveTokenRepository.deleteById(it.id)
                saveRepository.deleteById(it.save.id)
            }
    }

    private fun modifyImageList(images: List<ImageDto.S3Token>, save: Save, user: User): List<Image> {
        save.images.map {
            if (!images.contains(ImageDto.S3Token(it.token)))
                imageService.removeImage(ImageDto.RemoveRequest(it.token), user)
        }
        return postService.formatImageList(images, user)
    }

    private fun deleteSaveImage(save: Save, user: User) {
        imageRepository.findAllByUser_UserIdAndSave_Id(user.userId, save.id)
            .map { imageService.removeImage(ImageDto.RemoveRequest(it.token), user) }
    }
}
