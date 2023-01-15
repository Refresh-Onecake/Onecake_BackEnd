package refresh.onecake.menu.dto

data class MenuDescAndImages(
    var storeName: String,
    var menuName: String,
    var menuDescription: String,
    var allImages: List<MenuIdAndImage>?,
    var birthdayImages: List<MenuIdAndImage>?,
    var monthlyEventImages: List<MenuIdAndImage>?,
    var anniversaryImages: List<MenuIdAndImage>?,
    var employmentImages: List<MenuIdAndImage>?,
    var marriageImages: List<MenuIdAndImage>?,
    var dischargeImages: List<MenuIdAndImage>?
)

data class MenuIdAndImage(
    var menuId: Long,
    var image: String,
)