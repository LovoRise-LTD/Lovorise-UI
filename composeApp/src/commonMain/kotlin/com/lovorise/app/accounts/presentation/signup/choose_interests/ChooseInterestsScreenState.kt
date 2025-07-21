package com.lovorise.app.accounts.presentation.signup.choose_interests

data class ChooseInterestsScreenState(
    val selectedItems: List<String> = emptyList(),
    val searchQuery: String = "",
    val searchResults: List<InterestCategoryWithItems> = emptyList(),
    val allItems: List<InterestCategoryWithItems> = listOf(
        InterestCategoryWithItems(
            category = "Popular Interest",
            items = listOf(
                InterestCategoryWithItems.Item(name = "Travel"),
                InterestCategoryWithItems.Item(name = "Music"),
                InterestCategoryWithItems.Item(name = "Books"),
                InterestCategoryWithItems.Item(name = "Food"),
                InterestCategoryWithItems.Item(name = "Photography"),
                InterestCategoryWithItems.Item(name = "Sports"),
                InterestCategoryWithItems.Item(name = "Art"),
                InterestCategoryWithItems.Item(name = "Technology"),
                InterestCategoryWithItems.Item(name = "Gaming"),
                InterestCategoryWithItems.Item(name = "Fitness and Exercise"),
                InterestCategoryWithItems.Item(name = "Fashion"),
                InterestCategoryWithItems.Item(name = "Romance"),
                InterestCategoryWithItems.Item(name = "Gardening"),
                InterestCategoryWithItems.Item(name = "Culture"),
                InterestCategoryWithItems.Item(name = "Meditation"),
                InterestCategoryWithItems.Item(name = "Movies"),
                InterestCategoryWithItems.Item(name = "Pets"),
                InterestCategoryWithItems.Item(name = "Learning"),
                InterestCategoryWithItems.Item(name = "Psychology"),
                InterestCategoryWithItems.Item(name = "Comedy")
            ),
            id = "popularInterests"
        ),
        InterestCategoryWithItems(
            category = "Self Care",
            items = listOf(
                InterestCategoryWithItems.Item(name = "Meditation"),
                InterestCategoryWithItems.Item(name = "Healthy Eating Habits"),
                InterestCategoryWithItems.Item(name = "Exercise"),
                InterestCategoryWithItems.Item(name = "Journaling"),
                InterestCategoryWithItems.Item(name = "Digital Detox"),
                InterestCategoryWithItems.Item(name = "Creative Expression"),
                InterestCategoryWithItems.Item(name = "Time in Nature"),
                InterestCategoryWithItems.Item(name = "Setting Boundaries"),
                InterestCategoryWithItems.Item(name = "Self-Compassion"),
                InterestCategoryWithItems.Item(name = "Relaxation Techniques"),
                InterestCategoryWithItems.Item(name = "Social Connection"),
                InterestCategoryWithItems.Item(name = "Setting Goals"),
                InterestCategoryWithItems.Item(name = "Learning Something New"),
                InterestCategoryWithItems.Item(name = "Counselling"),
                InterestCategoryWithItems.Item(name = "Sleep"),
                InterestCategoryWithItems.Item(name = "Decluttering Spaces"),
                InterestCategoryWithItems.Item(name = "Positive Affirmations"),
                InterestCategoryWithItems.Item(name = "Getaways"),
                InterestCategoryWithItems.Item(name = "Acts of Kindness")
            ),
            id = "selfcare"
        ),
        InterestCategoryWithItems(
            category = "Sports",
            items = listOf(
                InterestCategoryWithItems.Item(name = "Football (Soccer)"),
                InterestCategoryWithItems.Item(name = "Basketball"),
                InterestCategoryWithItems.Item(name = "Tennis"),
                InterestCategoryWithItems.Item(name = "Cricket"),
                InterestCategoryWithItems.Item(name = "Rugby"),
                InterestCategoryWithItems.Item(name = "Golf"),
                InterestCategoryWithItems.Item(name = "Baseball"),
                InterestCategoryWithItems.Item(name = "Volleyball"),
                InterestCategoryWithItems.Item(name = "Swimming"),
                InterestCategoryWithItems.Item(name = "Athletics"),
                InterestCategoryWithItems.Item(name = "Badminton"),
                InterestCategoryWithItems.Item(name = "Table Tennis"),
                InterestCategoryWithItems.Item(name = "Ice Hockey"),
                InterestCategoryWithItems.Item(name = "Field Hockey"),
                InterestCategoryWithItems.Item(name = "Boxing"),
                InterestCategoryWithItems.Item(name = "Wrestling"),
                InterestCategoryWithItems.Item(name = "Martial Arts"),
                InterestCategoryWithItems.Item(name = "Cycling"),
                InterestCategoryWithItems.Item(name = "Skiing")
            ),
            id = "sports"
        ),
        InterestCategoryWithItems(
            category = "Film & TV",
            items = listOf(
                InterestCategoryWithItems.Item(name = "Action"),
                InterestCategoryWithItems.Item(name = "Adventure"),
                InterestCategoryWithItems.Item(name = "Comedy"),
                InterestCategoryWithItems.Item(name = "Drama"),
                InterestCategoryWithItems.Item(name = "Romance"),
                InterestCategoryWithItems.Item(name = "Science Fiction"),
                InterestCategoryWithItems.Item(name = "Fantasy"),
                InterestCategoryWithItems.Item(name = "Horror"),
                InterestCategoryWithItems.Item(name = "Thriller"),
                InterestCategoryWithItems.Item(name = "Mystery"),
                InterestCategoryWithItems.Item(name = "Documentary"),
                InterestCategoryWithItems.Item(name = "Anime"),
                InterestCategoryWithItems.Item(name = "Historical"),
                InterestCategoryWithItems.Item(name = "Crime"),
                InterestCategoryWithItems.Item(name = "Family"),
                InterestCategoryWithItems.Item(name = "Reality TV"),
                InterestCategoryWithItems.Item(name = "Talk Shows"),
                InterestCategoryWithItems.Item(name = "Bollywood"),
                InterestCategoryWithItems.Item(name = "Hollywood"),
                InterestCategoryWithItems.Item(name = "Biographical")
            ),
            id = "filmTv"
        ),
        InterestCategoryWithItems(
            category = "Going out",
            items = listOf(
                InterestCategoryWithItems.Item(name = "Night life"),
                InterestCategoryWithItems.Item(name = "Socializing"),
                InterestCategoryWithItems.Item(name = "Outing"),
                InterestCategoryWithItems.Item(name = "Events"),
                InterestCategoryWithItems.Item(name = "Dining Out"),
                InterestCategoryWithItems.Item(name = "Concerts"),
                InterestCategoryWithItems.Item(name = "Festivals"),
                InterestCategoryWithItems.Item(name = "Parties"),
                InterestCategoryWithItems.Item(name = "Night Out"),
                InterestCategoryWithItems.Item(name = "Gathering"),
                InterestCategoryWithItems.Item(name = "Live Music"),
                InterestCategoryWithItems.Item(name = "Theater"),
                InterestCategoryWithItems.Item(name = "Comedy Shows"),
                InterestCategoryWithItems.Item(name = "Clubbing"),
                InterestCategoryWithItems.Item(name = "Cultural Events"),
                InterestCategoryWithItems.Item(name = "Art Exhibitions"),
                InterestCategoryWithItems.Item(name = "Outdoor Activities"),
                InterestCategoryWithItems.Item(name = "Adventure Travel")
            ),
            id = "goingOut"
        ),
        InterestCategoryWithItems(
            category = "Staying In",
            items = listOf(
                InterestCategoryWithItems.Item(name = "Home Comforts"),
                InterestCategoryWithItems.Item(name = "Cozy Nights"),
                InterestCategoryWithItems.Item(name = "Indoor Activities"),
                InterestCategoryWithItems.Item(name = "Movie Night"),
                InterestCategoryWithItems.Item(name = "DIY Projects"),
                InterestCategoryWithItems.Item(name = "Reading Time"),
                InterestCategoryWithItems.Item(name = "Board Games"),
                InterestCategoryWithItems.Item(name = "Relaxing"),
                InterestCategoryWithItems.Item(name = "Self Care"),
                InterestCategoryWithItems.Item(name = "Netflix & Chill"),
                InterestCategoryWithItems.Item(name = "Staycation"),
                InterestCategoryWithItems.Item(name = "Virtual Hangout"),
                InterestCategoryWithItems.Item(name = "Family Time"),
                InterestCategoryWithItems.Item(name = "Indoor Gardening"),
                InterestCategoryWithItems.Item(name = "Spa Day"),
                InterestCategoryWithItems.Item(name = "Entertainment")
            ),
            id = "stayingIn"
        ),
        InterestCategoryWithItems(
            id = "valuesTraits",
            category = "Values & Traits",
            items = listOf(
                InterestCategoryWithItems.Item(name = "Integrity"),
                InterestCategoryWithItems.Item(name = "Respect"),
                InterestCategoryWithItems.Item(name = "Empathy"),
                InterestCategoryWithItems.Item(name = "Compassion"),
                InterestCategoryWithItems.Item(name = "Honesty"),
                InterestCategoryWithItems.Item(name = "Responsibility"),
                InterestCategoryWithItems.Item(name = "Kindness"),
                InterestCategoryWithItems.Item(name = "Generosity"),
                InterestCategoryWithItems.Item(name = "Perseverance"),
                InterestCategoryWithItems.Item(name = "Resilience"),
                InterestCategoryWithItems.Item(name = "Patience"),
                InterestCategoryWithItems.Item(name = "Courage"),
                InterestCategoryWithItems.Item(name = "Authenticity"),
                InterestCategoryWithItems.Item(name = "Gratitude"),
                InterestCategoryWithItems.Item(name = "Humility"),
                InterestCategoryWithItems.Item(name = "Tolerance"),
                InterestCategoryWithItems.Item(name = "Open-mindedness"),
                InterestCategoryWithItems.Item(name = "Self-discipline"),
                InterestCategoryWithItems.Item(name = "Adaptability"),
                InterestCategoryWithItems.Item(name = "Optimism")
            )
        ),
        InterestCategoryWithItems(
            id="foods",
            category = "Food",
            items = listOf(
                InterestCategoryWithItems.Item(name = "Baking"),
                InterestCategoryWithItems.Item(name = "Cooking"),
                InterestCategoryWithItems.Item(name = "Vegetarian"),
                InterestCategoryWithItems.Item(name = "Vegan"),
                InterestCategoryWithItems.Item(name = "Foodie")
            )
        )
    ),
    val showMoreIds:List<String> = emptyList()
) {
    data class InterestCategoryWithItems(
        val id:String,
        val category: String,
        val items: List<Item>,
       // val showLess: Boolean = true
    ) {
        data class Item(
            val name: String,
          //  val isSelected: Boolean = false
        )
    }
}

