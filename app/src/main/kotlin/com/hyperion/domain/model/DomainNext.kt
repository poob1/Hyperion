package com.hyperion.domain.model

data class DomainNext(
    val viewCount: String,
    val uploadDate: String,
    val channelAvatarUrl: String,
    val likesText: String,
    val subscribersText: String? = null,
    val comments: Comments,
    val relatedVideos: RelatedVideos,
    val badges: List<String>
) {
    data class Comments(
        val continuation: String? = null,
        val comments: List<DomainComment>
    )

    data class RelatedVideos(
        val continuation: String? = null,
        val videos: List<DomainVideoPartial>
    )
}