package com.fgfbrands.myapplication.domain.mapper


import com.fgfbrands.myapplication.data.local.post.PostEntity
import com.fgfbrands.myapplication.data.model.PostDto
import com.fgfbrands.myapplication.data.model.UserDto
import com.fgfbrands.myapplication.domain.model.Post

/**
 * Maps between Post-related DTOs, entities, and domain models.
 *
 * Key Highlights:
 * - Converts API responses to domain objects.
 * - Ensures consistency and reusability in data transformations.
 */
object PostMapper {
    fun mapDtoToEntity(postDto: PostDto, userDto: UserDto): PostEntity {
        return PostEntity(
            id = postDto.id,
            title = postDto.title,
            description = postDto.description,
            imageUrl = postDto.imageUrl,
            likes = emptyList(),
            userId = userDto.id,
            userName = "${userDto.firstName} ${userDto.lastName}",
            userImage = userDto.imageUrl
        )
    }

    fun mapEntityToDomain(entity: PostEntity): Post {
        return Post(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            imageUrl = entity.imageUrl,
            likes = entity.likes,
            userId = entity.userId,
            userName = entity.userName,
            userImage = entity.userImage
        )
    }
}
