package za.co.varsitycollage.st10050487.eventat

data class Event(
    var name: String? = null,
    var date: String? = null,
    var startTime: String? = null,
    var endTime: String? = null,
    var category: String? = null,
    var location: String? = null,
    var ticketPrice: String? = null,
    var ticketPriceAtVenue: String? = null,
    var isPaidEvent: Boolean = false,
    var imageUrl: String? = null,
    var videoUrl: String? = null
)
