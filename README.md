Better Bookshelves
==================
##### This is a mod that modifies Minecraft's Chiseled Bookshelves to be more useful.

## BETA
This mod is currently in beta. If you find any bugs, please report them on the [issue tracker](https://github.com/Benjamin-Norton/BetterBookshelves/issues)

### Features
* Book Type Rendering:
  * Books will render on the bookshelf differently depending on the type of book.
* Enchanting Table Integration:
  * The enchanting table will now treat Chiseled Bookshelves that contain 3 or more books as valid bookshelves for the purpose of enchanting. (The bookshelves must be facing the enchanting table)
* Book Labels:
  * Hovering over a book in the Chiseled Bookshelf will display the book's title and relevant NBT data

### Configuration
* Enable / Disable Book Labels or have the label render under the crosshair instead of on the bookshelf
  * text_preview: on/off/under_crosshair (default: on)
* Change the type of book textures used
  * per_book_texture: (true/false) (default: true)
* Change Book Label size
  * text_size: whole number between 5 and 20 (default: 10)
* Number of books required for enchanting table to recognize bookshelf
  * enchanting_table_book_requirement: whole number between 0 and 6 (default: 3, -1 to disable) 
* Change comparator output to be based on the type of book that was added to the bookshelf
  * book_type_comparator_output: (true/false) (default: false)
* Change the colour and model of each book type
  * book_textures: (array of all book types) 
    * book_type: book identifier
    * book_model: whole number between 0 and 5
    * hex_colour: hex colour code without the # (e.g. 00FF00 for green)
* Change the colour and model of each enchantment type
  * enchanted_book_textures: (array of all enchantments)
    * enchantment: enchantment identifier
    * book_model: whole number between 0 and 5 (-1 to inherit from enchantment book)
    * hex_colour: hex colour code without the # (e.g. 00FF00 for green) ("inherit" to inherit from enchantment book)

### Installation
#### Requires Fabric
1. Download the latest version of the mod from the [releases page](https://modrinth.com/mod/betterbookshelves/versions)
2. Download the latest version of [Fabric](https://fabricmc.net/use/)
3. Download the latest version of [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api)
4. Place the downloaded mod files in your mods folder
5. Launch Minecraft

### Reporting Bugs
If you find any bugs, please report them on the [issue tracker](https://github.com/Benjamin-Norton/BetterBookshelves/issues).