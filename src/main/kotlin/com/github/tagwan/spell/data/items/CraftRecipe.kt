package com.github.tagwan.spell.data.items

data class CraftRecipe(
    val id: Int,
    val category: CraftCategories
) {

    enum class CraftCategories {
        CRAFT_CATEGORY_NONE,
        CRAFT_CATEGORY_ALCHEMY,
        CRAFT_CATEGORY_SMITHING,
        CRAFT_CATEGORY_TAILORING,
        CRAFT_CATEGORY_ENCHANTING,
        CRAFT_CATEGORY_ENGINEERING
    }

    enum class CraftSubCategories {
        CRAFT_SUB_CATEGORY_NONE,
        CRAFT_SUB_CATEGORY_POTIONS
    }
}
